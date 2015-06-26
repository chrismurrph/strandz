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

import javax.swing.JComponent;
import java.util.Iterator;

/**
 * The root class of a hierarchy of classes whose function it
 * is to help deliver the VisibleStrandI interface. There is
 * no real hierarchy as such for VisibleStrand. This hierarchy
 * is more likely to come to reflect the hierarchy that exists
 * under Application.
 *
 * @author Chris Murphy
 */
abstract public class VisibleStrandHelper
{
    protected StrandI strand;
    
    abstract public void setPanelNodeTitle(
        JComponent p,
        Node node,
        String title);

    abstract public NodeController getNodeController();

    abstract public void setNodeController( NodeController nc);

    abstract public void setRequiresRefresh();
    
    abstract public void display( boolean b);

    abstract public void setCurrentPane( int idx);
    
    abstract public void init();
    
    /**
     * We don't actually detach the controls as they will be needed later so that
     * attributes can copy-construct themselves. Here we make sure that 'strandz'
     * is in a state whereby no data will be able to be retrieved (eg getItemValue()
     * will return null). This ensures no strange behaviour after we have say removed 
     * a tab and before we have replaced it with one with different data. See 
     * SdzNote.LOVS_CHANGE_DATA_SET for why needed to do this.
     */
    public void detachControls( boolean detach)
    {
        if(strand != null)
        {
            for(Iterator<Node> iterator = strand.getNodes().iterator(); iterator.hasNext();)
            {
                Node node = iterator.next();
                node.setDataDetached( detach);
            }
        }
        if(detach)
        {
            init();
        }
    }
}
