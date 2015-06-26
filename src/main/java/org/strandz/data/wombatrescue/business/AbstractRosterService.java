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
package org.strandz.data.wombatrescue.business;

import org.strandz.data.util.ServiceUtils;
import org.strandz.data.wombatrescue.calculated.MonthTransferObj;
import org.strandz.data.wombatrescue.calculated.RosterMonth;
import org.strandz.data.wombatrescue.calculated.RosterPrinterI;
import org.strandz.data.wombatrescue.calculated.RosterTransferObj;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.Print;

import java.util.Iterator;
import java.util.List;

abstract public class AbstractRosterService implements RosterServiceI
{
    private List rosteredVols;
    private Printer textKeeper;
    private DataStore ds;
    private boolean onServer = false;
    public static String notOpenSundayText = RosterSessionUtils.getProperty( "notOpenSundayText");
    public static String closedText = RosterSessionUtils.getProperty( "closedText");
    
    public DataStore getDataStore()
    {
        return ds;
    }
    
    protected void setDataStore( DataStore ds)
    {
        Assert.notNull( ds, "Cannot set the dataStore null in " + this.getClass());
        this.ds = ds;
    }

    private static class Printer implements RosterPrinterI
    {
        private StringBuffer textCollector;

        Printer(StringBuffer textCollector)
        {
            this.textCollector = textCollector;
        }

        public void pr(String txt)
        {
            textCollector.append(txt);
            textCollector.append(FileUtils.DOCUMENT_SEPARATOR);
        }
    }

    private List getAllRosterableWorkers()
    {
        return (getDataStore().getDomainQueries()).executeRetList(WombatDomainQueryEnum.ROSTERABLE_WORKERS_EVICT);
    }

    private void collectRosteringData(RosterMonth currentMonth)
    {
        //Looks like not needed
        //currentMonth.clearUnrosteredAvailableWorkers();
        Assert.isTrue( currentMonth.getUnrosteredAvailableWorkers().isEmpty());
        rosteredVols = currentMonth.getRosteredThisMonthVols();
        for(Iterator iter = getAllRosterableWorkers().iterator(); iter.hasNext();)
        {
            WorkerI rosterableWorker = (WorkerI) iter.next();
            Assert.isFalse( rosterableWorker.isUnknown(), "collectRosteringData() has collected an unknown: " + rosterableWorker);
            /*
            if(rosterableWorker.getSurname() != null && rosterableWorker.getSurname().equals( "Pritchett"))
            {
            Err.debug();
            }
            */
            if(!rosteredVols.contains(rosterableWorker))
            {
                if(!rosterableWorker.isUnknown())
                {
                    Err.pr(JDONote.APPEARS_CONSTRUCTOR_NOT_CALLED,
                           "VOL: " + rosterableWorker + ", BELONGS: " + rosterableWorker.getBelongsToGroup());

                    WorkerI group = rosterableWorker.getBelongsToGroup();
                    Assert.notNull( group, "Every worker must have a group, even if it is the NULL/dummy group, instead got: <" + rosterableWorker + ">, isDummy: " + rosterableWorker.isDummy());
                    //if(group.equals( Worker.NULL )) See LOVSubstitututeInvestigation for why bad thing to do
                    if(group.isDummy())
                    {
                        //This volunteer does not belong to a group as her group is the dummy group ie. unknown group
                        currentMonth.getUnrosteredAvailableWorkers().add(rosterableWorker);
                    }
                }
            }
        }
    }
    
    private void startTx()
    {
        if(isOnServer())
        {
            if(!getDataStore().isOnTx())
            {
                RosterSessionUtils.setGlobalRostererSession( 
                    RostererSessionFactory.newRostererSession( RostererSessionFactory.APP_PROPERTIES_ONLY));
                RosterSessionUtils.setGlobalCurrentParticularRoster( 
                    ParticularRosterFactory.newParticularRoster( "Global Current Particular Roster 1"));
            }
        }
        ServiceUtils.beforeProcessRequest( getDataStore(), isOnServer());
    }
    
    private void endTx()
    {
        ServiceUtils.afterProcessRequest( getDataStore(), isOnServer());
    }

    private List getRosterSlots()
    {
        List result;
        result = (getDataStore().getDomainQueries()).executeRetList(
                WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER_EVICT);
        chkAllFromRosterableWorkers( result);
        //Can cause "Cannot read fields from a deleted object"
        //Print.prList( result, "queryAllRosterSlotsOByWorker()");
        return result;
    }

    private WorkerI getDummyWorker()
    {
        WorkerI result;
        result = (WorkerI)ds.getDomainQueries().executeRetObject(WombatDomainQueryEnum.NULL_WORKER);
        return result;
    }

    private void chkAllFromRosterableWorkers( List<RosterSlotI> rosterSlots)
    {
        for(RosterSlotI rosterSlot : rosterSlots)
        {
            WorkerI worker = rosterSlot.getWorker();
            if(worker != null)
            {
                Assert.isFalse( worker.isUnknown(), "getRosterSlots() query has returned an unknown: " + worker);
                Assert.isTrue( worker.getBelongsToGroup().isDummy(), 
                               "It is the group that should be being rostered, not the group member: " + 
                                       worker.getToShort() + ", who is member of " + worker.getBelongsToGroup().getToShort());
                /*
                if(!worker.isDummy())
                {
                    Err.pr( "christianName: " + worker.getChristianName());
                    Err.pr( "surname: " + worker.getSurname());
                    Err.pr( "groupName: " + worker.getGroupName());
                    Err.pr( "");
                }
                */
            }
        }
    }

    /* Everyone has email these days
    private void printNoEmailWorkers( List noEmailVols)
    {
        textKeeper.pr("ROSTERED BUT NO EMAIL WORKERS");
        for(Iterator iterator = noEmailVols.iterator(); iterator.hasNext();)
        {
            Worker vol = (Worker) iterator.next();
            textKeeper.pr("" + vol);

            List particShifts = RosterBusinessUtils.getParticularShifts(
                    Utils.formList( vol),
                    calculatedObjectsServer.particularShifts, 
                    ds,
                    RosterSessionUtils.getProperty( "memory").equals( "true"),
                    false
                    );
            textKeeper.pr("" + particShifts);
        }
        textKeeper.pr("------------");
    }
    */

    /*
    private static List getNoEmailVols(List vols)
    {
        List result = new ArrayList();
        for(Iterator iterator = vols.iterator(); iterator.hasNext();)
        {
            Worker vol = (Worker) iterator.next();
            // Err.pr( vol.getEmail1() + " for " + vol.getChristianName());
            if(vol.getEmail1() == null)
            {
                result.add(vol);
            }
        }
        return result;
    }
    */

    public RosterTransferObj getRoster(int what, MonthTransferObj month)
    {
        RosterTransferObj result;
        startTx();
        StringBuffer roster = new StringBuffer();
        /*
        ClientObjProvider clientObjProvider = new ClientObjProvider();
        clientObjProvider.setDataStore( (EntityManagedDataStore)getDataStore());
        RosterBusinessUtils.checkSpreadOfDays( clientObjProvider.particularShifts, month.month);
        */
        RosterMonth currentMonth = new RosterMonth( month.month, month.year);
        if(WombatNote.BAD_ROSTER_FROM_TOMCAT.isVisible())
        {
            Print.prList( currentMonth.getNights(), "How make it across?");            
        }
        textKeeper = new Printer( roster);
        if(what == RosteringConstants.ROSTER)
        {
            List rosterSlots = getRosterSlots();
            WorkerI dummyWorker = getDummyWorker();
            currentMonth.allocateRosterSlots( rosterSlots, dummyWorker);
        }
        collectRosteringData(currentMonth);
        if(what == RosteringConstants.ROSTER || what == RosteringConstants.UNROSTERED_STATUS)
        {
            currentMonth.printRoster(textKeeper, 
                                     notOpenSundayText, 
                                     closedText);
            currentMonth.printClashes(textKeeper, notOpenSundayText, closedText);
            currentMonth.printFailedNights(textKeeper, notOpenSundayText, closedText);
            if(what != RosteringConstants.UNROSTERED_STATUS)
            {
                currentMonth.printUnrosteredWorkers(textKeeper, month);
            }
            currentMonth.printAllocationNotes(textKeeper);
        }
        else
        {
            if(what == RosteringConstants.UNROSTERED_VOLS || what == RosteringConstants.ROSTERED_NO_EMAIL_VOLS)
            {
                if(what == RosteringConstants.UNROSTERED_VOLS)
                {
                    currentMonth.printUnrosteredWorkers(textKeeper, month);
                }
                else //if(what == RosteringConstants.ROSTERED_NO_EMAIL_VOLS)
                {
                    //List noEmailRostered = getNoEmailVols(rosteredVols);
                    //printNoEmailWorkers( noEmailRostered);
                    Err.error( "No code for " + what);
                }
            }
            else
            {
                Err.error("YAHOO_BUT_UNKNOWN_VOLS and NOT_ON_YAHOO_VOLS are now reports");
                /*
                collectStaticData();
                if(what == YAHOO_BUT_UNKNOWN_VOLS)
                {
                printYahooButUnknown( allEmails);
                }
                else if(what == NOT_ON_YAHOO_VOLS)
                {
                printNotOnYahoo( allEmails);
                }
                */
            }
        }
        result = new RosterTransferObj( month.month, month.year, roster, currentMonth);
        endTx();
        return result;
    }

    public boolean isOnServer()
    {
        return onServer;
    }

    public void setOnServer(boolean onServer)
    {
        this.onServer = onServer;
    }
}
