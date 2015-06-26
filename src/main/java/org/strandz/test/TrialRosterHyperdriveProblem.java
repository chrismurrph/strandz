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
package org.strandz.test;

import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.extent.IndependentExtent;
import org.strandz.lgpl.extent.ListExtent;
import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.persist.EntityManagerProviderI;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.JDOWombatDataStore;
import org.strandz.store.wombatrescue.WombatConnections;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TrialRosterHyperdriveProblem
{
    private Worker nullWker;

    public static void main(String[] args)
    {
        new TrialRosterHyperdriveProblem();
    }

    TrialRosterHyperdriveProblem()
    {
        testWithList();
    }

    private static class EMProvider implements EntityManagerProviderI
    {
        private SdzEntityManagerI pm;

        EMProvider(SdzEntityManagerI pm)
        {
            this.pm = pm;
        }

        public SdzEntityManagerI getEntityManager()
        {
            return pm;
        }
    }

    private void testWithList()
    {
        JDOWombatDataStore data = startJDOTransactionStuff();
        //
        data.rollbackTx();
        data.startTx( data.getClass().getName());

        SdzEntityManagerI pm = data.getEM();
        EMProvider provider = new EMProvider(pm);
        Collection c = ((DomainQueriesI) data.getDomainQueries()).executeRetCollection(WombatDomainQueryEnum.ROSTERABLE_WORKERS);
        //queryRosterableWorkers();
        // Won't be able to modify c with JDO calls, so need a wrapper list
        List masterList = new ArrayList(c);
        // Need to tell data which extent it actually came from, otherwise
        // no saving will be taking place.
        // data.setRefinedList( POJOWombatData.VOLUNTEER, masterList);
        // RUBBISH - no need to call this anymore (for JDO anyway) as Cell.setData()
        // calls registerPersistentAll(), so we replicate that in our
        // local commit here. ALSO RUBBISH - JDOBug.shouldOnlyPersistToInsert
        // has commented it out and says it not necessary - which is true as
        // inserts of volunteers are working in production.
        Err.pr(JDONote.SHOULD_ONLY_PERSIST_TO_INSERT,
            "But not commiting here - actually was, query criteria was making insert invisible");

        ListExtent le = (ListExtent)
            IndependentExtent.createIndependent(masterList, provider, null);
        // ie.set
        //
        Worker chris = getNewInstance(data.getDomainQueries());
        chris.setChristianName("Ashley");
        Err.pr("For starters get size " + le.size());
        le.insert(chris, 0);

        Object masterId = JDOHelper.getObjectId(chris);
        Err.pr(masterId);
        ((PersistenceManager) pm.getActualEM()).currentTransaction().rollback();
        // commit( pm);
        // Err.pr( "To close (after insert) get size " + le.size());
    }

    public Worker getNewInstance(DomainQueriesI queriesI)
    {
        Worker result = null;
        if(nullWker == null)
        {
            //nullWker = queriesI.queryNullWorker();
            queriesI.executeRetCollection(WombatDomainQueryEnum.NULL_WORKER);
        }
        result = new Worker(nullWker);
        Err.pr("New Worker has been given a NULL");
        return result;
    }

    /**
     * data.commitTx( cell) requires a cell, so better to do it this way.
     *
     * @param pm
     */
    private static void commit(PersistenceManager pm)
    {
        Transaction tx = pm.currentTransaction();
        if(tx.isActive())
        {
            tx.commit();
        }
        else
        {
            Err.error("No active transaction so nothing will be committed");
        }
    }

//  private static void informJDOOfInsert( PersistenceManager pm, Object obj)
//  {
//    org.strandz.core.extent.pUtil.insert( pm, obj);
//  }

    private static JDOWombatDataStore startJDOTransactionStuff()
    {
        WombatConnections connections = new WombatConnections();
        JDOWombatDataStore result = new JDOWombatDataStore(connections.get(WombatConnectionEnum.DIRECT_DEMO_WOMBAT_CAYENNE));
        // Do this to get the properties read in
        // pm = pUtil.getNewPM( store.getConnectionProperties());
        // pm.currentTransaction().begin();
        return result;
    }
    // Prolly not necessary
    /*
    private static void startStrandzStuff()
    {
    RosterWorkersStrand rv = new RosterWorkersStrand();
    rv.sdzSetup();
    rv.preForm();
    rv.display( true, RosterWorkersStrand.ROSTERABLE_VOLS);
    }
    */
}
