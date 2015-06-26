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
package org.strandz.data.util;

import org.strandz.lgpl.util.UserDetailsI;
import org.strandz.lgpl.util.UserDetailsTransferObj;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DomainQueryEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.UserDetailsServiceI;
import org.strandz.lgpl.note.SdzNote;

import java.util.Iterator;
import java.util.List;

abstract public class AbstractUserDetailsService implements UserDetailsServiceI
{
    private DataStore ds;
    
    public DataStore getDS()
    {
        Err.pr( "In getDS(), but if touch the actual ds everything freezes");
        Err.pr( "getDS() to ret has name: " + ds.getName());
        Err.pr( "getDS() to ret has desc: " + ds.getDescription());
        return ds;
    }
    
    protected void setDataStore( DataStore ds)
    {
        Err.pr( "setDataStore() setting ds with name: " + ds.getName());
        Err.pr( "setDataStore() setting ds with desc: " + ds.getDescription());
        this.ds = ds;
        Err.pr( "setDataStore() have set to name: " + this.ds.getName());
    }

    private UserDetailsTransferObj getTransferObj( List<UserDetailsI> userDetailsList, String username)
    {
        UserDetailsTransferObj result = null;
        for(Iterator<UserDetailsI> iterator = userDetailsList.iterator(); iterator.hasNext();)
        {
            UserDetailsI userDetails = iterator.next();
            if(userDetails.getUsername().equals( username))
            {
                if(result != null)
                {
                    Err.error( "Have found > 1 UserDetails objects for username " + username);
                }
                result = new UserDetailsTransferObj( userDetails);
            }
        }
        return result;
    }
    
    /**
     * Must find one UserDetails for the username. We can throw serious errors here
     * because authentication has already been passed. 
     */
    public UserDetailsTransferObj getUserDetails(String username)
    {
        UserDetailsTransferObj result;
        Err.pr( "About to call getUserDetailsLazily()");
        List<UserDetailsI> userDetailsList = getUserDetailsLazily();
        Print.prList(userDetailsList, "All users found from lazy query");
        result = getTransferObj( userDetailsList, username);
        if(result == null)
        {
            /*
             * Doing it this way a new user will be recognised instantly but to get rid of an
             * old user the server will have to be rebooted. Should now be quicker as this
             * method is called all the time.
             */
            userDetailsList = getUserDetailsProperly();
            result = getTransferObj(userDetailsList, username);
            if(result == null) {
                Print.prList(userDetailsList, "All users found from query");
                Err.error("Have not found a UserDetails object for username " + username);
            }
        }
        return result;
    }
    
    abstract public DomainQueryEnum getAllUsersEnum();

    List<UserDetailsI> userDetails = null;
    private List<UserDetailsI> getUserDetailsLazily()
    {
        if(userDetails == null) {
            userDetails = getUserDetailsProperly();
        }
        return userDetails;
    }

    private List<UserDetailsI> getUserDetailsProperly()
    {
        List result;
        Err.pr( "In getUserDetailsProperly() abt to startTx()");
        strtTx();
        result = (getDS().getDomainQueries()).executeRetList( getAllUsersEnum());
        Err.pr( "Ret-ed list of size: " + result.size());
        return result;
    }

    private void strtTx()
    {
        if(!getDS().isOnTx())
        {
            String msg = "We have needed to force the start of a transaction for " + 
                    this + " using " + getDS();
            getDS().startTx(msg);
            Err.pr(SdzNote.BO_STUFF, msg);
        }
        else
        {
            Err.pr( "Already on tx, ds is " + getDS());
        }
    }

    public String getName()
    {
        return "User Details";
    }
}
