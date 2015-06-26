package org.strandz.lgpl.data.objects;

/**
 * User: Chris
 * Date: 15/02/2009
 * Time: 12:45:38 AM
 */
public interface PropertyI
{
    PropertyIdentityDOI getPropertyIdentity();
    PropertyI copyConstruct( int hostId);
    String getName();
    Class getType();
    Object getValue();    
    void setValue( Object value);
    Object newZeroValue();
    Object zeroValue();
    void addPropertyValueChangedListener( PropertyValueChangedListener l);
}
