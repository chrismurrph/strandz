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
package org.strandz.data.wombatrescue.business;

import org.strandz.data.wombatrescue.calculated.ParticularShift;
import org.strandz.data.wombatrescue.calculated.MonthTransferObj;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.PropertiesHolder;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.util.applic.wombatrescue.WombatConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class RosterSessionUtils
{
    private static RostererSessionI globalRostererSession;
    private static ParticularRosterI globalCurrentParticularRoster;
    private static DataStore dataStore;
    private static PropertiesHolder propertiesHolder = new PropertiesHolder();
    private static RosterSessionUtils INSTANCE = new RosterSessionUtils(); 
//    private static Lookups lookups;

    static List fromYahooEmails;

    static boolean noYahooFileExists()
    {
        return fromYahooEmails == null;
    }

    /**
     * All workers email addresses that have previously been downloaded into a file from yahoo.
     * Go to yahoo and "Download" into this file.
     */
    static
    {
        File file = new File(RosteringConstants.YAHOO_FILENAME);
        if(file.exists())
        {
            StringBuffer buf = FileUtils.readFile( file, false);
            String str = buf.toString();
            fromYahooEmails = Utils.getListFromString(str);
            //Print.pr( fromYahooEmails);
            //Err.pr( "Num yahoo vols: " + fromYahooEmails.size());
        }
        else
        {
            fromYahooEmails = null;
        }
    }

    public static void setDataStore(DataStore ds)
    {
        dataStore = ds;
    }

    public static MonthTransferObj getGlobalCurrentMonth()
    {
        return getGlobalCurrentParticularRoster().getCurrentMonth();
    }
    
    public static void setGlobalRostererSession(RostererSessionI globalRostererSession)
    {
        RosterSessionUtils.globalRostererSession = globalRostererSession;
    }

    public static void setGlobalCurrentParticularRoster(ParticularRosterI rm)
    {
        globalCurrentParticularRoster = rm;
    }
    
    public static ParticularRosterI getGlobalCurrentParticularRoster()
    {
        ParticularRosterI result = globalCurrentParticularRoster;
        if(result == null)
        {
            Err.error( "Functions for which do not know what the current month is are best put into RostererSessionI");
            /*
            * Have not yet done a roster yet the user is asking for a report. Not all reports
            * require a current month and for these functions ParticularRoster works fine
            * without one. In these cases we still need to get to the data and the difficulty
            * is knowing what type of connection to construct. We have chosen to use a static
            * and error here if it has not been set.
            */
            if(dataStore == null)
            {
                Err.error("No dataStore in RosterSessionUtils when about to create a ParticularRoster");
            }
            result = ParticularRosterFactory.newParticularRoster( "Global Current Particular Roster 2");
            result.init( getGlobalRostererSession(), dataStore);
            globalCurrentParticularRoster = result;
        }
        return result;
    }
    
    public static RostererSessionI getGlobalRostererSession()
    {
        RostererSessionI result = globalRostererSession;
        if(result == null)
        {
            if(dataStore == null)
            {
                Err.error("No dataStore in RosterSessionUtils when about to create a RostererSession");
            }
            result = RostererSessionFactory.newRostererSession( RostererSessionFactory.WITH_SERVICE);
            result.init(dataStore);
            globalRostererSession = result;
        }
        return result;
    }

    public static List findNightsFromShifts(List shifts)
    {
        List result = new ArrayList();
        for(Iterator iter = shifts.iterator(); iter.hasNext();)
        {
            ParticularShift shift = (ParticularShift) iter.next();
            if(!result.contains(shift.getNight()))
            {
                result.add(shift.getNight());
            }
        }
        return result;
    }
    
    public static LookupsI initLookups( DataStore dataStore)
    {
        return initLookups( dataStore, null);
    }

    public static LookupsI initLookups( RostererSessionI currentBO)
    {
        return initLookups( null, currentBO);
    }
    
    /**
     * The only way to getLookups() is thru a BO. This
     * method is only ever called once, as the lookups for an
     * application are attached to datastore
     */
    private static LookupsI initLookups( DataStore dataStore, RostererSessionI currentBO)
    {
        LookupsI result;
        if(currentBO == null)
        {
            currentBO = RostererSessionFactory.newRostererSession( RostererSessionFactory.WITH_SERVICE);
            currentBO.setName( "Only used for getting lookups");
            currentBO.init( dataStore);
        }
        result = currentBO.getLookups();
        Assert.notNull( result, "Expected to find a WombatLookups DO in dataStore <" + currentBO.getDataStore().getName() + ">");
        return result;
    }
            
    public static void setMemoryProperty( String key, String value)
    {
        propertiesHolder.setMemoryProperty( key, value);
    }
    
    public static String getProperty( String key)
    {
        return INSTANCE.getPropertyValue( key, false);
    }
    
    public static String getProperty( String key, boolean nullOk)
    {
        return INSTANCE.getPropertyValue( key, nullOk);
    }
    
    private String getPropertyValue( String key, boolean nullOk)
    {
        //return PropertyUtils.getProperty( key, properties);
        if(propertiesHolder.getProperties() == null)
        {
            String propFileName = WombatConstants.PROPERTY_FILE_NAME;
            //Err.pr( "Using props file: " + propFileName);
            //Err.pr( "Classpath is: " + System.getProperty( "java.class.path"));
            //Err.pr( "Boot Classpath is: " + System.getProperty( "sun.boot.class.path"));
            //Print.prMap( System.getProperties());
            Properties props = PropertyUtils.getPortableProperties(propFileName, this, true);
            if(props == null)
            {
                Err.pr( "A possibly (if not using simple file) required jar file was not found in the classpath: <" + System.getProperty( "java.class.path") + ">");
                props = PropertyUtils.getProperties(propFileName, true);
                if(props == null)
                {
                    Err.pr( "Property needed: <" + key + ">");
                    Err.error("Cannot find <" + propFileName + "> as a 'runtime available' resource or as a simple file");
                }
            }
            propertiesHolder.setProperties( props);
        }
        return propertiesHolder.getValue( key, nullOk);
    }
    
}
