package org.strandz.lgpl.widgets;

import javax.swing.*;
import java.awt.*;

/**
 * User: Chris
 * Date: 27/02/2009
 * Time: 3:23:42 PM
 */
public class MenuControls
{
    private static final Color IVORY = new Color( 247, 247, 247);

    public static class Menu extends JMenu
    {
        public Menu(String s)
        {
            super(s);
            setOpaque( true);
            setBackground( IVORY);
        }
    }

    public static class MenuItem extends JMenuItem
    {
        private Menu parentMenu;

        public MenuItem(String s)
        {
            super(s);
            setOpaque( true);
            setBackground( IVORY);
        }
        public MenuItem(String s, Menu parentMenu)
        {
            this( s);
            this.parentMenu = parentMenu;
        }
        public Menu getParentMenu()
        {
            return parentMenu;
        }
    }

    public static class PopupMenu extends JPopupMenu
    {
        public PopupMenu()
        {
            setOpaque( true);
            setBackground( IVORY);
        }
    }
}
