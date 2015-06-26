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

import org.strandz.lgpl.util.Print;

import javax.swing.JComboBox;
import java.awt.Dimension;

public class ComboBox extends JComboBox
{
    private int height;
    private static int times = 0;

    public ComboBox()
    {
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;

        Dimension dim = new Dimension(Integer.MAX_VALUE, height);
        setMaximumSize(dim);
    }

    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        // if(b)
        /*
        {
        times++;
        Err.pr( "@@  setEnabled " + b + " for " + hashCode() + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        }
        */
    }

    public void setEditable(boolean b)
    {
        super.setEditable(b);
        // if(b)
        {
            Print.pr("@@ setEditable " + b);
        }
    }
}