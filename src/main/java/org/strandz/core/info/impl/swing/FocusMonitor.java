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
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.core.info.domain.FocusMonitorI;
import org.strandz.core.info.domain.ItemAdapterI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Print;

import javax.swing.JComponent;
import java.awt.Container;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * Keeps track of focus changes
 * Item validation triggered here. Note that focus changes will not happen
 * unless move to a different attributed item. 
 */
public class FocusMonitor implements PropertyChangeListener, FocusMonitorI
{
    private JComponent oldComp = null;
    private Map<String, ItemAdapterI> adapters;
    private ItemAdapterI focused;
    private static boolean debugging = false;

    /**
     * @param firstFocusedComp the component that is the first one the user sees as
     *                         being in focus. It is a bit of a hack that this needs to be provided - making
     *                         up for the fact that there is no initial property change recorded.
     */
    public FocusMonitor( JComponent firstFocusedComp)
    {
        if(debugging)
        {
            if(firstFocusedComp != null)
            {
                Err.pr( "FocusMonitor created for control " + firstFocusedComp.getName());
            }
            else
            {
                Err.pr( "FocusMonitor created with null control");
            }
        }
        this.oldComp = firstFocusedComp;
    }

    public void acceptAdapters( Map<String, ItemAdapterI> adapters)
    {
        this.adapters = adapters;
        if(debugging)
        {
            Err.pr( "Adapters accepted into FocusMonitor");
            Print.prMap( adapters);
        }
    }
    
    public ItemAdapterI getFocused()
    {
        return focused;    
    }

    /**
     * Here we continually monitor all the focus changes, enabling
     * the class to always know what the last focused widget was.
     *
     * @param evt the PropertyChangeEvent to examine
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        String prop = evt.getPropertyName();
        if(("focusOwner".equals(prop)) &&
            (evt.getNewValue() != null) &&
            !(evt.getNewValue() instanceof Window))
        {
            JComponent newComp = (JComponent) evt.getNewValue();
            String newName = newComp.getName();
            if(Utils.isBlank(newName))
            {
                newName = newComp.getClass().getName();
            }
            String oldName = null;
            if(oldComp != null)
            {
                oldName = oldComp.getName();
                if(Utils.isBlank(oldName))
                {
                    oldName = oldComp.getClass().getName();
                }
            }
            Err.pr(SdzNote.FIELD_VALIDATION, "Focus owner is now " + newName + " (from " + oldName + ")");
            focusChanged( newComp);
            oldComp = newComp;
        }
    }

    private void focusChanged( JComponent to)
    {
        if(adapters != null)
        {
            String compTxt = to.getName();
            if(compTxt == null)
            {
                compTxt = to.getClass().getName();
            }
            ItemAdapter itemAdapter = (ItemAdapter) adapters.get(to.getName());
            if(itemAdapter == null)
            {
                if(SdzNote.FIELD_VALIDATION.isVisible())
                {
                    Err.pr("Have not been able to find an adapter for: " + to);
                    Err.pr("\tparent is: " + to.getParent());
                    if(to.getParent().getParent() != null)
                    {
                        Container grand = to.getParent().getParent();
                        Err.pr("\tgrandparent is: " + grand);
                        if(grand.getParent() != null)
                        {
                            Container greatGrand = grand.getParent();
                            Err.pr("\tgreat-grandparent is: " + greatGrand);
                            if(greatGrand.getParent() != null)
                            {
                                Container greatGreatGrand = greatGrand.getParent();
                                Err.pr("\tgreat-great-grandparent is: " + greatGreatGrand);
                            }
                        }
                    }
                    //Print.prSet(adapters.keySet());
                }
            }
            else if(!itemAdapter.isInError())
            {
                Err.pr(SdzNote.FIELD_VALIDATION, "==================In focusGained for " + compTxt);
                if(itemAdapter.getCell().getNode().isAllowed( CapabilityEnum.FOCUS_NODE))
                {
                    if(itemAdapter.getMoveBlock() == null)
                    {
                        Err.error( "itemAdapter.getMoveBlock() == null for <" + itemAdapter + ">, ID: " +
                            itemAdapter.getId());
                    }

                    MoveTrackerI mManager = itemAdapter.getMoveBlock().getMoveTracker();
                    itemAdapter.setOriginalAdapter(itemAdapter);
                    mManager.enter(itemAdapter, EntrySiteEnum.FIELD_FOCUS_GAINED);
                    /*
                     * To solve this setFocusCausesNodeChange() to false for
                     * itemAdapter.getCell().getNode() ie. set so that focusing
                     * on it does not cause a node change event. Found got
                     * problems when running fast in batch mode. Using event
                     * dispatch thread when running in batch mode will always
                     * cause problems.
                     */
                    /* Not only doing here as nothing recorded when go back on same
                     * control after going to another node via mouse click.
                     * See CM.ClickListenerI for a place where this call is
                     * also made: 
                     */
                    itemAdapter.getCell().getNode().changeFromCurrentNode();
                    mManager.exitEnter();
                }
                focused = itemAdapter;
            }
        }
    }

//  private static void pr( String txt)
//  {
//    if(debugging)
//    {
//      Err.pr( txt);
//    }
//  }
}
