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
import org.strandz.lgpl.util.IdentifierI;

import java.util.Iterator;

/**
 * abstract class UIToClassAdapter
 * Composite pattern
 */

abstract public class AbstBlock implements IdentifierI
{
    ProdNodeI node;
    // The 'detail' side, which may be composite or null, or a Block.
    AbstBlock child;
    OperationsProcessor oper;
    /**
     * Each time anyKey is pressed, it is called again and
     * again as we go down thru the levels. Sometime we
     * might need to know which level we are currently at
     */
    int level = 0; // 0 is meaningless, as levels start at 1

    public AbstBlock getChild()
    {
        return child;
    }

    public void setChild(AbstBlock child)
    {
        this.child = child;
    }

    abstract public BlockState getState();

    abstract public void setState(BlockState adapterState);

    //
    // These are methods that details have in the past required. Every class
    // in this composite pattern can act as a detail - thus they must be
    // implemented in Workhorse, Composite and Null.
    //
    abstract void setDataRecords(Object masterElement,
                                 AbstBlock parentBlock);

    abstract public Object getDataRecord(int idx);
    //abstract public int dataRecordsSize();
    
    abstract boolean dataRecordsEmpty();

    abstract public void zeroIndex();

    abstract public int getId();

    /**
     * In future this will return void. User will raise an exception in the
     * validation event eg./ throw ApplicationError (same principle used for
     * adds, deletes and edits). This will be caught in completeAdd (and others
     * such as applyDifferences) with the result that the collected command
     * structure (which is simple here, but will consist of a b4Image for
     * delete and validation problems) will be wound back eg./ delete will be
     * re-inserted. If mode is not already EDIT then it should be put back
     * into EDIT (as this is the only mode that allows deletes and inserts)
     */
    abstract public void deleteToBottom(boolean visual, boolean removeComboItems);

    abstract public void freezeToBottom();

    // abstract boolean dataRecordsEmpty();
    abstract public void blankoutDisplay(OperationEnum currentOperation, String reason, boolean removeComboItems);

    abstract public void detachData( boolean detach);
    
    abstract public void displayLovObjects();

    // abstract void changeMode();
    abstract void internalSyncDetail(OperationEnum currentOperation, boolean childrenToo, int idx, 
                                     boolean removeComboItems);

    /**
     * recursing done in internalLastHit()
     */
    abstract void post(OperationEnum key);

    // abstract void freezeAllBelow( AbstBlock block);

    /**
     * In instances where parent needs to find out the state
     * of the child/children, and act differently in each
     * circumstance, this deemed to be simpler than a spate
     * of new composite methods. Used to call getState() and
     * then act accordingly.
     */
    abstract public Iterator iterator();

    abstract public void setDataRecordsNull();

    abstract public void reviveNulledDataRecords();

    abstract public void setDisplayEditable( boolean b);

    abstract public void setFreshDataRecords();

    abstract public void adoptOutBaby( Block block);

    abstract public int getIndex();

    public ProdNodeI getNode()
    {
        return node;
    }
}
