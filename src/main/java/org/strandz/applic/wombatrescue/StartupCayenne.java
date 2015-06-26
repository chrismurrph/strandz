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
 * REMOTE_WOMBAT_CAYENNE is to a TEST DB.
 * REMOTE_DEMO_WOMBAT_CAYENNE is also to a TEST DB, but is three-tiered.
 * This program only runs on live when use REMOTE_PROD_WOMBAT_CAYENNE.
 *
 * 1./ Obtain user details, in order to get the DB un/pw
 * (1)Make sure that $SDZ/property-files/wombatrescue/clientContext.xml references
 * teresa/spring/clientforremote.properties, that clientforremote.properties is
 * pointing to the right server. In clientContext.xml point to clientforremote.properties
 * to point to the Linode server or clientforlocal.properties to point to the local tomcat
 * server. Despite the names they are both REMOTE! Accessed via the properties jar file,
 * so you will need to (2)re-jar from %SDZ%/property-files/wombatrescue using
 * 'ant cayenne-demo' for DEMO or 'ant cayenne-prod' for PROD.
 *
 * 2./ Connect to a Cayenne service
 * (3)All you need to do here is change the CONNECTION static below.
 *
 * 3./ Remote service itself
 * The Wombatrescue[Prod|Demo]DataNode.driver.xml file that exists in wr-cayenne-driver.jar on the server
 * determines authentication to the DB. It is not version controlled for obvious reasons. Also 
 * this is convenient because the file that exists on each machine points to its own database.
 */
public class StartupCayenne extends Startup
{
    /**
     * When change this normally also change Spring so that it points to the same server. See 1./ above
     */
    //private static final WombatConnectionEnum CONNECTION = WombatConnectionEnum.REMOTE_DEMO_WOMBAT_CAYENNE;
    //private static final WombatConnectionEnum CONNECTION = WombatConnectionEnum.REMOTE_WOMBAT_CAYENNE;
    private static final WombatConnectionEnum CONNECTION = WombatConnectionEnum.REMOTE_PROD_WOMBAT_CAYENNE;

    public static final boolean EASY_AUTHENTICATION = false;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new StartupCayenne().setVisible(true);
            }
        });
    }

    public StartupCayenne()
    {
        RosterSessionUtils.setMemoryProperty( "thickClient", "false");
        RosterSessionUtils.setMemoryProperty( "live", "true");
        RosterSessionUtils.setMemoryProperty( "memory", "false");
        init();
        Err.setVisualDurationMonitor( true);
        dataStoreFactory.addConnection( CONNECTION);
        if(CONNECTION == WombatConnectionEnum.REMOTE_PROD_WOMBAT_CAYENNE)
        {
            PropertiesChecker propertiesChecker = new PropertiesChecker( getPropertyFileName(), properties, this.getClass().getName());
            propertiesChecker.chkSecureServicePropertiesSet();
        }
    }

    public boolean setCredentials()
    {
        boolean isLive = RosterSessionUtils.getProperty( "live").equals( "true");
        String username = null;
        String password = null;
        if(!isLive)
        {
            //If not live then automatically fill the login screen:
            if(EASY_AUTHENTICATION)
            {
                //When have done a fresh call to PopulateForDemo, rather than the data having been
                //populated from PROD, or MakeNewUserDetails having been called on this DB.
                username = "Mike";
                password = "Mike";
            }
            else
            {
                UserDetailsProviderI userDetails = UserDetailsFactory.newUserDetails( 
                    UserDetailsFactory.LIVE_TERESA_USER_DETAILS);
                Assert.notNull( userDetails, "Classpath does not contain a UserDetailsProviderI - " +
                    "fix this by setting static JUST_POPULATED to true in " + getClass().getName());
                Object details[] = userDetails.get( 0);
                username = (String)details[0];
                Assert.notNull( username, "Expect a username for auto login");
                password = (String)details[1];
                Assert.notNull( password, "Expect a password for auto login");
            }
        }
        LoginHelper.Params params = LoginHelper.newParams( CONNECTION, dataStoreFactory,
                   "Rosterer Login", //TeresaSpringConstant.CLIENT_CONTEXT_CONFIG_LOCATION,
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