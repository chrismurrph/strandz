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
package org.strandz.applic.wombatrescue;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.AlterSdzSetupI;
import org.strandz.core.interf.Cell;
import org.strandz.data.wombatrescue.calculated.ShiftManagers;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.view.wombatrescue.ShiftManagersPanel;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BuddyManagerTriggers
{
    public DataStore dataStore;
    public DomainQueriesI queriesI;
    public BuddyManagerDT dt;
    private ClazzToActuallyUseChooser clazzChooser;

    public BuddyManagerTriggers(EntityManagedDataStore dataStore,
                                DomainQueriesI queriesI,
                                BuddyManagerDT dt,
                                SdzBagI sdzBag
                                )
    {
        this.dataStore = dataStore;
        this.queriesI = queriesI;
        this.dt = dt;
        dt.shiftManagerNode.addDataFlowTrigger(new DataFlowT0());
        sdzBag.getStrand().addTransactionTrigger(new FormCloseTransactionT());
        sdzBag.getStrand().setErrorHandler(new HandlerT());
        //Really ought to put this in an app specific global place,
        //otherwise will be doing in in each triggers
        clazzChooser = new ClazzToActuallyUseChooser( dataStore.getEM().getORMType());
        new AlterSdzSetup(sdzBag).performAlterSdzSetup();
    }
    
    private class AlterSdzSetup implements AlterSdzSetupI, ActionListener
    {
        private SdzBagI sbI;
        
        AlterSdzSetup( SdzBagI sbI)
        {
            this.sbI = sbI;
        }
        
        public void performAlterSdzSetup()
        {
            ShiftManagersPanel panel = (ShiftManagersPanel)sbI.getPane( 0);
            panel.getBSave().addActionListener( AlterSdzSetup.this);

            setForORM( dt.shiftManagerCell);
            setForORM( dt.friDinnerWkerLookupCell);
            setForORM( dt.friOvernightWkerLookupCell);
            setForORM( dt.monDinnerWkerLookupCell);
            setForORM( dt.monOvernightWkerLookupCell);
            setForORM( dt.tueDinnerWkerLookupCell);
            setForORM( dt.tueOvernightWkerLookupCell);
            setForORM( dt.wedDinnerWkerLookupCell);
            setForORM( dt.wedOvernightWkerLookupCell);
            setForORM( dt.thuDinnerWkerLookupCell);
            setForORM( dt.thuOvernightWkerLookupCell);
            setForORM( dt.satDinnerWkerLookupCell);
            setForORM( dt.satOvernightWkerLookupCell);
        }

        public void actionPerformed(ActionEvent e)
        {
            sbI.getStrand().COMMIT_RELOAD();
        }

        private void setForORM( Cell cell)
        {
            cell.setClazzToConstruct( clazzChooser.getClazzToUse( cell));
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
                Print.prThrowable(e, "BuddyManagerTriggers.HandlerT");
            }
        }
    }


    class DataFlowT0 implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                dataStore.rollbackTx();
                dataStore.startTx();
                setLOVs();
                Collection buddyManagers = RosterSessionUtils.getGlobalRostererSession().getBuddyManagers();
                //queryBuddyManagers();
                List shiftManagers = new ArrayList();
                ShiftManagers sm = new ShiftManagers((List) buddyManagers);
                shiftManagers.add(sm);
                dt.shiftManagerCell.setData(shiftManagers);
            }
        }
    }


    class FormCloseTransactionT implements CloseTransactionTrigger
    {
        public void perform(TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                dataStore.commitTx();
            }
        }
    }

    /**
     * TODO - Have a Worker.toFormal() method that use to order the collection
     * of vols. "Murphy, Chris" seemed to be the natural way that I wanted to interact
     * with a JCombo for finding a replacement Buddy Manager. Currently ordering is done
     * by first name, surname.
     */
    private void setLOVs()
    {
        /*
         * The way the test data is set up group and unknown workers serve as Buddy Managers
         * TODO We need to use a dialog that searches thru the workers - might be a query only
         * version of current workers list.
         */
        Collection rosterableWorkers = RosterSessionUtils.getGlobalRostererSession().getWorkers();
        //Collection groupWorkers = RosterSessionUtils.getGlobalRostererSession().getGroupWorkers();
        List displayWkers = new ArrayList(rosterableWorkers);
        //displayWkers.addAll( groupWorkers);
        displayWkers.add(0, queriesI.executeRetObject(WombatDomainQueryEnum.NULL_WORKER));
        dt.friDinnerWkerLookupCell.setLOV(displayWkers);
        dt.friOvernightWkerLookupCell.setLOV(displayWkers);
        dt.monDinnerWkerLookupCell.setLOV(displayWkers);
        dt.monOvernightWkerLookupCell.setLOV(displayWkers);
        dt.satDinnerWkerLookupCell.setLOV(displayWkers);
        dt.satOvernightWkerLookupCell.setLOV(displayWkers);
        dt.tueDinnerWkerLookupCell.setLOV(displayWkers);
        dt.tueOvernightWkerLookupCell.setLOV(displayWkers);
        dt.wedDinnerWkerLookupCell.setLOV(displayWkers);
        dt.wedOvernightWkerLookupCell.setLOV(displayWkers);
        dt.thuDinnerWkerLookupCell.setLOV(displayWkers);
        dt.thuOvernightWkerLookupCell.setLOV(displayWkers);
    }
}
