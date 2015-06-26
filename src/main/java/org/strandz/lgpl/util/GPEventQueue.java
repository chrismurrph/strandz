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
package org.strandz.lgpl.util;

import javax.swing.JComponent;
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
import java.awt.event.MouseEvent;

/**
 * Currently used by AlteredGlassPaneDemo. Once test AlteredGlassPaneDemo using
 * the more recent GlobalEventQueue, then delete this file!
 * <p/>
 * This EventQueue is created when a program starts. At creation time it
 * becomes the default queue.
 */
public class GPEventQueue extends EventQueue
{
    private EventQueue systemEQ;
    private boolean isGlassPane = false;
    private Container contentPane;
    private JMenuBar menuBar;
    private JComponent glassPane;
    private JFrame frame;

    public GPEventQueue()
    {
        Toolkit t = Toolkit.getDefaultToolkit();
        systemEQ = t.getSystemEventQueue();
        systemEQ.push(this);
    }

    protected void dispatchEvent(AWTEvent e)
    {
        if(!isGlassPane)
        {
            super.dispatchEvent(e);
            Err.pr("DISPATCHED: " + e);
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

            Object gp = frame.getGlassPane();
            // System.out.println( gp.getClass().getName());
            this.glassPane = (JComponent) frame.getGlassPane();
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
     * adding a layer to JRootPane's JlayeredPane, if indeed can do this sort of
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
