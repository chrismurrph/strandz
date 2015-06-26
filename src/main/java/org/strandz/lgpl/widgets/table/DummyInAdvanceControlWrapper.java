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

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Assert;

public class DummyInAdvanceControlWrapper
{
    private int row = Utils.UNSET_INT;
    private int col = Utils.UNSET_INT;
    private Object comp;


    public DummyInAdvanceControlWrapper(int row, int col, Object comp)
    {
        Assert.isFalse( row == Utils.UNSET_INT);    
        Assert.isFalse( col == Utils.UNSET_INT);
        this.row = row;
        this.col = col;
        this.comp = comp;
    }

    public String toString()
    {
        String result = "col " + col + ", row " + row + ", name <" + ComponentUtils.getName( comp) + ">";
        return result;
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof DummyInAdvanceControlWrapper))
        {// nufin
        }
        else
        {
            DummyInAdvanceControlWrapper test = (DummyInAdvanceControlWrapper) o;
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

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public Object getComp()
    {
        return comp;
    }
}
