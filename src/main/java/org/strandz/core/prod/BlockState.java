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
package org.strandz.core.prod;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;

abstract public class BlockState
{
    private static int times = 0;

    public StateEnum getState()
    {
        Err.error("getState() not been implemented by " + this.getClass());
        return StateEnum.UNKNOWN;
    }

    public void anyKeyPressed(OperationsProcessor context,
                              OperationEnum key,
                              Block block,
                              boolean fieldsChanged)
    {
        /*
        times++;
        Err.pr("DOING anyKeyPressed() KEY: " + key +
            ", BLOCK: " + block.getName() +
            ", STATE: " + this +
            ", times: " + times);
        if (key == OperationEnum.INSERT)
        {
            Err.debug();
        }
        */
        if(!key.isOperation())
        {
            Err.error("Key pressed must be an operation");
        }
        block.setAnyKeyPressed(key);
        if(key != OperationEnum.REMOVE)
        {
            if(block.getMoveBlock() != null)
            {
                if(key != OperationEnum.EXECUTE_QUERY)
                {
                    //Err.pr( "Why definitely validating when we don't know if fields have changed?");
                    if(fieldsChanged)
                    {
                        block.getMoveBlock().getMoveTracker().enter(null, EntrySiteEnum.ANY_KEY,
                            key);
                    }
                    else
                    {
                        Assert.notNull( block.getMoveBlock(), "No MoveBlock");
                        Assert.notNull( block.getMoveBlock().getMoveTracker(), "No MoveManager");
                        block.getMoveBlock().getMoveTracker().enterWithoutValidation(null,
                            EntrySiteEnum.ANY_KEY, key);
                    }
                }
                else
                {
                    block.getMoveBlock().getMoveTracker().enterWithoutValidation(null,
                        EntrySiteEnum.ANY_KEY, key);
                }
                block.getMoveBlock().getMoveTracker().exitEnter();
            }
            else
            {
                Err.pr( "No MoveBlock for debugging...");
            }
        }
    }

    /*
    protected void post( OperationsProcessornterface context)
    {
    Err.error("post() not been implemented by " + this.getClass());
    }
    */

    public void previous(OperationsProcessor context)
    {
        Err.error("previous() not been implemented by " + this.getClass());
    }

    public void next(OperationsProcessor context)
    {
        Err.error("next() not been implemented by " + this.getClass());
    }

    public void setRow(OperationsProcessor context, int row)
    {
        Err.error("setRow() not been implemented by " + this.getClass());
    }

    public void add(OperationsProcessor context)
    {
        Err.error("add() not been implemented by " + this.getClass());
    }

    /*
     * Try to achieve this by just calling add
    public void addIgnore( OperationsProcessor operationsProcessor )
    {
      Err.error( "addIgnore() not been implemented by " + this.getClass());
    }
    */

    /*
    public void addPrior(OperationsProcessor context)
    {
        Err.error("addPrior() not been implemented by " + this.getClass());
    }
    */

    public void remove(OperationsProcessor context)
    {
        Err.error("remove() not been implemented by " + this.getClass());
    }

    protected void addFromCurrent(OperationsProcessor context)
    {
        Err.error("addFromCurrent() not been implemented by " + this.getClass());
    }

    public void edit(OperationsProcessor context)
    {
        Err.error("edit() not been implemented by " + this.getClass());
    }

    public void browse(OperationsProcessor context)
    {
        Err.error("browse() not been implemented by " + this.getClass());
    }

    public void freeze(OperationsProcessor context)
    {
        Err.error("freeze() not been implemented by " + this.getClass());
    }

    public void enterQuery(OperationsProcessor context)
    {
        Err.error("enterQuery() not been implemented by " + this.getClass());
    }

    /*
    public void executeQuery( OperationsProcessor context)
    {
    Err.error("executeQuery() not been implemented by " + this.getClass());
    }
    */

    public void leaveBlock(OperationsProcessor context, Block toBlock)
    {
        Err.error("leaveBlock() not been implemented by " + this.getClass());
    }

    public void enterBlock(OperationsProcessor context, Block fromBlock)
    {
        // Err.pr( "### Entering into " + context.getCurrentBlock() + " from " + fromBlock +
        // " in state " + Constants.stringValue( getState( context)));
        Err.error("enterBlock() not been implemented by " + this.getClass());
    }

    public void capabilitiesSet(OperationsProcessor context)
    {
        // Err.pr( "### capabilitiesSet for " + context.getCurrentBlock() +
        // " in state " + Constants.stringValue( getState( context)));
        Err.error("capabilitiesSet() not been implemented by " + this.getClass());
    }

    public void ifInsertedDeleteActions(OperationsProcessor context)
    {
        if(context.getPreviousState().isNew())
        {
            // Err.pr( "coming out of insert");
            if(context.getCurrentBlock().isAllowed(OperationEnum.REMOVE))
            {// Err.pr( "allowing delete back, s/not be required");
                // ((Enabler)context.getEnabler()).enable( OperationEnum.REMOVE);
            }
            else
            {
                // Err.pr( "no more delete");
                ((Enabler) context.getEnabler()).disable(OperationEnum.REMOVE);
            }
        }
        /*
        //To get this working take from below.
        //inSomeNewState( block) && duringCommit will need to be ported as well
        times++;
        Err.pr( "Not yet written ifInsertedDeleteActions in " +
        context.getState( context.getCurrentBlock()) + " " +
        getState() + ", for " + context + ", opn: " +
        context.getCurrentOperation() + ", times: " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
    }

    public String toString()
    {
        return getState().toString();
    }
        
    /**
     *
     *
     *  DO NOT REMOVE BELOW
     *
     *
     */

    /*
    * FROM NodeCollectingState
    void ifInsertedDeleteActions( Strand context, Block block, boolean duringCommit)
    {
    //new MessageDlg("Now doing REMOVE, which s/set controller");
    InputControllerEvent ce = null;
    ce = new InputControllerEvent( OperationEnum.REMOVE);
    ce.setDynamicAllowed( block.isAllowed( OperationEnum.REMOVE));
    context.controller.setDynamicAllowed( ce);
    }
    */
    /*
    * FROM NodesGobbledState
    */
    /**
     * Called when switching b/ween blocks, when haven't yet pressed load
     * or insert.
     */
    /*
    void ifInsertedDeleteActions( Strand context, Block block, boolean duringCommit)
    {
    //new MessageDlg("NodesGobbled ifInsertedDeleteActions to allow REMOVE");
    InputControllerEvent ce = null;
    ce = new InputControllerEvent( OperationEnum.REMOVE);
    ce.setDynamicAllowed( block.isAllowed( OperationEnum.REMOVE));
    context.controller.setDynamicAllowed( ce);
    }
    */
    /*
    * FROM LoadInsertDoneState
    */
    /**
     * ifInsertedDeleteActions s/be done on any button may want to alter keys displayed.
     * For example when post on a record that have just inserted, will want the
     * delete key to display. In detail these times are:
     * <p/>
     * setNewCapabilities()
     * dynamicAllowed()
     * controlActionPerformed()
     * commit()
     */
    /*
    void ifInsertedDeleteActions( Strand context, Block block, boolean duringCommit)
    {
    boolean showDelete = false;

    if(block.isAllowed( OperationEnum.REMOVE))
    {
    //Err.pr( "Initially showDelete = true b/c block allows");
    showDelete = true;
    }

    if  //block doesn't allow but we may override and allow
    (! block.isAllowed( OperationEnum.REMOVE))
    {
    if(block.isInserted() || (inSomeNewState( block) && !duringCommit))
    // is an insert or will stay as some new
    {
    //Err.pr( "Block not allow but set to true anyway as either was inserted or in inserting state and user hasn't yet asked to change");
    showDelete = true;
    }
    }
    *
    else  //block allows but we may override and not allow
    {
    if((inSomeNewState( block) && duringCommit) || block.isInserted())
    // is an insert or will become one
    {
    //nuffin - will keep allowed if already allowed
    }
    else
    {
    Err.pr( "");
    showDelete = false;
    }
    }
    *
    InputControllerEvent ce = null;
    if(context.controller.getDynamicAllowed( OperationEnum.REMOVE))
    {
    if(! showDelete)
    {
    ce = new InputControllerEvent( OperationEnum.REMOVE);
    ce.setDynamicAllowed( false);
    context.controller.setDynamicAllowed( ce);
    }
    }
    else //controller not displaying delete
    {
    if(showDelete)
    {
    ce = new InputControllerEvent( OperationEnum.REMOVE);
    ce.setDynamicAllowed( true);
    context.controller.setDynamicAllowed( ce);
    }
    }
    }
    */
    /*
    private boolean inSomeNewState( Block block)
    {
    boolean result = false;
    int state = block.getState();
    if(state == StateEnum.NEW ||
    state == StateEnum.NOMOVE_NEW)
    {
    result = true;
    }
    return result;
    }
    */
} // end class
