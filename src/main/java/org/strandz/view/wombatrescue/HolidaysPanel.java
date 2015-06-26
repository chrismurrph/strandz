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
package org.strandz.view.wombatrescue;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import mseries.ui.MDateEntryField;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HolidaysPanel extends JPanel
{
    JLabel lAway1Start;
    MDateEntryField mdefAway1Start;
    
    JLabel lAway1End;
    MDateEntryField mdefAway1End;
    
    JLabel lAway2Start;
    MDateEntryField mdefAway2Start;
    
    JLabel lAway2End;
    MDateEntryField mdefAway2End;
    
    static final int GAP = 15;
    static final int BORDER = 55;
    static final int SMALL_SPACING = 2;
    static final int BIG_SPACING = 13;
    static final int TEXT_HEIGHT = 23;
    static final int LABEL_WIDTH = 80;
    static final int TITLE_WIDTH = 320;
    
    public HolidaysPanel()
    {
        
    }

    public void init()
    {
        lAway1Start = new JLabel();
        mdefAway1Start = new MDateEntryField();
        mdefAway1Start.setNullOnEmpty(true);
        lAway1End = new JLabel();
        mdefAway1End = new MDateEntryField();
        mdefAway1End.setNullOnEmpty(true);
        lAway2Start = new JLabel();
        mdefAway2Start = new MDateEntryField();
        mdefAway2Start.setNullOnEmpty(true);
        lAway2End = new JLabel();
        mdefAway2End = new MDateEntryField();
        mdefAway2End.setNullOnEmpty(true);
        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {BORDER, LABEL_WIDTH, GAP, 0.98, BORDER},
                // Rows
                {
                    ModernTableLayout.FILL,
                        TEXT_HEIGHT, SMALL_SPACING, 
                        TEXT_HEIGHT, SMALL_SPACING, 
                        TEXT_HEIGHT, SMALL_SPACING, 
                        TEXT_HEIGHT,
                        ModernTableLayout.FILL}};
        setLayout(new ModernTableLayout(size));
        lAway1Start.setHorizontalAlignment(SwingConstants.TRAILING);
        lAway1Start.setText("Away 1 Start");
        mdefAway1Start.setName("mdefAway1Start");
        lAway1End.setHorizontalAlignment(SwingConstants.TRAILING);
        lAway1End.setText("Away 1 End");
        mdefAway1End.setName("mdefAway1End");
        lAway2Start.setHorizontalAlignment(SwingConstants.TRAILING);
        lAway2Start.setText("Away 2 Start");
        mdefAway2Start.setName("mdefAway2Start");
        lAway2End.setHorizontalAlignment(SwingConstants.TRAILING);
        lAway2End.setText("Away 2 End");
        mdefAway2End.setName("mdefAway2End");

        /*
        add(lAway1Start, "5, 27");
        add(mdefAway1Start, "7, 27, 8, 27");
        add(lAway1End, "5, 29");
        add(mdefAway1End, "7, 29, 8, 29");
        add(lAway2Start, "5, 31");
        add(mdefAway2Start, "7, 31, 8, 31");
        add(lAway2End, "5, 33");
        add(mdefAway2End, "7, 33, 8, 33");
        */
        
        add(lAway1Start, "1, 1");
        add(mdefAway1Start, "3, 1");
        add(lAway1End, "1, 3");
        add(mdefAway1End, "3, 3");
        add(lAway2Start, "1, 5");
        add(mdefAway2Start, "3, 5");
        add(lAway2End, "1, 7");
        add(mdefAway2End, "3, 7");
        setName("HolidaysPanel");
        setMdefAway1Start(mdefAway1Start);
        setMdefAway1End(mdefAway1End);
        setMdefAway2Start(mdefAway2Start);
        setMdefAway2End(mdefAway2End);
    }
    
    public MDateEntryField getMdefAway1End()
    {
        return mdefAway1End;
    }

    public void setMdefAway1End(MDateEntryField mdefAway1End)
    {
        this.mdefAway1End = mdefAway1End;
    }

    public MDateEntryField getMdefAway1Start()
    {
        return mdefAway1Start;
    }

    public void setMdefAway1Start(MDateEntryField mdefAway1Start)
    {
        this.mdefAway1Start = mdefAway1Start;
    }

    public MDateEntryField getMdefAway2End()
    {
        return mdefAway2End;
    }

    public void setMdefAway2End(MDateEntryField mdefAway2End)
    {
        this.mdefAway2End = mdefAway2End;
    }

    public MDateEntryField getMdefAway2Start()
    {
        return mdefAway2Start;
    }

    public void setMdefAway2Start(MDateEntryField mdefAway2Start)
    {
        this.mdefAway2Start = mdefAway2Start;
    }
}
