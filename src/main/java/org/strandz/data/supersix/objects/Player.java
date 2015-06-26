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
package org.strandz.data.supersix.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;

import java.util.Date;
import java.io.Serializable;

/**
 *  First Name  
 *  Surname 
 *  Contact Ph No 
 *  Email Address 
 *  Home Address  
 *  Date of Birth 
 *  Age
 */
public class Player implements Comparable, Serializable //for debugging even thou fetch groups not working
{
    private boolean dummy;
    public transient static Player NULL = new Player();
    private int ordinal;
    private String firstName;
    private String surname;
    private String contactPhoneNumber;
    private String emailAddress;
    private String homeAddress1;
    private String homeAddress2;
    private Date dateOfBirth;
    /**
     * Put here because Kodo will automatically do a 1:M mapping if there is an inverse
     * relationship (rather than a M:M)
     */
    private TeamCompetitionSeason teamCompetitionSeason;

    private static final String equalsPropNames[] = {
        "dummy",
        "firstName",
        "surname",
        "emailAddress",
    };

    public Player()
    {

    }

    public int compareTo(Object o)
    {
        int result = 0;
        Player other = (Player) o;
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
        result = Utils.compareTo(firstName, other.getFirstName());
        if(result == 0)
        {
            result = Utils.compareTo(surname, other.getSurname());
            if(result == 0)
            {
                result = Utils.compareTo(emailAddress, other.getEmailAddress());
            }
        }
        return result;
    }

    public boolean equals(Object o)
    {
        boolean result = false;
        Utils.chkType(o, this.getClass());

        String txt = "Player " + this;
        ReasonNotEquals.addClassVisiting(txt);

        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Player))
        {
            ReasonNotEquals.addReason("not an instance of a Player");
        }
        else
        {
            Player test = (Player) o;
            result = SelfReferenceUtils.equalsByProperties(Player.equalsPropNames, this, test);
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = SelfReferenceUtils.hashCodeByProperties(result, Player.equalsPropNames, this);
        return result;
    }

    public String toString()
    {
        String result = null;
        result = "Player: <" + getFirstName() + "> <" +
            getSurname() + ">";
        return result;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getContactPhoneNumber()
    {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber)
    {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getHomeAddress1()
    {
        return homeAddress1;
    }

    public void setHomeAddress1(String homeAddress)
    {
        this.homeAddress1 = homeAddress;
    }

    public String getHomeAddress2()
    {
        return homeAddress2;
    }

    public void setHomeAddress2(String homeAddress)
    {
        this.homeAddress2 = homeAddress;
    }
    
    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public int getOrdinal()
    {
        return ordinal;
    }

    public void setOrdinal(int ordinal)
    {
        this.ordinal = ordinal;
    }

    public boolean isDummy()
    {
        return dummy;
    }

    public void setDummy(boolean dummy)
    {
        this.dummy = dummy;
    }

    public TeamCompetitionSeason getTeamCompetitionSeason()
    {
        return teamCompetitionSeason;
    }

    public void setTeamCompetitionSeason(TeamCompetitionSeason teamCompetitionSeason)
    {
        this.teamCompetitionSeason = teamCompetitionSeason;
    }
}
