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
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract public class DataStoreFactory
{
    private List<ManagedConnection> managedConnections = new ArrayList<ManagedConnection>();
    private Connections possibleConnections = null;
    private Object authenticationObject;
    private boolean setAConnection = false;

    private static int times;

    private static class ManagedConnection
    {
        ConnectionEnum connectionEnum;
        ConnectionInfo connectionInfo;
        DataStore createdDataStore;

        private ManagedConnection(ConnectionEnum connectionEnum, ConnectionInfo connectionInfo)
        {
            this.connectionInfo = connectionInfo;
            this.connectionEnum = connectionEnum;
            Err.pr(SdzNote.EMP_ERRORS, "ManagedConnection constructor: " + connectionInfo);
        }
    }

    public void setAuthenticationObject(Object authenticationObject)
    {
        this.authenticationObject = authenticationObject;
    }

    protected void setPossibleConnections(Connections connections)
    {
        possibleConnections = connections;
    }
    
    public void setConnection(ConnectionEnum connectionEnum)
    {
        Assert.isFalse( managedConnections.size() >= 1, "setConnection() s/not be used when already have a connection/s, use addConnection instead");
        addConnection( connectionEnum);
        setAConnection = true;
    }    
    
    public ConnectionInfo getConnectionInfo( ConnectionEnum connectionEnum)
    {
        ConnectionInfo result;
        ManagedConnection mc = getManagedConnection( connectionEnum, false);
        result = mc.connectionInfo;
        return result;
    }

    public void addConnection(ConnectionEnum connectionEnum)
    {
        Assert.isFalse( setAConnection, "Don't use setConnection if are adding many connections");
        if(managedConnections.size() >= 3)
        {
            Err.error("DataStoreFactory only supports three possible connections");
        }
        if(alreadyManaging(connectionEnum))
        {
            Err.pr("Observation: already managing a connection of type " + connectionEnum);
        }
        managedConnections.add(new ManagedConnection(connectionEnum, possibleConnections.get(connectionEnum)));
    }
    
    public void setCredentials( ConnectionEnum connectionEnum, String username, String password)
    {
        ManagedConnection connection = getManagedConnection( connectionEnum, true);
        ((SecureConnectionInfo)connection.connectionInfo).setUsername( username);    
        ((SecureConnectionInfo)connection.connectionInfo).setPassword( password);    
    }

    public void removeConnection(int idx)
    {
        ManagedConnection mc = (ManagedConnection) managedConnections.get(idx);
        if(mc == null)
        {
            Err.error("Could not find a managedConnection at " + idx);
        }
        else
        {
            removeConnection(mc);
        }
    }

    private void removeConnection(ManagedConnection mc)
    {
        if(mc.createdDataStore.isOnTx())
        {
            Err.error("Cannot ask to remove a connection that still has an active transaction");
        }
        else
        {
            if(mc.createdDataStore instanceof EntityManagedDataStore)
            {
                SdzEntityManagerI pm = ((EntityManagedDataStore) mc.createdDataStore).getEM();
                pm.close();
            }
            managedConnections.remove(mc);
        }
    }
    
    /**
     * Can have many of the same ConnectionEnum so lets not remove based on it without
     * making sure that only have one-of.
     */
    public void removeConnection(ConnectionEnum connectionEnum)
    {
        ManagedConnection mc = getManagedConnection( connectionEnum, false);
        if(mc.createdDataStore == null)
        {
            Err.error("Cannot ask to remove a connection that was never used");
        }
        else
        {
            removeConnection(mc);
        }
    }
    
    private ManagedConnection getManagedConnection( ConnectionEnum connectionEnum, boolean mustBeUnused)
    {
        ManagedConnection result = null;
        int foundTimes = 0;
        for(Iterator iterator = managedConnections.iterator(); iterator.hasNext();)
        {
            ManagedConnection managedConnection = (ManagedConnection) iterator.next();
            if(managedConnection.connectionEnum == connectionEnum)
            {
                foundTimes++;
                if(foundTimes > 1)
                {
                    Print.prList( managedConnections, "Added Managed Connections");
                    Err.error("Cannot get a connection by its ConnectionEnum when have many with that ConnectionEnum");
                }
                result = managedConnection;
            }
        }
        if(result == null)
        {
            Print.prList( managedConnections, "Added Managed Connections");
            Err.error("Cannot get connection: " + connectionEnum + ", as it was never added");
        }
        else
        {
            if(mustBeUnused)
            {
                if(result.createdDataStore != null)
                {
                    Err.error( "Cannot get a connection that has already been used");
                }
            }
        }
        return result;
    }

    private boolean alreadyManaging(ConnectionEnum connectionEnum)
    {
        boolean result = false;
        for(Iterator iterator = managedConnections.iterator(); iterator.hasNext();)
        {
            ManagedConnection managedConnection = (ManagedConnection) iterator.next();
            if(managedConnection.connectionEnum == connectionEnum)
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public EntityManagedDataStore getEntityManagedDataStore()
    {
        return getEntityManagedDataStore(0);
    }

    private EntityManagedDataStore getEntityManagedDataStore(int i)
    {
        return getEntityManagedDataStore(i, false);
    }

//    private EntityManagedDataStore getEntityManagedDataStoreSafely()
//    {
//        return getEntityManagedDataStore(0, true);
//    }

    public EntityManagedDataStore getEntityManagedDataStoreSafely(int i)
    {
        return getEntityManagedDataStore(i, true);
    }

    public DataStore getDataStore()
    {
        return getDataStore(0);
    }

    public DataStore getDataStore(int i)
    {
        return getDataStore(i, false);
    }

//    private DataStore getDataStoreSafely()
//    {
//        return getDataStore(0, true);
//    }

    public DataStore getDataStoreSafely(int i)
    {
        return getDataStore(i, true);
    }

    private EntityManagedDataStore getEntityManagedDataStore(int i, boolean beSafe)
    {
        return (EntityManagedDataStore) getDataStore(i, beSafe);
    }

    private DataStore getDataStore(int i, boolean beSafe)
    {
        DataStore result = null;
        ManagedConnection mc = managedConnections.get(i);
        if(mc.createdDataStore == null)
        {
            if(mc.connectionInfo instanceof EntityManagedConnectionInfo)
            {
                result = mc.connectionInfo.createDataStore();
            }
            else if(mc.connectionInfo instanceof SimpleConnectionInfo)
            {
                //Err.error( "DataStoreFactory does not yet support SimpleConnection");
                result = mc.connectionInfo.createDataStore();
            }
            else
            {
                Err.error("DataStoreFactory does not support connections of type " + mc.connectionInfo.getClass().getName());
            }
            Assert.notNull(result.getDomainQueries(), "Domain Queries should have been attached to <" + result + "> " +
                    "in its constructor");
            result.setConnection( mc.connectionInfo);
            ConnectionInfo connection = result.getConnection();
            if(connection instanceof SpringSecureConnectionInfo)
            {
                ((SpringSecureConnectionInfo)connection).setAuthenticationObject(
                    authenticationObject);
            }
            mc.createdDataStore = result;
        }
        else if(beSafe)
        {
            Err.error("You specified the safe option, yet the connection at " + i + " is already being used");
        }
        else
        {
            result = mc.createdDataStore;
        }
        return result;
    }
    
    protected Connections getPossibleConnections()
    {
        return possibleConnections;
    }
}
