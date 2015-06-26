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

import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.DynamicAllowedEvent;
import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.core.prod.OperationsProcessor;
import org.strandz.core.prod.Session;
import org.strandz.core.prod.move.MoveTracker;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Publisher;

import javax.swing.JComponent;
import java.util.List;

/**
 * This class is part of a decorator pattern used to conveniently stop NPEs.
 *
 * @author Chris Murphy
 */
public class NullStrand extends AbstStrand implements StrandI
{
    public void setNodeController(NodeController c)
    {
        Session.error("NullStrand: setNodeController(): " + c);
    }

    public Node getCurrentNode()
    {
        return null;
    }

    public OperationsProcessor getOPor()
    {
        return null;
    }

    public int getMode()
    {
        return -99;
    }

    public void addButton(JComponent newButton)
    {
        Session.error("NullStrand: addButton(): " + newButton);
    }

    public void removeButton(JComponent oldButton)
    {
        Session.error("NullStrand: addButton(): " + oldButton);
    }

    public void setDynamicAllowed(boolean b, int capability)
    {
        Session.error("NullStrand: setAllowed(): " + capability + " to " + b);
    }

    public boolean getDynamicAllowed(int capability)
    {
        return true;
    }

    public void addStateChangeTrigger(StateChangeTrigger listener)
    {
        Session.error("NullStrand: addStateChangeTrigger()");
    }

    public void removeStateChangeTrigger(StateChangeTrigger listener)
    {
        Session.error("NullStrand: removeStateChangeTrigger()");
    }

    public void addNodeChangeListener(NodeChangeListener listener)
    {
        Session.error("NullStrand: addNodeChangeListener()");
    }

    public void removeNodeChangeListener(NodeChangeListener listener)
    {
        Session.error("NullStrand: removeNodeChangeListener()");
    }

    public void removeAllNodeChangeListeners()
    {
        Session.error("NullStrand: removeAllNodeChangeListeners()");
    }

    public void windowsWorkaround(Node ignoreNode)
    {
        Session.error("NullStrand: windowsWorkaround()");
    }

    public void newNode(Node node)
    {
        Session.error("NullStrand: newNode()");
    }

    public void goNode(Node newNode)
    {
        Session.error("NullStrand: goNode()");
    }

    public void goNode(Node newNode, int row)
    {
        Session.error("NullStrand: goNode()");
    }
    
    public void setAlreadyPerformingGoNode(boolean b)
    {
        Session.error("NullStrand: setAlreadyPerformingGoNode()");
    }

    public void clear()
    {
        Session.error("NullStrand: clear()");
    }

    // public void controlActionPerformed( InputControllerEvent event) throws ValidationException
    // {
    // Session.error("NullStrand: controlActionPerformed");
    // }
    public void dynamicAllowed(Node nodeChanged, DynamicAllowedEvent evt)
    {
        Session.error("NullStrand: dynamicAllowed()");
    }

    public void dynamicAbilities(Node nodeChanged, List abilities)
    {
        Session.error("NullStrand: dynamicAbilities()");
    }

    public boolean execute(OperationEnum op)
    {
        Session.error("NullStrand: execute()");
        return false;
    }

    public boolean SET_ROW(int row)
    {
        Session.error("NullStrand: SET_ROW()");
        return false;
    }
    
    public boolean POST()
    {
        Session.error("NullStrand: POST()");
        return false;
    }

    void fireErrorHandler(ApplicationError e)
    {
        Session.error("NullStrand: fireErrorHandler( ApplicationError e)");
    }

    public void throwApplicationError(
        ValidationException ex, List error, ApplicationErrorEnum type)
    {
        Session.error("NullStrand: throwApplicationError()");
    }

    public void throwApplicationError(String error, ApplicationErrorEnum type)
    {
        Session.error("NullStrand: throwApplicationError()");
    }

    void setIgnoreValidation(boolean ignore)
    {
        Session.error("NullStrand: setIgnoreValidation()");
    }

    public boolean isIgnoreValidation()
    {
        Session.error("NullStrand: isIgnoreValidation()");
        return false;
    }

    public void makeUntouchable(boolean b)
    {
        Session.error("NullStrand: makeUntouchable()");
    }

    public boolean getIgnoreFirstTouch()
    {
        Session.error("NullStrand: getIgnoreFirstTouch()");
        return false;
    }

    public void setIgnoreFirstTouch(boolean b)
    {
        Session.error("NullStrand: setIgnoreFirstTouch()");
    }

    public boolean isValidationRequired()
    {
        Session.error("NullStrand: isValidationRequired()");
        return false;
    }

    public void b4HandleErrorProcessing(boolean b)
    {
        Session.error("NullStrand: b4HandleErrorProcessing()");
    }

    void postValidationProcessing(
        boolean success,
        ApplicationError err)
    {
        Session.error("NullStrand: postValidationProcessing()");
    }

    public MoveTracker getMoveManager()
    {
        Session.error("NullStrand: getMoveManager()");
        return null;
    }

    SdzEntityManagerI getEntityManager()
    {
        Session.error("NullStrand: getEntityManager()");
        return null;
    }

    public List<Node> getNodes()
    {
        Session.error("Not implemented");
        return null;
    }

    public Node getNodeByName(String name)
    {
        Session.error("Not implemented");
        return null;
    }

    public void setRequiresRefresh()
    {
        Err.error("Not implemented");
    }

    public void setWhenLastVisibleFalseControllerMemento(NodeController.ControllerMemento lastControllerMemento)
    {
        Err.error("Not implemented");
    }

    public Publisher removeAllPreControlActionPerformedTriggers()
    {
        Err.error("Not implemented");
        return null;
    }

    public Publisher removeAllPostControlActionPerformedTriggers()
    {
        Err.error("Not implemented");
        return null;
    }

    public void addPreControlActionPerformedTrigger(PreOperationPerformedTrigger cal)
    {
        Err.error("Not implemented");
    }

    public void addPostControlActionPerformedTrigger(PostOperationPerformedTrigger cal)
    {
        Err.error("Not implemented");
    }

    public void setNewCapabilities(Node localNode, boolean firstTime)
    {
        Err.error("Not implemented");
    }

    public void generateNodeChangeEvent(Node newNode)
    {
        Err.error("Not implemented");
    }

    public NodeController getNodeController()
    {
        Err.error("Not implemented");
        return null;
    }

    public boolean isTopLevel(Node node)
    {
        Err.error("Not implemented");
        return false;
    }

    public void consumeNodesIntoRT(Node newNode, String reason, Node nodeToSetAsCurrent, boolean dontReplaceNewNodeBlock)
    {
        Err.error("Not implemented");

    }

    public void setCurrentState(StrandState currentState)
    {
        Err.error("Not implemented");

    }

    public boolean isAlreadyBeenCustomized()
    {
        Err.error("Not implemented");
        return false;
    }

    public void setAlreadyBeenCustomized(boolean alreadyBeenCustomized)
    {
        Err.error("Not implemented");

    }

    public List retrieveValidateBeanMsg()
    {
        Err.error("Not implemented");
        return null;
    }

    public String getName()
    {
        return null;
    }
    
    public String toShow()
    {
        return null;
    }

    boolean isManualB4ImageValue()
    {
        return false;
    }
}
