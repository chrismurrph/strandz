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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import java.awt.Color;

public class ExpenseFieldPanel extends JPanel
{
    JLabel lDate;
    JLabel lExpenseType;
    JLabel lAmt;
    JLabel lDesc;
    MDateEntryField mdefDate;
    JTextField tfExpenseType;
    JTextField tfAmt;
    JTextField tfDesc;
    static final int MID_GAP = 7;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    public ExpenseFieldPanel()
    {
    }

    public void init()
    {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.black),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lDate = new JLabel();
        lExpenseType = new JLabel();
        lAmt = new JLabel();
        lDesc = new JLabel();
        mdefDate = new MDateEntryField();
        tfExpenseType = new JTextField();
        tfAmt = new JTextField();
        tfDesc = new JTextField();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {ExpenseFieldPanel.BORDER, 0.15, ExpenseFieldPanel.GAP, 0.25, ExpenseFieldPanel.MID_GAP, 0.25, ExpenseFieldPanel.GAP, 0.25,  ExpenseFieldPanel.BORDER},
                // Rows
                {
                    ModernTableLayout.FILL, ExpenseFieldPanel.TEXT_HEIGHT, ExpenseFieldPanel.SMALL_SPACING, ExpenseFieldPanel.TEXT_HEIGHT, ExpenseFieldPanel.SMALL_SPACING,
                    ExpenseFieldPanel.TEXT_HEIGHT, ExpenseFieldPanel.SMALL_SPACING, ExpenseFieldPanel.TEXT_HEIGHT, ExpenseFieldPanel.SMALL_SPACING, ExpenseFieldPanel.TEXT_HEIGHT,
                    ExpenseFieldPanel.SMALL_SPACING, ExpenseFieldPanel.TEXT_HEIGHT, ExpenseFieldPanel.SMALL_SPACING, ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lDate, "1, 1");
        add(lExpenseType, "3, 1");
        add(lAmt, "5, 1");
        add(lDesc, "7, 1");
        add(mdefDate, "1, 3");
        add(tfExpenseType, "3, 3");
        add(tfAmt, "5, 3");
        add(tfDesc, "7, 3");
        lDate.setText("Date");
        lExpenseType.setText("Expense Type");
        lExpenseType.setHorizontalAlignment(SwingConstants.CENTER);
        lAmt.setText("Amount");
        lAmt.setHorizontalAlignment(SwingConstants.CENTER);
        lDesc.setHorizontalAlignment(SwingConstants.CENTER);
        lDesc.setText("Desc");
        mdefDate.setName("mdefDate");
        tfExpenseType.setName("tfExpenseType");
        tfAmt.setName("tfAmt");
        tfDesc.setName("tfDesc");
        setName("ExpenseFieldPanel");
    }


    public MDateEntryField getMdefDate()
    {
        return mdefDate;
    }

    public JTextField getTfExpenseType()
    {
        return tfExpenseType;
    }

    public JTextField getTfAmt()
    {
        return tfAmt;
    }

    public JTextField getTfDesc()
    {
        return tfDesc;
    }
}
