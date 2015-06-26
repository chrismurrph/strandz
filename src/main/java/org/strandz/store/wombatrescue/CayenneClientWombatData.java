package org.strandz.store.wombatrescue;

/**
 * User: Chris
 * Date: 26/09/2008
 * Time: 12:27:44
 */
public class CayenneClientWombatData extends CayenneWombatData
{
    static final Class CLIENT_WORKER = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
    static final Class CLIENT_BUDDY_MANAGER = org.strandz.data.wombatrescue.objects.cayenne.client.BuddyManager.class;
    static final Class CLIENT_ROSTER_SLOT = org.strandz.data.wombatrescue.objects.cayenne.client.RosterSlot.class;
    static final Class CLIENT_USER = org.strandz.data.wombatrescue.objects.cayenne.client.UserDetails.class;

    static final Class CLIENT_DAY_IN_WEEK = org.strandz.data.wombatrescue.objects.cayenne.client.DayInWeek.class;
    static final Class CLIENT_INTERVAL = org.strandz.data.wombatrescue.objects.cayenne.client.NumDaysInterval.class;
    static final Class CLIENT_WEEK_IN_MONTH = org.strandz.data.wombatrescue.objects.cayenne.client.WeekInMonth.class;
    static final Class CLIENT_WHICH_SHIFT = org.strandz.data.wombatrescue.objects.cayenne.client.WhichShift.class;
    static final Class CLIENT_MONTH_IN_YEAR = org.strandz.data.wombatrescue.objects.cayenne.client.MonthInYear.class;
    static final Class CLIENT_SENIORITY = org.strandz.data.wombatrescue.objects.cayenne.client.Seniority.class;
    static final Class CLIENT_SEX = org.strandz.data.wombatrescue.objects.cayenne.client.Sex.class;
    static final Class CLIENT_FLEXIBILITY = org.strandz.data.wombatrescue.objects.cayenne.client.Flexibility.class;
    static final Class CLIENT_OVERRIDE = org.strandz.data.wombatrescue.objects.cayenne.client.Override.class;

    private static final Class[] CLIENT_CLASSES = {
        CLIENT_WORKER,
        //LOOKUPS,
        CLIENT_DAY_IN_WEEK, CLIENT_INTERVAL, CLIENT_WEEK_IN_MONTH,
        CLIENT_WHICH_SHIFT, CLIENT_MONTH_IN_YEAR, CLIENT_SENIORITY, CLIENT_SEX, CLIENT_FLEXIBILITY, CLIENT_OVERRIDE,
    };

    public Class[] getClasses()
    {
        return CLIENT_CLASSES;
    }

    public Class getWorkerClass()
    {
        return CLIENT_WORKER;
    }
    public Class getBuddyManagerClass()
    {
        return CLIENT_BUDDY_MANAGER;
    }
    public Class getRosterSlotClass()
    {
        return CLIENT_ROSTER_SLOT;
    }
    public Class getUserDetailsClass()
    {
        return CLIENT_USER;
    }
    public Class getDayInWeekClass()
    {
        return CLIENT_DAY_IN_WEEK;
    }
    public Class getIntervalClass()
    {
        return CLIENT_INTERVAL;
    }
    public Class getWeekInMonthClass()
    {
        return CLIENT_WEEK_IN_MONTH;
    }
    public Class getWhichShiftClass()
    {
        return CLIENT_WHICH_SHIFT;
    }
    public Class getMonthInYearClass()
    {
        return CLIENT_MONTH_IN_YEAR;
    }
    public Class getSeniorityClass()
    {
        return CLIENT_SENIORITY;
    }
    public Class getSexClass()
    {
        return CLIENT_SEX;
    }
    public Class getFlexibilityClass()
    {
        return CLIENT_FLEXIBILITY;
    }
    public Class getOverrideClass()
    {
        return CLIENT_OVERRIDE;
    }
}
