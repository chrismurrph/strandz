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
package org.strandz.data.supersix.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Round
{
    private List slots = new ArrayList();
    private int maxNumSlots = 0;
    private int ordinal;
    public boolean acceptSecond = false;
    
    public Round( int numTeams, int ordinal)
    {
        if(Utils.isOdd( numTeams))
        {
            Err.pr( "Have odd number of teams " + numTeams +  " so there will be a bye every round");
            numTeams++;
        }
        maxNumSlots = numTeams / 2;
        Err.pr( "Num of teams is " + numTeams + " so num slots to fill up is " + maxNumSlots);
        this.ordinal = ordinal;
    }
    
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append( ordinal + "/");
        result.append(FileUtils.DOCUMENT_SEPARATOR);
        for(int i = 0; i < slots.size(); i++)
        {
            Slot slot = (Slot) slots.get(i);
            result.append( (i+1) + " " + slot + FileUtils.DOCUMENT_SEPARATOR);
        }
        return result.toString();
    }

    public void addSlot(Slot slot)
    {
        if(slots.size() + 1 > maxNumSlots)
        {
            Err.error( "Already have " + slots.size() + " slots");
        }
        slots.add(slot);
        slot.addRoundPlayedIn(this);
    }

//    private void removeSlot(Slot slot)
//    {
//        if(slots.remove(slot))
//        {
//            //TODO Will be orphans like this so research on how to best
//            //delete the RosterSlot at the same time as this method is
//            //called
//            if(slot.getRoundPlayedIn() == this)
//            {
//                slot.setRoundPlayedIn(null);
//            }
//        }
//    }

    public List getSlots()
    {
        return Collections.unmodifiableList(slots);
    }

    public boolean isFilled()
    {
        boolean result = slots.size() == maxNumSlots;
        //Err.pr( "Num slots is " + slots.size() + " while max is " + maxNumSlots);
        return result;
    }

    public boolean teamAlreadyExistsIn( Combination combination)
    {
        boolean result = false;
        for(Iterator iterator = slots.iterator(); iterator.hasNext();)
        {
            Slot slot = (Slot) iterator.next();
            if(slot.getCombination().getTeam1().equals(combination.getTeam1()) ||
                    slot.getCombination().getTeam1().equals(combination.getTeam2()) ||
                    slot.getCombination().getTeam2().equals(combination.getTeam1()) ||
                    slot.getCombination().getTeam2().equals(combination.getTeam2())
                    )
            {
                result = true;
                //Err.pr( "A team from " + combination + " already exists in round " + ordinal + 
                //        " with " + getSlots() + " slots");
                break;
            }
        }
        return result;
    }

    public int getOrdinal()
    {
        return ordinal;
    }
}
