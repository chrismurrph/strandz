package org.strandz.core.prod;

import org.strandz.core.prod.view.FieldObj;

import java.util.List;

/**
 * Because validation and writing to the real initialData occurs
 * as move away from a record, we need to ensure that the initialData
 * is not changed prematurely. Thus effects on initialData will
 * be the same whether or not using Table control. Tables automatically
 * change their models as move out of a cell. Our mechanism is to grab
 * a whole row at a time.
 * <p/>
 * This class represents the extent that contains the extra
 * row that is in the middle of being inserted.
 * <p/>
 * If there is no row that is currently being inserted then
 * userInsertingRowNum becomes void, or -99 here.
 * <p/>
 * When is -99 we can directly read the underlying extent.
 * <p/>
 * This class represents the data for the table model, where as tableObj
 * represents the same data for core.info. When tableObj needs to have the table
 * model data stuck into it then fillFlat() is called.
 */
public class TempCursorExtent
{
    // when is -99 means has just been refreshed and is up to date
    // private int userInsertingRowNum = -99; //first is 0
    // private Object newRow;
    private Block block;
    private FieldObj tableObj;
    private NodeTableModelImpl nodeTableModelImpl;
    private static int constructedTimes;
    int id;
    // private boolean masterIsAdding = false;

    /**
     * Using block.getDataRecords() rather than keeping a copy of the
     * Extent, because what the extent points to liable to change.
     */
    TempCursorExtent(NodeTableModelImpl nodeTableModelImpl,
                     Block block,
                     FieldObj tableObj)
    {
        this.nodeTableModelImpl = nodeTableModelImpl;
        this.block = block;
        this.tableObj = tableObj;
        constructedTimes++;
        id = constructedTimes;
    }

    private boolean isEmpty()
    {
        boolean result = block.dataRecordsEmpty();
        if(!result && nodeTableModelImpl.masterIsAdding())
        {
            result = true;
        }
        return result;
    }

    private Object get(int index)
    {
        return block.getDataRecord(index);
    }

    int size()
    {
        int result = block.dataRecordsSize();
        if(nodeTableModelImpl.masterIsAdding())
        {
            // Err.pr( "Size 0 b/c master adding");
            result = 0;
        }
        else
        {// Err.pr( "Size " + result + " b/c master NOT adding");
        }
        return result;
    }

    /**
     * Called when tableObj needs to have the values of a particular row.
     * Thus the table model data is rolled up and put into tableObj.
     */
    void fillFlat(int index, List flatColumns)
    {
        if(isEmpty())
        {// return false;
            // Err.error( "No getDataRecords to fill flat columns with");
            // Err.pr( "o \tNot doing fillFlat b/c empty");
        }
        else
        {
            Object obj = get(index);
            // Err.pr("MOB: fillFlat " + index + " gives: " + obj);
            // for(int i=0; i<=flatColumns.size()-1; i++)
            // {
            // ((TableItemAdapter)flatColumns.get( i)).setRecursivelyRefreshed( false);
            // }
            // Err.pr( "fillAdaptersFlatObjectsTable with " + obj.getClass().getName());
            // Err.pr( "flatColumns will pass " + flatColumns);
            tableObj.fillAdaptersFlatObjectsTable(obj);
            // return true;
        }
    }
}
