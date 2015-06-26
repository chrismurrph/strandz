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

import org.strandz.lgpl.util.Err;

import javax.swing.Icon;
import javax.swing.ButtonModel;
import javax.swing.JRadioButton;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.UIResource;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;
import java.io.Serializable;

public class MyRadioButtonIcon1 implements Icon, UIResource, Serializable
{
    private Color dotColour;
    private boolean on;
    
    public MyRadioButtonIcon1( Color dotColour)
    {
        this.dotColour = dotColour;    
    }

    public void setOn(boolean on)
    {
        this.on = on;
    }

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        JRadioButton rb = (JRadioButton) c;
        ButtonModel model = rb.getModel();
        boolean drawDot = on || model.isSelected();

        Color background = c.getBackground();
        Color dotColor = dotColour; //c.getForeground();
        //Err.pr( "fg is " + c.getForeground());
        Color shadow = MetalLookAndFeel.getControlShadow();
        Color darkCircle = MetalLookAndFeel.getControlDarkShadow();
        Color whiteInnerLeftArc = MetalLookAndFeel.getControlHighlight();
        Color whiteOuterRightArc = MetalLookAndFeel.getControlHighlight();
        Color interiorColor = background;

        // Set up colors per RadioButtonModel condition
        if(!model.isEnabled())
        {
            whiteInnerLeftArc = whiteOuterRightArc = background;
            darkCircle = dotColor = shadow;
        }
        else 
        if(model.isPressed() && model.isArmed())
        {
            whiteInnerLeftArc = interiorColor = shadow;
        }

        g.translate(x, y);

        // fill interior
        g.setColor(interiorColor);
        g.fillRect(2, 2, 9, 9);

        // draw Dark Circle (start at top, go clockwise)
        g.setColor(darkCircle);
        g.drawLine(4, 0, 7, 0);
        g.drawLine(8, 1, 9, 1);
        g.drawLine(10, 2, 10, 3);
        g.drawLine(11, 4, 11, 7);
        g.drawLine(10, 8, 10, 9);
        g.drawLine(9, 10, 8, 10);
        g.drawLine(7, 11, 4, 11);
        g.drawLine(3, 10, 2, 10);
        g.drawLine(1, 9, 1, 8);
        g.drawLine(0, 7, 0, 4);
        g.drawLine(1, 3, 1, 2);
        g.drawLine(2, 1, 3, 1);

        // draw Inner Left (usually) White Arc
        //  start at lower left corner, go clockwise
        g.setColor(whiteInnerLeftArc);
        g.drawLine(2, 9, 2, 8);
        g.drawLine(1, 7, 1, 4);
        g.drawLine(2, 2, 2, 3);
        g.drawLine(2, 2, 3, 2);
        g.drawLine(4, 1, 7, 1);
        g.drawLine(8, 2, 9, 2);
        // draw Outer Right White Arc
        //  start at upper right corner, go clockwise
        g.setColor(whiteOuterRightArc);
        g.drawLine(10, 1, 10, 1);
        g.drawLine(11, 2, 11, 3);
        g.drawLine(12, 4, 12, 7);
        g.drawLine(11, 8, 11, 9);
        g.drawLine(10, 10, 10, 10);
        g.drawLine(9, 11, 8, 11);
        g.drawLine(7, 12, 4, 12);
        g.drawLine(3, 11, 2, 11);

        // selected dot
        if(drawDot)
        {
            g.setColor(dotColor);
            g.fillRect(4, 4, 4, 4);
            g.drawLine(4, 3, 7, 3);
            g.drawLine(8, 4, 8, 7);
            g.drawLine(7, 8, 4, 8);
            g.drawLine(3, 7, 3, 4);
        }

        g.translate(-x, -y);
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
