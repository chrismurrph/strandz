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

import org.strandz.core.prod.Session;

import java.beans.BeanDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * This class specifies how a Cell will look to the programmer at DT, and
 * as such is used by SdzDsgnr.
 *
 * @author Chris Murphy
 */
public class CellBeanInfo extends SimpleBeanInfo
{
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        PropertyDescriptor pd[] = new PropertyDescriptor[6];
        try
        {
            pd[0] = new PropertyDescriptor("name", beanClass);
            pd[1] = new PropertyDescriptor("clazz", beanClass);
            pd[2] = new PropertyDescriptor("refField", beanClass);
            pd[3] = new IndexedPropertyDescriptor("cells", beanClass, "getCells",
                "setCells", "getCell", "setCell");
            /*
            * Note that from xml getAttributes().add() will be called.
            * For independents, setIndependents() will be called. Thus
            * knock on effects will be able to happen (generating ties).
            */
            pd[4] = new PropertyDescriptor("attributes", beanClass);
            pd[5] = new PropertyDescriptor("nonJoinAttributes", beanClass);
        }
        catch(IntrospectionException e)
        {
            Session.error(e);
        }
        return pd;
    }

    public BeanDescriptor getBeanDescriptor()
    {
        BeanDescriptor bd = new BeanDescriptor(beanClass, customizerClass);
        return bd;
    }

    private final static Class beanClass = Cell.class;
    // will have to live with:
    private final static Class customizerClass = CellCustomizer.class;
}
