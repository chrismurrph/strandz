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
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;

/**
 * Houses a JTabbedPane, but if there is only one pane added then looks just like any other panel.
 */
public class TabbedPanel extends JPanel
{
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JComponent firstPane;
    private String firstName;

    public TabbedPanel()
    {
        setLayout( new BorderLayout());
    }

    public void addTab(String name, JComponent panel)
    {
        if(firstPane == null)
        {
            firstName = name;
            firstPane = panel;
            add( panel);
        }
        else
        {
            if(tabbedPane.getComponentCount() == 0)
            {
                remove( firstPane);
                tabbedPane.addTab( firstName, firstPane);
                add( tabbedPane);
            }
            tabbedPane.addTab( name, panel);
        }
    }

    public void addChangeListener( ChangeListener changeListener)
    {
        tabbedPane.addChangeListener( changeListener);
    }

    public void setSelectedIndex( int idx)
    {
        if(tabbedPane.getComponentCount() > idx)
        {
            tabbedPane.setSelectedIndex( idx);
        }
        else
        {
            Err.pr( "Cannot index into TabbedPanel at " + idx + " b/c component count is " +
                    tabbedPane.getComponentCount());
        }
    }

    public int getSelectedIndex()
    {
        return tabbedPane.getSelectedIndex();
    }

    public String getTitleAt(int currentIndex)
    {
        String result = null;
        if(tabbedPane.getComponentCount() == 0)
        {
            Assert.isTrue(currentIndex == 0);
            result = firstName;
        }
        else
        {
            result = tabbedPane.getTitleAt( currentIndex);
        }
        return result;
    }
}
