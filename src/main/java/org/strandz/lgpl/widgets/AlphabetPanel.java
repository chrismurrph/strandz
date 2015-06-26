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
import org.strandz.lgpl.util.Err;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class AlphabetPanel extends JPanel
    implements ActionListener, ComponentListener
{
    static final int BUTTON_WIDTH = 28;
    public static final int BUTTON_HEIGHT = 25;
    private ActionListener actionListener;
    private JButton buttons[];
    private boolean enabled = true;

    public void setEnabled(boolean b)
    {
        if(b != enabled)
        {
            //Err.pr( "Are setting AlphabetPanel enabled to " + b);
            for(int i = 0; i <= buttons.length - 1; i++)
            {
                buttons[i].setEnabled(b);
            }
            enabled = b;
        }
    }

    /**
     * Set the alphabet that is right for the data might have in the
     * application.
     *
     * @param letters
     */
    public void setLetters(String letters[])
    {
        buttons = new JButton[letters.length];
        double size[][] = new double[2][letters.length];
        for(int i = 0; i <= letters.length - 1; i++)
        {
            size[0][i] = percentOfButton();
        }
        size[1][0] = BUTTON_HEIGHT;
        setLayout(new ModernTableLayout(size));
        for(int i = 0; i <= letters.length - 1; i++)
        {
            buttons[i] = new JButton();
            buttons[i].setForeground(Color.magenta);
            buttons[i].setMargin(new Insets(0, 0, 0, 0));
            String where = i + ", 0";
            add(buttons[i], where);
            buttons[i].addActionListener(this);
            buttons[i].setText(letters[i]);
        }
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

    public void componentHidden(ComponentEvent arg0)
    {// nufin
    }

    public void componentMoved(ComponentEvent arg0)
    {// nufin
    }

    /**
     * Worked but caused bad flicker.
     */
    public void componentResized(ComponentEvent arg0)
    {
        invalidate();

        JComponent sourceParent = (JComponent) ((JComponent) arg0.getSource()).getParent();
        sourceParent.repaint();
        Err.pr("Being resized at " + sourceParent.getClass().getName());
    }

    public void componentShown(ComponentEvent arg0)
    {// nufin
    }

    private float percentOfButton()
    {
        /*
        float result = 1 / ALPHABET_SIZE;
        return result;
        */
        return BUTTON_WIDTH;
    }
}
