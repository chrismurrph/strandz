/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.data.wombatrescue.calculated;

import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.WombatNote;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MonthTransferObj implements Serializable
{
    public MonthInYearI month;
    public int year;

    public MonthTransferObj(MonthInYearI month, int year)
    {
        this.month = month;
        this.year = year;
    }
    
    public String toString()
    {
        return month + ", " + year;
    }

    public void obtainFirstLastDay(Date first, Date last, boolean nowSensitive)
    {
        Calendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
        cal.set(Calendar.MONTH, month.getOrdinal());
        cal.set(Calendar.YEAR, year);
        TimeUtils.clearHMS(cal);

        int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        if(nowSensitive)
        {
            //TODO Should call adjustToBeginningOfToday
            first.setTime(TimeUtils.adjustToEndOfToday(first).getTime());
        }
        else
        {
            first.setTime(cal.getTime().getTime());
        }
        cal.roll(Calendar.MONTH, true);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        last.setTime(cal.getTime().getTime());
        if(last.before(first))
        {
            Err.pr(WombatNote.FIRST_LAST_DAYS_EMAILS, "FIRST: " + first);
            Err.pr(WombatNote.FIRST_LAST_DAYS_EMAILS, "LAST: " + last);
            Err.pr(WombatNote.FIRST_LAST_DAYS_EMAILS, "Doing a roll because last comes before first:");
            cal.roll(Calendar.YEAR, true);
            last.setTime(cal.getTime().getTime());
        }
        Err.pr(WombatNote.FIRST_LAST_DAYS_EMAILS, "FIRST: " + chkFirstLastDaysEmails(first));
        Err.pr(WombatNote.FIRST_LAST_DAYS_EMAILS, "LAST: " + chkFirstLastDaysEmails(last));
    }
    
    public static String chkFirstLastDaysEmails(Date date)
    {
        String result = date.toString();
        if(WombatNote.FIRST_LAST_DAYS_EMAILS.isVisible() && result.indexOf(":00:00") == -1)
        {
            Err.pr("Cause for concern that WombatNote.firstLastDaysEmails not solved: " + result);
        }
        return result;
    }
}
