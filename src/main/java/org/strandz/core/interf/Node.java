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
package org.strandz.core.interf;

import org.strandz.core.domain.AbstNodeTable;
import org.strandz.core.domain.CalculationPlace;
import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.ErrorThrowerI;
import org.strandz.core.domain.Independent;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.MoveBlockI;
import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.domain.NodeI;
import org.strandz.core.domain.NodeTableModelI;
import org.strandz.core.domain.RecorderI;
import org.strandz.core.domain.SdzBeanI;
import org.strandz.core.domain.TableSignatures;
import org.strandz.core.domain.UnknownControlException;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.DataFlowEnum;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.ControlActionListener;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.DynamicAllowedEvent;
import org.strandz.core.domain.event.NavigationTrigger;
import org.strandz.core.domain.event.NodeDefaultTrigger;
import org.strandz.core.domain.event.NodeValidationEvent;
import org.strandz.core.domain.event.NodeValidationTrigger;
import org.strandz.core.domain.event.OperationEvent;
import org.strandz.core.domain.event.PostingTrigger;
import org.strandz.core.domain.event.RecordValidationEvent;
import org.strandz.core.domain.event.RecordValidationTrigger;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.core.prod.Block;
import org.strandz.core.prod.CommandActions;
import org.strandz.core.prod.Constants;
import org.strandz.core.prod.ProdNodeI;
import org.strandz.core.prod.Session;
import org.strandz.core.prod.BlockStateCreator;
import org.strandz.core.prod.BlockState;
import org.strandz.core.prod.move.ErrorSiteEnum;
import org.strandz.core.prod.move.MoveBlock;
import org.strandz.core.prod.move.MoveNodeI;
import org.strandz.lgpl.extent.ChildExtentGetterI;
import org.strandz.lgpl.extent.NodeGroup;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.NamedI;
import org.strandz.lgpl.util.Publisher;
import org.strandz.lgpl.util.StopWatch;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.widgets.IconEnum;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a record. This record is a conflation of a record you
 * might see on a screen, and a record that is an instance of a Data Object. Nodes
 * can be joined to one another via master/detail relationships. A node is made up
 * of a group of cells, and these cells are in turn made up of a group of attributes.
 * Both the cells and the attributes can be accessed from this class. A node has
 * capabilities, many of which are operations that can be executed. For example using
 * a node you can programmatically go to the next record.
 *
 * @author Chris Murphy
 */
public class Node extends AbstNodeTable
    implements
    NodeI, ProdNodeI, MoveNodeI,
    Serializable, SdzBeanI, ChildExtentGetterI, NamedI
{
    private List validateBeanMsg = new ArrayList();
    // No effect on what can be performed programmatically:
    //
    // Not setable by user:
    private boolean externalLoad = true;
    // End No effect section.
    private List abilities;
    //
    private List independents = new ArrayList();
    private ArrayList tempTies = new ArrayList();
    private Cell cell;
    private String title;
    private ControlActionListener controlActionListener;
    private RecordValidationTrigger recordValidationListener;
    private PostingTrigger postingListener;
    private NodeValidationTrigger nodeValidationListener;
    private Publisher navigationPublisher = new Publisher();
    private Publisher nodeDefaultPublisher = new Publisher();
    private Publisher dataFlowPublisher = new Publisher();
    private Block block;
    private Object tableControl;
    private String name;
    private boolean alreadyBeenCustomized = false;
    private OperationEnum executionType;
    private int currentOrdinal = 0;
    private CommandActions cmds = new CommandActions();
    private NodeGroup nodeGroup; 
    private transient boolean dataDetached;
    private transient AbstStrand strand = new NullStrand();
    private transient PropertyChangeSupport propChangeSupp;
    private transient Node outer;

    private static final transient IconEnum NODE_ICON = IconEnum.BLUE_CIRCLE;

    private static int refreshTimes;
    private static int timesAdded;
    static int constructedTimes;
    private int id;

    public IconEnum getIconEnum()
    {
        return NODE_ICON;
    }

    public Node()
    {
        // Err.pr( "Creating node " + hashCode());
        // For disabling controller buttons:
        // Oper doing this now
        // addNavigationListener( this);
        propChangeSupp = new PropertyChangeSupport(this);
        constructedTimes++;
        id = constructedTimes;
        outer = this;
        cmds.setNodeDefaults();
        addNavigationTrigger(new OwnNavigationT());
    }

    public void set(Node node)
    {
        setName(node.getName());
        setTitle(node.getTitle());
        setCell(node.getCell());
        setTableControl(node.getTableControl());
        setEnterQuery(node.isEnterQuery());
        setExecuteQuery(node.isExecuteQuery());
        setExecuteSearch(node.isExecuteSearch());
        setUpdate(node.isUpdate());
        setInsert(node.isInsert());
        setRemove(node.isRemove());
        setPost(node.isPost());
        setCommitReload(node.isCommitReload());
        setCommitOnly(node.isCommitOnly());
        setPrevious(node.isPrevious());
        setNext(node.isNext());
        setSetRow(node.isSetRow());
        setIgnoredChild(node.isIgnoredChild());
        setCascadeDelete(node.isCascadeDelete());
        setFocusCausesNodeChange(node.isFocusCausesNodeChange());
        setEditInsertsBeforeCommit(node.isEditInsertsBeforeCommit());
        setAlreadyBeenCustomized(node.isAlreadyBeenCustomized());
        setIndependents(node.getIndependents());
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, SdzBeanI.class);
        return equals(o, true);
    }

    private boolean equals(Object o, boolean onIndependents)
    {
        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Node))
        {
            result = false;
        }
        else
        {
            result = false;

            Node test = (Node) o;
            if(name == null ? test.name == null : name.equals(test.name))
            {
                if((cell == null ? test.cell == null : cell.equals(test.cell)))
                {
                    result = true;
                }
                else
                {// Err.pr("cell comparison failed: " + cell + " with " + test.cell);
                }
            }
            else
            {// Err.pr("name comparison failed: " + name + " with " + test.name);
            }
            if(result == true)
            {
                if(independents.size() == test.independents.size())
                {
                    if(onIndependents)
                    {
                        for(int i = 0; i <= independents.size() - 1; i++)
                        {
                            Independent independent = (Independent) independents.get(i);
                            Independent testIndependent = (Independent) test.independents.get(
                                i);
                            if(!independent.equals(testIndependent))
                            {
                                result = false;
                                break;
                            }
                        }
                    }
                }
                else
                {
                    result = false;
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (name == null ? 0 : name.hashCode());
        result = 37 * result + (cell == null ? 0 : cell.hashCode());
        result = 37 * independents.hashCode();
        // for(int i = 0; i <= independents.size() - 1; i++)
        // {
        // Independent independent = (Independent)independents.get( i);
        // result = 37 * result + independent.hashCode();
        // }
        // Err.pr( "node " + this + " returning hashCode of " + result);
        return result;
    }

    public void setDefaults(String controllerName)
    {
        // executeQuery = true;
        // executeSearch = true;
        // insert = true;
        // remove = true;
        // post = true;
        // commitOnly = false;
        // previous = true;
        // next = true;
        // setRow = true;
        // cascadeDelete = true;
        // focusCausesNodeChange = true;
        // cmds.setNodeDefaults();
        // Err.pr( ")))) in Node.setDefaults() and setRow is now " +
        // setRow + " for " + getName());
        if(controllerName != null)
        {
            setName( controllerName + " Node");
        }
        else
        {
            setName( NodeUtils.DEFAULT_NAME);
        }
    }

    public void setCell(Cell cell)
    {
        if(cell != null)
        {
            cell.setNode(this);
            cell.setChief(true);
        }
        this.cell = cell;
    }

    public Cell getCell()
    {
        return cell;
    }

    /*
    public List getTheLookupCells()
    {
    List result = getTheCells();
    result.remove( 0);
    return result;
    }
    */
    public List getCells()
    {
        List result = new ArrayList();
        if(cell != null)
        {
            result = getTheCells(cell, result);
        }
        return result;
    }

    private List getTheCells(Cell cell, List cells)
    {
        cells.add(cell);

        Cell lookups[] = cell.getCells();
        for(int i = 0; i < lookups.length; i++)
        {
            cells = getTheCells(lookups[i], cells);
        }
        return cells;
    }

    public Independent getIndependent(int index)
    {
        return (Independent) independents.get(index);
    }

    public Independent[] getIndependents()
    {
        return (Independent[]) independents.toArray(
            new Independent[independents.size()]);
    }

    public boolean containsIndependent(Independent independent)
    {
        // indexOf will use equals(), contains didn't seem to
        // return (independents.indexOf( independent) != -1);
        // indexOf not work either so do it ourselves:
        boolean result = false;
        // Err.pr( "containsIndependent() with " + independents.size() + " for " + getName());
        for(Iterator iter = independents.iterator(); iter.hasNext();)
        {
            Independent indep = (Independent) iter.next();
            if(indep.equals(independent))
            {
                result = true;
                break;
            }
        }
        // Err.pr( "containsIndependent() returning " + result);
        return result;
    }

    public void setIndependent(int index, Independent independent)
    {
        // Err.pr( "@@@ In setIndependent for " + this +
        // " with " + independent + " at " + index);
        /*
        if(alreadyCalledSetIndependent)
        {
        Session.error("TEST THIS NOW - addIndependent(): only 1 independent currently allowed per node");
        }
        */
        if(independent == null)
        {
            Session.error("Use removeIndependent() instead");
        }
        if(independent.getMasterNode() == null
            || independent.getMasterOrDetailField() == null
            )
        {
            Session.error("setIndependent(): all parameters must be NOT null");
        }
        /*
        List list = new ArrayList();
        list.add( independent);
        createTiesFromIndependents( list, this);
        */
        // alreadyCalledSetIndependent = true; //check that only 1
        if(independents.size() == index)
        {
            independents.add(index, independent);
        }
        else
        {
            independents.set(index, independent);
        }
    }

    public void setIndependents(Independent is[])
    {
        // Err.pr( "@@@ In setIndependents for " + this +
        // " with " + is);
        independents.clear();
        for(int i = 0; i < is.length; i++)
        {
            setIndependent(i, is[i]);
        }
    }

    // node.getTies().clear();
    /*
    public boolean removeIndependent( Independent independent)
    {
    boolean ok = independents.remove( independent);
    if(!ok)
    {
    Session.error( "Was unable to remove: " + independent + " from " + this);
    }
    else
    {
    node.getTies().remove( independent.getTie());
    }
    return ok;
    }
    */

    /**
     * Not depreciated, but way of not having to remember
     * the current index. Is not part of the methods used
     * when XML encoding being done.
     */
    public void addIndependent(Independent independent)
    {
        int lastIndependent = getIndependents().length;
        setIndependent(lastIndependent, independent);
    }

    public boolean removeIndependent(Independent independent)
    {
        /*
        times++;
        Err.pr( "Removing independent: " + independent + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        boolean result = independents.remove(independent);
        return result;
    }

    /**
     * Would expect the user to create subclasses of AbstractAction
     * which have an actionPerformed method (and descriptions and
     * icons etc). This abilities will then be visible (perhaps as
     * buttons) on whatever is the physical embodiment of the
     * NodeController.
     */
    public void setAbilities(List abilities)
    {
        if(SdzNote.ACTIONS_REQUIRED_ON_TOOL_BAR.isVisible())
        {
            //Make List above typed to List<Action>
            Utils.chkTypes( abilities, Action.class);
        }
        this.abilities = abilities;
        if(block != null)
        {
            strand.dynamicAbilities(this, abilities);
        }
    }

    public List getAbilities()
    {
        return abilities;
    }

    public AbstractAction getAbilityByName(String name)
    {
        AbstractAction result = null;
        for(Iterator iter = abilities.iterator(); iter.hasNext();)
        {
            AbstractAction action = (AbstractAction) iter.next();
            String propValue = (String) action.getValue(Action.NAME);
            if(propValue.equals(name))
            {
                result = action;
                break;
            }
        }
        return result;
    }

    public void fireRecordValidateEvent()
    {
        if(recordValidationListener != null && !isIgnoreValidation())
        {
            // Done earlier, as now works for internal as well
            // block.getMoveBlock().setRecordValidationOutcome( true);
            RecordValidationEvent evt = new RecordValidationEvent();
            // evt.setNode(this);
            // Err.pr( "fireRecordValidateEvent in " + this);
            try
            {
                recordValidationListener.validateRecord(evt);
            }
            catch(ValidationException ex)
            {
                Session.getErrorThrower().throwApplicationError(ex, null,
                    ApplicationErrorEnum.RECORD_VALIDATION);
            }
        }
    }

    public void firePostingEvent(Object source, OperationEnum op, StateEnum origState)
    {
        Assert.notNull( origState);
        if(getState().isNew())
        {
            Err.error(
                "Should not still be in a NEW state when POSTing event goes out");
        }
        if(postingListener != null)
        {
            OperationEvent evt = new OperationEvent(source, op, origState);
            postingListener.posted(evt);
        }
        if(origState.isNew())
        {
            recordCurrentValue( "Done background add", Utils.UNSET_INT, origState, op);
        }
    }

    public void fireNodeValidateEvent()
    {
        if(nodeValidationListener != null && !isIgnoreValidation())
        {
            NodeValidationEvent evt = new NodeValidationEvent();
            // evt.setNode(this);
            try
            {
                nodeValidationListener.validateNode(evt);
            }
            catch(ValidationException ex)
            {
                Session.getErrorThrower().throwApplicationError(ex, null,
                    ApplicationErrorEnum.NODE_VALIDATION);
            }
        }
    }

    public Block getBlock()
    {
        /*
        if(block == null)
        {
        Session.error("Why has Node.block been set to null?");
        }
        */
        return block;
    }

    public void setBlock(Block block)
    {
        // Err.pr( this + " will now have a block with ID: " + block.id);
        if(block == null)
        {
            Session.error("Cannot set Node.block null");
        }
        this.block = block;
    }

    void setBlockNull()
    {
        if(block != null)
        {
            Err.pr( SdzNote.SET_AND_CREATE_BLOCKS, "Making block null, ID: " + block.getId());
            //block.nullForGarbageCollection( true);
            block.nullTableControl();
            block = null;
        }
        if(nodeGroup != null)
        {
            nodeGroup.setAggregationExtent( null);
            nodeGroup.setCombinationExtent( null);
        }
    }

    public NodeTableModelI getNonVisualTableModel(int idx)
    {
        if(block == null)
        {
            Session.error("Too soon to call getTableModel( idx)");
        }
        return block.getNonVisualTableModel(idx);
    }

    public NodeTableModelI getTableModel()
    {
        if(block == null)
        {
            Session.error("Too soon to call getTableModel");
        }
        return block.getTableModel();
    }

    /*
    public void goAsBefore()
    {
    if(block == null)
    {
    Session.error("Too soon to call goAsBefore");
    }
    Session.getMoveManager().goAsBefore();
    }
    */

    public String toStringWithIds()
    {
        String blockId = "";
        if(block != null)
        {
            blockId = ", id:" + block.id;
        }
        if(cell != null)
        {
            return "[Node cell: <" + cell.toString() + ">]" + " ID:" + id + blockId;
        }
        else
        {
            return "[Node " + super.toString() + "]" + " ID:" + id + blockId;
        }
    }

    public String toString()
    {
        if(cell != null)
        {
            return "[Node cell: <" + cell.toString() + ">]";
        }
        else
        {
            return "[Node " + super.toString() + "]";
        }
    }

    public String getDefaultName()
    {
        String result = toString();
        {
            // Session.error( "Node not been given name " + this);
            if(cell != null)
            {
                result = NameUtils.spaceOutClassName(result);
            }
        }
        return result + " Node";
    }

    /**
     * If a name has not been specified then if a cell
     * exists will create something nice looking out of it.
     */
    public String getName()
    {
        return name;
    }

    public void changeState( StateEnum newState)
    {
        //Err.pr( "POST_QUERY, can't we set <" + getName() + "> UN-frozen?");
        //Err.pr( "Verify FROZEN: " + strand.getOPor().getCurrentState( getBlock()));
        strand.getOPor().changeState( BlockStateCreator.newInstance( strand.getOPor(), newState), getBlock());
        BlockState state = strand.getOPor().getCurrentState( getBlock());
        //Err.pr( "State changed to " + state + " for <" + getBlock().getNode().getName() + ">");
    }

    /**
     * Put these in for Node and Cell when 'getting into' design time.
     */
    public void setName(String name)
    {
        this.name = name;
        // Err.pr( "node's name set to " + name);
    }

    /* REFRESH is better name
    public void SYNC()
    {
        Err.pr( SdzNote.SYNC, "To SYNC in " + this);
        block.internalSyncDetail( null, false);
        Err.pr( SdzNote.SYNC, "Done SYNC");
    }
    */

    /**
     * At design-time user will have to make sure that this method is
     * called from the intended focus event. For instance when focus
     * on a text field, or a node in a tree, or a tab. This can be done
     * automatically if use higher-level Beans such as SimpleVisibleStrand
     * or TabbedVisibleStrand.
     * <p/>
     * If a frame does not have the focus, and then give a control within the
     * frame the focus, focus is first gained by the "TAB 1" control, and then
     * gained by the control where you put the cursor. Strange behaviour.
     * Strand.goNode() will ignore a change to the already current
     * block.
     */
    public boolean GOTO()
    {
        return surroundGoTo(false);
    }

    public void REFRESH()
    {
        StopWatch stopWatch = new StopWatch();
        stopWatch.startTiming();
        //For example if have done COMMIT_ONLY then want to REFRESH() (rather than COMMIT_RELOAD)
        block.adjustIndex( OperationEnum.REFRESH);
        int rowCount = block.getRowCount();
        if(block.getTableControl() != null)
        {
            //There is a bug where delete a roster slot. Could not repeat
            //See C://temp//bug_delete_rs.txt
            /*
            Err.pr( "To do refresh and rowCount of " + block + " is " + rowCount + ", has table " +
                    ((JComponent)block.getTableControl()).getName());
            Err.pr( "curr row before refresh: " + getRow());
            */
        }
        for(int i = 0; i < rowCount; i++)
        {
            if(i != getRow())
            {
                Err.pr( SdzNote.SET_TEXT_BLANK, "Going to refresh at row " + i + " for " + block);
                block.syncDetail( OperationEnum.REFRESH, i, Constants.REMOVE_COMBO_ITEMS, true);
            }
        }
        if(rowCount == 0)
        {
            block.syncDetail( OperationEnum.REFRESH, -1, Constants.REMOVE_COMBO_ITEMS, true);
        }
        else if(getRow() != -1)
        {
            block.syncDetail( OperationEnum.REFRESH, getRow(), Constants.REMOVE_COMBO_ITEMS, true);
        }
        //Err.pr( "curr row after refresh: " + getRow());
        stopWatch.stopTiming();
        if(SdzNote.REFRESH.isVisible())
        {
            if(stopWatch.getResult() > 0)
            {
                Err.pr( "REFRESH took: " + stopWatch.getElapsedTimeStr() + " for " + this);
                refreshTimes++;
                if(refreshTimes == 0)
                {
                    Err.stack();
                }
            }
        }
    }
    
    public CalculationPlace getCalculationPlace()
    {
        return block.getCalculationPlace();    
    }

    public boolean GOTOWithoutValidation()
    {
        boolean ok = surroundGoTo(true);
        if(!ok)
        {
            //Why this strange? You wouldn't call this method if you didn't
            //expect this
            //Session.error( "Strange that goToWithoutValidation() not ok");
        }
        Session.getMoveManager().forgetWhereCameFrom();
        return ok;
    }

    private boolean surroundGoTo(boolean ignoreValidation)
    {
        boolean result = true;
        if(this != strand.getCurrentNode())
        {
            if(!ignoreValidation)
            {
                ItemAdapter firstItemAdapter = null;
                if(block != null)
                {
                    firstItemAdapter = block.getFirstItemAdapter();
                }
                Session.getMoveManager().enter( firstItemAdapter, EntrySiteEnum.GOTO);
            }
            else
            {
                Session.getMoveManager().enterWithoutValidation(null,
                    EntrySiteEnum.GOTO);
            }
            // Want to keep the actual error coming back at the user
            /*
            *
            */
            if(Session.getMoveManager().readyNextStep())
            {
                result = goTo(ignoreValidation);
            }
            else
            {
                result = false;
            }
            Session.getMoveManager().exitEnter();
        }
        else
        {
            Err.error("Cannot node.GOTO() when already there, use strand.getCurrentNode() to check not already on " + this);
        }
        return result;
    }

    public void execute(OperationEnum op)
    {
        if(op == OperationEnum.GOTO_NODE)
        {
            goTo(false);
        }
        else if(op == OperationEnum.GOTO_NODE_IGNORE)
        {
            goTo(true);
        }
        else
        {
            getStrand().execute(op);
        }
    }

    boolean goTo(boolean ignoreValidation)
    {
        boolean ok = true;
        boolean fromItemLayer = false;
        Session.getMoveManager().createNewValidationContext();

        ApplicationError error = null;
        OperationEnum op = null;
        try
        {
            if(!ignoreValidation)
            {
                op = OperationEnum.GOTO_NODE;
            }
            else
            {
                op = OperationEnum.GOTO_NODE_IGNORE;
            }
            Session.getMoveManager().errorSite(ErrorSiteEnum.GOTO, op);
            Session.getRecorder().openRecordOperation(op, getName());
            goToEXCEPT(ignoreValidation);
            Session.getRecorder().closeRecordOperation();
            Session.getMoveManager().b4HandleErrorProcessing(null,
                (MoveBlockI) getMoveBlock(), op);
        }
        catch(ApplicationError err)
        {
            if(Session.getMoveManager().error(ErrorSiteEnum.GOTO))
            {
                // Throw it again b/cause this is an inner ApplicationError catcher.
                throw err;
            }
            error = err;
            ok = false;
            Session.getMoveManager().b4HandleErrorProcessing(err,
                (MoveBlockI) getMoveBlock(), op);
            strand.fireErrorHandler(err);
            Err.pr(SdzNote.NODE_VALIDATION,
                ">>>>>EXIT from fireErrorHandler in Node.goTo, so won't get to "
                    + this);
        }
        finally
        {
            postGoToProcessing(ok, error);
        }
        return ok;
    }

    private void postGoToProcessing(
        boolean success,
        ApplicationError err
    )
    {
        Session.getMoveManager().postValidationProcessing(success, err);
        strand.setAlreadyPerformingGoNode(false);
    }

    private void goToEXCEPT(boolean withoutValidation)
    {
        if(!(strand instanceof NullStrand))
        {
            strand.setIgnoreValidation(withoutValidation);
            strand.goNode(this); // setNode
            strand.setIgnoreValidation(false);
            //generateNodeAccess( true); // OPEN
        }
        else
        {
            Session.error(
                "Cannot goNode() to node " + this + " until setStrand() called on it");
        }
    }

    public int getRowCount()
    {
        int result = 0;
        if(block != null)
        {
            result = block.getRowCount();
        }
        return result;
    }

    public int getRow()
    {
        int result = -1; //16/05/05
        if(getState().isNew())
        {
            Err.pr(SdzNote.WHICH_ROW_ON,
                "Node.getRow(), should not be getting a current row whilst in NEW state");
        }
        if(block != null)
        {
            result = block.getIndex();
        }
        return result;
    }

    public StateEnum getState()
    {
        StateEnum result = StateEnum.FROZEN;
        if(block != null)
        {
            result = block.getState().getState();
        }
        return result;
    }

    public void setTableControl(Object component)
    {
        if(component != null)
        {
            try
            {
                TableSignatures.checkTableControlExists(component.getClass());
            }
            catch(UnknownControlException ex)
            {
                Session.error(ex.toString());
            }
            /*
            if (tableControl != null)
            {
                Err.pr("tableControl set to " + tableControl.getClass().getName() +
                    " in " + getName());
            }
            else
            {
                Err.pr("tableControl set to NULL" +
                    " in " + getName());
//                if("Person HeaderNode".equals( getName()))
//                {
//                    Err.stack();
//                }
            }
            */
            /*
            Err.pr( "table control propertyChange to: " +
             ((ComponentTableView)component).id + " on node: " + this + ", ID: " + id);
            */
        }
        else
        {
            // Err.pr( "/// table control propertyChange to NULL on node: " + this);
        }
        propChangeSupp.firePropertyChange("tableControl", this.tableControl,
            component);
        this.tableControl = component;
        /*
        times++;
        Err.pr( "Node.tableControl has been set to " + this.tableControl + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
    }

    public Object getTableControl()
    {
        // Err.pr( "getting tableControl " + tableControl + " in " + hashCode());
        return tableControl;
    }

    void setControlActionListener(ControlActionListener controlActionListener)
    {
        this.controlActionListener = controlActionListener;
        // Err.pr( "controlActionListener has been set for node: " + this);
        // Err.stack();
    }

    /**
     * public as from interface
     */
    public ControlActionListener getControlActionListener()
    {
        return controlActionListener;
    }

    /*
    **
    * If not using an official controller then obvious place to put
    * setRow and friends is here in Node. RUBBISH - StrandControl etc
    *
    public void setRow( int row)
    {
    InputControllerEvent ce = new InputControllerEvent( OperationEnum.SET_ROW);
    ce.setRow( row);
    controlActionListener.controlActionPerformed( ce);
    }
    */
    public void setRecordValidationTrigger(RecordValidationTrigger vl)
    {
        recordValidationListener = vl;
        // Err.pr( "$$$> RecordValidationTrigger for " + getName() +
        // " been set to " + vl);
        /*
        if(block != null)
        {
        block.setRecordValidationListener( vl);
        }
        */
    }

    RecordValidationTrigger getRecordValidationTrigger()
    {
        /*
        Err.pr(
        "$$$> RecordValidationTrigger being GOT for "
        + getName()
        + " is "
        + recordValidationListener);
        */
        return recordValidationListener;
    }

    public void setPostingTrigger(PostingTrigger vl)
    {
        postingListener = vl;
    }

    PostingTrigger getPostingTrigger()
    {
        return postingListener;
    }

    public void setNodeValidationTrigger(NodeValidationTrigger vl)
    {
        nodeValidationListener = vl;
        /*
        if(block != null)
        {
        block.setNodeValidationListener( vl);
        }
        */
    }

    NodeValidationTrigger getNodeValidationTrigger()
    {
        return nodeValidationListener;
    }

    /*
    * USER CAN MANAGE HERSELF!
    **
    * Initially used inside extent package, but then user might
    * be interested too. WHEN THIS happens will want to return
    * an Object, being of the type the user originally put in.
    *
    public NavExtent getInitialData_gone()
    {
    return getBlock().getDataRecords();
    }
    */

     /**/
    public void setStrand(AbstStrand strand)
    {
        // Won't be able to do as nothing wrong with dealing directly
        // with a Strand
        // Err.pr( "CODE CHANGE - remove setStrand() when have converted all old to new",
        // Err.NOT_URGENT_CODE_CHANGE);
        if(strand == null)
        {
            strand = new NullStrand();
        }
        else
        {
            this.strand = strand;
            strand.newNode(this);
        }
    }

     /**/
    public AbstStrand getStrand()
    {
        AbstStrand result = null;
        if(strand == null)
        {
            Session.error("Cannot getStrand() when it is null");
        }
        else
        {
            result = strand;
        }
        return strand;
    }
    
    public String getStrandDebugInfo()
    {
        return getStrand().toShow();
    }

    public void addNavigationTrigger(NavigationTrigger listener)
    {
        navigationPublisher.subscribe(listener);
    }

    public void removeNavigationTrigger(NavigationTrigger listener)
    {
        navigationPublisher.cancelSubscription(listener);
    }

    public void addNodeDefaultTrigger(NodeDefaultTrigger listener)
    {
        if(nodeDefaultPublisher.size() == 0)
        {
            nodeDefaultPublisher.subscribe(listener); // one listener only
        }
        else
        {
            Session.error("No support for multiple NodeDefaultTrigger objects - YET");
        }
    }

    public void removeNodeDefaultTrigger(NodeDefaultTrigger listener)
    {
        nodeDefaultPublisher.cancelSubscription(listener);
    }

    Publisher getNodePublisher()
    {
        return nodeDefaultPublisher;
    }

    Publisher getNavigationPublisher()
    {
        return navigationPublisher;
    }

    /*
    ArrayList getPostingTriggerList()
    {
    return postingTriggerList;
    }
    */

    /**
     * Required so that TableModelImpl can listen to
     * changes in current node
     */
    public void addNodeChangeListener(EventListener l)
    {
        NodeChangeListener ncl = (NodeChangeListener) l;
        strand.addNodeChangeListener(ncl);
        timesAdded++;
        Err.pr( SdzNote.NOT_REPLACE_BLOCKS, "* Have added NodeChangeListener " + l + " to " + strand + " on behalf of " + this + " times " + timesAdded);
        if(timesAdded == 0)
        {
            Err.stack();
        }
    }
    
    public void removeNodeChangeListener(EventListener l)
    {
        NodeChangeListener ncl = (NodeChangeListener) l;
        strand.removeNodeChangeListener( ncl);
    }

    public void addDataFlowTrigger(DataFlowTrigger listener)
    {
        dataFlowPublisher.subscribe(listener);
    }

    public void removeDataFlowTrigger(DataFlowTrigger listener)
    {
        dataFlowPublisher.cancelSubscription(listener);
    }

    public PostingTrigger removePostingTrigger()
    {
        PostingTrigger result = postingListener;
        postingListener = null;
        return result;
    }

    public RecordValidationTrigger removeRecordValidationTrigger()
    {
        RecordValidationTrigger result = recordValidationListener;
        recordValidationListener = null;
        // Err.pr( "recordValidationListener been set to null from " + result + " in " + this);
        return result;
    }

    public Publisher getDataFlowTriggers()
    {
        return dataFlowPublisher;
    }

    /**
     * fire the event in which user should call setData
     */
    void fireDataFlowEvent(DataFlowEnum eventType, final OperationEnum op)
    {
        final DataFlowEvent evt = new DataFlowEvent(eventType/* eg DataFlowEvent.PRE_QUERY*/, this);
        /*
        for(Iterator e = dataFlowPublisher.iterator(); e.hasNext();)
        {
          DataFlowTrigger tl = (DataFlowTrigger)e.next();
          // new MessageDlg("Telling to DataFlowEvent.PRE_QUERY to " + tl);
          try
          {
            executionType = op;
            tl.dataFlowPerformed( evt);
          }
          catch(ValidationException ex)
          {
            Session.error( ex);
          }
        }
        */
        dataFlowPublisher.publish
            (
                new Publisher.Distributor()
                {
                    public void deliverTo(Object subscriber)
                    {
                        DataFlowTrigger dft = (DataFlowTrigger) subscriber;
                        // new MessageDlg("Telling to DataFlowEvent.PRE_QUERY to " + tl);
                        try
                        {
                            executionType = op;
                            dft.dataFlowPerformed(evt);
                        }
                        catch(ValidationException ex)
                        {
                            Session.error(ex);
                        }
                    }
                }
            );
    }

    public void preCauseToSetChildsDependentExtent()
    {
        fireDataFlowEvent(DataFlowEvent.PRE_QUERY, OperationEnum.EXECUTE_QUERY);
    }

    public void postCauseToSetChildsDependentExtent()
    {
        fireDataFlowEvent(DataFlowEvent.POST_QUERY, OperationEnum.EXECUTE_QUERY);
    }

     /**/
    List getMasterTies()
    {
        if(tempTies == null)
        {
            Session.error("Ties for node " + hashCode() + " have been destroyed");
        }
        //Err.pr( "@-@ Returning MasterTies of size " + tempTies.size());
        return tempTies;
    }

    /**
     * To make it obvious that Ties were only ever kept here temporarily
     */
    boolean destroyMasterTies()
    {
        boolean result = true;
        // tempTies = null;
        if(tempTies.isEmpty())
        {
            //Err.error( "No need to destroyMasterTies()");
            result = false;
        }
        else
        {
            tempTies = new ArrayList();
        }
        return result;
    }

     /**/
    /*
    public String getMasterVectorFieldRelation()
    {
    return masterVectorFieldRelation;
    }

    public void setMasterVectorFieldRelation( String s)
    {
    this.masterVectorFieldRelation = s;
    }
    */

    /**
     * Following properties can be changed at any time and their effect
     * will be immediate - SILLY - if consider that there are many Nodes
     * and only one can be current at any one time.
     */
    public void setAll(boolean b)
    {
        /*
        setUpdate( b);
        setEnterQuery( b);
        setInsert( b);
        setExecuteSearch( b);
        setRemove( b);
        setPost( b);
        setCommitReload( b);
        setPrevious( b);
        setNext( b);
        setSetRow( b);
        setCascadeDelete( b);
        setFocusCausesNodeChange( b);
        */
        for(int i = 0; i <= CapabilityEnum.SET_EN_MASSE.length - 1; i++)
        {
            setAllowed(CapabilityEnum.SET_EN_MASSE[i], b);
        }
    }

    /*
    public void setUpdate(boolean allowed)
    {
    update = allowed;
    if(block != null)
    {
    Session.error("Haven't coded for doing runtime UPDATE changes YET");
    }
    }
    public boolean isUpdate()
    {
    return update;
    }
    */
    /**
     * If is a data entry point then can executeQuery (setData), o/wise no point.
     * If didn't want see the reachable data then could simply not put the link
     * in. Can only executeQuery if are at top level. Only time need to setLoad is if
     * are toplevel and don't want initialData to be used. (Should balk if anything
     * but a blank list is provided - DOES it?).
     */
    /*
    * 30/10/2003 - externalLoad stuff probably silly!
    public void setExecuteQuery(boolean allowed)
    {
    //XMLEncoder
    if(allowed != false)
    {
    if (!pBeans.isDesignTime())
    {
    Session.error(
    "Not allowed to Execute Query on a child");
    }
    }
    externalLoad = allowed;
    if (block != null)
    {
    Session.error("Haven't coded for doing runtime LOAD changes YET");
    }
    }
    */

    /**
     * Always called, and after setLoad called by user, so can use this as place
     * where executeQuery is finally set.
     */
    void setInternalLoad(boolean allowed)
    {
        if(allowed != true)
        {
            Session.error("Only intended setLoad TRUE, when found is toplevel");
        }
        // another way of saying is top level, 'cause when toplevel is the only
        // time this is called, and is always called with allowed == true.
        if(externalLoad != false)
        {
            // executeQuery = true;
            cmds.get(OperationEnum.EXECUTE_QUERY).setNodeAllowed(true);
        }
        else // user has specified that initialData will be null
        {
            // executeQuery = false; //by default already false, but may have been set true b4
            cmds.get(OperationEnum.EXECUTE_QUERY).setNodeAllowed(false);
        }
    }

    public boolean isAllowed(CapabilityEnum enumId)
    {
        return cmds.get(enumId).isNodeAllowed();
    }

    public void setAllowed(CapabilityEnum enumId, boolean b)
    {
        if(SdzNote.ACTIONS_REQUIRED_ON_TOOL_BAR.isVisible())
        {
            //Make List above typed to List<Action>
            Utils.chkType( enumId, Action.class);
        }
        cmds.get(enumId).setNodeAllowed(b);
        if(block != null)
        {
            DynamicAllowedEvent evt = new DynamicAllowedEvent(enumId);
            Err.pr(SdzNote.DYNAM_ALLOWED, "Evt " + evt + " being dynamically set to " + b);
            evt.setAllowed(b);
            evt.setDynamicAllowed(b);
            strand.dynamicAllowed(this, evt);
        }
    }

    public void setExecuteQuery(boolean allowed)
    {
        setAllowed(CapabilityEnum.EXECUTE_QUERY, allowed);
    }

    public boolean isExecuteQuery()
    {
        return isAllowed(CapabilityEnum.EXECUTE_QUERY);
    }

    public void setInsert(boolean allowed)
    {
        setAllowed(CapabilityEnum.INSERT_AFTER_PLACE, allowed);
    }

    public boolean isInsert()
    {
        return isAllowed(CapabilityEnum.INSERT_AFTER_PLACE);
    }

    public void setInsertPrior(boolean allowed)
    {
        setAllowed(CapabilityEnum.INSERT_AT_PLACE, allowed);
    }

    public boolean isInsertPrior()
    {
        return isAllowed(CapabilityEnum.INSERT_AT_PLACE);
    }

    public void setExecuteSearch(boolean allowed)
    {
        setAllowed(CapabilityEnum.EXECUTE_SEARCH, allowed);
    }

    public boolean isExecuteSearch()
    {
        return isAllowed(CapabilityEnum.EXECUTE_SEARCH);
    }

    public void setUpdate(boolean allowed)
    {
        setAllowed(CapabilityEnum.UPDATE, allowed);
    }

    public boolean isUpdate()
    {
        return isAllowed(CapabilityEnum.UPDATE);
    }

    public void setEnterQuery(boolean allowed)
    {
        setAllowed(CapabilityEnum.ENTER_QUERY, allowed);
    }

    public boolean isEnterQuery()
    {
        return isAllowed(CapabilityEnum.ENTER_QUERY);
    }

    public void setRemove(boolean allowed)
    {
        setAllowed(CapabilityEnum.REMOVE, allowed);
    }

    public boolean isRemove()
    {
        return isAllowed(CapabilityEnum.REMOVE);
    }

    public void setPost(boolean allowed)
    {
        setAllowed(CapabilityEnum.POST, allowed);
    }

    public boolean isPost()
    {
        return isAllowed(CapabilityEnum.POST);
    }

    public void setCommitOnly(boolean allowed)
    {
        setAllowed(CapabilityEnum.COMMIT_ONLY, allowed);
    }

    public boolean isCommitOnly()
    {
        return isAllowed(CapabilityEnum.COMMIT_ONLY);
    }

    public void setCommitReload(boolean allowed)
    {
        setAllowed(CapabilityEnum.COMMIT_RELOAD, allowed);
    }

    public boolean isCommitReload()
    {
        return isAllowed(CapabilityEnum.COMMIT_RELOAD);
    }

    public void setPrevious(boolean allowed)
    {
        setAllowed(CapabilityEnum.PREVIOUS, allowed);
    }

    public boolean isPrevious()
    {
        return isAllowed(CapabilityEnum.PREVIOUS);
    }

    public void setIgnoredChild(boolean allowed)
    {
        setAllowed(CapabilityEnum.IGNORED_CHILD, allowed);
    }

    public boolean isIgnoredChild()
    {
        return isAllowed(CapabilityEnum.IGNORED_CHILD);
    }

    public void setNext(boolean allowed)
    {
        setAllowed(CapabilityEnum.NEXT, allowed);
    }

    public boolean isNext()
    {
        return isAllowed(CapabilityEnum.NEXT);
    }

    public void setEditInsertsBeforeCommit(boolean allowed)
    {
        setAllowed(CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT, allowed);
    }

    public boolean isEditInsertsBeforeCommit()
    {
        return isAllowed(CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT);
    }

    public void setSetRow(boolean allowed)
    {
        setAllowed(CapabilityEnum.SET_ROW, allowed);
    }

    public boolean isSetRow()
    {
        return isAllowed(CapabilityEnum.SET_ROW);
    }

    public void setBlankRecord(boolean allowed)
    {
        setAllowed(CapabilityEnum.BLANK_RECORD, allowed);
    }

    public boolean isBlankRecord()
    {
        return isAllowed(CapabilityEnum.BLANK_RECORD);
    }

    public void setCascadeDelete(boolean allowed)
    {
        setAllowed(CapabilityEnum.CASCADE_DELETE, allowed);
    }

    public boolean isCascadeDelete()
    {
        return isAllowed(CapabilityEnum.CASCADE_DELETE);
    }

    public void setFocusCausesNodeChange(boolean allowed)
    {
        setAllowed(CapabilityEnum.FOCUS_NODE, allowed);
    }

    public boolean isFocusCausesNodeChange()
    {
        return isAllowed(CapabilityEnum.FOCUS_NODE);
    }
    
    /**
     * 1./ check has a class associated
     * 2./ and it can be instantiated (although setCell has already done this)
     */
    public boolean validateBean(boolean childrenToo)
    {
        validateBeanMsg.clear();

        boolean ok = true;
        String errMsg = null;
        /*
        if(isInsert() && !isDelete())
        {
        Session.error("If have Insert then must have Delete");
        }
        if(
        isDelete() &&
        ( !isInsert() && !isLoad() )
        )
        {
        Session.error("If have Delete then must have either Insert or Load " +
        isInsert() + "," + isLoad());
        }
        if(
        isUpdate() &&
        ( !isInsert() && !isLoad() )
        )
        {
        Session.error("If have Update then must have either Insert or Load " +
        isInsert() + "," + isLoad());
        }
        */
        if(tableControl != null)
        {
            {
                try
                {
                    TableSignatures.checkTableControlExists(tableControl.getClass());
                }
                catch(UnknownControlException ex)
                {
                    errMsg = "Not a valid table control: <" + tableControl + ">";
                }
            }
        }
        if(errMsg == null && childrenToo)
        {
            if(cell == null)
            {
                errMsg = "No cells";
            }
            else
            {
                boolean cok = cell.validateBean(true);
                if(!cok)
                {
                    String txt = cell.getName();
                    if(txt == null)
                    {
                        txt = cell.toString();
                    }

                    List list = cell.retrieveValidateBeanMsg();
                    if(list.size() != 1)
                    {
                        Session.error("Expected one error only");
                    }
                    errMsg = "Cell <" + txt + "> " + Utils.NEWLINE + list.get(0);
                }
            }
        }
        if(errMsg != null)
        {
            ok = false;
            validateBeanMsg.add(errMsg);
        }
        //validated = true;
        return ok;
    }

    public boolean validateBean()
    {
        return validateBean(true);
    }

    /**
     * Goes recursively into all cells so really will retrieve all the
     * attributes associated with this Node.
     * RO indexed property
     */
    public Attribute[] getAttributes()
    {
        Attribute[] result = new Attribute[0];
        if(cell != null)
        {
            result = (Attribute[]) cell.getAllAttributes().toArray(result);
        }
        // Err.pr( "From node.getAttributes[] are returning " + result.length);
        return result;
    }

    public List<RuntimeAttribute> getDisplayedAttributes()
    {
        List result = null;
        if(cell != null)
        {
            List allAttributes = cell.getAllAttributes();
            result = Utils.getSubList( allAttributes, RuntimeAttribute.class);
            result = Utils.getSubList( result, ReferenceLookupAttribute.class, Utils.EXCLUDE);
            result = Utils.getSubList( result, NonVisualAttribute.class, Utils.EXCLUDE);
        }
        if(result == null)
        {
            result = new ArrayList();
        }
        Collections.sort( result);
        return result;
    }
    
    public RuntimeAttribute getDisplayedAttribute( int index)
    {
        RuntimeAttribute result;
        List attributes = getDisplayedAttributes();
        result = (RuntimeAttribute)attributes.get( index);
        return result;
    }

    public List getDirtyAttributes()
    {
        List result;
        Attribute[] attributes = getAttributes();
        result = Attribute.getChangedAttributes(attributes);
        return result;
    }

    public boolean isDirty()
    {
        boolean result = false;
        Attribute[] attributes = getAttributes();
        List changed = Attribute.getChangedAttributes(attributes);
        if(!changed.isEmpty())
        {
            result = true;
        }
        return result;
    }

    public List getProgrammaticallyDirtyAttributes()
    {
        List result = null;
        Attribute[] attributes = getAttributes();
        result = Attribute.getProgrammaticallyChangedAttributes(attributes);
        return result;
    }

    /**
     * If one of the attributes has been changed by an actual user, so it is
     * different to any programmatic change that may have been done, then
     * this method will return false.
     */
    public boolean isProgrammaticallyDirty()
    {
        boolean result = false;
        Attribute[] attributes = getAttributes();
        List changed = Attribute.getProgrammaticallyChangedAttributes(attributes);
        if(!changed.isEmpty())
        {
            result = true;
        }
        return result;
    }

    /**
     * Only supposed to be called from a PRE_QUERY trigger as part
     * of leaving ENTER_QUERY state.
     */
    public List getEnterQueryAttributes()
    {
        List result = null;
        // Err.pr( "current state: " + getState());
        // Err.pr( "previous state: " + getStrand().getOPor().getPreviousState());
        StateEnum currentState = getState();
        if(executionType == OperationEnum.EXECUTE_QUERY
            && currentState == StateEnum.ENTER_QUERY)
        {
            result = Attribute.getNonBlankChangedAttributes(getAttributes());
        }
        else // if(currentState == StateEnum.FROZEN)
        {
            result = new ArrayList();
        }
        if(result == null)
        {
            Session.error();
        }
        return result;
    }

    public RuntimeAttribute getAttributeByName(String name)
    {
        RuntimeAttribute result = getAttributeByName(name, 1);
        if(result != null)
        {
            if(getAttributeByName(name, 2) != null)
            {
                Session.error(
                    "Use a more specific method, as have two attributes named <" + name
                        + ">");
            }
        }
        return result;
    }

    /**
     * TODO (SOON) AdaptersList will have displayName as well, which design-time user
     * will probably make unique for a Node. Thus won't need to use this slightly
     * tacky method.
     * NO - simpler to have both name and dataFieldName (dependencies b/ween
     * properties can make XML life difficult)
     * Almost certain this already done but chk and rm this comment
     */
    public RuntimeAttribute getAttributeByName(String name, int occurance)
    {
        List list = Utils.getSubList(cell.getAllAttributes(),
            RuntimeAttribute.class);
        RuntimeAttribute attribs[] = (RuntimeAttribute[]) list.toArray(
            new RuntimeAttribute[0]);
        RuntimeAttribute result = null;
        int timesWillHaveMatched = 1;
        for(int i = 0; i <= attribs.length - 1; i++)
        {
            result = attribs[i];

            String display = result.getName();
            if(display != null && display.equals(name))
            {
                if(timesWillHaveMatched == occurance)
                {
                    break;
                }
                else
                {
                    timesWillHaveMatched++;
                }
            }
            else
            {
                if(result.getDOField().equals(name))
                {
                    if(timesWillHaveMatched == occurance)
                    {
                        break;
                    }
                    else
                    {
                        timesWillHaveMatched++;
                    }
                }
            }
            result = null;
        }
        return result;
    }

    /**
     * TODO (SOON) AdaptersList will have displayName as well, which design-time user
     * will probably make unique for a Node. Thus won't need to use this slightly
     * tacky method. See notes for getAttributeByName
     */
    public RuntimeAttribute getAttributeByCellAndName(Cell cell, String name)
    {
        Attribute attribs[] = (Attribute[]) cell.getAllAttributes().toArray(
            new Attribute[0]);
        RuntimeAttribute result = null;
        for(int i = 0; i <= attribs.length - 1; i++)
        {
            if(attribs[i] instanceof RuntimeAttribute)
            {
                result = (RuntimeAttribute) attribs[i];
                if(result.getCell() == cell && result.getDOField().equals(name))
                {
                    break;
                }
                result = null;
            }
        }
        return result;
    }

    public RuntimeAttribute getAttributeByCellAndName(String cellName, String name)
    {
        RuntimeAttribute result = null;
        Cell cells[] = getCell().getCells();
        Cell cell = null;
        for(int i = 0; i < cells.length; i++)
        {
            // Err.pr( "examining " + cells[i].getName());
            if(cellName.equals(cells[i].getName()))
            {
                cell = cells[i];
                break;
            }
        }
        if(cell != null)
        {
            result = getAttributeByCellAndName(cell, name);
        }
        return result;
    }

    public List getParents()
    {
        List result = new ArrayList();
        for(Iterator iter = block.getParentsIterator( block.tm); iter.hasNext();)
        {
            Block parentBlock = (Block) iter.next();
            if(parentBlock != null)
            {
                result.add(parentBlock.getProdNodeI());
            }
            else
            {
                Session.error("parentBlock only being set with data");
            }
        }
        return result;
    }

    public boolean isDetail()
    {
        Independent independents[] = getIndependents();
        if(independents.length > 0)
        {
            return true;
        }
        return false;
    }

    /*
    public boolean isIgnoredChild()
    {
    return ignoredChild;
    }
    public void setIgnoredChild(boolean allowed)
    {
    ignoredChild = allowed;
    if (block != null)
    {
    DynamicAllowedEvent evt =
    new DynamicAllowedEvent(CapabilityEnum.IGNORED_CHILD);
    evt.setAllowed(allowed);
    //evt.setDynamicAllowed(allowed);
    strand.dynamicAllowed( this, evt);
    }
    }
    */

    /**
     * Since next para have made more general, and to be used by user. Cannot get
     * rid of all the equivalent isXXX() methods as that is what property relies on.
     * Also is stored seperately in block and node, presumably because the block ones
     * are changed at runtime.
     * <p/>
     * Strand will need to know if a direct physical action can be performed
     * by an ignoredChild. Fact that is an Operation (not Capability), means
     * that it is a direct physical action.
     */
    /*
    public boolean isAllowed( CapabilityEnum enumId)
    {
    boolean result = false;
    if(enumId == OperationEnum.EXECUTE_QUERY)
    {
    result = isLoad();
    }
    else if(enumId == OperationEnum.INSERT)
    {
    result = isInsert();
    }
    else if(enumId == OperationEnum.REMOVE)
    {
    result = isDelete();
    }
    else if(enumId == OperationEnum.POST)
    {
    result = isPost();
    }
    else if(enumId == OperationEnum.COMMIT)
    {
    result = isCommit();
    }
    else if(enumId == OperationEnum.PREVIOUS)
    {
    result = isPrevious();
    }
    else if(enumId == OperationEnum.NEXT)
    {
    result = isNext();
    }
    else if(enumId == OperationEnum.SET_ROW)
    {
    result = isSetRow();
    }
    else
    {
    Session.error( "isAllowed() only intended to be called for direct user actions");
    }
    return result;
    }
    */
    public void removePropertyChangeListener(
        String property,
        PropertyChangeListener l)
    {
        propChangeSupp.removePropertyChangeListener(property, l);
    }

    public void addPropertyChangeListener(
        String property,
        PropertyChangeListener l)
    {
        propChangeSupp.addPropertyChangeListener(property, l);
    }
    
    public PropertyChangeListener[] getPropertyChangeListeners( String property)
    {
        return propChangeSupp.getPropertyChangeListeners( property);
    }

    public boolean isAlreadyBeenCustomized()
    {
        return alreadyBeenCustomized;
    }

    public void setAlreadyBeenCustomized(boolean alreadyBeenCustomized)
    {
        this.alreadyBeenCustomized = alreadyBeenCustomized;
    }

    public List retrieveValidateBeanMsg()
    {
        return validateBeanMsg;
    }

    /*
    * Ways of getting to Strand, so create an interface sometime!
    */
    public void fireErrorHandler(ApplicationError e)
    {
        strand.fireErrorHandler(e);
    }

    public NodeI getCurrentNode()
    {
        return strand.getCurrentNode();
    }

    public int getId()
    {
        return id;
    }

    public void changeFromCurrentNode()
    {
        if(this != strand.getCurrentNode())
        {
            // Err.pr( "===========FOCUS/CLICK CAUSING NODE CHANGE to " + this);
            // Err.stack();
            goTo(false);
        }
    }

    /*
    public void b4HandleErrorProcessing( boolean b)
    {
    strand.b4HandleErrorProcessing( b);
    }
    */
    public boolean isValidationRequired()
    {
        return strand.isValidationRequired();
    }

    /*
    public void postValidationProcessing(
    boolean success,
    ApplicationError err)
    {
    strand.postValidationProcessing( success, err);
    }
    */
    public Object getMoveBlock()
    {
        MoveBlockI result = null;
        if(block != null)
        {
            result = block.getMoveBlock();
        }
        return result;
    }

    public MoveBlockI getMoveBlockI()
    {
        return (MoveBlockI) getMoveBlock();
    }

    public RecorderI getRecorderI()
    {
        return Session.getRecorder();
    }

    public ErrorThrowerI getErrorThrowerI()
    {
        return Session.getErrorThrower();
    }

    public boolean isIgnoreValidation()
    {
        return strand.isIgnoreValidation();
    }

    public Clazz getCellClazz()
    {
        return getCell().getClazz();
    }

    public String toShow()
    {
        return getName();
    }

    public String getTitle()
    {
        return title;
    }

    /**
     * Not a property, but allows name and display to stay
     * as properties that are independent of one another.
     *
     * @return The name that will be displayed on the UI
     */
    public String getDisplayName()
    {
        String result = title;
        if(result == null)
        {
            result = getName();
        }
        return result;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getCurrentOrdinal()
    {
        // Err.pr( "%% Reting current ordinal of " + currentOrdinal);
        return currentOrdinal;
    }

    public void incCurrentOrdinal()
    {
        currentOrdinal++;
        // Err.pr( "%% Just inced current ordinal to " + currentOrdinal);
    }

    public void resetCurrentOrdinal()
    {
        currentOrdinal = 0;
        // Err.pr( "%% Just reset current ordinal to 0");
    }

    public CommandActions getCommandActions()
    {
        return cmds;
    }

    /**
     * AbstNodeTable requires this method so that the TableModel impl can
     * decide whether a cell is visible or not. Of course can be used
     * for fields as well, as an alternative to getting from the Attribute
     * If these table style methods become too many or too silly then
     * we can think of hiding them behind a single method call on node,
     * or having a block implement AbstNodeTable. (Think reason did not
     * was some dependency problem which surely would be able to solve
     * now).
     */
    public boolean isColumnEnabled(int column)
    {
        boolean result = true;
        List attribs = cell.getAllAttributes();
        Collections.sort( attribs);
        if(SdzNote.ENABLEDNESS_REFACTORING.isVisible())
        {
            //Print.prList( attribs, "Displayed Attributes, s/be in order");
        }
        Attribute attr = (Attribute)attribs.get( column);
        if(SdzNote.ENABLEDNESS_REFACTORING.isVisible() && attr.getName().equals( "Expense Type"))
        {
            Err.debug();
        }
        if(Utils.instanceOf(attr, RuntimeAttribute.class))
        {
            RuntimeAttribute rtAttribute = (RuntimeAttribute) attr;
            result = rtAttribute.isEnabled();
        }
        Err.pr(SdzNote.ENABLEDNESS_REFACTORING,
            "%%% From column <" + column + "> got attribute <" + attr
                + "> and isEnabled " + result);
        return result;
    }
    
    void recordCurrentValue( String id, int row, StateEnum origState, OperationEnum operation)
    {
        Assert.notNull( origState);
        List cells = getCells();
        for(Iterator iterator = cells.iterator(); iterator.hasNext();)
        {
            Cell cell = (Cell) iterator.next();
            cell.recordCurrentValue( id, row, origState, operation);
        }
    }
    
    public boolean hasChanged()
    {
        boolean result = false;
        List cells = getCells();
        for(Iterator iterator = cells.iterator(); iterator.hasNext();)
        {
            Cell cell = (Cell) iterator.next();
            if(cell.hasChanged())
            {
                result = true;
                break;
            }
        }
        return result;
    }

    class OwnNavigationT implements NavigationTrigger
    {
        public void navigated(OperationEvent evt)
        {
            //Assert.isTrue( getState() == evt.getOrigState());
            //if(evt.getOrigState() != StateEnum.FROZEN)
            {
                String msg = "Navigated to <" + outer + ">";
                //Err.stack( SdzNote.RECORD_CURRENT_VALUE, msg);
                Err.pr( SdzNote.RECORD_CURRENT_VALUE, "About to recordCurrentValue by <" + evt + "> when in " + outer);
                recordCurrentValue( msg, Utils.UNSET_INT, evt.getOrigState(), evt.getReason());
            }
//            else 
//            {
//                Err.pr( /*SdzNote.RECORD_CURRENT_VALUE,*/ "It's frozen when navigating to it: " + outer);
//            }
        }
    }

    public boolean isDataDetached()
    {
        return dataDetached;
    }

    public void setDataDetached( boolean dataDetached)
    {
        //Assert.isTrue( dataDetached, "Intended that create a new Node rather than have a concept of attaching the data again");
        this.dataDetached = dataDetached;
        //Assert.notNull( block, "Don't have a block in node: " + this);
        if(block != null)
        {
            block.detachData( dataDetached);
        }
    }

    public NodeGroup getNodeGroup()
    {
        return nodeGroup;
    }

    public void setNodeGroup(NodeGroup nodeGroup)
    {
        this.nodeGroup = nodeGroup;
    }
        
    public Object getItemValue()
    {
        return cell.getItemValue();
    }
    
    public boolean fieldsChanged()
    {
        return block.fieldsChanged();
    }

    /*
     * Have decided that the right place for all DO stuff is at the cell.
    public Object getItemValue()
    {
      return cell.getItemValue();
    }
    */
}
