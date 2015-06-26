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

import java.util.Iterator;

/**
 * A DependentExtent will completely change its list in response to
 * the parent side of a Tie changing the index of its list. A
 * DependentExtent thus has setList() called on itself quite often.
 * It is used for master/detail navigation to be kept in sync.
 * The user never sees these lists, as a block will always contain
 * a CombinationExtent. A CombinationExtent is made up of many
 * DependentExtents, hence the reference in here to the
 * CombinationExtent. ce is informed when a navigation on the master
 * changes the whole list completely. Anything else that is done, such
 * as inserts or updates, it is ce that informs its group of
 * DependentExtents.
 */
abstract public class DependentExtent extends NavExtent
    //implements IterableList
{
    HasCombinationExtent hasCombinationExtent;

    abstract public boolean contains(Object elem);

    abstract public boolean remove(Object o);

    abstract public Iterator iterator();
}
