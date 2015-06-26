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

import org.strandz.core.domain.AbstNodeTable;
import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.NodeChangeEvent;
import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.domain.NodeTableMethods;
import org.strandz.core.domain.NodeTableModelI;
import org.strandz.core.domain.TableItemAdapter;
import org.strandz.core.domain.CalculationPlace;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.AccessEvent;
import org.strandz.core.domain.event.ControlActionListener;
import org.strandz.core.domain.event.InputControllerEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.Node;
import org.strandz.core.prod.Session;
import org.strandz.core.prod.move.MoveBlock;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.IdentifierI;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * This class should be visible to the user. It shows how we implement a
 * TableModel. Use is made of the implementation of NodeTableMethods to
 * communicate with strandz.
 * <p/>
 * There are two types of methods here. One type receives
 * information from strandz, and the other type sends information
 * to strandz.
 */
abstract public class AbstTableModelImpl extends NodeTableMethods
    implements TableModel
{
    AbstNodeTable node;
    ControlActionListener controlActionListener;
    private NodeTableModelI nodeTableModel;
    protected EventListenerList listenerList = new EventListenerList();
    // JTableImpl outer = this; //itself, for inner class
    private boolean rowEditable = true;
    private LocalNodeChangeListener localNodeChangeListener;
    private Node currentNode;
    public boolean debug = false;
    private static int times = 0;

//    abstract public JTable getTableView();
//    abstract public void setTableView(JTable tableView);
    abstract public int getSelectedRow();
    abstract public int getSelectedColumn();

    public void setDebug(boolean b)
    {
        this.debug = b;
    }

    public String toString()
    {
        String result = super.toString() + ", " + currentNode;
        return result;
    }

    AbstractTableItemAdapter formAdapterReleased(int row, int col, CalculationPlace calculationPlace)
    {
        AbstractTableItemAdapter oldAdapter = getAdapter(col);
        AbstractTableItemAdapter newAdapter = new TableItemAdapter(oldAdapter, row, calculationPlace);
        return newAdapter;
    }

    public boolean onCurrentRow()
    {
        boolean result = true;
        int nodeRow = currentNode.getRow() - 1;
        int tableRow = getSelectedRow();
        if(nodeRow != tableRow)
        {
            result = false;
        }
        return result;
    }

    public boolean onCurrentRow(int row)
    {
        boolean result = true;
        int nodeRow = currentNode.getRow() - 1;
        if(nodeRow != row)
        {
            result = false;
        }
        return result;
    }

    /**
     * If wanted could SET_ROW w/out finding out if needed to.
     */
    void processPossibleRowChangeKey()
    {
        int row = getSelectedRow();
        if(!onCurrentRow())
        {
            InputControllerEvent ce = new InputControllerEvent(this,
                OperationEnum.SET_ROW);
            ce.setRow(row);
            controlActionListener.execute(ce);
        }
    }

    /**
     * Called by Block in its setUpTable method. We get objects that will
     * later help us both inform strandz, and be informed by strandz.
     */
    public void setNode(AbstNodeTable node, NodeTableModelI model)
    {
        this.node = node;
        //28/11/04 currentNode is kept up to date by node changes, so if it hasn't
        //changed it must be this one right?
        this.currentNode = (Node) node;
        nodeTableModel = getNode().getTableModel();
        if(nodeTableModel == null)
        {
            Err.error("No nodeTableModel from Node " + node);
        }
        controlActionListener = getNode().getControlActionListener();
        if(controlActionListener == null)
        {
            Print.pr("WARNING: No controlActionListener from Node " + node);
        }
        /*
        moveBlock = node.getMoveBlock();
        if(moveBlock == null)
        {
        Err.error( "Must have a MoveBlock");
        }
        */
        if(localNodeChangeListener == null)
        {
            localNodeChangeListener = new LocalNodeChangeListener();
            // Err.pr("AND LISTENER IS " + localNodeChangeListener);
            getNode().removeNodeChangeListener(localNodeChangeListener);
            getNode().addNodeChangeListener(localNodeChangeListener);
        }
        else
        {
            /**
             * PROBABLY RUBBISH - one table per node
             *
             * Its not really the node that is the source for
             * LocalNodeChangeListener events, but the strand ie./
             * this method is called too many times for what we want
             * to do here. Despite this problem we get the convenience
             * of using only AbstNodeTable, and not creating
             * another interface and a setStrand call in this
             * hierarchy.
             */
            Print.pr("One table for one node");
            Print.pr("");
            Print.pr("localNodeChangeListener is not null");
            Err.error("");
        }
    }

    public int getColumnCount()
    {
        return getNodeTableModel().getColumnCount();
    }

    public int getRowCount()
    {
        int result = getNodeTableModel().getRowCount();
        // Err.pr( "@@ row count returning " + result + " for " + this);
        return result;
        /*
        Left as example code just in case we can't dynamically modify any
        Table BeanInfo from Table user wants to use. Reason for this is that
        the model is a runtime affair that will be worked out when requestFocus.


        int result = -99;
        NodeTableModelInterface model = getNodeTableModel();
        if(model != null)
        {
        result = model.getRowCount();
        }
        else
        {
        result = 0;
        }
        return result;
        */
    }

    private void executeCmdOnNode(InputControllerEvent ce)
    {
        node.changeFromCurrentNode();
        if(Session.getMoveManager().readyNextStep())
        {
            controlActionListener.execute(ce);
        }
    }

    /**
     * Not many 'inputs' allow the user to focus again and again on the same
     * spot. Other (non-mouse) inputs are always moving away, so it makes
     * UI sense to always validate, issue message and stop user from going
     * elsewhere. Focus listener for textfields does same as this. Here we
     * first need to find out if the last adpator was at the same row/col
     * as where user has clicked. If it was, and is already in error, then
     * we do super.mouse and return.
     */
    boolean isSpotInError(int row, int col)
    {
        boolean result = false;
        MoveBlock moveB = (MoveBlock) node.getMoveBlock();
        if(moveB.isInError())
        {
            AbstractTableItemAdapter itemAdapter = (AbstractTableItemAdapter) Session.getMoveManager().getConfirmedErrorAdapter();
            // TableItemAdapter origAdapter = (TableItemAdapter)lastAdapter.getOriginalAdapter();
            if(itemAdapter.getRow() == row && itemAdapter.getColumn() == col)
            {
                // Color color = TableSignatures.getBGColor( IdEnum.newTable( tableView, row, col, null));
                // if(origAdapter.isInError())
                // if(color == Color.RED)
                {
                    result = true;
                    // Err.pr( "Where clicked IN error as " + row + ", " + col + " is RED");
                }
            }
            else
            {// Err.pr( "Not same place, orig row " + row +
                // " orig col " + row);
            }
        }
        // Err.pr( "==============isSpotInError ret " + result);
        return result;
    }

    /**
     * Here we are told that the current row has changed. We relay
     * this information directly onto core.info. We can also paint in
     * the row identifier for the row have changed to.
     */
    void mousePressedOn(int idx)
    {
        InputControllerEvent ce = new InputControllerEvent(this,
            OperationEnum.SET_ROW);
        // times++;
        // Err.pr( "mousePressedOn: " + idx + " for node " + node + " times " + times);
        ce.setRow(idx);
        if(node == null)
        {
            Err.error("Node is null");
        }
        executeCmdOnNode(ce);
    }

    private static  int constructedTimes = 0;
    class LocalNodeChangeListener implements NodeChangeListener
    {
        LocalNodeChangeListener()
        {
            constructedTimes++;
            if(constructedTimes == 0)
            {
                Err.pr( "constructedTimes " + constructedTimes + " for node " + node);
                Err.stack();
            }
        }

        public void accessChange(AccessEvent event)
        {// not interested
        }

        public void nodeChangePerformed(NodeChangeEvent e)
        {
            /*
            if(((Node)e.getSource()).equals( "class applic.fishbowl.data.Contact")
            {
            times++;
            if(times == 1)
            {
            Err.stack();
            }
            }
            */
            Err.pr(SdzNote.IMPL, "** Node change to " + e.getSource());
            currentNode = (Node) e.getSource();
            //Err.pr("nodeChangePerformed() to node " + e.getSource() + " on " + tableView.getName());
            if(e.getSource() == node)
            {
                Err.pr( SdzNote.TABLE_REFRESH, "Time to refresh " + node);
                //This (modelDataChanged) didn't work - actually need to bring data out from Strandz onto the
                //controls - which ought to have been done when you clicked on a different master
                //row. Specific problem thta REFRESH() fixed was that when tabbed to the detail tab
                //at least (maybe all?) one non-current row did not hold the right values.
                //tableView.modelDataChanged( "Usually b/c visited another tab");
                //Err.pr( "About to refresh node " + node.getName() + " which contains table " +
                //        ((JComponent)node.getTableControl()).getName());
                Err.pr( SdzNote.SYNC, "To REFRESH " + node);
                node.REFRESH();
                Err.pr( SdzNote.SYNC, "Done REFRESH " + node);
            }
        }

        /**
         * Used as an identifier so we can delete this listener
         */
        public IdentifierI getNode()
        {
            return node;
        }
    }

    //
    public AbstractTableItemAdapter getAdapter(int columnIndex)
    {
        // Err.pr("WHEN/ inside getColName");
        return getNodeTableModel().getAdapter(columnIndex);
    }

    public String getColumnName(int index)
    {
        return getAdapter(index).getItemName();
    }

    public Class getColumnClass(int columnIndex)
    {
        Class result = getNodeTableModel().getColumnClass(columnIndex);
        //This is working!
        //Err.pr( "At " + columnIndex + " returning " + result);
        return result;
    }

    public Object getValueAt(int row, int col)
    {
        if( SdzNote.READ_USER_PROPERTY.isVisible())
        {
            Err.pr( "To get value at row " + row + ", col " + col);
            if(row == 0 && col == 1)
            {
                Err.debug();
            }
        }
        Object obj = getNodeTableModel().getValueAt(row, col);
        if(SdzNote.READ_USER_PROPERTY.isVisible())
        {
            if(obj != null)
            {
                Err.pr( "Got: <" + obj + ">, type: <" + obj.getClass().getName() + ">, obj: " + obj);
            }
            else
            {
                Err.pr( "Got NULL back");
            }
            Err.pr( "");
        }
        return obj;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        // Crap - will be called when user has finished editing a cell
        /**
         * ApplicationError handling always needs to be placed in
         * the outermost method of the stack that someone may alter
         * the source code of. When it is thrown it means that a
         * Dialog has been shown to the user, and the user must correct
         * the fault before doing anything else.
         * TODO - if could stay on same record when all blanks then
         * s/be able to do so here. REVISIT when have done record validation.
         */
        try
        {
            getNodeTableModel().setValueAt(aValue, rowIndex, columnIndex);
        }
        catch(ApplicationError ae)
        {// nufin
        }
    }

    /**
     * This is not explicitly called by my code. When a table
     * and a model are put together the table is automatically
     * made to listen to the model.
     */
    public void addTableModelListener(TableModelListener l)
    {
        listenerList.add(TableModelListener.class, l);
        // Err.pr("to fire later to ListSelectionListener.class");
        //listenerList.add(ListSelectionListener.class, l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        listenerList.remove(TableModelListener.class, l);
        //listenerList.remove(ListSelectionListener.class, l);
    }

    /**
     * WHAT ?
     * Called when we load up a table with a whole load of
     * fresh data. Copied from AbstractTableModel.
     * Problem in that when called from master when the
     * table is the detail, doesn't seem to work when model
     * has no records. Have to focus to get the effect.
     */
    public void fireTableDataChanged( String reason)
    {
        Err.pr( SdzNote.TOO_MANY_TABLE_NOTIFICATIONS, "\tTO fire fireTableDataChanged because " + reason);
        fireTableChanged(new TableModelEvent(this));
    }

    public void fireTableDataChanged( int firstRow, int lastRow, String reason)
    {
        Err.pr( SdzNote.TOO_MANY_TABLE_NOTIFICATIONS, "\tTO fire fireTableDataChanged because " + reason);
        fireTableChanged(new TableModelEvent( 
                this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
    }
    
    /*
    public void fireTableDataChanged()
    {
    *
    tableView.editingStopped( new ChangeEvent( this));
    fireTableChanged(new TableModelEvent( this));
    *
    *
    tableView.invalidate();
    tableView.validate();
    tableView.repaint();
    *
    }
    */

    /**
     * Copied from AbstractTableModel.
     */
    public void fireTableRowsInserted(int firstRow, int lastRow)
    {
        // Err.pr(
        // pUtils.separator + "$$-Inside fireTableRowsInserted for " + currentNode);
        //Err.pr( "$$-inserting from rows " + firstRow + " to " + lastRow);
        fireTableChanged(
            new TableModelEvent(this, firstRow, lastRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    /**
     * Copied from AbstractTableModel.
     */
    public void fireTableRowsDeleted(int firstRow, int lastRow)
    {
        //Err.pr( "\tTO fire fireTableRowsDeleted from " + firstRow + " to " + lastRow);
        fireTableChanged(
            new TableModelEvent(this, firstRow, lastRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
        // Err.pr( "\tDONE fire fireTableRowsDeleted");
    }

    /**
     * Copied from AbstractTableModel.
     */
    protected void fireTableChanged(TableModelEvent e)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for(int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if(listeners[i] == TableModelListener.class)
            {
                ((TableModelListener) listeners[i + 1]).tableChanged(e);
            }
        }
    }

    /*
    public void setSelection( int row)
    {
    Err.error( "NOT USED, setSelection being called to " + row);
    if(getRowCount() > 0)
    {
    lsm.setSelectionInterval( row, row);
    }
    else
    {
    Err.error( "setSelection() cannot be called when " +
    "there are no rows");
    }
    }
    */

    /**
     * Here the JTable is informed of the change. This is a copy from
     * AbstractTableModel.
     */
    /*
    protected void fireSelectionChanged( ListSelectionEvent e)
    {
    Err.error( "Where this called?");
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2)
    {
    if (listeners[i]==ListSelectionListener.class)
    {
    //Err.pr("NOT WORKING (Swing fault) to fire row changed in Impl to "
    //                   + listeners[i+1]);
    //Err.pr( "valueChanged from inside fireSelectionChanged");
    ((ListSelectionListener)listeners[i+1]).valueChanged( e);
    }
    }
    }
    */

    /**
     * Best to put here 'cause user will want to control exactly
     * how we achieve "you can't edit this row".
     */
    public void setEditable(boolean b)
    {
        // Err.pr( "Swing To set " + node + " enabled " + b);
        // CHANGE BACK DEPENDING! tableView.setEnabled( b);
        Err.pr("Enabled set to " + b + " for JTable");
        rowEditable = b;
    }

    public boolean isCellTextBlank(Object value)
    {
        if(value == null)
        {
            return true;
        }
        return false;
    }

    /**
     * Have not yet done a big refactoring for this editable/uneditable
     * stuff. See SdzBug.editabilityRefactoring.
     * <p/>
     * If the whole row is not to be edited then this overrides what
     * may be set for a particular column.
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        if(SdzNote.ENABLEDNESS_REFACTORING.isVisible())
        {
            times++;
            Err.pr("$$$ isCellEditable at " + rowIndex + " " + columnIndex + " times "
                    + times);
            if(times == 1)
            {
                Err.debug();
            }
        }
        boolean result = node.isColumnEnabled(columnIndex);
        if(!rowEditable) // false for whole row will override
        {
            result = false;
        }
        Err.pr(SdzNote.ENABLEDNESS_REFACTORING,
            "$$$ isCellEditable returning " + result + " at col " + columnIndex);
        return result;
    }

    public NodeTableModelI getNodeTableModel()
    {
        if(nodeTableModel == null)
        {
            if(!BeansUtils.isDesignTime())
            {
                Err.error("NodeTableModel is NULL");
            }
        }
        return nodeTableModel;
    }

    AbstNodeTable getNode()
    {
        if(node == null)
        {
            Err.error("Node is NULL");
        }
        return node;
    }

    /**
     * Used by our special CellRenderer
     */
    Node getCurrentNode()
    {
        return currentNode;
    }
    
    /*
    private class LocalNodeChangeListener implements NodeChangeListener
    {
        public void accessChange(AccessEvent event)
        {
        }

        public void nodeChangePerformed(NodeChangeEvent e)
        {
            //Err.pr("nodeChangePerformed() to node " + e.getSource() + " on " + tableView.getName());
            if(e.getSource() == node)
            {
                //Err.pr( SdzNote.TABLE_REFRESH, "Time to refresh " + tableView.getName());
                //This didn't work - actually need to bring data out from Strandz onto the
                //controls - which ought to have been done when you clicked on a different master
                //row. Specific problem thta REFRESH() fixed was that when tabbed to the detail tab
                //at least (maybe all?) one non-current row did not hold the right values. 
                //tableView.modelDataChanged( "Usually b/c visited another tab");
                node.REFRESH();
            }
        }
    }
    */

//    public void repositionTo(Object itemAdapter)
//    {
//        Err.error("No longer used");
//
//        TableItemAdapter tableAdpator = (TableItemAdapter) itemAdapter;
//        int col = tableAdpator.getColumn();
//        int row = tableAdpator.getRow();
//        tableView.setColumnSelectionInterval(col, col);
//        if(currentNode.getState().isNew())
//        {
//            if(!tableAdpator.wasInsertingWhenSetRow())
//            {
//                row++;
//            }
//        }
//        tableView.setRowSelectionInterval(row, row);
//        /*
//        times++;
//        Print.pr( "&&&&&&&&&&&  repositionTo() to row " + row + " times " + times);
//        if(times == 0)
//        {
//        Err.stack();
//        }
//        */
//    }

    /*
    public MoveTracker getMoveManager()
    {
    return moveManager;
    }
    public void setMoveManager( Object moveBlock)
    {
    this.moveManager = (MoveTracker)moveBlock;
    }
    */

}
