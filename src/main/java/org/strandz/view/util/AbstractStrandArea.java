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

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

abstract public class AbstractStrandArea extends JPanel
{
    // When in MenuApplication this will sometimes get replaced, so
    // it needs to be accessible so can save it away and then re-add
    // it back to this panel.
    private JPanel contentPanel;
    private EnclosurePanel enclosure;    
    
    public void init()
    {
        contentPanel = new JPanel();
        contentPanel.setBackground( Color.RED);
        contentPanel.setName( getClass().getName());
        contentPanel.setName( "contentPanel");
        Err.pr(SdzNote.TASK_BAR_APPEARANCE, "Border to debug");
        // contentPanel.setBorder( BorderFactory.createLineBorder( Color.RED));
        Err.pr(SdzNote.TOOL_BAR_SHOULD_BE_IN_STRAND_AREA, "Will reinstate this");
        enclosure = new EnclosurePanel();
        enclosure.setName( "enclosure, id: " + enclosure.id);
        enclosure.setLayout(new BorderLayout());
        Err.pr(SdzNote.TASK_BAR_APPEARANCE, "Border to debug");
        // enclosure.setBorder( BorderFactory.createLineBorder( Color.BLUE));

        Err.pr(SdzNote.TASK_BAR_APPEARANCE, "Border to debug");
        // setBorder( BorderFactory.createLineBorder( Color.GREEN));
        removeAll();
        layoutContentPanel( contentPanel);
    }
    
    public static class EnclosurePanel extends JPanel
    {
        private static int timesConstructed = 0;
        public int id;
        
        EnclosurePanel()
        {
            timesConstructed++;
            id = timesConstructed;
        }
        
        public void remove( Component comp)
        {
            super.remove( comp);
            if(SdzNote.BRING_BACK_TAB.isVisible())
            {
                Err.pr( "EnclosurePanel, have removed " + comp.getName() + "(" + comp.getClass().getName() +  ")" + 
                        " from " + this.getName() + "(" + this.getClass().getName() +  ")");
                if(comp.getName().equals( "tabbedPane created by TabVisibleStrandHelper"))
                {
                    Err.stack();
                }
            }
        }
    }
    
    abstract public StatusArea getStatusArea();
    abstract public ToolBarArea getToolBarArea();
    abstract public ToolBarPane getToolBarPane();
    abstract void layoutContentPanel( JPanel contentPanel);
    
    public JComponent getEnclosure()
    {
        return enclosure;
    }
    
    /*
    public JPanel getContentPanel()
    {
        return contentPanel;
    }

    public void setContentPanel(JPanel contentPanel)
    {
        this.contentPanel = contentPanel;
    }
    */
}
