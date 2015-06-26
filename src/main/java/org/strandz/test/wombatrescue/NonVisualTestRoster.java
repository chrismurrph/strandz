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
import org.strandz.applic.wombatrescue.PlayParticularAgain;
import org.strandz.applic.wombatrescue.Refill;
import org.strandz.applic.wombatrescue.RosterWorkersStrand;
import org.strandz.applic.wombatrescue.RosterWorkersTriggers;
import org.strandz.core.domain.MsgUtils;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.ReferenceLookupAttribute;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.interf.Cell;
import org.strandz.core.prod.Session;
import org.strandz.core.record.PlayerI;
import org.strandz.core.applichousing.SimpleApplication;
import org.strandz.core.applichousing.SimplestApplicationStrandRunner;
import org.strandz.data.wombatrescue.business.LookupsProvider;
import org.strandz.data.wombatrescue.business.ParticularRosterFactory;
import org.strandz.data.wombatrescue.business.ParticularRosterI;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.RosterSlot;
import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DataStoreOpsUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Assert;
import org.strandz.store.wombatrescue.MemoryWombatDataStore;
import org.strandz.store.wombatrescue.WombatConnections;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.task.data.wombatrescue.Flush;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This one runs on the server. To run on the client use the applichousing subclass. If you
 * really want to run this on the client then bring RosterWorkers_NON_VISUAL.xml
 * up to date by starting with RosterWorkers_NEW_FORMAT.xml and doing the substitution
 * the same as is done on the client.
 */
public class NonVisualTestRoster extends TestCase
{
    SdzBagI sdzBag;
    Node workerNode;
    Node rosterSlotNode;
    Node quickRosterSlotNode;
    Strand workerStrand;
    MemoryWombatDataStore data;
    RosterWorkersTriggers triggers;
    RosterWorkersStrand visualStrand;
    //
    RuntimeAttribute christianNameAttribute;
    RuntimeAttribute mobilePhoneAttribute;
    RuntimeAttribute belongsToGroupAttribute;
    RuntimeAttribute streetAttribute;
    RuntimeAttribute email1Attribute;
    //
    RuntimeAttribute firstShiftAttribute;
    RuntimeAttribute intervalAttribute;
    RuntimeAttribute startDateAttribute;
    RuntimeAttribute weekInMonthAttribute;
    RuntimeAttribute dayAttribute;
    RuntimeAttribute whichShiftAttribute;
    RuntimeAttribute disabledAttribute;
    RuntimeAttribute rosterSlotquickactiveAttribute;
    RuntimeAttribute rosterSlotmonthlyRestartAttribute;    
    //

    private static DataStore dataStore;

    /**
     * Rather than doing this, use the applichousing version (VisualTestRoster) if you are on the Desktop.
     * If want to use will need to create the NonVisual DT file which is automatically produced
     * on the server. 
     */
    public static void main(String s[]) throws Exception
    {
        NonVisualTestRoster obj = new NonVisualTestRoster();
        /*
        obj.setUp();
        try
        {
          obj.testCopyPaste();
        }
        catch(AssertionFailedError err)
        {
          Err.pr( SdzNote.secondTest, "Keep going");
        }
        obj.tearDown();
        */
        obj.setUp();
        //obj.testNotInsertBlank();
        //obj.testIntervalRSValidationWeekly();
        //obj.testPaneNode();
        //obj.testNotInsertBlank();
        //obj.testRosterLookup();
        //obj.testCopyPaste();
        //obj.testEffectOfDetailChange();
        //obj.testDetailToDetail();
        obj.testMemoryVersion();
        obj.tearDown();
    }
    
    public NonVisualTestRoster()
    {
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

    protected void tearDown() throws Exception
    {
        super.tearDown();
        /* overkill
        dataStore.rollbackTx();
        String args[] = new String[0];
        PopulateForDemo.main();
        */
        boolean ok = workerStrand.getValidationContext().isOk();
        Err.pr(SdzNote.SECOND_TEST, "ValidationContext at end of tearDown is " + ok);
        if(!ok)
        {
            workerStrand.getValidationContext().setOk(true);
            Err.pr(SdzNote.SECOND_TEST, "ValidationContext at end of tearDown is " + ok);
            if(!ok)
            {
                workerStrand.getValidationContext().setOk(true);
                ok = workerStrand.getValidationContext().isOk();
                Err.pr(SdzNote.SECOND_TEST, "ValidationContext fixed: " + ok);
                if(!ok)
                {
                    Err.error("Did not manage to right ValidationContext");
                }
            }
        }
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        RosterSessionUtils.setMemoryProperty( "thickClient", "true");
        RosterSessionUtils.setMemoryProperty( "live", "false");
        //Crashing on server? - endless loop or something
        //prolly b/c RosterWorkers_NON_VISUAL.xml not in a jar file so need
        //a special dt jar file. Wrong - build puts it in the same jar file
        //as the normal one. Actual problem was due to RosterWorkers_NON_VISUAL.xml
        //not being in version control and hence not getting into jar on server.
        //Now this discovery made, TestRoster.java can be made to work on the server.
        SimpleApplication simple = new SimpleApplication( getNonVisual());
        simple.setDataStore(dataStore);
        if(getNonVisual())
        {
            visualStrand = new RosterWorkersStrand( simple , "dt-files/RosterWorkers_NON_VISUAL.xml");
        }
        else
        {
            visualStrand = new RosterWorkersStrand(simple, "dt-files/RosterWorkers_NEW_FORMAT.xml");
        }

        VisibleStrandAction vsa = new VisibleStrandAction(
            WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription(),
            WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription());
        vsa.setVisibleStrand(visualStrand);
        simple.addItem(vsa);

        SimplestApplicationStrandRunner strandRunner = new SimplestApplicationStrandRunner(simple, getNonVisual());
        visualStrand.sdzInit();
        //Is there any point in this line? No - b/c RosterWorkersStrand created
        //RosterWorkersTriggers which does this anyway.
        //setData() didn't previously use an em - don't need when running memory but confusing for JDO - no other way with JDO
        //rv.sbI.getStrand().setEntityManager( rv.getData().getEM());
        //Lets shore up TestRoster against what other test cases have done before:
        boolean ok = visualStrand.getSbI().getStrand().getValidationContext().isOk();
        Err.pr(SdzNote.SECOND_TEST, "ValidationContext at start of setUp is " + ok);
        if(!ok)
        {
            visualStrand.getSbI().getStrand().getValidationContext().setOk(true);
        }
        //
        strandRunner.execute(vsa);
        /*
         * Now using SimplestApplicationStrandRunner instead (At REV 27). Had no effect on errors, so you
         * don't HAVE to use an Application
        rv.sdzSetup();
        rv.preForm();
        rv.display( true, RosterWorkersStrand.ROSTERABLE_VOLS);
        */
        triggers = visualStrand.volunteerTriggers;
        data = (MemoryWombatDataStore) visualStrand.getDataStore();
        visualStrand.disableToggle(true);
        sdzBag = visualStrand.getSbI();
        workerStrand = visualStrand.getSbI().getStrand();
        workerNode = visualStrand.getSbI().getNode(0);
        christianNameAttribute = workerNode.getAttributeByName("christianName");
        if(christianNameAttribute == null)
        {
            Print.prArray(workerNode.getAttributes(), "TestRoster.setUp()");
            Err.error("No such attribute as \"christianName\"");
        }
        else
        {
            //Print.prArray( volunteerNode.getAttributes(), "TestRoster.setUp()");
        }
        mobilePhoneAttribute = workerNode.getAttributeByName( "mobilePhone");
        if(mobilePhoneAttribute == null)
        {
            Print.prArray(workerNode.getAttributes(), "TestRoster.setUp()");
            Err.error("No such attribute as \"mobilePhoneAttribute\"");
        }
        else
        {
            //Print.prArray( volunteerNode.getAttributes(), "TestRoster.setUp()");
        }
        belongsToGroupAttribute = workerNode.getAttributeByName("belongsToGroup");
        streetAttribute = workerNode.getAttributeByName("street");
        email1Attribute = workerNode.getAttributeByName("email1");
        if(visualStrand.getSbI().getNodes().length > 1)
        {
            rosterSlotNode = visualStrand.getSbI().getNode(1);
            firstShiftAttribute = rosterSlotNode.getCell().getAttributeByName("startDate");
            intervalAttribute = rosterSlotNode.getAttributeByCellAndName(
                "interval Lookup Cell", "name");
            weekInMonthAttribute = rosterSlotNode.getAttributeByCellAndName(
                "weekInMonth Lookup Cell", "name");
            dayAttribute = rosterSlotNode.getAttributeByCellAndName(
                "dayInWeek Lookup Cell", "name");
            whichShiftAttribute = rosterSlotNode.getAttributeByCellAndName(
                "whichShift Lookup Cell", "name");
            //Node rosterSlotsListDetailNode = workerStrand.getNodeByName( "rosterSlots List Detail Node");            
            disabledAttribute = rosterSlotNode.getCell().getAttributeByName( "rosterSlot disabled");
            Cell rosterSlotsQuickCell = workerStrand.getNodeByName( "rosterSlots Quick List Detail Node").getCell();
            rosterSlotquickactiveAttribute = rosterSlotsQuickCell.getAttributeByName( "rosterSlot quick active");
            Cell rosterSlotsCell = rosterSlotNode.getCell();
            rosterSlotmonthlyRestartAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot monthlyRestart");   
            //
            quickRosterSlotNode = workerStrand.getNodeByName( "rosterSlots Quick List Detail Node"); 
        }
        else
        {
            Err.error("Always now running with two nodes");
        }
        //After query always pops this to true, so can't 
        //weekInMonthAttribute.setItemValue( null);
    }

    public static Test suite()
    {
        return new TestSuite(NonVisualTestRoster.class);
    }

    public void testIntervalRSValidationNotWeekly()
    {
        rosterSlotNode.GOTO();
        workerStrand.INSERT();
        intervalAttribute.setItemValue("fortnightly");
        dayAttribute.setItemValue("Friday");
        boolean ok = workerStrand.POST();
        assertFalse(ok);
    }

    public void testEffectOfDetailChange()
    {
        rosterSlotNode.GOTO();
        Boolean origValue = (Boolean)disabledAttribute.getItemValue();
        Boolean changeToValue = new Boolean( !origValue);  
        disabledAttribute.setItemValue( changeToValue);
        Boolean changedToValue = new Boolean((Boolean)disabledAttribute.getItemValue());
        assertTrue( "Did not manage to change disabled to " + changeToValue, changeToValue.equals( changedToValue));
        workerNode.GOTO();
        Object itemValue = rosterSlotquickactiveAttribute.getItemValue();
        /*
         * TODO
         * Following is a bug that needs to be fixed as rosterSlotquickactiveAttribute is a table style
         * attribute and thus getting its item value ought to return a List of Booleans
         */
        /* Incorrect statement above. Tables should (now) work just like fields - so not return a list
           but return the item value at the current row
        assertTrue( "VisualTestRoster seems to be returning as Boolean type: " + 
                itemValue.getClass().getName(), itemValue instanceof List);
        List roList = (List)itemValue;
        Boolean summaryIsActive = new Boolean((Boolean)roList.get( 0));
        */
        Boolean summaryIsActive = (Boolean)itemValue; 
        Err.pr( "Have changed disabled from " + origValue + " to " + changeToValue);
        Err.pr( "On master active is " + summaryIsActive);
        assertTrue( "Have changed disabled to " + changeToValue +
            ", yet on summary active remains at " + summaryIsActive, !changeToValue.equals( summaryIsActive));
    }

    /**
     * fails
     * No reason why going directly from a detail to another detail should mean that the
     * target (another detail) node becomes FROZEN. For workaround see testDetailToMasterToDetail(). 
     */
    public void testDetailToDetail()
    {
        quickRosterSlotNode.GOTO();
        rosterSlotNode.GOTO();
        StateEnum state = rosterSlotNode.getState();
        Err.pr( state);
        assertFalse( "Expect failure, evidenced by making rosterSlotNode frozen", state == StateEnum.FROZEN);
    }
    
    /**
     * passes
     */
    public void testDetailToMasterToDetail()
    {
        quickRosterSlotNode.GOTO();
        workerNode.GOTO();
        rosterSlotNode.GOTO();
        StateEnum state = rosterSlotNode.getState();
        Err.pr( state);
        assertFalse( state == StateEnum.FROZEN);
    }
    
    void insertRS()
    {
        rosterSlotNode.GOTO();
        workerStrand.INSERT();
    }
    
    /**
     * A new roster slot such that the worker's rule
     * is to -be rostered every week for the Friday
     * dinner shift- should pass validation
     */
    public void testIntervalRSValidationWeekly()
    {
        insertRS();
        intervalAttribute.setItemValue("weekly");
        dayAttribute.setItemValue("Friday");
        whichShiftAttribute.setItemValue("dinner");
        boolean ok = workerStrand.POST();
        assertTrue(ok);
    }
    
    public void testComponentTableViewTabSwitch()
    {
        testIntervalRSValidationWeekly();
        workerNode.GOTO();
    }    

    public void testComponentTableViewTabSwitchWithRefresh()
    {
        testIntervalRSValidationWeekly();
        quickRosterSlotNode.REFRESH();
        workerNode.GOTO();
    }    
    
    public void _testSetDisplayNoDetailData()
    {
        if(WombatConnections.DEFAULT_DATABASE
            == WombatConnectionEnum.TEST)
        {
            workerStrand.addNodeChangeListener(
                new PlayParticularAgain.LocalNodeChangeListener(visualStrand, visualStrand.getDt()));
            ((PlayerI) sdzBag.getStrand()).replayRecorded(
                "play/new_broken_combination.xml",
                PlayParticularAgain.getStartupCode());
            workerStrand.POST(); // did commit b4
        }
    }

    /**
     * The test is that when commit then reload, the details
     * actually go to the database. (Do prList() of what committing,
     * and when see an extra slot, success).
     */
    public void _testCommitReload()
    {
        if(WombatConnections.DEFAULT_DATABASE
            == WombatConnectionEnum.TEST)
        {
            List rosterSlots = (List) data.get(POJOWombatData.ROSTER_SLOT);
            int oldRosterSlotCount = rosterSlots.size();
            Print.prList(rosterSlots, "TestRoster.testCommitReload()");
            workerStrand.addNodeChangeListener(
                new PlayParticularAgain.LocalNodeChangeListener(visualStrand, visualStrand.getDt()));
            ((PlayerI) sdzBag.getStrand()).replayRecorded("play/commit_reload.xml",
                PlayParticularAgain.getStartupCode());
            rosterSlots = (List) data.get(POJOWombatData.ROSTER_SLOT);
            Print.prList(rosterSlots, "TestRoster.testCommitReload()");

            int newRosterSlotCount = rosterSlots.size();
            assertTrue(newRosterSlotCount == (oldRosterSlotCount + 1));
        }
    }

    /**
     * When reload do not want to see the details anymore
     * Will only work when can go to a volunteer w/out any
     * slots.
     */
    public void _testReloadDetails()
    {
        if(WombatConnections.DEFAULT_DATABASE
            == WombatConnectionEnum.TEST)
        {
            workerStrand.addNodeChangeListener(
                new PlayParticularAgain.LocalNodeChangeListener(visualStrand, visualStrand.getDt()));
            ((PlayerI) sdzBag.getStrand()).replayRecorded(
                "play/reload_details.xml", PlayParticularAgain.getStartupCode());
            assertTrue(rosterSlotNode.getRowCount() == 0);
        }
    }

    /**
     * Does not replicate the error as the focusing events are not
     * recorded (YET).
     */
    public void _nullNotAllowed()
    {
        if(WombatConnections.DEFAULT_DATABASE
            == WombatConnectionEnum.TEST)
        {
            workerStrand.addNodeChangeListener(
                new PlayParticularAgain.LocalNodeChangeListener(visualStrand, visualStrand.getDt()));
            ((PlayerI) sdzBag.getStrand()).replayRecorded(
                "play/null_not_allowed.xml", PlayParticularAgain.getStartupCode());
        }
    }

    /**
     * After had done a reload, a post would always set the dataStore
     * strangely. Here we insert a new rosterslot for Simon and
     * make sure that it is as we set it after we post.
     */
    public void _badPost()
    {
        if(WombatConnections.DEFAULT_DATABASE
            == WombatConnectionEnum.TEST)
        {
            workerStrand.addNodeChangeListener(
                new PlayParticularAgain.LocalNodeChangeListener(visualStrand, visualStrand.getDt()));
            ((PlayerI) sdzBag.getStrand()).replayRecorded("play/bad_post.xml",
                PlayParticularAgain.getStartupCode());
            Err.pr(dayAttribute.getItemValue());
            assertTrue(
                dayAttribute.getItemValue().equals(DayInWeek.SUNDAY.getName()));
        }
    }

    /**
     * Why is Mario's day being set to null when try to set it to
     * 'Monday' from 'Thursday'.
     */
    public void _testChangeDay()
    {
        if(WombatConnections.DEFAULT_DATABASE
            == WombatConnectionEnum.TEST)
        {
            workerStrand.addNodeChangeListener(
                new PlayParticularAgain.LocalNodeChangeListener(visualStrand, visualStrand.getDt()));
            ((PlayerI) sdzBag.getStrand()).replayRecorded("play/change_day.xml",
                PlayParticularAgain.getStartupCode());
        }
    }

    public void _testChangeFirstName()
    {
        if(WombatConnections.DEFAULT_DATABASE
            == WombatConnectionEnum.TEST)
        {
            workerStrand.addNodeChangeListener(
                new PlayParticularAgain.LocalNodeChangeListener(visualStrand, visualStrand.getDt()));
            ((PlayerI) sdzBag.getStrand()).replayRecorded(
                "play/change_first_name.xml", PlayParticularAgain.getStartupCode());
        }
    }

    private void addTwoVolunteers()
    {
        workerStrand.INSERT();
        if(christianNameAttribute == null)
        {
            Err.error("How on earth could christianNameAttribute become null");
        }
        christianNameAttribute.setItemValue("Chris");
        mobilePhoneAttribute.setItemValue("0594223445");
        workerStrand.INSERT();
        christianNameAttribute.setItemValue("Brian");
        mobilePhoneAttribute.setItemValue("0524323415");
    }

    /**
     * No matter which is the 'current pane', should be
     * able to run around the nodes, and behaviour be
     * exactly the same.
     * Error was getting was that NEXT() would change to
     * another node.
     * Reason was that focusing was causing a node change.
     * Solution testing here is for node to be able to be
     * made unresponsive by setting new property
     * focusCausesNodeChange.
     */
    public void testPaneNode()
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                // Had to do this in Designer
                // rosterNode.setFocusCausesNodeChange( false);
                addTwoVolunteers();
                sdzBag.setCurrentPane(1);
                Err.pr("Current Pane: " + sdzBag.getCurrentPane());
                Err.pr("Current Node: " + workerStrand.getCurrentNode());
                Err.pr("Current Row: " + workerStrand.getCurrentNode().getRow());
                assertTrue(sdzBag.getCurrentPane() == 1);
                workerStrand.SET_ROW(0);
                assertTrue(workerStrand.getCurrentNode().getRow() == 0);
                assertTrue(workerStrand.getCurrentNode() == workerNode);

                boolean ok = workerStrand.NEXT();
                if(ok)
                {
                    Err.pr("NEXT returned: " + ok);
                    Err.pr("Current Pane: " + sdzBag.getCurrentPane());
                    Err.pr("Current Node: " + workerStrand.getCurrentNode());
                    Err.pr("Current Row: " + workerStrand.getCurrentNode().getRow());
                    assertTrue(sdzBag.getCurrentPane() == 1);
                    assertTrue(workerStrand.getCurrentNode().getRow() == 1);
                    assertTrue(workerStrand.getCurrentNode() == workerNode);
                }
            }
        });
    }

    public void changeNotHappening()
    {
        if(workerStrand.getCurrentNode() != workerNode)
        {
            Err.error(
                "Expected to start testRosterMario() with volunteerNode current");
        }
        rosterSlotNode.GOTO();

        int rowCount = rosterSlotNode.getRowCount();
        if(rowCount != 1)
        {
            Err.error("Expected one row for this test");
        }

        String value = (String) whichShiftAttribute.getItemValue();
        assertTrue(value != null);
        assertTrue(value.equals("dinner"));
        whichShiftAttribute.setItemValue("overnight");
        new MessageDlg("Clear output here");
        workerStrand.POST();
        value = (String) whichShiftAttribute.getItemValue();
        assertTrue(value != null);
        assertTrue(value.equals("overnight"));
    }

    public void testNotInsertBlank()
    {
        Err.pr( "Error handler is " + workerStrand.getErrorHandler());
        boolean blank = workerNode.isBlankRecord();
        workerNode.setBlankRecord(false);
        workerStrand.INSERT();
        String msg = null;
        boolean ok = workerStrand.NEXT();
        if(!ok)
        {
            msg = workerStrand.getErrorMessage();
        }
        else
        {
            fail("volStrand.NEXT() was supposed to fail");
        }
        assertTrue(workerStrand.getErrorHistory().size() == 1);
        if(!msg.contains(MsgUtils.RECORD_IS_BLANK))
        {
            fail(workerStrand.getErrorMessage());
        }
        workerNode.setBlankRecord(blank);
    }

    /**
     * Are not able to get the item value that set.
     * This test failing.
     * <p/>
     * Have put thru debugger and is visiting the same control
     * When test as a normal user it works ok
     */
    public void testRosterLookup()
    {
        // Not flushing whole thing anymore
        //Flush.main( new String[] { "POJOWombatData", "TEST"});
        //volStrand.EXECUTE_QUERY();

        /*
        int rowCount = volunteerNode.getRowCount();
        if(rowCount != 0)
        {
          Err.error(
              "Expected to be flushed "
                  + "before testRosterLookup(), but still have " + rowCount);
        }
        */
        if(workerStrand.getCurrentNode() != workerNode)
        {
            Err.error(
                "Expected to start testRosterLookup() with volunteerNode current");
        }
        workerStrand.INSERT();
        christianNameAttribute.setItemValue("Chris");
        rosterSlotNode.GOTO();
        workerStrand.INSERT();
        whichShiftAttribute.setItemValue("dinner");
        dayAttribute.setItemValue("Sunday");
        intervalAttribute.setItemValue("weekly");
        firstShiftAttribute.setItemValue(TimeUtils.getDateFromString("30/07/2003"));
        weekInMonthAttribute.setItemValue("first of month");
        workerStrand.INSERT();
        whichShiftAttribute.setItemValue("dinner");
        dayAttribute.setItemValue("Sunday");
        intervalAttribute.setItemValue("weekly");
        firstShiftAttribute.setItemValue(TimeUtils.getDateFromString("30/07/2003"));
        weekInMonthAttribute.setItemValue("first of month");
        workerStrand.PREVIOUS();

        String value = (String) whichShiftAttribute.getItemValue();
        assertTrue(value != null);
        assertTrue(value.equals("dinner"));
        whichShiftAttribute.setItemValue("overnight");
        //Because we haven't committed yet this should make the dataStore the same again
        //(only if doesn't fail)
        //volStrand.EXECUTE_QUERY();
    }

    // Probably not right dataStore anymore
    public void _testRunFile()
    {
        workerStrand.addNodeChangeListener(
            new PlayParticularAgain.LocalNodeChangeListener(visualStrand, visualStrand.getDt()));
        ((PlayerI) sdzBag.getStrand()).replayRecorded("play/add_Caroline.xml",
            PlayParticularAgain.getStartupCode());
        assertTrue(workerNode.getRowCount() == 1);
        workerStrand.REMOVE();
        workerStrand.COMMIT_ONLY();
        assertTrue(workerNode.getRowCount() == 0);
    }

    // Not flushing TEST anymore
    public void _testSameLookupObject1()
    {
        // Flush.main( new String[]{ "POJOWombatData", "TEST"});
        // volStrand.EXECUTE_QUERY(); //so flush visible to ml!
        List slots = (List) data.get(POJOWombatData.ROSTER_SLOT);
        if(slots.size() != 0)
        {
            fail("Supposed to start off with no slots, have " + slots.size());
        }

        int rowCount = workerNode.getRowCount();
        if(rowCount != 0)
        {
            Err.error("Expected to be flushed before testSameLookupObject1");
        }
        if(workerStrand.getCurrentNode() != workerNode)
        {
            Err.error(
                "Expected to start testSameLookupObject1 with volunteerNode current");
        }
        workerStrand.INSERT();
        christianNameAttribute.setItemValue("Chris");
        rosterSlotNode.GOTO();
        workerStrand.INSERT();
        whichShiftAttribute.setItemValue("dinner");
        dayAttribute.setItemValue("Sunday");
        intervalAttribute.setItemValue("weekly");
        firstShiftAttribute.setItemValue("30/07/2003");
        weekInMonthAttribute.setItemValue("first of month");
        workerStrand.POST();
        Print.prList(slots, "TestRoster._testSameLookupObject1()");
        workerStrand.INSERT();
        whichShiftAttribute.setItemValue("dinner");
        dayAttribute.setItemValue("Sunday");
        intervalAttribute.setItemValue("weekly");
        firstShiftAttribute.setItemValue("30/07/2003");
        weekInMonthAttribute.setItemValue("first of month");
        workerStrand.POST();
        if(!dataStore.getConnection().getVersion().isORM())
        {
            if(slots.size() != 2)
            {
                fail(
                    "Supposed to have added two slots at this stage, have "
                        + slots.size());
            }

            RosterSlot first = (RosterSlot) slots.get(0);
            RosterSlot second = (RosterSlot) slots.get(1);
            // In physical memory we should be referring to the same RosterSlot
            if(first.getWhichShift() != second.getWhichShift())
            {
                fail("While inserting a second, did not reuse the first roster slot");
            }
        }
        workerStrand.COMMIT_ONLY();
    }

    /**
     * JDO gives actual numbers, while XML only
     * fills three spots.
     */
    public void _testFlushFillDisplay()
    {
        /*
        Flush.main( new String[]{ "POJOWombatData", "TEST"});
        Refill.main( new String[]{ "POJOWombatData", "TEST"});
        */
        DataStoreOpsUtils.viewData(dataStore);
    }

    public void display()
    {
        DataStoreOpsUtils.viewData(dataStore);
    }

    // Too dependent on dataStore, will do later
    public void _testSameLookupObject2()
    {
        //boolean jdo = (dataStore.getConnection().getVersion() == ORMTypeEnum.JDO);
        /**/
        Flush.main(new String[]{"POJOWombatData", "TEST"});
        Refill.main(new String[]{"POJOWombatData", "TEST"});
        workerNode.GOTO();
        workerStrand.EXECUTE_QUERY();

        /**/
        List allDataRosterSlots = (List) data.get(POJOWombatData.ROSTER_SLOT);
        if(workerStrand.getCurrentNode() != workerNode)
        {
            Err.error(
                "Expected to start testSameLookupObject2() with volunteerNode current");
        }
        Err.pr("We are at " + christianNameAttribute.getItemValue());

        /*
        boolean ok = volStrand.NEXT();
        if(!ok)
        {
        Err.error();
        }
        */
        boolean ok = false;
        Err.pr("We are at " + christianNameAttribute.getItemValue());
        if(dataStore.getConnection().getVersion().isORM())
        {
            ok = workerStrand.NEXT();
            if(!ok)
            {
                Err.error("Can't go NEXT, prob b/c dataStore not as expected");
            }
            Err.pr("We are at " + christianNameAttribute.getItemValue());
            ok = workerStrand.NEXT(); // To Martin (XML was not ordered)
            if(!ok)
            {
                Err.error();
            }
            Err.pr("We are at " + christianNameAttribute.getItemValue());
        }
        rosterSlotNode.GOTO();
        ok = workerStrand.NEXT();
        if(!ok)
        {
            Err.pr("roster node size: " + rosterSlotNode.getRowCount());
            Err.error(
                "This test assumes that Martin has at least"
                    + " two roster slots, not " + rosterSlotNode.getRowCount());
        }

        String name = (String) christianNameAttribute.getItemValue();
        Err.pr("We are rostering for " + name);
        if(!name.equals("Martin"))
        {
            fail("Expecting to be at Martin");
        }

        ReferenceLookupAttribute att = (ReferenceLookupAttribute)
            rosterSlotNode.getCell().getAttributeByName("whichShift");
        // effectively the fk:
        WhichShift secondWhichShift = (WhichShift) att.getItemAdapter().getItemValue();
        Err.pr("secondWhichShift: " + secondWhichShift);
         /**/
         /**/
        workerStrand.INSERT();
        whichShiftAttribute.setItemValue("overnight");
        dayAttribute.setItemValue("Friday");
        intervalAttribute.setItemValue("weekly");
        firstShiftAttribute.setItemValue("30/07/2003");
        workerStrand.POST();

        List martinScreenRosterSlots = new ArrayList();
        WhichShift createdWhichShift = (WhichShift) att.getItemAdapter().getItemValue();
        if(secondWhichShift != createdWhichShift)
        {
            Err.pr("secondWhichShift: " + secondWhichShift);
            Err.pr("createdWhichShift: " + createdWhichShift);

            String msg = "Did not expect to actually create another WhichShift!";
            fail(msg);
        }
        else
        {
            Err.pr("createdWhichShift: " + createdWhichShift);
            Err.pr("secondWhichShift: " + secondWhichShift);
        }
         /**/
    }

    public void testCopyPaste()
    {
        if(SwingUtilities.isEventDispatchThread())
        {
            Err.error("Don't want TestRoster.testCopyPaste() to be running in EDT");
        }
        Session.getRecorder().setRecordingName("test_copy_paste");
        if(workerStrand.getCurrentNode() != workerNode)
        {
            Err.error(
                "Expected to start testSameLookupObject2() with volunteerNode current");
        }
        workerStrand.INSERT();
        if(christianNameAttribute == null)
        {
            Err.error("christianNameAttribute == null");
        }
        christianNameAttribute.setItemValue("Chris");
        mobilePhoneAttribute.setItemValue("0594223445");
        boolean ok = rosterSlotNode.GOTO();
        assertTrue( ok);
        workerStrand.INSERT();
        whichShiftAttribute.setItemValue("overnight");
        dayAttribute.setItemValue("Wednesday");
        intervalAttribute.setItemValue("weekly");
        firstShiftAttribute.setItemValue(TimeUtils.getDateFromString("30/07/2003"));
        ok = workerStrand.POST();
        String err = null;
        if(!ok)
        {
            err = workerStrand.getErrorMessage();
        }
        assertTrue( err, ok);

        ReferenceLookupAttribute att = (ReferenceLookupAttribute)
            rosterSlotNode.getCell().getAttributeByName("whichShift");
        // effectively the fk:
        WhichShift secondWhichShift = (WhichShift) att.getItemValue();
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
            "copy");
        triggers.copyAction.actionPerformed(evt);
        workerStrand.INSERT();
        triggers.pasteAction.actionPerformed(evt);
        sdzBag.getStrand().POST();

        WhichShift createdWhichShift = (WhichShift) att.getItemAdapter().getItemValue();
        if(createdWhichShift == null)
        {
            //Err.pr( SdzNote.secondTest, "Lets not make the assertion while we are fixing a different problem");
            fail("Copying and pasting did not work, or could not getItemValue()");
        }
        else if(secondWhichShift != createdWhichShift)
        {
            Err.pr("secondWhichShift: " + secondWhichShift);
            Err.pr("createdWhichShift: " + createdWhichShift);
            fail("Did not expect to actually create another WhichShift!");
        }
        workerStrand.PREVIOUS();
        whichShiftAttribute.setItemValue("dinner");
        workerStrand.NEXT();
        whichShiftAttribute.setItemValue("overnight");
        workerStrand.POST();
        assertTrue(whichShiftAttribute.getItemAdapter().getItemValue().equals( "overnight"));
        Session.getRecorder().closeRecording();
        // }
        // });
         /**/
    }

    /**
     * If delete a roster slot then can't go on and do the roster because
     * will be reading a deleted DO.
     */
    public void _testDeleteJDOProblem()
    {
        rosterSlotNode.GOTO();
        workerStrand.REMOVE();
        ParticularRosterI bo = ParticularRosterFactory.newParticularRoster( "testDeleteJDOProblem");
        bo.init( null, dataStore);
        bo.setAtMonth(RosteringConstants.CURRENT);
        bo.display(RosteringConstants.ROSTER);
    }
    
    public void testReNavigate()
    {
        workerStrand.ENTER_QUERY();
        christianNameAttribute.setItemValue("Noreen");
        workerStrand.EXECUTE_SEARCH();
        int row = workerNode.getRow();
        boolean ok = workerStrand.COMMIT_RELOAD();
        if(!ok)
        {
            Err.error( "Unexpected problem committing");
        }
        else
        {
            int postSaveRow = workerNode.getRow();
            assertTrue( "Are at " + postSaveRow + " but supposed to be at " + row, postSaveRow == row);
        }
    }    

    /**
     * Found that the RS was not being attached in the memory
     * version yet was working fine with JDO.
     */
    public void testMemoryVersion()
    {
        //No guaranteed order (esp JDO)
//    for(int i = 0; i < 13; i++)
//    {
//      volStrand.NEXT();
//    }
        //Two "N people"
//    rv.volunteerTriggers.alphabetListener.perform( "N");
        //This way is fullproof:
        workerStrand.ENTER_QUERY();
        christianNameAttribute.setItemValue("Noreen");
        Err.pr( "Have set to Noreen and about to EXECUTE_SEARCH");
        workerStrand.EXECUTE_SEARCH();
        Err.pr( "Done EXECUTE_SEARCH");
        //Extra assertions put in for more low level problems:
        assertTrue(christianNameAttribute.getItemAdapter() != null);
        Err.pr( "christianNameAttribute ItemAdapter has value <" + christianNameAttribute.getItemAdapter().getItemValue() + ">");
        Err.pr( "christianNameAttribute ItemAdapter has default value <" + christianNameAttribute.getItemAdapter().getDefaultValue() + ">");
        assertTrue(christianNameAttribute.getItemValue() != null);
        //
        //Err.pr( "S/be Noreen: " + christianNameAttribute.getItemValue());
        assertTrue( "When searching for Noreen instead got to " + christianNameAttribute.getItemValue(), 
                   christianNameAttribute.getItemValue().equals("Noreen"));
        rosterSlotNode.GOTO();
        //Taking off 1 b/c the automatic insert will have already been done 
        int origNumRosterSlots = rosterSlotNode.getRowCount()-1;
        Err.pr("S/be ZERO: " + origNumRosterSlots);
        //Automatic insert, so don't need this
        //workerStrand.INSERT();
        whichShiftAttribute.setItemValue("overnight");
        dayAttribute.setItemValue("Friday");
        intervalAttribute.setItemValue("weekly");
        //Err.pr( "To set F on " + rosterSlotmonthlyRestartAttribute.getItem());
        rosterSlotmonthlyRestartAttribute.setItemValue( Boolean.FALSE);
        Assert.isTrue( rosterSlotmonthlyRestartAttribute.getItemValue() instanceof Boolean);
        Assert.isTrue( rosterSlotmonthlyRestartAttribute.getItemValue().equals( Boolean.FALSE), 
                       "getItemValue() is " + rosterSlotmonthlyRestartAttribute.getItemValue());
        //We have changed Noreen to be a per month volunteer which conflicts with using 
        //continous here - so we need to undo this value
        weekInMonthAttribute.setItemValue( null);
        //Err.pr( "State in before commit: " + workerStrand.getMode());
        boolean ok = workerStrand.COMMIT_RELOAD();
        if(!ok)
        {
            Err.error( "Unexpected problem committing: " + workerStrand.getErrorMessage());
        }
        else
        {
            //A PostOperationTrigger now automatically takes back to
            //rosterSlotNode (previously this test used to do that
            //itself manually).  
        }
        Err.pr("S/be ONE: " + rosterSlotNode.getRowCount());
        assertTrue( rosterSlotNode.getRowCount() + 
                " s/be one more than orig num of RSs which is " + origNumRosterSlots, 
                    rosterSlotNode.getRowCount() == (origNumRosterSlots + 1));
        //Test that are now being taken back automatically via trigger: 
        assertTrue( workerStrand.getCurrentNode() == rosterSlotNode);
        workerStrand.REMOVE();
        workerStrand.COMMIT_RELOAD();
    }

    //What were we testing here??
    public void _testRosterMario()
    {
        if(workerStrand.getCurrentNode() != workerNode)
        {
            Err.error(
                "Expected to start testRosterMario() with volunteerNode current");
        }
        Err.pr(workerNode.getRowCount());
        assertTrue(workerNode.getRowCount() == 7);
        if(true)
        {
            workerStrand.NEXT();
            workerStrand.NEXT();
            workerStrand.NEXT(); // 4th Adapter.setB4Image() to "dinner"
            workerStrand.NEXT(); // no RosterSlot children
            workerStrand.NEXT(); // 5th Adapter.setB4Image() to null
            workerStrand.NEXT(); // no RosterSlot children
        } // 6th doesn't happen during NEXT()
        else
        {
            workerStrand.SET_ROW(6);
        }
        rosterSlotNode.GOTO();
        workerStrand.INSERT();
         /**/
        whichShiftAttribute.setItemValue("dinner");
        dayAttribute.setItemValue("Sunday");
        intervalAttribute.setItemValue("weekly");
        firstShiftAttribute.setItemValue("30/07/2003");
        // Err.pr( "$$$ BIValue===========" + whichShiftAttribute.getBIValue());
        // Err.pr( "$$$ ItemValue===========" + whichShiftAttribute.getItemValue());

        /*
        volStrand.INSERT();
        whichShiftAttribute.setItemValue( "overnight");
        dayAttribute.setItemValue( "Sunday");
        whichVolAttribute.setItemValue( "1");
        intervalAttribute.setItemValue( "weekly");
        firstShiftAttribute.setItemValue( "30/07/2003");
        */
        workerStrand.POST();
    }
    
    /* Copy from TestActualRoster - used to verify whether DB is the cause of the problem 
    public void testDeleteRosterSlot()
    {
        visualStrand.volunteerTriggers.alphabetListener.findWorkerStartsWith("A");
        TestActualRoster.tabToRosterSlots( visualStrand);
        int rowCount = visualStrand.getDt().rosterSlotsListDetailNode.getRowCount();
        assertTrue( "Expected to see more than one roster slot for the first worker beginning with A", rowCount > 0);
        visualStrand.getDt().strand.REMOVE();
        assertTrue( visualStrand.getDt().rosterSlotsListDetailNode.getRowCount() == rowCount - 1);
        TestActualRoster.tabToVolunteers( visualStrand);
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
    */
}
