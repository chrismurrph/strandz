package org.strandz.lgpl.store;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;

import java.util.Collection;

/**
 * User: Chris
 * Date: 24/09/2008
 * Time: 15:38:32
 */
abstract public class CayenneServerDataStore extends CayenneDataStore
{
    void deleteObjects( ObjectContext objectContext, Collection collection)
    {
        ((DataContext)objectContext).deleteObjects( collection);
    }

    void registerNewObject( ObjectContext objectContext, DataObject dataObject)
    {
        objectContext.registerNewObject( dataObject);
    }
}
