package org.strandz.util;

import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.lgpl.util.Err;

/**
 * User: Chris
 * Date: 18/01/2009
 * Time: 1:55:09 AM
 */
public class DebugUtil
{
    /**
     * Shows that whether changed works just fine over
     * the commit (or post) cycle. Actually the cycle is when setDisplay()
     * is called, which is whenever a user is moving away from a record.
     */
    public static void debugAttributeChange( RuntimeAttribute attribute)
    {
        Object obj = attribute.getItemValue();
        if(obj != null)
        {
            Err.pr( "In " + attribute.getName() + " we have a <" + obj.getClass().getName() + ">");
            Err.pr( "toString: <" + obj + ">");
            if(attribute.hasChanged())
            {
                Err.pr( "Has changed and previous value was <" + attribute.getB4ImageValue() + ">");
            }
        }
        else
        {
            Err.pr( attribute.getName() + " is null");
        }
        Err.pr( "");
    }
}
