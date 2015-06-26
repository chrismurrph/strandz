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

import javax.swing.JPanel;
import javax.swing.JButton;

public class ThreeButtonPanel extends JPanel
{
    private String titles[];
    private JButton bOne;
    private JButton bTwo;
    private JButton bThree;
    static final int GAP = 15;
    static final int BORDER = 15;
    static final int TEXT_HEIGHT = 23;
    
    public ThreeButtonPanel( String titles[])
    {
        this.titles = titles;
    }

    public void init()
    {
        bOne = new JButton();
        bOne.setText( titles[0]);
        bTwo = new JButton();
        bTwo.setText( titles[1]);
        bThree = new JButton();
        bThree.setText( titles[2]);

        // Create a TableLayout for the panel
        double size[][] =
            {
                // Columns
                {ModernTableLayout.FILL, BORDER, 0.25, GAP, 0.30, GAP, 0.30, BORDER,
                    ModernTableLayout.FILL},
                // Rows
                {ModernTableLayout.FILL, TEXT_HEIGHT, ModernTableLayout.FILL}
            };
        setLayout(new ModernTableLayout(size));
        bOne.setName("bOne");
        bTwo.setName("bTwo");
        bThree.setName("bThree");
        add(bOne, "2, 1");
        add(bTwo, "4, 1");
        add(bThree, "6, 1");
        setBOne( bOne);
        setBTwo( bTwo);
        setBThree( bThree);
        setName("ThreeButtonPanel");
    }

    public JButton getBOne()
    {
        return bOne;
    }

    public JButton getBTwo()
    {
        return bTwo;
    }

    public JButton getBThree()
    {
        return bThree;
    }

    public void setBOne(JButton bOne)
    {
        this.bOne = bOne;
    }

    public void setBTwo(JButton bTwo)
    {
        this.bTwo = bTwo;
    }

    public void setBThree(JButton bThree)
    {
        this.bThree = bThree;
    }
}
