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
package org.strandz.core.interf;

import org.strandz.core.domain.Independent;
import org.strandz.lgpl.util.Utils;

/**
 * This class represents a field in a DO that is actually a collection of
 * other DOs.
 *
 * @author Chris Murphy
 */
public class CollectionLinkAttribute extends LinkAttribute
{
    // XMLEncode
    public CollectionLinkAttribute()
    {
    }

    public CollectionLinkAttribute(Attribute attrib,
                                   Independent independent)
    {
        super(attrib, independent);
    }

    /*
     * Not sure why this was commented out earlier - yet equivalent hashCode method
     * was not
     */
    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = true;
        if (o == this)
        {
            result = true;
        }
        else if (!(o instanceof CollectionLinkAttribute))
        {
            result = false;
        }
        else
        {
            result = super.equals(o);
            if (result)
            {
                CollectionLinkAttribute test = (CollectionLinkAttribute) o;
                if ((getIndependent() == null
                    ? test.getIndependent() == null
                    : getIndependent().equals(test.getIndependent())))
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
            }
        }
        return result;
    }

    /**/

    public int hashCode()
    {
        int result = super.hashCode();
        if(getIndependent() != null)
        {
            result = 37 * result + getIndependent().hashCode();
        }
        return result;
    }

    public void set(CollectionLinkAttribute attribute)
    {
        super.set(attribute);
        setIndependent(attribute.getIndependent());
    }

    public String toString()
    {
        String res = new String();
        res = res + "[CollectionLinkAttribute dOField: <" + getDOField() + ">, "
            + "cell: <" + getCell() + ">, " + "name: <" + getName() + ">, "
            + "independent: <" + getIndependent() + "> ]";
        return res;
    }
}
