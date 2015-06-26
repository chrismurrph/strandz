/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2005 Chris Murphy

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
import org.strandz.applic.wombatrescue.PrintRosterStrand;
import org.strandz.core.interf.Application;
import org.strandz.core.applichousing.SimplestApplication;
import org.strandz.data.wombatrescue.business.ParticularRosterFactory;
import org.strandz.data.wombatrescue.business.ParticularRosterI;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.business.RostererSessionFactory;
import org.strandz.data.wombatrescue.business.RostererSessionI;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.service.wombatrescue.ServerUploadRosterService;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

public class TestFileUpload extends TestCase
{
    private static DataStore dataStore;
    ServerUploadRosterService service;
    PrintRosterStrand nextRosterStrand;

    public static void main(String s[])
    {
        TestFileUpload obj = new TestFileUpload();
        obj.setUp();
        obj.testSimple();
    }

    public TestFileUpload()
    {
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection(WombatConnectionEnum.DEMO_WOMBAT_MEMORY);
        dataStore = dataStoreFactory.getDataStore();
    }
    
    protected void setUp()
    {
        RosterSessionUtils.setMemoryProperty( "thickClient", "true");
        RostererSessionI rostererSession =  RostererSessionFactory.newRostererSession( RostererSessionFactory.WITH_SERVICE);
        rostererSession.init( dataStore);
        ParticularRosterI particularRosterNext = ParticularRosterFactory.newParticularRoster( "TestFileUpload");
        particularRosterNext.init( rostererSession, dataStore);
        particularRosterNext.setAtMonth( RosteringConstants.NEXT);
        
        Application application = new SimplestApplication( true);
        nextRosterStrand = new PrintRosterStrand( application);
        nextRosterStrand.setBusinessObjects( particularRosterNext, rostererSession);
        nextRosterStrand.sdzInit();
        nextRosterStrand.display( true);
        service = new ServerUploadRosterService();
    }

    public static Test suite()
    {
      return new TestSuite( TestFileUpload.class );
    }

    public void testSimple()
    {
        String fileName = service.uploadRoster( nextRosterStrand.getRosterText(), RosteringConstants.TEST, MonthInYear.FEBRUARY);
        Err.pr( "File name written to is " + fileName);
        assertTrue(fileName != null);
        assertTrue( fileName.contains( "test_roster.html"));
    }
}
