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

import org.strandz.core.domain.OtherSignatures;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.ControlActionListener;
import org.strandz.core.domain.event.DynamicAllowedEvent;
import org.strandz.core.domain.event.InputControllerEvent;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.core.prod.ProdNodeI;
import org.strandz.core.prod.Session;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is where all user interaction goes through.
 * As there is only one user per application, there is only one
 * of these concrete objects in existence at RT. The actual events
 * may originate from the keyboard or mouse, but are all headed here.
 * Once here the events become something like 'go to the next row'.
 * <p/>
 * A NodeController is always associated with one node. As each node
 * becomes the current one (by getting the focus through a goNode() method
 * call or when the user focuses on it) it tells the NodeController what it
 * is allowed to do, and the NodeController reacts accordingly, for example
 * by disabling buttons.
 * <p/>
 * If the 'go to the next row' button is enabled then this event will travel
 * on to the node, which will increment the position of the cursor, and the
 * data in the user's display will change.
 * <p/>
 * At RT a user may switch between different Strands. A switch away causes
 * the creation of a memento. When the user comes back to the same Strand,
 * its state at the time the user switched away can can be restored by
 * applying back the saved ControllerMemento. This is what happens during
 * runtime.
 * <p/>
 * * OLD COMMENTS:
 * This class exists to create and pass
 * events to whatever the physical embodiment of the
 * controller is. Each Node sends events to this class thru the
 * ControlActionListener interface. A Strand is a container for a
 * group of Nodes that participate in the same "applichousing transaction".
 * These Nodes use this class to constantly change what the end
 * user is allowed to do.
 * <p/>
 * Some events do not go through here ... ignoredChild, setRow ...
 * REALLY?
 * <p/>
 * The other side of this class is to take events that are
 * passing from the physical controller to go on to the
 * AbstStrand - eg insert has been pressed.
 * <p/>
 * Third purpose is to inform the physical embodiment
 * when a transaction has changed. Needed to be used by a physical
 * embodiment that needs to know when the transaction has changed -
 * for example one that contains a NodeBar.
 * <p/>
 * Second last purpose is to keep Memento.
 * <p/>
 * Last purpose is to do anything that must be done only once.
 * <p/>
 * If a user wasn't using a type of Application then he would
 * have to construct a NodeController and an (eg) Toolbar
 * seperately and then stick them together with
 * <code>nodeController.setPhysicalController( toolBar);</code>
 * <p/>
 * This class could theoretically be folded into being an inner
 * class of Strand to make it clear that the user never uses
 * it. (Using relay of setPhysicalEmbodiment() to call
 * setPhysicalController as in above paragraph).
 * <p/>
 * When want to make so that delete is visually unenabled,
 * rather than visually hidden, then get/setAllowed should
 * reflect real reason eg./ setAllowedInsertedRec() and
 * getAllowedInsertedRec(). (Have ints instead to show
 * mutual exclusivity). NON_INSERTED_REC, INSERTED_REC. Thus
 * any NodeController can decide its own response.
 *
 * @author Chris Murphy
 */
public class NodeController extends AbstNodeController
    implements Serializable, ControlActionListener
{
    private ActualNodeControllerI controllerForType;
    /**
     * Values of these are constantly changing, depending on the values
     * for the current Node, and then on whether the current record is an
     * insert or not. Initial values are all true, so initially events
     * (InputControllerEvent to PhysicalNodeControllerInterface) will be sent out only to buttons
     * that are to be false. Thus physical controller s/initially have all
     * buttons visible.
     * Note purpose of this state is only to know what the state of the
     * current physical embodiment of this class should be. Recording
     * seperately here has advantage that can check thst controlActionPerformed
     * events are actually allowed. Recording seperately also makes it easy to
     * implement a memento.
     */
    private ControllerState currentState = new ControllerState();
    /**
     * So can exist independently in Beans environment
     * AbstStrand only exists because of this need for
     * nulls - it is not intended to have different types of
     * NodeTransactions
     */
    private StrandI strand = new NullStrand();
    /**
     * forceCapabilityChanges (=true) may not be necessary when come to real designtime/runtime
     * environment. To do with property defaults and overridding them. Investigate then.
     * See setHowDisplayNotAllowed()
     */
    private boolean forceCapabilityChanges = true;
    private static int times;
    private static int times1;
    private static int constructedTimes;
    private int id;

    /**
     * Place to do anything that only want done once.
     */
    public NodeController()
    {
        OtherSignatures.setNotNullConstructors();
        constructedTimes++;
        id = constructedTimes;
        // Err.pr( "NodeController: " + id);
    }

    public int getId()
    {
        return id;
    }

    /**
     * Used to restore NodeController back to the state it once was in. Also
     * first time ever set, the abstract displayed transaction set visible
     * = true will give the physical controller the false events it requires.
     */
    public void setControllerMemento(NodeController.ControllerMemento cm)
    {
        /*
        Err.pr( "Inside setControllerMemento where will call NodeController.setAbilities");
        */
        ControllerState state;
        if(cm == null)
        {
            state = currentState;
        }
        else
        {
            state = cm.getState();
        }

        DynamicAllowedEvent ce = null;
        for(int i = 0; i < OperationEnum.ALL_CAPABILITIES.length; i++)
        {
            CapabilityEnum capability = CapabilityEnum.ALL_CAPABILITIES[i];
            if(capability.isOperation() && capability != CapabilityEnum.UNKNOWN)
            {
                ce = new DynamicAllowedEvent(capability);
                ce.setAllowed(state.get(capability));
                ce.setDynamicAllowed(state.get(capability));
                setDynamicAllowed(ce);
            }
        }
        setAbilities(state.getAbilities());
    }

    /**
     * Used to take a copy of the current state, that another object
     * (Strand) may keep it, and pass it back again.
     */
    public NodeController.ControllerMemento createControllerMemento()
    {
        /*
        * This not required as following line does a deep copy
        * of the state that is passed to it.
        *
        * ControllerState state = new ControllerState( currentState);
        */
        return new ControllerMemento(currentState);
    }
    
    public StrandI getStrand()
    {
        return strand;
    }

    /**
     * Will become a property that can set in Beans environment.
     * WHY ?? The Strand will always be switched as for example TAB. This
     * handled by AbstractVisualStrands. Thus for user NodeController is
     * a property of StrandControl.
     */
    public void setStrand( StrandI strand)
    {
        // so NodeController can be notified of BlockChange events
        // (the relevant Nodes will be notified of StateChange events)
        // new MessageDlg("NodeController being set with Strand: " + strand);
        this.strand = strand;
        strand.setNodeController(this);
        //
        /*
        for(Iterator en=physicalControllerList.iterator(); en.hasNext(); )
        {
        PhysicalNodeControllerInterface al = (PhysicalNodeControllerInterface)en.next();
        al.strandChanged( strand);
        }
        */
        if(controllerForType != null)
        {
            controllerForType.strandChanged( strand);
        }
        /*
        else
        {
        times++;
        Err.pr( "!! WILL NOT strandChanged(): " + strand + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        }
        */
    }

    private void initUncapable()
    {
        DynamicAllowedEvent ce = null;
        for(int i = 0; i < OperationEnum.ALL_CAPABILITIES.length; i++)
        {
            CapabilityEnum capability = CapabilityEnum.ALL_CAPABILITIES[i];
            if(capability.isOperation() && capability != OperationEnum.UNKNOWN)
            {
                ce = new DynamicAllowedEvent(capability);
                ce.setAllowed(false);
                ce.setDynamicAllowed(false);
                setDynamicAllowed(ce);
            }
        }
    }

    public void setPhysicalController(ActualNodeControllerI al)
    {
        if(al != null)
        {
            al.removeAllControlActionListeners();
            controllerForType = al;
            initUncapable();
            controllerForType.addControlActionListener(this);
            times1++;
            Err.pr( SdzNote.WRONG_PHYSICAL, "######## Setting ActualNodeControllerI: " + al.getId() + " where strand is " + getStrand());
            if(times1 == 0)
            {
                Err.stack();
            }
        }
        else
        {
            Err.pr("!! will not setPhysicalController()");
        }
    }

    // XMLEncode
    public ActualNodeControllerI getPhysicalController()
    {
        /*
        PhysicalNodeControllerInterface result = null;
        if(physicalControllerList.size() >= 1)
        {
        result = (PhysicalNodeControllerInterface)physicalControllerList.get(0);
        }
        return result;
        */
        return controllerForType;
    }

    /*
    private void removeAllPhysicalControllers()
    {
    //physicalControllerList.clear();
    controllerForType = null;
    }
    */

    /*
    * Multiples never tested so keep this code away from public. This called
    * by setPhysicalController.
    */
    /*
    private void addPhysicalController( PhysicalNodeControllerInterface al)
    {
    **
    physicalControllerList.add( al);
    al.addControlActionListener( this);
    if(physicalControllerList.size() == 1)
    {
    //This silly?
    //As setHowDisplayNotAllowed is property that will always be set, that does
    //this, then probably not necessary come real design/realtime.
    initUncapable();
    }
    //Err.pr( "As have added PhysicalController, size is now " +
    //                    physicalControllerList.size());
    **
    //controllerForType.addControlActionListener( this);
    }
    */

    /*
    public void removePhysicalController( PhysicalNodeControllerInterface al)
    {
    physicalControllerList.remove( al);
    }
    */

    /**
     * This is only called when part of an Application
     */
    public void setVisible(boolean b)
    {
        /*
        for(Iterator en=physicalControllerList.iterator(); en.hasNext(); )
        {
        PhysicalNodeControllerInterface al = (PhysicalNodeControllerInterface)en.next();
        al.setVisible( b);
        }
        */
        if(controllerForType != null)
        {
            controllerForType.setVisible(b);
        }
        else
        {// Session.error( "What are we going to setVisible " + b);
        }
    }

    public void execute(InputControllerEvent e)
    {
        checkTransaction();
        if(e.getReason() == OperationEnum.SET_ROW)
        {
            strand.SET_ROW(e.getRow());
        }
        else
        {
            strand.execute(e.getReason());
        }
    }
    
    public void goNode( Object node, int row)
    {
        checkTransaction();
        if(strand.getCurrentNode() != node)
        {
            StateEnum state = getState(); 
            if(StateEnum.ENTER_QUERY != state)
            {
                strand.goNode( (Node)node, row);
            }
            else
            {
                Err.error( "Application should not be trying to go to another node (" + 
                        node + ") whilst in " + state);
            }
        }
    }
    
    public StateEnum getState()
    {
        return strand.getCurrentNode().getState();
    }

    public ActualNodeControllerI createDefaultControllerForType()
    {
        /* Q: If none specified in Designer then why create one here?
         * A: Because a NodeController can be manually created, as SdzDsgnr does for example.
         */
        if(controllerForType == null)
        {
            try
            {
                setPhysicalController(
                    (ActualNodeControllerI) OtherSignatures.getDefaultPhysicalControllerType().newInstance());
            }
            catch(InstantiationException e)
            {
                Session.error(e);
            }
            catch(IllegalAccessException e)
            {
                Session.error(e);
            }
            controllerForType.strandChanged(strand);
        }
        return getPhysicalController();
    }

    /*
    * TODO NOTE - events only sent out if dynamic allowed has changed. Should
    * also send out events if reason has changed. Test what are getting
    * here first. Trigger for this will be when want to implement
    * greying out buttons. Will probably also need to give information
    * as to whether a block has been changed. PhysicalNodeControllerInterface could
    * have a property as to what level of detail it wanted to receive
    * ControllerEvents. Thus on simple case, as now, alot of information
    * in the event would not be useable. If really wanted could implement
    * the creation of the event as a builder!
    */
    private boolean setComponent(boolean currentlyAllowed,
                                 DynamicAllowedEvent e)
    {
        boolean wantAllow = e.getDynamicAllowed(); // So may have created an even for
        // no reason! - happens in response
        // to user pressing a button, so
        // performance not an issue.
        /*
        new MessageDlg("For " + e +
        "\n currentlyAllowed: " + currentlyAllowed +
        "\n wantAllow " + wantAllow);
        */
        if(forceCapabilityChanges || wantAllow != currentlyAllowed)
        {
            /*
            for(Iterator en=physicalControllerList.iterator(); en.hasNext(); )
            {
            PhysicalNodeControllerInterface al = (PhysicalNodeControllerInterface)en.next();
            al.allowedPerformed( e);
            }
            */
            if(controllerForType != null)
            {
                if(!wantAllow)
                {
                    controllerForType.allowedPerformed(e);
                }
                else if(wantAllow && e.isAllowed())
                {
                    controllerForType.allowedPerformed(e);
                }
            }
            else
            {
                Err.pr( SdzNote.NO_HOUSING_HELPERS, "!! will not setComponent()");
            }
        }
        return wantAllow;
    }

    /**
     * Sets the currentState as well as physically setting
     * the component.
     */
    public void setDynamicAllowed(DynamicAllowedEvent e)
    {
        createDefaultControllerForType();

        CapabilityEnum capability = e.getID();
        boolean b = setComponent(currentState.get(capability), e);
        /*
        if(e.getID() == OperationEnum.SET_ROW)
        {
        times++;
        Err.pr( "%--% NodeController.setDynamicAllowed: " + e.getID() + " to " + b + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        }
        */
        currentState.set(capability, b);
    }

    public boolean getDynamicAllowed(OperationEnum capability)
    {
        // Err.pr( "Getting allowed for NodeController on capability " +
        // (new InputControllerEvent( capability)).toString());
        return currentState.get(capability);
    }

    /**
     * Sets the currentState as well as physically setting
     * the component. Is called from two places:
     * Strand.setNewCapabilities
     * this.setControllerMemento
     * There is an overlap in that the momento call is always
     * made (always = whenever an environ.AbstractVisibleStrand
     * is made visible), whilst setNewCapabilities is only called
     * whenever you first ever leaveBlock on a strand.
     */
    void setAbilities(List abilities)
    {
        // Err.pr( "Abilities buttons to be physically set");
        /*
        for(Iterator en=physicalControllerList.iterator(); en.hasNext(); )
        {
        PhysicalNodeControllerInterface p = (PhysicalNodeControllerInterface)en.next();
        p.setAbilities( abilities);
        }
        */
        if(controllerForType != null)
        {
            controllerForType.setAbilities(abilities);
        }
        else
        {
            if(abilities != null && !abilities.isEmpty())
            {
                Print.prList( abilities, "Abilities that will not be set, due to not having a controller");
            }
        }
        currentState.setAbilities(abilities);
    }

    private void checkTransaction()
    {
        if(strand instanceof NullStrand)
        {
            String msg = "This Controller has not yet been associated with a current Transaction";
            Session.getErrorThrower().throwApplicationError(msg,
                ApplicationErrorEnum.INTERNAL);
        }
    }

    /**
     * Called as result of a StateChangeEvent
     */

    public ProdNodeI getCurrentNode()
    {
        return strand.getCurrentNode();
    }

    class ControllerMemento
    {
        private ControllerState state;

        public String toString()
        {
            return state.toString();
        }

        ControllerMemento(ControllerState state)
        {
            this.state = new ControllerState(state); // way of doing a deep copy
        }

        private ControllerState getState()
        {
            return state;
        }
    } // end ControllerMemento class


    private class ControllerState
    {
        /**
         * A Map of booleans
         */
        private Map map = new HashMap();
        private List abilities;

        public String toString()
        {
            String result = "ControllerState is:" + Utils.NEWLINE;
            for(int i = 0; i <= CapabilityEnum.ALL_KNOWN_CAPABILITIES.length - 1; i++)
            {
                result = result + map.get(CapabilityEnum.ALL_KNOWN_CAPABILITIES[i])
                    + Utils.NEWLINE;
            }
            return result;
        }

        private ControllerState
            (
                Map map,
                List abilities
            )
        {
            if(this.map == map)
            {
                Err.error(
                    "Should not be constructed with same map, want to make a copy");
            }
            this.map.putAll(map);
            this.abilities = abilities;
        }

        ControllerState()
        {
            Map map = new HashMap(CapabilityEnum.ALL_KNOWN_CAPABILITIES.length);
            for(int i = 0; i <= CapabilityEnum.ALL_KNOWN_CAPABILITIES.length - 1; i++)
            {
                map.put(CapabilityEnum.ALL_KNOWN_CAPABILITIES[i], Boolean.valueOf( true));
            }
            this.map.putAll(map);
            this.abilities = null;
        }

        /**
         * Not 100% sure of the official way of doing
         * a deep copy. Not doing a deep copy for abilities.
         */
        ControllerState
            (
                ControllerState state
            )
        {
            this.map.putAll(state.getMap());
            this.abilities = state.getAbilities();
        }

        private void set(CapabilityEnum capability, boolean value)
        {
            map.put(capability, Boolean.valueOf( value));
        }

        private boolean get(CapabilityEnum capability)
        {
            Boolean b = (Boolean) map.get(capability);
            return b.booleanValue();
        }

        private List getAbilities()
        {
            return abilities;
        }

        private void setAbilities(List abilities)
        {
            this.abilities = abilities;
        }

        public Map getMap()
        {
            return map;
        }

        public void setMap(Map map)
        {
            this.map = map;
        }
    } // end ControllerState class
}
