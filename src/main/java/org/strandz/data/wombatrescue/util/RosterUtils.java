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
package org.strandz.data.wombatrescue.util;

import org.strandz.data.wombatrescue.objects.BuddyManager;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.BuddyManagerI;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Contains static rostering methods that are called from both the server and the client sides
 */
public class RosterUtils
{
    public static Comparator SEARCH_BY = new SearchByComparator();
    
    /*
     * Duplicating this:
     * "orderBy ascending"
     */
    public static int workerCf(Object one, Object two)
    {
        int result = -99; //not relevant
        if(!(one instanceof WorkerI))
        {
            return 1;
        }
        if(!(two instanceof WorkerI))
        {
            return 1;
        }
        WorkerI vol1 = (WorkerI) one;
        WorkerI vol2 = (WorkerI) two;
        result = Utils.compareTo( vol1.getOrderBy(), vol2.getOrderBy());
        /*
        String text1 = vol1.getSurname();
        String text2 = vol2.getSurname();
        result = Utils.compareTo(text1, text2);
        if(result == 0)
        {
            text1 = vol1.getChristianName();
            text2 = vol2.getChristianName();
            result = Utils.compareTo(text1, text2);
        }
        if(result == 0)
        {
            text1 = vol1.getGroupName();
            text2 = vol2.getGroupName();
            result = Utils.compareTo(text1, text2);
        }
        */
        return result;
    }
    
    public static class SearchByComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            return workerCf( o1, o2);
        }
    }
    public static boolean contains(WorkerI vol, List<WorkerI> vols, SdzEntityManagerI em, boolean memory)
    {
        boolean result = false;
        /*
        if(vol.getTo().equals( "Tanith Rule") && ((Worker)vols.get( 0)).getTo().equals( "Tanith Rule"))
        {
          Err.debug();
        }
        */
        if(!memory)
        {
            if(vols.isEmpty())
            {
                chkHasPM( vol, em);
            }
            else
            {
                chkBothHavePMs( vol, vols.get( 0), em);
            }
        }
        if(vols.contains(vol))
        {
            result = true;
        }
        return result;
    }

    /**
     * Important to call this method to ensure app is not dealing with DOs that
     * may have come across from Spring.
     * 
     * @param worker1
     * @param worker2
     */
    private static void chkBothHavePMs( WorkerI worker1, WorkerI worker2, SdzEntityManagerI em)
    {
        if(em == null)
        {
            //This application will never use file-based storage
            Err.error( "Cannot compare two workers that are not entity managed");
        }
        chkHasPM( worker1, em);
        chkHasPM( worker2, em);
        /*
        PersistenceManager em1 = JDOHelper.getPersistenceManager( worker1);
        PersistenceManager em2 = JDOHelper.getPersistenceManager( worker2);
        if(!(em1 == null && em2 == null))
        {
            if(!(em1 != null && em2 != null))
            {
                if(em1 == null)
                {
                    Err.error( "worker1 " + worker1 + " does not have a pm");
                }
                else if(em2 == null)
                {
                    Err.error( "worker2 " + worker2 + " does not have a pm");
                }
            }
        }
        */
    }
    
    private static void chkHasPM( WorkerI worker, SdzEntityManagerI em)
    {
        if(!em.isEntityManaged( worker))
        {
            Err.error( "worker " + worker + " is not entity managed");
        }    
    }
        
    public static BuddyManagerI getOppositeBM(BuddyManagerI bm, List<BuddyManagerI> buddyManagers)
    {
        BuddyManagerI result = null;
        for(Iterator iterator = buddyManagers.iterator(); iterator.hasNext();)
        {
            BuddyManagerI buddyManager = (BuddyManagerI) iterator.next();
            if(!buddyManager.equals(bm) && buddyManager.getDayInWeek().equals(bm.getDayInWeek()))
            {
                result = buddyManager;
                break;
            }
        }
        if(result == null)
        {
            Err.error("Could not find a Buddy Manager for " + bm.getDayInWeek() + " to partner " + bm.getWorker());
        }
        return result;
    }
    
    /**
     * This could be more generic obviously
     * 
     * @param fileName
     * @return
     */
    public static String translateToURL( String fileName)
    {
        String result = null;
        //file:///C:/temp/current_roster.html
        //WINDOWS_CURRENT_FILENAME
        //"C:/temp/current_roster.html"
        if(fileName.equals( RosteringConstants.WINDOWS_CURRENT_FILENAME))
        {
            result = "file:///C:/temp/roster.html";
        }
        else if(fileName.equals( RosteringConstants.WINDOWS_JUST_GONE_FILENAME))
        {
            result = "file:///C:/temp/old_roster.html";
        }
        else if(fileName.equals( RosteringConstants.UNIX_CURRENT_FILENAME)) 
        {
            result = "www.strandz.org/teresahouse/roster.html";
        }
        else if(fileName.equals( RosteringConstants.UNIX_JUST_GONE_FILENAME))
        {
            result = "www.strandz.org/teresahouse/old_roster.html";
        }
        else
        {
            Err.error( "Do not know how to translate <" + fileName + ">");
        }
        return result;
    }
}
