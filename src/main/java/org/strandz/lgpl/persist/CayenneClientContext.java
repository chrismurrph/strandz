package org.strandz.lgpl.persist;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.PersistentObject;
import org.apache.cayenne.DataObject;
import org.strandz.lgpl.util.Err;

/**
 * User: Chris
 * Date: 24/09/2008
 * Time: 16:55:31
 */
public class CayenneClientContext extends CayenneObjectContext
{
    public CayenneClientContext(ObjectContext objectContext)
    {
        super( objectContext);
    }

    public ORMTypeEnum getORMType()
    {
        return ORMTypeEnum.CAYENNE_CLIENT;
    }

    public void deletePersistent(Object obj)
    {
        objectContext.deleteObject( obj);
    }

    public void registerPersistent(Object obj)
    {
        Err.error( "Cannot register an object on the client");
    }

    public void registerTransient(Object obj)
    {
        Err.error( "Cannot unregister an object on client");
    }

    public boolean isEntityManaged(Object obj)
    {
        boolean result;
        if(obj instanceof PersistentObject)
        {
           //Here a server DO has appeared on the client 
           result = ((PersistentObject)obj).getObjectContext() != null;
        }
        else
        {
            result = ((DataObject)obj).getObjectContext() != null;
        }
        return result;
    }
}