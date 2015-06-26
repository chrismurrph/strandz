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
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Print;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * class CompositeBlock
 * <p/>
 * Used by any class that refers to a composite - acts as master side of a
 * master-detail
 */

class CompositeBlock extends AbstBlock
{
    ArrayList list = new ArrayList();
    private static int times;

    void add(AbstBlock block)
    {
        if(block.getId() == 6)
        {
            times++;
            Err.pr(SdzNote.ADD_SAME_BLOCK_TWICE, "Adding to CompositeBlock, block of ID " + block.getId() + " times " + times);
            if(times == 0)
            {
                Err.stack();
            }
        }
        chkNotAlready(block);
        list.add(block);
    }

    private void chkNotAlready(AbstBlock block)
    {
        if(list.contains(block))
        {
            Print.prList( list, "Blocks already in this composite");
            for (int i = 0; i < list.size(); i++)
            {
                AbstBlock abstBlock = (AbstBlock) list.get(i);
                Err.pr( "name: " + abstBlock.getName() + ", ID: " + abstBlock.getId());
            }
            Err.error("Cannot add the same block twice to a CompositeBlock - " + "name: " + block.getName() + ", ID: " + block.getId());
        }
    }

    void remove(AbstBlock block)
    {
        Err.error("Composite not intended to have adapterRemove called on it");
        list.remove(block);
    }

    /**
     * Used for debugging, to see if all frozen.
     * If not all the same will return undefined (-1)
     */
    public BlockState getState()
    {
        Err.error("Do not call getMode for composite block - use iterator()");

        /*
        int thisMode = -1;
        for( Iterator e = list.iterator(); e.hasNext();)
        {
        thisMode = ((AbstBlock)e.next()).getState();
        if(firstMode == -9)
        {
        firstMode = ((AbstBlock)e.next()).getState();
        }
        if(thisMode != firstMode)
        {
        firstMode = -1;
        break;
        }
        }
        if(firstMode == -9)
        {
        Err.error("Should never happen that don't have any modes");
        }
        */
        BlockState result = BlockStateCreator.newInstance(oper, StateEnum.FROZEN);
        return result;
        // FreezingState.getNewInstance( oper, this);
    }

    public void setState(BlockState adapterState)
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).setState(adapterState);
        }
    }

    /**
     * From each member of the composite we get the element at point idx
     * and put them all together into the list we return. If none found
     * then an empty list is returned. 
     */
    public Object getDataRecord(int idx)
    {
        List result = null;
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            AbstBlock block = (AbstBlock) e.next();
            Object obj = null;
            if(!block.dataRecordsEmpty()) //No point in getting IndexOutOfBounds
            {
                obj = block.getDataRecord(idx);
            }
            if(obj != null)
            {
                if(result == null)
                {
                    result = new ArrayList();
                }
                result.add(obj);
            }
        }
        if(result == null)
        {
            result = new ArrayList();
        }
        return result;
    }
    
    boolean dataRecordsEmpty()
    {
        boolean result = false;
        List firstFromAll = (List)getDataRecord( 0);
        if(firstFromAll.isEmpty())
        {
            result = true;
        }
        return result;        
    }

    void setDataRecords(Object masterElement,
                        AbstBlock parentBlock)
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).setDataRecords(masterElement, parentBlock);
        }
    }

    public void deleteToBottom(boolean visual, boolean removeComboItems)
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).deleteToBottom(visual, removeComboItems);
        }
    }

    public void freezeToBottom()
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).freezeToBottom();
        }
    }

    public void zeroIndex()
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).zeroIndex();
        }
    }

    public int getIndex()
    {
        return Utils.UNSET_INT;
    }

    public void blankoutDisplay(OperationEnum currentOperation, String reason, boolean removeComboItems)
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).blankoutDisplay(currentOperation, reason, removeComboItems);
        }
    }
    
    public void detachData( boolean detach)
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).detachData( detach);
        }
    }
    
    public void displayLovObjects()
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).displayLovObjects();
        }
    }

    void internalSyncDetail(OperationEnum currentOperation, boolean childrenToo, int idx, boolean removeComboItems)
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            AbstBlock block = (AbstBlock) e.next();
            block.internalSyncDetail(currentOperation, childrenToo, block.getIndex(), removeComboItems);
        }
    }

    void post(OperationEnum key)
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).post(key);
        }
    }

    public void setDataRecordsNull()
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).setDataRecordsNull();
        }
    }

    public void setFreshDataRecords()
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).setFreshDataRecords();
        }
    }

    public void setDisplayEditable(boolean b)
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).setDisplayEditable(b);
        }
    }

    public void reviveNulledDataRecords()
    {
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            ((AbstBlock) e.next()).reviveNulledDataRecords();
        }
    }
    
//    public int dataRecordsSize()
//    {
//        Err.error();
//        return Utils.UNSET_INT;
//    }

    public Iterator iterator()
    {
        return list.iterator();
    }

    public String toString()
    {
        String result = "\n<<Composite Block" + Utils.NEWLINE;
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            result += ((AbstBlock) e.next()).toString() + Utils.NEWLINE;
        }
        result += ">>\n";
        return result;
    }

    public int getId()
    {
        Err.error("getId() not expected to be called on CompositeBlock");
        return -99;
    }

    public void adoptOutBaby(Block block)
    {
        AbstBlock childToRemove = null;
        for(Iterator e = list.iterator(); e.hasNext();)
        {
            AbstBlock child = (AbstBlock) e.next();
            if(child == block)
            {
                childToRemove = block;
                break;
            }
        }
        if(childToRemove != null)
        {
            list.remove(childToRemove);
        }
    }

    public String getName()
    {
        return toString();
    }
} // end class
