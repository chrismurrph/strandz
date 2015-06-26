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

// import java.io.File;

import org.strandz.data.wombatrescue.objects.Volunteer;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.store.wombatrescue.WombatConnections;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import javax.jdo.PersistenceManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Refill
{
    private static Volunteer joeZammit;
    private static Volunteer martinLivingston;
    private static Volunteer gerrardsGroup;
    private static Volunteer mikeDaughton;
    private static Volunteer seanMullin;
    private static Volunteer clareDeJongh;
    private static DataStore dataStore;
    private static final Integer ONE = new Integer(1);
    private static final Integer TWO = new Integer(2);
    private static boolean useCaptureFile = true;
    /*
    private static Integer ONE
    {
    return new Integer( 1);
    }
    private static Integer TWO
    {
    return new Integer( 2);
    }
    */

    private static PersistenceManager pm;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"POJOWombatData", WombatConnections.DEFAULT_DATABASE.getName()};
            processParams(str);
        }
    }

    public static DataStore getData()
    {
        if(dataStore == null)
        {
            String str[] = {};
            main(str);
        }
        return dataStore;
    }

    /**
     * Like removing all the records of a table but
     * leaving the table there!
     */
    private static void processParams(String s[])
    {
         /**/
        if(s[0].equals("POJOWombatData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database to refill");
            }
            if(WombatConnectionEnum.getFromName(s[1]).isProduction())
            {
                Err.error(
                    "Cannot work with the " + WombatConnectionEnum.getFromName(s[1]) + " database");
            }
            if(s.length == 2)
            {
                DataStoreFactory factory = new WombatDataStoreFactory();
                factory.addConnection(WombatConnectionEnum.getFromName(s[1]));
                dataStore = factory.getDataStore();
                //dataStore = WombatDataFactory.getNewInstance( WombatConnectionEnum.getFromName( s[1]));
            }
            else
            {
                Err.error();
            }
            // Err.error( "Use running of a capture file and scrap this!");
            if(!useCaptureFile)
            {
                dataStore.startTx();

                List vols = getVolunteerData();
                // List rosterSlots = getRosterSlotData();
                // List shifts = getParticularShiftData();
                Print.pr("************************************");
                Print.pr(vols.size() + " volunteers (Volunteers)");
                // Print.pr( rosterSlots.size() + " rosterSlots (rosterSlots)");
                // Print.pr( shifts.size() + " shifts (ParticularShift)");
                Print.pr("************************************");
                dataStore.set(POJOWombatData.WORKER, vols);
                // dataStore.getData().set( POJOWombatData.ROSTER_SLOT, rosterSlots); //must be after volunteers
                // dataStore.getData().set( POJOWombatData.PARTICULAR_SHIFT, shifts);
                dataStore.commitTx();
            }
            else
            {
                replay();
            }
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void replay()
    {
        // ? Such a big deal?
        // Err.error( "Need work, o/wise data w/be dependent on applic");
        PlayParticularAgain.replay(new File(PlayParticularAgain.REFILL), true);
    }

    private static List getVolunteerData()
    {
        ArrayList volunteers = new ArrayList();
        {
            Volunteer v;
            v = new Volunteer();
            v.setChristianName("Joe");
            v.setSurname("Zammit");
            // v.setHolidays( getJoeHolidayData());
            joeZammit = v;
            volunteers.add(v);
            v = new Volunteer();
            v.setChristianName("Martin");
            v.setSurname("Livingston");
            martinLivingston = v;
            volunteers.add(v);
            /*
            gerrardsGroup = new Volunteer();
            gerrardsGroup.setGroupName( "Gerrard's group");
            volunteers.add( gerrardsGroup);

            v = new Volunteer();
            v.setChristianName( "Gerrard");
            v.setSurname( "Bromley");
            v.setBelongsToGroup( gerrardsGroup);
            v.setGroupContactPerson( true);
            volunteers.add( v);
            */

            v = new Volunteer();
            v.setChristianName("Mary-Ellen");
            v.setSurname("Mcleod");
            // v.setBelongsToGroup( gerrardsGroup);
            volunteers.add(v);
            v = new Volunteer();
            v.setChristianName("Mike");
            v.setSurname("Daughton");
            mikeDaughton = v;
            volunteers.add(v);
            v = new Volunteer();
            v.setChristianName("Sean");
            v.setSurname("Mullin");
            seanMullin = v;
            volunteers.add(v);
            v = new Volunteer();
            v.setChristianName("Mario");
            volunteers.add(v);
        }
        return volunteers;
    }
    /*
    private static List getRosterSlotData()
    {
    ArrayList rosterSlots = new ArrayList();
    {
    RosterSlot rs;
    rs = new RosterSlot();
    rs.setVolunteer( martinLivingston);
    rs.setWorkerOrdinal( ONE);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.FRIDAY);
    rs.setWeekInMonth( WeekInMonth.SECOND_OF_MONTH);
    rs.setWhichShift( WhichShift.OVERNIGHT);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( joeZammit);
    rs.setWorkerOrdinal( TWO);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.FRIDAY);
    rs.setWeekInMonth( WeekInMonth.SECOND_OF_MONTH);
    rs.setWhichShift( WhichShift.OVERNIGHT);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( martinLivingston);
    rs.setWorkerOrdinal( ONE);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.FRIDAY);
    rs.setWeekInMonth( WeekInMonth.THIRD_OF_MONTH);
    rs.setWhichShift( WhichShift.OVERNIGHT);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( joeZammit);
    rs.setWorkerOrdinal( TWO);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.FRIDAY);
    rs.setWeekInMonth( WeekInMonth.THIRD_OF_MONTH);
    rs.setWhichShift( WhichShift.OVERNIGHT);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( gerrardsGroup);
    rs.setWorkerOrdinal( ONE);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.TUESDAY);
    rs.setWeekInMonth( WeekInMonth.SECOND_OF_MONTH);
    rs.setWhichShift( WhichShift.EVENING);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( gerrardsGroup);
    rs.setWorkerOrdinal( TWO);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.TUESDAY);
    rs.setWeekInMonth( WeekInMonth.SECOND_OF_MONTH);
    rs.setWhichShift( WhichShift.EVENING);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( gerrardsGroup);
    rs.setWorkerOrdinal( ONE);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.TUESDAY);
    rs.setWeekInMonth( WeekInMonth.SECOND_OF_MONTH);
    rs.setWhichShift( WhichShift.OVERNIGHT);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( gerrardsGroup);
    rs.setWorkerOrdinal( TWO);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.TUESDAY);
    rs.setWeekInMonth( WeekInMonth.SECOND_OF_MONTH);
    rs.setWhichShift( WhichShift.OVERNIGHT);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( gerrardsGroup);
    rs.setWorkerOrdinal( ONE);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.TUESDAY);
    rs.setWeekInMonth( WeekInMonth.FOURTH_OF_MONTH);
    rs.setWhichShift( WhichShift.EVENING);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( gerrardsGroup);
    rs.setWorkerOrdinal( TWO);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.TUESDAY);
    rs.setWeekInMonth( WeekInMonth.FOURTH_OF_MONTH);
    rs.setWhichShift( WhichShift.EVENING);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( gerrardsGroup);
    rs.setWorkerOrdinal( ONE);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.TUESDAY);
    rs.setWeekInMonth( WeekInMonth.FOURTH_OF_MONTH);
    rs.setWhichShift( WhichShift.OVERNIGHT);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( gerrardsGroup);
    rs.setWorkerOrdinal( TWO);
    rs.setMonthlyRestart( true);
    rs.setDayInWeek( DayInWeek.TUESDAY);
    rs.setWeekInMonth( WeekInMonth.FOURTH_OF_MONTH);
    rs.setWhichShift( WhichShift.OVERNIGHT);
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( clareDeJongh);
    rs.setWorkerOrdinal( ONE);
    rs.setMonthlyRestart( false);
    rs.setDayInWeek( DayInWeek.WEDNESDAY);
    rs.setInterval( Interval.FORTNIGHTLY);
    rs.setStartDate( pUtils.getDateFromString( "26/06/2002"));
    rosterSlots.add( rs);

    rs = new RosterSlot();
    rs.setVolunteer( mikeDaughton);
    rs.setWorkerOrdinal( TWO);
    rs.setMonthlyRestart( false);
    rs.setDayInWeek( DayInWeek.WEDNESDAY);
    rs.setInterval( Interval.FORTNIGHTLY);
    rs.setStartDate( pUtils.getDateFromString( "26/06/2002"));
    rs.setWhichShift( WhichShift.OVERNIGHT);
    rosterSlots.add( rs);
    }
    return rosterSlots;
    }
    */

    /*
    private static List getParticularShiftData()
    {
    ArrayList shifts = new ArrayList();
    {
    Date date = null;

    date = pUtils.getDateFromString( "01/08/2002");
    ParticularShift s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "01/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "01/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "01/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);


    date = pUtils.getDateFromString( "02/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "02/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "02/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "02/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);


    date = pUtils.getDateFromString( "03/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "03/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "03/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "03/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);


    date = pUtils.getDateFromString( "04/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "04/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "04/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "04/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);


    date = pUtils.getDateFromString( "05/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "05/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "05/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "05/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);


    date = pUtils.getDateFromString( "06/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "06/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "06/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "06/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);


    date = pUtils.getDateFromString( "07/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "07/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "07/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "07/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);


    date = pUtils.getDateFromString( "08/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "08/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "08/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "08/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "09/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "09/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "09/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "09/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);


    date = pUtils.getDateFromString( "10/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "10/08/2002");
    s = new ParticularShift( date, WhichShift.EVENING, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);

    date = pUtils.getDateFromString( "10/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, ONE);
    //s.setVolunteer( joeZammit);
    shifts.add( s);

    date = pUtils.getDateFromString( "10/08/2002");
    s = new ParticularShift( date, WhichShift.OVERNIGHT, TWO);
    //s.setVolunteer( gerrardsGroup);
    shifts.add( s);
    }
    return shifts;
    }
    */

}
