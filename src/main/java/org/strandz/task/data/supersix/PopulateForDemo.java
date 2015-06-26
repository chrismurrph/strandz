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
package org.strandz.task.data.supersix;

import org.strandz.store.supersix.SuperSixDemoData;
import org.strandz.data.supersix.domain.SuperSixConnectionEnum;
import org.strandz.store.supersix.SuperSixDataStoreFactory;
import org.strandz.data.supersix.domain.SuperSixDomainQueryEnum;
import org.strandz.data.supersix.domain.SuperSixDomainLookupEnum;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.data.supersix.objects.SuperSixLookups;

import java.util.List;

/**
 * This will create a fresh load of data. If any data already exists it
 * will be replaced.
 */
public class PopulateForDemo
{
    private SuperSixDemoData demoData = SuperSixDemoData.getInstance();
    private EntityManagedDataStore dataStore;
    private DomainQueriesI queries;

    private static final boolean COMMIT_POPULATION = true;
    private static final boolean FLUSH_ALLOWED = true;
    private static final boolean KODO = true;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            Err.error("No command line parameters are expected for PopulateForDemo");
        }
        else
        {
            PopulateForDemo.main();
        }
        System.exit(0);
    }

    public static void main()
    {
        PopulateForDemo obj = new PopulateForDemo();
        String conName;
        if(PopulateForDemo.KODO)
        {
            conName = SuperSixConnectionEnum.SUPERSIX_KODO.getName();
        }
        else
        {
            //Not implemented
            conName = SuperSixConnectionEnum.DEMO_SUPERSIX_JPOX.getName();
        }
        String str[] = {conName};
        obj.processParams(str);
    }

    private void processParams(String s[])
    {
        /*
        * Comment out below when you are sure you want to do it on a
        * PROD DB.
        */
        if(SuperSixConnectionEnum.getFromName(s[0]).isProduction())
        {
            Err.error(
                "Cannot work with the " + SuperSixConnectionEnum.getFromName(s[0])
                    + " database");
        }
        DataStoreFactory dataStoreFactory = new SuperSixDataStoreFactory();
        dataStoreFactory.addConnection(SuperSixConnectionEnum.getFromName(s[0]));
        dataStore = dataStoreFactory.getEntityManagedDataStore();
        queries = dataStore.getDomainQueries();
        dataStore.startTx();
        doPopulation();
        if(!PopulateForDemo.COMMIT_POPULATION)
        {
            dataStore.rollbackTx();
        }
        else
        {
            dataStore.commitTx();
        }
    }

    /**
     * Setup data so will give a full roster no matter which month, no matter
     * which TZ
     */
    private void doPopulation()
    {
        SdzEntityManagerI pm = dataStore.getEM();
        if(!isEmpty())
        {
            dataStore.rollbackTx(); //flush checks that not already in a transaction
            dataStore.flush();
            dataStore.startTx(); //flush didn't start another one
        }
        //Will have done when getInstance()
        //demoData.doPopulation();
        //This will be achieved when make lookups persistent
        //fillLookupTables();
        pm.registerPersistentAll(demoData.newGlobals);
        pm.registerPersistentAll(demoData.newTeams);
        pm.registerPersistentAll(demoData.newPlayers);
        pm.registerPersistentAll(demoData.newSeasons);
        pm.registerPersistentAll(demoData.newUsers);
        pm.registerPersistent(demoData.superSixLookups);
        //Don't need to do registerPersistentAll for individual lookups, as above does by reachability
        Err.pr("Population complete, have added " + demoData.newGlobals.size() +
            " Globals and " + demoData.newTeams.size() +
            " Teams and " + demoData.newPlayers.size() +
            " Players and " + demoData.newSeasons.size() +
            " Seasons and " + demoData.newUsers.size() +
            " Users to the database");
    }

    private boolean isEmpty()
    {
        boolean result = false;
        List teams = queries.executeRetList(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM);
        SuperSixLookups superSixLookups = (SuperSixLookups)queries.executeRetObject( SuperSixDomainQueryEnum.LOOKUPS);
        List divisions = null;
        if(superSixLookups != null)
        {
            //SuperSixManagerUtils.setLookups( lookups);
            divisions = superSixLookups.get( SuperSixDomainLookupEnum.DIVISION);
        }
        //queries.executeRetList(SuperSixDomainQueryEnum.DIVISION_CLOSED);
        //TODO s/be able to ask DataStore if it is empty
        if(!teams.isEmpty() || !Utils.isBlank( divisions))
        {
            if(!FLUSH_ALLOWED)
            {
                Err.error("flushAllowed must be called before PopulateForDemo will flush first");
            }
        }
        else
        {
            Err.pr("As have " + teams.size() + " teams, and null or no divisions, are assuming DB empty");
            result = true;
        }
        return result;
    }
}
