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

import javax.swing.JTable;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

public class JTableModelImpl extends AbstTableModelImpl
{
    private JTable tableView;
    private DefaultListSelectionModel lsm;

    public int getSelectedRow()
    {
        return tableView.getSelectedRow();
    }

    public int getSelectedColumn()
    {
        return tableView.getSelectedColumn();
    }

    public String getTableName()
    {
        return tableView.getName();
    }

    /**
     * Commiting, posting, moving to another record will trigger this. These
     * are the times when anything applichousing is synched to the data.
     * <p/>
     * JTable normally does this itself when move to a different cell.
     * Unfortunately we sometimes need it sooner. This will mean that
     * editingStopped will usually we called twice!
     */
    public void acceptEdit()
    {
        // tableView.editingStopped( new ChangeEvent( this));
        if(tableView.isEditing())
        {
            tableView.getCellEditor().stopCellEditing();
        }
    }

    public void rejectEdit()
    {
        // tableView.editingCanceled( new ChangeEvent( this));
        if(tableView.isEditing())
        {
            tableView.getCellEditor().cancelCellEditing();
        }
    }

    public void oneRowSelectionOn(Object tableView)
    {
        // one table biased:
        this.tableView = (JTable) tableView;
        /* tmp
        SeaweedTableUI ui = new SeaweedTableUI();
        ui.setOuter( this);
        ui.setTableView( this.tableView);
        this.tableView.setUI( ui);
        */
        /*
        * As couldn't trap key events we will disable them.
        * (Mouse events stay enabled even thou this is probably
        * wrong).
        */
        // this.tableView.addKeyListener( this);
        this.tableView.setEnabled(true);
        //
        lsm = new DefaultListSelectionModel();
        this.tableView.setSelectionModel(lsm);
        // lsm = this.tableView.getSelectionModel();
        /*
        Alot of these have not been implemented
        KeyStroke keys[] = this.tableView.getRegisteredKeyStrokes();
        for(int i=0; i<=keys.length-1; i++)
        {
        Err.pr( (KeyStroke)keys[i]);
        }
        */
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

    /**
     * One of our own inventions.
     * Only button presses and when user has
     * said add a record get thru to here
     */
    public void fireRowChangedTo(int row)
    {
        // Err.pr( "\nFor " + node + " fireRowChangedTo called with " + row);
        /*
        times++;
        Err.pr( "fireRowChangedTo DONE " + times);
        if(times == 3 && node.toString().equals("sdzdsgnr.data.GUIClass"))
        {
        Err.stack();
        }
        */
        /**
         * This also triggered for START, ie when fireTableDataChanged()
         * triggered, when we first fill a table. As there may be
         * nothing to fill the table with we check getRowCount() > 0
         */
         /**/
        // Err.pr( "\tInside fireRowChangedTo " + row);
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
            // NOW RUBBISH if(!tableDidCursor)
            {
                lsm.setSelectionInterval(row, row);
            }
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
}
