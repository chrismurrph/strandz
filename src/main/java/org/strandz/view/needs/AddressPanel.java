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
package org.strandz.view.needs;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class AddressPanel extends JPanel
{
    JLabel lAddress;
    JLabel lStreet;
    JTextField tfStreet;
    JLabel lSuburb;
    JTextField tfSuburb;
    JLabel lPostcode;
    JTextField tfPostcode;
    // JLabel lCourier;
    // JTextField tfCourier;

    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    public AddressPanel()
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
        lAddress = new JLabel();
        lStreet = new JLabel();
        tfStreet = new JTextField();
        lSuburb = new JLabel();
        tfSuburb = new JTextField();
        lPostcode = new JLabel();
        tfPostcode = new JTextField();

        // lCourier = new JLabel();
        // tfCourier = new JTextField();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {BORDER, 0.37, GAP, 0.64, ModernTableLayout.FILL, BORDER},
                // Rows
                {
                    ModernTableLayout.FILL, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lAddress, "1, 1");
        add(lStreet, "1, 3");
        add(tfStreet, "3, 3");
        add(lSuburb, "1, 5");
        add(tfSuburb, "3, 5");
        add(lPostcode, "1, 7");
        add(tfPostcode, "3, 7");
        lAddress.setText("Address");
        lStreet.setHorizontalAlignment(SwingConstants.TRAILING);
        lStreet.setText("Street");
        tfStreet.setName("tfStreet");
        lSuburb.setHorizontalAlignment(SwingConstants.TRAILING);
        lSuburb.setText("Suburb");
        tfSuburb.setName("tfSuburb");
        lPostcode.setHorizontalAlignment(SwingConstants.TRAILING);
        lPostcode.setText("Postcode");
        tfPostcode.setName("tfPostcode");
        setName("AddressPanel");
    }
}
