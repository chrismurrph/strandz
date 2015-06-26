package org.strandz.lgpl.util;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.*;
/*
Java Swing, 2nd Edition
By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
ISBN: 0-596-00408-7
Publisher: O'Reilly
*/

/*
This directory contains a fixed GlassPane example.

This fixes two bugs:
  1) Key events were not supressed in the original example, they are now
  2) On 1.2 and 1.3 systems, firHtmlLabelst mouse click after removing glass pane
     would not be sent to the component under the mouse.  This was a bug
     in the way JRootPane handled the glass pane component that has been
     fixed in the 1.4 release.  FixedGlassPane.java (see below) provides
     a workaround for 1.2 and 1.3, but is still safe to use with 1.4.

The updated files are:

SwingGlassExample.java       Updated to use (and control) the new glass pane
FixedGlassPane.java    Extension of JPanel that allows for redispatching
                                      erroneous events to their rightful owners
*/

// SwingGlassExample.java
//Show how a glass pane can be used to block mouse (and key!) events.
//Updated in response to discussions with Mark Hansen at Unify.
//
// Based in part on code from the Java Tutorial for glass panes (java.sun.com).
// This version handles both mouse events and focus events.  The focus is
// held on the panel so that key events are also effectively ignored.  (But
// a KeyListener could still be attached by the program activating this pane.)
//

/**
 * User: Chris
 * Date: 16/01/2009
 * Time: 1:08:13 AM
 *
 * Copied from http://www.java2s.com/Code/Java/Swing-JFC/Showhowaglasspanecanbeusedtoblockmouseandkeyevents.htm
 */
public class FixedGlassPane extends JPanel implements MouseListener,
    MouseMotionListener, FocusListener
{
    // helpers for redispatch logic
    Toolkit toolkit;

    JMenuBar menuBar;

    Container contentPane;

    boolean inDrag = false;

    // trigger for redispatching (allows external control)
    boolean needToRedispatch = false;

    public FixedGlassPane( JFrame frame)
    {
        toolkit = Toolkit.getDefaultToolkit();
        menuBar = frame.getJMenuBar();
        contentPane = frame.getContentPane();
        addMouseListener(this);
        addMouseMotionListener(this);
        addFocusListener(this);
        /*
         * Rather than setting opaque what would be ideal would be to take an
         * image copy of the screen and put it on the Glasspane for the duration.
         * Then there wouldn't be any screen flicker as things get done...
         */
        setOpaque(false);
        frame.setGlassPane( this);
    }

    public void setVisible(boolean v)
    {
        // Make sure we grab the focus so that key events don't go astray.
        if (v)
            requestFocus();
        super.setVisible(v);
    }

    // Once we have focus, keep it if we're visible
    public void focusLost(FocusEvent fe)
    {
        if (isVisible())
            requestFocus();
    }

    public void focusGained(FocusEvent fe)
    {
    }

    public void start()
    {
        setNeedToRedispatch(false);
        setVisible(true);
    }

    public void finish()
    {
        setVisible(false);
        // Again, manually control our 1.2/1.3 bug workaround
        setNeedToRedispatch(true);
    }

    // We only need to redispatch if we're not visible, but having full control
    // over this might prove handy.
    private void setNeedToRedispatch(boolean need)
    {
        needToRedispatch = need;
    }

    /*
    * (Based on code from the Java Tutorial) We must forward at least the mouse
    * drags that started with mouse presses over the check box. Otherwise, when
    * the user presses the check box then drags off, the check box isn't
    * disarmed -- it keeps its dark gray background or whatever its L&F uses to
    * indicate that the button is currently being pressed.
    */
    public void mouseDragged(MouseEvent e)
    {
        if (needToRedispatch)
            redispatchMouseEvent(e);
    }

    public void mouseMoved(MouseEvent e)
    {
        if (needToRedispatch)
            redispatchMouseEvent(e);
    }

    public void mouseClicked(MouseEvent e)
    {
        if (needToRedispatch)
            redispatchMouseEvent(e);
    }

    public void mouseEntered(MouseEvent e)
    {
        if (needToRedispatch)
            redispatchMouseEvent(e);
    }

    public void mouseExited(MouseEvent e)
    {
        if (needToRedispatch)
            redispatchMouseEvent(e);
    }

    public void mousePressed(MouseEvent e)
    {
        if (needToRedispatch)
            redispatchMouseEvent(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        if (needToRedispatch)
        {
            redispatchMouseEvent(e);
            inDrag = false;
        }
    }

    private void redispatchMouseEvent(MouseEvent e)
    {
        boolean inButton;
        boolean inMenuBar = false;
        Point glassPanePoint = e.getPoint();
        Container container = contentPane;
        Point containerPoint = SwingUtilities.convertPoint(this,
            glassPanePoint, contentPane);
        int eventID = e.getID();

        if (containerPoint.y < 0)
        {
            inMenuBar = true;
            container = menuBar;
            containerPoint = SwingUtilities.convertPoint(this, glassPanePoint,
                menuBar);
            testForDrag(eventID);
        }

        //XXX: If the event is from a component in a popped-up menu,
        //XXX: then the container should probably be the menu's
        //XXX: JPopupMenu, and containerPoint should be adjusted
        //XXX: accordingly.
        Component component = SwingUtilities.getDeepestComponentAt(container,
            containerPoint.x, containerPoint.y);

        if (component == null)
        {
            return;
        }
        else
        {
            inButton = true;
            testForDrag(eventID);
        }

        if (inMenuBar || inButton || inDrag)
        {
            Point componentPoint = SwingUtilities.convertPoint(this,
                glassPanePoint, component);
            component.dispatchEvent(new MouseEvent(component, eventID, e
                .getWhen(), e.getModifiers(), componentPoint.x,
                componentPoint.y, e.getClickCount(), e.isPopupTrigger()));
        }
    }

    private void testForDrag(int eventID)
    {
        if (eventID == MouseEvent.MOUSE_PRESSED)
        {
            inDrag = true;
        }
    }
}
