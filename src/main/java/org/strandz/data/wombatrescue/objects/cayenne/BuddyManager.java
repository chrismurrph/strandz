package org.strandz.data.wombatrescue.objects.cayenne;

import org.strandz.data.wombatrescue.objects.cayenne.auto._BuddyManager;
import org.strandz.data.wombatrescue.objects.BuddyManagerI;
import org.strandz.data.wombatrescue.objects.SexI;
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



