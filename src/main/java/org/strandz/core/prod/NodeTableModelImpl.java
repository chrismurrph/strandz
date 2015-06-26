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
package org.strandz.core.prod;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.NodeTableModelI;
import org.strandz.core.domain.NodeTableMethods;
import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.TableSignatures;
import org.strandz.core.domain.TableIdEnum;
import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.prod.view.FieldObj;
import org.strandz.core.prod.view.NodeTableModelImplI;
import org.strandz.core.widgets.TableComp;
import org.strandz.lgpl.extent.CombinationExtent;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.Utils;

import javax.swing.JTable;
import java.util.Iterator;
import java.util.List;

/**
 * TableObj does the traditional duties of a SubRecordObj, using this
 * class to view and change the data. This class contains the users
 * model (which is the real DataModel) so that TableObj may refer
 * to an object that is not caught up in the recursive/composite of
 * core.info.prod.view. In this class we can interface with the data
 * thru TempCursorExtent, and look at all the columns as a record
 * (flatColumns).
 * <p/>
 * This is the class that a user interfaces with thru
 * NodeTableModelI, so the user can write to the
 * actual TableModel. The users model is a TableModelImpl.
 * <p/>
 * NodeTableModelImplI is how TableObj in prod.view can
 * see this class.
 */
public class NodeTableModelImpl extends NodeTableModelImplI
    implements NodeTableModelI
{
    int lastRow = -99;
    int lastRowPainted = -99;
    /**
     * When a row is being viewed or edited, this is that
     * row.
     */
    List flatColumns;
    List nvFlatColumns;
    /*
    * many Adapters. A row holder var, used for getValueAt.
    * Also used by other methods where the particular row
    * are currently on is irrelevant. When want the actual
    * current row use extent.getCurrentRow()
    */
    private TempCursorExtent extent;
    private NodeTableMethods usersModel;
    private Block block;
    // private boolean dontUseCache = false;
    private OperationsProcessor oper;
    private List nvTables;
    //private boolean doingNVRecurse;
    int nonVisualOrdinal = VISUAL_MODE;
    public static final boolean DEBUG = false;

    public static int VISUAL_MODE = -1;
    private static int times = 0;
    private static int times1 = 0;
    private static int times2 = 0;
    private static boolean original = false;
    private static int timesConstructed;
    int id;

    public NodeTableModelImpl(Block block, OperationsProcessor oper)
    {
        this.block = block;
        this.oper = oper;
        timesConstructed++;
        id = timesConstructed;
        pr("Created NodeTableModelImpl ID: " + id + " for " + block.getName());
    }

    public void setTableObj(FieldObj tableObj)
    {
        ///fieldObj = tableObj;
        extent = new TempCursorExtent(this, block, tableObj);
        flatColumns = tableObj.getVisualTableAdaptersArrayList();
        nvFlatColumns = tableObj.getNonVisualTableAdaptersArrayList();
        Err.pr( SdzNote.MANY_NON_VISUAL_ATTRIBS, "NVFlatColumns set in <" + block.getName() + 
                "> and is size " + nvFlatColumns.size());
        // Err.pr( "flatColumns: " + flatColumns);
    }

    void nullBlock()
    {
        extent = null;
        block = null;
    }

    public int getNonVisualOrdinal()
    {
        return nonVisualOrdinal;
    }

    public void setNonVisualOrdinal(int nonVisualOrdinal)
    {
        this.nonVisualOrdinal = nonVisualOrdinal;
    }

    private boolean doingNonVisual()
    {
        return nonVisualOrdinal != VISUAL_MODE;
    }

    public List getFlatColumns()
    {
        if(flatColumns == null)
        {
            Err.error("Need to setTableObj to get flatColumns");
        }
        return flatColumns;
    }

    public void setUsersModel(NodeTableMethods usersModel)
    {
        this.usersModel = usersModel;
    }

    public NodeTableMethods getUsersModel()
    {
        return usersModel;
    }

    public int getColumnCount()
    {
        int result = getFlatColumns().size();
        // Err.pr( "# Col count getting " + result);
        return result;
    }

    public int getRowCount()
    {
        return applyInsertModeForRowCount(extent.size());
    }

    public void fillFlat(int row)
    {
        if(row == -1)
        {
            if(block.getTableControl() instanceof JTable)
            {
                //Happening when paint so lets keep paint happy
                extent.fillFlat(0, flatColumns);
            }
            else
            {
                //Err.error("fillFlat() param is -1 in " + block);
                extent.fillFlat(0, flatColumns);
            }
        }
        else
        {
            extent.fillFlat(row, flatColumns);
        }
    }

    private int applyInsertModeForCursor(int index)
    {
        int result = index;
        if(block == oper.getCurrentBlock()
            && (oper.getState(block).isNew() && !oper.getState(block).isPrior())
            )
        {
            result++;
        }
        if(index != result)
        {
            pr( "applying insert mode from old cursor " + index + " to new cursor " + result);
        }
        return result;
    }

    boolean masterIsAdding()
    {
        boolean result = false;
        OperationEnum enumId = oper.getCurrentOperation();
        if(enumId == OperationEnum.INSERT_AFTER_PLACE || enumId == OperationEnum.INSERT_AT_PLACE)
        {
            if(block.isChildOf(oper.getCurrentBlock()))
            {
                result = true;
            }
        }
        return result;
    }

    private int applyInsertModeForAllOtherRows(int index)
    {
        int result = index;
         /**/
        if(block == oper.getCurrentBlock()
            && (oper.getState(block).isNew()/* && !oper.getState( block).isPrior()*/)
            )
        {
            //Not using JTable anymore, and this -1 was not helping SdzDsgnr - so decided
            //to start using ComponentTableView instead
            int cursor = block.getIndex(); // - 1;
            if(index > cursor)
            {
                result--;
            }
        }
         /**/
        return result;
    }

    private int applyInsertModeForRowCount(int index)
    {
        int result = index;
        if(block == oper.getCurrentBlock()
            && (oper.getState(block).isNew()/* && !oper.getState( block).isPrior()*/)
            )
        {
            result++;
        }
        return result;
    }

    public Class getColumnClass(int columnIndex)
    {
        Class result;
        if(!doingNonVisual())
        {
            result = ((AbstractTableItemAdapter) flatColumns.get(columnIndex)).getDoAdapter().getClassType();
        }
        else
        {
            result = ((AbstractTableItemAdapter) nvFlatColumns.get(columnIndex)).getDoAdapter().getClassType();
        }
        //Err.pr( "getColumnClass() is returning <" + result + "> from col " + columnIndex);
        return result;
    }

    private Object getTableControl()
    {
        Object result;
        if(!doingNonVisual())
        {
            result = block.getTableControl();
        }
        else
        {
            result = block.getNvTables().get(getNonVisualOrdinal());
        }
        return result;
    }

    public Object getValueAt(int row, int col)
    {
        Object result = null;
        if(row < getRowCount())
        {
            pr( "{{ row " + row + " is less than getRowCount() " + getRowCount(), true);
            if(!TableSignatures.useNew( getTableControl()) && row == applyInsertModeForCursor(block.getIndex()))
            {
                pr("{{ for row " + row + " col " + col + ", getting from special insert buffer, actual index is " + block.getIndex(), true);
                TableIdEnum id = IdEnum.newTable(getTableControl(), row, col,
                    getColumnClass(col), null);
                result = TableSignatures.getText(id);
            }
            else
            {
                pr("{{ for row " + row + " col " + col + ", getting directly from database table", true);
                fillFlat(applyInsertModeForAllOtherRows(row));

                AbstractTableItemAdapter ta = null;
                if(!doingNonVisual())
                {
                    ta = (AbstractTableItemAdapter) flatColumns.get(col);
                    if(ta.getTableControl().getClass() == TableComp.class)
                    {
                        Err.error("Expected a Visual table, got " + ta.getTableControl().getClass().getName());
                    }
                }
                else
                {
                    ta = (AbstractTableItemAdapter) nvFlatColumns.get(getNonVisualOrdinal());
                    if(ta.getTableControl().getClass() != TableComp.class)
                    {
                        Err.error("Expected a Non-Visual table, got " + ta.getTableControl().getClass().getName());
                    }
                }
                pr("Getting database table value from DoAdapter() id " + ta.getDoAdapter().id, true);
                Object obj = ta.getStoredFlatObject();
                pr("StoredFlatObject: " + obj, true);
                if(obj != null)
                {
                    pr( "\tis of type " + obj.getClass().getName(), true);
                }
                result = ta.getDoAdapter().getFieldValue(obj);
            }
            if(!doingNonVisual() && nvTables != null)
            {
                if(row != lastRowPainted)
                {
                    int i = 0;
                    for(Iterator iterator = nvTables.iterator(); iterator.hasNext(); i++)
                    {
                        TableComp tableComp = (TableComp) iterator.next();
                        setNonVisualOrdinal(i);
                        tableComp.getModel().getValueAt(row, 0);
                        setNonVisualOrdinal(VISUAL_MODE);
                    }
                    lastRowPainted = row;
                }
            }
        }
        else
        {
            Print.pr("Want row " + row);
            Print.pr("Row count is " + getRowCount());
            Err.error("Table asking for a row that's not there: <" + row + "> for block " + block);
        }
        if(result != null)
        {
            pr( "Value at " + col + ", " + row + " is " + result + " of type " + result.getClass().getName() + " from ID:" + id);
        }
        return result;
    }

    private void pr(String txt)
    {
        pr(txt, false);
    }

    private void pr(String txt, boolean force)
    {
        if(DEBUG)
        {
            if(/*block.getName().equals("Job Node") &&*/ (force || doingNonVisual()))
            {
                Err.pr(txt);
            }
        }
    }

    public void setValueAt(Object aValue, int row, int col)
    {
        int current = applyInsertModeForCursor(block.getIndex());
        /**/
        if(row != current)
        {
            Err.error(
                "Should be updating the current row, " + current + " not " + row
                    + " for " + block);
            // block.setIndex( true, row);
        }
        /**/
        TableIdEnum id = IdEnum.newTable(getTableControl(), row, col,
            getColumnClass(col), null);
        TableSignatures.setText(id, aValue);
    }

    /**
     * Used as part of the pasting operation
     */
    /*
    public boolean goBlock()
    {
    boolean result = true;
    try
    {
    block.goBlock();
    }
    catch( GoNodeChangeException ex)
    {
    result = false;
    }
    return result;
    }
    */

    /**
     * Used as part of the pasting operation
     */
    /*
    public void createNewRow()
    {
    extent.flush();
    extent.masterIsAdding = false;
    int index = block.getIndex()+1;
    //Err.pr( "o createNewRow(): block.getIndex(): " + index);
    extent.add( index);
    fillFlat( index);
    //tableObj.setDefaultValues();
    usersModel.fireTableRowsInserted( index,
    index);
    usersModel.fireRowChangedTo( index);
    }
    */

    /**
     * Used as part of the pasting operation
     */
    public void increment()
    {
        block.incCursor(false);
    }

    public void insertLine(String line, Class clazz)
    {
        int index = getRowCount();
        // Err.pr( "===S/be adding line number " + index + " to block: " + block);
        CombinationExtent ext = block.getCombinationExtent();
        // Err.pr( "===In state " + block.getCurrentState());
        String args[] = new String[1];
        args[0] = line;

        Object newObj = ObjectFoundryUtils.factory(clazz, args);
        if(index != 0)
        {
            ext.insert(newObj, index);
        }
        else
        {
            ext.insert(newObj, 0);
        }
    }

    public void removeLine(int index)
    {
        CombinationExtent ext = block.getCombinationExtent();
        ext.removeElementAt(index);
    }

    public AbstractTableItemAdapter getAdapter(int columnIndex)
    {
        AbstractTableItemAdapter result = null;
        if(columnIndex != -1)
        {
            ItemAdapter ad = (ItemAdapter) flatColumns.get(columnIndex);
            if(!(ad instanceof AbstractTableItemAdapter))
            {// Err.error( "Got a " + ad.getClass().getName() + " for " + ad.getDOFieldName());
            }
            else
            {
                result = (AbstractTableItemAdapter) ad;
            }
        }
         /**/
        else
        {
            Print.pr("ERROR, Could not get an Adapter as columnIndex is -1");
        }
         /**/
        return result;
    }
    
    public void blankoutDisplay( OperationEnum currentOperation, String reason)
    {
        blankoutDisplay( currentOperation, Utils.UNSET_INT, reason);
    }

    /**
     * Not part of the table model (so doesn't get called when the table is
     * painting). Has proven to be a good place to set the table up just
     * prior to painting.
     */
    public void blankoutDisplay( OperationEnum currentOperation, int row, String reason)
    {
        int idxCurrentRow = block.getIndex();
        // OperationEnum currentOperation = oper.getCurrentOperation();
        StateEnum currentState = oper.getState(block);
        Block currentBlock = oper.getCurrentBlock();
        if(false /*SdzNote.SET_DISPLAY_ON_TABLE.isVisible()*/)
        {
            times++;
            Err.pr("");
            Err.pr("Inside blankoutDisplay for <" + block + "> times: " + times);
            Err.pr("\tcurrentBlock: " + currentBlock);
            Err.pr("\tblock that has table: " + block);
            Err.pr("\tcurrent operation: " + currentOperation);
            Err.pr("\tcurrent state: " + currentState);
            Err.pr("\tidxCurrentRow: " + idxCurrentRow);
            if(times == 39)
            {
                Err.debug();
            }
        }
        if(currentOperation == OperationEnum.INSERT_AFTER_PLACE
            || currentOperation == OperationEnum.INSERT_AT_PLACE
            )
        {
            if(block == currentBlock)
            {
                int index = -99;
                /* We don't do this for field, so lets not do it for table either
                if(currentOperation == OperationEnum.INSERT || currentOperation == OperationEnum.INSERT_IGNORE)
                {
                    index = block.getIndex() + 1;
                }
                else
                */
                {
                    index = block.getIndex();
                }
                usersModel.acceptEdit();
                usersModel.fireTableRowsInserted(index, index);
                // Was trying to select the row on the table. Doing at the post operation performed
                // trigger (see SdzDsgnr for this) actually works, so as there's a workaround
                // we've left this as a slight note
                Err.pr(SdzNote.AUTO_SELECT_TABLE_ROW_AFTER_INSERT,
                    "Do we actually need access to the JTable if wanted to do this here?");
                //usersModel.fireRowChangedTo(index);
                Err.pr( "Inserting a row s/know to move to it as well...");
                Err.pr(SdzNote.SET_DISPLAY_ON_TABLE, "blankoutDisplay, have changed row to " + index);
            }
            else
            {
                // For example when insert master
                usersModel.fireTableDataChanged( "blankoutDisplay because of an " +
                        "INSERT on different block to current, because " + reason);
            }
        }
        else if(currentOperation == OperationEnum.REMOVE/* && block == currentBlock*/
            )
        {
            /*
            * Do not want to fireTableDataChanged(), as this would lead to the
            * edited data being written ie. setValueAt() being called.
            */
            usersModel.rejectEdit();
            if(/* (currentState.isNavigating() ||
           currentState.isNew()) ||*/
                currentState == StateEnum.FROZEN)
            {
                // When do this then model will no longer have the extra artificial
                // inserted row, and fireTableRowsDeleted will work
                usersModel.fireTableRowsDeleted(idxCurrentRow, idxCurrentRow);
            }
        }
        else if(currentOperation == OperationEnum.EXECUTE_QUERY)
        {
            /*
            * As above
            */
            usersModel.rejectEdit();
            usersModel.fireTableDataChanged( "blankoutDisplay because of EXECUTE_QUERY because " + reason);
        }
        else if((currentOperation == OperationEnum.NEXT
            || currentOperation == OperationEnum.PREVIOUS)
            && currentState.isNavigating())
        {
            usersModel.fireTableDataChanged( "blankoutDisplay because of NEXT/PREVIOUS because " + reason);
        }
        else if(currentOperation == OperationEnum.REFRESH)
        {
            Err.pr(/*SdzNote.SET_DISPLAY_ON_TABLE*/ false, "@@ blankoutDisplay for op " + currentOperation);
            usersModel.fireTableDataChanged( row, row, "blankoutDisplay for op " + currentOperation + 
                    " because " + reason);
        }
        else
        {
            Err.pr(/*SdzNote.SET_DISPLAY_ON_TABLE*/ false, "@@ blankoutDisplay, default as op was " + currentOperation);
            usersModel.fireTableDataChanged( "blankoutDisplay because of Un-programmed for op " + currentOperation + 
                    " because " + reason);
        }
    }

    public void setDisplay( OperationEnum currentOperation, String reason)
    {
        setDisplay( currentOperation, Utils.UNSET_INT, reason);
    }

    /**
     * set param is hardly ever being used, which means things like will
     * be setting the current row visibly current when in fact it already
     * is visibly current. No big deal!
     */
    public void setDisplay( OperationEnum currentOperation, int row, String reason)
    {
        StateEnum currentState = oper.getState(block);
        Block currentBlock = oper.getCurrentBlock();
        //int idxCurrentRow = block.getCursorPosition() - 1;
        int idxCurrentRow = block.getIndex();
        if(currentState.isNew() && currentState.isPrior())
        {
            idxCurrentRow--;
        }
         /**/
        if(false /*SdzNote.SET_DISPLAY_ON_TABLE.isVisible()*/)
        {
            times++;
            Err.pr("");
            Err.pr("Inside setDisplay for <" + block + "> times: " + times);
            Err.pr("\tCurrent block: " + currentBlock);
            Err.pr("\tblock that has table: " + block);
            Err.pr("\tcurrent operation: " + currentOperation);
            Err.pr("\tcurrent state: " + currentState);
            Err.pr("\trow to change to: " + idxCurrentRow);
            if(times == 39)
            {
                Err.debug();
            }
        }
         /**/
        if(currentOperation == OperationEnum.REMOVE
            // && currentState.isNew()
            // && !block.isChildOf( currentBlock)
            )
        {
            usersModel.fireTableRowsDeleted(idxCurrentRow, idxCurrentRow);
            // usersModel.fireRowChangedTo( idxCurrentRow-1);
        }
        /*
        else if(currentOperation == OperationEnum.REMOVE &&
        currentState.isNavigating() &&
        !block.isChildOf( currentBlock))
        {
        // w/out this, in case where deleted first row,
        // would not have become selected again
        usersModel.fireRowChangedTo( idxCurrentRow);
        }
        */
        else if(currentOperation == OperationEnum.EXECUTE_QUERY)
        {
            usersModel.fireTableDataChanged( "setDisplay because of " + currentOperation + ", because " + reason);
            //No evidence yet that need this
            //usersModel.fireRowChangedTo( idxCurrentRow);
        }
        if(currentState.isNew()
            && (currentOperation == OperationEnum.PREVIOUS
            || currentOperation == OperationEnum.NEXT))
        {
            usersModel.fireRowChangedTo(idxCurrentRow);
        }
        /*
        else
        if((currentOperation == OperationEnum.INSERT
        || currentOperation == OperationEnum.INSERT_PRIOR)
        && block == currentBlock)
        {
        usersModel.fireRowChangedTo( idxCurrentRow);
        Err.pr( "blankoutDisplay, have changed row to " + idxCurrentRow);
        }
        */
        else if(currentOperation == OperationEnum.REFRESH)
        {
            Err.pr(/*SdzNote.SET_DISPLAY_ON_TABLE*/ false, "@@ setDisplay for op " + currentOperation);
            usersModel.fireTableDataChanged( row, row, "setDisplay for op " + currentOperation + 
                    " because " + reason);
        }
        else
        {
            Err.pr(SdzNote.SET_DISPLAY_ON_TABLE, "@@ setDisplay, default as op was " + currentOperation);
            usersModel.fireRowChangedTo(idxCurrentRow);
        }
    }

    public boolean getRecordValidationOutcome()
    {
        // Err.pr( "row is " + row);
        // Err.pr( "cf to is " + String.valueOf( block.getCursorPosition()-1));
        // int idxCurrentRow = block.getCursorPosition()-1; //03/07/01
        return block.getMoveBlock().getRecordValidationOutcome();
    }

    /*
    public boolean allowUserEvent()
    {
    int state = block.getState();
    boolean aNewState = false;
    if(state == StateChangeEvent.NEW ||
    state == StateChangeEvent.NOMOVEADD ||
    state == StateChangeEvent.NOMOVEEDIT ||
    state == StateChangeEvent.NOMOVEBROWSE ||
    state == StateChangeEvent.NOMOVEBROWSETEST )
    {
    aNewState = true;
    }
    boolean result = true;
    //Err.pr( "ZOG aNewState " + aNewState);
    //Err.pr( "ZOG tableObj.UIIsBlank() " + tableObj.UIIsBlank());
    if(aNewState && *tableObj.UIIsBlank()*
    block.blankComplaint( null))
    {
    result = false;
    }
    return result;
    }
    */

    /*
    public void moveAdapterReleased()
    {
    block.moveAdapterReleased();
    }
    public void setAdapterReleased( Adapter itemAdapter)
    {
    block.setAdapterReleased( itemAdapter);
    }
    public void itemValidationPoint()
    {
    block.itemValidationPoint();
    }
    */

    /*
    public void setDebug(boolean b)
    {
        this.DEBUG = b;
    }
    */

    public void addCompTables(List nvTables)
    {
        this.nvTables = nvTables;
        pr("Have added " + nvTables.size() + " nvTables to NTMI of " + block.getName());
        /*
        if( block.getName().equals( "Client Node"))
        {
          Err.stack();
        }
        */
    }
}
