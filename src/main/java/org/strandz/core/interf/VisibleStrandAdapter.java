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
package org.strandz.core.interf;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.view.util.AbstractStrandArea;

import javax.swing.JComponent;

/**
 * Make no-ops of the ones that are commonly no-ops. Thus b/c not all done then when
 * it is used, your implementing class will still/also have to implement ActionableStrandI
 */
public class VisibleStrandAdapter
{
    /**
     * TODO This is the way the code would always have been, however this control is never visible for
     * the user - so case for a NullStrandArea 
     */
    //private AbstractStrandArea strandArea = new UnadornedStrandArea();
    private String title;
    
    public void sdzInit()
    {
    }

    public void preForm()
    {
    }

    public boolean select(boolean b, String reason)
    {
        return true;
    }

    public void setNodeController(NodeController nodeController)
    {
    }

    public void setStrandArea(AbstractStrandArea belowMenuPanel)
    {
    }

    public void setCurrentPane(int idx)
    {
    }
    
    public void setRequiresRefresh()
    {
    }

    public NodeController getNodeController()
    {
        return null;
    }
    
    public void detachControls()
    {
    }
    
    public void setAdorned( boolean b)
    {
    }
    
    public boolean isAdorned()
    {
        Err.pr( SdzNote.NO_HOUSING_HELPERS, "Returning adorned false in " + this.getClass().getName());
        return false;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    
    public SdzBagI getSdzBagI()
    {
        return null;
    }
    
    public void relocateBack(RelocationBackCallbacksI relocationBackCallbacks)
    {
        
    }
    
    public void relocateOut(AbstractStrandArea externalStrandArea, 
                            RelocationOutCallbacksI relocationOutCallbacks, VisibleStrandI callingStrand)
    {
        
    }
    
    public void setPanelNodeTitle(JComponent pane, Node workerNode, String s)
    {
        
    }
    
    public Application getApplication()
    {
        return null;
    }
}
