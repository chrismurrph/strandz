package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.event.TableModelEvent;

/**
 * User: Chris
 * Date: 22/01/2009
 * Time: 6:23:08 PM
 */
public class SpacerAndBufferAwareModel implements CTVModelI
{
    private BufferAwareModel bufferAwareModel;
    /**
     * Before this row there will be some space. So if the row is 1:
     * 0 - go thru
     * 1 - blank row
     * 2 - what is actually at 1
     * 3 - what is actually at 2
     */
    private int spacerRow = Utils.UNSET_INT;

    SpacerAndBufferAwareModel( BufferAwareModel bufferAwareModel, int spacerRow)
    {
        this.bufferAwareModel = bufferAwareModel;
        this.spacerRow = spacerRow;
    }

    private boolean spacerSet()
    {
        return spacerRow != Utils.UNSET_INT;
    }

    public int getRowCount()
    {
        int result;
        result = bufferAwareModel.getRowCount();
        if(result != 0)
        {
            if(spacerSet())
            {
                result++;
            }
        }
        else
        {
            //If there are no rows then adding any kind of spacer makes no sense
        }
        return result;
    }

    public void modelChanged()
    {
        bufferAwareModel.modelChanged();
    }

    public void modelChanging(String reason, TableModelEvent evt)
    {
        bufferAwareModel.modelChanging( reason, evt);
    }

    /*
    boolean passThru()
    {
        return bufferAwareModel.passThru() && !spacerSet();
    }
    */

    public Object getValueAt(int row, int col)
    {
        Object result;
        if(! spacerSet())
        {
            result = bufferAwareModel.getValueAt( row, col);
        }
        else
        {
            if(row == spacerRow)
            {
                result = "";
            }
            else if(row > spacerRow)
            {
                result = bufferAwareModel.getValueAt( row-1, col);
            }
            else
            {
                result = bufferAwareModel.getValueAt( row, col);
            }
        }
        return result;
    }

    public int adjustRowFromTableForModel( int row)
    {
        int result = Utils.UNSET_INT;
        if(row == spacerRow)
        {
            //leave as UNSET_INT - and won't go out to renderer
        }
        else if(row > spacerRow)
        {
            result = row-1;
        }
        else
        {
            result = row;
        }
        return result;
    }

    public int adjustRowFromModelForTable( int row)
    {
        int result;
        if(bufferAwareModel.getRowCount() > 0 && row >= spacerRow)
        {
            result = row+1;
        }
        else
        {
            result = row;
        }
        return result;
    }

    public boolean isPendingInsert()
    {
        return bufferAwareModel.isPendingInsert();
    }
}
