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

/**
 * Used when we need to pass a Boolean to a method and we want that
 * method to be able to change that value, and the caller to be able
 * to see that change. Possibly not a good idea to use it for other
 * purposes, as immutability is a virtue.
 *
 * @author Chris Murphy
 */
public class MutableBoolean
{
    private boolean val;


    public MutableBoolean(boolean val)
    {
        this.val = val;
    }

    public MutableBoolean()
    {
        this.val = false;
    }

    public void setValue(boolean val)
    {
        this.val = val;
    }

    public boolean getValue()
    {
        return val;
    }

    public String toString()
    {
        if(val)
        {
            return "TRUE";
        }
        else
        {
            return "FALSE";
        }
    }
}
