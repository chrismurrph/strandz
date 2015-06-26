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
package org.strandz.data.fishbowl.objects;

import org.strandz.lgpl.util.Utils;

import java.io.Serializable;

public class StreetAddress implements Serializable
{
    private String street;
    private String suburb;
    private String state;
    private String postcode;

    public StreetAddress()
    {
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof StreetAddress))
        {
            result = false;
        }
        else
        {
            result = false;

            StreetAddress test = (StreetAddress) o;
            if((street == null
                ? test.getStreet() == null
                : street.equals(test.getStreet()))
                && (suburb == null
                ? test.getSuburb() == null
                : suburb.equals(test.getSuburb()))
                && (state == null
                ? test.getState() == null
                : state.equals(test.getState()))
                && (postcode == null
                ? test.getPostcode() == null
                : postcode.equals(test.getPostcode()))
                )
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (street == null ? 0 : street.hashCode());
        result = 37 * result + (suburb == null ? 0 : suburb.hashCode());
        result = 37 * result + (state == null ? 0 : state.hashCode());
        result = 37 * result + (postcode == null ? 0 : postcode.hashCode());
        return result;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getSuburb()
    {
        return suburb;
    }

    public void setSuburb(String suburb)
    {
        this.suburb = suburb;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getPostcode()
    {
        return postcode;
    }

    public void setPostcode(String postcode)
    {
        this.postcode = postcode;
    }
}
