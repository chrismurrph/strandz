package org.strandz.test.fishbowl;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.interf.Node;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Err;
import junit.framework.TestCase;

/**
 * User: Chris
 * Date: 17/10/2008
 * Time: 23:07:40
 */
public class TestTableDifficults extends TestCase
{
    private TableHelper helper = new TableHelper();

    public TestTableDifficults()
    {
        helper.init();
    }

    /**
     * Test to see if can copy a master and a detail together. If the
     * detail count is the same as it was before, even thou are now on
     * the next (inserted to copy into) row, then it is assumed that
     * this has happened.
     */
    public void testCopyPasteMasterDetailNulls()
    {
        helper.example.deconstituteToilet();
        int detailRows = helper.copyPasteMasterDetail();
        assertTrue(detailRows == helper.jobNode.getRowCount());
    }

    public void testCantMoveFromInvalidChild()
    {
        Err.pr("Current context is " + helper.strand.getValidationContext());
        if(helper.jobNode.isAllowed(CapabilityEnum.IGNORED_CHILD) != false)
        {
            Err.error(
                "This test to be run for an NON ignored child - want to set dynamically");
        }
        if(!helper.jobNode.GOTO())
        {
            Err.pr("Used " + helper.strand.getValidationContext());
            fail("Could not get to helper.jobNode b/c " + helper.strand.getErrorMessage());
        }
        helper.jobDescription.setItemValue("Fred");
        if(!helper.strand.POST())
        {
            Print.pr("Expected to fail");
        }
        else
        {
            fail("POST s/not have suceeded");
        }
        helper.clientNode.GOTO();

        Node currentNode = helper.strand.getCurrentNode();
        if(currentNode == helper.clientNode)
        {
            fail("Did not expect to get to helper.clientNode while it is inError");
        }
        /*
        * Here are in ridiculous position where cannot exeqry as it is not
        * allowed on the child (and makes no sense anyway), and cannot move
        * out of the child b/c it is in error. Conclusion is that exeqry
        * s/be able to be directed at a particular node, regardless of
        * which node are currently on. [thus goToWithoutValidation()]
        */
        Print.pr("current node is " + helper.strand.getCurrentNode());
        helper.clientNode.GOTOWithoutValidation();
        currentNode = helper.strand.getCurrentNode();
        if(currentNode == helper.jobNode)
        {
            fail("Expected to get back to helper.clientNode");
        }
    }

    public void testCanMoveFromInvalidChild()
    {
        if(helper.jobNode.isAllowed(CapabilityEnum.IGNORED_CHILD) != false)
        {
            Err.error(
                "This test to be run for an NON ignored child - want to set dynamically");
        }
        if(!helper.jobNode.GOTO())
        {
            // Supposed to fail here
            Err.error(
                "Not able to get to job node - weird error as haven't done anything!");
        }
        // Set to something else first so that the change will be recorded
        helper.jobDescription.setItemValue("William");
        // Nowdays this will fail immediately
        helper.jobDescription.setItemValue("Fred");
        if(!helper.strand.POST())
        {
            Print.pr("Expected to fail");
        }
        else
        {
            /*
            * Actually should succeed as can't have an error condition
            * hanging around forever - so expect this test to fail later!
            */
            fail("POST s/not have suceeded");
        }
        helper.clientNode.GOTOWithoutValidation();

        Node currentNode = helper.strand.getCurrentNode();
        if(currentNode == helper.jobNode)
        {
            fail("Did not expect NOT to get to helper.clientNode, EVEN while it is inError");
        }
        /*
        * Here are in ridiculous position where cannot exeqry as it is not
        * allowed on the child (and makes no sense anyway), and cannot move
        * out of the child b/c it is in error. Conclusion is that exeqry
        * s/be able to be directed at a particular node, regardless of
        * which node are currently on. [thus goToWithoutValidation()]
        */
        Print.pr("current node is " + helper.strand.getCurrentNode());
        helper.strand.EXECUTE_QUERY();
    }

    public void testCantSetIfNotOnThatNode()
    {
        boolean caught = false;
        try
        {
            helper.jobDescription.setItemValue("Fred");
        }
        catch(Error err)
        {
            // Err.pr( err.getMessage());
            caught = true;
        }
        if(!caught)
        {
            fail();
        }
    }

    public void testDetailToMaster()
    {
        if(!helper.visible)
        {
            helper.jobNode.GOTO();
            helper.strand.execute(OperationEnum.INSERT_AFTER_PLACE);
            helper.clientNode.GOTO();
            Print.pr(helper.strand.getErrorHistory().size());
            assertTrue(helper.strand.getErrorHistory().size() == 1);
        }
    }
}
