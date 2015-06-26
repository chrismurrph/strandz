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

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Statement
{
    int id; // Added this as sorting on timestamp not always work (when same)
    Date timeStamp = new Date();
    private CRUDEnum statementType;
    /**
     * Used by DELETE and INSERT
     */
    private List pkValues;
    private List nonPkValues;
    private String colName;
    private Object colValue;
    private boolean colValueNull;
    private boolean superflous = false;
    private static int idCounter = Integer.MIN_VALUE;
    private static int times;
    static boolean watchSets = false;

    Statement(CRUDEnum statementType)
    {
        this.statementType = statementType;
        if(idCounter == Integer.MAX_VALUE)
        {
            Err.error("Maximum number of statements exceeded");
        }
        idCounter++;
        id = idCounter;
    }

    public void setPKValues(List list)
    {
        checkList(list, "setPKValues()");
        pkValues = list;
    }

    void setNonPKValues(List list)
    {
        checkList(list, "setNonPKValues()");
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object obj = iter.next();
            if(obj != null)
            {
                Class clazz = obj.getClass();
                if(clazz != NullObject.class)
                {
                    Err.error("Getting a " + clazz);
                }
                if(watchSets)
                {
                    times++;
                    Print.pr(
                        "Setting NonPKValue to " + ((NullObject) obj).obj + " times "
                            + times);
                    if(times == 0)
                    {
                        Err.stack();
                    }
                }
            }
            else
            {/*
         if(watchSets)
         {
         Err.pr( "Setting NonPKValue to null times " + times);
         }
         */}
        }
        nonPkValues = list;
    }

    public void setSuperflous(boolean superflous)
    {
        /*
        if(superflous == true)
        {
        if(
        //getType() != CRUDEnum.DELETE && getType() != CRUDEnum.INSERT &&
        pkValues.get( 0).equals( "Angel2"))
        {
        times++;
        Err.pr( "Making superflous for an " + getType() + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        }
        }
        */
        this.superflous = superflous;
    }

    private void checkList(List list, String txt)
    {
        if(list == null)
        {
            Err.error("Cannot " + txt + " to a null list");
        }
        else if(list.isEmpty())
        {
            Err.error("Cannot " + txt + " to an empty list");
        }
        else if(Utils.listIsAllNulls(list))
        {
            Err.error("Cannot " + txt + " to an list entirely made up of nulls");
        }
    }

    public void setColName(String colName)
    {
        /*
        * legitimate thing to do
        *
        if(!superflous && colName == null)
        {
        Err.error( "setColName() must not be null");
        }
        */
        this.colName = colName;
    }

    public void setColValue(Object colValue)
    {
        if(colValue == null)
        {
            /*
            times++;
            Err.pr( "setColValue() to null IOW NULL times " + times);
            if(times == 0)
            {
            Err.stack();
            }
            */
            colValueNull = true;
        }
        else
        {
            this.colValue = colValue;
        }
    }

    private StringBuffer everything(StringBuffer result)
    {
        if(pkValues != null)
        {
            result.append("PK values: ");
            for(Iterator iter = pkValues.iterator(); iter.hasNext();)
            {
                String val = (String) iter.next();
                result.append("<" + val + ">,");
            }
        }
        if(nonPkValues != null)
        {
            result.append("NON PK values: ");
            for(Iterator iter = nonPkValues.iterator(); iter.hasNext();)
            {
                /*
                * If nullObj == null then it means it is a placeholder. IOW there
                * is to be no deliberate UPDATE to NULL
                */
                NullObject nullObj = (NullObject) iter.next();
                String txt = null;
                if(nullObj != null)
                {
                    txt = nullObj.toString();
                }
                result.append("<" + txt + ">,");
            }
        }
        if(colName != null)
        {
            result.append("COL NAME <" + colName + "> VALUE <" + colValue + ">");
        }
        return result;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer(
            timeStamp.getTime() + " " + statementType.toString() + ", ");
        result = everything(result);
        return result.toString();
    }

    public CRUDEnum getType()
    {
        return statementType;
    }

    public List getPkValues()
    {
        return pkValues;
    }

    public String getColName()
    {
        return colName;
    }

    public Object getColValue()
    {
        /*
        if(colValueNull)
        {
        times++;
        if(times == 2)
        {
        Err.stack( "getColValue() wants to return null times " + times);
        }
        }
        */
        return colValue;
    }

    public List getNonPkValues()
    {
        return nonPkValues;
    }

    public boolean isSuperflous()
    {
        return superflous;
    }

    public boolean isColValueNull()
    {
        boolean result = colValueNull;
        /*
        times++;
        if(result == true)
        {
        Err.pr( "Returning that colValue NULL times " + times);
        }
        */
        return result;
    }

    public static class NullObject
    {
        boolean isNull;
        Object obj;

        NullObject(boolean isNull, Object obj)
        {
            this.isNull = isNull;
            /*
            times++;
            Err.pr( "creating isNull NullObject times " + times);
            if(times == 0)
            {
            Err.stack();
            }
            */
            this.obj = obj;
        }

        public String toString()
        {
             /**/
            boolean actuallyNull = false;
            if(!(obj instanceof String))
            {
                if(obj == null)
                {
                    actuallyNull = true;
                    // Err.error( "Statement cannot support a null obj");
                }
                else
                {
                    Err.error(
                        "Statement cannot support an of type " + obj.getClass().getName());
                }
            }

             /**/
            String result = null;
            if(!actuallyNull)
            {
                result = (String) obj;
                Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS, "Why null String appearing?");
            }
            if(isNull)
            {
                result = "NULL";
            }
            return result;
        }

        public boolean isNull()
        {
            return isNull;
        }
    }
}
