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
package org.strandz.store.timesheet;

import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;

import java.util.Collection;

public class TimesheetApplicationData
{
    public static final String JDO = "JDO";
    public static final String XML = "XML";
    public static final String storageType = XML;
    public static final String TEST = "TEST";
    public static final String DEV = "DEV";
    public static final String PROD = "PROD";
    public static final String databaseName = DEV;
    private static TimesheetApplicationData instance1;
    private static JDOTimesheetDataStore jdoInstance1;
    private static XMLTimesheetData xmlInstance1;
    private DataStore data;
    private static TimesheetApplicationData instance2;
    private static JDOTimesheetDataStore jdoInstance2;
    private static XMLTimesheetData xmlInstance2;
    private TimesheetQueries queries;

    /*
    * name is either a file name or a database name. In either
    * case it is a place where loads ro tables/classes are stored.
    */
    private TimesheetApplicationData(String type, String name)
    {
        if(type.equals(JDO))
        {
            if(name == null)
            {
                data = new JDOTimesheetDataStore(databaseName);
            }
            else
            {
                data = new JDOTimesheetDataStore(name);
            }
        }
        else if(type.equals(XML))
        {
            if(name == null)
            {
                data = new XMLTimesheetData(databaseName);
            }
            else
            {
                data = new XMLTimesheetData(name);
            }
        }
        else
        {
            Err.error("Do not support connection to a " + type);
        }
        queries = (TimesheetQueries) data;
    }

    public static TimesheetApplicationData getInstance()
    {
        return getInstance(databaseName);
    }

    public static TimesheetApplicationData getInstance(String name, String storageType)
    {
        if(instance1 == null)
        {
            instance1 = new TimesheetApplicationData(storageType, name);
            if(storageType == JDO)
            {
                jdoInstance1 = (JDOTimesheetDataStore) instance1.data;
            }
            else if(storageType == XML)
            {
                xmlInstance1 = (XMLTimesheetData) instance1.data;
            }
        }
        return instance1;
    }

    public static TimesheetApplicationData getInstance(String name)
    {
        if(instance1 == null)
        {
            instance1 = new TimesheetApplicationData(storageType, name);
            if(storageType == JDO)
            {
                jdoInstance1 = (JDOTimesheetDataStore) instance1.data;
            }
            else if(storageType == XML)
            {
                xmlInstance1 = (XMLTimesheetData) instance1.data;
            }
        }
        return instance1;
    }

    public static TimesheetApplicationData getInstance2(String name)
    {
        if(instance2 == null)
        {
            instance2 = new TimesheetApplicationData(storageType, name);
            if(storageType == JDO)
            {
                jdoInstance2 = (JDOTimesheetDataStore) instance2.data;
            }
            else if(storageType == XML)
            {
                xmlInstance2 = (XMLTimesheetData) instance2.data;
            }
        }
        return instance2;
    }

    public static JDOTimesheetDataStore getJDOInstance2(String name)
    {
        if(jdoInstance2 == null)
        {
            instance2 = new TimesheetApplicationData(JDO, name);
            jdoInstance2 = (JDOTimesheetDataStore) instance2.data;
        }
        return jdoInstance2;
    }

    public static XMLTimesheetData getXMLInstance2(String fileName)
    {
        if(xmlInstance2 == null)
        {
            instance2 = new TimesheetApplicationData(XML, fileName);
            xmlInstance2 = (XMLTimesheetData) instance2.data;
        }
        return xmlInstance2;
    }

    /*
    * Make user getData()
    */
    /*
    public void readData( int i)
    {
    data.readData( i);
    }
    public Object get( Class clazz)
    {
    return data.get( clazz);
    }
    public int writeData( int i)
    {
    return data.writeData( i);
    }
    */

    public Collection queryAllTimesheets()
    {
        return queries.queryAllTimesheets();
    }

    public TimesheetQueries getQueries()
    {
        return queries;
    }

    public DataStore getData()
    {
        return data;
    }

    public DataStore getData2()
    {
        return data;
    }
}
