/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.store;

import org.strandz.lgpl.persist.MemoryData;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LoopyQuery extends DomainQuery implements DomainQueryI, Comparator
{
    private MemoryData data;

    public LoopyQuery(Class clazz, String id)
    {
        this.queryOn = clazz;
        this.id = id;
    }

    private boolean discriminatorMatch(Object row, Object[] parameters)
    {
        boolean result;
        if(parameters.length != 0)
        {
            result = match(row, parameters);
        }
        else
        {
            result = match(row);
        }
        return result;
    }
    
    public void preExecute()
    {
        //nufin
    }

    public void postExecute(Collection c)
    {
        //nufin
    }
    
    public Collection execute(Object[] parameters)
    {
        List result = new ArrayList();
        chkSimpleParams( parameters);
        singleResult = null;
        preExecute();
        start(id);
        Assert.notNull( data, "setEM() has not been called on LoopyQuery <" + this + ">");
        List allRows = data.getList(queryOn);
        
        List rowsCopy = new ArrayList( allRows); //to stop java.util.ConcurrentModificationException
        //List rowsCopy = allRows; //keep to see how frequent ConcurrentModificationException is?
        //A: is repeatable, not always
        for(Iterator iterator = rowsCopy.iterator(); iterator.hasNext();)
        {
            Object row = iterator.next();
            //if(row != null) //Wouldn't get this in a real DB
            //{
                if(discriminatorMatch(row, parameters))
                {
                    result.add(row);
                }
            }
//            else
//            {
//                Err.error( "A null object is a problem in " + this + ". This wouldn't come from a " +
//                        "DB so use a blank/dummy DO instead");
//            }
//        }
        Collections.sort(result, this);
        chkResult(result);
        postExecute(result);
        formSingleResult(result);
        stop();
        return result;
    }

    public boolean match(Object row)
    {
        return true;
    }

    public boolean match(Object row, Object[] parameters)
    {
        Err.error("If match() with params is being called then the " + this.getClass().getName() + " subclass needs to implement match()");
        return true;
    }

    public int compare(Object one, Object two)
    {
        return 0;
    }

    public void setEM(SdzEntityManagerI em, ErrorMsgContainer err)
    {
        data = (MemoryData) em.getActualEM();
    }
}
