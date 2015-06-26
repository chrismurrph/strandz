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
import org.strandz.lgpl.util.ObjectFoundryUtils;

import java.util.Collection;
import java.util.List;

/**
 * We are dealing with these lists the JDO way. With JDO you have to pop your
 * queried list into another before you can modify it. Thus our original list doesn't
 * get updated in this memory version either. Thus here we make sure that the
 * original data is updated as well. POSSIBILITY Bring the creating of the extra list
 * under control. If we did we would have the option of returning the same list
 * back again for the in-memory situation, and we wouldn't have to simulate, and
 * thus could ignore all the calls to in here. (Just an alternative).
 */
public class MemoryEntityManager implements SdzEntityManagerI
{
    MemoryData data;

    MemoryEntityManager(MemoryData data)
    {
        this.data = data;
    }

    public Object getActualEM()
    {
        return data;
    }
    
    public Object getActualEMF()
    {
        Err.error( "[18]Not implemented");
        return null;
    }

    public ORMTypeEnum getORMType()
    {
        return ORMTypeEnum.MEMORY;
    }

    public void close()
    {
    }

    public void registerPersistentAll(Collection c)
    {
        Err.error(SdzNote.CANT_ADD_RS, "FIX ME! Doing registerPersistentAll() for " + c);
    }

    public void deletePersistent(Object obj)
    {
        List list = data.getList(obj.getClass());
        list.remove(obj);
    }
    
    public void deletePersistentAll(Collection c)
    {
        Err.error(SdzNote.CANT_ADD_RS, "FIX ME! Doing deletePersistentAll() for " + c);
    }

    public Object newPersistent( Class clazz)
    {
        Object result = ObjectFoundryUtils.factory( clazz);
        registerPersistent( result);
        return result;
    }

    public void registerPersistent(Object obj)
    {
        List list = data.getList(obj.getClass());
        Err.pr(SdzNote.BG_ADD, "Adding with a MemoryEntityManager to a " + list.getClass().getName());
        list.add(obj);
    }

    public void registerTransient(Object obj)
    {
        List list = data.getList(obj.getClass());
        list.remove(obj);
        Err.pr( "Have not tested this");
    }

    public Collection detachCopyAll(Collection c)
    {
        Collection result = null;
        Err.error( "detachCopyAll(Collection c) not implemented for " + this.getClass().getName());
        return result;
    }

    public void flush()
    {
        Err.error("flush()" + " not implemented - let's test it first");
    }
    
    public boolean isEntityManaged( Object obj)
    {
        boolean result;
        if(data == null)
        {
            result = false;
        }
        else
        {
            List list = data.getList( obj.getClass());
            result = list.contains( obj);
        }
        return result;
    }
    
    public boolean isDirty(Object obj)
    {
        Err.error("isDirty()" + " not implemented");
        return false;
    }
}
