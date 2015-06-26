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
package org.strandz.data.wombatrescue.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.data.objects.DayInWeekI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.lgpl.data.objects.MonthInYear;

import java.util.Date;
import java.util.List;
import java.io.Serializable;

/**
 * Because RosterSlot implements InstanceCallbacks then some kind of
 * a jdo jar file must exist. (See method for explanation).
 */
public class RosterSlot implements Serializable, RosterSlotI //implements InstanceCallbacks
{
    private DayInWeek dayInWeek;
    private WhichShift whichShift;
    private boolean monthlyRestart;
    private NumDaysInterval numDaysInterval; // only needed where monthlyRestart == false
    private Date startDate; // same (also refered to as 'continuing')
    private WeekInMonth weekInMonth; // only needed where monthlyRestart == true
    private Date specificDate;
    private Override overridesOthers;
    private boolean disabled;
    private boolean notAvailable;
    private MonthInYear onlyInMonth;
    private MonthInYear notInMonth;
    private Worker worker;
    private transient RosterSlotHelper rosterSlotHelper;
    //
    private transient static int times;

    public RosterSlot()
    {
        super();
        rosterSlotHelper = new RosterSlotHelper( this);
    }

    public void installHelper()
    {
        Err.error();
    }

    /**
     * Returns the monthlyRestart.
     *
     * @return boolean
     */
    public boolean isMonthlyRestart()
    {
        return monthlyRestart;
    }

    /**
     * Sets the monthlyRestart.
     *
     * @param monthlyRestart The monthlyRestart to set
     */
    public void setMonthlyRestart(boolean monthlyRestart)
    {
        this.monthlyRestart = monthlyRestart;
    }

    /**
     * Sets the dayOfWeek.
     *
     * @param dayOfWeek The dayOfWeek to set
     */
    public void setDayInWeek(DayInWeekI dayOfWeek)
    {
        /*
        times++;
        if(times > 304)
        {
        Err.pr(
        "$$$ SETDayOfWeek: " + dayOfWeek + " on rosterslot " + id + " times "
        + times);
        if(times == 307)
        {
        Err.stackOff();
        }
        }
        */
        this.dayInWeek = (DayInWeek)dayOfWeek;
    }

    /**
     * Sets the weekOfMonth.
     *
     * @param weekInMonth The weekInMonth to set
     */
    public void setWeekInMonth(WeekInMonthI weekInMonth)
    {
        // Err.pr( "$%$ Setting WeekInMonth to " + weekInMonth);
        this.weekInMonth = (WeekInMonth)weekInMonth;
    }

    /**
     * Returns the dayOfWeek.
     *
     * @return Day
     */
    public DayInWeekI getDayInWeek()
    {
        // Err.pr( "$$$ GETDayOfWeek: " + dayOfWeek + " from " +
        // "RosterSlot " + id);
        return dayInWeek;
    }

    /**
     * Returns the interval.
     *
     * @return Interval
     */
    public NumDaysIntervalI getNumDaysInterval()
    {
        return numDaysInterval;
    }

    /**
     * Sets the interval.
     *
     * @param numDaysInterval The interval to set
     */
    public void setNumDaysInterval( NumDaysIntervalI numDaysInterval)
    {
        this.numDaysInterval = (NumDaysInterval)numDaysInterval;
    }

    /**
     * Returns the weekOfMonth.
     *
     * @return WeekInMonth
     */
    public WeekInMonthI getWeekInMonth()
    {
        // Err.pr( "$%$ Getting WeekInMonth " + weekInMonth);
        return weekInMonth;
    }

    /**
     * Returns the startDate.
     *
     * @return Date
     */
    public Date getStartDate()
    {
        return startDate;
    }

    /**
     * Sets the startDate.
     *
     * @param startDate The startDate to set
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    /**
     * Returns the worker.
     *
     * @return Worker
     */
    public WorkerI getWorker()
    {
        return worker;
    }

    public void setWorker(WorkerI worker)
    {
        this.worker = (Worker)worker;
        // Err.pr( "Setting worker to " + worker + " for RS: " + this);
    }

    /*
    public Volunteer getVolunteer()
    {
      Err.error( "RosterSlots no longer have volunteers");
      return null;
    }
    */
    /*
    private void setVolunteer( Volunteer volunteer)
    {
      this.volunteer = volunteer;
      // Err.pr( "Setting volunteer to " + volunteer + " for RS: " + this);
    }
    */

    /*
    public void setVolunteer(Volunteer volunteer)
    {
    if(!this.volunteer.equals( volunteer))
    {
    this.volunteer = volunteer;
    if(volunteer != null)
    {
    List slots = volunteer.getPlayers();
    if(!slots.contains( this))
    {
    slots.add( this);
    }
    else
    {
    Err.pr( "%%%%%%%%%%%Slots already has " + this);
    }
    }
    else
    {
    Err.pr( "%%%%%%%%%%%Null volunteer");
    }
    }
    else
    {
    Err.pr( "%%%%%%%%%%%Trying to assign the same vol to " + this);
    }
    }
    */

    /**
     * JDO implementations always seem to use their own version of Date.
     * This method automatically substitutes in a real Java date. It
     * might be important to do this sort of thing when doing extreme things
     * such as attempting to copy the data across from one vendor implementation
     * to another.
     * <p/>
     * Have commented out because we don't want any JDO dependency. This done
     * for the in-memory JWS WR application.
     */
    /*
    public void jdoPostLoad()
    {
      if(startDate != null)
      {
        startDate = new Date( startDate.getTime());
      }
    }

    public void jdoPreStore()
    {}

    public void jdoPreClear()
    {}

    public void jdoPreDelete()
    {}
    */
    public WhichShiftI getWhichShift()
    {
        /*
        Err.pr( "$$$ GETWhichShift: " + whichShift + " from " +
        "RosterSlot " + id);
        */
        return whichShift;
    }

    public void setWhichShift(WhichShiftI whichShift)
    {
        this.whichShift = (WhichShift)whichShift;
        /*
        times++;
        if(times >= 255)
        {
        Err.pr( "$$$ SETWhichShift: " + whichShift + " times " + times);
        }
        */
        /*
        if(whichShift.id > 5)
        {
        Err.error( "BAD WHICH SHIFT " + whichShift.id);
        }
        */
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public boolean isActive()
    {
        return !isDisabled();
    }

    public void setActive( boolean active)
    {
        //Err.pr( SdzNote.SYNC, "Active on RosterSlot being set to " + active);
        setDisabled( !active);
    }
    
    public Date getSpecificDate()
    {
        return specificDate;
    }

    public void setSpecificDate(Date specificDate)
    {
        this.specificDate = specificDate;
    }

    public String getToSentence()
    {
        return rosterSlotHelper().getToSentence(
            WeekInMonth.NULL, WhichShift.NULL, MonthInYear.NULL, DayInWeek.NULL, NumDaysInterval.WEEKLY);
    }

    public OverrideI getOverridesOthers()
    {
        return overridesOthers;
    }

    public void setOverridesOthers(OverrideI overridesOthers)
    {
        this.overridesOthers = (Override)overridesOthers;
    }

    public boolean isNotAvailable()
    {
        return notAvailable;
    }

    public void setNotAvailable(boolean notAvailable)
    {
        this.notAvailable = notAvailable;
    }

    /*
    * XML doesn't recognise transient, so lets use a public var
    public List getUnavailableDates() {
    return unavailableDates;
    }
    public void setUnavailableDates(List unavailableDates) {
    this.unavailableDates = unavailableDates;
    }
    */

    public MonthInYearI getOnlyInMonth()
    {
        return onlyInMonth;
    }

    public void setOnlyInMonth(MonthInYearI onlyInMonth)
    {
        this.onlyInMonth = (MonthInYear)onlyInMonth;
    }

    public MonthInYearI getNotInMonth()
    {
        return notInMonth;
    }

    public void setNotInMonth(MonthInYearI notInMonth)
    {
        this.notInMonth = (MonthInYear)notInMonth;
    }

    public int getId()
    {
        return rosterSlotHelper().getId();
    }

    public String getName()
    {
        return rosterSlotHelper().getName();
    }

    public int compareTo(Object o)
    {
        return rosterSlotHelper().compareTo( o);
    }

    public String toString()
    {
        return rosterSlotHelper().helperToString();
    }

    public boolean equals(Object o)
    {
        return rosterSlotHelper().helperEquals( o);
    }

    public int hashCode()
    {
        return rosterSlotHelper().helperHashCode();
    }

    public List getUnavailableDates()
    {
        return rosterSlotHelper().getUnavailableDates();
    }
    
    public void clearUnavailableDates()
    {
        rosterSlotHelper().clearUnavailableDates();
    }

    /*
     * copyTrick made an NPE - b/c WorkerHelper is transient
     */
    private RosterSlotHelper rosterSlotHelper()
    {
        if(rosterSlotHelper == null)
        {
            rosterSlotHelper = new RosterSlotHelper( this);
        }
        return rosterSlotHelper;
    }
}