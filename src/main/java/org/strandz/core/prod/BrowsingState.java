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
package org.strandz.core.prod;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.lgpl.util.Err;

public class BrowsingState extends NavigatingState
{
    static public BlockState newInstance(OperationsProcessor context)
    {
        return new BrowsingState(context);
    }

    private BrowsingState(OperationsProcessor context)
    {
        super(context);
        // Err.pr( "Now in Browsing StateEnum constructor, where setUIEditable( false)");
        // time to stop context being editable
        context.getCurrentBlock().setDisplayEditable(false);
        // fail( context);
    }

    /**
     * Second param is only to give us a different signature
     * so that we don't do anything to editable state of the
     * context.
     */
    BrowsingState(OperationsProcessor context, int irrelevant)
    {
        super(context);
        // fail( context);
    }

    private void fail(OperationsProcessor context)
    {
        if(context.toString().equals("block applic.fishbowl.data.Job"))
        {
            Err.error("WHY BROWSE?");
        }
    }

    public StateEnum getState()
    {
        return StateEnum.VIEW;
    }

    /**
     * Concerned with not allowing user to change record within a
     * block if there is a problem.
     */
    public void anyKeyPressed(OperationsProcessor context,
                              OperationEnum key,
                              Block block,
                              boolean fieldsChanged)
    {
        super.anyKeyPressed(context, key, block, fieldsChanged);
        /*
        * This was bugged code!
        *
        * if(key == StateEnum.EDIT)
        */
        if(key == OperationEnum.UPDATE)
        {
            String msg = "Cannot enter Editing mode from Browsing - if change this rule "
                + "make sure only zeus is allowed to receive this message";
            Session.getErrorThrower().throwApplicationError(msg,
                ApplicationErrorEnum.INTERNAL);
        }
    }

    public void browse(OperationsProcessor context)
    {// Why into same again?
        // context.changeStateToNoMove( BrowsingState.getNewInstance( context), context.getCurrentBlock());
    }

    StateEnum getCantMoveState(OperationsProcessor context)
    {
        // Err.pr( "*#* block " + context + " being set to NOMOVESTATE");
        return StateEnum.NOMOVE_VIEW;
    }
}
