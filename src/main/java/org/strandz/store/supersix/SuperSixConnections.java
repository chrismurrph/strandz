/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.store.supersix;

import org.strandz.lgpl.store.Connections;
import org.strandz.lgpl.store.MemoryConnectionInfo;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.JDOByFileConnectionInfo;
import org.strandz.lgpl.store.ConnectionEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.data.supersix.domain.SuperSixConnectionEnum;

/**
 *
 */
public class SuperSixConnections extends Connections
{
    public static final SuperSixConnectionEnum DEFAULT_DATABASE = SuperSixConnectionEnum.SUPERSIX_DEMO_MEMORY;
    
    abstract public static class SuperSixJDOByFileConnectionInfo extends JDOByFileConnectionInfo
    {
        public SuperSixJDOByFileConnectionInfo(String propsFileName, ConnectionEnum connectionEnum, int estimatedConnectDuration, int loadLookupDataDuration)
        {
            super(propsFileName, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
        }

        protected SuperSixJDOByFileConnectionInfo(String propsFileNames[], ConnectionEnum connectionEnum, int estimatedConnectDuration, int loadLookupDataDuration)
        {
            super(propsFileNames, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
        }

        public String getSpringBeansFileName()
        {
            Err.error("getSpringBeansFileName()" + " not implemented");
            return null;
        }
    }

    public SuperSixConnections()
    {
        initConnection(SuperSixConnectionEnum.SUPERSIX_DEMO_MEMORY,
                       new MemoryConnectionInfo(
                           SuperSixConnectionEnum.SUPERSIX_DEMO_MEMORY, 672, 688)
                       {
                           public DataStore createDataStore()
                           {
                               return new MemorySuperSixDataStore(this);
                           }
                       });
        String files[] = new String[]{
                "C:\\sdz-zone\\property-files\\supersix\\supersix_kodo_postgres.properties",
                "/usr/local/sdz-zone/property-files/supersix/supersix_kodo_postgres.properties"
        };
        initConnection(SuperSixConnectionEnum.SUPERSIX_KODO,
                       new SuperSixJDOByFileConnectionInfo( files,
                                                    SuperSixConnectionEnum.SUPERSIX_KODO,
                                                    3000, 50)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOSuperSixDataStore(this);
                           }
                       });
        files = new String[]{
                "C:\\sdz-zone\\property-files\\supersix\\supersix_kodo_postgres_service.properties",
                "/usr/local/sdz-zone/property-files/supersix/supersix_kodo_postgres_service.properties"
        };
        initConnection(SuperSixConnectionEnum.SUPERSIX_KODO_SERVICE,
                       new SuperSixJDOByFileConnectionInfo( files,
                                                    SuperSixConnectionEnum.SUPERSIX_KODO_SERVICE,
                                                    3000, 50)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOSuperSixDataStore(this);
                           }
                       });
        /**
         * Used by a client when it is a remote proxy to a server-side persistence manager.
         * The word Demo means it accesses the Tomcat Server on the Desktop machine.  
         */
        initConnection(SuperSixConnectionEnum.REMOTE_DEMO_SUPERSIX_KODO,
                       new SuperSixJDOByFileConnectionInfo(
                           "C:\\sdz-zone\\property-files\\supersix\\remote_demo_supersix_kodo_postgres.properties",
                           SuperSixConnectionEnum.REMOTE_DEMO_SUPERSIX_KODO,
                           DEFAULT_CONNECT_DURATION, 100)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOSecureSuperSixDataStore(this);
                           }
                       });
        /**
         * Used by a client when it is a remote proxy to a server-side persistence manager.
         * This is the one used on the real live server, which will become production.
         */
        initConnection(SuperSixConnectionEnum.REMOTE_SUPERSIX_KODO,
                       new SuperSixJDOByFileConnectionInfo(
                           "property-files/remote_supersix_kodo_postgres.properties",
                           SuperSixConnectionEnum.REMOTE_SUPERSIX_KODO,
                           DEFAULT_CONNECT_DURATION, 16500)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOSecureSuperSixDataStore(this);
                           }
                       });
    }
}
