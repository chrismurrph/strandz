/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.Err;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Insets;

public class DateSelectControl extends JPanel
{
    private JTextField tfDate;
    private JButton bSelectDate;
    static final int BUTTON_WIDTH = 23;
    static final String ICON_FILE = "images/Calendar.gif";
    static final String ICON_DESC = "Calendar";

    public DateSelectControl()
    {
    }

    public void init()
    {
        tfDate = new JTextField();
        bSelectDate = new JButton();

        double size[][] =
            { // Columns
                {ModernTableLayout.FILL, BUTTON_WIDTH},
                // Rows
                {ModernTableLayout.FILL}
            };
        setLayout(new ModernTableLayout(size));
        // Should not be given a name, but rather name s/be set from
        // outside
        // tfDate.setName( "tfDate");

        add(tfDate, "0, 0");
        add(bSelectDate, "1, 0");
        setName("DateSelectControl");
        // Date_selector selector = new Date_selector_panel();
        /*
        bSelectDate.addActionListener( new ActionListener()
        {
        public void actionPerformed( ActionEvent e)
        {
        Print.pr( "Will not see this when viewing XMLDecoded version");
        }
        }
        );
        */
        bSelectDate.setMargin(new Insets(0, 0, 0, 0));

        Icon icon = PortableImageIcon.createImageIcon(ICON_FILE, ICON_DESC);
        if(icon == null)
        {
            // Problem will not be this, but the fact that the name
            // of the icon is not getting into the XML file. Fixed!
            Err.alarm("Could not load " + ICON_FILE);
        }
        else
        {
            bSelectDate.setIcon(icon);
            bSelectDate.setName("bSelectDate");
            setBSelectDate(bSelectDate);

            tfDate.setName("tfDate");
            setTfDate(tfDate);
        }
    }

    public JButton getBSelectDate()
    {
        return bSelectDate;
    }

    public void setBSelectDate(JButton bSelectDate)
    {
        this.bSelectDate = bSelectDate;
    }

    public JTextField getTfDate()
    {
        return tfDate;
    }

    public void setTfDate(JTextField tfDate)
    {
        this.tfDate = tfDate;
    }
}
