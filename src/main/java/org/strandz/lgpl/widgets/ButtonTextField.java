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

import org.strandz.lgpl.util.Err;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Insets;

public class ButtonTextField extends JPanel
{
    private JButton button;
    private JTextField textField;
    // private ImageIcon openIcon;

    public ButtonTextField()
    {
        button = new JButton();
        // Border empty = BorderFactory.createEmptyBorder();
        // button.setBorder( empty);
        button.setMargin(new Insets(0, 0, 0, 0));
        textField = new JTextField();
        // Dimension dim = new Dimension( 0, 0);
        // setPreferredSize( dim);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(SwingConstants.LEFT);
        // FlowLayout fl = new FlowLayout();
        // fl.setAlignment( FlowLayout.RIGHT);
        // setLayout( fl);
        add(textField);
        add(button);
    }

    public void setName(String name)
    {
        textField.setName("tf" + name + " textField");
        button.setName("b" + name + " button");
    }

    public Dimension getMinimumSize()
    {
        Dimension result = super.getMinimumSize();
        Err.pr("Minimum size is " + result);
        return result;
    }

    public JButton getButton()
    {
        return button;
    }

    public JTextField getTextField()
    {
        return textField;
    }

    public ImageIcon getOpenIcon()
    {
        return (ImageIcon) button.getIcon();
    }

    public void setOpenIcon(Icon openIcon)
    {
        button.setIcon(openIcon);
    }
    /*
    public void setText( String t)
    {
    textField.setText( t);
    }

    public String getText()
    {
    return textField.getText();
    }
    */
}
