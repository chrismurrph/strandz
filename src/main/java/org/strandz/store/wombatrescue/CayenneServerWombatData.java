package org.strandz.store.wombatrescue;

import org.strandz.data.wombatrescue.objects.cayenne.*;
import org.strandz.data.wombatrescue.objects.cayenne.Override;
import org.strandz.lgpl.util.Err;

/**
 * User: Chris
 * Date: 26/09/2008
 * Time: 12:28:05
 */
public class CayenneServerWombatData extends CayenneWombatData
{
    static final Class SERVER_WORKER = Worker.class;
    static final Class SERVER_BUDDY_MANAGER = BuddyManager.class;
    static final Class SERVER_ROSTER_SLOT = RosterSlot.class;
    static final Class SERVER_USER = UserDetails.class;

    static final Class SERVER_DAY_IN_WEEK = DayInWeek.class;
    static final Class SERVER_INTERVAL = NumDaysInterval.class;
    static final Class SERVER_WEEK_IN_MONTH = WeekInMonth.class;
    static final Class SERVER_WHICH_SHIFT = WhichShift.class;
    static final Class SERVER_MONTH_IN_YEAR = MonthInYear.class;
    static final Class SERVER_SENIORITY = Seniority.class;
    static final Class SERVER_SEX = Sex.class;
    static final Class SERVER_FLEXIBILITY = Flexibility.class;
    static final Class SERVER_OVERRIDE = org.strandz.data.wombatrescue.objects.cayenne.Override.class;

    /**
     * Order of these important if have RI constraints on. To see if they are on:
     * SHOW CREATE TABLE Worker;
     * In MySql.
     * Theoretically flush s/be smart enough to delete objects in the right order to
     * navigate around FK constraints, even for when Worker refers to itself. Our
     * slightly tacky solution is to not have integrity constraints for Worker->Worker
     */
    private static final Class[] SERVER_CLASSES = {
        SERVER_WORKER,
        //LOOKUPS,
        SERVER_DAY_IN_WEEK, SERVER_INTERVAL, SERVER_WEEK_IN_MONTH,
        SERVER_WHICH_SHIFT, SERVER_MONTH_IN_YEAR, SERVER_SENIORITY, SERVER_SEX, SERVER_FLEXIBILITY, SERVER_OVERRIDE,
    };

    private static final Class[] SERVER_NON_LOOKUP_CLASSES = {
        SERVER_WORKER,
        //LOOKUPS,
    };

    private static final Class[] SERVER_LOOKUP_CLASSES = {
        SERVER_DAY_IN_WEEK, SERVER_INTERVAL, SERVER_WEEK_IN_MONTH,
        SERVER_WHICH_SHIFT, SERVER_MONTH_IN_YEAR, SERVER_SENIORITY, SERVER_SEX, SERVER_FLEXIBILITY, SERVER_OVERRIDE,
    };

    public Class[] getClasses()
    {
        return SERVER_CLASSES;
    }

    public Class getWorkerClass()
    {
        return SERVER_WORKER;
    }
    public Class getBuddyManagerClass()
    {
        return SERVER_BUDDY_MANAGER;
    }
    public Class getRosterSlotClass()
    {
        return SERVER_ROSTER_SLOT;
    }
    public Class getUserDetailsClass()
    {
        return SERVER_USER;
    }
    public Class getDayInWeekClass()
    {
        return SERVER_DAY_IN_WEEK;
    }
    public Class getIntervalClass()
    {
        return SERVER_INTERVAL;
    }
    public Class getWeekInMonthClass()
    {
        return SERVER_WEEK_IN_MONTH;
    }
    public Class getWhichShiftClass()
    {
        return SERVER_WHICH_SHIFT;
    }
    public Class getMonthInYearClass()
    {
        return SERVER_MONTH_IN_YEAR;
    }
    public Class getSeniorityClass()
    {
        return SERVER_SENIORITY;
    }
    public Class getSexClass()
    {
        return SERVER_SEX;
    }
    public Class getFlexibilityClass()
    {
        return SERVER_FLEXIBILITY;
    }
    public Class getOverrideClass()
    {
        return SERVER_OVERRIDE;
    }

    private static Object[] getLookupValues( Class clazz)
    {
        Object[] result = null;
        if(clazz == SERVER_DAY_IN_WEEK)
        {
            result = DayInWeek.OPEN_VALUES;
        }
        else if(clazz == SERVER_INTERVAL)
        {
            result = NumDaysInterval.OPEN_VALUES;
        }
        else if(clazz == SERVER_WEEK_IN_MONTH)
        {
            result = WeekInMonth.OPEN_VALUES;
        }
        else if(clazz == SERVER_WHICH_SHIFT)
        {
            result = WhichShift.OPEN_VALUES;
        }
        else if(clazz == SERVER_MONTH_IN_YEAR)
        {
            result = MonthInYear.OPEN_VALUES;
        }
        else if(clazz == SERVER_SENIORITY)
        {
            result = Seniority.OPEN_VALUES;
        }
        else if(clazz == SERVER_SEX)
        {
            result = Sex.OPEN_VALUES;
        }
        else if(clazz == SERVER_FLEXIBILITY)
        {
            result = Flexibility.OPEN_VALUES;
        }
        else if(clazz == SERVER_OVERRIDE)
        {
            result = Override.OPEN_VALUES;
        }
        else
        {
            Err.error( "Unknown class: " + clazz.getName());
        }
        return result;
    }

    private static boolean isLookupDomainObject( Class clazz)
    {
        boolean result = true;
        if(clazz == SERVER_WORKER /*|| clazz == BUDDY_MANAGER || clazz == ROSTER_SLOT || clazz == USER*/)
        {
            result = false;
        }
        return result;
    }
}
