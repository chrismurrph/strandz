package org.strandz.core.prod.move;

import org.strandz.core.domain.MoveTrackerI;
import org.strandz.core.domain.MoveBlockI;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.lgpl.util.Err;

/**
 * User: Chris
 * Date: 19/12/2008
 * Time: 11:36:04 AM
 */
public class NullMoveBlock implements MoveBlockI
{
    private MoveTrackerI moveTracker;

    public NullMoveBlock( MoveTrackerI moveTracker)
    {
        this.moveTracker = moveTracker;
    }

    public MoveTrackerI getMoveTracker()
    {
        return moveTracker;
    }

    public void setItemValueAdapter(ItemAdapter ad)
    {
        Err.error("Not implemented");

    }

    public boolean isIgnoreValidation()
    {
        Err.error("Not implemented");
        return false;
    }

    public Object getTableControl()
    {
        Err.error("Not implemented");
        return null;
    }

    public void setRecordValidationOutcome(boolean b)
    {
        //Is called when click on a row
    }

    public boolean getRecordValidationOutcome()
    {
        Err.error("Not implemented");
        return false;
    }
}
