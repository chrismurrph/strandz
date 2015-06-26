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
package org.strandz.task.wombatrescue;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.NewInstanceTrigger;
import org.strandz.core.domain.NewInstanceEvent;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p/>
 * Result:
 * Find out why code is trying to insert a duplicate Flexibility when all
 * we wanted to do was insert a new Worker, with an existing Flexibility
 * if any.
 * <p/>
 * Ingredients:
 * A worker with known Flexibility
 * LOV of Flexibility (ie. all Flexibility DOs in the DB)
 * logically it is still a LOV even if it is a set of Radio Buttons
 * A WorkerPanel to display him in
 * A Strand to suck him onto the display
 * A way to insert a new Worker and then commit
 * <p/>
 * Method:
 * 1./Make sure can get exception
 * Could not replicate when commit straight after insert
 * 2./Query on many workers so can go to another one before try to commit
 * JDO does not allow modification of collections
 * 3./Apply trick to get around this (an ArrayList and a call to setRefinedList)
 * Seems to insert ok
 * 4./Work with DisplayParticularWorker to check that insert worked
 * Insert did not happen (no JDO commit was done, as this is left to the
 * callback trigger by design) TODO error when no trigger found
 * 5./Create a transaction trigger so that the inserted worker can go
 * into the DataStore
 * [
 * When trying to see if worker was inserted got a NPE indicating the a null
 * worker exists. Got rid of the dud which must have been hanging around for
 * a while. When doing 3 again, followed by 4, again without the transaction trigger,
 * just to check that that was not the problem (it couldn't be but still checking)
 * got JDOGenieObjectNotFoundException. This is because the dud I deleted was being
 * referred to elsewhere! Thus get a brand new DB, restart the JDO Server and change
 * RemoveDuds into FixDuds. FixDuds showed there weren't any, so we'll move on.
 * ]
 * This time DisplayParticularWorker shows that "A dud worker exists". This could be
 * a real note, so time to run our unit tests. Unit tests now fine. What was happening
 * was that a Worker was being created by Strandz, including it's belongsToGroup
 * member. Thus we use WorkerNewInstanceT to make sure that we control the creating
 * of a new Worker. Even though WorkerNewInstanceT set up are still getting the dud
 * worker! Thus put some dud checks in. That wasn't the problem, but somehow the
 * WorkerNewInstanceT is not being activated. workerNode.GOTO() should have been
 * occuring after all the triggers had been set up, so GOTO() moved out of
 * helper.sdzSetup(). Now finally Papa Smurf is being created and there are no
 * duds
 * 6./Try StartupDev, to see if we have inadvertently fixed the problem
 * We haven't!
 * 7./Debug on creating Flexibilities here and StartupDev
 * Found that a Flexibility was being factoried in SubRecordObj.getLookupAcross. A
 * selectable item (LOV) s/never be created!
 * 8./See if RosterWorkersTriggers have specified the LOVs, the way we do here
 * No!
 * 9./Thus fix RosterWorkersTriggers
 * <p/>
 * TODO
 * As a postscript, we will provide the ability to mark certain classes so that an
 * error will occur when we try to factory them.
 * <p/>
 */
public class DupFlexibilityIWorkerSdzExample
{
    //private WorkerPanel panel = new WorkerPanel();
    private DataStore data;
    private DomainQueriesI queriesI;
    private Worker nullVol;
    private WorkerAppHelper helper;
    private Worker papaSmurf;

    private static boolean commitWork = true;

    public static void main(String[] args)
    {
        Err.setBatch(false);

        DupFlexibilityIWorkerSdzExample obj = new DupFlexibilityIWorkerSdzExample();
        obj.init();
    }

    public void init()
    {
        helper = new WorkerAppHelper();
        helper.sdzSetup(); //TODO Make sure this method signature is used everywhere
        helper.getPanel().frbFlexibilityRadioButtons.setDebug(true);
        helper.getWorkerNode().addDataFlowTrigger(new DupFlexibilityIWorkerSdzExample.LocalDataFlowListener());
        helper.getStrand().setErrorHandler(new DupFlexibilityIWorkerSdzExample.HandlerT());
        helper.getStrand().setEntityManagerTrigger(new EntityManagerT());
        helper.getStrand().addTransactionTrigger(new DupFlexibilityIWorkerSdzExample.CloseTransactionT());
        helper.getWorkerCell().setNewInstanceTrigger(new DupFlexibilityIWorkerSdzExample.WorkerNewInstanceT());
        initData();
        helper.displayWorkerPanel();
        helper.getWorkerNode().GOTO();
        helper.getStrand().EXECUTE_QUERY();
        helper.getStrand().INSERT();
        helper.christianNameAttribute.setItemValue("Papa");
        helper.surnameAttribute.setItemValue("Smurf");
        helper.getStrand().COMMIT_RELOAD();
    }

    private void initData()
    {
        //WombatDataFactory ad = WombatDataFactory.getNewInstance( WombatConnectionEnum.DEBIAN_DEV_TERESA );
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection(WombatConnectionEnum.DEBIAN_DEV_TERESA);
        DataStore dataStore = dataStoreFactory.getDataStore();
        queriesI = dataStore.getDomainQueries();
        data = dataStore;
    }

    class HandlerT implements ValidationHandlerTrigger
    {
        public void handleError(ApplicationError e)
        {
            List msg = e.getMsg();
            if(msg != null)
            {
                new MessageDlg(msg, JOptionPane.ERROR_MESSAGE);
                // Err.pr( msg);
                Err.alarm(msg.get(0).toString());
            }
            else
            {
                Print.prThrowable(e, "ComponentNotDisplayingDataRecipe");
            }
        }
    }
    
    class EntityManagerT implements EntityManagerTriggerI
    {
        public SdzEntityManagerI fetchEM()
        {
            return ((EntityManagedDataStore)data).getEM();
        }
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        // throws ValidationException
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                data.rollbackTx();
                data.startTx();

                setLOVs();
                Collection c = queriesI.executeRetCollection(WombatDomainQueryEnum.ROSTERABLE_WORKERS);
                //queryRosterableWorkers();
                List list = new ArrayList(c);
                //Was a NOP
                //data.setRefinedList( POJOWombatData.WORKER, list );
                helper.getWorkerCell().setData(list);
            }
        }
    }

    class CloseTransactionT implements CloseTransactionTrigger
    {
        public void perform(TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                Err.pr("B4 commit, papaSmurf has flexibility " +
                    papaSmurf.getFlexibility() + ", id " + papaSmurf.getFlexibility().getId());
                if(commitWork)
                {
                    data.commitTx();
                }
                else
                {
                    data.rollbackTx();
                }
            }
        }
    }

    private void setLOVs()
    {
        List flexibilities = (List) data.get(POJOWombatData.FLEXIBILITY);
        helper.getFlexibilityCell().setLOV(flexibilities);
    }

    private void dudCheck(Worker worker)
    {
        if(!worker.isDummy())
        {
            if(worker.toString() == null)
            {
                Err.error("dud worker");
            }
        }
    }

    class WorkerNewInstanceT implements NewInstanceTrigger
    {
        public Object getNewInstance( NewInstanceEvent event)
        {
            Worker result = null;
            if(nullVol == null)
            {
                nullVol = (Worker) queriesI.executeRetObject(WombatDomainQueryEnum.NULL_WORKER);
                //queryNullWorker();
                dudCheck(nullVol);
            }
            result = new Worker(nullVol);
            //Would expect the worker to be a dud when first created!
            //dudCheck( result);
            Err.pr("A New Worker has been given a NULL/dummy Worker as the group he/she belongs to");
            papaSmurf = result;
            return result;
        }
    }
}
