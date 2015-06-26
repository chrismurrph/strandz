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
package org.strandz.store.wombatrescue;

import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.store.Connections;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.JDOByFileConnectionInfo;
import org.strandz.lgpl.store.JDOLocalRemoteHTTPConnectionInfo;
import org.strandz.lgpl.store.JDOLocalRemoteRMIConnectionInfo;
import org.strandz.lgpl.store.JDOPortableConnectionInfo;
import org.strandz.lgpl.store.MemoryConnectionInfo;
import org.strandz.lgpl.store.CayenneByStringConnectionInfo;
import org.strandz.lgpl.store.ConnectionEnum;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.EasyLoginAction;
import org.strandz.lgpl.store.SpringLoginAction;
import org.strandz.lgpl.view.LoginAction;
import org.strandz.lgpl.persist.ORMTypeEnum;

/**
 *
 */
public class WombatConnections extends Connections
{
    public static final WombatConnectionEnum DEFAULT_DATABASE = WombatConnectionEnum.SERVER_CAYENNE;
    
    abstract public static class WombatJDOByFileConnectionInfo extends JDOByFileConnectionInfo
    {
        public WombatJDOByFileConnectionInfo(String propsFileName, ConnectionEnum connectionEnum, int estimatedConnectDuration, int loadLookupDataDuration)
        {
            super(propsFileName, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
        }

        protected WombatJDOByFileConnectionInfo(String propsFileNames[], ConnectionEnum connectionEnum, int estimatedConnectDuration, int loadLookupDataDuration)
        {
            super(propsFileNames, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
        }

        public String getSpringBeansFileName()
        {
            return TeresaSpringConstant.CLIENT_CONTEXT_CONFIG_LOCATION;
        }

        public String getConfigStringName()
        {
            return null;
        }
    }
    
    abstract public static class WombatJDOPortableConnectionInfo extends JDOPortableConnectionInfo
    {
        public WombatJDOPortableConnectionInfo(String propsFileName, ConnectionEnum connectionEnum, int estimatedConnectDuration, int loadLookupDataDuration)
        {
            super(propsFileName, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
        }

        protected WombatJDOPortableConnectionInfo(String propsFileNames[], ConnectionEnum connectionEnum, int estimatedConnectDuration, int loadLookupDataDuration)
        {
            super(propsFileNames, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
        }

        public String getSpringBeansFileName()
        {
            return TeresaSpringConstant.CLIENT_CONTEXT_CONFIG_LOCATION;
        }

        public String getConfigStringName()
        {
            return null;
        }
    }

    abstract public static class WombatLocalRemoteRMIConnectionInfo extends JDOLocalRemoteRMIConnectionInfo
    {
        public WombatLocalRemoteRMIConnectionInfo(String localPropsFileName, String remotePropsFileName,
                                                  String stdErrFileName, String stdOutFileName, ConnectionEnum connectionEnum,
                                                  String serverName, int estimatedConnectDuration, int loadLookupDataDuration)
        {
            super(localPropsFileName, remotePropsFileName, stdErrFileName, stdOutFileName, connectionEnum, serverName, estimatedConnectDuration, loadLookupDataDuration);
        }

        public String getSpringBeansFileName()
        {
            return TeresaSpringConstant.CLIENT_CONTEXT_CONFIG_LOCATION;
        }
    }

    abstract public static class WombatLocalRemoteHTTPConnectionInfo extends JDOLocalRemoteHTTPConnectionInfo
    {
        public WombatLocalRemoteHTTPConnectionInfo(String localPropsFileName, String remotePropsFileName,
                                                   String stdErrFileName, String stdOutFileName, ConnectionEnum connectionEnum,
                                                   String serverName, int estimatedConnectDuration, int loadLookupDataDuration)
        {
            super(localPropsFileName, remotePropsFileName, stdErrFileName, stdOutFileName, connectionEnum, serverName, estimatedConnectDuration, loadLookupDataDuration);
        }

        public String getSpringBeansFileName()
        {
            return TeresaSpringConstant.CLIENT_CONTEXT_CONFIG_LOCATION;
        }
    }

    abstract public static class WombatCayenneByStringConnectionInfo extends CayenneByStringConnectionInfo
    {
        public WombatCayenneByStringConnectionInfo( String fileName, ConnectionEnum connectionEnum,
                                                    int estimatedConnectDuration,
                                                    int loadLookupDataDuration,
                                                    ORMTypeEnum ormTypeEnum)
        {
            super( fileName, connectionEnum, estimatedConnectDuration,
                loadLookupDataDuration, ormTypeEnum);
        }
        public String getSpringBeansFileName()
        {
            return TeresaSpringConstant.CLIENT_CONTEXT_CONFIG_LOCATION;
        }
    }

    public WombatConnections()
    {
        /**
         * Not portable, as just used to get used to Kodo
         */
        String files[] = new String[]{
                "C:\\sdz-zone\\property-files\\wombatrescue\\local_demo_wombat_kodo_mysql.properties",
                "/usr/local/sdz-zone/property-files/wombatrescue/teresa_kodo_mysql.properties"
        };
        initConnection(WombatConnectionEnum.WOMBAT_KODO,
                       new WombatJDOByFileConnectionInfo(
                           files,
                           WombatConnectionEnum.WOMBAT_KODO,
                           3797, 4687)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOWombatDataStore(this);
                           }
                       });
        /**
         * Not portable, as just used to get used to Kodo
         * TODO What is this one used for? Spring service
         */
        files = new String[]{
                "C:\\sdz-zone\\property-files\\wombatrescue\\local_demo_wombat_kodo_mysql_service.properties",
                "/usr/local/sdz-zone/property-files/wombatrescue/teresa_kodo_mysql_service.properties"
        };
        initConnection(WombatConnectionEnum.WOMBAT_KODO_SERVICE,
                       new WombatJDOByFileConnectionInfo(
                           files,
                           WombatConnectionEnum.WOMBAT_KODO_SERVICE,
                           3797, 4687)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOWombatDataStore(this);
                           }
                       });
        /**
         * Used by a client when it is a remote proxy to a server-side persistence manager.
         * The word Demo means it accesses the Tomcat Server on the Desktop machine.  
         */
        initConnection(WombatConnectionEnum.REMOTE_DEMO_WOMBAT_KODO,
                       new WombatJDOByFileConnectionInfo(
                           "C:\\sdz-zone\\property-files\\wombatrescue\\remote_demo_wombat_kodo_mysql.properties",
                           WombatConnectionEnum.REMOTE_DEMO_WOMBAT_KODO,
                           2500, 4000)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOSecureWombatDataStore(this);
                           }
                       });
        /**
         * Used by a client when it is a remote proxy to a server-side persistence manager.
         * This is the one that talks to the real live server - production.
         * As using a portable properties file, can be used by a JWS app. 
         */
        initConnection(WombatConnectionEnum.REMOTE_WOMBAT_KODO,
                       new WombatJDOPortableConnectionInfo(
                           "property-files/remote_teresa_kodo_mysql.properties",
                           WombatConnectionEnum.REMOTE_WOMBAT_KODO,
                           3531, 12875)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOSecureWombatDataStore(this);
                           }
                       });
        initConnection(WombatConnectionEnum.LOCAL_TEST_WOMBAT,
                       new WombatLocalRemoteHTTPConnectionInfo(
                           "Used for simulating remote - doesn't take connections from a local",
                           "property-files/local_demo_wombat_test_mysql.jpox",
                           "C:\\jdoprodteresaserver.err",
                           "C:\\jdoprodteresaserver.out",
                           WombatConnectionEnum.LOCAL_TEST_WOMBAT,
                           "localhost", 2500, 50)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOWombatDataStore(this);
                           }
                       });
        initConnection(WombatConnectionEnum.LINODE_PROD_TERESA,
                       new WombatLocalRemoteRMIConnectionInfo(
                           "property-files/local_linode_teresa_prod_mysql.properties",
                           "property-files/remote_linode_teresa_prod_mysql.properties",
                           "/var/log/jdoprodteresaserver.err",
                           "/var/log/jdoprodteresaserver.out",
                           WombatConnectionEnum.LINODE_PROD_TERESA,
                           "www.strandz.org", 15600, 5200)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOWombatDataStore(this);
                           }
                       });
        initConnection(WombatConnectionEnum.TEST_LINODE_PROD_TERESA,
                       new WombatLocalRemoteRMIConnectionInfo(
                           "property-files/local_client_teresa_prod_mysql.properties",
                           "property-files/remote_client_teresa_prod_mysql.properties",
                           "C:\\jdoprodteresaserver.err",
                           "C:\\jdoprodteresaserver.out",
                           WombatConnectionEnum.TEST_LINODE_PROD_TERESA,
                           "localhost", 26000, 26000)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOWombatDataStore(this);
                           }
                       });
        /*
        initConnection( WombatConnectionEnum.BACKUP,
            new JDODirectConnection(
                "C:\\sdz-zone\\property-files\\wombatrescue\\local_client_wombat_back_hsqldb.properties",
                "Teresa BACKUP on localhost (Hypersonic)",
                500)
            {
            } );
        */
        initConnection(WombatConnectionEnum.DEV,
                       new WombatJDOByFileConnectionInfo(
                           "C:\\sdz-zone\\property-files\\wombatrescue\\local_client_wombat_dev_hsqldb.properties",
                           WombatConnectionEnum.DEV,
                           500, 500)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOWombatDataStore(this);
                           }
                       });
        //Got stuck with this error:
        //org.jpox.store.exceptions.IncompatibleDataTypeException: Incompatible data type for column
        // ROSTER_SLOT.DISABLED : was Unrecognized type (16), but type expected was Types.CHAR
        //From docs it seems that type must be server and version must be 1.7.1, whereas
        //1.7.2 current used with jdogenie
        //Thus going om to try MySql DEV Locally with JPOX
        initConnection(WombatConnectionEnum.DEV_JPOX,
                       new WombatJDOByFileConnectionInfo(
                           "C:\\sdz-zone\\property-files\\wombatrescue\\local_client_wombat_dev_hsqldb.jpox",
                           WombatConnectionEnum.DEV_JPOX,
                           500, 500)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOWombatDataStore(this);
                           }
                       });
        initConnection(WombatConnectionEnum.SERVER_CAYENNE,
            new WombatCayenneByStringConnectionInfo(
                "cayenne.xml",
                WombatConnectionEnum.SERVER_CAYENNE,
                750, 820, ORMTypeEnum.CAYENNE_SERVER)
            {
                public DataStore createDataStore()
                {
                    return new CayenneWombatDataStore(this);
                }

                public LoginAction createLoginAction(ConnectionEnum connectionEnum,
                                                     DataStoreFactory dataStoreFactory
                )
                {
                    LoginAction result = new EasyLoginAction();
                    return result;
                }
            });
        initConnection(WombatConnectionEnum.DIRECT_DEMO_WOMBAT_CAYENNE,
            new WombatCayenneByStringConnectionInfo(
                "cayenne-demo.xml",
                WombatConnectionEnum.DIRECT_DEMO_WOMBAT_CAYENNE,
                750, 820, ORMTypeEnum.CAYENNE_SERVER)
            {
                public DataStore createDataStore()
                {
                    return new CayenneWombatDataStore(this);
                }

                public LoginAction createLoginAction(ConnectionEnum connectionEnum,
                                                     DataStoreFactory dataStoreFactory
                )
                {
                    LoginAction result = new EasyLoginAction();
                    return result;
                }
            });
        /**
         * Used by a client when it is a remote proxy to a server-side persistence manager.
         * The word Demo means it accesses the Tomcat Server on the Desktop machine.
         */
        initConnection(WombatConnectionEnum.REMOTE_DEMO_WOMBAT_CAYENNE,
                       new WombatCayenneByStringConnectionInfo(
                           "http://localhost:8080/cayenneRemoteService/cayenne-service",
                           WombatConnectionEnum.REMOTE_DEMO_WOMBAT_CAYENNE,
                           1274, 512, ORMTypeEnum.CAYENNE_CLIENT)
                       {
                           public DataStore createDataStore()
                           {
                               return new CayenneWombatDataStore(this);
                           }
                       });

        initConnection(WombatConnectionEnum.REMOTE_PROD_WOMBAT_CAYENNE,
                       new WombatCayenneByStringConnectionInfo(
                           "http://www.strandz.org/cayenneRemoteService/cayenne-service",
                           WombatConnectionEnum.REMOTE_PROD_WOMBAT_CAYENNE,
                           1274, 512, ORMTypeEnum.CAYENNE_CLIENT)
                       {
                           public DataStore createDataStore()
                           {
                               return new CayenneWombatDataStore(this);
                           }
                       });


        /**
         * Unlike with Kodo this is not going to PROD but to test on the Linode
         */
        initConnection(WombatConnectionEnum.REMOTE_WOMBAT_CAYENNE,
                       new WombatCayenneByStringConnectionInfo(
                           "http://www.strandz.org/cayenneRemoteService/cayenne-service",
                           WombatConnectionEnum.REMOTE_WOMBAT_CAYENNE,
                           1274, 512, ORMTypeEnum.CAYENNE_CLIENT)
                       {
                           public DataStore createDataStore()
                           {
                               return new CayenneWombatDataStore(this);
                           }
                           public LoginAction createLoginAction(ConnectionEnum connectionEnum,
                                                                DataStoreFactory dataStoreFactory
                           )
                           {
                               LoginAction result = new EasyLoginAction();
                               return result;
                           }
                       });

        String testFiles[] = new String[]{
                "C:\\sdz-zone\\property-files\\wombatrescue\\local_demo_wombat_test_mysql.properties",
                //Not yet done
                //"/usr/local/sdz-zone/property-files/wombatrescue/teresa_kodo_mysql.properties"
        };
        initConnection(WombatConnectionEnum.DEMO_WOMBAT_KODO,
                       new WombatJDOByFileConnectionInfo(
                           testFiles,
                           WombatConnectionEnum.DEMO_WOMBAT_KODO,
                           3797, 4687)
                       {
                           public DataStore createDataStore()
                           {
                               return new JDOWombatDataStore(this);
                           }
                       });

        /**
        * This one is used by the web start demo
        */
        initConnection(WombatConnectionEnum.DEMO_WOMBAT_MEMORY,
                       new MemoryConnectionInfo(
                           WombatConnectionEnum.DEMO_WOMBAT_MEMORY, 750, 820)
                       {
                           public DataStore createDataStore()
                           {
                               return new MemoryWombatDataStore(this);
                           }
                       });
        /*
        initConnection( WombatConnectionEnum.LOCAL_WOMBAT_JDOGENIE,
            new JDODirectByFileConnection(
                "C:\\sdz-zone\\property-files\\wombatrescue\\local_client_wombat_test_mysql.properties",
                "Wombat JDOGenie on localhost (MySql)",
                2500, 50)
            {
            } );
        */
    }
}
