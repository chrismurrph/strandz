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
package org.strandz.view.fault;

import org.strandz.lgpl.util.Err;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TrialPanel
{
    private static MainSupportPanel TEST_PANEL = new MainSupportPanel();

    static
    {
        TEST_PANEL.init();
    }

    public static void main(String[] args)
    {
        Err.setBatch(false);

        TrialPanel obj = new TrialPanel();
        obj.init();
    }

    public void init()
    {
        JPanel panel = TEST_PANEL;
        JFrame frame = new JFrame();
        WindowListener l = new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        };
        frame.addWindowListener(l);

        Dimension preferredSize = panel.getPreferredSize();
        // Err.pr( "preferred size was " + preferredSize);
        preferredSize.height = 550;
        preferredSize.width = 750;
        panel.setPreferredSize(preferredSize);
        frame.setContentPane(panel);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - preferredSize.width / 2,
            screenSize.height / 2 - preferredSize.height / 2);
        frame.setVisible(true);
    }
}
