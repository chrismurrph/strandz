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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NamedI;
import org.strandz.lgpl.persist.EntityManagerProviderI;

public class OrphanTie extends Tie
{
    /**
     * Constructor for parentless Ties:
     */
    public OrphanTie(NamedI childObj)
    {
        this.childObj = childObj;
        topLevel = true;
    }

    public int getType()
    {
        Err.error("Not required for Orphan");
        return -1;
    }

    public Class getFieldType()
    {
        Err.error("Not required for Orphan");
        return new Object().getClass();
    }

    public Object getFieldValue(Object object)
    {
        Err.error("Not required for Orphan");
        return new Object();
    }

    public void setFieldValue(Object object, Object value)
    {
        Err.error("Not required for Orphan");
    }
}
