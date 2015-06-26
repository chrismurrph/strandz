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
package org.strandz.view.wombatrescue;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Dimension;

public class MonthYearPanel extends JPanel
{
    private JLabel lMonth;
    public JTextField tfMonth;
    private JLabel lYear;
    public JTextField tfYear;

    static final int GAP = 5;
    static final int BORDER = 15;
    static final int SMALL_SPACING = 2;
    static final int BIG_SPACING = 13;
    static final int TEXT_HEIGHT = 23;
    static final int BUTTON_1_WIDTH = 90;
    static final int BUTTON_2_WIDTH = 120;

    public void init()
    {
        lMonth = new JLabel();
        lYear = new JLabel();
        tfMonth = new JTextField();
        tfMonth.setEditable(false);
        tfYear = new JTextField();
        tfYear.setEditable(false);

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    BORDER, 0.10, GAP, 0.15, GAP, 0.10, GAP, 0.15, ModernTableLayout.FILL,
                    BUTTON_1_WIDTH, 0.05, BUTTON_2_WIDTH, 0.05, BORDER},
                // Rows
                {ModernTableLayout.FILL, BORDER, TEXT_HEIGHT, BORDER, ModernTableLayout.FILL}};
        setLayout(new ModernTableLayout(size));
        lMonth.setHorizontalAlignment(SwingConstants.TRAILING);
        lMonth.setText( "Month");
        tfMonth.setName( "tfMonth");
        lYear.setHorizontalAlignment(SwingConstants.TRAILING);
        lYear.setText( "Year");
        tfYear.setName( "tfYear");
        add(lMonth, "1, 2");
        add(tfMonth, "3, 2");
        add(lYear, "5, 2");
        add(tfYear, "7, 2");
        setName("MonthYearPanel");
    }
    
    public Dimension getPreferredSize()
    {
        Dimension result = super.getPreferredSize();
        result.height = 90;
        return result;
    }
} // end class
