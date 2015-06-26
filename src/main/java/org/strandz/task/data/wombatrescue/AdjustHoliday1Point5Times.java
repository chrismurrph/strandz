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

import org.strandz.data.wombatrescue.objects.Volunteer;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.store.wombatrescue.WombatConnections;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AdjustHoliday1Point5Times
{
    private static boolean displayOnly = false;
    private static DataStore dataStore;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"POJOWombatData", WombatConnections.DEFAULT_DATABASE.getName()};
            processParams(str);
        }
        System.exit(0);
    }

    private static void processParams(String s[])
    {
         /**/
        if(s[0].equals("POJOWombatData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database");
            }
             /**/
            if(WombatConnectionEnum.getFromName(s[1]).isProduction())
            {
                Err.error(
                    "Cannot work with the " + WombatConnectionEnum.getFromName(s[1])
                        + " database");
            }
             /**/
            if(s.length == 2)
            {
                DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
                dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[1]));
                dataStore = dataStoreFactory.getDataStore();
                //td = WombatDataFactory.getNewInstance( WombatConnectionEnum.getFromName( s[1]));
            }
            else
            {
                Err.error();
            }
            dataStore.startTx();
            display(dataStore);
            dataStore.commitTx();
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static Date holidayTimeNeedsAdjusting(Date date, String text)
    {
        Date result = null;
        int hours = TimeUtils.getHours(date);
        long time = date.getTime();
        Err.pr(text + " date has time " + date);
        if(hours != 0)
        {
            Err.pr("\thours " + hours);
            Err.pr("\ttime " + time);

            Date newDate = AdjustRosterSlot1Point5Times.minusTwelveHours(date);
            Err.pr("\t     " + newDate.getTime());
            Err.pr("\t     " + newDate);
            if(!displayOnly)
            {
                result = newDate;
            }
        }
        return result;
    }

    private static void display(DataStore data)
    {
        List list = (List) data.get(POJOWombatData.WORKER);
        int timesFound = 0;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Volunteer volunteer = (Volunteer) iter.next();
            Date adjustTo = null;
            if(volunteer.getAway1Start() != null)
            {
                adjustTo = holidayTimeNeedsAdjusting(volunteer.getAway1Start(),
                    "First holiday start for " + volunteer);
                if(adjustTo != null)
                {
                    volunteer.setAway1Start(adjustTo);
                }
            }
            if(volunteer.getAway1End() != null)
            {
                adjustTo = holidayTimeNeedsAdjusting(volunteer.getAway1End(),
                    "First holiday end for " + volunteer);
                if(adjustTo != null)
                {
                    volunteer.setAway1End(adjustTo);
                }
            }
            if(volunteer.getAway2Start() != null)
            {
                adjustTo = holidayTimeNeedsAdjusting(volunteer.getAway2Start(),
                    "Second holiday start for " + volunteer);
                if(adjustTo != null)
                {
                    volunteer.setAway2Start(adjustTo);
                }
            }
            if(volunteer.getAway2End() != null)
            {
                adjustTo = holidayTimeNeedsAdjusting(volunteer.getAway2End(),
                    "Second holiday end for " + volunteer);
                if(adjustTo != null)
                {
                    volunteer.setAway2End(adjustTo);
                }
            }
        }
    }
}
