package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Assert;

import java.util.Calendar;
import java.util.Date;

/**
 * See TestDates instead for unit tests
 *
 * User: chris
 * Date: 11/07/2005
 * Time: 14:44:10
 */
public class WeekYear implements PeriodI
{
    /**
     * First week is 1. As deals with whole weeks you can also have week 0
     * before week 1 starts.
     */
    private int week = Utils.UNSET_INT;
    /**
     * the year that the user is viewing
     */
    private int year = Utils.UNSET_INT;

    public void chkHaveDate()
    {
        if (week == Utils.UNSET_INT)
        {
            Err.error("week not set");
        }
        if (year == Utils.UNSET_INT)
        {
            Err.error("year not set");
        }
    }

    public static WeekYear newInstance( Date date)
    {
        return newInstance( date, false);
    }

    /**
     * @param anyDate
     */
    public static WeekYear newInstance( Date anyDate, boolean extraDay)
    {
        int resultWeek;
        int resultYear;
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        Date dayToUse;
        if(extraDay)
        {
            dayToUse = TimeUtils.addDays(anyDate, -1);
        }
        else
        {
            dayToUse = anyDate;
        }
        calendar.setTime( dayToUse);
        resultYear = calendar.get( Calendar.YEAR);
        //Will be getting the first Sunday in the year
        Calendar firstSundayDayOfMonth = TimeUtils.getFirstDayInMonth( Calendar.JANUARY, resultYear, calendar.getFirstDayOfWeek());
        if(anyDate.getTime() < firstSundayDayOfMonth.getTime().getTime())
        {
            resultWeek = 0;
        }
        else
        {
            int dayOfYear = calendar.get( Calendar.DAY_OF_YEAR);
            int firstSunday = firstSundayDayOfMonth.get( Calendar.DAY_OF_YEAR);
            int daysAfter = dayOfYear - firstSunday;
            int numWeeks = (int)Utils.divide( daysAfter, 7);
            resultWeek = numWeeks + 1;
        }
        //Err.pr( "newInstance(), from end date of " + endDateOf + ", get week " + resultWeek);
        return new WeekYear( resultWeek, resultYear);
    }

    public WeekYear(int week, int year)
    {
        Assert.isTrue( week >= 0, "Got week " + week);
        Assert.isTrue( week <= 53, "Got week " + week);
        this.week = week;
        this.year = year;
    }

    public PeriodI next()
    {
        PeriodI result = null;
        int newWeek = week+1;
        int newYear = this.year;
        if(newWeek == 53)
        {
            /*
             * The date at the beginning of the week may still
             * be in the current year.
             */
            WeekYear temp = new WeekYear( newWeek, newYear);
            Calendar cal = temp.getBeginDateAsCalendar();
            if(cal.get( Calendar.YEAR) == newYear+1)
            {
                //Go to first week of next year
                result = new WeekYear( 0, newYear+1);
            }
            else
            {
                //Still in the current year
            }
        }
        else
        {
            result = new WeekYear( newWeek, newYear);
        }
        return result;
    }

    public PeriodI previous()
    {
        PeriodI result = null;
        int newWeek = week-1;
        int newYear = this.year;
        if(newWeek == 0)
        {
            /*
             * The date at the beginning of the week may still
             * be in the current year.
             */
            WeekYear temp = new WeekYear( newWeek, newYear);
            Calendar cal = temp.getBeginDateAsCalendar();
            if(cal.get( Calendar.YEAR) == newYear-1)
            {
                //Go to last week of previous year
                result = new WeekYear( 52, newYear-1);
            }
            else
            {
                //Still in the current year
            }
        }
        else if(newWeek == -1)
        {
            /*
             * If we were on week 0 then previous will definitely take
             * us to the last week of the previous year - which is
             * the one before the 52nd week. Note that the 52nd week
             * and the 0th week of the next year are both in the same
             * week.
             */
            result = new WeekYear( 51, newYear-1);
        }
        else
        {
            //Err.pr( "previous week: " + newWeek);
            //Err.pr( "previous year: " + newYear);
            result = new WeekYear( newWeek, newYear);
        }
        return result;
    }

    public String toString()
    {
        return "week: " + week + ", year: " + year;
    }

    private Calendar getBeginDateAsCalendar()
    {
        Calendar result = (Calendar)TimeUtils.CALENDAR.clone();
        result.setTime( TimeUtils.getBeginDateFromWeekYear( week, year));
        return result;
    }

    private Calendar getEndDateAsCalendar()
    {
        Calendar result = (Calendar)TimeUtils.CALENDAR.clone();
        result.setTime( TimeUtils.getEndDateFromWeekYear( week, year));
        return result;
    }

    /**
     *
     * @return The beginning date of the week
     */
    public Date getEndPoint()
    {
        return getEndDateAsCalendar().getTime();
    }

    public Date getBeginPoint()
    {
        return getBeginDateAsCalendar().getTime();
    }

    public Date getMiddlePoint()
    {
        return TimeUtils.getMiddlePoint( getBeginPoint(), getEndPoint());
    }

    public PeriodFactory.PeriodEnum getPeriodEnum()
    {
        PeriodFactory.PeriodEnum result = PeriodFactory.PeriodEnum.WEEK;
        return result;
    }

    public boolean contains( Date date)
    {
        boolean result = false;
        if(date.getTime() >= getBeginPoint().getTime() && date.getTime() <= getEndPoint().getTime())
        {
            result = true;
        }
        return result;
    }

    public boolean equals(Object other)
    {
        boolean result;
        WeekYear otherWY = (WeekYear) other;
        if(easyRange( this) && easyRange( otherWY))
        {
            result = Utils.equals(this.week, otherWY.week) &&
                Utils.equals(this.year, otherWY.year);
        }
        else
        {
            Date thisBegin = this.getBeginPoint();
            Date otherBegin = otherWY.getBeginPoint();
            result = Utils.equals( thisBegin, otherBegin);
        }
        return result;
    }

    private boolean easyRange( WeekYear weekYear)
    {
        boolean result = false;
        if(weekYear.week > 0 && weekYear.week < 52)
        {
            result = true;
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        if(easyRange( this))
        {
            result = Utils.hashCode(result, week);
            result = Utils.hashCode(result, year);
        }
        else
        {
            result = Utils.hashCode(result, getBeginPoint());
        }
        return result;
    }

    public boolean greaterThan(Object obj)
    {
        boolean result = false;
        WeekYear weekYear = (WeekYear)obj;
        if(easyRange( this) && easyRange( weekYear))
        {
            result = simpleGreaterThan( weekYear);
        }
        else
        {
            Date thisBegin = this.getBeginPoint();
            Date otherBegin = weekYear.getBeginPoint();
            result = Utils.greaterThan( thisBegin, otherBegin);
        }
        return result;
    }

    private boolean simpleGreaterThan( WeekYear weekYear)
    {
        boolean result = false;
        if (this.year > weekYear.year)
        {
            result = true;
        }
        else if (this.year == weekYear.year)
        {
            if (this.week > weekYear.week)
            {
                result = true;
            }
        }
        return result;
    }

//    boolean equalTo(WeekYear monthYear)
//    {
//        boolean result = false;
//        if (this.year == monthYear.year && this.week == monthYear.week)
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

    public int howManyBetween( PeriodI other)
    {
        return PeriodUtils.howManyBetween( this, other);
        /*
        int result = 0;
        if (this.greaterThan(other))
        {
            Err.error("Arguement must be greater than this");
        }
        WeekYear nextOne = this;
        while (true)
        {
            if(nextOne.equals( other))
            {
                break;
            }
            nextOne = (WeekYear)nextOne.next();
            result++;
        }
        return result;
        */
    }

    public int getWeek()
    {
        return week;
    }

    public int getYear()
    {
        return year;
    }

    public static String getDateAsWeekEndingDisplay( Date date)
    {
        String result;
        result = TimeUtils.getPeriodAsWeekEndingDisplay( date);
        return result;
    }

//    public static String getDateAsWeekYearDisplay( Date date)
//    {
//        String result;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime( date);
//        int currentMonth = calendar.get( Calendar.MONTH);
//        int currentYear = calendar.get( Calendar.YEAR);
//        WeekYear currentMY = new WeekYear( currentMonth, currentYear);
//        result = TimeUtils.getPeriodAsMonthYearDisplay( currentMY.month, currentMY.year);
//        return result;
//    }

    public static void main(String[] args)
    {
    }
}