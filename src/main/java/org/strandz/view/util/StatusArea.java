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
package org.strandz.view.util;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Dimension;

public class StatusArea extends JPanel
{
    private JComponent component;
    private static final int LEFT_MARGIN = 10;

    public StatusArea()
    {
        setPreferredSize(
            new Dimension(super.getPreferredSize().width, StrandArea.STATUS_HEIGHT));
        // for debugging:
        Err.pr(SdzNote.TASK_BAR_APPEARANCE, "Border to debug");
        // setBorder( BorderFactory.createEtchedBorder());
    }

    public String toString()
    {
        String result = "StatusArea: WITH NUFIN IN IT";
        if(component != null)
        {
            result = "StatusArea: " + component.hashCode() + " called "
                + component.getName();
        }
        return result;
    }

    private void configureTableLayout(JComponent component)
    {
        double size[][] = // Columns
            {{
                LEFT_MARGIN, ModernTableLayout.FILL,
            }, // Rows
                {ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(component, "1,0");
        this.component = component;
    }

    public void refitArea(JComponent panel)
    {
        removeAll();
        if(panel != null)
        {
            configureTableLayout(panel);
        }
        /*
        * Needed revalidate call for first time
        * manually focused on a different internal
        * frame.
        */
        revalidate();
        repaint();
        // Err.pr( "o-o Have set contents to " + this);
    }

    public void blankContents()
    {
        removeAll();
    }
}
