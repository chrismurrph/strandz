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

class AggregationExtent extends DependentExtent
{
    /**
     * actualList will never be displayed, but is changed whenever user moves to
     * a different parent record. It is where the physical data for a Tie is
     * kept.
     */
    private IndependentExtent actualList;
    // will later find out this Object's dataRecords
    private HasCombinationExtent childObj;
    private boolean firstSetList = true;

    AggregationExtent(IndependentExtent actualList, HasCombinationExtent childObj)
    {
        if(actualList == null)
        {
            Err.error("Cannot create ReferenceNodeExtent with a null actualList");
        }
        this.actualList = actualList;
        this.childObj = childObj;
    }

    /**
     *
     */
    int getType()
    {
        return AGGREGATION;
    }

    /**
     * The first ever call to setList, and setList will grab the list that is
     * attached to the Block, and make it the CombinationExtent that every
     * DependentExtent has a reference to. Will then tell this CombinationExtent
     * that this DependentExtent (really actualList) is one of the lists it must
     * take care of.
     * Normal setList activity (which will find in ReferenceExtent.setList
     * also) is to alter the list have been entrusted to take setList calls for,
     * then inform the CombinationExtent that have done this, by calling its
     * single parameter setList.
     */
    public void setList(Object masterElement, Tie tie)
    {
        super.setList(masterElement, tie);
        actualList.setList(masterElement, tie);
        if(firstSetList)
        {
            firstSetList = false;
            // childObj never have their listVectors overwritten
            // should do as calling
            hasCombinationExtent = childObj;
            // ce.addDependent( this, tie);
        }
        hasCombinationExtent.getCombinationExtent().setList(this, tie);
    }

    public Object get(int index)
    {
        return actualList.get(index);
    }

    public Object set(int index, Object value)
    {
        return actualList.set(index, value);
    }

    public boolean isEmpty()
    {
        return actualList.isEmpty();
    }

    public int size()
    {
        return actualList.size();
    }

    public int indexOf(Object elem)
    {
        return actualList.indexOf(elem);
    }

    public void insert(Object obj, int index)
    {
        actualList.insert(obj, index);
    }

    public boolean remove(Object o)
    {
        return actualList.remove(o);
    }

    public Iterator iterator()
    {
        return actualList.iterator();
    }

    public void removeElementAt(int index)
    {
        actualList.removeElementAt(index);
    }

    public String toString()
    {
        return actualList.toString();
    }

    public boolean contains(Object obj)
    {
        return actualList.contains(obj);
    }
}
