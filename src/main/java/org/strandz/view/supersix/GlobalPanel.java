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
import org.strandz.lgpl.widgets.WidgetUtils;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import java.awt.Font;

public class GlobalPanel extends JPanel
{
    JLabel lGlobal;
    JLabel lSeasonYear;
    JComboBox cbSeasonYear;
    JLabel lCompetition;
    JComboBox cbCompetition;

    static final int MID_GAP = 7;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    private static Font TITLE_FONT = new Font("Dialog", Font.BOLD, 14);

    public GlobalPanel()
    {
    }

    public void init()
    {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lGlobal = new JLabel();
        WidgetUtils.setLabelProperties( lGlobal, null, GlobalPanel.TITLE_FONT, null);
        lSeasonYear = new JLabel();
        cbSeasonYear = new JComboBox();
        lCompetition = new JLabel();
        cbCompetition = new JComboBox();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    GlobalPanel.BORDER, 0.20, GlobalPanel.GAP, 0.30, GlobalPanel.MID_GAP, 0.20, GlobalPanel.GAP, 0.30, ModernTableLayout.FILL,
                    GlobalPanel.BORDER},
                // Rows
                {
                    ModernTableLayout.FILL,
                        GlobalPanel.TEXT_HEIGHT, GlobalPanel.SMALL_SPACING, 
                        GlobalPanel.TEXT_HEIGHT, GlobalPanel.SMALL_SPACING,
                        GlobalPanel.TEXT_HEIGHT, GlobalPanel.SMALL_SPACING, 
                        GlobalPanel.TEXT_HEIGHT, GlobalPanel.SMALL_SPACING, 
                    ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lGlobal, "1, 1");
        add(lSeasonYear, "1, 3");
        add(cbSeasonYear, "3, 3");
        add(lCompetition, "1, 5");
        add(cbCompetition, "3, 5");

        lGlobal.setText("Globals");
        lSeasonYear.setHorizontalAlignment(SwingConstants.TRAILING);
        lSeasonYear.setText("Current Year");
        cbSeasonYear.setName("cbSeasonYear");
        lCompetition.setHorizontalAlignment(SwingConstants.TRAILING);
        lCompetition.setText("Current Competition");
        cbCompetition.setName("cbCompetition");
        
        setName("GlobalPanel");
        
        setCbSeasonYear( cbSeasonYear);
    }

    public JComboBox getCbSeasonYear()
    {
        return cbSeasonYear;
    }

    public void setCbSeasonYear(JComboBox cbSeasonYear)
    {
        this.cbSeasonYear = cbSeasonYear;
    }

    public JComboBox getCbCompetition()
    {
        return cbCompetition;
    }

    public void setCbCompetition(JComboBox cbCompetition)
    {
        this.cbCompetition = cbCompetition;
    }
}
