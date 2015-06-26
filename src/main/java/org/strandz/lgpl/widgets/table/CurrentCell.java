package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.*;

/**
 * User: Chris
 * Date: 22/01/2009
 * Time: 11:45:51 PM
 */
class CurrentCell
{
    private ComponentTableView componentTableView;
    private JComponent lastEditableFocusedControl;
    private ColRow colRow = new ColRow( 0, 0);
    /**
     * If true then focusing has chosen which is the current cell.
     * When false the client will have to ask for the RowCol
     */
    private boolean on = true;

    CurrentCell( ComponentTableView componentTableView)
    {
        this.componentTableView = componentTableView;
    }

    JComponent getLastEditableFocusedControl()
    {
        JComponent result = null;
        if(on)
        {
            result = lastEditableFocusedControl;
        }
        else
        {
            Err.error( "---Not on");
        }
        return result;
    }

    ColRow getColRow()
    {
        return colRow;
    }

    String getControlName()
    {
        String result = null;
        if(on)
        {
            result = lastEditableFocusedControl.getName();
        }
        return result;
    }

    String getControlText()
    {
        String result = null;
        if(on)
        {
            result = ComponentUtils.getText( lastEditableFocusedControl);
        }
        return result;
    }

    String getControlClassName()
    {
        String result = null;
        if(on)
        {
            result = lastEditableFocusedControl.getClass().getName();
        }
        return result;
    }

    void setLastEditableFocusedControl( JComponent lastEditableFocusedControl,
                                        String reason)
    {
        this.lastEditableFocusedControl = lastEditableFocusedControl;
        Err.pr( SdzNote.CTV_CRUD, "---Again have a lastEditableFocusedControl because <" + reason + ">");
        on = true;
    }

    void setLastEditableFocusedControl( JComponent lastEditableFocusedControl,
                                        String reason,
                                        int row, int col)
    {
        this.lastEditableFocusedControl = lastEditableFocusedControl;
        Err.pr( SdzNote.CTV_CRUD, "---Have a lastEditableFocusedControl because <" + reason + "> on " +
            componentTableView.getName());
        on = true;
        colRow = new ColRow( col, row);
    }

    void newSetOfControls()
    {
        Err.pr( SdzNote.CTV_CRUD, "---Last lastEditableFocusedControl written over");
    }

    void setOn( boolean on)
    {
        this.on = on;
        Err.pr( SdzNote.CTV_CRUD, "---Set on to <" + on + "> for " + componentTableView.getName());
    }

    public boolean isOn()
    {
        return on;
    }
}
