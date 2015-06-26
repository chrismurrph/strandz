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
import java.util.List;

public class WorkItem
{
    // pk
    private String name;
    private Date timeStamp;
    //
    private ProductVersion productVersion;
    private ProductVersion targetProductVersion;
    private WorkItemStatus workItemStatus;
    private WorkItemType workItemType;
    private String text;
    private Client client;
    private Employee supportPerson;
    private Employee reviewer;
    private Employee developer;
    private Employee tester;
    private List workFlows;
    private static int timesConstructed;
    public int id;

    public WorkItem()
    {
        timesConstructed++;
        id = timesConstructed;
        /*
        Err.pr( "Fault ### CREATED id: " + id);
        if(id == 0)
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
        else if(!(o instanceof WorkItem))
        {// nufin
        }
        else
        {
            WorkItem test = (WorkItem) o;
            if((timeStamp == null
                ? test.timeStamp == null
                : timeStamp.equals(test.timeStamp)))
            {
                if((name == null ? test.name == null : name.equals(test.name)))
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
        result = 37 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public WorkItemStatus getWorkItemStatus()
    {
        return workItemStatus;
    }

    public void setWorkItemStatus(WorkItemStatus faultStatus)
    {
        this.workItemStatus = faultStatus;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ProductVersion getProductVersion()
    {
        return productVersion;
    }

    public void setProductVersion(ProductVersion product)
    {
        this.productVersion = product;
    }

    public String toString()
    {
        return getName();
    }

    public Employee getDeveloper()
    {
        return developer;
    }

    public void setDeveloper(Employee developer)
    {
        this.developer = developer;
    }

    public Employee getReviewer()
    {
        return reviewer;
    }

    public void setReviewer(Employee reviewer)
    {
        this.reviewer = reviewer;
    }

    public Employee getSupportPerson()
    {
        return supportPerson;
    }

    public void setSupportPerson(Employee supportPerson)
    {
        this.supportPerson = supportPerson;
    }

    public Employee getTester()
    {
        return tester;
    }

    public void setTester(Employee tester)
    {
        this.tester = tester;
    }

    public WorkItemType getWorkItemType()
    {
        return workItemType;
    }

    public void setWorkItemType(WorkItemType workItemType)
    {
        this.workItemType = workItemType;
    }

    public ProductVersion getTargetProductVersion()
    {
        return targetProductVersion;
    }

    public void setTargetProductVersion(ProductVersion targetProductVersion)
    {
        this.targetProductVersion = targetProductVersion;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public List getWorkFlows()
    {
        return workFlows;
    }

    public void setWorkFlows(List workFlows)
    {
        this.workFlows = workFlows;
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
