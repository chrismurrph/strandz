package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.Date;

/**
 * User: Chris
 * Date: 24/01/2009
 * Time: 2:20:10 AM
 */
public class PeriodUtils
{
    public static int howManyBetween( PeriodI thisPeriod, PeriodI laterPeriod)
    {
        int result = 0;
        if (thisPeriod.greaterThan( laterPeriod))
        {
            Err.error("laterPeriod " + laterPeriod + " must be greater than thisPeriod " + thisPeriod);
        }
        PeriodI nextPeriod = thisPeriod;
        while (true)
        {
            if(nextPeriod.equals( laterPeriod))
            {
                break;
            }
            nextPeriod = nextPeriod.next();
            result++;
        }
        return result;
    }

    public static PeriodI toCurrent( PeriodI source, PeriodFactory.PeriodEnum periodEnum)
    {
        PeriodI result;
        /* Actually it can make sense...
        if(source.getPeriodEnum() == periodEnum)
        {
            Err.error( "Makes no sense to go to current " + periodEnum + " when already at " + source.getPeriodEnum());
        }
        else
        */
        {
            Date middleTime = source.getMiddlePoint();
            result = PeriodFactory.newInstance( periodEnum, middleTime);
        }
        return result;
    }

    public static PeriodI toPrevious( PeriodI source, PeriodFactory.PeriodEnum periodEnum)
    {
        PeriodI result;
        if(source.getPeriodEnum() == periodEnum)
        {
            result = source.previous();
        }
        else
        {
            Date beginTime = source.getBeginPoint();
            result = PeriodFactory.newInstance( periodEnum, beginTime).previous();
        }
        return result;
    }

    public static PeriodI toNext( PeriodI source, PeriodFactory.PeriodEnum periodEnum)
    {
        PeriodI result;
        if(source.getPeriodEnum() == periodEnum)
        {
            result = source.next();
        }
        else
        {
            Date endTime = source.getEndPoint();
            result = PeriodFactory.newInstance( periodEnum, endTime).next();
        }
        return result;
    }

}
