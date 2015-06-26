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
package org.strandz.view.fishbowl;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class JobsPanel extends JPanel
{
    JLabel lDescription = new JLabel();
    public JTextField tfDescription = new JTextField();
    JLabel lStartingInstructions = new JLabel();
    public JTextField tfStartingInstructions = new JTextField();
    static final int GAP = 25;
    static final int BORDER = 15;
    static final int SPACING = 15;
    static final int TEXT_HEIGHT = 23;

    public JobsPanel()
    {
        // Create a TableLayout for the panel
        double size[][] = // Columns
            {{BORDER, 0.25, GAP, ModernTableLayout.FILL, BORDER}, // Rows
                {ModernTableLayout.FILL, TEXT_HEIGHT, SPACING, TEXT_HEIGHT, ModernTableLayout.FILL}};
        setLayout(new ModernTableLayout(size));
        lDescription.setHorizontalAlignment(SwingConstants.TRAILING);
        lDescription.setText("Description");
        tfDescription.setName("tfJobsDescription");
        lStartingInstructions.setHorizontalAlignment(SwingConstants.TRAILING);
        lStartingInstructions.setText("Starting Instructions");
        tfStartingInstructions.setName("tfStartingInstructions");
        add(lDescription, "1, 1");
        add(tfDescription, "3, 1");
        add(lStartingInstructions, "1, 3");
        add(tfStartingInstructions, "3, 3");
    }
} // end class
