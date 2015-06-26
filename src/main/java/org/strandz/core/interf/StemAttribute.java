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

import org.strandz.lgpl.widgets.IconEnum;

/**
 * Fitting in with the sometimes biological naming theme, a StemAttribute
 * is one that has the potential to become any one of its subclasses.
 * (This is not strictly true, but if you use the static methods in
 * Attribute that start with toXXX(), then you can think of it as true).
 * <p/>
 * At DT these attributes are created by reading the methods of a DO. When
 * these StemAttributes are mapped to controls (Items) then they turn into
 * other types of Attributes.
 * <p/>
 * It is possible that CIs, especially those constructed via XMLDecoding,
 * have <code>StemAttribute</code>s in them. They serve no function at RT,
 * however it is quite legitimate for them to be there.
 *
 * @author Chris Murphy
 */
public class StemAttribute extends Attribute
{
    /**
     * A sop to sdzdsgnr, so theoretically sdzdsgnr.BeansTreeModel s/deal with
     * a wrapper around this Data Object
     */
    private boolean alreadyBeenCustomized = false;
    
    private static final transient IconEnum STEM_ATTRIBUTE_ICON = IconEnum.RED_CIRCLE;
    // public static final String NO_FIELD_NAME_STR = "NO FIELD NAME";
    public static final StemAttribute NO_NAME_ATTR = new StemAttribute(/* NO_FIELD_NAME_STR*/);

    public IconEnum getIconEnum()
    {
        return STEM_ATTRIBUTE_ICON;
    }

    public StemAttribute()
    {
        super();
    }

    public StemAttribute(Attribute attrib)
    {
        super(attrib);
    }
    
    public StemAttribute(String dataFieldName)
    {
        super(dataFieldName, new Integer( -1));
    }

    public StemAttribute(String dataFieldName, Integer ordinal)
    {
        super(dataFieldName, ordinal);
    }

    public int hashCode()
    {
        int result = 18;
        if(getName() != null)
        {
            result = 38 * result + getName().hashCode();
        }
        if(getDOField() != null)
        {
            result = 38 * result + getDOField().hashCode();
        }
        return result;
    }

    public boolean equals(Object o)
    {
        // pUtils.chkType( o, this.getClass());

        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof StemAttribute))
        {
            result = false;
        }
        else
        {
            result = super.equals(o);
        }
        return result;
    }

     /**/

    public boolean validateBean(boolean childrenToo)
    {
        // nufin. Used in MLD, and don't want DOField to be a requirement
        return true;
    }

    /*
    public Object getControlValue()
    {
    Err.error( "StemAttribute has no associated control: " + this);
    return null;
    }
    public void setControlValue(Object value)
    {
    Err.error( "StemAttribute has no associated control: " + this);
    }
    */
    public String toString()
    {
        String res = new String();
        res = res + "[StemAttribute dOField: <" + getDOField() + ">, " + "cell: <"
            + getCell() + ">, " + "name: <" + getName() + ">, " + "id: <" + id
            + ">]";
        return res;
    }

    public boolean isAlreadyBeenCustomized()
    {
        return alreadyBeenCustomized;
    }

    public void setAlreadyBeenCustomized(boolean alreadyBeenCustomized)
    {
        this.alreadyBeenCustomized = alreadyBeenCustomized;
    }
}
