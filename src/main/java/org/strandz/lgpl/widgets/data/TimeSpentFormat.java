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
package org.strandz.lgpl.widgets.data;

import org.strandz.lgpl.data.objects.TimeSpent;
import org.strandz.lgpl.util.Err;

import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class TimeSpentFormat extends Format
{
    NumberFormat nf;

    public Object parseObject(String source, ParsePosition pos)
    {
        TimeSpent result = null;
        Err.pr("parse pos: " + pos);
        result = TimeSpent.newInstance(source);
        if(result != null)
        {
            pos.setIndex(source.length());
        }
        Err.pr("Returning " + result);
        return result;
    }

    public StringBuffer format(
        Object obj, StringBuffer toAppendTo, FieldPosition pos)
    {
        StringBuffer result = TimeSpent.format(obj);
        Err.pr("Asking to format at " + pos);
        return result;
    }
}
