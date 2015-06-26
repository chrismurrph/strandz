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

import org.strandz.core.domain.DOAdapter;
import org.strandz.core.domain.FieldItemAdapter;
import org.strandz.core.domain.IdEnum;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.CalculationPlace;
import org.strandz.core.domain.CalculatedResultI;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.event.ItemChangeTrigger;
import org.strandz.lgpl.extent.ItemMatcher;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.Utils;

/**
 * This base class is what we normally refer to as an 'attribute'. It
 * represents the mapping of a DO Field to an Item that the user will
 * see on the screen.
 *
 * @author Chris Murphy
 */
abstract public class RuntimeAttribute extends StemAttribute
    implements ItemMatcher, Comparable
{
    boolean alwaysEnabled = false;
    boolean update = true;
    private ItemAdapter itemAdapter;
    private DOAdapter doAdapter;
    private boolean enabled = true;
    private boolean readExternally;
    private ItemValidationTrigger itemValidationTrigger;
    private ItemChangeTrigger itemChangeTrigger;
    private CalculatedResultI calculatedResultI;
    private static int times;
    private static int timesHasChanged;
    private static int timesNull;

    public RuntimeAttribute()
    {
        super();
    }

    public RuntimeAttribute(Attribute attrib)
    {
        super(attrib);
        if(attrib instanceof RuntimeAttribute)
        {
            ItemAdapter itemAdapter = ((RuntimeAttribute) attrib).getItemAdapter();
            if(itemAdapter != null)
            {
                setItemAdapter(itemAdapter);
            }

            DOAdapter doAdapter = ((RuntimeAttribute) attrib).getDOAdapter();
            if(doAdapter != null)
            {
                setDOAdapter(doAdapter);
            }
        }
    }

    public RuntimeAttribute(String dataFieldName, Integer ordinal)
    {
        super(dataFieldName, ordinal);
    }

    /**
     * Useful to create your own subclassed ItemAdapter - so far for debugging purposes.
     * Note that not called polymorphically and some method signatures differ - but the 
     * name will remain the same in subclasses. 
     */
    public ItemAdapter createItemAdapter( DOAdapter doAdapter, CalculationPlace calculationPlace)
    {
        ItemAdapter result = new FieldItemAdapter(
                getItem(),
                getCell(),
                isAlwaysEnabled(),
                getName(),
                isUpdate(),
                getItemValidationTrigger(),
                getItemChangeTrigger(),
                getCell().getNode().getErrorThrowerI(),
                //((RuntimeAttribute)attribute).getOrdinal().intValue(),
                Utils.UNSET_INT,
                doAdapter,
                this,
                calculationPlace,
                isEnabled(),
                isReadExternally()
        );
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        if(getItemAdapter() != null)
        {
            result = 37 * result + getItemAdapter().hashCode();
        }
        if(getDOAdapter() != null)
        {
            result = 37 * result + getDOAdapter().hashCode();
        }
        return result;
    }

    public boolean equals(Object o)
    {
        boolean result = super.equals(o);
        if(result)
        {
            RuntimeAttribute test = (RuntimeAttribute) o;
            if((getItemAdapter() == null
                ? test.getItemAdapter() == null
                : getItemAdapter().equals(test.getItemAdapter())))
            {
                if((getDOAdapter() == null
                    ? test.getDOAdapter() == null
                    : getDOAdapter().equals(test.getDOAdapter())))
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
            }
            else
            {
                result = false;
            }
        }
        return result;
    }

    abstract public Object getItem();
    //abstract public Object getItemName();
    
    public Object getItemName()
    {
        String result = null;
        if(getItemAdapter() != null)
        {
            result = getItemAdapter().getItemName();
        }
        /*
        else
        {
        Err.stack( "Why calling getItemLabel() when have no Adapter " +
        "and are therefore at design time");
        }
        */
        if(result == null || result.equals(""))
        {
            result = NameUtils.variableToDisplay(getDOField());
        }
        return result;
    }

    public void setB4ImageValue( Object value)
    {
        if(getItemAdapter() != null)
        {
            getItemAdapter().setB4ImageValue( value);
        }
    }

    public void clearItemAdapter()
    {
        setItemAdapter( null);
    }

    public void clearDOAdapter()
    {
        setDOAdapter( null);
    }

    void setItemAdapter(ItemAdapter itemAdapter)
    {
        /*
        if(itemAdapter == null)
        {
            if(this.itemAdapter != null)
            {
                timesNull++;
                Err.pr( "Attribute <" + getName() + "> setting item adapter (ID: " + this.itemAdapter.getId() + ") to NULL, times " + timesNull);
                if(timesNull == 0)
                {
                    Err.stack();
                }
            }
        }
        else
        {
            Err.pr( "Attribute ID " + getId() + " now has item adapter: " + itemAdapter + ", ID: " + itemAdapter.getId());
        }
        */
        this.itemAdapter = itemAdapter;
        toRuntime(itemAdapter);
    }

    void setDOAdapter(DOAdapter doAdapter)
    {
        this.doAdapter = doAdapter;
    }

    /**
     * WHAT WAS i GOING ON ABOUT?
     * Way could do unenable would be to get the TableColumn and set its
     * editor as the renderer that will be used for that type of column
     * (find by first seeing if the column has an renderer then getting
     * the renderer for that class). When enable again just set to null.
     * Probably this is too complicated, and just give user Err.error()
     * that could not unenable, and probably best to subclass own table.
     */
    private void toRuntime(ItemAdapter itemAdapter)
    {/*
     if(!isEnabled())
     {
     Err.error( "Need to code to enable/unenable " + this);
     }
     */}

    public ItemAdapter getItemAdapter()
    {
        return itemAdapter;
    }

    public DOAdapter getDOAdapter()
    {
        return doAdapter;
    }

    public boolean isBlank()
    {
        boolean result = false;
        Object value = itemAdapter.getItemValue();
        if(value == null)
        {
            result = true;
        }
        else if(!(value instanceof String))
        {// Err.error( "RuntimeAttribute.isBlank() is a convenience"
            // + " method that only supports Strings, got a " + value.getClass().getName());
        }
        else
        {
            String strVal = (String) value;
            if(strVal.equals(""))
            {
                result = true;
            }
        }
        return result;
    }

    public boolean isValueEqualTo(Object value)
    {
        boolean result = false;
        Object val = itemAdapter.getItemValue();
        if(val == null && value == null)
        {
            result = true;
        }
        else if(val == null || value == null)
        {
            result = false;
        }
        else
        {
            if(val.equals(value))
            {
                result = true;
            }
        }
        return result;
    }

    public Object getItemValue()
    {
        Object result = null;
        if(itemAdapter != null)
        {
            result = itemAdapter.getItemValue();
            if(result == null)
            {
                result = itemAdapter.getDefaultValue();
            }
        }
        else
        {
            //ReferenceLookupAttribute now going thru here, so don't error (10/5/05)
            //This change would have been to do with integrating non-visuals in with the
            //rest, for cases where they are used to simulate a normal running application
            //ReferenceLookupAttribute was included at same time. There might be an
            //interpretation of item value similar to what we did with cell. TODO - investigate
            //
            //Err.error( "No item (control) with a value yet exists for a " + this.getClass().getName());
            // return defaultValue;
        }
        return result;
    }

    public Object getProgrammaticValue()
    {
        Object result = null;
        if(itemAdapter != null)
        {
            result = itemAdapter.getProgrammaticValue();
        }
        else
        {
            Err.error("No concept of an item (control) with a value yet exists");
            // return defaultValue;
        }
        return result;
    }

    public boolean isProgrammaticValueSet()
    {
        return itemAdapter.isProgrammaticValueSet();
    }

    public Object getB4ImageValue()
    {
        Object result = null;
        //Assert.notNull( itemAdapter, "itemAdapter null in " + this);
        if(itemAdapter != null)
        {
            result = itemAdapter.getB4ImageValue();
        }
        return result;
    }

    public void setItemValue(Object value)
    {
        if(itemAdapter == null)
        {
            Err.error(
                "If not see this, then not setting the default value at runtime");
            // defaultValue = value;
        }
        else
        {
            itemAdapter.setItemValue(value);
        }
    }

    /**
     * Should only matter for visuals, where comparable implemented properly
     */
    public int compareTo(Object other)
    {
        int result = 0;
        // reversed direction so that 1 comes before 0. As 0 is default
        // this makes it easy to select which comes first. But non-
        // intuitive, and possibly non-standard.
        int thisOrd = this.getOrdinal().intValue();
        int otherOrd = ((RuntimeAttribute) other).getOrdinal().intValue();
        //REMOVED minus here - so against above - order was wrong way round when did
        //a sort in Node.isColumnEditable()
        result = (thisOrd - otherOrd);
        return result;
    }

    /**
     * Will also take care of the lookup conversion - ie obj param will be suitable for the
     * top level cell, but not for any of the lookup cells which will require a different
     * type of object.
     */
    public Object getFieldValue( Object obj)
    {
        Object dataFieldValue = getItemAdapter().getDoAdapter().getFieldValueFromAny(
            obj);
        return dataFieldValue;
    }

    /**
     * Will handle situation where are given a Volunteer, yet this
     * Attribute is for a WhichShift.
     */
    public boolean itemMatchesData(Object obj)
    {
        boolean result = false;
        Object cf = getItemValue();
        Object dataFieldValue = getItemAdapter().getDoAdapter().getFieldValueFromAny(
            obj);
        if(cf == null && dataFieldValue == null)
        {
            result = true;
        }
        else if(cf == null)
        {
            result = false;
        }
        else if(cf.equals(dataFieldValue))
        {
            result = true;
        }
        return result;
    }

    public String toString()
    {
        String res = new String();
        res = res + "[RuntimeAttribute dataFieldName: <" + getDOField() + ">, "
            + "cell: <" + cell + ">, " + "name: <" + name + ">, " + "value: <"
            + getItemValue() + ">" + "]";
        return res;
    }

    /**
     * If the value that see (in the control) has changed and this value is
     * equal to what was programatically set, then return true.
     *
     * @return Whether runtime item value altering has occured
     */
    public boolean hasProgrammaticallyChanged()
    {
        boolean result = hasChanged();
        if(result)
        {
            if(isProgrammaticValueSet())
            {
                Object ai = getItemValue();
                Object aiProgramatically = getProgrammaticValue();
                result = !getItemAdapter().afterAndB4Different(ai, aiProgramatically,
                    null);
            }
            else
            {
                result = false;
            }
        }
        return result;
    }

    public boolean hasChanged()
    {
        boolean result;
        if((SdzNote.ROSTERABILITY.isVisible() && getDOField().equals( "specificDate")))
        {
            timesHasChanged++;
            //Err.pr( "To try b4 and after different in " + this + " times " + timesHasChanged);
            if(timesHasChanged == 2)
            {
                Err.debug();
            }
        }
        Object ai = getItemValue();
        Object bi = getB4ImageValue();
        result = itemAdapter.afterAndB4Different(ai, bi, null);
        if(result)
        {
            Err.pr( SdzNote.ROSTERABILITY.isVisible(), "Have changed because bi was <" + bi + "> and ai now <" + ai + 
                    ">, ID: " + itemAdapter.getId() + " for <" + getName() +
                    "> in <" + getCell().getNode().getStrandDebugInfo() + ">");
            //Err.pr( SdzNote.BI_AI, "\tControl of type: " + getItemAdapter().getItem().getClass().getName()
            // + " in " + ((JComponent)getItemAdapter().getItem()).getParent().getClass().getName());
            if(Utils.equals(ai, itemAdapter.getDefaultValue()))
            {
                // Err.pr( "Now not because ai was equal to defaultValue");
                result = false;
            }
            else
            {// Err.pr( "ai was not equal to default value of <" + getAdapter().getDefaultValue() + ">");
            }
        }
        /*
        if(ai != null && ai.equals( bi))
        {
        result = false;
        }
        else
        {
        if(ai == null && bi == null)
        {
        result = false;
        }
        }
        */
        return result;
    }

    /**
     * Works at both design and run times.
     * (Before was working only at Design Time)
     */
    public boolean isEnabled()
    {
        boolean result = true;
        ItemAdapter itemAdapter = getItemAdapter();
        if(itemAdapter != null)
        {
            result = itemAdapter.isEnabled();
        }
        else
        {
            result = this.enabled;
        }
        return result;
    }

    /**
     * Works at both design and run times.
     */
    public void setEnabled(boolean b)
    {
        /*
        if(SdzNote.QUERY_NOT_WORKING_NON_VISUAL.isVisible()
            && getName() != null && getName().equals( "christianName")
            && Utils.instanceOf( this, RuntimeAttribute.class)
            )
        */
        /*
        if(id == 14)
        {
            times++;
            Err.pr( "In RuntimeAttribute.setEnabled for " + getName() + ", enabled is " + isEnabled() + " times " + times);
            if(times == 0)
            {
                Err.debug();
            }
        }
        */
        ItemAdapter itemAdapter = getItemAdapter();
        if(itemAdapter != null)
        {
            itemAdapter.setEnabled(b);
            this.enabled = b;
        }
        else
        {
            this.enabled = b;
        }
    }

    public boolean isAlwaysEnabled()
    {
        return alwaysEnabled;
    }

    public void setAlwaysEnabled(boolean alwaysEnabled)
    {
        this.alwaysEnabled = alwaysEnabled;
    }

    public boolean isUpdate()
    {
        return update;
    }

    public void setUpdate(boolean b)
    {
        this.update = b;
    }

    /**
     * If true then won't go to the DO at all. First use for a table attribute that
     * wanted to solely use its renderer
     * @return
     */
    public boolean isReadExternally()
    {
        return readExternally;
    }

    public void setReadExternally(boolean readExternally)
    {
        if(readExternally == true)
        {
            Err.stack( "readExternally == true");
        }
        this.readExternally = readExternally;
    }

    public IdEnum createIdEnum()
    {
        IdEnum result = null;
        ItemAdapter ad = getItemAdapter();
        if(ad != null) //
        {
            result = ad.createIdEnum( toString());
        }
        return result;
    }

    public void setInError(boolean b)
    {
        Err.pr( SdzNote.FIELD_VALIDATION, "Setting in error for <" + this + "> to " + b);
        getItemAdapter().setInError(b);
    }

    public boolean isInError()
    {
        return getItemAdapter().isInError();
    }

    public void setItemValidationTrigger(ItemValidationTrigger vl)
    {
        itemValidationTrigger = vl;
    }

    public ItemValidationTrigger getItemValidationTrigger()
    {
        return itemValidationTrigger;
    }

    public ItemChangeTrigger getItemChangeTrigger()
    {
        return itemChangeTrigger;
    }

    public void setItemChangeTrigger(ItemChangeTrigger itemChangeTrigger)
    {
        this.itemChangeTrigger = itemChangeTrigger;
    }
    
    public CalculatedResultI getCalculatedResultI()
    {
        return calculatedResultI;
    }

    public void setCalculatedResultI(CalculatedResultI calculatedResultI)
    {
        this.calculatedResultI = calculatedResultI;
    }
}
