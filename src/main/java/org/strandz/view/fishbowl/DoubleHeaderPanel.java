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
package org.strandz.view.fishbowl;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.Print;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DoubleHeaderPanel extends JPanel
{
    final static int SEPERATOR_WIDTH = 10;
    final static int ROW_HEIGHT = 100;
    final static int SHORT_ROW_HEIGHT = 25;
    final static int BORDER = 12;
    public ClientPanel clientPanel;
    public JobTypePanel jobTypePanel;
    public JobPanel jobPanel;

    public static void main(String args[])
    {
        // Create a frame
        Frame frame = new Frame("Double Header Screen");
        /*
        //frame.setBounds (100, 100, 300, 300);

        // Create a TableLayout for the frame
        double size[][] =
        {{0.50, 0.50},  // Columns
        {0.50, 0.50}}; // Rows

        frame.setLayout (new TableLayout(size));

        ClientPanel clientPanel = new ClientPanel();
        clientPanel.init();
        JobTypePanel jobTypePanel = new JobTypePanel();
        jobTypePanel.init();
        JobPanel jobPanel = new JobPanel();
        jobPanel.init();
        frame.add ( clientPanel, "0, 0");
        frame.add ( jobTypePanel, "1, 0");
        frame.add ( jobPanel, "0, 1, 1, 1");
        */
        DoubleHeaderPanel dh = new DoubleHeaderPanel();
        dh.init();
        frame.add(dh);
        // Allow user to close the window to terminate the program
        frame.addWindowListener
            (new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    System.exit(0);
                }
            });
        // Make the frame understand the preferred sizes
        frame.pack();
        // Show frame
        frame.setVisible(true);
    }

    public void init()
    {
        // Create a TableLayout for the panel
        double size[][] = {{0.50, 0.50}, // Columns
            {0.50, 0.50}}; // Rows
        setLayout(new ModernTableLayout(size));
        clientPanel = new ClientPanel();
        clientPanel.init();
        jobTypePanel = new JobTypePanel();
        jobTypePanel.init();
        jobPanel = new JobPanel();
        jobPanel.init();
        Print.pr("About to add clientPanel to DoubleHeader with constraints");
        add(clientPanel, "0, 0");
        add(jobTypePanel, "1, 0");
        add(jobPanel, "0, 1, 1, 1");
        setName("Double Header Outside Panel");
        Print.pr("Inside DoubleHeader.init()");
    }
}
