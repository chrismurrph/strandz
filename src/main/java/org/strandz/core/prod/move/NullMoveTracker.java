package org.strandz.core.prod.move;

import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.ValidationContext;
import org.strandz.core.domain.NodeChangeEvent;
import org.strandz.core.domain.MoveBlockI;
import org.strandz.core.domain.MoveTrackerI;
import org.strandz.core.domain.event.AccessEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.IdentifierI;

import java.util.List;

/**
 * User: Chris
 * Date: 18/12/2008
 * Time: 12:01:37 AM
 */
public class NullMoveTracker implements MoveManagerI, MoveTrackerI
{
    ValidationContext validationContext = new ValidationContext();

    private static boolean NO_NULL_MOVE_BLOCK = false;
    private static boolean DEBUG = false;

    public void updateValidationContext(List list)
    {
        //Is called when commit and an error message is evoked 
    }

    public void updateValidationContext(Object obj)
    {
        //Err.pr("Updating ValidationContext to FALSE");
        if(validationContext == null)
        {
            Err.error("How can validationContext == null");
        }
        validationContext.setMessage(obj);
        validationContext.setOk(false);
    }

    public ValidationContext getValidationContext()
    {
        //Is called when click on a row
        return validationContext;
    }

    public boolean readyNextStep()
    {
        return true;
    }

    public ItemAdapter getConfirmedErrorAdapter()
    {
        return null;
    }

    public void forgetWhereCameFrom()
    {
        Err.error("Not implemented");
    }

    public void enter(ItemAdapter itemAdapter, EntrySiteEnum where)
    {
        pr( "Entering at " + itemAdapter + ", at " + where);
    }

    public void enterWithoutValidation(ItemAdapter itemAdapter, EntrySiteEnum where)
    {
        Err.error("Not implemented");
    }

    public void exitEnter()
    {
        //Is called
    }

    public void errorSite(ErrorSiteEnum whereError, OperationEnum enumId)
    {
        //Is called
    }

    public void setSomeRowsQueried(boolean b)
    {
        //Is called
    }

    public ItemAdapter getEnteredAdapter()
    {
        Err.error("Not implemented");
        return null;
    }

    public void createNewValidationContext()
    {
        validationContext = new ValidationContext();
    }

    public void closeTo(EntrySiteEnum where)
    {
        //Is called when click on a row
    }

    public boolean initialPoint(List blocks)
    {
        //Is called
        return false;
    }

    public void setBlock(BlockForMoveBlockI block)
    {
        if(!NO_NULL_MOVE_BLOCK)
        {
            MoveBlockI mb = new NullMoveBlock( this);
            block.setMoveBlock( mb);
        }
        else
        {
            Err.pr( "NO_NULL_MOVE_BLOCK, should not see this in PROD!");
        }
    }

    public void openAgainTo(EntrySiteEnum where)
    {
        //Is called when click on a row
    }

    public boolean error(ErrorSiteEnum whereError)
    {
        return false;
    }

    public void b4HandleErrorProcessing(ApplicationError err, MoveBlockI mBlock, OperationEnum reason)
    {
        //Is called
    }

    public void postValidationProcessing(boolean success, ApplicationError err)
    {
        //Is called
    }

    public void accessChange(AccessEvent event)
    {
        Err.error("Not implemented");
    }

    public void nodeChangePerformed(NodeChangeEvent e)
    {
        Err.error("Not implemented");
    }

    public void enter(ItemAdapter itemAdapter, EntrySiteEnum where, OperationEnum key)
    {
        pr( "Entering at " + itemAdapter + ", at " + where);
    }

    public void enterWithoutValidation(ItemAdapter itemAdapter, EntrySiteEnum where, OperationEnum key)
    {
        //Is called
    }

    public ItemAdapter getPreviousAdapterReleased()
    {
        Err.error("Not implemented");
        return null;
    }

    public void setRowSetting(int row)
    {
        //Is called when click on a row
    }

    public void notifySuccessfulInsert()
    {
        //Is called when controller executes an insert
    }

    public IdentifierI getNode()
    {
        return null;
    }

    private static void pr( String txt)
    {
        if(DEBUG)
        {
            Err.pr( txt);
        }
    }
}
