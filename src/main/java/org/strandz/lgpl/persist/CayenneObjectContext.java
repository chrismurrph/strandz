package org.strandz.lgpl.persist;

import org.strandz.lgpl.util.Err;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.PersistentObject;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: Chris
 * Date: 1/08/2008
 * Time: 18:22:10
 */
abstract public class CayenneObjectContext implements SdzEntityManagerI
{
    ObjectContext objectContext;

    public CayenneObjectContext( ObjectContext objectContext)
    {
        this.objectContext = objectContext;
    }

    public Object getActualEM()
    {
        return objectContext;
    }

    public Object getActualEMF()
    {
        Err.error( "Not coded for yet");
        return null;
    }

    abstract public ORMTypeEnum getORMType();

    public void close()
    {
        Err.error( "Not coded for yet");
    }

    public void registerPersistentAll(Collection c)
    {
        for (Iterator iterator = c.iterator(); iterator.hasNext();)
        {
            Object o = iterator.next();
            registerPersistent( o);
        }
    }

    abstract public void deletePersistent(Object obj);

    public void deletePersistentAll(Collection c)
    {
        for (Iterator iterator = c.iterator(); iterator.hasNext();)
        {
            Object o = iterator.next();
            deletePersistent( o);
        }
    }

    abstract public void registerPersistent(Object obj);

    abstract public void registerTransient(Object obj);

    abstract public boolean isEntityManaged(Object obj);

    public boolean isDirty(Object obj)
    {
        Err.error( "Not coded for yet");
        return false;
    }

    public Object newPersistent(Class clazz)
    {
        //Err.pr( "To instantiate a " + clazz);
        Object result = objectContext.newObject( clazz);
        return result;
    }
}
