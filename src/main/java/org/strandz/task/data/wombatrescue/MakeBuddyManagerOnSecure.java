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
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.BuddyManager;
import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.AbstractWRTask;
import org.strandz.applic.wombatrescue.StartupKodo;

import javax.swing.SwingUtilities;

public class MakeBuddyManagerOnSecure extends AbstractWRTask
{
    private static final boolean LIVE = true;
    
    public static void main(String s[])
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run() 
            {
                MakeBuddyManagerOnSecure obj = new MakeBuddyManagerOnSecure();
                WombatConnectionEnum wombatConnectionEnum;
                if(LIVE)
                {
                    wombatConnectionEnum = WombatConnectionEnum.REMOTE_WOMBAT_KODO;
                }
                else
                {
                    wombatConnectionEnum = WombatConnectionEnum.REMOTE_DEMO_WOMBAT_KODO;
                }
                obj.processParams( wombatConnectionEnum, "Make Buddy Manager Logon on LIVE: " + LIVE);
                System.exit(0);
            }
        });
    }

    public String getUsername()
    {
        if(!StartupKodo.JUST_POPULATED || LIVE)
        {
            return super.getUsername();
        }
        else
        {
            return "Mike";
        }
    }

    public String getPassword()
    {
        if(!StartupKodo.JUST_POPULATED || LIVE)
        {
            return super.getPassword();
        }
        else
        {
            return "Mike";
        }
    }

    public void update( EntityManagedDataStore dataStore)
    {
        DomainQueriesI queriesI = dataStore.getDomainQueries();
        WombatLookups wombatLookups = (WombatLookups)queriesI.executeRetObject( WombatDomainQueryEnum.LOOKUPS);
        DayInWeek thu = (DayInWeek) wombatLookups.getByName(WombatDomainLookupEnum.ALL_DAY_IN_WEEK, "Thursday");
        //
        WhichShift dinner = (WhichShift) wombatLookups.getByName(WombatDomainLookupEnum.ALL_WHICH_SHIFT, "dinner");
        WhichShift overnight = (WhichShift) wombatLookups.getByName(WombatDomainLookupEnum.ALL_WHICH_SHIFT, "overnight");
        //
        createBuddyManager( dataStore, queriesI, "Sean", "Mullin" /*"Michael", "Bucket"*/, thu, dinner);
        createBuddyManager( dataStore, queriesI, "Sean", "Mullin" /*"Michael", "Bucket"*/, thu, overnight);
    }
    
    private static void createBuddyManager( EntityManagedDataStore dataStore, 
                                            DomainQueriesI queriesI, String n1, String n2, 
                                            DayInWeek dayInWeek, WhichShift whichshift)
    {
        Worker vol = (Worker) queriesI.executeRetObject(
            WombatDomainQueryEnum.WORKER_BY_NAMES, n1, n2);
        if(vol == null)
        {
            Err.error("Could not find a volunteer called " + n1 + " " + n2);
        }
        BuddyManager buddyManager = new BuddyManager();
        buddyManager.setDayInWeek(dayInWeek);
        buddyManager.setWhichShift(whichshift);
        buddyManager.setWorker(vol);
        dataStore.getEM().registerPersistent(buddyManager);
    }
}
