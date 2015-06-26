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
package org.strandz.data.needs.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.Date;

public class Contact
{
    public static Contact NULL = new Contact();
    // pk
    private String firstName;
    private String secondName;
    //
    private String jobTitle;
    private String companyName;
    private Address address;
    private String mobilePhone;
    private String dayPhone;
    private String eveningPhone;
    private String emailAddress1;
    private String emailAddress2;
    private String comments;
    private Date dateCreated;
    private Date dateLastUpdated;

    public Contact()
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
        else if(!(o instanceof Contact))
        {// nufin
        }
        else
        {
            Contact test = (Contact) o;
            if((firstName == null
                ? test.firstName == null
                : firstName.equals(test.firstName)))
            {
                if((secondName == null
                    ? test.secondName == null
                    : secondName.equals(test.secondName)))
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
        result = 37 * result + (firstName == null ? 0 : firstName.hashCode());
        result = 37 * result + (secondName == null ? 0 : secondName.hashCode());
        return result;
    }

    /*
    * class XMLWombatData.GroupNameSurnameOrder operates
    * by the same rules, enabling SET_ROW to give the
    * right result.
    */
    public boolean startsWith(String str)
    {
        boolean result = false;
        if(secondName != null)
        {
            if(secondName.startsWith(str))
            {
                result = true;
            }
        }
        else if(firstName != null)
        {
            if(firstName.startsWith(str))
            {
                result = true;
            }
        }
        return result;
    }

    public String toString()
    {
        String result = null;
        if(firstName != null || secondName != null)
        {
            if(firstName == null && secondName != null)
            {
                result = secondName;
            }
            else if(firstName != null && secondName == null)
            {
                result = firstName;
            }
            else
            {
                result = firstName + " " + secondName;
            }
        }
        else
        {
            Err.error("Must have either a first name or a second name");
        }
        return result;
    }

    public Address getAddress()
    {
        return address;
    }

    public String getComments()
    {
        return comments;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public Date getDateLastUpdated()
    {
        return dateLastUpdated;
    }

    public String getDayPhone()
    {
        return dayPhone;
    }

    public String getEmailAddress1()
    {
        return emailAddress1;
    }

    public String getEmailAddress2()
    {
        return emailAddress2;
    }

    public String getEveningPhone()
    {
        return eveningPhone;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getJobTitle()
    {
        return jobTitle;
    }

    public String getMobilePhone()
    {
        return mobilePhone;
    }

    public String getSecondName()
    {
        return secondName;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public void setDateLastUpdated(Date dateLastUpdated)
    {
        this.dateLastUpdated = dateLastUpdated;
    }

    public void setDayPhone(String dayPhone)
    {
        this.dayPhone = dayPhone;
    }

    public void setEmailAddress1(String emailAddress1)
    {
        this.emailAddress1 = emailAddress1;
    }

    public void setEmailAddress2(String emailAddress2)
    {
        this.emailAddress2 = emailAddress2;
    }

    public void setEveningPhone(String eveningPhone)
    {
        this.eveningPhone = eveningPhone;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
    }

    public void setMobilePhone(String mobilePhone)
    {
        this.mobilePhone = mobilePhone;
    }

    public void setSecondName(String secondName)
    {
        this.secondName = secondName;
    }
}
