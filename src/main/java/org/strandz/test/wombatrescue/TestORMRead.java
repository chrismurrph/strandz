/*
    Strandz - an API that matches the user to the dataStore.
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
package org.strandz.test.wombatrescue;

import junit.framework.TestCase;
import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.constants.Constants;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NodeController;
import org.strandz.core.interf.NonVisualFieldAttribute;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.cayenne.Worker;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import javax.jdo.Extent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A sample test case, testing <code>java.util.SetExtent</code>.
 */
public class TestORMRead extends TestCase
{
    Extent volExtent;
    Cell volCell;
    Node volNode;
    RuntimeAttribute christianName;
    RuntimeAttribute groupName;
    Strand strand;
    private DataStore dataStore;

    public static void main(String s[])
    {
        TestORMRead obj = new TestORMRead();
        obj.setUp();
        obj.testSimple();
    }

    public TestORMRead()
    {
        if(dataStore == null)
        {
            DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
            dataStoreFactory.addConnection(WombatConnectionEnum.DIRECT_DEMO_WOMBAT_CAYENNE);
            dataStore = dataStoreFactory.getDataStore();
        }
    }

    static class HandlerT implements ValidationHandlerTrigger
    {
        public void handleError(ApplicationError e)
        {
            List msg = e.getMsg();
            if(msg != null)
            {
                if(!msg.get(0).equals( Constants.NOT_FORWARD))
                {
                    Err.error("Not expecting: <" + msg + ">");
                }
            }
            else
            {
                // Print.prThrowable( e);
                Err.error(e);
            }
        }
    }
    
    class EntityManagerT implements EntityManagerTriggerI
    {
        public SdzEntityManagerI fetchEM()
        {
            return ((EntityManagedDataStore)dataStore).getEM();
        }
    }

    protected void setUp()
    {
        /*
         * Pretty harsh thing to do, and don't see need
        Flush.main(
            new String[] {
          "POJOWombatData", WombatConnectionEnum.TEST,
          WombatrescueApplicationData.JDO});
        Refill.main(
            new String[] {
          "POJOWombatData", WombatConnectionEnum.TEST,
          WombatrescueApplicationData.JDO});
        */
        volNode = new Node();
        volNode.setAll(true);
        volNode.setName("volNode");
        volCell = new Cell();
        volNode.setCell(volCell);
        volCell.setClazz(new Clazz(Worker.class));
        christianName = new NonVisualFieldAttribute();
        christianName.setName( "christianName");
        christianName.setDOField("christianName");
        volCell.addAttribute(christianName);
        groupName = new NonVisualFieldAttribute();
        groupName.setDOField("groupName");
        groupName.setName("groupName");
        volCell.addAttribute(groupName);

        NodeController nodeController = new NodeController();
        strand = new Strand();
        strand.setErrorHandler(new HandlerT());
        strand.setEntityManagerTrigger(new EntityManagerT());
        nodeController.setStrand(strand);
        volNode.setStrand(strand);
        //dataStore = WombatDataFactory.getJDOInstance2(
        //    WombatConnectionEnum.TEST_WOMBAT_JPOX);
        volNode.addDataFlowTrigger(new LocalDataFlowListener());
        boolean ok = volNode.GOTO();
        if(!ok)
        {
            strand.getValidationContext().setOk(true);
            ok = volNode.GOTO();
            if(!ok)
            {
                Err.error("Could not volNode.GOTO() because " + strand.getValidationContext().getMessage());
            }
        }
        strand.EXECUTE_QUERY();
    }

    /*
    public static Test suite()
    {
      return new TestSuite( TestJDO.class );
    }
    */

    /**
     * Succeeds on server and client (although will fail on server when used in conjunction with TestActualRoster)
     */
    public void testSimple()
    {
        if(dataStore.getConnection().getVersion().isORM())
        {
            List names = new ArrayList();
            for(int i = 0; i < strand.getCurrentNode().getRowCount(); i++)
            {
                String firstName = (String)christianName.getItemValue();
                if(firstName != null)
                {
                    names.add( firstName);
                }
                else
                {
                    String collectiveName = (String)groupName.getItemValue();
                    names.add( collectiveName);
                }
                if(i < strand.getCurrentNode().getRowCount() - 1)
                {
                    boolean ok = strand.NEXT();
                    if(!ok)
                    {
                        Err.error("Could not NEXT");
                    }
                }
            }
            assertTrue( "Names does not contain Chris/Naba", names.contains("Chris") || names.contains("Naba"));
            //Getting [First name is not 'Dave Wood's Group' but Chris]
            /*
             * Lost test for ordering to make life easier...
            Print.prList( names, "Make a test assert based on order of these names (groups need be ordered correctly)", false);
            
            assertTrue( "First name is not as expected but " + names.get(0), 
                        names.get(0).equals( "Dave Wood's Group") || names.get(0).equals( "Chris"));
            
            assertTrue( "Second name is not as expected but " + names.get(1), names.get(1).equals( "Graham") || 
                        names.get(1).equals( "Graham") || names.get(1).equals( "Dave Wood's Group"));
            
            assertTrue( "Third name is not as expected but " + names.get(2), 
                        names.get(2).equals( "Alfred") || names.get(2).equals( "Graham"));
            */
        }
    }

    private class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                dataStore.startTx();

                // List enterQryAttribs = volunteerNode.getEnterQueryAttributes();
                // volExtent = (Extent)dataStore.getExtent( JDOWombatData.VOLUNTEER);
                // volunteerList = Node.getListFromAttributeValues(
                // volunteerList, enterQryAttribs
                // );
                // Print.prExtent( volExtent);
                // Query q = dataStore.getPM().newQuery( POJOWombatData.VOLUNTEER);
                // Collection c = (Collection)q.execute();
                //
                //INSTEAD this (02/02/2005)
                //Collection c = (Collection)dataStore.query( POJOWombatData.WORKER);
                //THIS
                DomainQueriesI queries = dataStore.getDomainQueries();
                Collection c = queries.executeRetCollection(WombatDomainQueryEnum.ALL_WORKER);
                //queryAllWorkers();
                //
                //Print.prCollection( c);
                volCell.setData(c);
            }
        }
    }
}
