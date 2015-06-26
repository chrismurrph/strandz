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

import org.strandz.core.domain.AbstNodeTable;
import org.strandz.core.domain.FieldItemAdapter;
import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.MsgUtils;
import org.strandz.core.domain.NodeTableMethods;
import org.strandz.core.domain.NodeTableModelI;
import org.strandz.core.domain.TableSignatures;
import org.strandz.core.domain.UnknownControlException;
import org.strandz.core.domain.CalculationPlace;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.MoveBlockI;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.Constants;
import org.strandz.core.domain.constants.DataFlowEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.NavigationTrigger;
import org.strandz.core.domain.event.NodeDefaultTrigger;
import org.strandz.core.domain.event.OperationEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.prod.move.BlockForMoveBlockI;
import org.strandz.core.prod.move.MoveNodeI;
import org.strandz.core.prod.view.Builder;
import org.strandz.core.prod.view.CreatableI;
import org.strandz.core.prod.view.FieldObj;
import org.strandz.core.prod.view.NodeTableModelImplI;
import org.strandz.core.prod.view.SubRecordObj;
import org.strandz.core.prod.view.ViewBlockI;
import org.strandz.lgpl.extent.CombinationExtent;
import org.strandz.lgpl.extent.DEErrorContainer;
import org.strandz.lgpl.extent.HasCombinationExtent;
import org.strandz.lgpl.extent.NodeGroup;
import org.strandz.lgpl.extent.Ties;
import org.strandz.lgpl.extent.TiesManager;
import org.strandz.lgpl.extent.VisibleExtent;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MutableInteger;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Publisher;
import org.strandz.lgpl.util.StopWatch;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.widgets.table.ComponentTableView;

import java.awt.Component;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * class
 * <p/>
 * Will later implement HasCombinationExtent, with another method
 */

public class Block extends AbstBlock
    implements
    HasCombinationExtent,
    ViewBlockI,
    BlockForMoveBlockI
{
    /**
     * Zero delimited. Used for indexing into dataRecords.
     */
    private int index = -1;
    private Ties ties;
    public MasterDetailTiesManager tm;
    private Publisher navigationPublisher;
    Publisher nodePublisher;
    private Object tableControl;
    private BlockState currentState;
    /**
     * Keeps the value of whether the current cursor for this Block
     * contains a record that has previously been inserted during
     * the current transaction. If it is then the user can still
     * update or delete the value, regardless of what the initial
     * limitations were when the records were first loaded.
     */
    boolean isAnInsert = false;
    /**
     * Is a lookedupByNone SubRecordObj because there can only be one
     * pivot point.
     */
    private SubRecordObj recordObj;
    private NodeTableModelImpl ntmii;
    private MoveBlockI moveBlock;
    private List anyComplaintsMsg = new ArrayList();
    private List validateNodeMsg = new ArrayList();
    private String navigationWarningMessage;
    private OperationEnum anyKeyPressed;
    private CommandActions cmds;
    private List nvTables;
    private DataRecords dataRecords;
    private boolean isManualBeforeImageValue;
    private boolean constructing = true;
    private String debug_name; //So JProfiler will see it
    private static int times = 0;
    private static int times2 = 0;
    private static int times3 = 0;
    private static int timesSet = 0;
    private static int indexTimes = 0;
    private static boolean validationOff = false;
    static int constructedTimes;
    public int id;
    
    private static final boolean DEBUG = false;
    private static final boolean ORIG_WAY_REFRESH = true;

    public Block(ProdNodeI node,
                 OperationsProcessor oper,
                 MasterDetailTiesManager tm,
                 Publisher navigationPublisher,
                 Publisher nodePublisher,
                 Object tableControl,
                 NodeGroup nodeGroup,
                 boolean isManualBeforeImageValue)
    {
        child = new NullBlock();
        dataRecords = new DataRecords( node.getName(), nodeGroup);
        this.node = node;
        if(tm != null)
        {
            if(!tm.gotAllTies())
            {
                /*
                * makesTies requires the hierarchy to be there before the
                * first block is created
                */
                Err.error(
                    "TiesManager must have all Nodes before a Block can be created");
            }
            this.tm = tm;
        }
        else
        {
            Err.error("AbstBlock cannot be constructed with a null TiesManager");
        }
        this.tableControl = tableControl;
        this.navigationPublisher = navigationPublisher;
        this.nodePublisher = nodePublisher;
        this.oper = oper;
        this.cmds = node.getCommandActions();
        this.isManualBeforeImageValue = isManualBeforeImageValue;
        //Is being done when consumeNodesIntoRT, so no need to repeat
        //oper.setCurrentBlock(this);
        setState(BlockStateCreator.newInstance( oper, node));
        // Trying to do later
        // currentState = FreezingState.getNewInstance( oper, this);
        //
        /*
        setDynamicAllowed( node.isUpdate(), CapabilityEnum.UPDATE);
        setDynamicAllowed( node.isEnterQuery(), OperationEnum.ENTER_QUERY);
        setDynamicAllowed( node.isExecuteQuery(), OperationEnum.EXECUTE_QUERY);
        setDynamicAllowed( node.isExecuteSearch(), OperationEnum.EXECUTE_SEARCH);
        setDynamicAllowed( node.isInsert(), OperationEnum.INSERT);
        setDynamicAllowed( node.isRemove(), OperationEnum.REMOVE);
        setDynamicAllowed( node.isPost(), OperationEnum.POST);
        setDynamicAllowed( node.isCommitOnly(), OperationEnum.COMMIT_ONLY);
        setDynamicAllowed( node.isCommitReload(), OperationEnum.COMMIT_RELOAD);
        setDynamicAllowed( node.isPrevious(), OperationEnum.PREVIOUS);
        setDynamicAllowed( node.isNext(), OperationEnum.NEXT);
        //Err.pr( ")))) new block from " + node.getName() +
        //        " where isSetRow() is " + node.isSetRow());
        setDynamicAllowed( node.isSetRow(), OperationEnum.SET_ROW);
        setDynamicAllowed( node.isEditInsertsBeforeCommit(), CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT);
        setDynamicAllowed( node.isBlankRecord(), CapabilityEnum.BLANK_RECORD);
        setDynamicAllowed( node.isCascadeDelete(), CapabilityEnum.CASCADE_DELETE);
        setDynamicAllowed( node.isFocusCausesNodeChange(), CapabilityEnum.FOCUS_NODE);
        */
        cmds.setDynamicAllowed();
        constructedTimes++;
        id = constructedTimes;
        /*
        Err.pr( "Created itemAdapter with dataFieldName " + dataFieldName);
        Err.pr( "Created itemAdapter with displayName " + displayName);
        Err.pr( "Created itemAdapter with id " + id);
        */
        if(id == 0)
        {
            Err.debug();
        }
        /**/
        Err.pr( SdzNote.SET_AND_CREATE_BLOCKS, "###CONSTRUCTING NEW " + node.getName() + " block with id " + id);
        if(id == 7)
        {
            //Err.stack();
        }
        /**/
        this.debug_name = node.getName();
        constructing = false;
    }

    public CombinationExtent getCombinationExtent()
    {
        return dataRecords.getCombinationExtent();
    }

    public void setCombinationExtent(CombinationExtent combinationExtent)
    {
        dataRecords.setCombinationExtent( combinationExtent);
    }

    public NodeGroup getNodeGroup()
    {
        return dataRecords.getNodeGroup();
    }

    /**
     * Used to restore Block back to the state it once was in. Making it visually apparent
     * is done straight after this call.
     */
    public void setBlockMemento(BlockMemento bm)
    {
        cmds = bm.getState();
    }

    /**
     * Used to take a copy of the current state, that another object
     * (OperationsProcessor) may keep it, and pass it back again.
     */
    public BlockMemento createBlockMemento()
    {
        /*
        * Following line does a deep copy
        * of the state that is passed to it.
        */
        return new Block.BlockMemento(cmds);
    }

    public boolean equals(Object o)
    {
        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Block))
        {
            result = false;
        }
        else
        {
            Block test = (Block) o;
            Assert.notNull( test.getNode(), "block doing equals with has no node: " + test.getId());
            Assert.notNull( node, "block doing equals on has no node: " + this.getId());
            result = node.equals(test.getNode());
        }
        return result;
    }

    public int hashCode()
    {
        int result = 37 * node.hashCode();
        return result;
    }

    public void setCreatableForField(
        // probably doesn't need to be a CreatableInterface, Creatable
        // would be fine - but better like this as shows we only need
        // a small part of Creatable
        CreatableI creatable)
    {
        if(tableControl == null)
        {
            recordObj = Builder.createRecordObj(IdEnum.FIELD, null, this, creatable,
                null);
        }
        else
        {
            Err.error();
        }
    }

    public void setNodeTableModelForTable( NodeTableModelImpl ntmii)
    {
        this.ntmii = ntmii;
    }

    public void setCreatableForTable(
        CreatableI creatable,
        NodeTableMethods model,
        FieldObj tableObj)
    {
        if(tableControl == null)
        {
            Err.error();
        }
        else
        {
            // oper.addStateChangeTrigger( btmi);
//            NodeTableMethods model = Block.setupTable(tableControl, btmi,
//                (AbstNodeTable) getProdNodeI());
//            FieldObj tableObj = Builder.createTableObj(null, this, creatable, btmi, entityManager);
//            btmi.setTableObj(tableObj);
            // also acts as the real model:
            //Need done earlier 
            TableSignatures.setModel(tableControl, model);
            recordObj = tableObj;
        }
    }

    public List getNvTables()
    {
        return nvTables;
    }

    public void setNvTables(List nvTables)
    {
        this.nvTables = nvTables;
    }

    public OperationEnum getCurrentOperation()
    {
        return oper.getCurrentOperation();
    }

    public NodeTableModelImplI getNodeTableModelImplI()
    {
        return ntmii;
    }

    public NodeTableModelImpl getNodeTableModelImpl()
    {
        return ntmii;
    }

    /**
     * The class the user writes will need the NodeTableModelI
     */
    public NodeTableModelI getTableModel()
    {
        return ntmii;
    }

    public NodeTableModelI getNonVisualTableModel(int idx)
    {
        /*
        NodeTableModelI result = null;
        TableComp table = (TableComp)nvTables.get( idx);
        TableCompModelImpl model = (TableCompModelImpl)table.getModel();
        result = model.getNodeTableModel();
        if(result == btmi)
        {
          Err.error( "Whats the point?");
        }
        return result;
        */
        return ntmii;
    }

    public boolean isInserted()
    {
        return isAnInsert;
    }

    /**
     * This method does not read a database list, so can be used after
     * the values have been written away to the database.
     */
    public void closedStateOKConfirmed()
    {
        dataRecords.emptyInsertedList();
        // Err.pr( "*** dataRecords.emptyInsertedList()");
        // times++;
        // Err.pr( "dataRecords closedStateOKConfirmed" + times);
        isAnInsert = false;
    }

    public String toString()
    {
        String result = null;
        if(recordObj != null)
        {
            result = "block " + recordObj.toString();
        }
        else
        {
            result = getName();
        }
        return result + ", ID: " + getId();
    }

    public Ties getMasterTies()
    {
        return ties;
    }

    public VisibleExtent getDataRecords()
    {
        return dataRecords.getDataRecords();
    }

    public void setDataRecords(VisibleExtent extent)
    {
        dataRecords.setDataRecords( extent);
    }

    public void setMasterTies(Ties ties)
    {
        this.ties = ties;
        Err.pr(SdzNote.TIES, "### ties been set for " + this);
        if(ties.isEmptyOfTies())
        {
            //only after makeTies do they become non-empty
            //Err.error( "Did not expect ties to be empty");
        }
    }

    /**
     * Every time nodes are passed thru the ties and the
     * composite structure are re-read. Structure is re-done by
     * calling makeBabies() for every newly created block. All children
     * of each block are used to re-create the composite structure.
     * <p/>
     * Loop is for every child that this block has. As may be many children a NullBlock
     * may be converted and a Composite created as iterate
     * multiple times. <code>add()</code> called on CompositeBlock but not
     * NullBlock or Block.
     * <p/>
     * Flexibility of Builder pattern not required, as the types of the blocks in
     * the composite pattern are not going to vary.
     */
    public void makeBabies( MasterDetailTiesManager tm)
    {
        /*
        * Given this internal Block, for each child that has been set up for it,
        * call adapterAdd and get the composite pattern going. 'children' refers
        * to the physical links that have been set up b/ween Nodes by the user.
        */
        //Assert.notNull( tm, "block ID: " + id + " does not have a tm");
        for(Iterator e = tm.getChildren(this); e.hasNext();)
        {
            Block detailAdapter = (Block) e.next();
            if(detailAdapter == null)
            {
                Err.error("Didn't expect detailAdapter to be null");
            }
            else
            {
                if(child instanceof org.strandz.core.prod.CompositeBlock)
                {
                    ((CompositeBlock) child).add(detailAdapter);
                }
                else if(child instanceof org.strandz.core.prod.NullBlock)
                {
                    child = detailAdapter;
                }
                else
                {
                    AbstBlock singleAdapter = child;
                    child = new CompositeBlock();
                    Err.pr( SdzNote.ADD_SAME_BLOCK_TWICE, "Complete hack fix here:");
                    if(singleAdapter.getId() != detailAdapter.getId())
                    {
                        ((CompositeBlock) child).add(singleAdapter);
                        ((CompositeBlock) child).add(detailAdapter);
                    }
                    else
                    {
                        ((CompositeBlock) child).add(detailAdapter);
                    }
                }
            }
        }
    }

    /*
    * isAnInsert being updated when syncDetail will work for all the times
    * when 'move around'. When commit or post syncDetail will not be called.
    * syncDetail SHOULD BE CALLED FOR post and commit
    * As last
    * operation may have been an insert, we will have to call isInserted
    * here.
    */
    public void visualCursorChange()
    {// Err.pr( "Doing visualCursorChange in " + this);
        /*
        Remove for now
        isAnInsert = dataRecords.isInserted( getVisualEleIndexZeroDelimited());
        */ // setDisplayEditable();
        /*
        if(getVisualEleIndexZeroDelimited() == -1)
        {
        Err.error("eleIndex can be null in visualCursorChange when anyadding");
        }
        if(getVisualEleIndexZeroDelimited() != -1)
        {
        if(howDo == VisualCursorChangeParams.SET_AND_DO ||
        howDo == VisualCursorChangeParams.SET_ONLY)
        {
        isAnInsert = dataRecords.isInserted(
        getVisualEleIndexZeroDelimited());
        }
        if(howDo == VisualCursorChangeParams.DO_IT_IF_SHOULD ||
        howDo == VisualCursorChangeParams.SET_AND_DO)
        {
        setUIEditable();
        }
        }
        */}

    public void firePostingEvent(Object source, OperationEnum op, StateEnum origState)
    {
        // Err.stack();
        node.firePostingEvent(source, op, origState);
    }

    public void generateNavigationEvent(final OperationEnum operationEnum, final StateEnum origState)
    {
        if(!operationEnum.isOperation())
        {
            Err.error("generateNavigationEvent s/take an OperationEnum, not " + operationEnum);
        }
        navigationPublisher.publish
            (
                new Publisher.Distributor()
                {
                    public void deliverTo(Object subscriber)
                    {
                        NavigationTrigger nl = (NavigationTrigger)subscriber;
                        nl.navigated(new OperationEvent(node, operationEnum, origState));
                    }
                }
            );
        /*
        for(Iterator e = navigationListenerList.iterator(); e.hasNext();)
        {
          NavigationTrigger nl = (NavigationTrigger)e.next();
          nl.navigated( new OperationEvent( node, operationEnum));
        }
        */
    }

    /**
     * Now post only done when user POSTs or COMMITs. Thus always starts from the
     * topmost level.
     * OLD:
     * When are doing this from prev/next action, will not be going up to topmost
     * levels, as is done by oper.post. This is like the true post
     * as it is called no matter what the user is doing (up/down/post/commit).
     */
    public void post(OperationEnum key)
    {
        times++;
        Err.pr( SdzNote.SYNC, "In post() for " + this + " times " + times);
        getState().anyKeyPressed(oper, key, this, fieldsChanged());
        child.post(key);
    }

    /**
     * See if there's a complaint about moving away from this
     * record. Encompasses defaultsPartOfBlank property.
     * If defaultsPartOfBlank == true then there is no way a
     * default record will be able to be added to the database,
     * just as there's no way a default record can be added to
     * the database.
     * <p/>
     * defaultsPartOfBlank too complex for now (or ever)! Better
     * solution if really want to implement this is to have both a
     * way of entering (per Node) what will be interpretted as blank,
     * and what will come out as default.
     * <p/>
     * TODO - FIX BUG. Certainly in a JTable, sometimes when commit (therefore post too)
     * anyComplaints is not being called. Is this still really true?
     * Yes - TestTable.testCanMoveFromInvalidChild
     */
    public boolean anyComplaints(StateEnum potentialState)
    {
        anyComplaintsMsg.clear();

        /*
        times++;
        Err.pr( SdzNote.generic, "Inside anyComplaints for " + this + " times " + times);
        if (times == 1)
        {
          Err.debug();
        }
        */
        // Err.pr( "In anyComplaints, recordValidationOutcome is " + getRecordValidationOutcome());
        boolean result = false;
        /*
        Err.pr( "Blank record allowed " +
          isAllowed( OperationEnum.BLANK_RECORD) + " for " +
          this);
        */
        if(!validationOff && oper.isValidationRequired())
        {
            if(anyKeyPressed != OperationEnum.REMOVE
                && anyKeyPressed != OperationEnum.EXECUTE_QUERY)
            {
                boolean allowedBlank = isAllowed(CapabilityEnum.BLANK_RECORD);
                boolean displayBlank = recordObj.displayIsBlank();
                // boolean onCurrentRow = moveBlock.getMoveManager().onCurrentRow();
                getMoveBlock().setRecordValidationOutcome(true);
                if(!allowedBlank && displayBlank/* && onCurrentRow*/ && getRowCount() > 0)
                {
                    //Err.pr( SdzNote.generic, "displayIsBlank() for block: " + this);
                    anyComplaintsMsg.add(MsgUtils.RECORD_IS_BLANK);
                    //Err.stack();
                    oper.changeStateToNoMove(potentialState, this);
                    result = true;
                }
                else
                {
                    /*
                    Err.pr( "Was not blank, or blanks ok");
                    Err.pr( "Is blank:" + recordObj.UIIsBlank());
                    Err.pr( "blanks allowed:" + isAllowed( InputControllerEvent.BLANK_RECORD));
                    */
                    // 16/07/04 Not sure we need this here if it is in AnyAdding and Navigating
                    // getProdNodeI().firePostingEvent( getProdNodeI(), anyKeyPressed);
                    getProdNodeI().fireRecordValidateEvent();
                }
            }
        }
        /*
        * This was ok when returning T/F, but is not ok for when user
        * raises the exception
        if(result)
        {
        //Err.pr( "recordValidationOutcome in anyComplaints:=FALSE");
        setRecordValidationOutcome( false);
        }
        else
        {
        //Err.pr( "recordValidationOutcome in anyComplaints:=TRUE");
        setRecordValidationOutcome( true);
        }
        */
        return result;
    }

    public List retrieveAnyComplaintsMsg()
    {
        return anyComplaintsMsg;
    }

    public void zeroIndex()
    {
        //Err.pr( "############### In zeroIndex for " + this);
        setIndex(false, -1);
        // Err.pr( "############### , so dataRecordsSize is " + dataRecordsSize());
        child.zeroIndex();
    }
    
    public void displayLovObjects()
    {
        recordObj.displayLovObjects();
        child.displayLovObjects();
    }

    public void internalBlankoutDisplay( OperationEnum currentOperation, String reason, boolean removeComboItems)
    {
        recordObj.blankoutDisplay( currentOperation, reason, Utils.UNSET_INT, removeComboItems);
        if(ntmii != null)
        {
            ntmii.blankoutDisplay( currentOperation, "blankoutDisplay from Block where have a tableModel because " + reason);
        }  
        else
        {
        }
    }

    public void internalBlankoutDisplay( OperationEnum currentOperation, String reason, int row, boolean removeComboItems)
    {
        //Err.pr( "blank at row " + row + ", yet max is " + getRowCount());
        //if(row == Utils.UNSET_INT || (row != -1 && row < getRowCount()))
        {
            recordObj.blankoutDisplay( currentOperation, reason, row, removeComboItems);
            if(ntmii != null)
            {
                ntmii.blankoutDisplay( currentOperation, row, "blankoutDisplay from Block where have a tableModel because " + reason);
            }  
            else
            {
            }
        }
    }

    public void blankoutDisplay(OperationEnum currentOperation, String reason, boolean removeComboItems)
    {
        recordObj.blankoutDisplay( currentOperation, reason, Utils.UNSET_INT, removeComboItems);
        if(ntmii != null)
        {
            ntmii.blankoutDisplay( currentOperation, "blankoutDisplay from Block where have a tableModel because " + reason);
        }        
        else
        {
        }
        child.blankoutDisplay( currentOperation, "blankoutDisplay on child because " + reason, removeComboItems);
    }
    
    public void detachData( boolean detach)
    {
        recordObj.detachData( detach);
        child.detachData( detach);
    }

    /*
    public int getState()
    {
    int result = currentState.getState( oper);
    Err.pr( "For " + this + " state being GOT is " +
    Constants.stringValue( result));
    return result;
    }
    */

    public BlockState getState()
    {
        // Err.pr( "For " + this + " state being GOT is " + currentState);
        return currentState;
    }

    public void setState(BlockState adapterState)
    {
        currentState = adapterState;
        /*
        times++;
        Err.pr("For " + this + " state has been set to " +
            currentState + " times " + times);
        if (times == 0)
        {
            Err.stack();
        }
        */
    }
    
    public void syncAllDetails(OperationEnum currentOperation, boolean removeComboItems)
    {
        internalBlankoutDisplay( currentOperation, "internalSyncDetail", 0, removeComboItems);
        //for(int i = dataRecords.size() - 1; i >= 0; i--)        
        for(int i = 0; i < dataRecords.size(); i++)
        {
            syncDetail( currentOperation, i, removeComboItems, false);
        }
    }

    public void syncDetail(OperationEnum currentOperation, boolean removeComboItems)
    {
        syncDetail( currentOperation, getIndex(), removeComboItems, true);
    }

    public void syncDetail(OperationEnum currentOperation, int idx, boolean removeComboItems, boolean blank)
    {
        StopWatch stopWatch = new StopWatch();
        stopWatch.startTiming();
        Assert.notNull( tm, "No tm in block ID: " + getId());
        tm.recursingSyncDetail = false;
        visualCursorChange();

        /*
        int currentRow = getRow();
        times3++;
        Err.pr("\tAbout to sync on row " + currentRow + " whilst in " +
                currentState.getState() +
                " block: " + getName() + " times " + times3);
        if(times3 == 2)
        {
            Err.debug();
        }
        */
        /*
         * Idea here is that you might be on a child block and an item change you have
         * made affects the master - for instance the master might have a total in it.
         * Currently in this situation the master will not be synced. With this code
         * we are making sure that syncing (which calls setDisplay()) is always done from 
         * the topmost down. It worked for the situation described but caused a problem
         * in that thereafter NEXT/PREVIOUS had no effect. Rather than solve this problem
         * right now I decided it was more urgent to have a manual REFRESH operation that
         * does the opposite of POST, except that rather than being for a Strand it is
         * only for a Node - it is simpler and also I have the feeling that syncing is
         * an expensive operation.
        for(Iterator<Block> iterator = getOldestAncestors().iterator(); iterator.hasNext();)
        {
            Block block = iterator.next();
            block.internalSyncDetail(currentOperation);
        }
        */
        internalSyncDetail( currentOperation, true, idx, removeComboItems);
        stopWatch.stopTiming();
        if(stopWatch.getResult() > 0)
        {
//            Err.pr( "syncDetail took: " + stopWatch.getElapsedTimeStr() + " for " + idx);
//            refreshTimes++;
//            if(refreshTimes == 0)
//            {
//                Err.stack();
//            }
        }
    }

    /**
     * Method getName.
     *
     * @return String
     */
    public String getName()
    {
        return node.getName();
    }

    public boolean dataRecordsEmpty()
    {
        return dataRecords.dataRecordsEmpty();
    }

    public boolean dataRecordsNull()
    {
        return dataRecords.dataRecordsNull();
    }

    public void insertDataRecord(int index, Object obj)
    {
        dataRecords.insertDataRecord( index, obj);
    }

    /**
     * Does more than syncDetail, represents current, then brings
     * details' dataRecords and representation into line with it.
     */
    public void internalSyncDetail(OperationEnum currentOperation, boolean childrenToo, 
                                   int idx, boolean removeComboItems)
    {
        /*
         * Following if state used to make REFRESH more robust but should not be needed
         * long term. When turn this into an Assert make sure have capability to turn
         * asserting off with a local file in user's home directory - is that the way
         * Spring does it?
         */
        if(ORIG_WAY_REFRESH || (idx == Utils.UNSET_INT || 
                (idx != -1 && idx < getRowCount()) || (idx == -1 && currentOperation == OperationEnum.REMOVE)))
        {        
            if(ORIG_WAY_REFRESH)
            {
                Err.pr( SdzNote.SAFE_REFRESH, "To do ORIG_WAY_REFRESH for operation " + currentOperation + 
                        " with index at " + idx + " and rowCount is " + getRowCount() + " in <" + getName() + ">");
                if(currentOperation == OperationEnum.REMOVE && idx == -1)
                {
                    //Err.stack();
                }
                else if(currentOperation == OperationEnum.PREVIOUS && idx == -1)
                {
                    //Err.stack();
                }
            }
            else
            {
                Err.pr( SdzNote.SAFE_REFRESH, "Doing refresh for operation " + currentOperation + " with index at " + idx + " and rowCount is " + getRowCount());
            }
            internalBlankoutDisplay( currentOperation, "internalSyncDetail", idx, removeComboItems);
            // this method is recursive anyway, so they will
            // all get blanked out in time
            if(!dataRecordsEmpty())
            {
                // Err.pr( "^^^ getIndex (NOT EMPTY) gives " + getIndex() + " in block " + this + " times " + times);
                Err.pr( SdzNote.BI_AI, "dataRecords NOT Empty() in <" + node.getName() + ">");
                Err.pr( SdzNote.INVOKE_WRONG_FIELD, "To setDisplay from <" + node.getName() + ">");
//                if(node.getName().equals( "Group Node") || node.getName().equals( "Company Node"))
//                {
//                    Err.debug( SdzNote.INVOKE_WRONG_FIELD.toString());
//                }
                recordObj.setDisplay( currentOperation, idx, dataRecords.get( idx), false, true, "internalSyncDetail");                    
                if(ntmii != null)
                {
                    times++;
                    String txt = "times " + times + " setDisplay from " + node.getName() + " where have a tableModel";
                    if(times == 0)
                    {
                        Err.debug();
                    }
                    Err.pr( SdzNote.SET_DISPLAY_ON_TABLE, txt);
                    ntmii.setDisplay( currentOperation, idx, txt);
                }
                else
                {
                }
                recordObj.getAdapters().getCalculationPlace().fireCalculationFromSync( 
                    recordObj.getAdapters().getId(), dataRecords.get( idx), idx);
            }
            else
            {
                Err.pr( SdzNote.BI_AI, "dataRecordsEmpty() in <" + node.getName() + "> in " + 
                        "<" + node.getStrandDebugInfo() + ">");
                recordObj.setDisplay( currentOperation, idx, null, false, true, "internalSyncDetail");
                // Err.pr( "^^^ getIndex (EMPTY) gives " + idx + " in block " + this + " times " + times);
            }
        }
        else
        {
            Err.pr( "Avoiding bug for operation " + currentOperation + " b/c index is " + idx + 
                    " and rowCount is " + getRowCount() + " in " + getName());
            childrenToo = false;
        }
        /*
        if(times == 0)
        {
        Err.stack();
        }
        */
        // Whilst a master is given a ArrayList to work with and this does not
        // change, the detail ArrayList has to work with a diff dataRecords
        // each time master's currIndex is changed
        /*
        if(child instanceof NullBlock)
        {
        Err.pr( "##  child of " + this + " is a null block");
        }
        else
        {
        Err.pr( "##  child of " + this + " is a real block: " + child);
        }
        */
        if(childrenToo)
        {
            if(!dataRecordsEmpty())
            {
                /*
                Err.pr( "## setDataRecords for child of " + getName() + " from index " + index);
                Err.pr( dataRecords);
                Err.pr( dataRecords.get( index));
                */
                Err.pr(SdzNote.CTV_STRANGE_LOADING, "####To syncDetail on " + child);
                child.setDataRecords(dataRecords.get( idx), this);
                if(!child.dataRecordsEmpty())
                {
                    Err.pr(SdzNote.CTV_STRANGE_LOADING, "## first dataRecord for child has become " + child.getDataRecord( 0));
                }
            }
            else
            {
                // Err.pr( "### dataRecordsEmpty() for " + this + ", so will ** NOW NOT ** deleteToBottom()");
                child.blankoutDisplay(currentOperation, 
                                      "blankoutDisplay on child when " + currentOperation, removeComboItems);
                freezeToBottom();
                child.setDataRecords(null, this);
                return;
            }
            if(this == child)
            {
                Err.error("ridic error");
            }
            tm.recursingSyncDetail = true;
            child.internalSyncDetail(currentOperation, true, child.getIndex(), removeComboItems);
        }
    }

    /**
     * Always called on the child, and then calls itself
     * recursively. Called when the last record is deleted.
     * Each child contains its current list. Normally with
     * syncDetail we are swapping these about because the
     * master record is changing. Thus from a child's point
     * of view a delete of the master is similar to a setRow
     * of the master. One time we don't want the child
     * to have any records is when we have deleted the last
     * master record. We catch this circumstance here and
     * delete the whole list of every child.
     * Other time need to call is at end of an insert when
     * actually create a row.
     */
    public void deleteToBottom(boolean visual, boolean removeComboItems)
    {
        if(visual)
        {
            internalBlankoutDisplay(OperationEnum.UNKNOWN, "deleteToBottom", removeComboItems);
        }
        if(!dataRecordsNull() && // cascadeDelete
            cmds.get(CapabilityEnum.CASCADE_DELETE).isBlockAllowed()
            )
        {
            /*
            times++;
            Print.pr( "*** In nullToBottom for " + this + " times " + times);
            if(times == 0)
            {
            Err.stack();
            }
            Err.pr( "*** REM size " + dataRecords.size());
            Err.pr( "*** REM " + dataRecords);
            */
            for(int i = dataRecords.size() - 1; i >= 0; i--)
            {
                dataRecords.removeDataRecord(i);
                // This line in 29/07/2003, to make cascade deleting the default.
                child.deleteToBottom(true, removeComboItems);
            }
        }
        // Print.pr( "*** To recurse to " + child);
        child.deleteToBottom(visual, removeComboItems);
    }

    public void freezeToBottom()
    {
        // Print.pr( "*** In freezeToBottom for " + this);
        oper.changeState( // FreezingState.getNewInstance( oper, this),
            BlockStateCreator.newInstance(oper, StateEnum.FROZEN), this);
        setDisplayEditable(false);
        child.freezeToBottom();
    }

    public boolean fieldsChanged()
    {
        if(id == 2)
        {
            // Err.debug();
            recordObj.debugBlock = true;
        }

        boolean result = recordObj.haveFieldsChanged();
        recordObj.debugBlock = false;
        return result;
    }

    /**
     * applyDifferences() is NOT recursive 'cause what calls anyKeyPressed() is.
     * This is done whilst in Edit mode and previous or next
     * has decided the current record has changed, or when
     * post() is called, or whenever mode is changed - triggered
     * by anyKeyPressed
     */
    public void applyDifferences()
    {
        /*
        times++;
        Err.pr( "-------------------------- Block.applyDifferences() times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        // if(!getNode().isIgnoreValidation())
        if(dataRecords.size() > 0)
        {
            Object obj = dataRecords.get(getIndex());
            /*
            data.wombatrescue.objects.Volunteer vol = null;
            if(obj instanceof data.wombatrescue.objects.Volunteer)
            {
            vol = (data.wombatrescue.objects.Volunteer)obj;
            Err.pr( "@@@@@@@@@ To applyDifferences() to " + vol + " for block " + this);
            }
            */
            recordObj.getDisplay(obj, true);
            /*
            if(vol != null)
            {
            Err.pr( "@@@@@@@@@, Has become: " + vol + ", ID: " + vol.id);
            }
            */
        }
        else
        {
            if(getIndex() >= 0)
            {
                Err.pr( "There are no data records yet index is " + getIndex());
            }
        }
    }

    public void completeAdd(boolean isPrior)
    {
        // if(!getNode().isIgnoreValidation())
        {
            Err.pr(SdzNote.BG_ADD, "-------------------------- Block.completeAdd() in " + this);
            recordObj.backgroundAdd(isPrior);
            /* Causes trouble for our better (CTV) table. Row change ought to have happened when you
               pressed the insert button - which is now the case for a table. For a non-table we
               keep the code as is - waiting until user moves away/posts to increment.
             */
            /*
            if(ntmii == null)
            {
                if(!isPrior)
                {
                    incCursor(false); // 01/03/02 Must have for table to work. Now used by both
                }
            }
            */
            /**/
            /*
            Err.pr( CMLBug.incrementWhenAdd, "############### rowCount now " +  dataRecords.size() + " for " + this);
            Err.pr( CMLBug.incrementWhenAdd, "############### index now " +  getIndex() + " for " + this);
            Err.pr( CMLBug.incrementWhenAdd, "############### row now at " +  getRow() + " for " + this);
            */
            /*
            * S/not need to do these two when coming out of a row that was
            * adding. They should have been zeroed and nulled (synced) when
            * first pressed insert.
            */
            // child.zeroIndex();
            // child.nullToBottom( false);
        }
    }

    public void postCompleteAdd( OperationEnum currentOperation)
    {
        recordObj.completeBackgroundAdd( currentOperation, getIndex());
    }

    public boolean validateNode()
    {
        validateNodeMsg.clear();
        try
        {
            node.fireNodeValidateEvent();
        }
        catch(ApplicationError e)
        {
            validateNodeMsg.addAll(e.getMsg());
            return false;
        }
        return true;
    }

    public List retrieveValidateNodeMsg()
    {
        return validateNodeMsg;
    }

    /*
    public void setDynamicAllowed( boolean b,  CapabilityEnum capability)
    {
    //    if(capability == CapabilityEnum.EXECUTE_SEARCH
    //       //&& getName() != null
    //       //&& getName().equals( "promotion Reference Detail Node")
    //       )
    //    {
    //	    times++;
    //	    Err.pr( times + "=== In setAllowed for " + this + " setting capability " +
    //	            capability + " to " + b);
    //	    if(times == 0)
    //	    {
    //	      //Err.stack();
    //	    }
    //    }
    if(capability == OperationEnum.EXECUTE_QUERY)
    executeLoad = b;
    else if(capability == OperationEnum.EXECUTE_SEARCH)
    executeSearch = b;
    else if(capability == OperationEnum.ENTER_QUERY)
    enterQuery = b;
    else if(capability == OperationEnum.INSERT)
    insert = b;
    else if(capability == OperationEnum.REMOVE)
    delete = b;
    else if(capability == OperationEnum.POST)
    post = b;
    else if(capability == OperationEnum.COMMIT_ONLY)
    commitOnly = b;
    else if(capability == OperationEnum.COMMIT_RELOAD)
    commitReload = b;
    else if(capability == OperationEnum.PREVIOUS)
    up = b;
    else if(capability == OperationEnum.NEXT)
    down = b;
    else if(capability == OperationEnum.SET_ROW)
    setRow = b;
    else if(capability == CapabilityEnum.UPDATE)
    update = b;
    else if(capability == CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT)
    editInsertsBeforeCommit = b;
    else if(capability == CapabilityEnum.BLANK_RECORD)
    blankRecord = b;
    else if(capability == CapabilityEnum.IGNORED_CHILD)
    ignoredChild = b;
    else if(capability == CapabilityEnum.CASCADE_DELETE)
    cascadeDelete = b;
    else if(capability == CapabilityEnum.FOCUS_NODE)
    focusCausesNodeChange = b;
    else
    {
    Err.error("Block.setAllowed cannot process OperationEnum/CapabilityEnum: " + capability);
    }
    }
    */

    public boolean isAllowed(CapabilityEnum capability)
    {
        return cmds.get(capability).isBlockAllowed();
        /*
        boolean result = false;
        if(capability.equals( OperationEnum.EXECUTE_QUERY))
        result = executeLoad;
        else if(capability.equals( OperationEnum.EXECUTE_SEARCH))
        result = executeSearch;
        else if(capability.equals( OperationEnum.ENTER_QUERY))
        result = enterQuery;
        else if(capability.equals( OperationEnum.INSERT))
        result = insert;
        else if(capability.equals( OperationEnum.REMOVE))
        result = delete;
        else if(capability.equals( OperationEnum.POST))
        result = post;
        else if(capability.equals( OperationEnum.COMMIT_ONLY))
        result = commitOnly;
        else if(capability.equals( OperationEnum.COMMIT_RELOAD))
        result = commitReload;
        else if(capability.equals( OperationEnum.PREVIOUS))
        result = up;
        else if(capability.equals( OperationEnum.NEXT))
        result = down;
        else if(capability.equals( OperationEnum.SET_ROW))
        result = setRow;
        else if(capability == CapabilityEnum.UPDATE)
        result = update;
        else if(capability == CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT)
        result = editInsertsBeforeCommit;
        else if(capability == CapabilityEnum.BLANK_RECORD)
        result = blankRecord;
        else if(capability == CapabilityEnum.IGNORED_CHILD)
        result = ignoredChild;
        else if(capability == CapabilityEnum.CASCADE_DELETE)
        result = cascadeDelete;
        else if(capability == CapabilityEnum.FOCUS_NODE)
        result = focusCausesNodeChange;
        else
        {
        Err.error("Block.isAllowed cannot process OperationEnum/CapabilityEnum: " +
        capability);
        }
        return result;
        */
    }

    /**
     * Always called on the children, never the parent. Actual dataRecords that
     * end up with will be a combination of listVectors from all the parents.
     * <p/>
     * Is now being called as part of post. This this case we are not just
     * physically navigating thru the master record, but may have hit the "commit" whilst
     * physically on the child. In this case no reason to change eleIndex.
     * <p/>
     * 06/10/2001
     * Experimenting with idea that this only needs to be called for RELOAD.
     * Is currently also called for START and POST.
     * Don't think s/be called at all for POST or START.
     */
    void setDataRecords(Object masterElement,
                        AbstBlock parentBlock)
    {
        StopWatch stopWatch = new StopWatch();
        stopWatch.startTiming();
        if(!tm.gotAllTies())
        {
            Err.error("Must have all ties before setDataRecords for a detail");
        }

        // Err.pr("^^ inside default setDataRecords() for " + this);
        // if(masterElement != null)
        // {
        /**/
        times3++;
        Err.pr( SdzNote.CTV_STRANGE_LOADING, "*** In setDataRecords for " + this + " will use master " +
                masterElement + " times " + times3);
        if(SdzNote.CTV_STRANGE_LOADING.isVisible() && times3 == 2)
        {
            Err.debug();
        }
        /**/
        /*
        * Don't need assignment here. This is NOT where the Master/Detail
        * world becomes the Lookup world. This is ONLY about M/D.
        */
        if(ties == null)
        {
            Err.error("ties == null for block ID " + id);
        }
        StopWatch stopWatch2 = new StopWatch();
        stopWatch2.startTiming();
        DEErrorContainer errCon = ties.setNewListVector(masterElement, parentBlock);
        stopWatch2.stopTiming();
        Err.pr( SdzNote.PERFORMANCE_TUNING.isVisible() && stopWatch2.getResult() > 30, "setNewListVector on block <" + this + 
                "> using DO <" + masterElement + "> took " + stopWatch2.getElapsedTimeStr());
        if(errCon != null)
        {
            Err.pr(SdzNote.FILL_DATA_FOR_DISCONNECTED_NODES,
                "We won't always want to error here");

            Block childBlock = (Block) errCon.getChildCausingError();
            Err.error(
                errCon.toString()
//          "Have not called setData() on the child Node called <"
//              + childBlock.getName() + ">"
            );
        }
        if(dataRecordsEmpty())
        {
            // timesSet++;
            // Err.pr( "$$ setDataRecords did not work with NULL extent");
            setIndex(false, -1);
        }
        else
        {
            // Print.pr( "$$ setDataRecords worked with id " + dataRecords.id);
            // have just created a dataRecords, as a map to what is already there
            setIndex(false, 0);
        }
        // generateNavigationEvent( StateEnum.FROZEN); //FROZEN to compile
        // }
        // else
        // {
        // Err.error("no longer called for null");
        // }
        stopWatch.stopTiming();
        if(SdzNote.PERFORMANCE_TUNING.isVisible() && stopWatch.getResult() > 3)
        {
            Err.pr( "setDataRecords on <" + this + "> took " + stopWatch.getElapsedTimeStr());
        }
    }

    public Object getDataRecord(int idx)
    {
        return dataRecords.getDataRecord( idx);
    }

    public int dataRecordsSize()
    {
        return dataRecords.dataRecordsSize();
    }
    
    public void removeDataRecord( int idx)
    {
        dataRecords.removeDataRecord( idx);
    }

    public Iterator getParentsIterator( MasterDetailTiesManager tm)
    {
        Iterator result = null;
        if(Utils.isClassOfType(tm.getEnforceType(), ProdNodeI.class))
        {
            result = tm.getParentsIterator(getProdNodeI());
        }
        else if(tm.getEnforceType() == AbstBlock.class)
        {
            result = tm.getParentsIterator(this);
        }
        else
        {
            Err.error("Cannot talk a TiesManager of enforced type: " + tm.getEnforceType());
        }
        return result;
    }
    
    public List<Block> getParentsList( MasterDetailTiesManager tm)
    {
        List<Block> result = new ArrayList<Block>();
        for(Iterator iter = getParentsIterator( tm); iter.hasNext();)
        {
            Block parentBlock = (Block) iter.next();
            if(parentBlock != null)
            {
                result.add( parentBlock);
            }
            else
            {
                Session.error("parentBlock only being set with data");
            }
        }
        return result;    
    }
    
    public List<Block> getOldestAncestors( MasterDetailTiesManager tm)
    {
        List<Block> result = new ArrayList<Block>();
        List<Block> parentsList = getParentsList( tm);
        if(parentsList.isEmpty())
        {
            result.addAll( Utils.formList( this));    
        }
        else
        {
            for(Iterator iterator = parentsList.iterator(); iterator.hasNext();)
            {
                Block parentBlock = (Block) iterator.next();
                result.addAll( parentBlock.getOldestAncestors( tm));
            }
        }
        return result;
    }

    public boolean decCursor(boolean physical)
    {
        if(index <= 0)
        {
            if(physical)
            {
                navigationWarningMessage = Constants.NOT_BACKWARD;
                return false;
            }
            else
            {// no prob - happens when remove
            }
        }
        Err.pr( SdzNote.INDEX, "[[Decing eleIndex, from " + index + " in " + this.toString());
        setIndex(false, index - 1);
        return true;
    }

    public boolean incCursor(boolean physical)
    {
        // Err.pr( "getIndex() rets: " + getIndex());
        // Err.pr( "dataRecords.size() rets: " + dataRecords.size());
        if(index >= dataRecords.size() - 1)
        {
            if(physical)
            {
                navigationWarningMessage = Constants.NOT_FORWARD;
                // Err.stack();
                return false;
            }
            else
            {// nufin
            }
        }
        // Err.pr("[[Incing eleIndex, from " + index + " in " + this.toString());
        setIndex(false, index + 1);
        return true;
    }
    
    public void adjustIndex( OperationEnum op)
    {
        int index = getIndex();
        //int cursor = getRow();
        int max = getRowCount();
        // Err.pr( "$$ cursor in remove " + cursor);
        // Err.pr( "$$ max in remove " + max);
        /*
        if(!(idx == 0 && size > 0))
        {
        context.getCurrentBlock().decCursor( false);
        }
        */
        if(op == OperationEnum.REMOVE)
        {
            if(max == 1 && index == 0) // 2nd follows
            {
                zeroIndex(); // recursive
                // Err.pr( "Zeroing to bottom b4 syncDetail, but NOT changing state");
            }
        }
        else
        {
            // Err.pr( "BACK SENSIBLE, INDEX: " + index);
            if(index == -1)
            {
                /*
                 * Has come from a freezing state that when we started adding records to
                 * it, it was changed into an adding state. Here post or the next insert
                 * is being done, so we will give it a sensible cursor position. (This
                 * normally happens with execute query).
                 */
                /*
                 * Became general function when decided that doing REFRESH when
                 * frozen is ok. Will be in frozen state after a COMMIT_ONLY
                 */
                if(!dataRecordsNull())
                {
                    setIndex(true, 0);
                }
            }
        }
    }
    
    public boolean setIndex(boolean physical, int i)
    {
        /**/
        SdzNote.INDEX.incTimes();
        if(!dataRecords.dataRecordsNull())
        {
            Err.pr( SdzNote.INDEX, "@@ Setting index to " + i +
                " where have " + dataRecords.size() + " for " + this + " times " + times);
            /*
            //if(SdzNote.INDEX.getTimes() == 3)
            if(i == 1)
            {
                Err.stack();
            }
            */
        }
        /**/
        if(physical)
        {
            if(dataRecords.dataRecordsNull())
            {
                Err.error( "On <" + dataRecords.getName() + "> block ID: " + getId() + ", have not called setCombinationExtent()");
            }
            else
            {
                //Err.pr( "On <" + dataRecords.getName() + "> block ID: " + getId() + ", about to setIndex to " + i);
            }
            if(i >= dataRecords.size())
            {
                String text = "Can't go past record " + dataRecords.size() + " (trying "
                    + i + ") for " + this;
                if(DEBUG)
                {
                    Print.prCollection( dataRecords.getDataRecords(), "Records must remain within"); 
                    Err.stack( text);
                }
                navigationWarningMessage = text;
                return false;
            }
            else if(i < 0)
            {
                String text = "Can't go before the first record (trying " + i + ")";
                if(DEBUG)
                {
                    Err.stack( text);
                }
                navigationWarningMessage = text;
                return false;
            }
        }
        index = i;
        /*
        indexTimes++;
        Err.pr( "============= index set to " + index + " in block " + this +
        " times " + indexTimes);
        if(indexTimes == 0)
        {
        Err.stack();
        }
        */
        return true;
    }

    public String getNavigationWarningMessage()
    {
        return navigationWarningMessage;
    }

    public int getIndex()
    {
        // Err.pr( "^^^ index getting " + index + " from " + this);
        /*
         * We might have multiple blocks representing the same data. There is nothing wrong
         * with the indexes on these blocks being different. The only problem will be if
         * the data has become smaller than the current index - in which case we just make
         * it the biggest possible.
         */
        int rowCount = getRowCount();
        if((oper.getCurrentOperation() == null || oper.getCurrentOperation() != OperationEnum.INSERT_AFTER_PLACE) && index >= rowCount)
        {
            index = rowCount-1;
        }
        return index;
    }

    /**
     * Cursor Position is always one greater than index whilst user is
     * in Adding state. Like index, it starts at 0.
     */
    /*
    public int getRow()
    {
        int result = getIndex();
        if(oper.getCurrentBlock() == this)
        {
            StateEnum enumId = oper.getState(this);
            if(enumId.isNew() && !enumId.isPrior())
            {
//                result++;
//                times++;
//                Err.pr( "getCursorPosition() whilst in new will be " + result +
//                " for block " + this + " times " + times);
//                if(times == 0)
//                {
//                Err.stack();
//                }
            }
            else
            {
//         times++;
//         Err.pr( "getCursorPosition() whilst OUT OF new will be " + result +
//         " for block " + this + " times " + times);
//         if(times == 0)
//         {
//         Err.stack();
//         }
            }
        }
        if(result < -1)
        {
            Err.error("Negative row, row on " + this + " is " + result);
        }
        return result;
    }
    */

    /**
     * Whilst the state of NEW is not block specific, you are only ever in
     * isNew() for the current block. Thus since the addition of current this
     * method will be accurate for both current and non-current blocks.
     */
    public int getRowCount()
    {
        int result = dataRecords.dataRecordsSize();
        if(oper.getCurrentBlock() == this && oper.getState(this).isNew())
        {
            result++;
            // Err.pr( "getMaxCursorPosition() whilst in new will be " + result);
        }
        else
        {// Err.pr( "getMaxCursorPosition() whilst NOT in new will be " + result);
        }
        return result;
    }

    /**
     * Called straight after we have determined whether the eleIndex we
     * are at isAnInsert or not, unless user aborted the transaction.
     */
    void setDisplayEditable()
    {
        if(!isAllowed(CapabilityEnum.UPDATE) && !isInserted())
        {
            recordObj.setDisplayEnabled(false);
        }
        else // either an insert or it's allowed, or both.
        {
            if(isInserted() && !isAllowed(CapabilityEnum.UPDATE)
                && !isAllowed(CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT))
            {
                recordObj.setDisplayEnabled(false);
            }
            else
            {
                recordObj.setDisplayEnabled(true);
            }
        }
    }

    public void setDisplayEditable(boolean editable)
    {
        if(recordObj != null)
        {
            recordObj.setDisplayEnabled(editable);
        }
        else
        {
            if(!constructing)
            {
                Err.error();
            }
        }
    }

    public void setFreshDataRecords()
    {
        dataRecords.setFreshDataRecords();
        child.setFreshDataRecords();
    }

    /*
    protected boolean modeIsAdd( StateEnum mode)
    {
    if(mode != StateEnum.NEW)
    return false;
    else
    return true;
    }
    */
    public boolean isChildOf(AbstBlock isParent)
    {
        ArrayList manyThis = new ArrayList();
        manyThis.add(this);

        MutableInteger levelsMoved = new MutableInteger(0);
        return tm.isChildOf(isParent, manyThis, levelsMoved);
    }

    /**
     * Is this a child of the passed
     * in argument
     */
    public boolean isDescendantOf(AbstBlock isParent, MutableInteger levelsMoved)
    {
        ArrayList manyThis = new ArrayList();
        manyThis.add( this);
        return tm.isChildOf(isParent, manyThis, levelsMoved);
    }

    public boolean isDescendantOf(AbstBlock isParent)
    {
        return isDescendantOf(isParent, new MutableInteger(20));
    }

    public TiesManager getTiesManager()
    {
        return tm;
    }

    public Iterator iterator()
    {
        return new BlockCompositeEnumerator(this);
    }

    public void setDataRecordsNull()
    {
        dataRecords.setDataRecordsNull();
        child.setDataRecordsNull();
    }

    public void reviveNulledDataRecords()
    {
        dataRecords.reviveNulledDataRecords();
        child.reviveNulledDataRecords();
    }

    public static NodeTableMethods setupTable(
        Object tableControl, NodeTableModelImpl nodeTableModelImpl, AbstNodeTable node)
    {
        /*
        * Perhaps slightly contorted here, but this method is called right after
        * a block has been created in consumeNodesIntoRT. Right when focus on a Node
        * and it must be visible.
        */
        // Err.pr( "Inside setupTable() for " + tableControl);
        NodeTableMethods tableModel = null;
        if(tableControl != null)
        {
            if(SdzNote.DYNAM_ATTRIBUTES.isVisible())
            {
                Err.pr( "Table and node");
                ComponentTableView table = (ComponentTableView)tableControl;
                Err.pr( "\ttable is <" + table.getName() + ">, with ID <" + table.getId() + ">");
                Err.pr( "\tnode: " + node + ", with ID " + node.getId());
                Err.pr( "\tNodeTableModelImpl: " + nodeTableModelImpl);
            }
            try
            {
                TableSignatures.checkTableControlExists(tableControl.getClass());
            }
            catch(UnknownControlException ex)
            {
                Err.error(ex.toString());
            }

            /*
            * Can now construct the model that we will use, and add it
            * to the table we have been given.
            */
            Class staticModel = TableSignatures.getTableModel(tableControl);
            // new MessageDlg("model will instantiate is " + staticModel);
            // Check that class is more than just an interface, and thus can be
            // instantiated:
            if(Modifier.isInterface(staticModel.getModifiers()))
            {
                Err.error(
                    "Model should not be an interface, but a concrete instantiation");
            }
            tableModel = (NodeTableMethods) ObjectFoundryUtils.factory(staticModel);
            if(tableModel == null)
            {
                Err.error("Model s/not be null");
            }
            nodeTableModelImpl.setUsersModel( tableModel);
            tableModel.setNode( node, null); // this line b4 setModel
            // Err.pr( "MM done setNode");
            /*
            * This is done automatically when set model for the control
            * model.indepAddTableModelListener( tableControl);
            */
            tableModel.oneRowSelectionOn( tableControl); // TODO - remove param to support many
            // TableView will go to all listeners
        }
        return tableModel;
    }

    /*
    public Adapter getLastAdapter()
    {
    if(lastAdapter == null)
    {
    Err.error( "Asking for lastField when there is none");
    }
    return lastAdapter;
    }
    */

    public String toStringRepresentation()
    {
        String result;
        Object last = moveBlock.getMoveTracker().getPreviousAdapterReleased().getItemName();
        if(last == null)
        {
            result = "Last field is NULL (error condition)";
        }
        else if(!(last instanceof Component))
        {
            result = "Last field is " + last;
        }
        else
        {
            Component lastComp = (Component) ((FieldItemAdapter) moveBlock.getMoveTracker().getPreviousAdapterReleased()).getItem();
            result = "Last field is " + lastComp.getName();
        }
        return result;
    }

    public MoveNodeI getMoveNodeI()
    {
        return node;
    }

    public ProdNodeI getProdNodeI()
    {
        return node;
    }

    public Object getTableControl()
    {
        return tableControl;
    }

    /**
     * The tableObject is attached to the Node, and the node usually lasts forever in the program.
     * Blocks are re-created frequently (in consumeNodesIntoRT()). When a block is created so too
     * are the models that lie behind the tableObject. Doing this breaks the link, and stops a leak.
     */
    public void nullTableControl()
    {
        if(ntmii != null)
        {
            ntmii.nullBlock();
            ntmii = null;
        }
        tableControl = null;
    }

    /**
     * If don't do this then CTV.ControlFocuser still has a reference, and this block
     * won't be GCed.
     */
    /*
    public void nullForGarbageCollection( boolean removeNodesPresentAsTies)
    {
        Err.pr( "About to null for GC, block with ID: " + id);
        if(getTableControl() != null)
        {
            ntmii.nullBlock();
            ntmii = null;
            tableControl = null;
        }
        if(removeNodesPresentAsTies)
        {
            tm.purgeOldNode( getNode());
        }
        node = null;
        tm = null;
    }
    */

    public void fireNodeDefaultEvent(DataFlowEnum eventType)
    {
        final DataFlowEvent evt = new DataFlowEvent(eventType, getNode());
        // never used
        // evt.setNode( node);
        /*
        for(Iterator e = nodeListenerList.iterator(); e.hasNext();)
        {
          NodeDefaultTrigger bl = (NodeDefaultTrigger)e.next();
          bl.nodeChange( evt);
        }
        */
        nodePublisher.publish
            (
                new Publisher.Distributor()
                {
                    public void deliverTo(Object subscriber)
                    {
                        NodeDefaultTrigger ndt = (NodeDefaultTrigger) subscriber;
                        ndt.nodeChange(evt);
                    }
                }
            );
    }

    public boolean isIgnoreValidation()
    {
        return getProdNodeI().isIgnoreValidation();
    }

    /*
    public void throwApplicationError( String error, ApplicationErrorEnum type)
    {
    Session.throwApplicationError( error, type, getNode().isIgnoreValidation());
    }
    */
    // public void setState() {
    // }

    /*
    public SubRecordObj getRecordObj() {
    return recordObj;
    }
    */
    /*
    public List getAllAdapters()
    {
    return recordObj.getAdapters().getAdapters();
    }
    */
    public List getVisualAdaptersArrayList()
    {
        return recordObj.getVisualAdaptersArrayList();
    }

    public List getAdaptersArrayList()
    {
        List result = Utils.EMPTY_ARRAYLIST;
        if(!constructing)
        {
            result = recordObj.getAdaptersArrayList();
        }
        return result;
    }

    /*
    public void setRecordObj(SubRecordObj recordObj) {
    this.recordObj = recordObj;
    }
    */
    
    public CalculationPlace getCalculationPlace()
    {
        return recordObj.getAdapters().getCalculationPlace();
    }

    public MoveBlockI getMoveBlock()
    {
        return moveBlock;
    }

    public void setMoveBlock(MoveBlockI moveBlock)
    {
        this.moveBlock = moveBlock;
    }

    public void setAnyKeyPressed(OperationEnum key)
    {
        this.anyKeyPressed = key;
    }

    public OperationEnum getAnyKeyPressed()
    {
        return anyKeyPressed;
    }

    public void flushAllTies(MasterDetailTiesManager btm)
    {
        ties = null;
        tm = btm;
        Err.pr(SdzNote.TIES, "### ties been flushed for block ID " + id);
    }

    public CommandActions getCommandActions()
    {
        return cmds;
    }

    public int getId()
    {
        int result = id;
        if(constructing)
        {
            result = -99;
        }
        return result;
    }

    public boolean isManualBeforeImageValue()
    {
        return isManualBeforeImageValue;
    }

    public void adoptOutBaby(Block block)
    {
        if(block == this)
        {
            Err.pr("Need to have a NullBlock instead of " + this);
        }
    }

    public boolean isConstructing()
    {
        return constructing;
    }

    public ItemAdapter getFirstItemAdapter()
    {
        ItemAdapter result = recordObj.getAdapters().getFirstVisualItemAdapter();
        if(result != null)
        {
            result.setOriginalAdapter( result);
            Err.pr( SdzNote.FIRST_ITEM_FALLBACK, 
                    "FirstItemAdapter returning from getFirstItemAdapter() is " + result.getId());
        }
        return result;
    }

//    public CombinationExtent getCombinationExtent()
//    {
//        return dataRecords.getCombinationExtent();
//    }

    /**
     * If it is not being finalized when you expect it to be, then it is leaking
     */
    protected void finalize()
    {
         Err.pr( SdzNote.SET_AND_CREATE_BLOCKS, "block <" + this.getNode().getName() + "> ID: " + id + " finalized");
    }

    class BlockMemento
    {
        private CommandActions state;

        public String toString()
        {
            return state.toString();
        }

        BlockMemento(CommandActions state)
        {
            this.state = new CommandActions(state); // way of doing a deep copy
            /*
            if(this.state.isEmpty())
            {
              Err.error( "It is unlikely that a block will have no CommandActions");
            }
            */
        }

        private CommandActions getState()
        {
            return state;
        }
    } // end BlockMemento class
} // end class


final class BlockCompositeEnumerator implements Iterator
{
    Block block;
    int count;

    BlockCompositeEnumerator(Block b)
    {
        block = b;
        count = 0;
    }

    public boolean hasNext()
    {
        return count < 1;
    }

    public java.lang.Object next()
    {
        if(count < 1)
        {
            count++;
            return block;
        }
        Err.error("Called next in Block's enumerator too many times");
        return null; // for compiler
    }

    public void remove()
    {
        Err.error("remove not implemented for BlockCompositeEnumerator");
    }
}
