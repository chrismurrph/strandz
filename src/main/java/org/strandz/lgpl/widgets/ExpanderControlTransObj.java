package org.strandz.lgpl.widgets;

import javax.swing.*;
import java.awt.*;

/**
 * One of these for each title. If a property is null then it is not used.
 *
 * User: Chris
 * Date: 12/05/2009
 * Time: 10:20:33 AM
 */
public class ExpanderControlTransObj
{
    private Color background;
    private Color foreground;
    private String toolTipText;
    private IconLabel iconLabel;
    private String iconToolTipText;

    public Color getBackground()
    {
        return background;
    }

    public void setBackground(Color background)
    {
        this.background = background;
    }

    public Color getForeground()
    {
        return foreground;
    }

    public void setForeground(Color foreground)
    {
        this.foreground = foreground;
    }

    public String getToolTipText()
    {
        return toolTipText;
    }

    public void setToolTipText(String toolTipText)
    {
        this.toolTipText = toolTipText;
    }

    public IconLabel getIconLabel()
    {
        return iconLabel;
    }

    public void setIconLabel(IconLabel iconLabel)
    {
        this.iconLabel = iconLabel;
    }

    public String getIconToolTipText()
    {
        return iconToolTipText;
    }

    public void setIconToolTipText(String iconToolTipText)
    {
        this.iconToolTipText = iconToolTipText;
    }
}
