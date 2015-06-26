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
import org.strandz.lgpl.util.BeansUtils;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A control that can have its buttons (or whatever) dynamically added and
 * taken away. Has a concept of predefined order. Can be vertical or
 * horizontal in orientation.
 * <p/>
 * If don't use pack() then there will be a gap at the bottom as add buttons.
 * Not allowing resize as 'gap creep' still occured.
 * As pack depends on preferred size, we have forced preferred size whenever
 * a button is added.
 * <p/>
 * 1./ Allow set size of button when construct, null constructor using
 * defaults.
 * 2./ Have properties to set the button's size at any time. As well as
 * setting button width and height will have to setPreferredSize
 * for all of them and then call validate() to force to be repainted.
 * 3./ Next enhancement is to be able to receive javax.swing.Action
 * interfaces. For each Action that add will listen to state changes so
 * will know to disable the corresponding event and its display.
 * 4./ Allow resizing.
 * 5./ Rewrite parts to get rid of 'pack' - or get pack to work as resize.
 * 6./ Support for horizontal as well as vertical. DONE
 */
public class Manipulator extends JInternalFrame implements MouseMotionListener
{
    static final int BAR_HEIGHT = 25; // so if don't add any buttons will still
    // see top portion
    static final int DEF_WIDTH = 120;
    static final int DEF_BUTTON_HEIGHT = 30;
    int height = BAR_HEIGHT;
    int width = DEF_WIDTH;
    int buttonWidth = DEF_WIDTH;
    int buttonHeight = DEF_BUTTON_HEIGHT;
    boolean initialised = false;
    ArrayList orderedList = new ArrayList();
    ArrayList currentList = new ArrayList();
    int lastPlace = 0; // initially no buttons
    JPanel panel = new JPanel();
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;
    private int orientation = HORIZONTAL;
    /**
     * GridLayout actually irrelevant, as we manually setRows() or setColums().
     */
    private GridLayout layout = new GridLayout(0, 1); // one column, can setRows() dynamically

    /**
     * Call to setBounds here gives a height of 0, so Manipulator is
     * not made visible at this point.
     */
    public Manipulator()
    {
        if(BeansUtils.isDesignTime())
        {
            return;
        }

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        panel.setLayout(layout);
        contentPane.add(panel);
        /*
        * SWING BUG FORCED - but this not super important anyway
        *super.setVisible( false);
        */
        setBounds(200, 10, buttonWidth, BAR_HEIGHT);
        setResizable(false);
    }

    public void setOrientation(int orientation)
    {
        if(!currentList.isEmpty())
        {
            Err.error("Can only change orientation when no buttons exist");
        }
        if(this.orientation != orientation)
        {
            this.orientation = orientation;
        }
    }

    /**
     * Visibility can be set true AFTER have added all the buttons. At least two bugs
     * which stop this happening in Swing 1.0.1
     */
    public void setVisible(boolean b)
    {
        if(b)
        {
            initialised = true;
        }
        if(!BeansUtils.isDesignTime())
        {
            // new MessageDlg("jInternalFrame to be set visible: " + b);
            /* nearly taken out for new Swing*/ pack();
            super.setVisible(b);
        }
    }

    private int putItemAt(ArrayList list, int place, JComponent button, boolean replace)
    {
        int result = 0; // result only relevant where replace == false
        if(place == 0)
        {
            Err.error("For putItemAt, first element s/be 1, not 0");
        }
        if(place > list.size())
        {
            if(!((list.size() + 1) == place))
            {
                Err.error("When putItemAt(), you must be consecutive, starting from 1");
            }
            // Err.pr("Adding ele just after " + list.size());
            list.add(button);
        }
        else
        {
            if(replace)
            {
                // Err.pr("Replacing ele at " + place);
                list.set(place - 1, button); // will dicard previous
            }
            else
            {
                // Err.pr("Inserting ele at " + place);
                list.add(place - 1, button);
                result = place;
            }
        }
        return result;
    }

    /**
     * Sets the physical placement of a Control. Allows the control to have
     * a predefined order in relation to other controls. addButton() respects
     * this predefined order.
     *
     * @param order  index of control (starts at 1)
     * @param button the control that may later add (addButton)
     */
    public void setOrder(int order, JComponent button)
    {
        putItemAt(orderedList, order, button, true);
    }

    JComponent getOrder(int order)
    {
        Err.error("getOrder() not yet implemented");
        // compiler
        return null;
    }

    /**
     * Find out where the JComponent is on the orderedList
     */
    private int getPreordainedPlacing(JComponent newButton)
    {
        int i = 0;
        for(Iterator e = orderedList.iterator(); e.hasNext(); i++)
        {
            if(e.next() == newButton)
            {
                return (i + 1);
            }
        }
        /**
         * Means that this JComponent is on the current list, yet does not have a pre-defined
         * place in the order. Returning 0 means this JComponent will not influence where
         * a new JComponent will be placed.
         */
        // Err.error("JComponent " + ((Button)newButton).getLabel() + " passed to getPreordainedPlacing() does not exist on orderedList");
        return 0;
    }

    /**
     * Given a newButton that want to add, find out where to
     * add it. If it hasn't got a pre-ordained placing, then
     * answer is simply lastPlace + 1.
     * If has pre-ordained placing then find, from the current
     * visible buttons, the button whose pre-ordained placing
     * is <= newButton will be the button we want to add in
     * after. Thus return this button's actual placing + 1
     */
    private int getActualPlacing(JComponent newButton)
    {
        int newButtonOrder = 0; // pre-ordained placing of new Button
        int i = 1;
        for(Iterator e = orderedList.iterator(); e.hasNext(); i++)
        {
            if(e.next() == newButton)
            {
                newButtonOrder = i;
                break;
            }
        }
        // if newButtonOrder is still 0, then newButton does not have a
        // preordained place
        if(newButtonOrder == 0)
        {
            // Err.pr("New button is not pre-ordained");
            return lastPlace + 1;
        }

        JComponent goAfterButton;
        i = 1;
        for(Iterator e = currentList.iterator(); e.hasNext(); i++)
        {
            int orderValue = getPreordainedPlacing((JComponent) e.next());
            if(orderValue > newButtonOrder)
            {
                break;
            }
        }
        // -1 'cause we don't want the element we got to, but the one b4 it
        // +1 'cause we are returning where the button should go
        return i - 1 + 1;
    }

    public JComponent add(JComponent newButton)
    {
        int currentPos = currentList.indexOf(newButton);
        if(currentPos != -1)
        {
            return newButton; // can't add a button that'a already there
        }

        int actualPlacing = getActualPlacing(newButton);
        // Err.pr("Where should put the new button is " + actualPlacing);
        putItemAt(currentList, actualPlacing, newButton, false);

        /**
         * Will need to make our controller bigger by the size of a button
         */

        Dimension dim = new Dimension();
        dim.width = buttonWidth;
        dim.height = buttonHeight;
        newButton.setPreferredSize(dim);
        if(!BeansUtils.isDesignTime())
        {
            if(orientation == VERTICAL)
            {
                int heightChange = buttonHeight;
                height += heightChange;
                layout.setRows(++lastPlace);
                setSize(buttonWidth, height + getInsets().top);
            }
            else
            {
                int widthChange = buttonWidth;
                width += widthChange;
                layout.setColumns(++lastPlace);
                setSize(width, height + getInsets().top);
            }
            panel.add(newButton, actualPlacing - 1);
            if(initialised)
            {
                // Err.pr("validate during ADD");
                pack();
                validate();
            }
            else
            {// Err.pr("no validate during ADD");
            }
        }
        return newButton;
    }

    public void remove(JComponent oldButton)
    {
        int currentPos = currentList.indexOf(oldButton);
        if(currentPos == -1)
        {
            return;
        }
        currentList.remove(currentPos);
        if(!BeansUtils.isDesignTime())
        {
            if(orientation == VERTICAL)
            {
                int heightChange = buttonHeight;
                height -= heightChange;
                layout.setRows(--lastPlace);
                setSize(buttonWidth, height + getInsets().top);
            }
            else
            {
                int widthChange = buttonWidth;
                width -= widthChange;
                layout.setColumns(--lastPlace);
                setSize(width, height + getInsets().top);
            }
            panel.remove(oldButton);
            if(initialised)
            {
                pack();
                validate();
            }
        }
    }

    public void remove(Action a)
    {
        Err.error("Not yet implemented remove");
    }

    /*
    * Add a new button which dispatches the action
    * @param a the action for the button to be added
    */
    public JButton add(Action a)
    {
        // SWING_VER_CHANGE
        JButton b = new JButton((String) a.getValue(Action.NAME));
        // JButton b = new JButton( (String)a.getText( Action.NAME));
        b.addActionListener(a);
        this.add(b);
        a.addPropertyChangeListener(new ActionChangedListener(b));
        return b;
    }

    private static class ActionChangedListener implements PropertyChangeListener
    {
        JButton b;

        ActionChangedListener(JButton b)
        {
            super();
            this.b = b;
        }

        /**
         * Copied from JMenu, so copy again later
         */
        public void propertyChange(PropertyChangeEvent e)
        {
            String propertyName = e.getPropertyName();
            if(propertyName.equals(Action.NAME))
            {
                String text = (String) e.getNewValue();
                b.setText(text);
            }
            else if(propertyName.equals("enabled"))
            {
                Boolean enabledState = (Boolean) e.getNewValue();
                b.setEnabled(enabledState.booleanValue());
            }
        }
    }

    public void setButtonHeight(int height)
    {
        buttonHeight = height;
    }

    public int getButtonHeight()
    {
        return buttonHeight;
    }

    public void setButtonWidth(int width)
    {
        // new MessageDlg("Setting button width to " + width);
        buttonWidth = width;
    }

    public int getButtonWidth()
    {
        return buttonWidth;
    }

    public JInternalFrame getFrame()
    {
        return this;
    }

    public void mouseDragged(MouseEvent e)
    {
        Err.error("mouse been dragged");
    }

    public void mouseMoved(MouseEvent e)
    {
        Err.error("mouse been moved");
    }
    /*
    These worked - but not right event - need to look at some sort of resizing
    event on a frame, and only catch mouse released - might be a drag event.
    public void mouseDragged( MouseEvent e)
    {
    Err.error("mouse been ");
    }

    public void mouseMoved( MouseEvent e)
    {
    Err.error("mouse been ");
    }
    */

    /*
    public void setBounds(int x,
    int y,
    int w,
    int h)
    {
    Err.pr("Set Bounds called");
    // - nasty effect: pack();
    super.setBounds( x, y, w, h);
    }
    */
}
/*
 at manipulator.Manipulator.setVisible(Manipulator.java:119)
 at javax.swing.JInternalFrame.show(JInternalFrame.java:1064)
 at java.awt.Component.show(Component.java:498)
 at java.awt.Component.setVisible(Component.java:460)
 at javax.swing.JComponent.setVisible(JComponent.java:1168)
 at javax.swing.JInternalFrame.setVisible(JInternalFrame.java:458)

 at manipulator.Manipulator.setVisible(Manipulator.java:119)
 */
