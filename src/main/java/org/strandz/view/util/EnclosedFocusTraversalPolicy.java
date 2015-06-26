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
package org.strandz.view.util;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.core.domain.ControlSignatures;

import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.Component;
import java.awt.Container;
import java.util.Iterator;
import java.util.List;

/**
 * Whilst user is tabbing, this policy will not take the focus to the containing
 * parent (can be relaxed) or any other components, except for those that are meant to be focused
 * on in a normal application - all of which reside within the containing parent.
 */
public class EnclosedFocusTraversalPolicy extends ContainerOrderFocusTraversalPolicy
{
    private Component parentPanel;
    private boolean acceptParent = false;
    private Component defaultComponent;
    private boolean debug = false;

    private static int times;

    public EnclosedFocusTraversalPolicy( Component parentPanel)
    {
        this(parentPanel, false, null);
    }

    public EnclosedFocusTraversalPolicy( Component parentPanel, Component defaultComponent)
    {
        this(parentPanel, false, defaultComponent);
    }

    private EnclosedFocusTraversalPolicy( Component parentPanel, boolean acceptParent, Component defaultComponent)
    {
        this.parentPanel = parentPanel;
        this.acceptParent = acceptParent;
        this.defaultComponent = defaultComponent;
        //setImplicitDownCycleTraversal( false);
    }

    public Component getDefaultComponent(Container aContainer)
    {
        Component result = super.getDefaultComponent( aContainer);
        if(debug)
        {
            Err.pr( "default comp: <" + result + ">, where parent panel's class is " + 
                    parentPanel.getClass().getName());
        }
        return result;
    }

    protected boolean accept( Component comp)
    {
        boolean result = super.accept( comp);
        String acceptReason = null;
        String rejectReason;
        if(result)
        {
            times++;
            if(times == 0)
            {
                Err.debug();
            }
            result = false;
            rejectReason = "ContainerOrderFocusTraversalPolicy rejected";
            if(comp == parentPanel)
            {
                if(!acceptParent)
                {
                    result = false;
                    rejectReason = comp.getName() + " is the parent panel";
                }
                else
                {
                    result = true;
                    acceptReason = comp.getName() + " is the parent panel";
                }
            }
            else if(isALookThruControl( comp))
            {
                result = false;
                rejectReason = "will not be interesting, but one of its children will be";
            }
            else if(hasNameButNotTerminating( comp))
            {
                if(hasChildWithSameName( comp))
                {
                    result = false;
                    rejectReason = "one of its children will be visible (used for MDef)";
                }
                else
                {
                    result = true;
                    acceptReason = "has name <" + comp.getName() + "> (but is not terminating)";
                }
            }
            else if(comp.getParent() != parentPanel && hasNameButNotTerminating( comp.getParent()))
            {
                result = true;
                acceptReason = "parent has name <" + comp.getParent().getName() + "> (but is not terminating)";
            }
            if(debug)
            {
                if(result)
                {
                    Err.pr( "Accepted " + times + " component " + comp.getClass().getName() + " because " + acceptReason);
                }
                else
                {
                    Err.pr( "\tRejected " + times + " component " + comp.getClass().getName() + " because " + rejectReason);
                }
            }
        }
        return result;
    }

    private static boolean hasNameButNotTerminating( Component comp)
    {
        boolean result = false;
        if(!Utils.isBlank( comp.getName()))
        {
            result = true;
        }
        Class compClass = comp.getClass();
        if(ControlSignatures.isTerminatingControl( compClass))
        {
            result = false;
        }
        else if(ControlSignatures.isDisplayOnly( compClass))
        {
            result = false;
        }
        return result;
    }
    
    private static boolean isALookThruControl( Component comp)
    {
        boolean result = false;
        if(ControlSignatures.isLookThruControl( comp.getClass()))
        {
            result = true;
        }
        return result;
    }

    private static boolean hasChildWithSameName( Component comp)
    {
        boolean result = false;
        if(comp instanceof Container)
        {
            Container container = (Container)comp;
            String containerName = container.getName();
            if(Utils.isBlank( containerName))
            {
                Err.error( "containerName is blank");
            }
            List allChildren = ComponentUtils.getAllControls( container);
            for(Iterator iterator = allChildren.iterator(); iterator.hasNext();)
            {
                Component subControl = (Component)iterator.next();
                if(!Utils.isBlank( subControl.getName()))
                {
                    if(containerName.equals( subControl.getName()))
                    {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Note that debugging won't be obvious when you override the accept button.
     * @param debug
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

}
