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
package org.strandz.task.books;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.data.books.objects.Expense;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.table.ColumnWidthsInformerI;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.store.books.BooksDataStoreFactory;
import org.strandz.store.books.BooksDemoData;
import org.strandz.store.books.BooksQueryEnum;
import org.strandz.view.books.ExpenseTablePanel;

import javax.swing.event.ChangeEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Ingredients:
 * An expense
 * An ExpensePanel to display the expense in
 * A Strand to pull expense onto ExpensePanel
 * A DataStore to communicate with a database
 */
public class ExpenseSdzExample0
{
    private DataStore dataStore;
    private List data;
    private ExpenseAppHelper helper;
    private QueryActionListener queryActionListener = new QueryActionListener();
    private PostActionListener postActionListener = new PostActionListener();
    private InsertActionListener insertActionListener = new InsertActionListener();
    private DeleteActionListener deleteActionListener = new DeleteActionListener();
    private ShowDataActionListener showDataActionListener = new ShowDataActionListener();
    private DebugActionListener debugActionListener = new DebugActionListener();
    private DateValidationTrigger dateValidationTrigger;

    private static final boolean USE_DATASTORE = false;

    private class ExpenseTableColumnWidthsInformer implements ColumnWidthsInformerI
    {
        public double getColumnWidthAt(int column, int numOfColumns)
        {
            double result = Utils.floatDivide( 1, numOfColumns);
            return result;
        }
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI()
    {
        Err.setBatch( false);
        ExpenseSdzExample0 obj = new ExpenseSdzExample0();
        obj.init();
    }

    class QueryActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            helper.getStrand().EXECUTE_QUERY();
            //helper.getPanel().getOtherMatchDetailsPanel().getTable().modelChanged();
        }
    }

    class InsertActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            helper.getStrand().INSERT_AFTER_PLACE();
            //ExpenseTablePanel panel = (ExpenseTablePanel)helper.getPanel().getDataPanel();
            //panel.getTable().insert();
        }
    }

    class DeleteActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            helper.getStrand().REMOVE();
        }
    }

    class PostActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            helper.getStrand().POST();
        }
    }

    class ShowDataActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Print.prList( data, "Data kept in client memory, that may have been altered by the user", false);
        }
    }
    
    class DebugActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            debugAction();
        }
    }

    private void debugAction()
    {
        if(helper.getPanel().getDataPanel() instanceof ExpenseTablePanel)
        {
            ExpenseTablePanel panel = (ExpenseTablePanel)helper.getPanel().getDataPanel();
            Print.prList( panel.getTable().getListOfEditableRowControls(),
                          "Which row do these controls show as being on?");
            Print.prList( panel.getTable().getListOfNonEditableRowControls(),
                          "Non editables");
            Err.pr( "Table thinks are on row: " + panel.getTable().getSelectedRow());
            Err.pr( "Node thinks are on row: " + helper.getExpenseNode().getRow());
            panel.getTable().debugEditableControls();
        }
    }

    static class DateValidationTrigger implements ItemValidationTrigger
    {
        public void validateItem(ChangeEvent validationEvent)
            throws ValidationException
        {
            RuntimeAttribute attribute = (RuntimeAttribute)validationEvent.getSource();
            String txt = attribute.getItemValue().toString();
            if(!Utils.isBlank(txt))
            {
                try
                {
                    TimeUtils.DATE_FORMAT.parse(txt);
                }
                catch(ParseException ex)
                {
                    attribute.setInError(true);
                    Err.pr( "Failed date: <" + txt + "> is not in format <" + TimeUtils.DATE_PARSE_STRING + ">");
                    throw new ValidationException("Date fails validation");
                }
            }
            attribute.setInError(false);
        }
    }

    public void init()
    {
        JFrame frame = new JFrame();
        MessageDlg.setFrame( frame);
        helper = new ExpenseAppHelper();
        helper.sdzSetup();
        initDataStore();
        initEvents();
        helper.getExpenseNode().addDataFlowTrigger(new ExpenseSdzExample0.LocalDataFlowListener());
        helper.getStrand().setErrorHandler(new HandlerT());
        //helper.getStrand().setEntityManagerTrigger(new EntityManagerT());
        //dateValidationTrigger = new DateValidationTrigger();
        //helper.getDateAttribute().setItemValidationTrigger( dateValidationTrigger);
        ExpenseTableColumnWidthsInformer expenseTableColumnWidthsInformer = new ExpenseTableColumnWidthsInformer();
        //helper.getPanel().getOtherMatchDetailsPanel().getTable().setColumnWidthsInformer( expenseTableColumnWidthsInformer);
        helper.displayExpensePanel( frame);
        helper.getExpenseNode().GOTO();
    }

    private void initDataStore()
    {
        if(USE_DATASTORE)
        {
            DataStoreFactory dataStoreFactory = new BooksDataStoreFactory( true);
            DataStore dataStore = dataStoreFactory.getDataStore();
            this.dataStore = dataStore;
        }
    }

    private void initEvents()
    {
        helper.getPanel().getButtonPanel().getBQuery().addActionListener( queryActionListener);
        helper.getPanel().getButtonPanel().getBInsert().addActionListener( insertActionListener);
        helper.getPanel().getButtonPanel().getBDelete().addActionListener( deleteActionListener);
        helper.getPanel().getButtonPanel().getBPost().addActionListener( postActionListener);
        helper.getPanel().getButtonPanel().getBShowData().addActionListener( showDataActionListener);
        helper.getPanel().getButtonPanel().getBDebugTable().addActionListener( debugActionListener);
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
                Print.prThrowable(e, "ExpenseSdzExample0");
            }
        }
    }
    
    /*
    class EntityManagerT implements EntityManagerTriggerI
    {
        public SdzEntityManagerI fetchEM()
        {
            return ((EntityManagedDataStore)dataStore).getEM();
        }
    }
    */

    private void query()
    {
        if(dataStore != null)
        {
            dataStore.rollbackTx();
            dataStore.startTx();

            data = dataStore.getDomainQueries().executeRetList(
                    BooksQueryEnum.ALL_EXPENSE);
        }
        else
        {
            /*
            data = BooksDemoData.getInstance().newExpenses;
            data.remove(Expense.NULL);
            */
            data = new ArrayList();
        }
        Collections.reverse( data);
        helper.getExpenseCell().setData(data);
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                query();
            }
        }
    }
}
