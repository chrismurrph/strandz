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

import org.strandz.lgpl.util.Err;

import javax.swing.JCheckBox;

public class DebugCheckBox extends JCheckBox
{
    private static int times;
    private static boolean debugging = false;
    private static int constructedTimes;
    public int id;

    public DebugCheckBox()
    {
        init();
    }

    private void init()
    {
        constructedTimes++;
        id = constructedTimes;
        pr("---DebugCheckBox constructed, id: " + id);
    }

    private static void pr(String txt)
    {
        if(debugging)
        {
            Err.pr(txt);
        }
    }

    public void setSelected(boolean b)
    {
        times++;
        pr("DebugCheckBox setSelected to " + b + " times " + times);
        if(times == 0)
        {
            Err.stack();
        }
        super.setSelected(b);
    }

    public boolean isSelected()
    {
        boolean result = super.isSelected();
        return result;
    }

    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        /*
        times++;
        pr( "SET ENABLED (DebugCheckBox[" + getName() + "]): " + b + " times " + times);
        if(times == 21)
        {
        Err.stack();
        }
        */
    }
}
