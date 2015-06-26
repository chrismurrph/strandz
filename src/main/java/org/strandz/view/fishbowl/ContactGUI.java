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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class ContactGUI extends JPanel
{
    public JLabel jlabel0;
    public JTextField tfName;

    public ContactGUI()
    {
        this.setBounds(130, 70, 240, 120);
        this.setSize(new Dimension(240, 120));
        this.setPreferredSize(new Dimension(1, 1));
        this.setMinimumSize(new Dimension(1, 1));
        this.setBounds(new Rectangle(130, 70, 240, 120));
        this.setLocation(new Point(130, 70));
        this.setLayout(null);
        jlabel0 = new JLabel();
        jlabel0.setSize(new Dimension(80, 20));
        jlabel0.setPreferredSize(new Dimension(79, 17));
        jlabel0.setMaximumSize(new Dimension(79, 17));
        jlabel0.setMinimumSize(new Dimension(79, 17));
        jlabel0.setBounds(new Rectangle(10, 10, 80, 20));
        jlabel0.setText("Contact Name");
        jlabel0.setLocation(new Point(10, 10));
        jlabel0.setName("jlabel0");
        jlabel0.setLayout(null);
        this.add(jlabel0);
        tfName = new JTextField();
        tfName.setLocation(new Point(10, 40));
        tfName.setSize(new Dimension(200, 20));
        tfName.setBounds(new Rectangle(10, 40, 200, 20));
        tfName.setName("tfName");
        tfName.setLayout(null);
        this.add(tfName);
    }
}
