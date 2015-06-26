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
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.TimeUtils;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * read only attributes so this Class is
 * immutable.
 */
class DayWeekTimesegment
{
    static final int OPEN_AT_BEGINNING = 0;
    static final int OPEN_AT_END = 1;
    static final int CLOSED = 2;
    static final int OPEN_AT_BOTHENDS = 3; // ie DAY
    private Time start;
    private Time end;
    private int type;
    /**
     * Use Calendar for the int dayOfWeek parameter.
     */
    private int dayOfWeek;
    private static final long START_CLOCK = 0;
    private static final long END_CLOCK = ((1000 * 60 * 60) * 23)
        + ((1000 * 60) * 59) + (1000 * 59) + 999;

    private void checkDayOfWeek(int dayOfWeek)
    {
        if(dayOfWeek != Calendar.MONDAY && dayOfWeek != Calendar.TUESDAY
            && dayOfWeek != Calendar.WEDNESDAY && dayOfWeek != Calendar.THURSDAY
            && dayOfWeek != Calendar.FRIDAY && dayOfWeek != Calendar.SATURDAY
            && dayOfWeek != Calendar.SUNDAY)
        {
            Err.error("expect dayOfWeek from java.util.Calendar, not " + dayOfWeek);
        }
    }

    DayWeekTimesegment(int dayOfWeek, Time start, Time end)
    {
        this.start = start;
        this.end = end;
        if(start != null && end != null)
        {
            type = CLOSED;
        }
        else if(start == null && end == null)
        {
            type = OPEN_AT_BOTHENDS;

            /*
            Because this code for some unknown reason didn't
            work was forced to use following elaborate method.
            this.start = new Time( START_CLOCK);
            this.end = new Time( END_CLOCK);
            */
            GregorianCalendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            this.start = new Time(cal.getTime().getTime());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            this.end = new Time(cal.getTime().getTime());
            Print.pr("Start set to " + this.start);
            Print.pr("End set to " + this.end);
        }
        else if(start == null)
        {
            type = OPEN_AT_BEGINNING;
            // start is really the beginning of the day
            this.start = new Time(START_CLOCK);
        }
        else if(end == null)
        {
            type = OPEN_AT_END;
            // end is really the end of the day
            this.end = new Time(END_CLOCK);
        }
        checkDayOfWeek(dayOfWeek);
        this.dayOfWeek = dayOfWeek;
        Print.pr("dayOfWeek has been set to " + dayOfWeek + " in constructor");
    }

    DayWeekTimesegment(int dayOfWeek)
    {
        this(dayOfWeek, null, null);
        // Err.pr( "called detailed constructor, " + dayOfWeek);
    }

    Time getStart()
    {
        return start;
    }

    Time getEnd()
    {
        return end;
    }

    int getDayOfWeek()
    {
        return dayOfWeek;
    }

    int getType()
    {
        return type;
    }

    public String toString()
    {
        String result = new String();
        result = "TYPE " + type + '\n';
        result += "DAY OF WEEK " + dayOfWeek + '\n';
        result += "START " + start + '\n';
        result += "END " + end + '\n';
        /*
        if(type == CLOSED || type == OPEN_AT_END)
        {
        result += "START " + start + '\n';
        }
        if(type == CLOSED || type == OPEN_AT_BEGINNING)
        {
        result += "END " + end + '\n';
        }
        if(type == OPEN_AT_BOTHENDS)
        {
        result += "ALL DAY LONG" + '\n';
        }
        */
        return result;
    }
}
