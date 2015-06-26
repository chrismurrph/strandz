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

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import java.util.Collection;
import java.util.List;

public class NullEntityManager implements SdzEntityManagerI
{
    NullEntityManager()
    {
        //Err.stack( "NullEntityManager creation");
    }

    public Object getActualEM()
    {
        Err.error("getActualPM() should never need to be called for a NullEntityManager");
        return null;
    }
    
    public Object getActualEMF()
    {
        Err.error( "[19]Not implemented");
        return null;
    }

    public ORMTypeEnum getORMType()
    {
        return ORMTypeEnum.NULL;
    }

    public void close()
    {
    }

    public void registerPersistentAll(Collection c)
    {
    }

    public void deletePersistent(Object obj)
    {
    }
    
    public void deletePersistentAll(Collection c)
    {
    }

    public Object newPersistent(Class clazz)
    {
        return null;
    }

    public void registerPersistent(Object obj)
    {
        Err.pr(SdzNote.BG_ADD, "Adding with a NullEntityManager (so won't add to anything!!): " + obj.getClass().getName());
    }

    public void registerTransient(Object obj)
    {
    }

    public Collection detachCopyAll(Collection c)
    {
        Collection result = null;
        return result;
    }
    
    public void flush()
    {
    }

    /**
     * See TestClientsChildFocus.testChildFocus() for an example of where don't have an EntityManager,
     * yet things work 
     */
    public boolean isEntityManaged( Object obj)
    {
        //Err.error( "S/not be calling isEntityManaged() on " + this.getClass().getName());
        return false;
    }
    
    public boolean isDirty(Object obj)
    {
        //Err.error( "S/not be calling isDirty() on " + this.getClass().getName());
        return false;
    }
}
