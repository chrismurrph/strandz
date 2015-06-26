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
package org.strandz.task.data.wombatrescue;

import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.store.wombatrescue.AbstractWRTask;

import javax.swing.SwingUtilities;
import java.util.Iterator;
import java.util.List;

/**
 * Originally used against local tomcat to replicate error on PROD where have
 * two NULLs - so that can write code to automatically fix it.
 */
public class MakeNullWorkerOnSecure extends AbstractWRTask
{
    public static void main(String s[])
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run() 
            {
                MakeNullWorkerOnSecure obj = new MakeNullWorkerOnSecure();
                obj.processParams( WombatConnectionEnum.REMOTE_DEMO_WOMBAT_KODO,
                                   "Make Null Worker Logon");
                System.exit(0);
            }
        });
    }

    public void update( EntityManagedDataStore dataStore)
    {
        DomainQueriesI queriesI = dataStore.getDomainQueries();
        List list = queriesI.executeRetList(WombatDomainQueryEnum.MULTIPLE_NULL_WORKERS);
        int timesFound = 0;
        Worker nullWorker = null;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Worker vol = (Worker) iter.next();
            timesFound++;
            if(timesFound > 1)
            {
                Err.error("Should only be one NULL volunteer, have found " + timesFound 
                          /*+ " and will delete them all"*/);
            }
            nullWorker = vol;
            Err.pr("NULL vol have found is " + nullWorker);
        }
        if(nullWorker == null)
        {
            Print.prList( list, "All Null Workers");
            Err.pr("Could not find a worker equal to NULL, so creating one");
            nullWorker = new Worker();
        }
        nullWorker.setDummy(true);
        nullWorker.setBelongsToGroup(nullWorker);
        dataStore.getEM().registerPersistent(nullWorker);
        Err.pr("MADE A NULL WORKER, " + nullWorker);
    }
}
