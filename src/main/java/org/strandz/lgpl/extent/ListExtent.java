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
import org.strandz.lgpl.util.DebugList;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.ObjectFoundryUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListExtent extends IndependentExtent implements AddFailer
{
    private ActualList actualList;
    private String dupPKError;
    private static int times;
    private static boolean debugging = false;

    ListExtent(EntityManagerProviderI entityManagerProvider, InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        super(entityManagerProvider, addRemoveTrigger);
    }

    public String getDupPKError()
    {
        String result = dupPKError;
        return result;
    }

    ListExtent(List displayList, EntityManagerProviderI entityManagerProvider, InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        super(entityManagerProvider, addRemoveTrigger);
        times++;
        Err.pr(SdzNote.BG_ADD, "ACTUAL list in constructor: " + displayList.getClass().getName());
        Err.pr(SdzNote.BG_ADD, "values: " + displayList);
        Err.pr(SdzNote.BG_ADD, "addRemoveTrigger: " + addRemoveTrigger);
        if(times == 0)
        {
            Err.stack();
        }
        this.actualList = new ActualList(displayList, addRemoveTrigger);
        if(displayList == null)
        {
            Err.error("constructor - displayList must not be null");
        }
        /*
        if(this.actualList.size() > 0)
        {
        Err.pr( "## ListExtent constructed with " +
        this.actualList.get( 0).getClass().getName());
        }
        */
    }

    /*
    * Testing - will later have to implement previous/next etc, and keep up
    * to date with rowNum to implement List as a subclass.
    */
    int getType()
    {
        return JAVA_UTIL_VECTOR;
    }

    /**
     *
     */
    public void setList(Object masterElement, Tie tie)
    {
        super.setList(masterElement, tie);

        Object obj = null;
        if(masterElement != null)
        {
            obj = tie.getFieldValue(masterElement);
        }
        if(obj != null)
        {
            Err.pr(SdzNote.BG_ADD, "ACTUAL list set: " + obj.getClass().getName());
        }
        else
        {
            Err.pr(SdzNote.EMP_ERRORS, "");
        }
        if(obj != null && SdzNote.WANT_ALL_ON_DETAIL.isVisible())
        {
            Print.prList( (List) obj, "Need see many if there are many");
        }
        actualList = new ActualList((List) obj, addRemoveTrigger);
        /*
        * This code will be used where the stored object has not
        * initialized one of it's Vectors:
        */
        if(actualList.hasNullDisplay())
        {
            Class toInstantiate = tie.getFieldType();
            if(toInstantiate == List.class)
            {
                toInstantiate = ArrayList.class;
            }
            if(masterElement != null)
            {
                tie.setFieldValue(masterElement, ObjectFoundryUtils.factory(toInstantiate));
                setList(masterElement, tie); // will only recurse once!
            }
        }
    }

    public Object get(int index)
    {
        Object result = getInternalActualList().get(index);
        pr("Getting: " + result);
        return result;
    }

    public Object set(int index, Object value)
    {
        Object result = get(index);
        getInternalActualList().set(index, value);
        return result;
    }

    public boolean isEmpty()
    {
        return getInternalActualList().isEmpty();
    }

    public int size()
    {
        int result = getInternalActualList().size();
        // pr( "## size():" + result);
        return result;
    }
    
    public Object[] toArray()
    {
        Object[] result = getInternalActualList().display().toArray();
        return result;
    }

    public void insert(Object obj, int index)
    {
        super.insert(obj, index);

        ActualList list = getInternalActualList();
        list.add(index, obj);
        if(list.display instanceof DebugList)
        {
            Err.pr(
                SdzNote.BG_ADD, "%% Added (1), obj to list " + ((DebugList) list.display).id + " at "
                + index);
        }
        else
        {
            Err.pr(SdzNote.BG_ADD, "%% Added (1), obj to list " + list.hashCode() + " at " + index);
        }

        boolean ok = true;
        dupPKError = null;
        if(list.display() instanceof AddFailer)
        {
            AddFailer failer = (AddFailer) list.display();
            if(failer.getDupPKError() != null)
            {
                dupPKError = failer.getDupPKError();
                ok = false;
            }
        }
        if(ok)
        {
            if(index < list.size() && list.get(index) == obj)
            {
                insertIntoInserted(index);
            }
            else
            {
                if(list.get(index) != obj)
                {
                    Err.pr( "obj wanted to insert: <" + obj + ">");
                    Err.pr( "obj get: <" + list.display.get(index) + ">");
                    Print.prList( list.display, "Is new obj there?", false);
                    Err.error( "obj not the same at " + index);
                }
                else
                {
                    Err.pr("Problem adding, obj: " + obj);
                    Err.pr("Problem adding, index: " + index);
                    Err.error("Problem adding, list.size(): " + list.size());
                }
            }
        }
    }

    public Iterator iterator()
    {
        return getInternalActualList().iterator();
    }

    public void removeElementAt(int index)
    {
        // removeFromInserted( index);
        insertIntoDeletes(index);

        Object obj = get(index);
        pr("%% Removed (1), obj from list " + obj);
        getInternalActualList().remove(obj);
        super.remove(obj);
    }

    public boolean add(Object obj)
    {
        boolean result = getInternalActualList().add(obj);
        pr("%% Added (2), obj to list " + obj);
        if(result)
        {
            insertIntoInserted(getInternalActualList().indexOf(obj));
        }
        return result;
    }

    private void pr(String str)
    {
        if(debugging)
        {
            Err.pr(str);
        }
    }

    /**
     * This is where the references to a master are removed
     * from.
     */
    public boolean remove(Object obj)
    {
        /*
        times++;
        pr( "%% To remove (2), obj from ListExtent <" +
        obj + "> of type " + obj.getClass().getName() + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        int idx = indexOf(obj);
        if(idx == -1)
        {
            Err.pr("size is " + size());
            Err.error("ListExtent does not have an " + obj);
        }
        insertIntoDeletes(obj);

        // removeFromInserted( obj);
        boolean result = getInternalActualList().remove(obj);
        super.remove(obj);
        return result;
    }

    public int indexOf(Object elem)
    {
        return getInternalActualList().indexOf(elem);
    }

    private ActualList getInternalActualList()
    {
        if(actualList == null)
        {
            Err.error("actualList must not be null");
        }
        return actualList;
    }
    
    public List getList()
    {
        return getInternalActualList().display();
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        // result.append( super.toString());
        result.append("[ ListExtent: ");

        Iterator en = null;
        if(actualList != null)
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
                result.append("EMPTY actualList in ListExtent ]");
            }
            else
            {
                result.append(" ]");
            }
        }
        else
        {
            result.append("NULL actualList in ListExtent ]");
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

    public static List refineFromMatchingValues
        (
            List inList, List queryAttributes
        )
    {
        List result = new ArrayList();
        for(Iterator iter = inList.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            boolean matchingRecord = true;
            for(Iterator iterator = queryAttributes.iterator(); iterator.hasNext();)
            {
                ItemMatcher attribute = (ItemMatcher) iterator.next();
                if(!attribute.itemMatchesData(element))
                {
                    matchingRecord = false;
                    break;
                }
                else
                {
                    matchingRecord = true;
                }
            }
            if(matchingRecord)
            {
                result.add(element);
            }
        }
        return result;
    }
}
