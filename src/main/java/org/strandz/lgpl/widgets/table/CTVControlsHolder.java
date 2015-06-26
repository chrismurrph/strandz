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
package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.widgets.CellControlSignatures;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.JComponent;
import javax.swing.table.TableModel;
import java.util.Iterator;
import java.util.List;

public class CTVControlsHolder
{
    private ComponentTableView context;
    private ColRowListSet editableRowControls = new ColRowListSet();
    private ColRowListSet nonEditableControls = new ColRowListSet();

    private static final boolean DEBUG = false;
    private static int times;

    public CTVControlsHolder( ComponentTableView context)
    {
        this.context = context;
    }
    
    void clearControls()
    {
        editableRowControls.clear();
        nonEditableControls.clear();
        Err.pr( SdzNote.DYNAM_ATTRIBUTES, "Cleared controls from <" + context.getName() +
            ">, with ID <" + context.getId() + ">");
        if(context.getId() == 6)
        {
            //Err.stack();
        }
    }
        
    ComponentControlWrapper obtainWrapper( Object control, boolean chk)
    {
        ComponentControlWrapper result;
        result = editableRowControls.findWrapperByControl( control);
        if(result == null)
        {
            result = nonEditableControls.findWrapperByControl( control);
        }
        if(result == null)
        {
            /*
             * If control is from a table that has since been replaced then
             * we can use the fact that the name/position of the control will
             * still be the same.
             */
            result = editableRowControls.findWrapperByName( ComponentUtils._getName( control));
            if(result == null)
            {
                result = nonEditableControls.findWrapperByName( ComponentUtils._getName( control));
            }
        }
        if(chk && result == null)
        {
            Print.prList( editableRowControls.getListOfControls(), "This list does not have the requested control");
            Print.prList( nonEditableControls.getListOfControls(), "Nor does this one");
            Err.error( "Could not find a control == to " + control);
        }
        return result;
    }
        
    ComponentControlWrapper obtainWrapperFromControl( JComponent comp)
    {
        ComponentControlWrapper result = editableRowControls.findWrapperByControl( comp);
        if(result == null)
        {
            result = nonEditableControls.findWrapperByControl( comp);
        }
        if(result == null)
        {
            /* This happens a lot because the control that has been focused on is no
             * longer in ctvControls (an instance of this class), yet the user has focused
             * on it. There will be an equivalent new control inside a ComponentControlWrapper,
             * so if we really needed this to work we could make it. To repeat just set this to
             * error and turtle around.
             * CTV.AlwaysFocusListener is the culprit.
            Print.prList( editableRowControls.getListOfControls(), "This list does not have the requested control");
            Print.prList( nonEditableControls.getListOfControls(), "Nor does this one");
            Err.pr( "Could not find a ComponentControlWrapper for " + comp.getName() +
                ", ID: " + ((DebugTextField)comp).id);
            */
        }
        return result;
    }
    
    ComponentControlWrapper obtainWrapper( 
            int row, int col, boolean editable, boolean chk, int editableRow)
    {
        if(col == 1 && row == 0)
        {
            Err.pr( SdzNote.CTV_STRANGE_LOADING, "To obtain wrapper at col == 1 && row == 0");
        }
        ComponentControlWrapper result;
        List<ComponentControlWrapper> wrappers;
        if(editable && !context.readOnly)
        {
            wrappers = editableRowControls.getList();
            if(wrappers.isEmpty())
            {
                result = null;
            }
            else
            {
                result = editableRowControls.findWrapperByPosition( col, row);
            }
        }
        else
        {
            wrappers = nonEditableControls.getList();
            if(wrappers.isEmpty())
            {
                result = null;
            }
            else
            {
                result = nonEditableControls.findWrapperByPosition( col, row);
            }
        }
        if(chk && result == null)
        {
            Print.prList( wrappers, "Doesn't this list of controls have the right row/col?");
            Err.pr( "editable row: " + editableRow);
            Err.error( "Could not find an ?editable(T/F): <" + editable + 
                    "> control at row " + row + ", column " + col + " on " + context.getName() + ", id: " + context.getId());
        }
        return result;
    }

    void addToEditableRowControls( ComponentControlWrapper wrapper)
    {
        editableRowControls.add( wrapper);
    }

    void clearEditableRowControls()
    {
        editableRowControls.clear();
    }

    void addToNonEditableRowControls( ComponentControlWrapper wrapper)
    {
        nonEditableControls.add( wrapper);
        Err.pr( /*SdzNote.DYNAM_ATTRIBUTES*/DEBUG, "Added <" + wrapper + "> to <" + context.getName() +
            ">, with ID <" + context.getId() + ">");
        //Err.stack();
    }

    void removeFromNonEditableRowControls( ComponentControlWrapper wrapper)
    {
        nonEditableControls.remove( wrapper);
        pr( "Removed <" + wrapper + ">");
        //Err.stack();
    }

    private void pr( String txt)
    {
        if(DEBUG)
        {
            Err.pr( txt);
        }
    }

    void clearNonEditableRowControls()
    {
        nonEditableControls.clear();
        pr( "Removed ALL non-editable controls");
        pr( "");
    }

    List getListOfEditableRowControls()
    {
        return editableRowControls.getListOfControls();
    }

    List getListOfNonEditableRowControls()
    {
        return nonEditableControls.getListOfControls();
    }

    List<ComponentControlWrapper> getListOfEditableRowWrappers()
    {
        return editableRowControls.getList();
    }

    List<ComponentControlWrapper> getListOfNonEditableRowWrappers()
    {
        return editableRowControls.getList();
    }

    /**
     * Save what is on the screen to the model
     */
    void commitEditableRow( TableModel model, CellControlSignatures cellControlSignatures)
    {
        Object[] screenValues = new Object[editableRowControls.getList().size()];
        int i = 0;
        for (Iterator iterator = editableRowControls.getListOfControls().iterator(); iterator.hasNext(); i++)
        {
            Object obj = iterator.next();
            screenValues[i] = cellControlSignatures.getText( obj);
        }
        Print.prArray( screenValues, "values from screen in order");
        if(model == null)
        {
            //Err.error( "No model for " + getName() + " id " + id);
        }
        else
        {
            saveEditableRowToModel( model, screenValues);
        }
    }
    
    private void saveEditableRowToModel( TableModel model, Object[] screenValues)
    {
        Err.error( "This actually commits back to the screen!");
        int row = 0;
        for(int i = 0; i < screenValues.length; i++)
        {
            Object screenValue = screenValues[i];
            model.setValueAt( screenValue, row, i);
        }
    }
}
