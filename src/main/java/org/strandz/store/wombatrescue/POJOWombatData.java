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
package org.strandz.store.wombatrescue;

import org.strandz.data.wombatrescue.objects.BuddyManager;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.data.objects.UserDetails;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.data.wombatrescue.objects.NumDaysInterval;
import org.strandz.data.wombatrescue.objects.Override;
import org.strandz.data.wombatrescue.objects.RosterSlot;
import org.strandz.data.wombatrescue.objects.Seniority;
import org.strandz.data.wombatrescue.objects.Sex;
import org.strandz.data.wombatrescue.objects.WeekInMonth;
import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.lgpl.util.Err;

public class POJOWombatData
{
    public static final Class WORKER = Worker.class;
    public static final Class ROSTER_SLOT = RosterSlot.class;
    public static final Class BUDDY_MANAGER = BuddyManager.class;
    public static final Class DAY_IN_WEEK = DayInWeek.class;
    public static final Class INTERVAL = NumDaysInterval.class;
    public static final Class USER = UserDetails.class;
    public static final Class LOOKUPS = WombatLookups.class;    
    
    public static final Class WEEK_IN_MONTH = WeekInMonth.class;
    public static final Class WHICH_SHIFT = WhichShift.class;
    public static final Class MONTH_IN_YEAR = MonthInYear.class;
    public static final Class SENIORITY = Seniority.class;
    public static final Class SEX = Sex.class;
    public static final Class FLEXIBILITY = Flexibility.class;
    public static final Class OVERRIDE = Override.class;

    /**
     * Order of these important if have RI constraints on. To see if they are on:
     * SHOW CREATE TABLE Worker;
     * In MySql.
     * Theoretically flush s/be smart enough to delete objects in the right order to
     * navigate around FK constraints, even for when Worker refers to itself. Our
     * slightly tacky solution is to not have integrity constraints for Worker->Worker
     */
    public static final Class[] CLASSES = {
        WORKER,
        BUDDY_MANAGER, ROSTER_SLOT, USER, LOOKUPS,
        DAY_IN_WEEK, INTERVAL, WEEK_IN_MONTH,
        WHICH_SHIFT, MONTH_IN_YEAR, SENIORITY, SEX, FLEXIBILITY, OVERRIDE,
    };

    public static final Class[] NON_LOOKUP_CLASSES = {
        WORKER,
        BUDDY_MANAGER,
        ROSTER_SLOT,
        //USER,    
        LOOKUPS,    
    };

    public static final Class[] LOOKUP_CLASSES = {
            DAY_IN_WEEK, INTERVAL, WEEK_IN_MONTH,
            WHICH_SHIFT, MONTH_IN_YEAR, SENIORITY, SEX, FLEXIBILITY, OVERRIDE,
    };

    public static Object[] getLookupValues( Class clazz)
    {
        Object[] result = null;
        if(clazz == DAY_IN_WEEK)
        {
            result = DayInWeek.OPEN_VALUES;
        }
        else if(clazz == INTERVAL)
        {
            result = NumDaysInterval.OPEN_VALUES;
        }
        else if(clazz == WEEK_IN_MONTH)
        {
            result = WeekInMonth.OPEN_VALUES;
        }
        else if(clazz == WHICH_SHIFT)
        {
            result = WhichShift.OPEN_VALUES;
        }
        else if(clazz == MONTH_IN_YEAR)
        {
            result = MonthInYear.OPEN_VALUES;
        }
        else if(clazz == SENIORITY)
        {
            result = Seniority.OPEN_VALUES;
        }
        else if(clazz == SEX)
        {
            result = Sex.OPEN_VALUES;
        }
        else if(clazz == FLEXIBILITY)
        {
            result = Flexibility.OPEN_VALUES;
        }
        else if(clazz == OVERRIDE)
        {
            result = Override.OPEN_VALUES;
        }
        else
        {
            Err.error( "Unknown class: " + clazz.getName());
        }
        return result;
    }

    public static boolean isLookupDomainObject( Class clazz)
    {
        boolean result = true;
        if(clazz == WORKER || clazz == BUDDY_MANAGER || clazz == ROSTER_SLOT || clazz == USER)
        {
            result = false;
        }
        return result;
    }
}
