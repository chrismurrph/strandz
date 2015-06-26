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
package org.strandz.applic.wombatrescue;

import org.strandz.core.domain.NodeChangeEvent;
import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.AccessEvent;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.Strand;
import org.strandz.core.record.GoToOperation;
import org.strandz.core.record.PlayerI;
import org.strandz.core.record.SimpleOperation;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.IdentifierI;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.WombatConnections;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import javax.jdo.PersistenceManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayParticularAgain
{
    private static DataStore dataStore;
    private static PersistenceManager pm;
    public static RosterWorkersStrand rv;
    public static String AGAIN = "play/lookup_disappears.xml";
    public static final String REFILL = "play/refill.xml";

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
    }

    public static DataStore getData()
    {
        if(dataStore == null)
        {
            String str[] = {};
            main(str);
        }
        return dataStore;
    }

    /**
     * Like removing all the records of a table but
     * leaving the table there!
     */
    private static void processParams(String s[])
    {
         /**/
        if(s[0].equals("POJOWombatData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database to replay against");
            }
            if(WombatConnectionEnum.getFromName(s[1]).isProduction())
            {
                Err.error(
                    "Cannot work with the " + WombatConnectionEnum.getFromName(s[1])
                        + " database");
            }
            if(s.length == 2)
            {
                //td = WombatDataFactory.getNewInstance( WombatConnectionEnum.getFromName( s[1]));
                DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
                dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(s[1]));
                dataStore = dataStoreFactory.getDataStore();
            }
            else
            {
                Err.error();
            }
            replay();
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void installActions()
    {
        ChangeNodeAction cnAction = new ChangeNodeAction();
        cnAction.putValue(Action.NAME, "Change Node");
        cnAction.putValue(Action.SHORT_DESCRIPTION, "Change Node");
        rv.getVolunteerAbilities().add(cnAction);
        rv.getRosterSlotAbilities().add(cnAction);

        //
        DebugDataAction ddAction = new DebugDataAction();
        ddAction.putValue(Action.NAME, "Debug Data");
        ddAction.putValue(Action.SHORT_DESCRIPTION, "Debug Data");
        rv.getVolunteerAbilities().add(ddAction);
        rv.getRosterSlotAbilities().add(ddAction);
    }

    private static void replay()
    {
        replay(new File(AGAIN), false);
    }

    public static void replay(File file, boolean commit)
    {
        Err.setBatch(false);
        rv = new RosterWorkersStrand();
        rv.sdzInit();

        //
        Strand strand = rv.getSbI().getStrand();
        strand.addNodeChangeListener(new LocalNodeChangeListener(rv, rv.getDt()));
        rv.preForm();
        installActions();
        // Err.pr( file);
        // Can't have even dispatch thread causing node changes when
        // running in batch mode (Did in designer, as not needed at all
        // for this application). Can't do dynamically yet!
        // rv.volunteerTriggers.rosterSlotNode.setFocusCausesNodeChange( false);
        //
        ((PlayerI) rv.getSbI().getStrand()).replayRecorded(file.getPath(), null,
            getStartupCode());
        if(commit)
        {
            strand.COMMIT_ONLY();
        }
    }

    public static List getStartupCode()
    {
        List result = new ArrayList();
        GoToOperation goTo = new GoToOperation();
        goTo.setNodeName("Volunteer Node");
        goTo.setOperation(OperationEnum.GOTO_NODE);
        result.add(goTo);

        SimpleOperation simple = new SimpleOperation();
        simple.setOperation(OperationEnum.EXECUTE_QUERY);
        result.add(simple);
        return result;
    }

    public static class LocalNodeChangeListener implements NodeChangeListener
    {
        private RosterWorkers_NEW_FORMATDT dt;
        private RosterWorkersStrand rv;

        public LocalNodeChangeListener(
            RosterWorkersStrand rv, RosterWorkers_NEW_FORMATDT dt)
        {
            this.rv = rv;
            this.dt = dt;
        }

        public void accessChange(AccessEvent event)
        {// not interested
        }

        public void nodeChangePerformed(NodeChangeEvent e)
        {
            Node current = (Node) e.getSource();
            if(current == dt.workerNode)
            {
                // Err.pr( "VOL: " + rv.sbI);
                rv.getSbI().setCurrentPane(0);
            }
            else if(current == dt.rosterSlotsListDetailNode)
            {
                // Err.pr( "SLOT rv: " + rv);
                // Err.pr( "SLOT rv.sbI: " + rv.sbI);
                rv.getSbI().setCurrentPane(1);
            }
        }

        public IdentifierI getNode()
        {
            return null;
        }
    }

    private static class ChangeNodeAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            Strand strand = rv.getSbI().getStrand();
            Node current = strand.getCurrentNode();
            if(current == rv.getDt().workerNode)
            {
                // rv.sbI.setCurrentPane( 1);
                rv.getDt().rosterSlotsListDetailNode.GOTO();
            }
            else if(current == rv.getDt().rosterSlotsListDetailNode)
            {
                // rv.sbI.setCurrentPane( 0);
                rv.getDt().workerNode.GOTO();
            }
        }
    }

    private static class DebugDataAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            Print.prList((List) rv.getDataStore().get(POJOWombatData.WORKER),
                "PlayParticularAgain.DebugDataAction");
            Print.prList((List) rv.volunteerTriggers.dataStore.get(POJOWombatData.WORKER),
                "PlayParticularAgain.DebugDataAction");
            // Print.prList( rv.volunteerTriggers.list);
            // Print.prList( rv.volunteerTriggers.backingList);
            // rv.volunteerTriggers.testData();
        }
    }
}
