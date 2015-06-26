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
package org.strandz.data.fishbowl.objects;

import org.strandz.lgpl.data.objects.Timespace;
import org.strandz.lgpl.util.Err;

import java.io.Serializable;

public class Job extends Entity implements Serializable, Comparable
{
    private String description;
    // private ArrayList jobRequirements;
    private JobType jobType;
    private Client client;
    private String status;
    private Timespace timespace;
    private String startingInstructions;
    private String endingInstructions;

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String toString()
    {
        return "[" + getDescription() + ", " + client + ", " + jobType + "]";
    }

    public String getContact()
    {
        return super.getName();
    }

    public void setContact(String contact)
    {
        super.setName(contact);
    }

    public StreetAddress getSite()
    {
        return super.getAddress();
    }

    public void setSite(StreetAddress site)
    {
        super.setAddress(site);
    }

    /*
    public ArrayList getJobRequirements()
    {
    return jobRequirements;
    }
    public void setJobRequirements( ArrayList jobRequirements)
    {
    this.jobRequirements = jobRequirements;
    }
    */

    public JobType getJobType()
    {
        return jobType;
    }

    public void setJobType(JobType jobType)
    {
        this.jobType = jobType;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        if(status.equals("Unfilled") || status.equals("Filled")
            || status.equals("Invoiced") || status.equals("Paid"))
        {// ok
        }
        else
        {
            Err.error("Job can only have 1 of 4 statuses");
        }
        this.status = status;
    }

    /**
     * All the associated JobRequirement timespaces should
     * be within this one.
     */
    public Timespace getTimespace()
    {
        return timespace;
    }

    public void setTimespace(Timespace timespace)
    {
        this.timespace = timespace;
    }

    public String getStartingInstructions()
    {
        return startingInstructions;
    }

    public void setStartingInstructions(String startingInstructions)
    {
        this.startingInstructions = startingInstructions;
    }

    public String getEndingInstructions()
    {
        return endingInstructions;
    }

    public void setEndingInstructions(String endingInstructions)
    {
        this.endingInstructions = endingInstructions;
    }

    public int compareTo(Object obj)
    {
        int result = 0;
        Job other = (Job) obj;
        String otherJobDesc = other.getDescription();
        String thisJobDesc = getDescription();
        if(otherJobDesc == null && thisJobDesc == null)
        {
            result = 0;
        }
        else
        {
            if(thisJobDesc == null || otherJobDesc == null)
            {
                // Err.error( "Invent something to return");
                result = -1;
            }
            else
            {
                result = thisJobDesc.compareTo(otherJobDesc);
            }
        }
        return result;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if(!(obj instanceof Job)) // is implicit test for null as well
        {// nufin
        }
        else
        {
            result = (compareTo(obj) == 0);
            // Err.pr( "Cfed " + this + " with "
            // + obj + " and got " + result);
        }
        return result;
    }
}
