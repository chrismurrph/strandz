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
package org.strandz.lgpl.view;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Intended that get the FillerPanel and add() to it, and what you add
 * will completely take it up.
 */
public class ThreeButtonWrapperPanel extends JPanel
{
    private JPanel fillerPanel;
    private ThreeButtonPanel threeButton;

    static final int BUTTON_PANEL_HEIGHT = 35;
    public static final String POST = "Post";
    public static final String ADD = "Add";
    public static final String DELETE = "Delete";

    public ThreeButtonWrapperPanel()
    {
    }

    public void init()
    {
        fillerPanel = new JPanel();
        threeButton = new ThreeButtonPanel( new String[]{ POST, ADD, DELETE});
        threeButton.init();
//        JButton bPost = threeButton.getBOne();
//        JButton bAdd = threeButton.getBTwo();
//        JButton bDelete = threeButton.getBThree();
//        bPost.setText( POST);
//        bAdd.setText( ADD);
//        bDelete.setText( DELETE);
        // Create a TableLayout for the panel
        double size[][] =
            {
                // Columns
                {ModernTableLayout.FILL},
                // Rows
                {ModernTableLayout.FILL, BUTTON_PANEL_HEIGHT}
            };
        setLayout( new ModernTableLayout(size));
        fillerPanel.setName( "fillerPanel");
        fillerPanel.setLayout( new BorderLayout());
        add( fillerPanel, "0,0");
        add( threeButton, "0,1");

        setFillerPanel( fillerPanel);
        setThreeButton( threeButton);
        setName( "ThreeButtonWrapperPanel");
    }

    /**
     * In the future this preferred size may be overridden and will vary
     * according to the width of the name
     */
    public Dimension getPreferredSize()
    {
        Dimension result = super.getPreferredSize();
        result.width = 430;
        return result;
    }

    public ThreeButtonPanel getThreeButton()
    {
        return threeButton;
    }

    public void setThreeButton(ThreeButtonPanel threeButton)
    {
        this.threeButton = threeButton;
    }

    public JPanel getFillerPanel()
    {
        return fillerPanel;
    }

    public void setFillerPanel(JPanel fillerPanel)
    {
        this.fillerPanel = fillerPanel;
    }
} // end class