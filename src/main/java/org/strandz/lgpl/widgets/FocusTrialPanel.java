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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FocusTrialPanel extends JPanel
{
    JTextField numberField = new JTextField(10); // Will be validated
    JTextField clickField = new JTextField(10); // To take the focus

    public FocusTrialPanel()
    {
        // Build the layout
        setLayout(new GridLayout(2, 2));
        add(new JLabel("Enter integer multiple of 3: "));
        add(numberField);
        add(new JLabel("Click in this field to move focus: "));
        add(clickField);
        // Add listeners
        numberField.addFocusListener(new ValidatorFocusHandler());
    }

    public class ValidatorFocusHandler implements FocusListener
    {
        public void focusGained(FocusEvent e)
        {
        }

        public void focusLost(FocusEvent e)
        {
            try
            {
                int n = Integer.parseInt(numberField.getText());
                if((n % 3) != 0)
                {
                    handleBadText("Number must be divisible by 3. Please try again");
                }
            }
            catch(NumberFormatException nfe)
            {
                handleBadText("Text must be an integer. Please try again");
            }
        }

        void handleBadText(String s)
        {
            final String errorMessage = s;
            SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()
                    {
                        JOptionPane.showMessageDialog(null, errorMessage, "Error",
                            JOptionPane.ERROR_MESSAGE);
                        // numberField.selectAll();
                        // numberField.requestFocus();
                    }
                });
        }
    }

    public static void main(String[] args)
    {
        try
        {
            String s = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(s);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }

        JFrame frame = new JFrame();
        frame.setLocation(300, 300);

        FocusTrialPanel p = new FocusTrialPanel();
        frame.getContentPane().add(p);
        frame.pack();
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                Window w = e.getWindow();
                w.setVisible(false);
                w.dispose();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }
}
