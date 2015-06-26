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
package org.strandz.data.wombatrescue.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.data.objects.DayInWeekI;

import java.io.Serializable;

public class BuddyManager implements Comparable, Serializable, BuddyManagerI
{
    private Worker worker;
    private DayInWeek dayInWeek;
    private WhichShift whichShift;
    private transient static int constructedTimes;
    public transient int id;

    public BuddyManager()
    {
        constructedTimes++;
        id = constructedTimes;
    }

    public int compareTo(Object o)
    {
        int result;
        BuddyManager other = (BuddyManager) o;
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
        result = Utils.compareTo(dayInWeek, other.getDayInWeek());
        if(result == 0)
        {
            result = Utils.compareTo(whichShift, other.getWhichShift());
            if(result == 0)
            {
                result = Utils.compareTo(worker, other.getWorker());
            }
        }
        return result;
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        String txt = "BuddyManager " + this;
        ReasonNotEquals.addClassVisiting(txt);

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof BuddyManager))
        {
            ReasonNotEquals.addReason("not an instance of a BuddyManager");
        }
        else
        {
            BuddyManager test = (BuddyManager) o;
            if(Utils.equals(dayInWeek, test.getDayInWeek()))
            {
                if(Utils.equals(whichShift, test.getWhichShift()))
                {
                    if(Utils.equals(worker, test.getWorker()))
                    {
                        result = true;
                    }
                    else
                    {
                        ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("worker", test, this));
                    }
                }
                else
                {
                    ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("whichShift", test, this));
                }
            }
            else
            {
                ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("dayInWeek", test, this));
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = Utils.hashCode(result, dayInWeek);
        result = Utils.hashCode(result, whichShift);
        result = Utils.hashCode(result, worker);
        return result;
    }

    public String toString()
    {
        String result = null;
        result = "BM: " + getDayInWeek() + " " + getWhichShift() + " " + getWorker();
        return result;
    }

    /*
    public Volunteer getVolunteer()
    {
      Err.error( "BuddyManagers no longer have volunteers");
      return null;
    }
    */
    /*
    public void setVolunteer( Volunteer volunteer )
    {
      this.volunteer = volunteer;
    }
    */

    public WorkerI getWorker()
    {
        //Err.pr( "worker in <" + id + "> being returned is <" + this.worker.getChristianName() + ">");
        return worker;
    }

    public void setWorker(WorkerI worker)
    {
        this.worker = (Worker)worker;
        //Err.pr( "worker in <" + id + "> been set to <" + this.worker.getChristianName() + ">");
    }

    public DayInWeekI getDayInWeek()
    {
        return dayInWeek;
    }

    public void setDayInWeek(DayInWeekI dayInWeek)
    {
        this.dayInWeek = (DayInWeek)dayInWeek;
    }

    public WhichShiftI getWhichShift()
    {
        return whichShift;
    }

    public void setWhichShift(WhichShiftI whichShift)
    {
        this.whichShift = (WhichShift)whichShift;
    }
}
