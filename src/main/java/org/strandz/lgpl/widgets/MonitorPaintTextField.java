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

import javax.swing.JComponent;
import javax.swing.JTextField;
import java.awt.Graphics;

public class MonitorPaintTextField extends JTextField
{
    private JComponent toInvalidate;

    public void paint(Graphics g)
    {
        super.paint(g);
        /*
        if(isVisible())
        {
        Err.pr( "Painting " + getName());
        Err.pr( "So invalidate " + toInvalidate.getName());
        toInvalidate.invalidate();
        JComponent sourceParent = (JComponent)((JComponent)toInvalidate).getParent();
        sourceParent.repaint();
        Err.pr( "Repaint of container: " + sourceParent.getClass().getName());
        }
        */
    }

    public void setToInvalidateComponent(JComponent comp)
    {
        this.toInvalidate = comp;
    }
}
