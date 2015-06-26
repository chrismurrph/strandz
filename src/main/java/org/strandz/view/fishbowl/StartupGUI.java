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

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Container;
import java.awt.Dimension;

public class StartupGUI extends JFrame
{
    public JLabel lblImage;

    public StartupGUI()
    {
        this.setBounds(0, 0, 480, 247);

        Container thisContent = this.getContentPane();
        this.setBounds(0, 0, 480, 247);
        this.setFont(new java.awt.Font("dialog", 0, 12));
        this.setTitle(
            "Fishbowl's Recruitment Application");
        lblImage = new JLabel();
        lblImage.setToolTipText("FishBowl Recruitment Consultants at work");
        lblImage.setMaximumSize(new Dimension(480, 64));
        lblImage.setMinimumSize(new Dimension(466, 201));
        lblImage.setName("lblImage");
        lblImage.setLayout(null);
        thisContent.add(lblImage);
    }
}
