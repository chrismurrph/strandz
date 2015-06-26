package org.strandz.lgpl.data.objects;

import java.util.List;
import java.util.ArrayList;

/**
 * Used by both Merge and...
 *
 * User: Chris
 * Date: 27/02/2009
 * Time: 2:56:22 AM
 */
public class PropertyMemento
{
    private List<PropertyI> properties;

    public PropertyMemento()
    {
        properties = new ArrayList<PropertyI>();
    }

    public PropertyMemento( List<PropertyI> properties)
    {
        this.properties = properties;
    }

    /* Now using two mementos
    public PropertyMemento( PropertyI sourceProperty, PropertyI destinationProperty)
    {
        properties = new ArrayList<PropertyI>();
        properties.add( sourceProperty);
        properties.add( destinationProperty);
    }
    */

    public List<PropertyI> getProperties()
    {
        return properties;
    }
}
