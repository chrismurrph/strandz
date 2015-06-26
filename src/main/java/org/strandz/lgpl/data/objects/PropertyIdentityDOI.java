package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.NameableI;
import org.strandz.lgpl.util.TypeableI;
import org.strandz.lgpl.util.IdentifierI;

/**
 * Remember that this is a general interface! Use AccountDOI if you want
 * to use something from prodkpi.
 * 
 * User: Chris
 * Date: 02/02/2009
 * Time: 1:50:38 PM
 */
public interface PropertyIdentityDOI extends TypeableI, IdentifierI
{
    public String getDisplayName();
    public boolean isToBeAveraged();
    public void setToBeAveraged( boolean b);
}
