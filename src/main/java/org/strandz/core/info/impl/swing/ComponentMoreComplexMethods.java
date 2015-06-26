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
package org.strandz.core.info.impl.swing;

import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.MoveTrackerI;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.core.info.domain.AbstractOwnFieldMethods;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.SelfReferenceUtils;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import java.util.EventListener;

/**
 *
 */
abstract public class ComponentMoreComplexMethods extends AbstractOwnFieldMethods
{
    private static int times;

    public ComponentMoreComplexMethods()
    {
    }

    abstract public void blankComponent(Object fieldComponent);

    abstract public void setComponent(Object fieldComponent, Object obj);

    abstract public void uninstallListeners(Object fieldComponent, Method removeActionListenerMethod);

    abstract public void installClickListener(Object fieldComponent, Object itemAdapter);

    abstract public void installRClickRestore(Object fieldComponent, Object itemAdapter);

    abstract public void uninstallRClickRestore(Object fieldComponent);

    //Imple here to test JComboBox
    //abstract public Color getBGColor( Object fieldComponent);
    //abstract public void setBGColor( Object fieldComponent, Object color);
    public Color getBGColor(Object fieldComponent)
    {
        Color result = null;
        Component comp = (Component) fieldComponent;
        result = comp.getBackground();
        return result;
    }

    public void setBGColor(Object fieldComponent, Object color)
    {
        if(fieldComponent instanceof Component)
        {
            Component comp = (Component) fieldComponent;
            comp.setBackground((Color) color);
        }
        else
        {
            Err.error("getBGColor(), got a " + fieldComponent.getClass().getName());
        }
    }

    public void doRuntimeChanges(Object fieldComponent)
    {
        //nufin
    }

    public Object getMethod(Object fieldComponent)
    {
        Err.error("Either have not specified a get method in *ControlInfoImpl " +
            "or have not overridden this method in a subclass");
        return null;
    }
    
    public void setMethod(Object fieldComponent, Object arg)
    {
        Err.error("Either have not specified a setMethod method in *ControlInfoImpl " +
            "or have not overridden this method in a subclass");
    }

    public void requestFocusMethod(Object fieldComponent)
    {
        Err.error("Either have not specified a requestFocus method in *ControlInfoImpl " +
            "or have not overridden this method in a subclass");
    }

    void removeClickListeners(Component comp)
    {
        MouseListener ls[] = comp.getMouseListeners();
        for(int i = 0; i < ls.length; i++)
        {
            if(ls[i] instanceof ClickListener)
            {
                comp.removeMouseListener(ls[i]);
                Err.pr( SdzNote.FIELD_VALIDATION, "Removed all focus listeners from " + comp);
            }
        }
    }

    ClickListener createClickListener(Component comp, Object itemAdapter)
    {
        ClickListener result = null;
        MouseListener ls[] = comp.getMouseListeners();
        for(int i = 0; i < ls.length; i++)
        {
            if(ls[i] instanceof ClickListener)
            {
                Err.error("Already have a ClickListenerI on " + comp.getName());
            }
        }
        result = new ClickListener(
            (ItemAdapter) itemAdapter);
        return result;
    }

    public void installListeners(
        Object fieldComponent, Object itemAdapter, Method addActionListenerMethod)
    {
        installActionListener(fieldComponent, addActionListenerMethod, itemAdapter);
        //Listening to components' focus is not really a per-Strandz-component
        //thing, but a 'whole Strandz application' thing
        installClickListener( fieldComponent, itemAdapter);
        // installItemValidator(
        // fieldComponent);
    }

    private void installActionListener(
        Object fieldComponent, Method actionListenerMethod, Object itemAdapter)
    {
        if(actionListenerMethod != null)
        {
            Object[] args = new Object[1];
            args[0] = new MyActionListener((ItemAdapter) itemAdapter);
            SelfReferenceUtils.invoke(fieldComponent, actionListenerMethod, args);
            // Err.pr( "Installed actionListener for " + itemAdapter);
        }
    }

    void removeActionListeners(Component comp, Method removeActionListenerMethod)
    {
        if(removeActionListenerMethod != null)
        {
            EventListener ls[] = comp.getListeners(ActionListener.class);
            for(int i = 0; i < ls.length; i++)
            {
                Object[] args = new Object[1];
                args[0] = ls[i];
                if(ls[i] instanceof MyActionListener)
                {
                    SelfReferenceUtils.invoke(comp, removeActionListenerMethod, args);
                }
            }
        }
    }

    void removePopupListeners(Component comp)
    {
        MouseListener ms[] = comp.getMouseListeners();
        for(int i = 0; i < ms.length; i++)
        {
            if(ms[i] instanceof PopupListener)
            {
                comp.removeMouseListener(ms[i]);
            }
        }
    }

    void addRClickListener(Component comp, ItemAdapter itemAdapter)
    {
        String txt = "Restore to <" + itemAdapter.getB4ImageValue() + ">";
        RClickListener l = new RClickListener(itemAdapter);
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(txt);
        menuItem.addActionListener(l);
        popup.add(menuItem);

        MouseListener popupListener = new PopupListener(popup);
        comp.addMouseListener(popupListener);
    }

    private static class RClickListener implements ActionListener
    {
        ItemAdapter itemAdapter;

        private RClickListener(
            ItemAdapter itemAdapter)
        {
            this.itemAdapter = itemAdapter;
        }

        public void actionPerformed(ActionEvent e)
        {
            itemAdapter.resetToB4ImageValue();
        }
    }

    private static class PopupListener extends MouseAdapter
    {
        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu)
        {
            popup = popupMenu;
        }

        public void mousePressed(MouseEvent e)
        {
            maybeShowPopup(e);
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
    
    static class ClickListener extends MouseAdapter
    {
        private ItemAdapter itemAdapter;

        public ClickListener(ItemAdapter itemAdapter)
        {
            this.itemAdapter = itemAdapter;
        }

        public void mouseClicked( MouseEvent e) 
        {
            itemAdapter.getCell().getNode().changeFromCurrentNode();
        }
    }

    /**
     * For every component (JTextField etc) when the user has signalled that
     * he is going to the next component (by enter or click elsewhere)
     * then this class informs MoveTracker
     */
    private static class MyActionListener implements ActionListener
    {
        ItemAdapter itemAdapter;

        private MyActionListener(
            ItemAdapter itemAdapter)
        {
            this.itemAdapter = itemAdapter;
        }

        public void actionPerformed(ActionEvent e)
        {
            /*
            Err.pr( "$$$$$$$      Action Listening: " + e);
            times++;
            Err.pr( "-----------> times " + times);
            if(times == 0)
            {
            Err.stack();
            }
            */
            // ((Component)e.getSource()).getName());
            if(itemAdapter.isInError())
            {
                // Doesn't work
                // ((Component)e.getSource()).invalidate();
                // ((Component)e.getSource()).repaint();
                return;
            }

            itemAdapter.setOriginalAdapter(itemAdapter);
            // Too many messages
            // Err.pr( "TEMPORARILY DISABLED EntrySiteEnum.ACTION_LISTENER");
            if(itemAdapter.getMoveBlock() != null)
            {
                MoveTrackerI mManager = itemAdapter.getMoveBlock().getMoveTracker();
                mManager.enter(itemAdapter, EntrySiteEnum.ACTION_LISTENER);
                mManager.exitEnter();
            }
            // TO HERE BRING BACK
            // Doesn't work
            // ((Component)e.getSource()).invalidate();
            // ((Component)e.getSource()).repaint();
        }
    }
}
