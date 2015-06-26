/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.widgets;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Keeps the List it is constructed with up to date.
 * Replace JTextArea with JList!
 */
public class ListPanel extends JPanel
{
    private static boolean debugging = false;
    private int height = -99;
    public JList jList;
    // public JTable jList;

    public ListPanel()
    {
        jList = new JList(); // JTable();

        JScrollPane sp = new JScrollPane(jList,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setLayout(new BorderLayout());
        add(sp);
    }

    public void setHeight(int height)
    {
        Dimension dim = new Dimension(Integer.MAX_VALUE, height);
        setMaximumSize(dim);
        this.height = height;
    }

    public int getHeight()
    {
        return height;
    }
}
