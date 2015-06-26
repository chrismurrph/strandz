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

import java.io.Serializable;
import java.util.Date;

public class Communication implements Serializable
{
    private Date date;
    private String description;
    private CommunicationType communicationType;
    private Client client;
    private Associate associate;
    private Unavailability unavailability;
    private JobRequirement jobRequirement;

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public CommunicationType getCommunicationType()
    {
        return communicationType;
    }

    public void setCommunicationType(CommunicationType communicationType)
    {
        this.communicationType = communicationType;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public Associate getAssociate()
    {
        return associate;
    }

    public void setAssociate(Associate associate)
    {
        this.associate = associate;
    }

    public Unavailability getUnavailability()
    {
        return unavailability;
    }

    public void setUnavailability(Unavailability unavailability)
    {
        this.unavailability = unavailability;
    }

    public JobRequirement getJobRequirement()
    {
        return jobRequirement;
    }

    public void setJobRequirement(JobRequirement jobRequirement)
    {
        this.jobRequirement = jobRequirement;
    }
}
