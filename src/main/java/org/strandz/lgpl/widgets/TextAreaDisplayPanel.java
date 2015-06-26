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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

public class TextAreaDisplayPanel extends JPanel
{
    private JTextArea textArea;
    private JScrollPane scrollPane;

    public void scrollToTop()
    {
        textArea.setCaretPosition(0);
        scrollPane.getVerticalScrollBar().setValue(0);
        repaint();
        revalidate();
    }

    public TextAreaDisplayPanel()
    {
        setLayout(new BorderLayout());
        textArea = new MyTextArea();
        textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public Dimension getPreferredSize()
    {
        Dimension result = super.getPreferredSize();
        result.height = 498;
        // doesn't help
        result.width = 900;
        return result;
    }

    public JTextArea getTextArea()
    {
        return textArea;
    }

    private static class MyTextArea extends JTextArea
    {
        public MyTextArea()
        {
            super(null, 0, 0);
            setEditable(false);
            // setText("");
        }
        /*
        public float getAlignmentX () {
        return LEFT_ALIGNMENT;
        }
        public float getAlignmentY () {
        return TOP_ALIGNMENT;
        }
        */
    }
}
