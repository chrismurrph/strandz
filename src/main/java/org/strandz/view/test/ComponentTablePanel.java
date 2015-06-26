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
package org.strandz.view.test;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class ComponentTablePanel extends JPanel
{
    public JButton bAction;
    JLabel lColumnOne;
    JLabel lColumnTwo;
    JLabel lMon;
    public JTextField tfMonDinner;
    public JTextField tfMonOvernight;
    JLabel lTue;
    JComboBox cbTueDinner;
    JComboBox cbTueOvernight;
    JLabel lWed;
    JComboBox cbWedDinner;
    JComboBox cbWedOvernight;
    JLabel lFri;
    JComboBox cbFriDinner;
    JComboBox cbFriOvernight;
    JLabel lSat;
    JComboBox cbSatDinner;
    JComboBox cbSatOvernight;
    public static final int MID_GAP = 7;
    public static final int GAP = 15;
    public static final int BORDER = 20;
    public static final int SMALL_SPACING = 2;
    public static final int TEXT_HEIGHT = 23;

    public ComponentTablePanel()
    {
    }

    public void init()
    {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        bAction = new JButton();
        lColumnOne = new JLabel();
        lColumnTwo = new JLabel();
        lMon = new JLabel();
        tfMonDinner = new JTextField();
        tfMonOvernight = new JTextField();
        lTue = new JLabel();
        cbTueDinner = new JComboBox();
        cbTueOvernight = new JComboBox();
        lWed = new JLabel();
        cbWedDinner = new JComboBox();
        cbWedOvernight = new JComboBox();
        lFri = new JLabel();
        cbFriDinner = new JComboBox();
        cbFriOvernight = new JComboBox();
        lSat = new JLabel();
        cbSatDinner = new JComboBox();
        cbSatOvernight = new JComboBox();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {ComponentTablePanel.BORDER, 0.20, ComponentTablePanel.GAP, 0.30, ComponentTablePanel.MID_GAP, 0.30, ModernTableLayout.FILL, ComponentTablePanel.BORDER},
                // Rows
                {
                    ModernTableLayout.FILL, ComponentTablePanel.TEXT_HEIGHT, ComponentTablePanel.SMALL_SPACING, ComponentTablePanel.TEXT_HEIGHT, ComponentTablePanel.SMALL_SPACING,
                    ComponentTablePanel.TEXT_HEIGHT, ComponentTablePanel.SMALL_SPACING, ComponentTablePanel.TEXT_HEIGHT, ComponentTablePanel.SMALL_SPACING, ComponentTablePanel.TEXT_HEIGHT,
                    ComponentTablePanel.SMALL_SPACING, ComponentTablePanel.TEXT_HEIGHT, ComponentTablePanel.SMALL_SPACING, ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(bAction, "1, 1");
        add(lColumnOne, "3, 1");
        add(lColumnTwo, "5, 1");
        add(lMon, "1, 3");
        add(tfMonDinner, "3, 3");
        add(tfMonOvernight, "5, 3");
        add(lTue, "1, 5");
        add(cbTueDinner, "3, 5");
        add(cbTueOvernight, "5, 5");
        add(lWed, "1, 7");
        add(cbWedDinner, "3, 7");
        add(cbWedOvernight, "5, 7");
        add(lFri, "1, 9");
        add(cbFriDinner, "3, 9");
        add(cbFriOvernight, "5, 9");
        add(lSat, "1, 11");
        add(cbSatDinner, "3, 11");
        add(cbSatOvernight, "5, 11");
        bAction.setText("Action");
        lColumnOne.setText("Column One");
        lColumnOne.setHorizontalAlignment(SwingConstants.CENTER);
        lColumnTwo.setText("Column Two");
        lColumnTwo.setHorizontalAlignment(SwingConstants.CENTER);
        lMon.setHorizontalAlignment(SwingConstants.TRAILING);
        lMon.setText("Mon");
        tfMonDinner.setName("cbMonDinner");
        tfMonOvernight.setName("cbMonOvernight");
        lTue.setHorizontalAlignment(SwingConstants.TRAILING);
        lTue.setText("Tue");
        cbTueDinner.setName("cbTueDinner");
        cbTueOvernight.setName("cbTueOvernight");
        lWed.setHorizontalAlignment(SwingConstants.TRAILING);
        lWed.setText("Wed");
        cbWedDinner.setName("cbWedDinner");
        cbWedOvernight.setName("cbWedOvernight");
        lFri.setHorizontalAlignment(SwingConstants.TRAILING);
        lFri.setText("Fri");
        cbFriDinner.setName("cbFriDinner");
        cbFriOvernight.setName("cbFriOvernight");
        lSat.setHorizontalAlignment(SwingConstants.TRAILING);
        lSat.setText("Sat");
        cbSatDinner.setName("cbSatDinner");
        cbSatOvernight.setName("cbSatOvernight");
        setName("ShiftManagersPanel");
    }
}
