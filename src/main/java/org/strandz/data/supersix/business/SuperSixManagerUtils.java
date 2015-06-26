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
package org.strandz.data.supersix.business;

import org.strandz.util.applic.supersix.SuperSixConstants;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.PropertiesHolder;
import org.strandz.lgpl.util.PropertyUtils;

import java.util.Properties;

public class SuperSixManagerUtils
{
    private static SuperSixManager globalCurrentSuperSixManager;
    private static DataStore dataStore;
    private static PropertiesHolder propertiesHolder = new PropertiesHolder();
    private static SuperSixManagerUtils INSTANCE = new SuperSixManagerUtils();     
    private static CompetitionSeason currentCompetitionSeason;
//    private static Lookups lookups;

    public static void setDataStore(DataStore ds)
    {
        dataStore = ds;
    }

    public static SuperSixManagerI getGlobalCurrentSuperSixManager()
    {
        SuperSixManager result = globalCurrentSuperSixManager;
        if(result == null)
        {
            if(dataStore == null)
            {
                Err.error("SuperSixManager does not have a dataStore");
            }
            result = new SuperSixManager();
            result.init( dataStore);
            globalCurrentSuperSixManager = result;
        }
        return result;
    }

    public static CompetitionSeason getCurrentCompetitionSeason()
    {
        return currentCompetitionSeason;
    }

    public static void setCurrentCompetitionSeason(CompetitionSeason currentCompetitionSeason)
    {
        if(currentCompetitionSeason == null)
        {
            Err.error( "currentCompetitionSeason == null");
        }
        //User does this through changing globals 
//        if(SuperSixManagerUtils.currentCompetitionSeason != null)
//        {
//            Err.error( "setCurrentSeason() has already been called");
//        }
        SuperSixManagerUtils.currentCompetitionSeason = currentCompetitionSeason;
    }
    
    public static SuperSixLookups initLookups( DataStore dataStore)
    {
        return initLookups( dataStore, null);
    }

    public static SuperSixLookups initLookups( SuperSixManagerI currentBO)
    {
        return initLookups( null, currentBO);
    }
        
    /**
     * The only way to getLookups() is thru a BO
     */
    private static SuperSixLookups initLookups( DataStore dataStore, SuperSixManagerI currentBO)
    {
        SuperSixLookups result;
        if(currentBO == null)
        {
            currentBO = getGlobalCurrentSuperSixManager();
            currentBO.init( dataStore);
        }
        result = currentBO.getLookups();
        Assert.notNull( result, "No lookups found in dataStore <" + currentBO.getDataStore().getName() + ">");
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
            String propFileName = SuperSixConstants.PROPERTY_FILE_NAME;
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

//    public static Lookups getLookups()
//    {
//        return lookups;
//    }
//
//    public static void setLookups(Lookups lookups)
//    {
//        if(lookups == null)
//        {
//            Err.error( "lookups == null");
//        }
//        if(SuperSixManagerUtils.lookups != null)
//        {
//            Err.error( "setLookups() has already been called");
//        }
//        SuperSixManagerUtils.lookups = lookups;
//        chkLookups();
//    }
//
//    private static void chkLookups()
//    {
//        List days = lookups.getDayInWeeks();
//        if(days.size() != 8)
//        {
//            Err.error( "Expect 8 days, got " + days.size());
//        }
//        DayInWeek firstDay = (DayInWeek)days.get(0);
//        Object id = JDOHelper.getObjectId( firstDay);
//        if(id == null)
//        {
//            //Hmm - not if running in memory
//            //Err.error( "Expect lookups have set to be persistence capable");
//        }
//    }
}
