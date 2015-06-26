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
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.widgets.table.ComponentTableView;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;

public class CTVPanel extends JPanel
{
    private ComponentTableView table;

    static final int BORDER = 15;

    public CTVPanel()
    {
    }

    public Dimension getPreferredSize()
    {
        Dimension result = super.getPreferredSize();
        result.width = 500;
        result.height = 300;
        return result;
    }

    public void init()
    {
        table = new ComponentTableView();
        table.setEditableRowMoves( true);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create a TableLayout for the panel
        double size[][] =
            {
                // Columns
                {BORDER,
                 ModernTableLayout.FILL,
                 BORDER},
                // Rows
                {//ExpenseTablePanel.BORDER, Don't want a border when another panel stacked on top
                 ModernTableLayout.FILL,
                 BORDER
                }
            };
        setLayout(new ModernTableLayout(size));
        table.setName("Table type " + NameUtils.endOfDots( table.getClass().getName()));

        add(scrollPane, "1, 0");

        setName("CTVPanel");
    }

    public ComponentTableView getTable()
    {
        return table;
    }

    public void setTable(ComponentTableView table)
    {
        this.table = table;
    }
} // end class
