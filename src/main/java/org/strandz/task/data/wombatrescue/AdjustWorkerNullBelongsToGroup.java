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
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.util.Err;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.WombatConnections;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;

import java.util.Iterator;
import java.util.List;

public class AdjustWorkerNullBelongsToGroup
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
            String str[] = {"POJOWombatData", WombatConnections.DEFAULT_DATABASE.getName()};
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
            if(WombatConnectionEnum.getFromName(s[1]).isProduction())
            {
                Err.error(
                    "Cannot work with the " + WombatConnectionEnum.getFromName(s[1])
                        + " database");
            }
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
            update(dataStore);
            dataStore.commitTx();
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void update(DataStore data)
    {
        List list = (List) data.get(POJOWombatData.WORKER);
        DomainQueriesI queriesI = (DomainQueriesI) dataStore.getDomainQueries();
        Worker nullWorker = (Worker) queriesI.executeRetObject(WombatDomainQueryEnum.NULL_WORKER);
        //queryNullWorker();
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Worker worker = (Worker) iter.next();
            if(worker.getBelongsToGroup() == null)
            {
                Err.pr(worker + " has bad belongsToGroup");
                worker.setBelongsToGroup(nullWorker);
            }
            else
            {
                Err.pr(worker + " has good belongsToGroup");
            }
        }
    }
}
