package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;

import java.util.Date;

/**
 * User: Chris
 * Date: 20/11/2008
 * Time: 9:50:21 AM
 */
public class MondayWeekTimePeriod implements PeriodI
{
    private MondayWeekPeriod mondayWeek;

    public MondayWeekTimePeriod( Date date)
    {
        mondayWeek = new MondayWeekPeriod( date);
    }

    public boolean equals(Object other)
    {
        return mondayWeek.equals( ((MondayWeekTimePeriod)other).mondayWeek);
    }

    public int hashCode()
    {
        return mondayWeek.hashCode();
    }

    public PeriodI previous()
    {
        return new MondayWeekTimePeriod( mondayWeek.previous().getBeginPoint());
    }

    public PeriodI next()
    {
        return new MondayWeekTimePeriod( mondayWeek.next().getBeginPoint());
    }

    public Date getBeginPoint()
    {
        return mondayWeek.getBeginPoint();
    }

    public Date getEndPoint()
    {
        return mondayWeek.getEndPoint();
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
        return mondayWeek.greaterThan( ((MondayWeekTimePeriod)o).mondayWeek);
    }
}
