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
package org.strandz.store.wombatrescue;

import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.persist.JDOEntityManagerFactory;
import org.strandz.lgpl.store.ConnectionInfo;
import org.strandz.lgpl.store.JDODataStore;
import org.strandz.lgpl.store.JDOLocalRemoteConnectionInfo;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.JDONote;
import org.strandz.core.prod.Session;

import java.util.Properties;

public class JDOWombatDataStore extends JDODataStore
{
    private static boolean fromJDOConnectionServer = false;
    private static int times;
    
    /**
     * Always called on by a server
     *
     * @param enumIdWombat
     */
    public static JDOWombatDataStore newInstance(WombatConnectionEnum enumIdWombat)
    {
        JDOWombatDataStore result;
        fromJDOConnectionServer = true;
        Err.pr("Server service: " + enumIdWombat);
        WombatConnections connections = new WombatConnections();
        result = new JDOWombatDataStore(connections.get(enumIdWombat));
        fromJDOConnectionServer = false;
        // Do this to get the properties read in
        Err.pr("To try to get an EM");
        /*
        SdzEntityManagerI emI = EntityManagerFactory.createSdzEMI( 
            ORMTypeEnum.JDO, null, result.getConnectionProperties());
        */
        SdzEntityManagerI emI = JDOEntityManagerFactory.createSdzEMI(
            result.getConnection().getVersion(),
            result.getConnection().getConfigStringName(),
            result.getConnection().getProperties());
        Err.pr("em: " + emI);
        return result;
    }
    
    /**
     * Get the connection properties depending on which enumIdWombat are connecting
     * to. Also there are two types of connections. One, the server, is to
     * the actual database. The other type of connection is a client connection,
     * which connects to the server. From the client's point of view this server
     * is know as a host. (The host serves the guest/client). If !fromJDOConnectionServer
     * then we are currently at a client. There is only ever one server for a client,
     * so the logic in here sets the host property in this case.
     *
     * @param connectionInfo
     */
    public JDOWombatDataStore(ConnectionInfo connectionInfo)
    {
        super.setClasses(POJOWombatData.CLASSES);
        Properties props = connectionInfo.getProperties();
        if(!fromJDOConnectionServer) //is directly client facing
        {
            if(connectionInfo instanceof JDOLocalRemoteConnectionInfo)
            {
                /*
                * We are a client, so we specify the remote host we will connect to
                */
                JDOLocalRemoteConnectionInfo connection = (JDOLocalRemoteConnectionInfo) connectionInfo;
                props.setProperty("host", connection.getServerName());
                Err.pr("Have set host property to " + connection.getServerName() + " for the client part of " + connection);
            }
            setEstimatedConnectDuration(connectionInfo.getEstimatedConnectDuration());
            setEstimatedLookupDataDuration(connectionInfo.getEstimatedLoadLookupDataDuration());
            setErrorThrower( Session.getErrorThrower());
            setSupportPhone( RosterSessionUtils.getProperty( "supportPhone"));
        }
        else
        {
            Err.error( "Not using this type of server at the moment");
            Err.pr("Database facing PM here");
            if(connectionInfo instanceof JDOLocalRemoteConnectionInfo)
            {
                Err.pr("Need to verify somehow that have permissions");
            }
            else
            {
                Err.error("If running as a server the connection must be " +
                        "of type JDOLocalRemoteConnection, toString() is <" + connectionInfo + ">");
            }
        }
        setConnectionProperties(props, connectionInfo);
        setDomainQueries( new JDOWombatDomainQueries());
        setName(connectionInfo.getName());
    }        
    
    protected void initLookups()    
    {
        //Err.stack();
        super.initLookups();
    }
    
    public void startTx()
    {
        if(JDONote.RELOAD_PM_KODO_BUG.isVisible())
        {
            Err.pr( "Would be nice to provide a reason when JDONote.RELOAD_PM_KODO_BUG.isVisible()");
        }
        super.startTx();
    }

    public void startTx( String reason)
    {
        times++;
        Object pm = null;
        if(getEM() != null)
        {
            pm = getEM().getActualEM();
        }
        Err.pr(JDONote.RELOAD_PM_KODO_BUG, reason + " in " + this + " with pm: " + pm + " times " + times + " isOnTxn " + isOnTx());
        super.startTx( reason);
    }
}
