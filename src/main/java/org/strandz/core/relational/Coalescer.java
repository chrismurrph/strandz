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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Coalescer
{
    private List updateableColumnNames;
    private List origStatements;
    /**
     * Will allow us to reorder without affecting the
     * origStatements order, yet access the original Statements.
     */
    private List workingStatements;
    private List statementsOnSamePkList = new ArrayList();
    private RelationalDebugList coalescedStatements;
    private static StatementPKComparator pkComparator = new StatementPKComparator();
    public static StatementTimestampComparatorAsc timestampComparator = new StatementTimestampComparatorAsc();
    private static int times;

    Coalescer(List updateableColumnNames, List origStatements)
    {
        this.origStatements = origStatements;
        this.updateableColumnNames = updateableColumnNames;
        workingStatements = new ArrayList(origStatements);
        coalesce();
    }

    RelationalDebugList getCoalescedStatements()
    {
        return coalescedStatements;
    }

    private void coalesce()
    {
        /*
        * Create a load of lists that have the same pk
        */
        Statement.watchSets = true;
        Collections.sort(workingStatements, pkComparator);

        Statement lastStatement = null;
        List currentList = new ArrayList();
        statementsOnSamePkList.add(currentList);
        for(Iterator iter = workingStatements.iterator(); iter.hasNext();)
        {
            Statement statement = (Statement) iter.next();
            if(lastStatement != null)
            {
                if(pkComparator.compare(lastStatement, statement) != 0)
                {
                    currentList = new ArrayList();
                    statementsOnSamePkList.add(currentList);
                    currentList.add(statement);
                }
                else // are the same
                {
                    currentList.add(statement);
                }
            }
            else
            {
                currentList.add(statement);
            }
            lastStatement = statement;
        }
        coalescedStatements = new RelationalDebugList();
        /*
        * Order each of these lists by date.
        * Go thru each perform coalescing operations.
        */
        for(Iterator iter = statementsOnSamePkList.iterator(); iter.hasNext();)
        {
            List list = (List) iter.next();
            Collections.sort(list, timestampComparator);
            // pUtils.debugList( list);
            chkSuperfluous(list, false);

            int deleteAt = markMultiples(list, CRUDEnum.DELETE);
            int insertAt = markMultiples(list, CRUDEnum.INSERT);
            if(deleteAt != -1)
            {
                markUpdatesComeB4Delete(deleteAt, list);
            }
            if(insertAt != -1 && deleteAt != -1)
            {
                markInsertComeB4Delete(insertAt, deleteAt, list);
            }
            Print.pr("updateableColumnNames are " + updateableColumnNames);

            HashMap hash = createHash(updateableColumnNames);
            markUpdatesIntoUpdate(list, hash, updateableColumnNames);
            if(insertAt != -1)
            {
                if(!((Statement) list.get(insertAt)).isSuperflous())
                {
                    hash = createHash(updateableColumnNames);
                    markUpdatesIntoInsert(list, hash, updateableColumnNames, insertAt);
                }
            }

            int numberLeft = 0;
            for(Iterator iterator = list.iterator(); iterator.hasNext();)
            {
                Statement statement = (Statement) iterator.next();
                if(!statement.isSuperflous())
                {
                    coalescedStatements.add(statement);
                    numberLeft++;
                }
            }
            if(numberLeft > 1)
            {
                // Can still be left with an INSERT followed by an UPDATE
                Err.error(
                    "At most one statement should be left"
                        + " as not being superflous, get " + numberLeft);
            }
            else if(list.size() != 0 && numberLeft == 0)
            {
                Print.pr("Have collapsed an INSERT followed by a DELETE");
            }
        }
        Collections.sort(coalescedStatements, timestampComparator);
        Statement.watchSets = false;
    }

    private static HashMap createHash(List cols)
    {
        HashMap hash = new HashMap();
        for(Iterator iter = cols.iterator(); iter.hasNext();)
        {
            String colName = (String) iter.next();
            hash.put(colName, null);
        }
        return hash;
    }

    private static void chkSuperfluous(List list, boolean b)
    {
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Statement statement = (Statement) iter.next();
            if(statement.isSuperflous() != b)
            {
                Err.error("Statement " + statement + " is not superflous " + b);
                break; // only for habits sake!
            }
        }
    }

    /**
     * Get rid of all but the last of a particular type of statement.
     * Return true if found a last one at all.
     */
    private static int markMultiples(List list, CRUDEnum what)
    {
        int result = -1;
        Statement lastStatement = null;
        int i = 0;
        for(Iterator iter = list.iterator(); iter.hasNext(); i++)
        {
            Statement statement = (Statement) iter.next();
            if(statement.getType() == what)
            {
                statement.setSuperflous(true);
                lastStatement = statement;
                result = i;
            }
        }
        if(lastStatement != null)
        {
            lastStatement.setSuperflous(false);
        }
        return result;
    }

    private static void markUpdatesComeB4Delete(int deleteAt, List list)
    {
        for(int i = 0; i <= deleteAt - 1; i++)
        {
            Statement statement = (Statement) list.get(i);
            if(statement.getType().isUpdate())
            {
                statement.setSuperflous(true);
            }
        }
    }

    private static void markInsertComeB4Delete(
        int insertAt, int deleteAt, List list)
    {
        if(insertAt < deleteAt)
        {
            Statement statement = (Statement) list.get(insertAt);
            statement.setSuperflous(true);
            statement = (Statement) list.get(deleteAt);
            statement.setSuperflous(true);
        }
    }

    private static void markUpdatesIntoUpdate(
        List list, HashMap hash, List updateableColumnNames)
    {
        /*
        times++;
        Err.pr("In markUpdatesIntoUpdate times " + times);
        if(times == 4)
        {
            Err.debug();
        }
        */
        Statement lastStatement = null;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Statement statement = (Statement) iter.next();
            if(!statement.isSuperflous() && statement.getType().isUpdate())
            {
                String colName = statement.getColName();
                if(colName == null)
                {
                    /*
                    * This situation exists where all the updates have been
                    * rolled into one in the previous method call.
                    */
                    List values = statement.getNonPkValues();
                    for(int j = 0; j <= values.size() - 1; j++)
                    {
                        Object value = values.get(j);
                        hash.put(updateableColumnNames.get(j), value);
                    }
                }
                else
                {
                    Statement.NullObject obj = null;
                    Object colValue = statement.getColValue();
                    boolean isNull = statement.isColValueNull();
                    obj = new Statement.NullObject(isNull, colValue);
                    hash.put(colName, obj);
                }
                statement.setSuperflous(true);
                lastStatement = statement;
            }
        }
        if(lastStatement != null)
        {
            lastStatement.setSuperflous(false);
            lastStatement.setColName(null);
            lastStatement.setColValue(null);

            List nonPKValues = new ArrayList();
            for(Iterator iter = updateableColumnNames.iterator(); iter.hasNext();)
            {
                String colName = (String) iter.next();
                Object value = hash.get(colName);
                if(value != null)
                {
                    nonPKValues.add(value);
                }
                else
                {
                    // Statement.NullObject nullObj = new Statement.NullObject( false, null);
                    nonPKValues.add(null);
                }
            }
            lastStatement.setNonPKValues(nonPKValues);
        }
    }

    private static void markUpdatesIntoInsert(
        List list, HashMap hash, List updateableColumnNames, int insertAt)
    {
        boolean foundAnUpdate = false;
        for(int i = insertAt + 1; i <= list.size() - 1; i++)
        {
            Statement statement = (Statement) list.get(i);
            if(!statement.isSuperflous() && statement.getType().isUpdate())
            {
                String colName = statement.getColName();
                if(colName == null)
                {
                    /*
                    * This situation exists where all the updates have been
                    * rolled into one in the previous method call.
                    */
                    List values = statement.getNonPkValues();
                    for(int j = 0; j <= values.size() - 1; j++)
                    {
                        Object value = values.get(j);
                        hash.put(updateableColumnNames.get(j), value);
                    }
                }
                else
                {
                    Object colValue = statement.getColValue();
                    hash.put(colName, colValue);
                }
                foundAnUpdate = true;
                statement.setSuperflous(true);
            }
        }
        if(foundAnUpdate)
        {
            Statement insertStatement = (Statement) list.get(insertAt);
            List insertColChanges = insertStatement.getNonPkValues();
            List nonPKValues = new ArrayList();
            for(int i = 0; i <= updateableColumnNames.size() - 1; i++)
            {
                String colName = (String) updateableColumnNames.get(i);
                Object value = hash.get(colName);
                if(value != null)
                {
                    nonPKValues.add(value);
                }
                else
                {
                    nonPKValues.add(insertColChanges.get(i));
                }
            }
            insertStatement.setNonPKValues(nonPKValues);
        }
    }

    public static class StatementPKComparator implements Comparator
    {
        public int compare(Object obj1, Object obj2)
        {
            Statement state1 = (Statement) obj1;
            Statement state2 = (Statement) obj2;
            List pk1 = state1.getPkValues();
            List pk2 = state2.getPkValues();
            return Utils.stringListComparator.compare(pk1, pk2);
        }
    }


    public static class StatementTimestampComparatorAsc implements Comparator
    {
        public int compare(Object obj1, Object obj2)
        {
            Statement state1 = (Statement) obj1;
            Statement state2 = (Statement) obj2;
            Date time1 = state1.timeStamp;
            Date time2 = state2.timeStamp;
            int result = time1.compareTo(time2);
            if(result == 0)
            {
                result = state1.id - state2.id;
            }
            return result;
        }
    }


    public static class StatementTimestampComparatorDsc implements Comparator
    {
        public int compare(Object obj1, Object obj2)
        {
            Statement state1 = (Statement) obj1;
            Statement state2 = (Statement) obj2;
            Date time1 = state1.timeStamp;
            Date time2 = state2.timeStamp;
            int result = time2.compareTo(time1);
            if(result == 0)
            {
                result = state2.id - state1.id;
            }
            return result;
        }
    }
}
