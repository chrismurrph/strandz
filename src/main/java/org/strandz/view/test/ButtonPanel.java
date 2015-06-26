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

import javax.swing.JPanel;
import javax.swing.JButton;

public class ButtonPanel extends JPanel
{
    private JButton bDebug;
    private JButton bOtherButton;
    static final int GAP = 15;
    static final int BORDER = 15;
    static final int TEXT_HEIGHT = 23;

    public void init()
    {
        bDebug = new JButton();
        bDebug.setText( "Debug");
        bOtherButton = new JButton();
        bOtherButton.setText( "Other Button");

        // Create a TableLayout for the panel
        double size[][] =
            {
                // Columns
                {ModernTableLayout.FILL, org.strandz.view.test.ButtonPanel.BORDER, 0.30, org.strandz.view.test.ButtonPanel.GAP, 0.35, org.strandz.view.test.ButtonPanel.BORDER, ModernTableLayout.FILL},
                // Rows
                {ModernTableLayout.FILL, org.strandz.view.test.ButtonPanel.BORDER, org.strandz.view.test.ButtonPanel.TEXT_HEIGHT, org.strandz.view.test.ButtonPanel.BORDER, ModernTableLayout.FILL}
            };
        setLayout(new ModernTableLayout(size));
        bDebug.setName("bDebug");
        bOtherButton.setName("bOtherButton");
        add(bDebug, "2, 2");
        //Not yet needed
        //add(bOtherButton, "4, 2");
        setName("ButtonPanel");
    }

    public JButton getBDebug()
    {
        return bDebug;
    }

    public void setBDebug(JButton bDebug)
    {
        this.bDebug = bDebug;
    }

    public JButton getBShowData()
    {
        return bOtherButton;
    }

    public void setBShowData(JButton bShowData)
    {
        this.bOtherButton = bShowData;
    }
}
