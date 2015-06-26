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

import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.ItemSnapshot;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.lgpl.extent.CombinationExtent;
import org.strandz.lgpl.extent.IndependentExtent;
import org.strandz.lgpl.extent.Tie;
import org.strandz.lgpl.extent.VisibleExtent;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.DOHelperUtils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NamedI;
import org.strandz.lgpl.util.NullVerifiable;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Some Sub-Record Objects can have their data value set by the user. The user
 * does this by calling setData() on Cell, which gets to here
 * thru Creatable. Other Sub-Record Objects are part of the composite which is
 * created in Builder, and have their data set when for instance a user moves
 * to a new record. The Ties that are interlinked to the Sub-Record Objects in this
 * constructor, as well as the Adapters from AdaptersList, provide the information
 * necessary to allow this composite/recursive to create and update the right
 * objects at runtime.
 * <p/>
 * Contains references to both children and parent so it has a place in the
 * lookup hierarchy. The children uses the composite features of an
 * AbstObj. (For example an Aeroplane might have both AeroplaneType and
 * Hanger as its children).
 * <p/>
 * If a SubRecordObj is distinguished by being <isLookedupByNone == true> then
 * it will not have a parent, and will not have a tie. The parent refers to
 * another SubRecordObj (a data provider), and a Tie contains all the details of
 * this relationship. (Tie has parent and child the other way round).
 * <p/>
 * Note that when rows are saved, by either Table or Field Obj,
 * we rely on the fact that any altering of a value in a
 * control will result in a new value - ie./ underlying object
 * is immutable. For more complex objects a copy method will have
 * to be defined.
 */
abstract public class SubRecordObj implements AbstInterf, NamedI
{
    /**
     * Reasons the various methods here are called.
     * Introduced when table first created (ie. FieldObj
     * will ignore them)
     */
    // setDisplay
    public static final int START = 1;
    public static final int ROW_CHANGE = 2;
    public static final int REMOVE_WHILST_ADDING = 3;
    // blankoutDisplay
    // public static final int NAVIGATING_DELETE = 4;
    // public static final int NEW_DELETE = 5; //not used as not needed
    // public static final int ADD = 6;
    // public static final int RELOAD = 8;
    /**
     * Used only in Block.syncDetail
     */
    // public static final int OWN_HAS_CHANGED = 9;
    // public static final int MASTER_HAS_CHANGED = 10;
    // public static final int MASTER_IS_NEW = 11;
    /**
     * Used only in Block.syncDetail
     */
    public static final int DOWN_TO_DETAIL = 12;
    /**
     * Used when post calling syncDetail so that when visualCursorChange
     * is called it is only doing the marking of an inserted record, and not
     * setUIEditable, which of course will be unnecessary in any POST operation.
     * ie./ SET_ONLY s/be the parameter to visualCursorChange.
     */
    public static final int POST = 13;
    AdaptersListI adapters;
    /**
     * Many RecordObjs are contained within a Block.
     */
    ViewBlockI block;
    /**
     * Used if this object needs to lookup an object to which it does not have
     * a reference.
     */
    private IndependentExtent extent; // only needed if lookup was across an 'aggregate'
    public Object defaultElement; // consumed when UI is blanked out
    /**
     * So we can recognise that an element still has its default value, even if
     * the user has typed in and out some values.
     */
    public Object consumedDefaultElement;
    /**
     * If isLookupByNone, then parent and tie will be null.
     */
    boolean isLookedUpByNone = false;
    SubRecordObj parent;
    Tie tie;
    protected CreatableI creatable;
    ArrayList uiItemsChanged = new ArrayList();
    ArrayList uiItemsAll = new ArrayList();
    private boolean noNeedToFill;
    //private EntityManagerProviderI entityManagerProvider;
    public boolean debug = false;
    private static int times;
    private static int times1;
    private static int timesWorker;
    private static int timesLook;
    public boolean debugBlock;

    void doNotNeedToFill()
    {
        noNeedToFill = true;
    }

    void doNeedToFill()
    {
        noNeedToFill = false;
    }

    boolean isNeedToFill()
    {
        return !noNeedToFill;
    }

    SubRecordObj(SubRecordObj parent,
                 ViewBlockI block,
                 CreatableI creatable)
    {
        // Err.pr( "###CONSTRUCTING NEW SubRecordObj for " + block);
        if(parent == null)
        {
            isLookedUpByNone = true;
        }
        else
        {
            this.parent = parent;
        }
        this.block = block;
        creatable.setRecordObj( this);
        this.adapters = creatable.getAdapters();
        checkCreatable( creatable);
        this.creatable = creatable;

        // Too early - do at time user calls setLOV
        // this.lovObjects = creatable.getLovObjects();
        Tie tie = creatable.getTie();
        if(tie != null) // if isLookedUpByNone, then tie is always null
        {
            // new MessageDlg("prob is of type " +
            // tie.getChild().getClass() + " and is " +
            // tie.getChild());
            /*
            * Changing the character of a Tie from being between
            * CreatablesInterfaces to being between ChiefsObjs
            */
            CreatableI childObj = (CreatableI) tie.getChild();
            tie.setChild(childObj.getSubRecordObj());

            // debugging
            CreatableI parentObj = (CreatableI) tie.getParent();
            if(parentObj != creatable)
            {
                Err.error("thought would get parent");
            }
            tie.setParent(this);
        }
        this.tie = tie;
        // Err.pr( "recordObj " + this + " created with attributes " + attributes);
    }

    private void checkCreatable(CreatableI creatable)
    {
        if(debug)
        {
            Print.pr("++++++++" + creatable.getAdapters().getInstantiable().getClassObject().getName());

            Class result = Utils.checkAllSameClass(
                creatable.getAdapters().getAllAdapters());
            if(result == null)
            {
                Err.error("Not all same class");
            }
            else
            {
                Print.pr("++++++++" + "ADAPTORS ARE ALL " + result.getName());
            }
        }
    }

    /**
     * Call will always come straight thru as each Cell (now thru
     * Creatable) has a SubRecordObj.
     */
    public void setInitialData( IndependentExtent anyData)
    {
        if(isLookedUpByNone)
        {
            Assert.notNull( block);
            Assert.notNull( block.getTiesManager(), "No TiesManager in block ID: " + block.getId());
            if(block.getTiesManager().isTopLevel(block))
            {
                /*
                * Here user must have specified that can't load, thus will require user
                * to have passed a blank initialData:
                */
                if(block.isAllowed(OperationEnum.EXECUTE_QUERY) == false)
                {
                    if(!anyData.isEmpty())
                    {
                        Err.error(
                            "If have specified can't load, then initialData "
                                + "must be empty - " + block);
                    }
                }
                block.setDataRecords(anyData);
            }
            else
            {
                block.getMasterTies().setDependentExtents(anyData);
            }
        }
        else
        {
            if(!lookupDoneByAggregate())
            {
                String txt = "Unnecessary call to setInitialData for " + this;
                /*
               Session.getErrorThrower().throwApplicationError( txt,
                   ApplicationErrorEnum.INTERNAL)
                */
                //Above - not sure why needed an ErrorThrower, and dep peoblem, so
                //now do like rest of code in here:
                Err.error(txt);
            }
            extent = anyData;
        }
//    if(entityManagerProvider != null)
//    {
//      anyData.setEntityManagerProvider( entityManagerProvider);
//    }
    }

    /**
     * If a lookup has been done by having the lookedup Cell contain
     * a ArrayList, then we need to call setInitialData in order to do the
     * lookup at runtime. Here we catch any unnecessary calls to setInitialData.
     * (Not talking about isLookedUpByNone)
     */
    private boolean lookupDoneByAggregate()
    {
        return tie.getType() == Tie.PARENT_LIST;
    }

    /**
     * Creatable uses this method ...
     * If set was done for the isLookedupByNone then initialData will be with
     * the block, either directly if block is top-level, or indirectly
     * thru Ties if done for reference reasons. IF DONE FOR REF REASONS
     * THEN POSSIBLY WON'T BE ABLE TO GET UNTIL A SETLIST HAS BEEN PERFORMED,
     * BY WHICH TIME LIST WILL BE MUCH SMALLER. This is intended behaviour.
     * For now not allowing. Could set the actual Block extent with the hugeList
     * on call to ReferenceExtent constructor. Would overwrite when first setList
     * called, but would mean that this method would work.
     */
    public VisibleExtent getDataRecords()
    {
        VisibleExtent result = null;
        if(isLookedUpByNone)
        {
            // result = (IndependentExtent)block.getDataRecords();
            if(block.dataRecordsNull())
            {
                Err.pr( SdzNote.NODE_GROUP, "lazy load for " + block.getName());
                block.setDataRecords(new CombinationExtent());
            }
            result = block.getDataRecords();
        }
        else //is NOT the most shallow yellow cell in Designer ie. it branches off other yellow cells
        {
            result = extent;
            // Err.error("Haven't coded extent yet");
            /*
            if(extent == null)
            {
            Err.error("Haven't set anything yet");
            }
            result = extent;
            */
        }
        return result;
    }

    public void setDefaultElement(Object defaultElement)
    {
        if(this.defaultElement != null)
        {
            Err.error("defaultElement has not expired: " + this.defaultElement);
        }
        else
        {
            Err.pr( SdzNote.DEFAULTING, "defaultElement being set to " + defaultElement);
            Err.pr( SdzNote.DEFAULTING, "");
        }
        this.defaultElement = defaultElement;
        for(Iterator e = adapters.allIterator(); e.hasNext();)
        {
            ItemAdapter ad = (ItemAdapter) e.next();
            Object fieldValue = ad.getDoAdapter().getFieldValue(defaultElement);
            if(fieldValue != null)
            {
                ad.setDefaultValue(fieldValue);
                Err.pr( SdzNote.DEFAULTING, "From DO adapter: " + ad.getDoAdapter());
                Err.pr( SdzNote.DEFAULTING, "To item adapter: " + ad);
                Err.pr( SdzNote.DEFAULTING, "Have applied value: " + fieldValue);
                Err.pr( SdzNote.DEFAULTING, "Of type: " + fieldValue.getClass().getName());
                Err.pr( SdzNote.DEFAULTING, "");
            }
            else
            {
                Err.pr( SdzNote.DEFAULTING, "NULL fieldValue, From DO adapter: " + ad.getDoAdapter());
                Err.pr( SdzNote.DEFAULTING, "NULL fieldValue, To item adapter: " + ad);
                Err.pr( SdzNote.DEFAULTING, "");
            }
        }
    }

    abstract public void blankoutDisplay(OperationEnum currentOperation, String reason, int row, boolean removeComboItems);

    public void detachData( boolean detach)
    {
        adapters.detachData( detach);
    }
    
    abstract public void displayLovObjects();

    abstract public boolean displayIsBlank();

    /**
     * This is only called when at first element and in AnyAddingState.
     * (Which will mean there is only one element). If the answer is true
     * then will go into a frozen state, from which user can add or load.
     */
    // abstract public boolean UIIsBlankOrDefault();
    abstract public void setDisplay(
        OperationEnum currentOperation, int idx, Object element, boolean changedValuesOnly, boolean set, String reason);

    /*
    * Not needed by Table. For looking at what is currently displayed
    * at the current row of a table use TempCursorExtent extent in
    * core.info.prod.NodeTableModelImpl. See TableObj.UIIsBlank
    */
    // abstract public void getUI( Object element);
    abstract public boolean haveFieldsChanged();

    abstract public void getDisplay(
        Object element, boolean adding);

    abstract public void setDisplayEnabled(boolean editable);

    abstract public void backgroundAdd(boolean isPrior);

    abstract public void completeBackgroundAdd( OperationEnum currentOperation, int idx);

    abstract public List getVisualAdaptersArrayList();

    abstract public List getAdaptersArrayList();
    
    public Clazz getInstantiable()
    {
        return adapters.getInstantiable();
    }

    public AdaptersListI getAdapters()
    {
        return adapters;
    }

    public String toString()
    {
        if(adapters == null)
        {
            Err.error("A SubRecordObj should have attributes");
        }

        Class c = adapters.getInstantiable().getClassObject();
        if(c == null)
        {
            Err.error("A SubRecordObj should have an instantiable");
        }
        //return c.getName();
        return adapters.toString();
    }

    /**
     * Called recursively on SubRecordObj.children, so will be called for Aeroplane then
     * AeroplaneType. Note that tie is kept at the rightermost side, and that
     * tie's concept of parent and child will be other way round. First call will
     * simply return the value it passes. (As tie is null) | An order preference
     * Second call will need the SubRecordObj's tie        | is assumed here! BAD
     * and the value of the leftermost object to find the value of the element
     * represented by the parent side of the tie (rightermost element).
     * For a reference this is easy as the reference inside aeroplane already
     * is the Aeroplane type we want. Call to tie.getFieldValue will produce the
     * result.
     * For a parent list (use A(eroplane) >- H(anger)), job is to find the
     * H for an A. For each of the huge list of H, look at the A. When passed in
     * element == the A are iterating on, then have found the H result.
     * If have not found an assoc H will need to construct one and attach it
     * properly. To attach properly first attach it to the hugeList that keep.
     * Will also need to construct it's 'pointing ArrayList', to which will add
     * the Hanger-less aeroplane. When relationship was reference would return
     * a null (but attached) Hanger. Here returned Hanger will be new but not
     * null. It will contain a ArrayList which contains one element, the previously
     * Hanger-less aeroplane.
     * User can always get rid of this null Hanger if so required.
     * <p/>
     * eg. Aeroplane/AeroplaneType - this method will take the child (<=>Aeroplane
     * ) element, and turn it into that part that the lookup (AeroplaneType) to get
     * or set UI or whatever.
     */
    Object referenceAcross(Object element,
                         boolean changedValuesOnly, OperationEnum operation)
    {
        /*
        timesLook++;
        Err.pr( "To call getLookupAcross() times " + timesLook);
        if(timesLook == 120)
        {
            Err.debug();
        }
        */
        Object result = getLookupAcross(element, changedValuesOnly, operation);
        /*
        if(element != result)
        {
            if(result != null)
            {
                Err.pr( "getLookupAcross() starts with " + element.getClass().getName() + ", value: " + element);
                Err.pr( "ID: " + ((IdentifierI)element).getId());
                Err.pr( " and gives back " + result.getClass().getName() + ", value: " + result);
                Err.pr( " -----ID: " + ((IdentifierI)result).getId());
            }
            else
            {
                Err.pr( "getLookupAcross() starts with " + element.getClass().getName() + ", value: " + element);
                Err.pr( "ID: " + ((IdentifierI)element).getId());
                Err.pr( " and gives back null");
                Err.pr( " -----");
            }
        }
        */
        return result;
    }

    private static int luTimes;

    private Object getLookupAcross(Object element,
                                   boolean changedValuesOnly,
                                   OperationEnum operation)
    {
        /*
        Err.pr("In getLookupAcross for <" + getInstantiable() + ">");
        Err.pr("From element <" + element + "> of type <" +
            element.getClass().getName() + ">");
        if (getInstantiable().getClassObject() == CriteriaI.class)
        {
            times++;
            Err.pr("Doing CriteriaI, times " + times);
            if (times == 5)
            {
                Err.debug();
            }
        }
        */
        /*
         * If a tie exists, s/set the lefters reference.
         * If a Tie exists, then what has been passed is from parent
         */
        Object newElement = null;
        if(tie != null)
        {
            Object ob, obj;
            /*
             * Using IndependentExtent will check that list is of a type we know
             * about and from there we can use a general API.
             */
            IndependentExtent pointingExtent;
            Object list;
            if(tie.getType() == Tie.PARENT_LIST)
            {
                int timesFound = 0;
                if(extent == null)
                {
                    Err.error("Must call setInitialData for " + tie);
                }
                /*
                * Recurse thru the huge list of all the Hangers
                */
                for(Iterator e = extent.iterator(); e.hasNext();)
                {
                    ob = e.next(); // should give us a Hanger
                    Err.pr( "obj inside is: " + ob);
                    Err.pr( "field will use to get ArrayList is: " + tie.getFieldValue( ob));
                    list = tie.getFieldValue(ob);
                    // new MessageDlg("is ArrayList: " + list);
                    pointingExtent = IndependentExtent.createIndependent(list, creatable.getEntityManagerProvider(), creatable.getInsteadOfAddRemoveTrigger());
                    /*
                    * For each Hanger go thru the list that points back to the
                    * Aeroplane
                    */
                    for(Iterator en = pointingExtent.iterator(); en.hasNext();)
                    {
                        obj = en.next();
                        if(obj == element)
                        {
                            timesFound++;
                            if(timesFound != 1)
                            {
                                Err.error("Same Aeroplane cannot exist in two Hangars");
                            }
                            newElement = ob;
                        }
                    }
                }
                if(timesFound == 0)
                {
                    // Err.pr( "We have not found a lookup so must create one - " + clazz);
                    newElement = ObjectFoundryUtils.factoryClazz( getInstantiable(), "Solution is to call setLOV()");
                    extent.add(newElement);
                    list = tie.getFieldValue(newElement);
                    if(list == null) // eg Hanger constructor doesn't construct its own lists
                    {
                        tie.setFieldValue(newElement,
                            ObjectFoundryUtils.factory(tie.getFieldType()));
                        list = tie.getFieldValue(newElement);
                    }
                    if(list == null)
                    {
                        Err.error("Cannot get a list out of " + tie.getFieldType());
                    }
                    /*
                    * Have now created a Hanger and made it an element of the list of
                    * hangers. Have also made sure the individual hanger has a list of
                    * aeroplanes (brand new list). The following will now add the
                    * aeroplane element as the first element in the recently
                    * created hanger's list of aeroplanes.
                    */
                    pointingExtent = IndependentExtent.createIndependent(list, creatable.getEntityManagerProvider(),
                        creatable.getInsteadOfAddRemoveTrigger());
                    if(!pointingExtent.isEmpty())
                    {
                        Err.error("Really expected to create new list");
                    }
                    pointingExtent.add(element);
                }
                else
                {// Err.pr( "We have found a lookup object - " + newElement);
                }
            }
            else if(tie.getType() == Tie.CHILD_REFERENCE)
            {
                /*
                * Need to pass out the reference that exists in the element that
                * have been passed. Field of the reference is held in child's Tie
                */
                if(element != null)
                {
                    try
                    {
                        newElement = tie.getFieldValue(element);
                    }
                    // caught already!
                    catch(IllegalArgumentException ex)
                    {
                        Err.error("<" + tie + ">" + "<" + element + ">");
                    }
                }
                else
                {
                    Err.error(
                        "referenceDown really requires new newElement - and setField with it");
                }
                /*
                * Below was theory, which did not need to implement, as already implemented
                * as part of setDisplay()
                *
                * Here need to do a set or an add or nothing.
                *
                * Nothing
                * -------
                * If a Lookup has not been found then it should not be created. Example is
                * a Volunteer Cell that has a lookup based on the field belongsToGroup, which
                * is of type Volunteer. Volunteers that do not belong to a group do not need
                * to automatically have a 'group Volunteer' created for them.
                * Set
                * ---
                * When user has previously picked a value from a LOVs. Here we need to be able
                * to get at the object, not the String representation that might have been
                * picked from a JComboBox. (Will obviously want to support all sorts of ways
                * of doing LOVs). A straightforward example of an Set is when user has
                * pressed 'New' to create a new promotion, and there is an item which is a
                * JComboBox for PromotionType. The user has selected a PromotionType. Said
                * PromotionType is waiting here for us to pick it up.
                * Add
                * ---
                * SHOULD only rarely be necessary, so error. Would need to extend out to
                * another lookup past PromotionType in order to get any interveaning. Will
                * only be able to error if can know if this FieldObj is a leaf or an
                * interveaning. When don't know will have to do Nothing.
                *
                * If it was backgroundAdd() that caused the call to getDisplay(), and nothing
                * has been Set, then do this. Will be all the interveaning objects back to
                * the base 'looking up' object. In the future it may be possible to provide a
                * Set for these as well.
                */
                if(operation == null || operation == OperationEnum.EXECUTE_QUERY) // no oper, means no setDisplay
                {
                    /*
                    luTimes++;
                    Err.pr( "Might do lookup times " + luTimes + " in " +
                        getInstantiable().getClassObject().getName());
                    if(luTimes == 20)
                    {
                        Err.debug();
                    }
                    */
                    if(newElement == null)
                    {
                        // Err.pr( "Nothing, Set, Add for a " + clazz.getName() +
                        // " where backgroundAdd() " + adding);
                        doNeedToFill();
                        /*
                        * backgroundAdd() for lookups should assign a null. Example is
                        * where Volunteer has a parent Volunteer. When backgroundAdd()
                        * occurs, if user hasn't set a value then leave it at null.
                        */
                        /*
                        * If share lov type objects then if change the value eg to
                        * "Thursday" of one, then all the others will change! Thus
                        * either keep this commented-out, or get a bit more complex
                        * when user selects. Will have to place a different object
                        * in, ie. different WhichShift in a RosterSlot. Currently
                        * change the String in WhichShift. (Difference is mutability,
                        * to share lov objects they need to be immutable by ml).
                        *
                        * RUBBISH:
                        * Complex to achieve but possible! When Adapter.setFieldValue()
                        * is called, do following:
                        * If the cell has lovs, and this is the only attribute in the
                        * cell, then instead of assigning "weekly" to an Interval,
                        * change the actual Interval in the RosterSlot. Can achieve this
                        * as FieldObjs have both parent and children - so must be able
                        * to get to the RosterSlot somehow.
                        */
                        if(adapters.getLovObjects() != null)
                        {
                            newElement = findLovObject(getValuesSet(changedValuesOnly));
                            if(newElement != null)
                            {
                                // Err.pr( "SET TO TRUE");
                                doNotNeedToFill();
                            }
                        }
                        if(SdzNote.NULL_WORKER_ACROSS_WIRE.isVisible())
                        {
                            if(getInstantiable().getClassObject().toString().contains( "Worker"))
                            {
                                timesWorker++;
                                Err.pr( "To process worker <" + newElement + "> times " + timesWorker);
                                if(timesWorker == 1)
                                {
                                    Err.debug();
                                }
                            }
                        }
                        if(newElement == null)
                        {
                            newElement = ObjectFoundryUtils.factoryClazz( getInstantiable(),
                                "Solution is to call setLOV(), cell is " + creatable.getCell() +
                                    ", or to make sure are using a special dummy DO - as Sdz tries " +
                                    "to create where it finds nulls");
                        }
                        /*
                        * Here the newElement will be a WhichShift. element will be
                        * the RosterSlot. On next recursion into here element will be
                        * a WhichShift and newElement will be a String, with the value
                        * of say "dinner". Thus WhichShift will have been constructed
                        * on the previous recurse by the time we are able to see if
                        * "dinner" is in the LOV that have. Not impossible, but not
                        * ultra important. Works already with XML, but get extra
                        * WhichShifts for Mario. Same with JDO. May need applic id
                        * with JDO if want to limit the number of WhichShifts that
                        * appear in the DB. ie. if do not choose to implement this
                        * solution.
                        * (Explanation of code started above).
                        */
                        /*
                        Err.pr( "$1$ Using tie " + tie);
                        Err.pr( "$1$ On a " + element.getClass().getName());
                        Err.pr( "$1$ Will set a brand new " + newElement.getClass().getName());
                        */
                        tie.setFieldValue(element, newElement);
                    }
                    else
                    {
                        boolean study = false;
                        /*
                        if(newElement.getClass() == WhichShift.class)
                        {
                        study = true;
                        Err.pr( "WhichShift newElement got is <" + newElement + ">, id: " +
                        ((WhichShift)newElement).id);
                        }
                        */
                        Object b4Lov = newElement;
                        if(adapters.getLovObjects() != null)
                        {
                            if(operation == null)
                            {
                                newElement = findLovObject(getValuesSet(changedValuesOnly));
                            }
                            else if(operation == OperationEnum.EXECUTE_QUERY)
                            {
                                applyLovSubstitution(element, newElement);
                            }
                            if(newElement != null)
                            {
                                // Err.pr( "SET TO TRUE 2nd, for " + this);
                                doNotNeedToFill();
                            }
                        }
                        else
                        {/*
               * Do not have a lov, and value has been left at null.
               * This is the Volunteer lookup example.
               */}
                        if(study)
                        {
                            if(newElement != null)
                            {/*
                 Err.pr( "Now got is <" + newElement + ">, id: " +
                 ((WhichShift)newElement).id);
                 */}
                            else
                            {
                                /*
                                if(setDisplay)
                                {
                                newElement = b4Lov;
                                Err.pr( "Have NOT overwritten: " + b4Lov + " to null");
                                }
                                else
                                */
                                {
                                    Err.pr(
                                        "Have overwritten: " + b4Lov + ", (" + b4Lov.getClass()
                                            + ") to null");
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                Err.error("Only CHILD_REFERENCE and PARENT_LIST ties supported");
            }
        }
        else
        {
            newElement = element;
        }
        /*
        if(newElement == null)
        {
        Err.error( "In getLookupAcross(), to return null for <" + clazz.getName() + ">");
        }
        */
        return newElement;
    }

    private List getValuesSet(boolean changedValuesOnly)
    {
        /*
        times1++;
        Err.pr( "$$ In getValuesSet() times " + times1);
        if(times1 == 1)
        {
        Err.debug();
        }
        */
        List result = new ArrayList();
        List items = null;
        if(changedValuesOnly)
        {
            items = uiItemsChanged;
        }
        else
        {
            items = uiItemsAll;
        }
        /*
        if(items.get( 0) == null)
        {
        Err.stack();
        }
        */
        for(Iterator e = items.iterator(); e.hasNext();)
        {
            ItemSnapshot uif = (ItemSnapshot) e.next();
            result.add(uif.getAfterImage());
        }
        // Err.pr( "$$ getValuesSet(), changed are " + result);
        return result;
    }

    private List getLovValuesSet(Object lovObj)
    {
        List result = new ArrayList();
        for(Iterator e = adapters.allIterator(); e.hasNext();)
        {
            ItemAdapter ad = (ItemAdapter) e.next();
            Object value = ad.getDoAdapter().getFieldValue(lovObj);
            result.add(value);
        }
        return result;
    }

    private static boolean isDummy( Object lovObj)
    {
        boolean result = false;
        if(lovObj instanceof NullVerifiable)
        {
            NullVerifiable verifiable = (NullVerifiable)lovObj;
            result = verifiable.isDummy();
        }
        return result;
    }

    /**
     * @param userEntered The total of all the values a user has entered for
     *                    a particular cell. Often a lookup cell is for only
     *                    one attribute, and therefore there will only be one
     *                    value.
     * @return Object
     */
    public Object findLovObject(List userEntered)
    {
        Object result = null;
        /*
        if(userEntered.isEmpty())
        {
        Err.pr( "findLovObject(), userEntered arg is empty");
        }
        */
        if(SdzNote.INSTANTIATING_LOOKUP.isVisible())
        {
            //Err.pr( "");
            //Print.prList( userEntered, "!! userEntered");
        }
        for(Iterator e = adapters.getLovObjects().iterator(); e.hasNext();)
        {
            Object lovObj = e.next();
            if(lovObj == null)
            {
                Err.error("S/not have a lovObj that is null");
            }

            Err.pr( SdzNote.INSTANTIATING_LOOKUP, "!! lovObj: " + lovObj);
            List lovList = getLovValuesSet(lovObj);
            if(SdzNote.INSTANTIATING_LOOKUP.isVisible())
            {
                Print.prList( lovList, "!! lovList", false);
                //Err.stack();
            }
            if(userEntered.isEmpty())
            {
                /*
                * When user entered nothing, and if didn't do this, this
                * method would end up returning null, which would mean
                * that a brand new say WhichShift would be created. To
                * avoid this we observe the convention of returning the
                * first value, which by convention will be the NULL value.
                * TODO
                *
                */
                boolean isNULL = (lovObj.toString() != null && 
                    (lovObj.toString().indexOf("NULL") != -1 ||
                        //lovObj.toString().indexOf("N/A") != -1
                        isDummy( lovObj)
                    ));
                if(lovObj.toString() != null && !lovObj.toString().equals("") && !isNULL)
                {
                    Err.error(
                        "By convention, first LOV should be a special NULL (or isDummy()) Object, got <"
                            + lovObj.toString() + "> for a " + lovObj.getClass().getName());
                }
                else
                {
                    if(lovObj.toString() == null)
                    {/*
             times++;
             Err.pr( "--LOV to FIRST 1 for " + adapters + " times " + times);
             String txt = "[shiftPreference Name<class util.DebugComboBox>,]";
             if(true
             //adapters.toString().equals( txt)
             )						{
             Err.stack();
             }
             */}
                    else if(lovObj.toString().equals(""))
                    {/*
             times++;
             Err.pr( "--LOV to FIRST 2 times " + times);
             */}
                    else if(isNULL)
                    {
                    }
                    else
                    {
                        Err.error();
                    }
                }
                result = lovObj;
                break;
            }
            else if(userEntered.equals(lovList))
            {
                result = lovObj;
                // times++;
                // Err.pr( "--LOV to LOV LIST VALUE times " + times);
                break;
            }
        }
        /*
        String userEnteredStr = userEntered + ", " + userEntered.getClass().getName();
        String resultStr = null;
        if (result != null)
        {
          resultStr = result + ", " + result.getClass().getName();
        }
        times++;
        Err.pr( "Based on <" + userEnteredStr + ">, have LOV found <" + resultStr + "> times " + times );
        if (times == 4)
        {
          Err.debug();
        }
        */
        return result;
    }

    /**
     * Done as part of query. Here we substitute into the actual
     * data, after this call - actually, other way round - get from
     * the data and put into the lov
     * <p/>
     * element param would be RosterSlot, newElement param would be WhichShift
     * <p/>
     * One of the consequences of doing this is that we can then
     * do the more performant == rather than equals
     * TODO Check whether this is the case, do some unit tests
     */
    public void applyLovSubstitution(Object element, Object newElement)
    {
        if(element == null)
        {
            Err.error("applyLovSubstitution, element param s/not be null");
        }
        if(newElement == null)
        {
            Err.error("applyLovSubstitution, newElement param s/not be null");
        }
        // Err.pr( "element: " + element.getClass());
        // Err.pr( "newElement: " + newElement.getClass());
        List lovs = adapters.getLovObjects();
        // ItemAdapter itemAdapter = parent.getAdapters().getAdapter( newElement.getClass());
        boolean found = false;
        if(lovs != null)
        {
            int idx = lovs.indexOf(newElement);
            if(idx != -1)
            {
                // result = lovs.get( idx);
                // itemAdapter.setFieldValue( element, result);
                /* Not essential and we becoming indep of JDO 10/05/05 */
                if(!DOHelperUtils.equalsOrError(lovs.get(idx), newElement))
                {
                    Print.prList(lovs, "SubRecordObj.applyLovSubstitution()-1");
                    Err.error("Doing LOV substitution, but the values not DB equal: " + newElement);
                }
                /**/
                Err.pr( SdzNote.LOV_SUBSTITUTION, "About to try to substitute in " + newElement + ", type: " + newElement.getClass().getName());
                Err.pr( SdzNote.LOV_SUBSTITUTION, "List substituting into is of type " + lovs.getClass().getName());
                boolean ok = true;
                try
                {
                    lovs.set(idx, newElement);
                }
                catch(UnsupportedOperationException ex)
                {
                    Print.prList( lovs, "LOVs");
                    Err.pr( "Not substituting <" + newElement + "> as " + ex.getMessage());
                    ok = false;
                }
                if(ok)
                {
                    Err.pr( SdzNote.LOV_SUBSTITUTION, "Have substituted in " + newElement);
                }
                found = true;
            }
        }
        else
        {
            Err.error("----------- No LOVs for " + this);
        }
        if(!found)
        {
            Print.prList(lovs, "SubRecordObj.applyLovSubstitution()-2");
            //Err.pr("Dummy-style null Worker (s/be same as NULL) looks like: " + Worker.NULL);
            Err.error(
                "Expect to find a LOV substitute for a <" + newElement + "> of type "
                    + newElement.getClass().getName());
        }
    }
    
    public SubRecordObj getParent()
    {
        return parent;
    }
    
    abstract public Object getDerivedAt( int idx, Object element);    
    abstract public void setDerivedAt( int idx, Object element);    

//  public void setEntityManagerProvider( EntityManagerProviderI em)
//  {
//    entityManagerProvider = em;
//    if(extent != null)
//    {
//      extent.setEntityManagerProvider( em);
//    }
//  }

} // end class
