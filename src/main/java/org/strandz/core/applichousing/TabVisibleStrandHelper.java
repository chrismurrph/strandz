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

import org.strandz.core.interf.Application;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.StrandI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.widgets.TabbedPanel;
import org.strandz.view.util.AbstractStrandArea;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Helps implement the interface VisibleStrand
 */
public class TabVisibleStrandHelper extends AbstractStatusBarVisibleStrandHelper
{
    private ArrayList tabs = new ArrayList();
    private TabbedPanel tabbedPanel;
    private TabChangeListener tabListener;
    /**
     * Each time we manage to click on an index this is recorded so we can go
     * back later
     */
    private int backIndex = -1;
    private int times = 0;

    public TabVisibleStrandHelper(
        SBIStatusBarHelper barHelperSBI,
        StrandI strand,
        Application a,
        AbstractStrandArea.EnclosurePanel surface)
    {
        super(barHelperSBI, strand, a, surface);
    }

    /**
     * Will become an indexed property (so three params might become an external
     * (already internal) class in their own right).
     */
    public void setPanelNodeTitle(JComponent panel, Node node, String tabName)
    {
        if(panel == null)
        {
            Err.error("Each tab index property must have a tab");
        }

        // Err.pr( "panel height for " + tabName + " is "
        // + panel.getHeight());
        /*
        * times++; Err.pr( "++ setPanelNodeTitle with " + panel.getName() + "
        * times " + times); if(times == 0) { Err.stack(); }
        */
        NodeVisualContext tab = new NodeVisualContext(tabName, panel, node);
        Err.pr(SdzNote.ADDING_TABS_TOO_MANY_TIMES, "In helper adding tab(NodeVisualContext) with name " + tabName);
        tabs.add(tab);
    }

    public void display(boolean b)
    {
        super.display(b);
        if(b)
        {
            if(tabbedPanel == null) // Tabs displayed here are static
            {
                tabbedPanel = new TabbedPanel();
                // Border empty = BorderFactory.createEmptyBorder();
                // tabbedPane.setBorder( empty);
                tabbedPanel.setName("tabbedPane created by TabVisibleStrandHelper");
                if(tabs.isEmpty())
                {
                    Err.pr("Expect setPanelNodeTitle() to have been called at least once");
                }
                else
                {
                    for(Iterator en = tabs.iterator(); en.hasNext();)
                    {
                        NodeVisualContext tab = (NodeVisualContext) en.next();
                        Err.pr(SdzNote.GENERIC, "Adding tab with name " + tab.name);
                        tabbedPanel.addTab(tab.name, tab.panel);
                    }
                    tabListener = new TabChangeListener();
                    tabbedPanel.addChangeListener(tabListener);
                }
                getSurface().add(tabbedPanel, BorderLayout.CENTER);
                fireGoNodeEvent(); // so Controller looks ok
            }
            else
            {
                if(!ComponentUtils.controlIsInContainer( tabbedPanel, getSurface()))
                {
                    Err.error( "Missing out on add of " + tabbedPanel.getName() + " onto " + getSurface().getName());
                }
            }
        }
        else
        {
            Err.pr( SdzNote.BRING_BACK_TAB, 
                    "If the surface is being removed then we don't need to remove what is on the surface");
            /*
            Assert.notNull( tabbedPanel, "No tabbedPanel, so can't remove it from the surface");
            getSurface().remove( tabbedPanel);
            */
        }
        setupMessageDlg(b, tabbedPanel);
    }

    private class TabChangeListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent ev)
        {
            // JTabbedPane tp = (JTabbedPane)ev.getSource();
            fireGoNodeEvent();
        }
    } // end class TabChangeListener

    private NodeVisualContext getTabFromTitle(String title)
    {
        NodeVisualContext result = null;
        boolean foundTheTitle = false;
        for(Iterator en = tabs.iterator(); en.hasNext();)
        {
            NodeVisualContext tab = (NodeVisualContext) en.next();
            if(title.equals(tab.name))
            {
                result = tab;
                foundTheTitle = true;
                break;
            }
        }
        if(!foundTheTitle)
        {
            Err.error("useTab has not been called with " + title);
        }
        return result;
    }

    public void setCurrentPane(int idx)
    {
        tabbedPanel.setSelectedIndex(idx);

        ChangeEvent evt = new ChangeEvent(tabbedPanel);
        tabListener.stateChanged(evt);
    }
    
    public void init()
    {
        tabbedPanel.setSelectedIndex( 0);
    }
    
    private void fireGoNodeEvent()
    {
        /*
        * For later use, whenever tab record the current node of the place we
        * tabbed from
        */
        /*
        * if(backIndex != -1) { String title = tp.getTitleAt( backIndex);
        * NodeVisualContext tab = getTabFromTitle( title); tab.currentNode =
        * (Node)strand.getCurrentNode(); //Err.pr( "ZZ current node been set to " +
        * tab.currentNode + // " for " + title); }
        */
        int currentIndex = tabbedPanel.getSelectedIndex();
        if(currentIndex == -1)
        {
            return;
            // Err.error("No current index, source is " + ev.getSource());
        }

        String title = tabbedPanel.getTitleAt(currentIndex);
        NodeVisualContext tab = getTabFromTitle(title);
        /*
        * Need to give advice that changed tab to 3 entities: 1 core.info core -
        * that node has changed 2 status area - that node has changed 3 physical
        * control that was previously on To cope with possibility that there are >
        * 1 nodes: 2 as each TabbedPanel has its own NodeStatusBar, we simply make
        * sure that we alter this area of the screen so the right NodeStatusBar is
        * showing. This NodeStatusBar will already be showing the correct node
        * within the panel. 1 the latest node in any tab is always available in
        * tab.currentNode. 3 for some reason when focus on a tab it doesn't
        * remember what used to have the focus, so here is a good time to do that
        * job. (By default the focus goes to the first (whether that's upper left
        * or first added I don't know) window in the tabbed pane). Note that
        * behaviour may be confusing to user if non-core.info fields exist on a
        * tab.
        */
        refitStatusArea(tab.principalNode);

        // Err.pr( "To try go to " + tab.currentNode);
        boolean ok = true;
        if(strand.getCurrentNode() != tab.currentNode)
        {
            ok = tab.currentNode.GOTO();
            Err.pr( SdzNote.GO_NODE, "goTo was ok: " + ok + ", when went to " + tab.currentNode);
        }
        if(!ok)
        {
            if(backIndex != -1)
            {
                tabbedPanel.setSelectedIndex(backIndex);
                return; // so backIndex will stay same
            }
            else
            {
                Err.error("backIndex == -1 so can't correct tab");
            }
        }
        backIndex = currentIndex;
    }
}
