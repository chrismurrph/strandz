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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This all about populating a JDO database from a JDBC source. Have only got to the JDBC part.
 * Realised that IFF the source is also a JDO database then can go back to CopyDBtoDB and
 * get it working if go to more manual means. Thus use the code from there to complete this when
 * coding this becomes necessary. If wanted to move a legacy JDBC database to JDO/datastore identity
 * then would have to use this.
 */
public class JDBCVersantToJPOX
{
    private String driverClass;
    private String connectionURL;
    private String userID;
    private String userPassword;
    //
    private Col[] versantWorkerColumns = new Col[]{
        new Col("worker_id", String.class),
        new Col("away1_end", String.class),
        new Col("away1_start", String.class),
        new Col("away2_end", String.class),
        new Col("away2_start", String.class),
        new Col("blngs_to_group_worker_id", String.class),
        new Col("birthday", String.class),
        new Col("christian_name", String.class),
        new Col("comments", String.class),
        new Col("contact_name", String.class),
        new Col("dmmy", String.class),
        new Col("email1", String.class),
        new Col("email2", String.class),
        new Col("pk_id", String.class),
        new Col("group_contact_person", String.class),
        new Col("group_name", String.class),
        new Col("home_phone", String.class),
        new Col("mobile_phone", String.class),
        new Col("postcode", String.class),
        new Col("pk_id2", String.class),
        new Col("pk_id3", String.class),
        new Col("pk_id4", String.class),
        new Col("street", String.class),
        new Col("suburb", String.class),
        new Col("surname", String.class),
        new Col("unknown", String.class),
        new Col("work_phone", String.class),
        new Col("jdo_version", String.class),
    };
    private String queryVersantWorker =
        "SELECT " +
            versantWorkerColumns[0].getName() +
            versantWorkerColumns[1].getName() +
            versantWorkerColumns[2].getName() +
            versantWorkerColumns[3].getName() +
            versantWorkerColumns[4].getName() +
            versantWorkerColumns[5].getName() +
            versantWorkerColumns[6].getName() +
            versantWorkerColumns[7].getName() +
            versantWorkerColumns[8].getName() +
            versantWorkerColumns[9].getName() +
            versantWorkerColumns[10].getName() +
            versantWorkerColumns[11].getName() +
            versantWorkerColumns[12].getName() +
            versantWorkerColumns[13].getName() +
            versantWorkerColumns[14].getName() +
            versantWorkerColumns[15].getName() +
            versantWorkerColumns[16].getName() +
            versantWorkerColumns[17].getName() +
            versantWorkerColumns[18].getName() +
            versantWorkerColumns[19].getName() +
            versantWorkerColumns[20].getName() +
            versantWorkerColumns[21].getName() +
            versantWorkerColumns[22].getName() +
            versantWorkerColumns[23].getName() +
            versantWorkerColumns[24].getName() +
            versantWorkerColumns[25].getName() +
            versantWorkerColumns[26].getName() +
            versantWorkerColumns[27].getLastName() +
            "FROM WORKER" +
            " ORDER BY christian_name, surname;";
    //
    Connection sourceCon = null;
    Connection targetCon = null;

    public JDBCVersantToJPOX()
    {
        popClientWombatDev();
        sourceCon = connect();
        popLocalWombat();
        targetCon = connect();
        Err.pr("sourceCon is " + sourceCon);
        Err.pr("targetCon is " + targetCon);
    }

    private Connection connect()
    {
        Connection result = null;
        try
        {
            System.out.print("  Loading JDBC Driver  -> " + driverClass + "\n");
            Class.forName(driverClass).newInstance();
            System.out.print("  Connecting to        -> " + connectionURL + "\n");
            result = DriverManager.getConnection(connectionURL, userID, userPassword);
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
        return result;
    }

    private void closeConnection(Connection con)
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
        JDBCVersantToJPOX obj = new JDBCVersantToJPOX();
        if(obj.sourceCon != null && obj.targetCon != null)
        {
            obj.runCommands();
            obj.closeConnection(obj.sourceCon);
            obj.closeConnection(obj.targetCon);
        }
    }

    /**
     *
     */
    void runCommands()
    {
        queryTable(sourceCon);
        //queryTable( targetCon);
    }

    void queryTable(Connection con)
    {
        Statement stmt = null;
        ResultSet rset = null;
        String queryString = queryVersantWorker;
        try
        {
            stmt = con.createStatement();
            rset = stmt.executeQuery(queryString);

            int counter = 0;

            while(rset.next())
            {
                System.out.println();
                System.out.println("  Row [" + ++counter + "]");
                System.out.println("  ---------------------");
                for(int i = 0; i < versantWorkerColumns.length; i++)
                {
                    Col versantWorkerColumn = versantWorkerColumns[i];
                    Err.pr(versantWorkerColumn.getName() + rset.getString(i + 1));
                }
            }

            System.out.println();
            System.out.print("  Closing ResultSet...\n");
            rset.close();

            System.out.print("  Closing Statement...\n");
            stmt.close();

        }
        catch(SQLException e)
        {
            Err.error(e);
        }
    }

    private static class Col
    {
        String name;
        Class type;

        Col(String name, Class type)
        {
            this.name = name;
            this.type = type;
        }

        String getName()
        {
            return name + ", ";
        }

        public String getLastName()
        {
            return name + " ";
        }
    }

    private void popClientWombatDev()
    {
        driverClass = "org.hsqldb.jdbcDriver";
        connectionURL = "jdbc:hsqldb:hsql://localhost/wombat_dev";
        userID = "sa";
    }

    private void popLocalWombat()
    {
        driverClass = "com.mysql.jdbc.Driver";
        connectionURL = "jdbc:mysql://localhost:3306/test";
        userID = "root";
    }
}
