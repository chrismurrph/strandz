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

import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.JDONote;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.store.wombatrescue.POJOWombatData;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;

public class MakeNewWorker
{
    private static EntityManagedDataStore dataStore;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"DevData", WombatConnectionEnum.WOMBAT_KODO.getName()};
            processParams(str);
        }
        System.exit(0);
    }

    public static void processParams(String s[])
    {
         /**/
        if(s[0].equals("DevData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database");
            }
            /*
            * Dangerous!
            */
            if(s[1].equals(WombatConnectionEnum.PROD))
            {
                Err.error("Cannot work with the " + WombatConnectionEnum.getFromName(s[1]) +
                    " database");
            }
             /**/
            if(s.length == 2)
            {
                DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
                dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[1]));
                dataStore = dataStoreFactory.getEntityManagedDataStore();
            }
            else
            {
                Err.error();
            }
            dataStore.startTx();

            List list = (List) dataStore.get(POJOWombatData.WORKER);
            update(list);
            dataStore.commitTx();
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void update(Collection list)
    {
        int timesFound = 0;
        Worker nullWorker = null;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Worker vol = (Worker) iter.next();
            if(
                // (
                // vol.getChristianName() != null &&
                // vol.getChristianName().equals( "Bart")
                // ) ||
                vol.equals(Worker.NULL)
                )
            {
                timesFound++;
                if(timesFound > 1)
                {
                    Err.error("Should only be one NULL volunteer");
                }
                nullWorker = vol;
                Err.pr("NULL vol have found is " + nullWorker);
            }
        }
        if(nullWorker == null)
        {
            Err.pr("Could not find a worker equal to NULL, so creating one");
            nullWorker = new Worker();
        }
        nullWorker.setDummy(true);
        Err.pr(JDONote.APPEARS_CONSTRUCTOR_NOT_CALLED,
            "Why have to do this - Worker's constructor s/do it");
        if(nullWorker.getBelongsToGroup() != null)
        {
            Err.pr(JDONote.APPEARS_CONSTRUCTOR_NOT_CALLED,
                "appears Constructor was called");
        }
        else
        {
            Err.pr(JDONote.APPEARS_CONSTRUCTOR_NOT_CALLED,
                "appears Constructor was NOT called");
        }
        nullWorker.setBelongsToGroup(nullWorker);
        dataStore.getEM().registerPersistent(nullWorker);
        Err.pr("MADE A NULL WORKER, " + nullWorker);
    }
}
