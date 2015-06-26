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

import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import java.util.Iterator;
import java.util.List;

abstract class AnyAddingState extends BlockState
{
    private static int times = 0;

    /**
     * @param context
     */
    AnyAddingState(OperationsProcessor context)
    {
        /*
        times++;
        Err.pr( "Now in AnyAddingState constructor for " + context.getCurrentBlock()
        + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        /*
        * Also done in add, thus have blankout param so don't always do
        */
        List list = context.getCurrentBlock().getAdaptersArrayList();
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            ItemAdapter itemAdapter = (ItemAdapter) iter.next();
            if(!itemAdapter.isCalculated())
            {
                itemAdapter.setB4ImageValue(null);
            }
        }
         /**/
    }

    /**
     * Here we incorporate what was on the screen into data. completeAdd() gets
     * what was on the screen and puts it into data. Before we leave the adding
     * state (where the cursor is inflated by one), we increase the cursor to
     * the new row.
     * The actual key that was pressed will go on to be pressed, but in the
     * state we have changed to.
     */
    public void anyKeyPressed(OperationsProcessor context,
                              OperationEnum key,
                              Block block,
                              boolean fieldsChanged)
    {
        /**/
        super.anyKeyPressed(context, key, block, fieldsChanged);
        if(key != OperationEnum.REMOVE && key != OperationEnum.EXECUTE_QUERY)
        {
            if(!context.isIgnoreValidation() && block.anyComplaints(getNoMoveState()))
            {
                Session.getErrorThrower().throwApplicationError(
                    block.retrieveAnyComplaintsMsg(), ApplicationErrorEnum.INTERNAL);
            }
            block.completeAdd( getState().isPrior());
            Err.pr(SdzNote.COMPLETED_ADD_BUT_STILL_NEW,
                "RecordChange about to be fired, and state will be confusing to trigger writer");
            StateEnum origState = StateEnum.getFromName( block.getState().getState().getName()); //just assign numnuts!
            Err.pr(SdzNote.COMPLETED_ADD_BUT_STILL_NEW, "Orig state was " + origState);
            resetState( context, key, block);
            Err.pr(
                SdzNote.COMPLETED_ADD_BUT_STILL_NEW, "Have set state back to " + block.getState().getState()
                + " during op " + key);
            block.firePostingEvent( block.getProdNodeI(), key, origState);
            block.postCompleteAdd( key);
        }
        else
        {
            resetState(context, key, block);
        }
    }

    /*
    Experiment with doing this later. Initial reason was that
    NodeTableModelImpl.setDisplay needs to know is in NEW state.
    Need to do for POST as post hasn't got its own method call.
    NOW (12/07/04) wanting to make it earlier than after postCompleteAdd()
    due to CMLBug.completedAddButStillNew
    */
    private void resetState(
        OperationsProcessor context,
        OperationEnum key,
        Block block)
    {
        /*
        times++;
        Err.pr( "times ANYADDINGSTATE " + times);
        if(times == 1)
        {
        Err.pr( "backToSensibleState is culprit");
        }
        */
        if(key == OperationEnum.POST || key == OperationEnum.GOTO_NODE
            || key == OperationEnum.INSERT_AFTER_PLACE || key == OperationEnum.INSERT_AT_PLACE // 16/07/04 Added all these because wanted to go
            ||
            // back to a sensible state before PostingEvent
            key == OperationEnum.NEXT || key == OperationEnum.PREVIOUS
            || key == OperationEnum.SET_ROW
            )
        {
            backToSensibleState(context, key);
            block.syncDetail(OperationEnum.UNKNOWN, Constants.REMOVE_COMBO_ITEMS);
        }
        else if(key == OperationEnum.REMOVE)
        {
            /*
            * If backToSensibleState now, then won't be able to do differently
            * depending on state later. In particular don't want to actually
            * delete anything if are in AnyAddingState, so we keep the state
            * for now.
            */
            context.getCurrentBlock().getChild().reviveNulledDataRecords();
        }
        else if(key == OperationEnum.EXECUTE_QUERY)
        {
            /*
             * Legitimate - for example to reorder once have put a DO at
             * the beginning of a list - for example ContextController.reorderForMakeFirst()
             */
        }
        else
        {
            Err.pr(
                "key was " + key
                    + " so did not go backToSensible while AnyAdding for " + block);
            //Err.stack();
        }
    }

    private void backToSensibleState(
        OperationsProcessor context,
        OperationEnum op)
    {
        Err.pr(SdzNote.BACK_SENSIBLE,
            "@@@ where put back to editing/browsing for block: "
                + context.getCurrentBlock());
        context.getCurrentBlock().getChild().reviveNulledDataRecords();
        context.getCurrentBlock().adjustIndex( op);

        BlockState newState = null;
        if(context.getCurrentBlock().isAllowed(CapabilityEnum.UPDATE))
        {
            newState = BlockStateCreator.newInstance(context, StateEnum.EDIT, context.getCurrentBlock().getNode());
        }
        else
        {
            newState = BlockStateCreator.newInstance(context, StateEnum.VIEW, context.getCurrentBlock().getNode());
        }
        Err.pr(SdzNote.BACK_SENSIBLE, "Will be changing state back to " + newState);
        context.changeState(newState, context.getCurrentBlock());
         /**/
    }

    /*
    public void leaveBlock(OperationsProcessor context,
                           Block toBlock)
    {
        Err.error("Doesn't anyKeyPressed negate this ?");

        Block fromBlock = context.getCurrentBlock();
        if(!context.isIgnoreValidation())
        {
            if(fromBlock.anyComplaints(getNoMoveState()))
            {
                Session.getErrorThrower().throwApplicationError(
                    fromBlock.retrieveAnyComplaintsMsg(), ApplicationErrorEnum.INTERNAL);
            }
            if(!context.getCurrentBlock().validateNode())
            {
                Session.getErrorThrower().throwApplicationError(
                    fromBlock.retrieveValidateNodeMsg(),
                    ApplicationErrorEnum.NODE_VALIDATION);
            }
        }
        if(toBlock.isDescendantOf(fromBlock))
        {
            context.getCurrentBlock().completeAdd(getState().isPrior());
            backToSensibleState(context, OperationEnum.GOTO_NODE);
            // Detail will already be synced, so why do again?? (21/10/02)
            // One reason needs to be called is so that don't get any BI/AI problems
            fromBlock.syncDetail(OperationEnum.UNKNOWN);
            // throw new InternalNodeFocusChangeException( (LastFieldHolder)fromBlock);
        }
        else
        {
            context.getCurrentBlock().completeAdd(getState().isPrior());
            backToSensibleState(context, OperationEnum.GOTO_NODE);
        }
    }
    */

    // Called when focusing back, as were not meant to leave. Above leaveBlock
    // is the trigger for this call.
    public void enterBlock(OperationsProcessor context,
                           Block fromBlock)
    {// nufin
    }

    // As above
    public void capabilitiesSet(OperationsProcessor context)
    {// nufin
    }

    public void remove(OperationsProcessor context)
    {/*
     if(context.getCurrentBlock().getDataRecords().size() > 0)
     {
     Err.pr( "In remove and have at least one left");
     }
     else
     {
     Err.pr( "In remove and have NONE left");
     }
     */// backToSensibleState( context, OperationEnum.REMOVE);
    }

    public void next(OperationsProcessor context)
    {
        if(!context.getCurrentBlock().incCursor(true))
        {
            Session.getErrorThrower().throwApplicationError(
                context.getCurrentBlock().getNavigationWarningMessage(),
                ApplicationErrorEnum.INTERNAL);
        }
        // backToSensibleState( context, OperationEnum.NEXT);
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
        // backToSensibleState( context, OperationEnum.PREVIOUS);
        context.getCurrentBlock().syncDetail(OperationEnum.PREVIOUS, Constants.REMOVE_COMBO_ITEMS);
    }

    public void add(OperationsProcessor context)
    {// backToSensibleState( context, OperationEnum.INSERT);
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
            // backToSensibleState( context, OperationEnum.SET_ROW);
            context.getCurrentBlock().syncDetail(OperationEnum.SET_ROW, Constants.REMOVE_COMBO_ITEMS);
        }
        else
        {
            backToSensibleState(context, OperationEnum.SET_ROW);
        }
    }

    /*
    public void executeQuery( OperationsProcessorcontext)
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

    public void ifInsertedDeleteActions(OperationsProcessor context)
    {
        // Err.pr( "insert state s/always allow delete");
        Enabler enabler = (Enabler) context.getEnabler();
        if(enabler != null)
        {
            enabler.enable(OperationEnum.REMOVE);
        }
    }

    abstract StateEnum getNoMoveState();

    abstract boolean isPrior();
}
/********* HOW COMPLEX IT USED TO BE**********
 /**
 * Made UIIsBlank checking same whether isChildOf or not
 * because interface was
 * inconsistent with other modes and confusing for the
 * user. If the record is blank then don't allow the user
 * to move from that record - period.
 *
 void leaveBlock( Block context, Block toBlock) throws InternalNodeFocusChangeException
 {
 context.fireInternalDataFlowEvent(
 InternalDataFlowEvent.PRE_TABLE_VISUAL_POST);
 MyInteger levelsMoved = new MyInteger( 0);
 if(toBlock.isChildOf( context, levelsMoved)) //down
 {
 //Might assign to context.currentState
 AdapterState nmas = NoMoveAddState.getNewInstance( context);
 if(context.blankComplaint( nmas))
 {
 throw new InternalNodeFocusChangeException( (LastFieldHolder)context);
 }
 else if(context.exitComplaint( nmas, getState( context)))
 {
 throw new InternalNodeFocusChangeException( (LastFieldHolder)context);
 }
 else
 {
 context.completeAdd();
 }
 *
 * Make sure only going 1 block lower
 *
 //Err.pr("-^- levelsMoved has become " + levelsMoved.getValue());
 if( *toBlock != context.child*  levelsMoved.getValue() != 1)
 {
 MessageDlg oneDownOnly = new MessageDlg(
 "After Adding can only move to immediate child");
 oneDownOnly.getResponse();
 throw new InternalNodeFocusChangeException( (LastFieldHolder)context);
 }
 //how is completeAdd being done if this is changed to Editing
 //b4 lastHit gets a chance? - done here now
 //Err.pr("NN toBlock is child of context");
 if(toBlock.getState() == StateChangeEvent.FROZEN &&
 (!toBlock.isAllowed( InputControllerEvent.INSERT))
 )
 {
 MessageDlg noEnterDlg = new MessageDlg(
 "Block you are trying to enter is empty, and Insert not allowed");
 throw new InternalNodeFocusChangeException( (LastFieldHolder)context);
 }
 if(context.isAllowed( StateChangeEvent.EDIT))
 {
 context.changeState_( EditingState.getNewInstance( context));
 }
 else
 {
 context.changeState_( BrowsingState.getNewInstance( context));
 }
 *
 *experiment
 context.child.setDataRecords(
 context.dataRecords.get( context.eleIndex - 1), context);
 *
 //experiment
 //Err.pr( "experimental syncDetail");
 **
 * Haven't done detailed research as to exactly
 * why this was necessary.
 *
 context.syncDetail( true, SubRecordObj.DOWN_TO_DETAIL, false);
 }
 else //up or any
 {
 if(context.blankComplaint( NoMoveAddState.getNewInstance( context)))
 {
 throw new InternalNodeFocusChangeException( (LastFieldHolder)context);
 }
 else if(context.exitComplaint( NoMoveAddState.getNewInstance( context), getState( context)))
 {
 throw new InternalNodeFocusChangeException( (LastFieldHolder)context);
 }
 else //COMPLEX STUFF ADDED AS PER COMMENT ABOVE
 {
 context.completeAdd();
 if(context.isAllowed( StateChangeEvent.EDIT))
 {
 context.changeState_( EditingState.getNewInstance( context));
 }
 else
 {
 context.changeState_( BrowsingState.getNewInstance( context));
 }
 }
 *
 COMPLEX STUFF REMOVED AS PER COMMENT ABOVE
 //Err.pr("NN context is child of toBlock");
 if(context.dataRecords.isEmpty() && context.UIIsBlank())
 {
 context.zeroEleIndex();
 context.changeState_( FreezingState.getNewInstance( context));
 }
 else
 {
 //Err.pr("NN dataRecords NOT EMPTY: " + context.dataRecords.isEmpty() + " " +
 //                   context.UIIsBlank());
 context.completeAdd();
 context.changeState_( EditingState.getNewInstance( context));
 }
 *
 }
 *
 if(context.dataRecords.size() != 0)
 {
 Err.error("size must be 0 when leave block 2");
 }
 *
 }

 *
 Should not be necessary - when leaveBlock the add/edit will be
 completed. NOT true - can go to levels above, then come back to a
 block in AnyAddingState - at the moment only moving to the sides
 or Posting will trigger applyDifferences/completeAdd. This will
 make the most sense for validation purposes.
 *
 void enterBlock( Block context, Block fromBlock)
 {
 *
 * Thought of using isAnyMasterOf, but then no reason why couldn't
 * come from a child in any master's different leg.
 *
 }

 ***************************************/
