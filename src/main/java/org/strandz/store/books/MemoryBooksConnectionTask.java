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
package org.strandz.store.books;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.MemoryData;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.persist.EntityManagerFactory;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.AbstractTask;
import org.strandz.lgpl.util.Err;

import java.util.List;

public class MemoryBooksConnectionTask extends AbstractTask
{
    private EntityManagedDataStore dataStore;

    public MemoryBooksConnectionTask(MemoryBooksDataStore dataStore)
    {
        Err.pr(SdzNote.EMP_ERRORS, "In MemoryBooksConnectionTask constructor with dataStore: " + dataStore);
        this.dataStore = dataStore;
        setTitle("Simulated Connecting...");
        setCompletedPhrase("Connected");
        setDuration(dataStore.getEstimatedConnectDuration());
    }

    public Object newTask()
    {
        return new MemoryBooksConnectionTask.ActualTask();
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
            BooksDemoData demoData = BooksDemoData.getInstance();
            List lists[] = new List[2];
            lists[0] = demoData.newExpenses;
            lists[1] = demoData.expenseTypeLovs;
            for(int i = 0; i < lists.length; i++)
            {
                List list = lists[i];
                dataStore.addExtent(list);
            }
            MemoryData data = new MemoryData(BooksData.CLASSES, lists);
            Err.pr(SdzNote.EMP_ERRORS, "To do connect to " + data);
            SdzEntityManagerI pmI = EntityManagerFactory.createSdzEMI(data);
            dataStore.setEM(pmI, err);
            setDone(true);
        }
    }
}
