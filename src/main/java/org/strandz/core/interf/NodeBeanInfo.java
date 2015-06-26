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
 * This class specifies how a Node will look to the programmer at DT, and
 * as such is used by SdzDsgnr.
 *
 * @author Chris Murphy
 */
public class NodeBeanInfo extends SimpleBeanInfo
{
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        PropertyDescriptor rv[] = new PropertyDescriptor[22];
        try
        {
            rv[0] = new PropertyDescriptor("name", beanClass);
            rv[1] = new PropertyDescriptor("title", beanClass);
            rv[2] = new PropertyDescriptor("cell", beanClass);
            rv[3] = new PropertyDescriptor("tableControl", beanClass);
            rv[4] = new PropertyDescriptor("enterQuery", beanClass);
            rv[5] = new PropertyDescriptor("executeQuery", beanClass);
            rv[6] = new PropertyDescriptor("executeSearch", beanClass);
            rv[7] = new PropertyDescriptor("update", beanClass);
            rv[8] = new PropertyDescriptor("insert", beanClass);
            rv[9] = new PropertyDescriptor("remove", beanClass);
            rv[10] = new PropertyDescriptor("post", beanClass);
            rv[11] = new PropertyDescriptor("commitReload", beanClass);
            rv[12] = new PropertyDescriptor("commitOnly", beanClass);
            rv[13] = new PropertyDescriptor("previous", beanClass);
            rv[14] = new PropertyDescriptor("next", beanClass);
            rv[15] = new PropertyDescriptor("setRow", beanClass);
            rv[16] = new PropertyDescriptor("ignoredChild", beanClass);
            rv[17] = new PropertyDescriptor("cascadeDelete", beanClass);
            rv[18] = new PropertyDescriptor("focusCausesNodeChange", beanClass);
            rv[19] = new PropertyDescriptor("editInsertsBeforeCommit", beanClass);
            rv[20] = new PropertyDescriptor("alreadyBeenCustomized", beanClass);
            rv[21] = new IndexedPropertyDescriptor("independents", beanClass,
                "getIndependents", "setIndependents", "getIndependent",
                "setIndependent");
        }
        catch(IntrospectionException e)
        {
            Session.error(e);
        }
        return rv;
    }

    public BeanDescriptor getBeanDescriptor()
    {
        BeanDescriptor bd = new BeanDescriptor(beanClass, customizerClass);
        return bd;
    }

    private final static Class beanClass = Node.class;
    // will have to live with:
    private final static Class customizerClass = NodeCustomizer.class;
}
