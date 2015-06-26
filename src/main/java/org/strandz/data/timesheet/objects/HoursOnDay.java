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
package org.strandz.data.timesheet.objects;

import org.strandz.lgpl.data.objects.TimeSpent;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.Date;

public class HoursOnDay
{
    private Date day;
    private Task task;
    private Resource resource;
    private TimeSpent timeSpent;
    private static int constructedTimes;
    public int id;
    private static boolean debugging = false;

    public static HoursOnDay newInstance()
    {
        HoursOnDay result = new HoursOnDay();
        result.setTimeSpent(TimeSpent.newInstance("00:00"));
        return result;
    }

    public HoursOnDay()
    {
        constructedTimes++;
        id = constructedTimes;
        pr("## constructed HoursOnDay ID: " + id);
    }

    private static void pr(String s)
    {
        if(debugging)
        {
            Err.pr(s);
        }
    }

    public String toString()
    {
        return task + ", " + resource + ", " + day;
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof HoursOnDay))
        {// nufin
        }
        else
        {
            HoursOnDay test = (HoursOnDay) o;
            if((day == null ? test.day == null : day.equals(test.day)))
            {
                if((task == null ? test.task == null : task.equals(test.task)))
                {
                    if((resource == null
                        ? test.resource == null
                        : resource.equals(test.resource)))
                    {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (day == null ? 0 : day.hashCode());
        result = 37 * result + (task == null ? 0 : task.hashCode());
        result = 37 * result + (resource == null ? 0 : resource.hashCode());
        return result;
    }

    public Date getDay()
    {
        return day;
    }

    public void setDay(Date day)
    {
        this.day = day;
    }

    public TimeSpent getTimeSpent()
    {
        return timeSpent;
    }

    public void setTimeSpent(TimeSpent timeSpent)
    {
        pr("## setTimeSpent IN HoursOnDay ID: " + id + " to " + timeSpent);
         /**/
        if(timeSpent == null)
        {
            Err.error();
        }
         /**/
        this.timeSpent = timeSpent;
    }

    public Resource getResource()
    {
        return resource;
    }

    // Also make sure add this to the list that Resource hold
    public void setResource(Resource resource)
    {
        // Err.pr( "Are adding Resource to the list that Resource holds?");
        this.resource = resource;
    }

    public Task getTask()
    {
        return task;
    }

    // Also make sure add this to the list that Task hold
    public void setTask(Task task)
    {
        // Err.pr( "Are adding Task to the list that Task holds?");
        this.task = task;
    }
}
