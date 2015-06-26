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
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

public class BlockStateCreator
{
    private static int times;

    public static BlockState newInstance(
        OperationsProcessor context, ProdNodeI node)
    {
        BlockState result;
        StateEnum stateEnum = node.getState();
        result = newInstance( context, stateEnum, node);
        return result;
    }

    public static BlockState newInstance(
        OperationsProcessor context, StateEnum stateEnum)
    {
        BlockState result;
        result = newInstance( context, stateEnum, null);
        return result;
    }

    public static BlockState newInstance(
        OperationsProcessor context, StateEnum stateEnum, ProdNodeI node)
    {
        BlockState result = null;
        if(stateEnum == StateEnum.ENTER_QUERY)
        {
            result = EnterQueryState.newInstance(context);
        }
        else if(stateEnum == StateEnum.FROZEN)
        {
            if(SdzNote.TIGHTEN_RECORD_VALIDATION.isVisible())
            {
                times++;
                Err.pr(SdzNote.CREATE_FROZEN, "creating FROZEN times " + times);
                if(times == 0)
                {
                    Err.stack();
                }
            }
            result = FreezingState.newInstance(context);
        }
        else if(stateEnum == StateEnum.EDIT)
        {
            result = EditingState.newInstance(context);
        }
        else if(stateEnum == StateEnum.VIEW)
        {
            result = BrowsingState.newInstance(context);
        }
        else if(stateEnum == StateEnum.NEW)
        {
            result = AddingState.newInstance(context);
        }
        else if(stateEnum == StateEnum.NEW_PRIOR)
        {
            result = PriorAddingState.newInstance(context);
        }
        else if(stateEnum == StateEnum.NOMOVE_NEW)
        {
            result = NoMoveAddState.newInstance(context);
        }
        else if(stateEnum == StateEnum.NOMOVE_NEW_PRIOR)
        {
            result = NoMoveAddPriorState.newInstance(context);
        }
        else if(stateEnum == StateEnum.NOMOVE_EDIT)
        {
            result = NoMoveEditState.newInstance(context);
        }
        else
        {
            Err.error("Haven't done BlockStateCreator.newInstance() for " + stateEnum);
        }
        Err.pr(SdzNote.CHANGING_NODES_STATES_I_NOT_RECOGNISED,
            "=== new state: " + stateEnum + " for " + node);
        return result;
    }
}
