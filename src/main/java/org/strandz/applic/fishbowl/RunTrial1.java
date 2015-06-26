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
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.applichousing.SdzBag;
import org.strandz.data.fishbowl.objects.Client;
import org.strandz.data.fishbowl.objects.ClientIndustry;
import org.strandz.data.fishbowl.objects.ClientType;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.store.fishbowl.pFishbowlClasses;
import org.strandz.store.fishbowl.FishbowlDataStoreFactory;
import org.strandz.view.fishbowl.ClientUI;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Most of this file will be exactly the same no matter what
 * are running. Thus all in here apart from events stuff will
 * be read from a file that the user specifies in Context.
 * The shipping .java file will have only a main
 */
public class RunTrial1
{
    private static boolean fromBuilder = false;
    private static String filename = "C:\\dev\\applic\\fishbowl\\trial1.xml";
    private DataStore data;
    private Cell clientCell;
    private Node clientNode;
    private SdzBag sbI;
    private ClientUI clientUI;

    public RunTrial1()
    {
        super();
        sbI = (SdzBag) Utils.loadXMLFromResource(filename, this, false);
    }

    public static void setFilename(String s)
    {
        fromBuilder = true;
        filename = s;
    }

    public static void main(String[] args)
    {
        Err.setBatch(false);

        RunTrial1 obj = new RunTrial1();
        obj.fabInit(); // createOwnFrame will be true
    }

    public void fabInit()
    {
        // Err.pr( "&&&&&&&&&       sdzInit() for RunClients");
        JPanel panel = sbI;
        DisplayUtils.display(panel, !fromBuilder, 450, DisplayUtils.LEAVE);
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

        Dimension preferredSize = panel.getPreferredSize();
        // Err.pr( "preferred size was " + preferredSize);
        preferredSize.height = 450;
        panel.setPreferredSize( preferredSize);
        frame.setContentPane( panel);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation( screenSize.width / 2 - preferredSize.width / 2,
            screenSize.height / 2 - preferredSize.height / 2);
        frame.setVisible( true);
        */
        // data = new XMLFishBowlData();
        //data = FishbowlApplicationData.getInstance().getData();
        data = new FishbowlDataStoreFactory( true).getDataStore();
        if(sbI instanceof SdzBagI)
        {
            sbI.getStrand().addTransactionTrigger(new LocalCloseTransactionTrigger());
            if(!(sbI.getPane(0) instanceof ClientUI))
            {
                Err.error(
                    "Expected ClientUI but got " + sbI.getPane(0).getClass().getName());
            }
            clientUI = (ClientUI) sbI.getPane(0);
            // Err.pr( "control got from pane has hashCode: " +
            // clientUI.getClientGUI().getIndustryControl().hashCode());
            clientNode = sbI.getNode(0);
            clientNode.addDataFlowTrigger(new LocalDataFlowListener());
            // Err.pr( "Decoded StrandControl has node " + clientNode);
            // Err.pr( "This node has strand " + clientNode.getStrand());
            clientCell = clientNode.getCell(); // used to load data later
            // Err.pr( "cell will instantiate is " + clientCell.getClazz());
            // debugButton();
            // Err.pr( "About to focus on " + clientNode);
            clientNode.GOTO();
            sbI.execute(OperationEnum.EXECUTE_QUERY);
        }
        else
        {
            Err.error("Must be a ControllerInterface, got a " + sbI.getClass());
        }
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                // Err.pr( "DataFlowEvent.PRE_QUERY");
                data.startTx();

                // Err.pr( data);
                List list = null;
                list = (List) data.get(pFishbowlClasses.CLIENT);
                clientCell.setData(list);
                /*
                * W/out this section, setSelectedItem would not work, as will only
                * select a value that is already in the list.
                */
                Print.pr("cbIndustry: " + clientUI.clientGUI.cbIndustry);
                Print.pr("cbType: " + clientUI.clientGUI.cbClientType);
                Print.pr("cbEndClient: " + clientUI.clientGUI.cbEndClient);
                Print.pr("tfCompanyName: " + clientUI.clientGUI.tfCompanyName);
                clientUI.clientGUI.cbIndustry.removeAllItems();

                ArrayList industries = (ArrayList) data.get(
                    pFishbowlClasses.CLIENT_INDUSTRY);
                for(Iterator it = industries.iterator(); it.hasNext();)
                {
                    clientUI.clientGUI.cbIndustry.addItem(
                        ((ClientIndustry) it.next()).getDescription());
                }
                // So user can change to be null (core.info
                // stuff does "" to null convert) at any stage:
                clientUI.clientGUI.cbIndustry.addItem("");
                clientUI.clientGUI.cbClientType.removeAllItems();

                ArrayList types = (ArrayList) data.get(pFishbowlClasses.CLIENT_TYPE);
                for(Iterator it = types.iterator(); it.hasNext();)
                {
                    clientUI.clientGUI.cbClientType.addItem(
                        ((ClientType) it.next()).getDescription());
                }
                // So user can change to be null (core.info
                // stuff does "" to null convert) at any stage:
                clientUI.clientGUI.cbClientType.addItem("");
                clientUI.clientGUI.cbEndClient.removeAllItems();

                ArrayList clients = (ArrayList) data.get(pFishbowlClasses.CLIENT);
                for(Iterator it = clients.iterator(); it.hasNext();)
                {
                    Client client = (Client) it.next();
                    // Err.pr( "Company name to add " + client.getCompanyName());
                    // Err.pr( "Control to add it to " + clientUI.clientGUI.cbEndClient.getClass());
                    if(client.getCompanyName() != null)
                    {
                        clientUI.clientGUI.cbEndClient.addItem(client.getCompanyName());
                    }
                }
                // So user can change to be null (core.info
                // stuff does "" to null convert) at any stage:
                clientUI.clientGUI.cbEndClient.addItem("");
                 /**/
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

    private void debugButton()
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
