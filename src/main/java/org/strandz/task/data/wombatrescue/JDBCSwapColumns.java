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
package org.strandz.task.data.wombatrescue;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.PropertyUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCSwapColumns
{
    private static String driverClass;
    private static String connectionURL;
    private static String userID;
    private static String userPassword;

    Connection con = null;

    public JDBCSwapColumns()
    {
        popDebianTeresaDev();
        try
        {
            System.out.print("  Loading JDBC Driver  -> " + driverClass + "\n");
            Class.forName(driverClass).newInstance();
            System.out.print("  Connecting to        -> " + connectionURL + "\n");
            this.con = DriverManager.getConnection(connectionURL, userID, userPassword);
            System.out.print("  Connected as         -> " + userID + "\n");
        }
        catch(ClassNotFoundException e)
        {
            Err.error(e, "Could not connect to " + connectionURL);
        }
        catch(InstantiationException e)
        {
            Err.error(e, "Could not connect to " + connectionURL);
        }
        catch(IllegalAccessException e)
        {
            Err.error(e, "Could not connect to " + connectionURL);
        }
        catch(SQLException e)
        {
            Err.error(e, "Could not connect to " + connectionURL);
        }
    }

    public void closeConnection()
    {
        try
        {
            //autocommit probably true
            //con.commit();
            System.out.print("  Closing Connection...\n");
            con.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Sole entry point to the class and application.
     *
     * @param args Array of String arguments.
     */
    public static void main(String[] args)
    {
        JDBCSwapColumns obj = new JDBCSwapColumns();
        if(obj.con != null)
        {
            obj.runCommands();
            obj.closeConnection();
        }
    }

    /**
     *
     */
    void runCommands()
    {
        swapColumns();
    }

    //Not working, used mysql directly instead. Doing this command made both columns
    //have the same data!! If need again then use a ResultSet and dynamically create
    //rows like this:
    //UPDATE roster_slot SET pk_id2 = 0, pk_id = 3 WHERE roster_slot_id = 349;
    //OR use mysqldump then TextPad marcos to create the UPDATE statements
    void swapColumns()
    {
        Statement stmt = null;
        String statementString1 =
            "UPDATE roster_slot SET pk_id = pk_id2, pk_id2 = pk_id;";

        try
        {
            stmt = con.createStatement();
            stmt.executeUpdate(statementString1);
        }
        catch(SQLException e)
        {
            Err.error(e);
        }
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
}
