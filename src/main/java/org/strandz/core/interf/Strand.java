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

import org.strandz.core.domain.ControlSignatures;
import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.Independent;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.NodeChangeEvent;
import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.domain.NodeTableMethods;
import org.strandz.core.domain.SdzBeanI;
import org.strandz.core.domain.TableSignatures;
import org.strandz.core.domain.ValidationContext;
import org.strandz.core.domain.MoveBlockI;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.DynamicAllowedEvent;
import org.strandz.core.domain.event.InputControllerEvent;
import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.core.prod.AbstBlock;
import org.strandz.core.prod.Block;
import org.strandz.core.prod.BlockState;
import org.strandz.core.prod.BlockStateCreator;
import org.strandz.core.prod.Creatable;
import org.strandz.core.prod.Enabler;
import org.strandz.core.prod.LookupTiesManager;
import org.strandz.core.prod.MasterDetailTiesManager;
import org.strandz.core.prod.NodeTableModelImpl;
import org.strandz.core.prod.NullBlock;
import org.strandz.core.prod.OperationsProcessor;
import org.strandz.core.prod.Session;
import org.strandz.core.prod.move.ErrorSiteEnum;
import org.strandz.core.prod.move.MoveManagerI;
import org.strandz.core.prod.view.Builder;
import org.strandz.core.prod.view.FieldObj;
import org.strandz.core.record.GoToOperation;
import org.strandz.core.record.Operation;
import org.strandz.core.record.PlayerI;
import org.strandz.core.record.Recorder;
import org.strandz.core.record.SetValueOperation;
import org.strandz.core.record.SimpleOperation;
import org.strandz.core.record.XMLRecorderData;
import org.strandz.lgpl.extent.ChildExtentGetterI;
import org.strandz.lgpl.extent.OrphanTie;
import org.strandz.lgpl.extent.Tie;
import org.strandz.lgpl.extent.Ties;
import org.strandz.lgpl.note.SdzDsgnrNote;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.persist.EntityManagerFactory;
import org.strandz.lgpl.persist.EntityManagerProviderI;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.CapabilityAction;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.IndentationCounter;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Publisher;
import org.strandz.lgpl.util.StopWatch;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.widgets.CursorIndentationCounter;
import org.strandz.lgpl.widgets.IconEnum;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Contains all the Nodes and the NodeController. Goes into various
 * states at DT and RT. No applichousing plumbing exists in this class -
 * that is left to classes that implement the <code>SdzBagI</code> interface.
 * <p/>
 * At the outset the class Strand was created so
 * 1./ The state machine can trap the 'goNode' event,
 * so that nodes can be frozen whilst
 * they are not the focused node, then put into
 * adding/editing when the user chooses to enter them
 * 2./ End user doesn't need to code for complications
 * such as when the current Block
 * is changed, StateChangeListeners will not be notified.
 * (ie. user had to remember to do post())
 *
 * @author Chris Murphy
 */
public class Strand extends AbstStrand
    implements Serializable,
    Enabler, PlayerI, SdzBeanI, EntityManagerProviderI, StrandI
{
    /**
     * A sop to sdzdsgnr, so theoretically sdzdsgnr.BeansTreeModel s/deal with
     * a wrapper around this Data Object
     */
    private boolean alreadyBeenCustomized = false;
    private String name;
    private List<Node> nodes = new ArrayList<Node>();
    private List validateBeanMsg = new ArrayList();
    // In Oper now
    // private int currentBlock = -1;
    List<NodeChangeListener> nodeChangeListenerList = new ArrayList<NodeChangeListener>();
    /**
     * Only have one of these, rather than list, because api user may use
     * this trigger to focus on a different node. Strand.controlActionPerformed()
     * uses this new target node as the node that the action is performed on. It
     * would not be logical to collect many.
     */
    Publisher preControlActionPerformedTriggers = new Publisher();
    Publisher postControlActionPerformedTriggers = new Publisher();
    ValidationHandlerTrigger validationHandlerTrigger;
    EntityManagerTriggerI entityManagerTrigger;
    // ArrayList nodeControllerListenerList = new ArrayList();
    /*
    For StateChangeEvent events that this class is capable
    of generating (so eg./ a graphic that showed the user
    the state could observe the changes)
    */
    // list of registered StateChange listener objects
    private Publisher stateChangePublisher = new Publisher();
    Publisher transactionPublisher = new Publisher();
    MasterDetailTiesManager mdTiesManager;
    LookupTiesManager lTiesManager;
    AbstNodeController controller = new NullNodeController();
    public NodeController.ControllerMemento lastControllerMemento = null;
    private boolean ignoreValidation;
    private IndentationCounter indentationCounter = CursorIndentationCounter.getInstance( OperationEnum.class);
    /**
     * Used to ensure that goNode does not call itself. This happens for
     * example when setEnabled is called on a component. In this case the
     * swing code requests focus, which calls our focus listener.
     * Possibly would be cleaner to remove then add back MyFocusAdapter with
     * component.[remove/add]FocusListener, but would be more complex as
     * would have to check the type of the listener (as user can have them
     * too). Also there must be other calls (?setVisible) that would need
     * wrapping as well, so even more complicated.
     * <p/>
     * Note that this strategy does not help when swing code places a
     * FOCUS_GAINED on the EDThread Queue. A pesky example of this is when,
     * as a result of a button press programmatically initiating a goBlock(),
     * that same button becomes hidden. FOCUS_LOST goes to said button, but
     * where does FOCUS_GAINED go? Answer is determined by
     * FocusTraversablePolicy.
     */
    private boolean alreadyPerformingGoNode = false;
    private OperationsProcessor oper = new OperationsProcessor(
        stateChangePublisher);
    private MoveManagerI moveManager = Session.createMoveManager( indentationCounter);
    private List errorHistory = new ArrayList();
    /**
     * NOT HOW IMPLEMENTED
     * To do something (eg. insert, delete ...) to an ignoreChild node
     * must have got there programmatically. (Not true for when mouse
     * click). Want to get back right away in case applichousing controller has a
     * request. Cannot because for example with insert, an error will occur as
     * soon as leave ie. blank record. The request after the next one will be
     * for where the user last put herself to (not where the hidden code
     * put her to). For this reason as goNode to an igoredNode we save
     * which one to go back to in nodeCameFromB4Ignored, and consume it
     * after performing the one after the next action.
     * Want programmatic and non-programmatic to work in
     * exactly the same way. WAY to achieve result is to only do actions
     * for an ignoredChild if last move was to it. Otherwise actions are for
     * nodeCameFromB4Ignored. Way to achieve is if are doing an action on
     * an ignored set justDoneActionOnIgnored. If an action is asked for
     * and justDoneActionOnIgnored, then will go on to do it, but first of
     * all will go to nodeCameFromB4Ignored.
     * All above might even work with mouse click, as mouse click will move
     * to it then setRow() (I think). PROBLEM IS TOO COMPLEX, and user will
     * expect for example for insert, delete etc (imagine if they were key
     * presses) to act where are.
     * HOW IMPLEMENTED
     * Final solution - keep on allowing actions that are allowed for the
     * ignoredChild. When something that is not allowed for it comes up,
     * then go back to nodeCameFromB4Ignored.
     */
    private Node nodeCameFromB4Ignored;
    private boolean alreadyPassedNodesThru;
    private boolean manualB4ImageValue;
    private SdzEntityManagerI entityManager;
    /**
     * Set to true by ProdKpi as doing so will allow 
     * calculations to work.
     * TRUE for GOTO
     */
    public boolean dontReplaceBlocksWhenGoto = false;
    private StrandState currentState = NodeCollectingState.getNewInstance(this);
    private Node ignoreNode;

    static private int timesSetRow;
    static private int timesCommitReload;
    static private int timesOp;
    static private int goNodeTimes;
    private static int playTimes;
    private static int timesSdzControl = 0;
    private static int times;
    private static int timesGroup;

    public static final String NO_NODES_VAL_MSG = "No nodes";
    public static final int NODECOLLECTING = 1;
    static final int NODESGOBBLED = 2;
    public static final int LOADDONE = 3;
    static final int INSERTDONE = 4;

    private static int timesConstructed;
    public int id;

    public Strand()
    {
        //See comments for createMoveManager()
        //moveManager.reset();
        addStateChangeTrigger(oper);
        timesConstructed++;
        // Err.pr( "In constructor for strand ID: " + timesConstructed);
        id = timesConstructed;
        addPostControlActionPerformedTrigger(new OwnPostOperationTrigger());
        Err.pr( SdzNote.BI_AI, "Created strand, ID: " + id);
        //Err.pr( SdzNote.NOT_REPLACE_BLOCKS_HACK, "dontReplaceBlocks: " + dontReplaceBlocks);
    }

    public boolean isManualB4ImageValue()
    {
        return manualB4ImageValue;
    }

    public void setManualB4ImageValue(boolean manualB4ImageValue)
    {
        this.manualB4ImageValue = manualB4ImageValue;
    }

    /**
     * Wider than a Strand as one per user session. Just a thin
     * covering for Session. At this stage in development don't
     * want the user to know about a Session.
     */
    public RuntimeAttribute getLastChangedAttribute()
    {
        RuntimeAttribute result = null;
        ItemAdapter ad = moveManager.getEnteredAdapter();
        Cell cell = (Cell) ad.getCell();
        List attributes = cell.getAttributes();
        List subList = Utils.getSubList(attributes, RuntimeAttribute.class);
        for(Iterator iter = subList.iterator(); iter.hasNext();)
        {
            RuntimeAttribute att = (RuntimeAttribute) iter.next();
            if(att.getItemAdapter() == ad)
            {
                result = att;
                break;
            }
        }
        return result;
    }

     /**/
    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Strand))
        {
            result = false;
        }
        else
        {
            result = false;

            Strand test = (Strand) o;
            if(name.equals(test.name))
            {
                result = true;
                if(nodes.size() == test.nodes.size())
                {
                    for(int i = 0; i <= nodes.size() - 1; i++)
                    {
                        Node node = (Node) nodes.get(i);
                        Node testNode = (Node) test.nodes.get(i);
                        if(!node.equals(testNode))
                        {
                            result = false;
                            break;
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

    /*
    public int hashCode()
    {
    int result = 17;
    result = 37*result + (name == null ? 0 : name.hashCode());
    for(int i=0; i<=nodes.size()-1; i++)
    {
    Node node = (Node)nodes.get( i);
    result = 37*result + node.hashCode();
    }
    return result;
    }
    */

    public void affect(CapabilityEnum operation, boolean enable)
    {
        if(enable)
        {
            enable(operation);
        }
        else
        {
            disable(operation);
        }
    }

    public void enable(CapabilityEnum operation)
    {
        DynamicAllowedEvent evt = new DynamicAllowedEvent(operation);
        // evt.setID( operation);
        evt.setAllowed(true);
        evt.setDynamicAllowed(true);
        controller.setDynamicAllowed(evt);
        // Err.pr( "enabled " + Constants.stringValue( operation) + " for " + this);
    }

    public void disable(CapabilityEnum operation)
    {
        DynamicAllowedEvent evt = new DynamicAllowedEvent(operation);
        // evt.setID( operation);
        evt.setAllowed(false);
        evt.setDynamicAllowed(false);
        controller.setDynamicAllowed(evt);
        /*
        times++;
        Err.pr( "disabled " + operation + " for " + this + " times: " + times);
        if(times == 0 && operation == CapabilityEnum.REMOVE)
        {
        Err.stack();
        }
        */
    }

    public boolean dataRecordsEmpty()
    {
        return oper.getCurrentBlock().dataRecordsEmpty();
    }

    /*
    private void disableDeletePost( Node currentNode)
    {
    InputControllerEvent evt = new InputControllerEvent();
    evt.setID( OperationEnum.REMOVE);
    evt.setDynamicAllowed( false);
    dynamicAllowed( currentNode, evt);
    evt.setID( OperationEnum.POST);
    evt.setDynamicAllowed( false);
    dynamicAllowed( currentNode, evt);
    evt.setID( OperationEnum.PREVIOUS);
    evt.setDynamicAllowed( false);
    dynamicAllowed( currentNode, evt);
    evt.setID( OperationEnum.NEXT);
    evt.setDynamicAllowed( false);
    dynamicAllowed( currentNode, evt);
    }
    */

    /*
    private void disableLoad( Node currentNode)
    {
    InputControllerEvent evt = new InputControllerEvent();
    evt.setID( OperationEnum.EXECUTE_QUERY);
    evt.setDynamicAllowed( false);
    dynamicAllowed( currentNode, evt);
    }
    */

    public void setWhenLastVisibleFalseControllerMemento
        (
            NodeController.ControllerMemento lastControllerMemento
        )
    {
        // Err.pr("Setting lastControllerMemento to \n" + lastControllerMemento);
        this.lastControllerMemento = lastControllerMemento;
    }

    public void addNodeChangeListener(NodeChangeListener listener)
    {
        nodeChangeListenerList.add( listener);
        /*
        Err.pr( "&&&&&&&&& addNodeChangeListener called with " + listener.getNode() +
        " on strand ID: " + id + " so " + nodeChangeListenerList.size());
        */
    }

    public void removeNodeChangeListener(NodeChangeListener listener)
    {
        // Session.error( "removing from blockChangeListenerList " + listener);
        boolean removed = nodeChangeListenerList.remove(listener);
        if(!removed)
        {
            //Err.pr( "");
            //Print.prList( nodeChangeListenerList, "current listeners");
            //Err.pr( "Was not able to remove listener for: " + listener.getNode());
            if(listener.getNode() != null)
            {
                List<NodeChangeListener> toRemove = new ArrayList<NodeChangeListener>();
                for (int i = 0; i < nodeChangeListenerList.size(); i++)
                {
                    NodeChangeListener nodeChangeListener = nodeChangeListenerList.get(i);
                    if(nodeChangeListener.getNode() == listener.getNode())
                    {
                        toRemove.add( nodeChangeListener);
                    }
                }
                Assert.isTrue( toRemove.size() < 2, "Should not have two nodeChangeListeners on the same node");
                for (int i = 0; i < toRemove.size(); i++)
                {
                    NodeChangeListener nodeChangeListener = toRemove.get(i);
                    boolean removedOk = nodeChangeListenerList.remove( nodeChangeListener);
                    Assert.isTrue( removedOk);
                    //Err.pr( "ok removal of NodeChangeListener to " + listener.getNode());
                }
            }
        }
        else
        {
            //Err.pr( "ok removal of NodeChangeListener to " + listener.getNode());
        }
    }

    public void removeAllNodeChangeListeners()
    {
        // Session.error( "Clearing blockChangeListenerList");
        nodeChangeListenerList.clear();
    }

     /**/
    public void addTransactionTrigger(CloseTransactionTrigger listener)
    {
        transactionPublisher.subscribe(listener);
    }

    public void removeTransactionTrigger(CloseTransactionTrigger listener)
    {
        transactionPublisher.cancelSubscription(listener);
    }

    public Publisher getTransactionTriggers()
    {
        return transactionPublisher;
    }

    public void removeReferenceLinkAttributesTo(Node node)
    {
        List attributes = Utils.getSubList(getAttributes(),
            ReferenceLinkAttribute.class);
        for(Iterator iter = attributes.iterator(); iter.hasNext();)
        {
            ReferenceLinkAttribute attr = (ReferenceLinkAttribute) iter.next();
            if(attr.getIndependent().getMasterNode() == node)
            {
                Cell cell = attr.getCell();
                cell.removeAttribute(attr);
            }
        }
    }

    public void removeReferencesTo(Container container)
    {
        if(TableSignatures.isTableControl(container.getClass()))
        {
            Err.error(org.strandz.lgpl.note.SdzDsgnrNote.GENERATING_OVER_TABLE,
                "Not yet supporting TableControl");
        }
        SdzBagUtils.updateAttributesWithNullPane( getNodes(), container, 
            "Removing all references to " + container.getName());
    }

    public void addPreControlActionPerformedTrigger(PreOperationPerformedTrigger cal)
    {
        preControlActionPerformedTriggers.subscribe(cal);
    }

    public void removePreControlActionPerformedTrigger(PreOperationPerformedTrigger cal)
    {
        preControlActionPerformedTriggers.cancelSubscription(cal);
    }

    public void addPostControlActionPerformedTrigger(PostOperationPerformedTrigger cal)
    {
        postControlActionPerformedTriggers.subscribe(cal);
    }

    public Publisher removeAllPreControlActionPerformedTriggers()
    {
        Publisher result = new Publisher(preControlActionPerformedTriggers);
        preControlActionPerformedTriggers.clear();
        return result;
    }

    public Publisher removeAllPostControlActionPerformedTriggers()
    {
        Publisher result = new Publisher(postControlActionPerformedTriggers);
        postControlActionPerformedTriggers.clear();
        return result;
    }

    Publisher getStateChangeListenerList()
    {
        return stateChangePublisher;
    }

    /**
     * A NodeController will set itself as the controller so that later
     * it can be told when BlockChange events occur.
     * Using controller can also setup the newly current node with
     * the right abilities ( - the AbstStrand gives access to most
     * abilities).
     */
    public void setNodeController(NodeController c)
    {
        /*
        This is ok - could then run the transaction in a batch mode
        */
        if(c == null)
        {
            Session.error("Fatal Error: did not expect NodeController to be null");
        }
        controller = c;
        // Following call will set off the NodeController to its frozen appearance
        oper.setEnabler(this);
        /*
        Since XMLEncode don't care if none, as now each time add a node,
        attach it to the controller.
        if(nodes.size() == 0)
        {
        if(!pBeans.isDesignTime())
        {
        Session.error( "Strand " + this + " has no nodes hashCode:" + hashCode());
        }
        }
        */
        for(Iterator e = nodes.iterator(); e.hasNext();)
        {
            ((Node) e.next()).setControlActionListener(controller);
        }
    }
    
    public NodeController getNodeController()
    {
        NodeController result = null;
        if(controller instanceof NodeController)
        {
            result = (NodeController)controller;
        }
        return result;
    }

    public void addStateChangeTrigger(StateChangeTrigger listener)
    {
        stateChangePublisher.subscribe(listener);
    }

    public void removeStateChangeTrigger(StateChangeTrigger listener)
    {
        stateChangePublisher.cancelSubscription(listener);
    }

    public void changeState( int newState)
    {
        if(newState == Strand.LOADDONE)
        {
            currentState.changeState( this, LoadDoneState.getNewInstance( this));
        }
        else
        {
            Err.error( "Not coded to change state to " + newState);
        }
    }

    /**
     * This method works for a new node
     * (newNode != null)
     * that is already in RT
     * (newNode.getBlock() != null)
     * where we want to
     * erase any knowledge of it having been at RT before, in order to introduce
     * the DT changes.
     *
     * @param newNode
     */
    private void tidyNode( Node newNode, boolean setBlock)
    {
        if(newNode != null && newNode.getBlock() != null)
        {
            purgeNode(newNode);
            Err.pr(SdzNote.TIES, "#### block going to have all ties flushed is " + newNode.getBlock());
            newNode.getBlock().flushAllTies(mdTiesManager);
            //No need, as will already be empty
            boolean ok = newNode.destroyMasterTies();
            if(ok)
            {
                Err.error("Expected not to need to destroyMasterTies()");
            }
            /* Just forget this code - is leak free even without it
            if(setBlock)
            {
                 *
                 * Even thou this code not happening (for ProdKpi), we are no longer leaking blocks!
                 * This code was weird anyway - set when we didn't even know what setBlock meant.
                 * Is there any reason that setting a block as the current one should mean the old
                 * version needs to be GCed first?
                 *
                Err.stack( "Happenstance??? S/now be able to GC block ID " + newNode.getBlock().getId());
                newNode.getBlock().nullForGarbageCollection();
                newNode.setBlockNull();
            }
            */
        }
    }

    public void consumeNodesIntoRT( Node newNode, String reason, Node nodeToSetAsCurrent, boolean dontReplaceNewNodeBlock)
    {
        List<Node> newNodes = null;
        if(newNode != null)
        {
            newNodes = Utils.formList( newNode);
        }
        consumeNodesIntoRT( newNodes, reason, nodeToSetAsCurrent, dontReplaceNewNodeBlock);
    }


    /**
     * Called when exeqry, when goto another node(*), and with null when a
     * commit is done.
     * (*) Only when 'collecting nodes' - which means at RT but data has
     * not yet entered into any blocks. The idea is that you visit a node
     * to do something to it, and that something may require the existence
     * of blocks. So blocks must be useful things to have even if they
     * don't have any data in them.
     * <p/>
     * Validation that relies on ties between nodes goes can only
     * be done after this method has been called.
     * <p/>
     * Until this method has been called for first time, Strand
     * has merely been collecting Nodes. We can now check that there are
     * some Nodes[, and that they are reasonably filled in]. Then we can turn
     * them into Blocks. Design and run time activities can then be done at once,
     * without design changes interferring with the current state. After calling
     * this method design and RT are the same thing.
     *
     * [] <=> now in Node.validate()
     *
     * @param newNodes When not null we are introducing a node that has changed or was
     * never previously known. And these changes need to be reflected in RT - so it
     * is a full introduction. At the same time we are saying that all the
     * other nodes have not been changed. Thus the blocks that lay behind them are not
     * to be touched in any kind of a way that would change say what is stored as past
     * data. To introduce one, some things must be done for all. For instance M/D ties is
     * always done in here for all the nodes of the strand.
     * The behaviour of this param when not null is modified by dontReplaceBlock.
     *
     * @param reason Reason for calling this method.
     *
     * @param nodeToSetAsCurrent
     * If not null then it will become the current block.
     * This is important because here new blocks are created which make the old ones
     * redundant. 'true' is used internally when searching, committing etc. However this
     * method can be called by application code. When it is the objective may be to load
     * new attibutes or whatever - for many different blocks. At the end of doing many
     * different blocks the application programmer can manually set the current node/block.
     * For example with CarbonStartup doing QueryEngine.doQueryAtAllLevels() would always
     * leave us with the current block being the highest level one (Company). Yet we want
     * to keep the current block where we are. To avoid a redundant node being the current
     * one strand.syncNode() needs to be called.
     *
     * @param dontReplaceNewNodeBlock Usually false, but sometimes (?? usually) you might want to lessen the
     * impact. For example when is RT and are moving (GOTO) to another node, it might not be
     * necessary to bring across all the design time changes for the node you are going to -
     * in fact doing so would seem like the more rare case. It is necesssary not to replace
     * the nodes if you want to keep the results of the calculations that were done last
     * time you were there.
     */
    public void consumeNodesIntoRT( List<Node> newNodes, String reason, Node nodeToSetAsCurrent, boolean dontReplaceNewNodeBlock)
    {
        Node node;
        Block block;
        OrphanTie topTie;
        Err.pr( SdzNote.SET_AND_CREATE_BLOCKS, "consumeNodesIntoRT: " + reason);
        Assert.isFalse( dontReplaceNewNodeBlock, "Now always replace them - will remove this param soon");
        if(dontReplaceNewNodeBlock)
        {
            /*
             * This situation will change, as replacing blocks ought to be the edge case. For
             * instance when commit we might like to leave the blocks intact.
             */
            Assert.isTrue( "goNode".equals( reason) || "executeSearch".equals( reason),
                "Got < " + reason + ">");
        }
        if(nodeToSetAsCurrent != null)
        {
            Assert.notNull( newNodes);
            Assert.notEmpty( newNodes);
            Assert.isTrue( Utils.containsByInstance( newNodes, nodeToSetAsCurrent) > 0);
        }
        if(mdTiesManager != null)
        {
            if(newNodes != null)
            {
                for (int i = 0; i < newNodes.size(); i++)
                {
                    Node newNode = newNodes.get(i);
                    tidyNode( newNode, nodeToSetAsCurrent != null);
                }
            }
            mdTiesManager.purge( Node.class);
            //mdTiesManager.setEnforceType(Node.class);
        }
        else
        {
            //first time consumeNodesIntoRT has ever been called
            mdTiesManager = new MasterDetailTiesManager(Node.class);
        }
        if(lTiesManager != null)
        {
            lTiesManager.purge( Creatable.class);
            //lTiesManager.setEnforceType(Creatable.class);
        }
        else
        {
            lTiesManager = new LookupTiesManager(Creatable.class);
        }
        if(SdzNote.DYNAM_ATTRIBUTES.isVisible())
        {
            if(newNodes != null)
            {
                for (int i = 0; i < newNodes.size(); i++)
                {
                    Node newNode = newNodes.get(i);
                    if(newNode != null)
                    {
                        times++;
                        Err.pr( "**consumeNodesIntoRT because new node " +
                            newNode.getName() + " with ID " + newNode.getId() + " for reason " + reason + " times " + times);
                        if(times == 0)
                        {
                            Err.stack();
                        }
                    }
                    else
                    {
                        Err.pr( "consumeNodesIntoRT for reason " + reason);
                    }
                }
            }
        }
        for(Iterator e = nodes.iterator(); e.hasNext();)
        {
            node = (Node) e.next();
            if(newNodes == null) //Note that this is different to them being empty - null means we are going
                                 //the whole way into RT with all the nodes
            {
                /*
                 * If we are not specifically introducing a new node, then we
                 * are doing all blocks from scratch, so erase previous memory.
                 */
                node.setBlockNull();
            }

            boolean ok = node.validateBean(false);
            if(!ok)
            {
                Session.error(node.retrieveValidateBeanMsg());
            }
            //Err.pr( "#-# Num of independents in node " + node + " is " + node.getIndependents().length);
            //Err.pr( "#-# Num of MasterTies in node " + node + " is " + node.getMasterTies().size());
            createTiesFromIndependents(node.getIndependents(), node);
            /*
            * Ties are added to tiesManager here. Will have to wait until
            * every Node has a Block before can transform them into Tie(s)
            * with Block nodes.
            */
            if(node.getMasterTies().isEmpty())
            {
                /*
                * So that later BlockTiesManager.getSingles will work, we will now create
                * a Tie without a parent. For conversion (to Blocks) process to work
                * have to add the new Tie to the set held by a Node.
                */
                // new MessageDlg("To create a tie w/out a parent for " + node);
                topTie = new OrphanTie(node);
                node.getMasterTies().add(topTie);
                // Err.pr( "Have added OrphanTie to " + node);
            }
            mdTiesManager.addTies(node.getMasterTies());
        }
        mdTiesManager.setConsistent(true); // consistent in nodes
        /*
         * Any validation for the 'connectedness' of nodes would go here.
         */
        /*
         * At end of following every Node will have a Block.
         */
        for(Iterator e = nodes.iterator(); e.hasNext();)
        {
            node = (Node) e.next();
            /*
            * Don't create a new block for every node, as we don't
            * want to get rid of data just because we are querying
            * a different block. However where we are creating a new
            * block we need to make sure we replace it over the last
            * block.
            */
            boolean nodeInNewNodes = Utils.containsByInstance( newNodes, node) > 0; //node == newNode
            if(node.getBlock() == null || nodeInNewNodes)
            {
                if(dontReplaceNewNodeBlock && node.getBlock() != null && nodeInNewNodes)
                {
                    /*
                     * This is the updated node that we are re-introducing and there is a special
                     * marker to say lets keep its block, presumably because there is runtime data
                     * in it.
                     * Exactly what this achieves is that the calculations done from
                     * CalculationPlace will still work.
                     */
                    Err.pr( SdzNote.NOT_REPLACE_BLOCKS,
                        "* To keep calc results we ignore the creating new blocks aspect of these newNodes: " +
                            newNodes + ", reason: " + reason);
                    //Err.stack();
                }
                else
                {
                    String reasonNeedNewBlock = null;
                    if(node.getBlock() == null)
                    {
                        /*
                         * First time entry for node
                         */
                        reasonNeedNewBlock = "Are replacing block (and node does not have a block, so no brainer!): " + node.getName() + ", reason: " + reason;
                    }
                    else if(nodeInNewNodes)
                    {
                        /*
                         * Re-entry of a specific node where we want to preserve the others, but obliterate RT
                         * of the one we are introducing
                         */
                        reasonNeedNewBlock = "Introducing new nodes: " + newNodes + ", reason: " + reason;
                    }
                    Err.pr( SdzNote.NOT_REPLACE_BLOCKS, reasonNeedNewBlock);
                    newBlock(node);
                    //node.getBlock().tm = mdTiesManager; - done in above so s/not need
                }
            }
        }

        List blocks = new ArrayList();
        /*
        * Now that every Node has a Block we can create a real Ties object,
        * which will be in terms of many Tie objects that have Blocks as
        * nodes. So obviously it was necessary to create the blocks as we
        * did above.
        */
        mdTiesManager.setConsistent(false);
        for(Iterator e = nodes.iterator(); e.hasNext();)
        {
            node = (Node) e.next();
            block = node.getBlock();
            if(block == null)
            {
                Session.error("Node " + node + " does not have a block");
            }
            // Err.pr( "To makeTies for " + block + " where have " + node.getTies().size() + " ties");
            makeMasterDetailTies(block, node.getMasterTies());
            if(block.getMasterTies().isEmptyOfTies())
            {
                Err.error("Just done makeTies(), yet block doesn't have any");
            }
            mdTiesManager.setEnforceType(AbstBlock.class);
            node.destroyMasterTies(); // to make it obvious Ties were only in Node temporarily
            blocks.add(block);
        }
        /*
        * Now everything to do with M/D ties is complete.
        */
        mdTiesManager.setConsistent(true); // now consistent in blocks
        for(Iterator e = nodes.iterator(); e.hasNext();)
        {
            node = (Node) e.next();
            block = node.getBlock();
            if(mdTiesManager.isTopLevel(block))
            {
                node.setInternalLoad(true);
            }
            if(newNodes == null || Utils.containsByInstance( newNodes, node) > 0)
            {
                if(newNodes != null && !mdTiesManager.isTopLevel(block))
                {
                    /*
                     * Strange in the sense that you would almost always start off doing a query or
                     * going to a node that was the 'master' one. It makes no sense to say go to the
                     * detail Roster Slots node straight away.
                     */
                    Err.pr("STRANGE that NOT doing consumeNodesIntoRT() for a top level block, but instead for <" + node + ">");
                }
                /* Now that we are lazily creating blocks first when one asked for (so already
                   by time get to here), new node is now not really a new node, but a newer
                   instance of what was the current node, so no need to go thru leaving blocks etc.
                 */
                if(newNodes != null && (nodeToSetAsCurrent != null && block == nodeToSetAsCurrent.getBlock()))
                {
                    /**/
                    Block lastBlock = oper.getCurrentBlock();
                    if(lastBlock != null)
                    {
                        //lastBlock.nullForGarbageCollection( true);
                        lastBlock.nullTableControl();
                    }
                    /**/
                    oper.internalSetCurrentBlock( block);
                    Err.pr( SdzNote.SET_AND_CREATE_BLOCKS, "Have set current block to " + block);
                }
                /**/
            }
            /*
            * makeBabies assumes they are ties, so makeTies has to come
            * beforehand. (And have to have done them all, 'cause makeBabies
            * also uses the hierarchy)
            */
            block.makeBabies( mdTiesManager);
        }
        for(Iterator it1 = blocks.iterator(); it1.hasNext();)
        {
            Block bl = (Block) it1.next();
            boolean nodeNotInNewNodes = Utils.containsByInstance( newNodes, bl.getProdNodeI()) == 0; //(bl.getProdNodeI() != newNode
            if(nodeNotInNewNodes)
            {
                moveManager.setBlock(bl);
            }
        }
        if(moveManager.initialPoint(blocks))
        {
            addNodeChangeListener(moveManager);
        }
        connectFocusListener(blocks);
    }

    private void connectFocusListener(List blocks)
    {
        List itemAdapters = new ArrayList();
        //Print.prList( SdzNote.fieldValidation, blocks, "Blocks to get adapters from");
        for(Iterator it = blocks.iterator(); it.hasNext();)
        {
            Block bl = (Block) it.next();
            List adapters = bl.getAdaptersArrayList();
            itemAdapters.addAll(adapters);
        }
        //Print.prList( SdzNote.fieldValidation, itemAdapters, "all adapters");
        Map map = new HashMap();
        for(Iterator it = itemAdapters.iterator(); it.hasNext();)
        {
            ItemAdapter ad = (ItemAdapter) it.next();
            IdEnum idEnum = ad.createIdEnum( toString());
            //Err.pr( "Control is " + control);
            //if(!(control instanceof JComponent))
            if(ControlSignatures.isFocusMonitorable(idEnum))
            {
                JComponent control = (JComponent) idEnum.getControl();
                if(Utils.isBlank(control.getName()))
                {
                    Err.pr("Class: " + control.getClass().getName());
                    Component parent = control.getParent();
                    Err.pr("Parent: " + parent);
                    if(parent != null)
                    {
                        Err.pr("Parent's name: " + parent.getName());
                    }
                    Err.error("A control does not have a name: " + control);
                }
                else
                {
                    if(!map.containsKey( control.getName()))
                    {
                        //Putting name because using the actual control did not work.
                        //Not sure why - an 'in memory' equals() s/have been ok.
                        Object obj = map.put(control.getName(), ad);
                        if(obj != null)
                        {
                            //Now handled earlier
                            Err.error("Control name is not unique: " + control.getName());
                        }
                    }
                    else
                    {
                        Err.pr( "A control with same name exists: <" + control.getName() +
                            ">, so focus monitoring won't be done for " + control.getClass().getName());
                    }
                    //Err.pr( SdzNote.fieldValidation, ad + " is focus monitorable");
                }
            }
        }
        ControlSignatures.acceptAdapters(map);
    }

    void createTiesFromIndependents(Independent independents[], Node node)
    {
        if(node.getCell() == null)
        {
            Session.error("Node: " + node.getName() + " does not have a cell");
        }
        if(!node.getCell().isChief())
        {
            Session.error(
                "setIndependent(): dependent side must be the main Cell in a Node"
                    + " , being called on " + this);
        }
        for(int i = 0; i < independents.length; i++)
        {
            Independent independent = independents[i];
            /*
            * Need to determine whether parentOrChildField is a List on the parent
            * side or a reference from the child to the parent. Tie does this and related
            * validation in its constructor.
            */
            Tie tie = Tie.createTie(independent.getMasterNode(), // containerNode
                node, // the Node that this tie to an independent is for
                // fieldNode
                independent.getMasterNode().getCellClazz().getClassObject(), // Container.class
                node.getCell().getClazz().getClassObject(), independent.getMasterOrDetailField(),// "components"
                this, node.getCell().getInsteadOfAddRemoveTrigger());
            node.getMasterTies().add(tie);
        }
    }

    /**
     * Each particular Tie has been created in package interf. A Ties object
     * can only now be created, as Ties requires a HasCombinationExtent type. Also
     * requires the hierarchy to exist and a block to exist for every node.
     */
    private void makeMasterDetailTies(Block block, List v)
    {
        block.setMasterTies(new Ties(block));

        Node parentNode;
        Tie tie;
        for(Iterator e = v.iterator(); e.hasNext();)
        {
            tie = (Tie) e.next();
            tie.setChild(block);
            parentNode = (Node) tie.getParent();
            // new MessageDlg("parent node will be looking at node for is " + node);
            if(parentNode != null)
            {
                tie.setParent(parentNode.getBlock());
                tie.setChildExtentGetter((ChildExtentGetterI) block.getProdNodeI());
            }
            block.getMasterTies().addTie(tie);
        }
    }

    /**
     * Called when a user associates a Node with its NodeController
     */
    void newNode(Node node)
    {
        nodes.add(node);
        // XMLEncoder, so that controller and node/s can be set on a strand
        // in any order:
        // if(pBeans.isDesignTime() && controller != null)
        if(controller != null)
        {
            node.setControlActionListener(controller);
        }
        /*
        times++;
        Err.pr( "---!!! Adding a node to a strand, node: " + node);
        if(times == 0)
        {
        Err.stack();
        }
        */
    }

    /*
    * The Cell we get from a Node is the Chief one. It may be linked
    * (lookups) to many other Cells, all of which must also be
    * converted to Creatables. ie. recursive
    */
    private Creatable fromCellToCreatable(Cell cell)
    {
        Cell lookupCells[] = cell.getCells();
        List<Creatable> lookupCreatables = new ArrayList<Creatable>();
        List<String> lookupRefFields = new ArrayList<String>();
        for(int i = 0; i <= lookupCells.length - 1; i++)
        {
            lookupCreatables.add(fromCellToCreatable(lookupCells[i]));
            lookupRefFields.add(lookupCells[i].getRefField());
        }
        /*
        if(cell.toString().equals( "Group Cell"))
        {
            //Err.debug();
            timesGroup++;
            Err.pr( "To add Attributes to Group Cell times " + timesGroup);
            if(timesGroup == 3)
            {
                Err.debug();
            }
        }
        */
        AdaptersList adapters = new AdaptersList( cell.getCalculationPlace(), cell.getClazz().getClassObject().getName());
        int howMany = 0;
        for(Iterator it = cell.getAttributes().iterator(); it.hasNext();)
        {
            Attribute attribute = (Attribute) it.next();
            adapters.addAttribute(attribute, lTiesManager);
            howMany++;
        }
        Err.pr( SdzNote.ENABLEDNESS_REFACTORING.isVisible(), "Have added " + howMany + " attributes to AdaptersList of " + cell);
        SdzNote.CELL_CLAZZ.incTimes();
        Err.pr( SdzNote.CELL_CLAZZ, "Are going to create a creatable from cell <" + cell + ">, which has clazz: " +
            cell.getClazz() + ", times " + SdzNote.CELL_CLAZZ.getTimes());
        if(SdzNote.CELL_CLAZZ.getTimes() == 9)
        {
            Err.debug();
        }
        return new Creatable(cell, lookupCreatables, lookupRefFields, adapters, lTiesManager,
            cell.getNewInstanceTrigger(), cell.getInsteadOfAddRemoveTrigger(), this);
    }

    private static List getNVTables(List nvTableAttributes, NodeTableModelImpl btmi)
    {
        List result = new ArrayList();
        for(Iterator iterator = nvTableAttributes.iterator(); iterator.hasNext();)
        {
            NonVisualTableAttribute attribute = (NonVisualTableAttribute) iterator.next();
            //NodeTableMethods model = Block.setupTable( attribute.getItem(), btmi,
            //    (AbstNodeTable)getProdNodeI());
            /**/
            Class staticModel = TableSignatures.getTableModel(attribute.getItem());
            // new MessageDlg("model will instantiate is " + staticModel);
            // Check that class is more than just an interface, and thus can be
            // instantiated:
            if(Modifier.isInterface(staticModel.getModifiers()))
            {
                Err.error(
                    "Model should not be an interface, but a concrete instantiation");
            }
            NodeTableMethods tableModel = (NodeTableMethods) ObjectFoundryUtils.factory(staticModel);
            if(tableModel == null)
            {
                Err.error("Could not construct a " + staticModel);
            }
            tableModel.setNode(null, btmi); // this line b4 setModel
            /**/
            TableSignatures.setModel(attribute.getItem(), tableModel);
            Err.pr(SdzDsgnrNote.GENERIC, "table " + attribute.getItem() + " now has model " + tableModel);
            result.add(attribute.getItem());
        }
        if(btmi != null)
        {
            btmi.addCompTables(result);
        }
        else
        {
            if(!result.isEmpty())
            {
                Err.error("Nowhere to attach the NonVisualTables", result);
            }
        }
        return result;
    }

    /**
     * TODO Just call this and you will find there is a note in here!
     * Where cast to block it is actually a node.
     *
     * @param node
     */
    private void purgeNode(Node node)
    {
        //Didn't do a thorogh test but this seemed to work, so leaving comment here:
        Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "#### Needing to purge node with block " + node.getBlock());
        Iterator parents = node.getBlock().getParentsIterator( mdTiesManager);
        for(Iterator iterator = parents; iterator.hasNext();)
        {
            Block parent = (Block) iterator.next();
            parent.getChild().adoptOutBaby(node.getBlock());
            if(parent.getChild() == node.getBlock())
            {
                //Err.pr( "Need to remove the child reference to " + node.getBlock() + " from " + parent.getBlock());
                parent.setChild(new NullBlock());
            }
        }
        mdTiesManager.purgeOldNode(node);
    }

    /**
     * Everything to do with DT -> RT for a particular node -> block, starting from the
     * beginning so creating a block. M/D config has been done, but this will setup the
     * RT lookup relationships.
     */
    private Block newBlock(Node node)
    {
        /*
        times++;
        Err.pr( "In newBlock for " + node + " times " + times);
        if(times == 2)
        {
            Err.debug();
        }
        */
        /**/
        if(node.getBlock() != null)
        {
            //node.getBlock().nullForGarbageCollection( false);
            node.getBlock().nullTableControl();
        }
        /**/
        Block intBlock = new Block(node, oper, mdTiesManager,
            node.getNavigationPublisher(), node.getNodePublisher(),
            node.getTableControl(), node.getNodeGroup(), isManualB4ImageValue());
        node.setBlock(intBlock);
        node.resetCurrentOrdinal();
        NodeTableModelImpl btmi;
        if(node.getTableControl() != null)
        {
            btmi = new NodeTableModelImpl(intBlock, oper);
            if(intBlock.getProdNodeI() != node)
            {
                Err.error();
            }
            intBlock.setNodeTableModelForTable( btmi);
            if(node.getTableModel() == null)
            {
                Err.error();
            }
            NodeTableMethods model = Block.setupTable( node.getTableControl(), btmi, node);
            Err.pr( SdzNote.ENABLEDNESS_REFACTORING, "To perhaps set a tableBuffer for table " + node.getTableControl());
            TableSignatures.setTableBuffer( node.getTableControl(), node.getDisplayedAttributes().size());
            //trial TableSignatures.setModel( node.getTableControl(), model);
            Creatable creatable = fromCellToCreatable(node.getCell());
            FieldObj tableObj = Builder.createTableObj(null, intBlock, creatable, btmi);
            btmi.setTableObj(tableObj);
            intBlock.setCreatableForTable(creatable, model, tableObj);
        }
        else
        {
            Creatable creatable = fromCellToCreatable(node.getCell());
            intBlock.setCreatableForField(creatable);
        }
        List nonVisuals = InterfUtils.collectNonVisualTableAttributes(node);
        if(!nonVisuals.isEmpty())
        {
            //Err.pr( "NonVisuals on " + intBlock.getName());
            //intBlock.setNonVisualTableAttributes( nonVisuals);
            intBlock.setNvTables(getNVTables(nonVisuals, intBlock.getNodeTableModelImpl()));
        }
        else
        {
            //Err.pr( "NonVisuals NOT on " + intBlock.getName());
        }
        // intBlock.setState( FreezingState.getNewInstance( oper, intBlock));

        // BlockState state = FreezingState.getNewInstance( oper, intBlock);
        // BlockState state = BlockStateCreator.newInstance( oper, StateEnum.FROZEN);
        // intBlock.setDisplayEditable( false);

        moveManager.setBlock(intBlock);

        BlockState state = BlockStateCreator.newInstance(oper, node);
        if(state.getState() == StateEnum.FROZEN)
        {
            intBlock.setDisplayEditable(false);
        }
        // oper.setCurrentBlock( intBlock); Not set up right to do this!
        // Ellicit a state change, so the StatusBar gets it
        oper.changeState(state, intBlock);
        Err.pr(SdzNote.ANY_STATE_CHANGE_POSSIBLE, "Changing state to " + state);
        intBlock.setState(state);

        List attributes = Arrays.asList(node.getAttributes());
        List visibleAttributes = Utils.getSubList(attributes,
            RuntimeAttribute.class);
        if(visibleAttributes.size() == 0)
        {
            Err.pr( SdzNote.DYNAM_ATTRIBUTES, "No RuntimeAttributes for " + node);
        }
        else
        {
            Err.pr( SdzNote.DYNAM_ATTRIBUTES, "Have " + visibleAttributes.size() + " RuntimeAttributes for " + node);
        }
        for(Iterator iter = visibleAttributes.iterator(); iter.hasNext();)
        {
            RuntimeAttribute attribute = (RuntimeAttribute) iter.next();
            // attribute.clear();
            // attribute.getAdapter().setB4ImageValue( null);
            // Err.pr( "Have set b4image null for " + attribute.getAdapter().id);
            // attribute.getAdapter().setItemValue( null);
        }
        // Err.pr("intBlock post been set to " + intBlock.getPostAllowed());
        // WAS NOT USING: node.addNavigationListener( intBlock);
        // use closedStateOKConfirmed now addDataFlowListener( intBlock);
        /**
         * Validation done later.
         */
        //
        // We may need to replace the old current block
        //
        /**/
        String existingName = null;
        if(oper.getCurrentBlock() != null)
        {
            existingName = oper.getCurrentBlock().getName();
        }
        String newName = intBlock.getName();
        //Err.pr( "Existing name: " + existingName);
        //Err.pr( "New name: " + newName);
        if(Utils.equals( newName, existingName))
        {
            oper.internalSetCurrentBlock( intBlock);
        }
        /**/
        return intBlock;
    }

    /*
    public NodeInterface getCurrentNode()
    {
    if(currentBlock == -1)
    {
    //Session.error("A Current Block has not been specified");
    return null;
    }
    return (Node)nodes.get( currentBlock);
    }

    private void setCurrentNode( int newNode)
    {
    times++;
    Err.pr( "^^^   Current node being set to " + newNode + " times " + times);
    if(times == 0)
    {
    Err.stack();
    }
    currentBlock = newNode;
    }
    */

    public OperationsProcessor getOPor()
    {
        return oper;
    }
    
    public void goNode( Node target)
    {
        goNode( target, Utils.UNSET_INT);
    }

    /**
     * Use when know are already on the right node, and have re-built the blocks
     * using consume...(), but the current node may still have an old block as
     * the current block.
     * TODO Get rid of this method as it now prolly serves no purpose
     *
     * @param target
     */
    public void syncNode( Node target)
    {
        int b4Block = ((Node)target.getCurrentNode()).getBlock().getId();
        //Err.pr( "Assumption this is the wrong current block: " + b4Block);
        goNode(target, Utils.UNSET_INT, true);
        int afterBlock = ((Node)target.getCurrentNode()).getBlock().getId();
        //Err.pr( "? So has current block changed: " + afterBlock);
        Assert.isTrue( b4Block == afterBlock, "This method serves a purpose");
    }

    /**
     * As any switching is caught here, we can:
     * o Ensure underlying objects know about any recent (since last key press)
     * changes made to the users UI.
     * o Generate a block change event so client object (eg toolbar) can display
     * which block is current - no real need to do this, as block that sends
     * event may be same block that receives the event. Implement later for
     * when a status indicator is used, or for on-enter on-exit block type
     * handling, many need to respond, many to send etc.
     * o Spark a StateChange event, so the state of the new current block is
     * reflected.
     * <p/>
     * NB./ Put error detection in here so user doesn't call it too soon
     * <p/>
     * Additionally:
     * o As user can call this b4 we are ready, savedFirstBlock and
     * loadInsertDone() added.
     * o Change the abilities which a block has. Currently these are all
     * applichousing and on NodeController, apart from updateAllowed, which
     * must be changed in internal block.
     */
    public void goNode(Node target, int row)
    {
        goNode(target, row, false);
    }

    public void goNode(Node target, int row, boolean sameNode)
    {
        goNodeTimes++;
        Err.pr( SdzNote.GO_NODE, "Trying to goNode to " + target + " where current is " + getCurrentNode() + " times " + goNodeTimes);
        if(goNodeTimes == 0)
        {
            Err.stack();
        }
        if(alreadyPerformingGoNode)
        {
            return;
        }
        if(target == null)
        {
            Session.error("Current Block cannot be set to null in userFocus(..)");
        }
        if(target == ignoreNode)
        {
            // Err.pr("*./ ignoring " + ignoreNode);
            Session.error("ignoring " + ignoreNode + " due to Windows workaround");
            return;
        }
        else
        {
            ignoreNode = null;
        }

        /*
        times++;
        Err.pr( "userFocus being called for " + newNode.hashCode() + " for time " + times);
        Err.pr( "to (if don't STACK) call currentState.userNodeFocus for " + currentState);
        if(times == 2)
        {
        Err.stack();
        }
        */
        Node currentNode = null;
        if(oper.getCurrentBlock() != null)
        {
            if(!sameNode)
            {
                currentNode = (Node) oper.getCurrentBlock().getProdNodeI();
                if(target == currentNode)
                {
                    Err.alarm("No point in going to the node that are currently on");
                    return;
                }
            }
        }
        else
        {/*
       * Get a current one when first query or insert, but this is actually a note!
       * Mechanism s/be that first time need to access the current node, then
       * consumeNodesIntoRT() is performed.
       */// Session.error( "No point in going to another node when don't yet have a current one");
            /*
             * Now not setting in consumeNodesIntoRT(), so here instead
             */
            if(target.getBlock() != null)
            {
                oper.setCurrentBlock( target.getBlock());
                currentNode = (Node) oper.getCurrentBlock().getProdNodeI();
            }
        }
         /**/
        if(target.isAllowed(CapabilityEnum.IGNORED_CHILD))
        {
            if(currentNode == null
                || !currentNode.isAllowed(CapabilityEnum.IGNORED_CHILD))
            {
                nodeCameFromB4Ignored = currentNode;
                // Err.pr( "nodeCameFromB4Ignored: " + nodeCameFromB4Ignored);
            }
        }
        if(!isIgnoreValidation())
        {
            generatePreOperationPerformedEvent(target, OperationEnum.GOTO_NODE, row);
            /*
            OutNodeControllerEvent outputEvent = new OutNodeControllerEvent(
            OperationEnum.GOTO_NODE, target, oper.getPreviousState());
            for(Iterator iter = preControlActionPerformedTriggers.iterator(); iter.hasNext();)
            {
            PreOperationPerformedTrigger trig = (PreOperationPerformedTrigger)iter.next();
            try
            {
            trig.preOperationPerformed( outputEvent);
            }
            catch(ValidationException ex)
            {
            Session.getErrorThrower().throwApplicationError( ex,
            ApplicationErrorEnum.PRE_CAP);
            }
            }
            */
        }
        setAlreadyPerformingGoNode(true);
        Err.pr( SdzNote.INDENT, "Abt to start GOTO_NODE");
        startSdzHasControl( OperationEnum.GOTO_NODE);
        currentState.goNode(this, target, dontReplaceBlocksWhenGoto);
        Err.pr( SdzNote.INDENT, "Abt to end GOTO_NODE");
        endSdzHasControl( OperationEnum.GOTO_NODE);
        // experiment (required when navigate as well as for
        // goNode(), yet doesn't work well - test by flitting
        // b/ween tabs):
        // target.goAsBefore();
        setAlreadyPerformingGoNode(false);
        generatePostOperationPerformedEvent(target, OperationEnum.GOTO_NODE, row);
    }

    private void generatePostOperationPerformedEvent(Node target, OperationEnum enumId, int row)
    {
        // Err.pr( "$ In generatePostOperationPerformedEvent for " + enumId);
        final OutNodeControllerEvent outNodeEvent = new OutNodeControllerEvent(enumId, target,
            oper.getPreviousState(), row);
        /*
        for(Iterator iter = postControlActionPerformedTriggers.iterator(); iter.hasNext();)
        {
          PostOperationPerformedTrigger trig = (PostOperationPerformedTrigger)iter.next();
          trig.postOperationPerformed( outNodeEvent);
        }
        */
        postControlActionPerformedTriggers.publish
            (
                new Publisher.Distributor()
                {
                    public void deliverTo(Object subscriber)
                    {
                        PostOperationPerformedTrigger trig = (PostOperationPerformedTrigger) subscriber;
                        trig.postOperationPerformed(outNodeEvent);
                    }
                }
            );
    }

    private void generatePreOperationPerformedEvent(Node target, OperationEnum enumId, int row)
    {
        // Err.pr( "$ In generatePreOperationPerformedEvent for " + enumId);
        final OutNodeControllerEvent outNodeEvent = new OutNodeControllerEvent(enumId, target,
            oper.getPreviousState(), row);
        /*
        for(Iterator iter = preControlActionPerformedTriggers.iterator(); iter.hasNext();)
        {
          PreOperationPerformedTrigger trig = (PreOperationPerformedTrigger)iter.next();
          try
          {
            trig.preOperationPerformed( outNodeEvent);
          }
          catch(ValidationException ex)
          {
            Session.getErrorThrower().throwApplicationError( ex,
                ApplicationErrorEnum.PRE_CAP);
          }
        }
        */
        preControlActionPerformedTriggers.publish
            (
                new Publisher.Distributor()
                {
                    public void deliverTo(Object subscriber)
                    {
                        PreOperationPerformedTrigger trig = (PreOperationPerformedTrigger) subscriber;
                        try
                        {
                            trig.preOperationPerformed(outNodeEvent);
                        }
                        catch(ValidationException ex)
                        {
                            Session.getErrorThrower().throwApplicationError(ex,
                                ApplicationErrorEnum.PRE_CAP);
                        }
                    }
                }
            );
    }

    /*
    for(Iterator iter = preControlActionPerformedTriggers.iterator(); iter.hasNext();)
    {
    PreOperationPerformedTrigger trig = (PreOperationPerformedTrigger)iter.next();
    *
    * This also can throws a ValidationException. Again the exception will
    * be from the user.
    *
    try
    {
    trig.preOperationPerformed( outputEvent);
    }
    catch(ValidationException ex)
    {
    Session.getErrorThrower().throwApplicationError( ex,
    ApplicationErrorEnum.PRE_CAP);
    }
    }
    */

    public void generateNodeChangeEvent(Node newNode)
    {
        /*
        Err.pr("&&&&&&&&&&&&&&&&&&&& in generateNodeChangeEvent for Strand ID " + id);
        Err.pr("&&&&&&&&&&&&&&&&&&&& With " + blockChangeListenerList.size() + " blockChangeListenerList");
        */
        StopWatch stopWatch = new StopWatch();
        stopWatch.startTiming();
        for (Iterator e = nodeChangeListenerList.iterator(); e.hasNext();)
        {
            NodeChangeListener nodeChangeListener = (NodeChangeListener) e.next();
            /*
            Err.pr("\tAbout to generateNodeChangeEvent, to listener " + nodeChangeListener +
                " from node <" + newNode + ">, ID: " + newNode.getId());
            */
            NodeChangeEvent nce = new NodeChangeEvent(newNode);
            nodeChangeListener.nodeChangePerformed(nce);
        }
        stopWatch.stopTiming();
        /*
        Err.pr( "generateNodeChangeEvent() for " + newNode + " took: " + stopWatch.getElapsedTimeStr());
        Err.pr( "Did for " + nodeChangeListenerList.size());
        Err.pr( "");
        */
    }

    /**
     * See elsewhere for comments will get rid of this.
     */
    /*
    void generateNodeAccessible( int id)
    {
    for(Iterator e = blockChangeListenerList.iterator(); e.hasNext();)
    {
    NodeChangeListener nodeChangeListener = (NodeChangeListener)e.next();
    //Err.pr("gen blockChangeEvent for " + newNode);
    AccessEvent ae = new AccessEvent( id);
    nodeChangeListener.accessChange( ae);
    }
    }
    */

    /**
     * As well as being called right after consumeNodesIntoRT, when first focus on
     * a node, is also called whenever focus on a new Node
     * (NodesGobbledState and LoadInsertDoneState)
     * and whenever a reload is done (LoadInsertDoneState).
     * When beanify will only need to call this at time they press the
     * validate/commit/ok button in a Strand (just after consumeNodesIntoRT).
     * WILL NOT
     * <p/>
     * Now done for all Nodes. eg./ at beginning only master's abilities
     * would have been set otherwise.
     * <p/>
     * This method (and setControllerMemento) are only places Controller
     * is actually changed. Also dynamicAllowed and dynamicAbilities.
     * Also controlActionPerformed() and commit() and Enabler.enable()/disable()
     * SOME OF THIS RATIONALISED
     */
    public void setNewCapabilities(Node localNode, boolean firstTime)
    {
        if(controller instanceof NullNodeController)
        {// Err.pr( "In setNewCapabilities for " + localNode + " but controller null so will return");
            // Err.pr( "NEW CODE: Now not returning if no controller", Err.WARNING);
            // return;
        }

        // Err.pr("<<<inside setNewCapabilities " + localNode);
        Block localBlock = localNode.getBlock();
        if(localBlock == null)
        {
            Session.error( "Serious problem, node " + localNode + " does not have a block");
        }

        /**
         * Convenient time to set any new abilities that may have been set for
         * any of many nodes. Taking dynamism to SO FAR UNREQUIRED lengths. Means
         * that changes to a node will be reflected as soon as it is focused on.
         */
        int i = 0, newNode = -1;
        for(Iterator e = nodes.iterator(); e.hasNext(); i++)
        {
            Node node = (Node) e.next();
            if(node == localNode)
            {
                newNode = i;
            }
            if(firstTime)
            {
                // TODO - Make this happen when construct a block (14/02/2002)
                Block block = node.getBlock();
                if(block == null)
                {
                    Session.error("serious problem");
                }
                block.getCommandActions().setDynamicAllowed();
                /*
                block.setDynamicAllowed( node.isAllowed( CapabilityEnum.UPDATE), CapabilityEnum.UPDATE);
                block.setDynamicAllowed( node.isAllowed( OperationEnum.EXECUTE_QUERY), OperationEnum.EXECUTE_QUERY);
                block.setDynamicAllowed( node.isAllowed( OperationEnum.INSERT), OperationEnum.INSERT);
                block.setDynamicAllowed( node.isAllowed( OperationEnum.REMOVE), OperationEnum.REMOVE);
                block.setDynamicAllowed( node.isAllowed( OperationEnum.POST), OperationEnum.POST);
                block.setDynamicAllowed( node.isAllowed( OperationEnum.COMMIT_ONLY), OperationEnum.COMMIT_ONLY);
                block.setDynamicAllowed( node.isAllowed( OperationEnum.COMMIT_RELOAD), OperationEnum.COMMIT_RELOAD);
                block.setDynamicAllowed( node.isAllowed( OperationEnum.PREVIOUS), OperationEnum.PREVIOUS);
                block.setDynamicAllowed( node.isAllowed( OperationEnum.NEXT), OperationEnum.NEXT);
                block.setDynamicAllowed( node.isAllowed( OperationEnum.SET_ROW), OperationEnum.SET_ROW);
                block.setDynamicAllowed( node.isAllowed( CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT), CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT);
                block.setDynamicAllowed( node.isAllowed( CapabilityEnum.BLANK_RECORD), CapabilityEnum.BLANK_RECORD);
                block.setDynamicAllowed( node.isAllowed( CapabilityEnum.IGNORED_CHILD), CapabilityEnum.IGNORED_CHILD);
                */
            }
        }
         /**/
        /*
        * AbstStrand needs to be changed every time a different block
        * is made to be the current one - see above for where block.setAllowed
        * called.
        */
        // ?? EDIT AND REMOVE - delete is handled in ifInsertedDeleteActions, whilst
        // edit is not to do with controller.
        /*
        * Do not change the capabilities of the controller if the node that is
        * getting the focus is an ignored child (ie to be ignored by the controller
        * and the statusbar)
        */
        if(!localNode.isAllowed(CapabilityEnum.IGNORED_CHILD))
        {
            /*
            Err.pr( "Inside Strand.setNewCapabilities where will call NodeController.setAbilities");
            */
            controller.setAbilities(localNode.getAbilities());
        }
        /*
        * Done here, because haven't so far had a calamity:
        */
        // String str = "inside setNewCapabilities, current Node will now be set to " + newNode
        // + pUtils.separator + "for Strand <" + this + ">";
        // new MessageDlg( str);
        /*
        * Trying not setting, as s/already be there
        * setCurrentNode( newNode);
        */
        /*
        if(getCurrentNode() != null)
        {
        if(getCurrentNode() != localNode)
        {
        Session.error( "S/be on node: " + localNode + " but are on " + getCurrentNode());
        }
        }
        else
        {
        Err.pr( "Setting current node to " + newNode);
        setCurrentNode( newNode);
        }
        */
        /*
        * When all the controller buttons have been set for the new block that the
        * user has just focused on is time to change them according to how much
        * data is available (eg if no data then no prev/next s/be available). Now
        * is the time to do this. Note that this is still too soon when user is
        * going into the first block on the initial load - as there is no data
        * available and disableNavigation will be going on invalid readings.
        */
        // 11/11/2002
        // Made disableNavigation happen all the time. Effect needed was that every
        // time focus on a different block we need to go thru the proper process.
        // Could make this better by calling only every time a FOCUS event is
        // received by a block
        // NOW - moved to Per.enterblock as per above
        // oper.disableNavigation();
        /* 15/11/03
        if(firstTime)
        {
        oper.generateStateChangeEvent(
        StateEnum.FROZEN, StateEnum.UNKNOWN, oper.getCurrentBlock().getNode());
        }
        */
        if(!localNode.isAllowed(CapabilityEnum.IGNORED_CHILD))
        {
            if(oper.getCurrentBlock() == null)
            {
                Err.error( "No current block");
            }
            oper.capabilitiesSet();
        }
    }

    public void dynamicAllowed(Node nodeChanged, DynamicAllowedEvent evt)
    {
        // Err.pr( "dynamicAllowed being called for " + nodeChanged.getBlock());
        Block blockChanging = nodeChanged.getBlock();
        boolean allowedIntention = evt.getDynamicAllowed();
        CapabilityEnum id = evt.getID();
        Block block = oper.getCurrentBlock();
        // setDynamicAllowed was only ever setting allowed for the block as was for node
        // blockChanging.setDynamicAllowed( allowedIntention, id);
        CapabilityAction action = blockChanging.getCommandActions().get(id);
        action.setBlockAllowed(allowedIntention);
        if(block == blockChanging)
        {
            oper.dynamicAllowed(evt);
        }
    }

    /*
    void ifInsertedDeleteActions( Strand context, Block block, boolean duringCommit)
    {
    oper.ifInsertedDeleteActions();
    }
    */

    public void dynamicAbilities(Node nodeChanged, List abilities)
    {
        Block blockChanging = nodeChanged.getBlock();
        Block block = oper.getCurrentBlock();
        if(block == blockChanging)
        {
            controller.setAbilities(abilities);
        }
    }

    /**
     * When go to display a strand, if it does not have a 
     * current node, then is a signal to most applichousing strands to
     * re-query again. 
     */
    public void setRequiresRefresh()
    {
        //Calling oper.init ensures that getCurrentNode() will
        //return null
    }

    public Node getCurrentNode()
    {
        Node result = null;
        Block currentBlock = oper.getCurrentBlock();
        if(currentBlock != null)
        {
            result = (Node) currentBlock.getProdNodeI();
        }
        return result;
    }

    public void setValidationRequired(boolean b)
    {
        oper.setValidationRequired(b);
    }

    public boolean isValidationRequired()
    {
        return oper.isValidationRequired();
    }

    /**
     * The user is to be able to set an error handling trigger on a Strand.
     * This method will call the error handler the user has set. If user hasn't
     * set one then will get runtime error. In users method will need to call
     * the users ErrorHandlerTrigger, passing an ErrorHandlerEvent, which will
     * contain any info user might not be able to get (for instance currentNode
     * s/be easy to get, but is currentOperation?). Need to think about how
     * to achieve no further processing. For instance if called directly then
     * no problem. If called o/wise then does an ApplicationError that is caught
     * need to be thrown?
     * <p/>
     * Each of these methods to be in pairs, so execute( OperationEnum.EXECUTE_QUERY) will have a pair
     * called executeQueryErr() that is checked. Thus when user thinks that an
     * error is likely can code like this.
     * <p/>
     * Three ways that user will be able to achieve no further processing. First by
     * calling XXXErr() as above, and having a boolean in his trigger code. Second
     * by not assigning a handler and catching Session.error() that thrown here. (Not
     * recommended). Third by having own error handler throw own Error, which will
     * catch at bottom of Trigger code, similarly to second method.
     * <p/>
     * When none assigned should have own default error handler that does MessageDlg().
     * Must still raise an error to stop further user trigger processing. Error that
     * throw will have same message as what went out in MessageDlg(), and will have extra
     * useful information for the user.
     */
    public void fireErrorHandler(ApplicationError e)
    {
        /*
        times1++;
        Err.pr( "In fireErrorHandler(), times " + times1);
        if(times1 == 1)
        {
        Err.debug();
        }
        */
        if(validationHandlerTrigger != null)
        {
            if(e.getMsg() == null)
            {
                Print.prThrowable(e, "Strand.fireErrorHandler()");
                Session.error("Not producing an ApplicationError " + "with a message");
            }
            else if(!e.getMsg().isEmpty())
            {
                Component tableControl = (Component) getCurrentNode().getTableControl();
                if(tableControl != null)
                {
                    tableControl.repaint();
                }

                /*
                * No point making untouchable, as adding back a focus
                * listener seems to cause the control to gain focus,
                * which is what we were trying to stop in the first
                * place.
                */
                // makeUntouchable( true);
                final ApplicationError err = e;
                /*
                * Calling from invokeLater() in case the user decides
                * to focus on a control or something
                */
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        /*
                        * As part of handleError, goAsBefore() will be
                        * called, as often the problem is triggered by
                        * the user trying to focus elsewhere.
                        */
                        //Err.pr( "About to fire " + validationHandlerTrigger);
                        validationHandlerTrigger.handleError(err);
                    }
                });
            }
            else
            {
                Print.prThrowable(e, "Empty message list!");
                Session.error("Empty message list!");
            }
        }
        else
        {
            Session.error("No error handler set - see notes at this method");
        }
        errorHistory.add(e);
    }

    public List getErrorHistory()
    {
        return errorHistory;
    }

    /**
     * The error that is often being reported is that the user is trying
     * to navigate to where she can't. See Node.changeToCurrentNode() where
     * could possibly focus back on where came from before the user had a
     * chance to put out a MessageDlg.
     * <p/>
     * When what causes the error is
     * focusing, and the MessageDlg then tries to focus back on the control
     * can't go to, we get recursive messages. This method allows the focus
     * listener to be turned off while MessageDlg is potentially being
     * displayed. (Might be better but more complex to do for all fire
     * validations, for case when user put MessageDlg in one of these rather
     * than relying on ErrorHandler as we would prefer).
     */
    public void makeUntouchable(boolean b)
    {
        for(Iterator iter1 = getNodes().iterator(); iter1.hasNext();)
        {
            Node node = (Node) iter1.next();
            List attribs = Utils.getSubList(node.getAttributes(),
                RuntimeAttribute.class);
            for(Iterator iter2 = attribs.iterator(); iter2.hasNext();)
            {
                RuntimeAttribute rta = (RuntimeAttribute) iter2.next();
                rta.getItemAdapter().makeUntouchable(b);
            }
        }
    }

    public void setErrorHandler(ValidationHandlerTrigger validationHandlerTrigger)
    {
        this.validationHandlerTrigger = validationHandlerTrigger;
    }
    
    public ValidationHandlerTrigger getErrorHandler()
    {
        return validationHandlerTrigger;
    }

    public void setEntityManagerTrigger(EntityManagerTriggerI entityManagerT)
    {
        this.entityManagerTrigger = entityManagerT;
    }

    public void execute(InputControllerEvent e)
    {
        OperationEnum op = e.getReason();
        if(op == OperationEnum.SET_ROW)
        {
            int row = e.getRow();
            SET_ROW(row);
        }
        else
        {
            execute(op);
        }
    }

    public boolean execute(OperationEnum op)
    {
        InputControllerEvent ce = new InputControllerEvent(this, op);
        controlActionPerformed(ce);

        boolean result = moveManager.getValidationContext().isOk();
        return result;
    }

    /**
     * Everything is zero delimited
     */
    public boolean SET_ROW(int row)
    {
        InputControllerEvent ce = new InputControllerEvent(this,
            OperationEnum.SET_ROW);
        ce.setRow(row);
        timesSetRow++;
        Err.pr( SdzNote.SET_ROW, "Set row of Strandz being called to " + row + " times " + timesSetRow);
        Err.pr( SdzNote.SET_ROW, "\twhen current node is " + getCurrentNode());
        if(timesSetRow == 0)
        {
            Err.stack();
        }
        controlActionPerformed(ce);

        boolean result = moveManager.getValidationContext().isOk();
        return result;
    }

    /**
     * In case getErrorMessage() does not provide enough information
     * about the latest error. From the ValidationContext you can find
     * out at which Node/Cell/Attribute the error occured on.
     */
    public ValidationContext getValidationContext()
    {
        return moveManager.getValidationContext();
    }

    public String getErrorMessage()
    {
        String result = null;
        if(!moveManager.getValidationContext().isOk())
        {
            Object msg = moveManager.getValidationContext().getMessage();
            if(msg != null)
            {
                if(msg instanceof List)
                {
                    result = msg.toString();
                }
                else
                {
                    result = msg.toString();
                }
            }
        }
        return result;
    }

    public void ENTER_QUERY()
    {
        execute(OperationEnum.ENTER_QUERY);
    }

    public void EXECUTE_QUERY()
    {
        execute(OperationEnum.EXECUTE_QUERY);
    }

    public void EXECUTE_SEARCH()
    {
        execute(OperationEnum.EXECUTE_SEARCH);
    }

    /*
     * Not yet implemented
    public void INSERT_IGNORE()
    {
      execute( OperationEnum.INSERT_IGNORE);
    }
    */

    /**
     * TODO
     * Get rid of - didn't at time for better VC auditing
     */
    public void INSERT()
    {
        INSERT_AFTER_PLACE();
    }
    
    /*
    public void INSERT()
    {
        execute(OperationEnum.INSERT);
    }
    public void INSERT_PRIOR()
    {
        execute(OperationEnum.INSERT_PRIOR);
    }
    */
    public void INSERT_AT_PLACE()
    {
        execute(OperationEnum.INSERT_AT_PLACE);
    }
    public void INSERT_AFTER_PLACE()
    {
        execute(OperationEnum.INSERT_AFTER_PLACE);
    }

    public boolean POST()
    {
        boolean result = execute(OperationEnum.POST);
        /*
        times++;
        Err.pr( "POST will ret " + result + " times " + times);
        if(times == 2)
        {
          Err.stack();
        }
        */
        return result;
    }

    public boolean COMMIT_ONLY()
    {
        boolean result = execute(OperationEnum.COMMIT_ONLY);
        return result;
    }

    public boolean COMMIT_RELOAD()
    {
        boolean result = execute(OperationEnum.COMMIT_RELOAD);
        return result;
    }

    public boolean PREVIOUS()
    {
        boolean result = execute(OperationEnum.PREVIOUS);
        return result;
    }

    public boolean NEXT()
    {
        boolean result = execute(OperationEnum.NEXT);
        return result;
    }

    public void REMOVE()
    {
        execute(OperationEnum.REMOVE);
    }

    private void cap(InputControllerEvent event)
        throws ApplicationError
    {
        boolean couldGetOutOfIgnoredChild = true;
        /*
        times++;
        Err.pr( "controlActionPerformed for " + event.getID() + " "
        + oper.getCurrentBlock().getNode()
        + " times " + times);
        if(times == 2)
        {
        Err.debug();
        }
        */
        /*
        * This is a user setting
        if(event.getID() == OperationEnum.REMOVE || event.getID() == OperationEnum.EXECUTE_QUERY)
        {
        setValidationRequired( false);
        }
        */
        /*
        * If we are not operating from an Application that does this for us,
        * we may not have specified a current node before before (usually)
        * performing execute( OperationEnum.EXECUTE_QUERY). Then this code runs:
        */
        if(oper.getCurrentBlock() == null)
        {
            if(getNodes().isEmpty())
            {
                // All operations are on nodes, so if none then no point
                // return;
                Err.error("No nodes, so no wonder couldn't go to one to perform " + event.getReason());
            }
            else
            {
                Err.pr( "Strand in: <" + this + ">");
                Print.prList( getNodes(), "Its nodes");
                Err.error( "Should GOTO() one of the Strand's Nodes before performing a Strand operation, was trying to " + event.getReason());
            }
        }

        Node currentNode = (Node) oper.getCurrentBlock().getProdNodeI();
        Node targetNode = currentNode;
        /*
        times++;
        Err.pr( "-------------------");
        Err.pr( "currentNode: " + currentNode);
        Err.pr( "currentNode.isIgnoredChild(): " + currentNode.isIgnoredChild());
        Err.pr( "OPERATION: " + event.getReason());
        Err.pr( "currentNode.isAllowed( OPERATION): " + currentNode.isAllowed( event.getReason()));
        Err.pr( "times: " + times);
        Err.pr( "-------------------");
        if(times == 0)
        {
        Err.debug();
        }
        */
        if(currentNode.isAllowed(CapabilityEnum.IGNORED_CHILD)
            && !currentNode.isAllowed(event.getReason()))
        {
            targetNode = nodeCameFromB4Ignored;
            /*
            * This should never be removed - programmer will want to see
            * this
            */
            Print.pr(
                "In " + currentNode + " not allowed to " + event.getReason()
                    + ", so targetNode will use now " + targetNode);
            if(targetNode != null)
            {
                targetNode.goTo(true);
            }
            else
            {
                couldGetOutOfIgnoredChild = false;
            }
        }
        if(couldGetOutOfIgnoredChild)
        {
            if(!oper.getCurrentBlock().isAllowed(event.getReason()))
            {
                Session.error(
                    event.getReason() + " is not allowed (in current state: <" + 
                            oper.getCurrentBlock().getProdNodeI().getState()
                            + ">) for node "
                        + oper.getCurrentBlock().getProdNodeI() + ", named "
                        + oper.getCurrentBlock().getProdNodeI().getName());
            }
            if(!isIgnoreValidation())
            {
                generatePreOperationPerformedEvent(targetNode, event.getReason(), event.getRow());
                /*
                OutNodeControllerEvent outputEvent = new OutNodeControllerEvent( event.getReason(),
                targetNode, oper.getPreviousState());
                for(Iterator iter = preControlActionPerformedTriggers.iterator(); iter.hasNext();)
                {
                PreOperationPerformedTrigger trig = (PreOperationPerformedTrigger)iter.next();
                try
                {
                trig.preOperationPerformed( outputEvent);
                }
                catch(ValidationException ex)
                {
                Session.getErrorThrower().throwApplicationError( ex,
                ApplicationErrorEnum.PRE_CAP);
                }
                }
                */
                /*
                * User may have gone to another node, intending that that
                * is where the action is to be performed from:
                */
                targetNode = (Node) oper.getCurrentBlock().getProdNodeI();
                // Err.pr( "Current block is " + oper.getCurrentBlock());
            }
            OperationEnum eventId = event.getReason();
            startSdzHasControl( eventId);
            Session.getRecorder().openRecordOperation(eventId, null);

            StrandState state = currentStateReady();
            boolean ok = true;
            if(eventId == OperationEnum.INSERT_AT_PLACE)
            {
                state.addAt(this, targetNode);
            }
            else if(eventId == OperationEnum.INSERT_AFTER_PLACE)
            {
                state.addAfter(this, targetNode);
            }
            else if(eventId == OperationEnum.REMOVE)
            {
                state.remove(this, targetNode);
            }
            else if(eventId == OperationEnum.ENTER_QUERY)
            {
                state.enterQuery(this, targetNode);
            }
            else if(eventId == OperationEnum.EXECUTE_QUERY)
            {
                Err.pr( SdzNote.COMMIT_SELECT_DISPLAY, "EXECUTE_QUERY in " + NameUtils.tailOfPackage( this.getClass().getName()));
                state.executeQuery(this, targetNode);
            }
            else if(eventId == OperationEnum.EXECUTE_SEARCH)
            {
                state.executeSearch(this, targetNode);
            }
            else if(eventId == OperationEnum.POST)
            {
                Err.pr( SdzNote.SYNC, "To POST");
                state.post(this, targetNode, true);
                Err.pr( SdzNote.SYNC, "Done POST");
            }
            else if(eventId == OperationEnum.PREVIOUS)
            {
                StopWatch stopWatch = new StopWatch();
                if(WombatNote.VERSANT_TIMINGS.isVisible())
                {
                    stopWatch.startTiming();
                }
                state.previous(this, targetNode);
                if(WombatNote.VERSANT_TIMINGS.isVisible())
                {
                    stopWatch.stopTiming();
                    String howLong = stopWatch.getElapsedTimeStr();
                    Err.pr("Doing PREVIOUS took " + howLong);
                }
            }
            else if(eventId == OperationEnum.NEXT)
            {
                StopWatch stopWatch = new StopWatch();
                if(WombatNote.VERSANT_TIMINGS.isVisible())
                {
                    stopWatch.startTiming();
                }
                state.next(this, targetNode);
                if(WombatNote.VERSANT_TIMINGS.isVisible())
                {
                    stopWatch.stopTiming();
                    String howLong = stopWatch.getElapsedTimeStr();
                    Err.pr("Doing NEXT took " + howLong);
                }
            }
            else if(eventId == OperationEnum.SET_ROW)
            {
                state.setRow(this, targetNode, event.getRow());
            }
            else if(eventId == OperationEnum.COMMIT_ONLY)
            {
                Err.pr( SdzNote.COMMIT_SELECT_DISPLAY, "COMMIT_ONLY in " + NameUtils.tailOfPackage( this.getClass().getName()));
                internalCommitOnly( false);
            }
            else if(eventId == OperationEnum.COMMIT_RELOAD)
            {
                Err.pr( SdzNote.COMMIT_SELECT_DISPLAY, "COMMIT_RELOAD in " + NameUtils.tailOfPackage( this.getClass().getName()));
                ok = internalCommitReload( sdzHasControl(), targetNode);
            }
            else
            {
                Session.error("bad error, new OperationEnum required for " + eventId);
            }
            Session.getRecorder().closeRecordOperation();
            if(ok)
            {
                endSdzHasControl( eventId); //want to see NPE here
            }
            else
            {
                //Unwinding has aready been done 
            }
            generatePostOperationPerformedEvent(targetNode, event.getReason(), event.getRow());
            /*
            OutNodeControllerEvent outputEvent = new OutNodeControllerEvent( event,
            targetNode, oper.getPreviousState());
            for(Iterator iter = postControlActionPerformedTriggers.iterator(); iter.hasNext();)
            {
            PostOperationPerformedTrigger trig = (PostOperationPerformedTrigger)iter.next();
            trig.postOperationPerformed( outputEvent);
            }
            */
        }
    }

    private void controlActionPerformed(InputControllerEvent event)
        throws ApplicationError
    {
        boolean ok = true;
        moveManager.createNewValidationContext();

        MoveBlockI moveB = null;
        Node currentNode = getCurrentNode();
        if(currentNode != null)
        {
            moveB = (MoveBlockI) currentNode.getMoveBlock();
        }

        ApplicationError error = null;
        if(!getNodes().isEmpty())
        {
            try
            {
                moveManager.errorSite(ErrorSiteEnum.CONTROL_ACTION, event.getReason());
                // Err.pr( "------------>To closeTo");
                moveManager.closeTo(EntrySiteEnum.ACTION_LISTENER);
                cap(event);
                moveManager.openAgainTo(EntrySiteEnum.ACTION_LISTENER);
                moveManager.b4HandleErrorProcessing(null, moveB, event.getReason());
            }
            catch(ApplicationError err)
            {
                if(moveManager.error(ErrorSiteEnum.CONTROL_ACTION))
                {
                    // Throw it again b/cause this is an inner ApplicationError catcher.
                    throw err;
                }
                ok = false;
                error = err;
                Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "ApplicationError about to handle is " + err.getMsg());
                moveManager.b4HandleErrorProcessing(err, moveB, event.getReason());
                fireErrorHandler(err);
                // Print.pr( ">>>>>EXIT from fireErrorHandler in Strand.controlActionPerformed");
            }
            finally
            {
                moveManager.postValidationProcessing(ok, error);
            }
        }
    }

    void _internalCommitReload()
    {
        Node currentNode = (Node) oper.getCurrentBlock().getProdNodeI();
        int row = currentNode.getRow();
        internalCommitOnly( false);
        EXECUTE_QUERY();
        // Err.pr( "Will go to row " + row);
        SET_ROW(row);
    }

    /**
     * Bit of a hack for following reasons:
     * 1./ Will error for multiple parents
     * 2./ Does its job but does not change anything visually. Need to
     * effect idea of SdzBag etc being able to GOTO() as
     * well ie. whole framework for this
     * 3./ Will replaying still work. Err - probably - as Recorder
     * has a stack mechanism - just haven't tested.
     * 4./ How will this be incorporated with future framework
     * where first GOTO before first QUERY/SEARCH will be a
     * marker? Could be a marker for this method (rather than
     * going right to the top). Framework is all about
     * designating the first action that a user performs, and
     * in which node. If an error occurs can ignore all further
     * API requests - until of course the next one being marked
     * as a request from the user.
     * For now we are solving this problem by returning a boolean
     * that says whether this succeeded or not.
     */
    private boolean goToTopMost(Node currentNode)
    {
        boolean result = true;
        List parents = currentNode.getParents();
        if(parents.size() == 0)
        {// fine
        }
        else if(parents.size() == 1)
        {
            Node parent = (Node) parents.get(0);
            result = parent.GOTO();
            if(result)
            {
                goToTopMost(parent);
            }
        }
        else
        {
            Err.error("Need ordinal for which of many parents to go to");
        }
        return result;
    }

    boolean internalCommitReload( boolean sdzHasControl, Node node)
    {
        boolean result;
        timesCommitReload++;
        Err.pr( SdzNote.SDZ_HAS_CONTROL, "internalCommitReload(), sdzHasControl " + sdzHasControl +
            " for node " + node + ", ID: " + node.getId() + " times " + timesCommitReload);
        if(timesCommitReload == 0)
        {
            Err.stack();
        }
        boolean ok = goToTopMost((Node) oper.getCurrentBlock().getProdNodeI());
        if(ok)
        {
            Node currentNode = (Node) oper.getCurrentBlock().getProdNodeI();
            CopyPasteBuffer copyPasteBuffer = new CopyPasteBuffer();
            List attribs = Arrays.asList(currentNode.getAttributes());
            // Want all attributes, so this would be silly (used for searching
            // when started off with a blank screen).
            // List attribs = Attribute.getNonBlankChangedAttributes( currentNode.getAttributes());
            copyPasteBuffer.copyItemValues(attribs, currentNode);
            internalCommitOnly( false);
            ENTER_QUERY();
            copyPasteBuffer.pasteItemValues();
            EXECUTE_SEARCH();
        }
        result = ok;
        return result;
    }

    /**
     * ORDER is important here. post will make sure the UI updates the list,
     * so it must be done in a current transaction. PRE_CLOSE
     * may abort or txn.commit, so anytime after can be sure a txn.commit has
     * been done, so can make the applichousing alterations ie./ not allowing delete
     * or update if that is normally the case.
     */
    void internalCommitOnly( boolean removeComboItems)
    {
        Node currentNode = (Node) oper.getCurrentBlock().getProdNodeI();
        currentStateReady().post(this, currentNode, false);
        if(!isIgnoreValidation())
        {
            /*
            * fire the event, where user may close the transaction
            */
            final TransactionEvent evt = new TransactionEvent(TransactionEvent.PRE_CLOSE);
            /*
            for(Iterator e = transactionPublisher.iterator(); e.hasNext();)
            {
              CloseTransactionTrigger tl = (CloseTransactionTrigger)e.next();
              // new MessageDlg("Telling to TransactionEvent.PRE_CLOSE to " + tl);
              try
              {
                tl.perform( evt);
              }
              catch(ValidationException ex)
              {
                Session.getErrorThrower().throwApplicationError( ex,
                    ApplicationErrorEnum.TRANSACTION);
              }
            }
            */
            transactionPublisher.publish
                (
                    new Publisher.Distributor()
                    {
                        public void deliverTo(Object subscriber)
                        {
                            CloseTransactionTrigger ctt = (CloseTransactionTrigger) subscriber;
                            try
                            {
                                ctt.perform(evt);
                            }
                            catch(ValidationException ex)
                            {
                                Session.getErrorThrower().throwApplicationError(ex,
                                    ApplicationErrorEnum.TRANSACTION);
                            }
                        }
                    }
                );
        }
        for(Iterator e = nodes.iterator(); e.hasNext();)
        {
            Node node = (Node) e.next();
            Block block = node.getBlock();
            if(block == null)
            {
                Session.error("serious problem");
            }
            block.closedStateOKConfirmed(); // will empty insertedList, and isInsert = false
            // 29/09/03 - Don't want user to have access to data after have committed it.
            node.setBlockNull();
            block.setFreshDataRecords();
            block.zeroIndex();
            //
        }
        consumeNodesIntoRT( (Node)null, "commit done", null, false); // newBlock done by all, so all s/be made frozen
        // consumeNodesIntoRT did not set a current block, but did create
        // a whole new load of blocks for the nodes we have
        oper.discreteSetCurrentBlock( currentNode.getBlock());
        for(Iterator e = nodes.iterator(); e.hasNext();)
        {
            Node node = (Node) e.next();
            // setNewCapabilities( node, false);
            node.getBlock().internalBlankoutDisplay(OperationEnum.COMMIT_ONLY, "internalCommitOnly", removeComboItems);
        }
        currentStateReady().visualCursorChange(this, currentNode);
        // post above calls this, so guessing it's not needed!
        // oper.ifInsertedDeleteActions();
        //
        if(!isIgnoreValidation())
        {
            /*
             * This POST_CLOSE_SUCCESS not used or really required
             *
            TransactionEvent evt = new TransactionEvent(
                TransactionEvent.POST_CLOSE_SUCCESS);
            for(Iterator e = transactionPublisher.iterator(); e.hasNext();)
            {
              CloseTransactionTrigger tl = (CloseTransactionTrigger)e.next();
              // new MessageDlg("Telling to DataFlowEvent.POST_CLOSE_SUCCESS to " + tl);
              try
              {
                tl.perform( evt);
              }
              catch(ValidationException ex)
              {
                Session.getErrorThrower().throwApplicationError( ex,
                    ApplicationErrorEnum.TRANSACTION);
              }
            }
            */
        }
    }

    /**
     * If an ApplicationError has been thrown whilst trying to change the current
     * node, then this s/ware will think that the current node has not changed.
     * However as we can't veto the FocusChange event, the focus change will have
     * gone ahead. Thus if we move around in the bad node, or drag the window,
     * we will be getting focusChange events that need to be ignored. The param
     * here is the node we need to ignore.
     */
    public void windowsWorkaround(Node ignoreNode)
    {
        Print.pr("inside windowsWorkaround()");
        Session.error("inside windowsWorkaround()");
        if(this.ignoreNode != null)
        {
            Session.error("expect ignoreNode to be null");
        }
        this.ignoreNode = ignoreNode;
    }

    private StrandState currentStateReady()
    {
        Block currentBlock = oper.getCurrentBlock();
        if(currentBlock == null)
        {
            String txt = "Strand <" + this + "> does not have a current Node";
            Session.error("No current block");
        }
        return currentState;
    }

    public int getMode()
    {
        return currentState.getMode(this);
    }

    public void setCurrentState(StrandState currentState)
    {
        this.currentState = currentState;
    }

    public String toString()
    {
        return old_toString();
        /*
        String result = "no name Strand";
        if(name != null)
        {
        result = name;
        }
        return result;
        */
    }

    public String old_toString()
    {
        String result = "Strand:";
        if(nodes.size() == 0)
        {
            result += "[no nodes]";
        }
        else
        {
            for(Iterator e = nodes.iterator(); e.hasNext();)
            {
                result += e.next() + " ";
            }
        }
        return result;
    }

    public boolean validateBean()
    {
        return validateBean(true);
    }

    public boolean validateBean(boolean childrenToo)
    {
        boolean ok = true;
        String errMsg = null;
        validateBeanMsg.clear();
        if(childrenToo && nodes.size() == 0)
        {
            errMsg = NO_NODES_VAL_MSG;
            //Err.stack();
        }
        else
        {
            if(childrenToo)
            {
                for(Iterator e = nodes.iterator(); e.hasNext();)
                {
                    Node node = (Node) e.next();
                    boolean nok = node.validateBean();
                    if(!nok)
                    {
                        String txt = node.getName();
                        if(txt == null)
                        {
                            txt = node.toString();
                        }

                        List list = node.retrieveValidateBeanMsg();
                        if(list.size() != 1)
                        {
                            Session.error("Expected one error only");
                        }
                        errMsg = "Node <" + txt + "> " + list.get(0);
                        break;
                    }
                }
            }
        }
        if(errMsg != null)
        {
            ok = false;
            validateBeanMsg.add(errMsg);
        }
        return ok;
    }

    public List<Node> getNodes()
    {
        return nodes;
    }

    public List getAttributes()
    {
        List result = new ArrayList();
        List nodes = getNodes();
        for(Iterator iter = nodes.iterator(); iter.hasNext();)
        {
            Node node = (Node) iter.next();
            Attribute attributes[] = node.getAttributes();
            for(int i = 0; i <= attributes.length - 1; i++)
            {
                result.add(attributes[i]);
            }
        }
        return result;
    }
    
    public boolean hasChanged()
    {
        boolean result = false;
        List nodes = getNodes();
        for(Iterator iter = nodes.iterator(); iter.hasNext();)
        {
            Node node = (Node) iter.next();
            if(node.hasChanged())
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public Node getNodeByName(String name)
    {
        Node result = null;
        for(Iterator iter = nodes.iterator(); iter.hasNext();)
        {
            Node node = (Node) iter.next();
            if(name.equals(node.getName()))
            {
                result = node;
                break;
            }
        }
        return result;
    }

    /*
    public ml.record.NodeI getNodeIByName( String name)
    {
    return (NodeI)getNodeByName( name);
    }
    */

    public void setAlreadyPerformingGoNode(boolean b)
    {
        /*
        times++;
        Err.pr( "setAlreadyPerformingGoNode to " + b + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        alreadyPerformingGoNode = b;
    }

    public String getName()
    {
        return name;
    }

    public String toShow()
    {
        return getName() + " ID: " + id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List retrieveValidateBeanMsg()
    {
        return validateBeanMsg;
    }

    public IconEnum getIconEnum()
    {
        Err.error("Not yet coded the method getIconEnum() for a Strand");
        return null;
    }

    public boolean isAlreadyBeenCustomized()
    {
        return alreadyBeenCustomized;
    }

    public void setAlreadyBeenCustomized(boolean alreadyBeenCustomized)
    {
        this.alreadyBeenCustomized = alreadyBeenCustomized;
    }

     /**/
    void setIgnoreValidation(boolean ignore)
    {
        ignoreValidation = ignore;
    }

    public boolean isIgnoreValidation()
    {
        return ignoreValidation;
    }

     /**/
    public void replayRecorded(
        String filename, List startupCode)
    {
        replayRecorded(filename, null, startupCode);
    }

    public void replayRecorded(
        String filename, Recorder recorder, List startupCode)
    {
        XMLRecorderData dataStore = null;
        boolean alreadyRead = false; // not a bit silly?
        String file = null;
        if(recorder != null)
        {
            if(recorder.getDataStore() != null)
            {
                if(recorder.getDataStore().getFileName().equals(filename))
                {
                    alreadyRead = true;
                }
            }
            file = recorder.getFileName();
        }
        else
        {
            file = filename;
            recorder = new Recorder();
        }
        if(!alreadyRead)
        {
            dataStore = new XMLRecorderData(file, Recorder.CLASSES);
            dataStore.rollbackTx(); // as not writting away at end
            dataStore.startTx();
        }
        else
        {
            dataStore = recorder.getDataStore();
        }
        Recorder.prPlay("TO PLAY: " + file);

        List recording = (List) dataStore.get(Operation.class);
        if(recording == null)
        {
            Err.error("Could not get a recording from " + dataStore);
        }

        Node currentNode = null;
        int i = 0;
        for(Iterator iter = recording.iterator(); iter.hasNext(); i++)
        {
            Operation operation = (Operation) iter.next();
            if(operation.getOperation() == null)
            {
                Err.error(
                    "No operation - probably caused by tampering with the XML play file");
            }
            else if(operation == null)
            {
                Err.error("No operation");
            }
            playTimes++;
            Recorder.prPlay("PLAYING: <" + operation + ">, times " + playTimes);
            if(playTimes == 6)
            {// Err.debug();
            }
            if(operation instanceof GoToOperation)
            {
                GoToOperation goToOperation = (GoToOperation) operation;
                String nodeName = goToOperation.getNodeName();
                if(nodeName == null)
                {
                    Err.error("nodeName null in GoToOperation " + goToOperation);
                }
                currentNode = getNodeByName(nodeName);
                if(currentNode == null)
                {
                    Err.error(
                        "Was not able to find a node called <" + nodeName
                            + "> in Strand <" + this.getName() + ">");
                }
                if(recorder.isStartupOperation(i, goToOperation, startupCode))
                {
                    continue;
                }

                OperationEnum op = goToOperation.getOperation();
                if(op.equals(OperationEnum.GOTO_NODE))
                {
                    boolean ok = currentNode.GOTO();
                    if(!ok)
                    {
                        Err.error("Failed to GOTO " + goToOperation.getNodeName());
                    }
                    /*
                    else
                    {
                    pr( "Was able to GOTO " + goToOperation.getNodeName());
                    }
                    */
                }
                else if(op.equals(OperationEnum.GOTO_NODE_IGNORE))
                {
                    currentNode.GOTOWithoutValidation();
                }
                else
                {
                    Err.error("Unknown type of GoToOperation: " + op);
                }
                /*
                * Put this sleep in when going to a different node
                * was setting off a 'screen change'. This works,
                * although would be better to tell the event dispatch
                * thread to block while flushing itself out. Not sure
                * how to do this in Swing. And if do, it will have to
                * be an info package call.
                */
                try
                {
                    Thread.sleep(300);
                }
                catch(InterruptedException e)
                {
                    // nufin
                    e.printStackTrace();
                }
            }
            else if(operation instanceof SimpleOperation)
            {
                SimpleOperation simpleOperation = (SimpleOperation) operation;
                if(recorder.isStartupOperation(i, simpleOperation, startupCode))
                {
                    continue;
                }

                OperationEnum op = simpleOperation.getOperation();
                currentNode.execute(op);
                try
                {
                    Thread.sleep(300);
                }
                catch(InterruptedException e)
                {
                    // nufin
                    e.printStackTrace();
                }
            }
            else if(operation instanceof SetValueOperation)
            {
                SetValueOperation setOp = (SetValueOperation) operation;
                String cellName = setOp.getCellName();
                String attributeName = setOp.getAttributeName();
                RuntimeAttribute attr = currentNode.getAttributeByCellAndName(cellName,
                    attributeName);
                if(attr == null)
                {
                    attr = currentNode.getAttributeByName(attributeName);
                    if(attr == null)
                    {
                        Print.pr(currentNode);
                        Print.prArray(currentNode.getAttributes(), "Strand.replayRecorded()");
                        Err.error(
                            "Could not find attribute for CELL: " + cellName
                                + ", ATTRIBUTE: " + attributeName);
                    }
                }
                // Object obj = attr.getAdapter().getCell().getValue( node.getRow());
                // pSelfReference.invoke( obj, setOp.getMethod(), setOp.getArg());
                attr.setItemValue(setOp.getArg());
                try
                {
                    Thread.sleep(300);
                }
                catch(InterruptedException e)
                {
                    // nufin
                    e.printStackTrace();
                }
            }
        }
        /*
        * If write back then commented-out sections will
        * be removed.
        dataStore.writeData();
        */
    }

    public SdzEntityManagerI getEntityManager()
    {
        Err.pr(SdzNote.EMP_ERRORS, "To getEntityManager() from " + this);
        SdzEntityManagerI result = null;
        if(entityManagerTrigger != null)
        {
            entityManager = entityManagerTrigger.fetchEM();
        }
        if(entityManager == null)
        {
            /*
             * Here there is either no trigger or the trigger is returning null. Where a strand
             * has no dataStore or the dataStore is not entity managed (eg XMLDatastore) then
             * either of these will be the case.
             */
            Err.pr( SdzNote.EM_BECOMES_NULL, "Creating a Null Entity Manager in <" + this + ">");
            entityManager = EntityManagerFactory.createNullSdzEMI();
        }
        result = entityManager;
        return result;
    }
    
    private void startSdzHasControl( OperationEnum opEnum)
    {
        indentationCounter.indent( true, opEnum);
        timesSdzControl++;
        Err.pr( SdzNote.SDZ_HAS_CONTROL, "startSdzHasControl() times " + timesSdzControl);
        if(timesSdzControl == 0)
        {
            Err.stack();
        }
    }
    
    private void endSdzHasControl( Object id)
    {
        indentationCounter.indent( false, id);
    }
    
    public boolean sdzHasControl()
    {
        return indentationCounter.isDoingOneOf( OperationEnum.ALL_KNOWN_OPERATIONS);
    }

    public class OwnPostOperationTrigger
        implements PostOperationPerformedTrigger
    {
        public void postOperationPerformed(OutNodeControllerEvent evt)
        {
            timesOp++;
            Err.pr( SdzNote.RECORD_CURRENT_VALUE, "In postOperationPerformed for " + evt.getNode() + " with evt " + evt.getID() + " in state " + evt.getNode().getState() + 
            " times " + timesOp);
//            if(timesOp == 9 || timesOp == 10)
//            {
//                Err.debug();
//            }
            if(evt.getID() == OperationEnum.GOTO_NODE
                /*    
                && evt.getNode().getState() != StateEnum.FROZEN
                */
                    )
            {
                evt.getNode().recordCurrentValue( "Just landed in " + evt.getNode(), evt.getRow(), 
                                                  evt.getNode().getState(), (OperationEnum)evt.getID());
            }
            else if(evt.getNode().getState() == StateEnum.FROZEN)
            {
                if(evt.getID() == OperationEnum.SET_ROW)
                {
                    evt.getNode().recordCurrentValue( "Set Row done in frozen " + evt.getNode(), evt.getRow(), 
                                                      evt.getNode().getState(), (OperationEnum)evt.getID());
                }
            }
            else if(evt.getNode().getState() != StateEnum.FROZEN)
            {
                Err.pr(SdzNote.RECORD_CURRENT_VALUE_DETAILS, "It's NOT frozen when GOTO: " + evt.getNode());
                Err.pr(SdzNote.RECORD_CURRENT_VALUE_DETAILS, "? Is INSERT here: " + evt.getID());
                if(SdzNote.RECORD_CURRENT_VALUE_DETAILS.isVisible() && "Insert".equals( evt.getID().toString()))
                {
                    Err.debug();
                }
                if(evt.getID() == CapabilityEnum.EXECUTE_QUERY)
                {
                    evt.getNode().recordCurrentValue( "Query done in " + evt.getNode(), evt.getRow(), 
                                                      evt.getNode().getState(), (OperationEnum)evt.getID());
                }
                else if(evt.getID() == CapabilityEnum.INSERT_AFTER_PLACE
                    || evt.getID() == CapabilityEnum.INSERT_AT_PLACE)
                {
                    //Don't do it here as extent will not yet have anything - only
                    //time we can do it is after backgroundAdd
                    //evt.getNode().recordCurrentValue( "Inserting in " + evt.getNode());
                }
            }
        }
    }

    public boolean isTopLevel( Node node)
    {
        return mdTiesManager.isTopLevel( node.getBlock());
    }

    /**
     * Called as part of connecting for the first time.
     * The interface EntityManagerReceiverI as used by
     * connection tasks (for example MemoryWombatConnectionTask).
     *
     * Would be better if it wasn't public.
     * @param em
     */
    /* Don't need because using entityManagerTrigger instead
    public void _setEntityManager( SdzEntityManagerI em)
    {
      if(entityManager != null && entityManager.getORMType() == ORMTypeEnum.NULL)
      {
        Err.error( "An assumption about the EntityManager has already been made - call this method earlier");
      }
      else if(em == null)
      {
        Err.pr( "ERROR, Having a null em is ok, but setting it to null possibly reflects a bug");
      }
      else
      {
        Err.pr( SdzNote.bgAdd, "em using: " + em);
        entityManager = em;
        //setEntityManagerProviderForAllExtents();
      }
    }
    */

    /**
     * This didn't work because extents are re-created again and again.
     * @param em
     */
    /*
    private void setEntityManagerProviderForAllExtents()
    {
      for(Iterator e = nodes.iterator(); e.hasNext();)
      {
        Node node = (Node)e.next();
        Block block = node.getBlock();
        VisibleExtent extent = block.getDataRecords();
        extent.setEntityManagerProvider( this);
      }
      List nodes = getNodes();
      for(Iterator iter1 = nodes.iterator(); iter1.hasNext();)
      {
        Node node = (Node)iter1.next();
        List cells = node.getCells();
        for(Iterator iter2 = cells.iterator(); iter2.hasNext();)
        {
          Cell cell = (Cell)iter2.next();
          cell._setEntityManagerProvider( this);
        }
      }
    }
    */
}
