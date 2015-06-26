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

import org.strandz.lgpl.util.LoggingTaskTimeBandMonitor;
import org.strandz.lgpl.util.TaskTimeBandMonitorI;
import org.strandz.lgpl.util.Err;

import javax.swing.*;
import java.awt.*;

public class WidgetUtils
{
    public static TaskTimeBandMonitorI getTimeBandMonitor(int estimatedDuration)
    {
        TaskTimeBandMonitorI result;
        if(!Err.isBatch())
        {
            if(SwingUtilities.isEventDispatchThread() && estimatedDuration > 200 && Err.isVisualDurationMonitor())
            {
                result = new ProgressBarTBM();
            }
            else
            {
                result = new LoggingTaskTimeBandMonitor();
            }
        }
        else
        {
            result = new LoggingTaskTimeBandMonitor();
        }
        return result;
    }

    public static void setLabelProperties(JLabel label, Color color, Font font, Color bg)
    {
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.CENTER);
        if(bg != null)
        {
            label.setBackground(bg);
        }
        label.setOpaque(true);
        if(font != null)
        {
            label.setFont(font);
        }
        if(color != null)
        {
            label.setForeground(color);
        }
    }

    public static int calcPixelWidthOfStringOnComponent( String headingText, Component headingComponent)
    {
        int result;
        FontMetrics fm = headingComponent.getFontMetrics( headingComponent.getFont());
        int width = fm.stringWidth(headingText);
        result = width;
        return result;
    }

    public static long calcWidthFromString( String str, JComponent typicalTextContainer)
    {
        long result;
        int firstCalc = WidgetUtils.calcPixelWidthOfStringOnComponent(str, typicalTextContainer);
        //Err.pr( dataFieldName + " has been calculated at " + firstCalc);
        if(firstCalc < 50)
        {
            //result = (long)(firstCalc * 1.8);
            result = 65;
        }
        else if(firstCalc < 80)
        {
            result = (long)(firstCalc * 1.4);
        }
        else if(firstCalc < 95)
        {
            result = (long)(firstCalc * 1.2);
        }
        else
        {
            result = (long)(firstCalc * 1.05);
        }
        return result;
    }

    /*
     * Dumb to have this separate one if are going to use same thresholds as above, which makes sense
    public static long calcWidthFromNumber( String dataFieldNumber, JComponent typicalTextContainer)
    {
        long result;
        int firstCalc = WidgetUtils.calcPixelWidthOfStringOnComponent(dataFieldNumber, typicalTextContainer);
        //Err.pr( dataFieldNumber + " has been calculated at " + firstCalc);
        if(firstCalc < 50)
        {
            result = 65;
        }
        else if(firstCalc < 80)
        {
            result = (long)(firstCalc * 1.4);
        }
        else if(firstCalc < 95)
        {
            result = (long)(firstCalc * 1.2);
        }
        else
        {
            result = (long)(firstCalc * 1.05);
        }
        return result;
    }
    */
}
