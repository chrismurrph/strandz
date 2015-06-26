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

//
// factory it from interface import SwingControlInfoImpl;

import org.strandz.core.info.convert.AbstractObjectControlConvert;
import org.strandz.core.info.convert.AbstractDOInterrogate;
import org.strandz.core.info.domain.AbstractOwnFieldMethods;
import org.strandz.core.info.domain.ControlInfo;
import org.strandz.core.info.domain.ItemDescriptor;
import org.strandz.core.info.domain.ItemAdapterI;
import org.strandz.core.widgets.DateComp;
import org.strandz.lgpl.widgets.NonVisualComp;
import org.strandz.lgpl.widgets.ObjComp;
import org.strandz.core.widgets.PrimitiveBooleanComp;
import org.strandz.core.widgets.StrComp;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.BeansUtils;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ControlInfoImpl is the starting point for a hierarchy whereby the user
 * can tell us about all the controls. This class reads this hierarchy and
 * then can be used by core.info to perform certain acts upon the controls
 * that were specified in the hierarchy.
 * <p/>
 * User tells this class which component he would like to use. This class
 * will check that the component has been stored in "strandz.info.impl.xxx.XXXControlInfoImpl"
 * , before returning an id and storing the component for later use, namely
 * invocation of it's methods.
 */
public class ControlSignatures // public for outside testing
{
    public static final int SINGLEROW = 1;
    public static final int MULTIROW = 2;
    private static ControlInfo controlInfo;
    static Map<Class, ItemDescriptor> fcds = new HashMap<Class, ItemDescriptor>();
    private static List lookThruControls = new ArrayList();
    private static List lookThruButStructuralControls = new ArrayList();
    private static List terminatingControls = new ArrayList();
    private static List notTerminatingControls = new ArrayList();
    private static List visibleTerminatingControls = new ArrayList();
    private static List objectControlConverts = new ArrayList();
    static List doInterrogators = new ArrayList();
    private static List systemWidgetPackages = new ArrayList();
    //remove all these *Validated variabled soon enough:
    static boolean fieldsValidated = false;
    private static boolean lookThruControlsValidated = false;
    private static boolean lookThruButStructuralControlsValidated = false;
    private static boolean terminatingControlsValidated = false;
    private static boolean notTerminatingControlsValidated = false;
    private static boolean visibleTerminatingControlsValidated = false;
    private static boolean objectControlConvertsValidated = false;
    static boolean doInterrogatorsValidated = false;
    private static boolean systemWidgetsPackagesValidated = false;
    private static String implementationClassName;
    //end to remove
    //private static Method convertMethod;
    private static int times;
    private static int times1;
    private static int times2;
    private static int timesSetLov;
    private static Class startRecursiveFieldControl;
    private static final String DEFAULT_IMPLEMENTATION = "org.strandz.core.info.impl.swing.SwingControlInfoImpl";
    //private static final String IMPLEMENTATION = "com.seasoft.applic.prodkpi.ProdKpiSignatures";
    private static boolean initialized = false;

    /*#
    * The experiment of doing all init functions here did not work as there is
    * not one place where everything can be initialised from
    */
    public static void init()
    {
        if(!initialized)
        {
//      Err.pr( WombatNote.fieldValidation, "Doing initialization a simpler way!");
//      setObjectControlConverts();
//      setLookThruControls();
//      setLookThruButStructuralControls();
//      setVisibleTerminatingControls();
//      setTerminatingControls();
//      setSystemWidgetsPackages();
//      setFieldsInfo();
            controlInfo.doStartupCode();
            initialized = true;
        }
    }

    private static void setObjectControlConverts()
    {
        if(objectControlConvertsValidated)
        {
            Err.error("Cannot call setObjectControlConverts twice");
        }

        AbstractObjectControlConvert[] l_objectControlConverts = getImpl().getObjectControlConverts();
        for(int i = 0; i <= l_objectControlConverts.length - 1; i++)
        {
            objectControlConverts.add(l_objectControlConverts[i]);
        }
        objectControlConvertsValidated = true;
    }

    static void setDOInterrogators()
    {
        if(doInterrogatorsValidated)
        {
            Err.error("Cannot call setDOInterrogators twice");
        }

        AbstractDOInterrogate[] l_doInterrogators = getImpl().getDOInterrogators();
        for(int i = 0; i <= l_doInterrogators.length - 1; i++)
        {
            doInterrogators.add(l_doInterrogators[i]);
        }
        doInterrogatorsValidated = true;
    }

    private static void setLookThruControls()
    {
        if(lookThruControlsValidated)
        {
            Err.error("Cannot call setLookThruControls twice");
        }

        Class[] l_lookThruControls = getImpl().getLookThruControls();
        for(int i = 0; i <= l_lookThruControls.length - 1; i++)
        {
            lookThruControls.add(l_lookThruControls[i]);
        }
        lookThruControlsValidated = true;
    }

    private static void setLookThruButStructuralControls()
    {
        if(lookThruButStructuralControlsValidated)
        {
            Err.error("Cannot call setLookThruButStructuralControls twice");
        }

        Class[] l_lookThruButStructuralControls = getImpl().getLookThruButStructuralControls();
        for(int i = 0; i <= l_lookThruButStructuralControls.length - 1; i++)
        {
            lookThruButStructuralControls.add(l_lookThruButStructuralControls[i]);
        }
        lookThruButStructuralControlsValidated = true;
    }

    private static void setVisibleTerminatingControls()
    {
        if(visibleTerminatingControlsValidated)
        {
            Err.error("Cannot call setVisibleTerminatingControls twice");
        }

        Class[] l_visibleTerminatingControls = getImpl().getVisibleTerminatingControls();
        for(int i = 0; i <= l_visibleTerminatingControls.length - 1; i++)
        {
            visibleTerminatingControls.add(l_visibleTerminatingControls[i]);
        }
        visibleTerminatingControlsValidated = true;
    }

    private static void setTerminatingControls()
    {
        if(terminatingControlsValidated)
        {
            Err.error("Cannot call setTerminatingControls twice");
        }

        String[] l_terminatingControls = getImpl().getTerminatingControls();
        for(int i = 0; i <= l_terminatingControls.length - 1; i++)
        {
            terminatingControls.add(l_terminatingControls[i]);
        }
        terminatingControlsValidated = true;
    }
    
    private static void setNotTerminatingControls()
    {
        if(notTerminatingControlsValidated)
        {
            Err.error("Cannot call setNotTerminatingControls twice");
        }

        String[] l_notTerminatingControls = getImpl().getNotTerminatingControls();
        for(int i = 0; i <= l_notTerminatingControls.length - 1; i++)
        {
            notTerminatingControls.add(l_notTerminatingControls[i]);
        }
        notTerminatingControlsValidated = true;
    }

    private static void setSystemWidgetsPackages()
    {
        if(systemWidgetsPackagesValidated)
        {
            Err.error("Cannot call setSystemWidgetsPackages twice");
        }

        String[] l_systemWidgetsPackages = getImpl().getSystemWidgetsPackages();
        for(int i = 0; i <= l_systemWidgetsPackages.length - 1; i++)
        {
            systemWidgetPackages.add(l_systemWidgetsPackages[i]);
        }
        systemWidgetsPackagesValidated = true;
    }

    /*
     * If what is set here cannot be class loaded then the default DEFAULT_IMPLEMENTATION
     * will be resorted to, which is what we want at runtime for unit tests that are not
     * dealing with special components. Note that this call may need to be made both for
     * SdzDsgnr and for the applications that may need to call say getters and setters on
     * the special components that come across in the SdzBagI.
     */
    public static void setImplementationClassName( String className)
    {
        implementationClassName = className;
    }

    static ControlInfo getImpl()
    {
        if(controlInfo == null)
        {
            if(implementationClassName != null)
            {
                controlInfo = (ControlInfo) ObjectFoundryUtils.factory( implementationClassName, true);
                if(controlInfo == null)
                {
                    controlInfo = (ControlInfo) ObjectFoundryUtils.factory( DEFAULT_IMPLEMENTATION);
                }
            }
            else
            {
                controlInfo = (ControlInfo) ObjectFoundryUtils.factory( DEFAULT_IMPLEMENTATION);
            }
            init();
        }
        return controlInfo;
    }
    
    public static void setControlInfo( ControlInfo ci)
    {
        controlInfo = ci;
    }

    /**
     * Effectively 1/3 the constructor. Strandz does not itself know
     * about any Controls, other than through this and setTablesInfo method calls.
     */
    static void setFieldsInfo()
    {
        // Err.pr( "inside setFieldsInfo()");
        // Err.stack();
        if(fieldsValidated)
        {
            Err.error("Cannot call setFieldsInfo twice");
        }

        /*
        * ControlSignatures is filled with all it needs by reading ControlInfoImpl.class,
        * which must exist in the right place.
        */
        ItemDescriptor[] l_fcds = getImpl().getItemDescriptors();
        for(int i = 0; i <= l_fcds.length - 1; i++)
        {
            fcds.put(l_fcds[i].controlClass, l_fcds[i]);
        }
        // fcds.ensureCapacity( l_fcds.length);

        validateFields();
    }

    /**
     * Check that ControlInfoImpl contained reasonable values.
     */
    private static void validateFields()
    {
        if(fieldsValidated)
        {
            Err.error("Cannot call validate twice");
        }

        /*
        FieldControlDesriptor:
        get methods return the class to be used by the Control
        get methods do not have any parameters
        */
        Class retClass;
        ItemDescriptor fcd;
        Method getMethod, setMethod;
        Class[] paramTypes;
        Method[] methods;
        for(Iterator e = fcds.entrySet().iterator(); e.hasNext();)
        {
            // get
            fcd = (ItemDescriptor) ((Map.Entry) e.next()).getValue();
            getMethod = fcd.getControlMethod;
            if(getMethod != null)
            {
                retClass = getMethod.getReturnType();
                if(retClass != fcd.classClass)
                {
                    Err.error(
                        "FieldControlDescriptor.getControlMethod doesn't match RET TYPE: " + retClass
                            + ", CLASSCLASS: " + fcd.classClass + " for " + fcd.controlClass);
                }
                paramTypes = getMethod.getParameterTypes();
                if(paramTypes.length != 0)
                {
                    Err.error(
                        "FieldControlDescriptor.getControlMethod should have no parameters");
                }
            }
            else
            {
                //Since MDEF this is ok - it will employ
                //MDateEntryFieldMoreComplexMethods.getMethod()
            }
            // set
            setMethod = fcd.setControlMethod;
            if(setMethod != null)
            {
                retClass = setMethod.getReturnType();
                if(retClass != Void.TYPE)
                {
                    Err.error(
                        "FieldControlDescriptor.setControlMethod must have a void return");
                }
                paramTypes = setMethod.getParameterTypes();
                if(paramTypes.length != 1)
                {
                    Err.error(
                        "FieldControlDescriptor.setControlMethod should have one parameter");
                }
                if(paramTypes[0] != fcd.classClass)
                {
                    Err.error(
                        "FieldControlDescriptor.setControlMethod does not have the right param type");
                }
            }
            else
            {
                //*MoreComplexMethods subclass    
            }

            // controlClass actually contains the get and set methods
            boolean containsSet = false;
            boolean containsGet = false;
            methods = fcd.controlClass.getMethods();
            for(int i = 0; i <= methods.length - 1; i++)
            {
                if(methods[i].equals(getMethod))
                {
                    containsGet = true;
                }
                else if(methods[i].equals(setMethod))
                {
                    containsSet = true;
                }
            }
            /*
            if((containsSet == false)) // || (containsGet == false)
            {
                Err.error("controlClass must contain the specified set method");
            }
            */

            // See if the control can be created with a null constructor
            //Object control = ObjectFoundryUtils.factory(fcd.controlClass);
            // How do they know nothing is there!
            if((fcd.blankControlOperator == ItemDescriptor.EQUALS_OPERATOR)
                && (fcd.blankControlOperand == ItemDescriptor.NULL_KEYWORD))
            {// with this combination, the invocation code we will use later
                // should work
            }
            else if((fcd.blankControlOperator == ItemDescriptor.EQUALS_METHOD)
                && (fcd.blankControlOperand == ItemDescriptor.NULL_STRING))
            {// with this combination, the invocation code we will use later
                // should work
            }
            else if((fcd.blankControlOperator == ItemDescriptor.NOT_USED)
                && (fcd.blankControlOperand == ItemDescriptor.NOT_USED))
            {
            }
            else if((fcd.blankControlOperator == ItemDescriptor.EQUALS_METHOD)
                && (fcd.blankControlOperand == ItemDescriptor.NULL_KEYWORD))
            {// Comp
            }
            else
            {
                Err.error(
                    "Unsupported combination OPERATOR:" + fcd.blankControlOperator
                        + ",OPERAND:" + fcd.blankControlOperand + " for " + fcd.controlClass);
            }
        }
        fieldsValidated = true;
    }

    public static List getFieldControlTypesForLayout()
    {
        List result = new ArrayList();
        if(!fieldsValidated)
        {
            //Err.pr( SdzNote.initCS, "validated not needed");
            setFieldsInfo();
        }
        for(Iterator e = fcds.entrySet().iterator(); e.hasNext();)
        {
            ItemDescriptor fcd = (ItemDescriptor) ((Map.Entry) e.next()).getValue();
            if(fcd.defaultPropertyDisplayName != null)
            {
                result.add(fcd.controlClass);
            }
        }
        return result;
    }

    public static int findDefaultIndex(
        List propertyDescriptors, Class clazz)
    {
        int result = -99;
        String controlsDisplayProperty = null;
        for(Iterator e = fcds.entrySet().iterator(); e.hasNext();)
        {
            ItemDescriptor fcd = (ItemDescriptor) ((Map.Entry) e.next()).getValue();
            if(fcd.controlClass == clazz)
            {
                controlsDisplayProperty = fcd.defaultPropertyDisplayName;
            }
        }

        PropertyDescriptor propertyDescriptor = null;
        for(int i = 0; i < propertyDescriptors.size(); ++i)
        {
            PropertyDescriptor pd = (PropertyDescriptor) propertyDescriptors.get(i);
            if(pd.getPropertyType() != null)
            {
                if(pd.getDisplayName().equals(controlsDisplayProperty))
                {
                    propertyDescriptor = pd;
                    result = i;
                    break;
                }
            }
        }
        // Err.pr( "Found property " + propertyDescriptor + " that has " + controlsDisplayProperty + " at " + result);
        return result;
    }

    public static void acceptAdapters(Map<String, ItemAdapterI> adapters)
    {
        if(getImpl().getFocusMonitor() != null)
        {
            getImpl().getFocusMonitor().acceptAdapters( adapters);
        }
        else
        {
            //Happens if have subclassed SwingControlInfoImpl to not create a FocusMonitor
            //No reason to have a focus monitor if using a NullMoveTracker
        }
    }
    
    public static ItemAdapter getFocused()
    {
        return (ItemAdapter)getImpl().getFocusMonitor().getFocused();
    }

    public static boolean isFocusMonitorable(IdEnum idEnum)
    {
        boolean result = true;
        Class clazz = idEnum.getClazz();
        if(!isFieldControl(clazz))
        {
            result = false;
        }
        else if(idEnum instanceof FieldIdEnum && idEnum.getControl() instanceof NonVisualComp)
        {
            //NonVisualComp comp = (NonVisualComp) idEnum.getControl();
            //Err.pr(SdzNote.fieldValidation, idEnum + " is not a focusable control, " + comp.getName());
            result = false;
        }
        return result;
    }

    /**
     * Sometime estimate making checkFieldControlExists return a boolean or
     * an Object with a boolean in it. Trapping own Exceptions is silly.
     */
    public static boolean isFieldControl(Class clazz)
    {
        boolean result = true;
        try
        {
            result = internalCheckFieldControlExists(clazz, false);
        }
        catch(UnknownControlException ex)
        {
            Err.error("Will never happen");
        }
        return result;
    }

    public static void checkFieldControlExists(Class clazz)
        throws UnknownControlException
    {
        startRecursiveFieldControl = clazz;
        internalCheckFieldControlExists(clazz, true);
    }

    /**
     * If the control class does not exist in the list, but
     * a parent class does, then we must add a new entry onto
     * our HashMap that contains the new class as a key, and
     * the same value.
     */
    private static boolean internalCheckFieldControlExists(Class clazz, boolean thExp)
        throws UnknownControlException
    {
        if(!fieldsValidated)
        {
            //Err.error( "validated not needed");
            setFieldsInfo();
        }

        boolean componentAllowed = false;
        if(fcds.containsKey(clazz))
        {
            componentAllowed = true;
        }
        else
        {
            Class parent = clazz.getSuperclass();
            // Err.pr( "superclass got: " + parent);
            if(parent == Object.class)
            {
                if(thExp)
                {
                    throw new UnknownControlException(
                        startRecursiveFieldControl.getName(), getImpl().getClass());
                }
            }
            else
            {
                if(internalCheckFieldControlExists(parent, thExp))
                {
                    ItemDescriptor value = fcds.get(parent);
                    fcds.put(clazz, value);
                    componentAllowed = true;
                }
            }
        }
        return componentAllowed;
    }

    // id only for debugging
    public static boolean isSystemWidgetPackage(String packParam, String id)
    {
        boolean result = false;
        if(!systemWidgetsPackagesValidated)
        {
            //Err.error( "validated not needed");
            setSystemWidgetsPackages();
        }
        for(Iterator it = systemWidgetPackages.iterator(); it.hasNext();)
        {
            String pack = (String) it.next();
            if(pack.equals(packParam))
            {
                result = true;
                break;
            }
            else if(packParam.indexOf(pack) == 0)
            {
                Err.pr(
                    "[NOT SURE OF THIS] <" + packParam + "> found as a subpackage of "
                        + pack + " for <" + id + ">");
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * JScrollPane and JViewport are examples.
     */
    public static boolean isLookThruControl(Class clazz)
    {
        boolean result = false;
        if(!lookThruControlsValidated)
        {
            //Err.error( "validated not needed");
            setLookThruControls();
        }
        for(Iterator it = lookThruControls.iterator(); it.hasNext();)
        {
            if(clazz == it.next())
            {
                result = true;
                break;
            }
        }
        if(result != true)
        {
            Class parent = clazz.getSuperclass();
            if(parent == Object.class)
            {
                result = false;
            }
            else
            {
                // Err.pr( "\tTo recurse with " + parent);
                result = isLookThruControl(parent);
            }
        }
        return result;
    }

    /**
     * JPanel is a good example of one of these
     */
    public static boolean isLookThruButStructuralControl(Class clazz)
    {
        boolean result = false;
        if(!lookThruButStructuralControlsValidated)
        {
            //Err.error( "validated not needed");
            setLookThruButStructuralControls();
        }
        for(Iterator it = lookThruButStructuralControls.iterator(); it.hasNext();)
        {
            if(clazz == it.next())
            {
                result = true;
                break;
            }
        }
        if(result != true)
        {
            Class parent = clazz.getSuperclass();
            if(parent == Object.class)
            {
                result = false;
            }
            else
            {
                // Err.pr( "\tTo recurse with " + parent);
                result = isLookThruButStructuralControl(parent);
            }
        }
        return result;
    }
    
    private static boolean isNotTerminatingControl( Class clazz)
    {
        boolean result = false;
        for(Iterator it = notTerminatingControls.iterator(); it.hasNext();)
        {
            if(clazz.getName().equals(it.next()))
            {
                result = true;
                break;
            }
        }
        if(result != true)
        {
            //If anything we would want to view the children here, so all
            //children of ROJLabel come out as being able to be seen in the
            //Designer
        }
        return result;
    }
    
    public static boolean isDisplayOnly( Class clazz)
    {
        boolean result;
        if(!terminatingControlsValidated)
        {
            setTerminatingControls();
            setNotTerminatingControls();
        }
        result = isNotTerminatingControl( clazz);        
        return result;
    }

    /**
     * For example a javax.swing.JLabel and any of its subclasses may be
     * specified as being uninteresting when unpacking a
     * component hierarchy.
     */
    public static boolean isTerminatingControl(Class clazz)
    {
        boolean result = false;
        String strClass = clazz.getName();
        if(!terminatingControlsValidated)
        {
            //Err.error( "validated not needed");
            setTerminatingControls();
            setNotTerminatingControls();
        }
        boolean explicitlyFoundFalseCondition = false;
        if(isNotTerminatingControl( clazz))
        {
            explicitlyFoundFalseCondition = true;
            result = false; //being explicit
        }
        if(!explicitlyFoundFalseCondition)
        {
            for(Iterator it = terminatingControls.iterator(); it.hasNext();)
            {
                if(strClass.equals(it.next()))
                {
                    result = true;
                    break;
                }
            }
            if(!result)
            {
                Class parent = clazz.getSuperclass();
                if(parent == Object.class)
                {
                    result = false;
                }
                else
                {
                    // Err.pr( "\tTo recurse with " + parent);
                    result = isTerminatingControl(parent);
                }
            }
        }
        return result;
    }

    /**
     * Complex controls that we want to see and view as a single
     * control, even thou they are made up of loads of controls.
     * JTable and JComboBox are examples.
     */
    public static boolean isVisibleTerminatingControl(Class clazz)
    {
        // Err.pr( "Is " + clazz + " visibleTerminating ?");
        boolean result = false;
        if(!visibleTerminatingControlsValidated)
        {
            //Err.error( "validated not needed");
            setVisibleTerminatingControls();
        }
        for(Iterator it = visibleTerminatingControls.iterator(); it.hasNext();)
        {
            Class terminating = (Class) it.next();
            if(clazz == terminating)
            {
                result = true;
                break;
            }
        }
        if(result != true)
        {
            Class parent = clazz.getSuperclass();
            if(parent == Object.class)
            {
                result = false;
            }
            else
            {
                // Err.pr( "\tTo recurse with " + parent);
                result = isVisibleTerminatingControl(parent);
            }
        }
        if(!result)
        {// Err.pr( "NO");
        }
        else
        {// Err.pr( "YES");
        }
        return result;
    }

    /**
     *
     */
    public static Object getText(IdEnum id)
    {
        if(id.id == IdEnum.TABLE)
        {
            return TableSignatures.getText(id);
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            // Err.pr( "GETTEXT " + id);
            Method method = fcd.getControlMethod;
            if(id.getControl() == null)
            {
                Err.error("Must have a non-null control to call " + method);
            }

            Object text = invokeGetMethod(id, method);
            Class controlNormallyReturns = fcds.get(id.getControl().getClass()).classClass;
            //Err.pr( "control " + id.getControl().getClass().getName() + " normally returns " + controlNormallyReturns.getName());
            if(text != null)
            {
                if(text.getClass() != controlNormallyReturns)
                {
                    text = convertToDisplaySpecialControlType(id, controlNormallyReturns, text);
                }
            }
            return text;
        }
    }

    /**
     * Different purpose to setText(). Here for display purposes in design-time
     * environment
     */
    public static void setLabel(IdEnum id, Object o)
    {
        // boolean needChange = !isEditable( id);
        // if(needChange) setEditable( id, true);
        setText(id, o);
        setToolTipText(id, o);
        // if(needChange) setEditable( id, false);
        if(id.id == IdEnum.TABLE)
        {
            Err.error("Not yet written code to set a table column blank");
        }
        else
        {
            FieldIdEnum fid = (FieldIdEnum) id;
            getOwnFieldMethods(fid).setComponent(fid.getControl(), o);
        }
    }
    
    public static Object getItem( IdEnum id)
    {
        Object result = null;
        if(id.id == IdEnum.TABLE)
        {
            result = TableSignatures.getItem( (TableIdEnum)id);
        }
        else
        {
            Err.error( "Not necessary to call from ControlSignatures as a reference to component is in FieldAttribute and FieldItemAdapter");        
        }
        return result;
    }

    /**
     * TODO - looks like this method is being called too many times.
     * <p/>
     * We may have a null value in the database. Here we don't
     * set the control, which should have the same effect as setting
     * it to null, except we don't get an error for controls that
     * don't accept being set to null (such as JCheckBox).
     */
    public static void setText(IdEnum id, Object o)
    {
        /*
        String compName = IdEnum.getControlName(id.getControl());
        ///Err.pr( "To setText on control <" + compName + ">");
        if(compName.equals( "CenteredLabel for use with date"))
        {
            Err.debug();
        }
        */
        if(id.id == IdEnum.TABLE)
        {
            if(SdzNote.CTV_STRANGE_LOADING.isVisible())
            {
                if(((TableIdEnum)id).getRow() == 0 && id.getColumn() == 3)
                {
                    if(((String)o).contains( "Fri 01"))
                    {
                        Err.stack( "Setting wrong value on a control");                    
                    }
                }
            }
            //Q:
            //Err.pr( "Why setting table cell value by ControlSignatures when have block.ntmii?");
            //A: ObjComps are not done by ntmii - are done by field based method (ie. come thru here)
            TableSignatures.setText(id, o);
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            Method method = fcd.setControlMethod;
            Object args[] = {o};
            /*
            if (
                "frbFlexibilityRadioButtons".equals( compName) //Recipe
                || "frbFlexibilityRadioButtons".equals( compName) //Startup
                || "cbSeniority".equals( compName)
            )
            {
              times++;
              Err.pr( "" );
              Err.pr( "** setText-control " + id.getControl().getClass().getName() );
              Err.pr( "** setText-control-name " + getComponentsName( id.getControl() ) );
              Err.pr( "** setText-method " + method );
              Err.pr( "** setText-value " + o );
              if (o != null)
              {
                Err.pr( "** setText-value's class " + o.getClass().getName() );
                Err.pr( "** setText-value's hashCode " + o.hashCode() );
              }
              Err.pr( "** times: " + times );
              if (times == 0)
              {
                Err.stack();
              }
            }
            else
            {
              //Err.pr( "** not observing " + getComponentsName( id.getControl() ));
            }
            */
            /*
            if(
                id.getControl().getClass().toString().equals("class javax.swing.JComboBox")
                    && method.toString().equals("public void javax.swing.JComboBox.setSelectedItem(java.lang.Object)")
                )
            {
                times++;
                Err.pr("Setting selected item to " + o + " for control " + id + " times " + times);
                if(times == 1)
                {
                    JComboBox cb = (JComboBox)id.getControl();
                    Print.prList( ComponentUtils.getItems( cb), "Already in ComboBox", false);
                }
            }
            */
            String controlName = IdEnum.getControlName( id.getControl());
            //Err.pr( SdzNote.queryNotWorkingNonVisual, "controlName: " + controlName);
            if(SdzNote.QUERY_NOT_WORKING_NON_VISUAL.isVisible() && "tfChristianName".equals( controlName))
            {
                times++;
                Err.pr( "To setText to <" + o + "> times " + times);
                if(times == 0 /*3*/)
                {
                    Err.stack();
                }
            }
            if(method != null)
            {
                Class params[] = method.getParameterTypes();
                Class typeRequiredBySetMethod = params[0];
                //Err.pr( "typeRequiredBySetMethod: " + typeRequiredBySetMethod.getName() + 
                //        " and obj is type " + o.getClass().getName());
                if(o != null || typeRequiredBySetMethod.isPrimitive())
                {
                    if(!typeRequiredBySetMethod.isInstance(o))
                    {
                        // Primitive is ok, don't need to do any special conversions, invoke will handle it
                        // if(!typeRequiredBySetMethod.isPrimitive())
                        {
                            o = convertToDisplaySpecialControlType(id, typeRequiredBySetMethod, o);
                            args[0] = o;
                        }
                    }
                }
            }
            invokeSetMethod( id, method, o);            
            //SelfReferenceUtils.invoke(id.getControl(), method, args);
        }
    }
    
    public static void setToolTipText(IdEnum id, Object o)
    {
        if(id.id == IdEnum.TABLE)
        {
            TableSignatures.setToolTipText(id, o);
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            Method method = fcd.setToolTipMethod;
            Object args[] = {o};
            SelfReferenceUtils.invoke(id.getControl(), method, args);
        }
    }

    private static Object invokeGetMethod(IdEnum fid, Method method)
    {
        Object result;
        if(method != null)
        {
            result = SelfReferenceUtils.invoke(fid.getControl(), method);
        }
        else
        {
            result = getOwnFieldMethods((FieldIdEnum) fid).getMethod(fid.getControl());
        }
        return result;
    }
    
    private static void invokeSetMethod(IdEnum fid, Method method, Object arg)
    {
        if(method != null)
        {
            SelfReferenceUtils.invoke(fid.getControl(), method, arg);
        }
        else
        {
            getOwnFieldMethods((FieldIdEnum) fid).setMethod(fid.getControl(), arg);
        }
    }

    private static void invokeRequestFocusMethod(IdEnum fid, Method method)
    {
        if(method != null)
        {
            SelfReferenceUtils.invoke(fid.getControl(), method);
        }
        else
        {
            getOwnFieldMethods((FieldIdEnum) fid).requestFocusMethod(fid.getControl());
        }
    }

    /**
     * see validateFields()
     * <p/>
     * Gets the value from the control then depends on how
     * EQUALS_OPERATOR etc set. In future may need to be more
     * granular than at the control level. May depend on the
     * class that holds the control.
     */
    public static boolean isTextBlank(IdEnum id)
    {
        if(id.id == IdEnum.TABLE)
        {
            return TableSignatures.isTextBlank(id);
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            Object methodRetVal = invokeGetMethod(id, fcd.getControlMethod);
            /*
            Err.pr("methodRetVal: <" + methodRetVal + "> for " + id.getControl().getClass());
            if(methodRetVal != null)
            {
              Err.pr(" of type " + methodRetVal.getClass().getName());
            }
            */
            boolean retVal = false;
            if((fcd.blankControlOperator == ItemDescriptor.EQUALS_OPERATOR)
                && (fcd.blankControlOperand == ItemDescriptor.NULL_KEYWORD))
            {
                // Err.pr( "== null USED");
                if(methodRetVal == null)
                {
                    // Err.pr( "And null returned");
                    retVal = true;
                }
            }
            else if((fcd.blankControlOperator == ItemDescriptor.EQUALS_METHOD)
                && (fcd.blankControlOperand == ItemDescriptor.NULL_KEYWORD))
            {
                if(methodRetVal == null)
                {
                    retVal = true;
                }
            }
            else if((fcd.blankControlOperator == ItemDescriptor.EQUALS_METHOD)
                && (fcd.blankControlOperand == ItemDescriptor.NULL_STRING))
            {
                // Err.pr( ".equals( \"\") USED");
                if(methodRetVal == null)
                {
                    Err.error("When getting a value from " + id.getControl().getClass()
                        + Utils.NEWLINE
                        + " it is returning null, so use FieldControlDescriptor.EQUALS_OPERATOR and FieldControlDescriptor.NULL_KEYWORD"
                        + Utils.NEWLINE + " for ControlInfo implementation.");
                }
                if(!(methodRetVal instanceof String))
                {
                    // When get this will need to construct before test
                    Err.error("Can't yet test for nulls of type " + methodRetVal.getClass());
                }
                if(methodRetVal.equals(""))
                {
                    // Err.pr( "And \"\" returned");
                    retVal = true;
                }
            }
            else if(fcd.classClass == Boolean.TYPE) // small b boolean, but cast to
            // Boolean done when invoke called
            {
                /*
                * Booleans cannot be null as they have no null representation.
                * They are made false as soon as they are read in with null
                * values, thus they are never blank.
                * Above might be true but doesn't help much when asking the
                * question if a record is blank, so we've re-instated some code
                * here.
                */
                if(methodRetVal.equals(Boolean.FALSE))
                {
                    retVal = true;
                }
                else
                {
                    retVal = false;
                }
            }
            else if(fcd.blankControlOperator == ItemDescriptor.NOT_USED
                && fcd.blankControlOperand == ItemDescriptor.NOT_USED)
            {
                retVal = false;
            }
            else
            {
                Err.error("isTextBlank() not available for " + fcd.controlClass);
            }
            // Err.pr( "Method used was " + fcd.getControlMethod.getName());
            // Err.pr( "hashcode was " + control.hashCode());
            // Err.pr( "name was " + ((javax.swing.JTextField)control).getName());
            //Err.pr("Control " + id.getControl().getClass() + " is blank: " + retVal);
            return retVal;
        }
    }

    public static Object getBlankText(IdEnum id)
    {
        Object result = null;
        if(id.id == IdEnum.TABLE)
        {
            result = TableSignatures.getBlankText(id);
        }
        else
        {
            result = new Object[1];

            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            //Method method = fcd.setControlMethod;
            // Altered here to get Comp too
            if(// (fcd.blankControlOperator == FieldControlDescriptor.EQUALS_OPERATOR) &&
                (fcd.blankControlOperand == ItemDescriptor.NULL_KEYWORD))
            {
                result = null;
            }
            else if((fcd.blankControlOperator == ItemDescriptor.EQUALS_METHOD)
                && (fcd.blankControlOperand == ItemDescriptor.NULL_STRING))
            {
                result = "";
            }
            else if(fcd.classClass == Boolean.TYPE) // small b boolean, but cast to
            // Boolean done when invoke called
            {
                result = Boolean.FALSE;
            }
            else
            {
                // canDo = false;
                Err.error("getTextBlank() not available for " + fcd.controlClass);
            }
        }
        return result;
    }
    
    public static boolean isLabelBlank(IdEnum id)
    {
        return isTextBlank(id);
    }

    public static void setLabelBlankByDsgnr(IdEnum id)
    {
        //would recurse
        //setTextBlank(id);
        if(id.id == IdEnum.TABLE)
        {
            Err.error("Not yet written code to set a table column blank");
        }
        else
        {
            FieldIdEnum fid = (FieldIdEnum) id;
            AbstractOwnFieldMethods fieldMethods = getOwnFieldMethods(fid);
            if(fieldMethods == null)
            {
                Err.error("Could not find field methods for " + fid);
            }
            else
            {
                fieldMethods.blankComponent(fid.getControl());
            }
        }
    }
    
    private static void removeComboItems(IdEnum id)
    {
        if(isLOVable( id))
        {
            setLOV( id, new ArrayList());
        }
    }

    public static void setTextBlank(IdEnum id, boolean removeComboItems)
    {
        if(id.id == IdEnum.TABLE)
        {
            TableSignatures.setTextBlank(id);
        }
        else
        {
            /*
            times++;
            Err.pr( SdzNote.mDateEntryField, "setTextBlank() times " + times);
            if(times == 2)
            {
              Err.debug();
            }
            */
            invokeBlankTextMethod( id, removeComboItems);
        }
    }

    private static int timesInvoke;
    
    private static void invokeBlankTextMethod( IdEnum id, boolean removeComboItems)
    {
        ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
        /*
        timesInvoke++;
        Err.pr( "To invokeBlankTextMethod times " + timesInvoke);
        if(timesInvoke == 2)
        {
            Err.debug();
        }
        */
        Method method = fcd.setControlMethod;
        Object blankText = getBlankText(id);
        invokeSetMethod( id, method, blankText);
        /*
        * Pretty strange state of affairs that a Boolean can be set to "null"
        * but won't report this. Due to fact that doesn't have a null condition,
        * just a false one.
        */
        if(!isTextBlank(id) && fcd.classClass != Boolean.TYPE)
        {
            String lines[] = new String[3];
            lines[0] = "ERROR: Swing is strange - setItemSelected( null) has picked 1st because";
            lines[1] = "the ComboBox is NOT EDITABLE, which is intended for FROZEN nodes";
            lines[2] = "Editable: " + isEnabled(id);
            // new MessageDlg( lines);
            Err.pr(SdzNote.MDATE_ENTRY_FIELD, "ERROR control: " + id.getControl());
            if(method == null)
            {
                Err.pr(SdzNote.MDATE_ENTRY_FIELD, "ERROR method is null");
            }
            else
            {
                Err.pr(SdzNote.MDATE_ENTRY_FIELD, "ERROR method: " + method);
                Err.pr(SdzNote.MDATE_ENTRY_FIELD, "ERROR method class: " + method.getClass().getName());
            }
            Err.pr(SdzNote.MDATE_ENTRY_FIELD, "ERROR arg: <" + blankText + ">");
            Err.pr(SdzNote.MDATE_ENTRY_FIELD, "ERROR editable: " + isEnabled(id));
            Err.error("Blanking text did not work");
        }
        if(removeComboItems)
        {
            removeComboItems( id);
        }
        else
        {
            //Sometimes clearing the combobox of all its items is not what we want
            //to do. With no items you can't set the combobox to anything.
        }
    }
    

    /**
     * Often used when user has filled in a text item with a String, and we
     * need to convert the String to the actual object being stored.
     */
    public static Object convertFromDisplaySpecialControlType(Object object, // usually String
                                                              Class typeRequired) // eg JComponent will need to create
    {
        Object result = null;
        if(!objectControlConvertsValidated)
        {
            //Err.error( "validated not needed");
            setObjectControlConverts();
        }

        boolean found = false;
        for(Iterator it = objectControlConverts.iterator(); it.hasNext();)
        {
            AbstractObjectControlConvert conversion = (AbstractObjectControlConvert) it.next();
            SdzNote.MDATE_ENTRY_FIELD.incTimes();
            Err.pr(SdzNote.MDATE_ENTRY_FIELD, "Cfing " + conversion.typeRequiredByControlAccessors + " with " +
                object.getClass() + " times " + SdzNote.MDATE_ENTRY_FIELD.getTimes());
            if(SdzNote.MDATE_ENTRY_FIELD.getTimes() == 0)
            {
                Err.debug();
            }
            Err.pr(SdzNote.MDATE_ENTRY_FIELD, "And " + conversion.typeOfObject + " with " +
                typeRequired);
            Err.pr(SdzNote.MDATE_ENTRY_FIELD, "");
            if( //object.getClass().isAssignableFrom( conversion.typeRequiredByControlAccessors)
                conversion.typeRequiredByControlAccessors.isAssignableFrom(object.getClass())
                    //conversion.typeRequiredByControlAccessors == object.getClass()
                    && conversion.typeOfObject.isAssignableFrom(typeRequired))
            {
                Err.pr( SdzNote.PROP_EDITOR_CONVERT, "In convertFromDisplaySpecialControlType, calling pullOffScreen for control type required of " +
                  typeRequired + " with arg type " + object.getClass() + ", value: " + object);
                result = conversion.pullOffScreen(object);
                Err.pr( SdzNote.PROP_EDITOR_CONVERT, "When converted what on screen got <" + result + ">");
                found = true;
                break;
            }
        }
        if(!found && typeRequired == String.class)
        {
            result = object.toString();
        }
        else if(!found)
        {
            Err.error(
                "Need an AbstractObjectControlConvert for get method "
                    + "whereby what is required is a " + typeRequired
                    + " where have within a control a " + object.getClass());
        }
        return result;
    }

    /*
    * If control and object are badly matched then do toString() as
    * last resort. Before that will go thru the ObjectControlConverts
    */
    public static Object convertToDisplaySpecialControlType(IdEnum id,
                                                            Class typeRequired,
                                                            Object object)
    {
        Object result = null;
        if(!objectControlConvertsValidated)
        {
            //Err.error( "validated not needed");
            setObjectControlConverts();
        }

        boolean found = false;
        for(Iterator it = objectControlConverts.iterator(); it.hasNext();)
        {
            AbstractObjectControlConvert conversion = (AbstractObjectControlConvert) it.next();
            SdzNote.PROP_EDITOR_CONVERT.incTimes();
            Err.pr( SdzNote.PROP_EDITOR_CONVERT, "Cfing " + conversion.typeRequiredByControlAccessors + " with " +
            typeRequired + " times " + SdzNote.PROP_EDITOR_CONVERT.getTimes());
            if(SdzNote.PROP_EDITOR_CONVERT.getTimes() == 58)
            {
                Err.debug();
            }
            if(object == null)
            {
                Err.pr( SdzNote.PROP_EDITOR_CONVERT, "And " + conversion.typeOfObject + " with " + "NULL");
            }
            else
            {
                Err.pr( SdzNote.PROP_EDITOR_CONVERT, "And " + conversion.typeOfObject + " with " +
                object.getClass());
            }
            Err.pr( SdzNote.PROP_EDITOR_CONVERT, "");
            if(conversion.typeRequiredByControlAccessors.isAssignableFrom( typeRequired)
                && (object == null || conversion.typeOfObject.isAssignableFrom(object.getClass())))
            {
                //Err.pr( "Calling set for control type " + typeRequired + " with arg " + object.getClass());
                result = conversion.pushOntoScreen(object);
                found = true;
                break;
            }
        }
        if(!found && typeRequired == String.class)
        {
            result = object.toString();
        }
        else if(!found)
        {
            if(typeRequired.isAssignableFrom(object.getClass()))
            {
                //Happens regularly for the data-wise RadioButtons controls we use... 
                //Err.pr( "Warning, no object to control converter found for " + object);
                result = object;
            }
            else
            {
                Print.pr("ERR: For (toString()): <" + object + ">");
                Print.pr( //NonVisual controls also come through here
                    "ERR: " + id);
                Print.pr( "ERR: control: " + id.getControl());
                if(id.isTable())
                {
                    Print.pr( "ERR: table: " + id.getTable());
                }
                Err.pr( "<" + typeRequired + "> is not assignable from a <" + object.getClass() + ">");
                Err.error(
                    "ERR: Need an AbstractObjectControlConvert for set method "
                        + "whereby what is required is a <" + object.getClass() + ">"
                        + " where need to have within a control a <" + typeRequired + ">");
            }
        }
        return result;
    }

    private static ItemDescriptor getFCDescriptorForIdEnum(IdEnum id)
    {
        if(id.getControl() == null)
        {
            Err.error( "No control found in id <" + id + "> of type " + id.getClass().getName());
        }
        return getFCDescriptorForClass(id.getControl().getClass());
    }

    /*
    * The need for this method will not occur for a normal field control. Will
    * occur for a renderer on a table. (Thus the call from TableSignatures is
    * the only one that is absolutely necessary).
    */
    static ItemDescriptor getFCDescriptorForClass(Class clazz)
    {
        if(!fieldsValidated)
        {
            //Err.error( "validated not needed");
            setFieldsInfo();
        }

        ItemDescriptor fcd = (ItemDescriptor) fcds.get(clazz);
        if(fcd == null)
        {
            if(isFieldControl(clazz))
            {
                // Now try again, above s/have added to hash table.
                fcd = fcds.get(clazz);
            }
            else
            {
                Err.error(
                    "Need a new FieldControlDescriptor for the control "
                        + clazz.getName());
            }
        }
        return fcd;
    }

    /**
     * Always called when not currently in error. We get the current
     * BG colour and save it. We then set it to RED.
     */
    public static Color setInErrorTrue(IdEnum id, ItemAdapter itemAdapter)
    {
        Color result = null;
        /*
        * Get the current colour so can put it back later
        */
        result = getBGColor(id);
        /*
        * Set to the in-error colour
        */
        setBGColor(id, OtherSignatures.getErrorColor());
        if(id instanceof FieldIdEnum)
        {
            FieldIdEnum fid = (FieldIdEnum) id;
            getOwnFieldMethods(fid).installRClickRestore(fid.getControl(),
                itemAdapter);
        }
        else
        {
            Err.alarm(SdzNote.R_CLICK_RESTORE, "Have not done it for tables");
        }
        return result;
    }

    public static void setInErrorFalse(IdEnum id, Color original)
    {
        setBGColor(id, original);
        if(id.id == IdEnum.TABLE)
        {
        }
        else
        {
            FieldIdEnum fid = (FieldIdEnum) id;
            getOwnFieldMethods(fid).uninstallRClickRestore(fid.getControl());
        }
    }

    public static boolean isLOVable(IdEnum id)
    {
        boolean result = false;
        if(id.id == IdEnum.TABLE)
        {
            Err.error("Not yet implemented!");
            //TableSignatures.isLOVable( id);
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            Method method = fcd.removeAllItemsMethod;
            if(method != null)
            {
                result = true;
            }
        }
        return result;
    }

    public static void setLOV(IdEnum id, List items)
    {
        if(SdzNote.DISPLAY_LOV.isVisible() && (id instanceof FieldIdEnum) && 
                ((FieldIdEnum)id).getControlDescription().contains( "cbSeniority"))
        {
            timesSetLov++;
            Err.pr( "LOV item " + ((FieldIdEnum)id).getControlDescription() + ", being set with count " + items.size() + ", times " + timesSetLov);
            if(timesSetLov == 0 /*5*/)
            {
                Err.stack();
            }
        }
        if(id.id == IdEnum.TABLE)
        {
            TableSignatures.setLOV(id, items);
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            Method method = fcd.removeAllItemsMethod;
            if(method == null)
            {
                Err.error(fcd + " does not have a removeAllItemsMethod");
            }
            if(method != null)
            {
                SelfReferenceUtils.invoke(id.getControl(), method);
            }
            method = fcd.addItemMethod;
            if(method == null)
            {
                Err.error(fcd + " does not have an addItemMethod");
            }
            for(Iterator iter = items.iterator(); iter.hasNext();)
            {
                Object item = iter.next();
                //Object args[] = { item};
                SelfReferenceUtils.invoke(id.getControl(), method, item);
            }
        }
    }

    public static void requestFocus(IdEnum id)
    {
        repositionTo( id);
    }

    public static void repositionTo(IdEnum id)
    {
        Err.pr(SdzNote.CHANGING_NODES_STATES_I_NOT_RECOGNISED, "Repositioning to " + id);
        if(id.id == IdEnum.TABLE)
        {
            TableSignatures.repositionTo(id);
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForClass(
                id.getControl().getClass());
            Method method = fcd.requestFocusMethod;
            invokeRequestFocusMethod(id, method);
            /*
            FieldControlDescriptor fcd = getFCDescriptorForClass(
                id.getControl().getClass());
            Method method = fcd.requestFocusMethod;
            if(method != null)
            {
              // Doesn't allow you to tab anymore (no idea what doing)
              // ((JComponent)control).setFocusCycleRoot(true);
              Err.pr( SdzNote.fieldValidation, "To focus back to " + id.getControl());
              pSelfReference.invoke( id.getControl(), method);
            }
            // == null, for instance when Comp
            */
        }
    }

    /*
    public static void requestFocusField( Object control)
    {
    FieldControlDescriptor fcd = getFCDescriptorForClass( control.getClass());
    Method method = fcd.requestFocusMethod;
    Object empty[] = {};
    //Doesn't allow you to tab anymore (no idea what doing)
    //((JComponent)control).setFocusCycleRoot(true);
    pSelfReference.invoke( control, method, empty);
    }
    */

    public static int getBlankingPolicy(IdEnum id)
    {
        if(id.id == IdEnum.TABLE)
        {
            Err.pr(SdzNote.TABLE_BLANKING_POLICY,
                "Could use tcds to discover editor will use - then "
                    + "have control so fcds can find policy!");
            return TableSignatures.getBlankingPolicy(id);
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            return fcd.blankingPolicy;
        }
    }

    public static Color getDesignTimeColor(Class clazz)
    {
        if(TableSignatures.isTableControl(clazz))
        {
            return TableSignatures.getDesignTimeColor(clazz);
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForClass(clazz);
            return fcd.designTimeColor;
        }
    }

    public static void setEnabled(IdEnum id, boolean b)
    {
        if(SdzNote.QUERY_NOT_WORKING_NON_VISUAL.isVisible()
            && "tfChristianName".equals( IdEnum.getControlName( id.getControl()))
            )
        {
            times1++;
            Err.pr( "Setting enabled to " + b + " for " + id + " times " + times1);
            if(times1 == 0 /*5*/)
            {
                Err.stack();
            }
        }
        if(id.id == IdEnum.TABLE)
        {
            TableSignatures.setEnabled(id, b);
        }
        else
        {
            times2++;
            Err.pr( SdzNote.ENABLEDNESS_REFACTORING, "Setting field enabled to <" + b + "> on id: " + id + " times " + times2);
            if(times2 == 0)
            {
                Err.stack();
            }
            setEnabled( id.getControl(), b);
            if((id.getControl() instanceof JComponent) && isEnabled( id.getControl()) != b)
            {
                Err.error( "Setting enabled to " + b + " for " + 
                        ((JComponent)id.getControl()).getName() + " did not work, BeansUtils.isDesignTime() gives " + BeansUtils.isDesignTime());
            }
        }
    }
    
    static void setEnabled( Object control, boolean b)
    {
        ItemDescriptor fcd = getFCDescriptorForClass( control.getClass());
        Method method = fcd.setEditableControlMethod;
        if(method != null)
        {
            //Object args[] = { new Boolean( b)};
            /*
            times++;
            Err.pr( "$$$$$$ setting editable to " + b +
            " for " + ((Component)id.getControl()).getName() + " times " + times);
            */
            SelfReferenceUtils.invoke(control, method, Boolean.valueOf( b));
        }
    }

    static boolean isEnabled( Object control)
    {
        boolean result;
        ItemDescriptor fcd = getFCDescriptorForClass( control.getClass());
        Method method = fcd.getEditableControlMethod;
        if(method != null)
        {
            result = ((Boolean) SelfReferenceUtils.invoke( control, method)).booleanValue();
        }
        else
        {
            //TODO We will need to implement enabled/not methods for controls such
            //as org.strandz.core.widgets.ObjComp. O/wise always being enabled will
            //cause some tests to fail.
            //Err.error( "No method found to find out whether a <" + id.getControl().getClass().getName() + "> is enabled");
            result = true;
        }
        return result;
    }

    static boolean isEnabled(IdEnum id)
    {
        boolean result = false;
        if(id.id == IdEnum.TABLE)
        {
            result = TableSignatures.isEnabled(id);
        }
        else
        {
            result = isEnabled( id.getControl());
        }
        return result;
    }

    public static void setBGColor(IdEnum id, Object color)
    {
        if(id.id == IdEnum.TABLE)
        {
            TableSignatures.setBGColor(id, color);
        }
        else
        {
            /*
            FieldControlDescriptor fcd = getFCDescriptorForIdEnum( id);
            Method method = fcd.setBGColorMethod;
            if(method != null)
            {
            Object args[] = { color};
            pSelfReference.invoke( id.getControl(), method, args);
            }
            */
            FieldIdEnum fid = (FieldIdEnum) id;
            getOwnFieldMethods(fid).setBGColor(fid.getControl(), color);
        }
    }

    static Color getBGColor(IdEnum id)
    {
        Color result = null;
        if(id.id == IdEnum.TABLE)
        {
            result = TableSignatures.getBGColor(id);
        }
        else
        {
            /*
            FieldControlDescriptor fcd = getFCDescriptorForIdEnum( id);
            Method method = fcd.getBGColorMethod;
            if(method != null)
            {
            Object args[] = {};
            result = ((Color)pSelfReference.invoke( id.getControl(), method, args));
            }
            */
            FieldIdEnum fid = (FieldIdEnum) id;
            result = getOwnFieldMethods(fid).getBGColor(fid.getControl());
        }
        return result;
    }

    public static String getRequiredPrefixForClass(Class clazz)
    {
        String result = null;
        ItemDescriptor fcd = getFCDescriptorForClass(clazz);
        result = fcd.prefix;
        return result;
    }

    public static List getAllFieldControlDescriptors()
    {
        List result = new ArrayList();
        if(!fieldsValidated)
        {
            //Err.error( "validated not needed");
            setFieldsInfo();
        }
        for(Iterator e = fcds.entrySet().iterator(); e.hasNext();)
        {
            ItemDescriptor fcd = (ItemDescriptor) ((Map.Entry) e.next()).getValue();
            /*
            if(fcd.prefix != null)
            {
            result.add( fcd.prefix);
            }
            else
            {
            Err.error( "Should not have a null prefix");
            }
            */
            result.add(fcd);
        }
        return result;
    }

    public static String getNameExcludeRequiredPrefixForControl(Component control)
    {
        String result = control.getName();
        String prefix = getRequiredPrefixForClass(control.getClass());
        if(prefix == null)
        {
            //TODO This validation should happen as read in SwingControlInfoImpl. Don't
            //think there is such a concept - thus create validation for a ControlInfo
            Err.error("A null prefix is not allowed for any control, got one for a " + control.getClass().getName());
        }
        else
        {
            int index = result.indexOf(prefix);
            if(index != -1)
            {
                result = result.substring(index + prefix.length());
            }
            else
            {
                /*
                * Should not be allowing these problems to get into the
                * XML file in the first place, as per note. Need to keep
                * this as an error for when the prefixes are changed and
                * then we load in the XML file. In this situation we hope
                * that this error message is an indicator to manually alter
                * the XML file.
                */
                // nufin
                // This strictness enforced when called from sanityCheck
                // SURE, but sanity check only occurs when read in a file.
                // Flag this to remind us to put it properly in some validation
                Print.prMap(fcds);
                Err.error(org.strandz.lgpl.note.SdzDsgnrNote.NOT_VALIDATING_ON_CONTEXT,
                    "(Probably) XML file needs altered as control <" + control.getClass().getName() + "> with name <" + result + "> inside "
                        + getParentPanel(control).getClass().getName()
                        + " does not have required prefix for type of "
                        + control.getClass().getName() + ", which is <" + prefix + ">");
            }
        }
        return result;
    }

    public static Component getParentPanel(Component comp)
    {
        Component result = null;
        while(result == null && comp != null)
        {
            Component parent = comp.getParent();
            if(ControlSignatures.isLookThruButStructuralControl(parent.getClass()))
            {
                String fullClassName = parent.getClass().getName();
                String packageName = NameUtils.baseOfPackage(fullClassName);
                if(!ControlSignatures.isSystemWidgetPackage(packageName, fullClassName))
                {
                    result = parent;
                }
            }
            comp = parent;
        }
        if(result == null)
        {
            Err.error("Cannot get a parent from panel " + comp.getName());
        }
        return result;
    }

    /*
    public static void addItemFocusListener( IdEnum id, Object itemAdapter)
    {
    if(id.id == IdEnum.TABLE)
    {
    Err.error( "Not implemented");
    }
    else
    {
    FieldIdEnum fid = (FieldIdEnum)id;
    getOwnFieldMethods( fid).addItemFocusListener( fid.getControl(), itemAdapter);
    }
    }
    */

    public static void uninstallListeners(IdEnum id)
    {
        if(id.id == IdEnum.TABLE)
        {
            Err.error("[4]Not implemented");
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            Method method = fcd.removeActionListenerMethod;
            FieldIdEnum fid = (FieldIdEnum) id;
            AbstractOwnFieldMethods methods = getOwnFieldMethods(fid);
            if(methods != null)
            {
                methods.uninstallListeners(fid.getControl(), method);
            }
        }
    }

    /*
    public static void installItemValidator(
    IdEnum id, MoveBlockI pointer)
    {
    if(id.id == IdEnum.TABLE)
    {
    //nufin - we are listening all the time to JTable anyway. (And we had
    //to do this as a direct result of keys not being able to be
    //intercepted in the same way that mouse clicks can be).
    Err.error( "Might be required for non-Swing implementation");
    }
    else
    {
    //This is only done to give us a convenient way of determining
    //point at which user moved off a field. (Knowing which field are
    //on is done with a focus listener).
    FieldIdEnum fid = (FieldIdEnum)id;
    getOwnFieldMethods( fid).installItemValidator(
    fid.getControl(),
    pointer);
    }
    }
    */

    public static void installListeners(IdEnum id, Object itemAdapter)
    {
        uninstallListeners(id);
        if(id.id == IdEnum.TABLE)
        {
            Err.error("Might be required for non-Swing implementation");
        }
        else
        {
            ItemDescriptor fcd = getFCDescriptorForIdEnum(id);
            Method method = fcd.addActionListenerMethod;
            FieldIdEnum fid = (FieldIdEnum) id;
            AbstractOwnFieldMethods methods = getOwnFieldMethods(fid);
            if(methods != null)
            {
                methods.installListeners(fid.getControl(), itemAdapter, method);
            }
        }
    }

    public static NonVisualComp createNonVisualAlternative(IdEnum id)
    {
        NonVisualComp result = null;
        checkRecognisableControl(id);
        Class controlNormallyReturns = ((ItemDescriptor) fcds.get(id.getControl().getClass())).classClass;
        if(controlNormallyReturns == String.class)
        {
            result = new StrComp( IdEnum.getControlName( id.getControl()));
        }
        else if(controlNormallyReturns == Date.class)
        {
            result = new DateComp( IdEnum.getControlName( id.getControl()));
        }
        else if(controlNormallyReturns == Object.class)
        {
            result = new ObjComp( IdEnum.getControlName( id.getControl()));
        }
        else if(controlNormallyReturns == Boolean.TYPE)
        {
            result = new PrimitiveBooleanComp( IdEnum.getControlName( id.getControl()));
        }
        else
        {
            Err.error("Need to create a new non-applichousing alternative (NonVisualComp) that handles content of type " + controlNormallyReturns);
        }
        return result;
    }

    public static void checkRecognisableControl(IdEnum id)
    {
        Assert.notNull( id.getControl(), "Cannot set FieldAttribute control to null - instead create a StemAttribute");
        if(!ControlSignatures.isFieldControl(id.getControl().getClass()))
        {
            Err.error(
                "Do not have a FieldControlDescriptor for even a superclass of "
                    + id.getControl().getClass().getName());
        }
        FieldIdEnum fid = (FieldIdEnum) id;
        getOwnFieldMethods(fid).doRuntimeChanges(fid.getControl());
    }

    /**
     * Note using different method to that used in ControlsTreeModel. Will have to
     * reconcile the two and use Context to get user parameters.
     */
    public static List findControlsInside(Container container, List controls)
    {
        // Err.pr( "findControlsInside(): " + container.getName());
        Assert.notNull( container);
        for(int i = 0; i <= container.getComponentCount() - 1; i++)
        {
            Component component = container.getComponent(i);
            Class clazz = component.getClass();
            if(isFieldControl(clazz))
            {
                if(!isTerminatingControl(clazz))
                {
                    controls.add(component);
                }
            }
            else
            {
                controls = findControlsInside((Container) component, controls);
            }
        }
        return controls;
    }

    private static AbstractOwnFieldMethods getOwnFieldMethods(FieldIdEnum fid)
    {
        ItemDescriptor fcd = getFCDescriptorForIdEnum(fid);
        AbstractOwnFieldMethods ownFieldMethods = fcd.ownFieldMethods;
        return ownFieldMethods;
    }
    /*
    public static Object convert( Object value, Class to)
    {
    if(bii == null)
    {
    bii = (SwingControlInfoImpl)pSelfReference.factory( "strandz.info.impl.swing.SwingControlInfoImpl");
    }
    if(convertMethod == null)
    {
    convertMethod = bii.getStringToTypeConvertMethod();
    }
    Object args[] = { value, to};
    Object result = pSelfReference.invoke( bii, convertMethod, args);
    return result;
    }
    */

    /**
     * This only really used for demoing/debugging. (I liked the fact
     * that an error will ensue if try to use a control that you don't
     * want to be supported).
     * In reality will have
     * one ControlInfoImpl file that handles all controls that might
     * possibly use.
     */
    /*
    public static void setControlInfoImpl( String s)
    {
    Err.pr( "In setControlInfoImpl to " + s);
    s = "strandz.info.impl" + s;
    String cf = "class " + s;
    boolean needChange = false;
    if(bii == null)
    {
    needChange = true;
    }
    else if(!cf.equals( bii.getClass().toString()))
    {
    needChange = true;
    }
    if(needChange)
    {
    //Err.pr( "Setting control info to class file " + s);
    bii = (ControlInfo)pSelfReference.factory( s);
    fieldsValidated = false;
    tablesValidated = false;
    setFieldsInfo();
    setTablesInfo();
    }
    else
    {
    //Err.pr( "Didn't need to change");
    //Err.pr( cf);
    //Err.pr( bii.getClass().toString());
    }
    }
    */
}
