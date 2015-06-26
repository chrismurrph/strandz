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

import org.strandz.lgpl.util.Clazz;
import org.strandz.core.interf.*;
import org.strandz.data.books.objects.Expense;
import org.strandz.data.books.objects.ExpenseType;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.view.books.MainExpensePanel;
import org.strandz.view.books.ExpenseFieldPanel;
import org.strandz.view.books.ExpenseTablePanel;

import javax.swing.JFrame;
import java.util.List;

public class ExpenseAppHelper
{
    Strand strand;
    Node expenseNode;
    Cell expenseCell;
    Cell expenseTypeCell;
    MainExpensePanel panel;

    RuntimeAttribute dateAttribute;
    RuntimeAttribute typeAttribute;
    RuntimeAttribute amountAttribute;
    RuntimeAttribute purposeAttribute;

    SdzBagI sbI;
    ExpenseAppHelper outer;

    public static final boolean VISUAL = true;

    public ExpenseAppHelper()
    {
        strand = new Strand();
        expenseNode = new Node();
        expenseCell = new Cell();
        expenseTypeCell = new Cell();
        outer = this;
    }

    public void sdzSetup()
    {
        if(ExpenseAppHelper.VISUAL)
        {
            panel = new MainExpensePanel();
            panel.init();
            MessageDlg.setDlgParent( panel);
        }
        expenseCell.setClazz(new Clazz(Expense.class));
        expenseCell.setName("Expense Cell");
        expenseTypeCell.setClazz(new Clazz(ExpenseType.class));
        expenseTypeCell.setName("Expense Type Cell (looked up by Worker)");
        NodeController nodeController = new NodeController();
        nodeController.setStrand(strand);
        detailSetup();
        expenseNode.setStrand(strand);
        if(panel != null)
        {
            setupTableControl();
        }
        boolean ok = strand.validateBean();
        if(!ok)
        {
            List msg = strand.retrieveValidateBeanMsg();
            Print.prList(msg, "How Strand not correctly setup");
            Err.error("Strand not setup correctly");
        }
    }
    
    private void setupTableControl()
    {
        if(Utils.instanceOf( panel.getDataPanel(), ExpenseTablePanel.class))
        {
            Object tablePanel = panel.getDataPanel();
            expenseNode.setTableControl(((ExpenseTablePanel)tablePanel).getTable());
        }
    }

    private void detailSetup()
    {
        RuntimeAttribute attr = newAttribute();
        attr.setDOField("date");
        attr.setName("Date");
        setOrdinal( attr, new Integer( 0));
        mapToItem( attr);
        attr.setEnabled( true);
        dateAttribute = attr;
        expenseCell.addAttribute(attr);

        attr = newAttribute();
        attr.setDOField("name");
        attr.setName("Expense Type");
        setOrdinal( attr, new Integer( 1));
        mapToItem( attr);
        attr.setEnabled( false);
        typeAttribute = attr;
        expenseTypeCell.addAttribute(attr);

        attr = newAttribute();
        attr.setDOField("amount");
        attr.setName("Amount");
        setOrdinal( attr, new Integer( 2));
        mapToItem( attr);
        amountAttribute = attr;
        expenseCell.addAttribute(attr);

        attr = newAttribute();
        attr.setDOField("description");
        attr.setName("Description");
        setOrdinal( attr, new Integer( 3));
        mapToItem( attr);
        purposeAttribute = attr;
        expenseCell.addAttribute(attr);
        //
        expenseNode.setCell(expenseCell);
        expenseTypeCell.setRefField("expenseType");
        expenseCell.setCell(expenseCell.getCells().length, expenseTypeCell);
    }
    
    private static void setOrdinal( RuntimeAttribute attr, Integer value)
    {
        if(attr instanceof TableAttribute)
        {
            attr.setOrdinal( value);
        }
    }
    
    private void mapToItem( RuntimeAttribute attr)
    {
        if(attr instanceof FieldAttribute)
        {
            FieldAttribute fieldAttribute = (FieldAttribute)attr;
            Object panel = outer.panel.getDataPanel();
            ExpenseFieldPanel fieldPanel = (ExpenseFieldPanel)panel;
            if(attr.getDOField().equals( "date"))
            {
                fieldAttribute.setItem( fieldPanel.getMdefDate());
            }
            else if(attr.getDOField().equals( "name"))
            {
                fieldAttribute.setItem( fieldPanel.getTfExpenseType());
            }
            else if(attr.getDOField().equals( "amount"))
            {
                fieldAttribute.setItem( fieldPanel.getTfAmt());
            }
            else if(attr.getDOField().equals( "description"))
            {
                fieldAttribute.setItem( fieldPanel.getTfDesc());
            }
            else
            {
                Err.error( "Don't know how to map <" + fieldAttribute + ">");
            }
        }
    }
    
    private static RuntimeAttribute newAttribute()
    {
        RuntimeAttribute result = null;
        if(MainExpensePanel.TABLE)
        {
            result = new TableAttribute();            
        }
        else
        {
            result = new FieldAttribute();            
        }
        return result;
    }

    void displayExpensePanel( JFrame frame)
    {
        DisplayUtils.display( frame, panel);
    }

    public Cell getExpenseTypeCell()
    {
        Cell result = expenseTypeCell;
        return result;
    }

    public Cell getExpenseCell()
    {
        Cell result = expenseCell;
        return result;
    }

    public Strand getStrand()
    {
        Strand result = strand;
        return result;
    }

    public Node getExpenseNode()
    {
        return expenseNode;
    }

    public MainExpensePanel getPanel()
    {
        return panel;
    }

    public RuntimeAttribute getDateAttribute()
    {
        return dateAttribute;
    }

}
