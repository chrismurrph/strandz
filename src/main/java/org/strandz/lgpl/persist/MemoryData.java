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
package org.strandz.lgpl.persist;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Print;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryData
{
    private Map map = new HashMap();

    public MemoryData(Class classes[], List lists[])
    {
        boolean ok = classes.length == lists.length;
        if(!ok)
        {
            Print.prArray( classes, "classes");
            Print.prArray( lists, "lists");
        }
        Assert.isTrue( ok, "classes and lists need to be the same length, see above");
        for(int i = 0; i < classes.length; i++)
        {
            Class clazz = classes[i]; 
            List list = lists[i];
            //Uncomment if get problem below
            //Print.prList( list, "The Collection, that must contains " + clazz);
            Utils.chkTypes( list, clazz, "Lists in MemoryData must conform, in order, to the types specified by the DataStore");
            map.put(clazz, list);
        }
    }

    public List getList(Class clazz)
    {
        List result = (List) map.get(clazz);
        if(result == null)
        {
            Err.error( "Expected to find a list for the type " + clazz.getName());
        }
        return result;
    }

    public void setList(Class clazz, List list)
    {
        map.put(clazz, list);
    }
}
