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
package org.strandz.test;

import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.TableIdEnum;
import org.strandz.core.domain.TableSignatures;
import org.strandz.lgpl.data.objects.MoneyAmount;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Utils;

import javax.swing.table.AbstractTableModel;
import java.util.Date;

public class PersonTableModel extends AbstractTableModel
{
    final String[] names = {
        "First Name", "Last Name", "Favorite Color", "Favorite Number", "Vegetarian", "Salary", "Started"};
    final Class[] classes = {
        String.class, String.class, String.class, Integer.class, Boolean.class, MoneyAmount.class, Date.class};
    final Object[][] data = {
        {"Mark", "Andrews", "Red", new Integer(2), Boolean.valueOf( true), MoneyAmount.NULL, new Date()},
        {"Tom", "Ball", "Blue", new Integer(99), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Alan", "Chung", "Green", new Integer(838), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Jeff", "Dinkins", "Turquois", new Integer(8), Boolean.valueOf( true), MoneyAmount.NULL, new Date()},
        {"Amy", "Fowler", "Yellow", new Integer(3), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Brian", "Gerhold", "Green", new Integer(0), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"James", "Gosling", "Pink", new Integer(21), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"David", "Karlton", "Red", new Integer(1), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Dave", "Kloba", "Yellow", new Integer(14), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Peter", "Korn", "Purple", new Integer(12), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Phil", "Milne", "Purple", new Integer(3), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Dave", "Moore", "Green", new Integer(88), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Hans", "Muller", "Maroon", new Integer(5), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Rick", "Levenson", "Blue", new Integer(2), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Tim", "Prinzing", "Blue", new Integer(22), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Chester", "Rose", "Black", new Integer(0), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Ray", "Ryan", "Gray", new Integer(77), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Georges", "Saab", "Red", new Integer(4), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Willie", "Walker", "Phthalo Blue", new Integer(4), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Kathy", "Walrath", "Blue", new Integer(8), Boolean.valueOf( false), MoneyAmount.NULL, new Date()},
        {"Arnaud", "Weber", "Green", new Integer(44), Boolean.valueOf( false), MoneyAmount.NULL, new Date()}
    };
    private Object table;
    //Made final to show never changed!
    final int current = 0;

    PersonTableModel()
    {
    }

    public void setTable(Object table)
    {
        this.table = table;
        //TableSignatures.setTableBuffer(table, data[0]);
    }

    public int getColumnCount()
    {
        return 
                names.length;
                //1;
    }

    public int getRowCount()
    {
        return data.length;
    }

    public String getColumnName(int column)
    {
        return names[column];
    }

    public Class getColumnClass(int col)
    {
        // return getValueAt(0,col).getClass();
        return classes[col];
    }

    public Object getValueAt(int row, int col)
    {
        Object result = data[row][col];
        if(row == current)
        {
            TableIdEnum id = IdEnum.newTable(table, row, col, getColumnClass(col), null);
            Assert.isFalse( id.getRow() == Utils.UNSET_INT);
            Assert.isFalse( id.getColumn() == Utils.UNSET_INT);
            result = TableSignatures.getText(id);
        }
        return result;
    }

    public void setValueAt(Object aValue, int row, int column)
    {
//checking makes no sense - current is never changed
//        if(row != current)
//        {
//            Err.error("Can only set current");
//        }

        TableIdEnum id = IdEnum.newTable(table, column, getColumnClass(column), null);
        TableSignatures.setText(id, aValue);
    }

    public boolean isCellEditable(int row, int column)
    {
        boolean result = true;
//checking makes no sense - current is never changed
//        if(row != current)
//        {
//            Err.pr( "row: " + row);
//            Err.pr( "current: " + current);
//            Err.error( "Should only be seeing if editable for current");
//        }

        TableIdEnum id = IdEnum.newTable(table, column, getColumnClass(column), null);
        result = TableSignatures.isEnabled(id);
        return result;
    }
}
