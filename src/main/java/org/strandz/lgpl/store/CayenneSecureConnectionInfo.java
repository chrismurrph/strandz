package org.strandz.lgpl.store;

import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.util.Err;

import java.util.Properties;

/**
 * User: Chris
 * Date: 29/07/2008
 * Time: 13:53:41
 */
abstract public class CayenneSecureConnectionInfo extends SpringSecureConnectionInfo
{
    private int estimatedConnectDuration;
    private int loadLookupDataDuration;
    private String configStringName;
    private ORMTypeEnum ormTypeEnum;

    public CayenneSecureConnectionInfo(ConnectionEnum connectionEnum)
    {
        super(connectionEnum);
    }

    public CayenneSecureConnectionInfo(
        String configString,
        ConnectionEnum connectionEnum,
        int estimatedConnectDuration,
        int loadLookupDataDuration,
        ORMTypeEnum ormTypeEnum
        )
    {
        super(connectionEnum);
        this.configStringName = configString;
        this.estimatedConnectDuration = estimatedConnectDuration;
        this.loadLookupDataDuration = loadLookupDataDuration;
        if(!(ormTypeEnum.isCayenne()))
        {
            Err.error();
        }
        this.ormTypeEnum = ormTypeEnum;
    }

    public String toString()
    {
        return getName();
    }

    public Properties getProperties()
    {
        //Err.error( "Not needed for " + ORMTypeEnum.CAYENNE.getName());
        //return new Properties();
        return null;
    }

    public String getConfigStringName()
    {
        return configStringName;
    }

    public ORMTypeEnum getVersion()
    {
        return ormTypeEnum;
    }

    public int getEstimatedConnectDuration()
    {
        return estimatedConnectDuration;
    }

    public int getEstimatedLoadLookupDataDuration()
    {
        return loadLookupDataDuration;
    }

    public ORMTypeEnum getORMType()
    {
        return ormTypeEnum;
    }
}
