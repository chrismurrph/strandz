package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;

/**
 * User: Chris
 * Date: 19/10/2008
 * Time: 17:16:01
 */
public class ColRow
{
    int col;
    int row;

    public ColRow(int col, int row)
    {
        Assert.isFalse( col == Utils.UNSET_INT || row == Utils.UNSET_INT);
        this.col = col;
        this.row = row;
    }

    public ColRow(int col)
    {
        this.col = col;
        this.row = Utils.UNSET_INT;
    }

    public String toString()
    {
        return "col " + col + ", row " + row;
    }

    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }

        ColRow colRow = (ColRow) o;

        if(col != colRow.col)
        {
            return false;
        }
        if(row != colRow.row)
        {
            return false;
        }

        return true;
    }

    public int hashCode()
    {
        int result;
        result = col;
        result = 31 * result + row;
        return result;
    }

    public int getCol()
    {
        return col;
    }

    public int getRow()
    {
        return row;
    }
}
