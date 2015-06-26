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
import org.strandz.core.widgets.CompTableModelI;
import org.strandz.core.widgets.TableComp;
import org.strandz.lgpl.util.Err;

import java.util.ArrayList;
import java.util.List;

public class TableCompColumnLister extends AbstractOwnTableMethods
{
    private int ordinal = 0;

    /**
     * @param tableControl the TableComp used for NonVisualTableAttributes
     * @param identifier   for a column, not relevant here as always only one column
     *                     for a TableCompColumnLister
     */
    public List getList(Object tableControl, String identifier)
    {
        List result = new ArrayList();
        TableComp tableComp = (TableComp) tableControl;
        CompTableModelI model = tableComp.getModel();
        int size = model.getRowCount();
        //TableColumn column = tableComp.getColumn( identifier);
        //int col = column.getModelIndex();
        for(int i = 0; i <= size - 1; i++)
        {
            Object value = model.getValueAt(i, 0);
            /*
             * Not a note, but good for testing
            if(value == null)
            {
              Err.error( "Could not find a value at row " + i + " for " + tableComp.getName());
            }
            */
            result.add(value);
        }
        return result;
    }

    public void setList(Object tableControl, String identifier, Object value)
    {
        Err.error("TableCompColumnLister setList() not yet implemented");
    }

    public Object getValue(Object tableControl, String identifier)
    {
        Err.error("TableCompColumnLister getValue() not yet implemented");
        return null;
    }

    public void setValue(Object tableControl, String identifier, Object value)
    {
        Err.error("TableCompColumnLister setValue() not yet implemented");
    }

    /*
    public Object getControlAt(Object tableControl, int row, int col)
    {
        Err.error("TableCompColumnLister getRenderer() not yet implemented on a " + tableControl.getClass().getName());
        return null;
    }

    public Object getControlAt(Object tableControl, int col)
    {
        Err.error("getControlAt()" + " not implemented");
        return null;
    }
    */
    
    /*
    public Object getControlAt(Object tableControl, int col)
    {
        return getControlAt( tableControl, Utils.UNSET_INT, col);
    }
    */
    
    public Object getControlAt(Object tableControl, int row, int col)
    {
        Object result;
        TableComp table = (TableComp) tableControl;
        result = table.getControlAt(row, col);
        return result;
    }

    public Object getControlAt(Object tableControl, int col)
    {
        Object result;
        TableComp table = (TableComp) tableControl;
        result = table.getControlAt(col);
        return result;
    }

    public boolean isEditableControlAt(Object tableControl, int col)
    {
        return true;
    }

    public void setControlAt(Object tableControl, int row, int col, Object renderer)
    {
        Err.error("TableCompColumnLister setRenderer() not yet implemented");
    }

    public void repositionTo(Object tableControl, int row, int col)
    {
        Err.error("TableCompColumnLister repositionTo() not yet implemented");
    }
}
