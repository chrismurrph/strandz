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
import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.WombatConnections;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MakeNullVolunteer
{
    private static DataStore dataStore;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"DevData", WombatConnections.DEFAULT_DATABASE.getName()};
            processParams(str);
        }
        System.exit(0);
    }

    public static void processParams(String s[])
    {
         /**/
        if(s[0].equals("DevData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database");
            }
            /*
            * Dangerous!
            *
            if(WombatConnectionEnum.getFromName( s[1]).isProduction())
            {
            Err.error( "Cannot work with the " + WombatConnectionEnum.getFromName( s[1]) +
            " database");
            }
            **/
            if(s.length == 2)
            {
                // ok, this is a hack
                //td = WombatDataFactory.getSetInstance();
                // td = WombatrescueApplicationData.getNewInstance( s[1]);
                DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
                dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[1]));
                dataStore = dataStoreFactory.getDataStore();
            }
            else
            {
                Err.error();
            }
            /*
            try
            {
            JDOSupport.init( null);
            }
            catch(Exception ex)
            {
            Err.error( ex);
            }
            */
            dataStore.startTx();

            List list = (List) dataStore.get(POJOWombatData.WORKER);
            // Query q = JDOSupport.getPM().newQuery( POJOWombatData.VOLUNTEER);
            // Collection c = (Collection)q.execute();
            update(list);
            // JDOSupport.commit();
            dataStore.commitTx();
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void update(Collection list)
    {
        int timesFound = 0;
        Volunteer nullVol = null;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Volunteer vol = (Volunteer) iter.next();
            if(
                // (
                // vol.getChristianName() != null &&
                // vol.getChristianName().equals( "Bart")
                // ) ||
                vol.equals(Volunteer.NULL)
                )
            {
                timesFound++;
                if(timesFound > 1)
                {
                    Err.error("Should only be one NULL volunteer");
                }
                nullVol = vol;
                Err.pr("NULL vol have found is " + nullVol);
            }
        }
        if(nullVol == null)
        {
            Err.error("Could not find a volunteer equal to NULL");
        }
        else
        {
            nullVol.setDummy(true);
            Err.pr(JDONote.APPEARS_CONSTRUCTOR_NOT_CALLED,
                "Why have to do this - Volunteer's constructor s/do it");
            if(nullVol.getBelongsToGroup() != null)
            {
                Err.pr(JDONote.APPEARS_CONSTRUCTOR_NOT_CALLED,
                    "appears Constructor was called");
            }
            else
            {
                Err.pr(JDONote.APPEARS_CONSTRUCTOR_NOT_CALLED,
                    "appears Constructor was NOT called");
            }
            nullVol.setBelongsToGroup(nullVol);
            Err.pr("MADE A NULL VOLUNTEER, " + nullVol);
        }
    }
}
