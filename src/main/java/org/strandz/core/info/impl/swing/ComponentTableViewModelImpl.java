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
package org.strandz.core.info.impl.swing;

import org.strandz.core.domain.ControlSignatures;
import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.InputControllerEvent;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.IdentifierI;
import org.strandz.lgpl.widgets.CellControlSignatures;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.lgpl.widgets.table.RowChangeListenerI;
import org.strandz.lgpl.widgets.table.RowCompletedListenerI;
import org.strandz.lgpl.widgets.table.TouchListenerI;

import javax.swing.event.TableModelEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

public class ComponentTableViewModelImpl extends AbstTableModelImpl
{
    private ComponentTableView tableView;
    private LocalRowChangeListener localRowChangeListener = new LocalRowChangeListener();
    private LocalRowCompletedListener localRowCompletedListener = new LocalRowCompletedListener();
    private LocalTouchListener localTouchListener = new LocalTouchListener();
    private LocalCellControlSignatures localCellControlSignatures = new LocalCellControlSignatures();
    //private LocalNodeChangeListener localNodeChangeListener; // = new LocalNodeChangeListener();
    private LocalFocusListener localFocusListener = new LocalFocusListener();

    private static int constructedTimes;
    private int id;

    public ComponentTableViewModelImpl()
    {
        constructedTimes++;
        id = constructedTimes;
        if(id == 0)
        {
            Err.stack();
        }
    }

    public int getSelectedRow()
    {
        return tableView.getSelectedRow();
    }

    public int getSelectedColumn()
    {
        return tableView.getSelectedColumn();
    }

    public void fireRowChangedTo(int row)
    {
        if(getRowCount() > 0)
        {
            tableView.setRowSelectionInterval( row, row);
        }
    }
    
    private class LocalFocusListener implements FocusListener
    {
        public void focusGained(FocusEvent e)
        {
        }

        /**
         * Doesn't work well and is bad conceptually.
         * Conceptually - will happen whenever go from one field to another - which will be silly when
         * haven't filled one in and it doesn't have a good default value, and so a pullOffScreen()
         * will baulk.
         * node.fieldsChanged() - does not work.
         *
         * ProdKpi - has its own listener that keeps the calculations for the row up to date
         */
        public void focusLost(FocusEvent e)
        {
            /* For above reasons have scrapped - use AttributeChangesMonitor if need this functionality
            Err.pr( SdzNote.CTV_FOCUS, "Lost focus from " + e.getSource());
            if(node.fieldsChanged())
            {
                node.getCalculationPlace().fireCalculationFromItemAdapter( node.getItemValue());
            }
            */
        }
    }

    private class LocalRowCompletedListener implements RowCompletedListenerI
    {
        private LocalRowCompletedListener()
        {
        }

        public void rowDataFilledNotification( int row)
        {
            node.getCalculationPlace().fireCalculationFromPopulateRow( row);
        }

    }

    private class LocalRowChangeListener implements RowChangeListenerI
    {
        private LocalRowChangeListener()
        {
        }

        public void rowChanged(int fromRow, int toRow)
        {
            //Err.pr( "row changed from " + fromRow + " to " + toRow + " on " + tableView.getName());
            if(controlActionListener.getState() != StateEnum.ENTER_QUERY)
            {
                InputControllerEvent ce = new InputControllerEvent(this,
                                                                   OperationEnum.SET_ROW);
                ce.setRow(toRow);
                controlActionListener.execute(ce);
            }
            else
            {
                Err.pr( SdzNote.RED_WHEN_ENTER_QUERY, "Really the click that caused this should not have been" +
                        " allowed to happen in the first place");
            }
        }
    }
    
    private class LocalTouchListener implements TouchListenerI
    {
        public void touch( int row)
        {
            if(controlActionListener.getState() != StateEnum.ENTER_QUERY)
            {
                if(row != Utils.UNSET_INT)
                {
                    //Err.pr( "touched row " + row + " on " + tableView.getName() + " so will go to node " + node);
                    //Err.pr( "currently node is " + node.getCurrentNode());
                    controlActionListener.goNode( node, row);
                    Err.pr( SdzNote.SET_ROW, "touched on node " + node);
                }
                else
                {
                    //Coming back from having clicked [ok] on a validation dialog was causing this
                }
            }
            else
            {
                Err.pr( SdzNote.RED_WHEN_ENTER_QUERY, "Really the click that caused this should not have been" +
                        " allowed to happen in the first place");
            }
        }
    }
    
    private class LocalCellControlSignatures implements CellControlSignatures
    {
        public Object getText(Object comp)
        {
            Object result;
            IdEnum fieldId = IdEnum.newField( comp);
            result = ControlSignatures.getText( fieldId);
            return result;
        }

        public void setEditable(Object comp, boolean b)
        {
            ComponentUtils.setEditable( comp, b);
        }

        public boolean isEditable(Object comp)
        {
            return ComponentUtils.isEditable( comp);
        }

        public void setText(Object comp, Object txt)
        {
            IdEnum fieldId = IdEnum.newField( comp);
            if(txt != null)
            {
                ControlSignatures.setText( fieldId, txt);
            }
        }

        public boolean hasEditableMethod(Object comp)
        {
            return ComponentUtils.hasEditableMethod( comp);
        }
    }

    /**
     * When this method is called we are saying that a new one of these (CTVMI) has been created
     * and we want the table to forget about the old one. The old one will still be
     * listening to the table thru many of the table's listeners. So we need to get rid
     * of these listeners. As an example the tableView may already have a
     * RowChangeListenerI of type CTVMI.LocalRowChangeListener. If so we will remove it
     * from tableView before making it so that this CTVMI is the listener.
     *
     * @param obj
     */
    public void oneRowSelectionOn(Object obj)
    {
        tableView = (ComponentTableView)obj;
        List<RowChangeListenerI> rowChangeListeners = tableView.getRowChangeListeners();
        for (int i = 0; i < rowChangeListeners.size(); i++)
        {
            RowChangeListenerI rowChangeListener = rowChangeListeners.get(i);
            if(rowChangeListener.getClass().equals( localRowChangeListener.getClass()))
            {
                tableView.removeRowChangeListener( rowChangeListener);
                //Err.pr( "Removed the old RowChangeListener");
            }
        }
        tableView.addRowChangeListener( localRowChangeListener);


        List<RowCompletedListenerI> rowCompletedListeners = tableView.getRowCompletedListeners();
        for (int i = 0; i < rowCompletedListeners.size(); i++)
        {
            RowCompletedListenerI rowCompletedListener = rowCompletedListeners.get(i);
            if(rowCompletedListener.getClass().equals( localRowCompletedListener.getClass()))
            {
                tableView.removeRowCompletedListener( rowCompletedListener);
                //Err.pr( "Removed the old RowCompletedListener");
            }
        }
        tableView.addRowCompletedListener( localRowCompletedListener);

        List<TouchListenerI> touchListeners = tableView.getTouchListeners();
        for (int i = 0; i < touchListeners.size(); i++)
        {
            TouchListenerI touchListener = touchListeners.get(i);
            if(touchListener.getClass().equals( localTouchListener.getClass()))
            {
                tableView.removeTouchListener( touchListener);
                //Err.pr( "Removed the old TouchListener");
            }
        }
        tableView.addTouchListener( localTouchListener);

        tableView.setCellControlSignatures( localCellControlSignatures);
        tableView.setFocusListener( localFocusListener);
    }

    public void acceptEdit()
    {
        Err.pr( SdzNote.CTV_SDZ_INTEGRATION, "ComponentTableViewModelImpl.acceptEdit() currently not implemented");
    }

    public void rejectEdit()
    {
        Err.pr( SdzNote.CTV_SDZ_INTEGRATION, "ComponentTableViewModelImpl.rejectEdit() currently not implemented");
    }

    public String getTableName()
    {
        return tableView.getName();
    }
    
    public void fireTableDataChanged( String reason)
    {
        /*
         * Really rough way of getting some performance improvements
         */
        if(reason.contains( "blankoutDisplay") && !reason.contains( OperationEnum.REFRESH.getName()))
        {
            Err.pr( SdzNote.TOO_MANY_TABLE_NOTIFICATIONS, "TO NOT fire fireTableDataChanged because " + reason);
        }
        else
        {
            Err.pr( SdzNote.TOO_MANY_TABLE_NOTIFICATIONS, "FIRE fireTableDataChanged because " + reason);
            fireTableChanged(new TableModelEvent(this));
        }
    }
}
