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

import org.strandz.data.wombatrescue.objects.NumDaysInterval;
import org.strandz.data.wombatrescue.objects.Override;
import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.NumDaysIntervalI;
import org.strandz.data.wombatrescue.objects.OverrideI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.data.wombatrescue.objects.BuddyManagerI;
import org.strandz.data.wombatrescue.objects.Seniority;
import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.data.objects.DayInWeek;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class ParticularShift implements Comparable, Serializable
{
    // PK
    private WhichShift whichShift;
    private Integer workerOrdinal;
    //
    private DayInWeek dayInWeek;
    //
    private WorkerI worker;
    private RosterSlotI rosterSlot;
    private Night night;
    private OverrideI bump = Override.NULL;
    private boolean failed;
    private transient ClientObjProvider clientObjProvider;
    private transient GregorianCalendar targetCalendar = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
    private transient GregorianCalendar calendar = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
    private transient static int constructedTimes;
    public transient int id;
    public transient boolean debugging;

    /**
     * The strange worker gets created as part of looking up which dynamically
     * creates objects as needed. Happens because workers are looked up from
     * ParticularShift when they are displayed. Set to true now using Cayenne
     */
    public static final boolean STRANGE_WORKER_NOT_ALLOWED = true;

    public ParticularShift(WhichShift shift, Integer ordinal, DayInWeek dayInWeek)
    {
        this();
        if (shift == null)
        {
            Err.error("shift == null");
        }
        setWhichShift(shift);
        setWorkerOrdinal(ordinal);
        setDayInWeek(dayInWeek);
    }

    /**
     * Used for ease of client processing, so that the DOs each ParticularShift needs to make
     * entity managed can be made so cached access to queries - so for example the query for
     * matching a worker will not be done once for every single ParticularShift, but once for
     * all of them
     *
     * @param clientObjProvider
     *
     */
    public void setClientObjProvider(ClientObjProvider clientObjProvider)
    {
        this.clientObjProvider = clientObjProvider;
    }

    /**
     * Get who is responsible for this shift using using the day/shift that it is for
     */
    public WorkerI getBuddyManager(List<BuddyManagerI> buddyManagers)
    {
        WorkerI result = null;
        Err.pr(WombatNote.BAD_ALLOCATION_BM_EMAILS, "dayInWeek: " + night.getDayInWeek());
        //Are not entity managed but this s/be ok as equals() is defined for all the lookup DOs
        //SdzEMAssert.isEntityManaged( night.getDayInWeek());
        //result = shiftManagers.getBuddy( whichShift, night.getDayInWeek());
        if (buddyManagers.size() != 12)
        {
            Err.error("Expect to always have 12 BM rows, got " + buddyManagers.size());
        }
        for (Iterator iterator = buddyManagers.iterator(); iterator.hasNext();)
        {
            BuddyManagerI buddyManager = (BuddyManagerI) iterator.next();
            //Err.pr( WombatNote.badAllocationBMEmails.isVisible(), buddyManager.getWorker() + ", whichShift: " + buddyManager.getWhichShift() + ", dayInWeek: "+ buddyManager.getDayInWeek());
            if (buddyManager.getWhichShift().getName().equals(whichShift.getName()) &&
                buddyManager.getDayInWeek().getName().equals(night.getDayInWeek().getName()))
            {
                result = buddyManager.getWorker();
            }
        }
        if (result == null)
        {
            String msg = "Could not find a BM for " + whichShift + " and " + night.getDayInWeek();
            Err.error(msg);
            //new MessageDlg( msg);
        }
        Err.pr(WombatNote.BAD_ALLOCATION_BM_EMAILS, "dayInWeek: " + night.getDayInWeek());
        return result;
    }

    public void setNight(Night night)
    {
        this.night = night;
    }

    public ParticularShift()
    {
        super();
        constructedTimes++;
        id = constructedTimes;
        /*
        Err.pr( "$$$ Constructed ParticularShift" + id);
        if(id == 0)
        {
        Err.stack();
        }
        */
    }

    public Date getShiftDate()
    {
        return night.getShiftDate();
    }

    public int compareTo(Object obj)
    {
        int result = 0;
        ParticularShift other = (ParticularShift) obj;
        Date otherDate = other.getShiftDate();
        Date thisDate = getShiftDate();
        result = thisDate.compareTo(otherDate);
        if (result == 0)
        {
            WhichShift otherShift = other.getWhichShift();
            WhichShift thisShift = getWhichShift();
            // result = thisShift.compareTo( otherShift);
            if (otherShift == thisShift)
            {
                result = 0;
            }
            else
            {
                result = -1;
            }
            if (result == 0)
            {
                Integer otherVolunteerOrdinal = other.getWorkerOrdinal();
                Integer thisVolunteerOrdinal = getWorkerOrdinal();
                /*
                if(thisVolunteerOrdinal.compareTo( otherVolunteerOrdinal) < 0)
                {
                result = -1;
                }
                else if(thisVolunteerOrdinal.compareTo( otherVolunteerOrdinal) > 0)
                {
                result = 1;
                }
                */
                result = thisVolunteerOrdinal.compareTo(otherVolunteerOrdinal);
            }
        }
        return result;
    }

    public boolean isLastDinner()
    {
        boolean result = getWhichShift().equals(WhichShift.DINNER)
            && getWorkerOrdinal().equals(Utils.THREE);
        return result;
    }

    public boolean isLastOvernight()
    {
        boolean result = getWhichShift().equals(WhichShift.OVERNIGHT)
            && getWorkerOrdinal().equals(Utils.TWO);
        return result;
    }

    private void pr(String msg)
    {
        if (debugging)
        {
            Err.pr(msg);
        }
    }

    public boolean canAcceptSpecificDate(RosterSlotI rosterSlot)
    {
        boolean result = false;
        if (whichShift.equals(rosterSlot.getWhichShift()))
        {
            if (rosterSlot.getSpecificDate().equals(night.getShiftDate()))
            {
                result = true;
            }
            else
            {
                if (night.getShiftDate().toString().indexOf("Sat Dec 09") != -1)
                {
                    pr(
                        "Got a " + rosterSlot.getSpecificDate() + " when needed a "
                            + night.getShiftDate());
                    /*
                    TimeZone tz = TimeZone.getTimeZone( "Australia/Adelaide");
                    //TimeZone tz = TimeZone.getDefault();
                    //Err.pr( "TZ using is " + tz);
                    Calendar cal = new GregorianCalendar( tz);
                    cal.setTime( rosterSlot.getSpecificDate());
                    //pUtils.clearZone( cal);
                    Date clearedDate = cal.getTime();
                    pr(
                        "Could GET A " + clearedDate + " when needed a " + night.getShiftDate().getTime());
                    */
                    pr(
                        "GOT A " + rosterSlot.getSpecificDate().getTime()
                            + " when needed a " + night.getShiftDate().getTime());
                }
            }
        }
        else
        {// pr( "Got a " + rosterSlot.getWhichShift() + " when needed a " + whichShift);
        }
        return result;
    }

    public boolean canAcceptMonthlyRestart(RosterSlotI rosterSlot)
    {
        boolean result = false;
        if (whichShift.equals(rosterSlot.getWhichShift()))
        {
            if (night.getDayInWeek().equals(rosterSlot.getDayInWeek()))
            {
                if (night.getWeekInMonth().equals(rosterSlot.getWeekInMonth()))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * From startDate of the rosterSlot, come forward in interval
     * time periods
     */
    public boolean canAcceptContinuing(RosterSlotI rosterSlot, boolean debug)
    {
        boolean result = false;
        if (whichShift.equals(rosterSlot.getWhichShift()))
        {
            if (night.getDayInWeek().equals(rosterSlot.getDayInWeek()))
            {
                if (rosterSlot.getStartDate() == null)
                {
                    if (!rosterSlot.getNumDaysInterval().equals(NumDaysInterval.WEEKLY))
                    {
                        Err.error(
                            "rosterSlot.getStartDate() == null for "
                                + rosterSlot.getWorker() + " on "
                                + rosterSlot.getDayInWeek());
                    }
                    else
                    {
                        result = true;
                    }
                }
                else if (dateSyncedWithInterval(getShiftDate(),
                    rosterSlot.getStartDate(), rosterSlot.getNumDaysInterval(), debug))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    private boolean dateSyncedWithInterval(Date targetDate,
                                           Date startDate,
                                           NumDaysIntervalI numDaysInterval,
                                           boolean debug)
    {
        boolean result = false;
        int days = numDaysInterval.getDays();
        calendar.setTime(startDate);
        targetCalendar.setTime(targetDate);

        int targetDay = targetCalendar.get(Calendar.DATE);
        int targetMonth = targetCalendar.get(Calendar.MONTH);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        boolean reachedTarget = false;
        while (!reachedTarget)
        {
            int startDay = calendar.get(Calendar.DATE);
            int startMonth = calendar.get(Calendar.MONTH);
            int startYear = calendar.get(Calendar.YEAR);
            if (startDay == targetDay && startMonth == targetMonth
                && startYear == targetYear)
            {
                result = true;
                reachedTarget = true;
            }
            else
            {
                if (calendar.after(targetCalendar))
                {
                    if (debug)
                    {
                        Err.pr(
                            "Have overstepped target (" + targetCalendar.getTime()
                                + "\n) when calendar reached " + calendar.getTime());
                    }
                    reachedTarget = true;
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, days);
        }
        if (debug)
        {
            Err.pr(
                "dateSyncedWithInterval() reting " + result + " for " + targetDate
                    + " when startdate was " + startDate);
        }
        return result;
    }

    public String toString()
    {
        String formattedDate = TimeUtils.DATE_FORMAT.format(getShiftDate());
        return getWhichShift() + " " + formattedDate + " " + getWorkerOrdinal();
    }

    /**
     * Returns the shift.
     *
     * @return int
     */
    public WhichShift getWhichShift()
    {
        return whichShift;
    }

    /**
     * Sets the shift.
     *
     * @param shift The shift to set
     */
    public void setWhichShift(WhichShift shift)
    {
        this.whichShift = shift;
    }

    public DayInWeek getDayInWeek()
    {
        return dayInWeek;
    }

    public void setDayInWeek(DayInWeek dayInWeek)
    {
        this.dayInWeek = dayInWeek;
    }

    /**
     * Returns the workerOrdinal.
     *
     * @return int
     */
    public Integer getWorkerOrdinal()
    {
        return workerOrdinal;
    }

    public WorkerI getWorker()
    {
        WorkerI result = worker;
        if(clientObjProvider != null)
        {
            result = clientObjProvider.getClientWorker( worker);
        }
        return result;
    }

    public RosterSlotI getRosterSlot()
    {
        RosterSlotI result = rosterSlot;
        if(clientObjProvider != null)
        {
            result = clientObjProvider.getClientRosterSlot( rosterSlot);
        }
        return result;
    }

    /**
     * Sets the worker.
     *
     * @param worker The volunteer to set
     */
    public void setWorker(WorkerI worker)
    {
        Assert.isFalse(worker != null && (STRANGE_WORKER_NOT_ALLOWED && worker.isStrange()));
        if (this.worker != null && worker != null) //ok to make worker null
        {
            /*
            * Two rules will be clashing.
            */
            Err.error("Already have a worker for " + this);
        }
        //Not always dealing with entity managed:
        //SdzEMAssert.isEntityManaged( worker, "setWorker()");
        if (JDONote.ASSIGN_PM.isVisible() && worker != null && worker.getToLong().equals("Michael Gill"))
        {
            Err.debug();
        }
        //Err.pr(SdzNote.NULL_WORKER_ACROSS_WIRE, "SubRecordObj replaces nulls with " +
        //    "dummies that not isDummy()");
        /* The following does happen. See SubRecordObj.timesWorker - Strandz creates lookup DOs when only
           a null exists. Null workers are fed into ParticularShift on the server side where there is no
           worker. */
        boolean badWorker = worker != null && !worker.isDummy() && (worker.getChristianName() == null && worker.getSurname() == null &&
            worker.getGroupName() == null);
        Assert.isFalse(badWorker, "ParticularShift is not being assigned a meaningful worker");
         /**/
        this.worker = worker;
    }

     /**/

    public void allocate(WorkerI worker, RosterSlotI rosterSlot)
    {
        setWorker(worker);
        Assert.notNull(rosterSlot);
        this.rosterSlot = rosterSlot;
    }

    /**
     * Sets the workerOrdinal.
     *
     * @param workerOrdinal The workerOrdinal to set
     */
    public void setWorkerOrdinal(Integer workerOrdinal)
    {
        this.workerOrdinal = workerOrdinal;
    }

    public Night getNight()
    {
        return night;
    }

    public String forBuddySentence()
    {
        String seniorityStr = "";
        if(!getWorker().getSeniority().equals( Seniority.NULL))
        {
            seniorityStr = " (" + getWorker().getSeniority().getName() + ") ";
        }
        return getWorker().getToLong() + seniorityStr + "doing " + dayInWeek + " "
            + whichShift + " shift on " + night.getFormattedDate();
    }

    public String toSentence()
    {
        return " -> For the " + whichShift + " shift on " + night.getFormattedDate()
            + " with " + night.getPartnersTxt(this);
    }

    public String toBuddySentence(List buddyManagers)
    {
        String result;
        WorkerI bm = getBuddyManager(buddyManagers);
        if (bm != null)
        {
            String buddyStr = bm.formatWithPhone();
            result = "    (buddy manager: " + buddyStr + ")";
        }
        else
        {
            result = "    (buddy manager: NONE)";
        }
        return result;
    }

    public boolean isFailed()
    {
        return failed;
    }

    public void setFailed(boolean failed)
    {
        this.failed = failed;
    }

    public OverrideI getBump()
    {
        return bump;
    }

    public void setBump(OverrideI bump)
    {
        if (bump == null)
        {
            Err.error("bump being set to null");
        }
        this.bump = bump;
    }
}
