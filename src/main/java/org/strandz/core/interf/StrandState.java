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

import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.prod.Block;
import org.strandz.core.prod.Constants;
import org.strandz.core.prod.Session;
import org.strandz.core.prod.move.MoveTracker;
import org.strandz.core.prod.move.MoveManagerI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MsgSubstituteUtils;

import java.util.List;

/**
 * This class represents the root of a state machine that acts as the
 * first gateway for all operations directed at Strandz. The opening
 * state is NodeCollecting. When a Strand is in this state it is happy
 * to accept the addition of nodes. NodesGobbledState means
 * that a strand has been given a set of nodes and has validated them as
 * ok to be used. Thus at this time the operation <code>OperationEnum.EXECUTE_QUERY</code>
 * will get through and data, if there is any, will appear on the screen.
 * <p/>
 * The state will now have changed to LoadDoneState, which is a
 * LoadInsertDoneState. LoadInsertDoneState means RT. Across this transition
 * all the DT settings are used to create a Strandz RT environment. In this
 * state some DT style property settings will still get through, and some will
 * sensibly result in an error. For example setting <code>CapabilityEnum.INSERT</code>
 * from allowed to not allowed will get through. Adding another detail node will
 * not.
 * TODO Unit Tests to show above is true, and to show exactly what happens in
 * TODO terms of stack traces etc.
 *
 * @author Chris Murphy
 */
abstract class StrandState
{
    private MoveManagerI moveManager = Session.getMoveManager();
    private static int times;

    int getMode(StrandI context)
    {
        Session.error("getMode() not been implemented by " + this.getClass());
        // comp
        return -99;
    }

    void goNode(StrandI context, Node newNode, boolean dontReplaceBlocks)
    {
        Session.error(
            "userNodeInterfaceFocus() not been implemented by " + this.getClass());
    }

    void addAfter(StrandI context, Node node)
    {
        Session.error("addAfter() not been implemented by " + this.getClass());
    }

    void addAt(StrandI context, Node node)
    {
        Session.error("addAt() not been implemented by " + this.getClass());
    }

    void enterQuery(StrandI context, Node node)
    {
        Session.error("enterQuery() not been implemented by " + this.getClass());
    }

    void executeQuery(StrandI context, Node node)
    {
        Session.error("load() not been implemented by " + this.getClass());
    }

    void executeSearch(StrandI context, Node node)
    {
        Session.error("executeSearch() not been implemented by " + this.getClass());
    }

    void previous(StrandI context, Node node)
    {
        Session.error("previous() not been implemented by " + this.getClass());
    }

    void next(StrandI context, Node node)
    {
        Session.error("next() not been implemented by " + this.getClass());
    }

    void setRow(StrandI context, Node node, int row)
    {
        Session.error("setRow() not been implemented by " + this.getClass());
    }

    void post(StrandI context, Node node, boolean userInitiated)
    {
        Session.error("post() not been implemented by " + this.getClass());
    }

    void visualCursorChange(StrandI context, Node node)
    {
        Session.error(
            "visualCursorChange() not been implemented by " + this.getClass());
    }

    /*
    void commit( Strand context)
    {
    Session.error("commit() not been implemented by " + this.getClass());
    }
    */

    void remove(StrandI context, Node node)
    {
        Session.error("remove() not been implemented by " + this.getClass());
    }

    void changeState(StrandI context, StrandState newState)
    {
        // Err.pr( "Changing state from " + context.currentState + " to " + newState);
        context.setCurrentState( newState);
    }

    void commonLoad(StrandI context, Node node, OperationEnum op)
    {
        int row = 0;
        Block block = node.getBlock();
        if(!context.isTopLevel(node))
        {
            /*
            * Doing a query s/be an atomic operation that does not fail, thus
            * this is an Err.error().
            */
            String msg = MsgSubstituteUtils.formMsg(pMsg.NOT_TOP_NODE, node.toString());
            Err.error(msg);
        }
        block.setFreshDataRecords();
        block.zeroIndex();
        /*
        times++;
        if(times == 3)
        {
        Err.debug();
        }
        */
        node.fireDataFlowEvent(DataFlowEvent.PRE_QUERY, op);
        // times++;
        if(times == 3)
        {// Err.stack();
        }
        // changed here
        if(!block.dataRecordsEmpty())
        {
            // Err.pr( "dataRecords NOT Empty() in " + node.getBlock() +
            // ", got " + node.getRowCount() + " rows");
            block.incCursor(false);
            // Err.pr( "Have just inced cursor (data recs not empty) for " + node);
        }
        else
        {// Err.pr( "dataRecords ARE Empty() in " + node.getBlock());
        }
        if(op == OperationEnum.EXECUTE_QUERY)
        {
            context.getOPor().executeQuery(block);
        }
        else if(op == OperationEnum.EXECUTE_SEARCH)
        {
            CopyPasteBuffer copyPasteBuffer = new CopyPasteBuffer();
            List attribs = 
                    Attribute.getNonBlankAttributes( node.getAttributes());
                    //Utils.asArrayList( node.getAttributes());
            copyPasteBuffer.copyItemValues(attribs, node);
            row = context.getOPor().executeSearch(block, copyPasteBuffer,
                Attribute.getAdapters(attribs));
        }
        else
        {
            Err.error("commonLoad not for " + op);
        }
        /*
        * Now that the query has occured:
        */
        // pUtils.prCollection( block.getDataRecords());
        if(block.dataRecordsEmpty())
        {
            moveManager.setSomeRowsQueried(false);
            block.blankoutDisplay(
                    op, 
                    "commonLoad() produced no results, and we need to get rid of the 'by query' values",
                    Constants.REMOVE_COMBO_ITEMS);
            context.getOPor().getCurrentState(block).freeze(context.getOPor());
            row = -1;
        }
        else
        {
            moveManager.setSomeRowsQueried(true);
            if(block.isAllowed(CapabilityEnum.UPDATE))
            {
                context.getOPor().getCurrentState(block).edit(context.getOPor());
            }
            else
            {
                //Err.pr("Update not allowed for block " + block);
                context.getOPor().getCurrentState(block).browse(context.getOPor());
            }
        }
        node.fireDataFlowEvent(DataFlowEvent.POST_QUERY, op);
        if(op == OperationEnum.EXECUTE_SEARCH && row != -1)
        {
            // Err.pr( "row to set to is " + row);
            context.SET_ROW(row);
        }
    }
} // end class
