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
package org.strandz.data.wombatrescue.objects.cayenne;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.data.wombatrescue.objects.cayenne.auto._MonthInYear;

import java.io.Serializable;

public class MonthInYear extends _MonthInYear implements Comparable, Serializable, MonthInYearI
{
    private int pkId; // primary-key=true
    private int ordinal;
    private transient static int timesConstructed;
    private transient static final int maxConstructions = 12;
    public transient int id;

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
        this.setName( name);
        this.ordinal = ordinal;
        this.pkId = pkId;
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
     * These two methods can be used to compare with
     * Calendar.JANUARY etc.
     */
    public static MonthInYearI fromOrdinal(int i)
    {
        return CLOSED_VALUES[i];
    }
    /*
    public boolean containsDate( Date date)
    {
    boolean result = false;
    Calendar cal = Calendar.getInstance();
    cal.setTime( date);
    int month = cal.get( Calendar.MONTH);
    MonthInYear monthInYear = fromOrdinal( month);
    if(this.equals( monthInYear))
    {
    result = true;
    }
    return result;
    }
    public boolean betweenOrIncludesDates( Date date1, Date date2)
    {
    boolean result = false;
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime( date1);
    int month1 = cal1.get( Calendar.MONTH);
    MonthInYear monthInYear1 = fromOrdinal( month1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime( date2);
    int month2 = cal2.get( Calendar.MONTH);
    MonthInYear monthInYear2 = fromOrdinal( month2);
    if(monthInYear2.compareTo( monthInYear1) < 0) //same month is ok
    {
    Err.pr( "date1: " + date1);
    Err.pr( "date2: " + date2);
    Err.error( "Expected arg date2 to be greater than arg date1: " + monthInYear2.compareTo( monthInYear1));
    }
    if(this.equals( monthInYear1))
    {
    result = true;
    }
    else if(this.equals( monthInYear2))
    {
    result = true;
    }
    else if( this.compareTo( monthInYear1) >= 0 &&
    this.compareTo( monthInYear2) <= 0)
    {
    result = true;
    }
    return result;
    }
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

//    public int getPkId()
//    {
//        return pkId;
//    }

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }
}