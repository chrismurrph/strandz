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
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.data.wombatrescue.business.LookupsProvider;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Ingredients:
 * A worker
 * A WorkerPanel to display the worker in
 * A Strand to pull worker onto WorkerPanel
 * A DataStore to communicate with a database
 */
public class BasicTheorySdzExample0
{
    private DataStore dataStore;
    private WorkerAppHelper helper;

    public static void main(String[] args)
    {
        BasicTheorySdzExample0 obj = new BasicTheorySdzExample0();
        obj.init();
    }

    public void init()
    {
        helper = new WorkerAppHelper();
        helper.sdzSetup();
        initDataStore();
        helper.getWorkerNode().addDataFlowTrigger(new BasicTheorySdzExample0.LocalDataFlowListener());
        helper.getStrand().setErrorHandler(new BasicTheorySdzExample0.HandlerT());
        helper.getStrand().setEntityManagerTrigger(new EntityManagerT());
        helper.displayWorkerPanel();
        helper.getWorkerNode().GOTO();
        helper.getStrand().EXECUTE_QUERY();
    }

    private void initDataStore()
    {
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection(WombatConnectionEnum.DEMO_WOMBAT_MEMORY);
        DataStore dataStore = dataStoreFactory.getDataStore();
        this.dataStore = dataStore;
    }

    private Worker getWorker(String firstName, String secondName)
    {
        Worker result;
        result = (Worker) dataStore.getDomainQueries().executeRetObject(
            WombatDomainQueryEnum.WORKER_BY_NAMES, firstName, secondName);
        if(result == null)
        {
            Err.error(firstName + " " + secondName + " NOT found");
        }
        return result;
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
                Print.prThrowable(e, "HelloWorkerSdzExample0");
            }
        }
    }
    
    class EntityManagerT implements EntityManagerTriggerI
    {
        public SdzEntityManagerI fetchEM()
        {
            return ((EntityManagedDataStore)dataStore).getEM();
        }
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                dataStore.rollbackTx();
                dataStore.startTx();

                setLOVs();
                ArrayList list = new ArrayList();
                Worker worker = getWorker("Russell", "Hinze");
                list.add(worker);
                Err.pr(worker + " has flexibility " + worker.getFlexibility());
                helper.getWorkerCell().setData(list);
            }
        }
    }

    private void setLOVs()
    {
        List flexibilities = Utils.asArrayList(Flexibility.OPEN_VALUES);
        helper.getFlexibilityCell().setLOV(flexibilities);
    }
}
