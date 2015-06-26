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
package org.strandz.data.books.objects;

import org.strandz.lgpl.data.objects.MoneyAmount;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.Utils;

import java.util.Date;

public class Expense implements Comparable
{
    private boolean dummy;
    public transient static Expense NULL = new Expense();
    private Date date;
    private ExpenseType expenseType;
    private MoneyAmount amount;
    private String description;

    private static final String equalsPropNames[] = {
        "dummy",
        "date",
        "expenseType",
        "amount",
        "description",
    };

    public Expense()
    {

    }

    public int compareTo(Object o)
    {
        int result = 0;
        Expense other = (Expense) o;
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
        result = Utils.compareTo(date, other.getDate());
        if(result == 0)
        {
            result = Utils.compareTo(expenseType, other.getExpenseType());
            if(result == 0)
            {
                result = Utils.compareTo(amount, other.getAmount());
                if(result == 0)
                {
                    result = Utils.compareTo(description, other.getAmount());
                }
            }
        }
        return result;
    }

    public boolean equals(Object o)
    {
        boolean result = false;
        Utils.chkType(o, this.getClass());

        String txt = "Expense " + this;
        ReasonNotEquals.addClassVisiting(txt);

        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Expense))
        {
            ReasonNotEquals.addReason("not an instance of an Expense");
        }
        else
        {
            Expense test = (Expense) o;
            result = SelfReferenceUtils.equalsByProperties(equalsPropNames, this, test);
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = SelfReferenceUtils.hashCodeByProperties(result, equalsPropNames, this);
        return result;
    }

    public String toString()
    {
        String result = null;
        result = "Expense: <" + getDate() + "> <" +
            getExpenseType() + "> <" + getAmount() + "> <" + getDescription() + ">";
        return result;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public ExpenseType getExpenseType()
    {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType)
    {
        this.expenseType = expenseType;
    }

    public MoneyAmount getAmount()
    {
        return amount;
    }

    public void setAmount(MoneyAmount amount)
    {
        this.amount = amount;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isDummy()
    {
        return dummy;
    }

    public void setDummy(boolean dummy)
    {
        this.dummy = dummy;
    }
}
