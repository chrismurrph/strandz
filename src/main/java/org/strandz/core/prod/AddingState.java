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

import org.strandz.core.domain.constants.StateEnum;

class AddingState extends AnyAddingState
{
    static public BlockState newInstance(OperationsProcessor context)
    {
        return new AddingState(context);
    }

    private AddingState(OperationsProcessor context)
    {
        super(context);
        context.getCurrentBlock().setDisplayEditable(true);
        /*
        WRONG - eleIndex is same as current row and is used to index into
        ACTUAL dataRecords. Thus it should be incremented after the add
        operation has been completed.
        */
        // context.incEleIndex( false); //tmp

        // 09/06/04 - moved to happen earlier:
        // context.getCurrentBlock().fireNodeDefaultEvent( DataFlowEvent.PRE_INSERT);
        // following will pick up on anything that setDefaultData() may have
        // set. (setDefaultData may have been called in pre-insert trigger).
        // 03/08/03 -- took out as doing in add()
        // context.getCurrentBlock().blankoutDisplay();
        // Err.pr( "Now in Adding StateEnum constructor");
    }

    public void add(OperationsProcessor context)
    {
        /*
        * this may need to happen, but initial problem is when
        * add from Freezing
        if(! context.getCurrentBlock().incCursor( true))
        {
        throw new ApplicationError();
        }
        if(! context.getCurrentBlock().incCursor( true))
        {
        throw new ApplicationError();
        }
        */
        super.add(context);
        context.changeState( // AddingState.getNewInstance( context),
            BlockStateCreator.newInstance(context, StateEnum.NEW, context.getCurrentBlock().getNode()),
            context.getCurrentBlock());
    }

    public StateEnum getState()
    {
        return StateEnum.NEW;
    }

    StateEnum getNoMoveState()
    {
        return StateEnum.NOMOVE_NEW;
    }

    boolean isPrior()
    {
        return false;
    }
}
