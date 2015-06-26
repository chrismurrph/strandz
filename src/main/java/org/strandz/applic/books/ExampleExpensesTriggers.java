package org.strandz.applic.books;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.InterfUtils;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.store.books.BooksQueryEnum;
import org.strandz.core.interf.AlterSdzSetupI;

import javax.swing.JOptionPane;
import java.util.Collection;
import java.util.List;


public class ExampleExpensesTriggers
{
    public DataStore dataStore;
    public ExampleExpensesDT dt;

    public ExampleExpensesTriggers(
        DataStore dataStore,
        ExampleExpensesDT dt,
        SdzBagI controller
    )
    {
        this.dataStore = dataStore;
        this.dt = dt;
        dt.expenseNode.addDataFlowTrigger( new DataFlowT0());
        controller.getStrand().addTransactionTrigger( new FormTransactionT());
        controller.getStrand().setErrorHandler( new HandlerT());
        controller.getStrand().setEntityManagerTrigger(new EntityManagerT());
        new AlterSdzSetup( controller).performAlterSdzSetup();
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
        public void handleError( ApplicationError e)
        {
            List msg = e.getMsg();
            if(msg != null)
            {
                new MessageDlg( msg, JOptionPane.ERROR_MESSAGE);
                Err.alarm( msg.get( 0).toString());
            }
            else
            {
                Print.prThrowable( e, "Auto-generated handler");
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

    class DataFlowT0 implements DataFlowTrigger
    {
        public void dataFlowPerformed( DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                dataStore.rollbackTx();
                dataStore.startTx();
                setLOVs();
                Collection c = dataStore.getDomainQueries().executeRetCollection( BooksQueryEnum.DEFAULT_QUERY);
                List enterQryAttribs = dt.expenseNode.getEnterQueryAttributes();
                c = InterfUtils.refineFromMatchingValues(
                    c, enterQryAttribs
                );
                dt.expenseCell.setData( c);
            }
        }
    }

    class FormTransactionT implements CloseTransactionTrigger
    {
        public void perform( TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                dataStore.commitTx();
            }
        }
    }

    private void setLOVs()
    {
    }
}