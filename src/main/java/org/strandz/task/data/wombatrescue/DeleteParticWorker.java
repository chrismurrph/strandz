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
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.AbstractWRTask;

import javax.swing.SwingUtilities;
import java.util.Iterator;
import java.util.List;

public class DeleteParticWorker extends AbstractWRTask
{
    private static final boolean COMMIT_POPULATION = false;
    private static final String PARTICULAR_NAME = "Jen Winters";

    public static void main(String s[])
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                DeleteParticWorker obj = new DeleteParticWorker();
                obj.processParams( WombatConnectionEnum.REMOTE_WOMBAT_KODO,
                                   "Delete User " + PARTICULAR_NAME + " Logon");
                System.exit(0);
            }
        });
    }

    public void update(EntityManagedDataStore data)
    {
        List list = (List) data.get(POJOWombatData.WORKER);
        int timesDudFound = 0;
        int timesDummyFound = 0;
        int timesParticularFound = 0;
        Worker particular = null;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Worker vol = (Worker) iter.next();
            if(vol == null)
            {
                Err.error("The DB should never contain a null worker");
            }
            else if(vol.isDummy())
            {
                timesDummyFound++;
            }
            else if(!vol.isDummy() && vol.toString() == null)
            {
                timesDudFound++;
            }
            else if(!vol.isDummy() && vol.getToLong().indexOf(PARTICULAR_NAME) != -1 && vol.getFlexibility() == null)
            {
                particular = vol;                
                if(COMMIT_POPULATION)
                {
                    //When have many, uncomment this
                    Err.pr("To delete " + particular);
                    data.getEM().deletePersistent( particular);
                }
                timesParticularFound++;
            }
        }
        Err.pr("Have found " + timesDudFound + " dud workers");
        Err.pr("And " + timesDummyFound + " dummy workers");
        Err.pr("And " + timesParticularFound + " of " + PARTICULAR_NAME);
        if(COMMIT_POPULATION && timesParticularFound == 1)
        {
            Err.pr("To delete " + particular);
            data.getEM().deletePersistent(particular);
        }
    }
}
