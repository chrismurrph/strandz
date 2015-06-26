package org.strandz.applic.supersix;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.InterfUtils;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.data.supersix.business.SuperSixManagerUtils;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.core.interf.AlterSdzSetupI;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;


public class MaintainSeasonTriggers
{
    public DataStore dataStore;
    public MaintainSeasonDT dt;

    public MaintainSeasonTriggers(
        DataStore dataStore,
        MaintainSeasonDT dt,
        SdzBagI controller
    )
    {
        this.dataStore = dataStore;
        this.dt = dt;
        dt.seasonNode.addDataFlowTrigger( new DataFlowT0());
//        dt.meetsListDetailNode.addDataFlowTrigger( new DataFlowT1());
//        dt.matchesListDetailNode.addDataFlowTrigger( new DataFlowT2());
        controller.getStrand().addTransactionTrigger( new FormTransactionT());
        controller.getStrand().setErrorHandler( new HandlerT());
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

    class DataFlowT0 implements DataFlowTrigger
    {
        public void dataFlowPerformed( DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                dataStore.rollbackTx();
                dataStore.startTx();
                setLOVs();
                CompetitionSeason competitionSeason = SuperSixManagerUtils.getCurrentCompetitionSeason();
                List seasons = new ArrayList();
                seasons.add(competitionSeason);
                List enterQryAttribs = dt.seasonNode.getEnterQueryAttributes();
                seasons = InterfUtils.refineFromMatchingValues(
                    seasons, enterQryAttribs
                );
                dt.seasonCell.setData( seasons);
            }
        }
    }

//    class DataFlowT1 implements DataFlowTrigger
//    {
//        public void dataFlowPerformed( DataFlowEvent evt)
//        {
//            if(evt.getID() == DataFlowEvent.PRE_QUERY)
//            {
//                List list = dataStore.query( org.strandz.data.supersix.objects.Meet.class);
//                dt.meetsCell.setData( list);
//            }
//        }
//    }

//    class DataFlowT2 implements DataFlowTrigger
//    {
//        public void dataFlowPerformed( DataFlowEvent evt)
//        {
//            if(evt.getID() == DataFlowEvent.PRE_QUERY)
//            {
//                List list = dataStore.query( org.strandz.data.supersix.objects.Match.class);
//                dt.matchesCell.setData( list);
//            }
//        }
//    }

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