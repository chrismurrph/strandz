/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.extent;

import org.strandz.lgpl.util.Err;

import java.util.Iterator;
import java.util.List;

public class CombinationExtent extends VisibleExtent
{
    private final MemoryList memoryList = new MemoryList();
    private static int times = 0;
    private static int constructedTimes;

    public CombinationExtent()
    {
        super();
    }

    /**
     * Every element of the de being added will only be added to the
     * memoryList if all the other lists contain it. This is how we
     * achieve intersection, which will make intuitive sense to the
     * user.
     */
    /*
    void addDependent( DependentExtent de, Tie tie)
    {
    //experiment, as setList might work it out!
    //memoryList.addList( de, tie);
    }
    */

    int getType()
    {
        return COMBINATION;
    }

    public void setList(Object masterElement, Tie tie)
    {
        Err.error(
            "setList with 2 params is not supposed to be called "
                + "for a CombinationExtent");
    }

    void setList(DependentExtent de, Tie tie)
    {
        /*
        times++;
        Err.pr( "\t>> CombinationExtent.setList() [ID: " + id + "] called with " + de + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        memoryList.setList(de, tie);
    }

    public Object get(int index)
    {
        return memoryList.get(index);
    }

    public Object set(int index, Object value)
    {
        return memoryList.set(index, value);
    }

    public int indexOf(Object elem)
    {
        return memoryList.indexOf(elem);
    }

    public boolean isEmpty()
    {
        return memoryList.isEmpty();
    }

    /**
     * Didn't use checkDisplayList, for when table needs to know the size,
     * but we haven't got the necessary information yet.
     */
    public int size()
    {
        return memoryList.size();
    }

    public void insert(Object obj, int index)
    {
        memoryList.insert(obj, index);
        insertIntoInserted(index);
    }

    public Iterator iterator()
    {
        return memoryList.iterator();
    }
    
    public List getList()
    {
        return memoryList.getList();
    }

    public void removeElementAt(int index)
    {
        // removeFromInserted( index);
        insertIntoDeletes(index);
        memoryList.removeElementAt(index);
    }

    public String toString()
    {
        String result = "CombinationExtent having " + memoryList.displayList.size()
            + " elements";
        return result;
    }
}
