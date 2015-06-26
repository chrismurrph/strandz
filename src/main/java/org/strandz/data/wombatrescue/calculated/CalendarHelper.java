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

import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.util.TimeUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.Serializable;

public class CalendarHelper implements Serializable
{
    /**
     * We can't simply construct it here because this class goes across the wire.
     * Hmm - had troubles going across wire so experimenting with non-transient.
     * Night never goes into DB so no need to be transient. Changing to not
     * being transient fixed bug WombatNote.badAllocationBMEmails
     */
    private GregorianCalendar calendarWithDate;
    
    /**
     * Just so we are not creating this helper object all the time  
     */
    GregorianCalendar getCalendarWithDate()
    {
        if(calendarWithDate == null)
        {
            calendarWithDate = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
        }
        return calendarWithDate;
    }
    
    DayInWeek getDayInWeek()
    {
        DayInWeek result;

        int day = getCalendarWithDate().get(Calendar.DAY_OF_WEEK);
        result = DayInWeek.fromOrdinal(day);
        /*
        Err.pr( "From " + day + " got DAY_OF_WEEK " + result);
        Err.pr( "Where Mon: " + Calendar.MONDAY);
        Err.pr( "Where Tue: " + Calendar.TUESDAY);
        */
        return result;
    }    
}
