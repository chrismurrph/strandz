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

import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.NoTaskLoggingTimeBandMonitor;
import org.strandz.lgpl.util.NoTaskTimeBandMonitorI;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public abstract class DomainQueries implements DomainQueriesI
{
    protected NoTaskTimeBandMonitorI loggingMonitor = new NoTaskLoggingTimeBandMonitor();
    protected HashMap queries = new HashMap();
    private boolean errOnNullEnumId = true;

    protected void initQuery(DomainQueryEnum enumId, DomainQueryI query)
    {
        queries.put(enumId, query);
    }

    public DomainQueryI get(DomainQueryEnum enumId)
    {
        if(enumId == null)
        {
            chkNullEnumId();
        }
        DomainQueryI result = (DomainQueryI) queries.get(enumId);
        //Err.pr( "From " + enumId + " got " + result);
        if(result == null)
        {
            Print.prMap( queries);
            Err.error("Could not find a DomainQueryI for DomainQueries <" + enumId + ">, type: " +
                enumId.getClass().getName() + " in " + this.getClass().getName());
        }
        return result;
    }

    public Collection executeRetCollection(DomainQueryEnum enumId)
    {
        Assert.notNull( enumId, "Can only execute if are given a not-null DomainQueryEnum");
        DomainQueryI query = get(enumId);
        return query.execute();
    }

    public List executeRetList(DomainQueryEnum enumId)
    {
        List result;
        if(enumId == null)
        {
            chkNullEnumId();
        }
        DomainQueryI query = get(enumId);
        result = (List) query.execute();
        return result;
    }
    
    public List executeRetList(DomainQueryEnum enumId, Object param1)
    {
        List result;
        if(enumId == null)
        {
            chkNullEnumId();
        }
        DomainQueryI query = get(enumId);
        result = (List)query.execute(param1);
        return result;
    }
    
    public List executeRetList(DomainQueryEnum enumId, Object param1, Object param2)
    {
        List result;
        if(enumId == null)
        {
            chkNullEnumId();
            result = new ArrayList();
        }
        else
        {
            DomainQueryI query = get(enumId);
            result = (List)query.execute(param1, param2);
        }
        return result;
    }
    
    public List executeRetList(DomainQueryEnum enumId, Object param1, Object param2, Object param3)
    {
        List result;
        if(enumId == null)
        {
            chkNullEnumId();
        }
        DomainQueryI query = get(enumId);
        result = (List)query.execute(param1, param2, param3);
        return result;
    }

    public Object executeRetObject(DomainQueryEnum enumId)
    {
        if(enumId == null)
        {
            chkNullEnumId();
        }
        DomainQueryI domainQueryI = get(enumId);
        domainQueryI.execute();
        return domainQueryI.getSingleResult();
    }

    public Object executeRetObject(DomainQueryEnum enumId, Object param1)
    {
        if(enumId == null)
        {
            chkNullEnumId();
        }
        DomainQueryI domainQueryI = get(enumId);
        domainQueryI.execute(param1);
        return domainQueryI.getSingleResult();
    }

    public Object executeRetObject(DomainQueryEnum enumId, Object param1, Object param2)
    {
        if(enumId == null)
        {
            chkNullEnumId();
        }
        DomainQueryI domainQueryI = get(enumId);
        domainQueryI.execute(param1, param2);
        return domainQueryI.getSingleResult();
    }

    public void setEM(SdzEntityManagerI pm, ErrorMsgContainer err)
    {
        if(queries.isEmpty())
        {
            Err.error( "Do not expect to have no queries when setPM(), in " + getClass().getName());
        }
        for(Iterator iterator = queries.values().iterator(); iterator.hasNext();)
        {
            DomainQueryI jdoQuery = (DomainQueryI) iterator.next();
            Assert.notNull( jdoQuery, "A query in " + queries + " is returning null");
            jdoQuery.setEM(pm, err);
            if(err != null && err.isInError)
            {
                break;
            }
        }
    }
    
    private void chkNullEnumId()
    {
        if(isErrOnNullEnumId())
        {
            Err.error( "enumId == null");
        }
        else
        {
            //Err.pr( "enumId == null");
        }
    }

    public boolean isErrOnNullEnumId()
    {
        return errOnNullEnumId;
    }

    public void setErrOnNullEnumId(boolean errOnNullEnumId)
    {
        this.errOnNullEnumId = errOnNullEnumId;
    }
}
