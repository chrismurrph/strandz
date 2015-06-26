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

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.Clazz;
import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.Independent;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.event.NodeValidationEvent;
import org.strandz.core.domain.event.NodeValidationTrigger;
import org.strandz.core.domain.event.OperationEvent;
import org.strandz.core.domain.event.PostingTrigger;
import org.strandz.core.domain.event.RecordValidationEvent;
import org.strandz.core.domain.event.RecordValidationTrigger;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.core.domain.MsgUtils;
import org.strandz.core.interf.ActualNodeControllerI;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.FieldAttribute;
import org.strandz.core.interf.InterfUtils;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NonVisualFieldAttribute;
import org.strandz.core.interf.NonVisualTableAttribute;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.TableAttribute;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.applichousing.SdzBag;
import org.strandz.data.fishbowl.objects.Client;
import org.strandz.data.fishbowl.objects.Job;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.store.fishbowl.FishbowlConnectionEnum;
import org.strandz.store.fishbowl.FishbowlDataStoreFactory;
import org.strandz.store.fishbowl.pFishbowlClasses;
import org.strandz.view.fishbowl.ClientsPanel;
import org.strandz.view.fishbowl.JobsPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MLTableExample
{
    //private DataStore data = FishbowlApplicationData.getInstance().getData();
    private DataStore dataStore;
    public SdzBag strandC;
    public Node node1;
    private Node node2;
    public Cell cell1;
    private Cell cell2;
    private boolean visible;
    private ComponentTableView tableView1;
    private ComponentTableView tableView2;
    private JComponent masterView;
    private JComponent childView;
    // private ClientsPanel masterPanel;
    // private JobsPanel childPanel;
    private Strand strand;
    public RuntimeAttribute nameAttribute;
    public RuntimeAttribute descriptionAttribute;
    public RuntimeAttribute phoneAttribute;
    public TableAttribute startingInstructionsAttribute;
    public NonVisualTableAttribute endingInstructionsAttribute;
    public List phoneNumbers = new ArrayList();
    // private int timesMessage;
    // static String lock = "";
    private JTabbedPane tabbedpane;
    public boolean noData = false;
    public static int recValidatedTimes;
    public static int recChangedTimes;
    public static final String ML = "   ML   ";
    public static final String OTHER = " OTHER ";
    public static boolean masterNonTable = false; // set externally
    public static boolean childNonTable = false; // set externally
    public int postQueryParentHappenedTimes;
    public int postQueryChildHappenedTimes;
    public int executeQueryHappenedTimes;
    private LocalPanel localPanel;
    HandlerT handlerT = new HandlerT();
    private static int times;

    public MLTableExample(boolean visible)
    {
        this.visible = visible;
        phoneNumbers.add("0403162669");
        phoneNumbers.add("87531654");
        FishbowlDataStoreFactory factory = new FishbowlDataStoreFactory();
        factory.addConnection(FishbowlConnectionEnum.XML_FISHBOWL);
        dataStore = factory.getDataStore();
        if(visible)
        {
            JFrame frame = createFrame(strandC);
            MessageDlg.setFrame( frame);
        }
    }

    class DataFlowT1 implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent event)
        // throws ValidationException
        {
            if(event.getID() == DataFlowEvent.PRE_QUERY)
            {
                // Print.pr( "******** PRE_QUERY 1, no data: " + noData);
                executeQueryHappenedTimes++;
                if(noData)
                {
                    List list = new ArrayList();
                    cell1.setData(list);
                    // cell2.setData( list);
                }
                else
                {
                    dataStore.rollbackTx();
                    dataStore.startTx();

                    List backingList = (List) dataStore.get(pFishbowlClasses.CLIENT);
                    // used to get a wrapper for testDeleteAfterQuery()
                    Collection c = InterfUtils.refineFromMatchingValues(backingList,
                        new ArrayList());
                    List list2 = new ArrayList(c);
                    //Was a NOP
                    //data.setRefinedList( pFishbowlClasses.CLIENT, list2);
                    // end
                    cell1.setData(list2);
                }
            }
            else if(event.getID() == DataFlowEvent.POST_QUERY)
            {
                postQueryParentHappenedTimes++;
            }
        }
    }


    class DataFlowT2 implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent event)
        // throws ValidationException
        {
            if(event.getID() == DataFlowEvent.PRE_QUERY)
            {
                /*
                times++;
                Err.pr( "******** PRE_QUERY 2 times " + times);
                if(times == 2)
                {
                Err.stack();
                }
                */
                // Print.pr( "******** PRE_QUERY, noData is " + noData);
                if(noData)
                {
                    List list = new ArrayList();
                    cell2.setData(list);
                }
                else
                {
                    dataStore.rollbackTx();
                    dataStore.startTx();

                    List list = (List) dataStore.get(pFishbowlClasses.JOB);
                    Err.pr( "Returned " + list.size() + " jobs");
                    cell2.setData(list);
                }
            }
            else if(event.getID() == DataFlowEvent.POST_QUERY)
            {
                postQueryChildHappenedTimes++;
            }
        }
    }


    class FormCloseTransactionT implements CloseTransactionTrigger
    {
        public void perform(TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                Print.pr(cell1.getDataRecords());
                dataStore.commitTx();
                dataStore.startTx();
            }
        }
    }


    class HandlerT implements ValidationHandlerTrigger
    {
        public void handleError(ApplicationError e)
        {
            List msg = e.getMsg();
            if(msg != null)
            {
                //new MessageDlg( msg);
                if(msg.size() == 0)
                {
                    Err.error("Message size s/never be zero");
                }
                else
                {
                    Err.pr(msg);
                    Err.alarm(msg.get(0).toString());
                }
            }
            else
            {
                Print.prThrowable(e, "MLTableExample.HandlerT");
            }
        }
    }
    
    class EntityManagerT implements EntityManagerTriggerI
    {
        public SdzEntityManagerI fetchEM()
        {
            return null;
        }
    }

    public static class MasterItemValidationT implements ItemValidationTrigger
    {
        private Cell cell;
        private RuntimeAttribute nameAttribute;
        private RuntimeAttribute phoneAttribute;
        private List numbers;

        public MasterItemValidationT(Cell cell,
                                     RuntimeAttribute nameAttribute,
                                     RuntimeAttribute phoneAttribute,
                                     List numbers)
        {
            this.cell = cell;
            this.nameAttribute = nameAttribute;
            this.phoneAttribute = phoneAttribute;
            this.numbers = numbers;
        }

        public void validateItem(ChangeEvent validationEvent)
            throws ValidationException
        {
             /**/
            if(nameAttribute.isValueEqualTo("Fred"))
            {
                nameAttribute.setInError(true);
                throw new ValidationException("Fred fails validation (master name item validation)");
            }
            else
            {
                nameAttribute.setInError(false);
            }
            /*
            if(phoneAttribute != null)
            {
            if(!numbers.contains( phoneAttribute.getItemValue()))
            {
            phoneAttribute.setInError( true);
            throw new ValidationException( "Phone number fails validation: " + phoneAttribute.getItemValue());
            }
            else
            {
            phoneAttribute.setInError( false);
            }
            }
            */
        }
    }


    public static class MasterRecordChangeT implements PostingTrigger
    {
        public void posted(OperationEvent changeEvent)
        {
            recChangedTimes++;
            Print.pr("$$$$$$$$$$$$$$$$$$$ recChangedTimes now " + recChangedTimes);
        }
    }


    public static class MasterRecordValidationT implements RecordValidationTrigger
    {
        private Cell cell;
        private RuntimeAttribute nameAttribute;

        public MasterRecordValidationT(Cell cell, RuntimeAttribute nameAttribute)
        {
            this.cell = cell;
            this.nameAttribute = nameAttribute;
        }

        public void validateRecord(RecordValidationEvent validationEvent)
            throws ValidationException
        {
            if(nameAttribute.getItemValue().equals("Fred"))
            {
                nameAttribute.setInError(true);
                throw new ValidationException("Fred fails validation, master name record validation");
            }
            else
            {
                nameAttribute.setInError(false);
            }
            recValidatedTimes++;
            //Print.pr( "$$$$$$$$$$$$$$$$$$$ recValidatedTimes now " + recChangedTimes);
        }
    }

    List listMasterNames(String msg)
    {
        String message;
        if(msg != null)
        {
            message = msg;
        }
        else
        {
            message = "All Item Names";
        }
        List result = chkTableAttribute(nameAttribute).getItemList();
        Print.prList(result, message, false);
        return result;
    }

    List listDetailNonVisuals()
    {
        List result = endingInstructionsAttribute.getItemList();
        Print.prList(result, "Detail non-visuals", false);
        return result;
    }

    void deconstituteToilet()
    {
        if(strand.getCurrentNode() != node2)
        {
            node2.GOTO();
        }
        strand.SET_ROW(0);
        boolean foundHim = false;
        for(int i = 0; i < node2.getRowCount(); i++)
        {
            if(descriptionAttribute.getItemValue().equals("William"))
            {
                if(!startingInstructionsAttribute.isBlank())
                {
                    startingInstructionsAttribute.setItemValue(null);
                }
                if(!endingInstructionsAttribute.isBlank())
                {
                    endingInstructionsAttribute.setItemValue(null);
                }
                foundHim = true;
            }
        }
        if(!foundHim)
        {
            //causes testQueryOnChild and testMultipleInsert to fail
            Err.error("Not found William");
        }
        node1.GOTO();
    }

    void makeNotFredEverywhere()
    {
        if(strand.getCurrentNode() != node2)
        {
            boolean ok = node2.GOTOWithoutValidation();
            if(!ok)
            {
                Err.error("Could not get to node2 b/c " + strand.getErrorMessage());
            }
        }
        //strand.SET_ROW( 0); //prolly not need!
        //Err.pr( "Desc b4 set: " + descriptionAttribute.getItemValue());
        descriptionAttribute.setItemValue("William"); //so b4Image is different
        //Err.pr( "Desc after set: " + descriptionAttribute.getItemValue());
        boolean ok = node1.GOTO();
        if(!ok)
        {
            Err.error("Could not get to node1 b/c " + strand.getErrorMessage());
        }
        else
        {
            nameAttribute.setItemValue("Holly"); //so b4Image is different
        }
        if(!masterNonTable)
        {
            listMasterNames("Hopefully no Freds 1");
        }
    }

    /**
     * Somehow TODO - work out how this happened as sounds like a note
     * the Job description "William" gets its starting and ending instuctions
     * blown away. Presumably one of the tests is doing this. The Job
     * "Give management advice" stays intact. Thus here we look for William
     * and add back the instructions.
     */
    void reconstituteToilet()
    {
        if(!masterNonTable)
        {
            listMasterNames("Hopefully no Freds 2");
        }
        if(strand.getCurrentNode() != node2)
        {
            boolean ok = node2.GOTO();
            if(!ok)
            {
                Err.error("Could not get to " + node2 + " because " + strand.getErrorMessage());
            }
        }
        strand.SET_ROW(0);
        boolean foundHim = false;
        for(int i = 0; i < node2.getRowCount(); i++)
        {
            if(descriptionAttribute.getItemValue().equals("William"))
            {
                if(startingInstructionsAttribute.isBlank())
                {
                    startingInstructionsAttribute.setItemValue("open the lid on the toilet once");
                }
                if(endingInstructionsAttribute.isBlank())
                {
                    endingInstructionsAttribute.setItemValue("close the lid on the toilet once");
                }
                foundHim = true;
            }
        }
        if(!foundHim)
        {
            //causes testQueryOnChild and testMultipleInsert to fail
            //Err.error( "Not found William");
        }
        node1.GOTO();
    }

    void removeSubsequentlyCreated()
    {
        if(strand.getCurrentNode() != node1)
        {
            node1.GOTO();
        }
        while(true)
        {
            if(node1.getRowCount() >= 2)
            {
                boolean ok = strand.SET_ROW(1);
                if(ok)
                {
                    strand.REMOVE();
                    Err.pr("Removed a row!");
                }
                else
                {
                    Err.error("Strange that could not get to the second row");
                }
            }
            else
            {
                break;
            }
        }
    }

    void removeNullItemsCreated()
    {
        if(strand.getCurrentNode() != node1)
        {
            node1.GOTO();
        }
        boolean ok = false;
        while(!ok)
        {
            ok = strand.SET_ROW(0);
            if(!ok && strand.getErrorMessage().indexOf(MsgUtils.RECORD_IS_BLANK) != -1)
            {
                strand.REMOVE();
            }
            else if(!ok)
            {
                Err.pr(strand.getErrorMessage());
            }
        }
        if(ok)
        {
            for(int i = 0; i < node1.getRowCount(); i++)
            {
                Err.pr( "There are " + node1.getRowCount() + " rows and we will delete blanks");
                if(nameAttribute.isBlank())
                {
                    Err.pr( "To remove a blank name");
                    strand.REMOVE();
                }
                if(i < node1.getRowCount() - 1)
                {
                    Err.pr("Incr: " + i + ", To do next when at " + node1.getRow() + " of total " + node1.getRowCount());
                    ok = strand.NEXT();
                    if(!ok)
                    {
                        Err.error("Could not NEXT b/c " + strand.getErrorMessage());
                    }
                }
            }
        }
        else
        {
            Err.error("Not able to SET_ROW to 0 b/c " + strand.getErrorMessage());
        }
    }

    void _removeNullRowsCreated()
    {
        List newList = new ArrayList();
        List clients = (List) dataStore.get(pFishbowlClasses.CLIENT);
        for(Iterator iterator = clients.iterator(); iterator.hasNext();)
        {
            Client client = (Client) iterator.next();
            String name = client.getName();
            if(name != null)
            {
                newList.add(client);
            }
        }
        if(clients.size() != newList.size())
        {
            Err.pr("REASON TO CALL!!");
            dataStore.set(pFishbowlClasses.CLIENT, newList);
        }
        else
        {
            Err.stack("No reason to call");
        }
    }

    List listMasterNamesData()
    {
        List result = new ArrayList();
        List clients = (List) dataStore.get(pFishbowlClasses.CLIENT);
        for(Iterator iterator = clients.iterator(); iterator.hasNext();)
        {
            Client client = (Client) iterator.next();
            String name = client.getName();
            result.add(name);
        }
        Print.prList(result, "All DO Names");
        return result;
    }

    TableAttribute chkTableAttribute(RuntimeAttribute nameAttribute)
    {
        TableAttribute result = null;
        if(nameAttribute instanceof TableAttribute)
        {
            result = (TableAttribute) nameAttribute;
        }
        else
        {
            Err.error("This trigger cannot be used when dealing with master fields - is for table as master");
        }
        return result;
    }

    public class MasterNodeValidationT implements NodeValidationTrigger
    {
        private Node node;
        private TableAttribute nameAttribute;

        public MasterNodeValidationT(RuntimeAttribute nameAttribute, Node node)
        {
            this.nameAttribute = chkTableAttribute(nameAttribute);
            this.node = node;
        }

        public void validateNode(NodeValidationEvent validationEvent) throws ValidationException
        {
            int i = 0;
            if(!masterNonTable)
            {
                for(Iterator iterator = listMasterNames(null).iterator(); iterator.hasNext(); i++)
                {
                    String value = (String) iterator.next();
                    if(value != null && value.equals("Billy"))
                    {
                        //TODO
                        //An improvement to this could be to supply an index at the same time
                        //This way we would go to the right place vertically, which currently
                        //not doing.
                        nameAttribute.setInError(true);
                        throw new ValidationException("Billy fails validation");
                    }
                    else
                    {
                        nameAttribute.setInError(false);
                    }
                }
            }
        }
    }

    public class ChildRecValidationT implements RecordValidationTrigger
    {
        private Cell cell;

        public ChildRecValidationT(Cell cell)
        {
            this.cell = cell;
        }

        public void validateRecord(RecordValidationEvent validationEvent)
            throws ValidationException
        {
            String txt = (String) descriptionAttribute.getItemValue();
            if(txt != null && txt.equals("Fred"))
            {
                throw new ValidationException("Fred fails validation, child description");
            }
            else
            {
                Err.pr("In validate record and \"Fred validation\" will pass b/c getItemValue() gives " + txt);
            }
        }
    }

    public boolean loadCI()
    {
        boolean ok = true;
        strandC = new SdzBag();
        //Doing this line seemed to hide it:
        //strandC.setPhysicalController( localPanel.physicalController);
        //irrelevant
        //localPanel.physicalController.setVisible( true);
        // MASTER
        node1 = new Node();
        strandC.setNode(0, node1);
        node1.setAll(true);
        node1.setAllowed(CapabilityEnum.COMMIT_ONLY, true);
        // node1.setBlankRecord( true);
        node1.setName("Client Node");
        cell1 = new Cell();
        cell1.setName("Client Cell");
        node1.setCell(cell1);
        cell1.setClazz(new Clazz(Client.class));
        if(masterNonTable)
        {
            ClientsPanel clientsPanel = new ClientsPanel();
            FieldAttribute a = new FieldAttribute("name", clientsPanel.tfName);
            nameAttribute = a;
            cell1.addAttribute(a);
            a = new FieldAttribute("description", clientsPanel.tfDescription);
            cell1.addAttribute(a);
            a = new FieldAttribute("phone", clientsPanel.cbPhone);
            cell1.addAttribute(a);
            phoneAttribute = a;
            masterView = clientsPanel;
        }
        else
        {
            TableAttribute a = new TableAttribute("name");
            nameAttribute = a;
            cell1.addAttribute(a);
            a = new TableAttribute("description");
            cell1.addAttribute(a);
            a = new TableAttribute("email");
            cell1.addAttribute(a);
            tableView1 = new ComponentTableView();
            tableView1.setName("Client table");
            node1.setTableControl(tableView1);
            masterView = tableView1;
        }
        node1.addDataFlowTrigger(new DataFlowT1());
        // node1.setRecordValidationTrigger( new MasterRecordValidationT( cell1));
        nameAttribute.setItemValidationTrigger(
            new MasterItemValidationT(cell1, nameAttribute, phoneAttribute,
                phoneNumbers));
        if(!masterNonTable)
        {
            MasterNodeValidationT mnvt =
                new MLTableExample.MasterNodeValidationT(nameAttribute, node1);
            node1.setNodeValidationTrigger(mnvt);
        }
        // DETAIL
        node2 = new Node();
        strandC.setNode(1, node2);
        node2.setAll(true);
        node2.setAllowed(CapabilityEnum.IGNORED_CHILD, false);
        // node.setBlankRecord( true);
        node2.setName("Job Node");
        cell2 = new Cell();
        cell2.setName("Job Cell");
        node2.setCell(cell2);
        cell2.setClazz(new Clazz(Job.class));
        if(childNonTable)
        {
            JobsPanel jobsPanel = new JobsPanel();
            FieldAttribute a = new FieldAttribute("description",
                jobsPanel.tfDescription);
            descriptionAttribute = a;
            cell2.addAttribute(a);
            a = new FieldAttribute("startingInstructions",
                jobsPanel.tfStartingInstructions);
            cell2.addAttribute(a);
            NonVisualFieldAttribute nva = new NonVisualFieldAttribute("endingInstructions");
            cell2.addAttribute(nva);
            childView = jobsPanel;
        }
        else
        {
            TableAttribute a = new TableAttribute("description");
            descriptionAttribute = a;
            cell2.addAttribute(a);
            startingInstructionsAttribute = new TableAttribute("startingInstructions");
            cell2.addAttribute(startingInstructionsAttribute);
            endingInstructionsAttribute = new NonVisualTableAttribute("endingInstructions");
            cell2.addAttribute(endingInstructionsAttribute);
            tableView2 = new ComponentTableView();
            tableView2.setName("Job table");
            node2.setTableControl(tableView2);
            childView = tableView2;
        }
        node2.setIndependent(0, new Independent(node1, "client"));
        node2.setRecordValidationTrigger(new ChildRecValidationT(cell2));
        node2.addDataFlowTrigger(new DataFlowT2());
        strand = strandC.getStrand();
        strand.addTransactionTrigger(new FormCloseTransactionT());
        strand.setErrorHandler(handlerT);
        strand.setEntityManagerTrigger(new EntityManagerT());
        // Isn't this call just for design time.
        strandC.setDefaults();
        if(strandC.validateBean())
        {
            postQueryParentHappenedTimes = 0;
            postQueryChildHappenedTimes = 0;
            node1.GOTO();
            strand.execute(OperationEnum.EXECUTE_QUERY);
            preForm();
            if(visible)
            {
                /*
                * Decided to go down Application route instead
                */
                ActualNodeControllerI actualController = strandC.getPhysicalController();
                actualController.deleteTool(OperationEnum.ENTER_QUERY);
                //actualController.deleteTool( OperationEnum.EXECUTE_QUERY);
                actualController.deleteTool(OperationEnum.EXECUTE_SEARCH);
                if(actualController != null)
                {
                    Err.pr("Automatically creating a actualController, so add it to panel");
                }
                else
                {
                    Err.error("Relying on a actualController having been created");
                }
                localPanel.controllerArea.add((JComponent) actualController);
                MessageDlg.getFrame().pack();
                MessageDlg.getFrame().setVisible(true);
                 /**/
            }
        }
        else
        {
            List errors = strandC.retrieveValidateBeanMsg();
            new MessageDlg(errors, JOptionPane.ERROR_MESSAGE);
            Err.error();
            ok = false;
        }
        return ok;
    }

    /**
     * As an alternative to doing it this way, could have employed
     * a MenuTabApplication. Here serves as a good reference for
     * doing things the manual way.
     *
     * @param sbI
     */
    private JFrame createFrame(SdzBagI sbI)
    {
        //JPanel panel = (JPanel)sbI;
        JFrame result = new JFrame("MLTableExample");

        result.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        JPanel outerTabPanel = new JPanel();
        outerTabPanel.setLayout(new BorderLayout());
        outerTabPanel.setPreferredSize(new Dimension(700, 300));
        tabbedpane = new JTabbedPane();
        tabbedpane.setTabPlacement(JTabbedPane.LEFT);
        outerTabPanel.add(tabbedpane, BorderLayout.CENTER);
        localPanel = new LocalPanel();
        tabbedpane.add(ML, localPanel);
        tabbedpane.add(OTHER, new JPanel());
        //Looks confused but a sbI doesn't actually contain anything in it.
        //I presume that this one doesn't even have any panes in it.
        //panel.add( outerTabPanel);

        TabChangeListener tabListener = new TabChangeListener();
        tabbedpane.addChangeListener(tabListener);
        result.setContentPane(outerTabPanel);
        return result;
    }

    /**
     * As this panel is not being put into an <code>Application</code> we maunually
     * extract the PictToolBarController that resides in every SdzBag.
     */
    private class LocalPanel extends JPanel
    {
        private JComponent controllerArea;

        private LocalPanel()
        {
            double size[][] = {{ModernTableLayout.FILL}, // Columns
                {0.14, 0.08, 0.35, 0.08, 0.35}}; // Rows
            setLayout(new ModernTableLayout(size));

            //This technique didn't work, so using fact of automatic creation
            //in a SdzBag
            //physicalController = new PictToolBarController();
            controllerArea = new JPanel();
            controllerArea.setLayout(new BorderLayout());

            JLabel label1 = new JLabel("Clients");
            JScrollPane scrollpane1 = null;
            scrollpane1 = new JScrollPane(masterView);

            JLabel label2 = new JLabel("Jobs");
            JScrollPane scrollpane2 = new JScrollPane(childView);
            add(controllerArea, "0,0");
            add(label1, "0,1, c, c");
            add(scrollpane1, "0,2");
            add(label2, "0,3, c, c");
            add(scrollpane2, "0,4");
        }
    }

    public static void main(String[] args)
    {
        new MLTableExample(true);
    }

    public ComponentTableView getTableView1()
    {
        return tableView1;
    }

    public ComponentTableView getTableView2()
    {
        return tableView2;
    }

    public Strand getStrand()
    {
        return strand;
    }

    private class TabChangeListener implements ChangeListener
    {
        int previouslySelected = 0;

        public void stateChanged(ChangeEvent ev)
        {
            JTabbedPane tp = (JTabbedPane) ev.getSource();
            int selected = tp.getSelectedIndex();
            if(selected == tabbedpane.indexOfTab(ML))
            {
            }
            else if(selected == tabbedpane.indexOfTab(OTHER))
            {
                if(!strand.POST())
                {
                    tabbedpane.setSelectedIndex(previouslySelected);
                    selected = previouslySelected;
                }
            }
            else
            {
            }
            previouslySelected = selected;
        }
    } // end class TabChangeListener

    public JTabbedPane getTabbedpane()
    {
        return tabbedpane;
    }

    public void setTabbedpane(JTabbedPane tabbedpane)
    {
        this.tabbedpane = tabbedpane;
    }

    public void preForm()
    {
        if(phoneAttribute != null)
        {
            setLOVs();
        }
    }

    private void setLOVs()
    {// Does not work for Strings anymore.
        // cell1.setLOV( phoneNumbers);
    }
}
