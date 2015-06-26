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

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class AlphabetScrollPane extends JPanel
{
    private AlphabetPanel alphabetPanel;
    private JPanel contentPanel;
    static final int BORDER = 0;

    public void setLetters(String letters[])
    {
        alphabetPanel = new AlphabetPanel();
        alphabetPanel.setLetters(letters);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {{BORDER, ModernTableLayout.FILL, BORDER}, // Rows
                {AlphabetPanel.BUTTON_HEIGHT, ModernTableLayout.FILL}};
        setLayout(new ModernTableLayout(size));
        add(alphabetPanel, "1, 0");

        JScrollPane sp = new JScrollPane(contentPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(sp, "1, 1");
        setName("AlphabetScrollPane");
        setAlphabetPanel(alphabetPanel);
    }

    public AlphabetPanel getAlphabetPanel()
    {
        return alphabetPanel;
    }

    public void setAlphabetPanel(AlphabetPanel alphabetPanel)
    {
        this.alphabetPanel = alphabetPanel;
    }

    public JPanel getContentPanel()
    {
        return contentPanel;
    }

    public void setContentPanel(JPanel contentPanel)
    {
        this.contentPanel = contentPanel;
    }
} // end class
