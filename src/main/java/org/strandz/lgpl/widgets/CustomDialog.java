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

import org.strandz.lgpl.util.DialogEmbellisherI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * This class can be used for moderately complicated Dialog
 * interactions. DialogEmbellisherI is used for validation
 * of entered text and to receive 'which button was pressed'.
 * <p/>
 * To improve put anything variable in DialogEmbellisherI and
 * the variable part of the constructor in a new method called
 * displayDialog().
 *
 * @author Chris Murphy
 * @see org.strandz.lgpl.util.DialogEmbellisherI
 */
public class CustomDialog extends JDialog
{
    private String typedText = null;
    private JOptionPane optionPane;
    private CustomDialog outer;
    
    private Frame aFrame;
    private DialogEmbellisherI dialogEmbellisherI;
    private String title;
    private String line1;
    private String line2;
    private int textFieldLength;
    private String extraActionNames[];
    private String invalidResponseText;
    private String initValue;
    
    private boolean useOkButton = true;
    private boolean useCancelButton = true;
    private boolean useTextField = true;
    
    private boolean loaded = false;
    
    public static final String OK = "OK";
    public static final String CANCEL_BUTTON = "Cancel";
    public static final String CANCEL_WINDOW = "-1";
    
    public static void main(String[] args)
    {
        //String result = "C:\\sdz-zone\\data\\needs\\BackupNeedsData.xml";
        DialogEmbellisherTrialImpl dialogEmbellisherTrialImpl = new DialogEmbellisherTrialImpl();
        MessageDlg.setFrame( new JFrame());
        CustomDialog dialog = new CustomDialog
            (MessageDlg.getFrame(), dialogEmbellisherTrialImpl, "Attribute Matching",
                "Do you want to match blah with blah?", null, 40,
                new String[]{"Yes", "No"}, null, null);
        dialog.setUseOkButton( false);
        dialog.setUseCancelButton( false);
        dialog.setUseTextField( false);
        dialog.init();
        dialog.pack();
        dialog.setLocationRelativeTo(MessageDlg.getFrame());
        dialog.setVisible( true);
    }

    public void setUseOkButton(boolean useOkButton)
    {
        this.useOkButton = useOkButton;
    }

    public void setUseCancelButton(boolean useCancelButton)
    {
        this.useCancelButton = useCancelButton;
    }


    public void setUseTextField(boolean useTextField)
    {
        this.useTextField = useTextField;
    }

    private static class DialogEmbellisherTrialImpl implements DialogEmbellisherI
    {
        public boolean validate(String txt)
        {
            return true;
        }

        public void actionPerformed(ActionEvent e)
        {
            String pressed = e.getActionCommand();
            Err.pr(pressed);
            if(pressed == "Browse")
            {
                Err.error("Not yet implemented Browse");
            }
            System.exit( 0);
        }
    }

    public CustomDialog
        (
            Frame aFrame,
            DialogEmbellisherI dialogEmbellisherI,
            String title,
            String line1,
            String line2,
            int textFieldLength,
            String extraActionNames[],
            String invalidResponseText,
            String initValue
        )
    {
        super(aFrame, true);
        this.aFrame = aFrame;
        this.dialogEmbellisherI = dialogEmbellisherI;
        this.title = title;
        this.line1 = line1;
        this.line2 = line2;
        this.textFieldLength = textFieldLength;
        this.extraActionNames = extraActionNames;
        this.invalidResponseText = invalidResponseText;
        this.initValue = initValue;        
        //lazyLoad();
    }
    
    public void init()
    {
        lazyLoad();
    }
    
    private void lazyLoad()
    {
        if(!loaded)
        {
            if(extraActionNames != null && extraActionNames.length > 0)
            {
                if(dialogEmbellisherI == null)
                {
                    Err.error("Extra buttons will require an ActionListener");
                }
            }
            outer = this;
            setTitle(title);
    
            final DialogEmbellisherI validator = dialogEmbellisherI;
            final String msgString1 = line1;
            final String msgString2 = line2;
            final JTextField textField = new JTextField(textFieldLength);
            final String invalidResponse = invalidResponseText;
            Object[] array;
            if(useTextField)
            {
                array = new Object[3];
                array[0] = msgString1;
                array[1] = msgString2;
                array[2] = textField;
            }
            else
            {
                array = new Object[2];
                array[0] = msgString1;
                array[1] = msgString2;
            }
            final String btnString1 = OK;
            final String btnString2 = CANCEL_BUTTON;
            final List extraActions = Arrays.asList(extraActionNames);
            List options = new ArrayList();
            options.addAll(extraActions);
            if(useOkButton)
            {
                options.add(btnString1);
            }
            if(useCancelButton)
            {
                options.add(btnString2);
            }
            optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, options.toArray(), options.get(0));
            // optionPane.setInitialSelectionValue( initValue);
            typedText = initValue;
            textField.setText( typedText);
            setContentPane( optionPane);
            setDefaultCloseOperation( DO_NOTHING_ON_CLOSE);
            addWindowListener( new WindowAdapter()
            {
                public void windowClosing(WindowEvent we)
                {
                    /*
                    * Instead of directly closing the window,
                    * we're going to change the JOptionPane's
                    * value property.
                    */
                    optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
                }
            });
            textField.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    optionPane.setValue(btnString1);
                }
            });
            optionPane.addPropertyChangeListener(
                new PropertyChangeListener()
                {
                    public void propertyChange(PropertyChangeEvent e)
                    {
                        String prop = e.getPropertyName();
                        if(isVisible() && (e.getSource() == optionPane)
                            && (prop.equals(JOptionPane.VALUE_PROPERTY)
                            || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
                        {
                            Object value = optionPane.getValue();
                            if(value == JOptionPane.UNINITIALIZED_VALUE)
                            {
                                // ignore reset
                                return;
                            }
                            // Reset the JOptionPane's value.
                            // If you don't do this, then if the user
                            // presses the same button next time, no
                            // property change event will be fired.
                            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
    
                            ActionEvent evt = new ActionEvent(outer, ActionEvent.ACTION_PERFORMED,
                                value.toString());
                            validator.actionPerformed(evt);
                            if(value.equals(btnString1))
                            {
                                typedText = textField.getText();
    
                                String ucText = typedText.toUpperCase();
                                if(validator == null || validator.validate(ucText))
                                {
                                    // we're done; dismiss the dialog
                                    setVisible(false);
                                }
                                else
                                {
                                    // text was invalid
                                    textField.selectAll();
                                    JOptionPane.showMessageDialog(CustomDialog.this, invalidResponse,
                                        "Try again", JOptionPane.ERROR_MESSAGE);
                                    typedText = null;
                                }
                            }
                            else if(extraActions.contains(value))
                            {// nufin, will be processed by validator
                            }
                            else
                            { // user closed dialog or clicked cancel
                                typedText = null;
                                setVisible(false);
                            }
                        }
                    }
                });
            loaded = true;
        }
    }
    
    public void setVisible(boolean b) 
    {
        if(b)
        {
            lazyLoad();
        }
        super.setVisible( b);
    }

    public String getValidatedText()
    {
        return typedText;
    }
}
