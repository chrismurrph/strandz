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

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.AllowedEvent;
import org.strandz.core.prod.Session;

// import strandz.domain.event.InputControllerEvent;

/**
 * This class represents an event that comes out of a NodeController. The flow
 * of these events can be accessed in the triggers
 * <code>PostOperationPerformedEvent</code> and <code>PreOperationPerformedEvent</code>
 *
 * @author Chris Murphy
 */
public class OutNodeControllerEvent extends AllowedEvent
{
    private Node node;
    private StateEnum previousState;
    private int row;

    /*
    public OutNodeControllerEvent( InputControllerEvent evt,
    Node node,
    StateEnum previousState)
    {
    super( evt.getReason());
    init( node, previousState);
    }
    */
    public OutNodeControllerEvent(OperationEnum id,
                                  Node node,
                                  StateEnum previousState,
                                  int row)
    {
        super(id);
        init(node, previousState, row);
    }

    private void init(Node node,
                      StateEnum previousState,
                      int row)
    {
        if(previousState == null)
        {
            Session.error(
                "Cannot create a OutNodeControllerEvent with a null previousState");
        }
        this.node = node;
        this.previousState = previousState;
        this.row = row;
    }

    public Node getNode()
    {
        return node;
    }

    public StateEnum getPreviousState()
    {
        return previousState;
    }

    public int getRow()
    {
        return row;
    }

    public String toString()
    {
        return super.toString() + " " + node;
    }
}
