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
package org.strandz.store.books;

import org.strandz.data.books.objects.Expense;
import org.strandz.data.books.objects.ExpenseType;
import org.strandz.lgpl.data.objects.MoneyAmount;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The natural place for this class would be in the task package
 * however we don't want store.books depending back on a task
 * package, and there is just no need to go to the complication of
 * using interfaces to solve this problem.
 */
public class BooksDemoData
{
    public List newExpenses = new ArrayList();
    public List expenseTypeLovs;
    private static BooksDemoData instance;

    public static BooksDemoData getInstance()
    {
        BooksDemoData result = null;
        if(instance == null)
        {
            result = new BooksDemoData();
            result.fillLookupTables();
            result.doPopulation();
            instance = result;
        }
        else
        {
            result = instance;
        }
        return result;
    }

    private void fillLookupTables()
    {
        expenseTypeLovs = Arrays.asList(ExpenseType.OPEN_VALUES);
    }

    public void doPopulation()
    {
        Expense nullExpense = createNullExpense();
        newExpenses.add(nullExpense); //0

        newExpenses.add(createExpense( ExpenseType.ACCOUNTANCY_FEES, TimeUtils.getDate( 22, Calendar.MAY, 2006), new MoneyAmount( 50)));
        newExpenses.add(createExpense( ExpenseType.LAPTOP_DEPRECIATION, TimeUtils.getDate( 23, Calendar.MAY, 2006), new MoneyAmount( 51)));
        newExpenses.add(createExpense( ExpenseType.OFFICE_FURNITURE_STORAGE, TimeUtils.getDate( 24, Calendar.MAY, 2006), new MoneyAmount( 52)));
        newExpenses.add(createExpense( ExpenseType.ACCOUNTANCY_FEES, TimeUtils.getDate( 25, Calendar.MAY, 2006), new MoneyAmount( 53)));
        newExpenses.add(createExpense( ExpenseType.LAPTOP_DEPRECIATION, TimeUtils.getDate( 26, Calendar.MAY, 2006), new MoneyAmount( 54)));
    }

    /**
     * Initially set lookups to NULL to avoid NPEs.
     */
    private Expense createExpense()
    {
        Expense result = new Expense();
        result.setExpenseType((ExpenseType) Utils.getFromList(expenseTypeLovs, ExpenseType.NULL));
        return result;
    }

    private Expense createExpense(ExpenseType expenseType, Date date, MoneyAmount amount)
    {
        Expense result = new Expense();
        result.setDate(date);
        result.setAmount(amount);
        result.setExpenseType( expenseType);
        return result;
    }

    private Expense createNullExpense()
    {
        Expense result = Expense.NULL; //Never use NULL again, always use isDummy()
        result.setDummy(true);
        return result;
    }

    /**
     * Only exists to make calling code easier to read
     */
    Object obtainLOV(Object typeWant)
    {
        Object result = null;
        if(typeWant instanceof ExpenseType)
        {
            result = Utils.getFromList(expenseTypeLovs, typeWant);
        }
        else
        {
            Err.error("Populate not yet support " + typeWant);
        }
        if(result == null)
        {
            Err.error("Got a null when wanted a " + typeWant);
        }
        return result;
    }
}
