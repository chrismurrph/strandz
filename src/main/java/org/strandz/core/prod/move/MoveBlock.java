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
package org.strandz.core.prod.move;

import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.MoveBlockI;
import org.strandz.core.domain.MoveTrackerI;
import org.strandz.core.domain.NullAdapter;

import java.util.Iterator;
import java.util.List;

public class MoveBlock implements MoveBlockI
{
    private List adapters;
    private MoveNodeI nodeI;
    private Object tableControl;
    private MoveTrackerI moveManager;
    private String name;
    private ItemAdapter setItemValueAdapter;
    /*
    * Will be set to false whenever a blank or a validateRecord test is
    * failed. Only used by a table to stop a mouse click.
    */
    private boolean recordValidationOutcome = true;
    private static int times;
    private static int constructedTimes;
    private int id;

    private MoveBlock()
    {
        constructedTimes++;
        id = constructedTimes;
    }

    public MoveBlock(List adapters,
                     MoveNodeI nodeI,
                     Object tableControl,
                     MoveTrackerI moveTrackerI)
    {
        this();
        this.adapters = adapters;
        this.nodeI = nodeI;
        this.tableControl = tableControl;
        this.moveManager = moveTrackerI;
        this.name = nodeI.getName();
        /*
        Err.pr( "MoveBlock " + id + " has adapters: ");
        for(Iterator iter = adapters.iterator(); iter.hasNext();)
        {
        Adapter ad = (Adapter)iter.next();
        Err.pr( ad.getName());
        }
        */
    }

    public boolean isIgnoreValidation()
    {
        return nodeI.isIgnoreValidation();
    }

    public MoveTrackerI getMoveTracker()
    {
        return moveManager;
    }

    public boolean getRecordValidationOutcome()
    {
        // Err.pr( "------------------RecordValidationOutcome to ret " + recordValidationOutcome);
        return recordValidationOutcome;
    }

    public void setRecordValidationOutcome(boolean b)
    {
        recordValidationOutcome = b;
        // Err.pr( "------------------RecordValidationOutcome SET TO " + recordValidationOutcome);
        // Err.stack();
    }

    public boolean isInError()
    {
        boolean result = false;
        for(Iterator iter = adapters.iterator(); iter.hasNext();)
        {
            ItemAdapter ad = (ItemAdapter) iter.next();
            if(ad.isInError())
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public MoveNodeI getNodeI()
    {
        return nodeI;
    }

    public Object getTableControl()
    {
        return tableControl;
    }

    public List getAdapters()
    {
        return adapters;
    }

    /**
     * before 15/05/05
     * Adapter that was being returned was basically randomly selected. Even if use
     * ordinal (don't for fieldadapters) the fact that have it means that it gets
     * validated. It is senseless to validate something that the user has never
     * gone into. Thus:
     * after 15/05/05
     * Will return a NullAdapter. Thus validation as move away from it will be a NOP.
     *
     * @return the first adapter for a block
     */
    ItemAdapter getFirstVisualAdapter()
    {
        ItemAdapter result = new NullAdapter();
        /*
        for(int i = 0; i < adapters.size(); i++)
        {
          ItemAdapter itemAdapter = (ItemAdapter)adapters.get( i);
          if(itemAdapter.isVisual())
          {
            result = itemAdapter;
            break;
          }
        }
        */
        return result;
    }

    public String getName()
    {
        return name;
    }

    public void setItemValueAdapter(ItemAdapter itemAdapter)
    {
        setItemValueAdapter = itemAdapter;
        /*
        * ACTUALLY, the following was not the problem!! (yet)
        * When setting an item this was the only place that came near a
        * MoveTracker. Basically the error message is left around after
        * the user performs an operation, so she can see what the reason
        * for failure was. Setting the item value, as doing here, may well
        * be the thing that corrects the problem. Anyhow, the user is doing
        * something active, and if wanted to look at the error message
        * should have done so by now. It is no problem to clear the
        * ValidationContext. If the problem still exists and the user
        * does another operation that causes the same error, then
        * another validation context in error will be created. We also need
        * to clear to not block further operations.
        */
        //moveManager.createNewValidationContext();
    }

    ItemAdapter getItemValueAdapter()
    {
        return setItemValueAdapter;
    }

    public String toString()
    {
        String result = name;
        result += " (" + id + ")";
        return result;
    }
}
/*
 public interface ValidatePointer
 {
 void setAdapterReleased( Adapter itemAdapter);
 void moveAdapterReleased();
 void itemValidationPoint();
 void throwApplicationError( ValidationException ex, int type);
 }
 */
