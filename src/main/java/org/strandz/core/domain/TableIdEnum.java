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
package org.strandz.core.domain;

import org.strandz.core.widgets.TableComp;
import org.strandz.lgpl.util.Err;

import java.awt.Component;

public class TableIdEnum extends IdEnum
{
    private Object table;
    private Class clazz;
    private int column;
    private int row;
    private String description;
    private static int times;
    
    static class NothingObject
    {
        public String toString()
        {
            return "NOTHING_OBJECT";            
        }
    }

    private static final Object NOTHING_OBJECT = new NothingObject();

    TableIdEnum(String name)
    {
        super(name);
    }

    public String toString()
    {
        String result;
        String tableTxt = null;
        String classTxt = null;
        if(table != null)
        {
            if(table instanceof TableComp)
            {
                tableTxt = ((TableComp) table).getName();
            }
            else
            {
                tableTxt = ((Component) table).getName();
            }
            if(tableTxt == null)
            {
                Err.pr( "Table ought to name is of type: " + table.getClass().getName());
            }
        }
        else
        {
            tableTxt = "NULL_TABLE";
        }
        if(clazz != null)
        {
            classTxt = clazz.getName();
        }
        result = "table: " + tableTxt + ", class: " + classTxt + 
                ", row: " + row + ", col: " + column + ", desc: " + description;
        return result;
    }

    public int getColumn()
    {
        return column;
    }

    public int getRow()
    {
        return row;
    }

    public boolean isTable()
    {
        return true;
    }

    public Object getTable()
    {
        return table;
    }

    public Class getClazz()
    {
        return clazz;
    }

    public void setRow(int row)
    {
        if(row < 0)
        {
            if(row == -2)
            {
                //Will be set to -2 when the block does not yet exist
            }
//            else if(row == -1)
//            {
//                //
//            }
            else
            {
                //IdEnum.newTable() does this (to Utils.UNSET_INT), so not an error
                //Err.error( "tableIdEnum.row being set to " + row);
            }
        }
        this.row = row;
    }

    public void setColumn(int col)
    {
        this.column = col;
    }

    public void setTable(Object table)
    {
        this.table = table;
    }

    public void setClazz(Class clazz)
    {
        this.clazz = clazz;
        /*
        times++;
        Err.pr( "Have set class to " + clazz + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
    }

    public Object getControl()
    {
        //To get round some checking code that is intended only for fields
        return NOTHING_OBJECT;
    }
}
