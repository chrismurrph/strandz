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
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.FlexibilityI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import java.util.Iterator;
import java.util.List;

public class UpdateAcrossDBs
{
    private static boolean commitPopulation = false;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            Err.error("UpdateAcrossDBs requires no command line parameters");
        }
        else
        {
            process();
        }
        System.exit(0);
    }

    private static void process()
    {
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection(WombatConnectionEnum.DEBIAN_PROD_TERESA); //at 0
        dataStoreFactory.addConnection(WombatConnectionEnum.DEBIAN_DEV_TERESA); //at 1
        DataStore source = dataStoreFactory.getDataStore(0);
        DataStore target = dataStoreFactory.getDataStore(1);
        source.startTx();
        target.startTx();
        update(source, target);
        source.rollbackTx();
        if(!commitPopulation)
        {
            target.rollbackTx();
        }
        else
        {
            target.commitTx();
        }
    }

    private static void update(DataStore source, DataStore target)
    {
        List sourceList = (List) source.get(POJOWombatData.WORKER);
        List targetList = (List) target.get(POJOWombatData.WORKER);
        List targetFlexibilities = (List) target.get(POJOWombatData.FLEXIBILITY);
        int count = 0;
        for(Iterator iter = targetList.iterator(); iter.hasNext();)
        {
            Worker targetWker = (Worker) iter.next();
            Worker sourceWker = findSameNamed(targetWker, sourceList);
            if(sourceWker != null)
            {
                FlexibilityI targetFlexibility = targetWker.getFlexibility();
                FlexibilityI sourceFlexibility = sourceWker.getFlexibility();
                if(targetFlexibility.equals(Flexibility.NULL))
                {
                    if(!sourceFlexibility.equals(Flexibility.NULL))
                    {
                        //the two DBs out of sync
                        Err.pr(targetWker + "\t update flexibility to " + sourceFlexibility);
                        if(commitPopulation)
                        {
                            Flexibility newFlexibility = (Flexibility) Utils.getFromList(targetFlexibilities, sourceFlexibility);
                            targetWker.setFlexibility(newFlexibility);
                        }
                    }
                }
                else
                {
                    if(!sourceFlexibility.equals(targetFlexibility))
                    {
                        Err.alarm("\tNEED DECIDE if to update " + targetWker + " to " +
                            sourceFlexibility + " when currently " + targetFlexibility);
                    }
                    Err.pr(targetWker + " already has flexibility " + targetFlexibility);
                }
            }
        }
    }

    static Worker findSameNamed(Worker findWorker, List workers)
    {
        Worker result = null;
        for(Iterator iterator = workers.iterator(); iterator.hasNext();)
        {
            Worker worker = (Worker) iterator.next();
            if(worker != null && worker.getToLong() != null) //source list can be corrupt
            {
                if(worker.getToLong().equals(findWorker.getToLong()))
                {
                    result = worker;
                    break;
                }
            }
        }
        return result;
    }
}
