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
package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Utils;

import javax.swing.JComponent;
import java.awt.*;

/**
 * Wraps around a component and defines a position in a ComponentTableView
 */
public class ComponentControlWrapper
{
    private int row = Utils.UNSET_INT;
    private int col = Utils.UNSET_INT;
    private JComponent comp;
    private Color previousBackground;
    private int id;
    private static int constructedTimes;

    public ComponentControlWrapper(int row, int col, JComponent comp)
    {
        constructedTimes++;
        id = constructedTimes;
        /*
        if(comp instanceof JLabel)
        {
            JLabel label = (JLabel)comp;
            if(row == 0 && col == 3)
            {
                if(label.getText().equals( "Fri 01"))
                {
                    Err.debug();                    
                }
            }
        }
        */
        this.row = row;
        this.col = col;
        this.comp = comp;
    }
    
    public JComponent getComp()
    {
        return comp;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public ColRow getColRow()
    {
        return new ColRow( col, row);
    }

    public String toString()
    {
        String result = "col " + col + ", row " + row + ", name <" + 
                comp.getName() + ">, text <" + ComponentUtils.getText( comp) + ">";
        return result;
    }

    public Color getPreviousBackground()
    {
        return previousBackground;
    }

    public void savePreviousBackground(Color previousBackground)
    {
        this.previousBackground = new Color( previousBackground.getRGB());
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof ComponentControlWrapper))
        {// nufin
        }
        else
        {
            ComponentControlWrapper test = (ComponentControlWrapper) o;
            if(Utils.equals( row, test.row))
            {
                if(Utils.equals( col, test.col))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = Utils.hashCode(result, row);
        result = Utils.hashCode(result, col);
        return result;
    }
}
