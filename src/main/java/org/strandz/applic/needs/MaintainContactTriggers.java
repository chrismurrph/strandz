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
package org.strandz.applic.needs;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.RecordValidationEvent;
import org.strandz.core.domain.event.RecordValidationTrigger;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.InterfUtils;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.data.needs.objects.Contact;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.store.needs.NeedsQueries;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MaintainContactTriggers
{
    public DataStore data;
    public NeedsQueries queries;
    public MaintainContactDT dt;
    public AlphabetActionListener alphabetActionListener = new AlphabetActionListener();
    SdzBagI sdzBag;

    public MaintainContactTriggers(DataStore data,
                                   NeedsQueries queries,
                                   MaintainContactDT dt,
                                   SdzBagI sdzBag
    )
    {
        this.data = data;
        this.queries = queries;
        this.dt = dt;
        this.sdzBag = sdzBag;
        dt.contactNode.addDataFlowTrigger(new DataFlowT0());
        sdzBag.getStrand().addTransactionTrigger(new FormCloseTransactionT());
        sdzBag.getStrand().setErrorHandler(new HandlerT());
        sdzBag.getStrand().setEntityManagerTrigger(new EntityManagerT());
        //
        dt.ui0.getAlphabetPanel().setActionListener(alphabetActionListener);
        dt.contactNode.setRecordValidationTrigger(
            new MaintainContactRecordValidationT());
    }

    class AlphabetActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JButton button = (JButton) e.getSource();
            // Err.pr( button.getText());
            int i = 0;
            for(Iterator iter = dt.contactCell.getDataRecords().iterator(); iter.hasNext(); i++)
            // for(Iterator iter = list.iterator(); iter.hasNext(); i++)
            {
                Contact con = (Contact) iter.next();
                if(con.startsWith(button.getText()))
                {
                    break;
                }
            }
            if(i != dt.contactNode.getRowCount())
            {
                sdzBag.getStrand().SET_ROW(i);
            }
        }
    }


    class HandlerT implements ValidationHandlerTrigger
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
                Print.prThrowable(e, "MaintainContactTriggers.HandlerT");
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

                Collection c = queries.queryAllContacts();
                List enterQryAttribs = dt.contactNode.getEnterQueryAttributes();
                c = InterfUtils.refineFromMatchingValues(c, enterQryAttribs);
                //Was a NOP
                //data.setRefinedList( org.strandz.data.needs.objects.Contact.class, c);
                dt.contactCell.setData(c);
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

    class MaintainContactRecordValidationT implements RecordValidationTrigger
    {
        public void validateRecord(RecordValidationEvent validationEvent)
            throws ValidationException
        {
            // If one filled in, they all must be
            if(!dt.addressStreetAttribute.isBlank()
                || !dt.addressSuburbAttribute.isBlank()
                || !dt.addressPostcodeAttribute.isBlank())
            {
                if(!dt.addressStreetAttribute.isBlank()
                    && !dt.addressSuburbAttribute.isBlank()
                    && !dt.addressPostcodeAttribute.isBlank())
                {
                }
                else
                {
                    throw new ValidationException("The whole address must be filled in");
                }
            }
            if(dt.contactFirstNameAttribute.isBlank()
                && dt.contactSecondNameAttribute.isBlank())
            {
                throw new ValidationException(
                    "One of either First Name or Second Name must be filled in");
            }
        }
    }
}
