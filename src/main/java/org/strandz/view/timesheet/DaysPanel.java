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
package org.strandz.view.timesheet;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DaysPanel extends JPanel
{
    JLabel lMonday;
    JTextField tfMonday;
    JLabel lTuesday;
    JTextField tfTuesday;
    JLabel lWednesday;
    JTextField tfWednesday;
    JLabel lThursday;
    JTextField tfThursday;
    JLabel lFriday;
    JTextField tfFriday;
    JLabel lSaturday;
    JTextField tfSaturday;
    JLabel lSunday;
    JTextField tfSunday;
    // static final int GAP = 15;
    static final double BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    public DaysPanel()
    {
    }

    public void init()
    {
        /*
        setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.black),
        BorderFactory.createEmptyBorder(5,5,5,5)
        ));
        */
        // setBorder( new BevelBorder( BevelBorder.LOWERED));

        lMonday = new JLabel();
        tfMonday = new JTextField();
        lTuesday = new JLabel();
        tfTuesday = new JTextField();
        lWednesday = new JLabel();
        tfWednesday = new JTextField();
        lThursday = new JLabel();
        tfThursday = new JTextField();
        lFriday = new JLabel();
        tfFriday = new JTextField();
        lSaturday = new JLabel();
        tfSaturday = new JTextField();
        lSunday = new JLabel();
        tfSunday = new JTextField();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    BORDER, 0.09, 0.06, 0.09, 0.06, 0.09, 0.06, 0.09, 0.06, 0.09, 0.06, 0.09,
                    0.06, 0.09, BORDER},
                // Rows
                {
                    BORDER, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, ModernTableLayout.FILL,
                    BORDER
                }};
        setLayout(new ModernTableLayout(size));
        add(lMonday, "1, 1");
        add(tfMonday, "1, 3");
        add(lTuesday, "3, 1");
        add(tfTuesday, "3, 3");
        add(lWednesday, "5, 1");
        add(tfWednesday, "5, 3");
        add(lThursday, "7, 1");
        add(tfThursday, "7, 3");
        add(lFriday, "9, 1");
        add(tfFriday, "9, 3");
        add(lSaturday, "11, 1");
        add(tfSaturday, "11, 3");
        add(lSunday, "13, 1");
        add(tfSunday, "13, 3");
        lMonday.setHorizontalAlignment(SwingConstants.CENTER);
        lMonday.setText("Mon");
        tfMonday.setName("tfMonday");
        lTuesday.setHorizontalAlignment(SwingConstants.CENTER);
        lTuesday.setText("Tue");
        tfTuesday.setName("tfTuesday");
        lWednesday.setHorizontalAlignment(SwingConstants.CENTER);
        lWednesday.setText("Wed");
        tfWednesday.setName("tfWednesday");
        lThursday.setHorizontalAlignment(SwingConstants.CENTER);
        lThursday.setText("Thu");
        tfThursday.setName("tfThursday");
        lFriday.setHorizontalAlignment(SwingConstants.CENTER);
        lFriday.setText("Fri");
        tfFriday.setName("tfFriday");
        lSaturday.setHorizontalAlignment(SwingConstants.CENTER);
        lSaturday.setText("Sat");
        tfSaturday.setName("tfSaturday");
        lSunday.setHorizontalAlignment(SwingConstants.CENTER);
        lSunday.setText("Sun");
        tfSunday.setName("tfSunday");
        setName("DaysPanel");
    }
}
