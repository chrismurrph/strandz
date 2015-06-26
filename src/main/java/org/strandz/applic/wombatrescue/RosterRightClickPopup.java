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
package org.strandz.applic.wombatrescue;

import org.strandz.core.interf.AbstractTransferOutDialog;
import org.strandz.core.interf.ApplicationHelper;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.TableAttribute;
import org.strandz.data.wombatrescue.calculated.Night;
import org.strandz.data.wombatrescue.calculated.ParticularShift;
import org.strandz.data.wombatrescue.calculated.Clash;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.DebugLabel;
import org.strandz.lgpl.widgets.table.ClickAdapter;
import org.strandz.lgpl.widgets.table.ComponentTableView;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

public class RosterRightClickPopup extends ClickAdapter
{
    private JPopupMenu popupMenu;
    private ComponentTableView tableView;
    private RosterWorkersStrand transferStrand;
    private Node node;
    private RuntimeAttribute clickedOnAttribute;
    private List<TableAttribute> rightClickables;
    private TheRosterStrand callingStrand;
    private int currentRow;
    private int currentColumn;
    private ApplicationHelper applicationHelper;
    //private boolean rosterSlotNavPossible;
    private JMenuItem rosterSlotMenuItem;
    private MenuClickListener l;
    private TheRosterEvents theRosterEvents;

    private static final String WORKER = "Worker";
    private static final String ROSTER_SLOTS = "Roster Slots";
    
    private class MenuClickListener implements ActionListener
    {
        private MenuClickListener()
        {
        }

        public void actionPerformed( ActionEvent e)
        {
            if(e.getActionCommand().equals( WORKER))
            {
                AbstractTransferOutDialog dialog = new WorkerTransferOutDialog(
                        MessageDlg.getFrame(), "Rosterable Worker", 
                        true, transferStrand, clickedOnAttribute, callingStrand, applicationHelper);
                dialog.setVisible( true);
            }
            else if(e.getActionCommand().equals( ROSTER_SLOTS))
            {
                AbstractTransferOutDialog dialog = new RosterSlotTransferOutDialog(
                        MessageDlg.getFrame(), "Roster Slot", 
                        true, transferStrand, clickedOnAttribute, callingStrand, currentRow, 
                        currentColumn, applicationHelper);
                dialog.setVisible( true);
            }
        }
    }
    
    public RosterRightClickPopup( ComponentTableView tableView, 
                                  RosterWorkersStrand visibleStrand, 
                                  Node node,
                                  List<TableAttribute> rightClickables,
                                  TheRosterStrand callingStrand,
                                  ApplicationHelper applicationHelper,
                                  boolean rosterSlotNavPossible,
                                  TheRosterEvents theRosterEvents)
    {
        this.tableView = tableView;
        popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem( WORKER);
        MenuClickListener l = new MenuClickListener();
        menuItem.addActionListener(l);
        popupMenu.add( menuItem);
        if(rosterSlotNavPossible)
        {
            rosterSlotMenuItem = new JMenuItem( ROSTER_SLOTS);
            addRosterSlotsItem( rosterSlotMenuItem, l);
            /*
            menuItem = new JMenuItem( ROSTER_SLOTS);
            menuItem.addActionListener( l);
            popupMenu.add( menuItem);
            */
        }
        this.transferStrand = visibleStrand;
        this.node = node;
        this.rightClickables = rightClickables;
        this.callingStrand = callingStrand;
        this.applicationHelper = applicationHelper;
        //this.rosterSlotNavPossible = rosterSlotNavPossible;
        this.theRosterEvents = theRosterEvents;
    }
    
    private void addRosterSlotsItem( JMenuItem rosterSlotMenuItem, MenuClickListener l)
    {
        rosterSlotMenuItem.addActionListener( l);
        popupMenu.add( rosterSlotMenuItem);
    }

    private void removeRosterSlotsItem()
    {
        popupMenu.remove( rosterSlotMenuItem);
    }
    
    private boolean isNightAlwaysClosed( int row)
    {
        boolean result = false;
        if(theRosterEvents.isAnnotated( row, tableView))
        {
            result = true;
        }
        return result;
    }
    
    public void outerSingleClicked(int row, int column, MouseEvent e)
    {
        JLabel label = (JLabel) tableView.getControlAt( row, column);
        Err.pr(SdzNote.VISIBLE_STRAND_IS_INDEPENDENT, "Outer clicked on " + label.getText());
        clickedOnAttribute = node.getDisplayedAttribute( column);
        /*
        Cell cell = clickedOnAttribute.getCell(); //debug
        Worker worker = (Worker)cell.getLastNavigatedToDO(); //debug        
        Err.pr( SdzNote.RECORD_CURRENT_VALUE, "cell: <" + cell + ">, has worker DO: <" + worker + ">");
        Assert.notNull( worker.toString());
        */
        if(!Utils.isBlank(label.getText()) && rightClickables.contains( clickedOnAttribute))
        {
            if(!isNightAlwaysClosed( row))
            {
                Err.pr( SdzNote.SET_ROW, "comp for popup: " + label.getName() + " has type " + label.getClass().getName()
                 + ", was found at row " + row + ", column " + column);
                /* component that comes from mouse not always one we want to be 'invoker' */
                if(e.getComponent() != label)
                {
                    Err.pr( "label, name: " + label.getName() + ", type " + label.getClass().getName() 
                            + ", id " + ((DebugLabel)label).id + ", parent " + label.getParent());
                    Err.pr( "event comp, name: " + e.getComponent().getName() + ", type " + e.getComponent().getClass().getName() 
                            + ", id " + ((DebugLabel)e.getComponent()).id + ", parent " + e.getComponent().getParent()
                    );
                    Err.error();
                }
                /**/
                currentRow = row;
                currentColumn = column;
                /*
                 * Don't show RSs menu item if there are not any
                 */
                List<Night> list = node.getCell().getDataRecords().getList();
                //Print.prList( list, "DOs of table are on?");
                WorkerI worker = null;
                Object obj = list.get( row);
                if(obj instanceof Worker)
                {
                    worker = (Worker)obj;    
                }
                else if(obj instanceof Night)
                {
                    Night night = (Night)obj;
                    ParticularShift shift = night.getParticularShift( column-2);
                    worker = shift.getWorker();
                }
                else if(obj instanceof Clash)
                {
                    Clash clash = (Clash)obj;
                    ParticularShift shift = clash.getParticularShift( column-2);
                    worker = shift.getWorker();
                }
                else
                {
                    Err.error( "Not catered for type " + obj.getClass().getName());
                }
                //Err.pr( "We are on worker " + worker);
                boolean removed = false;
                if(worker.getRosterSlots().size() == 0)
                {
                    removeRosterSlotsItem();
                    removed = true;
                }
                popupMenu.show( label, e.getX(), e.getY());
                if(removed)
                {
                    addRosterSlotsItem( rosterSlotMenuItem, l);
                }
            }
        }
    }    
}
