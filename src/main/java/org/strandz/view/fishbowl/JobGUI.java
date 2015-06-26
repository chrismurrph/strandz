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

public class JobGUI extends JPanel
{
    // Instance variables
    public JLabel jlabel0;
    public JTextField tfDescription;
    public JLabel jlabel1;
    public JTextField tfContactPerson;
    public JLabel jlabel2;
    public JLabel jlabel3;
    public JLabel jlabel4;
    public JTextField tfPhone;
    public JTextField tfFax;
    public JTextField tfEmail;

    public JobGUI()
    {
        this.setBounds(30, 70, 440, 150);
        this.setSize(new Dimension(440, 150));
        this.setPreferredSize(new Dimension(1, 1));
        this.setMinimumSize(new Dimension(1, 1));
        this.setBounds(new Rectangle(30, 70, 440, 150));
        this.setLocation(new Point(30, 70));
        this.setLayout(null);
        jlabel0 = new JLabel();
        jlabel0.setSize(new Dimension(80, 20));
        jlabel0.setPreferredSize(new Dimension(63, 17));
        jlabel0.setMaximumSize(new Dimension(63, 17));
        jlabel0.setMinimumSize(new Dimension(63, 17));
        jlabel0.setBounds(new Rectangle(20, 10, 80, 20));
        jlabel0.setText("Description");
        jlabel0.setLocation(new Point(20, 10));
        jlabel0.setHorizontalAlignment(JLabel.RIGHT);
        jlabel0.setName("jlabel0");
        jlabel0.setLayout(null);
        this.add(jlabel0);
        tfDescription = new JTextField();
        tfDescription.setLocation(new Point(110, 10));
        tfDescription.setSize(new Dimension(310, 20));
        tfDescription.setBounds(new Rectangle(110, 10, 310, 20));
        tfDescription.setName("tfDescription");
        tfDescription.setLayout(null);
        this.add(tfDescription);
        jlabel1 = new JLabel();
        jlabel1.setSize(new Dimension(90, 20));
        jlabel1.setPreferredSize(new Dimension(85, 17));
        jlabel1.setMaximumSize(new Dimension(85, 17));
        jlabel1.setMinimumSize(new Dimension(85, 17));
        jlabel1.setBounds(new Rectangle(10, 40, 90, 20));
        jlabel1.setText("Contact Person");
        jlabel1.setLocation(new Point(10, 40));
        jlabel1.setHorizontalAlignment(JLabel.RIGHT);
        jlabel1.setName("jlabel1");
        jlabel1.setLayout(null);
        this.add(jlabel1);
        tfContactPerson = new JTextField();
        tfContactPerson.setLocation(new Point(110, 40));
        tfContactPerson.setSize(new Dimension(310, 20));
        tfContactPerson.setBounds(new Rectangle(110, 40, 310, 20));
        tfContactPerson.setName("tfContactPerson");
        tfContactPerson.setLayout(null);
        this.add(tfContactPerson);
        jlabel2 = new JLabel();
        jlabel2.setSize(new Dimension(50, 20));
        jlabel2.setPreferredSize(new Dimension(36, 17));
        jlabel2.setMaximumSize(new Dimension(36, 17));
        jlabel2.setMinimumSize(new Dimension(36, 17));
        jlabel2.setBounds(new Rectangle(10, 70, 50, 20));
        jlabel2.setText("Phone");
        jlabel2.setLocation(new Point(10, 70));
        jlabel2.setHorizontalAlignment(JLabel.RIGHT);
        jlabel2.setName("jlabel2");
        jlabel2.setLayout(null);
        this.add(jlabel2);
        jlabel3 = new JLabel();
        jlabel3.setSize(new Dimension(50, 20));
        jlabel3.setPreferredSize(new Dimension(21, 17));
        jlabel3.setMaximumSize(new Dimension(21, 17));
        jlabel3.setMinimumSize(new Dimension(21, 17));
        jlabel3.setBounds(new Rectangle(10, 90, 50, 20));
        jlabel3.setText("FAX");
        jlabel3.setLocation(new Point(10, 90));
        jlabel3.setHorizontalAlignment(JLabel.RIGHT);
        jlabel3.setName("jlabel3");
        jlabel3.setLayout(null);
        this.add(jlabel3);
        jlabel4 = new JLabel();
        jlabel4.setSize(new Dimension(50, 20));
        jlabel4.setPreferredSize(new Dimension(31, 17));
        jlabel4.setMaximumSize(new Dimension(31, 17));
        jlabel4.setMinimumSize(new Dimension(31, 17));
        jlabel4.setBounds(new Rectangle(10, 110, 50, 20));
        jlabel4.setText("email");
        jlabel4.setLocation(new Point(10, 110));
        jlabel4.setHorizontalAlignment(JLabel.RIGHT);
        jlabel4.setName("jlabel4");
        jlabel4.setLayout(null);
        this.add(jlabel4);
        tfPhone = new JTextField();
        tfPhone.setLocation(new Point(70, 70));
        tfPhone.setSize(new Dimension(170, 20));
        tfPhone.setBounds(new Rectangle(70, 70, 170, 20));
        tfPhone.setName("tfPhone");
        tfPhone.setLayout(null);
        this.add(tfPhone);
        tfFax = new JTextField();
        tfFax.setLocation(new Point(70, 90));
        tfFax.setSize(new Dimension(170, 20));
        tfFax.setBounds(new Rectangle(70, 90, 170, 20));
        tfFax.setName("tfFax");
        tfFax.setLayout(null);
        this.add(tfFax);
        tfEmail = new JTextField();
        tfEmail.setLocation(new Point(70, 110));
        tfEmail.setSize(new Dimension(170, 20));
        tfEmail.setBounds(new Rectangle(70, 110, 170, 20));
        tfEmail.setName("tfEmail");
        tfEmail.setLayout(null);
        this.add(tfEmail);
    }
}
