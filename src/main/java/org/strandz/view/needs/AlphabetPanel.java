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
package org.strandz.view.needs;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlphabetPanel extends JPanel implements ActionListener
{
    static final int ALPHABET_SIZE = 26 - 6 + 3;
    static final int BUTTON_WIDTH = 28;
    static final int BUTTON_HEIGHT = 25;
    private ActionListener actionListener;
    private JButton buttons[] = new JButton[ALPHABET_SIZE];

    public void init()
    {
        double size[][] = // Columns
            {
                {
                    BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH,
                    BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH,
                    BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH,
                    BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH,
                    BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH // 20
                },
                // Rows
                {BUTTON_HEIGHT}
            };
        setLayout(new ModernTableLayout(size));
        for(int i = 0; i <= ALPHABET_SIZE - 1; i++)
        {
            buttons[i] = new JButton();
            buttons[i].setForeground(Color.magenta);
            buttons[i].setMargin(new Insets(0, 0, 0, 0));

            // Setting text didn't work too well - I only have an ascii
            // chart, so did manually below
            // char c = Character.forDigit( i+65, 10);
            // buttons[i].setText( new Character( c).toString());
            String where = i + ", 0";
            // Err.pr( "Adding to <" + where + ">");
            add(buttons[i], where);
            buttons[i].addActionListener(this);
        }

        int index = 0;
        buttons[index++].setText("A");
        buttons[index++].setText("B");
        buttons[index++].setText("C");
        buttons[index++].setText("D");
        buttons[index++].setText("E");
        // buttons[index++].setText( "F");
        buttons[index++].setText("G");
        buttons[index++].setText("H");
        // buttons[index++].setText( "I");
        buttons[index++].setText("J");
        buttons[index++].setText("K");
        buttons[index++].setText("L");
        buttons[index++].setText("Ma");
        buttons[index++].setText("Mu");
        buttons[index++].setText("N");
        buttons[index++].setText("O");
        buttons[index++].setText("P");
        // buttons[index++].setText( "Q");
        buttons[index++].setText("R");
        buttons[index++].setText("Sa");
        buttons[index++].setText("Sh");
        buttons[index++].setText("St");
        buttons[index++].setText("T");
        // buttons[index++].setText( "U");
        buttons[index++].setText("V");
        buttons[index++].setText("W");
        // buttons[index++].setText( "X");
        // buttons[index++].setText( "Y");
        buttons[index++].setText("Z");
    }

    public void setActionListener(ActionListener l)
    {
        this.actionListener = l;
    }

    public void actionPerformed(ActionEvent e)
    {
        if(actionListener != null)
        {
            actionListener.actionPerformed(e);
        }
    }

    /*
    public Dimension getMinimumSize()
    {
    Dimension result = super.getMinimumSize();
    result.height = BUTTON_HEIGHT;
    return result;
    }
    public Dimension getPreferredSize()
    {
    Dimension result = super.getPreferredSize();
    result.height = BUTTON_HEIGHT;
    return result;
    }
    */
    public int getHeight()
    {
        return BUTTON_HEIGHT;
    }
}
