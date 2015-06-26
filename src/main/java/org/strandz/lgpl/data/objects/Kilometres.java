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

public class Kilometres implements Comparable
{
    public Integer value;
    public static final Kilometres ZERO = new Kilometres( 0);

    public Kilometres(Integer value)
    {
        this.value = value;
    }
    
    public Kilometres(String strValue)
    {
        this.value = new Integer( strValue);
        Assert.notNull( this.value);
    }
    
    public boolean equals( Object object)
    {
        Kilometres otherKilometres = (Kilometres)object;
        boolean result = false;
        if(otherKilometres != null)
        {
            result = this.value.intValue() == otherKilometres.value.intValue();
        }
        return result;
    }    
        
    public boolean greaterThanOrEqual( Kilometres kilometres)
    {
        boolean result = this.value.intValue() >= kilometres.value.intValue();
        return result;
    }
    
    public boolean greaterThan( Kilometres kilometres)
    {
        boolean result = this.value.intValue() > kilometres.value.intValue();
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
        return this.value.compareTo( ((Kilometres)o).value);
    }
}
