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
import mseries.ui.MDateEntryField;
import org.strandz.lgpl.widgets.ROJLabel;
import org.strandz.lgpl.widgets.WidgetUtils;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class MeetPanel extends JPanel
{
    JLabel lMeet;
    JLabel lRoundName;
    ROJLabel lRound;
    JLabel lDateName;
    MDateEntryField mdefDate;
    JLabel lSeasonName;
    ROJLabel lSeason;

    static final int MID_GAP = 7;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    public MeetPanel()
    {
    }

    public void init()
    {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lMeet = new JLabel();
        WidgetUtils.setLabelProperties( lMeet, null, SuperSixViewConstants.TITLE_FONT, null);
        lRoundName = new JLabel();
        lRound = new ROJLabel();
        lDateName = new JLabel();
        mdefDate = new MDateEntryField();
        lSeasonName = new JLabel();
        lSeason = new ROJLabel();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    MeetPanel.BORDER, 0.20, MeetPanel.GAP, 0.30, MeetPanel.MID_GAP, 0.20, MeetPanel.GAP, 0.30, ModernTableLayout.FILL,
                    MeetPanel.BORDER},
                // Rows
                {
                    ModernTableLayout.FILL,
                        MeetPanel.TEXT_HEIGHT, MeetPanel.SMALL_SPACING,
                        MeetPanel.TEXT_HEIGHT, MeetPanel.SMALL_SPACING,
                        MeetPanel.TEXT_HEIGHT, MeetPanel.SMALL_SPACING,
                        MeetPanel.TEXT_HEIGHT, MeetPanel.SMALL_SPACING,
                        MeetPanel.TEXT_HEIGHT, MeetPanel.SMALL_SPACING,
                    ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lMeet, "1, 1");
        add(lRoundName, "1, 3");
        add(lRound, "3, 3");
        add(lDateName, "1, 5");
        add(mdefDate, "3, 5");
        add(lSeasonName, "1, 7");
        add(lSeason, "2, 7, 3, 7");

        lMeet.setText("Round");
        
        lRoundName.setHorizontalAlignment(SwingConstants.TRAILING);
        lRoundName.setText("Round");
        
        lRound.setName("lRound");
        
        lDateName.setHorizontalAlignment(SwingConstants.TRAILING);
        lDateName.setText("Date");
        
        lSeasonName.setHorizontalAlignment(SwingConstants.TRAILING);
        lSeasonName.setText("Global CompetitionSeason is ");
        
        lSeason.setName("lSeason");
        lSeason.setHorizontalAlignment(SwingConstants.LEADING);
                
        mdefDate.setName("mdefDate");
        mdefDate.setNullOnEmpty(true);

        setName("MeetPanel");

        //Anything we need to refer to directly has to go through here
    }
}
