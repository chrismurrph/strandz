package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;

import java.util.Date;

/**
 * User: Chris
 * Date: 20/11/2008
 * Time: 9:45:15 AM
 */
public class DayTimePeriod implements PeriodI
{
    private DayPeriod dayPeriod;

    public DayTimePeriod( Date date)
    {
        dayPeriod = new DayPeriod( date);
    }

    public boolean equals(Object other)
    {
        return dayPeriod.equals( ((DayTimePeriod)other).dayPeriod);
    }

    public int hashCode()
    {
        return dayPeriod.hashCode();
    }

    public PeriodI previous()
    {
        return new DayTimePeriod( dayPeriod.previous().getBeginPoint());
    }

    public PeriodI next()
    {
        return new DayTimePeriod( dayPeriod.next().getBeginPoint());
    }

    public Date getBeginPoint()
    {
        return dayPeriod.getBeginPoint();
    }

    public Date getEndPoint()
    {
        Date result = dayPeriod.getEndPoint();
        return result;
    }

    public Date getMiddlePoint()
    {
        Date result = dayPeriod.getMiddlePoint();
        return result;
    }

    public PeriodFactory.PeriodEnum getPeriodEnum()
    {
        PeriodFactory.PeriodEnum result = PeriodFactory.PeriodEnum.DAY;
        return result;
    }

    public boolean greaterThan( Object o)
    {
        return dayPeriod.greaterThan( ((DayTimePeriod)o).dayPeriod);
    }
}
