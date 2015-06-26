package org.strandz.lgpl.store;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.persist.ORMTypeEnum;

import java.util.Properties;

/**
 * User: Chris
 * Date: 25/09/2008
 * Time: 11:42:04
 */
public class RemoteCayenneConnectionInfo extends EntityManagedConnectionInfo
{
    public RemoteCayenneConnectionInfo( String id, String description)
    {
        super( id, description);
    }

    public Properties getProperties()
    {
        Err.error("Not implemented");
        return null;
    }

    public int getEstimatedConnectDuration()
    {
        Err.error("Not implemented");
        return 0;
    }

    public int getEstimatedLoadLookupDataDuration()
    {
        Err.error("Not implemented");
        return 0;
    }

    public DataStore createDataStore()
    {
        Err.error("Not implemented");
        return null;
    }

    public ORMTypeEnum getVersion()
    {
        Err.error("Not implemented");
        return null;
    }
}
