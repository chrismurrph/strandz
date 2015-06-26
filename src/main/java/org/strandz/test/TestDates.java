package org.strandz.test;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.data.objects.WeekYear;
import org.strandz.lgpl.data.objects.MonthYear;
import org.strandz.lgpl.data.objects.PeriodI;
import org.strandz.lgpl.data.objects.QuarterYear;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Chris
 * Date: 20/11/2008
 * Time: 10:32:20 PM
 */
public class TestDates extends TestCase
{
    private Date lastSunday2008 = TimeUtils.getDate( 28, Calendar.DECEMBER, 2008);
    private Date firstSunday2009 = TimeUtils.getDate( 4, Calendar.JANUARY, 2009);

    public static void main(String[] args)
    {
        TestDates testDates = new TestDates();
        /*
        testDates.testWeekYear();
        testDates.testTimeUtils();
        testDates.testNextWeekYear();
        testDates.testDifferentDatesMonthYear();
        testDates.testMonthCurrentWeek();
        */
        testDates.testDifferentDatesQuarterYear();
    }

    public static Test suite()
    {
        return new TestSuite(TestDates.class);
    }

    /**
     * Everything about being on a week and going to the current month view
     */
    public void testMonthCurrentWeek()
    {
        PeriodI december = MonthYear.newInstance( lastSunday2008);
        PeriodI lastWeekInMonth = WeekYear.newInstance( TimeUtils.addMilliseconds( december.getEndPoint(), -1));
        //Err.pr( "beginning of last week of Dec: " + lastWeekInMonth.getBeginPoint());
        //Err.pr( "end of last week of Dec: " + lastWeekInMonth.getEndPoint());
        assertTrue( lastWeekInMonth.getBeginPoint().equals( lastSunday2008));
        assertTrue( lastWeekInMonth.getEndPoint().equals( firstSunday2009));
        //
        /* Doing something that works for both edge cases will be hard, so lets invent a middle point
        PeriodI may2009 = new MonthYear( Calendar.MAY, 2009);
        PeriodI lastWeekInMay2009 = WeekYear.newInstance( TimeUtils.addMilliseconds( may2009.getEndPoint(), -1));
        Err.pr( "beginning of last week of May: " + lastWeekInMay2009.getBeginPoint());
        Err.pr( "end of last week of May: " + lastWeekInMay2009.getEndPoint());
        */
        PeriodI may2009 = new MonthYear( Calendar.MAY, 2009);
        PeriodI currentWeekInMay2009 = WeekYear.newInstance( may2009.getMiddlePoint());
        //Err.pr( "begin of current week of May: " + currentWeekInMay2009.getBeginPoint());
        assertTrue( currentWeekInMay2009.getBeginPoint().equals( TimeUtils.getDate( 10, Calendar.MAY, 2009)));
        //Err.pr( "end of current week of May: " + currentWeekInMay2009.getEndPoint());
        assertTrue( currentWeekInMay2009.getEndPoint().equals( TimeUtils.getDate( 17, Calendar.MAY, 2009)));
    }

    public void testDifferentDatesWeekYear()
    {
        Date twentieth = TimeUtils.getDate( 20, Calendar.JANUARY, 2009);
        WeekYear twentiethWeekYear = WeekYear.newInstance( twentieth);
        Date twentyThird = TimeUtils.getDate( 23, Calendar.JANUARY, 2009);
        WeekYear twentyThirdWeekYear = WeekYear.newInstance( twentyThird);
        assertTrue( twentiethWeekYear.equals( twentyThirdWeekYear));
        //Err.pr( "end date: " + twentythirdWeekYear.getEndPoint());
        //Err.pr( "begin date: " + twentythirdWeekYear.getBeginPoint());
        Date twentyFifth = TimeUtils.getDate( 25, Calendar.JANUARY, 2009);
        Date eighteenth = TimeUtils.getDate( 18, Calendar.JANUARY, 2009);
        assertTrue( twentyThirdWeekYear.getEndPoint().equals( twentyFifth));
        assertTrue( twentyThirdWeekYear.getBeginPoint().equals( eighteenth));
    }

    public void testDifferentDatesMonthYear()
    {
        Date thirdJanDate = TimeUtils.getDate( 3, Calendar.JANUARY, 2009);
        MonthYear thirdJanMonthYear = MonthYear.newInstance( thirdJanDate);
        Date twentythirdJanDate = TimeUtils.getDate( 23, Calendar.JANUARY, 2009);
        MonthYear twentyThirdJanMonthYear = MonthYear.newInstance( twentythirdJanDate);
        assertTrue( thirdJanMonthYear.equals( twentyThirdJanMonthYear));
        Err.pr( "end date: " + thirdJanMonthYear.getEndPoint());
        Err.pr( "begin date: " + thirdJanMonthYear.getBeginPoint());
        Date beginFeb = TimeUtils.getDate( 1, Calendar.FEBRUARY, 2009);
        Date beginJan = TimeUtils.getDate( 1, Calendar.JANUARY, 2009);
        assertTrue( twentyThirdJanMonthYear.getEndPoint().equals( beginFeb));
        assertTrue( twentyThirdJanMonthYear.getBeginPoint().equals( beginJan));
    }

    /**
     * The quarter that the user is viewing.
     * 0 Jul(6), Aug(7), Sep(8) (1st quarter)
     * 1 Oct(9), Nov(10), Dec(11) (2nd quarter)
     * 2 Jan(0), Feb(1), Mar(2) (3rd quarter)
     * 3 Apr(3), May(4), Jun(5) (4th quarter)
     */
    public void testDifferentDatesQuarterYear()
    {
        Date thirdJanDate = TimeUtils.getDate( 3, Calendar.JANUARY, 2009);
        QuarterYear thirdJanQuarterYear = QuarterYear.newInstance( thirdJanDate);
        Date twentythirdJanDate = TimeUtils.getDate( 23, Calendar.JANUARY, 2009);
        QuarterYear twentyThirdJanQuarterYear = QuarterYear.newInstance( twentythirdJanDate);
        assertTrue( thirdJanQuarterYear.equals( twentyThirdJanQuarterYear));
        Err.pr( "end date: " + thirdJanQuarterYear.getEndPoint());
        Err.pr( "begin date: " + thirdJanQuarterYear.getBeginPoint());
        Date beginJan = TimeUtils.getDate( 1, Calendar.JANUARY, 2009);
        Date beginApr = TimeUtils.getDate( 1, Calendar.APRIL, 2009);
        assertTrue( twentyThirdJanQuarterYear.getEndPoint().equals( beginApr));
        assertTrue( twentyThirdJanQuarterYear.getBeginPoint().equals( beginJan));
    }

    public void testPreviousWeekYear()
    {
        Date date = TimeUtils.getDate( 2, Calendar.JANUARY, 2009);
        WeekYear weekYear = WeekYear.newInstance( date);
        //Err.pr( "beginning: " + weekYear.getBeginPoint());
        //Err.pr( "end: " + weekYear.getDate());
        assertTrue( weekYear.getBeginPoint().equals( TimeUtils.getDate( 28, Calendar.DECEMBER, 2008)));
        WeekYear previousWeekYear = (WeekYear)weekYear.previous();
        assertTrue( previousWeekYear.getWeek() == 51);
        //Err.pr( "beginning: " + previousWeekYear.getBeginPoint());
        //Err.pr( "end: " + previousWeekYear.getDate());
        assertTrue( previousWeekYear.getBeginPoint().equals( TimeUtils.getDate( 21, Calendar.DECEMBER, 2008)));
        /*
        WeekYear previousPreviousWeekYear = (WeekYear)previousWeekYear.previous();
        Err.pr( previousPreviousWeekYear);
        Err.pr( "beginning: " + previousPreviousWeekYear.getBeginPoint());
        Err.pr( "end: " + previousPreviousWeekYear.getDate());
        */
    }

    public void testNextWeekYear()
    {
        Date date = TimeUtils.getDate( 2, Calendar.JANUARY, 2009);
        WeekYear weekYear = WeekYear.newInstance( date);
        //Err.pr( "beginning: " + weekYear.getBeginPoint());
        //Err.pr( "end: " + weekYear.getDate());
        assertTrue( weekYear.getBeginPoint().equals( TimeUtils.getDate( 28, Calendar.DECEMBER, 2008)));
        WeekYear nextWeekYear = (WeekYear)weekYear.next();
        assertTrue( nextWeekYear.getWeek() == 1);
        //Err.pr( "beginning: " + previousWeekYear.getBeginPoint());
        //Err.pr( "end: " + previousWeekYear.getDate());
        assertTrue( nextWeekYear.getBeginPoint().equals( TimeUtils.getDate( 4, Calendar.JANUARY, 2009)));
        WeekYear nextNextWeekYear = (WeekYear)nextWeekYear.next();
        Err.pr( nextNextWeekYear);
        //Err.pr( "beginning: " + nextNextWeekYear.getBeginPoint());
        //Err.pr( "end: " + nextNextWeekYear.getDate());
        assertTrue( nextNextWeekYear.getBeginPoint().equals( TimeUtils.getDate( 11, Calendar.JANUARY, 2009)));
    }

    public void testWeekYear()
    {
        assertTrue( new WeekYear( 1, 2006).getBeginPoint().equals( TimeUtils.getDate( 1, Calendar.JANUARY, 2006)));
        assertTrue( new WeekYear( 1, 2008).getBeginPoint().equals( TimeUtils.getDate( 6, Calendar.JANUARY, 2008)));
        assertTrue( new WeekYear( 1, 2009).getBeginPoint().equals( TimeUtils.getDate( 4, Calendar.JANUARY, 2009)));

        WeekYear weekYear = new WeekYear( 0, 2009);
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( weekYear.getBeginPoint().equals( TimeUtils.getDate( 28, Calendar.DECEMBER, 2008)));
        weekYear = new WeekYear( 0, 2008);
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( weekYear.getBeginPoint().equals( TimeUtils.getDate( 30, Calendar.DECEMBER, 2007)));
        weekYear = new WeekYear( 0, 2006);
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( weekYear.getBeginPoint().equals( TimeUtils.getDate( 25, Calendar.DECEMBER, 2005)));

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 1, Calendar.JANUARY, 2006));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 1 but " + weekYear.getWeek(), weekYear.getWeek() == 1);
        assertTrue( weekYear.getYear() == 2006);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 6, Calendar.JANUARY, 2008));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 1 but " + weekYear.getWeek(), weekYear.getWeek() == 1);
        assertTrue( weekYear.getYear() == 2008);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 5, Calendar.JANUARY, 2008));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 0 but " + weekYear.getWeek(), weekYear.getWeek() == 0);
        assertTrue( weekYear.getYear() == 2008);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 4, Calendar.JANUARY, 2009));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 1 but " + weekYear.getWeek(), weekYear.getWeek() == 1);
        assertTrue( weekYear.getYear() == 2009);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 3, Calendar.JANUARY, 2009));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 0 but " + weekYear.getWeek(), weekYear.getWeek() == 0);
        assertTrue( weekYear.getYear() == 2009);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 27, Calendar.FEBRUARY, 2009));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 8 but " + weekYear.getWeek(), weekYear.getWeek() == 8);
        assertTrue( weekYear.getYear() == 2009);

        Err.pr( "");
        //
        weekYear = WeekYear.newInstance( TimeUtils.getDate( 3, Calendar.JANUARY, 2009));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 0 but " + weekYear.getWeek(), weekYear.getWeek() == 0);
        assertTrue( weekYear.getYear() == 2009);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 2, Calendar.JANUARY, 2009));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 0 but " + weekYear.getWeek(), weekYear.getWeek() == 0);
        assertTrue( weekYear.getYear() == 2009);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 1, Calendar.JANUARY, 2009));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 0 but " + weekYear.getWeek(), weekYear.getWeek() == 0);
        assertTrue( weekYear.getYear() == 2009);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 31, Calendar.DECEMBER, 2008));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 52 but " + weekYear.getWeek(), weekYear.getWeek() == 52);
        assertTrue( weekYear.getYear() == 2008);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 30, Calendar.DECEMBER, 2008));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 52 but " + weekYear.getWeek(), weekYear.getWeek() == 52);
        assertTrue( weekYear.getYear() == 2008);

        weekYear = WeekYear.newInstance( TimeUtils.getDate( 29, Calendar.DECEMBER, 2008));
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 52 but " + weekYear.getWeek(), weekYear.getWeek() == 52);
        assertTrue( weekYear.getYear() == 2008);

        weekYear = WeekYear.newInstance(lastSunday2008);
        Err.pr( "weekYear: " + weekYear.getBeginPoint());
        assertTrue( "week got not 52 but " + weekYear.getWeek(), weekYear.getWeek() == 52);
        assertTrue( weekYear.getYear() == 2008);
    }

    public void testTimeUtils()
    {
        Calendar cal = TimeUtils.getFirstDayInMonth( Calendar.JANUARY, 2006, TimeUtils.CALENDAR.getFirstDayOfWeek());
        Err.pr( "First SUN in 2006: " + cal.getTime());
        assertTrue( cal.get( Calendar.DAY_OF_YEAR) == 1);
        cal = TimeUtils.getFirstDayInMonth( Calendar.JANUARY, 2009, TimeUtils.CALENDAR.getFirstDayOfWeek());
        Err.pr( "First SUN in 2009: " + cal.getTime());
        assertTrue( cal.get( Calendar.DAY_OF_YEAR) == 4);
        Calendar calendar = TimeUtils.getFirstOfYear( Calendar.SUNDAY, 2008);
        assertTrue( calendar.getTime().toString().equals( "Sun Jan 06 00:00:00 EST 2008"));
        Date date = TimeUtils.getDate( 23, Calendar.NOVEMBER, 2008);
        date = TimeUtils.addHours( date, 12);
        int week = TimeUtils.getWeekInYear( date);
        assertTrue( "week actual is " + week, week == 47);
        date = TimeUtils.addDays( date, -1);
        week = TimeUtils.getWeekInYear( date);
        assertTrue( "week actual is " + week + " from " + date, week == 46);
        date = TimeUtils.addHours( date, 12);
        Err.pr( "Right at beginning of 47th week: " + date);
        week = TimeUtils.getWeekInYear( date);
        assertTrue( "week actual is " + week + " from " + date, week == 47);
        date = TimeUtils.addMilliseconds( date, -1);
        week = TimeUtils.getWeekInYear( date);
        assertTrue( "week actual is " + week + " from " + date, week == 46);
        date = TimeUtils.addMilliseconds( date, 1);
        week = TimeUtils.getWeekInYear( date);
        assertTrue( "week actual is " + week + " from " + date, week == 47);
        date = TimeUtils.addMilliseconds( date, 1);
        week = TimeUtils.getWeekInYear( date);
        assertTrue( "week actual is " + week + " from " + date, week == 47);
        Date endDate = TimeUtils.getEndDateFromWeekYear( 47, 2008);
        assertTrue( "Got end date of 47th week: " + endDate, endDate.toString().equals( "Sun Nov 30 00:00:00 EST 2008"));

        Date date0 = TimeUtils.getBeginDateFromWeekYear( 0, 2009);
        Err.pr( "date0: " + date0);
        Date date52 = TimeUtils.getBeginDateFromWeekYear( 52, 2008);
        Err.pr( "date52: " + date52);
        assertTrue(date0.equals( date52));
        assertTrue(date52.equals(lastSunday2008));

        date0 = TimeUtils.getEndDateFromWeekYear( 0, 2009);
        Err.pr( "date0: " + date0);
        date52 = TimeUtils.getEndDateFromWeekYear( 52, 2008);
        Err.pr( "date52: " + date52);
        assertTrue(date0.equals( date52));
        assertTrue(date52.equals(firstSunday2009));

    }
}
