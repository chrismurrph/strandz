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
package org.strandz.test.fishbowl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.strandz.lgpl.util.Clazz;
import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.Independent;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NodeController;
import org.strandz.core.interf.Strand;
import org.strandz.data.fishbowl.objects.Client;
import org.strandz.data.fishbowl.objects.Job;
import org.strandz.data.fishbowl.objects.JobType;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import java.util.ArrayList;
import java.util.List;

/**
 * A sample test case, testing <code>java.util.SetExtent</code>.
 */
public class TestComplexNavigation extends TestCase
{
    List jobs;
    List jobTypes;
    List clients;
    Cell jobTypeCell;
    Cell clientCell;
    Cell jobCell;
    Node jobNode;
    Node clientNode;
    Node jobTypeNode;

    public static void main(String s[])
    {
        TestComplexNavigation obj = new TestComplexNavigation();
        obj.setUp();
        obj.testParentMethodTie();
    }

    protected void setUp()
    {
        Client.constructedTimes = 0;

        Job cleanManlyUnits = new Job();
        cleanManlyUnits.setDescription("Clean Manly units");

        Job doAccounts = new Job();
        doAccounts.setDescription("Do accounts");

        Job repairAirconditioning = new Job();
        repairAirconditioning.setDescription("Repair airconditioning");

        Job giveManagementAdvice = new Job();
        giveManagementAdvice.setDescription("Give management advice");

        JobType whiteCollar = new JobType();
        whiteCollar.setDescription("White collar");

        JobType blueCollar = new JobType();
        blueCollar.setDescription("Blue collar");
        jobTypes = new ArrayList();
        jobTypes.add(whiteCollar);
        jobTypes.add(blueCollar);

        Client graceBrothers = new Client();
        graceBrothers.setName("Grace Brothers");

        Client joeZammitCleaning = new Client();
        joeZammitCleaning.setName("Joe Zammit Cleaning");
        clients = new ArrayList();
        clients.add(graceBrothers);
        clients.add(joeZammitCleaning);
        jobs = new ArrayList();
        cleanManlyUnits.setClient(joeZammitCleaning);
        cleanManlyUnits.setJobType(blueCollar);
        jobs.add(cleanManlyUnits);
        doAccounts.setClient(joeZammitCleaning);
        doAccounts.setJobType(whiteCollar);
        jobs.add(doAccounts);
        repairAirconditioning.setClient(graceBrothers);
        repairAirconditioning.setJobType(blueCollar);
        jobs.add(repairAirconditioning);
        giveManagementAdvice.setClient(graceBrothers);
        giveManagementAdvice.setJobType(whiteCollar);
        jobs.add(giveManagementAdvice);
        clientNode = new Node();
        clientNode.setName("clientNode");
        clientCell = new Cell();
        jobTypeNode = new Node();
        jobTypeNode.setName("jobTypeNode");
        jobTypeCell = new Cell();
        jobNode = new Node();
        jobNode.setName("jobNode");
        jobCell = new Cell();
        clientNode.setCell(clientCell);
        jobTypeNode.setCell(jobTypeCell);
        jobNode.setCell(jobCell);
        //
        clientCell.setClazz(new Clazz(org.strandz.data.fishbowl.objects.Client.class));
        jobTypeCell.setClazz(new Clazz(org.strandz.data.fishbowl.objects.JobType.class));
        jobCell.setClazz(new Clazz(org.strandz.data.fishbowl.objects.Job.class));
        //
        jobNode.addIndependent(new Independent(clientNode, "client"));
        jobNode.addIndependent(new Independent(jobTypeNode, "jobType"));

        NodeController nodeController = new NodeController();
        Strand strand = new Strand();
        nodeController.setStrand(strand);
        clientNode.setStrand(strand);
        jobTypeNode.setStrand(strand);
        jobNode.setStrand(strand);
        clientNode.addDataFlowTrigger(new DataFlowT1Client());
        jobTypeNode.addDataFlowTrigger(new DataFlowT1JobType());
        jobNode.addDataFlowTrigger(new DataFlowT2());
        clientNode.GOTO();
        strand.EXECUTE_QUERY();
        jobTypeNode.GOTO();
        try
        {
            strand.EXECUTE_QUERY();
        }
        catch(Error err)
        {
            if(err.getMessage().equals("Cannot add the same block twice to a CompositeBlock"))
            {
                fail(err.getMessage());
            }
            else
            {
                Err.error(err);
            }
        }
        /*
        Print.pr( "to do goNode() for clientNode");
        clientNode.GOTO();
        Print.pr( "done goNode() for clientNode");
        */
    }

    public static Test suite()
    {
        return new TestSuite(TestComplexNavigation.class);
    }

    public void testParentMethodTie()
    {
        String requiredString = "[Give management advice, 1, Grace Brothers, null, null, null, null, , White collar]";
        // "[Do accounts, 2, Joe Zammit Cleaning, null, null, null, null, , White collar]";

        //
        Print.pr("jobTypes data records: " + jobTypeCell.getDataRecords());
        // CombinationExtent ce = (CombinationExtent)jobTypeCell.getDataRecords();
        // Print.pr( "jobTypes data records ID: " + ce.id);
        Print.pr("jobs data records: " + jobCell.getDataRecords());
        Print.pr("clients data records: " + clientCell.getDataRecords());
        Print.pr(
            "client is on "
                + clientNode.getBlock().getDataRecord(
                clientNode.getBlock().getIndex())
                + " at index "
                + clientNode.getBlock().getIndex());
        Print.pr(
            "job type is on "
                + jobTypeNode.getBlock().getDataRecord(
                jobTypeNode.getBlock().getIndex())
                + " at index "
                + jobTypeNode.getBlock().getIndex());
        Print.pr(
            "job is on "
                + jobNode.getBlock().getDataRecord(jobNode.getBlock().getIndex())
                + " at index " + jobNode.getBlock().getIndex());

         /**/
        String value = jobNode.getBlock().getDataRecord(jobNode.getBlock().getIndex()).toString();
        Print.pr("SIZE is " + jobNode.getBlock().dataRecordsSize());
        Print.pr("value is " + value);
        assertTrue(value.equals(requiredString));
        if(!value.equals(requiredString))
        {
            Err.error(value);
        }
        assertTrue(jobNode.getBlock().dataRecordsSize() == 1);
    }

    private class DataFlowT1Client implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                clientCell.setData(clients);
                Err.pr("clientCell loaded " + clients.size());
            }
        }
    }

    private class DataFlowT1JobType implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                jobTypeCell.setData(jobTypes);
                Err.pr("jobTypeCell loaded with " + jobTypes.size());
            }
        }
    }

    private class DataFlowT2 implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                jobCell.setData(jobs);
                Err.pr("jobCell loaded " + jobs.size());
            }
        }
    }
}
