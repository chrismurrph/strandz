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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URL;

public class ClientGUI extends JPanel
{
    // Instance variables
    public JLabel jlabel0;
    public JTextField tfCompanyName;
    public JLabel jlabel2;
    public JLabel jlabel3;
    public JLabel jlabel4;
    public JTextField tfPhone;
    public JTextField tfFax;
    public JTextField tfEmail;
    // public JTextArea taCompanyDescription;
    public JTextArea tfCompanyDescription;
    public JLabel jlabel1;
    public JLabel jlabel5;
    public JLabel jlabel6;
    public JComboBox cbIndustry;
    public JComboBox cbClientType;
    public JComboBox cbEndClient;

    public JComboBox getIndustryControl()
    {
        return cbIndustry;
    }

     /**/
    public void setIndustryControl(JComboBox cbIndustry)
    {
        this.cbIndustry = cbIndustry;
    }

     /**/
    public JComboBox getClientTypeControl()
    {
        return cbClientType;
    }

     /**/
    public void setClientTypeControl(JComboBox cbClientType)
    {
        this.cbClientType = cbClientType;
    }

     /**/
    public JComboBox getEndClientControl()
    {
        return cbEndClient;
    }

     /**/
    public void setEndClientControl(JComboBox cbEndClient)
    {
        this.cbEndClient = cbEndClient;
    }

     /**/
    public ClientGUI()
    {
        this.setBounds(0, 0, 356, 219);
        this.setSize(new Dimension(356, 219));
        this.setPreferredSize(new Dimension(1, 1));
        this.setMinimumSize(new Dimension(1, 1));
        this.setBounds(new Rectangle(0, 0, 356, 219));
        // this.setName("this");
        this.setLayout(null);
    }

    public void init()
    {
        jlabel0 = new JLabel();
        jlabel0.setSize(new Dimension(42, 21));
        jlabel0.setPreferredSize(new Dimension(90, 17));
        jlabel0.setMaximumSize(new Dimension(90, 17));
        jlabel0.setMinimumSize(new Dimension(90, 17));
        jlabel0.setBounds(new Rectangle(35, 7, 42, 21));
        jlabel0.setText("Name");
        jlabel0.setLocation(new Point(35, 7));
        jlabel0.setName("jlabel0");
        jlabel0.setLayout(null);
        this.add(jlabel0);
        jlabel2 = new JLabel();
        jlabel2.setSize(new Dimension(42, 21));
        jlabel2.setPreferredSize(new Dimension(36, 17));
        jlabel2.setMaximumSize(new Dimension(36, 17));
        jlabel2.setMinimumSize(new Dimension(36, 17));
        jlabel2.setBounds(new Rectangle(28, 91, 42, 21));
        jlabel2.setText("Phone");
        jlabel2.setLocation(new Point(28, 91));
        jlabel2.setHorizontalAlignment(JLabel.RIGHT);
        jlabel2.setName("jlabel2");
        jlabel2.setLayout(null);
        this.add(jlabel2);
        jlabel3 = new JLabel();
        jlabel3.setSize(new Dimension(35, 21));
        jlabel3.setPreferredSize(new Dimension(21, 17));
        jlabel3.setMaximumSize(new Dimension(21, 17));
        jlabel3.setMinimumSize(new Dimension(21, 17));
        jlabel3.setBounds(new Rectangle(35, 112, 35, 21));
        jlabel3.setText("FAX");
        jlabel3.setLocation(new Point(35, 112));
        jlabel3.setHorizontalAlignment(JLabel.RIGHT);
        jlabel3.setName("jlabel3");
        jlabel3.setLayout(null);
        this.add(jlabel3);
        jlabel4 = new JLabel();
        jlabel4.setSize(new Dimension(42, 21));
        jlabel4.setPreferredSize(new Dimension(31, 17));
        jlabel4.setMaximumSize(new Dimension(31, 17));
        jlabel4.setMinimumSize(new Dimension(31, 17));
        jlabel4.setBounds(new Rectangle(28, 133, 42, 21));
        jlabel4.setText("email");
        jlabel4.setLocation(new Point(28, 133));
        jlabel4.setHorizontalAlignment(JLabel.RIGHT);
        jlabel4.setName("jlabel4");
        jlabel4.setLayout(null);
        this.add(jlabel4);
        tfCompanyName = new JTextField();
        tfCompanyName.setLocation(new Point(77, 7));
        tfCompanyName.setSize(new Dimension(273, 21));
        tfCompanyName.setBounds(new Rectangle(77, 7, 273, 21));
        tfCompanyName.setName("tfCompanyName");
        tfCompanyName.setLayout(null);
        tfPhone = new JTextField();
        tfPhone.setLocation(new Point(77, 91));
        tfPhone.setSize(new Dimension(168, 21));
        tfPhone.setBounds(new Rectangle(77, 91, 168, 21));
        tfPhone.setName("tfPhone");
        tfPhone.setLayout(null);
        tfFax = new JTextField();
        tfFax.setLocation(new Point(77, 112));
        tfFax.setSize(new Dimension(168, 21));
        tfFax.setBounds(new Rectangle(77, 112, 168, 21));
        tfFax.setName("tfFax");
        tfFax.setLayout(null);
        tfEmail = new JTextField();
        tfEmail.setLocation(new Point(77, 133));
        tfEmail.setSize(new Dimension(273, 21));
        tfEmail.setBounds(new Rectangle(77, 133, 273, 21));
        tfEmail.setName("tfEmail");
        tfEmail.setLayout(null);
        tfCompanyDescription = new JTextArea();
        tfCompanyDescription.setLocation(new Point(20, 28));
        tfCompanyDescription.setSize(new Dimension(328, 60));
        tfCompanyDescription.setBounds(new Rectangle(20, 28, 328, 60));
        tfCompanyDescription.setName("tfCompanyDescription");
        // tfCompanyDescription.setBackground( Color.RED);
        tfCompanyDescription.setLayout(null);
        cbClientType = new JComboBox();
        cbClientType.setSize(new Dimension(273, 21));
        cbClientType.setBounds(new Rectangle(77, 175, 273, 21));
        cbClientType.setLocation(new Point(77, 175));
        cbClientType.setName("cbClientType");
        jlabel1 = new JLabel();
        jlabel1.setSize(new Dimension(56, 21));
        jlabel1.setPreferredSize(new Dimension(43, 17));
        jlabel1.setMaximumSize(new Dimension(43, 17));
        jlabel1.setMinimumSize(new Dimension(43, 17));
        jlabel1.setBounds(new Rectangle(14, 154, 56, 21));
        jlabel1.setText("Industry");
        jlabel1.setLocation(new Point(14, 154));
        jlabel1.setHorizontalAlignment(JLabel.RIGHT);
        jlabel1.setName("jlabel1");
        jlabel1.setLayout(null);
        this.add(jlabel1);
        jlabel5 = new JLabel();
        jlabel5.setSize(new Dimension(56, 21));
        jlabel5.setPreferredSize(new Dimension(26, 17));
        jlabel5.setMaximumSize(new Dimension(26, 17));
        jlabel5.setMinimumSize(new Dimension(26, 17));
        jlabel5.setBounds(new Rectangle(14, 175, 56, 21));
        jlabel5.setText("Type");
        jlabel5.setLocation(new Point(14, 175));
        jlabel5.setHorizontalAlignment(JLabel.RIGHT);
        jlabel5.setName("jlabel5");
        jlabel5.setLayout(null);
        this.add(jlabel5);
        jlabel6 = new JLabel();
        jlabel6.setSize(new Dimension(63, 22));
        jlabel6.setPreferredSize(new Dimension(57, 17));
        jlabel6.setMaximumSize(new Dimension(57, 17));
        jlabel6.setMinimumSize(new Dimension(57, 17));
        jlabel6.setBounds(new Rectangle(8, 193, 63, 22));
        jlabel6.setText("End Client");
        jlabel6.setLocation(new Point(8, 193));
        jlabel6.setHorizontalAlignment(JLabel.RIGHT);
        jlabel6.setName("jlabel6");
        jlabel6.setLayout(null);
        this.add(jlabel6);
        cbEndClient = new JComboBox();
        cbEndClient.setSize(new Dimension(273, 21));
        cbEndClient.setBounds(new Rectangle(77, 196, 273, 21));
        cbEndClient.setLocation(new Point(77, 196));
        cbEndClient.setName("cbEndClient");
        cbIndustry = new JComboBox();
        cbIndustry.setSize(new Dimension(273, 21));
        cbIndustry.setBounds(new Rectangle(77, 154, 273, 21));
        cbIndustry.setLocation(new Point(77, 154));
        cbIndustry.setName("cbIndustry");
        this.add(cbEndClient);
        this.add(cbClientType);
        this.add(tfCompanyName);
        this.add(tfPhone);
        this.add(tfFax);
        this.add(tfEmail);
        this.add(tfCompanyDescription);
        this.add(cbIndustry);
    }

    URL getURL(String txtURL)
    {
        // return a URL object given the URL
        try
        {
            if(txtURL == null)
            {
                return new URL("file://");
            }
            else
            {
                return new URL(txtURL);
            }
        }
        catch(MalformedURLException mue)
        {
            return null;
        }
    }

    public String[][] getIconPathForComponent(Component comp, String methodName)
    {
        // return an Icon path(s) for a given Component/Method
        {
            String[][] rval = {{null}};
            return rval;
        }
    }

    private String getReference(String[][] references, String methodName)
    {
        for(int i = 0; i < references.length; i++)
        {
            if(references[i][0] == methodName)
            {
                return references[i][1];
            }
        }
        return null;
    }
}
