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

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import java.util.HashMap;

public class Connections
{
    private HashMap connections = new HashMap();
    private static int times;
    
    public static final int DEFAULT_CONNECT_DURATION = 3001;    

    protected void initConnection(ConnectionEnum enumId, ConnectionInfo connectionInfo)
    {
        connections.put(enumId, connectionInfo);
    }

    public ConnectionInfo get(ConnectionEnum enumId)
    {
        ConnectionInfo result = (ConnectionInfo) connections.get(enumId);
        times++;
        Err.pr(SdzNote.NEW_DATA_STORE_FACTORY, "From " + enumId + " got " + result + " times " + times);
        if(times == 0)
        {
            Err.stack();
        }
        if(result == null)
        {
            Err.error("Could not find a Connection for ConnectionEnum " + enumId + 
                    " in " + this.getClass().getName());
        }
        return result;
    }
    
    public int size()
    {
        return connections.size();
    }
}
