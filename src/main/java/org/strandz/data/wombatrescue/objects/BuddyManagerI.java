package org.strandz.data.wombatrescue.objects;

import org.strandz.lgpl.data.objects.DayInWeekI;

/**
 * User: Chris
 * Date: 6/09/2008
 * Time: 02:54:56
 */
public interface BuddyManagerI
{
    WorkerI getWorker();
    void setWorker( WorkerI worker);
    WhichShiftI getWhichShift();
    DayInWeekI getDayInWeek();
    void setDayInWeek( DayInWeekI dayInWeek);
    void setWhichShift( WhichShiftI whichshift);
}
