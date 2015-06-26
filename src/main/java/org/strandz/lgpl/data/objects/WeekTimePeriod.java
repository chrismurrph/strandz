package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;

import java.util.Date;

/**
 * TODO Get rid of this as does nothing but wrap!
 * User: Chris
 * Date: 20/11/2008
 * Time: 9:46:22 AM
 */
public class WeekTimePeriod implements PeriodI
{
    private WeekPeriod weekPeriod;

    public WeekTimePeriod( Date date)
    {
        weekPeriod = new WeekPeriod( date);
    }

    public boolean equals( Object other)
    {
        return weekPeriod.equals( ((WeekTimePeriod)other).weekPeriod);
    }

    public int hashCode()
    {
        return weekPeriod.hashCode();
    }

    public PeriodI previous()
    {
        return new WeekTimePeriod( weekPeriod.previous().getBeginPoint());
    }

    public PeriodI next()
    {
        return new WeekTimePeriod( weekPeriod.next().getBeginPoint());
    }

    public Date getBeginPoint()
    {
        return weekPeriod.getBeginPoint();
    }

    public Date getEndPoint()
    {
        Date result = null;
        Err.error( "Not yet implemented");
        return result;
    }

    public Date getMiddlePoint()
    {
        Date result = null;
        Err.error( "Not yet implemented");
        return result;
    }

    public PeriodFactory.PeriodEnum getPeriodEnum()
    {
        PeriodFactory.PeriodEnum result = null;
        Err.error( "Not yet implemented");
        return result;
    }

    public boolean greaterThan( Object o)
    {
        return weekPeriod.greaterThan( ((WeekTimePeriod)o).weekPeriod);
    }
}
