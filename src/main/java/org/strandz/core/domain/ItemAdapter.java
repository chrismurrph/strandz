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
package org.strandz.core.domain;

import org.strandz.core.domain.event.ItemChangeTrigger;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.core.info.domain.ItemAdapterI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ValidationException;

import javax.swing.event.ChangeEvent;
import java.awt.Color;
import java.util.List;

abstract public class ItemAdapter implements Comparable, ItemAdapterI
{
    private boolean wasInsertingWhenSetRow;
    private boolean alwaysEnabled = false;
    private boolean update = true;
    // private boolean alwaysEnabled; Seems only for fields, not tables.
    private String itemName;
    private Object b4Image;
    private Object programmaticValue;
    private boolean programmaticValueSet;
    // private boolean update; Only fields. just for laziness. Fix this and
    // alwaysEnabled together.
    /**
     * allowNulls is just a stopgap until a strategy is worked
     * out. One inconsistency is that blank Strings are always
     * created by editfields (tables as well I think). Should we
     * write nulls back to the objects in this case. Another -
     * booleans will appear false when null - but click twice and
     * you create a false object - might have been better to force
     * no nulls in the first place. No nulls to come from the data
     * base and no nulls to be created - this is what using the
     * exception NullAndHasNo_InfoImpl_ConstructorException is all about.
     * Another alternative is to have SeaweedInfoImpl give certain
     * controls null constructors (ie. use this constructor when
     * encounter a null), although same effect could easily be
     * achieved in the constructor of the object itself or by using
     * default values.
     * <p/>
     * If are using NullAndHasNo_InfoImpl_ConstructorException way then
     * when add a new column all your data becomes stuffed as all
     * its field values will be null. We could get round this problem
     * by employing setTextNull whenever a null came from the data
     * base. This could even be used instead of administering the
     * default error to the user. Only problem is that setTextNull
     * only works for Fields.
     * <p/>
     * Nagging worry - don't ever want to see a list of zeros in an
     * Integer column - how do we get round not using nulls in this
     * instance? Actually this won't be a problem as Integer treated
     * as JEditFields for both Tables and Fields. Crap - will be a
     * problem if it's stored as an Integer in the database. Try an
     * Integer in Aeroplane to test...
     * <p/>
     * Because of nagging worry decided that most flexiable and
     * intuitive for user is to allow nulls. Only case where nulls
     * wouldn't have a representation to user is Booleans where user
     * activity would create a false. Maxim - IF A NULL CAME OUT OF THE
     * DB THEN A NULL WILL GO BACK IN IF REPRESENTED AS A NULL - except
     * in case of Booleans where we can't tell what a null looks like,
     * so we simply do not allow nulls. CHANGED - for Booleans we do
     * allow nulls, but they become Boolean( false),
     */
    // private static final boolean allowNulls = true;
    protected Color notInErrorObject;
    private MoveBlockI moveBlock;
    private ItemAdapter originalAdapter;
    private ItemValidationTrigger itemValidationTrigger;
    private ItemChangeTrigger itemChangeTrigger;
    ErrorThrowerI errorThrower;
    private boolean inError = false;
    boolean isOrig = true;
    private int row = -1;
    public Object blankedoutValue;
    private List lovValues;
    private int ordinal;
    private Object defaultValue;
    // So don't think of a Comp as being focusable
    private boolean visual = true;
    private boolean dataReadExternally;
    private DOAdapter doAdapter;
    private AbstractCell cell;
    // The corresponding Attribute, passed to ChangeEvent
    private Object source;
    private CalculationPlace calculationPlace;
    static int constructedTimes;
    int id;
    static boolean debug = false;
    private static int times;
    private static int times1;
    private static int times2;

    /**
     * Only externally used by NullAdapter
     */
    public ItemAdapter()
    {
        constructedTimes++;
        id = constructedTimes;
        /*
        Err.pr( "CONSTRUCTED ItemAdapter " + id);
        if(id == 1)
        {
            Err.debug();
        }
        */
    }

    public ItemAdapter(
            String displayName,
            boolean alwaysEnabled,
            boolean update,
            ItemValidationTrigger itemValidationTrigger,
            ItemChangeTrigger itemChangeTrigger,
            ErrorThrowerI errorThrower,
            int ordinal,
            DOAdapter doAdapter,
            AbstractCell cell,
            Object source,
            CalculationPlace calculationPlace,
            boolean readExternally)
    {
        this();
        this.itemName = displayName;
        this.alwaysEnabled = alwaysEnabled;
        this.update = update;
        this.itemValidationTrigger = itemValidationTrigger;
        this.itemChangeTrigger = itemChangeTrigger;
        Assert.isNull( itemChangeTrigger, "ItemChangeTrigger not yet fully implemented " +
                "(and may be a focus change trigger after all)");
        this.errorThrower = errorThrower;
        this.ordinal = ordinal;
        this.doAdapter = doAdapter;
        this.cell = cell;
        this.source = source;
        Assert.notNull( calculationPlace, "Must be a CalculationPlace reference in every attribute");
        this.calculationPlace = calculationPlace;
        this.dataReadExternally = readExternally;
        /**/
        if(debug)
        {
            Err.pr(SdzNote.NV_PASTE_NOT_WORKING, "Created itemAdapter with displayName " + displayName);
            Err.pr(SdzNote.NV_PASTE_NOT_WORKING, ", and id " + id);
            if(id == 0)
            {
                Err.stackOff(SdzNote.INTERMITTENT_UNIT_TEST_FAILURE);
            }
        }
        /*
        Err.pr( "$$# Created itemAdapter " + doAdapter.getDOFieldName() + " with id " + id );
        if (id == 0)
        {
          Err.stackOff();
        }
        */
        if(cell == null)
        {
            Err.error( "An ItemAdapter must be created with a cell");
        }
    }

    public int getId()
    {
        return id;
    }
    
    public boolean isCalculated()
    {
        return calculationPlace.getCalculator( this) != null;
    }
    
    public CalculatedResultI getCalculator()
    {
        return calculationPlace.getCalculator( this);
    }

    public CalculationPlace getCalculationPlace()
    {
        return calculationPlace;
    }

    public boolean isReadExternally()
    {
        return dataReadExternally;
    }

    public boolean equals(Object o)
    {
        //Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof ItemAdapter))
        {// nufin
        }
        else
        {
            ItemAdapter test = (ItemAdapter) o;
            if(Utils.equals( itemName, test.itemName))
            {
                if(Utils.equals( cell.getName(), test.cell.getName()))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = Utils.hashCode(result, itemName);
        result = Utils.hashCode(result, cell.getName());
        return result;
    }

    public int compareTo(Object o)
    {
        int result = 0;
        ItemAdapter other = (ItemAdapter) o;
        result = this.ordinal - other.ordinal;
        return result;
    }

    public void setMoveBlock(MoveBlockI mb)
    {
        //Err.pr( "mb set to " + mb + " for itemAdapter " + this);
        this.moveBlock = mb;
    }

    public MoveBlockI getMoveBlock()
    {
        return moveBlock;
    }

    public ErrorThrowerI getErrorThrowerI()
    {
        return cell.getNode().getErrorThrowerI();
    }

    public String getName()
    {
        return itemName;
    }

    public String getItemName()
    {
        String result = itemName;
        if(result == null)
        {
            Err.error(
                "Re-write call to include below code now that DOAdapter is separate");
            // result = pUtils.variableToDisplay( doFieldName);
        }
        // Err.pr( "getItem() returning " + result);
        return result;
    }

    /**
     * If this is a special Adapter used to go back to where came
     * from then it will have an original itemAdapter. A copy of the
     * original was made, but should only be used for the special
     * purpose for which it was intended. Using this method we can
     * conveniently access the object we should be looking at.
     */
    public ItemAdapter getOriginalAdapter()
    {
        return originalAdapter;
    }

    public void setOriginalAdapter(ItemAdapter originalAdapter)
    {
        this.originalAdapter = originalAdapter;
    }

    public String toString()
    {
        String result;
        result = getName() + " ID: [" + id + "]";
        /*
        result = result
        + "<" + getClassType() + ">, "
        + "<" + doFieldName + ">, "
        + "<" + field + ">, "
        + "<" + readMethod + ">, "
        + "<" + writeMethod + ">";
        */
        return result;
    }

    /**
     * When becoming un-enabled (true into here) we want to visually blank out an item, or
     * 'set without value'. In this case we remember the value held so that when the item
     * is enabled again we (false into here) can set it to its previous value.
     *
     * Only intended to be called from setEnabled().
     * Is not really related to isBlank()
     */
    private void setWithoutValue(boolean b, IdEnum idEnum)
    {
        //Must set nonTransactionalRead to true - get this JDO error from calling getItemValue()
        // - thus have commented out the offending lines
//        Err.pr(SdzNote.nvPasteNotWorking, "In setWithoutValue() val is " + getItemValue( idEnum));
//        if(SdzNote.queryNotWorkingNonVisual.isVisible() && "christianName".equals( getName()))
//        {
//            times2++;
//            Err.pr( "setWithoutValue(), current value: " + getItemValue( idEnum) + " to " + b + " times " + times2);
//            if(times2 == 2)
//            {
//                Err.debug();
//            }
//        }
        if(b) //unenabling
        {
            Object value = getItemValue( idEnum);
            if(value == null)
            {// already blank
            }
            else if(!(value instanceof String))
            {
                // Err.error( "RuntimeAttribute.setBlank() is a convenience"
                // + " method that only supports Strings, got a " + value.getClass().getName());
                setItemValue(null, idEnum);
                blankedoutValue = value;
            }
            else
            {
                String strVal = (String) value;
                if(strVal.equals(""))
                {// already blank
                }
                else
                {
                    setItemValue(null, idEnum);
                    blankedoutValue = value;
                }
            }
        }
        else //enabling
        {
            if(blankedoutValue != null)
            {
                setItemValue(blankedoutValue, idEnum);
            }
        }
        if(SdzNote.QUERY_NOT_WORKING_NON_VISUAL.isVisible() && "christianName".equals( getName()))
        {
            Err.pr( "setWithoutValue(), to value: " + getItemValue( idEnum) + " to " + b);
        }
        if(SdzNote.NV_PASTE_NOT_WORKING.isVisible())
        {
            times1++;
            Err.pr(SdzNote.NV_PASTE_NOT_WORKING, "In setWithoutValue() to " + b + " times " + times1 + " val become " + getItemValue( idEnum));
            if(times1 == 0)
            {
                Err.stack();
            }
        }
    }

    /*
    public Object getFieldValueFromAny( Object element)
    {
    Object result = null;
    Object narrowed = element;
    Adapter ad = this;
    Tie tie = ad.getTie();
    if(tie != null)
    {
    Err.pr( "---");
    Err.pr( "tie's child is a " + ((FieldObj)tie.getChild()).getInstantiable().getName());
    Err.pr( "need to cf with a " + element.getClass());
    if(element.getClass() == ((FieldObj)tie.getChild()).getInstantiable())
    {
    narrowed = tie.getFieldValue( element);
    Err.pr( "tie.getFieldValue( ele) has allowed us to go from " +
    element.getClass() + " to " + narrowed.getClass());
    }
    else
    {
    *
    *
    *
    Err.error( "Need make work recursively");
    }
    }
    else
    {
    //Equivalently ad.getClazz() already equals element.getClass()
    }
    result = getFieldValue( narrowed);
    return result;
    }
    */

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(Object value)
    {
        this.defaultValue = value;
        // Err.pr( "defaultValue of itemAdapter being set to <" + value + ">");
    }

    public void setB4ImageValue(Object element)
    {
        /**/
        if(SdzNote.ROSTERABILITY.isVisible() && getName() != null &&
                (
                 Utils.getFalse()
                 //getName().equals("rosterSlot specificDate") 
                 //|| getName().equals("christianName")  
                 //|| getName().equals("seniority Name")
                 )
          )
        /**/
        {
            times1++;
            Err.pr(getName() + " ==$$$==  setB4ImageValue to " +
                    element + " times " + times1 + " ID " + id);
            if(times1 == 0)
            {
                Err.stack();
            }
        }
        b4Image = element;
        programmaticValueSet = false;
    }

    public Object getB4ImageValue()
    {
        /*
        times++;
        Err.pr( "$$# called getB4ImageValue() from itemAdapter ID: "
            + id + " times " + times );
        if (times == 1)
        {
            Err.stack();
        }
        */
        return b4Image;
    }

    public abstract Object getItemValue();
    public abstract Object getItem();
    public abstract Object getItemValue( IdEnum idEnum);
    public abstract void setItemValue(Object value, IdEnum idEnum);
    public abstract void requestFocus();

    /**
     * This method is only called by the user
     *
     * @param value
     */
    public void setItemValue(Object value)
    {
        /*
        if(debug && id == 1)
        {
          times++;
          Err.pr( "###SETTING TO <" + value + "> times " + times + " ID: " + id );
          if (times == 0)
          {
            Err.debug();
          }
        }
        */
        if(cell == null)
        {
            Err.error("An ItemAdapter must be created with a cell");
        }
        // Sometimes null!
        // Err.pr( "Setting item value to " + value +
        // " of type " + value.getClass().getName());
        String extra = "";
        if(value != null)
        {
            extra = ", of type <" + value.getClass().getName() + ">";
        }
        /* Not sure why needed, and got in the way when using CalculationPlace when focus
         * changed due to a tab - run ProdKPI
        cell.chkOnCurrentNode(
            "setItemValue()",
            "For item <" + itemName + ">, was trying to set value to <" + value + ">" + extra);
        */    
        // Non-applichousing adapters do not have a Move Block associated with them.
        if(getMoveBlock() != null)
        {
            getMoveBlock().setItemValueAdapter(this);
        }
        blankedoutValue = null;
        programmaticValue = value;
        programmaticValueSet = true;
    }

    public void resetToB4ImageValue()
    {
        setItemValue(b4Image);
    }

    public boolean isAlwaysEnabled()
    {
        return alwaysEnabled;
    }

    public boolean isUpdate()
    {
        return update;
    }

    abstract public IdEnum createIdEnum( String desc);    
    abstract public IdEnum createIdEnum( int row, String desc);
    abstract public IdEnum createIdEnum( Class clazz);

    protected void putPhysicallyInError(boolean howWant, boolean howIs)
    {
        // Err.pr( "putPhysicallyInError() being called for " + doFieldName +
        // " with " + howWant + " and " + howIs);
        if(howWant && !howIs)
        {
            notInErrorObject = ControlSignatures.setInErrorTrue(createIdEnum( "putPhysicallyInError()"), this);
        }
        else if(!howWant && howIs)
        {
            ControlSignatures.setInErrorFalse(createIdEnum( "putPhysicallyInError()"), notInErrorObject);
            notInErrorObject = null;
        }
    }

    public int getRow()
    {
        return row;
    }

    public boolean wasInsertingWhenSetRow()
    {
        return wasInsertingWhenSetRow;
    }

    public void setRow(int row)
    {
        if(SdzNote.BAD_TABLE_REPOSITIONING.isVisible() && cell != null)
        {
            times++;
            Err.pr("setRow() to " + row + " for " + getName() + " in " +
                cell.getNode().getState() + " times " + times +
                " id " + id + " cell " + cell.getName());
            if(row == 2)
            {
                Err.stackOff();
            }
        }
        this.row = row;
        // not sure using this anymore:
        /*
        this.wasInsertingWhenSetRow =
        cell.
        getNode().
        getState().
        isNew();
        */
    }

    public void setInError(boolean b)
    {
        chk();
        inError = b;
        // Err.pr( "Have setInError() to " + inError + " for " + getName() + " of id " + id);
    }

    public boolean isInError()
    {
        chk();
        if(inError)
        {// Err.pr( "Returning inError for " + getName());
        }
        return inError;
    }

    public void confirmError()
    {
        // Err.pr( "putPhysicallyInError() TO BE called for " + doFieldName +
        // " with " + _inError + " and " + (notInErrorObject != null) +
        // " and id of " + id);
        putPhysicallyInError(inError, notInErrorObject != null);
    }

    abstract public void makeUntouchable(boolean b);

    public ItemValidationTrigger getItemValidationTrigger()
    {
        return itemValidationTrigger;
    }

    public void setItemValidationTrigger(ItemValidationTrigger itemValidationTrigger)
    {
        this.itemValidationTrigger = itemValidationTrigger;
    }

    public ItemChangeTrigger getItemChangeTrigger()
    {
        return itemChangeTrigger;
    }

    public void setItemChangeTrigger(ItemChangeTrigger itemChangeTrigger)
    {
        this.itemChangeTrigger = itemChangeTrigger;
    }

    /**
     * What we are doing here (with lovValues) is a little bit weird because it is actually
     * Cell validation, done at the attribute level. We don't have any
     * Cell validation, so we have effectively split up each value in the
     * list was given to do lov validation. This will work for validation,
     * but not for display - test with a lookup to addresses.
     * Improvement will make is that this method will be fired only where
     * the cell has one attribute. If more than one then something similar
     * will be fired at the record validation level.
     */
    public void fireItemValidateEvent()
    {
        if(SdzNote.MDATE_ENTRY_FIELD.isVisible())
        {
            times++;
            Err.pr(SdzNote.MDATE_ENTRY_FIELD, "fireItemValidateEvent() for - " + getItemName() +
                " with trigger " + itemValidationTrigger);
            if(times == 0)
            {
                Err.stack();
            }
        }
        Assert.notNull( moveBlock, "No moveBlock in ItemAdapter - ID: " + id + " of type " + getClass().getName() + " - need to call setMoveBlock()");
        if(!moveBlock.isIgnoreValidation() && lovValues != null)
        {
            Object value = getItemValue();
            if(!lovValues.contains(value))
            {
                setInError(true);
                Err.pr("<" + value + "> is not in " + lovValues);
                errorThrower.throwApplicationError("<" + value + "> not allowed",
                    ApplicationErrorEnum.INTERNAL);
            }
            else
            {
                setInError(false);
            }
        }
        if(itemValidationTrigger != null && !moveBlock.isIgnoreValidation())
        {
            ChangeEvent evt = new ChangeEvent(source);
            try
            {
                itemValidationTrigger.validateItem(evt);
            }
            catch(ValidationException ex)
            {
                if(moveBlock == null)
                {
                    Err.error("No moveBlock in " + this);
                }
                errorThrower.throwApplicationError(ex,
                    ApplicationErrorEnum.ITEM_VALIDATION);
            }
        }
    }
    
    public void fireItemChangeEventOnAllOthers()
    {
        Assert.notNull( calculationPlace, "ItemAdapter has no calculationPlace, ID: " + id);
        /* Isn't fireCalculationFromSync enough? Only calling cell.getItemValue() and
         * thus manufacturing an object because too lazy to grab the current object from
         * an ItemAdapter.
         */
        Err.pr( SdzNote.CAYENNE_OVER_PERSISTING, "Doing fireCalculationFromItemAdapter's getItemValue()");
        Object obj = cell.getItemValue();
        calculationPlace.fireCalculationFromItemAdapter( obj);
    }

    public void fireItemChangeEvent()
    {
        if(itemChangeTrigger != null)
        {
            ChangeEvent evt = new ChangeEvent(source);
            itemChangeTrigger.changeItem(evt);
        }
    }
    
    private void chk()
    {
        if(!isOrig)
        {
            Err.error(
                "Accessing inError property s/NOT be done for "
                    + "Adapters created when key released");
        }
    }

    public void setEnabled(boolean b)
    {
        setEnabled(b, null);
    }

    // First method written as replacement to directly talking
    // to a control, for purposes of portability
    void setEnabled(boolean b, IdEnum id)
    {
        /*
        times++;
        Err.pr( "setEnabled to " + b + " on " + this + " times " + times);
        if(times == 14)
        {
            Err.debug();
        }
        */
        if(id == null)
        {
            id = createIdEnum( toString());
        }
        if(id.getControl() == null)
        {
            Err.error( "Can't setEnabled() until have a control in " + this);
        }
        ControlSignatures.setEnabled(id, b);
        /*
        times++;
        Err.pr(SdzNote.NV_PASTE_NOT_WORKING, "setWithoutValue() times " + times);
        if(times == 0)
        {
            Err.stack();
        }
        */
        setWithoutValue(!b, id);
    }

    public boolean isEnabled()
    {
        return ControlSignatures.isEnabled(createIdEnum( toString()));
    }

    public void setLovValues(List lovValues)
    {
        this.lovValues = lovValues;
    }

    public boolean isVisual()
    {
        return visual;
    }

    public void setVisual(boolean visual)
    {
        this.visual = visual;
    }

    public boolean isDataReadExternally()
    {
        return dataReadExternally;
    }

    public void setDataReadExternally(boolean dataReadExternally)
    {
        if(id == 1)
        {
            Err.stack();
        }
        this.dataReadExternally = dataReadExternally;
    }

    public int getOrdinal()
    {
        return ordinal;
    }

    public void setOrdinal(int ordinal)
    {
        this.ordinal = ordinal;
    }

    public boolean afterAndB4Different(
        Object aiValue, Object b4ImageVal, List uiItemsChanged)
    {
        boolean result = false;
        boolean theyAreEqual = false;
        boolean afterImageNullAndNotEqual = false;
        /*
        times++;
        Err.pr( "In afterAndB4Different times " + times);
        if(times == 33)
        {
        Err.debug();
        }
        */
        if(aiValue == null)
        {
            if(b4ImageVal == null)
            {
                theyAreEqual = true;
            }
            else
            {
                afterImageNullAndNotEqual = true;
            }
        }
        if(aiValue != null)
        {
            if(b4ImageVal == null && aiValue.getClass() == String.class
                && aiValue.equals(""))
            {
                aiValue = null;
                theyAreEqual = true;
            }
            else if(b4ImageVal == null && aiValue.toString() == null)
            {
                aiValue = null;
                theyAreEqual = true;
            }
            else if(b4ImageVal == null && aiValue.getClass() == Boolean.class
                && aiValue.equals(Boolean.FALSE))
            {
                aiValue = null;
                theyAreEqual = true;
            }
        }
        if(!theyAreEqual)
        {
            if(!afterImageNullAndNotEqual)
            {
                if(b4ImageVal != null)
                {
                    if(aiValue.getClass() != b4ImageVal.getClass())
                    {
                        /*
                        * AI shows what has been in the control and altered. Note this is after
                        * any changes made by the registered AbstractObjectControlConverts.
                        * (ie. getText does some converting).
                        * Rather than creating a new object from what is often a String, and then
                        * doing the cf, we will convert the data (BI) to be the same type as that
                        * that the control returned.
                        */
                        // Err.pr( "AI of type " + aiValue.getClass().getName() +
                        // " while BI of type " + b4ImageVal.getClass().getName());
                        b4ImageVal = ControlSignatures.convertToDisplaySpecialControlType(
                            createIdEnum( toString()), aiValue.getClass(), b4ImageVal);
                    }
                }
            }
            if(afterImageNullAndNotEqual || (!aiValue.equals(b4ImageVal)))
            {
                ItemSnapshot uif = new ItemSnapshot();
                uif.name = doAdapter.getDOFieldName();
                uif.setB4Image(b4ImageVal);
                uif.setAfterImage(aiValue);
                uif.itemAdapter = this;
                if(uiItemsChanged != null)
                {
                    uiItemsChanged.add(uif);
                }
                else
                {
                    //By the method call param == null - we are not interested in recording the change
                }
                /*
                times4++;
                Err.pr( "==============");
                Err.pr( "For " + uif.name);
                Err.pr( ", just added value (AI)" + uif.getAfterImage());
                Err.pr( ", did not == (BI)" + uif.getB4Image());
                Err.pr( "Adapter was \n" + this);
                Err.pr( "============== times " + times4);
                if(times4 == 0)
                {
                Err.stack();
                }
                */
                result = true;
            }
        }
        //
        return result;
    }

    public AbstractCell getCell()
    {
        return cell;
    }

    public DOAdapter getDoAdapter()
    {
        return doAdapter;
    }

    public Object getProgrammaticValue()
    {
        return programmaticValue;
    }

    public boolean isProgrammaticValueSet()
    {
        return programmaticValueSet;
    }

    public Object getSource()
    {
        return source;
    }
    
    abstract public Object getTableControl();


    
    /**
     * NullAndHasNo_InfoImpl_ConstructorException is only thrown when
     * allowNulls set to false.
     */
    /*
    public Object getFieldValue( Object element, int blankingPolicy)
    throws NullAndHasNo_InfoImpl_ConstructorException
    {
    Object result = null;
    **
    //Err.pr("BUZ to invoke read: " + readMethod);
    //Err.pr("BUZ on: " + element);
    **
    **
    if(element == null)
    {
    //Err.error( "getFieldValue must be called with not-null arg");
    result = null;
    }
    **
    else
    {
    result = pSelfReference.invoke( element, readMethod, null);
    if(result == null)
    {
    if(readMethod.getReturnType() == Boolean.class)
    {
    **
    *
    * Various objects that make up a record may well have
    * null values. If we can we create the objects rather
    * than have nulls. (First noticed nulls coming back with
    * JTable - they (nulls) were messing up comparisons were
    * doing in TableObj.applyDifferences()). Following error
    * message will only occur for tables.
    *
    //if(! pSelfReference.hasNullConstructor( readMethod.getReturnType()))
    //{ always true for Boolean so don't bother.
    *
    * Would be pointless to have tried to check this earlier,
    * as default values are specified dynamically.
    *
    //Err.error("BUZ have return null so will throw");
    throw new NullAndHasNo_InfoImpl_ConstructorException( readMethod);
    //}
    //else
    //{
    //  result = pSelfReference.factory( readMethod.getReturnType());
    //  if(result == null)
    //  {
    //    Err.error("Can't construct a <" + readMethod.getReturnType() + ">");
    //  }
    //}
    **
    }
    else
    {
    if(blankingPolicy == ControlInfo.NEED_CONSTRUCT_BLANK_OBJ_READ)
    {
    //If has a blank constructor
    *
    if(readMethod.getReturnType() != String.class)
    {
    Err.pr( this);
    Err.error( "Only works String IN DEVELOP - " +
    readMethod.getReturnType());
    }
    else
    {
    result = new String();
    }
    *
    }
    }
    }
    }
    return result;
    }
    */

}
