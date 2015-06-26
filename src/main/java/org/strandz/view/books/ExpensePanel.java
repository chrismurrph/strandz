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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dimension;

public class ExpensePanel extends JPanel
{
    private JLabel lDate;
    private JTextField tfDate;
    private JLabel lType;
    private JTextField tfType;
    private JLabel lAmount;
    private JTextField tfAmount;
    private JLabel lPurpose;
    private JTextField tfPurpose;

    static final int GAP = 15;
    static final int BORDER = 15;
    static final int SPACE = 3;
    static final int TEXT_HEIGHT = 23;

    public ExpensePanel()
    {
    }

    public Dimension getPreferredSize()
    {
        Dimension result = super.getPreferredSize();
        result.width = 700;
        return result;
    }

    public void init()
    {
        lDate = new JLabel();
        tfDate = new JTextField();
        lType = new JLabel();
        tfType = new JTextField();
        lAmount = new JLabel();
        tfAmount = new JTextField();
        lPurpose = new JLabel();
        tfPurpose = new JTextField();

        // Create a TableLayout for the panel
        double size[][] =
            {
                // Columns
                {ExpensePanel.BORDER,
                    0.08, SPACE, 0.16, ExpensePanel.GAP,
                    0.08, SPACE, 0.16, ExpensePanel.GAP,
                    0.08, SPACE, 0.16, ExpensePanel.GAP,
                    0.08, SPACE, 0.16, ExpensePanel.GAP,
                    ExpensePanel.BORDER},
                // Rows
                {ExpensePanel.TEXT_HEIGHT,
                 ExpensePanel.TEXT_HEIGHT,
                 ExpensePanel.TEXT_HEIGHT
                }
            };
        setLayout(new ModernTableLayout(size));
        lDate.setHorizontalAlignment(SwingConstants.TRAILING);
        lDate.setText("Date");
        tfDate.setName("tfDate");
        lType.setHorizontalAlignment(SwingConstants.TRAILING);
        lType.setText("Type");
        tfType.setName("tfType");
        lAmount.setHorizontalAlignment(SwingConstants.TRAILING);
        lAmount.setText("Amount");
        tfAmount.setName("tfAmount");
        lPurpose.setHorizontalAlignment(SwingConstants.TRAILING);
        lPurpose.setText("Purpose");
        tfPurpose.setName("tfPurpose");

        add(lDate, "1, 1");
        add(tfDate, "3, 1");
        add(lType, "5, 1");
        add(tfType, "7, 1");
        add(lAmount, "9, 1");
        add(tfAmount, "11, 1");
        add(lPurpose, "13, 1");
        add(tfPurpose, "15, 1");

        setName("ExpensePanel");
    }
} // end class
