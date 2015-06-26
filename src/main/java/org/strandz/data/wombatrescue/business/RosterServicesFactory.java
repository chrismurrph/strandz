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
package org.strandz.data.wombatrescue.business;

import org.springframework.security.core.Authentication;
import org.strandz.data.util.ServiceTypeEnum;
import org.strandz.data.util.SpringClientI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.store.SpringSecureConnectionInfo;
import org.strandz.lgpl.store.ConnectionInfo;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;

public class RosterServicesFactory
{
    private static ServiceTypeEnum rosterServiceType;
    private static int times;
        
    public static RosterServiceI newRosterService( ServiceTypeEnum type)
    {
        RosterServiceI result = null;
        if(type == ServiceTypeEnum.THICK)
        {
            result = new ThickClientRosterService();
        }
        else if(type == ServiceTypeEnum.THIN)
        {
            result = new ThinClientRosterService();
        }
        else
        {
            Err.error();
        }
        RosterServicesFactory.rosterServiceType = type;
        return result;
    }
    
    public static AbstractClientUploadRosterService newUploadRosterService( ServiceTypeEnum type)
    {
        AbstractClientUploadRosterService result = null;
        if(type == ServiceTypeEnum.THICK)
        {
            result = new ThickClientUploadRosterService();
        }
        else if(type == ServiceTypeEnum.THIN)
        {
            result = new ThinClientUploadRosterService();
        }
        else
        {
            Err.error();
        }
        return result;
    }

    public static void init( RosterServiceI rosterServiceI, DataStore ds)
    {
        Assert.notNull( rosterServiceI, "Cannot init a rosterServiceI when have not first constructed one");
        if(rosterServiceType == ServiceTypeEnum.THICK)
        {
            ((ThickClientRosterService)rosterServiceI).init( ds);
        }
        else if(rosterServiceType == ServiceTypeEnum.THIN)
        {
            initialiseService( (SpringClientI)rosterServiceI, (EntityManagedDataStore)ds);
        }
        else
        {
            Err.error( rosterServiceType.toString());
        }
    }
    
    public static void init( UploadRosterServiceI uploadRosterServiceI, DataStore ds)
    {
        Assert.notNull( uploadRosterServiceI, "Cannot init a uploadRosterServiceI when have not first constructed one");
        if(rosterServiceType == ServiceTypeEnum.THICK)
        {
            //No initiaisation required
            //((ThickClientUploadRosterService)uploadRosterServiceI).init( ds);
        }
        else if(rosterServiceType == ServiceTypeEnum.THIN)
        {
            initialiseService( (SpringClientI)uploadRosterServiceI, (EntityManagedDataStore)ds);
        }
        else
        {
            Err.error( rosterServiceType.toString());
        }
    }    
    
//    private static void initialiseService( SpringClientI springClientI, DataStore ds, ParticularRoster particularRoster)
//    {
//        Assert.notNull( springClientI);
//        initialiseService( springClientI, (EntityManagedDataStore)ds, particularRoster);
//    }
    
    private static void initialiseService( SpringClientI springClientI, EntityManagedDataStore ds)
    {
        ConnectionInfo connectionInfo = ds.getConnection();
        if(connectionInfo instanceof SpringSecureConnectionInfo)
        {
            SpringSecureConnectionInfo secure = (SpringSecureConnectionInfo)connectionInfo;
            Authentication authentication = (Authentication)secure.getAuthenticationObject();
            if(authentication != null)
            {
                Assert.isTrue(!authentication.isAuthenticated(), "Have already obtained authorization");
                springClientI.setAuthentication( authentication);
            }
            else
            {
                //As long as don't attempt to do any rostering then this is ok
                Err.pr( "No authentication object found");
            }
        }
        else
        {
            times++;
            Err.pr( "An insecure connection <" + connectionInfo.getName() +
                "> might mean you can't access the service: " + springClientI.getName());
            if(times == 0)
            {
                Err.stack();
            }
        }
        Assert.notNull( ds);
        springClientI.setDataStore( ds);
    }
}
