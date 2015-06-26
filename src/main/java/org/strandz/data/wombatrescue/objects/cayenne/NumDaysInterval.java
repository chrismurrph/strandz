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
import org.strandz.data.wombatrescue.objects.cayenne.auto._NumDaysInterval;
import org.strandz.data.wombatrescue.objects.NumDaysIntervalI;

import java.io.Serializable;

/**
 * non monthly restarters, interval of time between roster slots
 */
public class NumDaysInterval extends _NumDaysInterval implements Comparable, Serializable, NumDaysIntervalI
{
    private int pkId; // primary-key=true
    private transient static int timesConstructed;
    private transient static final int maxConstructions = 4;
    public transient int id;

    private NumDaysInterval(String name, int days, int pkId)
    {
        this();
        setName( name);
        setActualDays( days);
        this.pkId = pkId;
    }

    public NumDaysInterval()
    {
        timesConstructed++;
        id = timesConstructed;
        if(id > maxConstructions)
        {// Err.error( "Constructed too many times, Interval " + id);
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
        Utils.chkType(o, NumDaysIntervalI.class);

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof NumDaysIntervalI))
        {// nufin
        }
        else
        {
            NumDaysIntervalI test = (NumDaysIntervalI) o;
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
     * No point in having a weekly interval. Can achieve same result
     * by having five RosterSlots.
     * public static final Interval WEEKLY = new Interval( "weekly");
     */

    public transient static final NumDaysInterval NULL = new NumDaysInterval();
    public transient static final NumDaysInterval WEEKLY = new NumDaysInterval("weekly", 7, 1);
    public transient static final NumDaysInterval FORTNIGHTLY = new NumDaysInterval("fortnightly", 14, 2);
    public transient static final NumDaysInterval THREE_WEEKLY = new NumDaysInterval("three weekly", 21,
        3);
    public transient static final NumDaysInterval FOUR_WEEKLY = new NumDaysInterval("four weekly", 28, 4);
    /*
    public static final Interval WEEKLY()
    {
    return new Interval( "weekly", 7);
    }
    public static final Interval FORTNIGHTLY()
    {
    return new Interval( "fortnightly", 14);
    }
    public static final Interval THREE_WEEKLY()
    {
    return new Interval( "three weekly", 21);
    }
    public static final Interval FOUR_WEEKLY()
    {
    return new Interval( "four weekly", 28);
    }
    */

    /**
     * No point in having a monthly interval. If was monthly then
     * RosterSlot would be a monthly restart, and thus RosterSlot
     * would not need to have an interval of a month! Instead
     * RosterSlot would have a value for weekInMonth.
     * public static final Interval MONTHLY = new Interval( "monthly");
     */

    public transient static final NumDaysInterval[] OPEN_VALUES = {
        NULL, WEEKLY, FORTNIGHTLY, THREE_WEEKLY, FOUR_WEEKLY};

//    public int getPkId()
//    {
//        return pkId;
//    }

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }

    public int getDays()
    {
        return getActualDays();
    }
}