package org.strandz.lgpl.persist;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.access.DataContext;
import org.strandz.lgpl.util.Utils;

/**
 * User: Chris
 * Date: 24/09/2008
 * Time: 16:55:31
 */
public class CayenneServerContext extends CayenneObjectContext
{
    public CayenneServerContext(ObjectContext objectContext)
    {
        super( objectContext);
    }

    public ORMTypeEnum getORMType()
    {
        return ORMTypeEnum.CAYENNE_SERVER;
    }

    public void deletePersistent(Object obj)
    {
        objectContext.deleteObject( obj);
    }

    public void registerPersistent(Object obj)
    {
        objectContext.registerNewObject( obj);
    }

    public void registerTransient(Object obj)
    {
        ((DataContext)objectContext).unregisterObjects( Utils.formList( obj));
    }

    public boolean isEntityManaged(Object obj)
    {
        boolean result = ((DataObject)obj).getObjectContext() != null;
        return result;
    }
}
