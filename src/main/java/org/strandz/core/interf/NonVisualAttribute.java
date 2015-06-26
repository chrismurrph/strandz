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

import org.strandz.core.domain.ItemAdapter;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.widgets.IconEnum;

/**
 * This class is called 'non applichousing' because it is not connected to any component
 * on the screen. Instead it is connected to a little dummy component that it
 * keeps internally. This type of attribute can be used when setting up unit tests
 * where there is no need for a screen. Also for setting calculated but invisible
 * DO fields.
 *
 * @author Chris Murphy
 */
abstract public class NonVisualAttribute extends RuntimeAttribute
{
    private static final transient IconEnum SPECIAL_ATTRIBUTE_ICON = IconEnum.BLACK_CIRCLE;

    public IconEnum getIconEnum()
    {
        return SPECIAL_ATTRIBUTE_ICON;
    }

    public NonVisualAttribute()
    {
        super();
    }

    public NonVisualAttribute(String dataFieldName)
    {
        super(dataFieldName, new Integer(0));
    }

    public NonVisualAttribute(Attribute attrib)
    {
        super(attrib);
    }
    
    void toRuntime(ItemAdapter itemAdapter)
    {
        if(!isEnabled())
        {
            Err.error("Need to code to enable/unenable " + this);
        }
    }

    //No need - up hierarchy does, and uses item that the subclass of this class uses
    //public abstract Object getItemValue();

    public String toString()
    {
        String res = new String();
        res = res + "[NonVisualAttribute dataFieldName: <" + getDOField() + ">, "
            + "cell: <" + cell + ">, " + "name: <" + name + ">, " + "value: <"
            + getItemValue() + ">" + "]";
        return res;
    }
}
