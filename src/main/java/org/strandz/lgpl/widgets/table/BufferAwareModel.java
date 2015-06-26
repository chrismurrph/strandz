package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

/**
 * User: Chris
 * Date: 22/01/2009
 * Time: 4:31:32 PM
 */
class BufferAwareModel implements CTVModelI
{
    private boolean modelChanging;
    private String reason;
    private TableModelEvent evt;
    //boolean insert;
    int insertRow = Utils.UNSET_INT;
    private TableModel model;
    Object[] bufferRow;
    Object[] pendingInsert;

    BufferAwareModel( TableModel model)
    {
        this.model = model;
    }

    public int getRowCount()
    {
        int result = model.getRowCount();
        //Err.pr( "Type : <" + evt.getType() + "> when asking  getRowCount() <" + result +
        //    "> b/c <" + reason + ">");
        if(isModelChanging() && evt.getType() == 1)
        {
            //model.getRowCount() won't give ++ until after this INSERT user process is over
            result++;
        }
        return result;
    }

    public String toString()
    {
        return getClass().getName() + ", b/c: <" + reason +
            ">, type: <" + evt.getType() + ">, super: <" + model.toString() + ">";
    }

    public boolean isPendingInsert()
    {
        return pendingInsert != null;
    }

    public int adjustRowFromTableForModel( int row)
    {
        return row;
    }

    public int adjustRowFromModelForTable( int row)
    {
        return row;
    }

    boolean isModelChanging()
    {
        return modelChanging;
    }

    boolean passThru()
    {
        return !isModelChanging() || evt.getType() == TableModelEvent.UPDATE || insertRow == Utils.UNSET_INT;
    }

    public Object getValueAt( int row, int col)
    {
        Object result;
        if(passThru())
        {
            result = model.getValueAt( row, col);
        }
        else
        {
            //Err.pr( "To cf row: " + row + " with insertRow: " + insertRow);
            if(row == insertRow)
            {
                result = bufferRow[ col];
            }
            else if(row > insertRow)
            {
                result = model.getValueAt( row-1, col);
            }
            else
            {
                result = model.getValueAt( row, col);
            }
        }
        return result;
    }

    public void modelChanging(String reason, TableModelEvent evt)
    {
        this.modelChanging = true;
        this.reason = reason;
        this.evt = evt;
        if(evt.getType() == TableModelEvent.INSERT && (evt.getFirstRow() == evt.getLastRow()))
        {
            Err.pr( SdzNote.CTV_CRUD, "Insert at " + evt.getFirstRow());
            insertRow = evt.getFirstRow();
            bufferRow = new Object[model.getColumnCount()];
        }
        else if(evt.getType() == TableModelEvent.DELETE)
        {
            Err.pr( "Not tested DELETE, but this will prolly be fine...");
        }
        else if(evt.getType() == TableModelEvent.UPDATE)
        {
            //ok
        }
        else
        {
            Err.error( "Not coded for " + this);
        }
    }

    public void modelChanged()
    {
        modelChanging = false;
        insertRow = Utils.UNSET_INT;
        pendingInsert = bufferRow;
        bufferRow = null;
        reason = null;
        evt = null;
    }
}
