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
 * This class allows two types of <code>CapabilityEnum</code> state to be
 * stored. blockAllowed is what the user sees and may be changed frequently
 * according to established rules. (For example when you are on the last row
 * you cannot go forward any more). nodeAllowed is the design-time state,
 * and when no special rules are operating, is the same as blockAllowed.
 * It is important to note that everything that can be changed at DT can
 * also be changed at RT. Thus nodeAllowed may be changed at RT.
 *
 * @author Chris Murphy
 */
abstract public class CapabilityAction
{
    private boolean blockAllowed;
    private boolean nodeAllowed;

    public boolean isBlockAllowed()
    {
        return blockAllowed;
    }

    public void setBlockAllowed(boolean blockAllowed)
    {
        this.blockAllowed = blockAllowed;
    }

    public boolean isNodeAllowed()
    {
        return nodeAllowed;
    }

    public void setNodeAllowed(boolean nodeAllowed)
    {
        this.nodeAllowed = nodeAllowed;
    }

    abstract public boolean getDefaultAllowed();
}
