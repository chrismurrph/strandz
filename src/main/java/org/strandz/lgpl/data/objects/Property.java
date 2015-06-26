package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Chris
 * Date: 25/11/2008
 * Time: 3:56:00 PM
 */
public class Property implements PropertyI
{
    private PropertyIdentityDOI propertyIdentity;
    private Object value;
    private Object zeroValue;
    /**
     * When set to Utils.UNSET_INT (-99) then this property is a template
     * and has no value. Used for debugging.
     */
    private int hostId;
    private transient List<PropertyValueChangedListener> changedListeners;
    private static int constructedTimes = 0;
    private int id;

    public static PropertyI newInstance( PropertyIdentityDOI propertyIdentity, int hostId)
    {
        PropertyI result = new Property(propertyIdentity, hostId);
        return result;
    }

    public static PropertyI newInstance( PropertyIdentityDOI propertyIdentity)
    {
        PropertyI result = new Property(propertyIdentity, Utils.UNSET_INT);
        return result;
    }

    private Property( PropertyIdentityDOI propertyIdentity, int hostId)
    {
        constructedTimes++;
        id = constructedTimes;
        setPropertyIdentity( propertyIdentity);
        this.hostId = hostId;
    }

    public PropertyI copyConstruct( int hostId)
    {
        PropertyI result = new Property( propertyIdentity, hostId);
        result.setValue( value);
        return result;
    }

    public PropertyIdentityDOI getPropertyIdentity()
    {
        return propertyIdentity;
    }

    public String getName()
    {
        return propertyIdentity.getName();
    }

    public String toString()
    {
        return propertyIdentity.getName() + ", getValue(): " + getValue() + ", ID: " + id;
    }

    public void setPropertyIdentity(PropertyIdentityDOI propertyIdentity)
    {
        this.propertyIdentity = propertyIdentity;
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

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
        /*
        if(hostId == 188 && "56.00".equals( value.toString()))
        {
            Err.stack( "Setting property " + getName() + " to <" + value + ">");
        }
        */
        fireChangedListeners();
    }

    private void fireChangedListeners()
    {
        if(changedListeners != null)
        {
            for (Iterator<PropertyValueChangedListener> propertyValueChangedListenerIterator = changedListeners.iterator(); propertyValueChangedListenerIterator.hasNext();)
            {
                PropertyValueChangedListener propertyValueChangedListener = propertyValueChangedListenerIterator.next();
                propertyValueChangedListener.propertyValueChanged();
            }
        }
    }

    public void addPropertyValueChangedListener( PropertyValueChangedListener l)
    {
        if(changedListeners == null)
        {
            changedListeners = new ArrayList<PropertyValueChangedListener>();
        }
        changedListeners.add( l);
    }

    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Property that = (Property) o;

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
