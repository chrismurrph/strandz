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
package org.strandz.applic.wombatrescue;

import org.strandz.applic.util.LoginHelper;
import org.strandz.applic.util.PropertiesChecker;
import org.strandz.data.util.UserDetailsFactory;
import org.strandz.data.util.UserDetailsProviderI;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;

import javax.swing.SwingUtilities;

/**
 * REMOTE_DEMO_WOMBAT_CAYENNE is also to a TEST DB, but is three-tiered.
 *
 * 1./ Obtain user details, in order to get the DB un/pw
 * (1)Make sure that $SDZ/property-files/wombatrescue/clientContext.xml references
 * teresa/spring/clientforremote.properties, that clientforremote.properties is
 * pointing to the right server. In clientContext.xml point to clientforlocal.properties
 * to point to the local tomcat server. Accessed via the properties jar file,
 * so you will need to (2)re-jar from %SDZ%/property-files/wombatrescue using
 * 'ant cayenne-demo'.
 *
 * 2./ Connect to a Cayenne service
 * (3)REMOTE_DEMO_WOMBAT_CAYENNE static below.
 *
 * 3./ Remote service itself
 * The WombatrescueDemoDataNode.driver.xml file that exists in wr-cayenne-driver.jar on the server
 * determines authentication to the DB.
 */
public class StartupDemoCayenne extends Startup
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new StartupDemoCayenne().setVisible(true);
            }
        });
    }

    public StartupDemoCayenne()
    {
        RosterSessionUtils.setMemoryProperty( "thickClient", "false");
        RosterSessionUtils.setMemoryProperty( "live", "false");
        RosterSessionUtils.setMemoryProperty( "memory", "false");
        init();
        Err.setVisualDurationMonitor( true);
        dataStoreFactory.addConnection( WombatConnectionEnum.REMOTE_DEMO_WOMBAT_CAYENNE);
    }

    public boolean setCredentials()
    {
        String username = "Mike";
        String password = "Mike";
        LoginHelper.Params params = LoginHelper.newParams(
            WombatConnectionEnum.REMOTE_DEMO_WOMBAT_CAYENNE, dataStoreFactory,
                   "Rosterer Login",
                   username, password, false);
        LoginHelper loginHelper = new LoginHelper( params);
        boolean ok = loginHelper.login();
        if(ok)
        {
            RosterSessionUtils.setDataStore(dataStoreFactory.getDataStore());
        }
        return ok;
    }
}