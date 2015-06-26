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
package org.strandz.lgpl.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Think of this class as keeping a 'table' of data, consisting
 * of many rows. You can ask for any row by its row number, and
 * when you ask for size you are getting the number of rows.
 * <p/>
 * The interesting thing about this class is that you populate
 * it in the first place with colums of data - each column is a
 * column of actual data values.
 *
 * @author Chris Murphy
 */
public class RowsAsColumns
{
    private List columns = new ArrayList();

    public void addColumn(List column)
    {
        columns.add(column);
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        int size = size();
        for(int i = 0; i < size; i++)
        {
            List list = (List) get(i);
            String row = getRow(list);
            result.append(row);
            result.append(FileUtils.DOCUMENT_SEPARATOR);
        }
        return result.toString();
    }

    private static String getRow(List list)
    {
        StringBuffer buf = new StringBuffer();
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            buf.append(obj.toString());
            buf.append(", ");
        }
        //Print.pr( buf.toString());
        return buf.toString();
    }

    public List get(int row)
    {
        List result = new ArrayList();
        for(Iterator iter = columns.iterator(); iter.hasNext();)
        {
            List element = (List) iter.next();
            if(row >= element.size())
            {
                if(element.isEmpty())
                {
                    Err.error("Have a zero length column - they s/all be the same size");
                }
                Err.error("row >= element.size()");
            }
            result.add(element.get(row));
        }
        return result;
    }

    public int size()
    {
        int result = 0;
        if(columns.size() > 0)
        {
            result = ((List) columns.get(0)).size();
        }
        return result;
    }

    /**
     * Needs to be square-shaped, so lets check that each column
     * is the same length.
     */
    public void chkValid()
    {
        int lastLength = -1;
        for(Iterator iter = columns.iterator(); iter.hasNext();)
        {
            List column = (List) iter.next();
            if(lastLength != -1 && column.size() != lastLength)
            {
                if(column.isEmpty())
                {
                    Err.error("One of the columns is empty");
                }
                Err.error("Not every column is the same size");
            }
            else
            {
                lastLength = column.size();
                //Err.pr("Set lastLength to " + lastLength);
            }
        }
    }
}
