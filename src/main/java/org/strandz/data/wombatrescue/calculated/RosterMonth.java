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
package org.strandz.data.wombatrescue.calculated;

import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.data.wombatrescue.objects.Override;
import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.OverrideI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MsgSubstituteUtils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class RosterMonth implements Serializable
{
    /**
     * Used to convert server type DOs which will only have their pks into useable client DOs.
     * Set after this object arrives at the client.
     */
    private transient ClientObjProvider clientObjProvider;
    /**
     * Also set after this object arrives at the client. Provides a convenience variable
     * for particularShifts that came across stored in nights.
     */
    public transient List<ParticularShift> particularShifts;
    private MonthInYearI month;
    private int year;
    // Same info, different representation
    private List<Night> nights = new ArrayList<Night>();
    // Expect these 2 to come across the wire as well
    private List<Clash> clashes = new ArrayList<Clash>();
    private List<Night> failedNights = new ArrayList<Night>();
    private List<WorkerI> unrosteredAvailableWorkers = new ArrayList<WorkerI>();
    //
    private transient List<String> allocationNotes = new ArrayList<String>();

    private static final String SHIFT_ALREADY_HAS_VOL = "$ already has a volunteer";
    private static final String VOL_NOT_DO_OVERNIGHTS = "For $ no overnights allowed yet tried for $";
    private static final String VOL_NOT_DO_DINNERS = "For $ no evenings allowed yet tried for $";
    private static final String VOL_ON_HOLIDAY = "$ is on holiday on $";
    private static final String VOL_ALREADY_ROSTERED = "$ is already rostered on $";
    private static final String VOL_NOT_AVAILABLE = "$ is not available on $";

    private static int times;

    public static void main(String[] args)
    {
        int dateInt = 25;
        int monthInt = 8;
        int yearInt = 2004;
        GregorianCalendar day = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
        TimeUtils.clearCalendar(day);
        Err.pr("date using: " + dateInt);
        day.set(Calendar.DATE, dateInt);
        Err.pr("month using: " + monthInt);
        day.set(Calendar.MONTH, monthInt);
        Err.pr("year using: " + yearInt);
        day.set(Calendar.YEAR, yearInt);
        // With 1.5 get [Sat Sep 25 12:00:00 EST 2004]
        // With 1.4 got [Sat Sep 25 00:00:00 EST 2004]
        Err.pr("Got day: [" + day.getTime() + "]");
    }

    public RosterMonth(MonthInYearI month, int year)
    {
        if(month == null)
        {
            Err.error( "Cannot construct a NewMonth without a month param");
        }
        //TimeZone tz = TimeUtils.getDefaultTimeZone();
        // Err.pr( "Cal using b4 YEAR: " + pUtils.displayCalendar( cal));
        GregorianCalendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
        cal.set(Calendar.YEAR, year);
        // Err.pr( "Cal using b4 MONTH: " + pUtils.displayCalendar( cal));
        cal.set(Calendar.MONTH, month.getOrdinal());
        //This is required to get the actual maximum date - ajug had to help with this
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //Err.pr( "ordinal have given cal.set(): " + month.getOrdinal());
        //Err.pr( "Feb ordinal: " + Calendar.FEBRUARY);
        // Very weird but without this debug message was getting 30 days in August
        // rather than 31 as should have!
        Err.pr(WombatNote.GENERIC, "Cal using b4 clear: " + TimeUtils.displayCalendar(cal));
        /*
        Err.pr( "In Java JUL is " + Calendar.JULY);
        Err.pr( "In Java AUG is " + Calendar.AUGUST);
        Err.pr( "In Java SEP is " + Calendar.SEPTEMBER);
        Err.pr( "We asked for " + month.getOrdinal());
        Err.pr( "Using " + month);
        Err.pr( "And year " + year);
        */
        int numDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //Err.pr( "RosterMonth: Days in " + month + ": " + numDays);
        //Err.pr( "RosterMonth: Year " + year);
        TimeUtils.clearCalendar(cal);

        // Err.pr( "Cal using finally: " + pUtils.displayCalendar( cal));

        for(int i = 0; i < numDays; i++)
        {
            GregorianCalendar day = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
            TimeUtils.clearCalendar(day);
            //Err.pr( "date using: " + (i + 1));
            day.set(Calendar.DATE, i + 1);
            //Err.pr( "month using: " + month.getOrdinal());
            day.set(Calendar.MONTH, month.getOrdinal());
            //Err.pr( "year using: " + year);
            day.set(Calendar.YEAR, year);
            //Err.pr( "For day in error we asked for month " + cal.get( Calendar.MONTH));
            String rightTZChk = day.getTime().toString();
            if(rightTZChk.indexOf("CST") != -1 && rightTZChk.indexOf("EST") == -1)
            {
                //We should always be in Sydney (sometimes appear to be in Adelaide)
                Err.error("To construct a night, got day: " + day.getTime() + " with TZ: " + day.getTimeZone());
            }
            DayInWeek dayInWeek = DayInWeek.getDayInWeek( day.getTime());
            Night night = new Night(day.getTime());
            ParticularShift particShift = new ParticularShift(WhichShift.DINNER,
                                                              Utils.ONE, dayInWeek);
            night.setFirstEvening(particShift);
            particShift.setNight(night);
            particShift = new ParticularShift(WhichShift.DINNER, Utils.TWO, dayInWeek);
            night.setSecondEvening(particShift);
            particShift.setNight(night);
            particShift = new ParticularShift(WhichShift.DINNER, Utils.THREE, dayInWeek);
            night.setThirdEvening(particShift);
            particShift.setNight(night);
            particShift = new ParticularShift(WhichShift.OVERNIGHT, Utils.ONE, dayInWeek);
            night.setFirstOvernight(particShift);
            particShift.setNight(night);
            particShift = new ParticularShift(WhichShift.OVERNIGHT, Utils.TWO, dayInWeek);
            night.setSecondOvernight(particShift);
            particShift.setNight(night);
            nights.add(night);
        }
        this.month = month;
        this.year = year;
    }

    public void setupForClientUse(ClientObjProvider clientObjProvider)
    {
        this.clientObjProvider = clientObjProvider;
        particularShifts = new ArrayList<ParticularShift>();
        for (int i = 0; i < nights.size(); i++)
        {
            Night night = nights.get( i);
            particularShifts.add( night.getFirstEvening());
            particularShifts.add( night.getSecondEvening());
            particularShifts.add( night.getThirdEvening());
            particularShifts.add( night.getFirstOvernight());
            particularShifts.add( night.getSecondOvernight());
        }
    }

    List getGapNightsComingUp()
    {
        List result = new ArrayList();
        Date today = new Date();
        Date begin = DayInWeek.getBeginningDate(today);
        for(Iterator iterator = nights.iterator(); iterator.hasNext();)
        {
            Night night = (Night) iterator.next();
            Date shiftDate = night.getShiftDate();
            if(begin.before(shiftDate) || begin.equals(shiftDate))
            {
                if(night.isNotFullyComplete())
                {
                    result.add(night);
                }
            }
        }
        return result;
    }
    
    public List<Night> getNights()
    {
        return nights;
    }

    public List<Clash> getClashes()
    {
        return clashes;
    }
    
    public List<Night> getFailedNights()
    {
        return failedNights;
    }
    
    public List<WorkerI> getUnrosteredAvailableWorkers()
    {
        List<WorkerI> result = unrosteredAvailableWorkers;
        if(clientObjProvider != null)
        {
            result = clientObjProvider.getClientWorkers( unrosteredAvailableWorkers);
        }
        return result;
    }

    public void printRoster(RosterPrinterI printer, String notOpenSundayText, String closedText)
    {
        failedNights.clear();
        int i = 1;
        int week = 1;
        //Err.pr( WombatNote.badRosterFromTomcat, "Num of nights in " + this + " is " + nights.size());
        for(Iterator iterator = nights.iterator(); iterator.hasNext(); i++)
        {
            Night night = (Night) iterator.next();
            night.formWorkers();

            boolean ok = night.validate();
            if(!ok)
            {
                Err.error(night.retrieveValidateBeanMsg());
            }

            boolean incompleteNight = night.isNotComplete();
            if(incompleteNight)
            {
                DayInWeek day = night.getDayInWeek();
                if(day != DayInWeek.SUNDAY && day != DayInWeek.THURSDAY)
                {
                    failedNights.add(new Night(night));
                }
                night.unRosterAsNotComplete();
            }
            else
            {
                //Scrapped for now as unimportant and causing bug whereby first dinner
                // shifter is being put into the middle place, because NULL sorts before anything else
                //night.orderBySeniorityAndSex();
            }
            printer.pr("[" + week + "]" + night.formattedNight( notOpenSundayText, closedText));
            if(i == 7 || i == 14 || i == 21 || i == 28)
            {
                week++;
            }
        }
    }

    public void printClashes(RosterPrinterI rosterValue, String notOpenSundayText, String closedText)
    {
        if(!clashes.isEmpty())
        {
            rosterValue.pr("");
            //rosterValue.pr("CLASHES");
            rosterValue.pr("Clashed Nights");
            for(Iterator iterator = clashes.iterator(); iterator.hasNext();)
            {
                Clash clash = (Clash) iterator.next();
                rosterValue.pr("" + clash.formattedClash( notOpenSundayText, closedText));
            }
            rosterValue.pr("------------");
        }
    }

    public void printAllocationNotes(RosterPrinterI rosterValue)
    {
        if(!allocationNotes.isEmpty())
        {
            int i = 0;
            boolean printedSumfin = false;
            for(Iterator iterator = allocationNotes.iterator(); iterator.hasNext(); i++)
            {
                String note = (String) iterator.next();
                //We not so interested in these notes
                if(!MsgSubstituteUtils.conformsToMsg(VOL_ON_HOLIDAY, note) &&
                    !MsgSubstituteUtils.conformsToMsg(VOL_ALREADY_ROSTERED, note) &&
                    !MsgSubstituteUtils.conformsToMsg(SHIFT_ALREADY_HAS_VOL, note) &&
                    !MsgSubstituteUtils.conformsToMsg(VOL_NOT_AVAILABLE, note))
                {
                    if(i == 0)
                    {
                        rosterValue.pr("");
                        rosterValue.pr("ALLOCATION NOTES");
                        printedSumfin = true;
                    }
                    rosterValue.pr("" + note);
                }
            }
            if(printedSumfin)
            {
                rosterValue.pr("------------");
            }
        }
    }

    public void printFailedNights(RosterPrinterI rosterValue, String notOpenSundayText, String closedText)
    {
        if(!failedNights.isEmpty())
        {
            int i = 0;
            for(Iterator iterator = failedNights.iterator(); iterator.hasNext();)
            {
                Night night = (Night) iterator.next();
                //DayInWeek day = night.getDayInWeek();
                //S/not be failed nights in first place if on Sunday or Thursday
                //if(day != DayInWeek.SUNDAY && day != DayInWeek.THURSDAY)
                {
                    if(i == 0)
                    {
                        rosterValue.pr("");
                        //Assert.notBlank( nightsClosedText);
                        //rosterValue.pr( nightsClosedText);
                        rosterValue.pr( "Unfilled Nights");
                    }
                    rosterValue.pr("" + night.formattedNight( notOpenSundayText, closedText));
                    i++;
                }
            }
            if(i > 0)
            {
                rosterValue.pr("------------");
            }
        }
    }
    
    public void printUnrosteredWorkers(RosterPrinterI textKeeper, MonthTransferObj monthTO)
    {
        textKeeper.pr("");
        textKeeper.pr("Unrostered Workers");

        if(unrosteredAvailableWorkers.isEmpty())
        {
            Err.alarm("Unusual to have no unrostered workers");
        }

        Date first = new Date();
        Date last = new Date();
        monthTO.obtainFirstLastDay(first, last, false);
        for(Iterator iterator = unrosteredAvailableWorkers.iterator(); iterator.hasNext();)
        {
            WorkerI vol = (WorkerI) iterator.next();
            if(vol.isAvailable( first, last))
            {
                String formatted = vol.formatWithHolidays(first, last);
                textKeeper.pr("" + formatted);
            }
        }
        textKeeper.pr("------------");
    }

    public List getRosteredThisMonthVols()
    {
        List result = new ArrayList();
        List list = getShiftsFromNights();
        for (int i = 0; i < list.size(); i++)
        {
            ParticularShift particShift = (ParticularShift) list.get(i);
            WorkerI workerOnShift = particShift.getWorker();
            if(workerOnShift != null)
            {
                Assert.isFalse( workerOnShift.isUnknown(), "particularShiftsHolder contains an unknown: " + workerOnShift);
                if(!result.contains(workerOnShift))
                {
                    result.add(workerOnShift);
                }
            }
        }
        return result;
    }

    /**
     * When trying to assign a vol to a particular shift, there
     * must be two distinct volunteers for either evening or
     * overnight.
     * If we are rostering a group then we can do both shifts with
     * that group.
     */
    private boolean alreadyOn(Night night, WorkerI worker, ParticularShift particShift)
    {
        boolean result = false;
        if(worker.getGroupName() == null)
        {
            if(particShift.getWhichShift().equals(WhichShift.DINNER))
            {
                if(night.isAlreadyOnEvening(worker))
                {
                    result = true;
                }
            }
            else // WhichShift.OVERNIGHT
            {
                if(night.isAlreadyOnOvernight(worker))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    /*
    * Objective is to set a volunteer to needsVolShift
    */
    private String allocate(RosterSlotI slot, ParticularShift needsVolShift)
    {
        String result = null;
        /**/
        times++;
        // Err.pr( "***" + times + "/ Allocating " + slot.getWorker() + " for " + needsVolShift.getNight().getShiftDate());
        // if(times == 72 || times == 73 || times == 74)
        // {
        // Err.debug();
        // }
        /*
        if(slot.getWorker().toString().indexOf( "Russell Hinze") != -1)
        {
            Err.pr(
                    "***" + times + "/ Allocating " + slot.getWorker() + " for "
                            + needsVolShift.getNight().getShiftDate());
            if(needsVolShift.getNight().getShiftDate().toString().contains( "Fri Feb 13"))
            {
                Err.debug();
            }
            if(!slot.getUnavailableDates().isEmpty())
            {
                Err.pr("unavailableDates[0]: " + slot.getUnavailableDates().get(0));
            }
            Err.pr(
                    "needsVolShift.getNight().getShiftDate(): "
                            + needsVolShift.getNight().getShiftDate());
        }
        */
        WorkerI workerOnSlot = slot.getWorker();
        Assert.notNull( workerOnSlot, "slot.getWorker()");
        Assert.isFalse( workerOnSlot.isUnknown(), "workerOnSlot is unknown: " + workerOnSlot);
        if(!slot.getUnavailableDates().contains(needsVolShift.getNight().getShiftDate()))
        {
            if(!alreadyOn(needsVolShift.getNight(), slot.getWorker(),
                          needsVolShift))
            {
                if(!workerOnSlot.onHoliday(needsVolShift.getNight().getShiftDate()))
                {
                    Assert.notNull( workerOnSlot.getFlexibility(), "slot.getWorker().getFlexibility()");
                    if(!(workerOnSlot.getFlexibility().equals(Flexibility.NO_EVENINGS) &&
                        needsVolShift.getWhichShift().equals(WhichShift.DINNER)))
                    {
                        if(!(workerOnSlot.getFlexibility().equals(Flexibility.NO_OVERNIGHTS) && needsVolShift.getWhichShift().equals(WhichShift.OVERNIGHT)))
                        {
                            if(needsVolShift.getWorker() == null)
                            {
                                needsVolShift.allocate(workerOnSlot, slot);

                                // Print.pr( slot + " has been accepted into " + needsVolShift);
                                OverrideI override = slot.getOverridesOthers();
                                if(override == null)
                                {
                                    Err.error(slot + " has a null override");
                                }
                                needsVolShift.setBump(override); // so won't be bumped by others
                            }
                            else
                            {
                                WorkerI establishedWorker = needsVolShift.getWorker();
                                Assert.isFalse( establishedWorker.isUnknown(), "establishedWorker is unknown: " + workerOnSlot);
                                result = MsgSubstituteUtils.formMsg(SHIFT_ALREADY_HAS_VOL, needsVolShift);
                                /*
                                * If the slot overrides the others then it gets into a
                                * particular shift. Whatever volunteer was there before
                                * is marked as a bumped clash. Of course you can't bump
                                * a bump, and normal rules apply for trying to do so
                                * into the third spot.
                                */
                                if((needsVolShift.isLastDinner() || needsVolShift.isLastOvernight())
                                    && needsVolShift.getBump().compareTo(slot.getOverridesOthers())
                                    > 0)
                                {
                                    if(slot.getOverridesOthers().equals(Override.NULL)
                                        && needsVolShift.getBump().equals(Override.NULL))
                                    {
                                        Err.error();
                                    }
                                    Err.pr(slot.getOverridesOthers() + " greater than "
                                        + needsVolShift.getBump());

                                    Clash clash = new Clash(workerOnSlot + "'s slot overrides other people's slot");
                                    clash.tryWorker = workerOnSlot;
                                    clash.establishedWorker = establishedWorker;
                                    clash.night = needsVolShift.getNight();
                                    clash.bump = true;
                                    clashes.add(clash);
                                    needsVolShift.setWorker(null);
                                    needsVolShift.setWorker(workerOnSlot);
                                    needsVolShift.setBump(slot.getOverridesOthers());
                                    result = null;
                                }
                                else if(needsVolShift.isLastDinner())
                                {
                                    Clash clash = new Clash("already have three dinner workers");
                                    clash.tryWorker = workerOnSlot;
                                    clash.establishedWorker = establishedWorker;
                                    clash.night = needsVolShift.getNight();
                                    clashes.add(clash);
                                }
                                else if(needsVolShift.isLastOvernight())
                                {
                                    Clash clash = new Clash("already have two overnight workers");
                                    clash.tryWorker = workerOnSlot;
                                    clash.establishedWorker = establishedWorker;
                                    clash.night = needsVolShift.getNight();
                                    clashes.add(clash);
                                }
                            }
                        }
                        else
                        {
                            String args[] = {workerOnSlot.toString(), needsVolShift.getNight().getShiftDate().toString()};
                            result = MsgSubstituteUtils.formMsg(VOL_NOT_DO_OVERNIGHTS, args);
                        }
                    }
                    else
                    {
                        String args[] = {workerOnSlot.toString(), needsVolShift.getNight().getShiftDate().toString()};
                        result = MsgSubstituteUtils.formMsg(VOL_NOT_DO_DINNERS, args);
                    }
                }
                else
                {
                    String args[] = {workerOnSlot.toString(), needsVolShift.getNight().getShiftDate().toString()};
                    result = MsgSubstituteUtils.formMsg(VOL_ON_HOLIDAY, args);
                }
            }
            else
            {
                String args[] = {workerOnSlot.toString(), needsVolShift.getNight().getShiftDate().toString()};
                result = MsgSubstituteUtils.formMsg(VOL_ALREADY_ROSTERED, args);
            }
        }
        else
        {
            String args[] = {workerOnSlot.toString(), needsVolShift.getNight().getShiftDate().toString()};
            result = MsgSubstituteUtils.formMsg(VOL_NOT_AVAILABLE, args);
        }
        /*
        String first = "Emma";
        String second = "O'Conner";
        if(slot.getWorker().getChristianName().equals( first) &&
        slot.getWorker().getSurname().equals( second))
        {
        if(reasonNot != null)
        {
        Err.pr( first + " " + second + " not on because " + reasonNot + " " + needsVolShift);
        }
        else
        {
        Err.pr( first + " " + second + " been put onto " + needsVolShift);
        }
        }
        */
        if(result != null)
        {
            allocationNotes.add(result);
        }
        return result;
    }

    /**
     * Some slots represent an unavailable date. For these all
     * the other slots of the worker in question get to know
     * about this date.
     */
    private void prepareUnavailables(List rosterSlots)
    {
        for(Iterator iter = rosterSlots.iterator(); iter.hasNext();)
        {
            RosterSlotI notAvailSlot = (RosterSlotI) iter.next();
            if(notAvailSlot.isNotAvailable())
            {
                WorkerI vol = notAvailSlot.getWorker();
                Date date = notAvailSlot.getSpecificDate();
                if(date == null)
                {
                    Err.error(vol + " has a \'Not available\' roster slot without a date");
                }
                //Err.pr(
                //    vol + " is not available on " + pTimeUtils.dateFormatter.format( date));
                List slotsOfVol = vol.getRosterslots(rosterSlots);
                for(Iterator iter1 = slotsOfVol.iterator(); iter1.hasNext();)
                {
                    RosterSlotI volSlot = (RosterSlotI) iter1.next();
                    if(!volSlot.isNotAvailable())
                    {
                        volSlot.getUnavailableDates().add(date);
                    }
                }
            }
        }
    }

    /**
     * Used on the server
     */
    private transient List shiftsFromNights;
    private List<ParticularShift> getShiftsFromNights()
    {
        List<ParticularShift> result;
        if(shiftsFromNights == null)
        {
            shiftsFromNights = new ArrayList<ParticularShift>();
            for (Iterator<Night> iterator = nights.iterator(); iterator.hasNext();)
            {
                Night night = iterator.next();
                shiftsFromNights.add( night.getFirstEvening());
                shiftsFromNights.add( night.getSecondEvening());
                shiftsFromNights.add( night.getThirdEvening());
                shiftsFromNights.add( night.getFirstOvernight());
                shiftsFromNights.add( night.getSecondOvernight());
            }
            result = shiftsFromNights;
        }
        else
        {
            result = shiftsFromNights;
        }
        return result;
    }

    public void allocateRosterSlots(List rosterSlots, WorkerI dummyWorker)
    {
        prepareUnavailables(rosterSlots);
        for(Iterator iter = rosterSlots.iterator(); iter.hasNext();)
        {
            RosterSlotI slot = (RosterSlotI) iter.next();
            boolean useThisSlot = true;
            if(!slot.getNotInMonth().equals(MonthInYear.NULL)
                && !slot.getOnlyInMonth().equals(MonthInYear.NULL))
            {
                Err.pr("Not in month " + slot.getNotInMonth());
                Err.pr("Only in month " + slot.getOnlyInMonth());
                Err.error(slot.getWorker()
                    + " has a bad slot (both not and only for month)");
            }
            Assert.notNull( getMonth());
            if(Utils.isBlank( slot.getWhichShift().getName()))
            {
                Assert.isTrue( slot.getWhichShift().equals( WhichShift.NULL));
                if(!slot.isNotAvailable())
                {
                    Err.error( "WhichShift should have already been validated as not blank for " +
                        slot.getWorker().getToLong());
                }
            }
            if(getMonth().equals(MonthInYear.NULL))
            {
                Err.error("Did not expect to be allocating roster slots for the NULL month");
            }
            if(getMonth().equals(slot.getNotInMonth()))
            {
                useThisSlot = false;
            }
            if(!slot.getOnlyInMonth().equals(MonthInYear.NULL)
                && !getMonth().equals(slot.getOnlyInMonth()))
            {
                useThisSlot = false;
            }
            if(useThisSlot)
            {
                if(slot.getDayInWeek() == null)
                {
                    Err.error("DayInWeek: " + slot.getDayInWeek() + " for "
                        + slot.getWorker());
                }
                if(!slot.isDisabled() && !slot.getWorker().isUnknown()
                    && !slot.isNotAvailable())
                {
                    boolean debugThisIter;
                    List list = getShiftsFromNights();
                    for (int i = 0; i < list.size(); i++)
                    {
                        debugThisIter = false;
                        ParticularShift needsVolShift = (ParticularShift) list.get(i);
                        if(slot.getSpecificDate() != null)
                        {
                            if(WombatNote.PARTIC_DATE_SLOTS_NOT_ROSTERED.isVisible() &&
                                    slot.getSpecificDate().toString().indexOf("Sat 09") != -1)
                            {
                                Err.pr(WombatNote.PARTIC_DATE_SLOTS_NOT_ROSTERED, "SPEC DATE: " + slot.getSpecificDate() + " for " + slot.getWorker());
                                needsVolShift.debugging = true;
                            }
                            if(needsVolShift.canAcceptSpecificDate(slot))
                            {
                                allocate(slot, needsVolShift);
                            }
                            needsVolShift.debugging = false;
                        }
                        else if(slot.isMonthlyRestart())
                        {
                            if(needsVolShift.canAcceptMonthlyRestart(slot))
                            {
                                /*
                                if(slot.toString().equals( "Monday, [first of month], dinner-139"))
                                {
                                    Err.debug();
                                }
                                Err.pr( "To allocate (monthly restart) <" + slot + "> to <" + needsVolShift + ">");
                                */
                                allocate(slot, needsVolShift);
                            }
                        }
                        else
                        {
                            if(needsVolShift.canAcceptContinuing(slot, debugThisIter))
                            {
                                // Err.pr( "To allocate (continuing) " + slot + " to " + needsVolShift);
                                allocate(slot, needsVolShift);
                            }
                        }
                        // Print.pr( slot + " has so far not been accepted into " + particShift);
                    }
                }
            }
            else
            {// Err.pr( "Are not using slot " + slot);
            }
        }
        fillInBlanks( dummyWorker);
    }

    private void fillInBlanks( WorkerI dummyWorker)
    {
        List list = getShiftsFromNights();
        for (int i = 0; i < list.size(); i++)
        {
            ParticularShift particShift = (ParticularShift) list.get(i);
            if(particShift.getWorker() == null)
            {
                particShift.setWorker( dummyWorker);
            }
        }
    }

    public MonthInYearI getMonth()
    {
        return month;
    }

    public int getYear()
    {
        return year;
    }

    public ClientObjProvider getClientObjProvider()
    {
        if(clientObjProvider == null)
        {
            clientObjProvider = new ClientObjProvider();
        }
        return clientObjProvider;
    }

    public String toString()
    {
        return month.toString();
    }
}
