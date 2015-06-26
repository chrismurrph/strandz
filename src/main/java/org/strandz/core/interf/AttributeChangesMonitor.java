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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Useful for monitoring changes to attributes where Attribute.hasChanges(), which only works over the
 * commit cycle, is not granular enough. Only works for text fields currently but be should (TODO) make
 * it depend on the toolkit for that service.
 */
public class AttributeChangesMonitor
{
    private Map<Object, Record> map = new HashMap<Object, Record>();
    private AttributeFocusListener attributeFocusListener = new AttributeFocusListener();
    private Object focusedComponent;
    private RuntimeAttribute focusedAttribute;
    //private Object lastFocusedComponent;
    private CallbackI callback;
    private boolean enabled = true;
    private static int constructedTimes;
    private int id;

    public AttributeChangesMonitor(CallbackI callback)
    {
        this.callback = callback;
        constructedTimes++;
        id = constructedTimes;
    }

    public static interface CallbackI
    {
        public void changed( Object oldValue, Object newValue, RuntimeAttribute attribute);
    }

    private class Record
    {
        Object focusOnValue;
        Object focusOffValue;
        RuntimeAttribute attribute;

        void maybeTriggerChange()
        {
            //Err.pr( "begin: " + focusOnValue);
            //Err.pr( "end: " + focusOffValue);
            if(!focusOnValue.equals( focusOffValue))
            {
                callback.changed( focusOnValue, focusOffValue, attribute);
                focusOnValue = focusOffValue;
            }
        }
    }

    private class AttributeFocusListener implements FocusListener
    {
        public void focusGained(FocusEvent e)
        {
            if(enabled)
            {
                JTextField component = (JTextField)e.getSource();
                Record record = map.get( component);
                record.focusOnValue = component.getText();
                //lastFocusedComponent = focusedComponent;
                focusedComponent = component;
                focusedAttribute = record.attribute;
                //Err.pr( "Gained: " + component.getName());
            }
        }

        /**
         * Will work for tab changes too
         */
        public void focusLost(FocusEvent e)
        {
            if(enabled)
            {
                Object comp = null;
                /*
                if(lastFocusedComponent != null)
                {
                    comp = lastFocusedComponent;
                }
                else
                */
                if(focusedComponent != null)
                {
                    comp = focusedComponent;
                }
                if(comp != null)
                {
                    Record record = map.get( comp);
                    record.focusOffValue = ((JTextField)comp).getText();
                    Err.pr( SdzNote.DISABLE_ATTRIBUTE_CHANGES, "EVENT - focusLost");
                    record.maybeTriggerChange();
                }
                //Err.pr( "Lost: " + ((JTextField)comp).getName());
            }
        }
    }

    public Object getFocusedComponent()
    {
        return focusedComponent;
    }

    public RuntimeAttribute getFocusedAttribute()
    {
        return focusedAttribute;
    }

    public void addAttribute( RuntimeAttribute attribute)
    {
        JComponent comp = (JComponent)attribute.getItem();
        FocusListener[] focusListeners = comp.getFocusListeners();
        for (int i = 0; i < focusListeners.length; i++)
        {
            FocusListener focusListener = focusListeners[i];
            if(focusListener instanceof AttributeFocusListener)
            {
                comp.removeFocusListener( focusListener);
                //Will not happen if your monitor is as dynamic as the controls, but
                //will for other apps, for instance CarbonStartup that at time of writing
                //this did not have any dynamic properties
                //Err.pr( "Removed " + focusListener);
            }
        }
        comp.addFocusListener( attributeFocusListener);
        /* Memory leak debugging...
        Assert.isTrue( comp instanceof IdentifierI, "Does not implement IdentifierI: <" + comp.getClass().getName() + ">");
        Err.pr( "comp gets focus listener - name: <" + comp.getName() + ">, ID: " + ((IdentifierI)comp).getId());
        JComponent theParent = (JComponent)comp.getParent();
        Assert.isTrue( theParent instanceof IdentifierI, "Does not implement IdentifierI: <" + theParent.getClass().getName() + ">");
        Err.pr( "theParent - name: <" + theParent.getName() + ">, ID: " + ((IdentifierI)theParent).getId());
        */
        Record record = new Record();
        record.attribute = attribute;
        map.put( attribute.getItem(), record);
        //Err.pr( "AttributeChangesMonitor " + id + " now has " + map.size() + " attributes");
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}