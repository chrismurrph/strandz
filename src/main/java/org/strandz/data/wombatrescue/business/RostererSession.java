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
package org.strandz.data.wombatrescue.business;

import org.strandz.data.util.ManagerI;
import org.strandz.data.util.ServiceTypeEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.util.RosterUtils;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.persist.SdzEMAssert;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class RostererSession implements RostererSessionI, ManagerI
{
    private DataStore ds;
    private RosterServiceI rosterService;
    private AbstractClientUploadRosterService uploadRosterService;
    private String name;
    private transient int id;
    public transient static int constructedTimes;
    
    public RostererSession( boolean rosterServicesRequired)
    {
        constructedTimes++;
        id = constructedTimes;
        if(id == 0)
        {
            Err.stack();
        }
        if(rosterServicesRequired)
        {
            String propertyValue = RosterSessionUtils.getProperty( "thickClient"); 
            if(propertyValue.equals( "true"))
            {
                rosterService = RosterServicesFactory.newRosterService( ServiceTypeEnum.THICK);
                uploadRosterService = RosterServicesFactory.newUploadRosterService( ServiceTypeEnum.THICK);
            }
            else if(propertyValue.equals( "false"))
            {
                rosterService = RosterServicesFactory.newRosterService( ServiceTypeEnum.THIN);
                uploadRosterService = RosterServicesFactory.newUploadRosterService( ServiceTypeEnum.THIN);
            }
            else
            {
                Err.error( "Boolean property <thickClient> is set to " + propertyValue);
            }
        }
    }
    
    public void init(DataStore ds)
    {
        Assert.notNull( rosterService, "Cannot init a ParticularRoster that has no rosterService");
        Assert.notNull( ds, "Must init a RostererSession with a DataStore");
        this.ds = ds;
        init();
        RosterServicesFactory.init( rosterService, ds);
        RosterServicesFactory.init( uploadRosterService, ds);
    }

    private void init()
    {
        Err.pr( WombatNote.NO_EM_WHEN_SOME_QUERY, "Doing init in " + this.getClass().getName());
        if(!ds.isOnTx())
        {
            ds.startTx( "Doing init in " + this.getClass().getName());
            Err.pr(SdzNote.BO_STUFF, "We have needed to force the start of a transaction for " + this + " using " + ds);
        }
        else
        {
            Err.pr( WombatNote.NO_EM_WHEN_SOME_QUERY, "... but are already on a transaction");            
        }
    }
    
    public List getWorkers()
    {
        init();
        List result = (ds.getDomainQueries()).executeRetList(WombatDomainQueryEnum.ALL_WORKER);
        SdzEMAssert.isEntityManaged( result, "getWorkers()");
        return result;
    }

    public List<WorkerI> getRosterableWorkers()
    {
        init();
        List<WorkerI> queryResult = (ds.getDomainQueries()).executeRetList(WombatDomainQueryEnum.ROSTERABLE_WORKERS);
        List<WorkerI> result = new ArrayList<WorkerI>();
        result.addAll( queryResult);
        Collections.sort( result, RosterUtils.SEARCH_BY);
        SdzEMAssert.isEntityManaged( result, "getRosterableWorkers()");
        return result;
    }
    
    public List getGroupWorkers()
    {
        List result = null;
        init();
        result = (ds.getDomainQueries()).executeRetList(WombatDomainQueryEnum.GROUP_WORKERS);
        Collections.sort( result, RosterUtils.SEARCH_BY);
        return result;
    }

    public List getRosterSlots()
    {
        List result;
        init();
        result = (ds.getDomainQueries()).executeRetList(WombatDomainQueryEnum.ALL_ROSTER_SLOT_O_BY_WKER);
        //Can cause "Cannot read fields from a deleted object"
        //Print.prList( result, "queryAllRosterSlotsOByWorker()");
        return result;
    }

    /*
    public WorkerI getDummyWorker()
    {
        WorkerI result = null;
        init();
        result = (WorkerI)ds.getDomainQueries().executeRetObject(WombatDomainQueryEnum.NULL_WORKER);
        return result;
    }
    */

    public List getBuddyManagers()
    {
        init();
        return ds.getDomainQueries().executeRetList(WombatDomainQueryEnum.BUDDY_MANAGERS);
    }
    
    public LookupsI getLookups()
    {
        init();
        Err.pr( WombatNote.NO_EM_WHEN_SOME_QUERY, "Doing getLookups() in ParticularRoster");
        LookupsI result = (LookupsI) (ds.getDomainQueries()).executeRetObject
            (WombatDomainQueryEnum.LOOKUPS);
        return result;
    }

    public DataStore getDataStore()
    {
        Assert.notNull( ds, "Have not called init() on " + this.getClass().getName() + " with id " + id);
        return ds;
    }
        
    public boolean isUploadRosterServiceImplemented()
    {
        return uploadRosterService.isImplemented();    
    }
    
    public UploadRosterServiceI getUploadRosterService()
    {
        return uploadRosterService;    
    }
    
    public RosterServiceI getRosterService()
    {
        Assert.notNull( rosterService, "No rosterService on " + this.getClass().getName() + " with id " + id);
        return rosterService;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
