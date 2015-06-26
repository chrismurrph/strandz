package org.strandz.data.wombatrescue.objects.cayenne;

import org.strandz.data.wombatrescue.objects.cayenne.auto._DayInWeek;
import org.strandz.lgpl.util.NameableI;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.data.objects.DayInWeekI;

import java.io.Serializable;
import java.util.HashMap;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Calendar;

public class DayInWeek extends _DayInWeek implements Comparable, Serializable, NameableI, DayInWeekI
{
    private int pkId = -99; // primary-key=true
    private static HashMap daysPastSaturday = new HashMap(7);
    private static GregorianCalendar calendarWithDate = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
    private transient static int timesConstructed;
    private transient static final int maxConstructions = 7;
    public transient int id;

    private DayInWeek(String name, int pkId)
    {
        this();
        setName( name);
        this.pkId = pkId;
    }

    public DayInWeek()
    {
        timesConstructed++;
        id = timesConstructed;
        // Err.pr( "DayInWeek ### CREATED id: " + id);
        if(id > maxConstructions)
        {// Err.error( "Constructed too many times, DayInWeek " + id);
        }
    }

    public String toString()
    {
        return getName();
    }

    public int compareTo(Object obj)
    {
        return Utils.relativeRank(OPEN_VALUES, this, obj);
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, DayInWeekI.class);

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof DayInWeekI))
        {// nufin
        }
        else
        {
            DayInWeekI test = (DayInWeekI) o;
            if((getName() == null ? test.getName() == null : getName().equals(test.getName())))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (getName() == null ? 0 : getName().hashCode());
        return result;
    }

    /**
     * These three methods can be used to compare with
     * Calendar.MONDAY etc.
     */
    public static DayInWeek fromOrdinal(int i)
    {
        return CLOSED_VALUES[i - 1];
    }

    public static DayInWeek getDayInWeek(Date date)
    {
        calendarWithDate.setTime(date);

        int day = calendarWithDate.get(Calendar.DAY_OF_WEEK);
        return DayInWeek.fromOrdinal(day);
    }

    public static int toOrdinal( DayInWeek dayInWeek)
    {
        int result = Utils.UNSET_INT;
        for(int i = 0; i < CLOSED_VALUES.length; i++)
        {
            DayInWeek closedValue = CLOSED_VALUES[i];
            if(closedValue.equals( dayInWeek))
            {
                result = i+1;
                break;
            }
        }
        return result;
    }

    public static DayInWeek getDayInWeek(String s)
    {
        DayInWeek result = null;
        if(Utils.isBlank(s))
        {
            result = DayInWeek.NULL;
        }
        else
        {
            for(int i = 0; i < CLOSED_VALUES.length; i++)
            {
                DayInWeek openValue = CLOSED_VALUES[i];
                if(openValue.getName().equals(s))
                {
                    result = CLOSED_VALUES[i];
                    break;
                }
            }
        }
        return result;
    }

    public String getShortName()
    {
        return getName().substring( 0, 3);
    }

    public String toShow()
    {
        return getName();
    }

    public transient static final DayInWeek NULL = new DayInWeek();
    public transient static final DayInWeek SUNDAY = new DayInWeek("Sunday", 1);
    public transient static final DayInWeek MONDAY = new DayInWeek("Monday", 2);
    public transient static final DayInWeek TUESDAY = new DayInWeek("Tuesday", 3);
    public transient static final DayInWeek WEDNESDAY = new DayInWeek("Wednesday",
        4);
    public transient static final DayInWeek THURSDAY = new DayInWeek("Thursday",
        5);
    public transient static final DayInWeek FRIDAY = new DayInWeek("Friday", 6);
    public transient static final DayInWeek SATURDAY = new DayInWeek("Saturday",
        7);

    static
    {
        daysPastSaturday.put(SATURDAY, new Integer(0));
        daysPastSaturday.put(SUNDAY, new Integer(1));
        daysPastSaturday.put(MONDAY, new Integer(2));
        daysPastSaturday.put(TUESDAY, new Integer(3));
        daysPastSaturday.put(WEDNESDAY, new Integer(4));
        daysPastSaturday.put(THURSDAY, new Integer(5));
        daysPastSaturday.put(FRIDAY, new Integer(6));
    }

    /**
     * Get the date that is the first Saturday before the
     * date given.
     */
    public static Date getBeginningDate(Date date)
    {
        calendarWithDate.setTime(date);

        int day = calendarWithDate.get(Calendar.DAY_OF_WEEK);
        DayInWeek today = DayInWeek.fromOrdinal(day);
        Integer numberBack = (Integer) daysPastSaturday.get(today);
        calendarWithDate.add(Calendar.DATE, -numberBack.intValue());
        TimeUtils.clearHMS(calendarWithDate);
        return calendarWithDate.getTime();
    }

    public static Date getEightDaysOn(Date date)
    {
        calendarWithDate.setTime(date);
        calendarWithDate.add(Calendar.DATE, 8);
        return calendarWithDate.getTime();
    }

    public transient static final DayInWeek[] CLOSED_VALUES = {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY};

    public transient static final DayInWeek[] OPEN_VALUES = {
        NULL, SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY};

//    public int getPkId()
//    {
//        return pkId;
//    }

    public int getCalendarDayOfWeek()
    {
        return pkId;
    }

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }
}



