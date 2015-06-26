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
package org.strandz.view.supersix;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import org.strandz.lgpl.widgets.WidgetUtils;
import org.strandz.lgpl.widgets.ROJLabel;

import java.awt.Font;

public class TeamPanel extends JPanel
{
    JLabel lTeam;
    JLabel lName;
    JTextField tfName;
    JLabel lDivisionName;
    ROJLabel lDivision;
    JLabel lQuantity;
    JTextField tfQuantity;
    JCheckBox chkPaid;

    static final int MID_GAP = 7;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    private static Font TITLE_FONT = new Font("Dialog", Font.BOLD, 14);

    public TeamPanel()
    {
    }

    public void init()
    {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lTeam = new JLabel();
        WidgetUtils.setLabelProperties( lTeam, null, TITLE_FONT, null);
        lName = new JLabel();
        tfName = new JTextField();
        lDivisionName = new JLabel();
        lDivision = new ROJLabel();
        lQuantity = new JLabel();
        tfQuantity = new JTextField();
        chkPaid = new JCheckBox();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    TeamPanel.BORDER, 
                    0.20, 
                    TeamPanel.GAP, 
                    0.30,
                    TeamPanel.MID_GAP, 
                    0.20, 
                    TeamPanel.GAP, 
                    0.30, 
                    ModernTableLayout.FILL,
                    TeamPanel.BORDER},
                // Rows
                {
                    ModernTableLayout.FILL, TeamPanel.TEXT_HEIGHT, TeamPanel.SMALL_SPACING, TeamPanel.TEXT_HEIGHT, TeamPanel.SMALL_SPACING,
                    TeamPanel.TEXT_HEIGHT, TeamPanel.SMALL_SPACING, TeamPanel.TEXT_HEIGHT, TeamPanel.SMALL_SPACING, ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lTeam, "1, 1");
        add(lName, "1, 3");
        add(tfName, "3, 3");
        add(lDivisionName, "1, 5");
        add(lDivision, "3, 5");
        add(chkPaid, "1,7,3,7");
        //add(lAddr1, "5, 3");
        //add(tfAddr1, "7, 3");

        lTeam.setText("Team");
        lName.setHorizontalAlignment(SwingConstants.TRAILING);
        lName.setText("Name");
        tfName.setName("tfName");
        lDivisionName.setHorizontalAlignment(SwingConstants.TRAILING);
        lDivisionName.setText("Division");
        lDivision.setName("lDivision");
        lQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
        lQuantity.setText("Quantity");
        tfQuantity.setName("tfQuantity");
        chkPaid.setText("Has Paid");
        chkPaid.setName("chkPaid");
        chkPaid.setHorizontalTextPosition( SwingConstants.LEADING);
        setName("TeamPanel");
    }
}
