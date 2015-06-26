package org.strandz.data.wombatrescue.objects.cayenne.client;

import org.strandz.data.wombatrescue.objects.cayenne.client.auto._RosterSlot;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.data.wombatrescue.objects.NumDaysIntervalI;
import org.strandz.data.wombatrescue.objects.OverrideI;
import org.strandz.data.wombatrescue.objects.WeekInMonthI;
import org.strandz.data.wombatrescue.objects.WhichShiftI;
import org.strandz.data.wombatrescue.objects.RosterSlotHelper;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.data.objects.DayInWeekI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.note.WombatNote;
import org.apache.cayenne.PersistentObject;

import java.util.List;

public class RosterSlot extends _RosterSlot implements RosterSlotI
{
    private transient RosterSlotHelper rosterSlotHelper;
    private transient static int constructedTimes;
    public transient int incrementId;

    public RosterSlot()
    {
        constructedTimes++;
        incrementId = constructedTimes;
        /**/
        Err.pr(WombatNote.NEW_ROSTERSLOT_INSTANCE, "Constructing RosterSlot ID " + incrementId);
        if(incrementId == 0)
        {
            Err.stack();
        }
        //As the DB was originally a JDO one, and we want the JDO client to continue to work:
        setJdoclass( "org.strandz.data.wombatrescue.objects.RosterSlot");
        rosterSlotHelper = new RosterSlotHelper( this);
        Err.pr(WombatNote.NEW_ROSTERSLOT_INSTANCE, "Finished constructing RosterSlot ID " + incrementId);
    }

    public void installHelper()
    {
        Err.error();
    }

    /*
    public boolean isDisabled()
    {
        boolean result = true;
        if(getDisabled().intValue() == 0)
        {
            result = false;
        }
        return result;
    }

    public void setDisabled( boolean b)
    {
        if(b)
        {
            setDisabled( Utils.ONE.byteValue());
        }
        else
        {
            setDisabled( Utils.ZERO.byteValue());
        }
    }
    */

    /*
    public boolean isMonthlyRestart()
    {
        boolean result = true;
        if(getMonthlyRestart().intValue() == 0)
        {
            result = false;
        }
        return result;
    }

    public void setMonthlyRestart( boolean b)
    {
        if(b)
        {
            setMonthlyRestart( Utils.ONE.byteValue());
        }
        else
        {
            setMonthlyRestart( Utils.ZERO.byteValue());
        }
    }
    */

    /*
    public boolean isNotAvailable()
    {
        boolean result = true;
        if(getNotAvailable().intValue() == 0)
        {
            result = false;
        }
        return result;
    }

    public void setNotAvailable( boolean b)
    {
        if(b)
        {
            setNotAvailable( Utils.ONE.byteValue());
        }
        else
        {
            setNotAvailable( Utils.ZERO.byteValue());
        }
    }
    */

    public boolean isActive()
    {
        return !isDisabled();
    }

    public void setActive( boolean active)
    {
        setDisabled( !active);
    }

    public DayInWeekI getDayInWeek()
    {
        return getActualDayInWeek();
    }

    public void setDayInWeek(DayInWeekI dayOfWeek)
    {
        setActualDayInWeek( (DayInWeek)dayOfWeek);
    }

    public MonthInYearI getNotInMonth()
    {
        return getActualNotInMonth();
    }

    public void setNotInMonth(MonthInYearI notInMonth)
    {
        setActualNotInMonth( (MonthInYear)notInMonth);
    }

    public NumDaysIntervalI getNumDaysInterval()
    {
        return getActualNumDaysInterval();
    }

    public void setNumDaysInterval( NumDaysIntervalI numDaysInterval)
    {
        setActualNumDaysInterval( (NumDaysInterval)numDaysInterval);
    }

    public MonthInYearI getOnlyInMonth()
    {
        return getActualOnlyInMonth();
    }

    public void setOnlyInMonth(MonthInYearI onlyInMonth)
    {
        setActualOnlyInMonth( (MonthInYear)onlyInMonth);
    }

    public OverrideI getOverridesOthers()
    {
        return getActualOverridesOthers();
    }

    public void setOverridesOthers(OverrideI overridesOthers)
    {
        setActualOverridesOthers( (Override)overridesOthers);
    }

    public WeekInMonthI getWeekInMonth()
    {
        return getActualWeekInMonth();
    }

    public void setWeekInMonth(WeekInMonthI weekInMonth)
    {
        setActualWeekInMonth( (WeekInMonth)weekInMonth);
    }

    public WhichShiftI getWhichShift()
    {
        return getActualWhichShift();
    }

    public void setWhichShift(WhichShiftI whichShift)
    {
        setActualWhichShift( (WhichShift)whichShift);
    }

    public String getToSentence()
    {
        return rosterSlotHelper.getToSentence(
            WeekInMonth.NULL, WhichShift.NULL, MonthInYear.NULL, DayInWeek.NULL, NumDaysInterval.WEEKLY);
    }

    public int getId()
    {
        return rosterSlotHelper.getId();
    }

    public String getName()
    {
        return rosterSlotHelper.getName();
    }

    public int compareTo(Object o)
    {
        return rosterSlotHelper.compareTo( o);
    }

    public String toString()
    {
        return rosterSlotHelper.helperToString();
    }

    public boolean equals(Object o)
    {
        return rosterSlotHelper.helperEquals( o);
    }

    public int hashCode()
    {
        return rosterSlotHelper.helperHashCode();
    }

    public void setWorker( WorkerI worker)
    {
        /* This goes thru without an error on the server (NOT here), yet will fail on client (here)
         */
        Assert.notNull( ((PersistentObject)worker).getObjectContext(),
            "Cannot call setWorker( worker) with an arg that has no ObjectContext: <" +
                worker + ">, ID: " + worker.getId());
        super.setWorker( (Worker)worker);
    }

    public List getUnavailableDates()
    {
        return rosterSlotHelper.getUnavailableDates();
    }

    public void clearUnavailableDates()
    {
        rosterSlotHelper.clearUnavailableDates();
    }
}



