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

import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.event.ItemChangeTrigger;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.List;

abstract public class AbstractTableItemAdapter extends ItemAdapter
{
    private Object storedFlatObject;
    private int column;
    private String columnHeading;
    private static int times;
    private static int constructedTimes;

    public AbstractTableItemAdapter(int column,
                            AbstractCell cell,
                            boolean alwaysEnabled,
                            String name,
                            String columnHeading,
                            boolean update,
                            ItemValidationTrigger itemValidationTrigger,
                            ItemChangeTrigger itemChangeTrigger,
                            ErrorThrowerI errorThrower,
                            int ordinal,
                            DOAdapter doAdapter,
                            Object source,
                            CalculationPlace calculationPlace,
                            boolean readExternally)
    {
        super(name, alwaysEnabled, update, itemValidationTrigger, itemChangeTrigger,
              errorThrower, ordinal, doAdapter, cell, source, calculationPlace, readExternally);
        Assert.isTrue(column >= -1, "Trying to set a column to " + column);
        this.column = column;
        this.columnHeading = columnHeading;
        /*
        Err.pr( "------------TableItemAdapter------------ id " + id );
        Err.pr( "dataFieldName: " + doAdapter.getDOFieldName() );
        Err.pr( "clazz: " + doAdapter.getClassType() );
        Err.pr( "cell: " + cell );
        Err.pr( "name: " + name );
        Err.pr( "column: " + column );
        Err.pr( "ordinal: " + ordinal );
        if(id == 0)
        {
            Err.stack();
        }
        */
    }
    
    /**
     * When created like this, will only be used for being a copy (including
     * the row) of the information about the cell was previously on, so that
     * can go back to that cell.
     */
    public AbstractTableItemAdapter(AbstractTableItemAdapter ta, int row, CalculationPlace calculationPlace)
    {
        super(ta.getName(), ta.isAlwaysEnabled(), ta.isUpdate(),
            ta.getItemValidationTrigger(), ta.getItemChangeTrigger(),
            ta.getErrorThrowerI(), ta.getOrdinal(),
            ta.getDoAdapter(), ta.getCell(), ta.getSource(), 
            calculationPlace, ta.isReadExternally());
        setEnabled( ta.isEnabled());
        this.column = ta.column;
        // whether inError should never be used
        // this.setInError( ta.isInError());
        this.isOrig = false;
        this.setOriginalAdapter(ta);
        this.setMoveBlock(ta.getMoveBlock());
        setRow(row);
        constructedTimes++;
        id = constructedTimes;
    }

    public String toString()
    {
        String res = super.toString();
        res = res + " <" + getDoAdapter().getParentClazz() + ">, row " + (getRow());
        return res;
    }

    public Object getItemValue()
    {
        Object result = ControlSignatures.getText(createIdEnum( toString()));
        if(debug && id == 2)
        {
            Err.pr("###GETTING FROM TABLE <" + result + "> ID: " + id + ", type " + result.getClass().getName());
        }
        return result;
    }

    public Object getItemValue( IdEnum idEnum)
    {
        return getItemValue();
    }

    public void setItemValue(Object value, IdEnum idEnum)
    {
        super.setItemValue(value);
        if(idEnum == null)
        {
            idEnum = createIdEnum( toString());
        }
        ControlSignatures.setText(idEnum, value);
        if(debug && id == 2)
        {
            Err.pr("###SETTING ON TABLE <" + value + "> ID: " + id + ", type " + value.getClass().getName());
        }
    }

    public void requestFocus()
    {
        Err.error( "Not yet implemented");
    }

    public void setItemValue(Object value)
    {
        setItemValue( value, null);
    }

    public String getItemName()
    {
        String result = super.getItemName();
        if(!(Utils.isBlank( columnHeading)))
        {
            result = columnHeading;
        }
        return result;
    }

    public Object getStoredFlatObject()
    {
        return storedFlatObject;
    }

    public void setStoredFlatObject(Object storedFlatObject)
    {
        // Err.pr( "@@ storedFlatObject is of class " +
        // storedFlatObject.getClass().getName() + " in " + this);
        // Err.pr( "@@ clazz is " + clazz.getName());
        // Err.pr( "@@ classType is " + getClassType().getName());
        this.storedFlatObject = storedFlatObject;
    }

    public int compareTo(Object o)
    {
        int result = 0;
        AbstractTableItemAdapter other = (AbstractTableItemAdapter) o;
        result = this.column - other.column;
        return result;
    }
    
    public Object getItem()
    {
        Object result;
        /*
         * Err.pr( "Strange that are asking for an item from an attribute (ItemAdapter) when don't know for which row!");
         * Note confusion here. OldTableSignatures thinks of an item as the actual table, whilst
         * with NewTableSignatures we can think of it as an actual control inside the table.
         */
        IdEnum idEnum = createIdEnum( toString());
        result = ControlSignatures.getItem( idEnum);
        return result;
    }

    public List getItemList()
    {
        if(getTableControl() == null)
        {
            Err.error("Cannot getControlList() when tableControl is null");
        }
        return TableSignatures.getListFromColumnName(getTableControl(), getItemName());
    }

    public void setItemList(Object value)
    {
        if(getTableControl() == null)
        {
            Err.error("Cannot setControlList() when tableControl is null");
        }
        TableSignatures.setListFromColumnName(getTableControl(), getItemName(),
            value);
    }

    /**
     * With a FieldItemAdapter we know from the return type of the get method of the
     * control, just what the type of the value held in the control is. With a table
     * perhaps it is easiest to do as we do here, and find out this type when we are
     * actually retrieving from the table itself.
     *
     * @param clazz The type of what was held in the renderer - what the table is giving to us
     * @return IdEnum value object
     */
    public IdEnum createIdEnum( Class clazz)
    {
        IdEnum result;
        int row = getCell().getNode().getRow() - 1;
        Object table = getTableControl();
        result = IdEnum.newTable(table, row, column, clazz, getItemName());
        return result;
    }
    
    public IdEnum createIdEnum( String desc)
    {
        return createIdEnum( Utils.UNSET_INT, desc);
    }
    
    public IdEnum createIdEnum( int row, String desc)
    {
        IdEnum result;

        Object table = getTableControl();
        Err.pr(SdzNote.WHICH_ROW_ON,
            "Why is row-1 used, and this used when in INSERT mode");
        /*
         * Is used when in insert mode as part of getting the values that have been
         * inserted. The reason we need to -1 is that the value of getCell().getNode().getRow()
         * is at say 2 when plainly on the table we are at row one.  
         */
        //Above good question, so removed - 1
        Assert.notNull( table, "Table should not be null in " + getCell().getNode());
        int cellRow = getCell().getNode().getRow();
        Err.pr(SdzNote.WHICH_ROW_ON, "cellRow is " + cellRow);
        if(row == Utils.UNSET_INT)
        {
            if(cellRow != -1)
            {
                row = cellRow;
            }
            else
            {
                //No block yet exists
                row = 0;
            }
        }
        result = IdEnum.newTable(table, row, column,
            //This was wrong, this class s/always be the type of the column, which is the way
            //FieldItemAdapter works.
            //getDoAdapter().getParentClazz().getClassObject()
            //TODO
            //We are defaulting to String, but later will need to deal with typed columns in tables
            String.class,
            desc
        );
        Assert.isTrue(result.getColumn() >= -1);
        return result;
    }

    public int getColumn()
    {
        return column;
    }

    public void makeUntouchable(boolean b)
    {
        // Will not need to implement anything unless have a focus listener
        Err.error("Not used anyway");
    }
}
