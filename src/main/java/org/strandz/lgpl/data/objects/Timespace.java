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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Each Timespace is supposed to represent an activity
 * that someone might carry on. It is a heterogenous
 * list of time entries. For instance someone might be
 * at Uni on Mon, Wed and after noon on Friday. Thus the
 * list would contain two DAY entries and a
 * DAYWEEK_OPENEND_TIMESEGMENT entry.
 */
public class Timespace implements Serializable
{
    /**
     * Period between two specific timestamps.
     */
    public static final int DATE_CLOSED_TIMESEGMENT = 0;
    /**
     * All day on a particular day of every week up
     * until a specific timestamp.
     */
    public static final int DAYWEEK_OPENBEGINNING_TIMESEGMENT = 1;
    /**
     * On a particular day of every week from a specific
     * timestamp.
     */
    public static final int DAYWEEK_OPENEND_TIMESEGMENT = 2;
    /**
     * Period between two specific timestamps on a
     * particular day of every week.
     */
    public static final int DAYWEEK_CLOSED_TIMESEGMENT = 3;
    /**
     * All day on a particular day of every week.
     */
    public static final int DAY = 4;
    /**
     * Will implement this monthly stuff when see the need.
     */
    static final int DAYCALENDARMONTH_OPENBEGINNING_TIMESEGMENT = 5;
    static final int DAYCALENDARMONTH_OPENEND_TIMESEGMENT = 6;
    static final int DAYCALENDARMONTH_CLOSED_TIMESEGMENT = 7;
    private ArrayList types = new ArrayList();
    private ArrayList segments = new ArrayList();
    /**
     * For matching purposes there are only two types
     */
    private static final int BADRESULT = -99;
    private static final int DAYWEEKTIMESEGMENT = 100;
    private static final int CLOSEDTIMESEGMENT = 101;

    public void add(int type, Timesegment timesegment)
    {
        if(type != DATE_CLOSED_TIMESEGMENT)
        {
            Err.error("expect type DATE_CLOSED_TIMESEGMENT");
        }
        if(timesegment.getStart() == null || timesegment.getEnd() == null)
        {
            Err.error("expect a specific begin/end timesegment");
        }
        types.add(new Integer(type));
        segments.add(timesegment);
    }

    public void add(int type, DayWeekTimesegment dayWeekTimesegment)
    {
        if(type != DAYWEEK_OPENBEGINNING_TIMESEGMENT
            && type != DAYWEEK_OPENEND_TIMESEGMENT
            && type != DAYWEEK_CLOSED_TIMESEGMENT)
        {
            Err.error("expect a type with a Timesegment");
        }
        types.add(new Integer(type));
        segments.add(dayWeekTimesegment);
    }

    public void add(int type, int dayOfWeek)
    {
        if(type != DAY)
        {
            Err.error("expect type DAY");
        }
        types.add(new Integer(type));
        segments.add(new DayWeekTimesegment(dayOfWeek));
    }

    public int size()
    {
        if(types.size() != segments.size())
        {
            Err.error("Timespace corrupted");
        }
        return types.size();
    }

    private static int getType(int type)
    {
        int result = BADRESULT;
        if(type == DAYWEEK_OPENBEGINNING_TIMESEGMENT
            || type == DAYWEEK_OPENEND_TIMESEGMENT
            || type == DAYWEEK_CLOSED_TIMESEGMENT || type == DAY)
        {
            result = DAYWEEKTIMESEGMENT;
        }
        else if(type == DATE_CLOSED_TIMESEGMENT)
        {
            result = CLOSEDTIMESEGMENT;
        }
        else
        {
            Err.error("Type not yet supported");
        }
        return result;
    }

    private static boolean matchTimesegmentWithTimesegment
        (Timesegment seg1,
         Timesegment seg2
        )
    {
        // debug
        // Err.pr( "");
        // Err.pr( "seg1 " + seg1);
        // Err.pr( "");
        // Err.pr( "seg2 " + seg2);
        //
        boolean result = false;
        if(
            (
                seg1.getStart().before(seg2.getEnd())
                    || seg1.getStart().equals(seg2.getEnd())
            )
                && (
                seg1.getEnd().after(seg2.getStart())
                    || seg1.getEnd().equals(seg2.getStart())
            )
            )
        {
            result = true;
        }
        return result;
    }

    private static boolean matchDayWeekWithDayWeek
        (DayWeekTimesegment seg1,
         DayWeekTimesegment seg2
        )
    {
        // debug
        // Err.pr( "");
        // Err.pr( "seg1 " + seg1);
        // Err.pr( "");
        // Err.pr( "seg2 " + seg2);
        //
        boolean result = false;
        if(seg1.getDayOfWeek() != seg2.getDayOfWeek())
        {
            result = false;
        }
        else if(
            (
                seg1.getStart().before(seg2.getEnd())
                    || seg1.getStart().equals(seg2.getEnd())
            )
                && (
                seg1.getEnd().after(seg2.getStart())
                    || seg1.getEnd().equals(seg2.getStart())
            )
            )
        {
            result = true;
        }
        return result;
    }

    /**
     * The closed is on a particular date. We can work out
     * what day this is and then call matchDayWeekWithDayWeek.
     */
    private static boolean matchClosedTimeSegmentWithDayWeek
        (Timesegment closed,
         DayWeekTimesegment dayWeek
        )
    {
        ArrayList weeklyBasis = closed.getDayWeekTimesegments();
        for(Iterator e = weeklyBasis.iterator(); e.hasNext();)
        {
            DayWeekTimesegment day = (DayWeekTimesegment) e.next();
            if(matchDayWeekWithDayWeek(day, dayWeek))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean matchSegments(int type1,
                                         Object seg1,
                                         int type2,
                                         Object seg2)
    {
        boolean result = false;
        if(getType(type1) == CLOSEDTIMESEGMENT
            && getType(type2) == CLOSEDTIMESEGMENT)
        {
            result = matchTimesegmentWithTimesegment((Timesegment) seg1,
                (Timesegment) seg2);
        }
        else if(getType(type1) == DAYWEEKTIMESEGMENT
            && getType(type2) == DAYWEEKTIMESEGMENT)
        {
            result = matchDayWeekWithDayWeek((DayWeekTimesegment) seg1,
                (DayWeekTimesegment) seg2);
        }
        else if(getType(type1) == CLOSEDTIMESEGMENT
            && getType(type2) == DAYWEEKTIMESEGMENT)
        {
            result = matchClosedTimeSegmentWithDayWeek((Timesegment) seg1,
                (DayWeekTimesegment) seg2);
        }
        else if(getType(type1) == DAYWEEKTIMESEGMENT
            && getType(type2) == CLOSEDTIMESEGMENT)
        {
            result = matchClosedTimeSegmentWithDayWeek((Timesegment) seg2,
                (DayWeekTimesegment) seg1);
        }
        else
        {
            Err.error("Combination of types not yet supported");
        }
        return result;
    }

    /**
     * For matching purposes there are currently two types
     * of segments - weekly occuring and date occuring.
     */
    public boolean match(Timespace timespace)
    {
        for(int i = 0; i <= size() - 1; i++)
        {
            for(int j = 0; j <= timespace.size() - 1; j++)
            {
                if(matchSegments(((Integer) types.get(i)).intValue(), segments.get(i),
                    ((Integer) timespace.types.get(j)).intValue(),
                    timespace.segments.get(j))
                    )
                {
                    return true;
                }
            }
        }
        return false;
    }

    static public Timespace valueOf(String s)
    {
        return new Timespace();
    }
} // end class Timespace
