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
package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;

import java.util.Calendar;
import java.util.Date;

public class WeekPeriod extends Period
{
    public WeekPeriod( Calendar calendar)
    {
        super( calendar);
    }

    public WeekPeriod( Date date)
    {
        super( date);
    }

    Period createClone()
    {
        return new WeekPeriod( calendar);
    }

    void chk()
    {
        //nufin
    }

    public Period next()
    {
        Period result = createClone();
        result.calendar.add( Calendar.DATE, 7);
        return result;
    }

    public Period previous()
    {
        Period result = createClone();
        result.calendar.add( Calendar.DATE, -7);
        return result;
    }

    public Date getEndPoint()
    {
        Date result = getBeginPoint();
        result = TimeUtils.addDays( result, 7);
        return result;
    }

    public PeriodFactory.PeriodEnum getPeriodEnum()
    {
        return PeriodFactory.PeriodEnum.WEEK;
    }
}
