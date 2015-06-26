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
import org.strandz.widgets.data.wombatrescue.FlexibilityRadioButtons;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SimpleWorkerPanel extends JPanel
{
    private JLabel lChristianName;
    private JTextField tfChristianName;
    private JLabel lSurname;
    private JTextField tfSurname;
    private FlexibilityRadioButtons frbFlexibilityRadioButtons;

    static final int GAP = 15;
    static final int BORDER = 15;
    static final int BIG_SPACING = 13;
    static final int TEXT_HEIGHT = 23;

    public SimpleWorkerPanel()
    {
    }

    public void init()
    {
        lChristianName = new JLabel();
        tfChristianName = new JTextField();
        lSurname = new JLabel();
        tfSurname = new JTextField();
        frbFlexibilityRadioButtons = new FlexibilityRadioButtons();
        frbFlexibilityRadioButtons.init();

        // Create a TableLayout for the panel
        double size[][] =
            {
                // Columns
                {BORDER, 0.17, GAP, 0.32, GAP, ModernTableLayout.FILL},
                // Rows
                {BORDER, TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT, BORDER}
            };
        setLayout(new ModernTableLayout(size));
        lChristianName.setHorizontalAlignment(SwingConstants.TRAILING);
        lChristianName.setText("First Name");
        tfChristianName.setName("tfChristianName");
        lSurname.setHorizontalAlignment(SwingConstants.TRAILING);
        lSurname.setText("Surname");
        tfSurname.setName("tfSurname");
        frbFlexibilityRadioButtons.setName("frbFlexibilityRadioButtons");

        add(lChristianName, "1, 1");
        add(tfChristianName, "3, 1");
        add(lSurname, "1, 3");
        add(tfSurname, "3, 3");
        add(frbFlexibilityRadioButtons, "5,1,5,3");

        setName("SimpleWorkerPanel");
    }
} // end class

