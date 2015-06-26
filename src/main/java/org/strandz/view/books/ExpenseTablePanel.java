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
package org.strandz.view.books;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import mseries.ui.MDateEntryField;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.widgets.table.CellComponentCreatorI;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.util.Date;

public class ExpenseTablePanel extends JPanel
{
    private ComponentTableView table;

    static final int BORDER = 15;

    private class ExpenseTableCellComponentCreator implements CellComponentCreatorI
    {
        public JComponent createCell(int row, int col, boolean editableRow, String reason, boolean spacerRow)
        {
            JComponent result;
            Class clazz = table.getModel().getColumnClass( col);
            if(editableRow)
            {
                if(clazz == Date.class) //Picked up from our model, ie. Strandz
                {
                    result = new MDateEntryField();
                }
                else
                {
                    result = new JTextField();
                }
            }
            else
            {
                /* 
                 * TODO
                 * Doing this stops up and down cursor keys from working!
                 * Also making DateInTextComponentConvert stuffs things up in the same way
                 * - so problem is with Strandz code as the stack trace was fixing things!
                if(clazz == Date.class) //Picked up from our model, ie. Strandz
                {
                    result = new MDateEntryField();
                    result.setEnabled( false);
                }
                else
                */
                {
                    result = new JLabel();
                }
            }
            Err.pr( SdzNote.CTV_ADD_CELL, "In " + this.getClass().getName() + " have created a " + 
                    result.getClass().getName() + " because " + reason);
            result.setName( "ExpenseTableModel textfield at col " + col + ", row " + row + ", type <" + result.getClass().getName() + ">");
            return result;
        }
    }

    public ExpenseTablePanel()
    {
    }

    public Dimension getPreferredSize()
    {
        Dimension result = super.getPreferredSize();
        result.width = 500;
        result.height = 300;
        return result;
    }

    public void init()
    {
        table = new ComponentTableView(); //MyJTable
        //table.setSpacerRow( 1);
        table.setEditableRowMoves( true);
        ExpenseTableCellComponentCreator expenseTableCellComponentCreator = new ExpenseTableCellComponentCreator();
        table.setCellComponentCreator( expenseTableCellComponentCreator);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create a TableLayout for the panel
        double size[][] =
            {
                // Columns
                {ExpenseTablePanel.BORDER,
                 ModernTableLayout.FILL,
                 ExpenseTablePanel.BORDER},
                // Rows
                {//ExpenseTablePanel.BORDER, Don't want a border when another panel stacked on top
                 ModernTableLayout.FILL,
                 ExpenseTablePanel.BORDER
                }
            };
        setLayout(new ModernTableLayout(size));
        table.setName("Expense " + NameUtils.endOfDots( table.getClass().getName()));

        add(scrollPane, "1, 0");

        setName("ExpenseTablePanel");
    }

    public ComponentTableView getTable()
    {
        return table;
    }

    public void setTable(ComponentTableView table)
    {
        this.table = table;
    }
} // end class
