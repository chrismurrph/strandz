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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * This inner class has been designed so that having a controller
 * area is optional. If you never call refitStatusArea then
 * you will never see a status panel at the bottom. If you want
 * to get rid of the panel that is already there then calling
 * refitStatusArea with a null param will do the trick.
 * <p/>
 * ? Unclear as to whether works with both Controller and StatusArea,
 * but probably does!
 * <p/>
 * Probably statusArea stuff crap, so renamed ToolBarPane
 */
public class ToolBarPane extends JPanel
{
    private JComponent controllerAdded = null;
    static final int HEIGHT = 34;
    private static int times = 0;

    public ToolBarPane()
    {
        setLayout(new BorderLayout());
        times++;
        setName("Control Pane " + times);
    }

    /**
     * So don't get jumping around when refitControlArea is later
     * called.
     */
    public Dimension getPreferredSize()
    {
        Dimension dim = super.getPreferredSize();
        if(controllerAdded == null)
        {
            dim.height = HEIGHT;
        } // else the contained panel's height will have
        // figgered in dim value
        else if(dim.height != HEIGHT)
        {
            Print.pr(
                "WARNING Might get some jumping effects as "
                    + "enclosed control is now " + dim.height + " high NOT " + HEIGHT);
        }
        return dim;
    }

    public void refitControllerArea(JComponent panel)
    {
        Err.pr(
            "@@@ refitControllerArea: " + panel.getClass().getName() + ", "
                + panel.getName());
        if(panel != null)
        {
            removeAll();
            add(panel);
            controllerAdded = panel;
            /*
            * Needed revalidate call for first time
            * manually focused on a different internal
            * frame.
            */
            revalidate();
            repaint();
        }
        else
        {
            removeAll();
            controllerAdded = null;
        }
    }

    public JComponent getControllerArea()
    {
        return controllerAdded;
    }
}
