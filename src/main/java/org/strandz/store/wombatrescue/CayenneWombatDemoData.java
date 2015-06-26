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
package org.strandz.store.wombatrescue;

import org.strandz.lgpl.data.objects.DayInWeekI;
import org.strandz.data.wombatrescue.objects.SexI;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.BuddyManagerI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.data.wombatrescue.objects.WhichShiftI;
import org.strandz.data.wombatrescue.objects.SeniorityI;
import org.strandz.data.wombatrescue.objects.FlexibilityI;
import org.strandz.lgpl.store.NotPersistedLookupsI;
import org.strandz.data.wombatrescue.objects.WeekInMonthI;
import org.strandz.data.wombatrescue.objects.NumDaysIntervalI;
import org.strandz.data.wombatrescue.objects.OverrideI;
import org.strandz.data.wombatrescue.objects.cayenne.Flexibility;
import org.strandz.data.wombatrescue.objects.cayenne.WeekInMonth;
import org.strandz.data.wombatrescue.objects.cayenne.WhichShift;
import org.strandz.data.wombatrescue.objects.cayenne.Worker;
import org.strandz.data.wombatrescue.objects.cayenne.RosterSlot;
import org.strandz.data.wombatrescue.objects.cayenne.NumDaysInterval;
import org.strandz.data.wombatrescue.objects.cayenne.MonthInYear;
import org.strandz.data.wombatrescue.objects.cayenne.Seniority;
import org.strandz.data.wombatrescue.objects.cayenne.Sex;
import org.strandz.data.wombatrescue.objects.cayenne.BuddyManager;
import org.strandz.data.wombatrescue.objects.cayenne.DayInWeek;
import org.strandz.data.wombatrescue.objects.cayenne.UserDetails;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.data.wombatrescue.calculated.CayenneWombatLookups;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.UserDetailsI;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.store.EntityManagedDataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * The natural place for this class would be in the task package
 * however we don't want store.wombatrescue depending back on a task
 * package, and there is just no need to go to the complication of
 * using interfaces to solve this issue.
 */
public class CayenneWombatDemoData
{
    public List<WorkerI> newWorkers = new ArrayList<WorkerI>();
    public List<BuddyManagerI> newBMs = new ArrayList<BuddyManagerI>();
    public List<RosterSlotI> newRSs = new ArrayList<RosterSlotI>();
    public List<UserDetailsI> newUsers = new ArrayList<UserDetailsI>();
    public NotPersistedLookupsI wombatLookups;
    
    private WorkerI jeanie;
    private WorkerI lilia;
    private WorkerI alfred;
    private WorkerI derek;
    private WorkerI harry;
    private WorkerI michael;
    private WorkerI justin;
    private WorkerI samir;
    private WorkerI russell;
    private WorkerI guy;
    private WorkerI naba;
    private WorkerI harvey;
    private WorkerI dave;
    private WorkerI con;
    private WorkerI nullWorker;
    private WorkerI davesGroup;
    private WorkerI billy;
    private WorkerI erick;
    private WorkerI clayton;
    private WorkerI kurt;
    private WorkerI noreen;
    private WorkerI jenny;
    private WorkerI phillipa;

    private ORMTypeEnum ormType;
    private EntityManagedDataStore dataStore;

    private static CayenneWombatDemoData instance;
    
    private CayenneWombatDemoData( ORMTypeEnum ormType, EntityManagedDataStore dataStore)
    {
        this.ormType = ormType;
        this.dataStore = dataStore;
    }

    public static CayenneWombatDemoData getInstance()
    {
        return getInstance( ORMTypeEnum.MEMORY, null);
    }

    public static CayenneWombatDemoData getInstance( ORMTypeEnum ormType, EntityManagedDataStore dataStore)
    {
        CayenneWombatDemoData result;
        if(CayenneWombatDemoData.instance == null)
        {
            result = new CayenneWombatDemoData( ormType, dataStore);
            if(ormType.isCayenne())
            {
                /*
                 * Cayenne requires new DOs to be registered before setting them on one
                 * another. Thus the WombatDemoData instance will need an ObjectContext
                 * before doPopulation() is called. Otherwise the error will be like this:
                 * Exception in thread "main" java.lang.NullPointerException
                 * 	at org.apache.cayenne.CayenneDataObject.setToOneTarget(CayenneDataObject.java:300)
	             *  at org.strandz.data.wombatrescue.objects.cayenne.auto._Worker.setActualBelongsToGroup(_Worker.java:219)
	             *  at org.strandz.data.wombatrescue.objects.cayenne.Worker.setBelongsToGroup(Worker.java:227)
                 */
                result.wombatLookups = new CayenneWombatLookups( dataStore.getDomainQueries());
                result.wombatLookups.initValues();
            }
            else
            {
                result.wombatLookups = new org.strandz.data.wombatrescue.objects.WombatLookups();
                result.wombatLookups.initValues();
                result.doPopulation();
            }
            CayenneWombatDemoData.instance = result;
        }
        else
        {
            result = CayenneWombatDemoData.instance;
        }
        return result;
    }

    public void doPopulation()
    {
        nullWorker = createDummyWorker();
        newWorkers.add(nullWorker);

        davesGroup = createWorker(null, null, nullWorker, "Dave Wood's Group", "Dave Wood");
        //davesGroup.setGroupName("Dave Wood's Group");
        //davesGroup.setContactName("Dave Wood");
        davesGroup.setEmail1("Dave.Wood@small-town-isp.com.au");
        newWorkers.add(davesGroup); //1

        newWorkers.add(createWorker("Graham", "Fraenkel", nullWorker)); //2
        newWorkers.add(alfred = createWorker("Alfred", "Nooteboom", nullWorker)); //3
        workerDetailsEx1(alfred);
        newWorkers.add(createWorker("Pierre", "Poisson", nullWorker)); //4
        newWorkers.add(derek = createWorker("Derek", "Granleese", nullWorker)); //5
        workerDetailsEx2(derek);

        newWorkers.add(harry = createWorker("Harry", "Sufehmi", nullWorker)); //6
        workerDetailsEx3(harry);
        newWorkers.add(michael = createWorker("Michael", "Bucket", nullWorker)); //7
        workerDetailsNoPhone(michael);
        newWorkers.add(justin = createWorker("Justin", "Craddock", nullWorker)); //8
        newWorkers.add(createWorker("Raymond", "Cazabon", nullWorker)); //9

        newWorkers.add(samir = createWorker("Samir", "Noshy", nullWorker)); //10
        newWorkers.add(createWorker("George", "Moore", nullWorker)); //11
        newWorkers.add(createWorker("Web", "Masters", nullWorker)); //12
        newWorkers.add(createWorker("Ty", "Paywa", nullWorker)); //13

        newWorkers.add(harvey = createWorker("Harvey", "Crumpet", nullWorker)); //14
        newWorkers.add(russell = createWorker("Russell", "Hinze", nullWorker)); //15
        russell.setFlexibility( Flexibility.NO_OVERNIGHTS);
        newWorkers.add(createWorker("Joshua", "Bloch", nullWorker)); //16
        newWorkers.add(guy = createWorker("Guy", "Steele", nullWorker)); //17
        guy.setFlexibility( Flexibility.NO_EVENINGS);        
        newWorkers.add(billy = createWorker("Billy", "Smith", nullWorker)); //18
        newWorkers.add(erick = createWorker("Erick", "Jones", nullWorker)); //19

        naba = createWorker("Naba", "Barkakati", nullWorker);
        naba.setBelongsToGroup(davesGroup);
        newWorkers.add(naba); //20
        dave = createWorker("Dave", "Wood", nullWorker);
        dave.setGroupContactPerson(true);
        dave.setBelongsToGroup(davesGroup);
        newWorkers.add(dave); //21
        clayton = createWorker("Clayton", "Fahie", nullWorker);
        newWorkers.add( clayton); //22
        clayton.setAway1Start( TimeUtils.getDate( 13, Calendar.JULY, 2007));
        clayton.setAway1End( TimeUtils.getDate( 17, Calendar.JULY, 2007));
        kurt = createWorker("Kurt", "Hilario", nullWorker);
        newWorkers.add( kurt); //23
        kurt.setAway1Start( TimeUtils.getDate( 13, Calendar.JULY, 2007));
        kurt.setAway1End(  TimeUtils.getDate( 17, Calendar.JULY, 2007));
        newWorkers.add(noreen = createWorker("Noreen", "Neve", nullWorker)); //24
        con = createWorker("Con", "Polites", nullWorker);
        con.setBelongsToGroup(davesGroup);
        newWorkers.add(con); //25
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonthI) obtainLOV(WeekInMonth.SECOND_OF_MONTH),
            davesGroup, (WhichShiftI) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonthI) obtainLOV(WeekInMonth.SECOND_OF_MONTH),
            davesGroup, (WhichShiftI) obtainLOV(WhichShift.OVERNIGHT));
        /**/
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIRST_OF_MONTH),
            billy, (WhichShiftI) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonthI) obtainLOV(WeekInMonth.THIRD_OF_MONTH),
            billy, (WhichShiftI) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonthI) obtainLOV(WeekInMonth.FIFTH_OF_MONTH),
            billy, (WhichShiftI) obtainLOV(WhichShift.OVERNIGHT));
        //To create a clash:
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.WEDNESDAY),
            (WeekInMonthI) obtainLOV(WeekInMonth.SECOND_OF_MONTH),
            billy, (WhichShiftI) obtainLOV(WhichShift.OVERNIGHT));
        
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonthI) obtainLOV(WeekInMonth.FIRST_OF_MONTH),
            erick, (WhichShiftI) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonthI) obtainLOV(WeekInMonth.THIRD_OF_MONTH),
            erick, (WhichShiftI) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonthI) obtainLOV(WeekInMonth.FIFTH_OF_MONTH),
            erick, (WhichShiftI) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeekI) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonthI) obtainLOV(WeekInMonth.FOURTH_OF_MONTH),
            noreen, (WhichShiftI) obtainLOV(WhichShift.DINNER));
        /**/
        /* Don't roster here as closed on Thursdays
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.THURSDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.SECOND_OF_MONTH),
            davesGroup);
        */
        newWorkers.add(jeanie = createWorker("Jeanie", "Balcom", nullWorker)); //26
        newWorkers.add(lilia = createWorker("Lilia", "Lavallie", nullWorker)); //27
        newWorkers.add(jenny = createWorker("Jenny", "Jenkins", nullWorker)); //28
        newWorkers.add(phillipa = createWorker("Phillipa", "Hayes", nullWorker)); //29
        jeanie.setUnknown(true);
        lilia.setUnknown(true);

        DayInWeekI mon = (DayInWeekI) obtainLOV(DayInWeek.MONDAY);
        DayInWeekI tue = (DayInWeekI) obtainLOV(DayInWeek.TUESDAY);
        DayInWeekI wed = (DayInWeekI) obtainLOV(DayInWeek.WEDNESDAY);
        DayInWeekI thu = (DayInWeekI) obtainLOV(DayInWeek.THURSDAY);
        DayInWeekI fri = (DayInWeekI) obtainLOV(DayInWeek.FRIDAY);
        DayInWeekI sat = (DayInWeekI) obtainLOV(DayInWeek.SATURDAY);
        //
        WhichShift dinner = (WhichShift) obtainLOV(WhichShift.DINNER);
        WhichShift overnight = (WhichShift) obtainLOV(WhichShift.OVERNIGHT);
        //
        newBMs.add(createBuddyManager(alfred, mon, dinner));
        newBMs.add(createBuddyManager(derek, mon, overnight));
        newBMs.add(createBuddyManager(harry, tue, dinner));
        newBMs.add(createBuddyManager(justin, tue, overnight));
        newBMs.add(createBuddyManager(samir, wed, dinner));
        newBMs.add(createBuddyManager(harvey, wed, overnight));
        newBMs.add(createBuddyManager(samir, thu, dinner));
        newBMs.add(createBuddyManager(harvey, thu, overnight));
        newBMs.add(createBuddyManager(russell, fri, dinner));
        newBMs.add(createBuddyManager(naba, fri, overnight));
        newBMs.add(createBuddyManager(jeanie, sat, dinner));
        newBMs.add(createBuddyManager(lilia, sat, overnight));

        Worker groupOfFourWorkers[] = new Worker[4];
        groupOfFourWorkers = (Worker[]) Utils.toArray(newWorkers, groupOfFourWorkers, 2, 5);
        rosterAWholeMonth(DayInWeek.MONDAY, groupOfFourWorkers);
        groupOfFourWorkers = (Worker[]) Utils.toArray(newWorkers, groupOfFourWorkers, 6, 9);
        rosterAWholeMonth(DayInWeek.TUESDAY, groupOfFourWorkers);
        groupOfFourWorkers = (Worker[]) Utils.toArray(newWorkers, groupOfFourWorkers, 10, 13);
        rosterAWholeMonth(DayInWeek.WEDNESDAY, groupOfFourWorkers);
        groupOfFourWorkers = (Worker[]) Utils.toArray(newWorkers, groupOfFourWorkers, 14, 17);
        rosterAWholeMonth(DayInWeek.FRIDAY, groupOfFourWorkers);
        
        WorkerI groupOfOneWorker[] = new WorkerI[]{ naba};
        rosterAWholeMonth( DayInWeek.SATURDAY, groupOfOneWorker);
        
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIRST_OF_MONTH),
            clayton, (WhichShift) obtainLOV(WhichShift.OVERNIGHT));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.THIRD_OF_MONTH),
            clayton, (WhichShift) obtainLOV(WhichShift.OVERNIGHT));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FOURTH_OF_MONTH),
            clayton, (WhichShift) obtainLOV(WhichShift.OVERNIGHT));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIFTH_OF_MONTH),
            clayton, (WhichShift) obtainLOV(WhichShift.OVERNIGHT));
        
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIRST_OF_MONTH),
            kurt, (WhichShift) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIRST_OF_MONTH),
            kurt, (WhichShift) obtainLOV(WhichShift.OVERNIGHT));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.THIRD_OF_MONTH),
            kurt, (WhichShift) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FOURTH_OF_MONTH),
            kurt, (WhichShift) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIFTH_OF_MONTH),
            kurt, (WhichShift) obtainLOV(WhichShift.DINNER));
        
        newUsers.add( createUser( "Mike", "Mike"));
    }
    
    private void rosterAllNightsExcept( DayInWeek dayInWeek, Worker workers[])
    {
        
    }

    /**
     * Used for a Worker group to take out a whole night
     *
     * @param dayInWeek
     * @param weekInMonth
     * @param worker
     */
    private void rosterAParticularDayOfMonth(DayInWeekI dayInWeek, WeekInMonthI weekInMonth,
                                             WorkerI worker, WhichShiftI whichShift)
    {
        RosterSlotI rs = createRosterSlot();
        rs.setMonthlyRestart(true);
        rs.setDayInWeek(dayInWeek);
        rs.setWeekInMonth(weekInMonth);
        worker.addRosterSlot(rs);
        rs.setWhichShift( whichShift);
        newRSs.add(rs);
    }

    /**
     * To get params examples:
     * (Worker)newWorkers.get( 1)
     * (DayInWeek)obtainLOV( DayInWeek.MONDAY)
     *
     * @param dayInWeek
     * @param workers
     */
    private void rosterAWholeMonth(DayInWeekI dayInWeek, WorkerI workers[])
    {
        NumDaysInterval weekly = (NumDaysInterval) obtainLOV(NumDaysInterval.WEEKLY);
        WhichShift dinnerShift = (WhichShift) obtainLOV(WhichShift.DINNER);
        WhichShift overnightShift = (WhichShift) obtainLOV(WhichShift.OVERNIGHT);
        for(int i = 0; i < workers.length; i++)
        {
            WorkerI worker = workers[i];
            //Err.pr( "Rostering " + worker + " for a whole month on " + dayInWeek);
            if(worker == russell && dayInWeek == DayInWeek.SATURDAY)
            {
                Print.prArray( workers, "Group of workers");
                Err.stack();
            }
        }
        if(workers.length > 0 && workers[0] != null)
        {
            RosterSlotI rs = createRosterSlot();
            rs.setDayInWeek(dayInWeek);
            rs.setWhichShift(dinnerShift);
            rs.setNumDaysInterval(weekly);
            //rs.setWorker( workers[0]);
            workers[0].addRosterSlot(rs);
            newRSs.add(rs);
        }
        if(workers.length > 1 && workers[1] != null)
        {
            RosterSlotI rs = createRosterSlot();
            rs.setDayInWeek(dayInWeek);
            rs.setWhichShift(dinnerShift);
            rs.setNumDaysInterval(weekly);
            //rs.setWorker( workers[1]);
            workers[1].addRosterSlot(rs);
            newRSs.add(rs);
        }
        if(workers.length > 2 && workers[2] != null)
        {
            RosterSlotI rs = createRosterSlot();
            rs.setDayInWeek(dayInWeek);
            rs.setWhichShift(overnightShift);
            rs.setNumDaysInterval(weekly);
            //rs.setWorker( workers[2]);
            workers[2].addRosterSlot(rs);
            newRSs.add(rs);
        }
        if(workers.length > 3 && workers[3] != null)
        {
            RosterSlotI rs = createRosterSlot();
            rs.setDayInWeek(dayInWeek);
            rs.setWhichShift(overnightShift);
            rs.setNumDaysInterval(weekly);
            //rs.setWorker( workers[3]);
            workers[3].addRosterSlot(rs);
            newRSs.add(rs);
        }
    }
    
    private UserDetailsI createUser( String user, String pass)
    {
        UserDetailsI result = new UserDetails();
        result.setUsername( user);
        result.setPassword( pass);
        result.setDatabaseUsername( "root");
        return result;
    }

    private RosterSlotI createRosterSlot()
    {
        RosterSlotI result;
        if(ormType == ORMTypeEnum.CAYENNE_SERVER)
        {
            result = (RosterSlotI)dataStore.getEM().newPersistent(
                org.strandz.data.wombatrescue.objects.cayenne.RosterSlot.class);
        }
        else
        {
            result = new RosterSlot();
        }
        result.setDayInWeek((DayInWeekI) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_DAY_IN_WEEK), DayInWeek.NULL));
        result.setWhichShift((WhichShiftI) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_WHICH_SHIFT), WhichShift.NULL));
        result.setNumDaysInterval((NumDaysIntervalI) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_INTERVAL), NumDaysInterval.NULL));
        result.setWeekInMonth((WeekInMonthI) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_WEEK_IN_MONTH), WeekInMonth.NULL));
        result.setOverridesOthers((OverrideI) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_OVERRIDE),
            org.strandz.data.wombatrescue.objects.cayenne.Override.NULL));
        result.setOnlyInMonth((MonthInYear) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR), MonthInYear.NULL));
        result.setNotInMonth((MonthInYear) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR), MonthInYear.NULL));
        return result;
    }

    private BuddyManagerI createBuddyManager(WorkerI worker, DayInWeekI dayInWeek, WhichShiftI whichshift)
    {
        BuddyManagerI result;
        Assert.notNull(worker, "Cannot create a BMer without a worker");
        if(ormType == ORMTypeEnum.CAYENNE_SERVER)
        {
            result = (BuddyManagerI)dataStore.getEM().newPersistent(
                org.strandz.data.wombatrescue.objects.cayenne.BuddyManager.class);
        }
        else
        {
            result = new BuddyManager();
        }
        result.setDayInWeek(dayInWeek);
        result.setWhichShift(whichshift);
        result.setWorker(worker);
        return result;
    }

    private WorkerI createWorker(String name, String surname, WorkerI group)
    {
        return createWorker( name, surname, group, null, null);
    }

    private WorkerI createWorker(String name, String surname, WorkerI group, String groupName, String contactName)
    {
        WorkerI result;
        Assert.notNull( group);
        Assert.isFalse(name == null && surname == null && group == null);
        if(ormType == ORMTypeEnum.CAYENNE_SERVER)
        {
            result = (WorkerI)dataStore.getEM().newPersistent(
                org.strandz.data.wombatrescue.objects.cayenne.Worker.class);
            result.init( nullWorker);
        }
        else
        {
            result = new Worker();
        }
        result.setBelongsToGroup(group);
        result.setChristianName(name);
        result.setSurname(surname);
        result.setShiftPreference((WhichShiftI) obtainLOV(WhichShift.NULL));
        result.setSeniority((SeniorityI) obtainLOV(Seniority.EXPERIENCED));
        result.setSex((SexI) obtainLOV(Sex.NULL));
        result.setFlexibility((FlexibilityI) obtainLOV(Flexibility.FLEXIBLE));
        result.setEmail1(name + "." + surname + "@small-town-isp.com.au");
        result.setWorkPhone("9734 5500"); //everyone the same!
        result.setGroupName( groupName);
        result.setContactName( contactName);
        result.setComments( "comment for " + result.getToShort());
        return result;
    }

    private WorkerI createDummyWorker()
    {
        WorkerI result;
        if(ormType == ORMTypeEnum.CAYENNE_SERVER)
        {
            Assert.notNull( dataStore);
            Assert.notNull( dataStore.getEM());
            result = (WorkerI)dataStore.getEM().newPersistent(
                org.strandz.data.wombatrescue.objects.cayenne.Worker.class);
            result.init( result); //Yes the dummy's dummy is itself
            result.setDummy(true);
        }
        else
        {
            result = new Worker();
            result.setDummy(true);
        }
        return result;
    }

    private void workerDetailsEx1(WorkerI worker)
    {
        worker.setStreet("30 Heather Court");
        worker.setSuburb("Paradise");
        worker.setPostcode("");
        worker.setHomePhone("8276 5882");
        worker.setWorkPhone("8276 5882");
        worker.setMobilePhone("0402 456 895");
    }

    private void workerDetailsEx2(WorkerI worker)
    {
        worker.setStreet("17 Woodhouse Cresent");
        worker.setSuburb("Wattle Park");
        worker.setPostcode("");
        worker.setHomePhone("8276 5882");
        worker.setWorkPhone("8276 5882");
        worker.setMobilePhone("0402 456 895");
    }

    private void workerDetailsEx3(WorkerI worker)
    {
        worker.setStreet("6 Twilight Drive");
        worker.setSuburb("Happy Valley");
        worker.setPostcode("");
        worker.setHomePhone("8276 5882");
        worker.setWorkPhone("8276 5882");
        worker.setMobilePhone("0402 456 895");
    }

    private void workerDetailsNoPhone(WorkerI worker)
    {
        worker.setStreet("29 Amberly Road");
        worker.setSuburb("Loughton");
        worker.setPostcode("");
        worker.setHomePhone( null);
        worker.setWorkPhone( null);
        worker.setMobilePhone( null);
    }

    /**
     * Only exists to make calling code easier to read
     */
    Object obtainLOV(Object typeWant)
    {
        Object result = null;
        /*
        if(ormType == ORMTypeEnum.CAYENNE_SERVER)
        {

        }
        else if(ormType == ORMTypeEnum.MEMORY)
        */
        {
            if(typeWant instanceof DayInWeek)
            {
                result = Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_DAY_IN_WEEK), typeWant);
            }
            else if(typeWant instanceof Flexibility)
            {
                result = Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_FLEXIBILITY), typeWant);
            }
            else if(typeWant instanceof NumDaysInterval)
            {
                result = Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_INTERVAL), typeWant);
            }
            else if(typeWant instanceof MonthInYear)
            {
                result = Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR), typeWant);
            }
            else if(typeWant instanceof org.strandz.data.wombatrescue.objects.Override)
            {
                result = Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_OVERRIDE), typeWant);
            }
            else if(typeWant instanceof Seniority)
            {
                result = Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY), typeWant);
            }
            else if(typeWant instanceof Sex)
            {
                result = Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_SEX), typeWant);
            }
            else if(typeWant instanceof WeekInMonth)
            {
                result = Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_WEEK_IN_MONTH), typeWant);
            }
            else if(typeWant instanceof WhichShift)
            {
                result = Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_WHICH_SHIFT), typeWant);
            }
            else
            {
                Err.error("Populate not yet support " + typeWant);
            }
            if(result == null)
            {
                Err.error("Got a null when wanted a " + typeWant);
            }
        }
        /*
        else
        {
            Err.error( "Not yet coded");
        }
        */
        return result;
    }
}
