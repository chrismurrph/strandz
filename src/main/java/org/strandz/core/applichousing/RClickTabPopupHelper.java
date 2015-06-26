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
package org.strandz.core.applichousing;

import org.strandz.lgpl.util.Err;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Functionality for R-click remove of tabs that will un-select
 * and un-display them. (Just like IntelliJ and browsers).
 * <p/>
 * With 1.5 this is simpler, but we want Strandz to
 * compile for those who haven't upgraded their
 * environment as well.
 * <p/>
 * TODO Need to make it so that a tab switch doesn't occur at the same time, see:
 * http://www.talkaboutprogramming.com/group/comp.lang.java.programmer/messages/675871.html
 * where I asked the question and others have done it.
 * Hmm - they've deleted it!
 */
public class RClickTabPopupHelper
{
    private JPopupMenu popupMenu;
    private MouseListener popupListener;
    private RightClickListener rightClickListener;

    public static String CLOSE_ALL_BUT_SELECTED = "Close All But This";
    static String CLOSE_ALL = "Close All";
    static String CLOSE = "Close";
    private static boolean debugging = false;

    /**
     * Callback for what actually happens
     */
    static interface RightClickListener
    {
        void removeDisplayedStrands(String cmd);
    }

    RClickTabPopupHelper(JTabbedPane tabbedPane, RightClickListener rightClickListener)
    {
        popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(CLOSE);
        MenuClickListener l = new MenuClickListener();
        menuItem.addActionListener(l);
        popupMenu.add(menuItem);
        menuItem = new JMenuItem(CLOSE_ALL);
        menuItem.addActionListener( l);
        popupMenu.add( menuItem);
        menuItem = new JMenuItem(CLOSE_ALL_BUT_SELECTED);
        menuItem.addActionListener( l);
        popupMenu.add( menuItem);
        popupListener = new PopupListener(popupMenu);
        addRClickListener(tabbedPane);
        this.rightClickListener = rightClickListener;
    }

    private void addRClickListener(Component comp)
    {
        comp.addMouseListener(popupListener);
    }

    private static void pr(String msg)
    {
        if(debugging)
        {
            Err.pr(msg);
        }
    }

    private class PopupListener extends MouseAdapter
    {
        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu)
        {
            popup = popupMenu;
        }

        public void mousePressed(MouseEvent e)
        {
            if(e.isPopupTrigger())
            {
                Err.error("Not normally \"isPopupTrigger()\" on mousePressed (you may be on an exotic machine)");
            }
            maybeShowPopup(e); //won't do anything
        }

        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            if(e.isPopupTrigger())
            {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private class MenuClickListener implements ActionListener
    {
        private MenuClickListener()
        {
        }

        public void actionPerformed(ActionEvent e)
        {
            //Err.pr( "Done a menu click: " + e);
            rightClickListener.removeDisplayedStrands(e.getActionCommand());
        }
    }
}
