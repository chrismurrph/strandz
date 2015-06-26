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
package org.strandz.view.fault;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class SupportCallPanel extends JPanel
{
    JLabel lSupportCall;
    JLabel lClientContact;
    JComboBox cbClientContact;
    JTextArea taComments;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    public void init()
    {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lSupportCall = new JLabel();
        lClientContact = new JLabel();
        cbClientContact = new JComboBox();
        taComments = new JTextArea();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {BORDER, 0.30, GAP, 0.40, ModernTableLayout.FILL, BORDER},
                // Rows
                {
                    BORDER, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    ModernTableLayout.FILL, BORDER
                }};
        setLayout(new ModernTableLayout(size));
        add(lSupportCall, "1, 1");
        add(lClientContact, "1, 3");
        add(cbClientContact, "3, 3");

        JScrollPane sp = new JScrollPane(taComments);
        add(sp, "1, 5, 4, 5");
        lSupportCall.setText("Support Call");
        lClientContact.setHorizontalAlignment(SwingConstants.TRAILING);
        lClientContact.setText("Client Contact");
        cbClientContact.setName("cbClientContact");
        taComments.setName("taComments");
        setName("SupportCallPanel");
    }
}
