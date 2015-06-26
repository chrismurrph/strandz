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

public class Fault
{
    private Product product;
    private String name;
    private FaultStatus faultStatus;
    private String text;
    private static int timesConstructed;
    public int id;

    public Fault()
    {
        timesConstructed++;
        id = timesConstructed;
        /*
        Err.pr( "Fault ### CREATED id: " + id);
        if(id == 0)
        {
        Err.stack();
        }
        */
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Fault))
        {// nufin
        }
        else
        {
            Fault test = (Fault) o;
            if((product == null ? test.product == null : product.equals(test.product)))
            {
                if((name == null ? test.name == null : name.equals(test.name)))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (product == null ? 0 : product.hashCode());
        result = 37 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public FaultStatus getFaultStatus()
    {
        return faultStatus;
    }

    public void setFaultStatus(FaultStatus faultStatus)
    {
        this.faultStatus = faultStatus;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public String toString()
    {
        return getName();
    }
}
