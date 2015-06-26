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

import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.lgpl.store.DomainLookupEnum;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.lgpl.store.NotPersistedLookupsI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.NameableI;
import org.strandz.lgpl.util.Assert;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

public class WombatLookups implements LookupsI, NotPersistedLookupsI, Serializable //for debugging even thou fetch groups not working
{
    private List dayInWeeks;
    private List flexibilities;
    private List numDaysIntervals;
    private List monthInYears;
    private List overrides;
    private List seniorities;
    private List sexes;
    private List weekInMonths;
    private List whichShifts;

    /**
     * Only to be called when creating DemoData - always need to get these objects
     * from the DB otherwise
     */
    public void initValues()
    {
        setDayInWeeks( Utils.asArrayList(DayInWeek.OPEN_VALUES));
        setFlexibilities( Utils.asArrayList(Flexibility.OPEN_VALUES));
        setNumDaysIntervals( Utils.asArrayList(NumDaysInterval.OPEN_VALUES));
        setMonthInYears( Utils.asArrayList(MonthInYear.OPEN_VALUES));
        setOverrides( Utils.asArrayList(Override.OPEN_VALUES));
        setSeniorities( Utils.asArrayList(Seniority.OPEN_VALUES));
        setSexes( Utils.asArrayList(Sex.OPEN_VALUES));
        setWeekInMonths( Utils.asArrayList(WeekInMonth.OPEN_VALUES));
        setWhichShifts( Utils.asArrayList(WhichShift.OPEN_VALUES));
    }

    private List getDayInWeeks()
    {
        return dayInWeeks;
    }

    public void setDayInWeeks(List dayInWeeks)
    {
        this.dayInWeeks = dayInWeeks;
    }

    private List getFlexibilities()
    {
        return flexibilities;
    }

    private void setFlexibilities(List flexibilities)
    {
        this.flexibilities = flexibilities;
    }

    private List getNumDaysIntervals()
    {
        return numDaysIntervals;
    }

    private void setNumDaysIntervals(List numDaysIntervals)
    {
        this.numDaysIntervals = numDaysIntervals;
    }

    private List getMonthInYears()
    {
        return monthInYears;
    }

    private void setMonthInYears(List monthInYears)
    {
        this.monthInYears = monthInYears;
    }

    private List getOverrides()
    {
        return overrides;
    }

    private void setOverrides(List overrides)
    {
        this.overrides = overrides;
    }

    private List getSeniorities()
    {
        //return seniorities;
        //Err.pr( "getSeniorities() will be returning " + seniorities);
        return seniorities;
    }

    private void setSeniorities(List seniorities)
    {
        this.seniorities = seniorities;
        //Print.prList( this.seniorities, "setSeniorities() - have senior?");
        //Err.pr( "seniorities been set to " + this.seniorities);
    }

    private List getSexes()
    {
        return sexes;
    }

    private void setSexes(List sexes)
    {
        this.sexes = sexes;
    }

    private List getWeekInMonths()
    {
        return weekInMonths;
    }

    private void setWeekInMonths(List weekInMonths)
    {
        this.weekInMonths = weekInMonths;
    }

    private List getWhichShifts()
    {
        return whichShifts;
    }

    private void setWhichShifts(List whichShifts)
    {
        this.whichShifts = whichShifts;
    }

    public Object getByName( WombatDomainLookupEnum enumId, String name)
    {
        Object result = null;
        Assert.notNull( name);
        List<NameableI> list = get( enumId);
        for(Iterator<NameableI> iterator = list.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if(obj instanceof NameableI)
            {
                NameableI value = (NameableI)obj;
                String n = value.getName();
                if(Utils.equals( name, n))
                {
                    result = value;
                    break;
                }
            }
            else
            {
                Err.error( "Enumeration does not implement NameableI: " + obj.getClass().getName());
            }
        }
        return result;
    }

    public List get(DomainLookupEnum enumId)
    {
        List result = null;
        if(enumId == WombatDomainLookupEnum.ALL_DAY_IN_WEEK)
        {
            result = getDayInWeeks();
        }
        else if(enumId == WombatDomainLookupEnum.ALL_FLEXIBILITY)
        {
            result = getFlexibilities();
        }
        else if(enumId == WombatDomainLookupEnum.ALL_INTERVAL)
        {
            result = getNumDaysIntervals();
        }
        else if(enumId == WombatDomainLookupEnum.ALL_MONTH_IN_YEAR)
        {
            result = getMonthInYears();
        }
        else if(enumId == WombatDomainLookupEnum.ALL_OVERRIDE)
        {
            result = getOverrides();
        }
        else if(enumId == WombatDomainLookupEnum.ALL_SENIORITY)
        {
            result = getSeniorities();
        }
        else if(enumId == WombatDomainLookupEnum.ALL_SEX)
        {
            result = getSexes();
        }
        else if(enumId == WombatDomainLookupEnum.ALL_WEEK_IN_MONTH)
        {
            result = getWeekInMonths();
        }
        else if(enumId == WombatDomainLookupEnum.ALL_WHICH_SHIFT)
        {
            result = getWhichShifts();
        }
        else
        {
            Err.error( "Not yet implemented - " + enumId);
        }
        return result;
    }
}
