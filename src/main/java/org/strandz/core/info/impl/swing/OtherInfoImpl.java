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
package org.strandz.core.info.impl.swing;

import org.strandz.core.info.domain.NotNullaryConstructorDescriptor;
import org.strandz.core.info.domain.OtherInfo;
import org.strandz.core.applichousing.NodeStatusBar;
import org.strandz.core.applichousing.PictToolBarController;

import java.awt.Color;
import java.lang.reflect.Constructor;

// import data.fishbowl.objects.util.Timespace;

/**
 * May turn into a class that is all about particular
 * types.
 */
public class OtherInfoImpl extends OtherInfo
{
    private Class type;
    private Constructor constructor;
    private Object[] initargs = new Object[1];
    private NotNullaryConstructorDescriptor nncd;
    private NotNullaryConstructorDescriptor[] nncds = new NotNullaryConstructorDescriptor[2];

    public NotNullaryConstructorDescriptor[] getNotNullaryConstructorDescriptors()
    {
        Constructor[] cons = Boolean.class.getConstructors();
        constructor = cons[0];
        initargs[0] = Boolean.valueOf( false);
        type = Boolean.class;
        nncd = new NotNullaryConstructorDescriptor(type, constructor, initargs);
        nncds[0] = nncd;
        cons = Integer.class.getConstructors();
        constructor = cons[0];
        initargs[0] = new Integer(-1);
        type = Integer.class;
        nncd = new NotNullaryConstructorDescriptor(type, constructor, initargs);
        nncds[1] = nncd;
        return nncds;
    }

    public Class getDefaultPhysicalControllerType()
    {
        return PictToolBarController.class;
    }

    public Class getDefaultStatusBarType()
    {
        return NodeStatusBar.class;
    }

    /*
    * Used String[] to stop package recursion
    */
    public String[] getControllerInterfaces()
    {
        String[] cis = new String[2];
        cis[0] = "org.strandz.core.interf.OneRowSdzBag";
        cis[1] = "org.strandz.core.applichousing.SdzBag";
        return cis;
    }

    public String[] getRareDataFields()
    {
        String[] fields = new String[8];
        fields[0] = "class";
        fields[1] = "RO";
        //Cayenne fields we do not need to see:
        fields[2] = "dataContext";
        fields[3] = "objEntity";
        fields[4] = "objectContext";
        fields[5] = "objectId";
        fields[6] = "persistenceState";
        fields[7] = "snapshotVersion";
        return fields;
    }

    public Object getErrorColor()
    {
        return Color.RED;
    }
    /*
    public Method getStringToTypeConvertMethod()
    {
    Method method = null;
    args2[0] = java.lang.Object.class;
    args2[1] = java.lang.Class.class;
    try{
    method = getClass().getMethod("convert", args2);
    } catch (Exception ex) {
    Err.error("Missing method: " + ex);
    }
    return method;
    }
    */

    /***
     Debugging code:
     Method[] methods = javax.swing.JTextField.class.getMethods();
     Err.pr("ARGS");
     for(int i=0; i<=args1.length-1; i++)
     {
     Err.pr( args1[i]);
     }
     for(int i=0; i<=methods.length-1; i++)
     {
     Err.pr( methods[i].getReturnType() + " " + methods[i].getName());
     Class classes[] = methods[i].getParameterTypes();
     Err.pr("[");
     for(int j=0; j<=classes.length-1; j++)
     {
     System.out.print( "\t");
     System.out.print( classes[j]);
     Err.pr( "");
     }
     Err.pr("]");
     }

     ***/
}
