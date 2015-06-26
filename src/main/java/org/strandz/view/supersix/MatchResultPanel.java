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
import org.strandz.lgpl.widgets.ROJLabel;
import org.strandz.lgpl.widgets.WidgetUtils;
import org.strandz.view.util.EnclosedFocusTraversalPolicy;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class MatchResultPanel extends JPanel
{
    JLabel lScore;
    private ROJLabel lTeamOne;
    private JTextField tfTeamOneGoals;
    private ROJLabel lTeamTwo;
    private JTextField tfTeamTwoGoals;
    static final int GAP = 5;
    static final int BORDER = 15;
    static final int TEXT_HEIGHT = 23;
    static final int GOAL_WIDTH = 25;
    static final int SMALL_SPACING = 2;

    public MatchResultPanel()
    {
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(new EnclosedFocusTraversalPolicy( this));
    }

    public void init()
    {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lScore = new JLabel();
        WidgetUtils.setLabelProperties( lScore, null, SuperSixViewConstants.TITLE_FONT, null);
        lTeamOne = new ROJLabel();
        lTeamTwo = new ROJLabel();
        tfTeamOneGoals = new JTextField();
        tfTeamOneGoals.setEditable(false);
        tfTeamTwoGoals = new JTextField();
        tfTeamTwoGoals.setEditable(false);

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    BORDER,
                    0.35, GAP, GOAL_WIDTH, GAP, 0.35, GAP, GOAL_WIDTH,
                    BORDER},
                // Rows
                {
                 MatchResultPanel.BORDER,
                 MatchResultPanel.TEXT_HEIGHT,
                 MatchResultPanel.SMALL_SPACING,
                 ModernTableLayout.FILL,
                 MatchResultPanel.TEXT_HEIGHT,
                 MatchResultPanel.SMALL_SPACING,
                 MatchResultPanel.BORDER,
                 ModernTableLayout.FILL
                }};
        setLayout(new ModernTableLayout(size));
        lScore.setText("Score");
        lTeamOne.setHorizontalAlignment(SwingConstants.TRAILING);
        lTeamOne.setText("First Team");
        lTeamOne.setName("lTeamOne");
        tfTeamOneGoals.setName("tfTeamOneGoals");
        lTeamTwo.setHorizontalAlignment(SwingConstants.TRAILING);
        lTeamTwo.setText("Second Team");
        lTeamTwo.setName("lTeamTwo");
        tfTeamTwoGoals.setName("tfTeamTwoGoals");
        add(lScore, "1, 1");
        add(lTeamOne, "1, 4");
        add(tfTeamOneGoals, "3, 4");
        add(lTeamTwo, "5, 4");
        add(tfTeamTwoGoals, "7, 4");
        setName("MatchResultPanel");
        setLTeamOne( lTeamOne);
        setLTeamTwo( lTeamTwo);
    }

    public ROJLabel getLTeamOne()
    {
        return lTeamOne;
    }

    public void setLTeamOne(ROJLabel lTeamOne)
    {
        this.lTeamOne = lTeamOne;
    }

    public ROJLabel getLTeamTwo()
    {
        return lTeamTwo;
    }

    public void setLTeamTwo(ROJLabel lTeamTwo)
    {
        this.lTeamTwo = lTeamTwo;
    }
} // end class
