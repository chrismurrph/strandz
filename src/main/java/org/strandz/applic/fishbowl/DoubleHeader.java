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
package org.strandz.applic.fishbowl;

import org.strandz.lgpl.util.Clazz;
import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.Independent;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.FieldAttribute;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NodeController;
import org.strandz.core.interf.Strand;
import org.strandz.data.fishbowl.objects.Client;
import org.strandz.data.fishbowl.objects.Job;
import org.strandz.data.fishbowl.objects.JobType;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.store.fishbowl.pFishbowlClasses;
import org.strandz.store.fishbowl.FishbowlDataStoreFactory;
import org.strandz.view.fishbowl.DoubleHeaderPanel;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

/**
 * A sample test case, testing <code>java.util.SetExtent</code>.
 */
public class DoubleHeader
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
    Strand strand;
    DoubleHeaderPanel panel;
    private static DataStore data;
    private static boolean good = true;

    public static void main(String[] args)
    {
        DoubleHeader dh = new DoubleHeader();
    }

    public DoubleHeader()
    {
        if(good)
        {
            setUpData();
        }
        else
        {
            // data = new XMLFishBowlData();
            //data = FishbowlApplicationData.getInstance().getData();
            data = new FishbowlDataStoreFactory( true).getDataStore();
        }
        setUpUI();
        setUpNodes();
        setUpFields();
        strand.addTransactionTrigger(new LocalCloseTransactionTrigger());

        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        action();
        // testParentMethodTie();
    }

    private void setUpFields()
    {
        FieldAttribute fa = new FieldAttribute("name", panel.clientPanel.tfName);
        clientCell.addAttribute(fa);
        fa = new FieldAttribute("phone", panel.clientPanel.tfPhone);
        clientCell.addAttribute(fa);
        fa = new FieldAttribute("description", panel.clientPanel.tfDescription);
        clientCell.addAttribute(fa);
         /**/
        fa = new FieldAttribute("description", panel.jobTypePanel.tfDescription);
        jobTypeCell.addAttribute(fa);
         /**/
        fa = new FieldAttribute("description", panel.jobPanel.tfDescription);
        jobCell.addAttribute(fa);
        fa = new FieldAttribute("startingInstructions",
            panel.jobPanel.tfStartingInstructions);
        jobCell.addAttribute(fa);
    }

    private void setUpUI()
    {
        panel = new DoubleHeaderPanel();
        panel.init(); // won't be necessary when require .xml file for decoding
    }

    private void setUpNodes()
    {
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
        strand = new Strand();
        nodeController.setStrand(strand);
        clientNode.setStrand(strand);
        jobTypeNode.setStrand(strand);
        jobNode.setStrand(strand);
    }

    private void action()
    {
        if(good)
        {
            clientNode.addDataFlowTrigger(new OrigLocalDataFlowListener());
            jobTypeNode.addDataFlowTrigger(new OrigLocalDataFlowListener());
        }
        else
        {
            clientNode.addDataFlowTrigger(new LocalDataFlowListener());
            jobTypeNode.addDataFlowTrigger(new LocalDataFlowListener());
        }
        Print.pr("to do goNode() for clientNode");
        clientNode.GOTO();
        Print.pr("done goNode() for clientNode");
        strand.EXECUTE_QUERY();
        Print.pr("to do goNode() for jobTypeNode");
        jobTypeNode.GOTO();
        Print.pr("done goNode() for jobTypeNode");
        strand.EXECUTE_QUERY();
        Print.pr("to do goNode() for jobNode");
        jobNode.GOTO();
        Print.pr("done goNode() for jobNode");
    }

    public void testParentMethodTie()
    {
        /*
        * As have not loaded from Client, position will still be -1:
        */
        /*
        Err.pr( "client is on " +
        clientNode.getBlock().getDataRecords().get(
        clientNode.getBlock().getIndex()) + " at index " +
        clientNode.getBlock().getIndex());
        Err.pr( "job type is on " +
        jobTypeNode.getBlock().getDataRecords().get(
        jobTypeNode.getBlock().getIndex()) + " at index " +
        jobTypeNode.getBlock().getIndex());
        //Err.pr( "job index: " + jobNode.getBlock().getIndex());
        Err.pr( "job data records: " + jobNode.getBlock().getDataRecords());
        Err.pr( "job is on " +
        jobNode.getBlock().getDataRecords().get(
        jobNode.getBlock().getIndex()) + " at index " +
        jobNode.getBlock().getIndex());
        */
        String value = jobNode.getBlock().getDataRecord(jobNode.getBlock().getIndex()).toString();
        assertTrue(value.equals("Give management advice"));
        assertTrue(jobNode.getBlock().dataRecordsSize() == 1);
    }

    private void assertTrue(boolean b)
    {
        if(!b)
        {
            Err.error("Incorrect assertion");
        }
    }

    private void setUpData()
    {
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
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                data.startTx();
                Print.pr("? OK: " + data.get(pFishbowlClasses.JOB));

                List jobTypes = (List) data.get(pFishbowlClasses.JOB_TYPE);
                List clients = (List) data.get(pFishbowlClasses.CLIENT);
                List jobs = (List) data.get(pFishbowlClasses.JOB);
                jobTypeCell.setData(jobTypes);
                // Err.pr( "jobTypeCell loaded");
                clientCell.setData(clients);
                // Err.pr( "clientCell loaded");
                jobCell.setData(jobs);
                Print.pr("BAD Just loaded: " + jobs);
                // Err.pr( "jobCell loaded");
                /*
                Err.pr( "DataFlowEvent.PRE_QUERY");
                data.readData();
                Err.pr( data);
                List list = null;
                List list2 = null;
                if(nodeLoading == 0)
                {
                list = (List)data.get(pFishbowlClasses.CLIENT);
                list2 = (List)data.get(pFishbowlClasses.JOB);
                jobCell.setInitialData( list2);
                }
                else if(nodeLoading == 1)
                {
                //Err.error();
                list = (List)data.get(pFishbowlClasses.JOB_TYPE);
                }
                clientCell.setInitialData( list);
                */
            }
        }
    }


    class LocalCloseTransactionTrigger implements CloseTransactionTrigger
    {
        public void perform(TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                data.commitTx();
            }
        }
    }


    private class OrigLocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                jobTypeCell.setData(jobTypes);
                // Err.pr( "jobTypeCell loaded");
                clientCell.setData(clients);
                // Err.pr( "clientCell loaded");
                jobCell.setData(jobs);
                Print.pr("GOOD Just loaded: " + jobs);
            }
        }
    }
}
