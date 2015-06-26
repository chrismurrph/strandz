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

import org.strandz.core.info.domain.AbstractOwnTableMethods;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Gives direct access to the data in a table.
 */
public class JTableMethods extends AbstractOwnTableMethods
{
    /**
     * Get an entire column
     */
    public List getList(Object tableControl, String identifier)
    {
        List result = new ArrayList();
        JTable table = (JTable) tableControl;
        TableModel model = table.getModel();
        int size = model.getRowCount();
        TableColumn column = table.getColumn(identifier);
        int col = column.getModelIndex();
        for(int i = 0; i <= size - 1; i++)
        {
            Object value = table.getValueAt(i, col);
            if(value == null)
            {
                Err.pr( SdzNote.GENERIC, "Cell renderer is " + column.getCellRenderer() + " for <" + identifier + ">");
            }
            result.add(value);
        }
        return result;
    }

    /**
     * Get the value of the cell at the currently selected row, for the
     * identifier column name.
     */
    public Object getValue(Object tableControl, String identifier)
    {
        Object result = null;
        JTable table = (JTable) tableControl;
        TableModel model = table.getModel();
        /*
        for(int i=0; i<=model.getColumnCount()-1; i++)
        {
        String name = model.getColumnName( i);
        Err.pr( "table Column id: " + name);
        }
        */
        TableColumn column = table.getColumn(identifier);
        int col = column.getModelIndex();
        int row = table.getSelectedRow();
        if(row == -1)
        {
            Err.error("Cannot get a current row if none selected");
        }
        result = table.getValueAt(row, col);
        Err.pr("Will ret value " + result);
        return result;
    }

    /**
     * Fill in an entire column
     */
    public void setList(Object tableControl, String identifier, Object value)
    {
        List newValues = (List) value;
        JTable table = (JTable) tableControl;
        TableModel model = table.getModel();
        TableColumn column = table.getColumn(identifier);
        int col = column.getModelIndex();
        for(int i = 0; i <= newValues.size() - 1; i++)
        {
            model.setValueAt(newValues.get(i), i, col);
        }
    }

    /**
     * Fill in the cell at the selected row.
     */
    public void setValue(Object tableControl, String identifier, Object value)
    {
        JTable table = (JTable) tableControl;
        TableColumn column = table.getColumn(identifier);
        int col = column.getModelIndex();
        int row = table.getSelectedRow();
        table.setValueAt(value, row, col);
    }

    public Object getControlAt(Object tableControl, int row, int col)
    {
        Object result = null;
        JTable table = (JTable) tableControl;
        result = table.getCellRenderer(row, col);
        return result;
    }

    public Object getControlAt(Object tableControl, int col)
    {
        Err.error("getControlAt()" + " not implemented");
        return null;
    }

    public boolean isEditableControlAt(Object tableControl, int col)
    {
        Err.error("isEditableControlAt()" + " not implemented");
        return false;
    }

    public void setControlAt(Object tableControl, int row, int col, Object renderer)
    {
        JTable table = (JTable) tableControl;
        TableColumn tableColumn = table.getColumnModel().getColumn(col);
        tableColumn.setCellRenderer((TableCellRenderer) renderer);
    }

    public void repositionTo(Object tableControl, int row, int col)
    {
        JTable table = (JTable) tableControl;
        if(table.getColumnCount() > 0 && table.getRowCount() > 0)
        {
            table.setColumnSelectionInterval(col, col);
            Err.pr(SdzNote.BAD_TABLE_REPOSITIONING, "Repositioning to " + row);
            try
            {
                table.setRowSelectionInterval(row, row);
            }
            catch(IllegalArgumentException ex)
            {
                Err.error( "Don't have a row at " + row + " for table " + table);
            }
        }
    }
}
