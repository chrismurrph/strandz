/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Timesegment implements Serializable
{
    /**
     * read only attributes so this Class is
     * immutable.
     */
    private Date start;
    private Date end;
    /**
     * Used for matching with Days
     */
    private int firstDay;
    private int lastDay;
    private long lengthOfTime;
    /*
    private boolean sunday = false;
    private boolean monday = false;
    private boolean tuesday = false;
    private boolean wednesday = false;
    private boolean thursday = false;
    private boolean friday = false;
    private boolean saturday = false;
    */
    boolean[] days = new boolean[7];
    private Date partialBeginDayStart;
    private Date partialBeginDayEnd;
    private Date partialEndDayStart;
    private Date partialEndDayEnd;
    private static final int ONE_WEEK_IN_MILLISECONDS = 3600000 * 24 * 7;

    public Timesegment(Date start, Date end)
    {
        if(start == null || end == null)
        {
            Err.error(
                "A Timesegment must always be closed (ie delimited by a start and end)");
        }
        this.start = start;
        this.end = end;

        //
        GregorianCalendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
        cal.setTime(start);
        firstDay = cal.get(GregorianCalendar.DAY_OF_WEEK);
        // Err.pr( "firstDay " + firstDay);
        cal.setTime(end);
        lastDay = cal.get(GregorianCalendar.DAY_OF_WEEK);
        // Err.pr( "lastDay " + lastDay);
        lengthOfTime = end.getTime() - start.getTime();
        // Err.pr( "lengthOfTime (hours)" + lengthOfTime/1000/60/60);
        workOutDaysEvenPartiallyIncluded();
        workOutPartiallyIncludedDays();
    }

    private int nextDay(int currentDay)
    {
        if(currentDay < 1 || currentDay > 7)
        {
            Err.error("nextDay used for counting thru days");
        }
        if(currentDay == 7)
        {
            return 1;
        }
        else
        {
            return currentDay + 1;
        }
    }

    /**
     * For periods that are less than 1 week go thru from
     * firstDay to lastDay selecting the days as go.
     */
    private void workOutDaysEvenPartiallyIncluded()
    {
        for(int i = firstDay; ; i = nextDay(i))
        {
            if(i == 1)
            {
                days[i - 1] = true;
                // Err.pr( "sunday=true");
            }
            else if(i == 2)
            {
                days[i - 1] = true;
                // Err.pr( "monday=true");
            }
            else if(i == 3)
            {
                days[i - 1] = true;
                // Err.pr( "tuesday=true");
            }
            else if(i == 4)
            {
                days[i - 1] = true;
                // Err.pr( "wednesday=true");
            }
            else if(i == 5)
            {
                days[i - 1] = true;
                // Err.pr( "thursday=true");
            }
            else if(i == 6)
            {
                days[i - 1] = true;
                // Err.pr( "friday=true");
            }
            else if(i == 7)
            {
                days[i - 1] = true;
                // Err.pr( "saturday=true");
            }
            if(i == lastDay)
            {
                break;
            }
        }
    }

    /**
     * Create 2 new Timesegments. From the start of the first
     * day to whichever comes first, the end of a day or the end.
     * Also from whichever comes later, the start of the last
     * day or the start, to the end. These two values may be the
     * same, but that shouldn't matter.
     */
    private void workOutPartiallyIncludedDays()
    {
        GregorianCalendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
        cal.setTime(start);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        Date endOfFirstDay = cal.getTime();
        cal.setTime(end);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date startOfLastDay = cal.getTime();
        Date whicheverFirst;
        if(endOfFirstDay.before(end))
        {
            whicheverFirst = endOfFirstDay;
        }
        else
        {
            whicheverFirst = end;
        }
        partialBeginDayStart = start;
        partialBeginDayEnd = whicheverFirst;

        /*
        Err.pr( "Partial begin " + partialBeginDayStart);
        Err.pr( "\t" + partialBeginDayEnd);
        */
        Date whicheverLater;
        if(startOfLastDay.after(start))
        {
            whicheverLater = startOfLastDay;
        }
        else
        {
            whicheverLater = start;
        }
        partialEndDayStart = whicheverLater;
        partialEndDayEnd = end;
        /*
        Err.pr( "Partial end " + partialEndDayStart);
        Err.pr( "\t" + partialEndDayEnd);
        */
    }

    Date getStart()
    {
        return start;
    }

    Date getEnd()
    {
        return end;
    }

    ArrayList getDayWeekTimesegments()
    {
        ArrayList result = new ArrayList();
        int day;
        for(int i = 0; i <= days.length - 1; i++)
        {
            day = i + 1;
            if(days[i] == true)
            {
                if(day == firstDay)
                {
                    result.add(
                        new DayWeekTimesegment(day,
                            new Time(partialBeginDayStart.getTime()),
                            new Time(partialBeginDayEnd.getTime())));
                }
                else if(day == lastDay)
                {
                    result.add(
                        new DayWeekTimesegment(day,
                            new Time(partialEndDayStart.getTime()),
                            new Time(partialEndDayEnd.getTime())));
                }
                else
                {
                    result.add(new DayWeekTimesegment(day));
                }
            }
        }
        return result;
    }
} // end class
