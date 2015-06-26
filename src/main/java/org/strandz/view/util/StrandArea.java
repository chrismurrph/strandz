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

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.Assert;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

public class StrandArea extends AbstractStrandArea
{
    // One or other - experimenting!
    private ToolBarArea toolBarArea;
    private ToolBarPane toolBarPane;
    private StatusArea statusArea;
    private boolean doingKnownLayout = false;
    public static final boolean USE_TOOL_BAR_AREA = true;
    static final int TOOLBAR_HEIGHT = 40;
    static final int STATUS_HEIGHT = 25;
    
    void layoutContentPanel( JPanel contentPanel)
    {
        // Create a TableLayout for the panel
        double size[][] = // Columns
            {{
                ModernTableLayout.FILL,
            }, // Rows
                {TOOLBAR_HEIGHT, ModernTableLayout.FILL, STATUS_HEIGHT
                }};
        contentPanel.setLayout(new ModernTableLayout(size));
        // setBorder( empty);
        
        doingKnownLayout = true;
        contentPanel.add(getEnclosure(), "0, 1");
        statusArea = new StatusArea();
        contentPanel.add(statusArea, "0, 2");
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        if(USE_TOOL_BAR_AREA)
        {
            toolBarArea = new ToolBarArea();
            contentPanel.add(toolBarArea, "0, 0");
        }
        else
        {
            toolBarPane = new ToolBarPane();
            contentPanel.add(toolBarPane, "0, 0");
        }
        doingKnownLayout = false;
    }
    
    public void add(Component comp, Object constraints) 
    {
        Assert.isTrue( doingKnownLayout, "To add a component call getEnclosure().add(...) - don't add directly to " + 
                this.getClass().getName());
        super.add( comp, constraints);
    }

    public StatusArea getStatusArea()
    {
        return statusArea;
    }

    public ToolBarArea getToolBarArea()
    {
        return toolBarArea;
    }

    public ToolBarPane getToolBarPane()
    {
        return toolBarPane;
    }
}
