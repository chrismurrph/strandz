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
import org.strandz.lgpl.util.TimeUtils;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.EventObject;

/**
 * Supports date editing
 */
public class MyJTable extends JTable
{
    // DateFormat localFormatter;
    private int times = 0;

    public MyJTable()
    {
        super();
        // localFormatter = DateFormat.getDateInstance();

        addKeyListener(
            new KeyAdapter()
            {
                public void keyPressed(KeyEvent e)
                {
                    int keyCode = e.getKeyCode();
                    if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT
                        || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN)
                    {
                        // cancel the event
                        e.consume();
                    }
                }
            });
    }

    /**
     * Rectify the fact that a JTable does not
     * support editing of Dates.
     */
    protected void createDefaultEditors()
    {
        super.createDefaultEditors();
        if(defaultEditorsByColumnClass.get(Date.class) != null)
        {
            Err.error("JTable now supports Date editing");
        }
        else
        {
            // Dates
            JTextField rightAlignedTextField = new JTextField();
            rightAlignedTextField.setHorizontalAlignment(JTextField.RIGHT);
            rightAlignedTextField.setBorder(new LineBorder(Color.black));
            setDefaultEditor(Date.class,
                new MyDateDefaultCellEditor(rightAlignedTextField));
        }
        // TimeSpent
        // JTextField rightAlignedTextField = new JTextField();
        // rightAlignedTextField.setHorizontalAlignment( JTextField.RIGHT);
        // rightAlignedTextField.setBorder( new LineBorder( Color.black));
        setDefaultEditor(TimeSpent.class, new TimeSpentEditor());
    }

    protected void createDefaultRenderers()
    {
        super.createDefaultRenderers();

        /* for 1.2.2 */
        // Dates (Same code but we are using our local formatter)
        DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer()
        {
            public void setValue(Object obj)
            {
                setText((obj == null) ? "" : TimeUtils.DATE_FORMAT.format(obj));
            }
        };
        dateRenderer.setHorizontalAlignment(JTextField.RIGHT);
        setDefaultRenderer(Date.class, dateRenderer);

         /**/
        DefaultTableCellRenderer timeSpentRenderer = new DefaultTableCellRenderer()
        {
            public void setValue(Object obj)
            {
                setText((obj == null) ? "" : TimeSpent.format(obj).toString());
            }
        };
        dateRenderer.setHorizontalAlignment(JTextField.RIGHT);
        setDefaultRenderer(TimeSpent.class, timeSpentRenderer);
    }

    /**
     * Written to give us a special delegate
     * that will format dates.
     */
    class MyDateDefaultCellEditor extends DefaultCellEditor
    {
        /**
         * Same as super apart from new delegate
         */
        public MyDateDefaultCellEditor(JTextField tf)
        {
            super(tf);
            // super created a delegate that we don't want to be a listener
            tf.removeActionListener(delegate);
            // because we are are going to overwrite said delegate
            delegate = new DateEditorDelegate();
            // and make our new delegate the action listener
            tf.addActionListener(delegate);
        }

        /**
         * Required to get round compiler not allowing editorComponent
         * to be seen by classes other than subclasses - and
         * DateEditorDelegate is not a subclass.
         */
        private JTextField getEditorComponent()
        {
            return (JTextField) editorComponent;
        }

        /**
         * This delegate is the same as the one used for the JTextField
         * delegate, except where commented out and replaced.
         */
        class DateEditorDelegate extends EditorDelegate
        {
            public void setValue(Object obj)
            {
                // super.setValue( obj); can't do (by compiler) and don't need
                /*
                if (obj != null)
                getEditorComponent().setText( obj.toString());
                else
                getEditorComponent().setText("");
                */
                // above code replaced by this line
                getEditorComponent().setText((obj == null) ? "" : // otherwise
                    TimeUtils.DATE_FORMAT.format(obj));
            }

            public Object getCellEditorValue()
            {
                return getEditorComponent().getText();
            }

            public boolean startCellEditing(EventObject anEvent)
            {
                if(anEvent == null)
                {
                    getEditorComponent().requestFocusInWindow();
                }
                return true;
            }

            public boolean stopCellEditing()
            {
                return true;
            }
        }
    }
    /*
    * See TimeSpentEditor for a more current way of editing
    *
    class MyTimeSpentDefaultCellEditor extends DefaultCellEditor
    {
    public MyTimeSpentDefaultCellEditor(JTextField tf)
    {
    super( tf);
    //super created a delegate that we don't want to be a listener
    tf.removeActionListener( delegate);
    //because we are are going to overwrite said delegate
    delegate = new TimeSpentEditorDelegate();
    //and make our new delegate the action listener
    tf.addActionListener( delegate);
    }

    **
    * Required to get round compiler not allowing editorComponent
    * to be seen by classes other than subclasses - and
    * DateEditorDelegate is not a subclass.
    *
    private JTextField getEditorComponent()
    {
    return (JTextField)editorComponent;
    }

    **
    * This delegate is the same as the one used for the JTextField
    * delegate, except where commented out and replaced.
    *
    class TimeSpentEditorDelegate
    extends EditorDelegate
    {
    public void setValue( Object obj)
    {
    getEditorComponent().setText((obj == null)
    ? "" : //otherwise
    TimeSpent.format( obj));
    }
    public Object getCellEditorValue()
    {
    return getEditorComponent().getText();
    }
    public boolean startCellEditing(EventObject anEvent)
    {
    if(anEvent == null)
    getEditorComponent().requestFocusInWindow();
    return true;
    }
    public boolean stopCellEditing()
    {
    return true;
    }
    }
    }
    */

    /*
    public void repaint()
    {
    Err.pr( "Repaint for MyJTable <" +
    ++times + ">");
    }
    */
}
