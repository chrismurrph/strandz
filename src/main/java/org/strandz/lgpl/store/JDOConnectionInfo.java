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
package org.strandz.lgpl.store;

import org.strandz.lgpl.persist.ORMTypeEnum;

import java.util.Properties;

abstract public class JDOConnectionInfo extends SpringSecureConnectionInfo
{
    String propsFileNames[] = null;
    private String id;
    private int estimatedConnectDuration;
    private int loadLookupDataDuration;
    
    public JDOConnectionInfo(
        String propsFileName,
        ConnectionEnum connectionEnum,
        int estimatedConnectDuration,
        int loadLookupDataDuration)
    {
        this( new String[]{ propsFileName}, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
    }

    public JDOConnectionInfo(
        String propsFileName[],
        ConnectionEnum connectionEnum,
        int estimatedConnectDuration,
        int loadLookupDataDuration)
    {
        super( connectionEnum);
        this.propsFileNames = propsFileName;
        this.id = id;
        this.estimatedConnectDuration = estimatedConnectDuration;
        this.loadLookupDataDuration = loadLookupDataDuration;
    }

    public String toString()
    {
        return id + " using " + propsFileNames;
    }

    abstract public Properties getProperties();

    public int getEstimatedConnectDuration()
    {
        return estimatedConnectDuration;
    }

    public int getEstimatedLoadLookupDataDuration()
    {
        return loadLookupDataDuration;
    }

    public ORMTypeEnum getVersion()
    {
        return ORMTypeEnum.JDO;
    }
}
