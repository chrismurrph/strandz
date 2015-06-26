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

import org.strandz.data.wombatrescue.objects.BuddyManager;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

public class PopulateBuddyManager
{
    private static EntityManagedDataStore dataStore;
    private static DomainQueriesI queriesI;
    private static boolean commitPopulation = true;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            //String str[] = { "POJOWombatData", WombatConnectionEnum.CURRENTDATABASE.getName()};
            String str[] = {"POJOWombatData", WombatConnectionEnum.DEBIAN_PROD_TERESA.getName()};
            processParams(str);
        }
        System.exit(0);
    }

    private static void processParams(String s[])
    {
         /**/
        if(s[0].equals("POJOWombatData"))
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
                dataStore = dataStoreFactory.getEntityManagedDataStore();
            }
            else
            {
                Err.error();
            }
            dataStore.startTx();
            doPopulation();
            if(!commitPopulation)
            {
                dataStore.rollbackTx();
            }
            else
            {
                dataStore.commitTx();
            }
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void doPopulation()
    {
        queriesI = dataStore.getDomainQueries();
        WombatLookups wombatLookups = (WombatLookups)queriesI.executeRetObject( WombatDomainQueryEnum.LOOKUPS);
        DayInWeek mon = (DayInWeek) wombatLookups.getByName(WombatDomainLookupEnum.ALL_DAY_IN_WEEK, "Monday");
        DayInWeek tue = (DayInWeek) wombatLookups.getByName(WombatDomainLookupEnum.ALL_DAY_IN_WEEK, "Tuesday");
        DayInWeek wed = (DayInWeek) wombatLookups.getByName(WombatDomainLookupEnum.ALL_DAY_IN_WEEK, "Wednesday");
        DayInWeek fri = (DayInWeek) wombatLookups.getByName(WombatDomainLookupEnum.ALL_DAY_IN_WEEK, "Friday");
        DayInWeek sat = (DayInWeek) wombatLookups.getByName(WombatDomainLookupEnum.ALL_DAY_IN_WEEK, "Saturday");
        //
        WhichShift dinner = (WhichShift) wombatLookups.getByName(WombatDomainLookupEnum.ALL_WHICH_SHIFT, "dinner");
        WhichShift overnight = (WhichShift) wombatLookups.getByName(WombatDomainLookupEnum.ALL_WHICH_SHIFT, "overnight");
        //
        createBuddyManager("Chris", "Murphy", mon, dinner);
        createBuddyManager("Marty", "Moss", mon, overnight);
        createBuddyManager("Daniel", "Koo", tue, dinner);
        createBuddyManager("Mike", "Daughton", tue, overnight);
        createBuddyManager("Marty", "Moss", wed, dinner);
        createBuddyManager("Nick", "Airley", wed, overnight);
        createBuddyManager("Steve", "Ristau", fri, dinner);
        createBuddyManager("Ken", "Pritchett", fri, overnight);
        createBuddyManager("Rob", "Hartman", sat, dinner);
        createBuddyManager("Chloe", "Scott", sat, overnight);
        //
    }

    private static void createBuddyManager(
        String n1, String n2, DayInWeek dayInWeek, WhichShift whichshift)
    {
        createBuddyManager(commitPopulation, n1, n2, dayInWeek, whichshift);
    }

    private static void createBuddyManager(boolean commitPopulation,
                                           String n1, String n2, DayInWeek dayInWeek, WhichShift whichshift)
    {
        Worker vol = (Worker) queriesI.executeRetObject(
            WombatDomainQueryEnum.WORKER_BY_NAMES, n1, n2);
        //queryWorker( n1, n2);
        if(vol == null)
        {
            Err.error("Could not find a volunteer called " + n1 + " " + n2);
        }
        BuddyManager buddyManager = new BuddyManager();
        buddyManager.setDayInWeek(dayInWeek);
        buddyManager.setWhichShift(whichshift);
        buddyManager.setWorker(vol);
        if(commitPopulation) dataStore.getEM().registerPersistent(buddyManager);
    }
}
