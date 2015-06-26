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
import org.strandz.lgpl.persist.EntityManagerProviderI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This whole package deals with Master/Detail relationships.
 * <p/>
 * Looking at the subclasses of NavExtent, we have DependentExtent,
 * and then CombinationExtent and IndependentExtent which are subclasses
 * of VisibleExtent.
 * <p/>
 * IndependentExtent: underneath here is a simple decorator pattern by which
 * any list can be supported. External packages can deal with an
 * IndependentExtent without knowing what sort of a list it is. The user can
 * pass in his favorite collection, and here we can asertain which type it is,
 * and create that type, yet pass back an IndependentExtent.
 * By default every Block has a CombinationExtent, and the user is expected to
 * have his data absorbed by calling instantiable.setInitialData(). ie./ When
 * user calls getDataRecords(), he will get back a CombinationExtent.
 * <p/>
 * DependentExtent: underneath are the types that are used for the "Many" side
 * of a Tie. When a master moves to a new record a detail that is linked by
 * a reference must do a traditional relational join. For this to happen we
 * need the whole of the detail object's extent. Thus the user must give us
 * the ReferenceExtent. (Which means that setInitialData must be called on
 * the Node in question) The AggregationExtent is for the situation where the
 * master 'record' contains a list of the detail 'records'. Here no user
 * interaction is required - a call from the master to set the new detail list
 * (for example when go to a new record on master) will trigger the getting of
 * this list from the master.
 * <p/>
 * CombinationExtent: recognises the fact that a detail extent may in fact be
 * the detail extent for many masters - a multiple detail. A CombinationExtent
 * is actually made up of one or more DependentExtents. These two extent types
 * contain one another to keep up to date. Master navigation affects a
 * DependentExtent, which then informs the CombinationExtent. For all other
 * user interaction the communication is the other way round. The
 * CombinationExtent is on the Block and is obviously what the user sees.
 * <p/>
 * Factory Method Pattern class.
 * Validation occurs in any of the static 'constructors'.
 * Bags will never be supported, as all objects need to be of the same type.
 * Assumption that same element will not occur twice, even though supporting
 * Vectors.
 */
abstract public class NavExtent implements Collection
{
    /**
     * When become ultra-dynamic for this sort of thing, user can provide
     * a Constructor for the List she wants. It will be a constructor
     * for a class that implements NavExtent.
     */
    public static final int UNKNOWN_COLLECTION = -1;
    static final int JAVA_UTIL_VECTOR = 1;
    //static final int JDO_EXTENT = 3;
    static final int REFERENCE = 4;
    static final int AGGREGATION = 5;
    static final int COMBINATION = 6;
    static final int JAVA_UTIL_SET = 7;
    static final int JAVA_ARRAY = 8;
    static final int JAVA_UTIL_COLLECTION = 9;
    // private int type = -1;

    /**
     * At time that Tie is first created the actual list is unknown. Will be
     * changing constantly (each time Block.setDataRecords called)
     */
    public static AggregationExtent createAggregation(
        Class collectionClass, HasCombinationExtent childObj, 
        EntityManagerProviderI entityManagerProvider, 
        InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        // new MessageDlg("createDependent: List class type is " + collectionClass.getName());
        int type = ascertainType(collectionClass);
        if(type == -1)
        {
            Err.error("Currently NavExtent does not support " + collectionClass);
        }

        IndependentExtent independentExtent = null;
        if(type == JAVA_UTIL_VECTOR)
        {
            independentExtent = new ListExtent(entityManagerProvider, addRemoveTrigger);
        }
//    else if(type == JDO_EXTENT)
//    {
//      independentExtent = new _JDOExtent();
//    }
        else if(type == JAVA_UTIL_COLLECTION)
        {
            independentExtent = new CollectionExtent(entityManagerProvider, addRemoveTrigger);
        }
        else if(type == JAVA_UTIL_SET)
        {
            independentExtent = new SetExtent(entityManagerProvider, addRemoveTrigger);
        }
        else if(type == JAVA_ARRAY)
        {
            independentExtent = new ArrayExtent(entityManagerProvider, addRemoveTrigger);
        }
        else
        {
            Err.error("Need to construct an IndependentExtent of type " + type);
        }
        Err.pr(SdzNote.EM_BECOMES_NULL, "Have created a new AggregationExtent");
        return new AggregationExtent(independentExtent, childObj);
    }

    /**
     * hugeList passed in will have been created by a call to
     * createIndependent
     */
    public static ReferenceExtent createReference(IndependentExtent hugeList,
                                                  HasCombinationExtent ce,
                                                  Tie tie)
    {
        return new ReferenceExtent(hugeList, ce, tie);
    }

    /*
    * Here will use the last part of the String value of collectionClass
    * to check if is ODMG_ORDERED_COLLECTION or POET_EXTENT etc. If passes
    * this String test then will allow to construct the correct sub type.
    */
    public static int ascertainType(Class collectionClass)
    {
        int result = UNKNOWN_COLLECTION;
        //Err.pr( "Trying " + collectionClass.toString());
        if(List.class.isAssignableFrom(collectionClass))
        {
            result = JAVA_UTIL_VECTOR;
        }
        else if(Set.class.isAssignableFrom(collectionClass))
        {
            result = JAVA_UTIL_SET;
        }
        /*
         * Doubt we using Extent anymore at all 10/05/05
        else if(Extent.class.isAssignableFrom( collectionClass))
        {
          result = JDO_EXTENT;
        }
        */
        else if(collectionClass.isArray())
        {
            result = JAVA_ARRAY;
        }
        else if(Collection.class.isAssignableFrom(collectionClass))
        {
            result = JAVA_UTIL_COLLECTION;
        }
        else
        {// Err.error("Currently NavExtent does not support " + collectionClass);
        }
        return result;
    }

    /**
     * Can be called for any object. If object happens to be a list of
     * some type that NavExtent knows about then we can return the class
     * of the first element.
     */
    public static Class bestGuessHomogeneousClass(Class clazz)
    {
        Err.error(
            "To write this method clazz will need"
                + " to tell us what types it stores");
        return null;
    }

    public static Class bestGuessHomogeneousClass(Object listObject)
    {
        Class result = null;
        int type = NavExtent.ascertainType(listObject.getClass());
        if(type == JAVA_UTIL_VECTOR)
        {
            List list = (List) listObject;
            if(!list.isEmpty())
            {
                result = list.get(0).getClass();
            }
        }
        else if(type == UNKNOWN_COLLECTION)
        {// nufin
        }
        else
        {
            Err.error("Write some code!!");
        }
        return result;
    }

    /**
     * testing - will later have to implement previous/next etc, and keep up
     * to date with rowNum to implement List as a subclass.
     */
    abstract int getType();

    public void setList(Object masterElement, Tie tie)
    {// Err.pr( "## Doing ListExtent.setList() from master " + masterElement);
    }

    /*
    * Can ensure that calls to this will always mean that the cursor has
    * moved. If no then provide next, previous and setCursorAt (or just
    * the last) so can keep hold of a cursor, so can change position in
    * recordInfos, and possibly fire an event whenever the current
    * RecordInfo is not the same as the last one. But then how could
    * Controller listen to two conflicting sets of events? Could know
    * that this event always came with BlockChange event, so would record
    * a BlockChange event but only implement actions of when had received
    * this too. Listener method and event generated could be the same for
    * both. This functionality would not occur if was not a listener to
    * these events. Block could handle this complexity.
    */
    abstract public Object get(int index);

    abstract public Object set(int index, Object value);

    abstract public boolean isEmpty();

    abstract public int size();

    abstract public void insert(Object obj, int index);

    abstract public Iterator iterator();
    
    public List getList()
    {
        Err.error( "getList() not yet implemented in " + this.getClass().getName());
        return null;
    }

    abstract public void removeElementAt(int index);

    abstract public int indexOf(Object obj);

    public String toString()
    {
        String result = "\n<";
        for(Iterator it = iterator(); it.hasNext();)
        {
            Object obj = it.next();
            if(obj != null)
            {
                result += obj.toString() + Utils.NEWLINE;
            }
            else
            {
                result += "found-NULL" + Utils.NEWLINE;
            }
        }
        result += ">\n";
        return result;
    }

    /**
     * Utility function - value that have already inserted into a subList
     * will be inserted at the same relative point in the emcompassing
     * wholeList. Will be a simple implementation when ordering is not
     * supported by the wholeList.
     */
    static void relativePositionInsert(Object obj,
                                       List subList,
                                       NavExtent wholeList)
    {
        int index = subList.indexOf(obj);
        if(index == -1)
        {
            Err.error(
                "relativePositionInsert to be called only after "
                    + "have inserted into subList");
        }

        Object refObject;
        boolean insertB4 = false;
        try
        {
            refObject = subList.get(index - 1);
        }
        catch(IndexOutOfBoundsException e1)
        {
            insertB4 = true;
            try
            {
                refObject = subList.get(index + 1);
            }
            catch(IndexOutOfBoundsException e2)
            {
                if(subList.size() == 1)
                {
                    wholeList.insert(obj, 0); // 06/06/2003
                }
                else
                {
                    // an object of the 'sub type' does not already exist in the
                    // wholeList, so simply put in on the end of the wholeList:
                    wholeList.insert(obj, wholeList.size());
                }
                return;
            }
        }
        index = wholeList.indexOf(refObject);
        if(!insertB4)
        {
            wholeList.insert(obj, index + 1);
        }
        else
        {
            wholeList.insert(obj, index);
        }
    }

    public Object[] toArray()
    {
        Err.error("[8]Not implemented Object[] toArray() for type " + this.getClass().getName());
        return null;
    }

    public Object[] toArray(Object[] a)
    {
        Err.error("[9]Not implemented");
        return null;
    }

    public boolean containsAll(Collection c)
    {
        Err.error("[10]Not implemented");
        return false;
    }

    public boolean removeAll(Collection c)
    {
        Err.error("[11]Not implemented");
        return false;
    }

    public boolean retainAll(Collection c)
    {
        Err.error("[12]Not implemented");
        return false;
    }

    public void clear()
    {
        Err.error("[13]Not implemented");
    }

    public boolean addAll(Collection c)
    {
        Err.error("[14]Not implemented");
        return false;
    }

    public boolean add(Object o)
    {
        Err.error("[15]Not implemented");
        return false;
    }

    public boolean contains(Object o)
    {
        Err.error("[16]Not implemented");
        return false;
    }

    public boolean remove(Object o)
    {
        Err.error("[17]Not implemented");
        return false;
    }
}
