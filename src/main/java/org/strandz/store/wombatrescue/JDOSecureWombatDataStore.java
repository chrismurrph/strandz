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
package org.strandz.store.wombatrescue;

import org.strandz.lgpl.store.SecureConnectionInfo;
import org.strandz.lgpl.store.ConnectionInfo;
import org.strandz.lgpl.util.Assert;

import java.util.Properties;

public class JDOSecureWombatDataStore extends JDOWombatDataStore
{
    private static int times;
    
    public JDOSecureWombatDataStore(SecureConnectionInfo connectionInfo)
    {
        super(connectionInfo);
    }

    public void setConnectionProperties(Properties props, ConnectionInfo connectionInfo)
    {
        String username = ((SecureConnectionInfo)connectionInfo).getUsername();
        String password = ((SecureConnectionInfo)connectionInfo).getPassword();
        Properties newProps = props; //a copy constructor would be better
        /* Happens in unit tests
        times++;
        if(times > 1)
        {
            Err.error( "Need a copy constructor for properties");
        }
        */
        Assert.notBlank( username, "For a " + this.getClass().getName() + " a username should have been set by the time are connecting");
        if(username != null)
        {
            newProps.setProperty( "javax.jdo.option.ConnectionUserName", username);
        }
        if(password != null)
        {
            newProps.setProperty( "javax.jdo.option.ConnectionPassword", password);
        }
        this.props = newProps;
    }
}
