/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.util;

import java.beans.BeanInfo;
import java.beans.Beans;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

/**
 * This static class Beans wrapper allows us to modify Beans methods easily.
 * Intoduced when needed to find a way around Beans.setDesignTime()
 * not working with Web Start. It may just be a bit of a performance hit
 * to not use Beans, or there may be real trouble! For examples of why
 * Beans.setDesignTime() needed by Strandz see the callers of these
 * methods. The only code that will be troubled will be Swing code that
 * does similar stuff to the examples - and haven't done a text search
 * so don't really know what the Swing code does.
 * This class provides a convenient place to switch backwards and
 * forwards between using and not using the real Beans.
 *
 * @author Chris Murphy
 */
public class BeansUtils
{
    private static boolean dt;
    private static boolean oldValue;
    /*
     * Set away from NATIVE_WAY for RuntheRoster.rosterWorkersStrand() not to stack
     * saying:
     * 'Setting enabled to true for frbFlexibilityRadioButtons did not work, BeansUtils.isDesignTime() gives true' 
     */
    private static final boolean NATIVE_WAY = false;
    private static final boolean DEBUG = false;

    public static boolean isDesignTime()
    {
        return getValue();
    }

    public static void resetDesignTime()
    {
        setDesignTime(oldValue);
    }

    public static void setDesignTime(boolean b)
    {
        if(getValue() != b)
        {
            oldValue = getValue();
            if(DEBUG) Err.pr("Changing DT to " + b);
            setValue(b);
        }
    }

    private static boolean getValue()
    {
        boolean result = dt;
        if(NATIVE_WAY)
        {
            result = Beans.isDesignTime();
        }
        return result;
    }

    private static void setValue(boolean b)
    {
        if(NATIVE_WAY)
        {
            Beans.setDesignTime(b);
        }
        else
        {
            dt = b;
        }
    }

    /**
     * This is recursive, but prolly unnecessarily so!
     */
    public static void getPropertyDescriptors( Class clazz, List<PropertyDescriptor> result)
    {
        BeanInfo bi = null;
        if(clazz == Object.class)
        {
            return;
        }
        try
        {
            //Err.pr( "Trying to getBeanInfo for a cell of dubious class: " + clazz);
            bi = Introspector.getBeanInfo( clazz);
        }
        catch(IntrospectionException ex)
        {
            Err.error(ex);
        }
        //Don't need to be recursive:
        //getPropertyDescriptors( clazz.getSuperclass(), result);
        PropertyDescriptor pds[] = bi.getPropertyDescriptors();
        List<PropertyDescriptor> list = Arrays.asList( pds);
        for (int i = 0; i < list.size(); i++)
        {
            PropertyDescriptor propertyDescriptor = list.get(i);
            if(!result.contains( propertyDescriptor))
            {
                result.add( propertyDescriptor);
            }
        }
    }
}
