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
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.Iterator;

/**
 * class NullBlock
 * Used when a master is not a master, so that all communications to
 * children that are not there are meaningless.
 */

public class NullBlock extends AbstBlock
{
    public NullBlock()
    {// to make debugging easier
        // seems no longer used
        // dataRecords = new ArrayList();
        // used when looking at the size of a child's dataRecords
    }

    public BlockState getState()
    {
        BlockState result = BlockStateCreator.newInstance(oper, StateEnum.FROZEN);
        return result;
        // return FreezingState.getNewInstance( oper, this);
    }

    public void setState(BlockState adapterState)
    {
    }

    public void deleteToBottom(boolean visual, boolean removeComboItems)
    {/*
     Interesting to see NullBlock also has a composite hanging off it
     MessageDlg noBackDlg = new MessageDlg(
     "nullToBottom IN NULLADAPTOR ");
     */}

    public void freezeToBottom()
    {
    }

    public Object getDataRecord(int idx)
    {
        return null;
    }

    boolean dataRecordsEmpty()
    {
        return true;
    }


    void setDataRecords(Object masterElement,
                        AbstBlock parentBlock)
    {// Err.error("[[ INSIDE NULL setDataRecords");
        // Err.pr("[[ INSIDE NULL setDataRecords");
    }

    public void zeroIndex()
    {
    }

    /**/
    public void blankoutDisplay(OperationEnum currentOperation, String reason, boolean removeComboItems)
    {
    }
    
    public void detachData( boolean detach)
    {
    }
    
    public void displayLovObjects()
    {
        
    }
    

    /**/
    void internalSyncDetail(OperationEnum currentOperation, boolean childrenToo, 
                            int idx, boolean removeComboItems)
    {
    }

    void post(OperationEnum key)
    {
    }

    // void freezeAllBelow( AbstBlock block)
    // {
    // }
    public Iterator iterator()
    {
        return new NullEnumerator(this);
    }

    public void setDataRecordsNull()
    {
    }

    public void setDataRecordsListNull()
    {
    }

    public void setFreshDataRecords()
    {
    }

    public void setDisplayEditable(boolean b)
    {
    }

    public void reviveNulledDataRecords()
    {
    }
    
//    public int dataRecordsSize()
//    {
//        Err.error();
//        return Utils.UNSET_INT;
//    }

    public int getId()
    {
        Err.error("getId() not expected to be called on NullBlock");
        return -99;
    }

    public void adoptOutBaby(Block block)
    {
    }

    public int getIndex()
    {
        return Utils.UNSET_INT;
    }

    public String getName()
    {
        return toString();
    }


} // end class


final class NullEnumerator implements Iterator
{
    NullBlock block;
    boolean moreElements = true;

    NullEnumerator(NullBlock block)
    {
        this.block = block;
    }

    public boolean hasNext()
    {
        boolean toReturn = moreElements;
        moreElements = false;
        return toReturn;
    }

    /**
     * Can be called once, and will return a NullBlock.
     */
    public Object next()
    {
        return block;
    }

    public void remove()
    {
        Err.error("remove not implemented for NullEnumerator");
    }
}
