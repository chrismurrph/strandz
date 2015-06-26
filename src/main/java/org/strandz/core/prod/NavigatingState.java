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
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

abstract class NavigatingState extends BlockState
{
    private static int times;
    /*
    static public BlockState newInstance( OperationsProcessor context)
    {
    if(context.getCurrentBlock().isAllowed( CapabilityEnum.UPDATE))
    {
    return EditingState.getNewInstance( context);
    }
    else
    {
    return BrowsingState.getNewInstance( context);
    }
    }
    */

    NavigatingState(OperationsProcessor context)
    {
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
        //boolean fieldsHaveChanged = block.fieldsChanged();
        super.anyKeyPressed(context, key, block, fieldsChanged);
        if(key != OperationEnum.REMOVE && key != OperationEnum.EXECUTE_QUERY)
        {
            /*
            times++;
            Err.pr( "$$$ Using block " + block + ", id: " + block.id + ", to test fieldsChanged times " + times );
            if (times == 11)
            {
              Err.debug();
            }
            */
            if(fieldsChanged)
            {
                // Err.pr( "*** fields HAVE changed in " + block.id + ", times " + times);
                /*
                Err.pr( "block contains these attributes: " +
                Arrays.asList( block.getNode().getAttributes()));
                */
                /*
                * They can be changed whilst in Browsing state if they
                * were inserted and have not yet been added.
                */
                if(!block.isIgnoreValidation()
                    && block.anyComplaints(getCantMoveState(context)))
                {
                    Session.getErrorThrower().throwApplicationError(
                        block.retrieveAnyComplaintsMsg(), ApplicationErrorEnum.INTERNAL);
                }
                // Err.pr( "DONE blankComplaint on current and to do applyDifferences on " + block);
                Err.pr(org.strandz.lgpl.note.SdzDsgnrNote.CONTROLS_VIEW_NOT_GETTING_PANEL_NAME_CHANGE,
                    "Swapped next 2 calls");
                if(!block.isIgnoreValidation())
                {
                    block.applyDifferences();
                }
                block.firePostingEvent(block.getProdNodeI(), key, getState());
                 /**/
                if(key == OperationEnum.POST || key == OperationEnum.GOTO_NODE)
                {
                    // Err.pr( "recently added, syncDetail to be done on " + block +
                    // " when key is " + key);
                    /**
                     * Think - that doing this (for POST talking here) is a bit
                     * controversial, but done for the following reason:
                     * May have changed a pk value in master, meaning that a
                     * different detail ought to be showing. What need to do is
                     * sync so that this different detail will come up.
                     */
                    block.syncDetail(key, Constants.REMOVE_COMBO_ITEMS);
                }
                 /**/
            }
            else
            {/*
         Err.pr( "*** fields not changed for " + block.id + ", times " + times);
         if(times == 3)
         {
         Err.debug();
         }
         */}
        }
    }

    public void freeze(OperationsProcessor context)
    {
        // this constructor will make UI uneditable
        Block currentBlock = context.getCurrentBlock();
        context.changeState( // FreezingState.getNewInstance( context, currentBlock),
            BlockStateCreator.newInstance(context, StateEnum.FROZEN), currentBlock);
    }

    public void next(OperationsProcessor context)
    {
        if(!context.getCurrentBlock().incCursor(true))
        {
            Session.getErrorThrower().throwApplicationError(
                context.getCurrentBlock().getNavigationWarningMessage(),
                ApplicationErrorEnum.INTERNAL);
        }
        context.getCurrentBlock().syncDetail(OperationEnum.NEXT, Constants.REMOVE_COMBO_ITEMS);
    }

    public void previous(OperationsProcessor context)
    {
        if(!context.getCurrentBlock().decCursor(true))
        {
            Session.getErrorThrower().throwApplicationError(
                context.getCurrentBlock().getNavigationWarningMessage(),
                ApplicationErrorEnum.INTERNAL);
        }
        context.getCurrentBlock().syncDetail(OperationEnum.PREVIOUS, Constants.REMOVE_COMBO_ITEMS);
    }

    public void setRow(OperationsProcessor context, int row)
    {
        int cursor = context.getCurrentBlock().getIndex();
        // Err.pr( "*** row: " + row);
        // Err.pr( "*** cursor: " + cursor);
        if(row != cursor)
        {
            if(!context.getCurrentBlock().setIndex(true, row))
            {
                Session.getErrorThrower().throwApplicationError(
                    context.getCurrentBlock().getNavigationWarningMessage(),
                    ApplicationErrorEnum.INTERNAL);
            }
            context.getCurrentBlock().syncDetail(OperationEnum.SET_ROW, Constants.REMOVE_COMBO_ITEMS);
        }
    }

    /**
     * Exactly the same for Editing and Browsing
     */
    public void remove(OperationsProcessor context)
    {
        int idx = context.getCurrentBlock().getIndex();
        Err.pr(SdzNote.GENERIC, "REM AT " + idx);
        if(context.getCurrentBlock().dataRecordsSize() > 0)
        {
            context.getCurrentBlock().removeDataRecord(idx);

            int size = context.getCurrentBlock().dataRecordsSize();
            // Print.pr( "$$ idx of removed was " + idx);
            // Print.pr( "$$ size after removed was " + size);
            if(!(idx == 0 && size > 0))
            {
                context.getCurrentBlock().decCursor(false);
            }
            if(size == 0)
            {
                context.getCurrentBlock().blankoutDisplay(
                    OperationEnum.REMOVE, "Last record has been removed",
                    Constants.REMOVE_COMBO_ITEMS);
                context.getCurrentBlock().zeroIndex(); // recursive
                // Print.pr( "Zeroing to bottom b4 syncDetail, but NOT changing state");
                // If change state this early then blankoutUI can get confused. Now done
                // later
                // context.changeStateToNoMove( FreezingState.getNewInstance( context), context.getCurrentBlock());
            }
            else
            {
                context.getCurrentBlock().syncDetail(
                    OperationEnum.REMOVE, Constants.REMOVE_COMBO_ITEMS);
            }
        }
    }

    public void leaveBlock(OperationsProcessor context, Block toBlock)
    {
        /*
        times++;
        Err.pr( "$$(LEAVING)  " + context.getCurrentBlock() + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        /**
         * Concerned with not allowing user to change block if
         * there is a problem. Because of the exception that is
         * thrown, should (if tabbed control properly written)
         * work visually when want to change tabs. If the control
         * isn't a tabbed control then you know what I mean!
         * (see TabbedVisibleStrand.fireGoNodeEvent())
         *
         * In both edit and view because sometimes (EditInsertsBeforeCommit)
         * are able to alter values if have not committed
         */
        boolean fieldsHaveChanged = false;
        if(context.getCurrentBlock().fieldsChanged())
        {
            if(!context.isIgnoreValidation()
                && context.getCurrentBlock().anyComplaints(getCantMoveState(context)))
            {
                Session.getErrorThrower().throwApplicationError(
                    context.getCurrentBlock().retrieveAnyComplaintsMsg(),
                    ApplicationErrorEnum.INTERNAL);
            }
            fieldsHaveChanged = true;
        }
        if(!context.isIgnoreValidation()
            && !context.getCurrentBlock().validateNode())
        {
            Session.getErrorThrower().throwApplicationError(
                context.getCurrentBlock().retrieveValidateNodeMsg(),
                ApplicationErrorEnum.NODE_VALIDATION);
        }
        if(fieldsHaveChanged)
        {
            context.getCurrentBlock().applyDifferences();
        }
    }

    public void enterQuery(OperationsProcessor context)
    {
        context.changeState( // EnterQueryState.getNewInstance( context),
            BlockStateCreator.newInstance(context, StateEnum.ENTER_QUERY),
            context.getCurrentBlock());
        context.getCurrentBlock().setDisplayEditable(true);
        context.getCurrentBlock().blankoutDisplay(
                OperationEnum.ENTER_QUERY, 
                "pressed ENTER_QUERY in block in navigable state",
                Constants.REMOVE_COMBO_ITEMS);
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

    public void add(OperationsProcessor context)
    {
        context.changeState( // AddingState.getNewInstance( context),
            BlockStateCreator.newInstance(context, StateEnum.NEW),
            context.getCurrentBlock());
    }

    /*
    public void addPrior(OperationsProcessor context)
    {
        context.changeState(
            BlockStateCreator.newInstance(context, StateEnum.NEW_PRIOR),
            context.getCurrentBlock());
    }
    */

    public void enterBlock(OperationsProcessor context, Block fromBlock)
    {
        if(context.getCurrentBlock().dataRecordsEmpty())
        {
            Block currentBlock = context.getCurrentBlock();
            context.changeState( // FreezingState.getNewInstance( context, currentBlock),
                BlockStateCreator.newInstance(context, StateEnum.FROZEN),
                currentBlock);
            currentBlock.setDisplayEditable(false);
        }
    }

    /**
     * In here capabilities have been set. Also have data. (Note complication with
     * initial load, where when this method called (in FreezingState), have not
     * yet loaded the data into the block).
     */
    public void capabilitiesSet(OperationsProcessor context)
    {
        context.disableNavigation();
    }

    abstract StateEnum getCantMoveState(OperationsProcessor context);
}
