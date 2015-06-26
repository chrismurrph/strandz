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

import org.strandz.data.wombatrescue.objects.*;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.util.Err;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.data.wombatrescue.util.RosterUtils;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import javax.jdo.JDOHelper;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

public class DisplayDOsList
{
    private static DataStore dataStore;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"DevData", WombatConnectionEnum.SERVER_CAYENNE.getName()};
            processParams(str);
        }
        System.exit(0);
    }

    private static void processParams(String s[])
    {
         /**/
        if(s[0].equals("DevData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database");
            }
//      if(WombatConnectionEnum.getFromName( s[1]).isProduction())
//      {
//        Err.error(
//            "Cannot work with the " + WombatConnectionEnum.getFromName( s[1])
//            + " database");
//      }
            if(s.length == 2)
            {
                DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
                dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[1]));
                dataStore = dataStoreFactory.getDataStore();
            }
            else
            {
                Err.error();
            }
            dataStore.startTx();
            displayWorker(dataStore);
            dataStore.commitTx();
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void displayRosterSlot(DataStore data)
    {
        List list = (List) data.get(POJOWombatData.ROSTER_SLOT);
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            RosterSlot rosterSlot = (RosterSlot) iter.next();
            Err.pr("rosterSlot: " + rosterSlot);
            //Err.pr( "dayInWeek: " + rosterSlot.getDayInWeek());
            //Err.pr( "interval: " + rosterSlot.getInterval());
            Err.pr("monthlyRestart: " + rosterSlot.isMonthlyRestart());
            Err.pr("weekInMonth: " + rosterSlot.getWeekInMonth());
            Err.pr("worker: " + rosterSlot.getWorker());
            Err.pr("");
        }
    }

    private static void displayFlexibility(DataStore data)
    {
        List list = (List) data.get(POJOWombatData.FLEXIBILITY);
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Flexibility flexibility = (Flexibility) iter.next();
            Object id = JDOHelper.getObjectId(flexibility);
            Err.pr("flexibility name: " + flexibility + " has oid " + id);
            Err.pr("");
        }
    }

    public static void displayWhichShift(DataStore data)
    {
        //List list = (List)data.get( POJOWombatData.WHICH_SHIFT);
        //DomainQueriesI queriesI = data.getDomainQueries();
        //List list = RosterSessionUtils.getLookups().getWhichShifts(); 
                //queriesI.executeRetList(WombatDomainQueryEnum.ALL_WHICH_SHIFT);
        WombatLookups wombatLookups = (WombatLookups)dataStore.getLookups();
        List list = wombatLookups.get( WombatDomainLookupEnum.ALL_WHICH_SHIFT);
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            WhichShift whichShift = (WhichShift) iter.next();
            Object id = JDOHelper.getObjectId(whichShift);
            Err.pr("whichShift name: " + whichShift + " has oid " + id);
            Err.pr("");
        }
        Object id = JDOHelper.getObjectId(WhichShift.NULL);
        Err.pr("NULL whichShift has oid " + id);
        Err.pr("");
    }

    //this filter returning none back, why?
    //String filter = "unknown == false && belongsToGroup.dummy == true && dummy != true";
    private static void displayWorker(DataStore data)
    {
        DomainQueriesI queriesI = data.getDomainQueries();
        List list = queriesI.executeRetList(WombatDomainQueryEnum.ROSTERABLE_WORKERS);
        Collections.sort( list, RosterUtils.SEARCH_BY);
        int haveSex = 0;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Worker worker = (Worker) iter.next();
            if(worker.getSex() == null)
            {
               Err.error( "Bad error that a worker's sex is null: " + worker.getToLong());
            }
            //if(worker.getTo() == null || worker.getTo().equals( ""))
            //if(worker.getFlexibility() != null && !worker.getFlexibility().equals( Flexibility.NULL))
            if(worker.getSex() != null && !worker.getSex().equals( Sex.NULL))
            {
                if(!worker.getSex().equals(Sex.NULL))
                {
                    haveSex++;
                }
                Err.pr("worker: " + worker.getToLong());
                Err.pr("\tdummy: " + worker.isDummy());
                Err.pr("\tsex: " + worker.getSex());
                if(worker.getGroupName() != null)
                {
                    Err.pr("\tgroupName: " + worker.getGroupName());
                }
            }
        }
        Err.pr("Print of " + list.size() + " workers, " +
            haveSex + " have sex in DB " + data.getName());
    }

    /*
     * Can no longer use POJOWombatData.VOLUNTEER
    private static void displayVolunteer( DataStore data)
    {
      List list = (List)data.get( POJOWombatData.VOLUNTEER);
      Collections.sort( list);
      for(Iterator iter = list.iterator(); iter.hasNext();)
      {
        Volunteer vol = (Volunteer)iter.next();
        Err.pr( "vol: " + vol);
  //      Err.pr( "unknown: " + worker.isUnknown());
  //      Err.pr( "belongsToGroup.dummy: " + worker.getBelongsToGroup().isDummy());
  //      Err.pr( "dummy: " + worker.isDummy());
        Err.pr( "isFlexible: " + vol.isFlexible());
        Err.pr( "");
      }
    }
    */

    private static void displayBM(DataStore data)
    {
        DomainQueriesI queriesI = (DomainQueriesI) data.getDomainQueries();
        List list = queriesI.executeRetList(WombatDomainQueryEnum.BUDDY_MANAGERS);
        //queryBuddyManagers();
        //Collections.sort( list);
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            BuddyManager bm = (BuddyManager) iter.next();
            Err.pr("bm: " + bm);
            //Err.pr( "\tvol: " + bm.getVolunteer());
            Err.pr("\twker: " + bm.getWorker());
            Err.pr("");
        }
    }

}
