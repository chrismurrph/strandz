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
package org.strandz.test.wombatrescue;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.strandz.applic.util.LoginHelper;
import org.strandz.data.util.UserDetailsFactory;
import org.strandz.data.util.UserDetailsProviderI;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.business.RostererSessionFactory;
import org.strandz.data.wombatrescue.business.RostererSessionI;
import org.strandz.data.wombatrescue.calculated.MonthTransferObj;
import org.strandz.data.wombatrescue.calculated.RosterTransferObj;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.NullDomainQueries;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.service.wombatrescue.ServerUploadRosterService;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

/**
 * Note that Kodo enhancement is not available when the tests are done (they are JPOX enhanced at
 * that time). This is fine - ensures you only call the service here. Note that when on the server 
 * we are talking to live.
 */
public class TestRemoteService extends TestCase
{
    DataStore dataStore;
    
    private static final WombatConnectionEnum DESKTOP_CONNECTION = WombatConnectionEnum.REMOTE_DEMO_WOMBAT_KODO;
    //private static final WombatConnectionEnum DESKTOP_CONNECTION = WombatConnectionEnum.DEMO_WOMBAT_MEMORY;
    private static final WombatConnectionEnum SERVER_CONNECTION = WombatConnectionEnum.REMOTE_WOMBAT_KODO;

    public static void main(String s[])
    {
        TestRemoteService obj = new TestRemoteService();
        obj.testComplexTwice();
        System.exit(0);
    }
    
    public TestRemoteService()
    {
        Err.setBatch( true);
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        WombatConnectionEnum connection;
        /*
        if(Utils.OS_NAME.startsWith("Windows"))
        {
            connection = DESKTOP_CONNECTION;
        }
        else
        */
        {
            connection = SERVER_CONNECTION;
        }
        dataStoreFactory.addConnection( connection);
        UserDetailsProviderI userDetails = UserDetailsFactory.newUserDetails( 
                UserDetailsFactory.TEST_FILE_USER_DETAILS);
        Object details[] = userDetails.get( 0);
        String username = (String)details[0];
        Assert.notNull( username, "Expect a username for auto login");
        String password = (String)details[1];
        Assert.notNull( password, "Expect a password for auto login");
        LoginHelper.Params params = LoginHelper.newParams(connection, dataStoreFactory, 
                   "Test Remote Service", 
                   //TeresaSpringConstant.CLIENT_CONTEXT_CONFIG_LOCATION, 
                   username, password, true);
        LoginHelper loginHelper = new LoginHelper( params);
        boolean ok = loginHelper.login();
        if(ok)
        {
            dataStore = dataStoreFactory.getEntityManagedDataStore();
            if(connection == WombatConnectionEnum.REMOTE_DEMO_WOMBAT_KODO || 
                    connection == WombatConnectionEnum.REMOTE_WOMBAT_KODO)
            {
                RosterSessionUtils.setMemoryProperty( "thickClient", "false");
            }
            else if(connection == WombatConnectionEnum.DEMO_WOMBAT_MEMORY)
            {
                RosterSessionUtils.setMemoryProperty( "thickClient", "true");
            }
            else
            {
                Err.error();
            }
        }
        else
        {
            Err.error( "Login to " + connection + " didn't work");
        }
    }
        
    public static Test suite()
    {
        return new TestSuite( TestRemoteService.class );
    }
    
    public void testSimple()
    {
        ServerUploadRosterService service = new ServerUploadRosterService();
        String fileName = service.uploadRoster( 
                /*nextRosterStrand.getRosterText()*/ "Blah blah", RosteringConstants.TEST, MonthInYear.FEBRUARY);
        Err.pr( "File name written to is " + fileName);
        assertTrue( fileName != null);
        assertTrue( fileName.contains( "test_roster.html"));
    }

    /**
     * Once when started storing state on the server the second roster would fail because an object (Worker)
     * on the server was not in a transaction.
     */
    public void testComplexTwice()
    {
        RostererSessionI rostererSession = RostererSessionFactory.newRostererSession( RostererSessionFactory.WITH_SERVICE);
        dataStore.setDomainQueries( new NullDomainQueries());
        rostererSession.init( dataStore);
        /* This works fine against local tomcat but want to do on server where not kodo enhanced 
        ParticularRosterI particularRosterNext = ParticularRosterFactory.newParticularRoster();
        particularRosterNext.init( rostererSession, dataStore);
        particularRosterNext.setAtMonth( RosteringConstants.NEXT);
        ParticularRosterI particularRosterCurrent = ParticularRosterFactory.newParticularRoster();
        particularRosterCurrent.init( rostererSession, dataStore);
        particularRosterCurrent.setAtMonth( RosteringConstants.CURRENT);
        
        Application application = new SimplestApplication( true);
        
        PrintRosterStrand nextRosterStrand = new PrintRosterStrand( application);
        nextRosterStrand.setBusinessObjects( particularRosterNext, rostererSession);
        nextRosterStrand.sdzInit();
        nextRosterStrand.display( true);
        
        PrintRosterStrand currentRosterStrand = new PrintRosterStrand( application);
        currentRosterStrand.setBusinessObjects( particularRosterCurrent, rostererSession);
        currentRosterStrand.sdzInit();
        currentRosterStrand.display( true);
        */
        MonthTransferObj month = new MonthTransferObj( MonthInYear.AUGUST, 2006);
        RosterTransferObj rosterTransferObj = rostererSession.getRosterService().getRoster( 
                RosteringConstants.ROSTER, month);
        assertTrue( rosterTransferObj.toString().equals( "Roster for August, 2006"));
        month = new MonthTransferObj( MonthInYear.SEPTEMBER, 2006);
        rosterTransferObj = rostererSession.getRosterService().getRoster( 
                RosteringConstants.ROSTER, month);
        assertTrue( rosterTransferObj.toString().equals( "Roster for September, 2006"));
    }
}
