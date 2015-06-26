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
package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.NameableI;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class DayInWeek implements Comparable, Serializable, NameableI, DayInWeekI
{
    private int pkId = -99; // primary-key=true
    private String name;
    private static HashMap daysPastSaturday = new HashMap(7);
    private static GregorianCalendar calendarWithDate = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
    private transient static int timesConstructed;
    private transient static final int maxConstructions = 7;
    public transient int id;

    private DayInWeek(String name, int pkId)
    {
        this();
        this.name = name;
        this.pkId = pkId;
    }

    public DayInWeek()
    {
        timesConstructed++;
        id = timesConstructed;
        //Err.pr( "DayInWeek ### CREATED id: " + id);
        if(id > maxConstructions)
        {// Err.error( "Constructed too many times, DayInWeek " + id);
        }
        if(id == 0)
        {
            Err.stack();
        }
    }

    public String toString()
    {
        return name;
    }

    public int compareTo(Object obj)
    {
        return Utils.relativeRank(OPEN_VALUES, this, obj);
    }

    public boolean equals(Object o)
    {
        //Utils.chkType(o, this.getClass());

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
            if((name == null ? test.getName() == null : name.equals(test.getName())))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (name == null ? 0 : name.hashCode());
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

    public String getName()
    {
        return name;
    }

    public String toShow()
    {
        return getName();
    }

    public void setName(String name)
    {
        /*
        Err.pr( "$$$ SETName on DayInWeek to " + name);
        if(name.equals( "Sunday"))
        {
        Err.stack();
        }
        */
        this.name = name;
        if(pkId == -99)
        {
            if(name == null)
            {
                pkId = NULL.getPkId();
            }
            else
            {
                for(int i = 0; i < CLOSED_VALUES.length; i++)
                {
                    DayInWeek closedValue = CLOSED_VALUES[i];
                    if(closedValue.getName().equals(name))
                    {
                        pkId = closedValue.getPkId();
                        break;
                    }
                }
                if(pkId == -99)
                {
                    Err.error("setName() to <" + name + "> cannot be done for " + pkId);
                }
            }
        }
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

    public static class ID implements Serializable
    {
        public int pkId;

        public ID()
        {
        }

        public ID(String pkId)
        {
            this.pkId = Integer.parseInt(pkId);
        }

        public boolean equals(Object o)
        {
            boolean result = false;
            if(o == this)
            {
                result = true;
            }
            else if(!(o instanceof ID))
            {// nufin
            }
            else
            {
                ID test = (ID) o;
                if(test.pkId == pkId)
                {
                    result = true;
                }
            }
            return result;
        }

        public int hashCode()
        {
            int result = 17;
            result = 37 * result + pkId;
            return result;
        }

        public String toString()
        {
            return "" + pkId;
        }
    }

    public int getPkId()
    {
        return pkId;
    }

    public int getCalendarDayOfWeek()
    {
        return pkId;
    }

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }
}
