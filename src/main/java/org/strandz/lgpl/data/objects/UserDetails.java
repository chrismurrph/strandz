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

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.UserDetailsI;

import java.io.Serializable;

public class UserDetails implements Serializable, UserDetailsI
{
    private String username;
    private String password;
    private String databaseUsername;
    private String databasePassword;
    private String emailAddress;
    private boolean readOnly = true;
    
    private static final String EQUALS_PROP_NAMES[] = {
        "username",
        "password",
        "databaseUsername",
        "databasePassword",
        "emailAddress",    
    };
    
    public static UserDetails createUser( String username, String password, 
                                          String databaseUsername, String databasePassword, 
                                          String emailAddress, Boolean readOnly)
    {
        UserDetails result = new UserDetails();
        result.setUsername( username);
        result.setPassword( password);
        result.setDatabaseUsername( databaseUsername);
        result.setDatabasePassword( databasePassword);
        result.setEmailAddress( emailAddress);
        result.setReadOnly( readOnly);
        return result;
    }
    
    public String toString()
    {
        return getUsername() + ", " + getPassword() + ", " + getDatabaseUsername() + ", " + 
                getDatabasePassword() + ", RO: " + isReadOnly();
    }
    
    public boolean equals(Object o)
    {
        boolean result = false;
        Utils.chkType(o, this.getClass());

        String txt = "UserDetails " + this;
        ReasonNotEquals.addClassVisiting(txt);

        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof UserDetails))
        {
            ReasonNotEquals.addReason("not an instance of a UserDetails");
        }
        else
        {
            UserDetails test = (UserDetails) o;
            result = SelfReferenceUtils.equalsByProperties(UserDetails.EQUALS_PROP_NAMES, this, test);
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = SelfReferenceUtils.hashCodeByProperties(result, UserDetails.EQUALS_PROP_NAMES, this);
        return result;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getDatabaseUsername()
    {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername)
    {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword()
    {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword)
    {
        this.databasePassword = databasePassword;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public boolean isReadOnly()
    {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }
}
