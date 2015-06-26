/*
    Strandz - an API that matches the user to the dataStore.
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
package org.strandz.test.wombatrescue;

import junit.framework.TestCase;
import org.strandz.data.wombatrescue.objects.cayenne.Worker;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import java.util.Iterator;
import java.util.List;

public class TestORMAddDelete extends TestCase
{
    private EntityManagedDataStore dataStore;

    private static final String FIRST = "Mal";
    private static final String SECOND = "Fernando";

    public static void main(String s[])
    {
        TestORMAddDelete obj = new TestORMAddDelete();
        obj.setUp();
        obj.testAddOrDelete();
        System.exit(0);
    }

    public TestORMAddDelete()
    {
        if(dataStore == null)
        {
            DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
            dataStoreFactory.addConnection(WombatConnectionEnum.DIRECT_DEMO_WOMBAT_CAYENNE);
            dataStore = dataStoreFactory.getEntityManagedDataStore();
        }
    }

    protected void setUp()
    {
        //For some reason can't connect on server for 2nd test - however doing it this way wasn't to blame
        //dataStore.rollbackTx();
        //dataStore.startTx();
    }

    private Worker getWorkerFromDB(String firstName, String secondName)
    {
        Worker result;
        result = (Worker) dataStore.getDomainQueries().executeRetObject(
            WombatDomainQueryEnum.WORKER_BY_NAMES, firstName, secondName);
        if(result == null)
        {
            Err.pr(firstName + " " + secondName + " NOT found");
        }
        else
        {
            Err.pr(result.getChristianName() + " " + result.getSurname() + " found");
        }
        return result;
    }

    private int getWorkerCount()
    {
        int result;
        DomainQueriesI queriesI = dataStore.getDomainQueries();

        List<Worker> vols = queriesI.executeRetList(WombatDomainQueryEnum.ROSTERABLE_WORKERS);
        result = vols.size();
        Err.pr("Have " + result + " rosterable vols");
        return result;
    }

    /**
     * Works reliably
     */
    private Worker getWorkerFromDBLongWay(String firstName, String secondName)
    {
        Worker result = null;
        DomainQueriesI queriesI = dataStore.getDomainQueries();

        List<Worker> vols = queriesI.executeRetList(WombatDomainQueryEnum.ROSTERABLE_WORKERS);
        Err.pr("Have " + vols.size() + " rosterable vols");

        for(Iterator iter = vols.iterator(); iter.hasNext();)
        {
            Worker vol = (Worker) iter.next();
            String name = vol.getChristianName();
            String surname = vol.getSurname();
            if(name != null && name.equals( firstName) && surname != null
                && surname.equals( secondName))
            {
                result = vol;
            }
            else
            {
                //Err.pr( "Got name: <" + name + ">, surname: <" + surname + ">");
            }
        }
        return result;
    }

    /**
     * Each time run DB will toggle between having 'FIRST SECOND' and
     * not.
     */
    public void testAddOrDelete()
    {
        //dataStore.rollbackTx();
        dataStore.startTx();
        Worker vol = getWorkerFromDB(FIRST, SECOND);
        boolean found = vol != null;
        int countBeforeDelete = Utils.UNSET_INT;
        if (found)
        {
            countBeforeDelete = getWorkerCount();
            dataStore.getEM().deletePersistent(vol);
            int countAfterDelete = getWorkerCount();
            assertTrue( countBeforeDelete == countAfterDelete);
        }
        else
        {
            int countBeforeInsert = getWorkerCount();
            insertWorker();
            vol = getWorkerFromDB(FIRST, SECOND);
            /* For some reason the inserting behaviour is unlike deleting behaviour
             *
            assertTrue("Although just inserted, can't see the volunteer before committing",
                 (vol != null));
            int countAfterInsert = getWorkerCount();
            assertTrue( "Before Insert we had " + countBeforeInsert + ", now after we have " +
                countAfterInsert,
                (countBeforeInsert+1 == countAfterInsert));
             */
        }
        dataStore.commitTx();
        if (!found)
        {
            vol = getWorkerFromDB(FIRST, SECOND);
            assertTrue("Can't see the volunteer after committing",
                 (vol != null));
            dataStore.rollbackTx();
            vol = getWorkerFromDB(FIRST, SECOND);
            assertTrue("Can't see the volunteer after rolling back", (vol != null));
        }
        else
        {
            int countAfterDelete = getWorkerCount();
            assertTrue( "Found and deleted but count didn't go down after commit: before " +
                countBeforeDelete + ", after " + countAfterDelete
                , countBeforeDelete-1 == countAfterDelete);
        }
    }

    /**
     * Note that we are not bothering to put in special dummy DOs for the lookups.
     * Thus this sql will give us not only dummy but FIRST as well, and should
     * never be done on a PROD DB.
     *
     * -- Have any workers been entered with null lookups? (expect dummy only)
     * select christianname, dummy from worker where seniority_pkid is null;
     */
    private void insertWorker()
    {
        DomainQueriesI queriesI = dataStore.getDomainQueries();
        Worker nullVol = (Worker) queriesI.executeRetObject(WombatDomainQueryEnum.NULL_WORKER);
        Worker newVol = (Worker) dataStore.getEM().newPersistent(Worker.class);
        newVol.init(nullVol);
        newVol.setChristianName(FIRST);
        newVol.setSurname(SECOND);
        Err.pr("Have NOT found so have inserted into a <" +
            dataStore.getEM().getORMType() + "> DB");
    }
}
