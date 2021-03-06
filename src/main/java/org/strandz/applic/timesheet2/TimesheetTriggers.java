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
package org.strandz.applic.timesheet2;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.lgpl.data.objects.TimeSpent;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.store.timesheet.TimesheetQueries;

import javax.swing.event.ChangeEvent;
import javax.swing.JOptionPane;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TimesheetTriggers
{
    public DataStore data;
    public TimesheetQueries queries;
    public TimesheetDT dt;
    private static String resourceName = "Chris Murphy";

    public TimesheetTriggers(DataStore data,
                             TimesheetQueries queries,
                             TimesheetDT dt,
                             SdzBagI sdzBag
    )
    {
        this.data = data;
        this.queries = queries;
        this.dt = dt;
        dt.timesheetNode.addDataFlowTrigger(new DataFlowT0());
        sdzBag.getStrand().addTransactionTrigger(new FormCloseTransactionT());
        sdzBag.getStrand().setErrorHandler(new HandlerT());
        sdzBag.getStrand().setEntityManagerTrigger(new EntityManagerT());
        //
        /*
        HoursValidationTrigger hvt = new HoursValidationTrigger( dt.mondayHoursTimeSpentAttribute);
        dt.mondayHoursTimeSpentAttribute.setItemValidationTrigger( hvt);
        hvt = new HoursValidationTrigger( dt.tuesdayHoursTimeSpentAttribute);
        dt.tuesdayHoursTimeSpentAttribute.setItemValidationTrigger( hvt);
        hvt = new HoursValidationTrigger( dt.wednesdayHoursTimeSpentAttribute);
        dt.wednesdayHoursTimeSpentAttribute.setItemValidationTrigger( hvt);
        hvt = new HoursValidationTrigger( dt.thursdayHoursTimeSpentAttribute);
        dt.thursdayHoursTimeSpentAttribute.setItemValidationTrigger( hvt);
        hvt = new HoursValidationTrigger( dt.fridayHoursTimeSpentAttribute);
        dt.fridayHoursTimeSpentAttribute.setItemValidationTrigger( hvt);
        hvt = new HoursValidationTrigger( dt.saturdayHoursTimeSpentAttribute);
        dt.saturdayHoursTimeSpentAttribute.setItemValidationTrigger( hvt);
        hvt = new HoursValidationTrigger( dt.sundayHoursTimeSpentAttribute);
        dt.sundayHoursTimeSpentAttribute.setItemValidationTrigger( hvt);
        */
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
                Print.prThrowable(e, "TimesheetTriggers.HandlerT");
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

                Collection c = queries.queryAllTimesheetsOf(resourceName, new Date());
                dt.timesheetCell.setData(c);
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


    /**
     * Will be able to use this when integrate with 'new' Swing
     * style of having an editor and doing item validation. See
     * code around 'TimeSpent/Editor' for how this will work.
     */
    static class HoursValidationTrigger implements ItemValidationTrigger
    {
        private RuntimeAttribute attribute;

        HoursValidationTrigger(RuntimeAttribute attribute)
        {
            this.attribute = attribute;
        }

        // Currently need to convert if for a table or field item. Change
        // TableSignatures.getText() to fix this.
        public void validateItem(ChangeEvent validationEvent)
        // throws ValidationException
        {
            Object value = attribute.getItemValue();
            Err.pr("Value got is a " + value.getClass().getName());

            TimeSpent txt = (TimeSpent) value;
            if(!attribute.isBlank())
            {/*
         try
         {
         //Print.pr( "validating <" + txt + ">");
         TimeSpent.parse( txt);
         }
         catch(ParseException ex)
         {
         attribute.setInError( true);
         throw new ValidationException( ex.getMessage());
         }
         */}
            attribute.setInError(false);
        }
    }

    private void setLOVs()
    {
    }
}
