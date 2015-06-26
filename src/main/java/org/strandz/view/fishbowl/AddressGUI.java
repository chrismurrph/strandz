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
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class AddressGUI extends JPanel
{
    public JTextField tfStreet;
    public JTextField tfSuburb;
    public JTextField tfState;
    public JTextField tfPostcode;
    public JLabel jlabel1;
    public JLabel jlabel2;
    public JLabel jlabel3;
    public JLabel jlabel4;
    private Border thisBorder1;
    private Border titledBorder;
    private String title;

    public static class DebugTextField extends JTextField
    {
        /*
        public DebugTextField()
        {
        Err.pr( "@@@ construct DebugTextField");
        }
        public void setName( String name)
        {
        super.setName( name);
        }
        public String getName()
        {
        return super.getName();
        }
        */
    }

    public AddressGUI()
    {
        this.setBounds(0, 10, 360, 110);
        this.setSize(new Dimension(360, 110));
        this.setPreferredSize(new Dimension(1, 1));
        thisBorder1 = new EtchedBorder();
        titledBorder = new TitledBorder(thisBorder1, "Address");
        this.setBorder(titledBorder);
        this.setMinimumSize(new Dimension(1, 1));
        this.setBounds(new Rectangle(0, 10, 360, 110));
        this.setLocation(new Point(0, 10));
        this.setLayout(null);
        jlabel1 = new JLabel();
        jlabel1.setSize(new Dimension(50, 20));
        jlabel1.setPreferredSize(new Dimension(32, 17));
        jlabel1.setMaximumSize(new Dimension(32, 17));
        jlabel1.setMinimumSize(new Dimension(32, 17));
        jlabel1.setBounds(new Rectangle(10, 20, 50, 20));
        jlabel1.setText("Street");
        jlabel1.setLocation(new Point(10, 20));
        jlabel1.setHorizontalAlignment(JLabel.RIGHT);
        jlabel1.setName("jlabel1");
        jlabel1.setLayout(null);
        this.add(jlabel1);
        jlabel2 = new JLabel();
        jlabel2.setSize(new Dimension(50, 20));
        jlabel2.setPreferredSize(new Dimension(40, 17));
        jlabel2.setMaximumSize(new Dimension(40, 17));
        jlabel2.setMinimumSize(new Dimension(40, 17));
        jlabel2.setBounds(new Rectangle(10, 50, 50, 20));
        jlabel2.setText("Suburb");
        jlabel2.setLocation(new Point(10, 50));
        jlabel2.setHorizontalAlignment(JLabel.RIGHT);
        jlabel2.setName("jlabel2");
        jlabel2.setLayout(null);
        this.add(jlabel2);
        jlabel3 = new JLabel();
        jlabel3.setSize(new Dimension(50, 20));
        jlabel3.setPreferredSize(new Dimension(28, 17));
        jlabel3.setMaximumSize(new Dimension(28, 17));
        jlabel3.setMinimumSize(new Dimension(28, 17));
        jlabel3.setBounds(new Rectangle(10, 80, 50, 20));
        jlabel3.setText("State");
        jlabel3.setLocation(new Point(10, 80));
        jlabel3.setHorizontalAlignment(JLabel.RIGHT);
        jlabel3.setName("jlabel3");
        jlabel3.setLayout(null);
        this.add(jlabel3);
        jlabel4 = new JLabel();
        jlabel4.setSize(new Dimension(70, 20));
        jlabel4.setPreferredSize(new Dimension(52, 17));
        jlabel4.setMaximumSize(new Dimension(52, 17));
        jlabel4.setMinimumSize(new Dimension(52, 17));
        jlabel4.setBounds(new Rectangle(200, 80, 70, 20));
        jlabel4.setText("Postcode");
        jlabel4.setLocation(new Point(200, 80));
        jlabel4.setHorizontalAlignment(JLabel.RIGHT);
        jlabel4.setName("jlabel4");
        jlabel4.setLayout(null);
        this.add(jlabel4);
    }

    public void init()
    {
        tfStreet = new JTextField();
        tfStreet.setLocation(new Point(70, 20));
        tfStreet.setSize(new Dimension(280, 20));
        tfStreet.setBounds(new Rectangle(70, 20, 280, 20));
        tfStreet.setName("tfStreet");
        tfStreet.setLayout(null);
        tfSuburb = new JTextField();
        tfSuburb.setLocation(new Point(70, 50));
        tfSuburb.setSize(new Dimension(280, 20));
        tfSuburb.setBounds(new Rectangle(70, 50, 280, 20));
        tfSuburb.setName("tfSuburb");
        tfSuburb.setLayout(null);
        tfState = new JTextField();
        tfState.setLocation(new Point(70, 80));
        tfState.setSize(new Dimension(80, 20));
        tfState.setBounds(new Rectangle(70, 80, 80, 20));
        tfState.setName("tfState");
        tfState.setLayout(null);
        tfPostcode = new JTextField();
        tfPostcode.setLocation(new Point(280, 80));
        tfPostcode.setSize(new Dimension(70, 20));
        tfPostcode.setBounds(new Rectangle(280, 80, 70, 20));
        tfPostcode.setName("tfPostcode");
        tfPostcode.setLayout(null);
        this.add(tfStreet);
        this.add(tfSuburb);
        this.add(tfState);
        this.add(tfPostcode);
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        titledBorder = new TitledBorder(thisBorder1, title);
        this.title = title;
        this.setBorder(titledBorder);
    }
}
