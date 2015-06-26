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

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.Collection;

public class JDOEntityManager implements SdzEntityManagerI
{
    PersistenceManagerFactory pmf;
    PersistenceManager pm;

    JDOEntityManager(Object pmf)
    {
        this.pmf = (PersistenceManagerFactory)pmf;
        this.pm = this.pmf.getPersistenceManager(); 
    }

    public Object getActualEM()
    {
        return pm;
    }

    public Object getActualEMF()
    {
        return pmf;
    }
    
    public ORMTypeEnum getORMType()
    {
        return ORMTypeEnum.JDO;
    }

    public void close()
    {
        pm.close();
    }

    public void registerPersistentAll(Collection c)
    {
        //Rebuild this file - and the error will go away!
        //Err.pr( "About to error and will be using pm " + pm);
        pm.makePersistentAll(c);
    }

    public void deletePersistent(Object obj)
    {
        pm.deletePersistent(obj);
    }

    public void deletePersistentAll(Collection c)
    {
        pm.deletePersistentAll(c);
    }

    public Object newPersistent(Class clazz)
    {
        Object result = pm.newInstance( clazz);
        return result;
    }

    public void registerPersistent(Object obj)
    {
        pm.makePersistent(obj);
    }

    public void registerTransient(Object obj)
    {
        pm.makeTransient(obj);
    }

    public boolean isEntityManaged(Object obj)
    {
        return (JDOHelper.getPersistenceManager( obj) != null);
    }
    
    public boolean isDirty(Object obj)
    {
        return JDOHelper.isDirty( obj);
    }

//These two must be JDO2.0    
//    public Collection detachCopyAll(Collection c)
//    {
//        return pm.detachCopyAll( c);
//    }
//    
//    public void flush()
//    {
//        Err.error("flush()" + " not implemented - let's test it first");
//        pm.flush();
//    }

}
