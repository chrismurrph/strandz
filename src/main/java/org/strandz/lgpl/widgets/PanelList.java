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

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Keeps the List it is constructed with up to date.
 */
public class PanelList extends JPanel
{
    private int height = -99;
    private int tableHeight = -99;
    // When come to doing (insert and delete) keys can use this
    // public MyUnattachedJTable table;
    public JList list;
    private JScrollPane sp;
    private JComponent lowerPanel;

    public PanelList()
    {
    }

    public void init()
    {
        list = new JList();
        list.setEnabled(false); // ?? what happens?
        list.setFocusable(false); // works visually, but focus listener still picks up
        // list.setSelectionModel( new LocalSelectionModel());
        sp = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setLayout(new BorderLayout());
        add(sp, BorderLayout.NORTH);
        setName( "PanelList");
    }

    public void setHeight(int height)
    {
        Dimension dimMax = new Dimension(Integer.MAX_VALUE, height);
        setMaximumSize(dimMax);

        Dimension dimPref = new Dimension((int) getPreferredSize().getWidth(),
            height);
        setPreferredSize(dimPref);
        this.height = height;
    }

    public int getHeight()
    {
        return height;
    }

    public void setTableHeight(int height)
    {
        Dimension dimMax = new Dimension(Integer.MAX_VALUE, height);
        sp.setMaximumSize(dimMax);

        Dimension dimPref = new Dimension((int) getPreferredSize().getWidth(),
            height);
        sp.setPreferredSize(dimPref);
        this.tableHeight = height;
    }

    public int getTableHeight()
    {
        return tableHeight;
    }

    public void addSouthComponent(JComponent component)
    {
        // lowerPanel = new JButton( "Hi");
        add(component, BorderLayout.CENTER);
    }
    /*
    * Hapless attempt at making any selection not happen - need different control
    private static class LocalSelectionModel implements ListSelectionModel
    {
    public void setSelectionInterval(int index0, int index1){}
    public void addSelectionInterval(int index0, int index1){}
    public void removeSelectionInterval(int index0, int index1){}
    public int getMinSelectionIndex(){ return -1;}
    public int getMaxSelectionIndex(){ return -1;}
    public boolean isSelectedIndex(int index){ return false;}
    public int getAnchorSelectionIndex(){ return -1;}
    public void setAnchorSelectionIndex(int index){}
    public int getLeadSelectionIndex(){ return -1;}
    public void setLeadSelectionIndex(int index){}
    public void clearSelection(){}
    public boolean isSelectionEmpty(){ return true;}
    public void insertIndexInterval(int index, int length, boolean before){}
    public void removeIndexInterval(int index0, int index1){}
    public void setValueIsAdjusting(boolean valueIsAdjusting){}
    public boolean getValueIsAdjusting(){ return false;}
    public void setSelectionMode(int selectionMode){}
    public int getSelectionMode(){ return SINGLE_SELECTION;}
    public void addListSelectionListener(ListSelectionListener x){}
    public void removeListSelectionListener(ListSelectionListener x){}
    }
    */
}
