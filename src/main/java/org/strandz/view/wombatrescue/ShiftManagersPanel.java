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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

public class ShiftManagersPanel extends JPanel
{
    JLabel lShiftManager;
    JLabel lDinner;
    JLabel lOvernight;
    JLabel lMon;
    JComboBox cbMonDinner;
    JComboBox cbMonOvernight;
    JLabel lTue;
    JComboBox cbTueDinner;
    JComboBox cbTueOvernight;
    JLabel lWed;
    JComboBox cbWedDinner;
    JComboBox cbWedOvernight;
    JLabel lThu;
    JComboBox cbThuDinner;
    JComboBox cbThuOvernight;
    JLabel lFri;
    JComboBox cbFriDinner;
    JComboBox cbFriOvernight;
    JLabel lSat;
    JComboBox cbSatDinner;
    JComboBox cbSatOvernight;
    JButton bSave;
    
    static final int MID_GAP = 7;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    public ShiftManagersPanel()
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
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lShiftManager = new JLabel();
        lDinner = new JLabel();
        lOvernight = new JLabel();
        lMon = new JLabel();
        cbMonDinner = new JComboBox();
        cbMonOvernight = new JComboBox();
        lTue = new JLabel();
        cbTueDinner = new JComboBox();
        cbTueOvernight = new JComboBox();
        lWed = new JLabel();
        cbWedDinner = new JComboBox();
        cbWedOvernight = new JComboBox();
        lThu = new JLabel();
        cbThuDinner = new JComboBox();
        cbThuOvernight = new JComboBox();
        lFri = new JLabel();
        cbFriDinner = new JComboBox();
        cbFriOvernight = new JComboBox();
        lSat = new JLabel();
        cbSatDinner = new JComboBox();
        cbSatOvernight = new JComboBox();
        bSave = new JButton();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {BORDER, 0.20, GAP, 0.30, MID_GAP, 0.30, MID_GAP, ModernTableLayout.FILL, BORDER},
                // Rows
                {
                    ModernTableLayout.FILL, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT,
                    SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT,  
                    ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lShiftManager, "1, 1");
        add(lDinner, "3, 1");
        add(lOvernight, "5, 1");
        add(lMon, "1, 3");
        add(cbMonDinner, "3, 3");
        add(cbMonOvernight, "5, 3");
        add(lTue, "1, 5");
        add(cbTueDinner, "3, 5");
        add(cbTueOvernight, "5, 5");
        add(lWed, "1, 7");
        add(cbWedDinner, "3, 7");
        add(cbWedOvernight, "5, 7");
        add(lThu, "1, 9");
        add(cbThuDinner, "3, 9");
        add(cbThuOvernight, "5, 9");
        add(lFri, "1, 11");
        add(cbFriDinner, "3, 11");
        add(cbFriOvernight, "5, 11");
        add(lSat, "1, 13");
        add(cbSatDinner, "3, 13");
        add(cbSatOvernight, "5, 13");
        add(bSave, "7,14, 7,15");
        lShiftManager.setText("Shift Managers");
        lDinner.setText("Dinner");
        lDinner.setHorizontalAlignment(SwingConstants.CENTER);
        lOvernight.setText("Overnight");
        lOvernight.setHorizontalAlignment(SwingConstants.CENTER);
        lMon.setHorizontalAlignment(SwingConstants.TRAILING);
        lMon.setText("Mon");
        cbMonDinner.setName("cbMonDinner");
        cbMonOvernight.setName("cbMonOvernight");
        lTue.setHorizontalAlignment(SwingConstants.TRAILING);
        lTue.setText("Tue");
        cbTueDinner.setName("cbTueDinner");
        cbTueOvernight.setName("cbTueOvernight");
        lWed.setHorizontalAlignment(SwingConstants.TRAILING);
        lWed.setText("Wed");
        cbWedDinner.setName("cbWedDinner");
        cbWedOvernight.setName("cbWedOvernight");
        lThu.setHorizontalAlignment(SwingConstants.TRAILING);
        lThu.setText("Thu");
        cbThuDinner.setName("cbThuDinner");
        cbThuOvernight.setName("cbThuOvernight");
        lFri.setHorizontalAlignment(SwingConstants.TRAILING);
        lFri.setText("Fri");
        cbFriDinner.setName("cbFriDinner");
        cbFriOvernight.setName("cbFriOvernight");
        lSat.setHorizontalAlignment(SwingConstants.TRAILING);
        lSat.setText("Sat");
        cbSatDinner.setName("cbSatDinner");
        cbSatOvernight.setName("cbSatOvernight");
        bSave.setText( "Save Changes");
        setName("ShiftManagersPanel");
        setBSave( bSave);
    }

    public JButton getBSave()
    {
        return bSave;
    }

    public void setBSave(JButton bSave)
    {
        this.bSave = bSave;
    }
}
