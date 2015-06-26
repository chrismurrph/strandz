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
package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.note.SdzNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ColRowListSet
{
    private List<ComponentControlWrapper> list;
    private Map<ColRow,ComponentControlWrapper> map;

    ColRowListSet()
    {
        list = new ArrayList<ComponentControlWrapper>();
        map = new HashMap<ColRow,ComponentControlWrapper>();
    }

    ColRowListSet(List<ComponentControlWrapper> objects)
    {
        list = objects;
        map = new HashMap<ColRow,ComponentControlWrapper>();
        for(int i = 0; i < objects.size(); i++)
        {
            ComponentControlWrapper o = objects.get(i);
            map.put( new ColRow(o.getCol(),o.getRow()), o);
        }
    }

    void clear()
    {
        list.clear();
        map.clear();
        Err.pr(SdzNote.REFRESH, "Cleared list and map");
    }
    
    List<ComponentControlWrapper> getList()
    {
        return list;
    }

    List getListOfControls()
    {
        List result = new ArrayList();
        for(Iterator<ComponentControlWrapper> iterator = list.iterator(); iterator.hasNext();)
        {
            ComponentControlWrapper componentControlWrapper = iterator.next();
            result.add( componentControlWrapper.getComp());
        }
        return result;
    }
    
    void add( ComponentControlWrapper controlWrapper)
    {
        /*
        if(list.contains( controlWrapper))
        {
            Err.stack( "Need start a new list before add " + controlWrapper + " to list of size " + list.size());
        }
        */
        list.add( controlWrapper);
        map.put( new ColRow(controlWrapper.getCol(),controlWrapper.getRow()), controlWrapper);
    }

    void remove( ComponentControlWrapper controlWrapper)
    {
        list.remove( controlWrapper);
        map.remove( new ColRow(controlWrapper.getCol(),controlWrapper.getRow()));
    }
    
    ComponentControlWrapper findWrapperByControl( Object control)
    {
        ComponentControlWrapper result = null;
        List wrappers = list;
        for(Iterator iterator = wrappers.iterator(); iterator.hasNext();)
        {
            ComponentControlWrapper controlWrapper = (ComponentControlWrapper) iterator.next();
            if(control == controlWrapper.getComp())
            {
                result = controlWrapper;
                break;
            }
        }
        return result;
    }

    /**
     * Necessary when the control is being changed from beneath you... 
     */
    ComponentControlWrapper findWrapperByName( String controlName)
    {
        ComponentControlWrapper result = null;
        List wrappers = list;
        for(Iterator iterator = wrappers.iterator(); iterator.hasNext();)
        {
            ComponentControlWrapper controlWrapper = (ComponentControlWrapper) iterator.next();
            String name = ComponentUtils._getName( controlWrapper.getComp());
            if(controlName.equals( name))
            {
                result = controlWrapper;
                break;
            }
        }
        return result;
    }

    ComponentControlWrapper findWrapperByPosition( int col, int row)
    {
        Assert.isFalse( col == Utils.UNSET_INT || row == Utils.UNSET_INT);
        return findWrapperByPosition( new ColRow( col, row));
    }

    ComponentControlWrapper findWrapperByPosition( int col)
    {
        ComponentControlWrapper result = null;
        Assert.isFalse( col == Utils.UNSET_INT);
        Set keys = map.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();)
        {
            ColRow colRow = (ColRow)iterator.next();
            if(colRow.col == col)
            {
                result = map.get( colRow);
                break;
            }
        }
        return result;
    }

    ComponentControlWrapper findWrapperByPosition( ColRow colRow)
    {
        Assert.isFalse( colRow.col == Utils.UNSET_INT || colRow.row == Utils.UNSET_INT);
        ComponentControlWrapper result = map.get( colRow);
        return result;
    }

    ComponentControlWrapper _findWrapperByPosition( int col, int row)
    {
        ComponentControlWrapper result = null;
        List wrappers = list;
        for(Iterator iterator = wrappers.iterator(); iterator.hasNext();)
        {
            ComponentControlWrapper controlWrapper = (ComponentControlWrapper) iterator.next();
            if(col == controlWrapper.getCol())
            {
                if(row == controlWrapper.getRow())
                {
                    result = controlWrapper;
                    break;
                }
            }
        }
        return result;
    }
}
