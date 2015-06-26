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
 * This class is the same as an ArrayList. The only addition to functionality
 * is that it does some extra checking when <code>addAll</code> is called
 *
 * @author Chris Murphy
 */
public class CautiousArrayList extends ArrayList
{
    public boolean addAll(Collection v)
    {
        boolean result = false;
        if(v == null)
        {
            Err.error("addAll cannot be passed null");
        }
        else if(v == this)
        {
            Err.error("Can't add an ArrayList onto itself - " + v);
        }
        else
        {
            result = super.addAll(v);
        }
        /*
        for(Iterator e = v.iterator(); e.hasNext();)
        {
          // new MessageDlg("Looping round addAll", MessageDlg.KILL);
          add( e.next());
        }
        */
        return result;
    }

    /*
    public ArrayList asVector()
    {
      return this;
    }
    */
}
