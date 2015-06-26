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
package org.strandz.view.wombatrescue;

import org.strandz.view.util.EnclosedFocusTraversalPolicy;
import org.strandz.widgets.data.wombatrescue.FlexibilityRadioButtons;

import javax.swing.Scrollable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

public class TraversableWorkerPanel extends WorkerPanelWithTab implements Scrollable
{
    
    public TraversableWorkerPanel()
    {
        setFocusCycleRoot(true);
        EnclosedFocusTraversalPolicy policy = new TraversableWorkerPanel.LocalEnclosedFocusTraversalPolicy( this);
        policy.setDebug( false);
        setFocusTraversalPolicy( policy);
    }
    
    private static class LocalEnclosedFocusTraversalPolicy extends EnclosedFocusTraversalPolicy
    {
        public LocalEnclosedFocusTraversalPolicy(Component parentPanel)
        {
            super(parentPanel);
        }
        protected boolean accept( Component comp)
        {
            boolean result = super.accept( comp);
            if(result)
            {
                //We want tabbing to step right into the buttons rather than to first go to their
                //JPanel container, which the user can't even see.
                if(comp instanceof FlexibilityRadioButtons)
                {
                    result = false;
                }
            }
            return result;
        }
    }
    
    public void init()
    {
        super.init();
        setName("TraversableWorkerPanel");
    }

    public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2)
    {
        return 30;
    }

    public boolean getScrollableTracksViewportWidth()
    {
        return true;
    }

    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }

    public Dimension getPreferredScrollableViewportSize()
    {
        return null;
    }

    public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2)
    {
        return 30;
    }
}
