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

import org.strandz.lgpl.util.DebugList;
import org.strandz.lgpl.util.Err;

import java.util.Iterator;
import java.util.List;

/**
 * The list that is being displayed may be different to the list
 * that needs to be updated. This class will allow one operation to
 * affect the delegate list. Note than an equivalent does not exist for
 * ArrayExtent or SetExtent. (This is for ListExtent).
 * <p/>
 * Delegates for display, period! A better word
 * than display might be 'actual list', because that is what it is. Note
 * that when using a SdzEntityManagerI that the updating of a the real
 * list is done through it's methods - so debug there not here.
 */
public class ActualList
{
    public List display;
    private InsteadOfAddRemoveTrigger insteadOfTrigger;
    private static final boolean DEBUG = false;

    ActualList(List display, InsteadOfAddRemoveTrigger insteadOfTrigger)
    {
        /*
        * In ListExtent.setList() this happens but is quickly
        * remedied
        if(display == null)
        {
        Err.error( "Cannot create an ActualList with a null DisplayList");
        }
        */
        this.display = display;
        this.insteadOfTrigger = insteadOfTrigger;
        if(display instanceof DebugList)
        {
            Err.pr("ActualList created with " + display);
        }
//        String className = display.getClass().getName();
//        if("java.util.Collections$UnmodifiableRandomAccessList".equals( className))
//        {
//            Err.stack( "Getting a " + className);
//        }
    }

    List display()
    {
        return display;
    }

    boolean hasNullDisplay()
    {
        return (display == null);
    }

    int indexOf(Object elem)
    {
        return display.indexOf(elem);
    }

    boolean remove(Object obj)
    {
        boolean result = false;
        if(insteadOfTrigger == null)
        {
            try
            {
                result = display.remove(obj);
            }
            catch(UnsupportedOperationException ex)
            {
                Err.error( ex, "A <" + obj.getClass().getName() + "> exists in an unmodifiable collection, from which it cannot be deleted - put an InsteadOfAddRemove trigger on the cell");
            }
        }
        else
        {
            result = insteadOfTrigger.remove( display, obj);
        }
        return result;
    }
    
    boolean add(int idx, Object obj)
    {
        if(insteadOfTrigger == null)
        {
            try
            {
                pr( "add at " + idx);    
                display.add(idx, obj);
            }
            catch(UnsupportedOperationException ex)
            {
                Err.error( ex, "A <" + obj.getClass().getName() + "> exists in an unmodifiable collection, which cannot be added to - put an InsteadOfAddRemove trigger on the cell");
            }
        }
        else
        {
            pr( "insteadOf add at " + idx);
            //Err.stack();
            insteadOfTrigger.add( display, obj, idx);
        }
        return display.contains(obj);
    }

    boolean add(Object obj)
    {
        boolean result = false;
        if(insteadOfTrigger == null)
        {
            try
            {
                pr( "add at current");    
                result = display.add(obj);
            }
            catch(UnsupportedOperationException ex)
            {
                Err.error( ex, "A <" + obj.getClass().getName() + "> exists in an unmodifiable collection, which cannot be added to - put an InsteadOfAddRemove trigger on the cell");
            }
        }
        else
        {
            pr( "insteadOf add at current");    
            insteadOfTrigger.add( display, obj, display.size()-1);
        }
        return result;
    }

    Object get(int index)
    {
        return display.get(index);
    }

    Object set(int index, Object value)
    {
        pr( "set at " + index);    
        return display.set(index, value);
    }
    
    private static void pr( String txt)
    {
        if(DEBUG)
        {
            Err.pr( txt);
        }
    }

    int size()
    {
        return display.size();
    }

    boolean isEmpty()
    {
        return display.isEmpty();
    }

    Iterator iterator()
    {
        Iterator result;
        if(display == null)
        {
            result = new EmptyIterator(); 
        }
        else
        {
            result = display.iterator();
        }
        return result;
    }
    
    class EmptyIterator implements Iterator
    {
        public boolean hasNext()
        {
            return false;
        }

        public Object next()
        {
            return null;
        }

        public void remove()
        {
            Err.error("remove()" + " not implemented");
        }
    }

    boolean contains(Object elem)
    {
        return display.contains(elem);
    }
}
