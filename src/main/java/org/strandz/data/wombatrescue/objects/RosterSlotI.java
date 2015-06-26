package org.strandz.data.wombatrescue.objects;

import org.strandz.lgpl.data.objects.DayInWeekI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.lgpl.util.IdentifierI;

import java.util.Date;
import java.util.List;

/**
 * User: Chris
 * Date: 31/08/2008
 * Time: 20:10:49
 */
public interface RosterSlotI extends Comparable, IdentifierI
{
    void installHelper();
    WorkerI getWorker();
    DayInWeekI getDayInWeek();
    void setDayInWeek( DayInWeekI dayInWeek);
    MonthInYearI getNotInMonth();
    void setNotInMonth( MonthInYearI monthInYear);
    NumDaysIntervalI getNumDaysInterval();
    void setNumDaysInterval( NumDaysIntervalI numDaysInterval);
    MonthInYearI getOnlyInMonth();
    void setOnlyInMonth( MonthInYearI monthInYear);
    OverrideI getOverridesOthers();
    void setOverridesOthers( OverrideI override);
    WeekInMonthI getWeekInMonth();
    void setWeekInMonth( WeekInMonthI weekInMonth);
    WhichShiftI getWhichShift();
    void setWhichShift( WhichShiftI whichShift);
    boolean isDisabled();
    void setDisabled( boolean b);
    boolean isMonthlyRestart();
    void setMonthlyRestart( boolean b);
    boolean isNotAvailable();
    void setNotAvailable( boolean b);
    Date getSpecificDate();
    void setSpecificDate( Date date);
    Date getStartDate();
    void setStartDate( Date date);
    boolean isActive();
    void setActive( boolean b);

    String getToSentence();
    String toString();    
    boolean equals(Object o);
    int hashCode();
    int getId();
    void setWorker( WorkerI worker);
    List getUnavailableDates();
    void clearUnavailableDates();
}
