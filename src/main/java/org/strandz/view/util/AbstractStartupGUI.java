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
package org.strandz.view.util;

import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.widgets.PortableImageIcon;
import org.strandz.lgpl.widgets.ImagePanel;

import javax.swing.*;
import java.util.Properties;
import java.awt.*;

/**
 * This is the small screen that you get at the beginning.
 */
abstract public class AbstractStartupGUI extends JFrame
{
    public String titleText;
    public String version;
    protected Properties properties;

    static final String ICON_FILE = "images/Splash.jpg";
    static final String ICON_DESC = "Splash";
    static final String CORNER_ICON_FILE = "images/Icon.gif";

    private static int times;
    
    abstract protected String getPropertyFileName();

    public AbstractStartupGUI()
    {
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground", "true");
        String propFileName = getPropertyFileName();
        properties = PropertyUtils.getPortableProperties(propFileName, this);
        titleText = PropertyUtils.getProperty("titleText", properties);
        version = PropertyUtils.getProperty("strandzVersionNumber", properties);
        String toolTipText = PropertyUtils.getProperty("toolTipText", properties);
        Container thisContent = this.getContentPane();
        this.setFont(new Font("dialog", 0, 12));
        this.setTitle(titleText);
        PortableImageIcon icon = PortableImageIcon.createImageIcon(AbstractStartupGUI.CORNER_ICON_FILE, "Strandz");
        if(icon == null)
        {
            Err.alarm("Could not load " + AbstractStartupGUI.CORNER_ICON_FILE);
        }
        else
        {
            Image cornerImage = icon.getImage();
            this.setIconImage(cornerImage);
        }

        thisContent.setLayout(new BorderLayout());
        ImagePanel imagePanel = new ImagePanel(AbstractStartupGUI.ICON_FILE, AbstractStartupGUI.ICON_DESC, toolTipText, Color.black);
        imagePanel.init();
        thisContent.add(imagePanel, BorderLayout.CENTER);
        MessageDlg.setFrame(this);
        MessageDlg.setDlgParent(imagePanel);

        Dimension preferredSize = DisplayUtils.setPreferredSize((JComponent) thisContent, DisplayUtils.DEFAULT_HEIGHT - 60, DisplayUtils.DEFAULT_WIDTH / 2 + 150);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - preferredSize.width / 2,
                    screenSize.height / 2 - preferredSize.height / 2);
    }

}
