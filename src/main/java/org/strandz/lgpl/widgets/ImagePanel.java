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
import org.strandz.lgpl.util.Err;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;

/**
 * This panel was supposed to be able to stretch its image to its size
 * but haven't got around to it yet.
 */
public class ImagePanel extends JPanel
{
    public JLabel label;
    private String iconFile;
    private String iconDesc;
    private String toolTipText;

    public ImagePanel(String iconFile, String iconDesc, String toolTipText, Color background)
    {
        this.iconFile = iconFile;
        this.iconDesc = iconDesc;
        this.toolTipText = toolTipText;
        setBackground(background);
    }

    public ImagePanel(String iconFile, String toolTipText, Color background)
    {
        this.iconFile = iconFile;
        this.iconDesc = toolTipText;
        this.toolTipText = toolTipText;
        setBackground(background);
    }

    public void init()
    {
        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);

        double size[][] =
            { // Columns
                {ModernTableLayout.FILL},
                // Rows
                {ModernTableLayout.FILL}
            };
        setLayout(new ModernTableLayout(size));

        add(label, "0, 0");
        setName("ImagePanel");

        //Only for buttons
        //label.setMargin( new Insets( 0, 0, 0, 0));

        Icon icon = PortableImageIcon.createImageIcon(iconFile, iconDesc);
        if(icon == null)
        {
            Err.alarm("Could not load " + iconFile);
        }
        else
        {
            label.setIcon(icon);
            label.setToolTipText(toolTipText);
            label.setName("lblImage");
        }
    }
}
