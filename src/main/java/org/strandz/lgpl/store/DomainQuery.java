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

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;

import java.util.Collection;

public abstract class DomainQuery implements DomainQueryI
{
    Class queryOn;
    public String id;
    Object singleResult;
    /**
     * Only needed for debugging
     */
    public Object[] parameters;

    static final Object[] NO_PARAM = new Object[0];
    private static Object[] params1 = new Object[1];
    private static Object[] params2 = new Object[2];
    private static Object[] params3 = new Object[3];

    public String toString()
    {
        return id;
    }
    
    public String getDescription()
    {
        return id;
    }

    public final Collection execute()
    {
        return execute(NO_PARAM);
    }

    /**
     * This is a NOP by default, but you may want to do a sanity
     * check on whatever is returned.
     */
    public void chkResult(Collection c)
    {
        //nufin
    }

    /**
     * This is a NOP by default. Override when you want to form a single
     * result from the collection. In this case chkResult will sometimes also
     * be overridden to make sure that only one element exists in the
     * collection.
     */
    public void formSingleResult(Collection c)
    {
        //nufin, but subclasses will call setSingleResult()
    }

    public Object getSingleResult()
    {
        Object result = singleResult;
        return result;
    }

    public void setSingleResult(Object singleResult)
    {
        this.singleResult = singleResult;
    }

    void start(String name)
    {
    }

    protected void stop()
    {
    }
    
    public abstract Collection execute(Object[] parameters);
    
    void chkSimpleParams(Object[] parameters)
    {
        for(int i = 0; i < parameters.length; i++)
        {
            Object parameter = parameters[i];
            boolean ok = Utils.isSimple( parameter.getClass());
            if(!ok)
            {
                Err.error( "Parameter for query is not a simple type, is of type " + parameter.getClass());
            }
        }
        this.parameters = parameters;
    }

    public final Collection execute(Object param1)
    {
        params1[0] = param1;
        return execute(params1);
    }

    public final Collection execute(Object param1, Object param2)
    {
        params2[0] = param1;
        params2[1] = param2;
        return execute(params2);
    }

    public final Collection execute(Object param1, Object param2, Object param3)
    {
        params3[0] = param1;
        params3[1] = param2;
        params3[2] = param3;
        return execute(params3);
    }

    public String getId()
    {
        return id;
    }
}
