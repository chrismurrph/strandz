package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Utils;

import java.util.Date;

/**
 * User: Chris
 * Date: 20/11/2008
 * Time: 10:11:17 AM
 */
public interface PeriodI
{
    PeriodI next();
    PeriodI previous();
    Date getEndPoint();
    Date getBeginPoint();
    Date getMiddlePoint();
    PeriodFactory.PeriodEnum getPeriodEnum();
    boolean equals(Object other);
    int hashCode();
    boolean greaterThan( Object o);
    //Only required by reporting ones
    //int howManyBetween( PeriodI other);
}
