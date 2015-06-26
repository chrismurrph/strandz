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
import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

public class Flush
{
    private static final boolean REFILL_LOVS_ONLY = false;
    private static final boolean DONT_FILL_LOVS = true;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str [] = { "POJOWombatData", WombatConnectionEnum.WOMBAT_KODO.getName()};
            //String str[] = {"POJOWombatData", WombatConnections.DEFAULT_DATABASE.getName()};
            processParams(str);
            // Err.pr( "Specify an AbstData subclass as param to purge it");
        }
    }

    /**
     * Like removing all the records of a table but
     * leaving the table there
     */
    private static void processParams(String s[])
    {
        if(s[0].equals("POJOWombatData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database to flush");
            }
            if(!REFILL_LOVS_ONLY)
            {
                if(WombatConnectionEnum.getFromName(s[1]).isProduction())
                {
                    Err.error(
                        "Cannot work with the " + WombatConnectionEnum.getFromName(s[1])
                            + " database");
                }
            }
            else
            {
                if(s[1].equals(WombatConnectionEnum.BACKUP))
                {
                    Err.error("REFILL_LOVS_ONLY s/not be set, want to clear the backup");
                }
            }

            DataStore dataStore = null;
            String txt = "Flushed POJOWombatData using " + s[1];
            if(s.length == 2)
            {
                DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
                dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[1]));
                dataStore = dataStoreFactory.getDataStore();
            }
            else
            {
                Err.error();
                //td = WombatDataFactory.getNewInstance( WombatConnectionEnum.getFromName( s[1]), s[2]).getData();
                //txt += ", " + s[2];
            }
            if(!REFILL_LOVS_ONLY)
            {
                dataStore.flush();
            }
            if(!DONT_FILL_LOVS)
            {
                fillLOVs(dataStore);
            }
            Print.pr(txt);
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void fillLOVs(DataStore data)
    {
        // Err.error( "Needs fixed b4 call again");
        data.rollbackTx();
        data.startTx();
        // For XML needed to use new ArrayList() as result of Arrays.asList() did not
        // work for XML
        /*
        data.update(POJOWombatData.DAY_IN_WEEK,
            new ArrayList(Arrays.asList(DayInWeek.OPEN_VALUES)));
        data.update(POJOWombatData.WHICH_SHIFT,
            new ArrayList(Arrays.asList(WhichShift.OPEN_VALUES)));
        data.update(POJOWombatData.WEEK_IN_MONTH,
            new ArrayList(Arrays.asList(WeekInMonth.OPEN_VALUES)));
        data.update(POJOWombatData.INTERVAL,
            new ArrayList(Arrays.asList(NumDaysInterval.OPEN_VALUES)));
        data.update(POJOWombatData.MONTH_IN_YEAR,
            new ArrayList(Arrays.asList(MonthInYear.OPEN_VALUES)));
        data.update(POJOWombatData.SENIORITY,
            new ArrayList(Arrays.asList(Seniority.OPEN_VALUES)));
        data.update(POJOWombatData.SEX,
            new ArrayList(Arrays.asList(Sex.OPEN_VALUES)));
        data.update(POJOWombatData.OVERRIDE,
            new ArrayList(Arrays.asList(Override.OPEN_VALUES)));
        data.update(POJOWombatData.FLEXIBILITY,
            new ArrayList(Arrays.asList(Flexibility.OPEN_VALUES)));
        */
        WombatLookups lookups = new WombatLookups();
        lookups.initValues();
        data.set( POJOWombatData.LOOKUPS, Utils.formList( lookups));
        data.commitTx();
    }
}
