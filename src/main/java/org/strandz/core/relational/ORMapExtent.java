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
package org.strandz.core.relational;

import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.SetListener;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.StateChangeEvent;
import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.lgpl.extent.AddFailer;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ORMapExtent extends ArrayList
    implements SetListener, StateChangeTrigger, AddFailer
{
    private List statements = new ArrayList();
    private List setsBeforeAdd = new ArrayList();
    /**
     * Will be able to set this up before pkColumns and nonPkColumns,
     * but will be refering to the same actual columns
     */
    private List columns;
    private List pkColumns = new ArrayList();
    private List nonPkColumns = new ArrayList();
    /**
     * These two will come from a parse of setQuery. At this stage
     * will already have columns but will not have pkColumns or
     * nonPkColumns.
     */
    private String dbTableName;
    private StateEnum currentState;
    private String dupPKError;
    private static int times;
    private static int times1;

    public ORMapExtent(List adapters)
    {
        if(adapters.size() == 0)
        {
            Err.error("Cannot create an ORMapExtent with no adapters");
        }
        columns = new ArrayList();
        for(Iterator iter = adapters.iterator(); iter.hasNext();)
        {
            ItemAdapter itemAdapter = (ItemAdapter) iter.next();
            Column column = new Column(itemAdapter);
            itemAdapter.getDoAdapter().addSetListener(this);
            columns.add(column);
        }
    }

    public void coalesce()
    {
        List updateableColumnNames = new ArrayList();
        for(Iterator iter = nonPkColumns.iterator(); iter.hasNext();)
        {
            Column column = (Column) iter.next();
            updateableColumnNames.add(
                column.itemAdapter.getDoAdapter().getDOFieldName());
        }
        /*
        times1++;
        Print.pr( "BEFORE COALESCE");
        Print.pr( "--------------- times " + times1);
        Print.prList( statements, "BEFORE COALESCE");
        if(times1 == 2)
        {
          Err.debug();
        }
        */

        // Collections.sort( statements, Coalescer.timestampComparator);
        // pUtils.debugList( statements);
        Coalescer coalescer = new Coalescer(updateableColumnNames, statements);
        statements = coalescer.getCoalescedStatements();
        /*
        Print.pr( "AFTER COALESCE");
        Print.pr( "--------------");
        Print.prList( statements, "AFTER COALESCE");
        */
    }

    public void setDBTableName(String s)
    {
        this.dbTableName = s;
    }

    public void setPkColNames(List pkColNames)
    {
        pkColumns.clear();
        nonPkColumns.clear();
        // Err.pr( "Have " + columns.size());
        for(Iterator iterator = columns.iterator(); iterator.hasNext();)
        {
            Column column = (Column) iterator.next();
            boolean foundAsPk = false;
            for(Iterator iter = pkColNames.iterator(); iter.hasNext();)
            {
                String colName = (String) iter.next();
                if(colName.equals(column.itemAdapter.getDoAdapter().getDOFieldName()))
                {
                    column.setPk(true);
                    pkColumns.add(column);
                    foundAsPk = true;
                    break;
                }
            }
            if(!foundAsPk)
            {
                nonPkColumns.add(column);
            }
        }
    }

    public void stateChangePerformed(StateChangeEvent e)
    {
        currentState = e.getCurrentState();
    }

    public void setInitialState(StateEnum state)
    {
        currentState = state;
    }

    private boolean containsName(String colName, List pkColumns)
    {
        boolean result = false;
        for(Iterator iter = pkColumns.iterator(); iter.hasNext();)
        {
            Column col = (Column) iter.next();
            if(col.itemAdapter.getDoAdapter().getDOFieldName().equals(colName))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This set is logically an update statement where one
     * column is changed. Called for both what will become an
     * INSERT, and UPDATES. Way to differentiate is to know
     * what state currently in.
     */
    public void set(String colName, Object value, Object element)
    {
        times++;
        Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS,
            currentState + " set() being called for " + colName + " with <" + value
                + "> times " + times);
        Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS, "element: " + element);
        if(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS.isVisible() && times == 1)
        {
            Err.debug();
        }
        if(currentState.isEditing())
        {
            if(containsName(colName, pkColumns))
            {
                Err.error(
                    "Trying to update a PK column <" + colName + ">" + " in table "
                        + dbTableName);
            }

            Statement statement = new Statement(CRUDEnum.UPDATE);
            statement.setPKValues(extractValues(element, pkColumns, true));
            statement.setColName(colName);
            statement.setColValue(value);
            statements.add(statement);
        }
         /**/
        else if(currentState.isNew())
        {
            Statement statement = new Statement(CRUDEnum.SET_BEFORE_ADD);
            statement.setColName(colName);
            statement.setColValue(value);
            setsBeforeAdd.add(statement);
        }
        else
        {
            Err.error("Setting for reason: " + currentState);
        }
         /**/
    }

    /**
     * This set is logically an update statement where all
     * the columns are changed
     */
    public Object set(int index, Object element)
    {
        Err.error("Is NOT called");
        /*
        Object result = super.set( index, element);
        Statement statement = new Statement( CRUDEnum.UPDATE);
        statement.setPKValues( extractValues( element, pkColumns, true));
        statement.setNonPKValues( extractValues( element, nonPkColumns, false));
        statements.add( statement);
        return result;
        */
        return null;
    }

    /*
    public void set( String colName, Object value)
    {
    Err.error( "set( String, Object) not implemented YET by ORMapList");
    super.set( colName, value);
    }
    */

    public void add(int index, Object element)
    {
        //Print.pr( "add at index " + index);

        Statement statement = new Statement(CRUDEnum.INSERT);
        if(setsBeforeAdd.isEmpty())
        {// Nothing wrong with this, except might want to automatically
            // generate pks.
            // Err.error( "Expect to have setsBeforeAdd to add");
        }
        statement.setPKValues(extractSetsBeforeAdd(setsBeforeAdd, pkColumns, true));
        if(checkNotDupPK(statement))
        {
            super.add(index, element);
            statement.setNonPKValues(
                extractSetsBeforeAdd(setsBeforeAdd, nonPkColumns, false));
            statements.add(statement);
        }
        setsBeforeAdd.clear();
    }

    public String getDupPKError()
    {
        String result = dupPKError;
        return result;
    }

    public boolean add(Object o)
    {
        Err.error("add object is called!");
        /*
        boolean result = true;
        Statement statement = new Statement( CRUDEnum.INSERT);
        statement.setPKValues( extractValues( o, pkColumns, true));
        if(checkNotDupPK( statement))
        {
        statement.setNonPKValues( extractValues( o, nonPkColumns, false));
        statements.add( statement);
        result = false;
        }
        if(result)
        {
        result = super.add( o);
        }
        return result;
        */
        return false;
    }

    private boolean checkNotDupPK(Statement statement)
    {
        boolean result = true;
        coalesce();
        dupPKError = null;
        for(Iterator iter = statements.iterator(); iter.hasNext();)
        {
            Statement state = (Statement) iter.next();
            if(state.getPkValues().equals(statement.getPkValues()))
            {
                result = false;
                // Err.error();
                dupPKError = "Cannot Insert Duplicate PK into " + dbTableName;
                break;
            }
        }
        return result;
    }

    public boolean addAll(Collection c)
    {
        // Err.error( "addAll() not implemented by ORMapList");
        // return false;
        return super.addAll(c);
    }

    public boolean addAll(int index, Collection c)
    {
        Err.error("addAll() not implemented by ORMapList");
        return false;
    }

    public void clear()
    {
        Err.error("clear() not implemented YET by ORMapList");
        super.clear();
    }

    public Object remove(int index)
    {
        //Print.pr( "remove at index " + index);

        Object result = super.remove(index);
        Statement statement = new Statement(CRUDEnum.DELETE);
        statement.setPKValues(extractValues(result, pkColumns, true));
        statements.add(statement);
        return result;
    }

    public boolean remove(Object o)
    {
        //Print.pr( "remove of object " + o);

        boolean result = super.remove(o);
        Statement statement = new Statement(CRUDEnum.DELETE);
        statement.setPKValues(extractValues(o, pkColumns, true));
        statements.add(statement);
        return result;
    }

    public boolean removeAll(Collection c)
    {
        Err.error("removeAll() not implemented by ORMapList");
        return false;
    }

    public boolean retainAll(Collection c)
    {
        Err.error("retainAll() not implemented by ORMapList");
        return false;
    }

    /*
    public static ORMapExtent getInstance()
    {
    return null;
    }
    */
    private List extractValues(Object value, List cols, boolean forceNotNull)
    {
        List result = new ArrayList();
        for(Iterator iter = cols.iterator(); iter.hasNext();)
        {
            Column column = (Column) iter.next();
            Object fieldValue = column.itemAdapter.getDoAdapter().getFieldValue(value);
            if(forceNotNull && fieldValue == null)
            {
                Err.error(
                    "PK column <" + column.itemAdapter.getDoAdapter().getDOFieldName()
                        + "> cannot be NULL, table " + dbTableName);
            }
            if(!forceNotNull)
            {
                Statement.NullObject obj = new Statement.NullObject(false, fieldValue);
                result.add(obj);
            }
            else
            {
                result.add(fieldValue);
            }
        }
        return result;
    }

    private List extractSetsBeforeAdd(List sets, List cols, boolean doingPk)
    {
        //Print.prList( sets, "sets");
        //Print.prList( cols, "cols");

        List result = new ArrayList();
        for(Iterator iter1 = cols.iterator(); iter1.hasNext();)
        {
            Column column = (Column) iter1.next();
            boolean foundCol = false;
            for(Iterator iter2 = sets.iterator(); iter2.hasNext();)
            {
                Statement statement = (Statement) iter2.next();
                if(column.itemAdapter.getDoAdapter().getDOFieldName().equals(
                    statement.getColName()))
                {
                    //Print.pr( "col name will set is " + statement.getColName());

                    Object fieldValue = statement.getColValue();
                    if(doingPk && fieldValue == null)
                    {
                        Err.error(
                            "PK column <"
                                + column.itemAdapter.getDoAdapter().getDOFieldName()
                                + "> cannot be NULL, table " + dbTableName);
                    }
                    addToResult(result, doingPk, fieldValue, fieldValue == null);
                    foundCol = true;
                    break;
                }
            }
            if(!foundCol)
            {
                if(doingPk)
                {
                    /*
                    * Might want a trigger for automatically generating pks.
                    */
                    Err.error(
                        "PK column <" + column.itemAdapter.getDoAdapter().getDOFieldName()
                            + "> cannot be NULL, table " + dbTableName);
                }
                else
                {
                    // here isNull false b/c was not set to null by user and is thus a fill-in
                    addToResult(result, doingPk, null, false);
                }
            }
        }
        return result;
    }

    private void addToResult(
        List result, boolean doingPk, Object fieldValue, boolean isNull)
    {
        if(!doingPk)
        {
            Statement.NullObject obj = new Statement.NullObject(isNull, fieldValue);
            result.add(obj);
        }
        else
        {
            if(fieldValue == null)
            {
                Print.pr("Adding null to result");
            }
            result.add(fieldValue);
        }
    }

    public List getStatements()
    {
        return statements;
    }
}
