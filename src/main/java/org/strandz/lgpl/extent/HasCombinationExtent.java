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

import org.strandz.lgpl.util.NamedI;

/**
 * Should only be used where the initialData returned is a
 * CombinationExtent. In Seaweed app can't statically enforce
 * this as a Block's initialData has different meanings - SHOULD
 * CREATE A NEW METHOD IN BLOCK, SO CAN TIGHTEN THIS UP, AND GET
 * RID OF DYNAMIC CAST IN TIE.JAVA
 */
public interface HasCombinationExtent extends NamedI
{
    CombinationExtent getCombinationExtent();
    void setCombinationExtent( CombinationExtent combinationExtent);
    NodeGroup getNodeGroup();
}
