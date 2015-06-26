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
package org.strandz.core.interf;

import org.strandz.core.domain.AbstractCell;
import org.strandz.core.domain.ControlSignatures;
import org.strandz.core.domain.DOAdapter;
import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.CalculationPlace;
import org.strandz.core.domain.CalculatedResultI;
import org.strandz.core.domain.FieldItemAdapter;
import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.core.prod.LookupTiesManager;
import org.strandz.core.prod.view.AdaptersListI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.Utils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Crucial class, however not one that needs to be widely understood. Will put it
 * in a non-public package later.
 * <p/>
 * All the adapters for a cell are gathered together here. Of course from
 * a node you can get the cells.
 * <p/>
 * This class used because later on it will come to have intelligence.
 * addAttribute() will stay, but will manually replicate what will
 * go on in a Customiser that does the same thing. One possible UI is that
 * the attributes of a class are gathered on interface, and the UI then
 * does the matching to the component. All interaction is directly with
 * this class. This class should manage the mapping between elements and
 * components.
 * <p/>
 * o setClass() - used to getAttributes()
 * <p/>
 * o getAttributes() - signatures will be ODMG defined. Until then my
 * interpretation is public vars and properties (thus need Introspection
 * class). Actually 'ODMG defined' is ...look up... any var. Our definition
 * must be different, must be 'publically accessible'. Will use public vars
 * and properties. Wider defn than Enterprise Java Bean, which is just
 * properties.
 * <p/>
 * Model/View Note. Could possibly have used Observer/Observable pattern between
 * this class and block. For view to have got the state would have been ...
 *
 * @author Chris Murphy
 */
public class AdaptersList implements AdaptersListI
{
    private List<AbstractTableItemAdapter> tableAdapters = new ArrayList<AbstractTableItemAdapter>();
    private List<ItemAdapter> allAdapters = new ArrayList<ItemAdapter>();
    private List<DOAdapter> allDOAdapters = new ArrayList<DOAdapter>();
    // Object templateElement = null;
    private Clazz classType = null;
    private Clazz instantiable = null;
    private PropertyDescriptor propertyDescriptors[] = null;
    private PropertyDescriptor propertyDescriptorsToConstruct[] = null;
    private PropertyDescriptor propertyDescriptorsToSecondarilyConstruct[] = null;
    private boolean fieldsNotSet = true;
    private boolean setElementCalled = false;
    private List lovObjects;
    private List detachedLovObjects;
    private int currentOrdinal;
    private String creationReason;
    private transient boolean dataDetached;
    private transient CalculationPlace calculationPlace;

    private static int times;
    private static int timesConstructed;
    private int id;

    public AdaptersList( CalculationPlace calculationPlace, String creationReason)
    {
        id = ++timesConstructed;
        this.creationReason = creationReason;
        Err.pr( SdzNote.NO_CALCS, "created AdaptersList ID: " + id);
        if(id == 0)
        {
            Err.stack();
        }
        if(calculationPlace != null)
        {
            this.calculationPlace = calculationPlace;
        }
        else
        {
            this.calculationPlace = new CalculationPlace();
        }
    }

    /**
     * Note that textComponent may be a field within any component - ie doesn't
     * have to be related to other fields in the same Node, or may be a field
     * within a JTable.
     */
    public void addAttribute(Attribute attribute, LookupTiesManager lTiesManager)
    {
        if(SdzNote.QUERY_NOT_WORKING_NON_VISUAL.isVisible() &&
            attribute.getName() != null && attribute.getName().equals( "christianName") &&
            Utils.instanceOf( attribute, RuntimeAttribute.class))
        {
            times++;
            if(times == 3)
            {
                Err.debug();
            }
            Err.pr( "In addAttribute for " + attribute.getName() + ", enabled is " 
                + ((RuntimeAttribute)attribute).isEnabled() + " times " + times);
        }
        SdzNote.NO_CALCS.incTimes();
        Err.pr( SdzNote.NO_CALCS, "addAttribute being done " + SdzNote.NO_CALCS.getTimes() +
            " times for <" + attribute.getName() + "> in AdaptersList ID: " + id);
        if(SdzNote.NO_CALCS.getTimes() == 10)
        {
            Err.debug();
        }
        ItemAdapter currentAd = null;
        DOAdapter doAdapter = null;
        //Object lastItem = null;
        boolean doAdd = true;
        if(attribute instanceof FieldAttribute)
        {
            //lastItem = ((FieldAttribute) attribute).getItem();
            doAdapter = new DOAdapter(attribute.dataFieldName,
                attribute.getCell().getNode().getRecorderI(), lTiesManager,
                attribute.getCell(), attribute.getPropertyValue());
            currentAd = obtainAdapter( (RuntimeAttribute)attribute, doAdapter, Utils.UNSET_INT);
            /*
            if(((RuntimeAttribute)attribute).getItemAdapter() == null)
            {
                currentAd = ((RuntimeAttribute) attribute).createItemAdapter( doAdapter, calculationPlace);
                calculationPlace.addCalc( ((RuntimeAttribute) attribute).getCalculatedResultI());
            }
            else
            {
                //Preserving old item adapter will preserve the b4image values
                currentAd = ((RuntimeAttribute)attribute).getItemAdapter();
                calculationPlace.addCalc( ((RuntimeAttribute) attribute).getCalculatedResultI());
            }
            */
            doAdapter.setItemAdapter(currentAd);
        }
        else if(attribute instanceof TableAttribute)
        {
            //lastItem = attribute.getDOField();

            int column = attribute.getOrdinal().intValue();
            if(column == -1)
            {
                column = attribute.getCell().getNode().getCurrentOrdinal();
                //Err.pr( "to use defaultly assigned ordinal of " + column +
                // " for ID " + id + " from " + attribute.getCell().getNode());
                attribute.getCell().getNode().incCurrentOrdinal();
            }
            else
            {
                //Err.pr( "attribute's ordinal is " + column);
            }
            Assert.notNull( attribute.dataFieldName, "TableAttribute has no dataFieldName: <" + 
                    attribute + ">");
            doAdapter = new DOAdapter(attribute.dataFieldName,
                attribute.getCell().getNode().getRecorderI(), lTiesManager,
                attribute.getCell(), attribute.getPropertyValue());
            currentAd = obtainAdapter( (TableAttribute)attribute, doAdapter, column);
            /*
            if(((RuntimeAttribute)attribute).getItemAdapter() == null)
            {
                currentAd = ((TableAttribute) attribute).createItemAdapter( doAdapter, column, calculationPlace);
                calculationPlace.addCalc( ((RuntimeAttribute) attribute).getCalculatedResultI());
            }
            else
            {
                //Preserving old item adapter will preserve the b4image values
                currentAd = ((RuntimeAttribute)attribute).getItemAdapter();
            }
            */
            doAdapter.setItemAdapter(currentAd);
        }
        else if(attribute instanceof NonVisualTableAttribute)
        {
            //lastItem = attribute.getDOField();

            //BELOW Wrong - there could be a few non-visuals, and the ordinal is used to
            //index into the RowBuffer. Hmm - s/not be the case - column s/index into
            //the table or RowBuffer, while ordinal s/be used for ordering from left to
            //right
            //
            //ordinal irrelevant for a non-applichousing, so moved this property down the hierarchy
            //int column = ((NonVisualTableAttribute)attribute).getOrdinal().intValue();
            //int column = -99;
            int column = getCurrentNVOrdinal();
            incCurrentNVOrdinal();
            doAdapter = new DOAdapter(attribute.dataFieldName,
                attribute.getCell().getNode().getRecorderI(), lTiesManager,
                attribute.getCell(), attribute.getPropertyValue());
            currentAd = obtainAdapter( (NonVisualTableAttribute)attribute, doAdapter, column);
            /*
            if(((NonVisualTableAttribute)attribute).getItemAdapter() == null)
            {
                currentAd = ((NonVisualTableAttribute) attribute).createItemAdapter( doAdapter, column, calculationPlace);
                calculationPlace.addCalc( ((NonVisualTableAttribute) attribute).getCalculatedResultI());
            }
            else
            {
                //Preserving old item adapter will preserve the b4image values
                currentAd = ((NonVisualTableAttribute)attribute).getItemAdapter();
            }
            */
            doAdapter.setItemAdapter(currentAd);
            currentAd.setVisual(false);
        }
        else if(attribute instanceof RuntimeAttribute)
        {
            /*
             * Catch-all for all the Non-Visual Attributes: ReferenceLookupAttribute and NonVisualFieldAttribute
             * Note have to cast to RuntimeAttribute 
             */
            //lastItem = ((RuntimeAttribute) attribute).getItem();
            doAdapter = new DOAdapter(attribute.dataFieldName,
                attribute.getCell().getNode().getRecorderI(), lTiesManager,
                attribute.getCell(), attribute.getPropertyValue());
            if(SdzNote.QUERY_NOT_WORKING_NON_VISUAL.isVisible()
                && attribute.getName() != null && attribute.getName().equals( "christianName")
                && Utils.instanceOf( attribute, RuntimeAttribute.class)
                )
            {
                times++;
                Err.pr( "In addAttribute for " + attribute.getName() + ", enabled is " + ((RuntimeAttribute) attribute).isEnabled() + " times " + times);
                if(times == 0)
                {
                    Err.debug();
                }
            }
            /*
             * Note doing this type checking even thou actually RuntimeAttribute.createItemAdapter()
             * will be the method that is used
             */
            if(attribute instanceof ReferenceLookupAttribute)
            {
                currentAd = obtainAdapter( (ReferenceLookupAttribute)attribute, doAdapter, Utils.UNSET_INT);
                /*
                if(((ReferenceLookupAttribute)attribute).getItemAdapter() == null)
                {
                    currentAd = ((ReferenceLookupAttribute) attribute).createItemAdapter( doAdapter, calculationPlace);
                    calculationPlace.addCalc( ((ReferenceLookupAttribute) attribute).getCalculatedResultI());
                }
                else
                {
                    //Preserving old item adapter will preserve the b4image values
                    currentAd = ((ReferenceLookupAttribute)attribute).getItemAdapter();
                }
                */
            }
            else if(attribute instanceof NonVisualFieldAttribute)
            {
                currentAd = obtainAdapter( (RuntimeAttribute)attribute, doAdapter, Utils.UNSET_INT);
                /*
                //currentAd = ((NonVisualFieldAttribute) attribute).createItemAdapter( doAdapter, calculationPlace);
                if(((NonVisualFieldAttribute)attribute).getItemAdapter() == null)
                {
                    currentAd = ((NonVisualFieldAttribute) attribute).createItemAdapter( doAdapter, calculationPlace);
                    calculationPlace.addCalc( ((NonVisualFieldAttribute) attribute).getCalculatedResultI());
                }
                else
                {
                    //Preserving old item adapter will preserve the b4image values
                    currentAd = ((NonVisualFieldAttribute)attribute).getItemAdapter();
                }
                */
            }
            else
            {
                Err.error( "Not considered " + attribute.getClass().getName());
            }
            doAdapter.setItemAdapter(currentAd);
            currentAd.setVisual(false);
        }
        else if(attribute instanceof StemAttribute)
        {
            doAdd = false;
        }
        else
        {
            Err.error(
                "Serious problem, attribute adding is a " + attribute.getClass());
        }
        if(doAdd)
        {
            RuntimeAttribute attr = ((RuntimeAttribute) attribute);
            if(currentAd == null)
            {
                attr.setDOAdapter(doAdapter);
            }
            else
            {
                attr.setItemAdapter(currentAd);
                attr.setDOAdapter(doAdapter);
                if(allAdapters.contains( currentAd))
                {
                    Err.error();
                }
                else
                {
                    allAdapters.add(currentAd);
                }
            }
            Assert.notNull( attr.getItemAdapter(), 
                            "No ItemAdapter created for a " + attribute.getClass().getName());
            if(allDOAdapters.contains( doAdapter))
            {
                Err.error();
            }
            else
            {
                allDOAdapters.add( doAdapter);
            }
            if(currentAd != null && attribute instanceof TableAttributeI)
            {
                AbstractTableItemAdapter tableAdapter = (AbstractTableItemAdapter)currentAd;
                if(tableAdapters.contains( tableAdapter))
                {
                    Err.error();
                }
                else
                {
                    tableAdapters.add( tableAdapter);
                    //Err.pr( "tableAdapters for <" + creationReason + "> now size: " + tableAdapters.size());
                }
            }
            if(currentAd != null)
            {
                Err.pr(SdzNote.INTERMITTENT_UNIT_TEST_FAILURE,
                    "Adapter " + currentAd.getName() + " is now in AdaptersList for "
                        + attribute.getCell().getNode());
            }
            /*
            * When a new element has been called then there is still at least
            * one field that has not been set.
            */
            fieldsNotSet = true;
        }
        else
        {
            if(currentAd != null)
            {
                Err.pr(SdzNote.INTERMITTENT_UNIT_TEST_FAILURE,
                    "Adapter " + currentAd.getName()
                        + " is NOT to be in AdaptersList for " + attribute.getCell());
            }
        }
    }
    
    private ItemAdapter obtainAdapter( RuntimeAttribute runtimeAttribute,
                                       DOAdapter doAdapter,
                                       int column)
    {
        ItemAdapter result = null;
        if(runtimeAttribute.getItemAdapter() == null)
        {
            if(column == Utils.UNSET_INT)
            {
                result = runtimeAttribute.createItemAdapter( doAdapter, calculationPlace);
            }
            else
            {
                if(runtimeAttribute instanceof TableAttribute)
                {
                    result = ((TableAttribute)runtimeAttribute).createItemAdapter( 
                            doAdapter, column, calculationPlace);
                }
                else if(runtimeAttribute instanceof NonVisualTableAttribute)
                {
                    result = ((NonVisualTableAttribute)runtimeAttribute).createItemAdapter( 
                            doAdapter, column, calculationPlace);
                }
            }
            if(runtimeAttribute.getCalculatedResultI() != null)
            {
                calculationPlace.addCalc( runtimeAttribute.getCalculatedResultI(), result);
            }
            else
            {
                Err.pr( SdzNote.NO_CALCS, "runtimeAttribute <" + runtimeAttribute.getName() +
                    "> does not have a CalculatedResultI");
            }
        }
        else
        {
            //Preserving old item adapter will preserve the b4image values
            result = runtimeAttribute.getItemAdapter();
        }
        if(runtimeAttribute instanceof TableAttribute && result instanceof FieldItemAdapter)
        {
            Err.error( "Created a " + result + " from " + runtimeAttribute);
        }
        return result;
    }

    public List getTableAdapters()
    {
        return tableAdapters;
    }

    public List getAllAdapters()
    {
        return allAdapters;
    }

    public Iterator tableIterator()
    {
        return tableAdapters.iterator();
    }

    public Iterator allIterator()
    {
        return allAdapters.iterator();
    }

    public int tableSize()
    {
        return tableAdapters.size();
    }

    public int allSize()
    {
        return allAdapters.size();
    }

    public String toString()
    {
        String result = "[";
        for(Iterator it = allAdapters.iterator(); it.hasNext();)
        {
            ItemAdapter adap = (ItemAdapter) it.next();
            result += adap + ",";
        }
        result += "]";
        return result;
    }

    public boolean tableIsEmpty()
    {
        return tableAdapters.isEmpty();
    }

    public boolean allIsEmpty()
    {
        return allAdapters.isEmpty();
    }

    /**
     * If already have the Adapters then classElement will allow
     * us to set the field for each Adapter.
     */
    public void setElement(Clazz classElement,
                           Clazz instantiable,
                           PropertyDescriptor propertyDescriptors[],
                           PropertyDescriptor propertyDescriptorsToConstruct[],
                           PropertyDescriptor propertyDescriptorsToSecondarilyConstruct[])
    {
        this.classType = classElement;
        this.instantiable = instantiable;
        SdzNote.INSTANTIATING_LOOKUP.incTimes();
        Err.pr( SdzNote.INSTANTIATING_LOOKUP, "instantiable in " + this + " set to " +
            instantiable + ", times " + SdzNote.INSTANTIATING_LOOKUP.getTimes());
        if(SdzNote.INSTANTIATING_LOOKUP.getTimes() == 0)
        {
            Err.stack();
        }
        if(!SelfReferenceUtils.isInstantiable(classElement.getClassObject().getName()))
        {
            /* Too strict for RO objects, but useful for debugging
            Err.error(
                "<" + classElement.getClassObject().getName() + "> cannot be instantiated");
            */
        }
        if(!SelfReferenceUtils.isALoadedClass(classElement.getClassObject().getName()))
        {
            Err.error(
                "<" + classElement.getClassObject().getName() + "> is not recognised as a loaded class");
        }
        this.propertyDescriptors = propertyDescriptors;
        this.propertyDescriptorsToConstruct = propertyDescriptorsToConstruct;
        this.propertyDescriptorsToSecondarilyConstruct = propertyDescriptorsToSecondarilyConstruct;
        setElementCalled = true;
    }

    public Clazz getClassType()
    {
        if(!setElementCalled)
        {
            Err.error("setElement() has not been called before getCell()");
        }
        return classType;
    }

    public Clazz getInstantiable()
    {
        if(!setElementCalled)
        {
            Err.error("setElement() has not been called before getCell()");
        }
        return instantiable;
    }

    private static PropertyDescriptor propertyFromColumnName(
        String colName, PropertyDescriptor pds[], AbstractCell cell)
    {
        PropertyDescriptor result = null;
        boolean found = false;
        if(pds != null)
        {
            for(int i = 0; i <= pds.length - 1; i++)
            {
                // Err.pr("looking at " + pds[i].getName());
                if(pds[i].getName().equals(colName))
                {
                    found = true;
                    result = pds[i];
                    break;
                }
            }
            if(!found)
            {
                Err.pr( "colName: " + colName);
                Print.prPropertyDescriptorArray( pds, "PropertyDescriptors: ");
                Err.pr( "cell: " + cell);
                /*
                 * Accessors may not exist due to the fact that BeanInfo does not obtain property descriptors
                 * recursively. In this case 'source code write' the method into the class
                 * cell.getClazz().getClassObject().getName() - for instance 'MetricI' - even if it already
                 * inherits that method
                 */
                Err.pr( "An incorrect Attribute.setDOField( <" + colName + ">) call could be to blame for this, or the accessors may not exist");
                Err.error(
                    "Have not found a property/field for <" + colName + ">" + " in <" + cell.getClazz().getClassObject().getName()
                        + "> for cell <" + cell + ">");
            }
        }
        return result;
    }

    // Will get rid of this method when figger out smart way of getting
    // the adpator
    public DOAdapter getDOAdapter()
    {
        ItemAdapter result = null;
        if(allAdapters.size() != 1)
        {// Showing up error in thinking!
            // Err.error( "Can only call getAdapter() when there is only one");
        }
        return (DOAdapter) allDOAdapters.get(0);
    }

     /**/
    public ItemAdapter getAdapter(Class clazz)
    {
        ItemAdapter result = null;
        for(Iterator e = allAdapters.iterator(); e.hasNext();)
        {
            ItemAdapter ad = (ItemAdapter) e.next();
            if(clazz.equals(ad.getDoAdapter().getClassType()))
            {
                result = ad;
                break;
            }
            else
            {
                Err.pr(SdzNote.RE_TRANSFER_DO_TO_CELL,
                    "Rejected " + ad.getDoAdapter().getClassType());
            }
        }
        if(result == null)
        {
            Print.prIterator(allAdapters.iterator());
            Err.error(SdzNote.INTERMITTENT_UNIT_TEST_FAILURE,
                "Could not find an itemAdapter for " + clazz.getName()
                    + " when looking in AdaptersList of " + instantiable);
        }
        return result;
    }
    
    public ItemAdapter getFirstVisualItemAdapter()
    {
        ItemAdapter result = null;
        if(!allAdapters.isEmpty())
        {
            //result = allAdapters.get( 0);
            for(Iterator<ItemAdapter> iterator = allAdapters.iterator(); iterator.hasNext();)
            {
                ItemAdapter adapter = iterator.next();
                if(adapter.isVisual())
                {
                    result = adapter;
                    break;
                }
            }
        }
        return result;
    }

    public DOAdapter getDOAdapter(String colName)
    {
        DOAdapter result = null;
        for(Iterator e = allDOAdapters.iterator(); e.hasNext();)
        {
            DOAdapter ad = (DOAdapter) e.next();
            if(colName.equals(ad.getDOFieldName()))
            {
                result = ad;
                break;
            }
        }
        if(result == null)
        {
            Err.error("Could not find an itemAdapter for " + colName);
        }
        return result;
    }

    public void setAdapters(Clazz clazz)
    {
        // Err.pr( "========== adapters being set for " + clazz);
        if(allAdapters.isEmpty())
        {// Err.error("LEFT IN CASE The following class does not have" +
            // " any associated attributes: " + clazz);
        }
        else
        {
            // Why doing again? Object templateElement = pSelfReference.factory( clazz);
            if(!setElementCalled)
            {
                Err.error(
                    "Programmer's Error - not yet had a chance to set templateElement");
            }

            DOAdapter ad;
            for(Iterator e = allDOAdapters.iterator(); e.hasNext();)
            {
                ad = (DOAdapter) e.next();
                CalculatedResultI calculator = ad.getItemAdapter().getCalculator();
                if(calculator != null)
                {
                    if(calculator.isDOSetter())
                    {
                        setupField( clazz, ad);
                        /*
                        ad.setField(clazz,
                            propertyFromColumnName(ad.getDOFieldName(),
                                propertyDescriptors, ad.getCell()),
                            propertyFromColumnName(ad.getDOFieldName(),
                                propertyDescriptorsToConstruct, ad.getCell()),
                            propertyFromColumnName(ad.getDOFieldName(),
                                propertyDescriptorsToSecondarilyConstruct, ad.getCell())
                        );
                        */
                    }
                }
                else
                {
                    setupField( clazz, ad);
                    /*
                    ad.setField(clazz,
                        propertyFromColumnName(ad.getDOFieldName(),
                            propertyDescriptors, ad.getCell()),
                        propertyFromColumnName(ad.getDOFieldName(),
                            propertyDescriptorsToConstruct, ad.getCell()),
                        propertyFromColumnName(ad.getDOFieldName(),
                            propertyDescriptorsToSecondarilyConstruct, ad.getCell())
                    );
                    */
                }
            }
        }
        fieldsNotSet = false;
    }

    private void setupField( Clazz clazz, DOAdapter ad)
    {
        if(ad.isEndUserRuntime())
        {
            /*
             * Signifies that this field will be held as a name/value pair so there is
             * not need to discover the actual fields/methods that might be used to
             * access the properties. This property ought to be accessible via the
             * SdzPropertySetableI interface.
             */
            //This done later, when have parentClazz
            //Assert.isTrue( ad.getParentClazz().getClassObject().isAssignableFrom( SdzPropertySetableI.class));
        }
        else
        {
            ad.setField(
                propertyFromColumnName(ad.getDOFieldName(),
                    propertyDescriptors, ad.getCell()),
                propertyFromColumnName(ad.getDOFieldName(),
                    propertyDescriptorsToConstruct, ad.getCell()),
                propertyFromColumnName(ad.getDOFieldName(),
                    propertyDescriptorsToSecondarilyConstruct, ad.getCell())
            );
        }
        ad.setParentClazz( clazz);
    }

    public List getLovObjects()
    {
        Err.pr( SdzNote.LOVS_CHANGE_DATA_SET, "lovObjects to return is " + lovObjects);
        return lovObjects;
    }

    public void setLovObjects(List lovObjects)
    {
        this.lovObjects = lovObjects;
        //Quick check as it is easy to accidently put a list in a list
        for(int i = 0; i < lovObjects.size(); i++)
        {
            Object obj = lovObjects.get(i);
            if(Utils.instanceOf(obj, Collection.class))
            {
                Err.error("Unusual that an object in a LOVs is of type Collection: " + obj.getClass());
            }
        }
        if(SdzNote.DISPLAY_LOV.isVisible())
        {
            Print.prList( lovObjects, "lovObjects been set on " + this.id +
                " with reason " + creationReason, false);
        }
        //Err.pr( SdzNote.lovsChangeDataSet, "lovObjects been set to " + lovObjects);
    }
    
    public void detachData( boolean detach)
    {
        if(detach)
        {
            //Assert.notNull( lovObjects);
            detachedLovObjects = lovObjects; 
            lovObjects = null;
            Err.pr( SdzNote.LOVS_CHANGE_DATA_SET, "lovObjects been set to -null- on " + this.id);
        }
        else
        {
            lovObjects = detachedLovObjects;
            detachedLovObjects = null;
        }
    }

    public void displayLovObjects()
    {
        if(lovObjects != null)
        {
            Err.pr( SdzNote.DISPLAY_LOV, "displayLovObjects() being called on " + this.id + " with reason " + creationReason);
            List<Object> values = null;
            for(Iterator iter = allAdapters.iterator(); iter.hasNext();)
            {
                ItemAdapter ad = (ItemAdapter) iter.next();
                IdEnum id = ad.createIdEnum( toString());
                if(ControlSignatures.isLOVable(id))
                {
                    Object origValue = ControlSignatures.getText( id);
                    boolean origValueIncludedInNewList = false; 
                    if(ad.getDoAdapter().getDOFieldName() != null)
                    {
                        values = new ArrayList<Object>();
                        // Iterator does not keep the order
                        // for(Iterator it = lovObjects.iterator(); it.hasNext();)
                        for(int i = 0; i < lovObjects.size(); i++)
                        {
                            Object obj = lovObjects.get(i);
                            //It is usually a list of a property called name that is displayed in a LOV
                            Object name = SelfReferenceUtils.invokeReadProperty(obj,
                                    ad.getDoAdapter().getDOFieldName());
                            if(!origValueIncludedInNewList && Utils.equals( name, origValue))
                            {
                                origValueIncludedInNewList = true;
                            }
                            values.add( name);
                        }
                    }
                    else
                    {
                        Err.error("Adapter " + ad + ", expected to have a ad.doFieldName");
                    }
                    if(ad.getMoveBlock() != null)
                    {
                        ad.getMoveBlock().getMoveTracker().closeTo(
                            EntrySiteEnum.ACTION_LISTENER);
                    }
                    ControlSignatures.setLOV(id, values);
                    if(ad.getMoveBlock() != null)
                    {
                        ad.getMoveBlock().getMoveTracker().openAgainTo(
                            EntrySiteEnum.ACTION_LISTENER);
                    }
                    ad.setLovValues(values);
                    if(origValueIncludedInNewList)
                    {
                        ControlSignatures.setText( id, origValue);
                    }
                    else
                    {
                        Err.pr( SdzNote.DISPLAY_LOV, "orig value of <" + origValue + "> is not contained in new list");
                    }
                }
            }
        }
        else
        {
            Err.pr( SdzNote.DISPLAY_LOV, "NO lovObjects - displayLovObjects() being called on " + this.id + " with reason " + creationReason);
        }
    }

    public int getCurrentNVOrdinal()
    {
        // Err.pr( "%% Reting current ordinal of " + currentOrdinal);
        return currentOrdinal;
    }

    public void incCurrentNVOrdinal()
    {
        currentOrdinal++;
        // Err.pr( "%% Just inced current ordinal to " + currentOrdinal);
    }

    public void resetCurrentNVOrdinal()
    {
        currentOrdinal = 0;
        // Err.pr( "%% Just reset current ordinal to 0");
    }
    
    public CalculationPlace getCalculationPlace()
    {
        return calculationPlace;
    }

    public int getId()
    {
        return id;
    }
}
