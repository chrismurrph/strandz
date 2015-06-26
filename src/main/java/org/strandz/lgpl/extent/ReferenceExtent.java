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

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.DOHelperUtils;
import org.strandz.lgpl.util.Err;

import java.util.ArrayList;
import java.util.Iterator;

class ReferenceExtent extends DependentExtent
{
    private IndependentExtent hugeList;
    /*
    * Will be dynamically cleared and re-populated each time
    * the independent changes (see setList):
    */
    private ArrayList displayList = new ArrayList(); // may not always be a ArrayList
    private Tie tie;
    // The object that this list currently pertains to. eg./ would be the
    // order that we hold a load of order items for.
    private Object referenceValue;

    private static int times;

    ReferenceExtent(IndependentExtent hugeList, HasCombinationExtent ce, Tie tie)
    {
        if(hugeList == null)
        {
            Err.error("Cannot create ReferenceNodeExtent with a null hugeList");
        }
        this.hugeList = hugeList;
        times++;
        Err.pr(SdzNote.CANT_ADD_RS, "hugeList have been set has size " + hugeList.size() + " times " + times);
        if(ce == null)
        {
            Err.error("Cannot create a ReferenceExtent with a null CombinationExtent");
        }
        this.hasCombinationExtent = ce;
        // ce.addDependent( this, tie);
    }

    /* testing - will later have to implement previous/next etc, and keep up
    * to date with rowNum to implement ArrayList as a subclass.
    */

    /**
     *
     */
    int getType()
    {
        return REFERENCE;
    }


    private void debug(Object refVal, Object masterElement)
    {/*
     Print.pr( "See if refVal " + refVal + " equals masterElement " +
     masterElement);
     if(refVal == null)
     {
     //Err.pr( "Can't setList() for refVal being null");
     }
     else if(refVal.toString().equals( "null"))
     {
     Err.error( "Can't setList() for refVal being null String");
     }
     else if(refVal.toString().equals( null))
     {
     Err.error( "Can't setList() for refVal being null");
     }
     else
     {
     Print.pr( "refVal: <" + refVal + ">, of class: " +
     refVal.getClass().getName());
     }
     */}

    /**
     * Re-create the displayList. Will appear to outside world that only
     * this list exists.
     */
    public void setList(Object masterElement, Tie tie)
    {
        Err.pr(SdzNote.CANT_ADD_RS, "$$$   setList called for reference extent, masterElement: " + masterElement);
        Err.pr(SdzNote.CANT_ADD_RS, "$$$   setList called for reference extent, tie: " + tie.hashCode());
        super.setList(masterElement, tie);
        this.tie = tie;
        referenceValue = masterElement;
        displayList.clear();

        Object refVal, element;
        for(Iterator e = hugeList.iterator(); e.hasNext();)
        {
            element = e.next();
            // Err.pr("Looking for " + refField + " in " + element);
            refVal = tie.getFieldValue(element);
            debug(refVal, masterElement);
            if(DOHelperUtils.equalityTest(masterElement, refVal))
            // if(masterElement == refVal)
            {
                Err.pr(SdzNote.CANT_ADD_RS, "Match found on refVal " + refVal);
                displayList.add(element);
            }
            else
            {
//        Err.pr(SdzNote.cantAddRS, "");
//        Err.pr(SdzNote.cantAddRS, "NO match found on refVal: " + refVal);
//        Err.pr(SdzNote.cantAddRS, "WHEN CF WITH: " + masterElement);
//        Err.pr(SdzNote.cantAddRS, "");
            }
        }
        hasCombinationExtent.getCombinationExtent().setList(this, tie);
    }

    public Object get(int index)
    {
        return displayList.get(index);
    }

    public Object set(int index, Object value)
    {
        return displayList.set(index, value);
    }

    public boolean isEmpty()
    {
        return displayList.isEmpty();
    }

    public int indexOf(Object elem)
    {
        return displayList.indexOf(elem);
    }

    public int size()
    {
        return displayList.size();
    }

    public boolean remove(Object o)
    {
        // Err.pr( "hugeList size is " + hugeList.size());
        hugeList.remove(o);
        return displayList.remove(o);
    }

    public boolean contains(Object obj)
    {
        return displayList.contains(obj);
    }

    /**
     * Current reference value is implicitly set.
     * Are careful to insert in same relative order, although this depends on
     * hugeList being an ordered list (later NavExtent can have an
     * isOrdered method so can code both ways).
     */
    public void insert(Object obj, int index)
    {
        tie.setFieldValue(obj, referenceValue);
        Err.pr(SdzNote.BG_ADD, "--------------ReferenceExtent.insert()ing " + obj);
        displayList.add(index, obj);
        relativePositionInsert(obj, displayList, hugeList);
    }

    public Iterator iterator()
    {
        return displayList.iterator();
    }

    public void removeElementAt(int index)
    {
        hugeList.remove(displayList.get(index));
        displayList.remove(index);
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        // result.append( super.toString());
        result.append("[ ReferenceExtent: ");

        Iterator en = null;
        if(displayList != null)
        {
            en = iterator();

            boolean isEmpty = true;
            for(; en.hasNext();)
            {
                isEmpty = false;

                Object obj = en.next();
                result.append(obj);
                result.append(",");
            }
            if(isEmpty)
            {
                result.append("EMPTY displayList in ReferenceExtent ]");
            }
            else
            {
                result.append(" ]");
            }
        }
        else
        {
            result.append("NULL displayList in ReferenceExtent ]");
        }
        return result.toString();
    }
}
