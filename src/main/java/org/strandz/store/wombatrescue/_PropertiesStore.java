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

import org.strandz.data.wombatrescue.objects.DBProperty;
import org.strandz.lgpl.store.XMLFileData;
import org.strandz.lgpl.util.Err;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Portability introduced later. Will make writing properties impossible, but this
 * feature never used anyway. Intention is to use this file to enter the email
 * addresses that are on yahoo. Whenever deploy webstart the new addresses will
 * come across.
 * <p/>
 * Decided this hardly a priority, and could have led to a bit of refactoring
 * (ie. work) so put an underscore in front to indicate that no longer using
 * this class. (Properties.xml actually contained an empty array).
 */
public class _PropertiesStore extends XMLFileData
{
    private static final String fileName = "C:\\core\\data\\wombatrescue\\Properties.xml";
    private static final String portableFileName = "Properties.xml";
    public static final Class PROPERTIES = DBProperty.class;
    private static final int NUM_OF_PROPERTIES = 1;
    public static final String BACKUP_FILE_PROP = "backupFile";
    public static final Class[] CLASSES = {PROPERTIES};
    private Properties props = new Properties();
    private boolean portable;

    /**
     * @param portable If portable then need a different default filename.
     */
    public _PropertiesStore(boolean portable)
    {
        if(!portable)
        {
            init(fileName);
        }
        else
        {
            init(portableFileName);
        }
    }

    private _PropertiesStore(String fileName)
    {
        init(fileName);
    }

    private void init(String fileName)
    {
        super.setClasses(CLASSES);
        super.setFilename(fileName);
    }

    private void setPropertyNames()
    {
        props.setProperty(BACKUP_FILE_PROP,
            "C:\\sdz-zone\\data\\wombatrescue\\BackupWombatData.xml");
    }

    public String getProperty(String name)
    {
        String result = null;
        result = props.getProperty(name);
        return result;
    }

    private static String getDBProperty(String name, List dbProps)
    {
        String result = null;
        for(Iterator iter = dbProps.iterator(); iter.hasNext();)
        {
            DBProperty prop = (DBProperty) iter.next();
            if(prop.getName().equals(name))
            {
                result = prop.getValue();
                break;
            }
        }
        return result;
    }

    private static void setDBProperty(String name, String value, List dbProps)
    {
        for(Iterator iter = dbProps.iterator(); iter.hasNext();)
        {
            DBProperty prop = (DBProperty) iter.next();
            if(prop.getName().equals(name))
            {
                prop.setValue(value);
                break;
            }
        }
    }

    public void fillProperties()
    {
        if(!props.isEmpty())
        {
            Err.error("Expected fillProps() to start with an empty props");
        }
        setPropertyNames();
        startTx();

        List dbProps = (List) get(PROPERTIES);
        commitTx();
        /*
        if(dbProps.size() < 0)
        {
        Err.error( "Expected to find zero or more properties in the database, " +
        "but instead found " + dbProps.size());
        }
        */
        for(Enumeration enumId = props.propertyNames(); enumId.hasMoreElements();)
        {
            String name = (String) enumId.nextElement();
            String value = getDBProperty(name, dbProps);
            /*
            * Will be this when are first filling the DB
            */
            if(value == null)
            {// Err.error( "Did not expect to get a blank <" + name + "> property");
            }
            else
            {
                props.setProperty(name, value);
            }
        }
        // data.writeData();
    }

    public void storeProperties()
    {
        if(props.isEmpty())
        {
            Err.error("Expected fillProps() to start with a NOT empty props");
        }
        // Don't worry about reading as always have an open read as whenever
        // commit write then read again.
        rollbackTx(); // In case user did not commit the real data, will now
        // loose it all.
        startTx();

        List dbProps = (List) get(PROPERTIES);
        for(Enumeration enumId = props.propertyNames(); enumId.hasMoreElements();)
        {
            String name = (String) enumId.nextElement();
            setDBProperty(name, props.getProperty(name), dbProps);
        }
        commitTx();
    }
    /*
    public void storeProperties()
    {
    if(props.isEmpty())
    {
    Err.error( "Expected storeProps() to start with a NON empty props");
    }
    ds.readData( DataStore.NO_CURRENT_TXN);
    List dbProps = (List)ds.get( PROPERTIES);
    if(dbProps.size() != NUM_OF_PROPERTIES)
    {
    Err.error( "Expected to find " + NUM_OF_PROPERTIES + " properties, " +
    " but instead found " + dbProps.size());
    }
    for(Iterator iter = dbProps.iterator(); iter.hasNext();)
    {
    DBProperty dbProp = (DBProperty)iter.next();
    String dbName = dbProp.getName();
    boolean found = false;
    for(Enumeration enumId = props.propertyNames(); enumId.hasMoreElements();)
    {
    String name = (String)enumId.nextElement();
    if(dbName.equals( name))
    {
    dbProp.setValue( props.getProperty( name));
    found = true;
    }
    }
    if(!found)
    {
    Err.error( "Expected to find a property named: " + dbName);
    }
    }
    ds.writeData( DataStore.DO_NOT_START_NEW_TXN);
    }
    */
}
