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

import org.strandz.lgpl.persist.EntityManagerProviderI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ObjectFoundryUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class ArrayExtent extends IndependentExtent
{
    private Object array[];
    private Class elementType;

    ArrayExtent(EntityManagerProviderI entityManagerProvider, InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        super(entityManagerProvider, addRemoveTrigger);
    }

    ArrayExtent(Object actualArray[], EntityManagerProviderI entityManagerProvider, InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        super(entityManagerProvider, addRemoveTrigger);
        chkAddRemoveTrigger( addRemoveTrigger);
        this.array = actualArray;
        if(actualArray == null)
        {
            Err.error("constructor - actualArray must not be null");
        }
        if(actualArray.length > 0)
        {
            elementType = actualArray[0].getClass();
        }
    }

    int getType()
    {
        return JAVA_ARRAY;
    }

    /**
     *
     */
    public void setList(Object masterElement, Tie tie)
    {
        super.setList(masterElement, tie);
        // should check that field is an Array
        array = (Object[]) tie.getFieldValue(masterElement);
        // Err.pr( "## Doing ListExtent.setList() from master " + masterElement.getClass().getName());
        /*
        * This code will be used where the stored object has not
        * initialized one of it's Vectors:
        */
        if(array == null)
        {
            Class toInstantiate = tie.getFieldType();
            if(toInstantiate != elementType)
            {
                Err.error("Array is not of the right type");
            }
            tie.setFieldValue(masterElement, ObjectFoundryUtils.factory(toInstantiate));
            setList(masterElement, tie); // will only recurse once!
        }
    }

    public Object get(int index)
    {
        // return getActualList().get( index);
        return Array.get(getInternalActualList(), index);
    }

    public Object set(int index, Object value)
    {
        Object previous = get(index);
        Array.set(getInternalActualList(), index, value);
        return previous;
    }

    public boolean isEmpty()
    {
        // return getActualList().isEmpty();
        return (getInternalActualList().length == 0);
    }

    public int size()
    {
        int result = getInternalActualList().length;
        // Err.pr( "## size():" + result);
        return result;
    }

    public void insert(Object obj, int index)
    {
        Err.error("Cannot insert into an Array");
    }

    public Iterator iterator()
    {
        // return getActualList().iterator();
        List list = Arrays.asList(getInternalActualList());
        return list.iterator();
    }

    public void removeElementAt(int index)
    {
        Err.error("Cannot remove from an Array");
    }

    public boolean add(Object obj)
    {
        Err.error("Cannot add to an Array");
        return false;
    }

    public boolean remove(Object obj)
    {
        Err.error(
            "Cannot remove from an Array (instead delete actual data and reload)");
        return false;
    }

    public int indexOf(Object elem)
    {
        int result = -1;
        Object arr[] = getInternalActualList();
        for(int i = 0; i <= arr.length - 1; i++)
        {
            if(arr[i].equals(elem))
            {
                result = i;
                break;
            }
        }
        return result;
    }

    private Object[] getInternalActualList()
    {
        if(array == null)
        {
            Err.error("actualList must not be null");
        }
        return array;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        // result.append( super.toString());
        result.append("[ ArrayExtent: ");

        Iterator en = null;
        if(array != null)
        {
            en = iterator();

            boolean isEmpty = true;
            for(; en.hasNext();)
            {
                isEmpty = false;

                Object obj = en.next();
                result.append(obj);
                result.append(", ");
            }
            if(isEmpty)
            {
                result.append("EMPTY actualList in ArrayExtent ]");
            }
            else
            {
                result.append(" ]");
            }
        }
        else
        {
            result.append("NULL actualList in ArrayExtent ]");
        }
        return result.toString();
    }

    /**
     * Method contains.
     */
    public boolean contains(Object elem)
    {
        boolean result = false;
        Object[] array = getInternalActualList();
        for(int i = 0; i < array.length; i++)
        {
            if(array[i].equals(elem))
            {
                result = true;
                break;
            }
        }
        return result;
    }
}
