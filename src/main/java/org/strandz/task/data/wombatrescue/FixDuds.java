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

import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;

import java.util.Iterator;
import java.util.List;

/**
 * A dud is defined as having a toString() which returns null, which is not a dummy
 * (isDummy() == false). If ever find any, make sure they are not referenced elsewhere.
 * For example every worker has a belongsToGroup, which is also a worker.
 */
public class FixDuds
{
    private static EntityManagedDataStore dataStore;
    private static boolean commitPopulation = false;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"POJOWombatData", WombatConnectionEnum.DEBIAN_DEV_TERESA.getName()};
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
            if(WombatConnectionEnum.getFromName(s[1]).isProduction() && commitPopulation == true)
            {
                Err.error(
                    "Cannot work with the " + WombatConnectionEnum.getFromName(s[1])
                        + " database");
            }
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
            update(dataStore);
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

    private static void update(EntityManagedDataStore data)
    {
        List list = ((DomainQueriesI) data.getDomainQueries()).executeRetList(
            WombatDomainQueryEnum.ALL_WORKER);
        //queryAllWorkers();
        // List list = (List)data.get( POJOWombatData.WORKER);
        int timesDudFound = 0;
        int timesDummyFound = 0;
        Worker dud = null;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Worker vol = (Worker) iter.next();
            if(vol == null)
            {
                Err.error("The DB should never contain a null worker");
            }
            else if(vol.isDummy())
            {
                timesDummyFound++;
            }
            else if(!vol.isDummy() && vol.toString() == null)
            {
                //Err.pr( "A dud worker exists");
                dud = vol;
                //Only way to delete when have > 1
                //data.getPM().deletePersistent( dud);
                timesDudFound++;
            }
        }
        Err.pr("Have found " + timesDudFound + " dud workers");
        Err.pr("And " + timesDummyFound + " dummy workers (expect 0 as now doing query)");
        if(commitPopulation && timesDudFound == 1)
        {
            Err.error("Don't actually do this until we know where the bud is used");
            Err.pr("To delete the dud, so won't see next time run");
            data.getEM().deletePersistent(dud);
        }
    }
}
