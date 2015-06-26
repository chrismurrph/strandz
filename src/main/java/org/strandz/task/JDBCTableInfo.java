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
package org.strandz.task;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.lgpl.util.Utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCTableInfo
{
    private static String driverClass;
    private static String connectionURL;
    private static String userID;
    private static String userPassword;

    Connection con = null;

    public JDBCTableInfo()
    {
        popMoney();
        try
        {
            Err.pr("To load JDBC driver " + driverClass + Utils.NEWLINE);
            Class.forName(driverClass).newInstance();
            Err.pr("To connect to " + connectionURL + Utils.NEWLINE);
            this.con = DriverManager.getConnection(connectionURL, userID, userPassword);
            Err.pr("Are connected as " + userID + Utils.NEWLINE);
        }
        catch(ClassNotFoundException e)
        {
            Err.error(e);
        }
        catch(InstantiationException e)
        {
            Err.error(e);
        }
        catch(IllegalAccessException e)
        {
            Err.error(e);
        }
        catch(SQLException e)
        {
            Err.error(e);
        }
    }

    private void descTable(String tableName, DatabaseMetaData md) throws SQLException
    {
        ResultSet rs = md.getColumns(null, null, tableName, null);
        Err.pr("Table " + tableName + " has these columns:");
        Print.prResultSet(rs);
        rs.close();
    }

    /**
     * This method will use a DatabaseMetaData object to obtain the capabilities of the
     * JDBC driver and the database.
     */
    private void findDBInfo()
    {
        DatabaseMetaData md = null;
        try
        {
            // con was established on construction
            md = con.getMetaData();
            Err.pr("Tables listing:");
            try
            {
                ResultSet rs = md.getTables(null, null, null, null);
                Print.prTableResultSet(rs);
                rs.close();
            }
            catch(SQLException e)
            {
                Err.error(e);
            }
            //descTable( caseAdjustment( "VOLUNTEER"), md);
            runCommand();
        }
        catch(SQLException e)
        {
            Err.error(e);
        }
    }

    public void closeConnection()
    {
        try
        {
            con.close();
            Err.pr("Connection successfully closed");
        }
        catch(SQLException e)
        {
            Err.error(e);
        }
    }

    public static void main(String[] args)
    {
        JDBCTableInfo dmde = new JDBCTableInfo();
        if(dmde.con != null)
        {
            dmde.findDBInfo();
            dmde.closeConnection();
        }
    }

    /**
     * Might be a good place to say create a new table, sometimes better than running mysql etc
     */
    void runCommand()
    {
//    CREATE TABLE buddy_manager (
//       ->     buddy_manager_id INTEGER NOT NULL,      -- <pk>
//       ->     pk_id SMALLINT NULL,                    -- dayInWeek
//       ->     volunteer_id INTEGER NULL,              -- volunteer
//       ->     pk_id2 SMALLINT NULL,                   -- whichShift
//       ->     jdo_version SMALLINT NOT NULL,          -- <opt-lock>
//       ->     CONSTRAINT pk_buddy_manager PRIMARY KEY (buddy_manager_id)
//       -> ) TYPE = InnoDB;
    }

    private static String caseAdjustment(String name)
    {
        String result = null;
        boolean lowerC = true;
        if(driverClass.indexOf("mysql") != -1)
        {
            result = name.toLowerCase();
        }
        else if(driverClass.indexOf("hsqldb") != -1)
        {
            result = name.toUpperCase();
            lowerC = false;
        }
        else
        {
            Err.error("Do not know case of tables for DB " + driverClass);
        }
        if(lowerC)
        {
            Err.pr("Have changed to lowercase for DB " + driverClass);
        }
        return result;
    }

    //jdbc:mysql://localhost/mvnforum?useUnicode=true&characterEncoding=utf-8
    private void popMvnForum()
    {
        String propFileName = "property-files/mvnforum.properties";
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        driverClass = PropertyUtils.getProperty("driverClass", props);
        connectionURL = PropertyUtils.getProperty("connectionURL", props);
        userID = PropertyUtils.getProperty("userID", props);
        userPassword = PropertyUtils.getProperty("userPassword", props);
    }

    private void popMoney()
    {
        driverClass = "com.mysql.jdbc.Driver";
        connectionURL = "jdbc:mysql://127.0.0.1:3306/books";
        userID = "root";
    }

    private void popJim()
    {
        driverClass = "com.mysql.jdbc.Driver";
        connectionURL = "jdbc:mysql://127.0.0.1:3306/jim";
        userID = "jim";
        userPassword = "jimpass";
    }

    private void popWombatRescueDemo()
    {
        driverClass = "com.mysql.jdbc.Driver";
        connectionURL = "jdbc:mysql://localhost:3306/test";
        userID = "root";
        //Did not set a pw and don't seem to need one, which is good for a demo!
        //userPassword = "";
    }

    private void popClientWombatDev()
    {
        String propFileName = "property-files/local_client_wombat_dev_hsqldb.jdogenie";
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        driverClass = PropertyUtils.getProperty("store0.driver", props);
        connectionURL = PropertyUtils.getProperty("store0.url", props);
        userID = PropertyUtils.getProperty("store0.user", props);
    }

    private void popClientTeresaProd()
    {
        String propFileName = "property-files/local_client_teresa_prod_mysql.jdogenie";
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        driverClass = PropertyUtils.getProperty("store0.driver", props);
        connectionURL = PropertyUtils.getProperty("store0.url", props);
        userID = PropertyUtils.getProperty("store0.user", props);
        userPassword = PropertyUtils.getProperty("store0.password", props);
    }

    private void popDebianTeresaProd()
    {
        String propFileName = "property-files/remote_debian_teresa_prod_mysql.jdogenie";
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        driverClass = PropertyUtils.getProperty("store0.driver", props);
        connectionURL = PropertyUtils.getProperty("store0.url", props);
        userID = PropertyUtils.getProperty("store0.user", props);
        userPassword = PropertyUtils.getProperty("store0.password", props);
    }

    private void popDebianTeresaDev()
    {
        String propFileName = "property-files/remote_debian_teresa_dev_mysql.jdogenie";
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        driverClass = PropertyUtils.getProperty("store0.driver", props);
        connectionURL = PropertyUtils.getProperty("store0.url", props);
        userID = PropertyUtils.getProperty("store0.user", props);
        userPassword = PropertyUtils.getProperty("store0.password", props);
    }

    private void popLinodeTeresaProd()
    {
        String propFileName = "property-files/remote_linode_teresa_prod_mysql.jdogenie";
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        driverClass = PropertyUtils.getProperty("store0.driver", props);
        connectionURL = PropertyUtils.getProperty("store0.url", props);
        userID = PropertyUtils.getProperty("store0.user", props);
        userPassword = PropertyUtils.getProperty("store0.password", props);
    }
}
