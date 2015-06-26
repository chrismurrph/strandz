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

import org.strandz.core.domain.ControlSignatures;
import org.strandz.core.domain.DOAdapter;
import org.strandz.core.domain.FieldItemAdapter;
import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.widgets.IconEnum;

import javax.swing.JComponent;

/**
 * This class maps an Item Field (note that this is not the same thing
 * as a DO Field) to a DO Field.
 *
 * @author Chris Murphy
 */
public class FieldAttribute extends VisualAttribute
{
    private static final transient IconEnum FIELD_ATTRIBUTE_ICON = IconEnum.GREEN_CIRCLE;

    private Object component;
    private static int times = 0;

    public IconEnum getIconEnum()
    {
        IconEnum result = FIELD_ATTRIBUTE_ICON;
        // if(isNoLongerMatching())
        // {
        // result = unmatched;
        // }
        return result;
    }

    // XMLEncode
    public FieldAttribute()
    {
        super();
    }

    public FieldAttribute(StemAttribute attrib)
    {
        super(attrib);
        /*
        Will both error for StemAttribute:
        public Object getFieldRepresentation()
        public Object getControlValue()
        */
    }

    public FieldAttribute(String dataFieldName,
                          Object component)
    {
        super(dataFieldName, new Integer(0));
        if(component == null)
        {
            Err.error(
                "FieldAttribute <" + dataFieldName
                    + "> is not associated with an instantiated Control");
        }
        setItem(component);
    }

    /**
     * Convenience constructor
     */
    public FieldAttribute(Object component,
                          String dataFieldName)
    {
        this(dataFieldName, component);
    }
    
    public ItemAdapter createItemAdapter( DOAdapter doAdapter)
    {
        //ItemAdapter result = null;
        ItemAdapter result = new FieldItemAdapter(
            getItem(), 
            getCell(),
            isAlwaysEnabled(), 
            getName(),
            isUpdate(),
            getItemValidationTrigger(),
            getItemChangeTrigger(),
            getCell().getNode().getErrorThrowerI(),
            getOrdinal().intValue(), 
            doAdapter,
            this, 
            null,
            isEnabled(),
            isReadExternally());
        return result;
    }

     /**/
    public boolean equals(Object o)
    {
        // pUtils.chkType( o, BeanI.class);

        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof FieldAttribute))
        {
            result = false;
        }
        else
        {
            result = false;

            FieldAttribute test = (FieldAttribute) o;
            if(component.getClass().equals(test.component.getClass())
                && getDOField().equals(test.getDOField()))
            {
                result = true;
            }
            else
            {/*
         Err.pr( "component comparison failed: " + component.getClass() +
         " with " + test.component.getClass());
         Err.pr( "\tOR dataFieldName comparison failed: " + dataFieldName +
         " with " + test.dataFieldName);
         */}
        }
        return result;
    }

    public boolean validateBean()
    {
        boolean ok = super.validateBean();
        if(ok) // ok
        {
            if(getItem() == null)
            {
                ok = false;
                validateBeanMsg.add(
                    "Attribute " + getName() + " in cell " + getCell()
                        + " does not have" + " a applichousing field representation (aka control)");
            }
        }
        return ok;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + component.hashCode();
        result = 37 * result + getDOField().hashCode();
        return result;
    }

    public Object getItem()
    {
        return component;
    }
    
    public void setItem(Object component)
    {
        /*
        if(component instanceof JLabel)
        {
        times++;
        Err.pr( "### Setting FieldAttribute " + this + ", [" +
        component.hashCode() + "] 's control to " +
        ((Component)component).getName() + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        }
        */
        ControlSignatures.checkRecognisableControl(IdEnum.newField(component));
        this.component = component;
    }

    /*
    public Object getControl()
    {
    //Err.pr( "\tGetting Field's control " + component);
    return component;
    }
    */

    public String toString()
    {
        Object field = getItem();
        String compStr = "NULL COMPONENT"; // real problem!
        if(field != null)
        {
            compStr = field.getClass().getName();
        }
        if(field instanceof JComponent)
        {
            compStr += ", " + ((JComponent) field).getName();
        }

        String res = new String();
        res = res + "[FieldAttribute dOField: <" + getDOField() + ">, " + "cell: <"
            + getCell() + ">, " + "name: <" + getName() + ">, " + "item: <"
            + compStr + ">]";
        return res;
    }
}
