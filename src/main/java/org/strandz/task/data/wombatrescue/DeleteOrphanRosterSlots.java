package org.strandz.task.data.wombatrescue;

import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.RosterSlot;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.AbstractWRTask;

import javax.swing.SwingUtilities;
import java.util.Iterator;
import java.util.List;

/**
 * Are finding bad data in production where a roster slot does not have a Worker.
 * This task is used directly against production to find them and delete them.
 * Stack trace that app will throw is assert in ParticularRoster.getNonDisabledSlotsOfWorker():
 * "A rosterSlot has a null worker:".
 */
public class DeleteOrphanRosterSlots extends AbstractWRTask
{
    public static void main(String s[])
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run() 
            {
                DeleteOrphanRosterSlots obj = new DeleteOrphanRosterSlots();
                obj.processParams( WombatConnectionEnum.REMOTE_WOMBAT_KODO,
                                   "Display Orphan RosterSlots Logon");
                System.exit(0);
            }
        });
    }

    public void update( EntityManagedDataStore dataStore)
    {
        DomainQueriesI queriesI = dataStore.getDomainQueries();
        List list = queriesI.executeRetList(WombatDomainQueryEnum.ALL_ROSTER_SLOT);
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            RosterSlot rosterSlot = (RosterSlot) iter.next();
            WorkerI worker = rosterSlot.getWorker();
            if(worker != null)
            {
                Err.pr( "Ok RS, has worker: " + worker);
            }
            else
            {
                Err.pr( "Not ok RS, has no worker: " + rosterSlot);
                dataStore.getEM().deletePersistent( rosterSlot);
            }
        }
    }
}
