package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.math.BigDecimal;

/**
 * User: Chris
 * Date: 15/02/2009
 * Time: 12:46:09 AM
 */
public class SummingPropertyComposite implements PropertyI
{
    private List<PropertyI> properties;
    private PropertyIdentityDOI propertyIdentity;
    private Object value;
    private Object zeroValue;
    /**
     * When set to Utils.UNSET_INT (-99) then this property is a template
     * and has no value. Used for debugging.
     */
    private int hostId;
    private static int constructedTimes = 0;
    private int id;

    public static PropertyI newInstance(
        List<PropertyI> properties, PropertyIdentityDOI propertyIdentity, int hostId)
    {
        PropertyI result = new SummingPropertyComposite( properties, propertyIdentity, hostId);
        return result;
    }

    public static PropertyI newInstance( PropertyIdentityDOI propertyIdentity)
    {
        PropertyI result = new SummingPropertyComposite( propertyIdentity,
            Utils.UNSET_INT);
        return result;
    }

    private SummingPropertyComposite( List<PropertyI> properties,
                               PropertyIdentityDOI propertyIdentity, int hostId)
    {
        constructedTimes++;
        id = constructedTimes;

        this.properties = properties;
        this.propertyIdentity = propertyIdentity;
        this.hostId = hostId;
    }

    private SummingPropertyComposite( PropertyIdentityDOI propertyIdentity, int hostId)
    {
        constructedTimes++;
        id = constructedTimes;

        //this.tempPropertiesMap = tempPropertiesMap;
        this.propertyIdentity = propertyIdentity;
        this.hostId = hostId;
    }

    public String toString()
    {
        return propertyIdentity.getName() + ", getValue(): " + getValue() + ", ID: " + id;
    }

    public PropertyIdentityDOI getPropertyIdentity()
    {
        return propertyIdentity;
    }

    public PropertyI copyConstruct( int hostId)
    {
        PropertyI result = new SummingPropertyComposite( properties, propertyIdentity, hostId);
        if(value != null)
        {
            result.setValue( value);
        }
        else
        {
            //template property so no value?
        }
        return result;
    }

    public String getName()
    {
        return propertyIdentity.getName();
    }

    public Object getValue()
    {
        MoneyAmount result = new MoneyAmount( 0, 0);
        if(properties != null)
        {
            for (int i = 0; i < properties.size(); i++)
            {
                PropertyI propertyI = properties.get(i);
                result = result.add( (MoneyAmount)propertyI.getValue());
            }
        }
        /*
        if(isToBeAveraged())
        {
            Err.error( "Actually meant this code to be for composing within the " +
                "same property over a larger period of time which this is not");
            //result = MoneyAmount.divide( result, new MoneyAmount( properties.size(), 0));
            BigDecimal divideResult = Utils.decimalDivide( new BigDecimal( result.toString()), properties.size());
            result = MoneyAmount.newInstance( divideResult.toString());
        }
        */
        value = result;
        return result;
    }

    public List<PropertyI> getProperties()
    {
        return properties;
    }

    public void setValue(Object value)
    {
        if(value.equals( zeroValue()))
        {
            //Assert.notNull( properties,
            //    "PropertyComposite does not have any properties, hostId: " + hostId + ", this: " + this);
            if(getProperties() != null)
            {
                for (int i = 0; i < getProperties().size(); i++)
                {
                    PropertyI propertyI = getProperties().get(i);
                    propertyI.setValue( newZeroValue());
                }
            }
            else
            {
                Err.pr( SdzNote.FIRST_MERGE,
                    "When setting to zero, PropertyComposite does not have any properties, hostId: " +
                    hostId + ", this: " + this);
            }
        }
        else
        {
            /*
             * Setting a composite value makes no sense, so need to code around this
             */
            if(properties == null)
            {
                Err.pr( "properties == null");
            }
            else
            {
                Err.pr( "Could set composite if only one: " + properties.size());
            }
            Err.pr( "property type: <" + getClass().getName() + ">");
            Err.pr( "property: <" + this + ">");
            Err.error( "Not implemented, trying with <" + value + "> for <" + getName() + ">");
        }
    }

    public void addPropertyValueChangedListener( PropertyValueChangedListener l)
    {
        Err.error( "Not implemented");
    }

    public Object zeroValue()
    {
        if(zeroValue == null)
        {
            zeroValue = newZeroValue();
        }
        return zeroValue;
    }

    public Object newZeroValue()
    {
        Object result = null;
        try
        {
            result = getType().newInstance();
        }
        catch (InstantiationException e)
        {
            Err.error( e);
        }
        catch (IllegalAccessException e)
        {
            Err.error( e);
        }
        return result;
    }

    public Class getType()
    {
        return propertyIdentity.getType();
    }

    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SummingPropertyComposite that = (SummingPropertyComposite) o;

        if (propertyIdentity.getName() != null ? !propertyIdentity.getName().equals(that.propertyIdentity.getName()) : that.propertyIdentity.getName() != null)
            return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null)
            return false;

        return true;
    }

    public int hashCode()
    {
        int result = propertyIdentity.getName() != null ? propertyIdentity.getName().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        //result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
