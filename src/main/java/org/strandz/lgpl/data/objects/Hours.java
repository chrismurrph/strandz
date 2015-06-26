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

public class Hours implements Comparable
{
    public Integer value;    
    public static final Hours ZERO = new Hours( 0);

    public Hours(Integer value)
    {
        this.value = value;
        Assert.notNull( this.value);
    }

    public Hours(String strValue)
    {
        this.value = new Integer( strValue);
        Assert.notNull( this.value);
    }
    
    public boolean equals( Object object)
    {
        Hours otherHours = (Hours)object;
        boolean result = false;
        if(otherHours != null)
        {
            result = this.value.intValue() == otherHours.value.intValue();
        }
        return result;
    }    
    
    public boolean greaterThanOrEqual( Hours hours)
    {
        boolean result = this.value.intValue() >= hours.value.intValue();
        return result;
    }
        
    public boolean greaterThan( Hours hours)
    {
        boolean result = this.value.intValue() > hours.value.intValue();
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
        return this.value.compareTo( ((Hours)o).value);
    }
}
