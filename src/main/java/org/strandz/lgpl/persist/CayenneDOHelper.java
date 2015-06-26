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

import org.strandz.lgpl.util.Utils;

abstract public class CayenneDOHelper implements DOHelperI
{
    public boolean equalsOrError
        (Object obj1,
         Object obj2)
    {
        return CayenneUtils.equals(obj1, obj2);
    }

    public boolean equalityTest
        (Object master,
         Object ref)
    {
        boolean result = cayenneEqualityTest(master, ref);
        return result;
    }

    private static boolean cayenneEqualityTest(
        Object master,
        Object ref)
    {
        boolean result = Utils.equals( master, ref);
        return result;
    }
}