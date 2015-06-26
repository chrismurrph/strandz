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
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.store.MemoryConnectionInfo;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.TaskI;
import org.strandz.lgpl.util.TaskTimeBandMonitorI;
import org.strandz.lgpl.widgets.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

public class MemoryBooksDataStore extends EntityManagedDataStore
{
    public MemoryBooksDataStore(MemoryConnectionInfo connection)
    {
        super.setClasses(BooksData.CLASSES);
        setName(connection.getName());
        setEstimatedConnectDuration(connection.getEstimatedConnectDuration());
        setEstimatedLookupDataDuration(connection.getEstimatedLoadLookupDataDuration());
        setDomainQueries( new MemoryBooksDomainQueries());
        Err.pr(SdzNote.EMP_ERRORS, "MemoryBooksDataStore constructor: " + connection);
    }

    public void startTx( String reason)
    {
        super.startTx( reason);
        if(getEM() == null)
        {
            TaskTimeBandMonitorI monitor = WidgetUtils.getTimeBandMonitor(getEstimatedConnectDuration());
            TaskI task = new MemoryBooksConnectionTask(this);
            ErrorMsgContainer err = new ErrorMsgContainer( "Connecting to " + getName()); 
            monitor.start(task, err);
            if(err.isInError)
            {
                Err.error( err.message);
            }
            monitor.stop();
            //emc._setEntityManager( getEM());
        }
    }

    public void set(int whichExtent, Object obj)
    {
        Err.error("Unlikely that set() required");
    }

    public void flush()
    {
        Err.error("Unlikely that flush() required");
    }

    public List query(Class clazz)
    {
        Err.pr("Asked for a list of " + clazz.getName());
        return new ArrayList();
    }
    
    public void flush(Class classes[])
    {
        Err.error("flush()" + " not implemented");
    }
    
    protected void initLookups()
    {
        Err.pr("initLookups()" + " not configured in " + this.getClass().getName());
    }
}
