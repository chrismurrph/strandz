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
package org.strandz.core.prod.move;

import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.CalculationPlace;
import org.strandz.core.domain.ControlSignatures;
import org.strandz.core.domain.FieldItemAdapter;
import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.MoveTrackerI;
import org.strandz.core.domain.NodeChangeEvent;
import org.strandz.core.domain.NodeTableModelI;
import org.strandz.core.domain.NullAdapter;
import org.strandz.core.domain.TableItemAdapter;
import org.strandz.core.domain.ValidationContext;
import org.strandz.core.domain.MoveBlockI;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.AccessEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.StopWatch;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.IdentifierI;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class MoveTracker
    implements MoveManagerI, MoveTrackerI
{
    private List moveBlocks = new ArrayList();
    private List closedTo = new ArrayList();
    private MoveBlock currentMoveBlock;
    // Created here as EXECUTE_QUERY does not do anyKeyPressed()
    private ValidationContext validationContext;
    private ItemAdapter previousAdapterReleased;
    private ItemAdapter adapterReleased;
    private ItemAdapter enteredAdapter;
    private ItemAdapter confirmedErrorAdapter;
    private Date validationPtTime;
    // private int enteredTimes = 0;
    private EntrySiteEnum whereEnteredLastTime;
    private EntrySiteEnum whereLastValidated;
    // private boolean recordNextEntrySite = false;
    // private EntrySiteEnum nextStepEntry;
    // private int enterSteppedOn;
    private int rowSetting = -99;
    private Stack errorSites = new Stack();
    private Stack enterSites = new Stack();
    private static int times;
    private static int times0;
    private static int times1;
    private static int times2;
    private static int times3;
    private static int times4;
    private static int times5;
    private static int debugId;
    private static boolean debugging = false;
    private static int constructedTimes;
    private int id;

    private void pr(String txt)
    {
        if(debugging)
        {
            Print.pr("ID: " + id + " " + txt);
        }
    }

    public MoveTracker()
    {
        constructedTimes++;
        id = constructedTimes;
        pr("NEW MOVETRACKER");
    }

    public void reset()
    {
        moveBlocks = new ArrayList();
        currentMoveBlock = null;
        validationContext = null;
        previousAdapterReleased = null;
        adapterReleased = null;
        enteredAdapter = null;
        confirmedErrorAdapter = null;
        validationPtTime = null;
        whereEnteredLastTime = null;
        rowSetting = -99;
        errorSites = new Stack();
        enterSites = new Stack();
        closedTo = new ArrayList();
    }

    public void closeTo(EntrySiteEnum where)
    {
        if(closedTo.contains(where))
        {
            // Err.error( "Cannot close to, when are already closed to " + where);
            /*
            * Will sometimes have the same 'closeTo where' nested within another.
            * This will happen for example with ACTION_LISTENER when setLOV()
            * is done in a dataFlowPerformed().
            */
            closedTo.add(where);
        }
        else
        {
            closedTo.add(where);
        }
    }

    public void openAgainTo(EntrySiteEnum where)
    {
        boolean ok = closedTo.remove(where);
        if(!ok)
        {
            Err.error("Did not previously close access to " + where);
        }
    }

    public void forgetWhereCameFrom()
    {
        moveAdapterReleased();
        setAdapterReleased(new NullAdapter(), "forget it");
    }

    /**
     * Note there are a few enters. They are a trigger for 'its time to validate', as
     * well as being able to record the latest attribute that the users cursor has got to
     *
     * @param itemAdapter
     * @param where
     */
    public void enter(ItemAdapter itemAdapter, EntrySiteEnum where)
    {
        //Have to use a null when entering for first time
        //Assert.notNull( itemAdapter, "Do a best guess for itemAdapter (first in the block) rather than null");
        enter(itemAdapter, where, null, true);
    }

    /**
     * Here we only want to record the movement. We don't want an error to occur
     * to the user if validation trips something up
     *
     * @param itemAdapter
     * @param where
     */
    public void enterWithoutValidation(ItemAdapter itemAdapter, EntrySiteEnum where)
    {
        enter(itemAdapter, where, null, false);
    }

    /**
     * Note there are a few enters. They is a trigger for 'its time to validate', as
     * well as being able to record the latest spot that the users cursor has got to
     *
     * @param itemAdapter
     * @param where
     * @param key
     */
    public void enter(
        ItemAdapter itemAdapter, EntrySiteEnum where, OperationEnum key)
    {
        enter(itemAdapter, where, key, true);
    }

    public void enterWithoutValidation(
        ItemAdapter itemAdapter, EntrySiteEnum where, OperationEnum key)
    {
        enter(itemAdapter, where, key, false);
    }

    private void acceptEdits()
    {
        for(Iterator iter = moveBlocks.iterator(); iter.hasNext();)
        {
            MoveBlock mBlock = (MoveBlock) iter.next();
            NodeTableModelI model = mBlock.getNodeI().getTableModel();
            if(model != null)
            {
                model.getUsersModel().acceptEdit();
            }
        }
    }

    private boolean isOpenTo(EntrySiteEnum where)
    {
        boolean result = true;
        StateEnum state = null;
        if(currentMoveBlock != null)
        {
            state = currentMoveBlock.getNodeI().getState();
            if(state == StateEnum.FROZEN)
            {
                if(where == EntrySiteEnum.GOTO || where == EntrySiteEnum.ANY_KEY)
                {
                    result = true;
                    pr(
                        "Entering frozen block: " + currentMoveBlock.getNodeI()
                            + " at entry site " + where);
                }
                else
                {
                    result = false;
                    pr(
                        "Not entering frozen block: " + currentMoveBlock.getNodeI()
                            + " at entry site " + where);
                }
            }
        }
        if(result && closedTo.contains(where))
        {
            pr("Closed to " + where);
            result = false;
        }
        return result;
    }

    private void enter(
        ItemAdapter itemAdapter, EntrySiteEnum where, 
        OperationEnum key,
        boolean validate)
    {
        enterSites.push(where);
        if(isOpenTo(where))
        {
            if(enterSites.size() == 1)
            {
                /*
                times1++;
                */
                Err.pr(SdzNote.MDATE_ENTRY_FIELD,
                    "     ENTERING at " + where + " (" + key + "), (" + itemAdapter
                        + ") times " + times1);
                /*
                if(times1 == 4)
                {
                Err.debug();
                }
                */
                acceptEdits();
                if(itemAdapter != null)
                {
                    processAdapter(itemAdapter, key, itemAdapter.getCalculationPlace());
                }
                else
                {
                    Err.pr( SdzNote.FIRST_ITEM_FALLBACK, "May cause problems as no itemAdapter to go back to");
                }
                if(whereEnteredLastTime != null)
                {
                    whereEnteredLastTime = where; // So when exit doesn't use old whenEntered
                    if(validate)
                    {
                        itemValidationPoint1(where, key);
                    }
                    else
                    {
                        createNewValidationContext();
                    }
                }
                whereEnteredLastTime = where;
                enteredAdapter = itemAdapter;
                /*
                if(enteredAdapter != null)
                {
                Err.pr( "(first time) entry, enteredAdapter set to " +
                enteredAdapter.id + " row " + enteredAdapter.getRow());
                }
                */
            }
            else
            {
                pr(" o+o Entry at " + where);
                // Err.stack();
            }
        }
        else
        {// pr( "Closed to " + where + ", or " + enterSites.peek());
        }
    }

    private void processAdapter(ItemAdapter itemAdapter, OperationEnum key, CalculationPlace calculationPlace)
    {
        // Clear on exit, may have already collected some!
        if(itemAdapter == null)
        {
            itemAdapter = formAdapterReleased(key, calculationPlace);
        }
        if(itemAdapter instanceof AbstractTableItemAdapter)
        {
            /*
            TableItemAdapter ta = (TableItemAdapter)itemAdapter;
            if(ta.getRow() < 0)
            {
            Err.error( "Badly formed table itemAdapter " + ta.id);
            }
            */
        }
        moveAdapterReleased();
        String keyStr = "no key";
        if(key != null)
        {
            keyStr = key.toString();
        }
        setAdapterReleased(itemAdapter, keyStr);
    }

    /*
    * Call this method when you have done a complete ENTER/EXIT cycle, and
    * want to do another. If ok, you will go thru a fresh set of error sites,
    * and want any error that occurs to be sent back to the user.
    *
    * As part of the contract of calling this, if it returns false you will go
    * to the conclusion of the users trigger w/out calling any code.
    */
    public boolean readyNextStep()
    {
        boolean result = getValidationContext().isOk();
        errorSites.clear();
        // Err.pr( "=====readyNextStep() will RET " + result);
        return result;
    }

    public ItemAdapter getGoToAdapter()
    {
        ItemAdapter result = null;
        if(currentMoveBlock != null)
        {
            List adapters = currentMoveBlock.getAdapters();
            if(!adapters.isEmpty())
            {
                result = currentMoveBlock.getFirstVisualAdapter();
                //Err.pr( "Adapters in " + currentMoveBlock + " so FirstVisualAdapter will be a NULL one");
            }
            else
            {
                //Err.pr( "No Adapters in " + currentMoveBlock + " so FirstVisualAdapter will be null");
            }
        }
        return result;
    }

    public ItemAdapter getEnteredAdapter()
    {
        ItemAdapter result = enteredAdapter;
        if(result == null)
        {
            if(currentMoveBlock != null)
            {
                // when running headless
                result = currentMoveBlock.getItemValueAdapter();
                if(result == null)
                {
                    // when go to via a button, so we assume its the first
                    List adapters = currentMoveBlock.getAdapters();
                    if(!adapters.isEmpty())
                    {
                        // result = (Adapter)adapters.get( 0);
                        result = currentMoveBlock.getFirstVisualAdapter();
                        if(result != null)
                        {
                            Err.pr(SdzNote.INITIAL_ROW_WRONG,
                                "Setting row to 0 for [" + currentMoveBlock
                                    + "] is the MAIN problem. "
                                    + "Used to be -1 probably b/c initial test was with blank block then type "
                                    + "insert");
                            result.setRow(-1); // because will ++ when insert
                        }
                    }
                }
            }
            else
            {// happens when enter before have passedNodesThru. This job will
                // be done as part of setBlocks in this case.
            }
        }
        return result;
    }

    public void setRowSetting(int row)
    {
        rowSetting = row;
        pr("RowSetting set to " + row);
        // Err.stack();
    }

    /**
     * enter() is called for operations, and normally we know, at
     * the time of entering, to which row our 'used later validation
     * itemAdapter' should refer. At the time of enter for a query we don't
     * know whether more than 0 rows will be returned. A call to this
     * method tells us about this success/failure.
     * Thus when next enter occurs, it will form the right
     * previousAdapterReleased from the adapterReleased which we alter
     * here.
     * Does enteredAdapter have same function as adapterReleased, so we
     * can get rid of one?
     */
    public void setSomeRowsQueried(boolean b)
    {
        ItemAdapter ad = getAdapterReleased();
        if(ad != null) // != null for when running headless
        {
            if(b)
            {
                ad.setRow(0);
                // Err.pr( "setSomeRowsQueried===============row been set to 0 for " + ad.id);
            }
            else
            {
                ad.setRow(-1);
                // Err.pr( "setSomeRowsQueried===============row been set to -1 for " + ad.id);
            }
        }
        enteredAdapter = ad;
    }

    private EntrySiteEnum extractEntrySiteForError()
    {
        EntrySiteEnum result = whereEnteredLastTime;
        return result;
    }

    /**
     * Allows us to record error sites as we go thru them, so
     * we know whether to keep erroring as we unravel back thru
     * them. See error().
     * <p/>
     * Second param only for debugging
     */
    public void errorSite(ErrorSiteEnum whereError, OperationEnum enumId)
    {
        errorSites.push(whereError);
        pr("pushed error site: " + whereError + " for op " + enumId);
    }

    private void popError()
    {
        if(!errorSites.isEmpty()) // Running headless, user has done setText
        {
            ErrorSiteEnum popped = (ErrorSiteEnum) errorSites.pop();
            pr("(normal operation) popped error site: " + popped);
        }
    }

    /**
     * Return true if the error needs to be thrown. Depends on
     * where entered and what error point are at. Will want to
     * keep erroring until get to an error site that is just after
     * where you entered. Can then be guaranteed to proceed to exit
     * without any more code being run.
     */
    public boolean error(ErrorSiteEnum whereError)
    {
        boolean result = true;
        EntrySiteEnum entrySite = extractEntrySiteForError();
        if(entrySite == null)
        {
            Err.error("Cannot error where have not yet entered");
        }
        if(errorSites.size() > 1)
        {
            ErrorSiteEnum popped = (ErrorSiteEnum) errorSites.pop();
            pr("(error operation) popped " + popped);
            enterSites.clear();
        }
        else
        {
            if(!errorSites.isEmpty())
            {
                ErrorSiteEnum lastSite = (ErrorSiteEnum) errorSites.pop();
                if(lastSite != whereError) // running headless
                {
                    Err.error(
                        "Expect to error at the last site, popped " + lastSite + " at "
                            + whereError);
                }
            }
            result = false;
        }
        pr(
            "Erroring at " + whereError + " (entered: " + entrySite + ")"
                + ", will re-throw: " + result);
        if(!result)
        {// Should be exiting at outermost layer where entered, which will be
            // just outside where the error has been caught. As errored thru would
            // have missed all the exit spots, as enterSteppedOn did not get
            // gradually incremented down.
            // enterSteppedOn = 0;
        }
        return result;
    }

    public void exitEnter()
    {
        EntrySiteEnum pop = (EntrySiteEnum) enterSites.pop();
        if(isOpenTo(pop))
        {
            if(enterSites.size() == 0)
            {
                /**/
                if(pop == EntrySiteEnum.GOTO)
                {
                    processAdapter(null, OperationEnum.GOTO_NODE, null);
                    //10/12/04 Doing this meant that could not find out
                    //the reason why a node.GOTO failed using strand.getErrorMessage()
                    //createNewValidationContext();
                }
                /**/
                times4++;
                pr(
                    "     EXITING where entered at " + whereEnteredLastTime + " times "
                        + times4);
                pr(
                    "             - next time will validate "
                        + getPreviousAdapterReleased());
                if(times4 == 8)
                {// Err.debug();
                }
            }
            else
            {
                pr(" o-o Exit from enter at " + pop);
            }
        }
        else
        {// pr( "Exit where not allowed at " + pop);
        }
    }

    public ItemAdapter getAdapterReleased()
    {
        return adapterReleased;
    }

    private void moveAdapterReleased()
    {
        pr(
            "moveAdapterReleased() for " + currentMoveBlock + " so prev now "
                + adapterReleased);
        if(adapterReleased != null)
        {
            previousAdapterReleased = adapterReleased;
            pr( "previousAdapterReleased been set to " + previousAdapterReleased + " for " + currentMoveBlock);
            adapterReleased = null;
        }
    }

    private void setAdapterReleased(ItemAdapter itemAdapter, String cause)
    {
        //if(itemAdapter != null)
        {
            times1++;
            Err.pr( SdzNote.MDATE_ENTRY_FIELD, "setAdapterReleased() for " + currentMoveBlock + " to " +
                itemAdapter + " times " + times + ", cause: " + cause);
            if(times1 == 0)
            {
                Err.stack();
            }
        }
        adapterReleased = itemAdapter;
    }

    public ItemAdapter getPreviousAdapterReleased()
    {
        return previousAdapterReleased;
    }

    /*
    * With many listeners on one field, sometimes the same event ie. TAB,
    * will trigger many listeners which will trigger the same validation.
    * Here we can stop the same validation coming up many times.
    * Note that this kind of code, including the EntrySiteEnums, will end
    * up being part of an info package.
    */
    private boolean alreadyValidated(EntrySiteEnum whereWantToValidate)
    {
        boolean result = false;
        // Err.pr( " current " + adapterReleased);
        // Err.pr( " past " + getPreviousAdapterReleased());
        if(whereLastValidated != null)
        {
            if(adapterReleased == getPreviousAdapterReleased())
            {
                if(whereLastValidated == EntrySiteEnum.FIELD_FOCUS_GAINED
                    && whereWantToValidate == EntrySiteEnum.ACTION_LISTENER)
                {
                    Err.pr("SKIPPING");
                    result = true;
                }
            }
        }
        return result;
    }

    private void itemValidationPoint1(EntrySiteEnum where, OperationEnum key)
    {
        Date timestamp = new Date();
        ItemAdapter itemAdapter = getPreviousAdapterReleased();
        times0++;
        Err.pr(SdzNote.MDATE_ENTRY_FIELD, "getPreviousAdapterReleased() ret " + previousAdapterReleased +
            " in " + this + " times " + times0);
        if(itemAdapter == null)
        {
            if(!currentMoveBlock.getAdapters().isEmpty())
            {
                Err.error("Should have recorded a PreviousAdapterReleased in " + this);
            }
        }
        else
        {
            if(/* !alreadyValidated( where)*/true)
            {
                /*
                times2++;
                pr(
                "VALIDATE POINT " + currentMoveBlock.getName() + "."
                + itemAdapter.getName() + " times " + times2);
                if(times2 == 0)
                {
                Err.stack();
                }
                */
                if(validationPtTime != null)
                {
                    if(key != OperationEnum.POST && // where != EntrySiteEnum.FIELD_FOCUS_GAINED &&
                        // !(key == OperationEnum.INSERT && where == EntrySiteEnum.ANY_KEY)
                        true
                        )
                    {
                        if(StopWatch.areTimesClose(validationPtTime, timestamp, 50))
                        {
                            pr("" + key);
                            pr("" + where);
                            if(enterSites.lastElement() != EntrySiteEnum.FIELD_FOCUS_GAINED)
                            {
                                long lengthOfTime = timestamp.getTime()
                                    - validationPtTime.getTime();
                                if(Utils.visibleMode)
                                {
                                    Err.error(SdzNote.TIMES_TOO_CLOSE,
                                        "Times too close: " + lengthOfTime);
                                    // Err.soundOff();
                                }
                                else
                                {
                                    // Use for JUnit:
                                    Err.pr(SdzNote.TIMES_TOO_CLOSE,
                                        "=========ONLY FOR JUNIT==========TIMES TOO CLOSE: "
                                            + lengthOfTime);
                                }
                            }
                        }
                    }
                }
                // code here
                fireItemValidation(itemAdapter, key);
                // end code here
                validationPtTime = timestamp;
                whereLastValidated = where;
            }
        }
    }

    /**
     * @param itemAdapter
     * @param key         Debugging only
     */
    private void fireItemValidation(ItemAdapter itemAdapter, OperationEnum key)
    {
        boolean ok = true;
        Err.pr(SdzNote.MDATE_ENTRY_FIELD, "key when going to createNewValidationContext is " + key);
        createNewValidationContext();

        ApplicationError error = null;
        try
        {
            errorSite(ErrorSiteEnum.ITEM, key);
            itemAdapter.fireItemValidateEvent();
            b4HandleErrorProcessing(null, currentMoveBlock, key);
            //Err.pr( "To fireItemChangeEventOnAllOthers on " + itemAdapter);
            itemAdapter.fireItemChangeEventOnAllOthers();
            itemAdapter.fireItemChangeEvent();
        }
        catch(ApplicationError err)
        {
            if(error(ErrorSiteEnum.ITEM))
            {
                // Throw it again b/cause this is an inner ApplicationError catcher.
                throw err;
            }
            ok = false;
            error = err;
            b4HandleErrorProcessing(err, currentMoveBlock, key);
            currentMoveBlock.getNodeI().fireErrorHandler(err);
            times++;
            pr(
                ">>>>>EXIT from fireErrorHandler in MoveBlock.fireItemValidation times "
                    + times);
            if(times == 0)
            {
                Err.stack();
            }
        }
        finally
        {
            postValidationProcessing(ok, error);
        }
    }

    /**
     * Return false when pointless having a MoveManager
     */
    public void setBlock(BlockForMoveBlockI block)
    {
        // Err.pr( ">>>>>>>>>>>>>>>>>>setBlocks() with " + blocks.size() + " for " + this);
        // for( Iterator it1 = blocks.iterator(); it1.hasNext();)
        {
            // MoveBlockI block = (MoveBlockI)it1.next();
            List blocksAdapters = block.getVisualAdaptersArrayList();
            MoveBlock mb = new MoveBlock(blocksAdapters, block.getMoveNodeI(),
                block.getTableControl(), this);
            block.setMoveBlock(mb);
            /*
            if(block.getTableModel() != null)
            {
            block.getTableModel().getUsersModel().setMoveManager( this);
            }
            */
            for(Iterator it2 = blocksAdapters.iterator(); it2.hasNext();)
            {
                ItemAdapter itemAdapter = (ItemAdapter) it2.next();
                itemAdapter.setMoveBlock(mb);
            }
            // Err.pr( "MoveBlocks for " + block.getName() +
            // " with " + blocksAdapters.size() + " adapters");
            moveBlocks.add(mb);
        }
    }

    public boolean initialPoint(List blocks)
    {
        boolean result = true;
        if(!blocks.isEmpty())
        {
            currentMoveBlock = (MoveBlock) moveBlocks.get(0);

            // Err.pr( "currentMoveBlock set to " + currentMoveBlock + " for " +
            // this);
            List adapters = currentMoveBlock.getAdapters();
            if(!adapters.isEmpty())
            {
                // Adapter itemAdapter = (Adapter)adapters.get( 0);
                ItemAdapter itemAdapter = currentMoveBlock.getFirstVisualAdapter();
                if(itemAdapter != null)
                {
                    itemAdapter.setOriginalAdapter(itemAdapter);
                    Err.pr(SdzNote.INITIAL_ROW_WRONG,
                        "Setting row to -1 here is NOT the MAIN problem, as for "
                            + "tables the row is dynmically got every time. No row concept needed for fields.");
                    itemAdapter.setRow(-1); // If -1 then I op can increment
                    // itemAdapter.setRow( 0);
                    processAdapter(itemAdapter, null, itemAdapter.getCalculationPlace());
                    moveAdapterReleased();
                    Err.pr(SdzNote.INITIAL_ROW_WRONG,
                        "Finished setting orig itemAdapter to row -1");
                }
                else
                {
                    Err.error(
                        "First attribute did not have an itemAdapter, so could not set last itemAdapter");
                }
                // if(!readyNextStep())
                // {
                // Err.error( "How can we not be ready for the next step?");
                // }
            }
            else
            {
                // Will be the case when running screenless
                // Err.error( "Why have a MoveManager when first block has no adapters?");
                result = false;
            }
        }
        else
        {
            Err.error("Why have a MoveManager with no blocks?");
        }
        return result;
    }

    public void b4HandleErrorProcessing(ApplicationError err, MoveBlockI mBlock, OperationEnum reason)
    {
        /*
        if(err != null)
        {
        times++;
        Err.pr( "b4HandleErrorProcessing times " + times +
        " for " + err.getType());
        if(times == 0)
        {
        Err.stack();
        }
        }
        */
        setValidationOutcome(err == null, reason);
    }

    /**
     * When user expressed that a validation trigger
     * has failed, the stack was effectively unrolled
     * when ValidationException was turned into an
     * ApplicationError. Thus Strandz's state may be bad and
     * need correcting. Here we do this.
     */
    public void postValidationProcessing(
        boolean success, ApplicationError err)
    {
        //
        // setValidationRequired( origValidationRequired);
        if(success)
        {
            popError();
        }
        else
        {
            ApplicationErrorEnum errType = err.getType();
            if(errType == ApplicationErrorEnum.RECORD_VALIDATION)
            {
                currentMoveBlock.setRecordValidationOutcome(false);
            }
            else if(errType == ApplicationErrorEnum.INTERNAL)
            {
                currentMoveBlock.setRecordValidationOutcome(false);
            }
            else if(errType == ApplicationErrorEnum.UNDEFINED)
            {
                Print.prThrowable(err, "MoveTracker.postValidationProcessing()");
                Err.error("Validation Failure but reason not set");
            }
            /*
            else
            {
            pUtils.debug( err);
            Err.error( "Validation Failure, reason known, no post processing, " + errType);
            }
            */
        }
    }

    public ValidationContext getValidationContext()
    {
        if(validationContext == null)
        {
            validationContext = new ValidationContext();
        }
        pr("Returning ValidationContext: " + validationContext);
        return validationContext;
    }

    public void createNewValidationContext()
    {
        /*
        times5++;
        Err.pr( "Creating ValidationContext times " + times5 + " for id " + id );
        if (times5 == 0)
        {
          Err.stack();
        }
        */
        validationContext = new ValidationContext();
    }

    public void updateValidationContext(List list)
    {
        pr("Updating ValidationContext to FALSE");
        if(validationContext == null)
        {
            Err.error("How can validationContext == null ? id: " + id);
        }
        validationContext.setMessage(list);
        validationContext.setOk(false);
    }

    public void updateValidationContext(Object obj)
    {
        pr("Updating ValidationContext to FALSE");
        if(validationContext == null)
        {
            Err.error("How can validationContext == null ? id: " + id);
        }
        validationContext.setMessage(obj);
        validationContext.setOk(false);
    }

    public void setValidationOutcome(boolean b, OperationEnum reason)
    {
        // Here we are required to use the itemAdapter that we saved especially
        // so we could use it later to go back - ie. the one we made a copy
        // of after we called setRow on it, so that the next call to setRow
        // would not overwrite it.
        // Here we want to call the adpator that has been setInError by the
        // user
        ItemAdapter itemAdapter = getPreviousAdapterReleased();
        if(!b)
        {
            if(reason == OperationEnum.POST ||
                reason == OperationEnum.COMMIT_ONLY ||
                reason == OperationEnum.COMMIT_RELOAD)
            {
                Err.pr(SdzNote.GO_AS_BEFORE_TOO_COMPLEX, "No longer doing goAsBefore for a non-movement op " + reason);
            }
            else
            {
                _goAsBefore();
            }
            /*
            * adapterReleased is collected whenever click. When click again it
            * is moved across to previousAdapterReleased, if it is not null.
            * We don't want what last clicked on (the error) to be moved across,
            * so we set adapterReleased to null. Thus next time click and therefore
            * try to move across will have no effect on previousAdapterReleased.
            */
            adapterReleased = null;
        }
        if(itemAdapter != null)
        {
            /*
            times++;
            Err.pr( "To do confirmError times " + times);
            */
            Assert.notNull( itemAdapter.getOriginalAdapter(), "itemAdapter " + itemAdapter.getId() + " has no originalAdapter");
            itemAdapter.getOriginalAdapter().confirmError();
            confirmedErrorAdapter = itemAdapter;
        }
        else
        {
            if(currentMoveBlock == null)
            {
                Err.error("currentMoveBlock == null for " + this);
            }
            if(currentMoveBlock.getAdapters() == null)
            {
                Err.error("getAdapters() sometimes null!!");
            }
            else if(!currentMoveBlock.getAdapters().isEmpty())
            {
                Err.error("previousAdapterReleased should not be null");
            }
        }
    }

    public boolean onCurrentRow()
    {
        boolean result = true;
        if(adapterReleased == null)
        {// Not an error
            // Err.error( "In moveManager.onCurrentRow(), no adapterReleased");
        }
        if(adapterReleased instanceof AbstractTableItemAdapter)
        {
            AbstractTableItemAdapter itemAdapter = (AbstractTableItemAdapter) enteredAdapter;
            int adapterRow = itemAdapter.getRow();
            pr("");
            pr("(onCurrentRow()) entry, enteredAdapter use: " + enteredAdapter.getId());
            pr("adapterRow is " + adapterRow);

            int mlRow = currentMoveBlock.getNodeI().getRow() - 1;
            pr("mlRow is " + mlRow);
            pr("");
            result = adapterRow == mlRow;
        }
        return result;
    }

    /**
     * Focusing back on a particular item does not work, and is not
     * exactly essential for the product. Focusing back on the screen
     * is good enough.
     * <p/>
     * Even thou we collect lastField for a table, we currently do
     * not use it. Only advantage at this stage would be to get a
     * flashing caret - and its really JTable's fault that don't get
     * a flashing caret.
     */
    public void _goAsBefore()
    {
        Err.pr(SdzNote.GO_AS_BEFORE_TOO_COMPLEX, "goAsBefore being called");
        Err.pr(SdzNote.TWO_TABS_FOR_VALIDATION_TO_WORK,
            "Might start to debug here, and at enter");
        times3++;
        Err.pr(SdzNote.CHANGING_NODES_STATES_I_NOT_RECOGNISED,
            "%%%%%  In moveManager.goAsBefore() times " + times3);
        if(times3 == 0)
        {
            Err.debug();
        }
        if(previousAdapterReleased == null)
        {
            /*
            * To see why here consider the call to initialPoint() which will already
            * have occured. If a node has only NonVisual Attributes then the call to
            * getAdapters() will return none. Having none means that
            * formAdapterReleased() will not be called, thus when an error occurs
            * we end up here. MoveTracker is not suited for nonVisual blocks. It
            * is hard to see why would want to focus on a nonVisual Attribute.
            * However when need to make said change, it will be relatively painless.
            */
            Err.alarm("In moveManager.goAsBefore(), no previousAdapterReleased");
        }
        else
        {
            if(previousAdapterReleased instanceof AbstractTableItemAdapter)
            {
                AbstractTableItemAdapter itemAdapter = (AbstractTableItemAdapter) previousAdapterReleased;
                int row = itemAdapter.getRow();
                /*
                * Is not an error, for example if are going back to a node
                * that was frozen
                */
                if(row < 0)
                {
                    Err.pr("Badly made itemAdapter " + itemAdapter.getId() + " has row " + row);
                }
                 /**/
                /*
                if(currentMoveBlock.getNodeI().getState().isNew())
                {
                if(!itemAdapter.wasInsertingWhenSetRow())
                {
                row++;
                }
                }
                */
                if(row >= 0)
                {
                    pr("Repositioning using itemAdapter id: " + itemAdapter.getId());

                    // Object tableControl = currentMoveBlock.getTableControl();
                    Object tableControl = itemAdapter.getMoveBlock().getTableControl();
                    if(tableControl == null)
                    {
                        Err.error("No tableControl found for " + currentMoveBlock);
                    }

                    IdEnum id = IdEnum.newTable(tableControl, row, itemAdapter.getColumn(),
                        tableControl.getClass(), itemAdapter.getItemName());
                    ControlSignatures.repositionTo(id);
                }
            }
            else if(previousAdapterReleased instanceof NullAdapter)
            {
                //nufin
            }
            else
            {
                /* Now intentionally using NullAdapter as item that are on
                   before the user focuses anywhere
                if(previousAdapterReleased instanceof NullAdapter)
                {
                  // Hypoth is that this occurs b/c of classloader troubles, as
                  // happened while pasting and before had made a new ViewClassLoader
                  // be created (had only done DataClassLoader)
                  NullAdapter na = (NullAdapter)previousAdapterReleased;
                  Err.error(
                      "previousAdapterReleased is for NullAdapter "
                          + na.getDoAdapter().getDOFieldName());
                }
                */
                FieldItemAdapter itemItemAdapter = (FieldItemAdapter) previousAdapterReleased;
                IdEnum id = IdEnum.newField(itemItemAdapter.getItem());
                ControlSignatures.repositionTo(id);
            }
        }
    }

    /**
     * For tables we always create a new itemAdapter, sensitive to where the
     * key press has brought us. For fields we just use the one at the
     * field we get to.
     * <p/>
     * This method will be called from the controller or headless tests.
     * There is no command to change the column, only the row. If the
     * command is an insert the row will increased by one.
     */
    private ItemAdapter formAdapterReleased(OperationEnum op, CalculationPlace calculationPlace)
    {
        Err.pr(SdzNote.CHANGING_NODES_STATES_I_NOT_RECOGNISED,
            "###########- in formAdapterReleased for " + op);

        ItemAdapter result = null;
        /*
        * Done when exit from GOTO_NODE, as then will have target block.
        */
        if(op == OperationEnum.GOTO_NODE || op == OperationEnum.GOTO_NODE_IGNORE)
        {
            result = getGoToAdapter();
            pr("Got GOTO itemAdapter: " + result);
        }
        if(result == null)
        {
            result = getEnteredAdapter();
            pr("Got entered itemAdapter: " + result);
        }
        if(result == null)
        {// If user has done nothing, yet say wants to go to another block.
            // Err.error( "Cannot find the EnteredAdapter for " + moveBlock.getName());
            // Also if b4 consumeNodesIntoRT - see setBlocks()
        }
        else if(result instanceof AbstractTableItemAdapter)
        {
            AbstractTableItemAdapter ta = (AbstractTableItemAdapter) result;
            int row = ta.getRow();
            Err.pr(SdzNote.CHANGING_NODES_STATES_I_NOT_RECOGNISED, "Operation is " + op);
            if(op == OperationEnum.INSERT_AFTER_PLACE)
            {
                /*
                if(row == -1)
                {
                row += 2;
                pr( "Added 2");
                }
                else
                */
                {
                    //27/11/04 - put this back so TestTable.testInsertTableSelectedRowOk()
                    //would pass
                    //notifySuccessfulInsert() created to deplay when this happens
                    //row++;
                    Err.pr(SdzNote.INITIAL_ROW_WRONG,
                        "Added 1 to row from orig " + ta.getRow());
                }
                Err.pr(SdzNote.CHANGING_NODES_STATES_I_NOT_RECOGNISED,
                    "INSERT " + op + ", row starting at " + row + " from " + ta.getId());
            }
            else if(op == OperationEnum.NEXT)
            {
                row++;
            }
            else if(op == OperationEnum.PREVIOUS)
            {
                row--;
            }
            else if(op == OperationEnum.SET_ROW)
            {
                if(rowSetting == -99)
                {
                    Err.error("When setting a row, need to know which one");
                }
                row = rowSetting;
                rowSetting = -99;
            }

            /*
            if(row < 0)
            {
            //Err.error( "How did we get an entered itemAdapter with row " +
            //           row + " ?");
            row = 0;
            }
            */
            if(calculationPlace == null)
            {
                calculationPlace = result.getCalculationPlace();
            }
            AbstractTableItemAdapter newAdapter = new TableItemAdapter(ta, row, calculationPlace);
            debugId = newAdapter.getId();
            pr("@@@ TableItemAdapter ID that used to row++ is: " + newAdapter.getId());
            result = newAdapter;
        }
        else if(result instanceof FieldItemAdapter)
        {
            result.setOriginalAdapter(result);
        }
        return result;
    }

    /**
     * RUBBISH below - in the end this wasn't the cause of the problem. See
     * SdzBug.initialRowWrong for what was the cause of the problem.
     * <p/>
     * The row++ in formAdapterReleased was actually occuring too early. If
     * the insert drew a validation problem then the system (MoveTracker) was
     * trying to move back to a place that the 'cursor' did not get to in
     * the first place. Thus this exception was being drawn:
     * Exception in thread "AWT-EventQueue-0" java.lang.IllegalArgumentException: Row index out of range
     * at javax.swing.JTable.boundRow(JTable.java:1347)
     * at javax.swing.JTable.setRowSelectionInterval(JTable.java:1370)
     */
    public void notifySuccessfulInsert()
    {
        if(adapterReleased != null && adapterReleased instanceof AbstractTableItemAdapter)
        {
            AbstractTableItemAdapter adapter = (AbstractTableItemAdapter) adapterReleased;
            pr("@@@ TableItemAdapter ID that now going to row++ is: " + adapter.getId());
            if(adapter.getId() != debugId)
            {
                Err.error("Were about to increment the wrong tableAdapter");
            }
            else
            {
                int row = adapter.getRow();
                row++;
                adapter.setRow(row);
            }
        }
    }

    public void accessChange(AccessEvent event)
    {
    }

    public void nodeChangePerformed(NodeChangeEvent e)
    {
        MoveNodeI nodeI = (MoveNodeI) e.getSource();
        currentMoveBlock = null;
        for(Iterator iter = moveBlocks.iterator(); iter.hasNext();)
        {
            MoveBlock mBlock = (MoveBlock) iter.next();
            if(mBlock.getNodeI() == nodeI)
            {
                currentMoveBlock = mBlock;
                break;
            }
        }
        if(currentMoveBlock == null)
        {
            Print.prList(moveBlocks, "MoveManager knows these nodes");
            Err.error("MoveManager doesn't know about node " + nodeI.getName());
        }
    }

    public ItemAdapter getConfirmedErrorAdapter()
    {
        return confirmedErrorAdapter;
    }

    public String toString()
    {
        String result = super.toString();
        result += " (" + id + ")";
        return result;
    }

    public IdentifierI getNode()
    {
        return null;
    }
}
