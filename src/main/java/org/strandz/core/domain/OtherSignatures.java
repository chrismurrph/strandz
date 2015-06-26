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
package org.strandz.core.domain;

import org.strandz.core.info.domain.NotNullaryConstructorDescriptor;
import org.strandz.core.info.domain.OtherInfo;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.note.SdzNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtherSignatures
{
    private static boolean notNullaryConstructorsSet = false;
    private static OtherInfo oi;
    private static HashMap nncds = new HashMap(); // NotNullConstructorDescriptor
    private static List controllerInterfaces = new ArrayList();
    private static boolean controllerInterfacesValidated = false;
    private static Class defaultPhysicalControllerType;
    private static boolean defaultPhysicalControllerTypeValidated = false;
    private static Class defaultStatusBarType;
    private static boolean defaultStatusBarTypeValidated = false;
    private static List rareDataFields = new ArrayList();
    private static boolean rareDataFieldsValidated = false;
    private static Object errorColor;
    private static boolean gotErrorColor = false;
    private static String IMPLEMENTATION = "org.strandz.core.info.impl.swing.OtherInfoImpl";

    private static void setControllerInterfaces()
    {
        if(controllerInterfacesValidated)
        {
            Err.error("Cannot call setControllerInterfaces twice");
        }
        if(oi == null)
        {
            oi = (OtherInfo) ObjectFoundryUtils.factory(
                IMPLEMENTATION);
        }

        String[] l_controllerInterfaces = oi.getControllerInterfaces();
        for(int i = 0; i <= l_controllerInterfaces.length - 1; i++)
        {
            Err.pr( SdzNote.GENERIC, "sbI being assigned at posn " + i + " is " + l_controllerInterfaces[i]);
            controllerInterfaces.add(l_controllerInterfaces[i]);
        }
        controllerInterfacesValidated = true;
    }

    private static void setErrorColor()
    {
        if(gotErrorColor)
        {
            Err.error("Cannot call setControllerInterfaces twice");
        }
        if(oi == null)
        {
            oi = (OtherInfo) ObjectFoundryUtils.factory(
                IMPLEMENTATION);
        }
        errorColor = oi.getErrorColor();
        gotErrorColor = true;
    }

    private static void setDefaultPhysicalControllerType()
    {
        if(defaultPhysicalControllerTypeValidated)
        {
            Err.error("Cannot call setDefaultPhysicalControllerType twice");
        }
        if(oi == null)
        {
            oi = (OtherInfo) ObjectFoundryUtils.factory(
                IMPLEMENTATION);
        }
        defaultPhysicalControllerType = oi.getDefaultPhysicalControllerType();
        defaultPhysicalControllerTypeValidated = true;
    }

    private static void setDefaultStatusBarType()
    {
        if(defaultStatusBarTypeValidated)
        {
            Err.error("Cannot call setDefaultStatusBarType twice");
        }
        if(oi == null)
        {
            oi = (OtherInfo) ObjectFoundryUtils.factory(
                IMPLEMENTATION);
        }
        defaultStatusBarType = oi.getDefaultStatusBarType();
        defaultStatusBarTypeValidated = true;
    }

    /**
     * Effectively the constructor. Note that this has nothing to do
     * with control's signatures
     */
    public static void setNotNullConstructors()
    {
        if(notNullaryConstructorsSet)
        {
            // Err.error( "Cannot call setNotNullConstructors twice");
            // Err.pr( "WARNING: setNotNullConstructors called twice (due XMLEncoder)");
            return;
        }
        if(oi == null) // if haven't already explicitly set oi
        // (see ControlSignatures.setControlInfoImpl()
        // when need to implement similar).
        {
            oi = (OtherInfo) ObjectFoundryUtils.factory(
                IMPLEMENTATION);
        }

        NotNullaryConstructorDescriptor[] l_nncds = oi.getNotNullaryConstructorDescriptors();
        for(int i = 0; i <= l_nncds.length - 1; i++)
        {
            nncds.put(l_nncds[i].type, l_nncds[i]);
        }
        notNullaryConstructorsSet = true;
    }

    private static void setRareDataFields()
    {
        if(rareDataFieldsValidated)
        {
            Err.error("Cannot call setRareDataFields twice");
        }
        if(oi == null)
        {
            oi = (OtherInfo) ObjectFoundryUtils.factory(
                IMPLEMENTATION);
        }

        String[] l_rareDataFields = oi.getRareDataFields();
        for(int i = 0; i <= l_rareDataFields.length - 1; i++)
        {
            rareDataFields.add(l_rareDataFields[i]);
        }
        rareDataFieldsValidated = true;
    }

    public static Object constructWithParams(Class type)
    {
        Object result;
        NotNullaryConstructorDescriptor nncd = (NotNullaryConstructorDescriptor) nncds.get(
            type);
        if(nncd == null)
        {
            Err.error("Do not have a NotNullConstructorDescriptor " + "for " + type);
        }
        result = ObjectFoundryUtils.factory(nncd.constructor, nncd.initargs);
        return result;
    }

    public static Object getErrorColor()
    {
        if(!gotErrorColor)
        {
            setErrorColor();
        }
        return errorColor;
    }

    public static Object getDefaultControllerInterface()
    {
        Object result = null;
        if(!controllerInterfacesValidated)
        {
            setControllerInterfaces();
        }
        if(controllerInterfaces.size() == 0)
        {
            Err.error("No ControllerInterfaces");
        }
        result = ObjectFoundryUtils.factory((String) controllerInterfaces.get(0));
        return result;
    }

    public static List getControllerInterfaces()
    {
        if(!controllerInterfacesValidated)
        {
            setControllerInterfaces();
        }
        if(controllerInterfaces.size() == 0)
        {
            Err.error("No ControllerInterfaces");
        }
        return controllerInterfaces;
    }

    public static Class getDefaultPhysicalControllerType()
    {
        Object result = null;
        if(!defaultPhysicalControllerTypeValidated)
        {
            setDefaultPhysicalControllerType();
        }
        if(defaultPhysicalControllerType == null)
        {
            Err.error("No defaultPhysicalControllerType");
        }
        return defaultPhysicalControllerType;
    }

    public static Class getDefaultStatusBarType()
    {
        Object result = null;
        if(!defaultStatusBarTypeValidated)
        {
            setDefaultStatusBarType();
        }
        if(defaultStatusBarType == null)
        {
            Err.error("No defaultStatusBarType");
        }
        return defaultStatusBarType;
    }

    public static boolean isRareDataField(String s)
    {
        boolean result = false;
        if(!rareDataFieldsValidated)
        {
            setRareDataFields();
        }
        if(rareDataFields == null)
        {
            Err.error("No rareDataFields");
        }

        int idx = rareDataFields.indexOf(s);
        if(idx != -1)
        {
            result = true;
        }
        return result;
    }
}
