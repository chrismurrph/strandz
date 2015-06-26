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
package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Assert;

public class ThousandsDollars implements Comparable
{
    public Integer value;
    public static final ThousandsDollars ZERO = new ThousandsDollars( 0);

    public ThousandsDollars(Integer value)
    {
        this.value = value;
    }
    
    public ThousandsDollars(String strValue)
    {
        this.value = new Integer( strValue);
        Assert.notNull( this.value);
    }
    
    public boolean equals( Object object)
    {
        ThousandsDollars otherThousandsDollars = (ThousandsDollars)object;
        boolean result = false;
        if(otherThousandsDollars != null)
        {
            result = this.value.intValue() == otherThousandsDollars.value.intValue();
        }
        return result;
    }    
        
    public boolean greaterThanOrEqual( ThousandsDollars ThousandsDollars)
    {
        boolean result = this.value.intValue() >= ThousandsDollars.value.intValue();
        return result;
    }
    
    public boolean greaterThan( ThousandsDollars ThousandsDollars)
    {
        boolean result = this.value.intValue() > ThousandsDollars.value.intValue();
        return result;
    }
    
    public String toString()
    {
        return value.toString();
    }
    
    public int intValue()
    {
        return value.intValue();
    }
    
    public int compareTo( Object o)
    {
        return this.value.compareTo( ((ThousandsDollars)o).value);
    }
}