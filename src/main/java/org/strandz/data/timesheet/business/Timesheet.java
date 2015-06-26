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
package org.strandz.data.timesheet.business;

import org.strandz.data.timesheet.objects.HoursOnDay;
import org.strandz.data.timesheet.objects.Resource;
import org.strandz.data.timesheet.objects.Task;
import org.strandz.lgpl.data.objects.TimeSpent;
import org.strandz.lgpl.util.Err;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This class is not stored in any database but creates itself
 * from records in HoursOnADay. If some do not exist then they
 * are created. As the properties are accessed, really records
 * from the HoursOnADay table will be being affected.
 */
// Don't yet have a reason to incorporate DayInWeek
public class Timesheet
{
    private HoursOnDay[] days = new HoursOnDay[7];
    private static int MON = 0;
    private static int TUE = 1;
    private static int WED = 2;
    private static int THU = 3;
    private static int FRI = 4;
    private static int SAT = 5;
    private static int SUN = 6;
    private Task task;
    private Resource resource;

    /**
     * dates param will be from Mon-Sun
     */
    public Timesheet(
        Resource resource, Task task, List dates, List allHoursOnADayList
    )
    {
        int i = 0;
        for(Iterator iter = dates.iterator(); iter.hasNext(); i++)
        {
            Date date = (Date) iter.next();
            HoursOnDay hours = findHourPacket(task, resource, date,
                allHoursOnADayList);
            if(hours.getTimeSpent() == null)
            {
                Err.error("hours.getTimeSpent() == null");
            }
            if(!hours.getTimeSpent().equals(TimeSpent.NULL))
            {// Err.pr( "Queried Hours: " + hours.getTimeSpent());
            }
            days[i] = hours;
        }
        this.resource = resource;
        this.task = task;
    }

    private static HoursOnDay findHourPacket(
        Task task, Resource resource, Date day, List timePackets)
    {
        HoursOnDay result = null;
        // List timePackets = (List)get( TimesheetData.HOURS_ON_DAY);
        // Use contains here and elsewhere
        if(timePackets.size() == 29)
        {
            Err.stack("S/only have 28 hour packets");
        }
        for(Iterator iter = timePackets.iterator(); iter.hasNext();)
        {
            HoursOnDay hours = (HoursOnDay) iter.next();
            if(hours.getTask().equals(task) && hours.getResource().equals(resource)
                && hours.getDay().equals(day))
            {
                result = hours;
                break;
            }
        }
        if(result == null)
        {
            // Err.pr( "Did not find an Hours with task: " + task);
            // Err.pr( "resource: " + resource);
            // Err.pr( "day: " + day);
            result = HoursOnDay.newInstance();
            result.setDay(day);
            result.setResource(resource);
            result.setTask(task);
            timePackets.add(result);
            resource.getTimePackets().add(result);
            task.getTimePackets().add(result);
            // Err.pr( "In hours altogether are now: " + timePackets.size());
            /*
            if(timePackets.size() == 29)
            {
            Print.prList( timePackets);
            Err.stack();
            }
            */
        }
        return result;
    }

    public HoursOnDay getFridayHours()
    {
        return days[FRI];
    }

    public void setFridayHours(HoursOnDay fridayHours)
    {
        days[FRI] = fridayHours;
    }

    public HoursOnDay getMondayHours()
    {
        return days[MON];
    }

    public void setMondayHours(HoursOnDay mondayHours)
    {
        days[MON] = mondayHours;
    }

    public HoursOnDay getSaturdayHours()
    {
        return days[SAT];
    }

    public void setSaturdayHours(HoursOnDay saturdayHours)
    {
        days[SAT] = saturdayHours;
    }

    public HoursOnDay getSundayHours()
    {
        return days[SUN];
    }

    public void setSundayHours(HoursOnDay sundayHours)
    {
        days[SUN] = sundayHours;
    }

    public HoursOnDay getThursdayHours()
    {
        return days[THU];
    }

    public void setThursdayHours(HoursOnDay thursdayHours)
    {
        days[THU] = thursdayHours;
    }

    public HoursOnDay getTuesdayHours()
    {
        return days[TUE];
    }

    public void setTuesdayHours(HoursOnDay tuesdayHours)
    {
        days[TUE] = tuesdayHours;
    }

    public HoursOnDay getWednesdayHours()
    {
        return days[WED];
    }

    public void setWednesdayHours(HoursOnDay wednesdayHours)
    {
        days[WED] = wednesdayHours;
    }

    public Resource getResource()
    {
        return resource;
    }

    public void setResource(Resource resource)
    {
        this.resource = resource;
    }

    public Task getTask()
    {
        return task;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }
}
