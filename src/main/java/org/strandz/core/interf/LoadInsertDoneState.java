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
 * This state is entered into when the first action a user does is to
 * perform a query OR insert.
 *
 * @author Chris Murphy
 */
class LoadInsertDoneState extends StrandState
{
    void goNode(StrandI context, Node newNode, boolean dontReplaceBlocks)
    {
        /*
        if(context.getCurrentNode() == newNode)
        {
            *
            * Done so user can send focus event, and the controller will end up
            * looking ok.  - event that user creates s/indicate this use, as
            * don't want to do this every time move to a different field, if using
            * field rather than tab or internal frame trigger events.
            *
            * Now doing where set a transaction visible
            * TODO - I still would
            * have thought this is happening all the time, but what is the problem
            * with that?
            *
            return;
        }
        */
        context.getOPor().setCurrentBlock(newNode.getBlock());
        /*
        In case of Error in enterBlock(), the actual and applichousing changes come last:
        */
        context.setNewCapabilities(newNode, false);
        // Err.pr( "In userNodeFocus and new node is " + newNode);
        /*
        if(newNode.toString().equals( "applic.fishbowl.data.Client"))
        {
        Err.pr( "Debug");
        }
        */
        context.generateNodeChangeEvent(newNode);
    }

    void addAfter(StrandI context, Node node)
    {
        context.getOPor().add( node.getBlock(), OperationEnum.INSERT_AFTER_PLACE);
    }

    void addAt(StrandI context, Node node)
    {
        context.getOPor().add( node.getBlock(), OperationEnum.INSERT_AT_PLACE);
    }

    /**
     * Reload
     */
    void executeQuery(StrandI context, Node node)
    {
        //Don't expect this call to bring up any validation problems. Validation is skipped
        //for EXECUTE_QUERY, the theory being that the user wants to reload rather than sort
        //the problems out.
        context.getOPor().getCurrentState(node.getBlock()).anyKeyPressed(context.getOPor(), OperationEnum.EXECUTE_QUERY,
            node.getBlock(), /*node.getBlock().fieldsChanged()*/ false);
        if(!context.isTopLevel(node))
        {
            String msg = MsgSubstituteUtils.formMsg(pMsg.NOT_TOP_NODE, node.toString());
            Session.getErrorThrower().throwApplicationError(msg,
                ApplicationErrorEnum.INTERNAL);
        }
        context.consumeNodesIntoRT(node, "reload, EXECUTE_QUERY on " + node.getName() + " with ID " + node.getId(), 
            node, false);
        context.setNewCapabilities(node, false);
        commonLoad(context, node, OperationEnum.EXECUTE_QUERY);
    }

    void executeSearch(StrandI context, Node node)
    {
        context.getOPor().getCurrentState(
                node.getBlock()).anyKeyPressed(context.getOPor(), OperationEnum.EXECUTE_SEARCH,
            node.getBlock(), node.getBlock().fieldsChanged());
        if(!context.isTopLevel(node))
        {
            String msg = MsgSubstituteUtils.formMsg(pMsg.NOT_TOP_NODE, node.toString());
            Session.getErrorThrower().throwApplicationError(msg,
                ApplicationErrorEnum.INTERNAL);
        }
        // Err.pr( "rowCount of " + node + " b4 pass: " + node.getRowCount());
        context.consumeNodesIntoRT(node, "executeSearch", node, false);
        // Err.pr( "rowCount of " + node + " after pass: " + node.getRowCount());
        context.setNewCapabilities(node, false);
        commonLoad(context, node, OperationEnum.EXECUTE_SEARCH);
    }

    void enterQuery(StrandI context, Node node)
    {
        context.getOPor().enterQuery(node.getBlock());
    }

    void previous(StrandI context, Node node)
    {
        context.getOPor().previous(node.getBlock());
    }

    void next(StrandI context, Node node)
    {
        context.getOPor().next(node.getBlock());
    }

    void setRow(StrandI context, Node node, int row)
    {
        context.getOPor().setRow(node.getBlock(), row);
    }

    void post(StrandI context, Node node, boolean userInitiated)
    {
        /*
        Err.pr( "POST node are using " + node + " has id " + node.id);
        Err.pr( "And its black has id " + node.getBlock().id);
        */
        context.getOPor().post(node.getBlock(), userInitiated);
    }

    void visualCursorChange(StrandI context, Node node)
    {
        node.getBlock().visualCursorChange();
    }

    /**
     * Is this being used? It doesn't happen when press commit!!
     */
    /*
    void commit( Strand context)
    {
    context.commit();
    }
    */

    void remove(StrandI context, Node node)
    {
        context.getOPor().remove(node.getBlock());
    }
}
