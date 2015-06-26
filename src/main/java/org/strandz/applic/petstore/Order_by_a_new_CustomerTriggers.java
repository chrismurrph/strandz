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
package org.strandz.applic.petstore;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.InterfUtils;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.AlterSdzSetupI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.store.petstore.PetstoreQueries;

import javax.swing.JOptionPane;
import java.util.Collection;
import java.util.List;

public class Order_by_a_new_CustomerTriggers
{
    public DataStore data;
    public PetstoreQueries queries;
    public Order_by_a_new_CustomerDT dt;

    public Order_by_a_new_CustomerTriggers(DataStore data,
                                           PetstoreQueries queries,
                                           Order_by_a_new_CustomerDT dt,
                                           SdzBagI sdzBag
    )
    {
        this.data = data;
        this.queries = queries;
        this.dt = dt;
        dt.accountNode.addDataFlowTrigger(new DataFlowT0());
        dt.orderNode.addDataFlowTrigger(new DataFlowT1());
        sdzBag.getStrand().addTransactionTrigger(new FormCloseTransactionT());
        sdzBag.getStrand().setErrorHandler(new HandlerT());
        sdzBag.getStrand().setEntityManagerTrigger(new EntityManagerT());
        new AlterSdzSetup( sdzBag).performAlterSdzSetup();
    }

    private class AlterSdzSetup implements AlterSdzSetupI
    {
        private SdzBagI sbI;
        
        AlterSdzSetup( SdzBagI sbI)
        {
            this.sbI = sbI;
        }
        
        public void performAlterSdzSetup()
        {
            //If need to dynamically alter the sbI then write the code here
        }
    }

    static class HandlerT implements ValidationHandlerTrigger
    {
        public void handleError(ApplicationError e)
        {
            List msg = e.getMsg();
            if(msg != null)
            {
                new MessageDlg(msg, JOptionPane.ERROR_MESSAGE);
                Err.alarm(msg.get(0).toString());
            }
            else
            {
                Print.prThrowable(e, "Order_by_a_new_CustomerTriggers.HandlerT");
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

    class DataFlowT0 implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                data.rollbackTx();
                data.startTx();
                setLOVs();

                Collection c = queries.queryAllAccounts();
                List enterQryAttribs = dt.accountNode.getEnterQueryAttributes();
                c = InterfUtils.refineFromMatchingValues(c,
                    enterQryAttribs);
                //Was a NOP
                //data.setRefinedList( org.strandz.data.petstore.objects.Account.class, c);
                dt.new_Customer_makes_orderCell.setData(c);
            }
        }
    }


    class DataFlowT1 implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                List list = data.query(org.strandz.data.petstore.objects.Order.class);
                dt.orderCell.setData(list);
            }
        }
    }


    class FormCloseTransactionT implements CloseTransactionTrigger
    {
        public void perform(TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                data.commitTx();
            }
        }
    }

    private void setLOVs()
    {
    }
}
