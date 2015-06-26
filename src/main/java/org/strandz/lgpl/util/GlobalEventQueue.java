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
/**
 * MAY NEED TO PUT SUN LICENSING LICENCING STUFF UP HERE?
 */
package org.strandz.lgpl.util;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

/**
 * This EventQueue is created when a program starts. At creation time it
 * becomes the default queue.
 * <p/>
 * It is useful for debugging events, such as
 * mouse events, focus events, anything.
 * <p/>
 * Also useful for dealing with events really early, such as when you want
 * to capture mouse events and draw on the glass pane. See
 * AlteredGlassPaneDemo
 * <p/>
 * Lastly another event queue may be extended or somehow attached here. That
 * is a listener (from a JavaWorld article) that automatically brings up the
 * hourglass whenever an event is taking a bit long.
 *
 * @author Chris Murphy
 *         (actually original start off code came from the Swing tutorial)
 */
public class GlobalEventQueue extends EventQueue
{
    private EventQueue systemEQ;
    private boolean isGlassPane = false;
    private Container contentPane;
    private JMenuBar menuBar;
    private JPanel glassPane;
    private JFrame frame;
    public static boolean debug = false;
    private static int debugEvents[] = new int[4];
    private static String debugStr[] = new String[4];

    public GlobalEventQueue()
    {
        debugEvents[0] = FocusEvent.FOCUS_GAINED;
        debugEvents[1] = FocusEvent.FOCUS_LOST;
        debugEvents[2] = FocusEvent.FOCUS_FIRST;
        debugEvents[3] = FocusEvent.FOCUS_LAST;
        debugStr[0] = "FOCUS_GAINED";
        debugStr[1] = "FOCUS_LOST";
        debugStr[2] = "FOCUS_FIRST";
        debugStr[3] = "FOCUS_LAST";

        Toolkit t = Toolkit.getDefaultToolkit();
        systemEQ = t.getSystemEventQueue();
        systemEQ.push(this);
    }

    public void postEvent(AWTEvent e)
    {
        debug(e, "POST");
        super.postEvent(e);
    }

    private void debug(AWTEvent e, String txt)
    {
        if(debug)
        {
            for(int i = 0; i < debugEvents.length; i++)
            {
                if(e.getID() == debugEvents[i])
                {
                    Print.pr(
                        txt + " ============:" + debugStr[i] + " "
                            + ((Component) e.getSource()).getName() + " "
                            + ((Component) e.getSource()).getClass().getName() + " "
                            + e.paramString());
                }
            }
        }
    }

    protected void dispatchEvent(AWTEvent e)
    {
        debug(e, "DISPATCH");
        if(!isGlassPane)
        {
            super.dispatchEvent(e);
        }
        else
        {
            boolean isMouse = false;
            int id = e.getID();
            switch(id)
            {
                case MouseEvent.MOUSE_PRESSED:
                case MouseEvent.MOUSE_RELEASED:
                case MouseEvent.MOUSE_MOVED:
                case MouseEvent.MOUSE_DRAGGED:
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                case MouseEvent.MOUSE_WHEEL:
                    isMouse = true;
                    break;

                default:
            }
            if(!isMouse)
            {
                super.dispatchEvent(e);
            }
            else
            {
                redispatchMouseEvent((MouseEvent) e);
            }
        }
    }

    /**
     * Passing in null will 'disable glass pane'
     */
    public void enableGlassPaneFor(JFrame frame)
    {
        if(frame != null)
        {
            this.contentPane = frame.getContentPane();
            this.glassPane = (JPanel) frame.getGlassPane();
            this.menuBar = frame.getJMenuBar();
            this.frame = frame;
            isGlassPane = true;
        }
        else
        {
            isGlassPane = false;
        }
    }

    /**
     * Works better than GlassPaneDemo. Still does not do the colouration of menu
     * items as move mouse up and down a JPopupMenu. May have been better off
     * adding a layer to JRootPane's JLayeredPane, if indeed can do this sort of
     * thing. Best would be a layer below the popup layer.
     */
    private void redispatchMouseEvent(MouseEvent e)
    {
        Point glassPanePoint = e.getPoint();
        glassPanePoint.y -= menuBar.getHeight(); // Tacky! Still don't really know

        // what is going on here, when
        // GlassPane example from tutorial
        // didn't need to do this
        Component component = null;
        Container container = contentPane;
        Point containerPoint = SwingUtilities.convertPoint(glassPane,
            glassPanePoint, contentPane);
        int eventID = e.getID();
        /*
        * If it is above the line then we have a different container and a
        * different container point.
        */
        JPopupMenu popupMenu = null;
        if(containerPoint.y < 0)
        {
            container = menuBar;
            containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint,
                menuBar);
        }
        else
        {
            /*
            * Next will be getting the deepest point on contentPane or menuBar.
            * Doing this we fail to get any drop down menu choices. Thus here we
            * see if a  JPopupMenu is active. If it is then it becomes the
            * container.
            */
            JLayeredPane layered = frame.getLayeredPane();
            Component comps[] = layered.getComponentsInLayer(
                JLayeredPane.POPUP_LAYER.intValue());
            if(comps.length == 1)
            {
                JPanel panel = (JPanel) comps[0];
                popupMenu = (JPopupMenu) panel.getComponent(0);
            }
            else
            {
                if(comps.length != 0)
                {
                    throw new Error("Will never happen");
                }
            }
            if(popupMenu != null)
            {
                container = popupMenu;
                containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint,
                    popupMenu);
            }
        }
        component = SwingUtilities.getDeepestComponentAt(container,
            containerPoint.x, containerPoint.y);
        /*
        * Here have worked out which component the mouse is hovering above or
        * has clicked on, and will thus be able to redirect the mouse event to
        * that component.
        */
        if(component == null)
        {
            return;
        }
        else
        {
            Point componentPoint = SwingUtilities.convertPoint(glassPane,
                glassPanePoint, component);
            MouseEvent me = new MouseEvent(component, eventID, e.getWhen(),
                e.getModifiers(), componentPoint.x, componentPoint.y,
                e.getClickCount(), e.isPopupTrigger());
            component.dispatchEvent(me);
        }
    }
}
