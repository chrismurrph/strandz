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

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.data.fishbowl.objects.Client;
import org.strandz.data.fishbowl.objects.Job;
import org.strandz.data.fishbowl.objects.JobType;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.store.fishbowl.pFishbowlClasses;
import org.strandz.store.fishbowl.FishbowlDataStoreFactory;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RunDoubleHeader
{
    // public static String filename = null;
    private static boolean fromBuilder = false;
    private static String filename = "C:\\dev\\applic\\fishbowl\\doubleheader.xml";
    private static DataStore data;
    private static Cell clientCell;
    private static Cell jobTypeCell;
    private static Cell jobCell;
    // private static Node node;
    // private static ControllerInterface sbI;
    // private static ClientUI clientUI;
    private static Node clientNode;
    private static Node jobTypeNode;
    private static Node jobNode;
    private static Strand strand;
    private static boolean good = true;
    // only used when data is local ie. good
    private static List jobs;
    private static List jobTypes;
    private static List clients;
    private static int nodeLoading = -99;

    public static void setFilename(String s)
    {
        fromBuilder = true;
        filename = s;
    }

    public static void main(String[] args)
    {
        if(good)
        {
            setUpData();
        }

        InputStream s = null;
        try
        {
            s = new FileInputStream(filename);
        }
        catch(FileNotFoundException ex)
        {
            Err.error(ex);
        }
        BeansUtils.setDesignTime(true);

        XMLDecoder decoder = new XMLDecoder(s);
        decoder.setExceptionListener(new ExceptionListener()
        {
            public void exceptionThrown(Exception exception)
            {
                BeansUtils.setDesignTime(false);
                Err.error(exception);
            }
        });

        Object result = decoder.readObject();
        BeansUtils.setDesignTime(false);
        if(result instanceof JPanel)
        {
            JPanel panel = (JPanel) result;
            DisplayUtils.displayRespectingPreferred(panel, !fromBuilder);
            /*
            JFrame frame = new JFrame();
            WindowListener l = new WindowAdapter()
            {
              public void windowClosing( WindowEvent e)
              {
                // sbI.commit();
                if(!fromBuilder)
                {
                  System.exit( 0);
                }
              }
            };
            frame.addWindowListener( l);
            frame.setContentPane( panel);
            frame.pack();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension preferredSize = panel.getPreferredSize();
            Print.pr( "preferred size is " + preferredSize);
            frame.setLocation( screenSize.width / 2 - preferredSize.width / 2,
                screenSize.height / 2 - preferredSize.height / 2);
            frame.setVisible( true);
            */
        }
        else
        {
            Err.error("Must be a JPanel, got a " + result.getClass());
        }
         /**/
        // data = new XMLFishBowlData();
        //data = FishbowlApplicationData.getInstance().getData();
        data = new FishbowlDataStoreFactory( true).getDataStore();
        if(result instanceof SdzBagI)
        {
            SdzBagI sbI = (SdzBagI) result;
            clientNode = sbI.getNode(0);
            clientNode.setAll(true);
            clientCell = clientNode.getCell();
            jobTypeNode = sbI.getNode(1);
            jobTypeNode.setAll(true);
            jobTypeCell = jobTypeNode.getCell();
            jobNode = sbI.getNode(2);
            jobNode.setAll(true);
            jobCell = jobNode.getCell();
            strand = sbI.getStrand();
            strand.addTransactionTrigger(new LocalCloseTransactionTrigger());
            /*
            sbI = (ControllerInterface)result;
            sbI.addDataFlowListener( new LocalDataFlowListener());
            nodeLoading = 0;
            node = sbI.getNode( 0);
            Err.pr( "Decoded StrandControl has node " + node);
            Err.pr( "This node has strand " + node.getStrand());
            cell = node.getCell(); //used to load data later
            Err.pr( "cell will instantiate is " + cell.getClazz());
            Node jobNode = sbI.getNode( 2);
            jobCell = jobNode.getCell();
            try
            {
            Err.pr( "About to focus on " + node);
            node.goNode();
            }
            catch(GoNodeChangeException ex)
            {
            Err.error( ex);
            }
            sbI.load();
            nodeLoading = 1;
            node = sbI.getNode( 1);
            Err.pr( "Decoded StrandControl has 2nd node " + node);
            cell = node.getCell(); //used to load data later
            try
            {
            Err.pr( "About to focus on " + node);
            node.goNode();
            }
            catch(GoNodeChangeException ex)
            {
            Err.error( ex);
            }
            sbI.load();
            */
            action();
        }
        else
        {
            Err.error("Must be a ControllerInterface, got a " + result.getClass());
        }
    }

    private static void action()
    {
        if(good)
        {
            clientNode.addDataFlowTrigger(new OrigLocalDataFlowListener());
            jobTypeNode.addDataFlowTrigger(new OrigLocalDataFlowListener());
        }
        else
        {
            clientNode.addDataFlowTrigger(new LocalDataFlowListener());
            jobTypeNode.addDataFlowTrigger(new OrigLocalDataFlowListener());
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

    private static void setUpData()
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

    static class LocalDataFlowListener implements DataFlowTrigger
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
                jobCell.setData( list2);
                }
                else if(nodeLoading == 1)
                {
                //Err.error();
                list = (List)data.get(pFishbowlClasses.JOB_TYPE);
                }
                clientCell.setData( list);
                */
            }
        }
    }


    static class LocalCloseTransactionTrigger implements CloseTransactionTrigger
    {
        public void perform(TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                // Err.pr( "TransactionEvent.PRE_CLOSE");
                data.commitTx();
            }
        }
    }


    private static class OrigLocalDataFlowListener implements DataFlowTrigger
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
                // Err.pr( "jobCell loaded");
                Print.pr("GOOD Just loaded: " + jobs);
            }
        }
    }

    static private void debugButton()
    {
        ArrayList abilities = new ArrayList();
        AbstractAction abstractAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent ae)
            {
                /*
                Err.pr( "tfFile.getText()");
                Err.pr( "----------------");
                ArrayList contexts = (ArrayList)data.get( ContextData.CONTEXT);
                for(Iterator it = contexts.iterator(); it.hasNext();)
                {
                Context context = (Context)it.next();
                Err.pr( "From data: " + context.getBeansFile());
                }
                ContextPanel panel = (ContextPanel)sbI.getPane();
                Err.pr( "From panel: " + panel.tfFile.getText());
                Err.pr( "with hashCode: " + panel.tfFile.hashCode());
                Err.pr( "and name: " + panel.tfFile.getName());
                */
                Print.pr("Not used at moment");
            }
        };
        abstractAction.putValue(Action.NAME, "Debug");
        abstractAction.putValue(Action.SHORT_DESCRIPTION, "Debug");
        abilities.add(abstractAction);
        clientNode.setAbilities(abilities);
    }
}
