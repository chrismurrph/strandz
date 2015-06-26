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
package org.strandz.lgpl.widgets.data;

import org.strandz.lgpl.data.objects.TimeSpent;
import org.strandz.lgpl.util.Err;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.InternationalFormatter;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;

/**
 * Implements a cell editor that uses a formatted text field
 * to edit Integer values.
 */
public class TimeSpentEditor extends DefaultCellEditor
{
    JFormattedTextField ftf;
    TimeSpentFormat timeSpentFormat;
    private boolean DEBUG = true;

    public TimeSpentEditor()
    {
        super(new JFormattedTextField(new TimeSpentFormat()));
        ftf = (JFormattedTextField) getComponent();
        // Err.pr( "Formatter type: " + ftf.getFormatter().getClass().getName());
        // The default
        ((InternationalFormatter) ftf.getFormatter()).setAllowsInvalid(true);
        ((InternationalFormatter) ftf.getFormatter()).setCommitsOnValidEdit(false);
        // ftf.setHorizontalAlignment(JTextField.TRAILING);
        ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);
        // React when the user presses Enter while the editor is
        // active.  (Tab is handled as specified by
        // JFormattedTextField's focusLostBehavior property.)
        ftf.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            "check");
        ftf.getActionMap().put("check", new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                Err.pr("actionPerformed()");
                if(!ftf.isEditValid())
                { // The text is invalid.
                    if(userSaysRevert())
                    { // reverted
                        ftf.postActionEvent(); // inform the editor
                    }
                }
                else
                {
                    try
                    {
                        // The text is valid,
                        ftf.commitEdit(); // so use it.
                        ftf.postActionEvent(); // stop editing
                    }
                    catch(java.text.ParseException exc)
                    {
                    }
                }
            }
        });
    }

    // Override to invoke setValue on the formatted text field.
    public Component getTableCellEditorComponent(
        JTable table,
        Object value, boolean isSelected,
        int row, int column)
    {
        JFormattedTextField ftf = (JFormattedTextField) super.getTableCellEditorComponent(
            table, value, isSelected, row, column);
        ftf.setValue(value);
        return ftf;
    }

    // Override to ensure that the value remains a TimeSpent.
    public Object getCellEditorValue()
    {
        Err.pr("getCellEditorValue()");

        JFormattedTextField ftf = (JFormattedTextField) getComponent();
        Object o = ftf.getValue();
        if(o instanceof TimeSpent)
        {
            return o;
        }
        else
        {
            if(DEBUG)
            {
                Err.pr(
                    "getCellEditorValue: o isn't a TimeSpent, but: "
                        + o.getClass().getName());
            }
            try
            {
                return TimeSpent.parse(o.toString());
            }
            catch(ParseException exc)
            {
                Err.error("getCellEditorValue: can't parse o: " + o);
                return null;
            }
        }
    }

    // Override to check whether the edit is valid,
    // setting the value if it is and complaining if
    // it isn't.  If it's OK for the editor to go
    // away, we need to invoke the superclass's version
    // of this method so that everything gets cleaned up.
    public boolean stopCellEditing()
    {
        Err.pr("stopCellEditing()");

        JFormattedTextField ftf = (JFormattedTextField) getComponent();
        if(ftf.isEditValid())
        {
            try
            {
                ftf.commitEdit();
            }
            catch(java.text.ParseException exc)
            {
            }
        }
        else
        { // text is invalid
            if(!userSaysRevert())
            { // user wants to edit
                return false; // don't let the editor go away
            }
        }
        return super.stopCellEditing();
    }

    /**
     * Lets the user know that the text they entered is
     * bad. Returns true if the user elects to revert to
     * the last good value. Otherwise, returns false,
     * indicating that the user wants to continue editing.
     */
    protected boolean userSaysRevert()
    {
        Err.stack();
        Toolkit.getDefaultToolkit().beep();
        ftf.selectAll();

        Object[] options = {"Edit", "Revert"};
        int answer = JOptionPane.showOptionDialog(
            SwingUtilities.getWindowAncestor(ftf),
            "The value must be parseable as a TimeSpent. "
                + "You can either continue editing "
                + "or revert to the last valid value.",
            "Invalid Text Entered",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            options,
            options[1]);
        if(answer == 1)
        { // Revert!
            ftf.setValue(ftf.getValue());
            return true;
        }
        return false;
    }
}
