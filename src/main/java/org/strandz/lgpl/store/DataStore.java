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

import org.strandz.lgpl.persist.DataStoreI;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.note.JDONote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract public class DataStore implements DataStoreI
{
    Class classes[];
    List extents = new ArrayList();
    private String name;
    private String description;
    private int readingTimes;
    private int estimatedConnectDuration;
    private int estimatedLookupDataDuration;
    private DomainQueriesI domainQueriesI;
    ConnectionInfo connectionInfo;
    private LookupsProviderI lookupsProvider;
    private LookupsI lookups;    

    private static int writeTimes;
    private static int readTimes;
    private static int counter;
    private static int constructedTimes;
    private int id;

    public DataStore()
    {
        constructedTimes++;
        id = constructedTimes;
    }

    public void setClasses(Class classes[])
    {
        this.classes = classes;
    }

    public int size()
    {
        return classes.length;
    }

    public Object get(Class clazz)
    {
        Object result = null;
        int index = indexOfClass(clazz);
        if(index == -1)
        {
            Err.error( "Could not find a " + clazz + " in " + this.getClass().getName());
        }
        if(extents.size() <= index)
        {
            //We want more flexibility than this
            //Print.prList( extents, "Too small extents", false);
            //Err.error( "Only have " + extents.size() + " extents, trying to index " + index + " in [" + this /*+ "] for extents id " + extents.id*/);
        }
        else
        {
            result = extents.get( index);
        }
        return result;
    }

    abstract public void set(int whichExtent, Object obj);

    public void set(Class clazz, Object obj)
    {
        set(indexOfClass(clazz), obj);
    }

    /**
     * Don't use this - when read from file in subclasses extents is overwritten, say when using XMLFileData
     * @param list
     */
    public void addExtent(List list)
    {
        extents.add(list);
        //Err.pr( "Added extent to [" + this + "] so now: " + extents.size() /*+ ", for extents id: " + extents.id*/);
    }

    int indexOfClass( Class clazz)
    {
        int i = classIndex( clazz);
        if (i == classes.length)
        {
            Err.error("Are not storing an extent of type " + clazz.getName() + " in " + this);
        }
        return i;
    }

    private int classIndex( Class clazz)
    {
        int result = 0;
        boolean found = false;
        for (; result < classes.length; result++)
        {
            if (clazz == classes[result])
            {
                found = true;
                break;
            }
        }
        if(!found)
        {
            result = -1;
        }
        return result;
    }

    public boolean storesClass( Class aClass)
    {
        boolean result = false;
        if(classIndex( aClass) != -1)
        {
            result = true;
        }
        return result;
    }

    public String info()
    {
        List list = Utils.asArrayList(classes, new ArrayList(classes.length));
        return "ID: [" + id + "]" + " <" + Print.getPrList(list, "Classes", new ArrayList()) + ">";
    }

    /**
     * Intended to make updating a LOV a little safer.
     */
    public void update(Class clazz, List newList)
    {
        Object obj = get(clazz);
        if (obj instanceof List)
        {
            List list = (List) obj;
            if (list.isEmpty())
            {
                set(clazz, newList);
            }
            else
            {
                // Leave alone values that already have, and add values
                // that don't. (No provision for deletes)
                for (Iterator iter = newList.iterator(); iter.hasNext();)
                {
                    Object potentialEle = iter.next();
                    if (!list.contains(potentialEle))
                    {
                        list.add(potentialEle);
                    }
                }
            }
        }
        else
        {
            Err.error("update() does not YET support a " + obj.getClass());
        }
    }

    public void debug()
    {
        Err.pr("DEBUG");
        if (extents.size() == 0)
        {
            Err.error("No extents in " + this);
        }
        else
        {
            Print.prList( extents, "The extents in " + this, false);
        }
        /*
        * This fine
        for(int i = 0; i < extents.size(); i++)
        {
        if(extents.get(i).getClass() == DebugList.class)
        {
        DebugList dList = (DebugList)extents.get(i);
        Print.pr( "ID: " + dList.id);
        Print.prList( dList);
        }
        }
        */
        // Volunteer vol = (Volunteer)((List)extents.get( 0)).get( 1);
        // Err.pr( "In extent: " + vol + ", ID: " + vol.id);
    }

    abstract public void flush();

    public boolean commitTx()
    {
        if (readingTimes == 0)
        {
            Err.error("WRITE DATA[times:" + ++writeTimes
                + "], Cannot write data when have not first read it - " + info());
        }
        decReadingTimes();
        /*
         Err.pr( "WRITE DATA[times:" + ++writeTimes + "]: " + info());
         if(writeTimes == 0)
         {
         Err.stack();
         }
         */
        return false;
    }
    
    protected void decReadingTimes()
    {
        readingTimes--;
    }

    protected void incReadingTimes()
    {
        readingTimes++;
        Err.pr(JDONote.RELOAD_PM_KODO_BUG, "have incReadingTimes() to " + readingTimes + " in " + this);
    }
        
    /*
     * If a transaction is currently open, and we don't
     * want to write it away, then call unReadData().
     * For instance when user presses load, then what
     * have put into text fields but not yet committed
     * will effectively be discarded. If DB knows nothing
     * of what went on since last commit then don't need
     * to tell it to rollback. (With JDO the read will
     * cause the active transaction to rollback).
     */
    public void rollbackTx()
    {
        readingTimes = 0;
    }

    public boolean isOnTx()
    {
        return readingTimes != 0;
    }
    
    public void startTx()
    {
        startTx( this.getClass().getName());
    }

    public void startTx( String reason)
    {
        Err.pr( "** startTx(): " + reason);
        if (readingTimes != 0)
        {
            Err.error("READ DATA[" + ++readTimes
                + "], Have not yet written data that have read previously - " + info());
        }
        // DON'T comment this line out!
        incReadingTimes();
        /**/
        Err.pr("startTx()[" + ++readTimes + "]: " + info());
        if(readTimes == 0)
        {
            Err.stack();
        }
        /**/
    }

    // abstract public void close();
    abstract public List query(Class clazz);

    //abstract public void setRefinedList( Class clazz, Object list );

    public String getName()
    {
        return name;
    }

    public void setName( String name)
    {
        this.name = name;
        Err.pr( "Setting " + getClass().getName() + " name to <" + name + ">");
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
        Err.pr( "Setting " + getClass().getName() + " description to <" + description + ">");
    }

    public String toString()
    {
        String result = getName() + " ID: " + id;
        if (result == null)
        {
            result = info();
        }
        return result;
    }

    /**
     * This method is a NOP (no operation). Some DataStores may choose to
     * implement this. In that case some of their queries will just get
     * from an already stored list. Intended to be used when you do not
     * need the absolutely latest. It is also important when you want
     * consistent results. Thus all reports should set to use the cache
     * before the report starts, and turn the cache off at the end.
     *
     * By default caching is set to on. For pessimistic transactions
     * no point in turning the cache off. Instead use clearCaches()
     * at the time that doing a re-query from the DB. This includes
     * the time when the user presses commit.
     *
     * @param b
     */
    /*
    public void setUseCache( boolean b )
    {
    }
    */

    /**
     * For pessimistic transactions use this method at the time that doing
     * a re-query from the DB. This usually includes the time when the user presses
     * commit.
     */
    /*
    public void clearCaches( String id)
    {
    }
    */

    public Class[] getClasses()
    {
        return classes;
    }

    /**
     * @return milliseconds
     */
    public int getEstimatedConnectDuration()
    {
        return estimatedConnectDuration;
    }

    /**
     * @param duration milliseconds
     */
    public void setEstimatedConnectDuration(int duration)
    {
        this.estimatedConnectDuration = duration;
    }

    public void setEstimatedLookupDataDuration(int duration)
    {
        this.estimatedLookupDataDuration = duration;
    }

    public int getEstimatedLookupDataDuration()
    {
        return estimatedLookupDataDuration;
    }

    public DomainQueriesI getDomainQueries()
    {
        if (domainQueriesI == null)
        {
            Err.error("domainQueriesI of DataStore has not been set");
        }
        return domainQueriesI;
    }

    public void setDomainQueries(DomainQueriesI domainQueriesI)
    {
        this.domainQueriesI = domainQueriesI;
    }

    public void setConnection(ConnectionInfo connectionInfo)
    {
        this.connectionInfo = connectionInfo;
    }

    public ConnectionInfo getConnection()
    {
        return connectionInfo;
    }
        
    public void setLookupsProvider( LookupsProviderI lookupsProvider)
    {
        this.lookupsProvider = lookupsProvider;        
    }
    
    public LookupsProviderI getLookupsProvider()
    {
        return lookupsProvider;
    }
    
    protected void initLookups()    
    {
        LookupsProviderI lookupsProvider = getLookupsProvider();
        //Too harsh
        Assert.notNull( lookupsProvider, "No lookupsProvider in " + this.getClass().getName());
        if(lookupsProvider == null)
        {
            Err.pr( "WARNING: Must be a very simple application not to have a lookups provider");
        }
        else
        {
            lookups = lookupsProvider.obtainLookups();
            Assert.notNull( lookups);
        }
    }
    
    public LookupsI getLookups()
    {
        //Assert.notNull( lookups, "initLookups() was not called");
        if(lookups == null)
        {
            initLookups();
        }
        return lookups;
    }
}
