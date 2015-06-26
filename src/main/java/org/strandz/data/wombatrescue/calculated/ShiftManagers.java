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
package org.strandz.data.wombatrescue.calculated;

import org.strandz.data.wombatrescue.objects.BuddyManager;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.BuddyManagerI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import java.util.Iterator;
import java.util.List;

public class ShiftManagers
{
    private List buddyManagers;
    //private DomainQueriesI queriesI;

    public ShiftManagers(List buddyManagers /*, DomainQueriesI queriesI*/)
    {
        this.buddyManagers = buddyManagers;
        //this.queriesI = queriesI;
    }

    public WorkerI getMonDinnerWker()
    {
        return getWorker(DayInWeek.MONDAY, WhichShift.DINNER);
    }

    public void setMonDinnerWker(WorkerI monDinner)
    {
        setWorker(DayInWeek.MONDAY, WhichShift.DINNER, monDinner);
    }

    public WorkerI getMonOvernightWker()
    {
        return getWorker(DayInWeek.MONDAY, WhichShift.OVERNIGHT);
    }

    public void setMonOvernightWker(WorkerI monOvernight)
    {
        setWorker(DayInWeek.MONDAY, WhichShift.OVERNIGHT, monOvernight);
    }

    public WorkerI getTueDinnerWker()
    {
        return getWorker(DayInWeek.TUESDAY, WhichShift.DINNER);
    }

    public void setTueDinnerWker(WorkerI tueDinner)
    {
        setWorker(DayInWeek.TUESDAY, WhichShift.DINNER, tueDinner);
    }

    public WorkerI getTueOvernightWker()
    {
        return getWorker(DayInWeek.TUESDAY, WhichShift.OVERNIGHT);
    }

    public void setTueOvernightWker(WorkerI tueOvernight)
    {
        setWorker(DayInWeek.TUESDAY, WhichShift.OVERNIGHT, tueOvernight);
    }

    public WorkerI getWedDinnerWker()
    {
        return getWorker(DayInWeek.WEDNESDAY, WhichShift.DINNER);
    }

    public void setWedDinnerWker(WorkerI wedDinner)
    {
        setWorker(DayInWeek.WEDNESDAY, WhichShift.DINNER, wedDinner);
    }

    public WorkerI getWedOvernightWker()
    {
        return getWorker(DayInWeek.WEDNESDAY, WhichShift.OVERNIGHT);
    }

    public void setWedOvernightWker(WorkerI wedOvernight)
    {
        setWorker(DayInWeek.WEDNESDAY, WhichShift.OVERNIGHT, wedOvernight);
    }

    public WorkerI getThuDinnerWker()
    {
        return getWorker(DayInWeek.THURSDAY, WhichShift.DINNER);
    }

    public void setThuDinnerWker(WorkerI thuDinner)
    {
        setWorker(DayInWeek.THURSDAY, WhichShift.DINNER, thuDinner);
    }

    public WorkerI getThuOvernightWker()
    {
        return getWorker(DayInWeek.THURSDAY, WhichShift.OVERNIGHT);
    }

    public void setThuOvernightWker(WorkerI thuOvernight)
    {
        setWorker(DayInWeek.THURSDAY, WhichShift.OVERNIGHT, thuOvernight);
    }
    
    public WorkerI getFriDinnerWker()
    {
        return getWorker(DayInWeek.FRIDAY, WhichShift.DINNER);
    }

    public void setFriDinnerWker(WorkerI friDinner)
    {
        setWorker(DayInWeek.FRIDAY, WhichShift.DINNER, friDinner);
    }

    public WorkerI getFriOvernightWker()
    {
        return getWorker(DayInWeek.FRIDAY, WhichShift.OVERNIGHT);
    }

    public void setFriOvernightWker(WorkerI friOvernight)
    {
        setWorker(DayInWeek.FRIDAY, WhichShift.OVERNIGHT, friOvernight);
    }

    public WorkerI getSatDinnerWker()
    {
        return getWorker(DayInWeek.SATURDAY, WhichShift.DINNER);
    }

    public void setSatDinnerWker(WorkerI satDinner)
    {
        setWorker(DayInWeek.SATURDAY, WhichShift.DINNER, satDinner);
    }

    public WorkerI getSatOvernightWker()
    {
        return getWorker(DayInWeek.SATURDAY, WhichShift.OVERNIGHT);
    }

    public void setSatOvernightWker(WorkerI satOvernight)
    {
        setWorker(DayInWeek.SATURDAY, WhichShift.OVERNIGHT, satOvernight);
    }

    private WorkerI getWorker(DayInWeek dayInWeek, WhichShift whichShift)
    {
        WorkerI result = null;
        for(Iterator iterator = buddyManagers.iterator(); iterator.hasNext();)
        {
            BuddyManagerI buddyManager = (BuddyManagerI) iterator.next();
            //Using getName() for non-POJOs
            if(buddyManager.getDayInWeek().getName().equals(dayInWeek.getName()) &&
                buddyManager.getWhichShift().getName().equals(whichShift.getName()))
            {
                result = buddyManager.getWorker();
                break;
            }
        }
        if(result == null)
        {
            Print.prList(buddyManagers, "BMs - where is " + dayInWeek + " and " + whichShift);
            Err.error("Could not find a Buddy Manager for " + dayInWeek + " " + whichShift);
        }
        return result;
    }

    private void setWorker(DayInWeek dayInWeek, WhichShift whichShift, WorkerI volunteer)
    {
        boolean found = false;
        for(Iterator iterator = buddyManagers.iterator(); iterator.hasNext();)
        {
            BuddyManagerI buddyManager = (BuddyManagerI) iterator.next();
            if(buddyManager.getDayInWeek().getName().equals(dayInWeek.getName()) &&
                buddyManager.getWhichShift().getName().equals(whichShift.getName()))
            {
                buddyManager.setWorker(volunteer);
                found = true;
                break;
            }
        }
        if(!found)
        {
            Err.error("Could not find a Buddy Manager for " + dayInWeek + " " + whichShift);
        }
    }
}
