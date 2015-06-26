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

import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.core.prod.Session;
import org.strandz.lgpl.util.Err;

/**
 * This is the state before all the nodes have been configured. Thus most
 * RT operations are not allowed.
 *
 * @author Chris Murphy
 */
class NodeCollectingState extends StrandState
{
    static StrandState getNewInstance(StrandI context)
    {
        return new NodeCollectingState(context);
    }

    NodeCollectingState(StrandI context)
    {// Err.pr( "Now in NodeCollecting Sector constructor");
    }

    int getMode(StrandI context)
    {
        return Strand.NODECOLLECTING;
    }

    void goNode(StrandI context, Node newNode, boolean dontReplaceBlocks)
    {
        // Err.pr("userNodeFocus in NodeCollectingState");
        // Err.pr( "o-o userNodeFocus called: " + context + "," + newNode);
        if(newNode.getBlock() == null)
        {
            context.consumeNodesIntoRT( newNode, "goNode", null, dontReplaceBlocks);
        }
        /*
         * Need to properly set another block so that for instance the visual controller
         * will show the right buttons enabled.
         */
        if(context.getOPor().getCurrentBlock() != newNode.getBlock())
        {
            context.getOPor().setCurrentBlock( newNode.getBlock());
        }
        context.setNewCapabilities( newNode, true);
        /*
        * TODO - when have an implementation of NodeBar can remove
        * these two seperate calls. They both end up at NodeBar and
        * a CLOSE is never performed (?). OPEN doesn't even have a
        * Node as part of the event. S/be able to get rid of call to
        * context.generateNodeAccessible( AccessEvent.OPEN).
        * generateNodeAccessible is only called here, whilst
        * generateNodeChangeEvent is called for NodesGobbledState
        * and LoadInsertDoneState as well. Original reason for having
        * two events is baffling. Prolly already done but check.
        */
        // context.generateNodeAccessible( AccessEvent.OPEN);
        context.generateNodeChangeEvent(newNode);
        changeState(context, NodesGobbledState.getNewInstance(context));
    }

    void add(StrandI context)
    {
        String msg = "Cannot ADD until focus has been set to a Top Level Node";
        Session.getErrorThrower().throwApplicationError(msg,
            ApplicationErrorEnum.INTERNAL);
    }

    void executeQuery(StrandI context, Node node)
    {
        String msg = "Cannot LOAD until focus has been set to a Top Level Node";
        Session.getErrorThrower().throwApplicationError(msg,
            ApplicationErrorEnum.INTERNAL);
    }

    void previous(StrandI context, boolean alreadyDone)
    {
        String msg = "Must start with Add or Query";
        Session.getErrorThrower().throwApplicationError(msg,
            ApplicationErrorEnum.INTERNAL);
    }

    void next(StrandI context, boolean alreadyDone)
    {
        String msg = "Must start with Add or Query";
        Session.getErrorThrower().throwApplicationError(msg,
            ApplicationErrorEnum.INTERNAL);
    }

    void post(StrandI context)
    {
        String msg = "Must start with Add or Query";
        Session.getErrorThrower().throwApplicationError(msg,
            ApplicationErrorEnum.INTERNAL);
    }

    void clear(StrandI context, Node node)
    {
        String txt = "Must start with Add or Query";
        Session.getErrorThrower().throwApplicationError(txt,
            ApplicationErrorEnum.INTERNAL);
    }

    void commit(StrandI context)
    {
        String msg = "Must start with Add or Query";
        Session.getErrorThrower().throwApplicationError(msg,
            ApplicationErrorEnum.INTERNAL);
    }

    void remove(StrandI context)
    {
        String msg = "Must start with Add or Query";
        Session.getErrorThrower().throwApplicationError(msg,
            ApplicationErrorEnum.INTERNAL);
    }
}
