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
package org.strandz.core.domain;

import org.strandz.core.domain.event.ItemChangeTrigger;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.widgets.NonVisualComp;

import javax.swing.JComponent;
import java.awt.Component;

public class FieldItemAdapter extends ItemAdapter
{
    private Object component;
    private FieldItemAdapter outer;
    private static int times;

    public FieldItemAdapter(
        Object component,
        AbstractCell cell,
        boolean alwaysEnabled,
        String displayName,
        boolean update,
        ItemValidationTrigger itemValidationTrigger,
        ItemChangeTrigger itemChangeTrigger,
        ErrorThrowerI errorThrower,
        int ordinal,
        DOAdapter doAdapter,
        Object source,
        CalculationPlace calculationPlace,
        boolean enabled,
        boolean readExternally)
    {
        super(displayName, alwaysEnabled, update, itemValidationTrigger,
            itemChangeTrigger, errorThrower, ordinal, doAdapter, cell, source, calculationPlace, readExternally);
        IdEnum id = createIdEnum( component);
        setEnabled( enabled, id);
        if(component instanceof NonVisualComp)
        {
            if(SdzNote.NV_PASTE_NOT_WORKING.isVisible() && displayName != null && displayName.equals("weekInMonth Name"))
            {
                Err.pr(SdzNote.NV_PASTE_NOT_WORKING,
                    "$$ weekInMonth Name comp is of type " + component.getClass().getName()
                        + " itemAdapter ID: " + id);
            }
        }
        else
        {
            if(SdzNote.NV_PASTE_NOT_WORKING.isVisible() && ((JComponent) component).getName().equals("cbWeekInMonth"))
            {
                Err.pr(SdzNote.NV_PASTE_NOT_WORKING,
                    "$$ cbWeekInMonth comp is of type " + component.getClass().getName()
                        + " itemAdapter ID: " + id);
            }
        }
        if(component == null)
        { // Relaxed this to put ReferenceLookupAttribute on a different place on hierarchy
            // Err.error("component in an Adapter must be not null, for " + doAdapter.getDOFieldName() + " in cell " + cell);
        }
        else
        {
            /*
            if(!(component instanceof Comp))
            {
            Err.pr( "###, component " + ((Component)component).getName());
            }
            else
            {
            Err.pr( "###, component Comp for " + dataFieldName);
            }
            */
        }
        // this.setCell( cell);
        this.component = component;
        ControlSignatures.installListeners(id, this);
    }

    public Object getItemValue()
    {
        return getItemValue( null);
    }

    public Object getItemValue( IdEnum idEnum)
    {
        if(idEnum == null)
        {
            idEnum = createIdEnum();
            if(idEnum.getControl() == null)
            {
                Err.error( "Need a control, so don't pass in null as param");
            }
        }
        if(id == 34 && SdzNote.NV_PASTE_NOT_WORKING.isVisible())
        {
            times++;
            Err.pr(SdzNote.NV_PASTE_NOT_WORKING, "Doing FieldItemAdapter.getItemValue() for 34, times " + times);
            if(times == 1) //is 3 when use NV components
            {
                Err.debug();
            }
        }
        Object result = ControlSignatures.getText(idEnum);
        if(id == 34)
        {
            Err.pr(SdzNote.NV_PASTE_NOT_WORKING, "In FieldItemAdapter control value returning is " + result);
        }
        return result;
    }

    public void setItemValue(Object value)
    {
        setItemValue( value, null);
    }

    public void setItemValue(Object value, IdEnum idEnum)
    {
        super.setItemValue(value);
        if(idEnum == null)
        {
            idEnum = IdEnum.newField(component);
        }
        ControlSignatures.setText(idEnum, value);
        // DO NOT DO THIS. WHEN ACTING PROGRAMMATICALLY NO VALIDATION
        // TRIGGERS ARE TO GO OFF, BUT OTHER ONES WILL
        // fireItemValidateEvent();
        // Err.pr( "In FieldItemAdapter control value been set to " + value);
    }

    public void requestFocus()
    {
        IdEnum idEnum = IdEnum.newField(component);
        ControlSignatures.requestFocus( idEnum);
    }

    public void makeUntouchable(boolean b)
    {
        if(b)
        {
            // ((Component)component).removeFocusListener( myFocusAdapter);
            ControlSignatures.uninstallListeners(createIdEnum());
            Print.pr(
                "Focus Listener now removed from " + ((Component) component).getName());
        }
        else
        {
            // ((Component)component).addFocusListener( myFocusAdapter);
            ControlSignatures.installListeners(createIdEnum(), this);
            Print.pr(
                "Focus Listener now added back to " + ((Component) component).getName());
        }
    }

    public Object getItem()
    {
        return component;
    }

    public String toString()
    {
        String res = super.toString();
        if(component == null)
        {
            res = res + ", <NULL component>";// + Utils.SEPARATOR;
        }
        else
        {
            res = res + ", <" + component.getClass() + ">";// + Utils.SEPARATOR;
        }
        return res;
    }

    /*
    public void setAlwaysEnabled( boolean alwaysEnabled)
    {
    this.alwaysEnabled = alwaysEnabled;
    }
    */
    public IdEnum createIdEnum()
    {
        return IdEnum.newField(component);
    }

    /**
     * Both params ignored
     * @param row This param is ignored as a FieldItemAdapter always has only one row (unlike TableItemAdapter)
     * @return
     */
    public IdEnum createIdEnum( int row, String desc)
    {
        return createIdEnum();
    }
    public IdEnum createIdEnum( String desc)
    {
        return createIdEnum();
    }
    public IdEnum createIdEnum( Class clazz)
    {
        //Wrong assumption below, happens 
        //Err.error( "S/not need to call this version of createIdEnum() for a " + this.getClass().getName());
        return IdEnum.newField(component);
    }
    private IdEnum createIdEnum( Object component)
    {
        return IdEnum.newField(component);
    }
    public Object getTableControl()
    {
        return null;
    }
    /*
    void setInError( boolean b)
    {
    putPhysicallyInError( b, isInError());
    }
    public boolean isInError()
    {
    //If we are storing the original colour in here then we
    //are not in error
    //Err.pr( "isInError() has an error object: " + (notInErrorObject != null)
    //        + " for " + doFieldName);
    return (notInErrorObject != null);
    }
    public void confirmError()
    {
    //No need for a delayed effect
    }
    */
}
