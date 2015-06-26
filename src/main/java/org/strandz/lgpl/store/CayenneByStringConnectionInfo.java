package org.strandz.lgpl.store;

import org.strandz.lgpl.persist.ORMTypeEnum;

/**
 * User: Chris
 * Date: 29/07/2008
 * Time: 15:22:02
 */
abstract public class CayenneByStringConnectionInfo extends CayenneSecureConnectionInfo
{
    public CayenneByStringConnectionInfo( ConnectionEnum connectionEnum)
    {
        super( connectionEnum);
    }

    public CayenneByStringConnectionInfo(String configString,
                                         ConnectionEnum connectionEnum,
                                         int estimatedConnectDuration,
                                         int loadLookupDataDuration,
                                         ORMTypeEnum ormTypeEnum)
    {
        super(configString, connectionEnum, estimatedConnectDuration, loadLookupDataDuration, ormTypeEnum);
    }
}
