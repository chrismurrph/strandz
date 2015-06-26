/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.JComboBox;
import java.awt.event.MouseMotionListener;

public class DebugComboBox extends JComboBox
{
    private static int times;
    private static int times1;
    private static int times2;
    private static boolean debugging = true;
    private static boolean debugGetSet = SdzNote.NV_PASTE_NOT_WORKING.isVisible();
    private static int constructedTimes;
    public int id;

    public DebugComboBox()
    {
        init();
    }

//    public DebugComboBox(ComboBoxModel aModel)
//    {
//        init();
//    }
//
//    public DebugComboBox(Object[] items)
//    {
//        init();
//    }
//
//    public DebugComboBox(Vector items)
//    {
//        init();
//    }

    private void init()
    {
        constructedTimes++;
        id = constructedTimes;
        pr("---DebugComboBox constructed, id: " + id);
    }

    public void addMouseMotionListener(MouseMotionListener l)
    {
        pr("addMouseMotionListener(), id: " + id + " " + l);
        super.addMouseMotionListener(l);
    }

    public void removeMouseMotionListener(MouseMotionListener l)
    {
        pr("removeMouseMotionListener(), id: " + id + " " + l);
        super.removeMouseMotionListener(l);
    }

    private static void pr(String txt)
    {
        if(debugging)
        {
            Err.pr(txt);
        }
    }

    protected void selectedItemChanged()
    {
        /*
        times++;
        pr( "---selectedItemChanged() " +
        ", id " + id + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        super.selectedItemChanged();
    }

    public Object getSelectedItem()
    {
        Object result = super.getSelectedItem();
        if(id == 1)
        {
            Err.pr(debugGetSet, "DebugComboBox: GETting txt " + result);
        }
        return result;
    }

    public void setSelectedItem(Object anObject)
    {
        if(id == 1)
        {
            times++;
            Err.pr(debugGetSet, "DebugComboBox: SETting txt " + anObject + " times " + times);
            if(times == 0)
            {
                Err.stack(debugGetSet);
            }
        }
        super.setSelectedItem(anObject);

        boolean bothNull = getSelectedItem() == null && anObject == null;
        boolean oneNull = false;
        if(!bothNull)
        {
            if(getSelectedItem() == null || anObject == null)
            {
                oneNull = true;
            }
        }
        if(!oneNull && (bothNull || anObject.equals(getSelectedItem())))
        {
            if(getSelectedItem() != anObject)
            {// This ok, as means that the value was already set to the same
            }
        }
        else
        {
            /*
            if(!isEditable())
            {
            setEditable( true);
            super.setSelectedItem( anObject);
            setEditable( false);
            }
            */
            if(!isEditable())
            {
                Err.pr( "Not editable might be reason for error");
            }
            if(getItemCount() == 0)
            {
                Err.pr( "No items might be reason for error");
            }
            Err.error( "setSelectedItem() did not work, could not be set to " + anObject
                + " so left at " + getSelectedItem());
        }
    }

    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        /*
        times++;
        pr( "SET ENABLED (DebugComboBox[" + getName() + "]): " + b + " times " + times);
        if(times == 21)
        {
        Err.stack();
        }
        */
    }
}
