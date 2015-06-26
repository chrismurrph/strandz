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

import org.strandz.core.prod.Session;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.persist.CayenneEntityManagerFactory;
import org.strandz.lgpl.store.CayenneServerDataStore;
import org.strandz.lgpl.store.CayenneSecureConnectionInfo;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;

public class CayenneWombatDataStore extends CayenneServerDataStore
{
    private String connectionConfigString;

    private static int times;

    /**
     * Always called on by a server. Getting an EM takes time. On the client it is always
     * done in its own special task.
     *
     * @param enumIdWombat
     */
    public static CayenneWombatDataStore newInstance(WombatConnectionEnum enumIdWombat)
    {
        CayenneWombatDataStore result;
        Err.pr("Server service: " + enumIdWombat);
        WombatConnections connections = new WombatConnections();
        CayenneSecureConnectionInfo connectionInfo = (CayenneSecureConnectionInfo)connections.get( enumIdWombat);
        result = new CayenneWombatDataStore( connectionInfo);
        Err.pr("To try to get an EM");
        /*
        SdzEntityManagerI emI = EntityManagerFactory.createSdzEMI(
            connectionInfo.getORMType(),
            result.getConnectionConfigString(), null);
        */
        SdzEntityManagerI emI = CayenneEntityManagerFactory.createSdzEMI(
            result.getCayenneORMType(), result.getConnection().getConfigStringName(),
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
    public CayenneWombatDataStore(CayenneSecureConnectionInfo connectionInfo)
    {
        Err.pr( "Creating a CayenneWombatDataStore");
        boolean client = connectionInfo.getORMType() == ORMTypeEnum.CAYENNE_CLIENT;
        //boolean client = true;
        super.setClasses( CayenneWombatData.getInstance( client).getClasses());
        Assert.isTrue( connectionInfo.getORMType().isCayenne());
        setEstimatedConnectDuration( connectionInfo.getEstimatedConnectDuration());
        setEstimatedLookupDataDuration( connectionInfo.getEstimatedLoadLookupDataDuration());
        setErrorThrower( Session.getErrorThrower());
        setConnectionConfigString( connectionInfo.getConfigStringName());
        setCayenneORMType( connectionInfo.getORMType());
        setDomainQueries( new CayenneWombatDomainQueries( client));
        setName(connectionInfo.getName());
    }

    public String getConnectionConfigString()
    {
        return connectionConfigString;
    }

    public void setConnectionConfigString(String connectionConfigString)
    {
        this.connectionConfigString = connectionConfigString;
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