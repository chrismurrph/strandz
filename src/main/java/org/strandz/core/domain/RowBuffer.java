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

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RowBuffer
{
    List record;
    List enableds;
    List classes;
    private String tableId;
    static int constructedTimes;
    int id;
    boolean debugging;
    private static int times;

    public RowBuffer(boolean debug, String tableId)
    {
        this.debugging = debug;
        id = ++constructedTimes;
        pr("Created RowBuffer ID: " + id);
        if(id == 0)
        {
            Err.stack();
        }
        this.tableId = tableId;
    }

    private void pr(String txt)
    {
        if(debugging)
        {
            Err.pr(txt);
        }
    }

    void setRow(Object row[])
    {
        record = new ArrayList(Arrays.asList(row));
        enableds = new ArrayList(row.length);
        for(int i = 0; i <= row.length - 1; i++)
        {
            enableds.add(Boolean.valueOf( true));
        }
        classes = new ArrayList(row.length);
        for(int i = 0; i <= row.length - 1; i++)
        {
            Object value = row[i];
            Class clazz = Object.class;
            if(value != null)
            {
                clazz = value.getClass();
            }
            classes.add(clazz);
        }
    }

    void setRow(List row)
    {
        record = row;
        enableds = new ArrayList(record.size());
        for(int i = 0; i <= record.size() - 1; i++)
        {
            enableds.add(Boolean.valueOf( true));
        }
        classes = new ArrayList(record.size());
        for(int i = 0; i <= record.size() - 1; i++)
        {
            Object value = record.get(i);
            Class clazz = Object.class;
            if(value != null)
            {
                clazz = value.getClass();
            }
            classes.add(clazz);
        }
    }

    Object get(int column)
    {
        Object result = record.get(column);
        // pr( "From buffer asking for " + column + " returning " + result);
        return result;
    }

    void set(Object aValue, int column)
    {
        if(debugging)
        {
            Print.pr("Setting buffer to " + aValue + " at " + column + " for ID " + id);
            if(aValue != null && aValue.equals("pControl V2.0"))
            {// Err.stack();
            }
        }
        if(record.size() <= column)
        {
            // This first required when ordinal actually used
            for(int i = record.size(); i < column; i++)
            {
                record.add(i, null);
                classes.add(i, null);
                // pr( "Put in a null at " + i);
            }
            //
            record.add(column, aValue);

            Class clazz = Object.class;
            if(aValue != null)
            {
                clazz = aValue.getClass();
            }
            classes.add(column, clazz);
        }
        else
        {
            record.set(column, aValue);

            Class clazz = (Class) classes.get(column);
            if(clazz == Object.class)
            {
                if(aValue != null)
                {
                    clazz = aValue.getClass();
                }
                classes.set(column, clazz);
            }
        }
    }

    boolean isEnabled(int column)
    {
        //needNewEnabled( column);
        if(column >= enableds.size())
        {
            Print.prList( enableds, "enableds", false);
            Print.prList( record, "record", false);
            Print.prList( classes, "classes", false);
            Err.error( "RowBuffer for <" + tableId + "> only has " + enableds.size() + " enableds, so can't isEnabled() at " + column);
        }
        Boolean result = (Boolean) enableds.get(column);
        Err.pr(SdzNote.ENABLEDNESS_REFACTORING,
            "RowBuffer.isEnabled() returning " + result + " for col " + column);
        return result.booleanValue();
    }

    private void needNewEnabled( int column)
    {
        if(enableds.size() <= column)
        {
            // This first required when ordinal actually used
            for(int i = enableds.size(); i < column; i++)
            {
                enableds.add(i, Boolean.FALSE);
                Err.pr(SdzNote.ENABLEDNESS_REFACTORING,
                    "--setEnabled in RowBuffer, put in a FALSE at " + i);
            }
            //
            enableds.add(column, null);
            record.add(column, null);
            classes.add(column, null);
            Err.pr(SdzNote.ENABLEDNESS_REFACTORING,
                "--setEnabled in RowBuffer, put final in at " + column);
        }
    }

    void setEnabled(boolean b, int column)
    {
        needNewEnabled( column);
        enableds.set(column, Boolean.valueOf( b));
        if(SdzNote.ENABLEDNESS_REFACTORING.isVisible())
        {
            times++;
            Err.pr("--setEnabled in RowBuffer " + id + " to " + b + " at " + column + " times " + times);
            if(times == 0)
            {
                Err.stack();
            }
        }
    }

    Class getClass(int column)
    {
        Class result = (Class) classes.get(column);
        return result;
    }
}
