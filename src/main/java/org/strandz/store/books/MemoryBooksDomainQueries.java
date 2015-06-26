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
import org.strandz.lgpl.store.DomainQueries;
import org.strandz.lgpl.store.LoopyQuery;
import org.strandz.lgpl.util.Utils;

import java.util.Date;

/**
 *
 */
public class MemoryBooksDomainQueries extends DomainQueries
{
    /*
    * Duplicating this:
    * "date descending"
    */
    public static int expenseCf(Object one, Object two)
    {
        int result = -99; //not relevant
        if(!(one instanceof Expense))
        {
            return 1;
        }
        if(!(two instanceof Expense))
        {
            return 1;
        }
        Expense expense1 = (Expense) one;
        Expense expense2 = (Expense) two;
        Date date1 = expense1.getDate();
        Date date2 = expense2.getDate();
        //Have reversed these to get the descending effect
        result = Utils.compareTo(date2, date1);
        return result;
    }

    public MemoryBooksDomainQueries()
    {
        initStableQueries();
    }

    private void initStableQueries()
    {
        initQuery(BooksQueryEnum.ALL_EXPENSE,
            new LoopyQuery(
                BooksData.EXPENSE,
                BooksQueryEnum.ALL_EXPENSE.getDescription())
            {
                public boolean match(Object row)
                {
                    boolean result = false;
                    Expense expense = (Expense) row;
                    if(!expense.isDummy())
                    {
                        result = true;
                    }
                    return result;
                }

//                public int compare(Object one, Object two)
//                {
//                    return BooksQueries.expenseCf(one, two);
//                }
            });
        initQuery(BooksQueryEnum.EXPENSE_TYPE,
            new LoopyQuery(
                BooksData.EXPENSE_TYPE,
                BooksQueryEnum.EXPENSE_TYPE.getDescription())
            {
            });
    }

}
