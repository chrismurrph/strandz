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
package org.strandz.applic.supersix;

import org.strandz.applic.util.LoginHelper;
import org.strandz.applic.util.PropertiesChecker;
import org.strandz.data.util.UserDetailsProviderI;
import org.strandz.data.supersix.business.SuperSixManagerUtils;
import org.strandz.data.supersix.domain.SuperSixConnectionEnum;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.data.util.UserDetailsFactory;

/**
 * 1./ Obtain user details, in order to get the DB un/pw 
 * Make sure that $SDZ/property-files/supersix/clientContext.xml references
 * supersix/spring/clientforremote.properties, that clientforremote.properties is
 * pointing to the right server. Accessed via the properties jar file, so you
 * will need to re-jar
 * 
 * 2./ Connect to a Kodo service
 * For example if CONNECTION points to WombatConnectionEnum.REMOTE_WOMBAT_KODO
 * then we will be talking to a remote Kodo service on the real server.
 * Files such as remote_teresa_kodo_mysql.properties
 * are obtained from the properties jar file. Will allow to access a
 * service: http://www.strandz.org/teresaRemoteService/kodoservice
 * 
 * 3./ Remote service itself
 * The kodo.properties file that exists on the server determines access to the DB.
 * It is not version controlled for obvious reasons. Also this is convenient because
 * the file that exists on each machine points to its own database. 
 */
public class StartupKodo extends Startup
{
    //private LoginDialog loginDialog;

    static final String CLIENT_CONTEXT_CONFIG_LOCATION = "supersix/spring/clientContext.xml";

    /**
     * When change this normally also change Spring so that it points to the same server. See 1./ above
     */
    private static final SuperSixConnectionEnum CONNECTION = SuperSixConnectionEnum.REMOTE_DEMO_SUPERSIX_KODO;
    //private static final SuperSixConnectionEnum CONNECTION = SuperSixConnectionEnum.REMOTE_SUPERSIX_KODO;
    
    public static void main(String[] args)
    {
        new StartupKodo().setVisible(true);
    }    

    public StartupKodo()
    {
        if(CONNECTION == SuperSixConnectionEnum.REMOTE_SUPERSIX_KODO)
        {
            SuperSixManagerUtils.setMemoryProperty( "live", "true");
        }
        else
        {
            SuperSixManagerUtils.setMemoryProperty( "live", "false");
        }
        SuperSixManagerUtils.setMemoryProperty( "memory", "false");
        init();
        Err.setVisualDurationMonitor( true);
        dataStoreFactory.addConnection( CONNECTION);        
        PropertiesChecker propertiesChecker = new PropertiesChecker( getPropertyFileName(), properties, this.getClass().getName());
        propertiesChecker.chkSecureServicePropertiesSet();
    }

//        SuperSixManagerUtils.setDataStore( dataStoreFactory.getDataStore());

//    public boolean setCredentials()
//    {
//        LoginAction whenLogInAction =
//                new LoginAction( CLIENT_CONTEXT_CONFIG_LOCATION, CONNECTION, dataStoreFactory);
//        loginDialog = new LoginDialog( MessageDlg.getFrame(), "SuperSix Administrator Login", whenLogInAction, "Leo", "Leo");
//        loginDialog.setVisible( true);
//        SuperSixManagerUtils.setDataStore( dataStoreFactory.getDataStore());
//        return whenLogInAction.isOkLogin();
//    }
    
    public boolean setCredentials()
    {
        //LoginAction logInAction =
        //        new LoginAction( CLIENT_CONTEXT_CONFIG_LOCATION, CONNECTION, dataStoreFactory);
        String username = null;
        String password = null;
        boolean isLive = SuperSixManagerUtils.getProperty( "live").equals( "true");
        if(SdzNote.USER_DETAILS.isVisible() && !isLive)
        {
            UserDetailsProviderI userDetails = UserDetailsFactory.newUserDetails( UserDetailsFactory.LIVE_SUPERSIX_USER_DETAILS);
            Object details[] = userDetails.get( 0);
            username = (String)details[0];
            password = (String)details[1];
        }
        LoginHelper.Params params = LoginHelper.newParams( CONNECTION, dataStoreFactory, "SuperSix Login", 
                                                           //CLIENT_CONTEXT_CONFIG_LOCATION, 
                                                           username, password, false);
        LoginHelper helper = new LoginHelper( params);
        boolean ok = helper.login();
        //loginDialog = new LoginDialog( MessageDlg.getFrame(), "SuperSix Login", logInAction, username, password);
        //loginDialog.setVisible( true);
        //loginDialog.securePanel.tfUsername.requestFocusInWindow();
        if(ok)
        {
            SuperSixManagerUtils.setDataStore( dataStoreFactory.getDataStore());
        }
        return ok;
    }
}
