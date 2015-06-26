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

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;

public class TrafficLightsComponent extends JPanel
{
    private TrafficLight rbRed;
    private TrafficLight rbAmber;
    private TrafficLight rbGreen;
    
    private static final Color AMBER = new Color(255, 126, 0);
    
    private static final int BUTTON_WIDTH = 23;
    private static final int BUTTON_HEIGHT = 23;
    
    private static class FillPanel extends JPanel
    {
        private FillPanel()
        {
            setOpaque( true);
            setBackground( Color.WHITE);                
        }
    }
    
    public void init()    
    {
        rbRed = new TrafficLight( Color.RED);
        rbAmber = new TrafficLight( AMBER);
        rbGreen = new TrafficLight( Color.GREEN);
        
        double size[][] =
            {   // Columns
                {ModernTableLayout.FILL},
                // Rows
                {6, 0.34, 0.33, 0.33}
            };
        setLayout( new ModernTableLayout(size));
        add( new FillPanel(), "0, 0");
        add( rbRed, "0, 1");
        add( rbAmber, "0, 2");
        add( rbGreen, "0, 3");
    }
    
    private Dimension getAlwaysSize()
    {
        Dimension result = super.getMaximumSize();
        result.height = BUTTON_HEIGHT*3-15;
        result.width = BUTTON_WIDTH;
        return result;
    }

    public Dimension getMaximumSize()
    {
        return getAlwaysSize();
    }
    
    public Dimension getMinimumSize()
    {
        return getAlwaysSize();
    }
    
    public Dimension getPreferredSize()
    {
        return getAlwaysSize();
    }

    public TrafficLight getRed()
    {
        return rbRed;
    }

    public TrafficLight getAmber()
    {
        return rbAmber;
    }

    public TrafficLight getGreen()
    {
        return rbGreen;
    }
}