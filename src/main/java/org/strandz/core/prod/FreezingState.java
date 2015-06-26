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

import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MutableInteger;

import java.util.Iterator;

public class FreezingState extends BlockState
{
    private static int times = 0;

    static public BlockState newInstance(
        OperationsProcessor context/* , AbstBlock block*/)
    {
        /*
        times++;
        Err.pr( "Creating FREEZING " + times + " for " +
        context.getCurrentBlock());
        if(times == 5)
        {
        Err.stack( "16/11/03 - make sure block.setDisplayEditable( false)");
        }
        */
        return new FreezingState(context);
    }

    private FreezingState(OperationsProcessor context)
    {/*
     //Block currentBlock = context.getCurrentBlock();
     if(block != null)
     {
     //Err.pr( "FreezingState init, Will setDisplayEditable( false) for block: " + currentBlock);
     block.setDisplayEditable( false);
     //StateEnum fromState =
     //context.generateStateChangeEvent( StateEnum.FROZEN, StateEnum.UNKNOWN);
     }
     else
     {
     Print.pr( "Don't have current block, so can't un-display");
     }
     */}

    public StateEnum getState()
    {
        return StateEnum.FROZEN;
    }

    public void anyKeyPressed(OperationsProcessor context,
                              OperationEnum key,
                              Block block,
                              boolean fieldsChanged)
    {
        super.anyKeyPressed(context, key, block, fieldsChanged);
        if(key == OperationEnum.POST)
        {
            /* As POST does all the blocks then detail blocks will often be frozen, so this is
               not an error */
            //Session.getErrorThrower().throwApplicationError(
            Err.pr(
                "Cannot POST when " + getState() + " on <" + block.getNode().getName() + ">"
                //,ApplicationErrorEnum.INTERNAL
            );
            /**/
        }
    }

    /*
    public void browse( OperationsProcessornterface context)
    {
    context.changeStateToNoMove( BrowsingState.getNewInstance( context), context.getCurrentBlock());
    }
    */

    // If you can add in browsing state, you mose well add directly from here.
    // Putting into Navigating first, so if browsing ever catches this switch
    // and puts out a message, we will get it too.
    public void add(OperationsProcessor context)
    {
        BlockState newState = null;
        if(context.getCurrentBlock().isAllowed(CapabilityEnum.UPDATE))
        {
            // newState = EditingState.getNewInstance( context);
            newState = BlockStateCreator.newInstance(context, StateEnum.EDIT);
        }
        else
        {
            // newState = BrowsingState.getNewInstance( context);
            newState = BlockStateCreator.newInstance(context, StateEnum.VIEW);
        }
        context.changeState( // vigatingState.getNewInstance( context),
            newState, context.getCurrentBlock());
        /*
        * Doing the add next line will also achieve this, so no
        * point doing it here 08/06/04
        *
        context.changeStateToNoMove(
        //AddingState.getNewInstance( context),
        BlockStateCreator.newInstance( context, StateEnum.NEW),
        context.getCurrentBlock());
        */
        context.getCurrentState(context.getCurrentBlock()).add(context);
    }

    /*
    * TestTable.testMultipleInsert() is what will fail if
    * comment this method out. Shouldn't really need this
    * method
    */
    public void freeze(OperationsProcessor context)
    {// stay in this state
    }

     /**/

    public void enterQuery(OperationsProcessor context)
    {
        context.changeState( // EnterQueryState.getNewInstance( context),
            BlockStateCreator.newInstance(context, StateEnum.ENTER_QUERY),
            context.getCurrentBlock());
        context.getCurrentBlock().setDisplayEditable(true);
        context.getCurrentBlock().blankoutDisplay(OperationEnum.ENTER_QUERY, 
                                                  "pressed ENTER_QUERY in frozen block", 
                                                  Constants.REMOVE_COMBO_ITEMS);
        /*
        List list = context.getCurrentBlock().getAdaptersArrayList();
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
        Adapter itemAdapter = (Adapter)iter.next();
        itemAdapter.setB4ImageValue( null);
        itemAdapter.setItemValue( null);
        }
        */
    }

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

    /*
    public void executeQuery( OperationsProcessor context)
    {
    context.changeStateToNoMove(
    //ExecuteQueryState.getNewInstance( context),
    BlockStateCreator.newInstance( context, StateEnum.EXECUTE_QUERY),
    context.getCurrentBlock());
    }
    */

    public void enterBlock(OperationsProcessor context, Block fromBlock)
    {
        boolean parentFound = false;
        // Block parent = context.getCurrentBlock().getParent();
        Block firstParent = null; // debug
        int i = 0;
        for(Iterator iter = context.getCurrentBlock().getParentsIterator( context.getCurrentBlock().tm); iter.hasNext(); i++)
        {
            Block parent = (Block) iter.next();
            if(i == 0)
            {
                firstParent = parent;
            }
            /*
            * No reason why shouldn't be able to do this!
            if(context.getCurrentBlock().dataRecordsNull())
            {
            Err.error( "Trying to enter freezing block and context.dataRecords is null");
            }
            else
            */
            if(parent == fromBlock)
            {
                parentFound = true;
                if(!fromBlock.getState().getState().isNew())
                {
                    Block current = context.getCurrentBlock();
                    if(!current.dataRecordsEmpty())
                    {
                        // Err.pr( "***********Data records not empty for " + current + " in " +
                        // current.getState().getState());
                        BlockState newState = null;
                        if(context.getCurrentBlock().isAllowed(CapabilityEnum.UPDATE))
                        {
                            newState = BlockStateCreator.newInstance(context, StateEnum.EDIT);
                        }
                        else
                        {
                            // Err.pr( "Update not allowed in " + context.getCurrentBlock());
                            newState = BlockStateCreator.newInstance(context, StateEnum.VIEW);
                        }
                        context.changeState(newState, current);
                    }
                    else
                    {// Err.pr( "***********Data records ARE empty for " + current + " in " +
                        // current.getState().getState());
                    }
                }
                else
                {
                    Err.error("Can't imagine when parent would be left in INSERT state");
                }
            }
        }
        /*
        if(fromBlock != null)
        {
        if(!parentFound)
        {
        Err.pr( "ooooooooo " + context.getCurrentBlock() +
        " did not have a parent that was the same as fromBlock: " +
        fromBlock);
        Err.pr( "First parent was " + firstParent);
        }
        else
        {
        Err.pr( "oo-oo-ooo-oo " + context.getCurrentBlock() +
        " DID have a parent that was the same as fromBlock: " +
        fromBlock);
        }
        }
        */
    }

    public void leaveBlock(OperationsProcessor context, Block toBlock)
    {
        Block currentBlock = context.getCurrentBlock();
        if(currentBlock != null)
        {
            if(toBlock.isDescendantOf( currentBlock, new MutableInteger(0))) // down
            {
                Session.getErrorThrower().throwApplicationError(
                    "Cannot move to a child from a frozen parent",
                    ApplicationErrorEnum.INTERNAL);
                // ae.setLastFieldHolder( (LastFieldHolder)context.getCurrentBlock());
                /**
                 * When get focusChange working (Swing environment) then do
                 * it here
                 */
            }
        }
    }

    public void capabilitiesSet(OperationsProcessor context)
    {// ignored
    }

    /*
    * If NEXT being called in a loop don't want to have
    * it going on forever, thus make sure it returns false
    */
    public void next(OperationsProcessor context)
    {
        // ignored
        Err.pr("NEXT in freezing");
        Session.getMoveManager().getValidationContext().setOk(false);
    }

     /**/

    public void previous(OperationsProcessor context)
    {
        // ignored
        // Err.pr( "previous in Freezing");
        Session.getMoveManager().getValidationContext().setOk(false);
    }

    public void setRow(OperationsProcessor context, int row)
    {
        // ignored
        // Err.pr( "next in Freezing");
        Session.getMoveManager().getValidationContext().setOk(false);
    }

    public void ifInsertedDeleteActions(OperationsProcessor context)
    {/*
     * Is required when go to a new block that is frozen
     Err.pr( "???????????   Freezing, disabling delete, WHY HAVE");
     *//*
     Enabler enabler = (Enabler)context.getEnabler();
     if(enabler != null)
     {
     enabler.disable( OperationEnum.REMOVE);
     }
     */}
}
