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

import org.strandz.core.domain.AbstNodeTable;
import org.strandz.core.domain.NodeTableMethods;
import org.strandz.core.domain.NodeTableModelI;
import org.strandz.core.widgets.CompTableModelI;
import org.strandz.core.widgets.TableComp;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.BeansUtils;

import javax.swing.event.TableModelListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Each NonVisualTableAttribute will have one of these. It is basically a list which stores a 
 * column worth of values. (Note that this is different to the applichousing variants which are used
 * to access the whole matrix of values). Thus everything is asked for at column 0.
 */
public class TableCompModelImpl extends NodeTableMethods
    implements CompTableModelI
{
    private AbstNodeTable node;
    private NodeTableModelI nodeTableModel;
    private TableComp tableView;
    List list = new ArrayList();
    public boolean debug = false;

    public void setDebug(boolean b)
    {
        this.debug = b;
    }

    public List getList()
    {
        return list;
    }

    public void setList(List list)
    {
        this.list = list;
    }
    
    public String getId()
    {
        String result = null;
        if(node != null)
        {
            result = node.getName();
        }
        else
        {
            
        }
        return result;
    }

    public int getRowCount()
    {
        int result = getNodeTableModel().getRowCount();
        return result;
    }

    public int getColumnCount()
    {
        //return 1;
        return getNodeTableModel().getColumnCount();
    }

    public String getColumnName(int columnIndex)
    {
        return null;
    }

    public Class getColumnClass(int columnIndex)
    {
        //return null;
        return getNodeTableModel().getColumnClass(columnIndex);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if(columnIndex != 0)
        {
            Err.error("Expect columnIndex to be always 0");
        }
        Object obj = getNodeTableModel().getValueAt(rowIndex, 0);
        return obj;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Err.error("Won't be called, as TableComp doesn't have an editor/s");
        /*
        if(columnIndex != 0)
        {
          Err.error( "Expect columnIndex to be always 0");
        }
        list.set( rowIndex, aValue);
        */
    }

    public void addTableModelListener(TableModelListener l)
    {
    }

    public void removeTableModelListener(TableModelListener l)
    {
    }

    public void acceptEdit()
    {// nufin
    }

    public void rejectEdit()
    {// nufin
    }

    public String getTableName()
    {
        return tableView.getName();
    }

    public void oneRowSelectionOn(Object tableView)
    {
        this.tableView = (TableComp) tableView;
    }

    public void repositionTo(Object itemAdapter)
    {
        Err.error("[7]Not implemented yet");
    }

    public void fireTableRowsInserted(int firstRow, int lastRow)
    {
        Err.error("[8]Not implemented yet");
    }

    public void fireTableRowsDeleted(int firstRow, int lastRow)
    {
        Err.error("[9]Not implemented yet");
    }

    public void fireRowChangedTo(int row)
    {
        Err.error("[10]Not implemented yet");
    }

    public void fireTableDataChanged( String reason)
    {
        Err.error("[11]Not implemented yet");
    }

    public void setNode(AbstNodeTable node, NodeTableModelI model)
    {
        //always null
        this.node = node;
        nodeTableModel = model;
        if(nodeTableModel == null)
        {
            Err.error("No nodeTableModel from Node " + node);
        }
    }

    /**
     * When we use this method we go thru to the non applichousing table model 
     * which contains both flatColumns and nvFlatColumns - and which is 
     * accessed will depend on how the question doingNonVisual() is
     * answered. 
     */
    public NodeTableModelI getNodeTableModel()
    {
        if(nodeTableModel == null)
        {
            if(!BeansUtils.isDesignTime())
            {
                Err.error("NodeTableModel is NULL for node " + node);
            }
        }
        return nodeTableModel;
    }
}
