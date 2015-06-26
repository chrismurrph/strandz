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
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.PropertyUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCCopyTable
{
    private static String driverClass;
    private static String connectionURL;
    private static String userID;
    private static String userPassword;
    //
    private static String ddlDropStatement;
    private static String ddlCreateStatement;
    private static String queryInsertStatement;

    Connection con = null;

    public JDBCCopyTable()
    {
        popDebianTeresaProd();
        popNumDaysIntervalTable();
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

    private void descTable(String tableName, DatabaseMetaData md) throws SQLException
    {
        ResultSet rs = md.getColumns(null, null, tableName, null);
        Err.pr("Table " + tableName + " has these columns:");
        Print.prResultSet(rs);
        rs.close();
    }

    public void closeConnection()
    {
        try
        {
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
        JDBCCopyTable obj = new JDBCCopyTable();
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
        dropTable();
        createTable();
        copyTable();
    }

    void dropTable()
    {
        Statement stmt = null;
        String ddlString = ddlDropStatement;

        try
        {
            stmt = con.createStatement();
            stmt.executeUpdate(ddlString);
        }
        catch(SQLException e)
        {
            //No need to error, it might not exist
            //Err.error( e );
        }
    }

    void copyTable()
    {
        Statement stmt = null;
        String ddlString = queryInsertStatement;

        try
        {
            stmt = con.createStatement();
            stmt.executeUpdate(ddlString);
        }
        catch(SQLException e)
        {
            Err.error(e);
        }
    }

    void createTable()
    {
        Statement stmt = null;
        String ddlString = ddlCreateStatement;

        try
        {
            stmt = con.createStatement();
            stmt.executeUpdate(ddlString);
        }
        catch(SQLException e)
        {
            Err.error(e);
        }
    }

    private void popRosterSlotTable()
    {
        ddlDropStatement = "DROP TABLE roster_slot";
        ddlCreateStatement =
            "CREATE TABLE roster_slot (" +
                "    roster_slot_id INTEGER NOT NULL,        " + //-- <pk>
                "    pk_id SMALLINT NULL,                    " + //-- betweenShifts
                "    pk_id2 SMALLINT NULL,                   " + //-- dayInWeek
                "    disabled BIT NULL,                      " + //-- disabled
                "    monthly_restart BIT NULL,               " + //-- monthlyRestart
                "    not_available BIT NULL,                 " + //-- notAvailable
                "    not_in_month_pk_id SMALLINT NULL,       " + //-- notInMonth
                "    only_in_month_pk_id SMALLINT NULL,      " + //-- onlyInMonth
                "    pk_id3 SMALLINT NULL,                   " + //-- overridesOthers
                "    specific_date DATETIME NULL,            " + //-- specificDate
                "    start_date DATETIME NULL,               " + //-- startDate
                "    volunteer_id INTEGER NULL,              " + //-- volunteer
                "    pk_id4 SMALLINT NULL,                   " + //-- weekInMonth
                "    pk_id5 SMALLINT NULL,                   " + //-- whichShift
                "    worker_id INTEGER NULL,                 " + //-- worker
                "    jdo_version SMALLINT NOT NULL,          " + //-- <opt-lock>
                "    CONSTRAINT pk_roster_slot PRIMARY KEY (roster_slot_id)" +
                ") TYPE = InnoDB;";
        queryInsertStatement =
            "INSERT INTO roster_slot" +
                "( roster_slot_id, pk_id, pk_id2, disabled, monthly_restart, not_available, " +
                "not_in_month_pk_id, only_in_month_pk_id, pk_id3, specific_date, start_date, " +
                "volunteer_id, pk_id4, pk_id5, worker_id, jdo_version)" +
                " SELECT roster_slot_id, pk_id, pk_id2, disabled, monthly_restart, not_available, " +
                " not_in_month_pk_id, only_in_month_pk_id, pk_id3, specific_date, start_date, " +
                " volunteer_id, pk_id4, pk_id5, worker_id, jdo_version FROM roster_slot_old_data;";
    }

    private void popNumDaysIntervalTable()
    {
        ddlDropStatement = "DROP TABLE num_days_interval";
        /* MySql style */
        ddlCreateStatement =
            "CREATE TABLE num_days_interval (" +
                "     pk_id SMALLINT NOT NULL,       " + //-- pkId
                "     days INTEGER NULL,             " + //-- days
                "     nme VARCHAR(255) NULL,         " + //-- name
                "     jdo_version SMALLINT NOT NULL, " + //-- <opt-lock>
                "CONSTRAINT pk_num_days_interval PRIMARY KEY (pk_id)" +
                ") TYPE = InnoDB;";
        /* Hypersonic style
        ddlCreateStatement =
            "CREATE CACHED TABLE NUM_DAYS_INTERVAL (" +
            "     pk_id SMALLINT NOT NULL,       " + //-- pkId
            "     days INTEGER NULL,             " + //-- days
            "     nme VARCHAR(255) NULL,         " + //-- name
            "     jdo_version SMALLINT NOT NULL, " + //-- <opt-lock>
            "CONSTRAINT pk_num_days_interval PRIMARY KEY (pk_id)" +
            ");";
        */
        //With MySql the table will be INTERVL
        queryInsertStatement =
            "INSERT INTO num_days_interval" +
                "( pk_id, days, nme, jdo_version)" +
                " SELECT pk_id, days, nme, jdo_version FROM intervl;";
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
}
