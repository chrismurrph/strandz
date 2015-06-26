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

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.TimeUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class MonthInYear implements Comparable, Serializable, MonthInYearI
{
    private int pkId; // primary-key=true
    private String name;
    private int ordinal;
    private transient static int timesConstructed;
    private transient static final int maxConstructions = 12;
    public transient int id;

    private static final String MONTHS[] = new String[12];

    static
    {
        MONTHS[0] = "Jan";
        MONTHS[1] = "Feb";
        MONTHS[2] = "Mar";
        MONTHS[3] = "Apr";
        MONTHS[4] = "May";
        MONTHS[5] = "Jun";
        MONTHS[6] = "Jul";
        MONTHS[7] = "Aug";
        MONTHS[8] = "Sep";
        MONTHS[9] = "Oct";
        MONTHS[10] = "Nov";
        MONTHS[11] = "Dec";
    }

    public MonthInYear()
    {
        timesConstructed++;
        id = timesConstructed;
        if(id > maxConstructions)
        {// Err.error( "Constructed too many times, MonthInYear " + id);
        }
    }

    private MonthInYear(String name, int ordinal, int pkId)
    {
        this();
        this.name = name;
        this.ordinal = ordinal;
        this.pkId = pkId;
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
        Utils.chkType(o, MonthInYearI.class);

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof MonthInYearI))
        {// nufin
        }
        else
        {
            MonthInYearI test = (MonthInYearI) o;
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

    public String getName()
    {
        return name;
    }

    public Integer getOrdinal()
    {
        return ordinal;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setOrdinal(int ordinal)
    {
        this.ordinal = ordinal;
    }

    /**
     * These two methods can be used to compare with
     * Calendar.JANUARY etc.
     */
    public static MonthInYear fromOrdinal(int i)
    {
        return CLOSED_VALUES[i];
    }

    public static MonthInYear fromLongName( String name)
    {
        MonthInYear result = null;
        Assert.notNull( name);
        for (int i = 0; i < CLOSED_VALUES.length; i++)
        {
            MonthInYear closedValue = CLOSED_VALUES[i];
            if(name.equalsIgnoreCase( closedValue.getName()))
            {
                result = closedValue;
            }
        }
        return result;
    }

    public static MonthInYear fromShortName( String name)
    {
        MonthInYear result = null;
        Assert.notNull( name);
        for (int i = 0; i < MONTHS.length; i++)
        {
            if(name.equalsIgnoreCase( MONTHS[i]))
            {
                result = CLOSED_VALUES[i];
            }
        }
        return result;
    }

    public static List<String> monthNames()
    {
        List<String> result = new ArrayList<String>(CLOSED_VALUES.length);
        for (int i = 0; i < CLOSED_VALUES.length; i++)
        {
            MonthInYear closedValue = CLOSED_VALUES[i];
            result.add( closedValue.getName());
        }
        return result;
    }

    public Date getBeginDate( int year)
    {
        Date result = null;
        Calendar firstDayOfMonth = TimeUtils.getFirstDayInMonth( getOrdinal(), year, 1);
        return result;
    }

    public Date getEndDate( int year)
    {
        Date result = null;
        Date endOfFirstDayOfMonth;
        return result;
    }

    /**
     * JANUARY is 0 in Java, so these ordinal values are compatible.
     */
    public static final MonthInYear NULL = new MonthInYear();
    public static final MonthInYear JANUARY = new MonthInYear("January", 0, 1);
    public static final MonthInYear FEBRUARY = new MonthInYear("February", 1, 2);
    public static final MonthInYear MARCH = new MonthInYear("March", 2, 3);
    public static final MonthInYear APRIL = new MonthInYear("April", 3, 4);
    public static final MonthInYear MAY = new MonthInYear("May", 4, 5);
    public static final MonthInYear JUNE = new MonthInYear("June", 5, 6);
    public static final MonthInYear JULY = new MonthInYear("July", 6, 7);
    public static final MonthInYear AUGUST = new MonthInYear("August", 7, 8);
    public static final MonthInYear SEPTEMBER = new MonthInYear("September", 8, 9);
    public static final MonthInYear OCTOBER = new MonthInYear("October", 9, 10);
    public static final MonthInYear NOVEMBER = new MonthInYear("November", 10, 11);
    public static final MonthInYear DECEMBER = new MonthInYear("December", 11, 12);
    public static final MonthInYear[] CLOSED_VALUES = {
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER,
        NOVEMBER, DECEMBER};
    public static final MonthInYear[] OPEN_VALUES = {
        NULL, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER,
        OCTOBER, NOVEMBER, DECEMBER};

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

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }

    public static String getPeriodAsMonthYearDisplay( int month, int year)
    {
        String result;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        String yearStr = TimeUtils.YEAR_FORMAT.format(calendar.getTime());
        String monthStr;
        monthStr = MONTHS[month];
        result = monthStr + "-" + yearStr;
        //Err.pr( "From month " + month + " will ret " + result);
        return result;
    }
}
