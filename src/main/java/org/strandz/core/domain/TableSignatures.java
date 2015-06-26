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

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.core.widgets.TableComp;

import javax.swing.*;
import java.util.List;
import java.awt.*;

/**
 * This is a bit of a hack class until we finally get rid of using JTable
 * at all with Strandz. NewTableSignatures will become this class.
 */
public class TableSignatures
{

    private static int times;
    
    public static boolean useNew(Object tableControl)
    {
        boolean result = false;
        if(Utils.instanceOf( tableControl, ComponentTableView.class))
        {
            result = true;
        }
        else if(Utils.instanceOf( tableControl, TableComp.class))
        {
            result = true;
        }
        else
        {
            if(Utils.instanceOf( tableControl, TableIdEnum.class))
            {
                Object innerTableControl = ((TableIdEnum)tableControl).getTable();
                if(Utils.instanceOf( innerTableControl, ComponentTableView.class))
                {
                    result = true;
                }
                else if(Utils.instanceOf( innerTableControl, TableComp.class))
                {
                    result = true;
                }
                //Err.error( "useNew() should be passed a control, not a " + tableControl.getClass().getName());    
            }
        }
        if(!result)
        {
            if(!Utils.instanceOf( tableControl, TableIdEnum.class))
            {
                Err.pr( SdzNote.CTV_SDZ_INTEGRATION, 
                        "[1]Will not use NewTableSignatures for " + tableControl.getClass().getName());
            }
            else
            {
                Err.pr( SdzNote.CTV_SDZ_INTEGRATION, "[2]Will not use NewTableSignatures for " + 
                        ((TableIdEnum)tableControl).getTable().getClass().getName());
            }
        }
        return result;
    }

    public static boolean isTableControl(Class aClass) 
    {
        boolean result = NewTableSignatures.isTableControl( aClass);
        return result;
    }

    public static Object getText(TableIdEnum id) 
    {
        Object result = null;
        if(useNew( id))
        {
            result = NewTableSignatures.getText( id);
        }
        else
        {
            result = OldTableSignatures.getText( id);
        }
        return result;
    }
    
    public static Object getItem( TableIdEnum id) 
    {
        Object result = null;
        if(useNew( id))
        {
            result = NewTableSignatures.getItem( id);
        }
        else
        {
            Err.alarm( "Possible but quite difficult to implement getItem() with OldTableSignatures" +
                    " as would need a component per cell");
        }
        return result;
    }

    public static void setText(TableIdEnum id, Object aValue) 
    {
        if(SdzNote.TABLE_MODEL_CONFUSED.isVisible())
        {
            Err.pr( "setValue of <" + id + "> to <" + aValue + ">");
            if(id.getColumn() == 0)
            {
                //Err.stack();
            }
        }
        if(useNew( id))
        {
            NewTableSignatures.setText( id, aValue);
        }
        else
        {
            OldTableSignatures.setText( id, aValue);
        }
    }

    public static void setModel(Object tableControl, NodeTableMethods model) 
    {
        NewTableSignatures.setModel( tableControl, model);
    }

    public static void checkTableControlExists(Class aClass) throws UnknownControlException
    {
        NewTableSignatures.checkTableControlExists( aClass);
    }

    public static Class getTableModel(Object tableControl) {
        Class result = NewTableSignatures.getTableModel( tableControl);
        return result;
    }

    public static List getListFromColumnName(Object tableControl, String s) {
        List result = NewTableSignatures.getListFromColumnName( tableControl, s);
        return result;
    }

    public static void setListFromColumnName(Object tableControl, String s, Object value) 
    {
        NewTableSignatures.setListFromColumnName( tableControl, s, value);
    }

    public static Object getValueFromColumnName(JTable tableControl, String id) 
    {
        Object result = NewTableSignatures.getValueFromColumnName( tableControl, id);
        return result;
    }

    public static void setTableBuffer(Object tableControl, int i) 
    {
        if(useNew( tableControl))
        {
            //Do nufin until we get rid of this call altogether
            //Err.error( "Should not need to set a table buffer for a " + tableControl.getClass().getName());
        }
        else
        {
            OldTableSignatures.setTableBuffer( tableControl, i);
        }
    }

    public static void setTableBuffer(Object tableControl, Object[] objects) 
    {
        if(useNew( tableControl))
        {
            Err.error( "Should not need to set a table buffer for a " + tableControl.getClass().getName());
        }
        else
        {
            OldTableSignatures.setTableBuffer( tableControl, objects);
        }
    }

    public static boolean isEnabled(TableIdEnum id) 
    {
        boolean result;
        if(useNew( id))
        {
            result = NewTableSignatures.isEnabled( id);
        }
        else
        {
            result = OldTableSignatures.isEnabled( id);
        }
        return result;
    }

    public static void setTextBlank(TableIdEnum id) 
    {
        if(useNew( id))
        {
            NewTableSignatures.setTextBlank( id);
        }
        else
        {
            OldTableSignatures.setTextBlank( id);
        }
    }

    public static boolean isTextBlank(TableIdEnum id) 
    {
        boolean result;
        if(useNew( id))
        {
            result = NewTableSignatures.isTextBlank( id);
        }
        else
        {
            result = OldTableSignatures.isTextBlank( id);
        }
        return result;
    }

    public static void setEnabled(TableIdEnum id, boolean b) 
    {
        if(useNew( id))
        {
            NewTableSignatures.setEnabled( id, b);
        }
        else
        {
            OldTableSignatures.setEnabled( id, b);
        }
    }

    public static Object getText(IdEnum id) 
    {
        Object result;
        if(useNew( id))
        {
            result = NewTableSignatures.getText( id);
        }
        else
        {
            result = OldTableSignatures.getText( id);
        }
        return result;
    }

    public static void setText(IdEnum id, Object o) 
    {
        if(useNew( id))
        {
            NewTableSignatures.setText( id, o);
        }
        else
        {
            OldTableSignatures.setText( id, o);
        }
    }

    public static void setToolTipText(IdEnum id, Object o) 
    {
        if(useNew( id))
        {
            NewTableSignatures.setToolTipText( id, o);
        }
        else
        {
            OldTableSignatures.setToolTipText( id, o);
        }
    }
    
    public static boolean isTextBlank(IdEnum id) 
    {
        boolean result;
        if(useNew( id))
        {
            result = NewTableSignatures.isTextBlank( id);
        }
        else
        {
            result = OldTableSignatures.isTextBlank( id);
        }
        return result;
    }

    public static Object getBlankText(IdEnum id) 
    {
        Object result = NewTableSignatures.getBlankText( id);
        return result;
    }

    public static void setTextBlank(IdEnum id) 
    {
        if(useNew( id))
        {
            NewTableSignatures.setTextBlank( id);
        }
        else
        {
            OldTableSignatures.setTextBlank( id);
        }
    }

    public static void setLOV(IdEnum id, List items) 
    {
        NewTableSignatures.setLOV( id, items);
    }

    public static void repositionTo(IdEnum id) 
    {
        NewTableSignatures.repositionTo( id);
    }

    public static int getBlankingPolicy(IdEnum id) 
    {
        int result = NewTableSignatures.getBlankingPolicy( id);
        return result;
    }

    public static Color getDesignTimeColor(Class clazz) 
    {
        Color result = NewTableSignatures.getDesignTimeColor( clazz);
        return result;
    }

    public static boolean isEnabled(IdEnum id) 
    {
        boolean result;
        if(useNew( id))
        {
            result = NewTableSignatures.isEnabled( id);
        }
        else
        {
            result = OldTableSignatures.isEnabled( id);
        }
        return result;
    }

    public static void setEnabled(IdEnum id, boolean b) 
    {
        if(useNew( id))
        {
            times++;
            Err.pr( SdzNote.ENABLEDNESS_REFACTORING, "Setting table enabled to <" + b + "> on id: "
                    + id + " times " + times);
            if(times == 0)
            {
                Err.debug();
            }
            NewTableSignatures.setEnabled( id, b);
            boolean isEnabled = NewTableSignatures.isEnabled( id);
            if(isEnabled != b)
            {
                Err.error( "Setting enabled to " + b + " for <" + id + "> did not work, enabled is still at " + 
                        isEnabled + ", times " + times);
            }
        }
        else
        {
            OldTableSignatures.setEnabled( id, b);
        }
    }

    public static void setBGColor(IdEnum id, Object color) 
    {
        NewTableSignatures.setBGColor( id, color);
    }

    public static Color getBGColor(IdEnum id) 
    {
        Color result = NewTableSignatures.getBGColor( id);
        return result;
    }
}
