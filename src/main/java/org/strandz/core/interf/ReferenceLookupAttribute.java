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

import org.strandz.lgpl.widgets.ObjComp;
import org.strandz.lgpl.widgets.IconEnum;

/**
 * This Attribute class can be thought of as representing the 'foreign key'
 * column of the 'looking up' table in a Lookup relationship set up.
 * <p/>
 * A Lookup relationship implemented with a collection is not supported.
 * (And may be so rare that it will never be supported).
 * <p/>
 * A hidden Attribute that contains effectively the fk lookup object. So if
 * a LineItem looked up a Product, then the hidden component in this Attribute
 * would contain the Product. Thus for example if you INSERT, enter some values
 * into the visible LineItem and Product Attributes, and then POST, the hidden
 * control will have been filled with the newly created Product. This is just
 * a convenient way of getting at what is in the DO List.
 * <p/>
 * [
 * Thus it could be
 * argued that ReferenceLookupAttribute does not need to have an 'Item'. A
 * refactoring that was later reversed showed that if take away 'Item' all our
 * unit test except those that use 'Item' methods (eg. TestRoster.testCopyPaste())
 * will still pass.
 * If we have done this for fks for lookups, then we should probably also do it
 * for fks for master/details, thus ReferenceLinkAttribute should also be
 * a NonVisualAttribute. ANSWER: This extending from NonVisualAttribute is
 * actually a where a problem lies. ReferenceLinkAttribute is fine - it has
 * no item. TODO - put this directly below RuntimeAttribute (or maybe even below
 * LinkAttribute). Not urgent as all of these working fine. (The problem is that
 * NonVisualAttribute actually has an internal Comp which is not used by this
 * class at all - actually getItem() is called - we effectively have a null
 * component, of type Comp)
 * ]
 *
 * @author Chris Murphy
 */
public class ReferenceLookupAttribute extends RuntimeAttribute
{
    ObjComp comp = new ObjComp();
    private static final transient IconEnum REF_LOOKUP_ATTRIBUTE_ICON = IconEnum.LOOKUP_LINK;

    public ReferenceLookupAttribute()
    {
        super();
    }

    public ReferenceLookupAttribute(Attribute attrib)
    {
        super(attrib);
    }
    
    public Object getItem()
    {
        return comp;
    }
    
    public String toString()
    {
        String res = new String();
        res = res + "[ReferenceLookupAttribute dataFieldName: <" + getDOField()
            + ">, " + "cell: <" + cell + ">, " + "name: <" + name + ">, "
            + "value: <" + getItemValue() + ">" + "]";
        return res;
    }

//S/be able to rely on a base class to do this
//  public Object getItemValue()
//  {
//    return comp.getTxt(); //same as returning null!
//  }

    public IconEnum getIconEnum()
    {
        return REF_LOOKUP_ATTRIBUTE_ICON;
    }
}
