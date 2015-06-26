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
import org.strandz.lgpl.util.Err;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an intersection of many DependentExtents. Each element
 * in this list belongs to every list in listOfLists. Thus for
 * instance with deleting, the right element in all the right
 * DependentExtents will be deleted. Each DependentExtent will in turn
 * delete using a not dissimilar notion of deleting the right element
 * from hugelist (in the case of a ReferenceExtent). Last point gives
 * rise to a good test - maybe it would be better to set the detail's
 * master reference to null.
 * <p/>
 * Note that the use of the word 'Memory' here has no connection with
 * the same word used in the package persist.
 */
class MemoryList
{
    private HashMap mapOfLists = new HashMap(); // DependentExtents
    final List displayList = new ArrayList();
    private static int times;
    private static int constructedTimes;
    private int id;
    private static boolean debugging = SdzNote.WANT_ALL_ON_DETAIL.isVisible();

    private void pr(String str)
    {
        if(debugging)
        {
            Err.pr(str);
        }
    }

    public MemoryList()
    {
        constructedTimes++;
        id = constructedTimes;
        pr("## CONSTRUCTED MemoryList ID: " + id);
    }

    /**
     * Use of this class will be that every time a new dependent pops along
     * we recreate the MemoryList in the CombinationExtent. For performance
     * reasons may want to change this behaviour in the future. As you add a
     * list you would remove from displayList anything that was not in this
     * new list. Too simple so implemented!
     */
    private void addList(DependentExtent dependentExtent, Tie tie)
    {
        pr("## (addList) ML ID: " + id);
        if(tie == null)
        {
            Err.error("addList being called with a null Tie");
        }
        // Err.pr( "### MemoryList.addList being called with tie " + tie);
        mapOfLists.put(tie, dependentExtent);
        if(displayList.isEmpty())
        {
            /*
            if(!dependentExtent.isEmpty())
            {
            Err.error( "Previously suspected that addList always given empty dependentExtent");
            }
            */
            for(Iterator iter = dependentExtent.iterator(); iter.hasNext();)
            {
                Object element = iter.next();
                pr(
                    "[[[   displayList is having " + element + " added from "
                        + dependentExtent.getClass());
                displayList.add(element);
            }
        }
        else
        {
            // Err.error( "This code being called");
            List removals = new ArrayList();
            for(Iterator iter = displayList.iterator(); iter.hasNext();)
            {
                Object element = iter.next();
                if(!dependentExtent.contains(element))
                {
                    removals.add(element);
                }
            }
            for(Iterator iter = removals.iterator(); iter.hasNext();)
            {
                Object element = iter.next();
                displayList.remove(element);
            }
        }
    }

    public void setList(DependentExtent dependentExtent, Tie tie)
    {
        /*
        times++;
        Err.pr( "### MemoryList.setList being called with " + dependentExtent.hashCode()
        + " times " + times);
        if(times == 4)
        {
        Err.debug();
        }
        */
        // replaceList( dependentExtent);
        displayList.clear();

        DependentExtent replacing = (DependentExtent) mapOfLists.get(tie);
        if(replacing == null)
        {
            addList(dependentExtent, tie);
            // Err.error( "Do not already have a DependentExtent in MemoryExtent keyed by " + tie);
        }
        else if(replacing != dependentExtent)
        {
            Err.error("replacing != dependentExtent");
        }

        HashMap copyListOfLists = new HashMap(mapOfLists);
        mapOfLists.clear();
        for(Iterator iter = copyListOfLists.values().iterator(); iter.hasNext();)
        {
            DependentExtent list = (DependentExtent) iter.next();
            if(list != replacing)
            {
                addList(list, tie);
            }
        }
        addList(dependentExtent, tie);
        pr("### MemoryList.setList DONE");
    }

    private static int[] zeroToMaxExceptParam(int max, int param)
    {
        int[] result = new int[max - 1];
        int counter = 0;
        for(int i = 0; i < result.length; i++)
        {
            if(i != param)
            {
                result[counter++] = i;
            }
        }
        return result;
    }

    public Object get(int index)
    {
        return displayList.get(index);
    }

    public Object set(int index, Object value)
    {
        return displayList.set(index, value);
    }

    public int indexOf(Object elem)
    {
        return displayList.indexOf(elem);
    }

    public boolean isEmpty()
    {
        return displayList.isEmpty();
    }

    public int size()
    {
        return displayList.size();
    }

    public void insert(Object obj, int index)
    {
        Err.pr(SdzNote.BG_ADD, "## (insert) ML ID: " + id);
        displayList.add(index, obj);
        for(Iterator iter = mapOfLists.values().iterator(); iter.hasNext();)
        {
            DependentExtent list = (DependentExtent) iter.next();
            Err.pr(SdzNote.BG_ADD, "--------------MemoryList.insert()ing " + obj);
            NavExtent.relativePositionInsert(obj, displayList, list);
        }
    }

    public Iterator iterator()
    {
        return displayList.iterator();
    }
    
    public List getList()
    {
        return displayList;
    }

    public void removeElementAt(int index)
    {
        if(index == -1)
        {
            Err.error("Can't remove from MemoryList at -1");
        }
        pr("## (remove) ML ID: " + id);
        // pr( "Have " + mapOfLists.size() + " listOfLists");
        for(Iterator iter = mapOfLists.values().iterator(); iter.hasNext();)
        {
            DependentExtent list = (DependentExtent) iter.next();
            Object obj = displayList.get(index);
            if(obj == null)
            {
                Err.error("Could not find a displayList element at " + index);
            }
            list.remove(obj);
        }
        pr("--------------MemoryList.rm()ing at " + index);
        displayList.remove(index);
    }
    
    /*
    private void replaceList( DependentExtent newList)
    {
    displayList = pUtils.findCommonElements( listOfLists);
    *
    times++;
    Err.pr( "[[[   displayList set to " + displayList);
    if(times == 0)
    {
    Err.stack();
    }
    *
    }
    */

    /**
     * A DependentList that is held in listOfLists has changed its
     * composition.
     */
    /*
    private void replaceList_( DependentExtent newList)
    {
    Err.error( "Not called anymore right?");
    displayList.clear();
    //boolean sameFound = false;
    int i=0;
    for (Iterator iter = listOfLists.iterator(); iter.hasNext(); i++)
    {
    DependentExtent dependentExtent = (DependentExtent)iter.next();
    //Err.pr( "Looking at dependentExtent: " + dependentExtent);
    *
    * This if stuff (using newList param) would be great if we could
    * first of all remove all the newList elements that are in displayList.
    * Problem is we have no way of knowing what these elements are.
    *
    //if(dependentExtent == newList)
    //{
    //sameFound = true;
    int[] others = zeroToMaxExceptParam( listOfLists.size(), i);
    for (Iterator iterator = dependentExtent.iterator(); iterator.hasNext();)
    {
    Object element = iterator.next();
    if(!displayList.contains( element))
    {
    boolean notInOneOfOtherLists = false;
    for (int j = 0; j < others.length; j++)
    {
    //Err.pr( "Considering whether " + element + " is in " +
    //  (DependentExtent)listOfLists.get(j) + " list " + j);
    if(!((DependentExtent)listOfLists.get(j)).contains( element))
    {
    notInOneOfOtherLists = true;
    break;
    }
    }
    if(!notInOneOfOtherLists)
    {
    *
    * For a dependentExtent we have gone thru all of the other lists
    * and determined that all of its elements exist in them. We thus
    * have an 'intersection element'. Of course we must make sure it hasn't
    * already been added, which we have already done above.
    *
    displayList.add( element);
    }
    }
    }
    //break;
    //}
    }
    //if(!sameFound)
    //{
    //	Err.error( "MemoryList.replaceList did not find a target");
    //}
    }
    */
}
