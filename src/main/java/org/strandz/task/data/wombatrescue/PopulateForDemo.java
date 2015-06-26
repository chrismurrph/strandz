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

import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Print;
import org.strandz.store.wombatrescue.CayenneWombatDemoData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.store.wombatrescue.WombatDemoData;

import java.util.List;

/**
 * This will create a fresh load of data. If any data already exists it
 * will be replaced.
 * See jarPopulateWombatForDemo.xml for how to run this on the server.
 */
public class PopulateForDemo
{
    private WombatDemoData demoDataCayenne = WombatDemoData.getInstance();
    private EntityManagedDataStore dataStore;
    private DomainQueriesI queries;

    private static final boolean COMMIT_POPULATION = true;
    private static final boolean FLUSH_ALLOWED = true;
    public static final boolean KODO = false;
    public static final boolean PROD = false;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            Err.error("No command line parameters are expected for PopulateForDemo");
        }
        else
        {
            main();
        }
        System.exit(0);
    }

    public static void main()
    {
        PopulateForDemo obj = new PopulateForDemo();
        String conName;
        if(!PROD)
        {
            if(KODO)
            {
                conName = WombatConnectionEnum.WOMBAT_KODO.getName();
            }
            else
            {
                conName = WombatConnectionEnum.SERVER_CAYENNE.getName();
            }
        }
        else
        {
            conName = WombatConnectionEnum.LINODE_PROD_TERESA.getName();
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
        if(WombatConnectionEnum.getFromName(s[0]).isProduction())
        {
            Err.error(
                "Cannot work with the " + WombatConnectionEnum.getFromName(s[0])
                    + " database");
        }
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[0]));
        dataStore = dataStoreFactory.getEntityManagedDataStore();
        queries = dataStore.getDomainQueries();
        dataStore.startTx();
        doPopulation();
        if(!COMMIT_POPULATION)
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
        //fillLookupTables();
        //demoData.doPopulation();
        pm.registerPersistentAll(demoDataCayenne.newWorkers);
        //Print.prList( demoData.newWorkers, "New workers, includes Noreen? ");
        pm.registerPersistentAll(demoDataCayenne.newBMs);
        pm.registerPersistentAll(demoDataCayenne.newRSs);
        pm.registerPersistentAll(demoDataCayenne.newUsers);
        //(JPOX: javax.jdo.JDOUserException: Class java.util.Arrays$ArrayList not supported as a Second-Class object)
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_DAY_IN_WEEK));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_FLEXIBILITY));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_INTERVAL));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_FLEXIBILITY));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_SEX));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_WEEK_IN_MONTH));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_WHICH_SHIFT));
        pm.registerPersistent(demoDataCayenne.wombatLookups);
        //Don't need to do registerPersistentAll for individual lookups, as above does by reachability
        Err.pr("Population complete, have added " + demoDataCayenne.newWorkers.size() +
            " Workers and " + demoDataCayenne.newRSs.size() +
            " RosterSlots and " + demoDataCayenne.newUsers.size() +
            " UserDetails and " + demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY).size() +
            " Seniorities and " + demoDataCayenne.newBMs.size() +
            " BuddyManagers to the database");
        Print.prList( demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY), "seniorities");
    }

    private boolean isEmpty()
    {
        boolean result = false;
        WombatLookups wombatLookups = (WombatLookups)queries.executeRetObject( WombatDomainQueryEnum.LOOKUPS);
        List weekInMonths = null;
        if(wombatLookups != null)
        {
            //RosterSessionUtils.setLookups( lookups);
            weekInMonths = wombatLookups.get( WombatDomainLookupEnum.ALL_WEEK_IN_MONTH);
        }
        //List weekInMonth = queries.executeRetList(WombatDomainQueryEnum.ALL_WEEK_IN_MONTH);
        List workers = queries.executeRetList(WombatDomainQueryEnum.ALL_WORKER);
        //TODO s/be able to ask DataStore if it is empty
        if(!Utils.isBlank( weekInMonths) || !workers.isEmpty())
        {
            if(!FLUSH_ALLOWED)
            {
                Err.error("flushAllowed must be called before PopulateForDemo will flush first");
            }
        }
        else
        {
            Err.pr("As have " + workers.size() + " workers, and null or no weekInMonths, are assuming DB empty");
            result = true;
        }
        return result;
    }
}
