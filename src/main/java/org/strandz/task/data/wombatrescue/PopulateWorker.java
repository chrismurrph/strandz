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

import org.strandz.data.wombatrescue.objects.Volunteer;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.WombatConnections;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import java.util.Iterator;
import java.util.List;

public class PopulateWorker
{
    private static EntityManagedDataStore dataStore;
    private static boolean commitPopulation = true;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"DevData", WombatConnections.DEFAULT_DATABASE.getName()};
            //String str[] = { "DevData", WombatConnectionEnum.DEBIAN_PROD_TERESA,  WombatrescueApplicationData.JDO};
            processParams(str);
        }
        System.exit(0);
    }

    public static void processParams(String s[])
    {
        if(s[0].equals("DevData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database");
            }
            /*
            * Dangerous to comment out below
            */
            if(s[1].equals(WombatConnectionEnum.PROD))
            {
                Err.error("Cannot work with the " + WombatConnectionEnum.getFromName(s[1]) +
                    " database");
            }
             /**/
            if(s.length == 2)
            {
                DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
                dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[1]));
                dataStore = dataStoreFactory.getEntityManagedDataStore();
            }
            else
            {
                //td = WombatDataFactory.getNewInstance( WombatConnectionEnum.getFromName( s[1]), s[2]);
            }
            //queriesI = (WombatrescueQueriesI) td.getData();
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
        DomainQueriesI queriesI = (DomainQueriesI) dataStore.getDomainQueries();
        Err.error("queryAllVolunteers() has gone");
        /*
        List vols = (List)queriesI.queryAllVolunteers();
        List flexibilityList = (List) td.getData().get( POJOWombatData.FLEXIBILITY );
        //
        List workers = new ArrayList();
        for (Iterator iterator = vols.iterator(); iterator.hasNext();)
        {
          Volunteer vol = (Volunteer) iterator.next();
          workers.add( createWorker( vol, flexibilityList));
        }
        //Now can do belongsToGroup stuff using findWorkerFromVolunteer:
        //Find every Volunteer that has a belongsToGroup that is not NULL
        //Thus find the two Workers involved and do the setBelongsToGroup operation
        for (Iterator iterator = vols.iterator(); iterator.hasNext();)
        {
          Volunteer vol = (Volunteer) iterator.next();
          if(vol.getBelongsToGroup() == null)
          {
            Err.error( "vol.getBelongsToGroup() == null for" + vol.getTo());
          }
          else if(vol.getBelongsToGroup().equals( Volunteer.NULL))
          {
            //nufin
            //Actually s/have worker.setBelongsToGroup( Worker.NULL);
            //As didn't have ended up with workers where belongsToGroup
            //is null needed to write AdjustWorkerNullBelongsToGroup
          }
          else
          {
            Volunteer group = vol.getBelongsToGroup();
            Worker worker = findWorkerFromVolunteer( vol, workers);
            Worker workerGroup = findWorkerFromVolunteer( group, workers);
            worker.setBelongsToGroup( workerGroup);
            Err.pr( "Assigned group " + workerGroup + " to worker " + worker);
          }
        }
        //Volunteer vol = queriesI.queryNullVolunteer();
        //Don't do this, instead MakeNullWorker
        //createWorker( vol);
        */
    }

//    private static Worker createWorker(Volunteer volunteer, List flexibilityList)
//    {
//        if(volunteer == null)
//        {
//            Err.error("Can only create a Worker from a Volunteer");
//        }
//        Worker worker = new Worker(volunteer, flexibilityList);
//        if(commitPopulation)
//        {
//            dataStore.getEM().registerPersistent(worker);
//        }
//        Err.pr("Created the worker: " + worker.getTo());
//        return worker;
//    }

    static Worker findWorkerFromVolunteer(Volunteer volunteer, List workers)
    {
        Worker result = null;
        for(Iterator iterator = workers.iterator(); iterator.hasNext();)
        {
            Worker worker = (Worker) iterator.next();
            if(worker.getToLong().equals(volunteer.getTo()))
            {
                result = worker;
                break;
            }
        }
        return result;
    }
}
