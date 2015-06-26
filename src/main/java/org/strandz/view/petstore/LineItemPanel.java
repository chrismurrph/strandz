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
package org.strandz.view.petstore;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class LineItemPanel extends JPanel
{
    JLabel lLineItem;
    JLabel lProduct;
    JComboBox cbProduct;
    JLabel lQuantity;
    JTextField tfQuantity;
    JLabel lUnitPrice;
    JTextField tfUnitPrice;
    // JLabel lCourier;
    // JTextField tfCourier;

    static final int MID_GAP = 7;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    public LineItemPanel()
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
        lLineItem = new JLabel();
        lProduct = new JLabel();
        cbProduct = new JComboBox();
        lQuantity = new JLabel();
        tfQuantity = new JTextField();
        lUnitPrice = new JLabel();
        tfUnitPrice = new JTextField();

        // lCourier = new JLabel();
        // tfCourier = new JTextField();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    BORDER, 0.20, GAP, 0.30, MID_GAP, 0.20, GAP, 0.30, ModernTableLayout.FILL,
                    BORDER},
                // Rows
                {
                    ModernTableLayout.FILL, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    TEXT_HEIGHT, SMALL_SPACING, ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lLineItem, "1, 1");
        add(lProduct, "1, 3");
        add(cbProduct, "3, 3");
        add(lQuantity, "5, 3");
        add(tfQuantity, "7, 3");
        add(lUnitPrice, "1, 5");
        add(tfUnitPrice, "3, 5");
        // add ( lCourier,              "5, 5");
        // add ( tfCourier,             "7, 5");

        // lOrder.setHorizontalAlignment( SwingConstants.LEADING);
        lLineItem.setText("Line Item");
        lProduct.setHorizontalAlignment(SwingConstants.TRAILING);
        lProduct.setText("Product");
        cbProduct.setName("cbProduct");
        lQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
        lQuantity.setText("Quantity");
        tfQuantity.setName("tfQuantity");
        lUnitPrice.setHorizontalAlignment(SwingConstants.TRAILING);
        lUnitPrice.setText("Unit Price");
        tfUnitPrice.setName("tfUnitPrice");
        // lCourier.setHorizontalAlignment( SwingConstants.TRAILING);
        // lCourier.setText( "Courier");
        // tfCourier.setName( "tfCourier");
        setName("LineItemPanel");
    }
}
