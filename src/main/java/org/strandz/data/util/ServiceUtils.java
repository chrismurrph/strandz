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
package org.strandz.data.util;

import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.store.JDODataStore;
import org.strandz.lgpl.util.Err;

public class ServiceUtils
{
    private static final boolean RELOAD_PM_EACH_CALL = false;

    public static void beforeProcessRequest( DataStore dataStore)
    {
        beforeProcessRequest( dataStore, true);
    }
    
    private static void specifyReloadPM( DataStore dataStore)
    {
        if(RELOAD_PM_EACH_CALL)
        {
            JDODataStore ds = (JDODataStore)dataStore;
            ds.setNewPMEachTransaction( true);
            Err.pr( JDONote.RELOAD_PM_KODO_BUG, "** Will create a new PM each time start a transaction!");
        }
        else
        {
            Err.pr( JDONote.RELOAD_PM_KODO_BUG, "** Not reloading PM, is bug fixed? - no, using Cayenne instead");
        }
    }
    
    public static void beforeProcessRequest( DataStore dataStore, boolean onServer)
    {
        if(onServer)
        {
            specifyReloadPM( dataStore);
            if(!dataStore.isOnTx())
            {
                //nufin - previously did ds.setNewPMEachTransaction in here - now have put it above
            }
            else
            {
                //Err.error( "Expect there not to be a transaction - all Spring services should use this class");
                dataStore.commitTx();
                Err.pr( JDONote.RELOAD_PM_KODO_BUG, "** Have committed previous open transaction b/c was on txn for " + ((EntityManagedDataStore)dataStore).getEM().getActualEM());
            }
            //Hopefully every query will know not to be content with getting data from the local cache,
            //so will get from the database where it has just been committed
            String msg = "startTx() on server";
            dataStore.startTx( msg);
            Err.pr( JDONote.RELOAD_PM_KODO_BUG, msg);
        }
    }

    public static void afterProcessRequest( DataStore dataStore)
    {
        afterProcessRequest( dataStore, true);
    }
    
    public static void afterProcessRequest( DataStore dataStore, boolean onServer)
    {
        if(onServer)
        {
            //dataStore.commitTx();
            Err.pr( JDONote.RELOAD_PM_KODO_BUG, "endTx() on server but no longer committing");
        }
    }
}
