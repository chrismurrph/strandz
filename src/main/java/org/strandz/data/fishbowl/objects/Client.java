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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.IdentifierI;

import java.io.Serializable;
import java.util.ArrayList;

public class Client extends Entity
    implements Serializable, Comparable, IdentifierI
{
    private ArrayList contacts;
    private String description;
    private PostalAddress postalAddress;
    private ClientIndustry clientIndustry;
    private ClientType clientType;
    private Client endClient;
    private static int times;
    public static int constructedTimes;
    private int id;
    private boolean ro = false;

    public Client()
    {
        constructedTimes++;
        id = constructedTimes;
        /*
        Print.pr( "----------->  constructed client with id " + id);
        if(id == 0)
        {
        Err.stack();
        }
        */
    }

    public ArrayList getContacts()
    {
        return contacts;
    }

    public void setContacts(ArrayList contacts)
    {
        if(ro)
        {
            Err.error("READ ONLY");
        }
        this.contacts = contacts;
    }

    public String getCompanyName()
    {
        return super.getName();
    }

    public void setCompanyName(String companyName)
    {
        if(ro)
        {
            Err.error("READ ONLY");
        }
        super.setName(companyName);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public PostalAddress getPostalAddress()
    {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress)
    {
        if(ro)
        {
            Err.error("READ ONLY");
        }
        this.postalAddress = postalAddress;
    }

    public Client getEndClient()
    {
        return endClient;
    }

    public void setEndClient(Client endClient)
    {
        if(ro)
        {
            Err.error("READ ONLY");
        }
        this.endClient = endClient;
    }

    public ClientIndustry getClientIndustry()
    {
        return clientIndustry;
    }

    public void setClientIndustry(ClientIndustry clientIndustry)
    {
        if(ro)
        {
            Err.error("READ ONLY");
        }
        this.clientIndustry = clientIndustry;
    }

    public ClientType getClientType()
    {
        return clientType;
    }

    public void setClientType(ClientType clientType)
    {
        if(ro)
        {
            Err.error("READ ONLY");
        }
        if(clientType == null)
        {
            Err.error("In a Client, cannot set ClientType to be null");
        }
        this.clientType = clientType;
    }

    public String toString()
    {
        String endClientName = "";
        /*
        if(getEndClient() != null)
        {
        if(!JDOHelper.isDeleted( getEndClient()))
        {
        endClientName = getEndClient().getName();
        }
        }
        */
        return id + ", " + getName() + ", " + getEmail() + ", " + getPhone() + ", "
            + getFax() + ", " + getDescription() + ", " + endClientName;
    }

    public int compareTo(Object obj)
    {
        int result = 0;
        /*
        Client other = (Client)obj;
        String otherClientName = other.getCompanyName();
        String thisClientName = getCompanyName();
        if(otherClientName == null && thisClientName == null)
        {
        result = 0;
        }
        else
        {
        if(thisClientName == null || otherClientName == null)
        {
        //Err.error( "Invent something to return");
        result = -1;
        }
        else
        {
        result = thisClientName.compareTo( otherClientName);
        }
        }
        */
         /**/
        result = this.id - ((Client) obj).id;
         /**/
        return result;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if(!(obj instanceof Client)) // is implicit test for null as well
        {// nufin
        }
        else
        {
            result = (compareTo(obj) == 0);
            /*
            times++;
            Print.pr( "Cfed " + this + " with "
            + obj + " and got " + result + " times " + times);
            if(times == 0)
            {
            Err.stack();
            }
            */
        }
        return result;
    }

    public int hashCode()
    {
        int result = id;
        return result;
    }

    public void setRO(boolean b)
    {
        super.setRO(b);
        this.ro = b;
    }

    public int getId()
    {
        return id;
    }

    /**
     * So beanbox won't crash
     */
    /*
    protected EventListenerList listenerList = new EventListenerList();
    public void addActionListener(ActionListener l)
    {
    listenerList.add(ActionListener.class, l);
    }
    public void removeActionListener(ActionListener l)
    {
    listenerList.remove(ActionListener.class, l);
    }
    */
}
