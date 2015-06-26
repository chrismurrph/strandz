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
import org.strandz.lgpl.widgets.DebugComboBox;
import org.strandz.view.util.EnclosedFocusTraversalPolicy;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class OtherMatchDetailsPanel extends JPanel
{
    JLabel lOtherDetails;
    JLabel lDivisionName;
    JComboBox cbDivision;
    JLabel lKickOffTimeName;
    JComboBox cbKickOffTime;
    JLabel lPitchName;
    JComboBox cbPitch;
    JLabel lMeetName;
    ROJLabel lMeet;
    JLabel lTeamOneName;
    DebugComboBox cbTeamOne;
    JLabel lTeamTwoName;
    JComboBox cbTeamTwo;

    static final int MID_GAP = 7;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    public OtherMatchDetailsPanel()
    {
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(new EnclosedFocusTraversalPolicy( this));
    }

    public void init()
    {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lOtherDetails = new JLabel();
        WidgetUtils.setLabelProperties( lOtherDetails, null, SuperSixViewConstants.TITLE_FONT, null);
        lDivisionName = new JLabel();
        cbDivision = new JComboBox();
        lKickOffTimeName = new JLabel();
        cbKickOffTime = new JComboBox();
        lPitchName = new JLabel();
        cbPitch = new JComboBox();
        lMeetName = new JLabel();
        lMeet = new ROJLabel();
        lTeamOneName = new JLabel();
        cbTeamOne = new DebugComboBox();
        lTeamTwoName = new JLabel();
        cbTeamTwo = new JComboBox();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    OtherMatchDetailsPanel.BORDER,
                    0.20,
                    OtherMatchDetailsPanel.GAP,
                    0.30,
                    OtherMatchDetailsPanel.MID_GAP,
                    0.20,
                    OtherMatchDetailsPanel.GAP,
                    0.30,
                    ModernTableLayout.FILL,
                    OtherMatchDetailsPanel.BORDER},
                // Rows
                {
                    OtherMatchDetailsPanel.BORDER,
                    OtherMatchDetailsPanel.TEXT_HEIGHT, OtherMatchDetailsPanel.SMALL_SPACING,
                    ModernTableLayout.FILL,
                    OtherMatchDetailsPanel.TEXT_HEIGHT, OtherMatchDetailsPanel.SMALL_SPACING,
                    OtherMatchDetailsPanel.TEXT_HEIGHT, OtherMatchDetailsPanel.SMALL_SPACING,
                    OtherMatchDetailsPanel.TEXT_HEIGHT, OtherMatchDetailsPanel.SMALL_SPACING,
                    OtherMatchDetailsPanel.TEXT_HEIGHT, OtherMatchDetailsPanel.SMALL_SPACING,
                    OtherMatchDetailsPanel.BORDER,
                    ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lOtherDetails, "1, 1");
        add(lDivisionName, "1, 4");
        add(cbDivision, "3, 4");
        add(lKickOffTimeName, "1, 6");
        add(cbKickOffTime, "3, 6");
        add(lPitchName, "1, 8");
        add(cbPitch, "3, 8");
        add(lMeetName, "1, 10");
        add(lMeet, "3, 10");
        add(lTeamOneName, "5, 6");
        add(cbTeamOne, "7, 6");
        add(lTeamTwoName, "5, 8");
        add(cbTeamTwo, "7, 8");

        lOtherDetails.setText("Match Details");
        lKickOffTimeName.setHorizontalAlignment(SwingConstants.TRAILING);
        lKickOffTimeName.setText("Kick-Off Time");
        cbKickOffTime.setName("cbKickOffTime");
        lPitchName.setHorizontalAlignment(SwingConstants.TRAILING);
        lPitchName.setText("Pitch");
        cbPitch.setName("cbPitch");
        lDivisionName.setHorizontalAlignment(SwingConstants.TRAILING);
        lDivisionName.setText("Division");
        cbDivision.setName("cbDivision");
        lMeetName.setHorizontalAlignment(SwingConstants.TRAILING);
        lMeetName.setText("Round");
        lMeet.setName("lMeet");
        lTeamOneName.setHorizontalAlignment(SwingConstants.TRAILING);
        lTeamOneName.setText("First Team");
        cbTeamOne.setName("cbTeamOne");
        lTeamTwoName.setHorizontalAlignment(SwingConstants.TRAILING);
        lTeamTwoName.setText("Second Team");
        cbTeamTwo.setName("cbTeamTwo");

        setName("OtherMatchDetailsPanel");

        setCbTeamOne( cbTeamOne);
        setCbTeamTwo( cbTeamTwo);
        setCbDivision( cbDivision);
    }

    public DebugComboBox getCbTeamOne()
    {
        return cbTeamOne;
    }

    public void setCbTeamOne(DebugComboBox cbTeamOne)
    {
        this.cbTeamOne = cbTeamOne;
    }

    public JComboBox getCbTeamTwo()
    {
        return cbTeamTwo;
    }

    public void setCbTeamTwo(JComboBox cbTeamTwo)
    {
        this.cbTeamTwo = cbTeamTwo;
    }

    public JComboBox getCbDivision()
    {
        return cbDivision;
    }

    public void setCbDivision(JComboBox cbDivision)
    {
        this.cbDivision = cbDivision;
    }
}
