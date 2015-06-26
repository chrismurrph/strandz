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

import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import java.util.Iterator;
import java.util.List;

/**
 * To correct problem where the name column in each Flexibility object
 * happened to contain null values, yet they had IDs which were
 * referenced everywhere. Note that Flexibility is a LOVs table so
 * has Application Identity, in JDO parlance.
 * <p/>
 * UPDATE
 * As run the application these NULL values are substituted in. ie. we
 * really need to solve the root cause of this problem.
 */
public class AdjustFlexibility
{
    private static DataStore dataStore;
    private static boolean commitPopulation = true;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"POJOWombatData", WombatConnectionEnum.DEBIAN_PROD_TERESA.getName()};
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
//      if(WombatConnectionEnum.getFromName( s[1]).isProduction())
//      {
//        Err.error(
//            "Cannot work with the " + WombatConnectionEnum.getFromName( s[1])
//            + " database");
//      }
            if(s.length == 2)
            {
                DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
                dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[1]));
                dataStore = dataStoreFactory.getDataStore();
            }
            else
            {
                Err.error();
            }
            dataStore.startTx();
            update(dataStore);
            if(!commitPopulation)
            {
                dataStore.rollbackTx();
            }
            else
            {
                dataStore.commitTx();
            }
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void update(DataStore data)
    {
        List flexibilityList = (List) dataStore.get(POJOWombatData.FLEXIBILITY);
        for(Iterator iter = flexibilityList.iterator(); iter.hasNext();)
        {
            Flexibility flexibility = (Flexibility) iter.next();
            int id = flexibility.getPkId();
            //Object id = JDOHelper.getObjectId( flexibility);
            //Err.pr( "oid type is " + id.getClass().getName());
            if(flexibility.getName() != null && flexibility.getName().equals("NULL"))
            {
                Err.error("Weird result that flexibility name is NULL");
            }
            else if(flexibility.getName() != null && flexibility.getName().equals(""))
            {
                Err.error("Weird result that flexibility name is a blank String");
            }
            else if(flexibility.getName() == null)
            {
                if(id == 0)
                {
                    flexibility.setName(Flexibility.NULL.getName());
                }
                else if(id == 1)
                {
                    flexibility.setName(Flexibility.NO_OVERNIGHTS.getName());
                }
                else if(id == 2)
                {
                    flexibility.setName(Flexibility.NO_EVENINGS.getName());
                }
                else if(id == 3)
                {
                    flexibility.setName(Flexibility.FLEXIBLE.getName());
                }
                Err.pr("flexibility now: " + flexibility.getName() + " for id " + id);
            }
            else
            {
                Err.pr("flexibility already ok: " + flexibility.getName() + " for id " + id);
            }
            Err.pr("");
        }
    }
}
