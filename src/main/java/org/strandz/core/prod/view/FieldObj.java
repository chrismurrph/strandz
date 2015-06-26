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
package org.strandz.core.prod.view;

import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.ControlSignatures;
import org.strandz.core.domain.DOAdapter;
import org.strandz.core.domain.FieldItemAdapter;
import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.ItemSnapshot;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.widgets.TableComp;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.CautiousArrayList;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.StopWatch;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.widgets.ObjComp;

import javax.swing.JComponent;
import javax.swing.JTextField;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A SubRecordObj for a single row set of fields. Much use is made of the composite
 * pattern (accessed by children) so that a Block need only talk to the
 * isLookedupByNone SubRecordObj. Also much use is made of all the Adapters in
 * attributes to transfer between (etc) the UI control and the actual object
 * with the data in it.
 */
public class FieldObj extends SubRecordObj implements AbstFieldObj
{
    private NodeTableModelImplI ntmii;
    // this is what the image looked like before it got updated:
    // Now in Adapter:
    // private ArrayList b4Image = new ArrayList();
    /**
     * The 'detail' side, which may be composite or null, or another SubRecordObj.
     * A possible synonym would be lookupees - the other AbstObjects that this
     * AbstObj may lookup further data from.
     */
    AbstFieldObj children;
    IdEnum idEnum;
    private Object newElement;
    private boolean haveFieldsChangedRunning = false;
    private Map<Integer, Object> derivedData = new HashMap<Integer, Object>();
    private static int constructedTimes;
    int id;
    private static int times;
    private static int setDisplayTimes;
    private static int timesEnabled;
    private static int timesBlank;
    private static int timesDerived;

    public static final int VISUAL_ONLY = 1;
    public static final int NON_VISUAL_ONLY = 2;
    public static final int VISUAL_AND_NON_VISUAL = 3;

    /**
     * After has done most of creation, if find a set of child instantiables
     * then can call createRecordObj, and set result to own child. createRecordObj
     * will call this constructor method again, thus the many levels composite
     * is built by recursion.
     */
    FieldObj(IdEnum idEnum,
             SubRecordObj parent,
             ViewBlockI block,
             CreatableI creatable,
             NodeTableModelImplI ntmii)
    {
        super(parent, block, creatable);
        constructedTimes++;
        id = constructedTimes;
        this.idEnum = idEnum;
        this.ntmii = ntmii;
        if(!creatable.getLookups().isEmpty())
        {
            // Err.pr("No of lookups are " + creatable.getLookups().size());
            // Err.pr("Lookups passing are " + creatable.getLookups());
            children = (AbstFieldObj) Builder.createAbstObj(idEnum, this, block,
                creatable.getLookups(), ntmii);
        }
        else
        {
            children = new NullFieldObj();
        }
        // Err.pr( "==================== fieldObj being created for " + creatable);
    }

    public String getName()
    {
        if(idEnum.getControl() != null)
        {
            return idEnum.getClazz().getName();
        }
        else
        {
            return idEnum.toString();
        }
    }
    
    public void blankoutDisplay(OperationEnum currentOperation, String reason, boolean removeComboItems)
    {
        blankoutDisplay( currentOperation, reason, Utils.UNSET_INT, removeComboItems);
    }
    
    /**
     * setUI is normally only called when the user presses a button eg
     * next row. setUI has side effect of creating objects that need to
     * exist. When a user enters data no objects are created. For
     * consistency we don't create any objects for default values either.
     */
    public void blankoutDisplay(OperationEnum currentOperation, String reason, int row, boolean removeComboItems)
    {
        StopWatch stopWatch = new StopWatch( SdzNote.SLOW_SYNC.isVisible());
        stopWatch.startTiming();
        /*
        times++;
        Err.pr( "1 Inside blankoutDisplay for " + this + " times " + times);
        if(times == 8)
        {
            Err.debug();
        }
        */
        // Err.pr( "\tdefaultElement " + defaultElement);
        if(defaultElement != null)
        {
            ItemAdapter ad;
            //Err.pr( "\tdefault element: " + defaultElement);
            //Err.pr( "\tdefaultElement is of type " + defaultElement.getClass().getName());
            for(Iterator e = getAdapters().allIterator(); e.hasNext();)
            {
                ad = (ItemAdapter) e.next();
                //Err.pr( "blankingPolicy will use: " + ControlSignatures.getBlankingPolicy( ad.component));
                IdEnum idEnum;
                //Comments kept in case doesn't work
                //if(row == Utils.UNSET_INT)
                //{
                    idEnum = ad.createIdEnum( row, toString());
                //}
                //else
                //{
                //    idEnum = ((TableItemAdapter)ad).createIdEnum( row);
                //}
                ControlSignatures.setText(
                    idEnum,
                    ad.getDoAdapter().getFieldValue
                        (defaultElement));
                ad.blankedoutValue = null;
            }
            consumedDefaultElement = defaultElement;
            defaultElement = null;
        }
        else
        {
            /*
            * This is needed so record becomes visually blank.
            */
            // Err.pr( "1a null attribributes: " + attributes.size());
            ItemAdapter ad;
            for(Iterator e = getAdapters().allIterator(); e.hasNext();)
            {
                Object obj = e.next();
                ad = (ItemAdapter) obj;
                Object table = ad.getTableControl();
                if(table != null)
                {
                    //Err.error( "No longer happening now MExclusive");
                    if(ntmii == null)
                    {
                        Err.error( "Adapter has a TableControl, but no NodeTableModelImplI");
                    }
                    else
                    {
                        String tablesName = null;
                        if(table instanceof JComponent)
                        {
                            tablesName = ((JComponent)table).getName();
                            if(tablesName == null)
                            {
                                Err.pr( "parent is a " + ((JComponent)table).getParent().getClass().getName());
                                Err.pr( "parent's name is " + ((JComponent)table).getParent().getName());
                                Err.error( "Table does not have a name, type is " + table.getClass().getName());
                            }
                        }
                        else if(table instanceof TableComp)
                        {
                            tablesName = ((TableComp)table).getName();
                        }
                        if(!ntmii.getUsersModel().getTableName().equals( tablesName))
                        {
                            if(table instanceof TableComp)
                            {
                                /*
                                 * For an as yet unknown reason tests can fail here:
                                 * NodeTableModelImplI uses <QuickViewRosterSlotsComponentTable> while from adpater 
                                 * get <A TableComp (with no model), ID: 1>
                                 * Perhaps don't bother to hook it up for non visual ... 
                                 */
                            }
                            else
                            {
                                /*
                                 * This will occur when you are trying to share attributes
                                 */
                                Err.error( "Different names, NodeTableModelImplI uses <" + ntmii.getUsersModel().getTableName() + 
                                        "> while from adpater get <" + tablesName + ">. Don't share TableAttributes!");
                            }
                        }
                    }
                }
                if(row != -1) //Will be -1 when is dealing with a table that has no rows
                {
                    //Err.pr( SdzNote.BI_AI, "To setTextBlank in " + this);
                    if(currentOperation.isInsert() || currentOperation == OperationEnum.ENTER_QUERY ||
                        currentOperation == OperationEnum.REMOVE)
                    {
                        ControlSignatures.setTextBlank( ad.createIdEnum( row, toString()), removeComboItems);
                    }
                    else
                    {
                        Err.pr( SdzNote.SET_TEXT_BLANK, "To NOT setTextBlank() in " + this + " with row " + row + 
                                " for op " + currentOperation);
                    }
                }
                else
                {
                    Err.pr( SdzNote.NODE_GROUP, "With JTable think ControlSignatures.setTextBlank would be called for " + block.getName());
                }
                ad.blankedoutValue = null;
                if(ad.isInError())
                {
                    Err.pr(SdzNote.STILL_RED_WHEN_DELETE, "Is in error when blanking for " + ad.toString());
                    ad.setInError(false);
                }
            }
        }
        Err.pr( SdzNote.SET_TEXT_BLANK, "In " + this + " in <" + block.getName() + "> about to see if has an ntmii: " + 
                (ntmii != null) + " for op " + currentOperation + " (or) " + block.getCurrentOperation());
        /*
        if((ntmii != null) && block.getName().equals( "rosterSlots Quick List Detail Node"))
        {
            Err.debug();    
        }
        */
        /* Moved to block
        if(ntmii != null)
        {
            ntmii.blankoutDisplay( currentOperation, "blankoutDisplay from FieldObj where have a tableModel because " + reason);
        }
        */
        stopWatch.stopTiming();
        if(stopWatch.getResult() > 0)
        {
            Err.pr( "blankoutDisplay took " + stopWatch.getElapsedTimeStr());
        }
        children.blankoutDisplay(currentOperation, "children blankoutDisplay because " + reason, removeComboItems);
    }
    
    public void displayLovObjects() 
    {
        adapters.displayLovObjects();
        children.displayLovObjects();
    }

    public boolean displayIsBlank()
    {
        boolean result = true;
        ItemAdapter ad;
        for(Iterator e = getAdapters().allIterator(); e.hasNext();)
        {
            ad = (ItemAdapter) e.next();
            if(ad instanceof FieldItemAdapter)
            {
                FieldItemAdapter fa = (FieldItemAdapter) ad;
                if(fa.getItem() instanceof JTextField)
                {
                    JTextField tf = (JTextField) fa.getItem();
                    // Err.pr( "To check for blank field of <" + tf.getText() + ">");
                }
            }
            result = ControlSignatures.isTextBlank(ad.createIdEnum( toString()));
            if(!result)
            {
                break;
            }
        }
        if(result)
        {
            /*
            * Whenever blank keep trying with children. Will eventually ensure that
            * all children are blank to return true.
            */
            result = children.displayIsBlank();
        }
        return result;
    }
    
    /**
     * When first show an element, and when move to a new element, syncDetail()
     * is called. When have just created a new element syncDetail() is not
     * called for that element, thus a separate method for creating a b4Image
     * is required. Same as old assignElementToUI(..), except for the assign line.
     * Instead of assignElementToUI we now use the set param.
     * <p/>
     * We already know which fields from the parameter 'element' we
     * want to display. (adapters). Thus can extract the text values
     * from element (DO) and display them in the related components.
     * (Each field has a related component - see FieldItemAdapter). At same
     * time a b4Image can be created, so when a key is pressed in Editing
     * mode, we can determine what has changed since the initial assignment.
     */
    public void setDisplay(
        OperationEnum currentOperation, int idx, Object element, boolean changedValuesOnly, boolean set, String reason)
    {
        /*
        setDisplayTimes++;
        Err.pr( "-----------------setDisplay times " + setDisplayTimes + " for " + this);
        if(setDisplayTimes == 23)
        {
            Err.debug();
        }
        */
        StopWatch stopWatch = new StopWatch( SdzNote.SLOW_SYNC.isVisible());
        stopWatch.startTiming();
        if(set)
        {
            adapters.displayLovObjects();
        }
        if(!changedValuesOnly)
        {
            uiItemsAll.clear();
            internalItemsAll();
            // Err.pr( "###ALL (setDisplay()): " + uiItemsAll);
        }
        else
        {
            if(set)
            {
                Err.error("Weird to be setting the display when have made changes");
            }
            else
            {
                uiItemsChanged.clear();
                internalItemsChanged();
                // Err.pr( "###CHANGED (setDisplay()): " + uiItemsChanged);
            }
        }

        Object newElement = null;
        if(element != null)
        {
            newElement = referenceAcross(element, changedValuesOnly,
                block.getCurrentOperation());
            derivedData.put( idx, newElement);
            timesDerived++;
            Err.pr( SdzNote.DERIVED_DATA, "Derived " + newElement + " in " + this + " at " + idx + " times " + timesDerived);
            if(timesDerived == 0)
            {
                Err.stack();
            }
        }
        if(newElement == null)
        {// Can't always find a lookup match
            // Err.error( "setDisplay requires new element");
            // Err.pr( "Lookup gives us a null");
        }

        ItemAdapter itemAdapter;
        Object text = null;
        for(Iterator e = getAdapters().allIterator(); e.hasNext();)
        {
            itemAdapter = (ItemAdapter) e.next();
            if(!itemAdapter.isReadExternally() && !itemAdapter.isCalculated())
            {
                if(newElement != null)
                {
                    text = itemAdapter.getDoAdapter().getFieldValue(newElement);
                }
                /*
                 * ntmii takes care of setting for the table - but it misses non-visual controls such as
                 * ObjComp - so here we ensure that they are set - but leave the others
                 */
                if(set && (ntmii == null || (itemAdapter.getItem() instanceof ObjComp)))
                {
                    Err.pr(SdzNote.CTV_STRANGE_LOADING, "ItemAdapter: " + itemAdapter);
                    Err.pr(SdzNote.CTV_STRANGE_LOADING, "doAdapter: " + itemAdapter.getDoAdapter());
                    if(element != null)
                    {
                        ControlSignatures.setText(itemAdapter.createIdEnum(idx, toString()), text); // assign line
                        Err.pr(SdzNote.REFRESH_DETAIL, "setDisplay() done, have set row " + idx + " to " + text);
                    }
                    else
                    {// Will have already blanked out, so no need to do it
                        // again. Assuming that element == null only occurs
                        // when called when no records exist. Really done so
                        // b4Image values are set sensibly.
                    }
                }
                if(!itemAdapter.isInError() /*&& !ad.isCalculated()*/)
                {
                    if(! block.isManualBeforeImageValue())
                    {
                        if(element != null)
                        {
                            itemAdapter.setB4ImageValue(text);
                        }
                        else
                        {
                            itemAdapter.setB4ImageValue(ControlSignatures.getBlankText(itemAdapter.createIdEnum(toString())));
                        }
                    }
                    else
                    {
                        //External program will call attribute.setB4ImageValue()
                    }
                }
                else
                {/*
             Err.error( "Nearly wrote away an attribute that was inError, " +
             "think caused by setDisplay()");
             */
                }
            }
            else
            {
                //Err.pr( "Not setting display on " + itemAdapter);
            }
        }
        /* Moved to block - as works for a whole table and this was at the lookup level
        if(ntmii != null)
        {
            times++;
            String txt = "times " + times + " setDisplay from FieldObj where have a tableModel, because " + reason;
            if(times == 38)
            {
                Err.debug();
            }
            Err.pr( SdzNote.SET_DISPLAY_ON_TABLE, txt);
            ntmii.setDisplay( currentOperation, txt);
        }
        */
        stopWatch.stopTiming();
        if(stopWatch.getResult() > 0)
        {
            Err.pr( "setDisplay took " + stopWatch.getElapsedTimeStr());
        }
        children.setDisplay( currentOperation, idx, newElement, changedValuesOnly, set, "setDisplay for children because " + reason);
    }

    public void getDisplay(Object element, boolean changedValuesOnly)
    {
        /*
        times++;
        Err.pr( "In getDisplay() element is " + element);
        Err.pr( "In getDisplay() changedValuesOnly is " + changedValuesOnly + " times " + times);
        if(times == 2)
        {
        Err.debug();
        }
        */
        boolean noneToExamine = false;
        if(changedValuesOnly)
        {
            uiItemsChanged.clear();
            internalItemsChanged();
            /*
            times0++;
            Err.pr( "###CHANGED (getDisplay()): " + uiItemsChanged + " times " + times0 + " in " + this);
            if(times0 == 0)
            {
            Err.stack();
            }
            */
            if(uiItemsChanged.isEmpty())
            {
                noneToExamine = true;
            }
        }
        else
        {
            uiItemsAll.clear();
            internalItemsAll();
            if(uiItemsAll.isEmpty())
            {
                noneToExamine = true;
            }
            // Err.pr( "###ALL (getDisplay()): " + uiItemsAll);
        }

        /*
        for( e=uiFieldsChanged.iterator(); e.hasNext();)
        {
        UIField uif = (UIField)e.next();
        if(uif.getAfterImage() == null)
        {
        Err.error( "Thought always have already read!");
        Object aiValue = ControlSignatures.getText(
        uif.itemAdapter.createIdEnum());
        Err.pr( "$2$ Using itemAdapter " + uif.itemAdapter);
        Err.pr( "$2$ Will set " + aiValue + " of type " + aiValue.getClass().getName());
        uif.setAfterImage( aiValue);
        }
        else
        {
        }
        }
        */

        Object newElement;
        if(!noneToExamine)
        {
            // Err.pr( "INTO referenceDown() " + element);
            doNeedToFill();
            newElement = referenceAcross(element, changedValuesOnly, null);
            // Err.pr( "OUT FROM referenceDown() " + newElement);

            /*
            * Here we are recording exactly what the user did. In this way
            * that will be what is played back. We can't do it later after
            * the noNeedToFill subversion.
            */
            if(newElement != null)
            {
                if(changedValuesOnly)
                {
                    for(Iterator e = uiItemsChanged.iterator(); e.hasNext();)
                    {
                        ItemSnapshot uif = (ItemSnapshot) e.next();
                        ItemAdapter ad = uif.itemAdapter;
                        ad.getDoAdapter().recordSetValue(uif.getAfterImage());
                    }
                }
                else
                {
                    for(Iterator e = uiItemsAll.iterator(); e.hasNext();)
                    {
                        ItemSnapshot uif = (ItemSnapshot) e.next();
                        ItemAdapter ad = uif.itemAdapter;
                        ad.getDoAdapter().recordSetValue(uif.getAfterImage());
                    }
                }
            }
            if(isNeedToFill())
            {
                // Err.pr( "--NORMAL FOR " + this);
                if(SdzNote.DYNAM_ATTRIBUTES.isVisible())
                {
                    Print.prList( uiItemsChanged, "UI items that have changed");
                }
                if(newElement != null)
                {
                    if(changedValuesOnly)
                    {
                        for(Iterator e = uiItemsChanged.iterator(); e.hasNext();)
                        {
                            ItemSnapshot uif = (ItemSnapshot) e.next();
                            // Err.pr( "$2$ In " + newElement);
                            uif.itemAdapter.getDoAdapter().setFieldValue(newElement,
                                uif.getAfterImage());
                        }
                    }
                    else
                    {
                        for(Iterator e = uiItemsAll.iterator(); e.hasNext();)
                        {
                            ItemSnapshot uif = (ItemSnapshot) e.next();
                            // Err.pr( "$2$ In " + newElement);
                            uif.itemAdapter.getDoAdapter().setFieldValue(newElement,
                                uif.getAfterImage());
                        }
                    }
                }
            }
            else
            {
                // Err.pr( "--NOT NORMAL FOR " + this);
                /**
                 Err.pr( "@@@ Need assign <" + newElement + "> into <" + element + ">");
                 Err.pr( "@@@ parent <" + parent + ">");
                 //Err.pr( "@@@ has adapters <" + parent.getAdapters() + ">");
                 */
                DOAdapter doAdapter = null;
                if(newElement == null)
                {
                    doAdapter = parent.getAdapters().getDOAdapter();
                    if(doAdapter == null)
                    {
                        Err.error(
                            "Won't be able to find an Adapter when newElement == null");
                    }
                }
                else
                {
                    /*
                    Err.pr( "@@@ Assignee type is <" + newElement.getClass() + ">");
                    Err.pr( "@@@ Will look in <" + parent.getAdapters() + ">");
                    Err.pr( "@@@ tie's parentObj is <" + tie.getParent() + ">");
                    Err.pr( "@@@ tie's childObj is <" + tie.getParent() + ">");
                    */
                    // Will doing it by class ever cause a problem? YES!! Thus fixed
                    // by using the tie's childField
                    doAdapter = parent.getAdapters().getDOAdapter(tie.getChildField());
                }
                // Err.pr( "@@@ itemAdapter: " + itemAdapter + " in " + this);
                doAdapter.setFieldValue(element, newElement);
                doNeedToFill(); // back to normal, where above will be done
            }
        }
        else
        {
            newElement = referenceAcross(element, changedValuesOnly, null);
        }
        children.getDisplay(newElement, changedValuesOnly);
    }

    /**
     * Goes recursively to the bottom to see if any fields have
     * changed.
     */
    public boolean haveFieldsChanged()
    {
        boolean result;
        uiItemsChanged.clear();
        haveFieldsChangedRunning = true;
        /*
        times++;
        Err.pr( "To call internalItemsChanged() times " + times);
        if(times == 36)
        {
        Err.debug();
        }
        */
        internalItemsChanged();
        haveFieldsChangedRunning = false;

        Object obj = this;
        if(!uiItemsChanged.isEmpty())
        {
            // Err.pr( "-----------CHANGED for " + this);
            result = true;
        }
        else
        {
            result = children.haveFieldsChanged();
        }
        if(!result)
        {// Err.pr( "-----------NOT CHANGED for " + this);
        }
        return result;
    }

    /*
    public ArrayList getOutputAttributes()
    {
    ArrayList fields = new ArrayList();
    Iterator e;
    int i;
    for( i=0,e=attributes.iterator(); e.hasNext(); i++)
    {
    FieldItemAdapter ad = (FieldItemAdapter)e.next();
    OutputAttribute oa = new OutputAttribute(
    ad,
    ControlSignatures.getText( ad.component));
    fields.add( oa);
    }
    fields.addAll( children.getOutputAttributes());
    //Err.pr( "getOutputAttributes() to ret " + fields);
    return fields;
    }
    */

    /**
     * Does not go recursively, but is called recursively.
     */
    private void internalItemsAll()
    {
        ItemAdapter ad;
        Object aiValue;
        ItemSnapshot uif;
        Iterator e;
        for(e = getAdapters().allIterator(); e.hasNext();)
        {
            ad = (ItemAdapter) e.next();
            aiValue = ControlSignatures.getText(ad.createIdEnum( toString()));
            uif = new ItemSnapshot();
            uif.name = ad.getDoAdapter().getDOFieldName();
            uif.setAfterImage(aiValue);
            // Err.pr( "--- AI been set to <" + aiValue + "> for <" + ad.doFieldName + ">");
            // Err.stack();
            uif.itemAdapter = ad;
            uiItemsAll.add(uif);
        }
    }

    /**
     * In here differences are held in case some sort of
     * record validation fails. It is assumed that a user's trigger would
     * need a list of the fields that have changed.
     */
    /*
    * getDisplay() does same thing
    public void applyDifferences( Object element)
    {
    UIField uif;
    Iterator e;
    int i;

    //Err.pr("inside applyDifferences for " + this);
    Object newElement = referenceDown( element, false);

    *
    * With these 2 calls we collect the data for the level
    * of recursion we are currently at.
    *
    uiFieldsChanged.clear();
    internalFieldsChanged();

    if(newElement == null)
    {
    return;
    }

    for( i=0, e=uiFieldsChanged.iterator(); e.hasNext(); i++)
    {
    uif = (UIField)e.next();
    if(newElement != null)
    {
    //Err.pr( "field changed is " + uif.itemAdapter);
    uif.itemAdapter.setFieldValue(
    newElement,
    uif.getAfterImage());
    }
    else
    {
    Err.error("applyDifferences really requires new newElement");
    }
    }
    children.applyDifferences( newElement);
    }
    */
    public void setDisplayEnabled(boolean enabled)
    {
        if(getAdapters().allIsEmpty())
        {// This not only happens in test cases, but also when lookup thru a
            // lookup
            //
            // In test cases don't want this to be an error
            // Err.pr( "No attributes so can't change their editability for " + this);
            // Err.soundOff();
        }
        else
        {
            ItemAdapter ad;
            for(Iterator e = getAdapters().allIterator(); e.hasNext();)
            {
                ad = (ItemAdapter) e.next();
                if(!ad.isUpdate() && enabled)
                {
                    // nufin
                    Err.pr( SdzNote.ENABLEDNESS_REFACTORING, "Not calling ad.setEnabled() for " + ad);
                }
                /*
                 * No point in changing the editability of a non-applichousing, but it should work!
                 * (Doing it this way may introduce a bug as we are not keeping any state
                 * of whether enabled or not for non-visuals - and code can rely on that)
                 */
//                else if(!ad.isVisual())
//                {
//                    // nufin
//                }
                else if(!ad.isAlwaysEnabled())
                {
                    timesEnabled++;
                    Err.pr( SdzNote.ENABLEDNESS_REFACTORING, "setEnabled() for <" + ad + ">, to set to <" + ad.isEnabled() + "> times " + timesEnabled);
                    if(timesEnabled == 0)
                    {
                        Err.stack();
                    }
                    //Wrong that not using the passed in value:
                    ControlSignatures.setEnabled(ad.createIdEnum( toString()), ad.isEnabled() /*enabled*/);
                }
            }
        }
        children.setDisplayEnabled( enabled);
    }

    public void completeBackgroundAdd( OperationEnum currentOperation, int idx)
    {
        // false means not actually setting the UI. All doing is setting the
        // B4Image
        setDisplay( currentOperation, idx, newElement, true, false, "completeBackgroundAdd");
        getAdapters().getCalculationPlace().fireCalculationFromSync( getAdapters().getId(), newElement, idx);
    }

    public void backgroundAdd(boolean isPrior)
    {
        Err.pr(SdzNote.BG_ADD, "############### In backgroundAdd for block " +
            getInstantiable().getClassObject().getName());
        if(!creatable.hasNewInstanceTrigger())
        {
            Err.pr(SdzNote.BG_ADD, "No newInstanceTrigger exists!");
            if(getInstantiable().equals(""))
            {
                Err.error("An instantiable Class has not been set for " + block);
            }
            newElement = ObjectFoundryUtils.factory(getInstantiable().getClassObject());
            if(newElement == null)
            {
                Err.error("Was not able to create a " + getInstantiable().getClassObject().getName());
            }
            else
            {/*
         Err.pr( "Created the element of type " + newElement.getClass().getName());
         if(newElement.getClass() == RosterSlot.class)
         {
         Err.pr( "$$$ background RosterSlot: " + ((RosterSlot)newElement).id);
         }
         */}
        }
        else
        {
            Err.pr(SdzNote.BG_ADD, "To fire newInstanceTrigger");
            newElement = creatable.firePrimaryNewInstanceTrigger();
            if(newElement == null)
            {
                Err.error(
                    "Was not able to create a " + getInstantiable().getClassObject().getName()
                        + " from Cell's trigger");
            }
        }
        /*
        * MOSTLY RUBBISH
        * Have just created one new element. This method is not recursive.
        * The creation of lookups is done in the following call. The end-most
        * lookup is assigned. For all the inbetween ones a factory call like
        * the above is done. In the future perhaps sets will be done as
        * go from outermost lookup to innermost lookup.
        */
        getDisplay(newElement, true);
        // insert at the element after current one
        /*
        times++;
        Err.pr(SdzNote.BG_ADD,
            "&&& Inserting created element at " + (block.getIndex()) + " for "
                + block + " times " + times);
        if(times == 3)
        {// Err.stack();
        }
        */
        int idx = block.getIndex();
        /* This comment out - as simplifying
        if(!isPrior)
        {
            idx++; // To correct for fact that now incing after backgroundAdd
        }
        */
        /*
        if(idx == -1)
        {
        idx = 0; //if this is the first element that we are inserting
        }
        */
        block.insertDataRecord(idx, newElement);
    }

    /**
     * These two copied from TableObj as needed for fillFlat, which
     * need to do to display the non-current rows.
     */
    public List getAdaptersArrayList()
    {
        CautiousArrayList sv = new CautiousArrayList();
        List result = getAdapters(sv, VISUAL_AND_NON_VISUAL, false);
        return result;
    }

    public CautiousArrayList getAdapters(
        CautiousArrayList v, int visualType, boolean tableOnly)
    {
        ItemAdapter ad;
        CautiousArrayList newVector = new CautiousArrayList();
        for(Iterator e = getAdapters().allIterator(); e.hasNext();)
        {
            ad = (ItemAdapter) e.next();
            if(visualType == VISUAL_AND_NON_VISUAL)
            {
                //Interesting that was getting "PROGRAM ERROR:" without the stack
                //trace when ran this as a JUnit test. Then run thru main and got
                //a stack trace
                //Err.error( "Not used in FieldObj.getAdapters()");
                if(tableOnly == true)
                {
                    Err.error("Not yet coded for VISUAL_AND_NON_VISUAL tableOnly");
                }
                newVector.add(ad);
            }
            else if(visualType == VISUAL_ONLY)
            {
                if(ad.isVisual())
                {
                    if(!tableOnly)
                    {
                        newVector.add(ad);
                    }
                    else if(ad instanceof AbstractTableItemAdapter)
                    {
                        newVector.add(ad);
                    }
                }
            }
            else if(visualType == NON_VISUAL_ONLY)
            {
                if(!ad.isVisual())
                {
                    if(!tableOnly)
                    {
                        newVector.add(ad);
                    }
                    else if(ad instanceof AbstractTableItemAdapter)
                    {
                        newVector.add(ad);
                    }
                }
            }
            /*
            else if(ad.isVisual())
            {
              if(!tableOnly)
              {
                newVector.add( ad);
              }
              else if(ad instanceof TableItemAdapter)
              {
                newVector.add( ad);
              }
            }
            */
        }
        if(!(children instanceof NullFieldObj))
        {
            newVector.addAll(children.getAdapters(v, visualType, tableOnly));
        }
        return newVector;
    }

    public List getVisualAdaptersArrayList()
    {
        CautiousArrayList sv = new CautiousArrayList();
        List result = getAdapters(sv, VISUAL_ONLY, false);
        Collections.sort(result);
        return result;
    }

    public List getVisualTableAdaptersArrayList()
    {
        CautiousArrayList sv = new CautiousArrayList();
        List result = getAdapters(sv, VISUAL_ONLY, true);
        Collections.sort(result);
        return result;
    }

    public List getNonVisualTableAdaptersArrayList()
    {
        CautiousArrayList sv = new CautiousArrayList();
        List result = getAdapters(sv, NON_VISUAL_ONLY, true);
        Collections.sort(result);
        return result;
    }

    /**
     * Here the recursive and flat worlds meet one another. The list of
     * tableAdapters given as a param here is from the flat world, and
     * there is one for each column. This list may encompass many TableObjs.
     * Once we have filled the Adapters of a particular TableObj we recursively
     * lookup (referenceDown) the Object for the next TableObj and then call
     * ourselves.
     * As the tableAdapters span many FieldObjs and therefore many data objects
     * we need a mechanism to know which Adapters have not yet been filled in. The
     * mechanism used is whether the Adapter has been refreshed.
     */
    public void fillAdaptersFlatObjectsTable(Object leftElement)
    {
        int numTheseAdapters = getAdapters().tableSize();
        // Err.pr( "-------------");
        // Err.pr( "fieldObj doing this for is " + this);
        // Err.pr( "Num of Adapters here: " + numTheseAdapters);

        Object sf = referenceAcross(leftElement, false, null);
        // Err.pr( "Done referenceDown from " + leftElement + " and got " + sf);
        List adapters = getAdapters().getTableAdapters();
        for(int i = 0; i <= adapters.size() - 1; i++)
        {
            AbstractTableItemAdapter ta = (AbstractTableItemAdapter) adapters.get(i);
            // Err.pr( "TableItemAdapter at " + i + " is " + ta);
            ta.setStoredFlatObject(sf);
            // ta.setRecursivelyRefreshed( true);
        }
        children.fillAdaptersFlatObjectsTable(sf);
        // Err.pr( "================");
    }

    /**
     * Does not go recursively, but is called recursively.
     */
    private void internalItemsChanged()
    {
        for(Iterator e = getAdapters().allIterator(); e.hasNext();)
        {
            ItemAdapter ad = (ItemAdapter) e.next();
            if(!ad.isCalculated() && !ad.isDataReadExternally())
            {
                Err.pr( SdzNote.SET_OBJCOMP_TABLE, "Adapter " + ad + ", of type " + ad.getClass().getName());
                Object aiValue = ControlSignatures.getText(ad.createIdEnum( toString()));
                boolean theyAreEqual = false;
                boolean afterImageNullAndNotEqual = false;
                Object b4ImageVal = ad.getB4ImageValue();
                /**/
                if(/*debugBlock &&*/ /*ad.getDoAdapter().getDOFieldName().equals("active")*/ true)
                {
                    Err.pr( SdzNote.SYNC, "DOFieldName: <" + ad.getDoAdapter().getDOFieldName() + ">");
                    Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS.isVisible() || 
                            SdzNote.SYNC.isVisible(), "BI: <" + b4ImageVal + ">");
                    Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS.isVisible() || 
                            SdzNote.SYNC.isVisible(), "AI: <" + aiValue + ">");
                    //Err.stack();
                }
                /**/
                if(ad.afterAndB4Different(aiValue, b4ImageVal, uiItemsChanged))
                {// Err.pr( "Difference in " + ad);
                }
            }
        }
    }
        
    public void setDerivedAt( int idx, Object element)
    {
//        if(!derivedData.containsKey( idx))
//        {
            derivedData.put( idx, element);
            children.getDerivedAt( idx, element);
//        }
//        else
//        {
//            if(!derivedData.get( idx).equals( element))
//            {
//                Err.error( "Just always put and do the children - otherwise won't cope with derived values changing");
//            }
//        }
    }

    public Object getDerivedAt( int idx, Object element)
    {
        if(!derivedData.containsKey( idx))
        {
            //Print.prMap( derivedData);
            //Assert.notNull( element, "derived data in <" + this + "> does not contain " + idx + ", and to get it" +
            //        " we will need to referenceDown using a value that is currently null!");
            if(element != null)
            {
                Err.pr( SdzNote.RECORD_CURRENT_VALUE, "\tIn getDerivedAt() for " + this + " to referenceDown using a <" + element.getClass().getName() + ">");
                Object newElement = referenceAcross(element, false, block.getCurrentOperation());
                setDerivedAt( idx, newElement);
            }
            else
            {
                //Even thou looked up the parent's derivedData.get( idx) element may be null
                derivedData.put( idx, null);
            }
        }
        return derivedData.get( idx);
    }

    public String getDescription()
    {
        return idEnum.getName();
    }
} // end class
