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
public class QuarterYear implements PeriodI
{
    /**
     * The quarter that the user is viewing.
     * 0 Jul(6), Aug(7), Sep(8) (1st quarter)
     * 1 Oct(9), Nov(10), Dec(11) (2nd quarter)
     * 2 Jan(0), Feb(1), Mar(2) (3rd quarter)
     * 3 Apr(3), May(4), Jun(5) (4th quarter)
     */
    private int quarter = Utils.UNSET_INT;
    /**
     * the year that the user is viewing
     */
    private int year = Utils.UNSET_INT;

    private static final String QUARTERS[] = new String[4];
    private static final String QUARTERS_IN_MONTHS[] = new String[4];

    static
    {
        QUARTERS[0] = "Q1";
        QUARTERS[1] = "Q2";
        QUARTERS[2] = "Q3";
        QUARTERS[3] = "Q4";
        QUARTERS_IN_MONTHS[0] = "JAS";
        QUARTERS_IN_MONTHS[1] = "OND";
        QUARTERS_IN_MONTHS[2] = "JFM";
        QUARTERS_IN_MONTHS[3] = "AMJ";
    }

    public void chkHaveDate()
    {
        if (quarter == Utils.UNSET_INT)
        {
            Err.error("quarter not set");
        }
        if (year == Utils.UNSET_INT)
        {
            Err.error("year not set");
        }
    }

    public static QuarterYear newInstance( Date date)
    {
        return newInstance( date, false);
    }

    /**
     * @param date
     */
    public static QuarterYear newInstance( Date date, boolean extraDay)
    {
        QuarterYear result;
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
        int currentQuarter = TimeUtils.quarterFromMonth( currentMonth);
        int currentYear = calendar.get( Calendar.YEAR);
        result = new QuarterYear( currentQuarter, currentYear);
        //Err.pr( ProdKpiNote.DATA_TO_GRAPH, "From end date of " + endDateOf + ", get month " + result.month);
        return result;
    }

    public QuarterYear(int quarter, int year)
    {
        this.quarter = quarter;
        this.year = year;
    }

    public String toString()
    {
        return "quarter: " + quarter + ", year: " + year;
    }

    public Date getBeginPoint()
    {
        Date result;
        int month = TimeUtils.monthFromMonthInQuarter( 0, quarter);
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
        PeriodFactory.PeriodEnum result = PeriodFactory.PeriodEnum.QUARTER;
        return result;
    }

    public boolean equals(Object other)
    {
        QuarterYear otherMY = (QuarterYear) other;
        return Utils.equals(this.quarter, otherMY.quarter) &&
            Utils.equals(this.year, otherMY.year);
    }

    public int hashCode()
    {
        int result = 17;
        result = Utils.hashCode(result, quarter);
        result = Utils.hashCode(result, year);
        return result;
    }

    public boolean greaterThan(Object obj)
    {
        boolean result = false;
        QuarterYear monthYear = (QuarterYear)obj;
        if (this.year > monthYear.year)
        {
            result = true;
        }
        else if (this.year == monthYear.year)
        {
            if (this.quarter > monthYear.quarter)
            {
                result = true;
            }
        }
        return result;
    }

    public PeriodI next()
    {
        QuarterYear result = new QuarterYear(this.quarter, this.year);
        result.quarter++;
        if(result.quarter == 4)
        {
            result.quarter = 0;
        }
        else if(result.quarter == 2)
        {
            result.year++;
        }
        return result;
    }

    public PeriodI previous()
    {
        QuarterYear result = new QuarterYear( this.quarter, this.year);
        result.quarter--;
        if(result.quarter == -1)
        {
            result.quarter = 3;
        }
        else if(result.quarter == 1)
        {
            result.year--;
        }
        return result;
    }

    public int howManyBetween( PeriodI other)
    {
        return PeriodUtils.howManyBetween( this, other);
    }

    public static boolean isDateInPreviousQuarter( Date date, Date current)
    {
        boolean result = false;
        QuarterYear quarterYear = QuarterYear.newInstance( current);
        QuarterYear previousQuarterYear = (QuarterYear)quarterYear.previous();
        Date begin = previousQuarterYear.getBeginPoint();
        Date end = previousQuarterYear.getEndPoint();
        if(date.getTime() > begin.getTime() && date.getTime() <= end.getTime())
        {
            result = true;
        }
        return result;
    }

    public static String getDateAsQuarterYearDisplay( Date endOfQuarterDate)
    {
        String result;
        TimeUtils.chkTimeZero( endOfQuarterDate, "Expect the inbetween dates of quarter years to have zero time");
        Calendar calendar = Calendar.getInstance();
        Date timeStillInQuarter = TimeUtils.addHours( endOfQuarterDate, -1);
        calendar.setTime( timeStillInQuarter);
        int currentMonth = calendar.get( Calendar.MONTH);
        int currentQuarter = TimeUtils.quarterFromMonth( currentMonth);
        int currentYear = calendar.get( Calendar.YEAR);
        QuarterYear currentQY = new QuarterYear( currentQuarter, currentYear);
        result = getPeriodAsQuarterYearDisplay( currentQY.quarter, currentQY.year);
        return result;
    }

    public int getQuarter()
    {
        return quarter;
    }

    public int getYear()
    {
        return year;
    }

    /**
     * Really belongs in QuarterInYear, but we have not needed such a class yet
     */
    public static String getPeriodAsQuarterYearDisplay( int quarter, int year)
    {
        String result;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        String yearStr = makeYearQuarterly( quarter, calendar.getTime());
        String quarterStr;
        quarterStr = QUARTERS[quarter];
        result = quarterStr + " " + yearStr;
        /* Do not have room for this. but it should go in rollover text
        //+ " " + QUARTERS_IN_MONTHS[quarter];
        */
        //Err.pr( "From quarter " + quarter + " will ret " + result);
        return result;
    }

    private static String makeYearQuarterly( int quarter, Date date)
    {
        String result = null;
        String thisYearStr = TimeUtils.YEAR_FORMAT.format( date).substring( 2,4);
        if(quarter == 0 || quarter == 1)
        {
            Date yearAdded = TimeUtils.addYears( date, 1);
            String nextYearStr = TimeUtils.YEAR_FORMAT.format( yearAdded).substring( 2,4);
            result = thisYearStr + "/" + nextYearStr;
        }
        else if(quarter == 2 || quarter == 3)
        {
            Date yearMinused = TimeUtils.addYears( date, -1);
            String lastYearStr = TimeUtils.YEAR_FORMAT.format( yearMinused).substring( 2,4);
            result = lastYearStr + "/" + thisYearStr;
        }
        return result;
    }
}