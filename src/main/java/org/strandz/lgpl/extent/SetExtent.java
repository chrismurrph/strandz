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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

class SetExtent extends IndependentExtent
{
    private MySet actualList;

    SetExtent(EntityManagerProviderI entityManagerProvider, InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        super(entityManagerProvider, addRemoveTrigger);
    }

    SetExtent(Object actualList, EntityManagerProviderI entityManagerProvider, InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        super(entityManagerProvider, addRemoveTrigger);
        chkAddRemoveTrigger( addRemoveTrigger);
        if(actualList == null)
        {
            Err.error("constructor - actualList must not be null");
        }
        this.actualList = new MySet((Set) actualList);
    }

    /*
    * Testing - will later have to implement previous/next etc, and keep up
    * to date with rowNum to implement Set as a subclass.
    */
    int getType()
    {
        return JAVA_UTIL_SET;
    }

    /**
     *
     */
    public void setList(Object masterElement, Tie tie)
    {
        super.setList(masterElement, tie);

        // should check that field is a Set
        Set set = (Set) tie.getFieldValue(masterElement);
        /*
        * This code will be used where the stored object has not
        * initialized one of it's Vectors:
        */
        if(set == null)
        {
            Class toInstantiate = tie.getFieldType();
            // didn't bother to check if is an instantiable type as did with
            // same code in ListExtent
            tie.setFieldValue(masterElement, ObjectFoundryUtils.factory(toInstantiate));
            setList(masterElement, tie); // will only recurse once!
        }
        else
        {
            actualList = new MySet(set);
        }
    }

    public Object get(int index)
    {
        return getInternalActualList().get(index);
    }

    public Object set(int index, Object value)
    {
        return getInternalActualList().set(index, value);
    }

    public boolean isEmpty()
    {
        return getInternalActualList().isEmpty();
    }

    public int size()
    {
        return getInternalActualList().size();
    }

    public void insert(Object obj, int index)
    {
        super.insert(obj, index);
        getInternalActualList().add(index, obj);
        insertIntoInserted(index);
    }

    public Iterator iterator()
    {
        return getInternalActualList().iterator();
    }

    public void removeElementAt(int index)
    {
        Object obj = get(index);
        // removeFromInserted( index);
        insertIntoDeletes(index);
        getInternalActualList().remove(index);
        super.remove(obj);
    }

    public boolean add(Object obj)
    {
        getInternalActualList().add(obj);
        insertIntoInserted(getInternalActualList().indexOf(obj));
        return true;
    }

    public boolean remove(Object obj)
    {
        // removeFromInserted( obj);
        insertIntoDeletes(obj);
        return getInternalActualList().remove(obj);
    }

    public int indexOf(Object elem)
    {
        return getInternalActualList().indexOf(elem);
    }

    private MySet getInternalActualList()
    {
        if(actualList == null)
        {
            Err.error("actualList must not be null");
        }
        return actualList;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("\n");
        result.append(super.toString());
        result.append("\n");
        for(Iterator en = iterator(); en.hasNext();)
        {
            Object obj = en.next();
            result.append(obj);
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Method contains.
     */
    public boolean contains(Object elem)
    {
        return getInternalActualList().contains(elem);
    }

    /**
     * Sets have no idea of order, yet we need to display a
     * set in some sort of order. Thus the user will see an
     * ArrayList, but we will keep the original set up to date
     * with changes in its contents.
     */
    class MySet // implements List (can't do 'cause it's an inner class)
    {
        ArrayList tandemList;
        Set set;

        MySet(Set set)
        {
            this.set = set;
            tandemList = new ArrayList(set);
        }

        public Object get(int index)
        {
            return tandemList.get(index);
        }

        public Object set(int index, Object value)
        {
            return tandemList.set(index, value);
        }

        public boolean isEmpty()
        {
            return tandemList.isEmpty();
        }

        public int size()
        {
            return tandemList.size();
        }

        public void add(int index, Object obj)
        {
            tandemList.add(index, obj);
            set.add(obj);
        }

        public Iterator iterator()
        {
            return tandemList.iterator();
        }

        public void remove(int index)
        {
            set.remove(tandemList.get(index));
            tandemList.remove(index);
        }

        public void add(Object obj)
        {
            tandemList.add(obj);
            set.add(obj);
        }

        boolean remove(Object obj)
        {
            set.remove(obj);
            return tandemList.remove(obj);
        }

        int indexOf(Object elem)
        {
            return tandemList.indexOf(elem);
        }

        /**
         * Method contains.
         */
        public boolean contains(Object elem)
        {
            return set.contains(elem);
        }
    }
}
