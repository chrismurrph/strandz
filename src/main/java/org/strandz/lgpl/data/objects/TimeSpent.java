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
import org.strandz.lgpl.util.Utils;

import java.text.ParseException;

public class TimeSpent
{
    private Integer hours;
    private Integer minutes;
    // used as temp vars for passing values
    private static Integer firstInt;
    private static Integer secondInt;
    public static TimeSpent NULL = TimeSpent.newInstance("00:00");

    public static void main(String[] args)
    {
        TimeSpent time1 = new TimeSpent(new Integer(1), new Integer(5));
        Err.pr(time1.toString());

        TimeSpent time2 = TimeSpent.newInstance("01:55");
        Err.pr(time2.toString());
    }

    public TimeSpent()
    {
    }

    public TimeSpent(Integer hours, Integer minutes)
    {
        this.hours = hours;
        this.minutes = minutes;
    }

    public TimeSpent(String str)
    {
        try
        {
            valuesFromStr(str);
        }
        catch(ParseException ex)
        {
            Err.error(ex);
        }
        this.hours = firstInt;
        this.minutes = secondInt;
    }

    public static TimeSpent newInstance(String timeStr)
    {
        TimeSpent result = null;
        try
        {
            result = TimeSpent.parse(timeStr);
        }
        catch(ParseException ex)
        {// Err.error( ex);
        }
        return result;
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof TimeSpent))
        {// nufin
        }
        else
        {
            TimeSpent test = (TimeSpent) o;
            if((hours == null ? test.hours == null : hours.equals(test.hours)))
            {
                if((minutes == null
                    ? test.minutes == null
                    : minutes.equals(test.minutes)))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (hours == null ? 0 : hours.hashCode());
        result = 37 * result + (minutes == null ? 0 : minutes.hashCode());
        return result;
    }

    public String toString()
    {
        return toStringBuffer().toString();
    }

    private StringBuffer toStringBuffer()
    {
        StringBuffer result = new StringBuffer();
        result.append(Utils.leftPadZero(hours.toString(), 2));
        result.append(":");
        result.append(Utils.leftPadZero(minutes.toString(), 2));
        return result;
    }

    public static StringBuffer format(Object timeSpent)
    {
        StringBuffer result = ((TimeSpent) timeSpent).toStringBuffer();
        return result;
    }

    private static void valuesFromStr(String timeStr) throws ParseException
    {
        if(timeStr.length() != 5)
        {
            throw new ParseException(
                "TimeSpent needs to be in format HH:MM: <" + timeStr + ">", 0);
        }

        int colonIndex = timeStr.indexOf(':');
        if(colonIndex == -1)
        {
            throw new ParseException("TimeSpent needs a : sign: <" + timeStr + ">", 0);
        }
        if(colonIndex != 2)
        {
            throw new ParseException(
                "TimeSpent needs a : sign as the third character: <" + timeStr + ">",
                0);
        }

        String first = timeStr.substring(0, colonIndex);
        String second = timeStr.substring(colonIndex + 1);
        if(first.length() != 2)
        {
            throw new ParseException(
                "TimeSpent requires two digits for hours, got [" + first + "]: <"
                    + timeStr + ">",
                colonIndex);
        }
        if(second.length() != 2)
        {
            throw new ParseException(
                "TimeSpent requires two digits for minutes, got [" + second + "]: <"
                    + timeStr + ">",
                colonIndex);
        }
        try
        {
            firstInt = Integer.valueOf(first);
        }
        catch(NumberFormatException ex)
        {
            throw new ParseException(
                "TimeSpent requires hours to be in number form, got [" + first
                    + "]: <" + timeStr + ">",
                colonIndex);
        }
        try
        {
            secondInt = Integer.valueOf(second);
        }
        catch(NumberFormatException ex)
        {
            throw new ParseException(
                "TimeSpent requires minutes to be in number form, got [" + second
                    + "]: <" + timeStr + ">",
                colonIndex);
        }
    }

    public static TimeSpent parse(String timeStr) throws ParseException
    {
        TimeSpent result = null;
        valuesFromStr(timeStr);
        result = new TimeSpent(firstInt, secondInt);
        return result;
    }

    public TimeSpent add(TimeSpent time)
    {
        TimeSpent result = null;
        Err.error("Not yet implemented TimeSpent add( TimeSpent time)");
        return result;
    }

    public Integer getHours()
    {
        return hours;
    }

    public void setHours(Integer hours)
    {
        this.hours = hours;
    }

    public Integer getMinutes()
    {
        return minutes;
    }

    public void setMinutes(Integer minutes)
    {
        this.minutes = minutes;
    }
}
