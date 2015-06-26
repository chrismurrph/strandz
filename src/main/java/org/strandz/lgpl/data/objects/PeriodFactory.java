package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;

import java.util.Date;

/**
 * User: Chris
 * Date: 09/01/2009
 * Time: 3:16:33 PM
 */
public class PeriodFactory
{
    public enum PeriodEnum{ NULL, DAY, WEEK, MONTH, QUARTER};

    public static PeriodI newInstance( PeriodEnum periodEnum, Date date)
    {
        PeriodI result = null;
        if(periodEnum == null)
        {
            //ok
        }
        else if(periodEnum == PeriodEnum.WEEK)
        {
            result = WeekYear.newInstance( date);
        }
        else if(periodEnum == PeriodEnum.MONTH)
        {
            result = MonthYear.newInstance( date);
        }
        else if(periodEnum == PeriodEnum.QUARTER)
        {
            result = QuarterYear.newInstance( date);
        }
        else
        {
            Err.error( "Not yet implemented");
        }
        return result;
    }
}
