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
package org.strandz.data.wombatrescue.objects;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;

import java.io.Serializable;

/**
 * monthly restarters, week of month
 */
public class WeekInMonth implements Comparable, Serializable, WeekInMonthI
{
    private int pkId; // primary-key=true
    private String name;
    private transient static int timesConstructed;
    private transient static final int maxConstructions = 5;
    public transient int id;

    private WeekInMonth(String name, int pkId)
    {
        this();
        this.name = name;
        this.pkId = pkId;
    }

    public WeekInMonth()
    {
        timesConstructed++;
        id = timesConstructed;
        if(id > maxConstructions)
        {// Err.error( "Constructed too many times, WeekInMonth " + id);
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
        else if(!(o instanceof WeekInMonthI))
        {// nufin
        }
        else
        {
            WeekInMonthI test = (WeekInMonthI) o;
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
     * These two methods can be used to compare with
     * int get when dealing with java WEEK_IN_MONTHs
     */
    public static WeekInMonth fromOrdinal(int i)
    {
        // Err.pr( "Being asked for week: " + i);
        return OPEN_VALUES[i];
    }

    public static final WeekInMonth NULL = new WeekInMonth();
    public static final WeekInMonth FIRST_OF_MONTH = new WeekInMonth(
        "first of month", 1);
    public static final WeekInMonth SECOND_OF_MONTH = new WeekInMonth(
        "second of month", 2);
    public static final WeekInMonth THIRD_OF_MONTH = new WeekInMonth(
        "third of month", 3);
    public static final WeekInMonth FOURTH_OF_MONTH = new WeekInMonth(
        "fourth of month", 4);
    public static final WeekInMonth FIFTH_OF_MONTH = new WeekInMonth(
        "fifth of month", 5);
    /*
    public static final WeekInMonth FIRST_OF_MONTH()
    {
    return new WeekInMonth( "first of month");
    }
    public static final WeekInMonth SECOND_OF_MONTH()
    {
    return new WeekInMonth( "second of month");
    }
    public static final WeekInMonth THIRD_OF_MONTH()
    {
    return new WeekInMonth( "third of month");
    }
    public static final WeekInMonth FOURTH_OF_MONTH()
    {
    return new WeekInMonth( "fourth of month");
    }
    public static final WeekInMonth FIFTH_OF_MONTH()
    {
    return new WeekInMonth( "fifth of month");
    }
    */

    public static final WeekInMonth[] OPEN_VALUES = {
        NULL, FIRST_OF_MONTH, SECOND_OF_MONTH, THIRD_OF_MONTH, FOURTH_OF_MONTH,
        FIFTH_OF_MONTH};

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getPkId()
    {
        return pkId;
    }

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }

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
}
