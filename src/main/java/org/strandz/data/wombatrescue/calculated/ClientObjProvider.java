/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2005 Seaweed Software Pty Ltd

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

    In addition, as a special exception, Seaweed Software Pty Ltd gives
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
package org.strandz.data.wombatrescue.calculated;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Is used on the client side only. Used to convert DOs into being the right 'client' type.
 */
public class ClientObjProvider
{
    private EntityManagedDataStore ds;

    public ClientObjProvider()
    {
    }

    public EntityManagedDataStore getDataStore()
    {
        return ds;
    }

    public void setDataStore(EntityManagedDataStore ds)
    {
        this.ds = ds;
    }

    public List<WorkerI> getClientWorkers(List<WorkerI> workers)
    {
        List<WorkerI> result = new ArrayList<WorkerI>();
        for (Iterator<WorkerI> workerIterator = workers.iterator(); workerIterator.hasNext();)
        {
            WorkerI worker = workerIterator.next();
            result.add(getClientWorker(worker));
        }
        return result;
    }

    public WorkerI getClientWorker(WorkerI worker)
    {
        WorkerI result;
        EntityManagedDataStore ds = getDataStore();
        if(Utils.instanceOf(ds, EntityManagedDataStore.class))
        {
            SdzEntityManagerI em = ds.getEM();
            if(worker == null)
            {
                Err.pr( "No worker: <" + this + ">");
            }
            if(worker != null && !em.isEntityManaged(worker))
            {
                worker.installHelper();
                result = replaceWorkerCayenne(worker, ds);
            }
            else
            {
                result = worker;
            }
        }
        else
        {
            result = worker;
        }
        return result;
    }

    public RosterSlotI getClientRosterSlot(RosterSlotI rosterSlot)
    {
        RosterSlotI result;
        EntityManagedDataStore ds = getDataStore();
        if(Utils.instanceOf(ds, EntityManagedDataStore.class))
        {
            SdzEntityManagerI em = ds.getEM();
            if(rosterSlot == null)
            {
                Err.pr( "No rosterSlot: <" + this + ">");
            }
            if(rosterSlot != null && !em.isEntityManaged(rosterSlot))
            {
                rosterSlot.installHelper();
                result = replaceRosterSlotCayenne(rosterSlot, ds);
            }
            else
            {
                result = rosterSlot;
            }
        }
        else
        {
            result = rosterSlot;
        }
        return result;
    }

    /**
     * With Cayenne what comes back across is the server DO with nothing
     * in it, except the PK, which we can use to get a local client object
     */
    private WorkerI replaceWorkerCayenne(
        WorkerI toReplace,
        EntityManagedDataStore ds
    )
    {
        WorkerI result;
        int toReplacePK = DataObjectUtils.intPKForObject((Persistent) toReplace);
        ObjectContext objectContext = (ObjectContext) ds.getEM().getActualEM();
        result = DataObjectUtils.objectForPK(
            objectContext,
            org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class,
            toReplacePK);
        return result;
    }

    /**
     * With Cayenne what comes back across is the server DO with nothing
     * in it, except the PK, which we can use to get a local client object
     */
    private RosterSlotI replaceRosterSlotCayenne(
        RosterSlotI toReplace,
        EntityManagedDataStore ds
    )
    {
        RosterSlotI result;
        int toReplacePK = DataObjectUtils.intPKForObject( (Persistent)toReplace);
        ObjectContext objectContext = (ObjectContext) ds.getEM().getActualEM();
        result = DataObjectUtils.objectForPK(
            objectContext,
            org.strandz.data.wombatrescue.objects.cayenne.client.RosterSlot.class,
            toReplacePK);
        return result;
    }
    
    /**
     * With Kodo the DOs that come across implement serializable and come across
     * fine, but are not entity managed, so we need to find the equivalent local object
     * , which we can do by comparing object values.
     */
    /* No reason why this won't work fine, just no longer using Kodo
    private static WorkerI replaceWorkerKodo(
        WorkerI toReplace,
        CachedRosterableWorkersProviderI cachedRosterableWorkersProvider)
    {
        WorkerI result = null;
        Assert.notNull(cachedRosterableWorkersProvider);
        if (toReplace != null)
        {
            int origNumRSs;
            int replacedNumRSs;
            if (!ALLOW_NO_ROSTERSLOTS)
            {
                //Err.pr( "orig worker " + toReplace.getToShort() + " has " + toReplace.getRosterSlots().size() + " RSs");
                Assert.notNull(toReplace.getRosterSlots(), "Wanting to replace a worker with no roster slots: <" +
                    toReplace.getToLong() + "> isDummy: " + toReplace.isDummy());
                origNumRSs = toReplace.getRosterSlots().size();
            }
            //Other queries done elsewhere will also update this (infered from JDO equals rule)
            for (WorkerI worker : cachedRosterableWorkersProvider.getRosterableWorkers())
            {
                if (toReplace.compareTo(worker) == 0)
                {
                    result = worker;
                    break;
                }
            }
            if (SdzNote.NULL_WORKER_ACROSS_WIRE.isVisible())
            {
                if (result == null)
                {
                    Err.pr("christianName: " + toReplace.getChristianName());
                    Err.pr("surname: " + toReplace.getSurname());
                    Err.pr("groupName: " + toReplace.getGroupName());
                    Err.pr("dummy: " + toReplace.isDummy());
                    Print.prList(cachedRosterableWorkersProvider.getRosterableWorkers(), "To choose from");
                }
                else
                {
                    Err.pr("worker that came across ok:");
                    Err.pr(" christianName: " + toReplace.getChristianName());
                    Err.pr(" surname: " + toReplace.getSurname());
                    Err.pr(" groupName: " + toReplace.getGroupName());
                    Err.pr(" dummy: " + toReplace.isDummy());
                    Err.pr("");
                }
                Assert.notNull(result, "A worker that came back from Spring does not exist: " +
                    "<" + toReplace + ">, rosterable: " + !toReplace.isUnknown());
            }
            if (result != null)
            {
                //Err.pr( "Replaced worker " + result.getToShort() + " has " +
                //        result.getRosterSlots().size() + " RSs");
                if (!ALLOW_NO_ROSTERSLOTS)
                {
                    replacedNumRSs = result.getRosterSlots().size();
                    if (replacedNumRSs != origNumRSs)
                    {
                        Err.pr("Used to have " + origNumRSs + ", but now have " + replacedNumRSs);
                    }
                }
            }
            else
            {
                //loads of these:
                //Err.pr( "Replaced worker is null");
            }
        }
        return result;
    }
    */

    /*
    private RosterSlotI replaceRosterSlot(RosterSlotI toReplace)
    {
        RosterSlotI result = null;
        Assert.notNull(cachedRosterableWorkersProvider);
        if (toReplace != null)
        {
            //Other queries done elsewhere will also update this (infered from JDO equals rule)
            for (RosterSlotI rosterSlot : cachedRosterableWorkersProvider.getRosterSlots())
            {
                if (toReplace.compareTo(rosterSlot) == 0)
                {
                    result = rosterSlot;
                    break;
                }
            }
        }
        return result;
    }
    */
}
