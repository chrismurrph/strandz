/*
    Strandz - an API that matches the user to the dataStore.
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
import org.strandz.core.applichousing.SdzBag;
import org.strandz.core.applichousing.SimpleApplication;
import org.strandz.core.applichousing.SimplestApplicationStrandRunner;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.data.wombatrescue.business.LookupsProvider;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.cayenne.Worker;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.widgets.DebugJPanel;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import javax.swing.JComponent;
import java.util.Iterator;
import java.util.List;

public class TestActualRoster extends TestCase
{
    private RosterWorkersStrand visualStrand;
    //private TabbedPanel tabbedPanel;

    private static DataStore dataStore;
    //private static Lookups lookups;

    public static void main(String s[])
    {
        TestActualRoster obj = new TestActualRoster();
        try
        {
            obj.setUp();
        }
        catch(Exception e)
        {
            Err.error(e);
        }
        obj.testNotBlankAfterInsert();
        /*
        try
        {
            obj.tearDown();
        }
        catch(Exception e)
        {
            Err.error(e);
        }
        obj.testNotBlankAfterInsert();
        try
        {
            obj.tearDown();
        }
        catch(Exception e)
        {
            Err.error(e);
        }
        */
    }
    
    public TestActualRoster()
    {
        if(dataStore == null)
        {
            DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
            dataStoreFactory.addConnection(WombatConnectionEnum.DIRECT_DEMO_WOMBAT_CAYENNE);
            dataStore = dataStoreFactory.getDataStore();
            dataStore.setLookupsProvider( new LookupsProvider( dataStore));
            /*
            //Uncomment these lines for whenever DB corruption is 
            //expected to be the cause of test failures:
            Assert.isFalse( PopulateForDemo.KODO);
            Assert.isFalse( PopulateForDemo.PROD);
            PopulateForDemo.main();
            */
        }
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        RosterSessionUtils.setMemoryProperty( "thickClient", "true");
        RosterSessionUtils.setMemoryProperty( "live", "false");
        System.setProperty("java.awt.headless", "true");
        Err.setBatch(true); //will get a headless exception on server otherwise

        SimpleApplication simple = new SimpleApplication(true);
        simple.setDataStore(dataStore);
        visualStrand = new RosterWorkersStrand(simple, "dt-files/RosterWorkers_NON_VISUAL.xml");
        //rvs = new RosterWorkersStrand( simple);

        VisibleStrandAction vsa = new VisibleStrandAction(
            WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription(),
            WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription());
        vsa.setVisibleStrand(visualStrand);
        simple.addItem(vsa);

        SimplestApplicationStrandRunner strandRunner = new SimplestApplicationStrandRunner(simple);
        visualStrand.sdzInit();
        strandRunner.execute(vsa);

        // AlphabetScrollPane alpha = (AlphabetScrollPane)rvs.sbI.getPane(0);
        SdzBag sdzBag = (SdzBag) visualStrand.getSbI();
        JComponent panel = sdzBag.getStrandArea().getEnclosure();
        if(panel instanceof DebugJPanel)
        {
            DebugJPanel debugPanel = (DebugJPanel)panel;
            Err.pr( "sdzBag.getStrandArea().getEnclosure() ID: " + debugPanel.id);
            Err.pr( "sdzBag.getStrandArea().getEnclosure() contents");
            Print.prContents( panel);            
        }
        //Component comp = panel.getComponent(0);
        // Err.pr( "comp in enclosure is a " + comp.getClass().getName());
        //tabbedPanel = (TabbedPanel) comp;
        visualStrand.getDt().rosterSlotsListDetailNode.setAllowed(
            CapabilityEnum.COMMIT_ONLY, true);
        boolean ok = visualStrand.getDt().strand.getValidationContext().isOk();
        Err.pr(SdzNote.SECOND_TEST, "ValidationContext in setup is " + ok);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        boolean ok = visualStrand.getDt().strand.getValidationContext().isOk();
        Err.pr(SdzNote.SECOND_TEST, "ValidationContext in tearDown is " + ok);
        if(!ok)
        {
            visualStrand.getDt().strand.getValidationContext().setOk(true);
            ok = visualStrand.getDt().strand.getValidationContext().isOk();
            Err.pr(SdzNote.SECOND_TEST, "ValidationContext fixed: " + ok);
            if(!ok)
            {
                Err.error("Did not manage to right ValidationContext");
            }
        }
        System.setProperty("java.awt.headless", "false");
    }

    public static Test suite()
    {
        return new TestSuite(TestActualRoster.class);
    }

    static void tabToRosterSlots( RosterWorkersStrand visualStrand)
    {
        // rvs.getDt().rosterslotReferenceDetailNode.GOTO();
        Err.pr(SdzNote.SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY,
            "When fix this note then this line won't be needed:");
        // Must be a better way as status panel not being updated:
        //tabbedPane.setSelectedIndex( 1);
        //In non-applichousing environ (on server) trying to get first value to be non null
        //Worked on client without above
        if(visualStrand.getDt().strand.getCurrentNode() != visualStrand.getDt().rosterSlotsListDetailNode)
        {
            boolean ok = visualStrand.getDt().rosterSlotsListDetailNode.GOTO();
            if(!ok)
            {
                Err.error( "Could not go to " + visualStrand.getDt().rosterSlotsListDetailNode);
            }
        }
    }

    static void tabToVolunteers( RosterWorkersStrand visualStrand)
    {
        // rvs.getDt().volunteerNode.GOTO();
        Err.pr(SdzNote.SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY,
            "When fix this note then this line won't be needed:");
        // Must be a better way as status panel not being updated.
        // (Have since done invokeandWait)
        //tabbedPane.setSelectedIndex(0);
        //In non-applichousing environ (on server) trying to get first value to be non null
        //Worked on client without above
        if(visualStrand.getDt().strand.getCurrentNode() != visualStrand.getDt().workerNode)
        {
            visualStrand.getDt().workerNode.GOTO();
        }
    }

    private void showWhatAirleyHas()
    {
        for(Iterator iter = visualStrand.volunteerTriggers.masterList.iterator(); iter.hasNext();)
        {
            Worker vol = (Worker) iter.next();
            String first = vol.getChristianName();
            String second = vol.getSurname();
            if(first != null && second != null)
            {
                if(first.equals("Nick") && second.equals("Airley"))
                {
                    List slots = vol.getRosterslots(visualStrand.volunteerTriggers.detailList);
                    Err.pr("Slots in mem for Nick: ");
                    Print.prList(slots, "TestActualRoster.showWhatAirleyHas()");
                }
            }
        }

        // Err.pr( "@@ List been examined is " + rvs.volunteerTriggers.detailList.id);

        Object value = visualStrand.getDt().dayInWeeknameAttribute.getItemValue();
        Err.pr("@@ DayInWeek value of current slot's control: " + value);

        /*
         * Casting won't work now using Comp
        JComponent control = (JComponent)rvs.getDt().dayInWeeknameAttribute.getItem();
        Err.pr(
            "control inspecting was called " + control.getName()
            //+ " with id " + control.id
        );
        */
    }

    /**
     * Succeeds on Desktop but not on Server - so no longer running on Server
     */
    public void testDeleteRosterSlot()
    {
        visualStrand.volunteerTriggers.alphabetListener.findWorkerStartsWith("A");
        String christianName = (String)visualStrand.getDt().christianNameAttribute.getItemValue();
        String surnameName = (String)visualStrand.getDt().surnameAttribute.getItemValue();
        Err.pr( "Now at " + christianName + " " + surnameName);
        tabToRosterSlots( visualStrand);
        int rowCount = visualStrand.getDt().rosterSlotsListDetailNode.getRowCount();
        assertTrue( "Expected to see more than one roster slot for the first worker beginning with A, got " + rowCount, 
                    rowCount > 0);
        visualStrand.getDt().strand.REMOVE();
        assertTrue( visualStrand.getDt().rosterSlotsListDetailNode.getRowCount() == rowCount - 1);
        tabToVolunteers( visualStrand);
        boolean ok = false;
        try
        {
            ok = visualStrand.getDt().strand.NEXT();
        }
        catch(Exception ex)
        {
            fail( ex.toString());
        }
        assertTrue( ok);
    }    

    /**
     * Succeeds on Desktop but not on Server - so no longer running on Server
     */
    /**
     * This test is failing when run as part of "All current tests", yet is
     * ok when run from main, or as a test on its own.
     * <p/>
     * "Why plugging a NULL into flexibility that used to be no overnights"
     * <p/>
     * I did try invokeAndWait and sleeps around the place, but nothing helped.
     * Made this a JFCTestCase but exactly same problem! Sometimes it works.
     * <p/>
     * Now trying headless, so no longer a JFCTestCase, back to TestCase
     * <p/>
     * TODO
     * When looking at output different messages on linode than on client. Not
     * due to fact that Linode is actually running headless, as now they both
     * are. This will be an interesting note to track down - the answer may
     * deliver on the MVC promise laid out on the web site.
     * (Had to stop this as near to launch!! - will put back as server test and
     * fix soon after).
     */
    public void testNotBlankAfterInsert()
    {
        visualStrand.volunteerTriggers.alphabetListener.findWorkerStartsWith("A");
        String christianName = (String)visualStrand.getDt().christianNameAttribute.getItemValue();
        String surnameName = (String)visualStrand.getDt().surnameAttribute.getItemValue();
        Err.pr( "Now at " + christianName + " " + surnameName);
        tabToRosterSlots( visualStrand);

        int numSlots = visualStrand.getSbI().getStrand().getCurrentNode().getRowCount();
        if(numSlots != 1)
        {
            fail( "Expect to have one slot for this test, got " + numSlots);
        }
        boolean ok = visualStrand.getDt().strand.getValidationContext().isOk();
        Err.pr(SdzNote.SECOND_TEST, "ValidationContext b4 copyAction is " + ok);
        visualStrand.volunteerTriggers.copyAction.execute();
        if(SdzNote.SECOND_TEST.isVisible())
        {
            visualStrand.volunteerTriggers.mostRecentCopyBuffer.prAllAttributesForCopy();
        }
        ok = visualStrand.getDt().strand.getValidationContext().isOk();
        Err.pr(SdzNote.SECOND_TEST, "ValidationContext after copyAction is " + ok);
        visualStrand.getSbI().getStrand().INSERT();
        StateEnum state = visualStrand.getSbI().getStrand().getCurrentNode().getState();
        if(!state.isNew())
        {
            fail( "State must be NEW after INSERT, instead is " + state);
        }
        visualStrand.volunteerTriggers.pasteAction.execute();
        if(!visualStrand.getSbI().getStrand().getValidationContext().isOk())
        {
            fail( "After paste - " + visualStrand.getSbI().getStrand().getErrorMessage());
        }
        Object firstValue = visualStrand.getDt().dayInWeeknameAttribute.getItemValue();
        if(firstValue == null)
        {
            fail( "firstValue == null - this failure only happening on server (on client now!)");
        }
        Err.pr( "firstValue: " + firstValue);
        Err.pr( "After paste are in " + visualStrand.getSbI().getStrand().getCurrentNode().getState());
        int newNumSlots = visualStrand.getSbI().getStrand().getCurrentNode().getRowCount();
        if(newNumSlots != numSlots + 1)
        {
            fail("before post expect numSlots+1 rows");
        }
        // Doing COMMIT_ONLY is a note in itself
        //showWhatAirleyHas();
        // note is that this sets the control to null
        visualStrand.getSbI().getStrand().POST();
        //showWhatAirleyHas();
         /**/
        tabToVolunteers( visualStrand);
        showWhatAirleyHas();
        tabToRosterSlots( visualStrand);

        Object lastValue = visualStrand.getDt().dayInWeeknameAttribute.getItemValue();
        Err.pr("lastValue: " + lastValue);
        assertTrue(firstValue.equals(lastValue));
         /**/
    }
}
