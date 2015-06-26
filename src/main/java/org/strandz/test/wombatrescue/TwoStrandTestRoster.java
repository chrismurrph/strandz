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
import org.strandz.applic.wombatrescue.RosterWorkersStrand;
import org.strandz.applic.wombatrescue.RosterWorkers_NEW_FORMATDT;
import org.strandz.data.wombatrescue.business.LookupsProvider;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DataStoreOpsUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.core.domain.ValidationContext;

/**
 * This one runs on the server. To run on the client use the applichousing subclass. If you
 * really want to run this on the client then bring RosterWorkers_NON_VISUAL.xml
 * up to date by starting with RosterWorkers_NEW_FORMAT.xml and doing the substitution
 * the same as is done on the server.
 */
public class TwoStrandTestRoster extends TestCase
{
    private RosterWorkersStrand rosterableWorkersVisualStrand;
    private RosterWorkersStrand unrosterableWorkersVisualStrand;
    //
    //Rosterable
    private RosterWorkers_NEW_FORMATDT dtR;
    //Unrosterable
    private RosterWorkers_NEW_FORMATDT dtU;
    private static DataStore dataStore;

    /**
     * Rather than doing this, use the applichousing version (VisualTestRoster) if you are on the Desktop.
     * If want to use will need to create the NonVisual DT file which is automatically produced
     * on the server. 
     */
    public static void main(String s[]) throws Exception
    {
        TwoStrandTestRoster obj = new TwoStrandTestRoster();
        obj.setUp();
        obj.testTabSwitchingChangingRosterability();
        obj.tearDown();
    }
    
    public TwoStrandTestRoster()
    {
        if(dataStore == null)
        {
            DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
            dataStoreFactory.addConnection(WombatConnectionEnum.DEMO_WOMBAT_MEMORY);
            dataStore = dataStoreFactory.getDataStore();
            dataStore.setLookupsProvider( new LookupsProvider(dataStore));
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
        RosterSessionUtils.setMemoryProperty( "thickClient", "true");
        RosterSessionUtils.setMemoryProperty( "live", "false");
        String rosterableTriggersTitle = WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription();
        String unrosterableTriggersTitle = WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription();
        if(getNonVisual())
        {
            rosterableWorkersVisualStrand = new RosterWorkersStrand( 
                    rosterableTriggersTitle, dataStore, "dt-files/RosterWorkers_NON_VISUAL.xml");
            unrosterableWorkersVisualStrand = new RosterWorkersStrand( 
                    unrosterableTriggersTitle, dataStore, "dt-files/RosterWorkers_NON_VISUAL.xml");
        }
        else
        {
            rosterableWorkersVisualStrand = new RosterWorkersStrand( 
                    rosterableTriggersTitle, dataStore, "dt-files/RosterWorkers_NEW_FORMAT.xml");
            unrosterableWorkersVisualStrand = new RosterWorkersStrand( 
                    unrosterableTriggersTitle, dataStore, "dt-files/RosterWorkers_NEW_FORMAT.xml");
        }

        rosterableWorkersVisualStrand.sdzInit();
        unrosterableWorkersVisualStrand.sdzInit();
        
        //Lets shore up TestRoster against what other test cases have done before:
        reverseChanges( rosterableWorkersVisualStrand);
        reverseChanges( unrosterableWorkersVisualStrand);
        
        dtR = rosterableWorkersVisualStrand.getDt();
        dtU = unrosterableWorkersVisualStrand.getDt();
        
        //Query
        dtU.workerNode.GOTO();
        dtU.strand.EXECUTE_QUERY();
        dtR.workerNode.GOTO();
        dtR.strand.EXECUTE_QUERY();
    }
    
    private static void reverseChanges( RosterWorkersStrand visibleStrand)
    {
        ValidationContext validationContext = visibleStrand.getSbI().getStrand().getValidationContext();
        boolean ok = validationContext.isOk();
        Err.pr(SdzNote.SECOND_TEST, "ValidationContext at start of setUp is " + ok);
        if(!ok)
        {
            validationContext.setOk(true);
        }
        visibleStrand.disableToggle(true);
    }

    public static Test suite()
    {
        return new TestSuite(TwoStrandTestRoster.class);
    }

    public void testTabSwitchingChangingRosterability()
    {
        //Make sure of numbers in each node:
        int rosterableCount = dtR.workerNode.getRowCount();
        assertTrue( "Rosterable count is not 24 but " + rosterableCount, rosterableCount == 24);
        int unrosterableCount = dtU.workerNode.getRowCount();
        assertTrue( "UnRosterable count is not 2 but " + unrosterableCount, unrosterableCount == 2);
        Err.pr( rosterableCount);
        Err.pr( unrosterableCount);
        //Navigate to the rosterable Joshua:
        dtR.strand.ENTER_QUERY();
        dtR.christianNameAttribute.setItemValue( "Joshua");
        dtR.strand.EXECUTE_SEARCH();
        assertTrue( dtR.christianNameAttribute.getItemValue().equals( "Joshua"));
        //Unroster him:
        dtR.unknownAttribute.setItemValue( true);
        //Save changes:
        dtR.strand.COMMIT_RELOAD();
        //Expect number of rosterable to be one less:
        int afterRosterableCount = dtR.workerNode.getRowCount();
        assertTrue( afterRosterableCount == rosterableCount - 1);
        //Expect number of Un-rosterable to be one more, once have picked them up:
        dtU.strand.EXECUTE_QUERY();
        int afterUnrosterableCount = dtU.workerNode.getRowCount();
        assertTrue( afterUnrosterableCount == unrosterableCount + 1);        
    }
    
    public void testTabSwitching()
    {
        dtR.strand.COMMIT_RELOAD();
        //dtU.strand.COMMIT_RELOAD();
        //assertFalse( dtU.rosterSlotspecificDateAttribute.hasChanged());
    }

    public void display()
    {
        DataStoreOpsUtils.viewData(dataStore);
    }

}
