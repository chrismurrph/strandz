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
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.persist.EntityManagerFactory;
import org.strandz.lgpl.util.AbstractTask;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;

public class ConnectionTask extends AbstractTask
{
    private EntityManagedDataStore dataStore;
    private ORMTypeEnum type;

    public ConnectionTask(EntityManagedDataStore dataStore, ORMTypeEnum type)
    {
        this.dataStore = dataStore;
        Assert.notNull( type, "No ORMTypeEnum specified");
        this.type = type;
        setTitle("Connecting...");
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
            Err.pr(SdzNote.EMP_ERRORS, "To do connect to " + dataStore.getName());
            SdzEntityManagerI emI = EntityManagerFactory.createSdzEMI(
                type, dataStore.getConnection().getConfigStringName(),
                dataStore.getConnection().getProperties());
            dataStore.setEM(emI, err);
            if(err == null || !err.isInError)
            {
                dataStore.postConnection();
                setDone(true);
            }
        }
    }
}
