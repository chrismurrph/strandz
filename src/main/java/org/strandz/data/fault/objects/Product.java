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
package org.strandz.data.fault.objects;

import org.strandz.lgpl.util.Utils;

public class Product
{
    private String name;
    private static int timesConstructed;
    public int id;

    public Product(String name)
    {
        this();
        this.name = name;
    }

    public Product()
    {
        timesConstructed++;
        id = timesConstructed;
        /*
        Err.pr( "FaultStatus ### CREATED id: " + id);
        if(id == 0)
        {
        Err.stack();
        }
        */
    }

    public String toString()
    {
        return name;
    }

    public int compareTo(Object obj)
    {
        return Utils.relativeRank(OPEN_VALUES, this, obj);
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Product))
        {// nufin
        }
        else
        {
            Product test = (Product) o;
            if((name == null ? test.name == null : name.equals(test.name)))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    public static final Product NULL = new Product();
    public static final Product P_CONTROL_V1 = new Product("pControl V1.0");
    public static final Product P_CONTROL_V2 = new Product("pControl V2.0");
    public static final Product[] OPEN_VALUES = {
        NULL, P_CONTROL_V1, P_CONTROL_V2};

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
