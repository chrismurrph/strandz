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

import junit.framework.TestCase;
import org.strandz.applic.wombatrescue.TheRosterDT;
import org.strandz.applic.wombatrescue.TheRosterStrand;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.applichousing.SimpleApplication;
import org.strandz.core.applichousing.SimplestApplicationStrandRunner;
import org.strandz.data.wombatrescue.business.LookupsProvider;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import javax.swing.JFrame;
import java.util.List;

/**
 * INTENTION (Need turn FILENAME into portableFilename as per RosterWorkerStrand):
 * To have NV and Visual versions
 * NV:
 * This one runs on the server. To run on the client use the applichousing subclass. If you
 * really want to run this on the client then bring RosterWorkers_NON_VISUAL.xml
 * up to date by starting with RosterWorkers_NEW_FORMAT.xml and doing the substitution
 * the same as is done on the client.
 */
public class VisualTestTheRoster extends TestCase
{
    private TheRosterDT dt;

    private static DataStore dataStore;
    
    public static void main(String s[]) throws Exception
    {
        VisualTestTheRoster obj = new VisualTestTheRoster();
        obj.setUp();
        obj.testGetAllNodes();
        obj.tearDown();
    }
    
    public VisualTestTheRoster()
    {
        MessageDlg.setFrame( new JFrame());
        if(dataStore == null)
        {
            DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
            dataStoreFactory.addConnection(WombatConnectionEnum.DEMO_WOMBAT_MEMORY);
            dataStore = dataStoreFactory.getDataStore();
            dataStore.setLookupsProvider( new LookupsProvider( dataStore));
        }
        if(getNonVisual())
        {
            Err.setBatch( true);
        }
        else
        {
            Err.setBatch( false);
        }
    }
    
    boolean getNonVisual()
    {
        return true;
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        SimpleApplication simple = new SimpleApplication( getNonVisual());
        simple.setDataStore(dataStore);
        TheRosterStrand visibleStrand = new TheRosterStrand( simple, null, true);
        VisibleStrandAction vsa = new VisibleStrandAction( null, null);
        vsa.setVisibleStrand( visibleStrand);
        simple.addItem(vsa);

        SimplestApplicationStrandRunner strandRunner = new SimplestApplicationStrandRunner(simple, getNonVisual());
        visibleStrand.sdzInit();
        dt = visibleStrand.getDt();
        boolean ok = visibleStrand.getSbI().getStrand().getValidationContext().isOk();
        Err.pr( "Strand read in ok: " + ok);
        strandRunner.execute(vsa);        
    }
    
    public void testGetAllNodes()
    {
        List<Node> nodes = dt.strand.getNodes();
        Print.prList( nodes, "testGetAllNodes() gives");
        assertTrue( nodes.size() == 3);
    }
}
