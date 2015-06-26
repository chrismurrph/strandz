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
package org.strandz.lgpl.widgets;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ButtonModel;
import javax.swing.UIManager;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.Color;
import java.io.Serializable;

class MyRadioButtonIcon2 implements Icon, Serializable
{
    private Color dotColour;
    private boolean on;

    public MyRadioButtonIcon2(Color dotColour)
    {
        this.dotColour = dotColour;
    }

    public void setOn(boolean on)
    {
        this.on = on;
    }

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        // fill interior
        if((model.isPressed() && model.isArmed()) || !model.isEnabled())
        {
            g.setColor(UIManager.getColor("RadioButton.background"));
        }
        else
        {
            g.setColor(UIManager.getColor("RadioButton.interiorBackground"));
        }
        g.fillRect(x + 2, y + 2, 8, 8);

        // outter left arc
        g.setColor(UIManager.getColor("RadioButton.shadow"));
        g.drawLine(x + 4, y + 0, x + 7, y + 0);
        g.drawLine(x + 2, y + 1, x + 3, y + 1);
        g.drawLine(x + 8, y + 1, x + 9, y + 1);
        g.drawLine(x + 1, y + 2, x + 1, y + 3);
        g.drawLine(x + 0, y + 4, x + 0, y + 7);
        g.drawLine(x + 1, y + 8, x + 1, y + 9);

        // outter right arc
        g.setColor(UIManager.getColor("RadioButton.highlight"));
        g.drawLine(x + 2, y + 10, x + 3, y + 10);
        g.drawLine(x + 4, y + 11, x + 7, y + 11);
        g.drawLine(x + 8, y + 10, x + 9, y + 10);
        g.drawLine(x + 10, y + 9, x + 10, y + 8);
        g.drawLine(x + 11, y + 7, x + 11, y + 4);
        g.drawLine(x + 10, y + 3, x + 10, y + 2);

        // inner left arc
        g.setColor(UIManager.getColor("RadioButton.darkShadow"));
        g.drawLine(x + 4, y + 1, x + 7, y + 1);
        g.drawLine(x + 2, y + 2, x + 3, y + 2);
        g.drawLine(x + 8, y + 2, x + 9, y + 2);
        g.drawLine(x + 2, y + 3, x + 2, y + 3);
        g.drawLine(x + 1, y + 4, x + 1, y + 7);
        g.drawLine(x + 2, y + 8, x + 2, y + 8);

        // inner right arc
        g.setColor(UIManager.getColor("RadioButton.light"));
        g.drawLine(x + 2, y + 9, x + 3, y + 9);
        g.drawLine(x + 4, y + 10, x + 7, y + 10);
        g.drawLine(x + 8, y + 9, x + 9, y + 9);
        g.drawLine(x + 9, y + 8, x + 9, y + 8);
        g.drawLine(x + 10, y + 7, x + 10, y + 4);
        g.drawLine(x + 9, y + 3, x + 9, y + 3);

        // indicate whether selected or not
        if(model.isSelected() || on)
        {
            if(model.isEnabled())
            {
                g.setColor(
                        //UIManager.getColor("RadioButton.foreground")
                        dotColour
                );
            }
            else
            {
                g.setColor(UIManager.getColor("RadioButton.shadow"));
            }
            g.fillRect(x + 4, y + 5, 4, 2);
            g.fillRect(x + 5, y + 4, 2, 4);
        }
    }

    public int getIconWidth()
    {
        return 13;
    }

    public int getIconHeight()
    {
        return 13;
    }
}