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
import org.strandz.lgpl.widgets.IconEnum;

import javax.swing.Icon;

/**
 * This Attribute class represents an object reference. There are two
 * concrete subclasses
 *
 * @author Chris Murphy
 */
abstract public class LinkAttribute extends StemAttribute
{
    private static transient Icon specialAttributeIcon;
    /**
     * This is just a reference to the real thing that is
     * kept with Node. If we delete one of these we know
     * which Independent needs to be deleted. Also if we
     * change the name of the field then we can directly
     * update the dataFieldName in the Independent.
     */
    private Independent independent;

    public Icon getIcon()
    {
        Icon result = null;
        if(specialAttributeIcon == null)
        {
            specialAttributeIcon = IconEnum.BLACK_CIRCLE.getIcon();
        }
        result = specialAttributeIcon;
        return result;
    }

    public LinkAttribute()
    {
        super();
    }

    public LinkAttribute(Attribute attrib, Independent independent)
    {
        super(attrib);
        this.independent = independent;
    }

    public Independent getIndependent()
    {
        return independent;
    }

    public void setIndependent(Independent independent)
    {
        this.independent = independent;
    }
}
