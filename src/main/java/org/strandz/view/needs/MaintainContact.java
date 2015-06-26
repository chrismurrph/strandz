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
package org.strandz.view.needs;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MaintainContact extends JPanel
{
    AlphabetPanel alphabetPanel;
    JLabel lFirstName;
    JTextField tfFirstName;
    JLabel lSecondName;
    JTextField tfSecondName;
    JLabel lJobTitle;
    JTextField tfJobTitle;
    JLabel lStreet;
    JTextField tfStreet;
    JLabel lSuburb;
    JTextField tfSuburb;
    JLabel lPostcode;
    JTextField tfPostcode;
    JLabel lDayPhone;
    JTextField tfDayPhone;
    JLabel lEveningPhone;
    JTextField tfEveningPhone;
    JLabel lMobilePhone;
    JTextField tfMobilePhone;
    JLabel lCompanyName;
    JTextField tfCompanyName;
    JLabel lEmail1;
    JTextField tfEmail1;
    JLabel lEmail2;
    JTextField tfEmail2;
    public JLabel lComments;
    JTextArea taComments;
    static final int GAP = 15;
    static final int BORDER = 15;
    static final int SMALL_SPACING = 2;
    static final int BIG_SPACING = 13;
    static final int TEXT_HEIGHT = 23;

    public void init()
    {
        alphabetPanel = new AlphabetPanel();
        alphabetPanel.init();
        lFirstName = new JLabel();
        tfFirstName = new JTextField();
        lSecondName = new JLabel();
        tfSecondName = new JTextField();
        lJobTitle = new JLabel();
        tfJobTitle = new JTextField();
        lStreet = new JLabel();
        tfStreet = new JTextField();
        lSuburb = new JLabel();
        tfSuburb = new JTextField();
        lPostcode = new JLabel();
        tfPostcode = new JTextField();
        lDayPhone = new JLabel();
        tfDayPhone = new JTextField();
        lEveningPhone = new JLabel();
        tfEveningPhone = new JTextField();
        lMobilePhone = new JLabel();
        tfMobilePhone = new JTextField();
        lCompanyName = new JLabel();
        tfCompanyName = new JTextField();
        lEmail1 = new JLabel();
        tfEmail1 = new JTextField();
        lEmail2 = new JLabel();
        tfEmail2 = new JTextField();
        lComments = new JLabel();
        taComments = new JTextArea();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {BORDER, 0.17, GAP, 0.32, GAP, 0.17, GAP, ModernTableLayout.FILL, BORDER},
                // Rows
                {
                    ModernTableLayout.FILL, TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT,
                    SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT,
                    SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT,
                    ModernTableLayout.FILL}};
        setLayout(new ModernTableLayout(size));
        lFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
        lFirstName.setText("First Name");
        tfFirstName.setName("tfFirstName");
        lSecondName.setHorizontalAlignment(SwingConstants.TRAILING);
        lSecondName.setText("Second Name");
        tfSecondName.setName("tfSecondName");
        lJobTitle.setHorizontalAlignment(SwingConstants.TRAILING);
        lJobTitle.setText("Job Title");
        tfJobTitle.setName("tfJobTitle");
        lCompanyName.setHorizontalAlignment(SwingConstants.TRAILING);
        lCompanyName.setText("Company Name");
        tfCompanyName.setName("tfCompanyName");
        lStreet.setHorizontalAlignment(SwingConstants.TRAILING);
        lStreet.setText("Street");
        tfStreet.setName("tfStreet");
        lSuburb.setHorizontalAlignment(SwingConstants.TRAILING);
        lSuburb.setText("Suburb");
        tfSuburb.setName("tfSuburb");
        lPostcode.setHorizontalAlignment(SwingConstants.TRAILING);
        lPostcode.setText("Postcode");
        tfPostcode.setName("tfPostcode");
        lDayPhone.setHorizontalAlignment(SwingConstants.TRAILING);
        lDayPhone.setText("Day Phone");
        tfDayPhone.setName("tfDayPhone");
        lEveningPhone.setHorizontalAlignment(SwingConstants.TRAILING);
        lEveningPhone.setText("Evening Phone");
        tfEveningPhone.setName("tfEveningPhone");
        lMobilePhone.setHorizontalAlignment(SwingConstants.TRAILING);
        lMobilePhone.setText("Mobile Phone");
        tfMobilePhone.setName("tfMobilePhone");
        lEmail1.setHorizontalAlignment(SwingConstants.TRAILING);
        lEmail1.setText("Email 1");
        tfEmail1.setName("tfEmail1");
        lEmail2.setHorizontalAlignment(SwingConstants.TRAILING);
        lEmail2.setText("Email 2");
        tfEmail2.setName("tfEmail2");
        lComments.setHorizontalAlignment(SwingConstants.LEADING);
        lComments.setText("Comments");
        taComments.setName("taComments");
        add(alphabetPanel, "0, 0, 7, 0");
        add(lCompanyName, "1, 1");
        add(tfCompanyName, "3, 1");
        add(lJobTitle, "1, 3");
        add(tfJobTitle, "3, 3");
        add(lFirstName, "1, 5");
        add(tfFirstName, "3, 5");
        add(lSecondName, "1, 7");
        add(tfSecondName, "3, 7");
        add(lStreet, "1, 9");
        add(tfStreet, "3, 9");
        add(lSuburb, "1, 11");
        add(tfSuburb, "3, 11");
        add(lPostcode, "1, 13");
        add(tfPostcode, "3, 13");
        add(lDayPhone, "1, 15");
        add(tfDayPhone, "3, 15");
        add(lEveningPhone, "1, 17");
        add(tfEveningPhone, "3, 17");
        add(lMobilePhone, "1, 19");
        add(tfMobilePhone, "3, 19");
        add(lEmail1, "5, 3");
        add(tfEmail1, "7, 3");
        add(lEmail2, "5, 5");
        add(tfEmail2, "7, 5");
        add(lComments, "1, 23");

        JScrollPane sp = new JScrollPane(taComments);
        add(sp, "1, 24, 4, 29");
        setName("MaintainContact");
        setAlphabetPanel(alphabetPanel);
    }

    public AlphabetPanel getAlphabetPanel()
    {
        return alphabetPanel;
    }

    public void setAlphabetPanel(AlphabetPanel alphabetPanel)
    {
        this.alphabetPanel = alphabetPanel;
    }
} // end class
