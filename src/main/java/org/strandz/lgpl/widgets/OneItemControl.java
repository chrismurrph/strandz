package org.strandz.lgpl.widgets;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.*;

/**
 * User: Chris
 * Date: 18/04/2009
 * Time: 3:52:16 PM
 */
public class OneItemControl extends JPanel
{
    JTextField tfItemControl;

    //static final int TEXT_HEIGHT = 23;
    static final int BORDER = 15;

    public void init( Integer width)
    {
        if(width == null)
        {
            width = 50;
        }
        tfItemControl = new JTextField();

        double size[][] =
            {   // Columns
                {width, ModernTableLayout.FILL, BORDER},
                // Rows
                {ModernTableLayout.FILL}
            };
        setLayout(new ModernTableLayout(size));

        tfItemControl.setName( "tfItemControl");
        tfItemControl.setHorizontalAlignment( SwingConstants.LEADING);

        add(tfItemControl, "0,0");
        setName( "OneItemControl");
    }
}
