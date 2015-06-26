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
import org.strandz.data.fishbowl.objects.Client;
import org.strandz.data.fishbowl.objects.ClientIndustry;
import org.strandz.data.fishbowl.objects.ClientType;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.store.fishbowl.pFishbowlClasses;
import org.strandz.store.fishbowl.FishbowlDataStoreFactory;
import org.strandz.view.fishbowl.ClientUI;

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
import java.util.Iterator;
import java.util.List;

public class RunTrial2
{
    private static boolean fromBuilder = false;
    private static String filename;
    private static DataStore data;
    private static Cell cell;
    private static Node node;
    private static SdzBagI sbI;
    private static ClientUI clientUI;

    public static void setFilename(String s)
    {
        fromBuilder = true;
        filename = s;
    }

    public static void main(String[] args)
    {
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
            sbI = (SdzBagI) result;
            sbI.getStrand().addTransactionTrigger(new LocalCloseTransactionTrigger());
            try
            {
                clientUI = (ClientUI) sbI.getPane(0);
            }
            catch(ClassCastException ex)
            {
                Err.error(
                    "ClassCastException got a " + sbI.getPane(0).getClass().getName());
            }
            // Err.pr( "control got from pane has hashCode: " +
            // clientUI.getClientGUI().getIndustryControl().hashCode());
            node = sbI.getNode(0);
            node.addDataFlowTrigger(new LocalDataFlowListener());
            // Err.pr( "Decoded StrandControl has node " + node);
            // Err.pr( "This node has strand " + node.getStrand());
            cell = node.getCell(); // used to load data later
            // Err.pr( "cell will instantiate is " + cell.getClazz());
            // debugButton();
            node.GOTO();
            sbI.getStrand().execute(OperationEnum.EXECUTE_QUERY);
        }
        else
        {
            Err.error("Must be a ControllerInterface, got a " + result.getClass());
        }
    }

    static class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                // Err.pr( "DataFlowEvent.PRE_QUERY");
                data.startTx();

                // Err.pr( data);
                List list = null;
                List list2 = null;
                list = (List) data.get(pFishbowlClasses.CLIENT);
                cell.setData(list);
                /*
                * W/out this section, setSelectedItem would not work, as will only
                * select a value that is already in the list.
                */
                // Err.pr( "control adding item to has hashCode: " + clientUI.clientGUI.cbIndustry.hashCode());
                /*
                clientUI.clientGUI.cbIndustry.removeAllItems();
                ArrayList industries = (ArrayList)data.get(pFishbowlClasses.CLIENT_INDUSTRY);
                for(Iterator it = industries.iterator(); it.hasNext();)
                {
                String desc = ((ClientIndustry)it.next()).getDescription();
                clientUI.clientGUI.cbIndustry.addItem( desc);
                Err.pr( "Loaded industry: " + desc);
                }
                clientUI.clientGUI.cbIndustry.addItem( "");
                Err.pr( "Loaded empty industry");
                */
                 /**/
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


    static class LocalCloseTransactionTrigger implements CloseTransactionTrigger
    {
        public void perform(TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                data.commitTx();
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
        node.setAbilities(abilities);
    }
}
