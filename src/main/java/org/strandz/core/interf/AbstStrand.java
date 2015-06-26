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
import org.strandz.core.prod.OperationsProcessor;
import org.strandz.lgpl.util.NameableI;

import java.util.List;

/**
 * The reason for this class is the decorator pattern. A NodeController starts
 * off with a NullStrand and should appear to process commands even before it
 * is attached to a NodeController. At DT for instance, a Strand has no conception
 * of a NodeController. Easier to do this than to be coding around nulls.
 *
 * @author Chris Murphy
 */
abstract public class AbstStrand implements NameableI
{
    abstract public void setNodeController(NodeController c);

    // made NodeInterface 'cause ControlActionListener
    // also returns NodeInterface
    abstract public Node getCurrentNode();

    abstract public OperationsProcessor getOPor();

    abstract public int getMode();

    abstract public void addStateChangeTrigger(StateChangeTrigger listener);

    abstract public void removeStateChangeTrigger(StateChangeTrigger listener);

    abstract public void addNodeChangeListener(NodeChangeListener listener);

    abstract public void removeNodeChangeListener(NodeChangeListener listener);

    abstract public void removeAllNodeChangeListeners();

    abstract public void windowsWorkaround(Node ignoreNode);

    abstract void newNode(Node node);
    
    abstract public void goNode( Node target);

    abstract public void goNode(Node newNode, int row);

    abstract public void setAlreadyPerformingGoNode(boolean b);

    abstract public void dynamicAllowed(Node nodeChanged, DynamicAllowedEvent evt);

    abstract public void dynamicAbilities(Node nodeChanged, List abilities);

    abstract public boolean execute(OperationEnum op);

    abstract public boolean SET_ROW(int row);

    abstract public boolean POST();
    
    abstract void fireErrorHandler(ApplicationError e);

    /*
    abstract void throwApplicationError(
    ValidationException ex, List error, ApplicationErrorEnum type);
    abstract void throwApplicationError( String error, ApplicationErrorEnum type);
    */
    abstract void setIgnoreValidation(boolean ignore);

    abstract public boolean isIgnoreValidation();

    abstract boolean isValidationRequired();

    abstract boolean isManualB4ImageValue();
    
    //Strand is one of these - suppose could make it return itself
    //abstract public EntityManagerProviderI getEntityManagerProvider();
}
