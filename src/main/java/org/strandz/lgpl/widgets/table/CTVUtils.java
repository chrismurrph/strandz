package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.Assert;

/**
 * User: Chris
 * Date: 22/01/2009
 * Time: 4:45:30 PM
 */
class CTVUtils
{
    static String formBodyQuotedStr(int n1, int n2)
    {
        String result = n1 + "," + n2;
        return result;
    }

    /**
     *
     * @param tl The TableLayout we are adding a column to
     * @param col The column that needs to be added to TableLayout
     * @param width A TableLayout width, so can be TableLayout.FILL or TableLayout.PREFERRED
     *              or a percentage or absolute value
     */
    static void addColumn(ModernTableLayout tl, int col, double width)
    {
        if (col >= tl.getNumColumn())
        {
            tl.insertColumn(col, width);
        }
        else
        {
            tl.setColumn(col, width);
        }
    }

    static void addRow(ModernTableLayout tl, int row, int width)
    {
        Assert.isTrue( width > 0);
        if (row >= tl.getNumRow())
        {
            tl.insertRow(row, width);
        }
        else
        {
            tl.setRow(row, width);
        }
    }
}
