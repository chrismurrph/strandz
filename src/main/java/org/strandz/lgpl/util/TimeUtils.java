/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.util;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.WombatNote;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * This static class groups all the little utility functions to do with times
 * and dates.
 *
 * @author Chris Murphy
 */
public class TimeUtils
{
    private static final Locale OZZIE = new Locale("en", "AU");
    /*
    * A DateFormat should exist for every thread. Thus later this will come
    * to be on a per 'user container' basis.
    */
    public static final DateFormat DATE_FORMAT;
    public static final DateFormat STANDARD_DATE_FORMAT;
    public static final DateFormat DATEPICKER_FORMAT;
    public static final DateFormat TIME_FORMAT;
    public static final DateFormat MONTH_FORMAT;
    public static final DateFormat YEAR_FORMAT;
    public static final DateFormat DATE_TIME_FORMAT;
    public static final DateFormat WEEK_FORMAT;
    private static final SimpleDateFormat DAY_FORMATTER;
    private static final SimpleDateFormat DAY_IN_MONTH_FORMAT;
    public static final String STANDARD_DATE_PARSE_STRING = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String DATE_PARSE_STRING = "dd/MM/yyyy";
    public static final String DATEPICKER_PARSE_STRING = "dd-MMM-yyyy";
    public static final String TIME_PARSE_STRING = "HH:mmaa";
    public static final String PROSE_DATE_PARSE_STRING = "dd MMM yyyy";
    public static final String DATE_TIME_PARSE_STRING = "dd_MM_yyyy__HH_mm_ss";
    public static final String WEEK_PARSE_STRING = "dd MMM";
    private static final String DAY_PARSE_STRING = "EEE";
    private static final String DAY_IN_MONTH_PARSE_STRING = "dd";
    public static final String MONTH_PARSE_STRING = "MMM";
    public static final String YEAR_PARSE_STRING = "yyyy";
    public static final TimeZone SYDNEY_TIME_ZONE;
    public static final Calendar CALENDAR;
    //private static final String MONTHS[] = new String[12];
    private static final transient String START_DAY_TIME = "00:00:00";

    static
    {
        MONTH_FORMAT = new SimpleDateFormat(MONTH_PARSE_STRING, OZZIE);
        YEAR_FORMAT = new SimpleDateFormat(YEAR_PARSE_STRING, OZZIE);

        String tzStr = System.getProperty("user.timezone");
        if(tzStr != null && !tzStr.equals("") && tzStr.equals("Australia/Sydney"))
        {
            TimeZone tz = TimeZone.getTimeZone(tzStr);
            TimeZone.setDefault(tz);
            SYDNEY_TIME_ZONE = tz;
            //Err.pr( "TZ have set as the default via Web Start is " + sydneyTimeZone.getDisplayName() );
        }
        else
        {
            SYDNEY_TIME_ZONE = TimeZone.getTimeZone("Australia/Sydney");
            TimeZone.setDefault(SYDNEY_TIME_ZONE);
            //Err.pr( "TZ have set as the default NOT via Web Start is " + sydneyTimeZone.getDisplayName() );
        }
        // Locale.setDefault( ozzie);
        DATE_FORMAT = new SimpleDateFormat(DATE_PARSE_STRING, OZZIE);
        DATEPICKER_FORMAT = new SimpleDateFormat(DATEPICKER_PARSE_STRING, OZZIE);
        STANDARD_DATE_FORMAT = new SimpleDateFormat(STANDARD_DATE_PARSE_STRING, OZZIE);
        TIME_FORMAT = new SimpleDateFormat(TIME_PARSE_STRING, OZZIE);
        DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_PARSE_STRING, OZZIE);
        WEEK_FORMAT = new SimpleDateFormat(WEEK_PARSE_STRING, OZZIE);
        DAY_FORMATTER = new SimpleDateFormat(DAY_PARSE_STRING);
        DAY_IN_MONTH_FORMAT = new SimpleDateFormat(DAY_IN_MONTH_PARSE_STRING);
        //String ids[] = TimeZone.getAvailableIDs();
        //Print.prArray( ids, "Available time zones");
        CALENDAR = new GregorianCalendar(SYDNEY_TIME_ZONE);
    }

    private static final HashMap DAYS_BACK_TO_MON = new HashMap(7);

    static
    {
        DAYS_BACK_TO_MON.put(new Integer(Calendar.MONDAY), new Integer(0));
        DAYS_BACK_TO_MON.put(new Integer(Calendar.TUESDAY), new Integer(-1));
        DAYS_BACK_TO_MON.put(new Integer(Calendar.WEDNESDAY), new Integer(-2));
        DAYS_BACK_TO_MON.put(new Integer(Calendar.THURSDAY), new Integer(-3));
        DAYS_BACK_TO_MON.put(new Integer(Calendar.FRIDAY), new Integer(-4));
        DAYS_BACK_TO_MON.put(new Integer(Calendar.SATURDAY), new Integer(-5));
        DAYS_BACK_TO_MON.put(new Integer(Calendar.SUNDAY), new Integer(-6));
    }
    
    private static void chkTimeZone(TimeZone tz)
    {
        //How described from WebStart ("Australia/Sydney")
        if(tz.getDisplayName().indexOf("Sydney") == -1)
        {
            //How described when running from client
            if(tz.getDisplayName().indexOf("Eastern Standard Time (New South Wales)") == -1)
            {
                //How described when running from WebStart after user.timezone property set then got
                if(tz.getDisplayName().indexOf("Eastern Standard Time") == -1)
                {
                    Err.error("TZ must be EST, got " + tz.getDisplayName());
                }
            }
        }
        Err.pr(SdzNote.TZ, "TZ using is " + tz.getDisplayName());
        Err.pr(SdzNote.TZ, "TZ short (with DS): " + tz.getDisplayName(true, TimeZone.SHORT));
        Err.pr(SdzNote.TZ, "TZ long (with DS): " + tz.getDisplayName(true, TimeZone.LONG));
        Err.pr(SdzNote.TZ, "TZ ID: " + tz.getID());
    }

    /**
     * Get the String representation of a <code>Calendar</code> as English/Australian people understand it,
     * that is with the day of the month coming before the month, coming before the year.
     *
     * @param cal
     * @return formatted String with both date and time elements showing
     */
    public static String displayCalendar(Calendar cal)
    {
        String result = DATE_FORMAT.format(cal.getTime());
        return result;
    }

    /**
     * Get the String representation of a <code>Date</code> as English/Australian people understand it,
     * that is with the day of the month coming before the month, coming before the year.
     *
     * @param date
     * @return formatted String with both date and time elements showing
     */
    public static String getFormattedDate(Date date)
    {
        return DATE_FORMAT.format(date);
    }

    /**
     * Get 'Mon', 'Tue' etc.
     *
     * @param date
     * @return formatted String with only the day of the week
     */
    public static String getFormattedDay(Date date)
    {
        return DAY_FORMATTER.format(date);
    }

    /**
     * As in '04' for the 4th of July
     *
     * @param date
     * @return formatted String with only the day of the month
     */
    public static String getFormattedDayInMonth(Date date)
    {
        return DAY_IN_MONTH_FORMAT.format(date);
    }

    /**
     * Adds a timestamp to the beginning of the name of a base file name
     *
     * @param base the base filename
     * @return the timestamped file name
     */
    public static String getACurrentFileName(String base)
    {
        String nowStr = DATE_TIME_FORMAT.format(new Date());
        return nowStr + base;
    }
    
    public static String getProseCurrentTimestamp()
    {
        String result;
        Date now = new Date();
        String timePart = TIME_FORMAT.format( now);
        String datePart = DATE_FORMAT.format( now);
        result = "at " + timePart + " on " + datePart;
        return result;
    }

    /**
     * Using dd/MM/yyyy as a mask, returns a date
     *
     * @param strDate String representing a date in format dd/MM/yyyy
     * @return the date
     */
    public static Date getDateFromString(String strDate)
    {
        Date date = null;
        try
        {
            date = DATE_FORMAT.parse(strDate);
        }
        catch(ParseException ex)
        {
            Err.error("Date is not in a recognisable format");
        }
        return date;
    }

    /**
     * Find the hour of the day from a date
     *
     * @param date the date we want to investigate
     * @return which hour of the day is represented by date
     */
    public static int getHours(Date date)
    {
        // return date.getHours();
        CALENDAR.setTime(date);
        return CALENDAR.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Find the minutes of the hour of the day from a date
     *
     * @param date the date we want to investigate
     * @return which minutes of the day is represented in date
     */
    public static int getMinutes(Date date)
    {
        // return date.getMinutes();
        CALENDAR.setTime(date);
        return CALENDAR.get(Calendar.MINUTE);
    }

    /**
     * Find the day from a date
     *
     * @param date the date we want to investigate
     * @return which day is represented in date
     */
    public static int getDay(Date date)
    {
        CALENDAR.setTime(date);
        return CALENDAR.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Will return the days Mon-Sun as a list of dates. For example if use the
     * result from <code>new Date()</code>, this method will return all the dates
     * in the current week, where the week starts on a Monday.
     * No evidence that a TZ was needed here but we put one in anyway, (03/05/05)
     * as part of a general rule of always doing so for GregorianCalendars.
     *
     * @param today
     * @return the list of dates surrounding the 'today' argument
     */
    public static List getDaysOfWeek(Date today)
    {
        List result = new ArrayList();
        Calendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
        cal.setTime(today);
        clearHMS(cal);

        int dayOfToday = cal.get(Calendar.DAY_OF_WEEK);
        Integer numberBack = (Integer) DAYS_BACK_TO_MON.get(new Integer(dayOfToday));
        for(int i = 0; i <= 7 - 1; i++)
        {
            Date date = new Date(cal.getTimeInMillis());
            Calendar calendar = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
            calendar.setTime(date);
            calendar.add(Calendar.DATE, i + numberBack.intValue());
            result.add(calendar.getTime());
        }
        return result;
    }

    /**
     * Clears the time 'fields' from a calendar. The resulting
     * calendar will only have a date component, so can be used
     * easily in calculations.
     *
     * @param calendarWithDate the calendar we want to become 'blank'
     */
    public static void clearHMS(Calendar calendarWithDate)
    {
        calendarWithDate.clear(Calendar.HOUR);
        calendarWithDate.clear(Calendar.HOUR_OF_DAY);
        calendarWithDate.clear(Calendar.MINUTE);
        calendarWithDate.clear(Calendar.SECOND);
        calendarWithDate.clear(Calendar.MILLISECOND);
        calendarWithDate.set( Calendar.HOUR_OF_DAY, 0);
        chkTimeZero( calendarWithDate.getTime());
    }

    /*
    public static void clearZone( Calendar calendar)
    {
      calendar.clear( Calendar.DST_OFFSET);
      calendar.clear( Calendar.ZONE_OFFSET);
    }
    */

//    private static void chkHoursZeroed(Date date)
//    {
//        int hours = getHours(date);
//        if(hours != 0)
//        {
//            Err.error("Hours have not been properly cleared from " + date);
//        }
//        int mins = getMinutes(date);
//        if(mins != 0)
//        {
//            Err.error("Minutes have not been properly cleared from " + date);
//        }
//    }
    
    /**
     * Find out if the first period covers the second period. 
     *
     * @param p1First beginning of first period
     * @param p1Last  end of first period
     * @param p2First beginning of second period
     * @param p2Last  end of second period
     * @param id      for debugging
     * @return true if the two periods overlap
     */
    public static boolean coversPeriod(Date p1First, Date p1Last, Date p2First, Date p2Last, String id)
    {
        boolean result = false;
        Assert.notNull( p1First, "p1First == null for " + id);
        Assert.notNull( p2First, "p2First == null for " + id);
        if(p2Last == null)
        {
            p2Last = p2First;
        }
        if(p1Last == null)
        {
            p1Last = p1First;
        }
        Assert.isTrue( p1Last.getTime() >= p1First.getTime(), 
            "For first period, end time is not greater than begin time");
        Assert.isTrue( p2Last.getTime() >= p2First.getTime(),
            "For second period, end time is not greater than begin time");
        if(p1First.getTime() <= p2First.getTime() && p1Last.getTime() >= p2Last.getTime())
        {
            result = true;
        }
        return result;
    }

    /**
     * Find out if there is some intersection between two time periods
     *
     * @param p1First beginning of first period
     * @param p1Last  end of first period
     * @param p2First beginning of second period
     * @param p2Last  end of second period
     * @param id      for debugging
     * @return true if the two periods overlap
     */
    public static boolean periodsOverlap(Date p1First, Date p1Last, Date p2First, Date p2Last, String id)
    {
        Err.error( "Instead use coversPeriod");
        boolean result = false;
        /*
        if (id.indexOf( "Tighe" ) != -1)
        {
          Err.pr( "Mike Tighe holiday for Nov:" );
          Err.pr( "p1First: " + p1First );
          Err.pr( "p1Last: " + p1Last );
          Err.pr( "p2First: " + p2First );
          Err.pr( "p2Last: " + p2Last );
        }
        */
        if(p1First == null)
        {
            Err.error("p1First == null for " + id);
        }
        if(p2First == null)
        {
            Err.error("p2First == null for " + id);
        }
        if(p2Last == null)
        {
            // Err.error( "p2Last == null for " + id);
            p2Last = p2First;
        }
        if(p1Last == null)
        {
            p1Last = p1First;
            // Err.error( "p1Last == null for " + id);
        }
        if((p2Last.getTime() >= p1First.getTime())
            && (p2First.getTime() <= p1Last.getTime()))
        {
            if(p1First.getTime() == p2First.getTime() && p1Last.getTime() == p2Last.getTime())
            {
                //If your holidays are exactly the same as the month we are showing, then
                //there isn't much point showing you as an unrosterable volunteer.
            }
            else
            {
                //Err.pr( "period [" + p1First + " to " + p1Last + "] overlaps with " +
                //    "period [" + p2First + " to " + p2Last + "]" );
                result = true;
            }
        }
        return result;
    }

    /**
     * The p2 args are your holiday. We are seeing if p2 completely covers p1.
     *
     * @param p1First beginning of first period
     * @param p1Last  end of first period
     * @param p2First beginning of second period
     * @param p2Last  end of second period
     * @param name    used for debugging
     */
    public static boolean periodEnclosed(Date p1First, Date p1Last, Date p2First, Date p2Last, String name)
    {
        boolean result = false;
        if((p2First.getTime() <= p1First.getTime())
            && (p2Last.getTime() >= p1Last.getTime()))
        {
            // Err.pr( "holiday [" + p2First + " to " + p2Last + "] encloses month start/end " +
            // "period [" + p1First + " to " + p1Last + "]");
            result = true;
        }
        else
        {// Err.pr( "holiday [" + p2First + " to " + p2Last + "] NOT encloses month start/end " +
            // "period [" + p1First + " to " + p1Last + "] for " + name);
        }
        return result;
    }

    /**
     * Wrapper around TimeZone.getDefault() which makes 100% sure we are in Sydney.
     */
    public static TimeZone getDefaultTimeZone()
    {
        TimeZone result = TimeZone.getDefault();
        chkTimeZone(result);
        return result;
    }

    /**
     * first will become today if first is before today.
     */
    public static Date adjustToEndOfToday(Date first)
    {
        Date result = first;
        Err.pr(WombatNote.FIRST_LAST_DAYS_EMAILS, "Have changed name of this method to end of day - " +
                "need to also create a beginning and that's what we ought to be calling");
        Date today = new Date();
        if(today.getTime() > first.getTime())
        {
            result = today;
        }
        Err.pr(WombatNote.FIRST_LAST_DAYS_EMAILS,
            "adjustToToday() started with " + first + " and will ret " + result);
        return result;
    }
    
    public static void chkTimeZero( Date date, String info)
    {
        if(date != null)
        {
            String msg;
            if(info == null)
            {
                msg = "Expect time to be at beginning of day, got " + date;
            }
            else
            {
                msg = "Expect time to be at beginning of day, got " + date + "[" + info + "]";
            }
            Assert.isTrue( date.toString().indexOf( START_DAY_TIME) != -1, msg);
        }
    }
    
    public static void chkTimeZero( Date date)
    {
        chkTimeZero( date, null);
    }

    public static boolean chkDateAndDayMatch(Date date, int day)
    {
        boolean result = false;
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(date);
        int calDay = newCal.get(Calendar.DAY_OF_WEEK);
        Err.pr(SdzNote.FIELD_VALIDATION, "To cf " + calDay + " with " + day);
        if(calDay == day)
        {
            result = true;
        }
        return result;
    }

    /**
     * When rectify deprecation error we will need to test that
     * making changes to Calendar actually affects the underlying
     * date.
     */
    /*
    public static void setHours( int i, Date date )
    {
      date.setHours( i );
    }
    */

    /**
     * When rectify deprecation error we will need to test that
     * making changes to Calendar actually affects the underlying
     * date.
     */
    /*
    public static void setMinutes( int i, Date date )
    {
      date.setMinutes( i );
    }
    */
    public static void setHours(int i, Calendar calendar)
    {
        calendar.set(Calendar.HOUR_OF_DAY, i);
    }

    public static void setMinutes(int i, Calendar calendar)
    {
        calendar.set( Calendar.MINUTE, i);
    }

    /**
     * The date that will be created will be at the beginning
     * of the day
     *  
     * @param day As you would normally write it
     * @param month 7 means August! - use Calendar.JULY
     * @param year As you would normally write it
     * @return
     */
    public static Date getDate(int day, int month, int year)
    {
        return getDateFromDayMonthYear(day, month, year, true);
    }

    public static Date getDateEndDay(int day, int month, int year)
    {
        return getDateFromDayMonthYear(day, month, year, false);
    }

    /*
     * 0 Jul(6), Aug(7), Sep(8) (1st quarter)
     * 1 Oct(9), Nov(10), Dec(11)
     * 2 Jan(0), Feb(1), Mar(2)
     * 3 Apr(3), May(4), Jun(5) (4th quarter)
     */
     public static int quarterFromMonth( int month)
     {
         int result = Utils.UNSET_INT;
         if(month >= 0 && month <= 2)
         {
             result = 2;
         }
         else if(month >= 3 && month <= 5)
         {
             result = 3;
         }
         else if(month >= 6 && month <= 8)
         {
             result = 0;
         }
         else if(month >= 9 && month <= 11)
         {
             result = 1;
         }
         return result;
     }

    /*
     * 0 Jul(6), Aug(7), Sep(8) (1st quarter)
     * 1 Oct(9), Nov(10), Dec(11)
     * 2 Jan(0), Feb(1), Mar(2)
     * 3 Apr(3), May(4), Jun(5) (4th quarter)
     *
     * @param monthInQuarter - Which one of three months want (first(0), middle(1), last(2))
     * @param quarter - The quarter to select the month from (0,1,2,3)
     * @return - The number for the eg third and last month in the quarter
     */
     public static int monthFromMonthInQuarter( int monthInQuarter, int quarter)
     {
         int result;
         int monthList[] = null;
         Assert.isTrue( monthInQuarter >= 0 && monthInQuarter <= 2);
         Assert.isTrue( quarter >= 0 && quarter <= 3);
         if(quarter == 0)
         {
            monthList = new int[]{6,7,8};
         }
         else if(quarter == 1)
         {
             monthList = new int[]{9,10,11};
         }
         else if(quarter == 2)
         {
             monthList = new int[]{0,1,2};
         }
         else if(quarter == 3)
         {
             monthList = new int[]{3,4,5};
         }
         else
         {
             Err.error();
         }
         result = monthList[monthInQuarter];
         return result;
     }

    /**
     * Watch the month here - 7 means August! - use Calendar.JULY
     *
     * @param day
     * @param month
     * @param year
     */
    public static Date getDateFromDayMonthYear(int day, int month, int year, boolean beginning)
    {
        Date result;
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        clearCalendar(calendar);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        //Err.pr( "calendar set to " + calendar.getTime());
        if(!beginning)
        {
            //If we really wanted midnight, then we add 24 hours
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        result = calendar.getTime();
        return result;
    }

    private static Calendar getFromWeekYear( int week, int year, int move)
    {
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        clearCalendar(calendar);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        //Shows that clearing the calendar didn't work at all - it still has today in it!
        Err.pr( "Setting week " + week + " and year " + year + " gives date " + calendar.getTime());
        calendar = TimeUtils.addDaysRetCalendar( calendar.getTime(), move);
        if(calendar.get( Calendar.YEAR) == year-1)
        {
            Err.pr( "Gone back a year");
            calendar = TimeUtils.addDaysRetCalendar( calendar.getTime(), 7);
        }
        else if(calendar.get( Calendar.YEAR) == year+1)
        {
            Err.pr( "Gone forward a year");
            calendar = TimeUtils.addDaysRetCalendar( calendar.getTime(), -7);
        }
        return calendar;
    }

    /*
    public static Date getBeginDateFromWeekYear( int week, int year)
    {
        //Always brings back a Thursday for some unknown reason. Removing four days would seem to give what the
        //documentation says ought to happen, which is that the first incomplete week is week 0 - except for cases
        //where the year starts on a Sunday - such as 2006
        Calendar now = (Calendar)TimeUtils.CALENDAR.clone();
        Calendar result = getFromWeekYear( week, year, calcDaysMove( now, true));
        return result.getTime();
    }

    public static Date getBeginOfWeekDate(Date date)
    {
        Date result = null;
        if(date != null)
        {
            Calendar calendar = (Calendar)CALENDAR.clone();
            calendar.setTime(date);
            int daysToMove = calcDaysMove( calendar, true);
            result = addDays( date, daysToMove);
        }
        return result;
    }
    */

    /**
     * Don't seem to be able to clear a calendar or date of any kind. Here we
     * work out the number of days we need to go back (or forward) to get to
     * Sunday from today (now).
     * Assumption that Sunday is the beginning of the week.
     * To improve perf caller don't clone but use straight CALENDAR.
     */
    /*
    private static int calcDaysMove( Calendar calendar, boolean back)
    {
        int result;
        int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK);
         *
         * If at Sat then dayOfWeek will be 7, and need to go back 6 days to get to Sun
         *
        result = -(dayOfWeek-1);
        if(!back)
        {
             *
             * If going forward then need to move on (7-6) days, or 1 day
             *
            result = (7 + result);
        }
        return result;
    }

    public static Date getEndDateFromWeekYear( int week, int year)
    {
        Calendar now = (Calendar)TimeUtils.CALENDAR.clone();
        Calendar result = getFromWeekYear( week, year, calcDaysMove( now, false));
        return result.getTime();
    }
    */

    public static Calendar getFirstOfYear( int day, int year)
    {
        Date actualFirst = getDate( 1, Calendar.JANUARY, year);
        Calendar calendar = (Calendar)CALENDAR.clone();
        calendar.setTime( actualFirst);
        while(calendar.get( Calendar.DAY_OF_WEEK) != day)
        {
            calendar = addDaysRetCalendar( calendar.getTime(), 1);
        }
        return calendar;
    }

    public static int getWeekInYear( Date date)
    {
        int result;
        Calendar calendar = (Calendar)CALENDAR.clone();
        calendar.setTime( date);
        int dayOfYear = calendar.get( Calendar.DAY_OF_YEAR);
        Calendar firstSunday = getFirstOfYear( Calendar.SUNDAY, calendar.get( Calendar.YEAR));
        int diff = dayOfYear - firstSunday.get( Calendar.DAY_OF_YEAR);
        if(diff < 0)
        {
            result = 0;
        }
        else
        {
            result = (int)Utils.divide( diff, 7);
            result++;
        }
        return result;
    }

    public static Date getEndDateFromWeekYear( int week, int year)
    {
        Date result;
        Calendar firstSunday = getFirstOfYear( Calendar.SUNDAY, year);
        int daysToAdd = week*7;
        Calendar calendar = (Calendar)firstSunday.clone();
        calendar.add( Calendar.DAY_OF_YEAR, daysToAdd);
        result = calendar.getTime();
        return result;
    }

    //Same as above except for -7:
    public static Date getBeginDateFromWeekYear( int week, int year)
    {
        Date result;
        Date endDate = getEndDateFromWeekYear( week, year);
        Calendar calendar = (Calendar)CALENDAR.clone();
        calendar.setTime( endDate);
        calendar.add( Calendar.DAY_OF_YEAR, -7);
        result = calendar.getTime();
        return result;
    }

    public static void main(String[] args)
    {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, Calendar.APRIL);
//        Err.pr("calendar month: " + calendar.get(Calendar.MONTH));
//        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        Err.pr("maxDayOfMonth: " + maxDayOfMonth);
//        String str = "APR";
//        Date date = DateUtils.getMonthFromString(str);
//        Err.pr("From " + str + " got " + date);
//        Calendar cal2 = new GregorianCalendar();
//        cal2.setTime(date);
//        int end = cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
//        Err.pr("End of " + str + " will be " + end);
        /*
        String displayed = "2005_07_24";
        Date date = getDateFromDisplayed( displayed, "yyyy_MM_dd");
        Err.pr( "From " + displayed + " got " + date);
        Date today = new Date();
        displayed = getDisplayedFromDate( today, "dd MMM yyyy");
        Err.pr( "Today nicely formatted is " + displayed);
        */
        /*
        String displayed = "2005_08_24";
        Date date = getDateFromDisplayed(displayed, "yyyy_MM_dd");
        boolean isInFinalWeek = isFinalWeekInMonth(date);
        Err.pr("Is " + displayed + " the final week in the month: " + isInFinalWeek);
        */
        /*
        Date today = new Date();
        int numMonthsAdd = 4;
        Date forward = addMonths(today, numMonthsAdd);
        Err.pr("Gone forward from today to " + forward + "by adding " + numMonthsAdd + " months");
        forward = getEndOfMonthDate(forward);
        Err.pr("End of month is " + forward);
        */
        /*
        Date eow = getEndOfWeekDate( new Date());
        Err.pr( "eow: " + eow);
        */
        /*
        Calendar cal = getFirstDayInMonth( Calendar.JANUARY, 2006, CALENDAR.getFirstDayOfWeek());
        Err.pr( "First SUN in 2006: " + cal.getTime());
        */
        GregorianCalendar cal = new GregorianCalendar();
        cal.set( Calendar.YEAR, 2009);
        cal.set( Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int numDays = cal.getActualMaximum( Calendar.DAY_OF_MONTH);
        System.out.println( "Num days: " + numDays);
    }
    
    /**
     * For there to be a week left in the month, there needs
     * to be seven days to the end of the month. Thus add seven
     * days and observe if you are still in the same month or not
     *
     * @param date
     * @return whether a week yet to go
     */
    public static boolean isFinalWeekInMonth(Date date)
    {
        boolean result = true;
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        calendar.setTime(date);
        int currentMonth = calendar.get(Calendar.MONTH);
        calendar.add(Calendar.DATE, 7);
        int newMonth = calendar.get(Calendar.MONTH);
        //Err.pr( "New month: " + newMonth);
        //Err.pr( "Current month: " + currentMonth);
        if(newMonth == currentMonth)
        {
            result = false;
        }
        //Err.pr( "isFinalWeekInMonth() for " + date + ": " + result);
        return result;
    }

    /**
     * To be in the first week in a month take seven
     * days and observe if you are still in the same month or not
     *
     * @param date
     * @return whether are in first seven days of a month
     */
    public static boolean isFirstWeekInMonth(Date date)
    {
        boolean result = true;
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        calendar.setTime(date);
        int currentMonth = calendar.get(Calendar.MONTH);
        calendar.add(Calendar.DATE, -7);
        int newMonth = calendar.get(Calendar.MONTH);
        //Err.pr( "New month: " + newMonth);
        //Err.pr( "Current month: " + currentMonth);
        if(newMonth == currentMonth)
        {
            result = false;
        }
        //Err.pr( "isFirstWeekInMonth() for " + date + ": " + result);
        return result;
    }

    /**
     * TODO Being called to many times and returning same date
     */
    public static Calendar getFirstDayInMonth( int month, int year, int day)
    {
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        calendar.clear();
        calendar.set( Calendar.MONTH, month);
        calendar.set( Calendar.YEAR, year);
        calendar.set( Calendar.DAY_OF_MONTH, 1);
        //Err.pr( "First day of month " + month + ", year " + year + " is " + calendar.getTime() +
        //    ", " + calendar.get( Calendar.DAY_OF_WEEK));
        while(calendar.get( Calendar.DAY_OF_WEEK) != day)
        {
            calendar = addDaysRetCalendar( calendar.getTime(), 1);
        }
        //Err.pr( "First Sunday is on day " + calendar.get( Calendar.DAY_OF_WEEK) + " b/c have gone to " + calendar.getTime());
        return calendar;
    }

    public static boolean isDateInMonth(Date date, int month)
    {
        boolean result = false;
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        calendar.setTime(date);
        int monthInDate = calendar.get(Calendar.MONTH);
        if(monthInDate == month)
        {
            result = true;
        }
        return result;
    }

    public static boolean isDateInYear(Date date, int year)
    {
        boolean result = false;
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        calendar.setTime(date);
        int yearInDate = calendar.get(Calendar.YEAR);
        if(yearInDate == year)
        {
            result = true;
        }
        return result;
    }

    public static boolean isDateInCurrentMonth(Date date)
    {
        boolean result = true;
        Calendar calendar = (Calendar)TimeUtils.CALENDAR.clone();
        return isDateInParticularMonth(date, calendar);
    }

    public static boolean isDateInParticularMonth(Date date, Calendar particular)
    {
        boolean result = true;
        int currentMonth = particular.get(Calendar.MONTH);
        int currentYear = particular.get(Calendar.YEAR);
        result = isDateInMonth(date, currentMonth) && isDateInYear(date, currentYear);
        //Err.pr( "isDateInCurrentMonth() for " + date + ": " + result);
        return result;
    }

    /* Only commented because not being used
    public static boolean isDateInNextMonth(Date date)
    {
        boolean result = true;
        Calendar calendar = Calendar.getInstance();
        result = isDateInNextMonth(date, calendar);
        return result;
    }

    public static boolean isDateInNextMonth(Date date, Calendar current)
    {
        boolean result = false;
        Calendar copyCurrent = Calendar.getInstance();
        copyCurrent.setTime(current.getTime());
        copyCurrent.add(Calendar.MONTH, 1);
        int nextMonth = copyCurrent.get(Calendar.MONTH);
        int thisYear = copyCurrent.get(Calendar.YEAR);
        int nextYear = thisYear + 1;
        if(isDateInMonth(date, nextMonth))
        {
            if(isDateInYear(date, thisYear))
            {
                result = true;
            }
            else if(isDateInYear(date, nextYear) && nextMonth == Calendar.JANUARY)
            {
                result = true;
            }
        }
        //Err.pr( "isDateInNextMonth() for " + date + ": " + result);
        return result;
    }
    */

    public static boolean isDateInPreviousMonth( Date date, Date current)
    {
        boolean result = false;
        Calendar copyCurrent = (Calendar)CALENDAR.clone();
        copyCurrent.setTime( current);
        copyCurrent.add(Calendar.MONTH, -1);
        int previousMonth = copyCurrent.get(Calendar.MONTH);
        int thisYear = copyCurrent.get(Calendar.YEAR);
        int previousYear = thisYear - 1;
        if(isDateInMonth( date, previousMonth))
        {
            if(isDateInYear(date, thisYear))
            {
                result = true;
            }
            else if(isDateInYear(date, previousYear) && previousMonth == Calendar.DECEMBER)
            {
                result = true;
            }
        }
        return result;
    }

    public static boolean isDateInDayOfWeek(Date date, int dayOfWeek)
    {
        boolean result = true;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if(day != dayOfWeek)
        {
            Err.pr("Sunday: " + Calendar.SUNDAY);
            Err.pr("Monday: " + Calendar.MONDAY);
            Err.error("lastSyncDate being set as a " + day + " - s/be a " + dayOfWeek + ", " + date);
        }
        return result;
    }

    public static Date getDateFromDisplayed(String displayed, String format, boolean retNull)
    {
        Date result = null;
        if(displayed == null)
        {
            Err.error("Cannot obtain a Date from a null");
        }
        else if(displayed.equals(""))
        {
            Err.error("Cannot obtain a Date from a \"\"");
        }
        DateFormat formatter = new SimpleDateFormat(format);
        try
        {
            result = formatter.parse(displayed);
        }
        catch(ParseException ex)
        {
            if(!retNull)
            {
                Err.error(ex, "Date is not in a recognisable format: " + format + ", tried: " + displayed);
            }
        }
        return result;
    }

    public static String getDisplayedFromDate(Date date, String format)
    {
        String result;
        DateFormat formatter = new SimpleDateFormat(format);
        result = formatter.format(date);
        return result;
    }

    /**
     * Using monthFormatter as a mask, returns a date
     *
     * @param strDate String representing a date in format monthFormatter
     * @return the date
     */
    public static Date getMonthFromString(String strDate)
    {
        Date date = null;
        try
        {
            date = MONTH_FORMAT.parse(strDate);
        }
        catch(ParseException ex)
        {
            Err.error("Date is not in a recognisable format");
        }
        return date;
    }

    //Jul-2005
    public static int getMonthFromDisplay(String display)
    {
        int result = -99;
        String tok = null;
        StringTokenizer st = new StringTokenizer(display, "-");
        if(st.hasMoreTokens())
        {
            tok = st.nextToken();
        }
        Date date = getMonthFromString(tok);
        CALENDAR.setTime(date);
        result = CALENDAR.get(Calendar.MONTH);
        return result;
    }

    //Jul-2005
    public static int getYearFromDisplay(String display)
    {
        int result = -99;
        String tok = null;
        StringTokenizer st = new StringTokenizer(display, "-");
        while(st.hasMoreTokens())
        {
            tok = st.nextToken();
        }
        result = new Integer(tok).intValue();
        return result;
    }
        
    public static String getPeriodAsWeekEndingDisplay( Date date)
    {
        date = addDays( date, -1); //To reflect that weeks end on a Saturday
        String result = "W/E " + WEEK_FORMAT.format( date);
        return result;
    }

    public static Date getPeriodAsEndDate(int month, int year)
    {
        Date result = null;
        Calendar calendar = Calendar.getInstance();
        clearCalendar(calendar);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        result = new Date(calendar.getTime().getTime());
        return result;
    }

    public static Date getPeriodAsBeginDate(int month, int year)
    {
        Date result = null;
        Calendar calendar = Calendar.getInstance();
        clearCalendar(calendar);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        result = new Date(calendar.getTime().getTime());
        return result;
    }

    /**
     * For instance we might want to find the next Sunday from today
     *
     * @param dayOfWeek
     * @return Date on which the next dayOfWeek will fall
     */
    public static Date getNextDayFromToday(int dayOfWeek)
    {
        return getDayFromDate(null, dayOfWeek, true, 1);
    }
    
    public static Date getNextDayFromDate(Date date, int dayOfWeek)
    {
        return getDayFromDate(date, dayOfWeek, true, 1);
    }
    
    public static Date getNextDayFromDate(Date date, int dayOfWeek, boolean beginning)
    {
        return getDayFromDate(date, dayOfWeek, beginning, 1);
    }

    public static Date getPreviousDayFromDate(Date date, int dayOfWeek)
    {
        return getDayFromDate(date, dayOfWeek, true, -1);
    }
    
    /**
     * @param beginning To return at the beginning of the day or at the end of the day
     */
    private static Date getDayFromDate(Date date, int dayOfWeek, boolean beginning, int direction)
    {
        Date result = null;
        if(!(direction == -1 || direction == 1))
        {
            Err.error( "direction param must be 1 or -1");
        }
        Calendar calendar = new GregorianCalendar();
        if(date != null)
        {
            clearCalendar(calendar);
            calendar.setTime(date);
        }
        clearTime(calendar);
//Logical, but don't want to risk it
//        if(!beginning)
//        {
//            //If we really wanted midnight, then we add 24 hours
//            calendar.add( Calendar.DAY_OF_YEAR, 1);
//        }
        while(true)
        {
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            if(day == dayOfWeek)
            {
                result = calendar.getTime();
                break;
            }
            else
            {
                calendar.add(Calendar.DAY_OF_WEEK, direction);
            }
        }
        if(!beginning)
        {
            //If we really wanted midnight, then we add 24 hours
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        result = calendar.getTime();
        return result;
    }

//    public static void adjustToNextDay( int day, Calendar today)
//    {
//        clearTime( today);
//        int dayOfWeek = today.get( Calendar.DAY_OF_WEEK);
//        Err.pr( "current dayOfWeek: " + dayOfWeek);
//        Err.pr( "dayOfWeek to get tot: " + day);
//    }

    /*
    public static String getPeriodAsEndStr( int month, int year)
    {
      String result = null;
      result = "2005-04-30";
      return result;
    }

    public static String getPeriodAsBeginStr( int month, int year)
    {
      String result = null;
      result = "2005-04-01";
      return result;
    }
    */

    /**
     * Clears the 'fields' from a calendar. The intention being
     * that this method can be called before the invoking code goes
     * on to set only the fields that it wants to set. This resulting
     * calendar might be used just for time, or just for year, or
     * whatever
     *
     * @param calendar the calendar we want to become 'blank'
     */
    public static void clearCalendar(Calendar calendar)
    {
        calendar.clear(Calendar.DAY_OF_WEEK_IN_MONTH);
        calendar.clear(Calendar.DATE);
        calendar.clear(Calendar.DAY_OF_MONTH);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.clear(Calendar.AM_PM);
    }

    public static void clearTime(Calendar calendar)
    {
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.clear(Calendar.AM_PM);
    }

    public static Date addMinutes(Date date, int i)
    {
        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, i);
        result = calendar.getTime();
        return result;
    }

    public static Date addMilliseconds(Date date, int i)
    {
        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, i);
        result = calendar.getTime();
        return result;
    }

    public static Date addWeeks(Date date, int i)
    {
        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, i);
        result = calendar.getTime();
        //Uncomment to aid with adjusting manually (DemoData.ON_WEEKS)
        //Err.pr( "Gone from " + date + " to " + result);
        return result;
    }

    public static Date addDays(Date date, int i)
    {
        Date result;
        Calendar calendar = addDaysRetCalendar( date, i);
        result = calendar.getTime();
        return result;
    }

    public static Calendar addDaysRetCalendar(Date date, int i)
    {
        Calendar result = Calendar.getInstance();
        result.setTime(date);
        result.add(Calendar.DAY_OF_YEAR, i);
        return result;
    }
    
    public static Date addYears(Date date, int i)
    {
        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, i);
        result = calendar.getTime();
        return result;
    }

    public static Date addHours(Date date, int i)
    {
        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, i);
        result = calendar.getTime();
        return result;
    }
    
    public static Date addMonths(Date date, int i)
    {
        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, i);
        result = calendar.getTime();
        return result;
    }
    
    public static Date addQuarters(Date date, int i)
    {
        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, i*3);
        result = calendar.getTime();
        return result;
    }

    public static Date getEndOfMonthDate(Date date)
    {
        Date result;
        Date beginning = getBeginOfMonthDate( date);
        result = addMonths( beginning, 1);
        return result;
    }

    /** 
     * Adds days until gets to first thing Sunday morning.
     * Do this by adding a day no matter what then check
     * b4 increment. When in day are looking for go to the
     * beginning of the day and return that. 
     *  
     * @param date
     * @return
     */
    public static Date getFirstOfYearEndOfWeekDate(Date date)
    {
        Date result;
        Calendar calendarMovedTo = (Calendar)CALENDAR.clone();
        calendarMovedTo.setTime( date);
        calendarMovedTo.add( Calendar.DAY_OF_YEAR, 1);        
        while(true)
        {
            int day = getDay( calendarMovedTo.getTime());
            if(day == Calendar.MONDAY)
            {
                clearHMS( calendarMovedTo);
                result = calendarMovedTo.getTime();
                break;
            }
            else
            {
                calendarMovedTo.add( Calendar.DAY_OF_YEAR, 1);        
            }
        }
        return result;
    }    
    
    public static Date getBeginOfMonthDate(Date date)
    {
        Date result = null;
        if(date != null)
        {
            Calendar calendar = (Calendar)CALENDAR.clone();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            result = getDateFromDayMonthYear(1, month, year, true);
        }
        return result;
    }

    public static Date getBeginOfQuarterDate(Date date)
    {
        Date result = null;
        if(date != null)
        {
            Calendar calendar = (Calendar)CALENDAR.clone();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH);
            int quarter = TimeUtils.quarterFromMonth( month);
            int beginMonth = TimeUtils.monthFromMonthInQuarter( 0, quarter);
            int year = calendar.get(Calendar.YEAR);
            result = getDateFromDayMonthYear(1, beginMonth, year, true);
        }
        return result;
    }

    public static boolean isBlank(Date date)
    {
        boolean result = false;
        if(date == null)
        {
            result = true;
        }
        return result;
    }

    public static int yearsFromPastDate(Date date)
    {
        int result;
        Date today = new Date();
        result = yearsBetween(date, today);
        return result;
    }

    public static int yearsBetween(Date date1, Date date2)
    {
        int result = Utils.UNSET_INT;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        // Get age based on year
        result = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);

        // Add the tentative age to the date of birth to get this year's birthday
        calendar1.add(Calendar.YEAR, result);

        // If this year's birthday has not happened yet, subtract one from age
        if(calendar2.before(calendar1))
        {
            result--;
        }
        return result;
    }

    public static int daysBetween(Date date1, Date date2)
    {
        int result = Utils.UNSET_INT;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        result = calendar2.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);

        if(calendar2.before(calendar1))
        {
            result--;
        }
        return result;
    }

    public static Date getMiddlePoint( Date beginPoint, Date endPoint)
    {
        Date result;
        long begin = beginPoint.getTime();
        long end = endPoint.getTime();
        float middle = Utils.floatDivide( begin+end, 2);
        result = new Date( (long)middle);
        return result;
    }
}
