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

import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.data.wombatrescue.calculated.CayenneWombatLookups;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Print;
import org.strandz.store.wombatrescue.CayenneWombatDemoData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import java.util.List;

/**
 * This will create a fresh load of data.
 * Use the Modeler to delete data if need to use this over.
 * If your database is not 'brand new' then drop all the tables first using Cayenne Modeler.
 */
public class PopulateForCayenne
{
    private CayenneWombatDemoData demoDataCayenne;
    private EntityManagedDataStore dataStore;

    private static final boolean COMMIT_POPULATION = true;

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
        PopulateForCayenne obj = new PopulateForCayenne();
        obj.populate();
    }

    private void populate()
    {
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection( WombatConnectionEnum.SERVER_CAYENNE);
        dataStore = dataStoreFactory.getEntityManagedDataStore();
        demoDataCayenne = CayenneWombatDemoData.getInstance( ORMTypeEnum.CAYENNE_SERVER, dataStore);
        dataStore.startTx();
        demoDataCayenne.doPopulation();
        populateFromDemoData();
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
     * Setup data so will give a full roster no matter which month, no matter what time
     */
    private void populateFromDemoData()
    {
        SdzEntityManagerI pm = dataStore.getEM();
        pm.registerPersistentAll(demoDataCayenne.newWorkers);
        pm.registerPersistentAll(demoDataCayenne.newBMs);
        pm.registerPersistentAll(demoDataCayenne.newRSs);
        pm.registerPersistentAll(demoDataCayenne.newUsers);
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_DAY_IN_WEEK));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_FLEXIBILITY));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_INTERVAL));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_FLEXIBILITY));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_SEX));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_WEEK_IN_MONTH));
        pm.registerPersistentAll(demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_WHICH_SHIFT));
        Err.pr("Population complete, have added " + demoDataCayenne.newWorkers.size() +
            " Workers and " + demoDataCayenne.newRSs.size() +
            " RosterSlots and " + demoDataCayenne.newUsers.size() +
            " UserDetails and " + demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY).size() +
            " Seniorities and " + demoDataCayenne.newBMs.size() +
            " BuddyManagers to the database");
        Print.prList( demoDataCayenne.wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY), "seniorities");
    }
}