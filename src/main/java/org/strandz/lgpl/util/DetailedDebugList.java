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
package org.strandz.lgpl.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class allows instrumentation style debugging for a List.
 *
 * @author Chris Murphy
 */
public class DetailedDebugList extends ArrayList
{
    public static int constructedTimes;
    public int id;

    public DetailedDebugList(Collection c)
    {
        super(c);
        init();
    }

    private void init()
    {
        constructedTimes++;
        id = constructedTimes;
        Err.pr("####### Have created DebugList " + id);
        if(id == 1)
        {
            Err.stack();
        }
    }

    public DetailedDebugList()
    {
        init();
    }

    public void clear()
    {
        Err.pr("####### DEBUG clear on " + id);
        super.clear();
    }

    public boolean addAll(Collection c)
    {
        Err.pr("####### DEBUG addAll: <" + c + "> to " + id);
        return super.addAll(c);
    }

    public boolean remove(Object obj)
    {
        Err.pr("####### DEBUG rem: <" + obj + "> from " + id);
        return super.remove(obj);
    }

    public void add(int index, Object o)
    {
        Err.pr("####### DEBUG add[@1]: <" + o + "> to " + id);
        // Err.stack();
        super.add(index, o);
    }
    /*
    public boolean add( Object o)
    {
    boolean result = super.add( o);
    Err.pr( "$$$ DEBUG add[@2]: <" + o + "> to " + id);
    return result;
    }
    */
}
