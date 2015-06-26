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
import org.strandz.core.domain.NodeChangeEvent;
import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.domain.NodeTableMethods;
import org.strandz.core.domain.NodeTableModelI;
import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.AccessEvent;
import org.strandz.core.domain.event.ControlActionListener;
import org.strandz.core.domain.event.InputControllerEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.Node;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.IdentifierI;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.event.KeyEvent;

/**
 * This class should be visible to the user. It shows how we implement a
 * ListModel. Use is made of the implementation of NodeTableMethods to
 * communicate with core.info.
 * <p/>
 * There are two types of methods here. One type receives
 * information, and the other type sends information
 * to.
 */
public class JListModelImpl extends NodeTableMethods
    implements ListModel
{
    private AbstNodeTable node;
    private ControlActionListener controlActionListener;
    private NodeTableModelI nodeTableModel;
    private JList tableView;
    private DefaultListSelectionModel lsm;
    protected EventListenerList listenerList = new EventListenerList();
    private boolean cellsEditable = true;
    private LocalNodeChangeListener localNodeChangeListener;
    private Node currentNode;
    public boolean debug = false;
    int times = 0;

    public void setDebug(boolean b)
    {
        this.debug = b;
    }

    /**
     * Slight problem in that with normal previous/next
     * button presses we would do the applichousing change, yet
     * with keypress events the change has already been
     * done when we get the event. Here we do not want to
     * call back on fireRowChangedTo, and thus have employed
     * setAlreadyDoneVisually.
     * <p/>
     * Pity that ListSelectionModel is only concerned with
     * mouse clicks.
     * <p/>
     * To do a 100% job of keys will also need to make
     * the table unaware of all keys not mentioned here.
     * At time of writing this only up/down/right/left
     * were implemented in all 3 L&Fs, and left/right can
     * stay.
     */
    private void keyPressed(KeyEvent evt)
    {
        Err.error("Listener just put in!");

        int keycode = evt.getKeyCode();
        switch(keycode)
        {
            case KeyEvent.VK_UP:
            {
                InputControllerEvent ce = new InputControllerEvent(this,
                    OperationEnum.PREVIOUS);
                // ce.setAlreadyDoneVisually( true);
                controlActionListener.execute(ce);
            }
            break;

            case KeyEvent.VK_DOWN:
            {
                InputControllerEvent ce = new InputControllerEvent(this,
                    OperationEnum.NEXT);
                // ce.setAlreadyDoneVisually( true);
                controlActionListener.execute(ce);
            }
            break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                break;

            default:
            {// Err.error( "Don't want to hear from " + evt);
            }
            break;
        }
    }

    /**
     * Called by Block in its setUpTable method. We get objects that will
     * later help us both inform seaweed, and be informed by seaweed.
     */
    public void setNode(AbstNodeTable node, NodeTableModelI model)
    {
        this.node = node;
        nodeTableModel = getNode().getTableModel();
        if(nodeTableModel == null)
        {
            Err.error("No nodeTableModel from Node " + node);
        }
        controlActionListener = getNode().getControlActionListener();
        if(controlActionListener == null)
        {
            Err.error("No controlActionListener from Node " + node);
        }
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
            Err.error("One table for one node");
            Print.pr("");
            Print.pr("localNodeChangeListener is not null");
            Print.pr("");
        }
    }

    public int getSize()
    {
        int result = getNodeTableModel().getRowCount();
        // Err.pr( "getSize() reting " + result);
        return result;
    }

    public int getColumnCount()
    {
        return getNodeTableModel().getColumnCount();
    }

    public int getRowCount()
    {
        int result = getNodeTableModel().getRowCount();
        // Err.pr( "getRowCount() reting " + result);
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

    /**
     * Here we are told that the current row has changed. We relay
     * this information directly onto core.info. We can also paint in
     * the row identifier for the row have changed to.
     */
    void valueChanged(int idx)
    {
        if(cellsEditable)
        {
            InputControllerEvent ce = new InputControllerEvent(this,
                OperationEnum.SET_ROW);
            ce.setRow(idx);
            if(node == null)
            {
                Err.error("Node is null");
            }
            if(getNode() == currentNode)
            {
                controlActionListener.execute(ce);
            }
            else
            {
                if(getNode().GOTO())
                {
                    controlActionListener.execute(ce);
                }
                else
                {
                    Print.pr(
                        "Focusing does work well enough to go back to last field of "
                            + currentNode.getBlock());
                    // ((LastFieldHolder)ex.getLastFieldHolder()).goAsBefore( ex);
                    // ((LastFieldHolder)currentNode.getBlock()).goAsBefore();
                }
            }
        }
    }

    class LocalNodeChangeListener implements NodeChangeListener
    {
        int times = 0;

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
            // Err.pr( "** Node change to " + (Node)e.getSource());
            currentNode = (Node) e.getSource();
        }

        public IdentifierI getNode()
        {
            return null;
        }
    }

    public void oneRowSelectionOn(Object tableView)
    {
        this.tableView = (JList) tableView;

        /* begin, do this like JTable*/
        ListUI ui = new ListUI();
        ui.setOuter(this);
        ui.setTableView(this.tableView);
        this.tableView.setUI(ui);
        /* end */
        this.tableView.setEnabled(true);
        // Err.pr( "Now enabled is " + this.tableView);
        lsm = new DefaultListSelectionModel();
        this.tableView.setSelectionModel(lsm);
        lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        /*
        * Mouse events work if click slowly so allow them!
        *
        * make JList and JTable use this. (Prolly done already)
        * Internal changes
        * can be ignored as is already being done with
        * Strand.alreadyPerformingGoNode. In fact with mechanism already
        * used are avoiding calling twice up the same stack.
        *
        * Now done directly rather than thru valueChanged 'cause
        * valueChanged was being called too much. For example would
        * be called when fireRowChangedTo called if a cell was
        * selected, which meant that whole setRow rigmarole would
        * happen again with setRow, that had just been done with
        * the insert button been pressed.
        */
        // lsm.addListSelectionListener( this);
    }

    public AbstractTableItemAdapter getAdapter(int columnIndex)
    {
        // Err.pr("WHEN/ inside getColName");
        return getNodeTableModel().getAdapter(columnIndex);
    }

    public Class getColumnClass(int columnIndex)
    {
        // Err.pr("WHEN/ inside getColClass");
        return getNodeTableModel().getColumnClass(columnIndex);
    }

    public Object getElementAt(int row)
    {
        Object obj = getNodeTableModel().getValueAt(row, 0);
        return obj;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Err.error("Won't be called, as JList doesn't have an editor/s");
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
     * This is not explicitly called by core.info code. When a table
     * and a model are put together the table is automatically
     * made to listen to the model.
     */
    public void addListDataListener(ListDataListener l)
    {
        listenerList.add(ListDataListener.class, l);
    }

    public void removeListDataListener(ListDataListener l)
    {
        listenerList.remove(ListDataListener.class, l);
    }

    /**
     * Commiting, posting, moving to another record will trigger this. These
     * are the times when anything applichousing is synched to the data.
     */
    public void acceptEdit()
    {// nufin
    }

    public void rejectEdit()
    {// nufin
    }

    public void fireTableDataChanged( String reason)
    {
        fireContentsChanged(this, 0, getRowCount() - 1);
    }

    /**
     * Called when we load up a table with a whole load of
     * fresh data. Copied from AbstractListModel.
     */
    protected void fireContentsChanged(Object source, int index0, int index1)
    {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;
        for(int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if(listeners[i] == ListDataListener.class)
            {
                if(e == null)
                {
                    e = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0,
                        index1);
                }
                ((ListDataListener) listeners[i + 1]).contentsChanged(e);
            }
        }
    }

    public void fireTableRowsInserted(int firstRow, int lastRow)
    {
        fireIntervalAdded(this, firstRow, lastRow);
    }

    /*
    * Copied from AbstractListModel.
    */
    protected void fireIntervalAdded(Object source, int index0, int index1)
    {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;
        for(int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if(listeners[i] == ListDataListener.class)
            {
                if(e == null)
                {
                    e = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0,
                        index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalAdded(e);
            }
        }
    }

    public void fireTableRowsDeleted(int firstRow, int lastRow)
    {
        fireIntervalRemoved(this, firstRow, lastRow);
    }

    /*
    * Copied from AbstractListModel.
    */
    protected void fireIntervalRemoved(Object source, int index0, int index1)
    {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;
        for(int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if(listeners[i] == ListDataListener.class)
            {
                if(e == null)
                {
                    e = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0,
                        index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalRemoved(e);
            }
        }
    }

    /*
    public void setSelection( int row)
    {
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
     * One of our own inventions.
     * Only button presses and when user has
     * said add a record get thru to here
     */
    public void fireRowChangedTo(int row)
    {
        if(getRowCount() > 0)
        {
            /*
            * This call diff to with klg equiv. With klg sm.setRowSelection()
            * would actually call beforeSelect() which has become the equivalent
            * of pressing the mouse key, thus it goes on to do valueChanged()
            * which does all the seaweed stuff.
            * With Swing there was no equivalent of the beforeSelect family, so
            * this call doesn't have any other implications.
            */
            /*
            * If took this away then START and UP/DOWN keys would stop working -
            * or more accurately would stop looking as if they were working,
            * because the row selector would disappear.
            */
            // times++;
            // Err.pr( "In JListImpl are setSelectionInterval to " + row + " times " + times);
            lsm.setSelectionInterval(row, row);
            // if(times == 3) Err.error( "STACK");
        }
         /**/
        // ListSelectionEvent evt = new ListSelectionEvent( this, row, row, false);
        /*
        * Don't seem to need this call, which makes sense if this method
        * (fireRowChangedTo()) is called when have already done the seaweed
        * stuff. This is so b/c valueChanged() is called from
        * fireSelectionChanged(), and valueChanged is what does the seaweed
        * stuff.
        */
        // fireSelectionChanged( evt);
    }

    /**
     * Best to put here 'cause user will want to control exactly
     * how we achieve "you can't edit this row".
     */
    public void setEditable(boolean b)
    {
        // Err.pr( "Swing To set " + node + " enabled " + b);
        tableView.setEnabled(b); // does nothing to JList
        cellsEditable = b;
    }

    public boolean isCellTextBlank(Object value)
    {
        boolean result = false;
        if(value == null)
        {
            result = true;
        }
        Print.pr("isCellTextBlank returning " + result + " for " + value);
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

    private AbstNodeTable getNode()
    {
        if(node == null)
        {
            Err.error("Node is NULL");
        }
        return node;
    }

    public void repositionTo(Object itemAdapter)
    {
        Err.error("[5]Not implemented yet");
    }

    /*
    public void setMoveManager( Object moveManager)
    {
    Err.error( "Not implemented yet");
    }
    */
    public String getTableName()
    {
        return tableView.getName();
    }
}
