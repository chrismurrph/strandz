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
package org.strandz.core.widgets;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.widgets.EditableObjComp;
import org.strandz.lgpl.widgets.NonVisualComp;

import java.util.HashMap;

/**
 * Dummy component for non applichousing table attributes.
 * Also used as a replacement for a table.
 */
public class TableComp implements NonVisualComp
{
    /**
     * This model is the proper table's model so don't use it for getting values
     * when this table is used for a non-applichousing attribute
     */
    private CompTableModelI model;
    /**
     * Just one row of controls kept that can have enabled turned on or off
     */
    private HashMap<Integer, EditableObjComp> controls = new HashMap<Integer, EditableObjComp>();
    private static int times;
    static int constructedTimes;
    int id;
    
    public TableComp()
    {
        id = ++constructedTimes;
        if(id == 0)
        {
            Err.stack();
        }
        /*
        times++;
        Err.pr( "creating Comp times " + times);
        if(times == 1) //ControlSignatures does one
        {
            Err.stack();
        }
        */
    }
    
    public CompTableModelI getModel()
    {
        return model;
    }

    public void setModel(CompTableModelI newModel)
    {
        model = newModel;
    }

    public Object getText(int i)
    {
        return model.getValueAt(i, 0);
    }

//  public List getList()
//  {
//    return model.getList();
//  }
//  public void setList( List list)
//  {
//    model.setList( list);
//  }

    public void setText(int i, Object txt)
    {
        model.setValueAt(txt, i, 0);
//        times++;
//        Err.pr( "COMP setText() - <" + txt + "> times " + times );
//        if(txt != null && txt.equals( "VolunteerPanel"))
//        {
//          Err.stack();
//        }
    }

    public String getName()
    {
        String result = null;
        if(model != null)
        {
            result = model.getId();
        }
        if(result == null)
        {
            result = "A TableComp (with no model), ID: " + id;
        }
        return result;
    }

    public EditableObjComp getControlAt(int row, int col)
    {
        return getEditableObjCompAt( col);
    }

    public EditableObjComp getControlAt(int col)
    {
        return getEditableObjCompAt( col);
    }
    
    private EditableObjComp getEditableObjCompAt( Integer column)
    {
        EditableObjComp result = controls.get( column);
        if(result == null)
        {
            controls.put( column, new EditableObjComp());
            result = controls.get( column);
        }
        return result;
    }
}
