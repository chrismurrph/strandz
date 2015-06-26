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

import org.strandz.applic.util.ValidationTriggers;
import org.strandz.core.applichousing.SdzBag;
import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.NewInstanceTrigger;
import org.strandz.core.domain.NewInstanceEvent;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.event.NavigationTrigger;
import org.strandz.core.domain.event.OperationEvent;
import org.strandz.core.domain.event.RecordValidationEvent;
import org.strandz.core.domain.event.RecordValidationTrigger;
import org.strandz.core.domain.event.StateChangeEvent;
import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.ActualNodeControllerI;
import org.strandz.core.interf.AlterSdzSetupI;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.CopyPasteBuffer;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.InterfUtils;
import org.strandz.core.interf.OutNodeControllerEvent;
import org.strandz.core.interf.PostOperationPerformedTrigger;
import org.strandz.core.interf.PreOperationPerformedTrigger;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.data.wombatrescue.calculated.Night;
import org.strandz.data.wombatrescue.domain.WombatDomainLookupEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.NumDaysInterval;
import org.strandz.data.wombatrescue.objects.RosterSlot;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.data.wombatrescue.objects.FlexibilityI;
import org.strandz.data.wombatrescue.util.RosterUtils;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.extent.InsteadOfAddRemoveTrigger;
import org.strandz.lgpl.extent.NodeGroup;
import org.strandz.lgpl.extent.VisibleExtent;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.KeyUtils;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.view.FixedDocument;
import org.strandz.lgpl.widgets.AlphabetPanel;
import org.strandz.lgpl.widgets.AlphabetScrollPane;
import org.strandz.lgpl.widgets.NonVisualComp;
import org.strandz.util.applic.wombatrescue.WombatConstants;
import org.strandz.util.DebugUtil;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

abstract public class RosterWorkersTriggers
{
    public CopyAction copyAction;
    public PasteAction pasteAction;
    private SdzBag sdzBag;
    RosterWorkers_NEW_FORMATDT dt;
    public EntityManagedDataStore dataStore;
    private DomainQueriesI queriesI;
    private String title;
    String triggersTitle; 
    public AlphabetActionListener alphabetListener;
    public List masterList;
    public List detailList;
    List<AbstractAction> rosterSlotAbilities = new ArrayList<AbstractAction>();
    List<AbstractAction> workerAbilities = new ArrayList<AbstractAction>();
    RosterWorkersOps workerOps;
    private boolean visuallyRepresented = false;
    private LookupsI wombatLookups;
    private VisibleStrandI visibleStrand;
    private boolean insertRequired = false;
    private WorkerI dummyWorker;
    private ClazzToActuallyUseChooser clazzChooser;
    //For debugging:
    public CopyPasteBuffer mostRecentCopyBuffer;

    private static int timesMaster;
    private static int constructedTimes;
    int id;

    private static boolean simplifiedForDemo; //can only alter via property file

    RosterWorkersTriggers(SdzBag sdzBag,
                          RosterWorkers_NEW_FORMATDT dt,
                          EntityManagedDataStore dataStore,
                          DomainQueriesI queriesI,
                          RosterWorkersOps workerOps,
                          LookupsI wombatLookups,
                          VisibleStrandI visibleStrand)
    {
        if(dt == null)
        {
            Err.error("dt is null");
        }
        if(wombatLookups == null)
        {
            Err.error("lookups is null");
        }
        this.workerOps = workerOps;
        this.sdzBag = sdzBag;
        this.dt = dt;
        this.wombatLookups = wombatLookups;
        this.dataStore = dataStore;
        this.queriesI = queriesI;
        this.visibleStrand = visibleStrand;
        dt.workerNode.addDataFlowTrigger(new DataFlowT1());

        dt.workerNode.setRecordValidationTrigger(new WorkerRecordValidationT());

        //TODO - tested poorly (colors always wrong and focusing strange) - next step
        //will be to setup a test outside of Strandz where we can get JComboBox validation
        //working. Transplanting back will be easy. For this test set up a JFrame with two
        //tabs and a JComboBox and a JTextField. Will need to understand how Strandz works
        //with the tab case and maybe incorp MoveTracker. First step find a tutorial.
        //Getting colours right will be a bonus. Concentrate on focusing first. If need
        //to use an InputVerifier or whatever then fine (InputVerifier s/be the same as
        //focusing but JComboBox is weird - so whatever works!).
        //http://developer.apple.com/qa/qa2001/qa1272.html
        //This trigger will still exist in version control at REV 159 (where did cleanup)
        //NoOvernightRosterSlotValidationTrigger norst = new NoOvernightRosterSlotValidationTrigger( dt.whichShiftNameAttribute, dt.workerNode );
        //dt.whichShiftNameAttribute.setItemValidationTrigger( norst );

        ValidationTriggers.DateValidationTrigger dvt = new ValidationTriggers.DateValidationTrigger(dt.workerBirthdayAttribute);
        dt.workerBirthdayAttribute.setItemValidationTrigger(dvt);
        dvt = new ValidationTriggers.DateValidationTrigger(dt.workerAway1EndAttribute);
        dt.workerAway1EndAttribute.setItemValidationTrigger(dvt);
        dvt = new ValidationTriggers.DateValidationTrigger(dt.workerAway1StartAttribute);
        dt.workerAway1StartAttribute.setItemValidationTrigger(dvt);
        dvt = new ValidationTriggers.DateValidationTrigger(dt.workerAway2EndAttribute);
        dt.workerAway2EndAttribute.setItemValidationTrigger(dvt);
        dvt = new ValidationTriggers.DateValidationTrigger(dt.workerAway2StartAttribute);
        dt.workerAway2StartAttribute.setItemValidationTrigger(dvt);
        
        NamesLengthValidationTrigger nlvt = new NamesLengthValidationTrigger( dt.groupNameAttribute, dt.workerCell);
        //dt.groupNameAttribute.setItemValidationTrigger( nlvt);
        GroupContactNameValidationTrigger gcnvt = new GroupContactNameValidationTrigger( 
                dt.groupNameAttribute, dt.contactNameAttribute);
        dt.contactNameAttribute.setItemValidationTrigger( gcnvt);
        
        nlvt = new NamesLengthValidationTrigger( dt.christianNameAttribute, dt.workerCell);
        dt.christianNameAttribute.setItemValidationTrigger( nlvt);        
        
        nlvt = new NamesLengthValidationTrigger( dt.surnameAttribute, dt.workerCell);
        dt.surnameAttribute.setItemValidationTrigger( nlvt);
                
        if(sdzBag.getNodes().length > 1)
        {
            if(SdzNote.BAD_WOMBAT_VALIDATION.isCauseProblem())
            {
                dt.rosterSlotsListDetailNode.setRecordValidationTrigger(new RosterSlotRecordValidationT());
            }
            else
            {
                Err.pr(/*SdzNote.badWombatValidation,*/ "No record validation for " + 
                        dt.rosterSlotsListDetailNode.getName() + " till fix note");
            }
            dvt = new ValidationTriggers.DateValidationTrigger(dt.rosterSlotstartDateAttribute);
            dt.rosterSlotstartDateAttribute.setItemValidationTrigger(dvt);
            
            dvt = new ValidationTriggers.DateValidationTrigger(dt.rosterSlotspecificDateAttribute);
            DateChkDayValidationTrigger datedvt = new DateChkDayValidationTrigger(dt.rosterSlotspecificDateAttribute, dt);
            dt.rosterSlotspecificDateAttribute.setItemValidationTrigger(datedvt);
            DayChkDateValidationTrigger daydvt = new DayChkDateValidationTrigger(dt);
            dt.dayInWeeknameAttribute.setItemValidationTrigger(daydvt);
            dt.rosterSlotsListDetailNode.addNavigationTrigger(new RosterSlotNavigationT());
            dt.rosterSlotsCell.setInsteadOfAddRemoveTrigger( new InsteadOfAddRemoveRosterSlotT());
            dt.rosterSlotsCell.setNewInstanceTrigger(new RosterSlotNewInstanceT());
        }
        dt.strand.addTransactionTrigger(new FormCloseTransactionT());
        dt.strand.setErrorHandler(new HandlerT());
        dt.strand.setEntityManagerTrigger(new EntityManagerT());
        dt.strand.setEntityManagerTrigger(new EntityManagerT());
        PostOperationTrigger postOperationTrigger = new PostOperationTrigger();
        dt.strand.addPostControlActionPerformedTrigger(new PostOperationTrigger());
        dt.strand.addStateChangeTrigger(new ToFrozenTrigger());
        dt.strand.addPreControlActionPerformedTrigger( 
                new PreOperationTrigger( postOperationTrigger));
        dt.workerCell.setNewInstanceTrigger(new WorkerNewInstanceT());

        clazzChooser = new ClazzToActuallyUseChooser( dataStore.getEM().getORMType());
        new AlterSdzSetup(sdzBag).performAlterSdzSetup();
        ComponentUtils.fixCopyPasteBug( sdzBag.getPanes());
        constructedTimes++;
        id = constructedTimes;
    }
    
    private class AlterSdzSetup implements AlterSdzSetupI
    {
        private SdzBagI sbI;
        
        AlterSdzSetup( SdzBagI sbI)
        {
            this.sbI = sbI;
        }
        
        public void performAlterSdzSetup()
        {
            AlphabetScrollPane sp = new AlphabetScrollPane();
            simplifiedForDemo = getSimplifiedForDemo();
            if(simplifiedForDemo)
            {
                String letters[] = {"B", "C", "D", "F", "G", "H", "J", "M", "N", "P", "S"};
                sp.setLetters(letters);
            }
            else
            {
                String letters[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "Mu", "N",
                    "O", "P", "Q", "R", "S", "St", "T", "U", "V", "W", "X", "Y", "Z"};
                sp.setLetters(letters);
            }
            sp.getContentPanel().add(sbI.getPane(0), BorderLayout.CENTER);
            sbI.setPane(0, sp);
            alphabetListener = new AlphabetActionListener(sbI, dt.workerNode,
                dt.workerCell);
            sp.getAlphabetPanel().setActionListener(alphabetListener);
            sdzBag.getStrand().addStateChangeTrigger(new AlphabetTrigger(sp.getAlphabetPanel()));
            //To limit the comments field
            Object item = dt.commentsAttribute.getItem();
            if(!(item instanceof NonVisualComp)) //A NonVisualAttribute has a Comp
            {
                JTextArea textArea = (JTextArea) item;
                textArea.setDocument(new FixedDocument(255));
                visuallyRepresented = true;
            }
            //To make demo a bit simpler:
            if(simplifiedForDemo)
            {
                ActualNodeControllerI actualController = ((SdzBag) sbI).getPhysicalController();
                actualController.deleteTool(OperationEnum.EXECUTE_QUERY);
                actualController.deleteTool(OperationEnum.POST);
                dt.rosterSlotsListDetailNode.setEnterQuery(false);
                dt.rosterSlotsListDetailNode.setExecuteSearch(false);
            }
            else
            {
                //experiment with having it for real too
                dt.rosterSlotsListDetailNode.setExecuteSearch(false);
            }
            /**/
            NodeGroup rosterSlotsNodeGroup = new NodeGroup();
            //These two nodes will share the same data list
            dt.rosterSlotsListDetailNode.setNodeGroup( rosterSlotsNodeGroup);
            dt.rosterSlotsQuickListDetailNode.setNodeGroup( rosterSlotsNodeGroup);
            /**/
            dt.ui1.getMdefSpecificDate().addPropertyChangeListener( new SpecificDateActionListener( dt));
            //
            setForORM( dt.workerCell);
            setForORM( dt.belongsToGroupLookupCell);
            setForORM( dt.seniorityLookupCell);
            setForORM( dt.sexLookupCell);
            setForORM( dt.shiftPreferenceLookupCell);
            setForORM( dt.flexibilityLookupCell);
            setForORM( dt.rosterSlotsCell);
            setForORM( dt.intervalLookupCell);
            setForORM( dt.dayInWeekLookupCell);
            setForORM( dt.notInMonthLookupCell);
            setForORM( dt.onlyInMonthLookupCell);
            setForORM( dt.overridesOthersLookupCell);
            setForORM( dt.weekInMonthLookupCell);
            setForORM( dt.whichShiftLookupCell);
            setForORM( dt.workerLookupCell);
            setForORM( dt.rosterSlotsQuickCell);
            //setForORM( dt.belongsToGroupLookupCell);
        }

        private void setForORM( Cell cell)
        {
            cell.setClazzToConstruct( clazzChooser.getClazzToUse( cell));
            cell.setSecondaryClazzToConstruct( clazzChooser.getSecondaryClazzToUse( cell));
        }
    }
    
    private static class SpecificDateActionListener implements ActionListener, PropertyChangeListener
    {
        RosterWorkers_NEW_FORMATDT dt;
                
        SpecificDateActionListener( RosterWorkers_NEW_FORMATDT dt)
        {
            this.dt = dt;
        }
        
        public void actionPerformed(ActionEvent e)
        {
            Err.pr( "actionPerformed() " + e.getSource());
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            if(evt.getPropertyName().equals( "text") || evt.getPropertyName().equals( "value"))
            {
                Err.pr( SdzNote.BI_AI, evt.getPropertyName() + " changed from " + 
                        evt.getOldValue() + " to " + evt.getNewValue());
                Assert.isTrue( 
                        Utils.equals( dt.ui1.getMdefSpecificDate().getText(), evt.getNewValue()),
                        "From event: <" + evt.getNewValue() + ">, from control <" + dt.ui1.getMdefSpecificDate().getText() + ">" 
                );
            }
        }
    }

    private boolean getSimplifiedForDemo()
    {
        boolean result = false;
        String propFileName = WombatConstants.PROPERTY_FILE_NAME;
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        String demoVersion = PropertyUtils.getProperty("demoVersion", props);
        if(demoVersion.equals("true"))
        {
            result = true;
        }
        else if(!demoVersion.equals("false"))
        {
            Err.error("demoVersion property not properly set - needs to be true or false");
        }
        return result;
    }

    private class ToFrozenTrigger implements StateChangeTrigger
    {
        public void stateChangePerformed(StateChangeEvent e)
        {
            StateEnum current = e.getCurrentState();
            if(current == StateEnum.FROZEN)
            {
                workerOps.calculateRSSentence(current);
            }
        }
    }

    private static class AlphabetTrigger implements StateChangeTrigger
    {
        private AlphabetPanel panel;

        AlphabetTrigger(AlphabetPanel panel)
        {
            this.panel = panel;
        }

        public void stateChangePerformed(StateChangeEvent e)
        {
            StateEnum current = e.getCurrentState();
            StateEnum previous = e.getPreviousState();
            if(current == StateEnum.ENTER_QUERY)
            {
                panel.setEnabled(false);
            }
            else if(previous == StateEnum.ENTER_QUERY)
            {
                panel.setEnabled(true);
            }
        }
    }

    public int getEstimatedLookupDataDuration()
    {
        return 3000;
    }

    class RosterSlotRecordValidationT implements RecordValidationTrigger
    {
        public void validateRecord(RecordValidationEvent validationEvent)
            throws ValidationException
        {
            Err.pr(SdzNote.BAD_WOMBAT_VALIDATION, "NOT avail: " + dt.rosterSlotnotAvailableAttribute.getItemValue());
            if(!dt.rosterSlotnotAvailableAttribute.isBlank() && dt.rosterSlotnotAvailableAttribute.getItemValue().equals(Boolean.TRUE))
            {
                if(dt.rosterSlotspecificDateAttribute.isBlank())
                {
                    throw new ValidationException("If <Not Available>, then it must be on a <Known Date>");
                }
            }
            else
            {
                if(!dt.rosterSlotmonthlyRestartAttribute.isBlank() && 
                        dt.rosterSlotmonthlyRestartAttribute.getItemValue().equals(Boolean.TRUE))
                {
                    if(dt.weekInMonthnameAttribute.isBlank())
                    {
                        Err.pr("Wkr: " + dt.christianNameAttribute.getItemValue() + " " + dt.surnameAttribute.getItemValue());
                        throw new ValidationException("If <Monthly Restart>, need to know <Week In Month>");
                    }
                    if(!dt.numDaysIntervalnameAttribute.isBlank())
                    {
                        throw new ValidationException("If <Monthly Restart>, do not need to know the <Interval> between shifts");
                    }
                    if(!dt.rosterSlotstartDateAttribute.isBlank())
                    {
                        throw new ValidationException("If <Monthly Restart>, do not need to know when the <First Shift> was done");
                    }
                    if(!dt.rosterSlotspecificDateAttribute.isBlank())
                    {
                        throw new ValidationException("If <Monthly Restart>, do not need to have a <Known Date>");
                    }
                    if(dt.dayInWeeknameAttribute.isBlank())
                    {
                        String txt = "If <Monthly Restart>, need to know which <Day>";
                        throw new ValidationException(txt);
                    }
                }
                else
                {
                    if(dt.numDaysIntervalnameAttribute.isBlank()
                        && dt.rosterSlotspecificDateAttribute.isBlank())
                    {
                        throw new ValidationException("If not <Monthly Restart>, must know either the <Interval> between shifts, or the <Known Date>");
                    }
                    if(dt.rosterSlotstartDateAttribute.isBlank()
                        && dt.rosterSlotspecificDateAttribute.isBlank())
                    {
                        if(dt.numDaysIntervalnameAttribute.isBlank()
                            || !dt.numDaysIntervalnameAttribute.getItemValue().equals(NumDaysInterval.WEEKLY.toString()))
                        {
                            throw new ValidationException("If not <Monthly Restart>, need to know when the <First Shift> was done, or the <Known Date>");
                        }
                    }
                    if(!dt.weekInMonthnameAttribute.isBlank())
                    {
                        //If not monthly restart then is continuous, and the particular week in the month in not required  
                        throw new ValidationException(
                                "If not <Monthly Restart>, do not need to know which <Week In Month>, but have <" + 
                                        dt.weekInMonthnameAttribute.getItemValue() + ">");
                    }
                }
            }
            if(!dt.notmonthInYearnameAttribute.isBlank()
                && !dt.onlymonthInYearnameAttribute.isBlank())
            {
                // Err.pr( "dt.notInMonthNameAttribute: " + dt.notInMonthNameAttribute.getItemValue());
                // Err.pr( "dt.onlyInMonthNameAttribute: " + dt.onlyInMonthNameAttribute.getItemValue());
                throw new ValidationException("If <Not in Month>, then cannot also have an <Only in Month>");
            }
            if(dt.whichShiftnameAttribute.isBlank() && !dt.rosterSlotnotAvailableAttribute.getItemValue().equals(Boolean.TRUE))
            {
                throw new ValidationException("A <Which Shift> must be entered");
            }
            if(!dt.dayInWeeknameAttribute.isBlank() && !dt.rosterSlotstartDateAttribute.isBlank())
            {
                Date firstShiftDate = (Date)dt.rosterSlotstartDateAttribute.getItemValue();
                DayInWeek firstShiftDay = DayInWeek.getDayInWeek( firstShiftDate);
                DayInWeek dayInWeekDay = DayInWeek.getDayInWeek( (String)dt.dayInWeeknameAttribute.getItemValue()); 
                if(!firstShiftDay.equals( dayInWeekDay))
                {
                    throw new ValidationException( "<First Shift> is on a " + firstShiftDay + 
                            ", whereas <Day> is on a " + dayInWeekDay);
                }
            }
        }
    }

    class WorkerRecordValidationT implements RecordValidationTrigger
    {
        public void validateRecord(RecordValidationEvent validationEvent)
            throws ValidationException
        {
            WorkerI screenWorker = (WorkerI)dt.workerCell.getItemValue();
            if(screenWorker == null)
            {
                Err.error( "BUG - worker you see in front of you s/not be null - please try to replicate");
            }
            else
            {
                screenWorker.validate();
                //Only here if above didn't throw validation exception
                if(dt.mobilePhoneAttribute.isBlank() && dt.homePhoneAttribute.isBlank() && dt.workPhoneAttribute.isBlank())
                {
                    throw new ValidationException( "No phone number has been entered");
                }
            }
        }
    }

    class EntityManagerT implements EntityManagerTriggerI
    {
        public SdzEntityManagerI fetchEM()
        {
            return dataStore.getEM();
        }
    }

    class HandlerT implements ValidationHandlerTrigger
    {
        public void handleError(ApplicationError e)
        {
            //Take out this copy call and will get "Strange empty message" below
            List msg = Utils.copyList( e.getMsg());
            if(msg != null)
            {
                Assert.notEmpty( msg);
                new MessageDlg( NameUtils.colourInVariables( msg));
//The real error stack trace is in 'e'
//No particular need to temporarily redirect output,
//we just happen to be doing so here
//        Print.setOutStream( "errorFile");
//        Print.prThrowable( e, "Error from RosterWorkersTriggers");
//        Print.setOutStream( null);
                if(msg.isEmpty())
                {
                    Err.pr( "Strange empty message: <" + msg + ">");
                }
                else
                {
                    Err.alarm(msg.get(0).toString());
                }
            }
            else
            {
                Print.prThrowable(e, "Error from RosterWorkersTriggers");
            }
            if(SdzNote.WHERE_AFTER_MESSAGE_DLG.isVisible())
            {
                final Runnable doFinished = new Runnable()
                {
                    public void run()
                    {
                        KeyUtils.debugGlobalFocusState();
                    }
                };
                SwingUtilities.invokeLater(doFinished);
            }
        }
    }
    
    static class GroupContactNameValidationTrigger implements ItemValidationTrigger
    {
        private RuntimeAttribute groupNameAttribute;
        private RuntimeAttribute groupContactNameAttribute;

        public GroupContactNameValidationTrigger(RuntimeAttribute groupNameAttribute, 
                                                 RuntimeAttribute groupContactNameAttribute)
        {
            this.groupNameAttribute = groupNameAttribute;
            this.groupContactNameAttribute = groupContactNameAttribute;
        }

        public void validateItem(ChangeEvent validationEvent) throws ValidationException
        {
            if(!Utils.isBlank( (String)groupContactNameAttribute.getItemValue()) && 
                    Utils.isBlank( (String)groupNameAttribute.getItemValue()))
            {
                String msg = "<Group Contact Name> should only be entered when have a <Group Name>";
                groupContactNameAttribute.setInError(true);
                throw new ValidationException( msg);
            }
            groupContactNameAttribute.setInError(false);
        }
    }

    /**
     * We are short of space when printing out the roster, so here we make sure that
     * user validation will avert the later stack trace. Note that 'Group Name', 
     * 'First Name' and 'Surname' are involved but that 'Contact Name' is not.
     * See SVN r588 for where was trying to make this generic, but it didn't work,
     * thus will do a trigger for each Item. Reason did not work is quite possibly
     * that was using one instance of this trigger object for listening to focus
     * changes on many controls. Asking for trouble with Swing's asynchronous focus 
     * system.
     */
    static class NamesLengthValidationTrigger implements ItemValidationTrigger
    {
        private RuntimeAttribute attribute;
        private Cell workerCell;

        NamesLengthValidationTrigger(RuntimeAttribute firstNameAttribute, Cell workerCell)
        {
            this.attribute = firstNameAttribute;
            this.workerCell = workerCell;
        }
        
        public void validateItem(ChangeEvent validationEvent)
            throws ValidationException
        {
            Err.pr( SdzNote.CAYENNE_OVER_PERSISTING, "Skipping NamesLengthValidationTrigger");
            if(!SdzNote.CAYENNE_OVER_PERSISTING.isVisible())
            {
                WorkerI worker = (WorkerI)workerCell.getItemValue();
                String formedName = worker.toString();
                if(formedName != null && formedName.length() > Night.NAME_WIDTH)
                {
                    String msg = "Formed name <" + formedName + "> will be too long for the roster";
                    Err.pr( SdzNote.FIELD_VALIDATION, "Validation failed on " + attribute.getName());
                    attribute.setInError(true);
                    throw new ValidationException( msg);
                }
                attribute.setInError(false);
                if(formedName != null)
                {
                    Err.pr( SdzNote.FIELD_VALIDATION, "Validation passed because formedName is " + formedName.length() + " long");
                }
            }
        }
    }

    /**/

    private void rosterSlotDebugAbility()
    {
        AbstractAction abstractAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent ae)
            {
                KeyUtils.debugGlobalFocusState();
            }
        };
        abstractAction.putValue(Action.NAME, "Debug");
        abstractAction.putValue(Action.SHORT_DESCRIPTION, "Debug");
        rosterSlotAbilities.add(abstractAction);
    }

    static class DateChkDayValidationTrigger extends ValidationTriggers.DateValidationTrigger implements ItemValidationTrigger
    {
        private RosterWorkers_NEW_FORMATDT dt;

        DateChkDayValidationTrigger(RuntimeAttribute rosterSlotspecificDateAttribute, RosterWorkers_NEW_FORMATDT dt)
        {
            super(rosterSlotspecificDateAttribute);
            this.dt = dt;
        }

        public void validateItem(ChangeEvent validationEvent)
            throws ValidationException
        {
            super.validateItem(validationEvent);
            Err.pr(SdzNote.FIELD_VALIDATION, "DateChkDayValidationTrigger");
            if(!attribute.isBlank() && !dt.dayInWeeknameAttribute.isBlank())
            {
                Date date = (Date) attribute.getItemValue();
                String dayInWeekStr = (String) dt.dayInWeeknameAttribute.getItemValue();
                DayInWeek dayInWeek = DayInWeek.getDayInWeek(dayInWeekStr);
                boolean daysMatch = TimeUtils.chkDateAndDayMatch(date, dayInWeek.getCalendarDayOfWeek());
                if(!daysMatch)
                {
                    attribute.setInError(true);
                    throw new ValidationException("<Known Date> is on a " + DayInWeek.getDayInWeek(date) +
                        " whereas <Day> is a " + dayInWeek);
                }
            }
            attribute.setInError(false);
        }
    }

    static class DayChkDateValidationTrigger implements ItemValidationTrigger
    {
        private RosterWorkers_NEW_FORMATDT dt;

        DayChkDateValidationTrigger(RosterWorkers_NEW_FORMATDT dt)
        {
            this.dt = dt;
        }

        public void validateItem(ChangeEvent validationEvent)
            throws ValidationException
        {
            Err.pr(SdzNote.FIELD_VALIDATION, "DayChkDateValidationTrigger");
            Err.pr(SdzNote.FIELD_VALIDATION, "dayInWeeknameAttribute.isBlank(): " + dt.dayInWeeknameAttribute.isBlank());
            Err.pr(SdzNote.FIELD_VALIDATION, "rosterSlotspecificDateAttribute.isBlank(): " + dt.rosterSlotspecificDateAttribute.isBlank());
            if(!dt.dayInWeeknameAttribute.isBlank() && !dt.rosterSlotspecificDateAttribute.isBlank())
            {
                //Will only become up to date when move to another record:
                //Err.pr( "DinW from dt.dayInWeekAttribute - " + dt.dayInWeekAttribute.getItemValue());
                //Is a String:
                //Err.pr( "DinW from dt.dayInWeeknameAttribute - " + dt.dayInWeeknameAttribute.getItemValue());
                //Will manufacture from the above String:
                //Err.pr( "DinW from dt.dayInWeekLookupCell - " + dt.dayInWeekLookupCell.getItemValue());
                DayInWeek dayInWeek = (DayInWeek) dt.dayInWeekLookupCell.getItemValue();
                Err.pr(SdzNote.FIELD_VALIDATION, "date from dt.rosterSlotspecificDateAttribute - " + dt.rosterSlotspecificDateAttribute.getItemValue());
                Date date = (Date) dt.rosterSlotspecificDateAttribute.getItemValue();
                boolean daysMatch = TimeUtils.chkDateAndDayMatch(date, dayInWeek.getCalendarDayOfWeek());
                if(!daysMatch)
                {
                    dt.dayInWeeknameAttribute.setInError(true);
                    throw new ValidationException("<Day> is on a " + dayInWeek + " whereas <Known Date> is a " + DayInWeek.getDayInWeek(date));
                }
            }
            dt.dayInWeeknameAttribute.setInError(false);
        }
    }

    class DataFlowT1 implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                dataStore.rollbackTx();
                dataStore.startTx( "PRE_QUERY in " + this.getClass().getName());
                masterQuery();
            }
            else if(evt.getID() == DataFlowEvent.POST_QUERY)
            {
                //Err.pr( "Have read data in, got " + masterList.size() + " Worker records");
            }
        }
    }

    private void masterQuery()
    {
        timesMaster++;
        Err.pr(SdzNote.NOT_KEEPING_PLACE.isVisible() || SdzNote.ROSTERABILITY.isVisible(), 
               "Doing masterQuery() times " + timesMaster + " for " + triggersTitle);
        if(timesMaster == 0)
        {
            Err.stack();
        }
        setLOVs();
        Collection c = null;
        Assert.notNull( triggersTitle, "No triggersTitle set for " + this.getClass().getName() + " with ID " + id);        
        if(triggersTitle.equals( WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription()))
        {
            c = queriesI.executeRetCollection(WombatDomainQueryEnum.ROSTERABLE_WORKERS);
        }
        else if(triggersTitle.equals( WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription()))
        {
            c = queriesI.executeRetCollection(WombatDomainQueryEnum.UNROSTERABLE_WORKERS);
        }
        else if(triggersTitle.equals( WombatDomainQueryEnum.GROUP_WORKERS.getDescription()))
        {
            c = queriesI.executeRetCollection(WombatDomainQueryEnum.GROUP_WORKERS);
        }
        else
        {
            Err.error( "Have not coded for title: <" + triggersTitle + ">");
        }
        Assert.notNull( c);
        if(c.size() == 0 && triggersTitle.equals( WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription()))
        {
            Err.error("Have not found any " + triggersTitle + " in " + dataStore);
        }

        List enterQryAttribs = dt.workerNode.getEnterQueryAttributes();
        masterList = InterfUtils.refineFromMatchingValues(c, enterQryAttribs);
        Collections.sort( masterList, RosterUtils.SEARCH_BY);
        dt.workerCell.setData(masterList);
        if(dt.workerCell.getNullVerifiable() == null)
        {
            dt.workerCell.setNullVerifiable( obtainDummyWorker());
        }
        if(dt.belongsToGroupLookupCell.getNullVerifiable() == null)
        {
            dt.belongsToGroupLookupCell.setNullVerifiable( obtainDummyWorker());
        }
    }

    public class PreOperationTrigger
        implements PreOperationPerformedTrigger
    {
        private PostOperationTrigger postOperationTrigger;
        
        PreOperationTrigger( PostOperationTrigger postOperationTrigger)
        {
            this.postOperationTrigger = postOperationTrigger;    
        }
        
        public void preOperationPerformed(OutNodeControllerEvent evt) throws ValidationException
        {
            if(evt.getNode() == dt.workerNode
                && evt.getID() == OperationEnum.REMOVE
                && evt.getNode().getState() != StateEnum.FROZEN)
            {
                List<String> msgs = new ArrayList<String>();
                WorkerI worker = (WorkerI)dt.workerCell.getItemValue();
                msgs.add( "Making workers Un-Rosterable via the check-box is preferred.");
                msgs.add( "It means we don't loose their details.");
                msgs.add( "Are you sure you want to continue and delete <" + worker.getToLong() + "> ?");
                int ret = MessageDlg.showConfirmDialog( NameUtils.colourInVariables( msgs), 
                                                        "Delete Worker Confirmation");
                if(ret != JOptionPane.YES_OPTION)
                {
                    ValidationException exception = new ValidationException();
                    exception.setMsg( "<" + worker.getToLong() + "> has NOT been deleted");
                    throw exception;
                }
            }
            else if(evt.getID() == OperationEnum.GOTO_NODE)
            {
                if(evt.getNode() == dt.rosterSlotsListDetailNode)
                {
                    if(dt.rosterSlotsListDetailNode.getRowCount() == 0)
                    {
                        //Err.pr( "insertRequired = true");
                        insertRequired = true;
                    }
                }
            }
        }
    }

    public class PostOperationTrigger
        implements PostOperationPerformedTrigger
    {        
        public void postOperationPerformed(OutNodeControllerEvent evt)
        {
            //Err.pr( "OP: " + evt.getNode() + ", id: " + evt.getID());
            if(evt.getNode() == dt.rosterSlotsListDetailNode
                && evt.getID() == OperationEnum.GOTO_NODE)
            {
                if(evt.getNode().getState() != StateEnum.FROZEN)
                {
                    Boolean itemValue = (Boolean)dt.rosterSlotmonthlyRestartAttribute.getItemValue();
                    workerOps.toggleMonthlyRestart(itemValue.booleanValue());
                    workerOps.calculateRSSentence(evt.getNode().getState());
                }
                else
                {
                    if(insertRequired)
                    {
                        insertRequired = false;
                        dt.strand.INSERT_AFTER_PLACE();
                        Err.pr( "Automatic insert done");
                    }
                }
            }
            else if(evt.getNode() == dt.workerNode
                && (evt.getID() == OperationEnum.SET_ROW || 
                    evt.getID() == OperationEnum.NEXT || 
                    evt.getID() == OperationEnum.PREVIOUS ||
                    evt.getID() == OperationEnum.GOTO_NODE) //See testEffectOfDetailChange()
                && evt.getNode().getState() != StateEnum.FROZEN)
            {
                debugging( "Before refresh of " + dt.rosterSlotsQuickListDetailNode.getRowCount());
                dt.rosterSlotsQuickListDetailNode.REFRESH();
                debugging( "Done refresh");
            }
            else if(evt.getNode() == dt.rosterSlotsListDetailNode
                && evt.getID() == OperationEnum.COMMIT_RELOAD
                && evt.getNode().getState() == StateEnum.FROZEN)
            {
                Err.pr( SdzNote.SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY, "Use has done Save on RosterSlot tab");
                //visibleStrand.setCurrentPane( 0);
                //More sensible alternative to above is this:
                dt.rosterSlotsListDetailNode.GOTO();
            }
            //For the attribute 'worker to' that is calculated from a lookup to Worker, as have now changed onto 
            //another worker (see the post action trigger for where this really done):
            //dt.rosterSlotsListDetailNode.REFRESH();
            else if( SdzNote.REFRESH.isVisible())
            {
                Err.pr( "Doing " + evt.getID() + " on " + evt.getNode() + " in " + evt.getNode().getState());
            }
        }

        /**
         * After REFRESH the items ought to look like the source DOs, as we are refreshing the items
         * in rosterSlotsQuickListDetailNode from the DOs they are backed by. 
         */
        private void debugging( String txt)
        {
            if(!dt.strand.getCurrentNode().getState().isNew())
            {
                VisibleExtent ve = dt.rosterSlotsCell.getDataRecords();
                //int row = dt.rosterSlotsListDetailNode.getRow();
                int rowCount = dt.rosterSlotsListDetailNode.getRowCount();
                for(int i = 0; i < rowCount; i++)
                {
                    //if(i != -1) //when stateId == StateEnum.FROZEN/UNKNOWN row will be -1
                    {
                        RosterSlotI rosterSlot = (RosterSlotI) ve.get( i);
                        Err.pr( SdzNote.REFRESH.isVisible() || SdzNote.CTV_SDZ_BUG.isVisible(), txt + ", disabled on DO " + i + " is " + rosterSlot.isDisabled());
                    }
                }
                if(rowCount == 0)
                {
                    Err.pr( SdzNote.REFRESH.isVisible() || SdzNote.CTV_SDZ_BUG.isVisible(), txt);
                }
            }
        }
    }

    /*
    private boolean isAnEveryMonthRestartRow()
    {
      boolean result = dt.ui1.getChkMonthlyRestart().isSelected();
      boolean byAttrib = ((Boolean)dt.monthlyRestartAttribute.getItemValue()).booleanValue();
      if(result != byAttrib)
      {
        Err.error();
      }
      return result;
    }
    */

    class RosterSlotNavigationT implements NavigationTrigger
    {
        public void navigated(OperationEvent evt)
        {
            if(dt.strand.getCurrentNode().getState() != StateEnum.FROZEN)
            {
                RosterWorkersTriggers.this.workerOps.toggleMonthlyRestart(((Boolean) dt.rosterSlotmonthlyRestartAttribute.getItemValue()).booleanValue());
            }
            workerOps.calculateRSSentence(dt.strand.getCurrentNode().getState());
        }
    }

    class FormCloseTransactionT implements CloseTransactionTrigger
    {
        public void perform(TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                dataStore.commitTx();
            }
        }
    }

    private WorkerI obtainDummyWorker()
    {
        if(dummyWorker == null)
        {
            dummyWorker = (WorkerI) queriesI.executeRetObject(WombatDomainQueryEnum.NULL_WORKER);
            Err.pr( WombatNote.NEW_WORKER_INSTANCE, "dummyWorker just queried from the DB itself has belongToGroup: " +
                dummyWorker.getBelongsToGroup() + " and ID: " + dummyWorker.getId() + " and dummy: " + dummyWorker.isDummy());
        }
        return dummyWorker;
    }

    class WorkerNewInstanceT implements NewInstanceTrigger
    {
        public Object getNewInstance( NewInstanceEvent event)
        {
            WorkerI result;
            Err.pr(WombatNote.NEW_WORKER_INSTANCE,
                "A New Worker will be given a NULL/dummy Worker as the group he/she belongs to");
            if(dataStore.getEM().getORMType().isCayenne())
            {
                if(event.isPrimary())
                {
                    if(dataStore.getEM().getORMType() == ORMTypeEnum.CAYENNE_CLIENT)
                    {
                        result = (WorkerI)dataStore.getEM().newPersistent(
                            org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class);
                    }
                    else //ORMTypeEnum.CAYENNE_SERVER
                    {
                        result = (WorkerI)dataStore.getEM().newPersistent(
                            org.strandz.data.wombatrescue.objects.cayenne.Worker.class);
                    }
                    result.init( obtainDummyWorker());
                }
                else
                {
                    result = new org.strandz.data.wombatrescue.objects.Worker();
                }
                if(triggersTitle.equals( WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription()))
                {
                    Err.pr( "User might be able to do this, but it is an unusual thing to do - create an unrosterable worker");
                    result.setUnknown( true);
                }
                else
                {
                    result.setUnknown( false);
                }
                
                Err.pr( WombatNote.NEW_WORKER_INSTANCE, "User trigger has caused worker with ID: " + result.getId() + " to be created");
                Err.pr( WombatNote.NEW_WORKER_INSTANCE, "Its belongToGroups is " + result.getBelongsToGroup());
                Err.pr( WombatNote.NEW_WORKER_INSTANCE, "Its dummy is " + result.isDummy());
                Err.pr( WombatNote.NEW_WORKER_INSTANCE, "Its unknown-ness " + result.isUnknown());
                if(WombatNote.NEW_WORKER_INSTANCE.isVisible())
                {
                    if(result.getFlexibility() != null)
                    {
                        Err.pr( "Its flexibility is " + result.getFlexibility().getName());
                    }
                    else
                    {
                        Err.pr( "No Flexibility in new Worker - Sdz is supposed to handle this b4 " +
                            result.getChristianName() + " gets into the DB");
                    }
                }
            }
            else
            {
                result = new Worker( obtainDummyWorker());
            }
            return result;
        }
    }

    class RosterSlotNewInstanceT implements NewInstanceTrigger
    {
        public Object getNewInstance( NewInstanceEvent event)
        {
            RosterSlotI result;
            if(dataStore.getEM().getORMType().isCayenne())
            {
                if(event.isPrimary())
                {
                    if(dataStore.getEM().getORMType() == ORMTypeEnum.CAYENNE_CLIENT)
                    {
                        result = (RosterSlotI)dataStore.getEM().newPersistent(
                            org.strandz.data.wombatrescue.objects.cayenne.client.RosterSlot.class);
                    }
                    else //ORMTypeEnum.CAYENNE_SERVER
                    {
                        result = (RosterSlotI)dataStore.getEM().newPersistent(
                            org.strandz.data.wombatrescue.objects.cayenne.RosterSlot.class);
                    }
                    /*
                     * If don't do this then one will be created dynamically using factoryClazz() as
                     * all the lookups need to be filled in. 
                     */
                    result.setWorker( obtainDummyWorker());
                }
                else
                {
                    result = new org.strandz.data.wombatrescue.objects.RosterSlot();
                }
                result.setDisabled( false);
                result.setMonthlyRestart( false);
                result.setNotAvailable( false);
                Err.pr( WombatNote.NEW_ROSTERSLOT_INSTANCE, "User trigger has caused rosterSlot with ID: " +
                    result.getId() + " to be created");
                Err.pr( WombatNote.NEW_ROSTERSLOT_INSTANCE, "Its disabled is " + result.isDisabled());
                Err.pr( WombatNote.NEW_ROSTERSLOT_INSTANCE, "Its monthly restart " + result.isMonthlyRestart());
                Err.pr( WombatNote.NEW_ROSTERSLOT_INSTANCE, "Its not available " + result.isNotAvailable());
                if(WombatNote.NEW_WORKER_INSTANCE.isVisible())
                {
                    if(result.getDayInWeek() != null)
                    {
                        Err.pr( "Its day in week is " + result.getDayInWeek().getName());
                    }
                    else
                    {
                        Err.pr( "No DayInWeek in new RS - isn't Sdz supposed to handle this? -" +
                            " yes I think will, but later");
                    }
                }
            }
            else
            {
                result = new RosterSlot();
                result.setWorker( obtainDummyWorker());
            }
            return result;
        }
    }

    public void preForm()
    {
        if(dt.rosterSlotsListDetailNode != null)
        {
            if(!simplifiedForDemo)
            {
                if(visuallyRepresented)
                {
                    dt.rosterSlotmonthlyRestartAttribute.setOrdinal(new Integer(1));
                }
                copyPasteButton(rosterSlotAbilities);
                //rosterSlotDebugAbility();
                dt.rosterSlotsListDetailNode.setAbilities(rosterSlotAbilities);
            }
            else
            {
                copyPasteButton(rosterSlotAbilities);
                dt.rosterSlotsListDetailNode.setAbilities(rosterSlotAbilities);
            }
            //addDebugButton( workerAbilities);
            dt.workerNode.setAbilities(workerAbilities);
        }
    }

    private void setLOVs()
    {
        List days = wombatLookups.get( WombatDomainLookupEnum.ALL_DAY_IN_WEEK); 
        List shifts = wombatLookups.get( WombatDomainLookupEnum.ALL_WHICH_SHIFT);
        List flexibilities = wombatLookups.get( WombatDomainLookupEnum.ALL_FLEXIBILITY); 
        List seniorities = wombatLookups.get( WombatDomainLookupEnum.ALL_SENIORITY);
        Assert.notEmpty( seniorities, "No list of seniorities returned from DB");
        List sexes = wombatLookups.get( WombatDomainLookupEnum.ALL_SEX);
        List weeksInMonth = wombatLookups.get( WombatDomainLookupEnum.ALL_WEEK_IN_MONTH);
        List intervals = wombatLookups.get( WombatDomainLookupEnum.ALL_INTERVAL);
        List overrides = wombatLookups.get( WombatDomainLookupEnum.ALL_OVERRIDE);
        List monthsInYear = wombatLookups.get( WombatDomainLookupEnum.ALL_MONTH_IN_YEAR);
        Collection groups = queriesI.executeRetCollection(WombatDomainQueryEnum.WORKER_GROUPS);
        List pickGroups = new ArrayList(groups);
        pickGroups.add(0, queriesI.executeRetObject(WombatDomainQueryEnum.NULL_WORKER));

        dt.dayInWeekLookupCell.setLOV(days);
        dt.whichShiftLookupCell.setLOV(shifts);
        dt.shiftPreferenceLookupCell.setLOV(shifts);
        dt.flexibilityLookupCell.setLOV(flexibilities);
        dt.seniorityLookupCell.setLOV(seniorities);
        dt.sexLookupCell.setLOV(sexes);
        dt.belongsToGroupLookupCell.setLOV(pickGroups);
        dt.weekInMonthLookupCell.setLOV(weeksInMonth);
        dt.intervalLookupCell.setLOV(intervals);
        dt.overridesOthersLookupCell.setLOV(overrides);
        dt.onlyInMonthLookupCell.setLOV(monthsInYear);
        dt.notInMonthLookupCell.setLOV(monthsInYear);
    }



    /**
     * Should be bringing back what user sees on the screen. However user has to
     * commit to get the right answer, which is a bug...
     * TODO Fix
     * Compare this with working with a JTextField - which works as you
     * would expect. Shows that whether changed works just fine over
     * the commit (or post) cycle. Actually the cycle is when setDisplay()
     * is called, which is whenever a user is moving away from a record.
     * See DebugUtil.debugAttributeChange()
     */
    private void debugFlexibilityAttribute()
    {
        Object obj = dt.flexibilityAttribute.getItemValue();
        if(obj != null)
        {
            Err.pr( "In flexibilityAttribute we have a <" + obj.getClass().getName() + ">");
            Err.pr( "toString: <" + obj + ">");
            if(obj instanceof FlexibilityI)
            {
                FlexibilityI flexibility = (FlexibilityI)obj;
                Err.pr( "Name: " + flexibility.getName());
                Err.pr( "Id: " + flexibility.getId());
            }
        }
        else
        {
            Err.pr( "flexibility is null");
        }
        Err.pr( "");
    }

    private void debugDataStore()
    {
        dataStore.debug();
    }

    private void debugComponentKeys()
    {
        KeyUtils.debugComponent( dt.ui0.getTfGroupName());
    }

    private void addDebugButton(List abilities)
    {
        AbstractAction abstractAction = new AbstractAction()
        {
            public void actionPerformed(ActionEvent ae)
            {
                DebugUtil.debugAttributeChange( dt.email2Attribute);
                //debugFlexibilityAttribute();
                //debugDataStore();
                //debugComponentKeys();
            }
        };
        abstractAction.putValue(Action.NAME, "Debug");
        abstractAction.putValue(Action.SHORT_DESCRIPTION, "Debug");
        abilities.add(abstractAction);
        Err.pr( "Debug button has been added to " + abilities);
    }

    public class PasteAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            execute();
        }

        public void execute()
        {
            sdzBag.pasteItemValues();
            workerOps.toggleMonthlyRestart(((Boolean) dt.rosterSlotmonthlyRestartAttribute.getItemValue()).booleanValue());
            workerOps.calculateRSSentence(dt.strand.getCurrentNode().getState());
            //For the attribute 'worker to' that is calculated from a lookup to Worker, as have now changed onto 
            //another worker - we get the DO up to date with a POST and then refresh rosterSlotsListDetailNode
            //so that the screen is up to date:
            dt.strand.POST();
            dt.rosterSlotsListDetailNode.REFRESH();
            //end For the attribute 'worker to'
            setEnabled(false);
        }
    }

    public class CopyAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            execute();
        }

        public void execute()
        {
            mostRecentCopyBuffer = sdzBag.copyItemValues(dt.rosterSlotsListDetailNode);
            pasteAction.setEnabled(true); // this property is listened to by
            // the physical NodeController
        }
    }

    private void copyPasteButton(List<AbstractAction> abilities)
    {
        boolean alreadyDone = false;
        for(AbstractAction ability : abilities)
        {
            if(ability.getValue( Action.NAME).equals( "Copy"))
            {
                alreadyDone = true;
                break;
            }
        }
        if(!alreadyDone)
        {
            copyAction = new CopyAction();
            copyAction.putValue(Action.NAME, "Copy");
            copyAction.putValue(Action.SHORT_DESCRIPTION,
                "Copy this screen out to the buffer");
            abilities.add(copyAction);
            //
            pasteAction = new PasteAction();
            pasteAction.setEnabled(false);
            pasteAction.putValue(Action.NAME, "Paste");
            pasteAction.putValue(Action.SHORT_DESCRIPTION,
                "Paste to this screen from the buffer");
            abilities.add(pasteAction);
            //
            Err.pr(WombatNote.GENERIC, "copyPasteButton been added");
        }
    }
    
    class InsteadOfAddRemoveRosterSlotT implements InsteadOfAddRemoveTrigger
    {
        public void add(Object collection, Object obj, int index)
        {
            RosterSlotI rosterSlot = (RosterSlotI)obj;
            WorkerI worker = (WorkerI)dt.workerCell.getLastNavigatedToDO();
            Assert.notNull( worker, "Is there is some kind of navigable worker around?");
            if(collection != worker.getRosterSlots())
            {
                Print.prList( (List)collection, "collection");
                Print.prList( worker.getRosterSlots(), "worker.getRosterSlots()");
                Err.pr( "worker: " + worker.getToValidate());
                Err.error( WombatNote.CANT_ADD_NEW_VOL, "collection != worker.getRosterSlots() - live with for now!");
            }
            worker.addRosterSlot( rosterSlot, index);
        }

        public boolean remove(Object collection, Object obj)
        {
            RosterSlotI rosterSlot = (RosterSlotI)obj;
            WorkerI worker = (WorkerI)dt.workerCell.getLastNavigatedToDO();
            /* JDO error if attempt an operation on a deleted instance 
            if(collection != worker.getRosterSlots())
            {
                Print.prList( (List)collection, "collection");
                Print.prList( worker.getRosterSlots(), "worker.getRosterSlots");
                Err.pr( "Current Team: " + worker);
                Err.error( "collection != worker.getRosterSlots");
            }
            */
            return worker.removeRosterSlot( rosterSlot);
        }
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    String getTitle()
    {
        return title;
    }    
}
