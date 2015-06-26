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

import org.strandz.lgpl.util.Err;

import javax.swing.JTabbedPane;
import java.awt.Component;

public class DebugTabbedPane extends JTabbedPane
{
    private static int constructedTimes;
    private static boolean debugging = false;
    private int id;

    private void pr(String s)
    {
        if(debugging)
        {
            Err.pr(s);
        }
    }

    public DebugTabbedPane()
    {
        constructedTimes++;
        id = constructedTimes;
        pr("## CONSTRUCTED DebugTabbedPane: " + id);
    }

    public void remove(Component component)
    {
        pr("To remove " + component);
        super.remove(component);
    }

    public void remove(int index)
    {
        pr("To remove component at " + index);
        super.remove(index);
    }

    public void removeAll()
    {
        pr("To remove all");
        super.removeAll();
    }

    public void addTab(String title, Component component)
    {
        pr("To addTab " + title + " to ID:" + id);
        pr("Already has " + getTabCount());
        super.addTab(title, component);
    }
}
