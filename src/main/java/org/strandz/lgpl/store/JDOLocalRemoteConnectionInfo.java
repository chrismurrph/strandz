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
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.IORedirectorUtils;
import org.strandz.lgpl.util.PropertyUtils;

import java.util.Date;
import java.util.Properties;

abstract public class JDOLocalRemoteConnectionInfo extends SpringSecureConnectionInfo
{
    private String localPropsFileName;
    private String remotePropsFileName;
    private String stdErrFileName;
    private String stdOutFileName;
    String id;
    private String serverName;
    private int estimatedConnectDuration;
    private int loadLookupDataDuration;

    public JDOLocalRemoteConnectionInfo(
        String localPropsFileName, String remotePropsFileName,
        String stdErrFileName, String stdOutFileName,
        ConnectionEnum connectionEnum,
        String serverName, int estimatedConnectDuration,
        int loadLookupDataDuration)
    {
        super(connectionEnum);
        this.localPropsFileName = localPropsFileName;
        this.remotePropsFileName = remotePropsFileName;
        this.stdErrFileName = stdErrFileName;
        this.stdOutFileName = stdOutFileName;
        this.id = id;
        this.serverName = serverName;
        this.estimatedConnectDuration = estimatedConnectDuration;
        this.loadLookupDataDuration = loadLookupDataDuration;
    }

    /**
     * Intend to plop a jar file in the lib dir. Thus where (where being a container concept)
     * the code is executing determines whether it is a client or a server.
     *
     * @return connection properties
     */
    public Properties getProperties()
    {
        Properties result = null;
        result = PropertyUtils.getPortableProperties(localPropsFileName, this, true);
        if(result == null)
        {
            Err.pr("Was not able to find <" + localPropsFileName + "> from available jars, so it looks " +
                "as if this connection is remote");
            result = PropertyUtils.getPortableProperties(remotePropsFileName, this);
            Err.pr("REMOTE " + id + " at " + new Date());
        }
        else
        {
            //A local connection is used by the JDO Service
            IORedirectorUtils.outputToStdOut(stdOutFileName);
            IORedirectorUtils.outputToStdErr(stdErrFileName);
            locatedOnServerNotification();
        }
        Print.prMap(result);
        return result;
    }
    
    abstract void locatedOnServerNotification();
    
    public String toString()
    {
        return id + " using REMOTE: " + remotePropsFileName + ", LOCAL: " + localPropsFileName;
    }

    public String getServerName()
    {
        return serverName;
    }

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
