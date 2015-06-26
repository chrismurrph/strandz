package org.strandz.data.wombatrescue.calculated;

import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.data.wombatrescue.objects.cayenne.DayInWeek;
import org.strandz.data.wombatrescue.objects.cayenne.NumDaysInterval;
import org.strandz.data.wombatrescue.objects.cayenne.Flexibility;
import org.strandz.data.wombatrescue.objects.cayenne.Seniority;
import org.strandz.data.wombatrescue.objects.cayenne.MonthInYear;
import org.strandz.data.wombatrescue.objects.cayenne.Sex;
import org.strandz.data.wombatrescue.objects.cayenne.WeekInMonth;
import org.strandz.data.wombatrescue.objects.cayenne.WhichShift;
import org.strandz.lgpl.store.NotPersistedLookupsI;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.lgpl.store.DomainLookupEnum;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.List;

/**
 * Only used by Cayenne
 */
public class CayenneWombatLookups extends WombatLookups implements LookupsI, NotPersistedLookupsI
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

    private DomainQueriesI queriesI;

    public CayenneWombatLookups( DomainQueriesI queriesI)
    {
        this.queriesI = queriesI;
    }

    public void initValues()
    {
        setDayInWeeks( Utils.asArrayList(DayInWeek.OPEN_VALUES));
        setFlexibilities( Utils.asArrayList(Flexibility.OPEN_VALUES));
        setNumDaysIntervals( Utils.asArrayList(NumDaysInterval.OPEN_VALUES));
        setMonthInYears( Utils.asArrayList(MonthInYear.OPEN_VALUES));
        setOverrides( Utils.asArrayList(org.strandz.data.wombatrescue.objects.cayenne.Override.OPEN_VALUES));
        setSeniorities( Utils.asArrayList(Seniority.OPEN_VALUES));
        setSexes( Utils.asArrayList(Sex.OPEN_VALUES));
        setWeekInMonths( Utils.asArrayList(WeekInMonth.OPEN_VALUES));
        setWhichShifts( Utils.asArrayList(WhichShift.OPEN_VALUES));
    }

    public List get(DomainLookupEnum enumId)
    {
        List result = null;
        //Err.error( "Not implemented");
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

    public List getDayInWeeks()
    {
        if(dayInWeeks == null)
        {
            dayInWeeks = queriesI.executeRetList(WombatDomainQueryEnum.DAY_IN_WEEK);
        }
        return dayInWeeks;
    }

    public List getFlexibilities()
    {
        if(flexibilities == null)
        {
            flexibilities = queriesI.executeRetList(WombatDomainQueryEnum.FLEXIBILITY);
        }
        return flexibilities;
    }

    public List getNumDaysIntervals()
    {
        if(numDaysIntervals == null)
        {
            numDaysIntervals = queriesI.executeRetList(WombatDomainQueryEnum.NUM_DAYS_INTERVAL);
        }
        return numDaysIntervals;
    }

    public List getMonthInYears()
    {
        if(monthInYears == null)
        {
            monthInYears = queriesI.executeRetList(WombatDomainQueryEnum.MONTH_IN_YEAR);
        }
        return monthInYears;
    }

    public List getOverrides()
    {
        if(overrides == null)
        {
            overrides = queriesI.executeRetList(WombatDomainQueryEnum.OVERRIDE);
        }
        return overrides;
    }

    public List getSeniorities()
    {
        if(seniorities == null)
        {
            seniorities = queriesI.executeRetList(WombatDomainQueryEnum.SENIORITY);
        }
        return seniorities;
    }

    public List getSexes()
    {
        if(sexes == null)
        {
            sexes = queriesI.executeRetList(WombatDomainQueryEnum.SEX);
        }
        return sexes;
    }

    public List getWeekInMonths()
    {
        if(weekInMonths == null)
        {
            weekInMonths = queriesI.executeRetList(WombatDomainQueryEnum.WEEK_IN_MONTH);
        }
        return weekInMonths;
    }

    public List getWhichShifts()
    {
        if(whichShifts == null)
        {
            whichShifts = queriesI.executeRetList(WombatDomainQueryEnum.WHICH_SHIFT);
        }
        return whichShifts;
    }

    public void setDayInWeeks(List dayInWeeks)
    {
        this.dayInWeeks = dayInWeeks;
    }

    public void setFlexibilities(List flexibilities)
    {
        this.flexibilities = flexibilities;
    }

    public void setNumDaysIntervals(List numDaysIntervals)
    {
        this.numDaysIntervals = numDaysIntervals;
    }

    public void setMonthInYears(List monthInYears)
    {
        this.monthInYears = monthInYears;
    }

    public void setOverrides(List overrides)
    {
        this.overrides = overrides;
    }

    public void setSeniorities(List seniorities)
    {
        this.seniorities = seniorities;
    }

    public void setSexes(List sexes)
    {
        this.sexes = sexes;
    }

    public void setWeekInMonths(List weekInMonths)
    {
        this.weekInMonths = weekInMonths;
    }

    public void setWhichShifts(List whichShifts)
    {
        this.whichShifts = whichShifts;
    }
}



