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
package org.strandz.applic.wombatrescue;

import org.strandz.core.interf.Node;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.DebugLabel;
import org.strandz.lgpl.widgets.table.ClickAdapter;
import org.strandz.lgpl.widgets.table.ComponentTableView;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class QuickRosterSlotRightClickPopup extends ClickAdapter
{
    private JPopupMenu popupMenu;
    private ComponentTableView table;
    private int currentRow;
    private VisibleStrandI visibleStrand;

    private static final String EDIT = "Edit";
    private static final boolean DEBUG = false;
    
    private class MenuClickListener implements ActionListener
    {
        private MenuClickListener()
        {
        }

        public void actionPerformed( ActionEvent e)
        {
            if(e.getActionCommand().equals(EDIT))
            {
                Strand strand = visibleStrand.getSdzBagI().getStrand(); 
                /*
                 * Another thing to throw into the mix here is that the detail node could be marked
                 * by Node.setIgnoredChild( true) in which case the master "Worker Node" node would 
                 * always stay the current one and so obviously we wouldn't need to jump up to it
                 * in the first place. Will want to do this if the StatusBar becomes annoying. If
                 * do this will want to switch off IgnoredChild for testDetailToDetail(). 
                 */
                Err.pr(SdzNote.SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY, 
                       "Should not need to explicity go to master first - testDetailToDetail() fails");
                strand.goNode( strand.getNodeByName( "Worker Node"));
                Node node = strand.getCurrentNode();
                Err.pr(SdzNote.SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY, "current node: " + node);
                visibleStrand.setCurrentPane( 1);
                node = strand.getCurrentNode();
                Err.pr(SdzNote.SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY, "After setCurrentPane(), current node: " + node);
                /**/
                boolean ok = strand.SET_ROW( currentRow);
                Err.pr(SdzNote.SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY, "Going to row " + currentRow + " on node " + 
                        strand.getCurrentNode() + " worked: " + ok);
                /**/        
            }
        }
    }
    
    public QuickRosterSlotRightClickPopup( ComponentTableView table, VisibleStrandI visibleStrand/*, JTabbedPane tabbedPane*/)
    {
        this.table = table;
        //this.tabbedPane = tabbedPane;
        this.visibleStrand = visibleStrand;
        popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(EDIT);
        MenuClickListener l = new MenuClickListener();
        menuItem.addActionListener(l);
        popupMenu.add( menuItem);
    }
    
    public void outerSingleClicked( int row, int column, MouseEvent e)
    {
        JLabel label = (JLabel)table.getControlAt( row, column);
        Err.pr( DEBUG, "Outer clicked on " + label.getText());
        if(!Utils.isBlank(label.getText()))
        {
            Err.pr( DEBUG, "comp for popup: " + label.getName() + " has type " + label.getClass().getName()
             + ", was found at row " + row + ", column " + column);
            /* component that comes from mouse not always one we want to be 'invoker' */
            if(e.getComponent() != label)
            {
                Err.pr( "label, name: " + label.getName() + ", type " + label.getClass().getName() 
                        + ", id " + ((DebugLabel)label).id + ", parent " + label.getParent());
                Err.pr( "event comp, name: " + e.getComponent().getName() + ", type " + e.getComponent().getClass().getName() 
                        + ", id " + ((DebugLabel)e.getComponent()).id + ", parent " + e.getComponent().getParent()
                );
                Err.error();
            }
            /**/
            currentRow = row;
            popupMenu.show( label, e.getX(), e.getY());
        }
    }    
}
