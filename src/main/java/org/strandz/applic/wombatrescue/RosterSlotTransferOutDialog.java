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

import org.strandz.core.applichousing.AdornedTransferOutDialog;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.core.interf.ApplicationHelper;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.StrandI;
import org.strandz.data.wombatrescue.calculated.Night;
import org.strandz.data.wombatrescue.calculated.ParticularShift;
import org.strandz.data.wombatrescue.calculated.RosterMonth;
import org.strandz.data.wombatrescue.objects.RosterSlotHelper;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Print;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.util.Iterator;
import java.util.List;

public class RosterSlotTransferOutDialog extends AdornedTransferOutDialog
{
    private int currentRow;
    private int currentColumn;
    private TheRosterDT callingDt;
    private WRTransferOutHelper helper;

    /**
     * particularShifts on a night are arranged in the same order there as they are
     * when a night is represented on a screen. This conversion factor accounts for
     * the extra columns on the screen that come in before particularShifts 
     */
    private static final int SCREEN_TO_PARTIC_SHIFT_TRANS = 2;
    
    public RosterSlotTransferOutDialog( Frame owner, String title, boolean modal, VisibleStrand transferVisibleStrand, 
                                   RuntimeAttribute clickedOnAttribute, TheRosterStrand callingStrand, 
                                   int currentRow, int currentColumn, ApplicationHelper applicationHelper)
            throws HeadlessException
    {
        super(owner, title, modal, transferVisibleStrand, clickedOnAttribute, callingStrand, applicationHelper);
        helper = new WRTransferOutHelper( transferVisibleStrand);
        this.currentRow = currentRow;
        this.currentColumn = currentColumn;
        callingDt = callingStrand.getDt();
        Assert.notNull( callingDt);
        relocationOutCallbacks.performSearch();
    }

    public void relocatePanelGoingOut( SdzBagI sbI)
    {
    }
    
    public void relocatePanelComingBack( SdzBagI sbI)
    {
    }
    
    public Node getTargetNode( StrandI transferStrand)
    {
        Node result;
        result = transferStrand.getNodeByName( "rosterSlots List Detail Node");
        return result;
    }

    public void setPanelNodeTitlePanelGoingOut( SdzBagI sbI)
    {
        //
        Err.pr( SdzNote.NOT_KEEPING_PLACE, "How Worker did it, don't need afterUserDismissed()");
        //How Worker did it - doing this way keeps the current Worker even after Dialog has been
        //closed - which is effect we want
        //transferVisibleStrand.setPanelNodeTitle( sbI.getPane(0), sbI.getNode(0), sbI.getNode(0).getDisplayName());
        //
        Node rosterSlotNode = sbI.getNode(1);
        transferVisibleStrand.setPanelNodeTitle( sbI.getPane(1), rosterSlotNode, 
                                          rosterSlotNode.getDisplayName());
    }
    
    public void setPanelNodeTitlePanelComingBack( SdzBagI sbI)
    {
        /*
        helper.setPanelNodeTitlePanelComingBack( sbI);
        */
    }

    /**
     * We will be going from the 'calling' strand to the 'transfer' strand, so the search is done on
     * the rosterSlots in the 'transfer' strand, using the current rosterSlot in the 'calling' strand.
     * 
     * @param clickedOnAttribute
     */
    public void performSearch( RuntimeAttribute clickedOnAttribute)
    {
        /**/
        Cell cell = clickedOnAttribute.getCell();
        WorkerI worker = (WorkerI)cell.getLastNavigatedToDO();
        Assert.notNull( worker, "Do not have a last navigated to worker in cell <" + cell + ">");
        Assert.isFalse( worker.isDummy());
        Assert.notNull(worker.toString(), "getLastNavigatedToDO().toString() s/not be null, used cell " + cell);
        String orderBy = worker.getOrderBy();
        AlphabetActionListener alphabetActionListener = ((RosterWorkersStrand)transferVisibleStrand).
                volunteerTriggers.alphabetListener; 
        boolean ok = alphabetActionListener.findWorkerStartsWith( orderBy);
        if(!ok)
        {
            Err.error( "Have not been able to find a worker that starts with <" + 
                    orderBy + "> in cell <" + alphabetActionListener.getWorkerCell() + ">");
        }
        transferStrand.goNode( getTargetNode( transferStrand));
        /**/
        if(currentColumn >= SCREEN_TO_PARTIC_SHIFT_TRANS)
        {
            Node transferRosterSlotsNode = transferStrand.getCurrentNode();
            //Err.pr( "After goNode(), current transferRosterSlotsNode: " + transferRosterSlotsNode);
            Cell rosterMonthCell = callingDt.rosterMonthCell;
            RosterMonth rosterMonth = (RosterMonth)rosterMonthCell.getLastNavigatedToDO(); //only one anyway
            List<Night> nights = rosterMonth.getNights();
            Night night = nights.get( currentRow);
            int shiftOnNight = currentColumn - SCREEN_TO_PARTIC_SHIFT_TRANS;
            ParticularShift particularShift = night.getParticularShift( shiftOnNight);
            RosterSlotI rosterSlot = particularShift.getRosterSlot();
            //Assert.notNull( rosterSlot, "No rosterSlot found in particularShift: <" + particularShift + ">");
            if(rosterSlot != null)
            {
                Cell transferCell = transferRosterSlotsNode.getCell();
                List<RosterSlotI> rosterSlots = transferCell.getDataRecords().getList();
                Assert.isFalse( rosterSlots.isEmpty(), "transfer cell " + transferCell + 
                        " does not have any rosterSlots in its dataRecords - yet would expect it to - " +
                        "as need a rosterSlot in order to have a name on a night");
                int idx = 0;
                int whichRosterSlot = Utils.UNSET_INT;
                boolean orig = RosterSlotHelper.ownHashEquals;
                RosterSlotHelper.ownHashEquals = true;
                for(Iterator<RosterSlotI> iterator = rosterSlots.iterator(); iterator.hasNext();)
                {
                    RosterSlotI slot = iterator.next();
                    if(slot.equals( rosterSlot))
                    {
                        whichRosterSlot = idx;
                        //Err.pr( "Match on rosterSlot: " + rosterSlot);
                        break;
                    }
                    idx++;
                }
                RosterSlotHelper.ownHashEquals = orig;
                if(whichRosterSlot == Utils.UNSET_INT)
                {
                    if(WombatNote.PARTIC_SHIFT_HAS_BAD_ROSTER_SLOTS.isVisible())
                    {
                        Err.pr( "roster slot that got from particular shift is <" + rosterSlot + ">");
                        Print.prList( rosterSlots, "It should have been matched to one of these but was not");
                    }
                }
                else
                {
                    ok = transferStrand.SET_ROW( whichRosterSlot);
                    if(ok)
                    {
                        //Err.pr( "Gone to " + whichRosterSlot);        
                    }
                    else
                    {
                        Err.error( "NO match on rosterSlot: <" + rosterSlot + "> because " + ReasonNotEquals.formatReasons());
                    }
                }
            }
            else
            {
                // 'No rosterSlot found in particularShift' when click on a Clashed Night. With no searching done will
                // just land at the first roster slot which is ok
            }
        }
        else
        {
            //When we just have a vertical list of workers then they are always in column 0. As the worker
            //is not on any particularShift then it does not make sense to search for the rosterSlot that
            //caused the particularShift.
        }
    }
    
    public boolean afterUserDismissed()
    {
        boolean result = true;
        super.afterUserDismissed();
        Strand strand = transferVisibleStrand.getSdzBagI().getStrand();
        Node node = strand.getCurrentNode();
        Node workerNode = strand.getNodeByName( "Worker Node");
        if(node != workerNode)
        {
            boolean ok = workerNode.GOTO();
            if(!ok)
            {
                /*
                 * Ok to do nothing, just don't put more code after here. Validation may have
                 * been triggered which means do not want to leave the dialog
                 */
                //Err.error( "Could not get to " + workerNode);
                result = false;
            }
            else
            {
                Err.pr( SdzNote.NOT_KEEPING_PLACE, "Node now: <" + workerNode.getName() + "> (fixes issue)");
            }
        }
        return result;
    }
}
