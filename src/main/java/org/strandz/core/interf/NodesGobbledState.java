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
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.core.prod.Session;
import org.strandz.lgpl.util.MsgSubstituteUtils;

/**
 * This state is entered whenever a user focuses on a Node (ie. one of the
 * UI fields of the node).
 *
 * @author Chris Murphy
 */
class NodesGobbledState extends StrandState
{
    static StrandState getNewInstance(StrandI context)
    {
        return new NodesGobbledState(context);
    }

    NodesGobbledState(StrandI context)
    {// Err.pr( "Now in NodesGobbled Sector constructor");
    }

    int getMode(StrandI context)
    {
        return Strand.NODESGOBBLED;
    }

    void goNode(StrandI context, Node newNode, boolean dontReplaceBlocks)
    {
        // new MessageDlg("userNodeFocus in NodesGobbledState");
        if(context.getCurrentNode() == newNode)
        {
            /*
            * Done so user can send focus event, and the controller will end up
            * looking ok. NO NEED to do here (only LoadInsertDoneState). Reason
            * for this is that present policy is to always have all buttons
            * showing when no data is attached to the controller.
            *
            * context.setControllerMemento( context.lastControllerMemento);
            * Now doing where set a transaction visible
            */
            return;
        }
        context.setNewCapabilities(newNode, false);
        context.generateNodeChangeEvent(newNode);
    }

    /*
     * Not yet implemented
    void addIgnore( Strand context, Node node)
    {
      add( context, node);
    }
    */

    void addAt( StrandI context, Node node)
    {
        add( context, node, OperationEnum.INSERT_AT_PLACE);
    }

    void addAfter( StrandI context, Node node)
    {
        add( context, node, OperationEnum.INSERT_AFTER_PLACE);
    }

    private void add(StrandI context, Node node, OperationEnum operationEnum)
    {
        // context.blocks.getCurrentBlock().usersBlock has given node
        if(!context.isTopLevel(node))
        {
            String txt = "Cannot ADD until focus has been set to a Top Level Node";
            Session.getErrorThrower().throwApplicationError(txt,
                ApplicationErrorEnum.INTERNAL);
        }
        node.getBlock().zeroIndex();
        // weird node.fireDataFlowEvent( DataFlowEvent.PRE_QUERY);
        /*
        * There is nothing wrong with adding when you don't know what
        * the other rows are, however if you do the status bar will
        * show "INSERT 1 of 3" if there were previously 2 records. What
        * programmer s/really be doing to avoid this message is to
        * either have an add-only screen, or automatically do a load
        * or (TODO) switch off this test and dont have a statusbar.
        *
        * TODO - make it an option on Node as to whether to show this
        * or not. Following Forms, way to do is to have auto query of
        * node when go it it.
        */
        /*
        if((! node.getBlock().dataRecordsEmpty()) && (node.isexecute( OperationEnum.EXECUTE_QUERY) == true))
        {
        new MessageDlg(
        "If first operation is Add, makes no sense " +
        "to have initialData - first operation s/be executeQuery");
        throw new ApplicationError();
        }
        */
        context.getOPor().add(node.getBlock(), operationEnum);
        // Err.pr( "-- INSERT from NodesGobbledState");
        changeState(context, InsertDoneState.getNewInstance(context));
    }

    /*
    void addPrior(Strand context, Node node)
    {
        if(!context.mdTiesManager.isTopLevel(node.getBlock()))
        {
            String txt = "Cannot ADD until focus has been set to a Top Level Node";
            Session.getErrorThrower().throwApplicationError(txt,
                ApplicationErrorEnum.INTERNAL);
        }
        node.getBlock().zeroIndex();
        context.getOPor().addPrior(node.getBlock());
        changeState(context, InsertDoneState.getNewInstance(context));
    }
    */

    void executeQuery(StrandI context, Node node)
    {
        if(!context.isTopLevel(node))
        {
            String msg = MsgSubstituteUtils.formMsg(pMsg.NOT_TOP_NODE, node.toString());
            Session.getErrorThrower().throwApplicationError(msg,
                ApplicationErrorEnum.INTERNAL);
        }
        commonLoad(context, node, OperationEnum.EXECUTE_QUERY);
        changeState(context, LoadDoneState.getNewInstance(context));
    }

    void previous(StrandI context, Node node, boolean alreadyDone)
    {
        Session.getErrorThrower().throwApplicationError(
            "Must either LOAD or INSERT before can PREVIOUS",
            ApplicationErrorEnum.INTERNAL);
    }

    void next(StrandI context, Node node, boolean alreadyDone)
    {
        Session.getErrorThrower().throwApplicationError(
            "Must either LOAD or INSERT before can NEXT",
            ApplicationErrorEnum.INTERNAL);
    }

    void setRow(StrandI context, Node node, int row, boolean alreadyDone)
    {
        // Err.stack();
        Session.getErrorThrower().throwApplicationError(
            "Must either LOAD or INSERT before can SETROW",
            ApplicationErrorEnum.INTERNAL);
    }

    void post(StrandI context, Node node, boolean userInitiated)
    {
        // Err.stack();
        Session.getErrorThrower().throwApplicationError(
            "Must either LOAD or INSERT before can POST",
            ApplicationErrorEnum.INTERNAL);
    }

    void clear(StrandI context, Node node)
    {
        Session.getErrorThrower().throwApplicationError(
            "Must either LOAD or INSERT before can CLEAR",
            ApplicationErrorEnum.INTERNAL);
    }

    void commit(StrandI context)
    {
        Session.getErrorThrower().throwApplicationError(
            "Must either LOAD or INSERT before can COMMIT",
            ApplicationErrorEnum.INTERNAL);
    }

    void remove(StrandI context, Node node)
    {
        Session.getErrorThrower().throwApplicationError(
            "Must either LOAD or INSERT before can REMOVE",
            ApplicationErrorEnum.INTERNAL);
    }
}
