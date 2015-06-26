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

import org.strandz.data.wombatrescue.objects.BuddyManager;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.data.objects.UserDetails;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.data.wombatrescue.objects.NumDaysInterval;
import org.strandz.data.wombatrescue.objects.Override;
import org.strandz.data.wombatrescue.objects.RosterSlot;
import org.strandz.data.wombatrescue.objects.Seniority;
import org.strandz.data.wombatrescue.objects.Sex;
import org.strandz.data.wombatrescue.objects.WeekInMonth;
import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.data.wombatrescue.objects.SexI;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Print;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * The natural place for this class would be in the task package
 * however we don't want store.wombatrescue depending back on a task
 * package, and there is just no need to go to the complication of
 * using interfaces to solve this issue.
 *
 * After running tests will give a two nulls problem
 *
 * mysql --user=root test 
mysql> select worker_id, BELONGS_TO_GROUP_WORKER_ID_OID from worker where DUMMY
= 1;
+-----------+--------------------------------+
| worker_id | BELONGS_TO_GROUP_WORKER_ID_OID |
+-----------+--------------------------------+
|         1 |                              2 |
|         2 |                           NULL |
+-----------+--------------------------------+
2 rows in set (0.00 sec)

mysql> update worker set belongs_to_group_worker_id_oid = 1 where worker_id = 1;

Query OK, 1 row affected (0.03 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> delete from worker where worker_id = 2;
Query OK, 1 row affected (0.03 sec) 
 */
public class WombatDemoData
{
    public List newWorkers = new ArrayList();
    public List newBMs = new ArrayList();
    public List newRSs = new ArrayList();
    public List newUsers = new ArrayList();
    public WombatLookups wombatLookups;
    
    private Worker jeanie; 
    private Worker lilia;
    private Worker alfred; 
    private Worker derek; 
    private Worker harry; 
    private Worker michael; 
    private Worker justin; 
    private Worker samir; 
    private Worker russell; 
    private Worker guy; 
    private Worker naba; 
    private Worker harvey; 
    private Worker dave; 
    private Worker con; 
    private Worker nullWorker;
    private Worker davesGroup; 
    private Worker billy; 
    private Worker erick;
    private Worker clayton;
    private Worker kurt;
    private Worker noreen;
    private Worker jenny;
    private Worker phillipa;

    private static WombatDemoData instance;
    
    private WombatDemoData()
    {
        
    }
    
    public static WombatDemoData getInstance()
    {
        WombatDemoData result;
        if(WombatDemoData.instance == null)
        {
            result = new WombatDemoData();
            result.wombatLookups = new WombatLookups();
            result.wombatLookups.initValues();
            result.doPopulation();
            WombatDemoData.instance = result;
        }
        else
        {
            result = WombatDemoData.instance;
        }
        return result;
    }

    public void doPopulation()
    {
        nullWorker = createNullWorker();
        newWorkers.add(nullWorker); //0

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
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.SECOND_OF_MONTH),
            davesGroup, (WhichShift) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.SECOND_OF_MONTH),
            davesGroup, (WhichShift) obtainLOV(WhichShift.OVERNIGHT));
        /**/
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIRST_OF_MONTH),
            billy, (WhichShift) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.THIRD_OF_MONTH),
            billy, (WhichShift) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIFTH_OF_MONTH),
            billy, (WhichShift) obtainLOV(WhichShift.OVERNIGHT));
        //To create a clash:
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.WEDNESDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.SECOND_OF_MONTH),
            billy, (WhichShift) obtainLOV(WhichShift.OVERNIGHT));
        
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIRST_OF_MONTH),
            erick, (WhichShift) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.THIRD_OF_MONTH),
            erick, (WhichShift) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FIFTH_OF_MONTH),
            erick, (WhichShift) obtainLOV(WhichShift.DINNER));
        rosterAParticularDayOfMonth((DayInWeek) obtainLOV(DayInWeek.SATURDAY),
            (WeekInMonth) obtainLOV(WeekInMonth.FOURTH_OF_MONTH),
            noreen, (WhichShift) obtainLOV(WhichShift.DINNER));
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

        DayInWeek mon = (DayInWeek) obtainLOV(DayInWeek.MONDAY);
        DayInWeek tue = (DayInWeek) obtainLOV(DayInWeek.TUESDAY);
        DayInWeek wed = (DayInWeek) obtainLOV(DayInWeek.WEDNESDAY);
        DayInWeek thu = (DayInWeek) obtainLOV(DayInWeek.THURSDAY);
        DayInWeek fri = (DayInWeek) obtainLOV(DayInWeek.FRIDAY);
        DayInWeek sat = (DayInWeek) obtainLOV(DayInWeek.SATURDAY);
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
        
        Worker groupOfOneWorker[] = new Worker[]{ naba};
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
    private void rosterAParticularDayOfMonth(DayInWeek dayInWeek, WeekInMonth weekInMonth, 
                                             Worker worker, WhichShift whichShift)
    {
        RosterSlot rs = createRosterSlot();
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
    private void rosterAWholeMonth(DayInWeek dayInWeek, Worker workers[])
    {
        NumDaysInterval weekly = (NumDaysInterval) obtainLOV(NumDaysInterval.WEEKLY);
        WhichShift dinnerShift = (WhichShift) obtainLOV(WhichShift.DINNER);
        WhichShift overnightShift = (WhichShift) obtainLOV(WhichShift.OVERNIGHT);
        for(int i = 0; i < workers.length; i++)
        {
            Worker worker = workers[i];
            //Err.pr( "Rostering " + worker + " for a whole month on " + dayInWeek);
            if(worker == russell && dayInWeek == DayInWeek.SATURDAY)
            {
                Print.prArray( workers, "Group of workers");
                Err.stack();
            }
        }
        if(workers.length > 0 && workers[0] != null)
        {
            RosterSlot rs = createRosterSlot();
            rs.setDayInWeek(dayInWeek);
            rs.setWhichShift(dinnerShift);
            rs.setNumDaysInterval(weekly);
            //rs.setWorker( workers[0]);
            workers[0].addRosterSlot(rs);
            newRSs.add(rs);
        }
        if(workers.length > 1 && workers[1] != null)
        {
            RosterSlot rs = createRosterSlot();
            rs.setDayInWeek(dayInWeek);
            rs.setWhichShift(dinnerShift);
            rs.setNumDaysInterval(weekly);
            //rs.setWorker( workers[1]);
            workers[1].addRosterSlot(rs);
            newRSs.add(rs);
        }
        if(workers.length > 2 && workers[2] != null)
        {
            RosterSlot rs = createRosterSlot();
            rs.setDayInWeek(dayInWeek);
            rs.setWhichShift(overnightShift);
            rs.setNumDaysInterval(weekly);
            //rs.setWorker( workers[2]);
            workers[2].addRosterSlot(rs);
            newRSs.add(rs);
        }
        if(workers.length > 3 && workers[3] != null)
        {
            RosterSlot rs = createRosterSlot();
            rs.setDayInWeek(dayInWeek);
            rs.setWhichShift(overnightShift);
            rs.setNumDaysInterval(weekly);
            //rs.setWorker( workers[3]);
            workers[3].addRosterSlot(rs);
            newRSs.add(rs);
        }
    }
    
    private UserDetails createUser( String user, String pass)
    {
        UserDetails result = new UserDetails();
        result.setUsername( user);
        result.setPassword( pass);
        result.setDatabaseUsername( "root");
        return result;
    }

    /**
     * Initially set lookups to NULL to avoid NPEs.
     */
    private RosterSlot createRosterSlot()
    {
        RosterSlot result = new RosterSlot();
        result.setDayInWeek((DayInWeek) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_DAY_IN_WEEK), DayInWeek.NULL));
        result.setWhichShift((WhichShift) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_WHICH_SHIFT), WhichShift.NULL));
        result.setNumDaysInterval((NumDaysInterval) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_INTERVAL), NumDaysInterval.NULL));
        result.setWeekInMonth((WeekInMonth) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_WEEK_IN_MONTH), WeekInMonth.NULL));
        result.setOverridesOthers((Override) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_OVERRIDE), Override.NULL));
        result.setOnlyInMonth((MonthInYear) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR), MonthInYear.NULL));
        result.setNotInMonth((MonthInYear) Utils.getFromList(wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR), MonthInYear.NULL));
        return result;
    }
    
    private Worker createWorker(String name, String surname, Worker group)
    {
        return createWorker( name, surname, group, null, null);
    }

    private Worker createWorker(String name, String surname, Worker group, String groupName, String contactName)
    {
        Assert.isFalse(name == null && surname == null && group == null);
        Worker result = new Worker();
        result.setBelongsToGroup(group);
        result.setChristianName(name);
        result.setSurname(surname);
        result.setShiftPreference((WhichShift) obtainLOV(WhichShift.NULL));
        result.setSeniority((Seniority) obtainLOV(Seniority.EXPERIENCED));
        result.setSex((SexI) obtainLOV(Sex.NULL));
        result.setFlexibility((Flexibility) obtainLOV(Flexibility.FLEXIBLE));
        result.setEmail1(name + "." + surname + "@small-town-isp.com.au");
        result.setWorkPhone("9734 5500"); //everyone the same!
        result.setGroupName( groupName);
        result.setContactName( contactName);
        result.setComments( "comment for " + result.getToShort());
        return result;
    }

    private Worker createNullWorker()
    {
        Worker result = new Worker(); //Never use NULL again, always use isDummy()
        result.setDummy(true);
        return result;
    }

    private void workerDetailsEx1(Worker worker)
    {
        worker.setStreet("30 Heather Court");
        worker.setSuburb("Paradise");
        worker.setPostcode("");
        worker.setHomePhone("8276 5882");
        worker.setWorkPhone("8276 5882");
        worker.setMobilePhone("0402 456 895");
    }

    private void workerDetailsEx2(Worker worker)
    {
        worker.setStreet("17 Woodhouse Cresent");
        worker.setSuburb("Wattle Park");
        worker.setPostcode("");
        worker.setHomePhone("8276 5882");
        worker.setWorkPhone("8276 5882");
        worker.setMobilePhone("0402 456 895");
    }

    private void workerDetailsEx3(Worker worker)
    {
        worker.setStreet("6 Twilight Drive");
        worker.setSuburb("Happy Valley");
        worker.setPostcode("");
        worker.setHomePhone("8276 5882");
        worker.setWorkPhone("8276 5882");
        worker.setMobilePhone("0402 456 895");
    }

    private void workerDetailsNoPhone(Worker worker)
    {
        worker.setStreet("29 Amberly Road");
        worker.setSuburb("Loughton");
        worker.setPostcode("");
        worker.setHomePhone( null);
        worker.setWorkPhone( null);
        worker.setMobilePhone( null);
    }
    
    private BuddyManager createBuddyManager(Worker worker, DayInWeek dayInWeek, WhichShift whichshift)
    {
        BuddyManager result = null;
        if(worker == null)
        {
            Err.error("Cannot create a BMer without a worker");
        }
        result = new BuddyManager();
        result.setDayInWeek(dayInWeek);
        result.setWhichShift(whichshift);
        result.setWorker(worker);
        return result;
    }

    /**
     * Only exists to make calling code easier to read
     */
    Object obtainLOV(Object typeWant)
    {
        Object result = null;
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
        return result;
    }
}
