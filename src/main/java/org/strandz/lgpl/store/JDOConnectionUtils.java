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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDOConnectionUtils
{
    private static Properties props;
    private static java.sql.Connection con;
    // private static PersistenceManagerFactory pmf;

    private JDOConnectionUtils()
    {
        Err.error("JDOConnectionUtils is not to be instantiated");
    }

    /*
    public static PersistenceManagerFactory getMySqlPMF()
    {
    Properties props = getMySql_DEV_Properties();
    Print.prMap( props);
    return JDOHelper.getPersistenceManagerFactory( props);
    }
    */

    public static PersistenceManagerFactory getPMF(String lidoconfig, // LiDO specific config file
                                                   String metadata)
    { // XML metadata
        Properties props = new Properties();
        props.put("javax.jdo.PersistenceManagerFactoryClass",
            "com.libelis.lido.PersistenceManagerFactory");
        if(lidoconfig != null)
        {
            props.put("lido.config", lidoconfig);
        }
        if(metadata != null)
        {
            props.put("jdo.metadata", metadata);
        }
        Print.prMap(props);
        return JDOHelper.getPersistenceManagerFactory(props);
    }

    private static Properties getProps(String path)
    {
        if(props == null)
        {
            try
            {
                InputStream propStream = new FileInputStream(path);
                props = new Properties();
                props.load(propStream);
            }
            catch(IOException e)
            {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return props;
    }

    // A method that will retrieve a JDBC connection to the database
    public static java.sql.Connection getConnection(String path)
    {
        if(con == null)
        {
            String driverName = getProps(path).getProperty("javax.jdo.option.connectionDriverName");
            try
            {
                Class.forName(driverName);
            }
            catch(ClassNotFoundException e)
            {
                e.printStackTrace();
                System.exit(-1);
            }

            String url = getProps(path).getProperty("javax.jdo.option.connectionURL");
            String userName = getProps(path).getProperty("javax.jdo.option.connectionUserName");
            String password = getProps(path).getProperty("javax.jdo.option.connectionPassword");
            try
            {
                con = DriverManager.getConnection(url, userName, password);
            }
            catch(SQLException e)
            {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return con;
    }
}
