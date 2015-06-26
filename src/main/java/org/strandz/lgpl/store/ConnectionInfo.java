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

import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.util.Err;

import java.util.Properties;

abstract public class ConnectionInfo
{
    private String id;
    private String description;

    public ConnectionInfo( String id, String description)
    {
        this.id = id;
        this.description = description;
        //Err.pr( "Setting " + getClass().getName() + " name to <" + id + ">");
        //Err.pr( "Setting " + getClass().getName() + " description to <" + description + ">");
    }

    abstract public Properties getProperties();

    abstract public int getEstimatedConnectDuration();

    abstract public int getEstimatedLoadLookupDataDuration();

    //These are all the same for a partic datastore, so best to createDomainQueries in the constructor 
//    public DomainQueriesI createDomainQueries()
//    {
//        return null;    
//    }

    //If the DataStore is to be re-used/pooled then some other 'managing' class can do that.
    abstract public DataStore createDataStore();

    abstract public ORMTypeEnum getVersion();

    public String getConfigStringName()
    {
        Err.error( "Need a getConfigStringName() method in " +
            getClass().getName() + ", has super: " + getClass().getSuperclass().getName());
        return null;
    }
    /*  Alternative:
        public String getConfigStringName()
        {
            return null;
        }
     */

    public String getName()
    {
        return id;
    }        

    public String getDescription()
    {
        return description;
    }
}
