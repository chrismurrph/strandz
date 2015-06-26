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
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.NonVisualComp;

import java.awt.Component;
import java.io.Serializable;

abstract public class IdEnum implements Serializable
{
    final String name;
    IdEnum id;

    IdEnum(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return name;
    }

    public static FieldIdEnum newField(Object control)
    {
        if(control == null)
        {
            Err.error( "Cannot create a FieldIdEnum with a null control");
        }
        FieldIdEnum result = new FieldIdEnum("PARAM_FIELD");
        result.id = FIELD;
        result.setControl(control);
        return result;
    }

    public static String getControlName(Object control)
    {
        String result = null;
        if(control instanceof Component)
        {
            result = ((Component) control).getName();
        }
        else if(control instanceof NonVisualComp)
        {
            result = ((NonVisualComp) control).getName();
        }
        else if(control instanceof TableIdEnum.NothingObject)
        {
            result = control.getClass().getName();
        }
        else
        {
            Err.error( "Wrong type of param: " + control.getClass().getName());
        }
        return result;
    }

//    public static TableIdEnum newTable(Object table, int column)
//    {
//        return newTable(table, column, String.class);
//    }

    public static TableIdEnum newTable(Object table, int column, Class clazz, String description)
    {
        return newTable(table, Utils.UNSET_INT, column, clazz, description);
    }

    /**
     *
     * @param table - the table control
     * @param row - the row number this value object will be representing
     * @param column - the column number this value object will be representing
     * @param clazz Represents the class of the column
     * @return TableIdEnum value object
     * TODO - description param not working very well
     */
    public static TableIdEnum newTable(Object table, int row, int column, Class clazz, String description)
    {
        //clazz is null for calculated
        //Err.pr( SdzNote.TABLE_BLANKING_POLICY, "IdEnum getting class " + clazz.getName());
        if(description == null)
        {
            description = "PARAM_TABLE";
        }
        TableIdEnum result = new TableIdEnum( description);
        result.id = TABLE;
        if(table == null)
        {
            Err.error("table cannot be null");
        }
        else if(table instanceof TableComp)
        {
            //nufin
        }
        else if(!(table instanceof Component))
        {
            Err.error(
                "What kind of table is not a component: " + table.getClass().getName());
        }
        result.setTable(table);
        result.setRow(row);
        result.setColumn(column);
        result.setClazz(clazz);
        return result;
    }

    public static final FieldIdEnum FIELD = new FieldIdEnum("FIELD");
    public static final TableIdEnum TABLE = new TableIdEnum("TABLE");

    public String getName()
    {
        return name;
    }

    // serialization
    private static int nextOrdinal = 0;
    private final int ordinal = nextOrdinal++;
    public static final IdEnum[] VALUES = {FIELD, TABLE};

    public int ordinalValue()
    {
        return ordinal;
    }

    abstract public boolean isTable();
    
    //TODO - why aren't these two on subclasses?

    // FIELD
    abstract public Object getControl();

    // TABLE
    abstract public int getColumn();

    abstract public Object getTable();

    abstract public Class getClazz();
    // BOTH
}
