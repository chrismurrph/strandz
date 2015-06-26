package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.TimeUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * See TestDates instead for unit tests
 *
 * User: chris
 * Date: 11/07/2005
 * Time: 14:44:10
 */
public class MonthYear implements PeriodI
{
    /**
     * The month that the user is viewing. January is 0, as that's
     * the way java months work.
     */
    private int month = Utils.UNSET_INT;
    /**
     * the year that the user is viewing
     */
    private int year = Utils.UNSET_INT;

    public void chkHaveDate()
    {
        if (month == Utils.UNSET_INT)
        {
            Err.error("month not set");
        }
        if (year == Utils.UNSET_INT)
        {
            Err.error("year not set");
        }
    }

    /**
     * The concept of 'the current time' needs to be quite dynamic so
     * that unit tests can vary the time, thus this is not a nullary.
     */
    /*
    public static MonthYear newInstance( Calendar calendar)
    {
        MonthYear result = null;
        //Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get( Calendar.MONTH);
        int currentYear = calendar.get( Calendar.YEAR);
        result = new MonthYear( currentMonth, currentYear);
        return result;
    }
    */

    public static MonthYear newInstance( Date date)
    {
        return newInstance( date, false);
    }

    /**
     * @param date
     */
    public static MonthYear newInstance( Date date, boolean extraDay)
    {
        MonthYear result;
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        Date dayToUse;
        if(extraDay)
        {
            dayToUse = TimeUtils.addDays( date, -1);
        }
        else
        {
            dayToUse = date;
        }
        calendar.setTime( dayToUse);
        int currentMonth = calendar.get( Calendar.MONTH);
        int currentYear = calendar.get( Calendar.YEAR);
        result = new MonthYear( currentMonth, currentYear);
        //Err.pr( ProdKpiNote.DATA_TO_GRAPH, "From end date of " + endDateOf + ", get month " + result.month);
        return result;
    }

    public MonthYear(int month, int year)
    {
        this.month = month;
        this.year = year;
    }

    public String toString()
    {
        return "month: " + month + ", year: " + year;
    }

    public Date getBeginPoint()
    {
        Date result;
//        Calendar calendar = Calendar.getInstance();
//        DateUtils.clearCalendar( calendar);
//        calendar.set( Calendar.MONTH, month);
//        calendar.set( Calendar.YEAR, year);
        result = TimeUtils.getDateFromDayMonthYear( 1, month, year, true);
        return result;
    }

    public Date getEndPoint()
    {
        Date result;
        PeriodI next = next();
        result = next.getBeginPoint();
        return result;
    }

    public Date getMiddlePoint()
    {
        return TimeUtils.getMiddlePoint( getBeginPoint(), getEndPoint());
    }

    public PeriodFactory.PeriodEnum getPeriodEnum()
    {
        PeriodFactory.PeriodEnum result = PeriodFactory.PeriodEnum.MONTH;
        return result;
    }

    public boolean equals(Object other)
    {
        MonthYear otherMY = (MonthYear) other;
        return Utils.equals(this.month, otherMY.month) &&
            Utils.equals(this.year, otherMY.year);
    }

    public int hashCode()
    {
        int result = 17;
        result = Utils.hashCode(result, month);
        result = Utils.hashCode(result, year);
        return result;
    }

    public boolean greaterThan(Object obj)
    {
        boolean result = false;
        MonthYear monthYear = (MonthYear)obj; 
        if (this.year > monthYear.year)
        {
            result = true;
        }
        else if (this.year == monthYear.year)
        {
            if (this.month > monthYear.month)
            {
                result = true;
            }
        }
        return result;
    }

//    boolean equalTo(MonthYear monthYear)
//    {
//        boolean result = false;
//        if (this.year == monthYear.year && this.month == monthYear.month)
//        {
//            result = true;
//        }
//        return result;
//    }
//
//    public static boolean equalTo()
//    {
//        boolean result = false;
//        return result;
//    }

    public PeriodI next()
    {
        MonthYear result = new MonthYear(this.month, this.year);
        result.month++;
        if (result.month == Calendar.DECEMBER + 1)
        {
            result.year++;
            result.month = Calendar.JANUARY;
        }
        return result;
    }

    public PeriodI previous()
    {
        MonthYear result = new MonthYear(this.month, this.year);
        result.month--;
        if (result.month == Calendar.JANUARY - 1)
        {
            result.year--;
            result.month = Calendar.DECEMBER;
        }
        return result;
    }

    public int howManyBetween( PeriodI other)
    {
        return PeriodUtils.howManyBetween( this, other);
        /*
        int result = 0;
        if (this.greaterThan(other))
        {
            Err.error("Arguement must be greater than this");
        }
        MonthYear nextOne = this;
        while (true)
        {
            if(nextOne.equals( other))
            {
                break;
            }
            nextOne = (MonthYear)nextOne.next();
            result++;
        }
        return result;
        */
    }

    public static String getDateAsMonthYearDisplay( Date date)
    {
        String result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date);
        int currentMonth = calendar.get( Calendar.MONTH);
        int currentYear = calendar.get( Calendar.YEAR);
        MonthYear currentMY = new MonthYear( currentMonth, currentYear);
        result = MonthInYear.getPeriodAsMonthYearDisplay( currentMY.month, currentMY.year);
        return result;
    }

    public int getMonth()
    {
        return month;
    }

    public int getYear()
    {
        return year;
    }
}
