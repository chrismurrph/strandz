package org.strandz.data.wombatrescue.objects.cayenne.client;

import org.strandz.data.wombatrescue.objects.cayenne.client.auto._BuddyManager;
import org.strandz.data.wombatrescue.objects.BuddyManagerI;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.WhichShiftI;
import org.strandz.lgpl.data.objects.DayInWeekI;

public class BuddyManager extends _BuddyManager implements BuddyManagerI
{
    public WorkerI getWorker()
    {
        return getActualWorker();
    }
    public void setWorker( WorkerI worker)
    {
        setActualWorker( (Worker)worker);
    }

    public WhichShiftI getWhichShift()
    {
        return getActualWhichShift();
    }

    public void setWhichShift( WhichShiftI whichShift)
    {
        setActualWhichShift( (WhichShift)whichShift);
    }

    public DayInWeekI getDayInWeek()
    {
        return getActualDayInWeek();
    }

    public void setDayInWeek( DayInWeekI dayInWeek) 
    {
        setActualDayInWeek( (DayInWeek)dayInWeek);
    }

}



