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
import org.strandz.view.util.EnclosedFocusTraversalPolicy;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import java.awt.Font;

public class SeasonPanel extends JPanel
{
    JLabel lSeason;
    JLabel lYearName;
    ROJLabel lYear;
    JLabel lStart;
    MDateEntryField mdefStart;
    JLabel lEnd;
    MDateEntryField mdefEnd;
    //JLabel lNight;
    //JComboBox cbNight;

    static final int MID_GAP = 7;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    private static Font TITLE_FONT = new Font("Dialog", Font.BOLD, 14);

    public SeasonPanel()
    {
        setFocusCycleRoot(true);
        EnclosedFocusTraversalPolicy policy = new EnclosedFocusTraversalPolicy( this);
        policy.setDebug( false);
        setFocusTraversalPolicy( policy);
    }

    public void init()
    {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lSeason = new JLabel();
        WidgetUtils.setLabelProperties( lSeason, null, SeasonPanel.TITLE_FONT, null);
        lStart = new JLabel();
        mdefStart = new MDateEntryField();
        lEnd = new JLabel();
        mdefEnd = new MDateEntryField();
        mdefEnd.setNullOnEmpty(true);
        //lNight = new JLabel();
        //cbNight = new JComboBox();
        lYearName = new JLabel();
        lYear = new ROJLabel();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    SeasonPanel.BORDER, 0.20, SeasonPanel.GAP, 0.30, SeasonPanel.MID_GAP, 0.20, SeasonPanel.GAP, 0.30, ModernTableLayout.FILL,
                    SeasonPanel.BORDER},
                // Rows
                {
                    ModernTableLayout.FILL,
                        SeasonPanel.TEXT_HEIGHT, SeasonPanel.SMALL_SPACING,
                        SeasonPanel.TEXT_HEIGHT, SeasonPanel.SMALL_SPACING,
                        SeasonPanel.TEXT_HEIGHT, SeasonPanel.SMALL_SPACING,
                        SeasonPanel.TEXT_HEIGHT, SeasonPanel.SMALL_SPACING,
                        SeasonPanel.TEXT_HEIGHT, SeasonPanel.SMALL_SPACING,
                        SeasonPanel.TEXT_HEIGHT, SeasonPanel.SMALL_SPACING,
                    ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lSeason, "1, 1");
        add(lStart, "1, 3");
        add(mdefStart, "3, 3");
        add(lEnd, "1, 5");
        add(mdefEnd, "3, 5");
        add(lYearName, "1, 7");
        add(lYear, "3, 7");
        //add(lNight, "1, 9");
        //add(cbNight, "3, 9");

        lSeason.setText("CompetitionSeason");
        lStart.setHorizontalAlignment(SwingConstants.TRAILING);
        lStart.setText("Start");
        mdefStart.setName("mdefStart");
        lEnd.setHorizontalAlignment(SwingConstants.TRAILING);
        lEnd.setText("End");
        mdefEnd.setName("mdefEnd");
        //lNight.setHorizontalAlignment(SwingConstants.TRAILING);
        //lNight.setText("Night");
        //cbNight.setName("cbNight");
        lYearName.setHorizontalAlignment(SwingConstants.TRAILING);
        lYearName.setText("Year");
        lYear.setName("lYear");

        setName("SeasonPanel");

        //Anything we need to refer to directly has to go through here
        setMdefStart( mdefStart);
        setMdefEnd( mdefEnd);
//        setCbNight( cbNight);
    }

    public ROJLabel getLYear()
    {
        return lYear;
    }

    public void setLYear(ROJLabel lYear)
    {
        this.lYear = lYear;
    }

    public MDateEntryField getMdefStart()
    {
        return mdefStart;
    }

    public void setMdefStart(MDateEntryField mdefStart)
    {
        this.mdefStart = mdefStart;
    }

    public MDateEntryField getMdefEnd()
    {
        return mdefEnd;
    }

    public void setMdefEnd(MDateEntryField mdefEnd)
    {
        this.mdefEnd = mdefEnd;
    }

//    public JComboBox getCbNight()
//    {
//        return cbNight;
//    }
//
//    public void setCbNight(JComboBox cbNight)
//    {
//        this.cbNight = cbNight;
//    }

}
