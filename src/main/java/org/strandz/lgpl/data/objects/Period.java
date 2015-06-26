package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Chris
 * Date: 17/11/2008
 * Time: 8:56:17 AM
 */
abstract public class Period implements Comparable, PeriodI
{
    protected Calendar calendar;

    public Period( Calendar calendar)
    {
        this.calendar = Calendar.getInstance();
        this.calendar.setTime( calendar.getTime());
        chk();
    }

    public Period( Date date)
    {
        this.calendar = Calendar.getInstance();
        this.calendar.setTime( date);
        chk();
    }

    public boolean equals(Object other)
    {
        return calendar.equals( ((Period)other).calendar);
    }

    public int hashCode()
    {
        return calendar.hashCode();
    }

    public Date getBeginPoint()
    {
        return calendar.getTime();
    }

    public Date getMiddlePoint()
    {
        return TimeUtils.getMiddlePoint( getBeginPoint(), getEndPoint());
    }

    public String toString()
    {
        return calendar.getTime().toString();
    }

    public boolean lessThan( Object o)
    {
        return calendar.getTime().compareTo( ((Period)o).calendar.getTime()) == -1;
    }

    public boolean greaterThan( Object o)
    {
        return calendar.getTime().compareTo( ((Period)o).calendar.getTime()) == 1;
    }

    public int compareTo( Object o)
    {
        return calendar.getTime().compareTo( ((Period)o).calendar.getTime());
    }

    abstract void chk();
    abstract Period createClone();
    abstract public PeriodI next();
    abstract public PeriodI previous();
}
