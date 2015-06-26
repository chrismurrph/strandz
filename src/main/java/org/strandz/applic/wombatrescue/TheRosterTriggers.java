package org.strandz.applic.wombatrescue;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.NewInstanceTrigger;
import org.strandz.core.domain.NewInstanceEvent;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.AlterSdzSetupI;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.data.wombatrescue.business.ParticularRosterI;
import org.strandz.data.wombatrescue.business.RosterBusinessUtils;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.business.RostererSession;
import org.strandz.data.wombatrescue.business.RostererSessionI;
import org.strandz.data.wombatrescue.calculated.MonthTransferObj;
import org.strandz.data.wombatrescue.calculated.RosterMonth;
import org.strandz.data.wombatrescue.calculated.RosterTransferObj;
import org.strandz.data.wombatrescue.calculated.Night;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;


public class TheRosterTriggers
{
    public DataStore dataStore;
    public TheRosterDT dt;
    private ParticularRosterI particularRoster;
    private RostererSessionI rostererSession;
    /**
     * How ran this strand before incorporated into proper application (RunWombatrescue)
     */
    private boolean debug;
    private static int times;

    public TheRosterTriggers(
        DataStore dataStore,
        TheRosterDT dt, 
        SdzBagI controller,
        boolean debug
    )
    {
        this.dataStore = dataStore;
        this.dt = dt;
        this.debug = debug;
        dt.rosterMonthNode.addDataFlowTrigger( new DataFlowT0());
        dt.nightsListDetailCell.setNewInstanceTrigger( new NightNewInstanceT());
        controller.getStrand().addTransactionTrigger( new FormTransactionT());
        controller.getStrand().setErrorHandler( new HandlerT());
        new AlterSdzSetup( controller).performAlterSdzSetup();
        if(debug)
        {
            RosterSessionUtils.setMemoryProperty( "thickClient", "true");
            rostererSession = new RostererSession( true);
            rostererSession.init( dataStore);
        }
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
            //Doing in Designer
            //dt.monthInYearnameAttribute.setEnabled( false);
            //dt.rosterMonthyearAttribute.setEnabled( false);
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

    class DataFlowT0 implements DataFlowTrigger, ActionListener
    {
        private String rosterText;
        
        public void dataFlowPerformed( DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                dataStore.rollbackTx();
                dataStore.startTx();
                setLOVs();
                RosterMonth rosterMonth;
                RosterTransferObj rosterTransferObj;
                if(debug)
                {
                    MonthTransferObj month = new MonthTransferObj( MonthInYear.SEPTEMBER, 2007);
                    rosterTransferObj = rostererSession.getRosterService().getRoster( 
                            RosteringConstants.ROSTER, month);
                    Err.pr( "rosterTransferObj: " + rosterTransferObj);
                    rosterMonth = rosterTransferObj.getRosterMonth();
                }
                else
                {
                    rosterMonth = null;
                    StringBuffer buf = particularRoster.display( RosteringConstants.ROSTER);
                    //buf will be null when server is down, and when here a message
                    //will have already gone to the user
                    String txt;
                    if(buf != null)
                    {
                        txt = buf.toString();
                        Assert.notNull( dt.ui0.getMonthYearPanel());
                        JButton uploadButton = dt.ui0.getMonthYearPanel().getBUpload();
                        uploadButton.removeActionListener( this);
                        uploadButton.addActionListener( this);
                        JButton uploadAsOldButton = dt.ui0.getMonthYearPanel().getBUploadAsOld();
                        uploadAsOldButton.removeActionListener( this);
                        uploadAsOldButton.addActionListener( this);
                        rosterTransferObj = particularRoster.getRosterTransferObj();
                        rosterMonth = rosterTransferObj.getRosterMonth();
                    }
                    else
                    {
                        txt = "Roster unable to be displayed";
                    }
                    rosterText = txt;
                    //Err.pr( txt);
                }
                Collection c = Utils.formList( rosterMonth);
                /*
                times++;
                Print.prCollection( c, "List setData on " + dt.rosterMonthCell + " times " + times);
                if(times == 2)
                {
                    Err.debug();
                }
                */
                dt.rosterMonthCell.setData( c);
                String msg = "result of roster request from server (this refreshing could be done by the framework?)";
                Err.pr( SdzNote.TABLE_REFRESH, msg);
                /*
                 * A: setData() above could do a REFRESH() when the node has a table
                 */
                /*
                dt.ui0.getNightsTablePanel().getNightsComponentTable().modelDataChanged( msg);
                dt.ui0.getNightsTablePanel().getClashesComponentTable().modelDataChanged( msg);
                dt.ui0.getNightsTablePanel().getUnfilledNightsComponentTable().modelDataChanged( msg);
                dt.ui0.getNightsTablePanel().getUnrosteredComponentTable().modelDataChanged( msg);
                */
            }
        }
        
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == dt.ui0.getMonthYearPanel().getBUpload())
            {
                RosterBusinessUtils.uploadRoster( false, rosterText, rostererSession, particularRoster);
            }
            else if(e.getSource() == dt.ui0.getMonthYearPanel().getBUploadAsOld())
            {
                RosterBusinessUtils.uploadRoster( true, rosterText, rostererSession, particularRoster);
            }
            else
            {
                Err.error();
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

    /**
     * Used for doing calculations on a Night, the difference being that
     * as night doesn't have any calculated fields, we do the minimum
     * to stop a stack trace.
     */
    class NightNewInstanceT implements NewInstanceTrigger
    {
        public Object getNewInstance( NewInstanceEvent event)
        {
            Night result = null;
            return result;
        }
    }

    private void setLOVs()
    {
    }    
    
    public void setBusinessObjects(ParticularRosterI particularRoster, RostererSessionI rostererSession)
    {
        Assert.notNull( particularRoster.getDataStore(), particularRoster.getClass().getName() + " requires a DataStore");
        Assert.notNull( rostererSession.getDataStore());
        this.particularRoster = particularRoster;
        this.rostererSession = rostererSession;
    }
}