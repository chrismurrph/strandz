/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.core.interf;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.PostingTrigger;
import org.strandz.core.prod.NodeTableModelImpl;
import org.strandz.core.prod.Session;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Publisher;
import org.strandz.lgpl.util.RowsAsColumns;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The purpose of this class to to help the <code>CopyPasteBuffer</code> with
 * copying from and then into other tables
 *
 * @author Chris Murphy
 */
public class TableDataCopyPasteHelper
{
    //private List columnDatas = new ArrayList();
    //private List attributes = new ArrayList();
    private List<Column> columns = new ArrayList<Column>();
    /**
     * TODO Set to true when using ComponentTableView
     */
    private static final boolean COPY_TABLE_DATA = false;

    public void clear()
    {
        //columnDatas.clear();
        //attributes.clear();
        columns.clear();
    }
    
    private static class Column implements Comparable
    {
        TableAttributeI tableAttribute;
        List columnOfData;
        
        Column( TableAttributeI tableAttribute, List columnOfData)
        {
            Assert.notNull( tableAttribute);
            Assert.notNull( columnOfData);
            this.tableAttribute = tableAttribute;
            this.columnOfData = columnOfData;
        }
        
        private static boolean bothSameVisualType( Class thisAttribType, Class otherAttribType)
        {
            boolean result = true;
            if(Utils.isClassOfType( thisAttribType, NonVisualTableAttribute.class) && 
                    Utils.isClassOfType( otherAttribType, NonVisualTableAttribute.class))
            {
            }
            else if(Utils.isClassOfType( thisAttribType, TableAttribute.class) && 
                    Utils.isClassOfType( otherAttribType, TableAttribute.class))
            {
            }
            else
            {
                result = false;
            }
            return result;
        }
        
        public int compareTo(Object o)
        {
            int result = 0;
            Column other = (Column) o;
            TableAttributeI otherAttr = other.tableAttribute;
            Class thisAttribType = tableAttribute.getClass();
            Class otherAttribType = otherAttr.getClass();
            if(bothSameVisualType( thisAttribType, otherAttribType))
            {
                result = ((RuntimeAttribute)tableAttribute).compareTo( otherAttr);
            }
            else
            {
                if(Utils.isClassOfType( thisAttribType, NonVisualTableAttribute.class))
                {
                    //Non-visuals are to be ordered towards the end
                    result = 1;    
                }
                else
                {
                    //Visuals always to come before non-visuals
                    result = -1;
                }
            }
            //return tableAttribute.compareTo( otherAttr);
            return result;
        }
    }

    public void addColumnData(List list, TableAttributeI attribute)
    {
        //columnDatas.add(list);
        //attributes.add(attribute);
        columns.add( new Column( attribute, list));
        Err.pr( SdzNote.TABLE_MODEL_CONFUSED, "Adding attribute: " + attribute);
    }

    public void setControlValues()
    {
        List nodes = new ArrayList();
        List matricies = new ArrayList();
        RowsAsColumns matrix = null;
        Node lastNode = null;
        //Ensure that the non-applichousing attributes come at the end:
        Collections.sort( columns);
        for(Iterator columnsIter = columns.iterator(); columnsIter.hasNext();)
        {
            Column column = (Column) columnsIter.next();
            Cell cell = column.tableAttribute.getCell();
            Node node = (Node) cell.getNode();
            if(node != lastNode)
            {
                matrix = new RowsAsColumns();
                nodes.add(node);
                matricies.add(matrix);
            }
            matrix.addColumn( column.columnOfData);
            lastNode = node;
        }
        if(nodes.size() != matricies.size())
        {
            Session.error("Models and Matricies must be same size");
        }

        Iterator nodesIter = nodes.iterator();
        for(Iterator matriciesIter = matricies.iterator(); matriciesIter.hasNext();)
        {
            Node node = (Node) nodesIter.next();
            // node.set
            if(!node.isAllowed(OperationEnum.SET_ROW))
            {
                Err.alarm("Cannot paste to " + node.getName() + " when SET_ROW not allowed");
                continue;
            }

            RowsAsColumns tableData = (RowsAsColumns) matriciesIter.next();
            //Err.pr( "MATRIX FOR node " + node);
            //Err.pr( table.toString());
            node.GOTOWithoutValidation();

            /*
            Err.pr( "Sector--------->" + node.getState() + " for " + node);
            Err.pr( "RowCount--------->" + node.getRowCount());
            Err.pr( "Gone to node: " + node);
            */
            final StrandI strand = (StrandI) node.getStrand();
            Publisher preTriggers = strand.removeAllPreControlActionPerformedTriggers();
            Publisher postTriggers = strand.removeAllPostControlActionPerformedTriggers();
            PostingTrigger changeTrigger = node.removePostingTrigger();
            int cursor = node.getRow();
            //Err.pr( "Cursor is at " + cursor + " in node " + node.getName());
            if(cursor == -1)
            {
                cursor++;
            }
            if(tableData.size() == 0)
            {
                Err.alarm("No rows to do!");
            }
            tableData.chkValid();
            boolean okToContinue = true;
            if(COPY_TABLE_DATA)
            {
                for(int row = 0; row < tableData.size(); row++)
                {
                    // Err.pr( "Will I row at " + (cursor+i));
                    enableValidation(false, strand);
                    okToContinue = strand.execute(OperationEnum.INSERT_AFTER_PLACE);
                    enableValidation(true, strand);
                    if(!okToContinue)
                    {
                        /*
                        * Theoretically we would undo all the inserts (with deletes) here.
                        * However this code will never happen now that we are doing a 'no validate'
                        * insert
                        */
                        Err.error("Should not happen, as should be temporarily removing all validation");
                        break;
                    }
                    // if(i < table.size()-1)
                    {// strand.SET_ROW( cursor+i-1);
                    }
    
                    // strand.setValidationRequired( true);
                    List dataRow = tableData.get(row);
                    int col = 0;
                    for(Iterator iter = dataRow.iterator(); iter.hasNext(); col++)
                    {
                        Object element = iter.next();
                        int columnCount = node.getTableModel().getColumnCount();
                        // row, column
                        int colDiff = col - columnCount;
                        if(colDiff < 0)
                        {
                            node.getTableModel().setValueAt(element, (cursor + row), col);
                        }
                        else
                        {
                            //This is why we ensured that the non-applichousing attributes come at the end:
                            NodeTableModelImpl ntmi = (NodeTableModelImpl) node.getNonVisualTableModel(colDiff);
                            ntmi.setNonVisualOrdinal(colDiff);
                            ntmi.setValueAt(element, (cursor + row), 0);
                            ntmi.setNonVisualOrdinal(NodeTableModelImpl.VISUAL_MODE);
                        }
                    }
                    // Err.pr( "Have Ied a row at " + (cursor+i));
                }
            }
            if(okToContinue)
            {
                strand.SET_ROW(cursor);
                node.setPostingTrigger(changeTrigger);
                /*
                for(Iterator iter = preTriggers.iterator(); iter.hasNext();)
                {
                  PreOperationPerformedTrigger preTrigger = (PreOperationPerformedTrigger)iter.next();
                  strand.addPreControlActionPerformedTrigger( preTrigger);
                }
                */
                preTriggers.publish
                    (
                        new Publisher.Distributor()
                        {
                            public void deliverTo(Object subscriber)
                            {
                                PreOperationPerformedTrigger preTrigger = (PreOperationPerformedTrigger) subscriber;
                                strand.addPreControlActionPerformedTrigger(preTrigger);
                            }
                        }
                    );
                /*
                for(Iterator iter = postTriggers.iterator(); iter.hasNext();)
                {
                  PostOperationPerformedTrigger postTrigger = (PostOperationPerformedTrigger)iter.next();
                  strand.addPostControlActionPerformedTrigger( postTrigger);
                }
                */
                postTriggers.publish
                    (
                        new Publisher.Distributor()
                        {
                            public void deliverTo(Object subscriber)
                            {
                                PostOperationPerformedTrigger postTrigger = (PostOperationPerformedTrigger) subscriber;
                                strand.addPostControlActionPerformedTrigger(postTrigger);
                            }
                        }
                    );
                /*
                * POSTing is left to the caller
                */
            }
        }
    }

    /**
     * Will become a better method. Does not yet remove or add back all validation.
     * Theoretically could become a method on a Node - enableAllValidation( true)
     *
     * @param b
     * @param strand
     */
    private void enableValidation(boolean b, StrandI strand)
    {
        Node node = strand.getCurrentNode();
        if(!b)
        {
            if(!node.isBlankRecord())
            {
                node.setBlankRecord(true);
            }
        }
        else
        {
            if(node.isBlankRecord())
            {
                node.setBlankRecord(false);
            }
        }
    }
}
