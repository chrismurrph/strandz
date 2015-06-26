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
package org.strandz.data.msgfault.objects;

import org.strandz.lgpl.util.Utils;

import java.util.Date;

public class Workflow
{
    private Date timeStamp;
    private WorkItemStatus sourceStatus;
    private WorkItemStatus targetStatus;
    private Employee sourceEmployee;
    private Employee targetEmployee;
    private String text;

    public Workflow()
    {
        super();
        /*
        constructedTimes++;
        id = constructedTimes;
        Err.pr( "Constructed " + id);
        if(id == 10)
        {
        Err.stack();
        }
        */
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Workflow))
        {// nufin
        }
        else
        {
            Workflow test = (Workflow) o;
            if((timeStamp == null
                ? test.timeStamp == null
                : timeStamp.equals(test.timeStamp)))
            {
                if((sourceEmployee == null
                    ? test.sourceEmployee == null
                    : sourceEmployee.equals(test.sourceEmployee)))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (timeStamp == null ? 0 : timeStamp.hashCode());
        result = 37 * result
            + (sourceEmployee == null ? 0 : sourceEmployee.hashCode());
        return result;
    }

    public String getName()
    {
        return text;
    }

    public void setName(String name)
    {
        this.text = name;
    }

    public String toString()
    {
        return getName();
    }

    public Employee getSourceEmployee()
    {
        return sourceEmployee;
    }

    public void setSourceEmployee(Employee sourceEmployee)
    {
        this.sourceEmployee = sourceEmployee;
    }

    public WorkItemStatus getSourceStatus()
    {
        return sourceStatus;
    }

    public void setSourceStatus(WorkItemStatus sourceStatus)
    {
        this.sourceStatus = sourceStatus;
    }

    public Employee getTargetEmployee()
    {
        return targetEmployee;
    }

    public void setTargetEmployee(Employee targetEmployee)
    {
        this.targetEmployee = targetEmployee;
    }

    public WorkItemStatus getTargetStatus()
    {
        return targetStatus;
    }

    public void setTargetStatus(WorkItemStatus targetStatus)
    {
        this.targetStatus = targetStatus;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Date getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp)
    {
        this.timeStamp = timeStamp;
    }
}
