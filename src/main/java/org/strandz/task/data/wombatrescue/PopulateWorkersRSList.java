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

import org.strandz.data.wombatrescue.objects.RosterSlot;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.DomainQueryI;
import org.strandz.lgpl.util.Err;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;

import java.util.Iterator;
import java.util.List;

/**
 * New bi-directional relationship needs population
 * This method will populate worker.rosterSlots with the values
 * for rosterSlot.worker
 */
public class PopulateWorkersRSList
{
    private static DataStore dataStore;

    private static boolean commitPopulation = true;

    public static void main(String s[])
    {
        PopulateWorkersRSList obj = new PopulateWorkersRSList();
        if(s.length != 0)
        {
            obj.processParams(s);
        }
        else
        {
            String str[] = {WombatConnectionEnum.LINODE_PROD_TERESA.getName()};
            obj.processParams(str);
        }
        System.exit(0);
    }

    private void processParams(String s[])
    {
        if(s.length != 1)
        {
            Err.error("Need to explicitly specify a WombatConnectionEnum database, and that's all");
        }
        /*
        * Comment out below when you are sure you want to do it on a
        * PROD DB.
        */
//    if(WombatConnectionEnum.getFromName( s[0]).isProduction())
//    {
//      Err.error(
//          "Cannot work with the " + WombatConnectionEnum.getFromName( s[0])
//          + " database");
//    }
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[0]));
        dataStore = dataStoreFactory.getDataStore();
        dataStore.startTx();
        doPopulation(dataStore);
        if(!commitPopulation)
        {
            dataStore.rollbackTx();
        }
        else
        {
            dataStore.commitTx();
        }
    }

    /**
     * Query all the RosterSlots
     * For each one get the worker
     * Then on worker call its NEW method addRosterSlot
     * (which also sets the worker which shouldn't matter)
     *
     * @param dataStore
     */
    void doPopulation(DataStore dataStore)
    {
        DomainQueriesI queriesI = (DomainQueriesI) dataStore.getDomainQueries();
        DomainQueryI q = queriesI.get(WombatDomainQueryEnum.ALL_ROSTER_SLOT);
        List rosterSlots = (List) q.execute();
        for(Iterator iterator = rosterSlots.iterator(); iterator.hasNext();)
        {
            RosterSlot rosterSlot = (RosterSlot) iterator.next();
            WorkerI worker = rosterSlot.getWorker();
            worker.addRosterSlot(rosterSlot);
            Err.pr("To " + worker.getToLong() + " with " + worker.getRosterSlots().size() + " will add " + rosterSlot);
        }
    }
}
