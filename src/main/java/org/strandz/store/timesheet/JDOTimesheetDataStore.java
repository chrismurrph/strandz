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
package org.strandz.store.timesheet;

import org.strandz.lgpl.store.JDODataStore;
import org.strandz.lgpl.util.Err;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

class JDOTimesheetDataStore extends JDODataStore implements TimesheetQueries
{
    public JDOTimesheetDataStore(String dbName)
    {
        super.setClasses(TimesheetData.CLASSES);

        Properties props = null;
        if(dbName.equals("wombat"))
        {
            Err.error( "Depreciated code style");                        
            //props = WombatConnectionUtils.get_DUMMY_Properties("getMySql_wombat_Properties");
        }
        else if(dbName.equals("TEST"))
        {
            Err.error( "Depreciated code style");                        
            //props = WombatConnectionUtils.get_DUMMY_Properties("getMySql_TEST_Properties");
        }
        else if(dbName.equals("DEV"))
        {
            Err.error( "Depreciated code style");                        
            //props = WombatConnectionUtils.get_DUMMY_Properties("getMySql_DEV_Properties");
        }
        else
        {
            Err.error("Do not yet support a connection to a database named " + dbName);
        }
        super.setConnectionProperties(props);
    }

    public Collection queryAllTimesheets()
    {
        Query q = ((PersistenceManager) getEM().getActualEM()).newQuery(TimesheetData.TIMESHEET);
        Collection result = null;
        // q.setOrdering( "groupName ascending, surname ascending");
        result = (Collection) q.execute();
        return result;
    }

    public Collection queryAllTimesheetsOf(String resourceName, Date today)
    {
        Err.error("Not yet implemented");
        return null;
    }
    
    protected void initLookups()
    {
        Err.pr("initLookups()" + " not configured in " + this.getClass().getName());
    }
}
