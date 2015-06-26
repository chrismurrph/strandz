package org.strandz.lgpl.data.objects;

import java.util.List;

/**
 * DOs that implement this signal that users can make up their own DO fields at
 * runtime. For instance a user might want to set up a new account called 'sweets'.
 *
 * In Strandz terms the user would be creating an Attribute - as well as a DOAdapter.
 * Attribute.endUserRuntime will be true to signify that reading and writing from the
 * DO is not to be be thru fields or methods, but thru this interface.
 *
 * User: Chris
 * Date: 24/11/2008
 * Time: 8:56:20 PM
 */
public interface SdzPropertySetableI
{
    boolean propertyExists( String propertyName);
    boolean propertyExists( String propertyName, boolean inChildren);
    Object getValue( String propertyName);
    //boolean isToBeAveraged( String propertyName);
    /**
     * Called by scripting languages
     */
    Object getAmount( String name);
    Object getValue( String name, Object defaultValue);
    void setValue( String name, Object value);
    void addProperty( PropertyI property);
    void removeProperty( PropertyI property);
    List<PropertyI> getProperties();
    int getId();
    Object copyConstructValue( Object source);
    Object copyConstruct( boolean childless);
    void setDefaultValues( boolean force);
}
