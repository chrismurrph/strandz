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
package org.strandz.test.fishbowl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.task.data.fishbowl.Flush;
import org.strandz.task.data.fishbowl.Refill;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TestTable extends TestCase
{
    /*
    private ComponentTableView table1;
    private ComponentTableView table2;
    private RuntimeAttribute clientName;
    private RuntimeAttribute endingInstructions;
    private List clientAbilities = new ArrayList();
    private List jobAbilities = new ArrayList();
    private Object commentVariable1 = SdzNote.TIMES_TOO_CLOSE;
    private Object commentVariable2 = SdzNote.MASTER_DETAIL_COPY_PASTE;
    private Object commentVariable3 = SdzNote.R_CLICK_RESTORE;
    */
    private TableHelper helper = new TableHelper();
    /*
    * S/always put back to these values.
    *
    * See CMLBug.masterDetailCopyPaste &
    * CMLBug.rClickRestore - until fixed
    * booleans must stay.
    * See CMLBug.timesTooClose for pUtils.visibleMode
    * always being false.
    *
    public static boolean helper.visible = pUtils.visibleMode;
    public static boolean masterNonTable = true;
    public static boolean childNonTable = false;
    */
    public static boolean masterNonTable = true;
    public static boolean childNonTable = true;
    private static int times;
    /*
    * Having a different event queue, even thou it behaves in exactly the same way,
    * (just calls supers), means that messages that key has been pressed (for table)
    * do not keep happening. Must be a timing/event.consume() thing, that will look
    * up on google sometime!
    * Errrrr - Problem that still exists is pressing ENTER rather than clicking
    * on the dialog, the dialog will always re-appear. Reason must be that ENTER
    * has a function on a table, which is being relayed onto the table
    */
    // private static final GlobalEventQueue globalEventQueue = new GlobalEventQueue();

    public static void main(String s[])
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    createAndShowGUI();
                }
                catch(Exception e)
                {
                    Err.error(e);
                }
            }
        });
    }

    public TestTable()
    {
        helper.init();
    }

    private static void createAndShowGUI() throws Exception
    {
        TestTable obj = new TestTable();
        obj.setUp();
        obj.testQueryTimes();
        //obj.testInsertTableSelectedRowOk();
        //obj.tearDown();
    }

    protected void setUp()
    {
        Err.pr("S/not see Fred: " + helper.clientName.getItemValue());
        if(helper.clientName.getItemValue().equals("Fred"))
        {
            Err.error("client Name not supposed to be Fred");
        }
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        //even thou doing GOTO w/out validation, are still getting
        //failures! TODO - make so don't need to temp allow blank record here
        //Err - above had no effect. All tests are passed when TestTable is done alone.
        //THUS - make this test suite non-applichousing before introduce it again.
        helper.clientNode.setBlankRecord(true);
        recordValidationOff();
        //here
        boolean ok = helper.example.getStrand().getValidationContext().isOk();
        Err.pr(SdzNote.SECOND_TEST, "ValidationContext in tearDown is " + ok);
        if(!ok)
        {
            helper.example.getStrand().getValidationContext().setOk(true);
            ok = helper.example.getStrand().getValidationContext().isOk();
            Err.pr(SdzNote.SECOND_TEST, "ValidationContext fixed: " + ok);
            if(!ok)
            {
                Err.error("Did not manage to right ValidationContext");
            }
        }
        //here
        helper.example.makeNotFredEverywhere();
        helper.example.removeNullItemsCreated();
        helper.example.removeSubsequentlyCreated();
        helper.example.reconstituteToilet();
        helper.example.getStrand().COMMIT_ONLY();
        //Was needed when doing structural things dynamically. Now the
        //constructor doing the structural things (creating Nodes etc,
        // we don't need to do this here)
        //Session.getMoveManager().reset();
        //ABOVE WAS JUST GUESSWORK - the constructor happens b4 every test just like setup() does
        helper.clientNode.setBlankRecord(false);
        //
    }

    private class DebugListSelectionListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent arg0)
        {
            times++;
            Err.pr(
                "Selected row now " + helper.example.getTableView1().getSelectedRow()
                    + " times " + times);
            if(times == 2)
            {
                Err.stackOff();
            }
        }
    }

    public static Test suite()
    {
        return new TestSuite(TestTable.class);
    }

    /*
    public void testDetailNotRefreshing()
    {
    helper.strand.INSERT();
    clientName.setItemValue( "See if refreshing detail 2nd client");
    helper.jobNode.GOTO();
    helper.strand.INSERT();
    helper.jobDescription.setItemValue( "See if refreshing detail 2nd job");
    helper.clientNode.GOTO();
    helper.strand.PREVIOUS();
    helper.strand.NEXT();
    helper.strand.PREVIOUS();
    }
    */

    public void testQueryTimes()
    {
        Err.pr("executeQuery Times: " + helper.example.executeQueryHappenedTimes);
        assertTrue(helper.example.executeQueryHappenedTimes == 1);
        Err.pr("postQueryParent Times: " + helper.example.postQueryParentHappenedTimes);
        assertTrue(helper.example.postQueryParentHappenedTimes == 1);
        Err.pr("postQueryChild Times: " + helper.example.postQueryChildHappenedTimes);
        assertTrue(helper.example.postQueryChildHappenedTimes <= 2);
    }

    public void testInsertBadPaint()
    {
        if(helper.visible)
        {
            recordValidationOn();
            helper.clientName.setItemValue("Fred");
            helper.strand.INSERT();
        }
    }

    public void testSetBlankAllowedFalse()
    {
        helper.clientNode.setBlankRecord(false);
        helper.strand.INSERT();

        boolean ok = helper.strand.POST();
        assertFalse(ok);
        helper.clientNode.setBlankRecord(true);
    }

    /*
    * Not much of a test as this is what happens with both tables and fields
    *
    public void testGettingValues()
    {
    helper.clientNode.setCommitOnly( true);
    helper.strand.COMMIT_ONLY();
    Attribute attribs[] = helper.clientNode.getAttributes();
    for(int i = 0; i < attribs.length; i++)
    {
    RuntimeAttribute attrib = (RuntimeAttribute)attribs[i];
    Object value = attrib.getItemValue();
    Err.pr( "Value of attrib " + attrib.getName() + " is <" + value + ">");
    if(value == null)
    {
    fail();
    }
    }
    }
    */

    public void testInsertPriorEnd()
    {
         /**/
        helper.clientNode.setBlankRecord(true);
        helper.strand.SET_ROW(helper.clientNode.getRowCount() - 1);
        helper.strand.INSERT_AT_PLACE();

        int cmlRow = helper.clientNode.getRow();
        Err.pr("On row: " + cmlRow);
        assertTrue(cmlRow == (helper.clientNode.getRowCount() - 2));
        helper.clientNode.setBlankRecord(false);
        //
        //Doing this b/c REMOVE has stopped working
        /*
        helper.strand.SET_ROW( 0);
        helper.strand.REMOVE();
        Err.pr( "row count: " + helper.clientNode.getRowCount()); //s/be 1, and contain data
        */
         /**/
    }

    public void testInsertPriorBegin()
    {
         /**/
        helper.clientNode.setBlankRecord(true);
        helper.strand.INSERT_AT_PLACE();

        int cmlRow = helper.clientNode.getRow();
        Err.pr("On row: " + cmlRow);
        assertTrue(cmlRow == 0);
        helper.clientNode.setBlankRecord(false);
         /**/
    }

    public void testCommitReload()
    {
        boolean ok = helper.strand.COMMIT_RELOAD();
        assertTrue(ok);
    }

    public void testSetBlankAllowedTrue()
    {
        helper.clientNode.setBlankRecord(true);
        helper.clientNode.setCommitOnly(true);
        helper.strand.INSERT();

        boolean ok = helper.strand.COMMIT_RELOAD();
        assertTrue(ok);
        helper.clientNode.setBlankRecord(false);
    }

    public void testInsertTableSelectedRowOk()
    {
        if(helper.table1 != null)
        {
            helper.strand.INSERT();
            helper.strand.COMMIT_RELOAD();

            int cmlRow = helper.clientNode.getRow();
            Err.pr("On row: " + cmlRow);

            int tableRow = helper.table1.getSelectedRow();
            Err.pr("On table row: " + tableRow);
            assertTrue(cmlRow == tableRow);
        }
    }

    //Don't like the effect flush will have on the other tests. Doing this on its own
    //now works fine (27/11/04)
    public void _testSimpleRemove()
    {
        Flush.main(new String[]{"FishBowlData"});
        helper.strand.EXECUTE_QUERY();
        helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        helper.clientName.setItemValue("Dominic");
        helper.strand.COMMIT_RELOAD();
        Err.pr(helper.clientNode.getRowCount());
        assertTrue(helper.clientNode.getRowCount() == 1);
        helper.strand.REMOVE();

        int count1 = helper.clientNode.getRowCount();
        helper.strand.COMMIT_RELOAD();

        int count2 = helper.clientNode.getRowCount();
        assertTrue(count1 == count2);
    }

    /**
     * Was a cause of failure for when using JDO only
     * "Operation attempted on a deleted instance".
     * More code added to get failure when using XML and
     * delete from a wrapper list rather than the real list.
     */
    //Don't like the effect flush will have on the other tests. Doing this on its own
    //now works fine (27/11/04)
    public void _testDeleteAfterQuery1()
    {
        Flush.main(new String[]{"FishBowlData"});
        Refill.main(new String[]{"FishBowlData"});
        helper.strand.EXECUTE_QUERY();
         /**/
        helper.strand.REMOVE();

        int count1 = helper.clientNode.getRowCount();
        helper.strand.EXECUTE_QUERY();

        int count2 = helper.clientNode.getRowCount();
        Err.pr(count1);
        Err.pr(count2);
        assertTrue(count1 == (count2 - 1));
        helper.strand.EXECUTE_QUERY();

        int count3 = helper.clientNode.getRowCount();
        assertTrue(count3 == count2);
        helper.strand.REMOVE();
        helper.strand.COMMIT_RELOAD();

        int count4 = helper.clientNode.getRowCount();
        Err.pr(count2);
        Err.pr(count4);
        // As committed this time, count4 should now be one less
        // than the full amount. If not true, then probably means
        // that delete did not work.
        assertTrue(count1 == count4);
         /**/
    }

    /**
     * To get failure when using XML and
     * delete from a wrapper list rather than the real list.
     */
    //Don't like the effect flush will have on the other tests. Doing this on its own
    //now works fine (27/11/04)
    public void _testDeleteAfterQuery2()
    {
        Flush.main(new String[]{"FishBowlData"});
        Refill.main(new String[]{"FishBowlData"});
        helper.strand.EXECUTE_QUERY();

        int count1 = helper.clientNode.getRowCount();
        helper.strand.REMOVE();
        helper.strand.COMMIT_RELOAD();

        int count2 = helper.clientNode.getRowCount();
        Err.pr(count1);
        Err.pr(count2);
        // As committed this time, count4 should now be one less
        // than the full amount. If not true, then probably means
        // that delete did not work.
        assertTrue((count1 - 1) == count2);
         /**/
    }

    public void testInsertNoChildren()
    {
        helper.strand.INSERT();
        helper.jobNode.GOTOWithoutValidation();
         /**/
        Err.pr("State--------->" + helper.jobNode.getState() + " for " + helper.jobNode);
        Err.pr("RowCount--------->" + helper.jobNode.getRowCount());
        Err.pr("Gone to node: " + helper.jobNode);
         /**/
        assertTrue(helper.jobNode.getRowCount() == 0);
         /**/
        helper.strand.INSERT();
        helper.jobDescription.setItemValue("Dusting 1");
        helper.strand.INSERT();
        helper.jobDescription.setItemValue("Dusting 2");
        helper.strand.INSERT();
        helper.jobDescription.setItemValue("Dusting 3");
         /**/
        assertTrue(helper.jobNode.getRowCount() == 3);
    }

    private void recordValidationOn()
    {
        helper.example.node1.setRecordValidationTrigger(
            new MLTableExample.MasterRecordValidationT(helper.example.cell1,
                helper.example.nameAttribute));
        helper.example.node1.setPostingTrigger(new MLTableExample.MasterRecordChangeT());
        helper.example.nameAttribute.setItemValidationTrigger(null);
    }

    private void recordValidationOff()
    {
        // Put back to how was originally:
        helper.example.node1.setRecordValidationTrigger(null);
        helper.example.node1.setPostingTrigger(null);
        helper.example.nameAttribute.setItemValidationTrigger(
            new MLTableExample.MasterItemValidationT(helper.example.cell1,
                helper.example.nameAttribute, helper.example.phoneAttribute, helper.example.phoneNumbers));
    }

    /**
     * Test to see if can copy a master and a detail together. If the
     * detail count is the same as it was before, even thou are now on
     * the next (inserted to copy into) row, then it is assumed that
     * this has happened.
     */
    public void testCopyPasteMasterDetail()
    {
        int detailRows = helper.copyPasteMasterDetail();
        //TODO A better assertTrue would call the yet to be written
        //method node.equalsByItems( node)
        assertTrue(detailRows == helper.jobNode.getRowCount());
    }

    /**
     * Test that performs this function no longer exists!
     */
    public void testPasteRecordValidation()
    {
    }

    /**
     * Test passing with error message:
     * "Cannot load until the current node is a top level one"
     */
    public void testQueryOnChild()
    {
        helper.jobNode.GOTO();
        helper.strand.EXECUTE_QUERY();

        int size = helper.strand.getErrorHistory().size();
        if(size == 0)
        {
            fail("Should not be able to execute query on a child, got " + size + " errors");
        }
        else if(size != 1)
        {
            fail("Expected only one error message, got " + size + " errors");
        }
    }

    /**
     * Can make a real test when we work out how to fire keys
     * and mouse events at a component.
     */
    /*
    public void testKeysAndMouseNotListenedTo()
    {
        DefaultListSelectionModel lsm = new DefaultListSelectionModel();
        if(table1 != null)
        {
            table1.setSelectionModel(lsm);
            lsm.addListSelectionListener(new LocalListSelectionListener());
        }
    }
    */

    private static class LocalListSelectionListener
        implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
            Print.pr("Got " + e);
        }
    }

    public void testInsertCursorPosn()
    {
        helper.strand.INSERT();
        helper.strand.POST();
        Print.pr(helper.clientNode.getRow());
    }

    public void postFailContinually()
    {
        if(!helper.visible)
        {
            helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);

            int timesFailed = 0;
            for(int i = 0; i <= 5 - 1; i++)
            {
                if(!helper.strand.POST())
                {
                    timesFailed++;
                    Print.pr("Expect to fail post");
                }
            }
            assertTrue(timesFailed == 5);
        }
    }

    public void tabToOtherFewTimes()
    {
        if(helper.visible) // too much effort to test invisibly
        {
            helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);

            JTabbedPane tabbedpane = helper.example.getTabbedpane();
            tabbedpane.setSelectedIndex(tabbedpane.indexOfTab(MLTableExample.OTHER));
            tabbedpane.setSelectedIndex(tabbedpane.indexOfTab(MLTableExample.OTHER));
            assertTrue(helper.strand.getErrorHistory().size() == 2);
        }
    }

    /**
     * Internal Error s/be that record is blank
     */
    public void testInternalValidationRepeatable()
    {
        if(!helper.visible)
        {
            helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
            for(int i = 0; i <= 3 - 1; i++) // 3 messages come out
            {
                helper.jobNode.GOTO();
            }
            Print.pr(helper.strand.getErrorHistory().size());
            assertTrue(helper.strand.getErrorHistory().size() == 3);
        }
    }

    public void _testExternalValidationRepeatable()
    {
        if(!helper.visible)
        {
            helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
            Print.pr(
                "clientName was " + helper.clientName.getItemValue() + " at "
                    + helper.strand.getCurrentNode().getRow());
            helper.clientName.setItemValue("Fred");
            for(int i = 0; i <= 3 - 1; i++) // 3 messages come out
            {
                helper.strand.POST(); //changed from GOTO
            }
            Print.pr(helper.strand.getErrorHistory().size());
            assertTrue(helper.strand.getErrorHistory().size() == 3);
        }
    }

    public void testMultipleChildInsert()
    {
        helper.example.noData = true;
        Print.pr("Parent's state: " + helper.clientNode.getState());
        helper.strand.EXECUTE_QUERY();
        helper.example.noData = false;
         /**/
        Print.pr("Parent's state: " + helper.clientNode.getState());
        helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        helper.clientName.setItemValue("Dominic");
        helper.jobNode.GOTOWithoutValidation();
        Print.pr("Parent's state: " + helper.clientNode.getState());
        Print.pr("Child's state: " + helper.jobNode.getState());
        assertTrue(helper.strand.getCurrentNode() == helper.jobNode);
         /**/
         /**/
        helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        helper.jobDescription.setItemValue("Clean");
        helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        helper.jobDescription.setItemValue("Paint");
        helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        helper.jobDescription.setItemValue("Spray");
        Print.pr(helper.strand.getCurrentNode().getState());
        Print.pr(helper.jobNode.getRowCount());
        assertTrue(helper.jobNode.getRowCount() == 3);
         /**/
    }

    public void testMultipleInsert()
    {
        helper.example.noData = true;
        helper.strand.EXECUTE_QUERY();
        helper.example.noData = false;
        /*
        Err.pr( "Sector--------->" + helper.strand.getCurrentNode().getState());
        Err.pr( "RowCount--------->" + helper.clientNode.getRowCount());
        */
        helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        helper.clientName.setItemValue("Dominic");
        helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        helper.clientName.setItemValue("Erik");
        helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        helper.clientName.setItemValue("Robert");
        assertTrue(helper.clientNode.getRowCount() == 3);
         /**/
        helper.strand.SET_ROW(0);
        assertTrue(helper.clientName.getItemValue().equals("Dominic"));
        helper.strand.SET_ROW(1);
        assertTrue(helper.clientName.getItemValue().equals("Erik"));
        helper.strand.SET_ROW(2);
        assertTrue(helper.clientName.getItemValue().equals("Robert"));
         /**/
    }

    private void initialQuery()
    {
        helper.strand.EXECUTE_QUERY();
    }

    public void testInvalidExecuteQuery()
    {
        helper.clientName.setItemValue("Fred");
        if(!helper.strand.POST())
        {
            Print.pr("Expected to fail");
        }
        helper.strand.EXECUTE_QUERY();
        Print.pr("POST result: " + helper.strand.POST());
        assertTrue(helper.strand.POST());
    }

    public void setMasterInError()
    {
        helper.clientName.setItemValue("Fred");
    }

    //Seems to be failing, but lets pick this up another time TODO
    public void _testSetError()
    {
        helper.clientName.setItemValue("Fred");
        if(!helper.strand.POST())
        {
            Print.pr("Expected to fail");
        }
        else
        {
            fail("Setting to Fred supposed to fail but did not");
        }
    }

    /*
    * Reinstate when can dynamically make it an
    * ignoredChild
    public void testInvalidExecuteQueryChildIgnored()
    {
    if(helper.jobNode.isIgnoredChild() != true)
    {
    //This won't work, which calls into question all dynamic
    //setting of 'can do' properties.
    //helper.jobNode.setIgnoredChild( true);
    fail( "This test to be run for an ignored child - want to set dynamically");
    }
    else
    {
    helper.jobNode.goTo();
    helper.jobDescription.setItemValue( "Fred");
    if(!helper.strand.POST())
    {
    Err.pr( "Expected to fail");
    }
    else
    {
    fail( "POST s/not have suceeded");
    }
    helper.strand.EXECUTE_QUERY();
    //Err.pr( "POST result: " + helper.strand.POST());
    assertTrue( helper.strand.POST());
    if(helper.jobDescription.getItemValue().equals("Fred"))
    {
    fail( "We have re-queried, yet still have \"Fred\"!");
    }
    }
    }
    */

    public void _testExternalValidationRepeatablePost()
    {
        if(!helper.visible)
        {
            // helper.clientNode.goToWithoutValidation();
            Print.pr(
                "clientName was " + helper.clientName.getItemValue() + " at "
                    + helper.strand.getCurrentNode().getRow());
            helper.clientName.setItemValue("Fred");

            int timesPostFailed = 0;
            for(int i = 0; i <= 3 - 1; i++) // 3 failures
            {
                if(!helper.strand.POST())
                {
                    timesPostFailed++;
                }
            }
            Print.pr(
                "Times failed testExternalValidationRepeatablePost() is "
                    + timesPostFailed);
            assertTrue(timesPostFailed == 3);
        }
    }

    private void showJobCursor()
    {
        Print.pr("job cursor position: " + helper.jobNode.getRow());
        Print.pr("job row count: " + helper.jobNode.getRowCount());
    }

    /*
    private void enablePaste( JButton button, boolean enable)
    {
    //Err.pr( "Will set " + button.getText() + " to " + enable);
    button.setEnabled( enable);
    }
    */
}
