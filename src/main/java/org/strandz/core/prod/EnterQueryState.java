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

public class EnterQueryState extends BlockState
{
    private static int times = 0;
    static String DONT_LEAVE = "In Enter Query state, so must Execute, not try to leave";

    static public BlockState newInstance(OperationsProcessor context)
    {
        return new EnterQueryState(context);
    }

    EnterQueryState(OperationsProcessor context)
    {/*
     List list = context.getCurrentBlock().getAdaptersArrayList();
     for(Iterator iter = list.iterator(); iter.hasNext();)
     {
     Adapter itemAdapter = (Adapter)iter.next();
     itemAdapter.setB4ImageValue( null);
     }
     */// from outside now
        // tmp
        // context.getCurrentBlock().setDisplayEditable( true);
        // tmp
        // context.getCurrentBlock().blankoutDisplay();
    }

    public StateEnum getState()
    {
        return StateEnum.ENTER_QUERY;
    }

    public void anyKeyPressed(OperationsProcessor context,
                              OperationEnum key,
                              Block block,
                              boolean fieldsChanged)
    {// No point doing field and record validation
        // super.anyKeyPressed( context, key, block);
        // Err.debug();
    }

    public void enterBlock(OperationsProcessor context, Block fromBlock)
    {/*
     List list = context.getCurrentBlock().getAdaptersArrayList();
     for(Iterator iter = list.iterator(); iter.hasNext();)
     {
     Adapter itemAdapter = (Adapter)iter.next();
     itemAdapter.setB4ImageValue( null);
     Err.pr( "Have set b4image null for " + itemAdapter.id);
     //itemAdapter.setItemValue( null);
     }
     */}

    /*
    public void executeQuery( OperationsProcessor context)
    {
    context.changeStateToNoMove(
    //ExecuteQueryState.getNewInstance( context),
    BlockStateCreator.newInstance( context, StateEnum.EXECUTE_QUERY),
    context.getCurrentBlock());
    }
    */

    public void browse(OperationsProcessor context)
    {
        context.changeState( // NavigatingState.getNewInstance( context),
            BlockStateCreator.newInstance(context, StateEnum.VIEW),
            context.getCurrentBlock());
    }

    public void edit(OperationsProcessor context)
    {
        context.changeState( // NavigatingState.getNewInstance( context),
            BlockStateCreator.newInstance(context, StateEnum.EDIT),
            context.getCurrentBlock());
    }

    public void freeze(OperationsProcessor context)
    {
        // this constructor will make UI uneditable
        Block currentBlock = context.getCurrentBlock();
        context.changeState( // FreezingState.getNewInstance( context, currentBlock), currentBlock
            BlockStateCreator.newInstance(context, StateEnum.FROZEN), currentBlock);
    }

    public void leaveBlock(OperationsProcessor context, Block toBlock)
    {
        Block currentBlock = context.getCurrentBlock();
        if(currentBlock != null)
        {
            Session.getErrorThrower().throwApplicationError(
                DONT_LEAVE, ApplicationErrorEnum.INTERNAL);
        }
    }

    public void capabilitiesSet(OperationsProcessor context)
    {
    }
}
