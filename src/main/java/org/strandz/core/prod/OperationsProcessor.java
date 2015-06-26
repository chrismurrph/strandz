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
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.DynamicAllowedEvent;
import org.strandz.core.domain.event.StateChangeEvent;
import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.SdzDsgnrNote;
import org.strandz.lgpl.util.CapabilityAction;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Publisher;
import org.strandz.lgpl.util.Utils;

import java.util.Iterator;
import java.util.List;

public class OperationsProcessor
    implements StateChangeTrigger
{
    private Block currentBlock;
    private OperationEnum currentOperation = OperationEnum.UNKNOWN;
    /*
    For StateChangeEvent events that this class is capable
    of generating (so eg./ a graphic that showed the user
    the mode could observe the changes)
    NB./ There is no method for adding listeners because
    what Node collects is transferred to here.
    */
    private Publisher stateChangePublisher;
    private Enabler enabler = new NullEnabler();
    private StateEnum previousState = StateEnum.UNKNOWN;
    private boolean validationRequired = true;
    private Block.BlockMemento whileQueryingMemento;

    private static int times;
    private static int timesExeQry;
    private static int timesCurrentBlock;
    private static int timesCurrentBlockNull;

    public OperationsProcessor(Publisher stateChangePublisher)
    {
        this.stateChangePublisher = stateChangePublisher;
    }

    public void setEnabler(Enabler enabler)
    {
        this.enabler = enabler;
    }

    public Object getEnabler()
    {
        return enabler;
    }

    public void stateChangePerformed(StateChangeEvent evt)
    {// Err.pr( "Changing state to " + evt.getCurrentState() + " from " +
        // evt.getPreviousState() + " for " + currentBlock.getNode());
        /*
        if(evt.getCurrentState() == StateEnum.FROZEN)
        {
        //disableDeletePost( node);
        enabler.disable( OperationEnum.REMOVE);
        enabler.disable( OperationEnum.POST);
        if(enabler.dataRecordsEmpty())
        {
        //disableLoad( node);
        Err.pr( "Empty");
        }
        else
        {
        Err.pr( "Not empty");
        }
        }
        */}

    public void setCurrentBlock(Block toBlock)
    {
        if(toBlock.getState() == null)
        {
            // Err.pr( "Set to freezing b/c didn't know");
            toBlock.setState( // FreezingState.getNewInstance( this, block)
                BlockStateCreator.newInstance(this, StateEnum.FROZEN));
        }
        // Done below !
        // setCurrentOperation( OperationEnum.CHANGE_BLOCK);
        // getCurrentState( block).anyKeyPressed( this, OperationEnum.CHANGE_BLOCK, block);
        /**/
        times++;
        Err.pr(SdzNote.CHANGING_NODES_STATES_I_NOT_RECOGNISED,
            "*** block changing from " + currentBlock + " to " + toBlock + " in "
                + toBlock.getState() + " times " + times);
        if(times == 0)
        {
            Err.stack();
        }
        /**/
        if(!toBlock.isConstructing() && currentBlock != null)
        {
            getCurrentState().anyKeyPressed(this,
                OperationEnum.GOTO_NODE, currentBlock, currentBlock.fieldsChanged());
            setCurrentOperation(OperationEnum.GOTO_NODE);
            leaveBlockToGoTo(toBlock);
        }

        Block fromBlock = currentBlock;
        internalSetCurrentBlock(toBlock);
        /*
        times++;
        Err.pr( "**!! currentBlock has been set to " + currentBlock + " times " + times);
        if(times == 3)
        {
        Err.stack();
        }
        */
        if(!toBlock.isConstructing())
        {
            enterBlockFrom(fromBlock);
        }
    }
    
    public void internalSetCurrentBlock( Block currentBlock)
    {
        if(currentBlock != null)
        {
            timesCurrentBlock++;
            int id = Utils.UNSET_INT;
            if(this.currentBlock != null)
            {
                id = this.currentBlock.id;
            }
            Err.pr( SdzNote.LOVS_CHANGE_DATA_SET.isVisible() /*||
                    SdzNote.NOT_KEEPING_PLACE.isVisible()*/ || SdzNote.SET_AND_CREATE_BLOCKS.isVisible(),
                "In oper, current block ID: " + id + " being set to " +
                    currentBlock.getName() + ", block ID: " + currentBlock.id +  
                    ", times " + timesCurrentBlock);
            if(timesCurrentBlock == 2)
            {
                //Err.stack();
            }
        }
        else
        {
            timesCurrentBlockNull++;
            int id = Utils.UNSET_INT;
            if(this.currentBlock != null)
            {
                id = this.currentBlock.id;
            }
            Err.pr( SdzNote.SET_AND_CREATE_BLOCKS.isVisible(),
                "current block ID: " + id + " being set to null, times " + timesCurrentBlockNull);
            if(timesCurrentBlockNull == 0)
            {
                Err.stack();
            }
        }
        this.currentBlock = currentBlock;
    }

    public Block getCurrentBlock()
    {
        //Err.pr( "!! currentBlock returning is " + currentBlock);
        return currentBlock;
    }

    public OperationEnum getCurrentOperation()
    {
        /*
        times++;
        Err.pr( "getCurrentOperation() to return " + currentOperation + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        return currentOperation;
    }

    public void addStateChangeListener(StateChangeTrigger listener)
    {
        stateChangePublisher.subscribe(listener);
    }

    public void removeStateChangeListener(StateChangeTrigger listener)
    {
        stateChangePublisher.cancelSubscription(listener);
    }

    /**
     * When are doing this from prev/next action, will not be going up to topmost
     * levels. When are doing from POST action, will be doing this behaviour down
     * from every single topmost level (not just every single related topmost level).
     * This method only called for the POST action. POST action called from
     * commit action.
     */
    public void post(Block block, boolean userInitiated)
    {
        setCurrentOperation(OperationEnum.POST);
        StateEnum origState = block.getState().getState();
        Err.pr( SdzNote.TIES_ENFORCE_TYPE, "What happens if POST when enforceType is still a node?");
        for(Iterator e = block.tm.getSingles(); e.hasNext();)
        {
            Block localBlock = (Block) e.next();
            localBlock.post(OperationEnum.POST);
        }
        // 16/07/04 Added this line but hoped this method only called by
        // user, not internally
        if(userInitiated)
        {
            block.generateNavigationEvent(OperationEnum.POST, origState);
        }
        /*
        * The last two params here mean that this call will have no effect
        * on the UI. The intention of this call is to set the b4Image
        * (or FieldObj's and TableObj's idea of b4Image) so that a call to
        * recordObj.fieldsChanged() will deliver a negative next time it is
        * called (this time it is called as part of internalLastHit).
        */
        /*
        Give this up, till know what doing.

        if(! block.getDataRecords().isEmpty())
        {
        Err.pr( "== Marking a potential change for index " + block.getIndex());
        Err.pr( "== Cursor will be " + block.getCursorPosition());
        block.recordObj.setUI( block.getDataRecords().get(
        block.getIndex()),
        false);
        }
        */
        // 03/07/01 At end of commit/post was losing cursor.
        // fix TODO. Way to fix will be to not lose it in first place.
    }

    public void previous(Block block)
    {
        getCurrentState( block).anyKeyPressed(this, OperationEnum.PREVIOUS, block, block.fieldsChanged());
        setCurrentOperation(OperationEnum.PREVIOUS);
        StateEnum origState = block.getState().getState();
        getCurrentState( block).previous(this);
        block.generateNavigationEvent(OperationEnum.PREVIOUS, origState);
        disableNavigation();
    }

    /**
     * In Adding we don't use syncDetail but blankoutUI. Reason for this
     * is that adding mode is completely disconnected from data, and syncDetail
     * is all about syncing the master/detail data.
     */
    public void add(Block block, OperationEnum operationEnum)
    {
        getCurrentState( block).anyKeyPressed(this, operationEnum, block, block.fieldsChanged());
        //At least this is after item validation (which is handled in MoveTracker).
        //However really needs to be after all validation. Assuming all validation
        //fired from item validation, which may be wrong! TODO Find out
        block.getMoveBlock().getMoveTracker().notifySuccessfulInsert();
        setCurrentOperation(operationEnum);
        StateEnum origState = block.getState().getState();
        //
        // Err.pr( "To blankoutDisplay() as part of add");
        // 09/06/04 moved here to fix unit test TestClientsChildFocus.testPreInsertDefault()
        block.fireNodeDefaultEvent(DataFlowEvent.PRE_INSERT);
        if(/*block.getIndex() == -1*/operationEnum == OperationEnum.INSERT_AFTER_PLACE)
        {
            block.incCursor( false);
        }
        //Err.pr( "To add for opn " + operationEnum + ", and idx of block <" + block + ">: " + block.getIndex());
        block.blankoutDisplay(operationEnum, "add to " + block + " due " + operationEnum, 
                              Constants.REMOVE_COMBO_ITEMS);
        block.displayLovObjects();
        freezeAllBelow(block.getChild());
        // Err.pr( "Going to zeroIndex() on child of " + block);
        block.getChild().zeroIndex();
        block.getChild().setDataRecordsNull();
        // Following gets rid of actual data, which is what want to do here.
        // Will put it back when move back to this master record, as part of
        // doing syncDetail
        // tmp block.getChild().nullToBottom( false);
        if(SdzDsgnrNote.ADD_S_HAVE_HAPPENED.isVisible())
        {
            Err.debug();
        }
        getCurrentState( block).add(this);
        block.generateNavigationEvent(operationEnum, origState);
        disableNavigation();
    }

    /*
    public void addPrior(Block block)
    {
        add(block, OperationEnum.INSERT_PRIOR);
    }
    */

    public void next(Block block)
    {
        getCurrentState( block).anyKeyPressed(this, OperationEnum.NEXT, block, block.fieldsChanged());
        setCurrentOperation(OperationEnum.NEXT);
        StateEnum origState = block.getState().getState();
        getCurrentState( block).next(this);
        block.generateNavigationEvent(OperationEnum.NEXT, origState);
        disableNavigation();
    }

    public void setRow(Block block, int row)
    {
        int cursor = block.getIndex();
        // Err.pr( "*** row: " + row);
        // Err.pr( "*** cursor: " + cursor);
        if(row != cursor)
        {
            block.getMoveBlock().getMoveTracker().setRowSetting(row);
            getCurrentState( block).anyKeyPressed(this, OperationEnum.SET_ROW, block, block.fieldsChanged());
            setCurrentOperation(OperationEnum.SET_ROW);
            StateEnum origState = block.getState().getState();
            getCurrentState( block).setRow(this, row);
            block.generateNavigationEvent(OperationEnum.SET_ROW, origState);
            disableNavigation();
            // Err.pr( "Block.setRow (ON DIFFERENT) called with " + row);
        }
        else
        {
            block.getMoveBlock().setRecordValidationOutcome(true);
        }
    }

    public void freezeAllBelow(AbstBlock block)
    {
        // Err.pr( "Calling freezeAllBelow with block: " + block);
        if(block != null && !(block instanceof NullBlock))
        {
            block.setState( // FreezingState.getNewInstance( this, block)
                BlockStateCreator.newInstance(this, StateEnum.FROZEN));
            freezeAllBelow(block.getChild());
        }
    }

    public void enterQuery(Block block)
    {
        getCurrentState( block).anyKeyPressed(this, OperationEnum.ENTER_QUERY,
            block, block.fieldsChanged());
        setCurrentOperation(OperationEnum.ENTER_QUERY);
        getCurrentState( block).enterQuery(this);
        disableForEnterQuery();
    }

    public int executeSearch(Block block, AttributeValueSaverI saver, List adapters)
    {
        enableAfterEnterQuery();
        int result = 0;
        setCurrentOperation(OperationEnum.EXECUTE_SEARCH);
        StateEnum origState = block.getState().getState();
        // CopyPasteBuffer copyPasteBuffer = new CopyPasteBuffer();
        // List attribs = Attribute.getNonBlankChangedAttributes( block.getNode().getAttributes());
        // copyPasteBuffer.copyItemValues( attribs, block.getNode());
        block.syncDetail(OperationEnum.EXECUTE_SEARCH, Constants.REMOVE_COMBO_ITEMS);

        int i = 0;
        for(Iterator iter1 = block.getDataRecords().iterator(); iter1.hasNext(); i++)
        {
            Object obj = iter1.next();
            int j = 0;
            int numMatched = 0;
            for(Iterator iter2 = adapters.iterator(); iter2.hasNext(); j++)
            {
                ItemAdapter ad = (ItemAdapter) iter2.next();
                // Err.pr( "obj of type: " + obj.getClass().getName());
                // Err.pr( "itemAdapter's cell's class of type: " + ad.getCell().getClazz().getName());
                Object objValue = ad.getDoAdapter().getFieldValueFromAny(obj);
                // List ties = ad.getCell().getTiesManager().getRouteTo( ad, obj.getClass());
                // Err.pr( "Have gone from a " + ad.getClazz() + " to a " + objValue.getClass());
                if(ad.getName() == null)
                {
                    Err.error(
                        "Attribute does not have a name: " + ad.getName() + ", cell: "
                            + ad.getCell());
                }

                Object searchValue = saver.getItemValue(ad.getName());
                if(searchValue == null)
                {
                    Err.pr( saver.toString());
                    Err.error(
                        "Could not find a value to search for using the name: <"
                            + ad.getName() + ">");
                }
                // Err.pr( "Cfing: <" + objValue + "> with <" + searchValue + "> of type " +
                // searchValue.getClass().getName());
                if(objValue != null
                    && !objValue.equals(
                    ad.getDoAdapter().convertToDOFieldValue(searchValue)))
                {
                    break;
                }
                else if(objValue == null)
                {
                    break;
                }
                else
                {
                    numMatched++;
                }
            }
            if(numMatched == adapters.size())
            {
                break; // they were all equal
            }
        }
        if(i != block.getDataRecords().size())
        {
            // Err.pr( "Found match at row " + i);
            result = i;
            // block.getNode().setRow( i);
            // block.setIndex( true, i);
        }
        block.generateNavigationEvent(OperationEnum.EXECUTE_SEARCH, origState);
        disableNavigation();
        return result;
    }

    /**
     * When this is called user has already, via an event,
     * loaded the data into block.
     */
    public void executeQuery(Block block)
    {
        enableAfterEnterQuery();
        setCurrentOperation(OperationEnum.EXECUTE_QUERY);
        StateEnum origState = block.getState().getState();
        timesExeQry++;
        Err.pr( SdzNote.CTV_STRANGE_LOADING, "To call syncDetail() on <" + block + ">, times " + timesExeQry);
        if(timesExeQry == 1)
        {
            //Err.debug();
        }
        block.syncDetail(OperationEnum.EXECUTE_QUERY, Constants.REMOVE_COMBO_ITEMS);
        block.generateNavigationEvent( OperationEnum.EXECUTE_QUERY, origState);
        disableNavigation();
    }

    public void remove(Block block)
    {
        getCurrentState( block).anyKeyPressed(this, OperationEnum.REMOVE, block, block.fieldsChanged());
        setCurrentOperation(OperationEnum.REMOVE);
        StateEnum origState = block.getState().getState();
        getCurrentState( block).remove(this);
        if(getCurrentBlock().dataRecordsSize() == 0)
        {
            getCurrentBlock().freezeToBottom();
        }
        else
        {
            StateEnum currentState = getCurrentState( block).getState();
            if(// NavigatingState.getNewInstance( this).getState()
                currentState == StateEnum.EDIT || currentState == StateEnum.VIEW
                )
            {// Err.pr( "Why does remove always have to change the state, when remaning at Navigating?");
            }
            else
            {
                BlockState newState = null;
                if(getCurrentBlock().isAllowed(CapabilityEnum.UPDATE))
                {
                    newState = BlockStateCreator.newInstance(this, StateEnum.EDIT);
                }
                else
                {
                    newState = BlockStateCreator.newInstance(this, StateEnum.VIEW);
                }
                changeState( // NavigatingState.getNewInstance( this),
                    newState, getCurrentBlock());
            }
        }
        //10/12/04 Found that this being done twice. Had just done in
        //NavigatingState.remove()
        //11/12/04 Getting problems with remove so put back
        //It was pretty necessary, for both master being table and not!
        //TODO - fix the problem of this happening just before in code somewhere
        //just above here
        block.syncDetail(OperationEnum.REMOVE, Constants.REMOVE_COMBO_ITEMS);
        block.generateNavigationEvent(OperationEnum.REMOVE, origState);
        disableNavigation();
    }

    private void visualCursorChange()
    {
        if(getState(currentBlock) != StateEnum.FROZEN)
        {
            currentBlock.visualCursorChange();
        }
    }

    /**
     * Doesn't seem to be any point in letting listeners know of changes
     * to state of the non-current block. Except - when clearing everything
     * out as part of a commit, are going thru all blocks and don't have a
     * concept of a current block. (As setting a current block does too many
     * other things that are not set up at the time).
     */
    public void generateStateChangeEvent(
        final StateEnum stateChangeTo,
        final StateEnum stateChangeFrom,
        final Object eminatingNode)
    {
        if(stateChangeTo == stateChangeFrom)
        {
            if(stateChangeTo == StateEnum.NOMOVE_NEW)
            {// ok - must happen every time attempt to get out of the row
                // Err.pr( "Trying to get out of StateEnum.NOMOVE_NEW");
            }
            else if(stateChangeTo == StateEnum.NOMOVE_NEW_PRIOR)
            {// just copy of above!
            }
            else if(stateChangeTo.isNavigating())
            {// ok - happens when reload
                // Err.pr( "Reloading so going back to " + Constants.stringValue( stateChangeTo));
            }
            else if(stateChangeTo == StateEnum.NEW)
            {// ok - happens when reload
                // Err.pr( "Fine to add one, then immediately add another");
            }
            else if(stateChangeTo == StateEnum.FROZEN)
            {// ok - happens when commit, and then commit again
            }
            else if(stateChangeTo == StateEnum.ENTER_QUERY)
            {// ok - happens when try to go to another node and are rejected whilst in this state
            }
            else
            {
                Err.error("Can't change to the same state: " + stateChangeTo);
            }
        }
        /*
        * DEBUG - another place to look is changeStateToNoMove method
        times++;
        Err.pr( "=========FROM: " + stateChangeFrom);
        Err.pr( "           TO: " + stateChangeTo);
        Err.pr( "           AT: " + eminatingNode);
        //Err.pr( "           ID: " + eminatingNode.getId());
        Err.pr( "times: " + times);
        Err.pr( "");
        if(times == 0)
        {
        Err.stack();
        }
        */
        /*
        Object eminatingNode = null;
        if(currentBlock != null)
        {
        eminatingNode = currentBlock.getNode();
        }
        */
        /*
        for(Iterator e = stateChangeListenerList.iterator(); e.hasNext();)
        {
          ((StateChangeTrigger)e.next()).stateChangePerformed(
              new StateChangeEvent( eminatingNode, stateChangeTo, stateChangeFrom));
        }
        */
        stateChangePublisher.publish
            (
                new Publisher.Distributor()
                {
                    public void deliverTo(Object subscriber)
                    {
                        StateChangeTrigger sct = (StateChangeTrigger) subscriber;
                        sct.stateChangePerformed(
                            new StateChangeEvent(eminatingNode, stateChangeTo, stateChangeFrom));
                    }
                }
            );
    }

    /*
    public boolean exitComplaint()
    {
    return !currentBlock.validateNode();
    }
    */

    void leaveBlockToGoTo(Block toBlock)
    {
        // Err.pr("Leaving: " + currentBlock);
        // Err.pr("Going to: " + toBlock);
        if(currentBlock == toBlock)
        {
            //Now happens when in NodeCollectingState
            //Err.error("Why leave to go to same: " + toBlock);
        }
        /*
        * If think about putting an anyKeyPressed here don't 'cause as part of
        * userFocus post is called b4 the leaveBlock/enterBlock duo,
        * which does an anyKeyPressed at all levels (which in turn means for all
        * intBlocks).
        */
        getCurrentState().leaveBlock(this, toBlock);
        /*
        * isAnInsert being updated when syncDetail will work for all the times
        * when 'move around'. Here syncDetail will not be called. As last
        * operation may have been an insert, we will have to call isInserted
        * here.
        */
        /*
        if(eleIndex != 0)
        {
        isAnInsert.setValue( dataRecords.isInserted( eleIndex - 1));
        setUIEditable();
        }
        */
        visualCursorChange();
    }

    void enterBlockFrom(Block fromBlock)
    {
        /*
        times++;
        Err.pr( "$$(ENTERING)  " + currentBlock + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        /*
        Err.pr("YY entering " + this);
        Err.pr("YY Mode is " + getState());
        Err.pr("YY from " +
        ((fromBlock == null) ? null : fromBlock.node)
        + "(" + fromBlock + ")");
        */
        //
        getCurrentState().enterBlock(this, fromBlock);

        // Re. following, now not done in setNewCapabilities, but brought
        // here and put b4 call to disableNavigation, so won't wipe it out
        //
        // setNewCapabilities always done after this, so will always wipe any
        // changes made here
        //
        ProdNodeI node = currentBlock.getProdNodeI();
        /*
        * IgnoredChild theory was to give no indication to
        * user that he is on a block, and always jump away b4
        * an event occurs. Unfortunately cannot jump away when
        * block is invalid. Get validation message and nufin else
        * happens.
        */
        if(!node.isAllowed(CapabilityEnum.IGNORED_CHILD))
        {
            // Err.pr( "node entering and questioning is " + node);
            // if(fromBlock != null) Err.pr( "came from: " + fromBlock.getNode());
            enabler.affect(OperationEnum.ENTER_QUERY,
                node.isAllowed(OperationEnum.ENTER_QUERY));
            enabler.affect(OperationEnum.EXECUTE_QUERY,
                node.isAllowed(OperationEnum.EXECUTE_QUERY));
            enabler.affect(OperationEnum.EXECUTE_SEARCH,
                node.isAllowed(OperationEnum.EXECUTE_SEARCH));
            enabler.affect(OperationEnum.INSERT_AFTER_PLACE,
                node.isAllowed(OperationEnum.INSERT_AFTER_PLACE));
            enabler.affect(OperationEnum.INSERT_AT_PLACE,
                node.isAllowed(OperationEnum.INSERT_AT_PLACE));
            enabler.affect(OperationEnum.REMOVE,
                node.isAllowed(OperationEnum.REMOVE));
            enabler.affect(OperationEnum.POST, node.isAllowed(OperationEnum.POST));
            enabler.affect(OperationEnum.PREVIOUS,
                node.isAllowed(OperationEnum.PREVIOUS));
            enabler.affect(OperationEnum.NEXT, node.isAllowed(OperationEnum.NEXT));
            enabler.affect(OperationEnum.SET_ROW,
                node.isAllowed(OperationEnum.SET_ROW));
            // No such thing as isAllowed for commit. It is
            // always allowed. (If give option later, it will
            // be at the transaction level of granularity).
            // Above strictly true. Commit will be for the whole
            // Strand, but most flexible to be able to determine
            // at which Node the end user is allowed to press the
            // commit button.
            enabler.affect(OperationEnum.COMMIT_ONLY,
                node.isAllowed(OperationEnum.COMMIT_ONLY));
            enabler.affect(OperationEnum.COMMIT_RELOAD,
                node.isAllowed(OperationEnum.COMMIT_RELOAD));
            /*
            Err.pr( "Inside Strand.setNewCapabilities where will call NodeController.setAbilities");
            */
        }
        disableNavigation();
    }

    public void capabilitiesSet()
    {
        getCurrentState().capabilitiesSet(this);
    }

    public StateEnum getState(Block block)
    {
        return getCurrentState( block).getState();
    }

    private boolean isCurrentBlock(Block block)
    {
        return currentBlock == block;
    }

    public void changeStateToNoMove(StateEnum newState, Block block)
    {
        if(newState == StateEnum.NOMOVE_EDIT)
        {
            changeState(BlockStateCreator.newInstance(this, StateEnum.NOMOVE_EDIT),
                // NoMoveEditState.getNewInstance( this),
                block);
        }
        else if(newState == StateEnum.NOMOVE_NEW)
        {
            changeState(BlockStateCreator.newInstance(this, StateEnum.NOMOVE_NEW), // NoMoveAddState.getNewInstance( this),
                block);
        }
        else if(newState == StateEnum.NOMOVE_NEW_PRIOR)
        {
            changeState(
                BlockStateCreator.newInstance(this, StateEnum.NOMOVE_NEW_PRIOR), // NoMoveAddState.getNewInstance( this),
                block);
        }
        else if(newState == StateEnum.NOMOVE_VIEW)
        {
            changeState(BlockStateCreator.newInstance(this, StateEnum.NOMOVE_VIEW),
                // NoMoveBrowseState.getNewInstance( this),
                block);
        }
        else
        {
            Err.error("Another NoMoveXXXState has been created");
        }
    }

    public void changeState(BlockState newState, Block block)
    {
        if(block == null || isCurrentBlock(block))
        {
            _changeState(newState, currentBlock);
        }
        else
        {
            /*
            * Valid code entry point when nullToBottom
            */
            _changeState(newState, block);
        }
    }

    /**
     * Note that freezeToBottom does not go thru here
     */
    private void _changeState(BlockState newState, Block block)
    {
        /*
        * Now generating the event after the state has changed, which
        * makes sense if something goes wrong with changing the state.
        * Similarly makes sense that data is changed before state is
        * changed.
        * Prompter for code change was that NodeStatusBar was giving an
        * incorrect reading when moving out of NEW. As state had not
        * changed but data had, the block's index was overreading by 1.
        * Reason for this is that when actually add we increment the
        * cursor, to make up for fact that getCursorPosition() will no
        * longer be overreading by one on purpose due to temporary record
        * being there whilst in NEW state. When outputting the label when
        * had not moved out of NEW, then getCursorPosition() was still
        * doing the purposeful overread, yet the buffered record had
        * been added.
        */
        // StateEnum oldStateCopy = currentBlock.getState().getState();
        block.setState(newState);
        /**/
        times++;
        Err.pr(SdzNote.OVER_FREEZING, "$$$  Have changed state from " + previousState +
            " to " + newState.getState() + " for <" +
            block.getName() + "> times " + times);
        if(times == 0)
        {
            Err.stack();
        }
        /**/
        if(previousState == newState.getState())
        {
            if(currentOperation != OperationEnum.EXECUTE_QUERY && // reload
                currentOperation != OperationEnum.INSERT_AFTER_PLACE && // insert into already NEW
                // Possibly s/protect from happening in first place TODO
                previousState != StateEnum.NOMOVE_NEW
                && previousState != StateEnum.NOMOVE_NEW_PRIOR
                && previousState != StateEnum.NOMOVE_EDIT
                && previousState != StateEnum.NOMOVE_VIEW
                && previousState != StateEnum.EDIT // TestComplexNavigation.setUp()
                && previousState != StateEnum.FROZEN // commit twice, now clearing
                && previousState != StateEnum.NEW // did when created CMLBug.anyStateChangePossible
                && previousState != StateEnum.VIEW // TheRosterStrand
                && previousState != StateEnum.ENTER_QUERY
                )
            {
                Err.error(SdzNote.ANY_STATE_CHANGE_POSSIBLE,
                    "Already in state " + previousState);
            }
        }
         /**/
        generateStateChangeEvent(newState.getState(), previousState,
            block.getProdNodeI());
        // previousState = oldStateCopy;
    }

    public BlockState getCurrentState()
    {
        return getCurrentState( currentBlock);
    }

    public BlockState getCurrentState( Block block)
    {
        /*
        if(!isCurrentBlock(block))
        {// Err.pr( "Trying to get state for non-current block " + block);
        }
        */
        if(block.getState() != null)
        {
            return block.getState();
        }
        else
        {
            Err.error( "blockState not yet set!");
            return null; // for comp
        }
    }

    /*
    public void ifInsertedDeleteActions()
    {
    currentBlock.getState().ifInsertedDeleteActions( this);
    }
    */

    public void dynamicAllowed(DynamicAllowedEvent evt)
    {
        DynamicAllowedEvent evts[] = new DynamicAllowedEvent[1];
        evts[0] = evt;
        dynamicAllowed(evts);
    }

    void dynamicAllowed(DynamicAllowedEvent evts[])
    {
        if(enabler != null)
        {
            for(int i = 0; i < evts.length; i++)
            {
                DynamicAllowedEvent evt = evts[i];
                if(evt.getDynamicAllowed())
                {
                    enabler.enable(evt.getID());
                }
                else
                {
                    enabler.disable(evt.getID());
                }
            }
        }
        currentBlock.getState().ifInsertedDeleteActions(this);
    }

    private boolean detAllowed(DynamicAllowedEvent evt)
    {
        boolean result = currentBlock.isAllowed(evt.getID());
        if(evt.getID() == OperationEnum.INSERT_AFTER_PLACE)
        {
            Print.pr(
                "For block " + currentBlock.getProdNodeI().getName() + " allowed "
                    + result);
        }
        return result;
    }

    /**
     * Will use enable/disable directly rather than calling dynamicAllowed() (which I'm
     * sure will be removed at some stage).
     */
    private void disableForEnterQuery()
    {
        whileQueryingMemento = currentBlock.createBlockMemento();
        CommandActions cmds = currentBlock.getCommandActions();
        for(int i = 0; i <= CapabilityEnum.ALL_KNOWN_CAPABILITIES.length - 1; i++)
        {
            CapabilityEnum enumId = CapabilityEnum.ALL_KNOWN_CAPABILITIES[i];
            CapabilityAction cap = cmds.get(enumId);
            if(enumId != CapabilityEnum.EXECUTE_QUERY && enumId != CapabilityEnum.EXECUTE_SEARCH)
            {
                if(cap.isBlockAllowed())
                {
                    cap.setBlockAllowed(false);
                    enabler.disable(enumId);
                }
            }
        }
    }

    private void enableAfterEnterQuery()
    {
        if(whileQueryingMemento != null)
        {
            currentBlock.setBlockMemento(whileQueryingMemento);
            whileQueryingMemento = null;
            //Now that the commands in currentBlock are back to normal we update the view with them
            CommandActions cmds = currentBlock.getCommandActions();
            for(int i = 0; i <= CapabilityEnum.ALL_KNOWN_CAPABILITIES.length - 1; i++)
            {
                CapabilityEnum enumId = CapabilityEnum.ALL_KNOWN_CAPABILITIES[i];
                CapabilityAction cap = cmds.get(enumId);
                if(cap.isBlockAllowed())
                {
                    enabler.enable(enumId);
                }
                else
                {
                    enabler.disable(enumId);
                }
            }
        }
        else
        {
            //this is the usual case where a query was done without being in
            //Enter Query mode first
        }
    }

    public void disableNavigation()
    {
        // Err.pr( "**\tdisableNavigation in " + currentBlock);
        if(!currentBlock.isAllowed(CapabilityEnum.IGNORED_CHILD))
        {
            int rowAfterInsert = currentBlock.getIndex();
            int rowCountAfterInsert = currentBlock.getRowCount();
            // Err.pr( "**\trowAfterInsert " + rowAfterInsert);
            // Err.pr( "**\trowCountAfterInsert " + rowCountAfterInsert);
            DynamicAllowedEvent evts3[] = new DynamicAllowedEvent[3];
            if(rowCountAfterInsert == 0)
            {
                evts3[0] = new DynamicAllowedEvent(OperationEnum.PREVIOUS,
                    currentBlock.isAllowed(OperationEnum.PREVIOUS));
                evts3[0].setAllowed(detAllowed(evts3[0]));
                evts3[0].setDynamicAllowed(false);
                evts3[1] = new DynamicAllowedEvent(OperationEnum.NEXT,
                    currentBlock.isAllowed(OperationEnum.NEXT));
                evts3[1].setAllowed(detAllowed(evts3[1]));
                evts3[1].setDynamicAllowed(false);
                evts3[2] = new DynamicAllowedEvent(OperationEnum.REMOVE,
                    currentBlock.isAllowed(OperationEnum.REMOVE));
                evts3[2].setAllowed(detAllowed(evts3[2]));
                evts3[2].setDynamicAllowed(false);
                dynamicAllowed(evts3);
            }
            else if(rowAfterInsert >= rowCountAfterInsert - 1)
            {
                // Err.pr( "TOP " + getName());
                boolean previous = true;
                boolean next = false;
                 /**/
                if(rowAfterInsert == 0 && rowCountAfterInsert == 1)
                {
                    // case where there is only one so can't go anywhere else
                    previous = false;
                }
                evts3[0] = new DynamicAllowedEvent(OperationEnum.PREVIOUS,
                    currentBlock.isAllowed(OperationEnum.PREVIOUS));
                evts3[0].setAllowed(detAllowed(evts3[0]));
                evts3[0].setDynamicAllowed(previous);
                evts3[1] = new DynamicAllowedEvent(OperationEnum.NEXT,
                    currentBlock.isAllowed(OperationEnum.NEXT));
                evts3[1].setAllowed(detAllowed(evts3[1]));
                evts3[1].setDynamicAllowed(next);
                evts3[2] = new DynamicAllowedEvent(OperationEnum.REMOVE,
                    currentBlock.isAllowed(OperationEnum.REMOVE));
                evts3[2].setAllowed(detAllowed(evts3[2]));
                evts3[2].setDynamicAllowed(true);
                dynamicAllowed(evts3);
            }
            else if(rowAfterInsert <= 0)
            {
                // Err.pr( "BOTTOM" + getName());
                evts3[0] = new DynamicAllowedEvent(OperationEnum.PREVIOUS,
                    currentBlock.isAllowed(OperationEnum.PREVIOUS));
                evts3[0].setAllowed(detAllowed(evts3[0]));
                evts3[0].setDynamicAllowed(false);
                evts3[1] = new DynamicAllowedEvent(OperationEnum.NEXT,
                    currentBlock.isAllowed(OperationEnum.NEXT));
                evts3[1].setAllowed(detAllowed(evts3[1]));
                evts3[1].setDynamicAllowed(true);
                evts3[2] = new DynamicAllowedEvent(OperationEnum.REMOVE,
                    currentBlock.isAllowed(OperationEnum.REMOVE));
                evts3[2].setAllowed(detAllowed(evts3[2]));
                evts3[2].setDynamicAllowed(true);
                dynamicAllowed(evts3);
            }
            else
            {
                // Err.pr( "MIDDLE" + getName());
                evts3[0] = new DynamicAllowedEvent(OperationEnum.NEXT,
                    currentBlock.isAllowed(OperationEnum.NEXT));
                evts3[0].setAllowed(detAllowed(evts3[0]));
                evts3[0].setDynamicAllowed(true);
                evts3[1] = new DynamicAllowedEvent(OperationEnum.PREVIOUS,
                    currentBlock.isAllowed(OperationEnum.PREVIOUS));
                evts3[1].setAllowed(detAllowed(evts3[1]));
                evts3[1].setDynamicAllowed(true);
                evts3[2] = new DynamicAllowedEvent(OperationEnum.REMOVE,
                    currentBlock.isAllowed(OperationEnum.REMOVE));
                evts3[2].setAllowed(detAllowed(evts3[2]));
                evts3[2].setDynamicAllowed(true);
                dynamicAllowed(evts3);
            }
        }
    }

    public String toString()
    {
        return enabler.toString();
    }

    /**
     * Returns the previousState.
     *
     * @return StateEnum
     */
    public StateEnum getPreviousState()
    {
        return previousState;
    }

    /**
     * Sets the currentOperation.
     *
     * @param currentOperation The currentOperation to set
     */
    private void setCurrentOperation(OperationEnum currentOperation)
    {
        if(currentOperation == null)
        {
            Err.error("Cannot set current operation null");
        }
        this.currentOperation = currentOperation;
        previousState = currentBlock.getState().getState();
        // Err.pr( "previousState been set to " + previousState + " for " +
        // currentBlock + " due " + currentOperation);
        if(previousState == null)
        {
            Err.error("Cannot set previous State to null");
        }
    }

    public void setValidationRequired(boolean b)
    {
        // Err.pr( "-----------Setting ValidationNotRequired to " + b);
        validationRequired = b;
    }

    public boolean isValidationRequired()
    {
        // Err.pr( "-----------Getting ValidationNotRequired " + validationRequired);
        return validationRequired;
    }

    public boolean isIgnoreValidation()
    {
        return currentBlock.getProdNodeI().isIgnoreValidation();
    }

    public void discreteSetCurrentBlock( Block replacementBlock)
    {
        currentBlock = replacementBlock;
    }
}
