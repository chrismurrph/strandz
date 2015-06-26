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
package org.strandz.core.info.convert;

// import java.text.DateFormat;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateInTextComponentConvert extends AbstractObjectControlConvert
{
    private static int timesPush;
    private static int timesPull;

    public DateInTextComponentConvert()
    {
        typeRequiredByControlAccessors = String.class;
        typeOfObject = Date.class;
    }

    public Object pushOntoScreen(Object date)
    {
        String result = null;
        timesPush++;
        Err.pr(SdzNote.DATE_CONVERT, "pushOntoScreen, to format <" + result + "> TYPE: " + date.getClass().getName());
        result = TimeUtils.DATE_FORMAT.format(date);
        Err.pr(SdzNote.DATE_CONVERT, "pushOntoScreen, result <" + result + ">");
        if(timesPush == 1)
        {
            //Err.stack();
        }
        return result;
    }

    public Object pullOffScreen(Object date)
    {
        Object result = null;
        String str = (String) date;
        timesPull++;
        Err.pr(SdzNote.DATE_CONVERT, "pullOffScreen, dateFormatter.parse of <" + str + "> times " + timesPull);
        ParsePosition pos = new ParsePosition(0);
        Date resultDate = TimeUtils.DATE_FORMAT.parse(str, pos);
        boolean ok = false;
        if(pos.getIndex() == 0)
        {
            //Have field validation so can keep this commented
            //resultDate = TimeUtils.STANDARD_DATE_FORMATTER.parse(str, pos);
            if(pos.getIndex() == 0)
            {
                if(str.equals(""))
                {
                    //CTV spacer
                }
                else
                {
                    Err.error( "Cannot parse this String into a date: <" + str + ">");
                }
            }
            else
            {
                ok = true;
            }
        }
        else
        {
            ok = true;
        }
        if(ok)
        {
            Calendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
            cal.setTime(resultDate);
            TimeUtils.clearHMS(cal);
            result = cal.getTime();
            Err.pr(SdzNote.DATE_CONVERT, "pullOffScreen, result <" + result + "> TYPE: " + result.getClass().getName());
            if(timesPull == 0)
            {
                Err.stack();
            }
        }
        return result;
    }
}
