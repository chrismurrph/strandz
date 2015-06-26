package org.strandz.test.fishbowl;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.OutNodeControllerEvent;
import org.strandz.core.interf.PreOperationPerformedTrigger;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.Strand;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.widgets.table.ComponentTableView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Chris
 * Date: 18/10/2008
 * Time: 07:52:18
 */
public class TableHelper
{
    ComponentTableView table1;
    ComponentTableView table2;
    Strand strand;
    Node clientNode;
    Node jobNode;
    RuntimeAttribute clientName;
    RuntimeAttribute jobDescription;
    private RuntimeAttribute endingInstructions;
    private List clientAbilities = new ArrayList();
    private List jobAbilities = new ArrayList();
    MLTableExample example;
    PasteAction paste = new PasteAction();
    CopyAction copy = new CopyAction();
    //private Object commentVariable1 = SdzNote.TIMES_TOO_CLOSE;
    //private Object commentVariable2 = SdzNote.MASTER_DETAIL_COPY_PASTE;
    //private Object commentVariable3 = SdzNote.R_CLICK_RESTORE;
    /*
    * S/always put back to these values.
    *
    * See CMLBug.masterDetailCopyPaste &
    * CMLBug.rClickRestore - until fixed
    * booleans must stay.
    * See CMLBug.timesTooClose for pUtils.visibleMode
    * always being false.
    *
    public static boolean visible = pUtils.visibleMode;
    public static boolean masterNonTable = true;
    public static boolean childNonTable = false;
    */
    public boolean visible = false;
    public static boolean masterNonTable = true;
    public static boolean childNonTable = true;
    private static int times;
    /*
    * Having a different event queue, even thou it behaves in exactly the same way,
    * (just calls supers), means that messages that key has been pressed (for table)
    * do not keep happening. Must be a timing/event.consume() thing, that will look
    * up on google sometime!
    * Errrrr - Problem that still exists is pressing ENTER rather than clicking
    * on the dialog, the dialog will always re-appear. Reason must be that ENTER
    * has a function on a table, which is being relayed onto the table
    */
    // private static final GlobalEventQueue globalEventQueue = new GlobalEventQueue();

    class PasteAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            example.strandC.pasteItemValues();
            setEnabled(false);
        }
    }


    class CopyAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            example.strandC.copyItemValues();
            paste.setEnabled(true); // this property is listened to by
            // the physical controller
        }
    }

    void init()
    {
        Err.setBatch(!visible);
        example = new MLTableExample(visible);
        MLTableExample.masterNonTable = masterNonTable;
        MLTableExample.childNonTable = childNonTable;

        boolean ok = example.loadCI();
        if(!ok)
        {
            Err.error("setUp failed");
        }
        table1 = example.getTableView1();
        table2 = example.getTableView2();
        strand = example.getStrand();
        clientNode = strand.getNodeByName("Client Node");
        jobNode = strand.getNodeByName("Job Node");
        clientName = clientNode.getAttributeByName("name");
        jobDescription = jobNode.getAttributeByName("description");
        endingInstructions = jobNode.getAttributeByName("endingInstructions");
        goToJobsAbility();
        goToClientsAbility();
        debugAbility();
        copyPasteButton((ArrayList) clientAbilities);
        noDataQuery();
        clientNode.setAbilities(clientAbilities);
        jobNode.setAbilities(jobAbilities);
        // example.zeroTimesMessage();
        //
        if(masterNonTable == false)
        {
            //table1.getSelectionModel().addListSelectionListener(
            //    new DebugListSelectionListener());
        }
        //for delete problem:
        LocalControllerButtonListener l = new LocalControllerButtonListener();
        strand.addPreControlActionPerformedTrigger(l);
    }

    private static class LocalControllerButtonListener
        implements PreOperationPerformedTrigger
    {
        public LocalControllerButtonListener()
        {
        }

        public void preOperationPerformed(OutNodeControllerEvent evt)
        {
            if(evt.getID() == OperationEnum.REMOVE)
            {
                //Err.debug();
            }
        }
    }

    private void noDataQuery()
    {
        AbstractAction abstractAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent ae)
            {
                example.noData = true;
                strand.EXECUTE_QUERY();
                example.noData = false;
            }
        };
        abstractAction.putValue(Action.NAME, "No data query");
        abstractAction.putValue(Action.SHORT_DESCRIPTION, "No data query");
        clientAbilities.add(abstractAction);
    }

    private void debugAbility()
    {
        AbstractAction abstractAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent ae)
            {
                //showJobCursor();
                //
                //example.listMasterNames();
                //example.listMasterNamesData();
                //
                example.listDetailNonVisuals();
            }
        };
        abstractAction.putValue(Action.NAME, "Debug");
        abstractAction.putValue(Action.SHORT_DESCRIPTION, "Debug");
        clientAbilities.add(abstractAction);
        jobAbilities.add(abstractAction);
    }

    private void copyPasteButton(ArrayList abilities)
    {
        copy.putValue(Action.NAME, "Copy");
        copy.putValue(Action.SHORT_DESCRIPTION,
            "Copy this screen out to the buffer");
        abilities.add(copy);
        //
        paste.setEnabled(false);
        paste.putValue(Action.NAME, "Paste");
        paste.putValue(Action.SHORT_DESCRIPTION,
            "Paste to this screen from the buffer");
        abilities.add(paste);
    }

    private void goToJobsAbility()
    {
        AbstractAction abstractAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent ae)
            {
                jobNode.GOTO();
            }

            public String toString()
            {
                return "Go to Jobs";
            }
        };
        abstractAction.putValue(Action.NAME, "Go to Jobs");
        abstractAction.putValue(Action.SHORT_DESCRIPTION, "Go to Jobs");
        clientAbilities.add(abstractAction);
    }

    private void goToClientsAbility()
    {
        AbstractAction abstractAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent ae)
            {
                if(strand.getCurrentNode() == clientNode)
                {
                    Err.error("Trying to GOTO current node is wrong");
                }
                clientNode.GOTO();
            }

            public String toString()
            {
                return "Go to Clients";
            }
        };
        abstractAction.putValue(Action.NAME, "Go to Clients");
        abstractAction.putValue(Action.SHORT_DESCRIPTION, "Go to Clients");
        jobAbilities.add(abstractAction);
    }

    int copyPasteMasterDetail()
    {
        // public static boolean masterNonTable = true;
        // public static boolean childNonTable = false;
        Err.pr(SdzNote.MASTER_DETAIL_COPY_PASTE,
            "This test only works when master field and detail table");

        int detailRows = jobNode.getRowCount();
        MLTableExample.recValidatedTimes = 0;
        MLTableExample.recChangedTimes = 0;

        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
        copy.actionPerformed(ae);
        Err.pr(
            "current row b4 insert is " + clientNode.getRow() + " with "
                + detailRows + " detail rows");
        strand.INSERT();
        Err.pr("current row after insert is " + clientNode.getRow());
        paste.actionPerformed(ae);
        Print.pr(MLTableExample.recValidatedTimes);
        Print.pr(MLTableExample.recChangedTimes);
        return detailRows;
    }
}
