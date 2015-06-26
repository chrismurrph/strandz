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
package org.strandz.core.info.impl.swing;

import mseries.ui.MDateEntryField;
import mseries.ui.MDateField;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;

import javax.swing.JFormattedTextField;
import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;

public class MDateEntryFieldMoreComplexMethods extends BasicMoreComplexMethods
{
    public void doRuntimeChanges(Object fieldComponent)
    {
        MDateEntryField mdef = (MDateEntryField) fieldComponent;
        mdef.setFocusLostBehavior(JFormattedTextField.COMMIT);
        mdef.setInputVerifier(null);
    }

    public Object getMethod(Object fieldComponent)
    {
        Object result = null;
        MDateEntryField mdef = (MDateEntryField) fieldComponent;
        try
        {
            result = mdef.getValue();
        }
        catch(ParseException e)
        {
            //LET null BE RETURNED
            Err.pr( "Not able to parse <" + mdef.getDisplay().getText() + ">");
        }
        return result;
    }

    public void setMethod(Object fieldComponent, Object arg)
    {
        MDateEntryField mdef = (MDateEntryField) fieldComponent;
        if((arg instanceof String) &&  "".equals( arg))
        {
            //Intention is for nothing to be written to this row - CTV spacer caused this
        }
        else if(arg == null)
        {
            mdef.setValue( (Date)arg);
            Assert.isTrue( arg == mdef.getDisplay().getValue());
        }
        else if(arg instanceof Date)
        {
            mdef.setValue( (Date)arg);
            Assert.isTrue( arg == mdef.getDisplay().getValue());
            try
            {
                Err.pr( SdzNote.BI_AI, "MDef, formatter to use: " + mdef.getDisplay().getFormatter().getClass().getName());
                Err.pr( SdzNote.BI_AI, "MDef, text will put into stringToValue() method <" + mdef.getDisplay().getText() + ">");
                mdef.getDisplay().commitEdit();
            }
            catch(ParseException e)
            {
                boolean isNull = mdef.getDisplay().getValue() == null;
                String txt = "In " + this.getClass().getName() + ", not able to parse " + mdef.getDisplay().getValue();
                if(isNull)
                {
                    txt += ", because it is null";
                }
                Err.error( txt);
            }
        }
    }

    public void requestFocusMethod(Object fieldComponent)
    {
        Err.pr(SdzNote.WHERE_AFTER_MESSAGE_DLG, "Now requesting on inner component");
        MDateEntryField mdef = (MDateEntryField) fieldComponent;
        MDateField display = mdef.getDisplay();
        boolean ok = display.requestFocusInWindow();
//Seems to work even thou is returning false
//    if(!ok)
//    {
//      Err.error( "Was not able to request focus in " + display);
//    }
    }

    public Color getBGColor(Object fieldComponent)
    {
        Color result = null;
        Component comp = (Component) fieldComponent;
        MDateEntryField mdef = (MDateEntryField) comp;
        result = ((Component) mdef.getDisplay()).getBackground();
        return result;
    }

    public void setBGColor(Object fieldComponent, Object color)
    {
        Component comp = (Component) fieldComponent;
        MDateEntryField mdef = (MDateEntryField) comp;
        mdef.getDisplay().setBackground((Color) color);
    }

    public void blankComponent(Object fieldComponent)
    {
        super.blankComponent(fieldComponent);
    }

    public void setComponent(Object fieldComponent, Object obj)
    {
        super.setComponent(fieldComponent, obj);
    }

    public void uninstallListeners(Object fieldComponent, Method removeActionListenerMethod)
    {
        super.uninstallListeners(fieldComponent, removeActionListenerMethod);
    }

    public void installClickListener(Object fieldComponent, Object itemAdapter)
    {
        Component comp = (Component) fieldComponent;
        Assert.notNull( comp.getName(), "Needs a name, type is " + comp.getClass().getName() + 
                ", and is in " + comp.getParent());
        if(comp.getName().indexOf("mdef") != -1)
        {
            Err.pr(SdzNote.MDATE_ENTRY_FIELD, "Installing a focus listener for " + comp.getName());
        }
        ClickListener adapter = createClickListener(comp, itemAdapter);
        MDateEntryField mdef = (MDateEntryField) comp;
        mdef.getDisplay().addMouseListener( adapter);
        comp.addMouseListener(adapter);
    }

    void removeClickListeners(Component comp)
    {
        super.removeClickListeners(comp);
    }

    public void installRClickRestore(Object fieldComponent, Object itemAdapter)
    {
        super.installRClickRestore(fieldComponent, itemAdapter);
    }

    public void uninstallRClickRestore(Object fieldComponent)
    {
        super.uninstallRClickRestore(fieldComponent);
    }
}
