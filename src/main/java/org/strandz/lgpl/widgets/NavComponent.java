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

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JPanel;
import javax.swing.Icon;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class NavComponent extends JPanel
{
    private JLabel bNext;
    private JLabel bPrevious;
    private MouseListener nextMouseListener = new MouseListener(); 
    private MouseListener previousMouseListener = new MouseListener(); 
    private List<NavigationListenerI> navigationListeners = new ArrayList<NavigationListenerI>();
    
    private static Icon nextPortableImageIcon;
    private static Icon previousPortableImageIcon;
    
    static
    {
        nextPortableImageIcon = PortableImageIcon.createImageIcon( 
                "images/Next.gif", NavEnum.Next.toString(), PortableImageIcon.STDERR_MSG);
        previousPortableImageIcon = PortableImageIcon.createImageIcon( 
                "images/Prev.gif", NavEnum.Previous.toString(), PortableImageIcon.STDERR_MSG);
    }
    
    private static class NavButton extends JLabel
    {
        private NavEnum navEnum;
        
        private NavButton( NavEnum navEnum, Icon icon, MouseListener mouseListener)
        {
            super( icon);
            this.navEnum = navEnum;
            setName( navEnum.toString());
            setToolTipText( navEnum.toString());
            setOpaque( true);
            setBackground( Color.WHITE);
            setBorder( BorderFactory.createEmptyBorder( 2,2,2,2));
            addMouseListener( mouseListener);
        }
    }
    
    private class MouseListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            NavButton source = (NavButton)e.getSource();
            fireNavigated( source.navEnum);
        }
    }
        
    public void init()    
    {
        setOpaque( true);
        setBackground( Color.WHITE);
        bNext = new NavButton( NavEnum.Next, nextPortableImageIcon, nextMouseListener);
        bPrevious = new NavButton( NavEnum.Previous, previousPortableImageIcon, previousMouseListener);
        
        double size[][] =
            {   // Columns
                {0.5, 0.5},
                // Rows
                {ModernTableLayout.FILL}
            };
        setLayout( new ModernTableLayout(size));
    }
        
    public void setVisible( boolean previousVisible, boolean nextVisible)
    {
        removeAll();
        if(previousVisible && nextVisible)
        {
            add( bPrevious, "0, 0");
            add( bNext, "1, 0");
        }
        else
        {
            if(previousVisible)
            {
                add( bPrevious, "1, 0");
            }
            else if(nextVisible)
            {
                add( bNext, "1, 0");
            }
        }
    }
    
    public void addNavigationListener( NavigationListenerI navigationListener)
    {
        navigationListeners.add( navigationListener);
    }
    
    public boolean removeNavigationListener( NavigationListenerI navigationListener)
    {
        boolean result = navigationListeners.remove( navigationListener);
        return result;
    }
    
    private void fireNavigated( NavEnum navEnum)
    {
        for(Iterator iterator = navigationListeners.iterator(); iterator.hasNext();)
        {
            NavigationListenerI navigationListenerI = (NavigationListenerI)iterator.next();
            navigationListenerI.navigated( navEnum);
        }
    }    
}