package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import java.awt.*;

/**
 * User: Chris
 * Date: 22/01/2009
 * Time: 4:49:00 PM
 */
class CTVLayout
{
    private ModernTableLayout tableLayout;

    static CTVLayout newInstance()
    {
        return new CTVLayout( new ModernTableLayout());
    }

    private CTVLayout( ModernTableLayout tableLayout)
    {
        this.tableLayout = tableLayout;
    }

    void addRow( int row, int width)
    {
        CTVUtils.addRow( tableLayout, row, width);
    }

    void addColumn( int col, double width)
    {
        CTVUtils.addColumn( tableLayout, col, width);
    }

    LayoutManager2 getLayoutManager2()
    {
        return tableLayout;
    }
}
