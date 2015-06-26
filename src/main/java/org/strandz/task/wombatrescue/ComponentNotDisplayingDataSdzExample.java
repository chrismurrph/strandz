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
package org.strandz.task.wombatrescue;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Result required:
 * Find out why FlexibilityRadioButtons was not displaying the underlying data
 * <p/>
 * Ingredients:
 * A worker with known Flexibility
 * LOV of Flexibility (ie. all Flexibility DOs in the DB)
 * (logically it is still a LOV even if it is a set of Radio Buttons)
 * A WorkerPanel to display the worker in
 * A Strand to suck him onto the display
 * <p/>
 * Method:
 * 1./Check to see that the Worker is marked as being flexible on WorkerPanel.
 * Is not.
 * 2./Debug component panel.flexibilityRadioButtons to see if its value is being set from the worker.
 * Want setSelectedText() to be being called on the RadioButton.
 * Is not.
 * 3./Is the impl done properly?
 * SwingControlInfoImpl - seems ok
 * 4./Debug msg on data coming out - flexibility is flexible, so that's fine
 * 5./Use a different control to check we got the structure right
 * Used seniority combobox, and it worked fine
 * 6./Data travels thru an ItemAdapter so turn debugging on there, especially near
 * where it talks to the actual component.
 * No setItemValue debugging came thru
 * 7./Try same but with seniority combobox
 * Also none - this was the wrong place to debug (Is only used for programmatic
 * setting of item values)
 * 8./Back to 3 - does a LOV capable control register itself as such? No
 * 9./Debug where the 'setItemValue' methods actually called. (What wanted to do
 * in 6). Thus ControlSignatures.setText() gets debugged. Must have been asleep
 * at 2 because can see the message coming thru.
 * 10./Use real debugger to see if the actual radio buttons are being got through to
 * setSelectedText() is being called with null
 * 11./Is this the same when using JComboBox?
 * Both work - my confusion was that the first time thru the control is cleared, so
 * the important one to debug on is the second time around. When debugged on the
 * 2nd one the answer was found - the expected values were different to what was
 * in the DB. Fixed this problem by getting the component to refer to the statics
 * in Flexibility. (eg Flexibility.NO_EVENINGS.getName()).
 * 12./Run StartupDev to see if this fixed up problem there
 * It didn't, so problem must be how the RosterWorkers xml file is hooked up to the
 * panel
 * 13./Check RosterWorkers xml file.
 * It seems that everything is hooked up ok
 * 14./Debug in ControlSignatures.setText, to observe component being set
 * (** not observing frbFlexibilityRadioButtons)
 * 15./Now observing frbFlexibilityRadioButtons
 * Realised that when changed the component in step 11, the xml version of the
 * component did not get automatically updated, so
 * 16./went into Designer and pressed the update button, after making a backup
 * of the xml file
 * TODO Designer must scan class files for panels and widgets used, to warn user to
 * get the latest if he wants. REALLY this should be a seperate service that is happening
 * on the client machine all the time.
 * 17./Use http://216.148.48.171/diff/phys_text_diff/ tp see what modifications were made
 * Mods seem good from a cursor glance
 * 18./Do ant where the xml file is to get it put into the jar that StartupDev uses.
 * 19./Run StartupDev
 * Not working! (Prolly needed to do more than just press the button in Designer)
 * 20./Run up Designer again
 * Got this error message:
 * "(Probably) XML file needs altered as <flexibilityRadioButtons>
 * inside org.strandz.view.wombatrescue.WorkerPanel does not have
 * required prefix for type of org.strandz.widgets.data.wombatrescue.FlexibilityRadioButtons,
 * which is <frb>"
 * 21./Fix WorkerPanel then manually change the xml file (it relys on name matching)
 * and repeat 18 and 19
 * No luck, so can't be hooked up properly
 * 22./Re-run Designer, observe that flexibility attribute coloured RED, and do the mapping
 * 23./Again do 18 and 19
 * Still no luck, but at least we can see this coming thru: "** setText-value no overnights"
 * for a FlexibilityRadioButtons component.
 * 24./Use debugger
 * It seems that our OneOnlyGroup does not have any buttons added to it.
 * 25./See if this is in fact the case with the XML file.
 * FlexibilityRadioButtons0 contains a OneOnlyGroup, which is supposed to contain the
 * buttons but does not.
 * 26./Use XMLMemoryGUI to take a look at WorkerAppHelper in XML form
 * ButtonGroup0's additions were not being preserved
 * 27./Made the buttons in FlexibilityRadioButtons into properties so they
 * would be preserved, hoping this would provide the missing link for the ButtonGroup
 * 28./From here load the XML, using buildFromCode = false for easy testing
 * Displayed fine
 * 29./Test StarupDev, after having loaded new component.
 * Displayed fine but not doing update. FlexibilityRadioButtons/OneOnlyGroup needed
 * finishing off so did so.
 * <p/>
 */
public class ComponentNotDisplayingDataSdzExample
{
    private DataStore data;
    private DomainQueriesI queriesI;
    private WorkerAppHelper helper;
    private static String xmlFileName = "C:\\sdz-zone\\dt-files\\wombatrescue\\experiment.xml";
    private static final boolean buildFromCode = false;

    public static void main(String[] args)
    {
        Err.setBatch(false);

        ComponentNotDisplayingDataSdzExample obj = new ComponentNotDisplayingDataSdzExample();
        //One of these
        obj.init();
        //obj.fix();
    }

    public void init()
    {
        //One of these
        if(buildFromCode)
        {
            helper = new WorkerAppHelper();
        }
        else
        {
            helper = new XMLWorkerAppHelper(xmlFileName);
        }
        helper.sdzSetup(); //TODO Make sure this method signature is used everywhere
        helper.getPanel().frbFlexibilityRadioButtons.setDebug(true);
        helper.getWorkerNode().addDataFlowTrigger(new ComponentNotDisplayingDataSdzExample.LocalDataFlowListener());
        helper.getStrand().setErrorHandler(new HandlerT());
        helper.getStrand().setEntityManagerTrigger(new EntityManagerT());
        initData();
        helper.displayWorkerPanel();
        helper.getWorkerNode().GOTO();
        helper.getStrand().EXECUTE_QUERY();
    }

    private void fix()
    {
        initData();
        data.startTx();
        Worker worker = getWorker();
        worker.setFlexibility(getFlexibleFlexibility());
        data.commitTx();
    }

    private void initData()
    {
        //WombatDataFactory ad = WombatDataFactory.getNewInstance( WombatConnectionEnum.DEBIAN_DEV_TERESA );
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection(WombatConnectionEnum.DEBIAN_DEV_TERESA);
        DataStore dataStore = dataStoreFactory.getDataStore();
        queriesI = dataStore.getDomainQueries();
        data = dataStore;
    }

    private Worker getWorker()
    {
        Worker result = null;
        result = (Worker) queriesI.executeRetObject(WombatDomainQueryEnum.WORKER_BY_GROUP_NAME, "Di Brogan's Group");
        //queryWorker( "Di Brogan's Group" );
        return result;
    }

    private Flexibility getFlexibleFlexibility()
    {
        Flexibility result = null;
        List flexibilityList = (List) data.get(POJOWombatData.FLEXIBILITY);
        result = (Flexibility) Utils.getFromList(flexibilityList, Flexibility.FLEXIBLE);
        return result;
    }

    class HandlerT implements ValidationHandlerTrigger
    {
        public void handleError(ApplicationError e)
        {
            java.util.List msg = e.getMsg();
            if(msg != null)
            {
                new MessageDlg(msg, JOptionPane.ERROR_MESSAGE);
                // Err.pr( msg);
                Err.alarm(msg.get(0).toString());
            }
            else
            {
                Print.prThrowable(e, "ComponentNotDisplayingDataRecipe");
            }
        }
    }
    
    class EntityManagerT implements EntityManagerTriggerI
    {
        public SdzEntityManagerI fetchEM()
        {
            return ((EntityManagedDataStore)data).getEM();
        }
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        // throws ValidationException
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                data.rollbackTx();
                data.startTx();

                setLOVs();
                ArrayList list = new ArrayList();
                list.add(getWorker());
                Err.pr(getWorker() + " has flexibility " + getWorker().getFlexibility());
                helper.getWorkerCell().setData(list);
            }
        }
    }

    private void setLOVs()
    {
        List flexibilities = (List) data.get(POJOWombatData.FLEXIBILITY);
        helper.getFlexibilityCell().setLOV(flexibilities);
    }

    /*
     * This group query does not work on hypersonic - even when have just copied
     * across a fresh MySql DB on which the query worked fine. Weird!
    private Worker getWorker( String groupName)
    {
      Worker result = null;
      result = queriesI.queryWorker( groupName );
      if(result == null)
      {
        Err.error( "No worker group called " +  groupName + " found");
      }
      return result;
    }
    */
}
