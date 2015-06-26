package org.strandz.lgpl.data.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.strandz.lgpl.util.IdentifierI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.SdzNote;

/**
 * User: Chris
 * Date: 17/02/2009
 * Time: 10:18:53 AM
 */
public class PropertyKeeperHelper
{
    /**
     * For composites only need the names and types, values are found from the children
     */
    protected List<PropertyI> properties = new ArrayList<PropertyI>();
    /**
     * For debugging
     */
    protected IdentifierI host;

    public PropertyKeeperHelper( IdentifierI host)
    {
        this.host = host;
    }

    public PropertyKeeperHelper( List<PropertyI> allProperties)
    {
        for (int i = 0; i < allProperties.size(); i++)
        {
            PropertyI property = allProperties.get(i);
            addProperty( property);
        }
    }

    private static int times;

    public void addProperty( PropertyI property)
    {
        //Err.pr( "Adding property " + property + " to " + host);
        Assert.isFalse( exists( property), "Cannot add as already exists: <" + property.getName() + ">, in <" + host + ">");
        Assert.notNull( property.getName());
        //Assert.notNull( property.getType());
        if(host != null)
        {
            boolean isSummingComposite = property instanceof SummingPropertyComposite;
            boolean isInformMissingComposite = property instanceof InformMissingPropertyComposite;
            if(!isSummingComposite && !isInformMissingComposite)
            {
                if(property.getValue() != null)
                {
                    /* We don't always have to put the value in afterwards.
                     * First time did not is when syncing from an imported file
                     *
                    String hostTxt = ", host ID: <" + host.getId() + ">, host type: <" + host.getClass().getName() + ">";
                    String type = property.getClass().getName();
                    Err.error( "Are adding a property <" + property +
                        "> and it already has a value <" +
                        property.getValue() + ">" + hostTxt + ", type: " + type);
                    */
                }
            }
        }
        properties.add( property);
        /*
        if( host != null && host.toString().contains( "1000, Thu Jan 08 00:00:00 EST 2009") && property.getName().equals( "Day Out"))
        {
            times++;
            Err.pr( "host: " + host + ", times " + times);
            if(times == 0)
            {
                Err.stack();
            }
        }
        */
    }

    public void removeProperty( PropertyI property)
    {
        Assert.isTrue( exists( property));
        properties.remove( property);
        //Err.pr( "Have removed " + property + " from host " + host);
        Assert.isFalse( exists( property));
    }

    public List<PropertyI> getProperties()
    {
        return properties;
    }

    private boolean exists( PropertyI property)
    {
        return properties.contains( property);
    }

    public boolean exists( String propertyName)
    {
        boolean result = getPropertyByName( propertyName) != null;
        return result;
    }

    public boolean existsIgnoreCase( String propertyName)
    {
        boolean result = getPropertyByNameIgnoreCase( propertyName) != null;
        return result;
    }

    public Object getValue( String name)
    {
        Object result = getPropertyByName( name).getValue();
        return result;
    }

    public Object getValue( String name, Object defaultValue)
    {
        Object result;
        PropertyI property = getPropertyByName( name);
        if(property != null)
        {
            result = property.getValue();
        }
        else
        {
            result = defaultValue;
        }
        return result;
    }

    public void setValue( String name, Object value)
    {
        PropertyI property = getPropertyByName( name);
        property.setValue( value);
    }

    public Object createDefaultInstance( String name)
    {
        PropertyI property = getPropertyByName( name);
        if(property == null)
        {
            Err.pr( printProperties());
            Err.pr( "Num properties: " + properties.size());
            Err.pr( "Host: " + host);
            Err.error( "Could not find a property named <" + name + ">");
        }
        Object result = property.newZeroValue();
        return result;
    }

    public Class getTypeByName( String name)
    {
        Class result = getPropertyByName( name).getType();
        return result;
    }

    public PropertyI getPropertyByName( String name)
    {
        PropertyI result = null;
        for (Iterator<PropertyI> propertyValueIterator = properties.iterator(); propertyValueIterator.hasNext();)
        {
            PropertyI property = propertyValueIterator.next();
            if(property.getName().equals( name))
            {
                result = property;
                break;
            }
        }
        return result;
    }

    public PropertyI getPropertyByNameIgnoreCase( String name)
    {
        PropertyI result = null;
        for (Iterator<PropertyI> propertyValueIterator = properties.iterator(); propertyValueIterator.hasNext();)
        {
            PropertyI property = propertyValueIterator.next();
            if(property.getName().equalsIgnoreCase( name))
            {
                result = property;
                break;
            }
        }
        return result;
    }

    public String printProperties()
    {
        StringBuffer result = new StringBuffer();
        result.append( " - print properties: [");
        List<PropertyI> properties = getProperties();
        for (int i = 0; i < properties.size(); i++)
        {
            PropertyI property = properties.get(i);
            if(i > 0)
            {
                result.append( ", ");
            }
            result.append( property.getName() + ": " + property.getValue());
        }
        result.append( "]");
        return result.toString();
    }

    /*
    public Object sumValues( Object value1, Object value2)
    {
        Object result = null;
        Assert.notNull( value1);
        Assert.notNull( value2);
        Assert.isTrue( value1.getClass().equals( value2.getClass()));
        if(value1 instanceof Money)
        {
            Money money1 = (Money)value1;
            Money money2 = (Money)value2;
            result = money1.add( money2);
        }
        else
        {
            Err.error( "Not yet supported: " + value1.getClass().getName());
        }
        return result;
    }
    */

    public void setDefaultValues( boolean force)
    {
        if(properties.isEmpty())
        {
            Err.pr( SdzNote.READ_USER_PROPERTY,
                "No properties to set default value for for <" + (host).getId() + ">");
        }
        else
        {
            Err.pr( SdzNote.READ_USER_PROPERTY, properties.size() + " properties to set default value for");
            for (Iterator iterator = properties.iterator(); iterator.hasNext();)
            {
                PropertyI prop = (PropertyI)iterator.next();
                Object value = prop.getValue();
                if(value == null || force)
                {
                    //Assert.notNull( prop.defaultValue, "No default value set for <" + prop + ">");
                    /*
                     * Need to think of a good way of doing default properties by type
                     */
                    prop.setValue( prop.newZeroValue());
                }
            }
        }
    }
}
