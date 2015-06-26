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
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.AbstractWRTask;
import org.strandz.store.wombatrescue.CayenneWombatData;

import javax.swing.SwingUtilities;
import java.util.Iterator;
import java.util.List;

public class DisplayParticularWorker extends AbstractWRTask
{
    private static final String PARTICULAR_NAME = "UCATSA";

    public static void main(String s[])
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                DisplayParticularWorker obj = new DisplayParticularWorker();
                obj.processParams( WombatConnectionEnum.SERVER_CAYENNE,
                                   "Display User " + PARTICULAR_NAME + " Logon");
                System.exit(0);
            }
        });
    }
        
    public void update( EntityManagedDataStore dataStore)
    {
        //List list = (List) dataStore.get(CayenneWombatData.WORKER);
        List list = dataStore.getDomainQueries().executeRetList( WombatDomainQueryEnum.ROSTERABLE_WORKERS);
        // List list = (List)((WombatrescueQueriesI)td.getData()).queryUnRosterableVolunteers();
        int timesFound = 0;
        WorkerI particVol = null;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            WorkerI worker = (WorkerI) iter.next();
            if(worker == null)
            {
                Err.error("The DB should never contain a null worker");
            }
            else if(!worker.isDummy() && worker.toString() == null)
            {
                Err.error("A dud worker exists: <" + worker.toString() + ">");
            }
            if(!worker.isDummy() && worker.getToLong().indexOf(PARTICULAR_NAME) != -1)
            {
                timesFound++;
                particVol = worker;
            }
            Err.pr( "worker " + worker /*+ " has flexibility " + worker.getFlexibility()*/);
        }
        if(timesFound != 1)
        {
            Err.pr("Have found " + timesFound + " instances of " + PARTICULAR_NAME);
        }
        else
        {
            Err.pr( "Found " + particVol);
            Err.pr( "isUnknown " + particVol.isUnknown());
            Err.pr( "isDummy " + particVol.isDummy());
            Err.pr( "belongsToGroup " + particVol.getBelongsToGroup());
            Err.pr( "belongsToGroup.dummy " + particVol.getBelongsToGroup().isDummy());
            Err.pr( "to: " + particVol.getToLong());
            Err.pr( "flexibility: " + particVol.getFlexibility());
            Err.pr( particVol.formatAllPhonesAndEmail());
        }
    }
}
