package org.strandz.applic.supersix;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.InterfUtils;
import org.strandz.core.interf.OutNodeControllerEvent;
import org.strandz.core.interf.PostOperationPerformedTrigger;
import org.strandz.core.interf.PreOperationPerformedTrigger;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.data.supersix.business.SuperSixManagerUtils;
import org.strandz.data.supersix.domain.SuperSixDomainLookupEnum;
import org.strandz.data.supersix.objects.Global;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.core.interf.AlterSdzSetupI;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import java.util.ArrayList;
import java.util.List;


public class MaintainGlobalTriggers
{
    public DataStore dataStore;
    public MaintainGlobalDT dt;
    private SuperSixLookups superSixLookups;

    public MaintainGlobalTriggers(
        DataStore dataStore,
        MaintainGlobalDT dt,
        SdzBagI controller,
        SuperSixLookups superSixLookups
    )
    {
        this.dataStore = dataStore;
        this.dt = dt;
        this.superSixLookups = superSixLookups;
        //this.queriesI = dataStore.getDomainQueries();
        dt.globalNode.addDataFlowTrigger( new DataFlowT0());
        controller.getStrand().addTransactionTrigger( new FormTransactionT());
        controller.getStrand().setErrorHandler( new HandlerT());
        controller.getStrand().setEntityManagerTrigger(new EntityManagerT());
        SeasonValidationTrigger seasonValidationTrigger = new SeasonValidationTrigger( dt.seasonYearnameAttribute);
        dt.seasonYearnameAttribute.setItemValidationTrigger( seasonValidationTrigger);
        controller.getStrand().addPreControlActionPerformedTrigger(new PreOperationTrigger());
        controller.getStrand().addPostControlActionPerformedTrigger(new PostOperationTrigger());
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
            SuperSixUtils.alterSdzSetupForOneRow( sbI);
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
                Global global = SuperSixManagerUtils.getGlobalCurrentSuperSixManager().getGlobal();
                List globals = new ArrayList();
                globals.add( global);
                List enterQryAttribs = dt.globalNode.getEnterQueryAttributes();
                globals = InterfUtils.refineFromMatchingValues(
                    globals, enterQryAttribs
                );
                setLOVs();
                dt.globalCell.setData( globals);
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
        List seasonYears = superSixLookups.get( SuperSixDomainLookupEnum.SEASON_YEAR);
        dt.currentSeasonYearLookupCell.setLOV(seasonYears);
        List competitions = superSixLookups.get( SuperSixDomainLookupEnum.COMPETITION);
        dt.currentCompetitionLookupCell.setLOV(competitions);
    }

    public class PostOperationTrigger
        implements PostOperationPerformedTrigger
    {
        public void postOperationPerformed(OutNodeControllerEvent evt)
        {
            if(evt.getNode() == dt.globalNode
                && evt.getID() == OperationEnum.GOTO_NODE
                //&& evt.getNode().getState() != StateEnum.FROZEN
                    )
            {
                Err.pr( SdzNote.PRE_AND_POST, "Now at global node " + evt.getNode() + ", which is in state " + evt.getNode().getState());
            }
        }
    }

    public class PreOperationTrigger
        implements PreOperationPerformedTrigger
    {
        public void preOperationPerformed(OutNodeControllerEvent evt)
        {
            if(evt.getNode() == dt.globalNode
                && evt.getID() == OperationEnum.GOTO_NODE
                //&& evt.getNode().getState() != StateEnum.FROZEN
                    )
            {
                Err.pr( SdzNote.PRE_AND_POST, "Gone from global node " + evt.getNode() + ", which is in state " + evt.getNode().getState());
            }
        }
    }

    static class SeasonValidationTrigger implements ItemValidationTrigger
    {
        RuntimeAttribute attribute;

        SeasonValidationTrigger(RuntimeAttribute attribute)
        {
            this.attribute = attribute;
        }

        /*
         * Problems here. The first change is not being picked up. Also, even when have seen
         * the change thru here, when exit the application it goes thru here one more time.
         * The first problem is a real problem, the second is expected behaviour of a validation
         * trigger - instead trapping directly - see MaintainGlobalEvents 
         */
        public void validateItem(ChangeEvent validationEvent)
            throws ValidationException
        {
            JComboBox cb = (JComboBox)attribute.getItem();
            String seasonYearStr = (String)cb.getSelectedItem();
            //String msgs[] = new String[]{ "One", "Two"};
            //MessageDlg.showConfirmCancelDialog( msgs, "Title");
            Err.pr( SdzNote.FIRST_VALIDATE_NOT_PICKED_UP, "year: " + seasonYearStr);
//            if(!Utils.isBlank(txt))
//            {
//                try
//                {
//                    Err.pr(SdzNote.mDateEntryField, "validating <" + txt + ">");
//                    TimeUtils.DATE_FORMATTER.parse(txt);
//                }
//                catch(ParseException ex)
//                {
//                    attribute.setInError(true);
//                    throw new ValidationException("Date fails validation");
//                }
//            }
//            else
//            {
//                Err.pr(SdzNote.mDateEntryField, "txt is blank so no point validating MDEF " + attribute.getName());
//            }
            attribute.setInError(false);
        }
    }
}