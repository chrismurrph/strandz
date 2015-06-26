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
package org.strandz.task.data.wombatrescue;

import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.data.wombatrescue.objects.BuddyManager;
import org.strandz.data.wombatrescue.objects.RosterSlot;
import org.strandz.data.wombatrescue.objects.Seniority;
import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.SeniorityI;
import org.strandz.data.wombatrescue.objects.SexI;
import org.strandz.data.wombatrescue.objects.WeekInMonthI;
import org.strandz.data.wombatrescue.objects.OverrideI;
import org.strandz.data.wombatrescue.objects.NumDaysIntervalI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.FlexibilityI;
import org.strandz.data.wombatrescue.objects.WhichShiftI;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.data.objects.DayInWeekI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DataStoreOpsUtils;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import java.util.List;

public class ReferenceFixer
{
    private DataStoreOpsUtils.CopiedResult copiedResult;
    private List sourceBMs;
    private List targetBMs;
    private List sourceWorkers;
    private List targetWorkers;
    private List sourceRosterSlots;
    private List targetRosterSlots;
    private List targetDayInWeeks;
    private List targetWhichShifts;
    private List targetWeekInMonths;
    private List targetOverrides;
    private List targetNumDaysIntervals;
    private List targetMonthInYears;
    private List targetFlexibilities;
    private List targetSeniorities;
    private List targetSexes;
    private DomainQueriesI queriesI;         

    ReferenceFixer( DataStoreOpsUtils.CopiedResult copiedResult)
    {
        this.copiedResult = copiedResult;
    }

    void fixReferences(EntityManagedDataStore from, EntityManagedDataStore to)
    {
        from.startTx();
        to.startTx();
        queriesI = to.getDomainQueries();         
        init( to);
        fixBuddyManagers();
        fixRosterSlots();
        fixWorkers();
        //addNullWorker( to);
        addRosterSlotsToEachWorker();
        debug();
        //debugBeforeReadBack();
        from.commitTx();
        to.commitTx();
        //debugAfterReadBack();
    }

    /**
     * Problem is too many, so don't need this!
     * MakeNullWorker does same thing 
     */
    private void addNullWorker( EntityManagedDataStore dataStore)
    {
        Worker nullWorker = new Worker();
        nullWorker.setDummy(true);
        nullWorker.setBelongsToGroup(nullWorker);
        dataStore.getEM().registerPersistent(nullWorker);
    }
    
    private void debugBeforeReadBack()
    {
        Err.pr( "Before commit:");
        Util.debugWorkerSlots( targetRosterSlots, "Chris", "Murphy");
    }    

    private void debugAfterReadBack()
    {
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection( WombatConnectionEnum.WOMBAT_KODO);
        DataStore dataStore = dataStoreFactory.getDataStore();
        dataStore.startTx();
        Err.pr( "After read back in " + dataStore + ":");
        List afterSlots = dataStore.query( RosterSlot.class);
        Util.debugWorkerSlots( afterSlots, "Chris", "Murphy");
    }

    private void init( DataStore dataStore)
    {
        sourceBMs = Utils.obtainListOfType( copiedResult.source, BuddyManager.class);
        targetBMs = Utils.obtainListOfType( copiedResult.target, BuddyManager.class);
        sourceWorkers = Utils.obtainListOfType( copiedResult.source, Worker.class);
        targetWorkers = Utils.obtainListOfType( copiedResult.target, Worker.class);
        sourceRosterSlots = Utils.obtainListOfType( copiedResult.source, RosterSlot.class);
        targetRosterSlots = Utils.obtainListOfType( copiedResult.target, RosterSlot.class);

        //copiedResult does not include lookups
        WombatLookups wombatLookups = new WombatLookups();
        wombatLookups.initValues();
        dataStore.set(POJOWombatData.LOOKUPS, Utils.formList( wombatLookups));
        targetDayInWeeks = wombatLookups.get( WombatDomainLookupEnum.ALL_DAY_IN_WEEK); 
        targetWhichShifts = wombatLookups.get( WombatDomainLookupEnum.ALL_WHICH_SHIFT); 
        targetWeekInMonths = wombatLookups.get( WombatDomainLookupEnum.ALL_WEEK_IN_MONTH);
        targetOverrides = wombatLookups.get( WombatDomainLookupEnum.ALL_OVERRIDE);
        targetNumDaysIntervals = wombatLookups.get( WombatDomainLookupEnum.ALL_INTERVAL);
        targetMonthInYears = wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR);
        targetFlexibilities = wombatLookups.get( WombatDomainLookupEnum.ALL_FLEXIBILITY);
        targetSeniorities = wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY);
        targetSexes = wombatLookups.get( WombatDomainLookupEnum.ALL_SEX);
        Utils.chkNoNulls( targetSeniorities, "targetSeniorities");
    }
    
    private void debug()
    {
//        Print.prList( sourceBMs, "sourceBMs", false);
//        Print.prList( targetBMs, "targetBMs", false);
//        Print.prList( sourceWorkers, "sourceWorkers", false);
//        Print.prList( targetWorkers, "targetWorkers", false);
//        Print.prList( sourceRosterSlots, "sourceRosterSlots", false);
//        Print.prList( targetRosterSlots, "targetRosterSlots", false);
    }

    private void fixBuddyManagers()
    {
        for(int i = 0; i < sourceBMs.size(); i++)
        {
            BuddyManager sourceBuddyManager = (BuddyManager)sourceBMs.get(i);
            BuddyManager targetBuddyManager = (BuddyManager)targetBMs.get(i);
            WorkerI targetWorker = findEquivalentTargetWorker( sourceBuddyManager.getWorker());
            targetBuddyManager.setWorker( targetWorker);
            DayInWeekI dayInWeek = findEquivalentDayInWeek( sourceBuddyManager.getDayInWeek());
            targetBuddyManager.setDayInWeek( dayInWeek);
            WhichShiftI whichShift = findEquivalentWhichShift( sourceBuddyManager.getWhichShift());
            targetBuddyManager.setWhichShift( whichShift);
        }
    }
    
    private void fixRosterSlots()
    {
        for(int i = 0; i < sourceRosterSlots.size(); i++)
        {
            RosterSlot sourceRosterSlot = (RosterSlot)sourceRosterSlots.get(i);
            RosterSlot targetRosterSlot = (RosterSlot)targetRosterSlots.get(i);
            if(sourceRosterSlot.getWorker() == null)
            {
                //Err.pr( "RosterSlot orphan: " + sourceRosterSlot);
            }
            else
            {
                WorkerI targetWorker = findEquivalentTargetWorker( sourceRosterSlot.getWorker());
                //Err.pr( "source RosterSlot had worker: " + sourceRosterSlot.getWorker());
                targetRosterSlot.setWorker( targetWorker);
                //Err.pr( "target RosterSlot now has worker: " + targetRosterSlot.getWorker());
            }
            DayInWeekI dayInWeek = findEquivalentDayInWeek( sourceRosterSlot.getDayInWeek());
            targetRosterSlot.setDayInWeek( dayInWeek);
            WhichShiftI whichShift = findEquivalentWhichShift( sourceRosterSlot.getWhichShift());
            targetRosterSlot.setWhichShift( whichShift);
            WeekInMonthI weekInMonth = findEquivalentWeekInMonth( sourceRosterSlot.getWeekInMonth());
            targetRosterSlot.setWeekInMonth( weekInMonth);
            OverrideI override = findEquivalentOverride( sourceRosterSlot.getOverridesOthers());
            targetRosterSlot.setOverridesOthers( override);
            NumDaysIntervalI numDaysInterval = findEquivalentNumDaysInterval( sourceRosterSlot.getNumDaysInterval());
            targetRosterSlot.setNumDaysInterval( numDaysInterval);
            MonthInYearI onlyInMonth = findEquivalentMonthInYear( sourceRosterSlot.getOnlyInMonth());
            targetRosterSlot.setOnlyInMonth( onlyInMonth);
            MonthInYearI notInMonth = findEquivalentMonthInYear( sourceRosterSlot.getNotInMonth());
            targetRosterSlot.setNotInMonth( notInMonth);
        }
    }
    
    private void fixWorkers()
    {
        for(int i = 0; i < sourceWorkers.size(); i++)
        {
            WorkerI sourceWorker = (Worker)sourceWorkers.get(i);
            WorkerI targetWorker = (Worker)targetWorkers.get(i);
            if(!sourceWorker.isDummy())
            {
                WorkerI belongsToGroup = findEquivalentTargetWorker( sourceWorker.getBelongsToGroup());
                targetWorker.setBelongsToGroup( belongsToGroup);
            }
            else
            {
                Err.pr( "dummy source worker belongs to group " + sourceWorker.getBelongsToGroup());
                Err.pr( "dummy target worker belongs to group " + targetWorker.getBelongsToGroup());
                Err.pr( "dummy source worker s/mean dummy target worker " + targetWorker.isDummy());
                Assert.isTrue( targetWorker.isDummy());
                //targetWorker.setBelongsToGroup( targetWorker);
            }
            if(sourceWorker.getSeniority() != null)
            {
                SeniorityI seniority = findEquivalentSeniority( sourceWorker.getSeniority());
                targetWorker.setSeniority( seniority);
                SexI sex = findEquivalentSex( sourceWorker.getSex());
                targetWorker.setSex( sex);
                FlexibilityI flexibility = findEquivalentFlexibility( sourceWorker.getFlexibility());
                targetWorker.setFlexibility( flexibility);
            }
            else
            {
                Err.pr( "S/be NULL worker " + sourceWorker);
                Assert.isTrue( sourceWorker.isDummy());
            }
        }
    }

    /**
     * This should fill the join table
     */
    private void addRosterSlotsToEachWorker()
    {
        for(int i = 0; i < targetRosterSlots.size(); i++)
        {
            RosterSlot targetRosterSlot = (RosterSlot)targetRosterSlots.get(i);
            WorkerI worker = targetRosterSlot.getWorker();
            worker.addRosterSlot( targetRosterSlot);
        }
    }

    private WorkerI findEquivalentTargetWorker( WorkerI sourceWorker)
    {
        Worker result = (Worker)Utils.getFromList( targetWorkers, sourceWorker, true, new String[]{ "christianName", "surname", "groupName"});
        return result;
    }

    private DayInWeekI findEquivalentDayInWeek( DayInWeekI dayInWeek)
    {
        DayInWeek result = (DayInWeek)Utils.getFromList( targetDayInWeeks, dayInWeek, true, new String[]{ "name"});
        if(!result.equals( dayInWeek))
        {
            Err.error( "Got " + result + " from " + dayInWeek);
        }
        return result;
    }
    
    private WhichShiftI findEquivalentWhichShift( WhichShiftI whichShift)
    {
        WhichShiftI result = (WhichShiftI)Utils.getFromList( targetWhichShifts, whichShift, true, new String[]{ "name"});
        return result;
    }
    
    private WeekInMonthI findEquivalentWeekInMonth( WeekInMonthI weekInMonth)
    {
        WeekInMonthI result = (WeekInMonthI)Utils.getFromList( targetWeekInMonths, weekInMonth, true, new String[]{ "name"});
        return result;
    }
    
    private OverrideI findEquivalentOverride( OverrideI override)
    {
        OverrideI result = (OverrideI)Utils.getFromList( targetOverrides, override, true, new String[]{ "name"});
        return result;
    }
    
    private NumDaysIntervalI findEquivalentNumDaysInterval( NumDaysIntervalI interval)
    {
        NumDaysIntervalI result = (NumDaysIntervalI)Utils.getFromList( targetNumDaysIntervals, interval, true, new String[]{ "name"});
        return result;
    }
    
    private MonthInYearI findEquivalentMonthInYear( MonthInYearI monthInYear)
    {
        MonthInYearI result = (MonthInYearI)Utils.getFromList( targetMonthInYears, monthInYear, true, new String[]{ "name"});
        return result;
    }

    private FlexibilityI findEquivalentFlexibility( FlexibilityI flexibility)
    {
        FlexibilityI result = (FlexibilityI)Utils.getFromList( targetFlexibilities, flexibility, true, new String[]{ "name"});
        return result;
    }

    private SeniorityI findEquivalentSeniority( SeniorityI seniority)
    {
//        Utils.chkNoNulls( targetSeniorities, "targetSeniorities");
//        Err.pr( "Done chkNoNulls in findEquivalentSeniority");
//        Assert.notNull( seniority);
        SeniorityI result = (SeniorityI)Utils.getFromList( targetSeniorities, seniority, true, new String[]{ "name"}, false);
        if(result == null)
        {
            if(seniority.getName().equals( "senior"))
            {
                result = (Seniority)Utils.getFromList( targetSeniorities, Seniority.EXPERIENCED);
            }
            else if(seniority.getName().equals( "new"))
            {
                result = (Seniority)Utils.getFromList( targetSeniorities, Seniority.NEWBIE);
            }
            else
            {
                Print.prList( targetSeniorities, "targetSeniorities");
                Err.error( "Not coded for <" + seniority.getName() + "> seniority");
            }
        }
        return result;
    }
    
    private SexI findEquivalentSex( SexI sex)
    {
        SexI result = (SexI)Utils.getFromList( targetSexes, sex, true, new String[]{ "name"});
        return result;
    }    
}
