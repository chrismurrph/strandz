package org.strandz.lgpl.tablelayout;

import info.clearthought.layout.TableLayout;

import java.util.LinkedList;

/**
 * TableLayout persistence delegates no longer work with modern JDKs
 * so we use our own, and ModernTableLayout gives them the increased
 * outside package access required.
 *
 * User: Chris
 * Date: 24/10/2008
 * Time: 00:56:38
 */
public class ModernTableLayout extends TableLayout
{
    public ModernTableLayout()
    {
    }

    public ModernTableLayout(double[][] doubles)
    {
        super(doubles);
    }

    public ModernTableLayout(double[] doubles, double[] doubles1)
    {
        super(doubles, doubles1);
    }

    LinkedList getList()
    {
        return list;
    }
}
