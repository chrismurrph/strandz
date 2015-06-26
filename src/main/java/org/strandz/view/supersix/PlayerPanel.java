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
package org.strandz.view.supersix;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import mseries.ui.MDateEntryField;
import org.strandz.lgpl.widgets.WidgetUtils;
import org.strandz.lgpl.util.TimeUtils;

import java.awt.Font;

public class PlayerPanel extends JPanel
{
    JLabel lPlayer;
    JLabel lFirstName;
    JTextField tfFirstName;
    JLabel lSurname;
    JTextField tfSurname;
    JLabel lEmail;
    JTextField tfEmail;
    JLabel lContactPhone;
    JTextField tfContactPhone;
    JLabel lAddr1;
    JTextField tfAddr1;
    JLabel lAddr2;
    JTextField tfAddr2;
    JLabel lDateOfBirth;
    MDateEntryField mdefDateOfBirth;
    JLabel lAge;

    static final int MID_GAP = 7;
    static final int GAP = 15;
    static final int BORDER = 20;
    static final int SMALL_SPACING = 2;
    static final int TEXT_HEIGHT = 23;

    private static Font TITLE_FONT = new Font("Dialog", Font.BOLD, 14);

    public PlayerPanel()
    {
    }

    public void init()
    {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        lPlayer = new JLabel();
        WidgetUtils.setLabelProperties( lPlayer, null, TITLE_FONT, null);
        lFirstName = new JLabel();
        tfFirstName = new JTextField();
        lSurname = new JLabel();
        tfSurname = new JTextField();
        lEmail = new JLabel();
        tfEmail = new JTextField();
        lContactPhone = new JLabel();
        tfContactPhone = new JTextField();
        lAddr1 = new JLabel();
        tfAddr1 = new JTextField();
        lAddr2 = new JLabel();
        tfAddr2 = new JTextField();
        lDateOfBirth = new JLabel();
        mdefDateOfBirth = new MDateEntryField();
        mdefDateOfBirth.setNullOnEmpty(true);
        mdefDateOfBirth.setMinimum( TimeUtils.getDate( 1, 1, 1940));
        mdefDateOfBirth.setMaximum( TimeUtils.getDate( 1, 1, 2000));
        lAge = new JLabel();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {
                    PlayerPanel.BORDER, 
                    0.20, 
                    PlayerPanel.GAP, 
                    0.30, 
                    PlayerPanel.MID_GAP, 
                    0.20, 
                    PlayerPanel.GAP, 
                    0.30, 
                    ModernTableLayout.FILL,
                    PlayerPanel.BORDER},
                // Rows
                {
                    ModernTableLayout.FILL,
                        PlayerPanel.TEXT_HEIGHT, PlayerPanel.SMALL_SPACING,
                        PlayerPanel.TEXT_HEIGHT, PlayerPanel.SMALL_SPACING,
                        PlayerPanel.TEXT_HEIGHT, PlayerPanel.SMALL_SPACING,
                        PlayerPanel.TEXT_HEIGHT, PlayerPanel.SMALL_SPACING,
                        PlayerPanel.TEXT_HEIGHT, PlayerPanel.SMALL_SPACING,
                        PlayerPanel.TEXT_HEIGHT, PlayerPanel.SMALL_SPACING,
                    ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
        add(lPlayer, "1, 1");
        add(lFirstName, "1, 3");
        add(tfFirstName, "3, 3");
        add(lSurname, "1, 5");
        add(tfSurname, "3, 5");
        add(lEmail, "1, 7");
        add(tfEmail, "3, 7");
        add(lContactPhone, "1, 9");
        add(tfContactPhone, "3, 9");
        add(lAddr1, "5, 3");
        add(tfAddr1, "7, 3");
        add(lAddr2, "5, 5");
        add(tfAddr2, "7, 5");
        add(lDateOfBirth, "5, 9");
        add(mdefDateOfBirth, "7, 9");
        add(lAge, "7, 11");

        lPlayer.setText("Player");
        lFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
        lFirstName.setText("First Name");
        tfFirstName.setName("tfFirstName");
        lSurname.setHorizontalAlignment(SwingConstants.TRAILING);
        lSurname.setText("Surname");
        tfSurname.setName("tfSurname");
        lEmail.setHorizontalAlignment(SwingConstants.TRAILING);
        lEmail.setText("Email");
        tfEmail.setName("tfEmail");
        lContactPhone.setHorizontalAlignment(SwingConstants.TRAILING);
        lContactPhone.setText("Contact Ph");
        tfContactPhone.setName("tfContactPhone");
        lAddr1.setHorizontalAlignment(SwingConstants.TRAILING);
        lAddr1.setText("Address 1");
        tfAddr1.setName("tfAddr1");
        lAddr2.setHorizontalAlignment(SwingConstants.TRAILING);
        lAddr2.setText("Address 2");
        tfAddr2.setName("tfAddr2");
        lDateOfBirth.setHorizontalAlignment(SwingConstants.TRAILING);
        lDateOfBirth.setText("DOB");
        mdefDateOfBirth.setName("mdefDateOfBirth");
        lAge.setText(""); //Will be set whenever the DOB changes

        setName("PlayerPanel");

        //Anything we need to refer to directly has to go through here
        setMdefDateOfBirth( mdefDateOfBirth);
        setLAge( lAge);
    }

    public MDateEntryField getMdefDateOfBirth()
    {
        return mdefDateOfBirth;
    }

    public void setMdefDateOfBirth(MDateEntryField mdefDateOfBirth)
    {
        this.mdefDateOfBirth = mdefDateOfBirth;
    }

    public JLabel getLAge()
    {
        return lAge;
    }

    public void setLAge(JLabel lAge)
    {
        this.lAge = lAge;
    }
}
