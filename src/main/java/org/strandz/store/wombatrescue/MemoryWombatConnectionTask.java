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

import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.EntityManagerFactory;
import org.strandz.lgpl.persist.MemoryData;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.AbstractTask;
import org.strandz.lgpl.util.Err;

import java.util.ArrayList;
import java.util.List;

public class MemoryWombatConnectionTask extends AbstractTask
{
    private EntityManagedDataStore dataStore;

    public MemoryWombatConnectionTask(MemoryWombatDataStore dataStore)
    {
        Err.pr(SdzNote.EMP_ERRORS, "In MemoryWombatConnectionTask constructor with dataStore: " + dataStore);
        this.dataStore = dataStore;
        setTitle("Simulated Connecting...");
        setCompletedPhrase("Connected");
        setDuration(dataStore.getEstimatedConnectDuration());
    }

    public Object newTask()
    {
        return new ActualTask();
    }

    public class ActualTask
    {
        ActualTask()
        {
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException e)
            {
                Err.error(e);
            }
            WombatDemoData wombatDemoData = WombatDemoData.getInstance();
            List lists[] = new List[14];
            int index = 0;
            lists[index++] = wombatDemoData.newWorkers;
            lists[index++] = wombatDemoData.newBMs;
            lists[index++] = wombatDemoData.newRSs;
            lists[index++] = wombatDemoData.newUsers;
            List lookupLists = new ArrayList();
            lookupLists.add( wombatDemoData.wombatLookups);
            lists[index++] = lookupLists; //only ever one
            lists[index++] = wombatDemoData.wombatLookups.get( WombatDomainLookupEnum.ALL_DAY_IN_WEEK);
            lists[index++] = wombatDemoData.wombatLookups.get( WombatDomainLookupEnum.ALL_INTERVAL);
            lists[index++] = wombatDemoData.wombatLookups.get( WombatDomainLookupEnum.ALL_WEEK_IN_MONTH);
            lists[index++] = wombatDemoData.wombatLookups.get( WombatDomainLookupEnum.ALL_WHICH_SHIFT);
            lists[index++] = wombatDemoData.wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR);
            lists[index++] = wombatDemoData.wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY);
            lists[index++] = wombatDemoData.wombatLookups.get( WombatDomainLookupEnum.ALL_SEX);
            lists[index++] = wombatDemoData.wombatLookups.get( WombatDomainLookupEnum.ALL_FLEXIBILITY);
            lists[13] = wombatDemoData.wombatLookups.get( WombatDomainLookupEnum.ALL_OVERRIDE);
            for(int i = 0; i < lists.length; i++)
            {
                List list = lists[i];
                dataStore.addExtent(list);
            }
            MemoryData data = new MemoryData(POJOWombatData.CLASSES, lists);
            Err.pr(SdzNote.EMP_ERRORS, "To do connect to " + data);
            SdzEntityManagerI pmI = EntityManagerFactory.createSdzEMI(data);
            dataStore.setEM(pmI, err);
            setDone(true);
            Err.pr(SdzNote.BO_STUFF, "Have populated memory DB");
        }
    }
}
