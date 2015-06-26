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

import org.strandz.lgpl.util.Err;

import java.beans.BeanDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * This class specifies how a OneRowSdzBag will look to the programmer at
 * DT, and as such is used by SdzDsgnr.
 *
 * @author Chris Murphy
 */
public class OneRowSdzBagBeanInfo extends SimpleBeanInfo
{
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        PropertyDescriptor pd[] = new PropertyDescriptor[3];
        try
        {
            pd[0] = new PropertyDescriptor("name", beanClass);
            // pd[1] = new PropertyDescriptor( "pane", beanClass);
            pd[1] = new IndexedPropertyDescriptor("panes", beanClass, "getPanes",
                "setPanes", "getPane", "setPane");
            pd[2] = new IndexedPropertyDescriptor("nodes", beanClass, "getNodes",
                "setNodes", "getNode", "setNode");
        }
        catch(IntrospectionException e)
        {
            Err.error(e);
        }
        return pd;
    }

    public BeanDescriptor getBeanDescriptor()
    {
        BeanDescriptor bd = new BeanDescriptor(beanClass, customizerClass);
        return bd;
    }

    /**
     * This useless as SimpleBeanInfo does this already, and it is being
     * ignored both times.
     * Don't want to be able to <add> a pane.
     * Perhaps we are still getting the <add> method b/cause JPanelBeanInfo
     * has an <add> method.
     * My understanding of the JavaBeans spec must be wrong.
     */
    public MethodDescriptor[] getMethodDescriptors()
    {
        return null;
    }

    private final static Class beanClass = OneRowSdzBag.class;
    private final static Class customizerClass = OneRowSdzBagCustomizer.class;
}
