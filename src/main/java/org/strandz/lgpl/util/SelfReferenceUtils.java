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
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This static class provides an API to the Reflection API, enabling us to interact
 * with objects that were unknown at compile time.
 *
 * @author Chris Murphy
 */
public class SelfReferenceUtils
{
    private static int times;
    private static Object empty[] = {};

    /**
     * Checks to see if you can construct a class without needing
     * to provide any arguments
     *
     * @param clazz the class to evaluate
     * @return true if the passed in class is a nullary
     */
    public static boolean hasNullConstructor(Class clazz)
    {
        boolean result = true;
        Class args[] = {};
        try
        {
            Constructor con = clazz.getDeclaredConstructor(args);
        }
        catch (NoSuchMethodException ex)
        {
            result = false;
        }
        return result;
    }

    /**
     * Checks to see if a class is an interface
     *
     * @param clazz the class to evaluate
     */
    public static boolean isInterface(Class clazz)
    {
        return clazz.isInterface();
    }

    /**
     * Don't just go up the class hierarchy, but look out and up at the interfaces.
     */
    public static boolean implementsInterface( Class testType, Class seeIfImplementsType)
    {
        boolean result = false;
        if(testType != null)
        {
            /*
            times++;
            Err.pr( "To see if " + testType.getName() + " implements " + seeIfImplementsType.getName() + ", times " + times);
            if(times == 0)
            {
                Err.debug();
            }
            */
            if(testType.equals( seeIfImplementsType))
            {
                return true;
            }
            Class[] interfaces = testType.getInterfaces();
            for(int i = 0; i < interfaces.length; i++)
            {
                if(interfaces[i].equals( seeIfImplementsType))
                {
                    result = true;
                    break;
                }
            }
            if(!result)
            {
                interfaces = testType.getInterfaces();
                for(int i = 0; i < interfaces.length; i++)
                {
                    Class[] superInterfaces = interfaces[i].getInterfaces();
                    for(int j = 0; j < superInterfaces.length; j++)
                    {
                        result = implementsInterface( superInterfaces[j], seeIfImplementsType);
                        if(result)
                        {
                            break;
                        }
                    }
                }
                if(!result)
                {
                    return implementsInterface( testType.getSuperclass(), seeIfImplementsType);
                }
            }
        }
        return result;
    }

    /**
     * Checks to see if a class is known about by the current classloader
     *
     * @param className the name of the class to investigate
     * @return true is classsName is a class
     */
    public static boolean isALoadedClass(String className)
    {
        return isALoadedClass(className, null);
    }

    /**
     * Checks to see if a class is known about by the passed in classloader
     *
     * @param className the name of the class to investigate
     * @param cl        the classloader which might know about the class
     * @return true is classsName is a class
     */
    public static boolean isALoadedClass(String className, ClassLoader cl)
    {
        boolean result = true;
        Class clazz = null;
        try
        {
            if (cl != null)
            {
                clazz = Class.forName(className, true, cl);
            }
            else
            {
                clazz = Class.forName(className);
            }
        }
        catch (NoClassDefFoundError e1)
        {
            result = false;
        }
        catch (ClassNotFoundException e2)
        {
            result = false;
        }
        return result;
    }

    /**
     * Determines if className represents a class that can be instantiated.
     * It must be a class and not an interface or abstract class
     *
     * @param className
     * @return true is classsName can be instantiated
     */
    public static boolean isInstantiable(String className)
    {
        return isInstantiable(className, null);
    }

    /**
     * Determines if className represents a class that can be instantiated.
     * It must be a class and not an interface or abstract class
     *
     * @param className the name of the class
     * @param cl        classloader
     * @return true if className can be instantiated
     */
    public static boolean isInstantiable(String className, ClassLoader cl)
    {
        boolean result = true;
        Class clazz = null;
        try
        {
            if (cl != null)
            {
                clazz = Class.forName(className, true, cl);
            }
            else
            {
                clazz = Class.forName(className);
            }
        }
        catch (ClassNotFoundException e)
        {
            result = false;
        }
         /**/
        catch (NoClassDefFoundError e)
        {
            result = false;
        }
         /**/
        if (result)
        {
            int modifiers = clazz.getModifiers();
            if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
            {
                result = false;
            }
        }
        return result;
    }

    /**
     * Determines in as loose a way as possible whether className is of
     * the requiredType. Thus a JComponent is of the required type
     * Container. Another way of saying this is that className must be
     * deeper or at the same level as requiredType, when talking in terms
     * of subclasses.
     *
     * @param className    the class to be tested
     * @param requiredType the class to test against
     * @param cl           the classloader that knows about the classes
     * @return true if requiredType is assignable from className
     */
    public static boolean isRequiredType(
        String className, String requiredType, ClassLoader cl)
    {
        boolean result = true;
        Class clazz = null;
        try
        {
            clazz = Class.forName(className, true, cl);
        }
        catch (ClassNotFoundException e)
        {
            Print.pr("Not found class: " + className);
            result = false;
        }

        Class required = null;
        try
        {
            required = Class.forName(requiredType, true, cl);
        }
        catch (ClassNotFoundException e)
        {
            Print.pr("Not found class: " + requiredType);
            result = false;
        }
        if (result)
        {
            result = false;
            if (required.isAssignableFrom(clazz))
            {
                result = true;
            }
            /*
            Err.pr( "Looking to match with " + required.getName());
            Class classes[] = clazz.getDeclaredClasses();
            if(classes.length == 0)
            {
            Err.pr( "None returned for " + clazz.getName());
            }
            for( int i=0; i<=classes.length-1; i++)
            {
            Err.pr( "Cfing " + required + " with " + classes[i]);
            if(required == classes[i])
            {
            result = true;
            break;
            }
            }
            */
        }
        /*
        if(result)
        {
        Err.pr( className + " is of required type " + requiredType);
        }
        */
        return result;
    }

    public static Object invoke(Object target,
                                String methodName)
    {
        Class emptyClasses[] = {};
        Object emptyObjs[] = {};
        return invoke(target, methodName, emptyClasses, emptyObjs);
    }

    /**
     * methodParams and methodArgs can both be null
     */
    public static Object invoke(Object target,
                                String methodName,
                                Class[] methodParams,
                                Object[] methodArgs)
    {
        Method method = null;
        try
        {
            method = target.getClass().getMethod(methodName, methodParams);
        }
        catch (NoSuchMethodException ex)
        {
            Err.error(ex,
                "Missing method " + methodName + " from " + target.getClass());
        }
        return SelfReferenceUtils.invoke(target, method, methodArgs);
    }

    public static Object invoke(Class targetClass,
                                String methodName,
                                Class[] methodParams,
                                Object[] methodArgs)
    {
        Method method = null;
        try
        {
            method = targetClass.getMethod(methodName, methodParams);
        }
        catch (NoSuchMethodException ex)
        {
            Err.error(ex, "Missing method " + methodName + " from " + targetClass);
        }
        return SelfReferenceUtils.invoke(targetClass, method, methodArgs);
    }

    /**
     * Invoke a method that was unknown at compile time
     *
     * @param o      The object on which the method will be invoked
     * @param method The method to invoke
     * @return The return value that the method invocation returns
     */
    public static Object invoke(
        Object o, Method method)
    {
        return invoke(o, method, empty);
    }

    /**
     * Invoke a method that was unknown at compile time
     *
     * @param o      The object on which the method will be invoked
     * @param method The method to invoke
     * @param arg    The argument value that the method will require
     */
    public static Object invoke(
        Object o, Method method, Object arg)
    {
        Object objs[] = new Object[]{arg};
        return invoke(o, method, objs);
    }

    /**
     * Invoke a method that was unknown at compile time
     *
     * @param o      The object on which the method will be invoked
     * @param method The method to invoke
     * @param args   The argument values that the method will require
     */
    public static Object invoke(
        Object o, Method method, Object args[])
    {
         /**/
        if (args == null)
        {
            Err.error("args[] itself cannot be null");
        }
        if (o == null)
        {
            Err.error("Cannot invoke a method on a null arg");
        }

        /*
        if(o instanceof DebugComboBox)
        {
        DebugComboBox combo = (DebugComboBox)o;
        if(combo.getName().equals( "cbDay"))
        {
        times++;
        String txt = "Invoking " + method +
        " On object " + o.getClass().getName() +
        //"With args " + args +
        " times " + times;
        Err.pr( txt);
        Err.pr( "");
        if(times == 54)
        {
        Err.stack();
        }
        }
        }
        */
        Object result = null;
        try
        {
            result = method.invoke(o, args);
        }
        catch (IllegalAccessException e)
        {
            Err.error(e.toString());
        }
        catch (IllegalArgumentException e)
        {
            Print.pr("\nIllegalArgumentException on dynamic method invocation of:");
            Print.pr(method.toString());
            Print.pr("on object: " + o);
            Print.pr("of type: " + o.getClass());
            for (int i = 0; i <= args.length - 1; i++)
            {
                Print.pr(i + "./ arg value: <" + args[i] + ">");
                if (args[i] != null)
                {
                    Print.pr("\targ type: " + args[i].getClass());
                    Print.pr("\targ toString(): <" + args[i].toString() + ">");
                }
            }
            Err.error(e.toString());
        }
        catch (InvocationTargetException e)
        {
            iteError(e, o, method, args);
        }
        /*
        * Not all methods return something!!
        if(result == null)
        {
        Err.error( "Problems invoking method " + method.getName());
        }
        */
        return result;
    }

    /**
     * Get a Class object from the name of the class
     *
     * @param p  The name of the class
     * @param cl The classloader
     * @return The Class object
     */
    public static Class getClass(String p, ClassLoader cl)
    {
        Class c = null;
        try
        {
            c = Class.forName(p, true, cl);
        }
        catch (ClassNotFoundException e)
        {
            Err.error(e);
        }
        return c;
    }

    /**
     * Get a Class object from the name of the class
     *
     * @param p The name of the class
     * @return The Class object
     */
    public static Class getClass(String p)
    {
        Class c = null;
        try
        {
            c = Class.forName(p);
        }
        catch (ClassNotFoundException e)
        {// Err.error( e);
        }
        return c;
    }

    /**
     * Given a Class object, find all its public fields
     *
     * @param clazz The Class object
     * @return An array of Field objects
     */
    public static Field[] getFields(Class clazz)
    {
        Field[] publicFields = null;
        try
        {
            publicFields = clazz.getFields();
        }
        catch (SecurityException e)
        {
            Err.error(e.toString());
        }
        return publicFields;
    }

    /**
     * Given two objects where one could reference the other, achieve it.
     *
     * @param mainObj  - the object that needs one of its fields/properties to be set
     * @param refObj   - the value that the field/property needs to be set to
     * @param refField - String that identifies the field/property
     */
    public static void setReferenceValue(Object mainObj, Object refObj, String refField, Class refClass)
    {
        Class mainClass = mainObj.getClass();
        //Class refClass = refObj.getClass();
        Field field = null;
        try
        {
            //Only going for public fields as we do here will avoid getting an illegal access
            //later on when we try to set the field value.
            field = mainClass.getField(refField);
        }
        catch (NoSuchFieldException e)
        {
            //we handle this
        }
        if (field == null)
        {
            Method method = null;
            String methodName = "set" + NameUtils.upperCaseFirstChar(refField);
            try
            {
                //maybe we should be introspecting here!
                Class args[] = new Class[1];
                args[0] = refClass;
                method = mainClass.getMethod(methodName, args);
            }
            catch (NoSuchMethodException e)
            {
                Err.pr("Got field: <" + field + ">");
                Err.pr("Tried to find method name: <" + methodName + "> with arg <" + refClass + ">");
                Err.error("No field/method relationship: no <" + refField + "> field/method in <" + mainClass + ">");
            }
            invoke(mainObj, method, refObj);
        }
        else
        {
            setFieldValue(mainObj, field, refObj);
        }
    }

    /**
     * Perform the assignment of an object value to a field in the 'housing' object. Needless
     * to say this method is only necessary when we did not know the field at compile time.
     *
     * @param obj    The instance whose member field we wish to assign to
     * @param field  The Field object that represents a member field of obj
     * @param objVal The object value that is the RHS of the assignment statement.
     */
    public static void setFieldValue(Object obj, Field field, Object objVal)
    {
        if (field == null)
        {
            Err.error("setFieldValue(..) has been passed null for the field param");
        }
        if (obj == null)
        {
            Err.error("Can't set a null Object to anything, so don't call");
        }
        try
        {
            field.set(obj, objVal);
        }
        catch (IllegalAccessException e)
        {
            Err.error(
                "Field " + field.getType().toString() + " in " + obj.toString()
                    + " is being illegally accessed in set operation");
        }
        catch (IllegalArgumentException e)
        {
            Err.error(
                field + " in obj of type " + obj.getClass().toString()
                    + " has tried to be assigned to with an illegal arg: " + objVal
                    + " of type " + objVal.toString());
        }
        // new MessageDlg("from obj,field: " + obj + "," + field + " have set " +
        // getFieldValue(obj, field));
    }

    /**
     * Get the value of a field in an instance.
     * Used in SubRecordObj, and when getting a reference value
     *
     * @param obj   The instance
     * @param field The Field object
     * @return The value of the Field object
     */
    public static Object getFieldValue(Object obj, Field field)
    {
        if (field == null)
        {
            Err.error("Cannot getFieldValue when Field passed is null");
        }
        if (obj == null)
        {
            Err.error("The field value of a null object is null, so don't call");
        }

        Object fieldValue = null;
        // new MessageDlg("Field using to GET is " + field);
        try
        {
            fieldValue = field.get(obj);
        }
        catch (IllegalArgumentException e)
        {
            Err.error(
                "Field " + field.toString() + " does not exist in " + obj.toString());
        }
        catch (IllegalAccessException e)
        {
            Err.error(
                "Field " + field.toString() + " in " + obj.toString()
                    + " is being illegally accessed in get operation");
        }
        // Err.pr("from obj,field: " + obj + "," + field + " will ret " + fieldValue);
        return fieldValue;
    }

    /**
     * Get the value of a field in an instance when we only know the field name as a String
     *
     * @param obj       The instance
     * @param fieldName The String name of the field
     * @return The value of the Field object
     */
    public static Object getFieldValue(Object obj, String fieldName)
    {
        Object fieldValue = null;
        Class clazz = obj.getClass();
        // only VM can do this: Field field = new Field();
        try
        {
            // change to getField if need to go to superclass
            fieldValue = (clazz.getDeclaredField(fieldName)).get(obj);
        }
        catch (NoSuchFieldException e)
        {
            Err.error("Field " + fieldName + " does not exist in " + obj.toString());
        }
        catch (IllegalAccessException e)
        {
            Err.error(
                "Field " + fieldName + " in " + obj.toString()
                    + " is being illegally accessed");
        }
        return fieldValue;
    }

    public static void displayMethods(Class clazz)
    {
        Method methods[] = clazz.getDeclaredMethods();
        // Err.pr( methods);
        for (int i = 0; i <= methods.length - 1; i++)
        {
            Print.pr("Name: " + methods[i].getName());

            Class params[] = methods[i].getParameterTypes();
            for (int j = 0; j <= params.length - 1; j++)
            {
                Print.pr("\tParam: " + params[j]);
            }
        }
    }

    public static boolean doesMethodExist(Class clazz, String strInit)
    {
        if (clazz == null)
        {
            Err.error("doesMethodExist(), clazz param cannot be null");
        }
        if (strInit == null || strInit.equals(""))
        {
            Err.error("doesMethodExist(), strInit param cannot be null or \"\"");
        }

        boolean result = true;
        Class params[] = {};
        try
        {
            clazz.getDeclaredMethod(strInit, params);
        }
        catch (NoSuchMethodException ex)
        {
            result = false;
        }
        return result;
    }

    public static Class getPropertyType(Class clazz, String propertyName)
    {
        Class result = null;
        BeanInfo bi = null;
        try
        {
            bi = Introspector.getBeanInfo(clazz);
        }
        catch (IntrospectionException ex)
        {
            Err.error(ex);
        }

        PropertyDescriptor pds[] = bi.getPropertyDescriptors();
        for (int i = 0; i <= pds.length - 1; i++)
        {
            if (pds[i].getName().equals(propertyName))
            {
                result = pds[i].getPropertyType();
                break;
            }
        }
        if (result == null)
        {// This will happen when adding a new attribute
            // Err.error( "Could not find a property with the name [" + propertyName + "] in class [" + clazz.getName() + "]");
        }
        return result;
    }

    /**
     * Method classOfFieldInClass.
     *
     * @param clazz
     * @param dataFieldName
     * @return Class
     */
    public static Class classOfMethodInClass(Class clazz, String dataFieldName)
    {
        Class result = null;
        BeanInfo bi = null;
        if (clazz == null)
        {
            Err.error("ObjectFoundry.classOfMethodInClass(), clazz param is null");
        }
        try
        {
            bi = Introspector.getBeanInfo(clazz);
        }
        catch (IntrospectionException ex)
        {
            Err.error(ex);
        }

        PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        /*
        * As this is called during validation, first of all check that
        * the property can be read and written:
        */
        for (int i = 0; i <= pds.length - 1; i++)
        {
            Method readMethod = pds[i].getReadMethod();
            if (pds[i].getName().equals(dataFieldName))
            {
                result = pds[i].getPropertyType();
            }
        }
        return result;
    }

    public static Object invokeReadProperty(
        BeanInfo beanInfo, PropertyDescriptor pd, Object o, String propertyStr)
    {
        Object result = null;
        if (o == null)
        {
            Err.error("ObjectFoundry.invokeReadProperty(), the instance param is null");
        }

        Method readMethod = null;
        if (pd.getName().equals(propertyStr))
        {
            readMethod = pd.getReadMethod();
        }
        if (readMethod == null)
        {/*
       * Write only component
       * component, focusTraversalKeys, inputMap, component, actionCommand,
       * componentOrientation, toInvalidateComponent (ours!)
       Err.pr(
       "ObjectFoundry.invokeReadProperty(), could not find "
       + "read method for " + propertyStr + " in a " + o.getClass());
       */
        }
        else
        {
            result = invoke(o, readMethod);
        }
        return result;
    }

    /**
     * In context that this method is used (with data), should really be able
     * to read both a field and a method - other code might even already be
     * doing!
     */
    public static Object invokeReadProperty(Object o, String propertyStr)
    {
        Object result = null;
        BeanInfo bi = null;
        if (o == null)
        {
            Err.error( "ObjectFoundry.invokeReadProperty(), o param is null");
        }
        try
        {
            bi = Introspector.getBeanInfo(o.getClass());
        }
        catch (IntrospectionException ex)
        {
            Err.error(ex);
        }

        PropertyDescriptor pds[] = bi.getPropertyDescriptors();
        Method readMethod = null;
        for (int i = 0; i <= pds.length - 1; i++)
        {
            // Err.pr( pds[i].getName());
            if (pds[i].getName().equals(propertyStr))
            {
                readMethod = pds[i].getReadMethod();
                break;
            }
        }
        if (readMethod == null)
        {
            Err.error(
                "ObjectFoundry.invokeReadProperty(), could not find "
                    + "read method <" + propertyStr + "> in a " + o.getClass());
        }
        else
        {
            result = invoke(o, readMethod);
        }
        return result;
    }

    public static int hashCodeByProperties(int result, String propertyNames[], Object obj)
    {
        for (int i = 0; i < propertyNames.length; i++)
        {
            String propertyName = propertyNames[i];
            Object value = invokeReadProperty(obj, propertyName);
            result = 37 * result + (value == null ? 0 : value.hashCode());
        }
        return result;
    }

    /**
     * Using particular propertyNames, makes sure that the values of these propertyNames are
     * the same in two objects.
     * <p/>
     * Used to make writing equals() methods for DOs easier. Normally the only properties you
     * would put in here would be the keys of a DO. Occassionally, for instance when want to
     * compare two databases, will put put in all the properties. Watch out for comparing
     * first class objects, that recursion does not occur. The obvious case being where a
     * member contains another member of the same type. Just leave these properties out and
     * find another way of comparing them! (If we really wanted to solve this problem we
     * could have this method taking the properties to use to compare for that particular
     * property). (If you need an example, a Worker contains 'belongsToGroup', which is
     * another Worker).
     *
     * @param propertyNames the set of properties we need to assess the values of
     * @param obj1          first object whose properties need to be looked at
     * @param obj2          second object whose properties need to be looked at
     * @return true if property values of obj1 and obj2 are all equal
     */
    public static boolean equalsByProperties(String propertyNames[], Object obj1, Object obj2)
    {
        boolean result = true;
        //Err.pr( ReasonNotEquals.isTurnedOn() && ProdKpiNote.FAKE_LEAVES.isVisible(), "");
        for (int i = 0; i < propertyNames.length; i++)
        {
            String propertyName = propertyNames[i];
            //See recursion from this line
            //Err.pr( ReasonNotEquals.isTurnedOn() && ProdKpiNote.FAKE_LEAVES.isVisible(),
            //    "To do equals on property " + propertyName + " b/ween " + obj1 + " and " + obj2);
            if (!Utils.equals(invokeReadProperty(obj1, propertyName),
                invokeReadProperty(obj2, propertyName)))
            {
                ReasonNotEquals.addReason(SelfReferenceUtils.getDifference(propertyName, obj1, obj2));
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * See equalsByProperties for explanation
     */
    public static int compareToByProperties(String propertyNames[], Object obj1, Object obj2)
    {
        int result = 0;
        for (int i = 0; i < propertyNames.length; i++)
        {
            String propertyName = propertyNames[i];
            //See recursion from this line
            //Err.pr( "To do compareTo on property " + propertyName + " b/ween " + obj1 + " and " + obj2);
            int compareTo = Utils.compareTo((Comparable) invokeReadProperty(obj1, propertyName), (Comparable) invokeReadProperty(obj2, propertyName));
            if (compareTo != 0)
            {
                ReasonNotEquals.addReason(SelfReferenceUtils.getDifference(propertyName, obj1, obj2));
                result = compareTo;
                break;
            }
        }
        return result;
    }

    public static String getDifference(String propertyName, Object obj1, Object obj2)
    {
        String result = null;
        Object val1 = invokeReadProperty(obj1, propertyName);
        Object val2 = invokeReadProperty(obj2, propertyName);
        result = propertyName + " first value: " + val1 + " second value: " + val2;
        return result;
    }

    /*
    private static void writeProperty(
    HashMap properties, PropertyDescriptor pd, Component control)
    {
    if(properties.get( pd) != null)
    {
    Object vals[] = { properties.get( pd)};
    if((pd.getWriteMethod()) != null)
    {
    invoke( control, pd.getWriteMethod(), vals);
    Err.pr( "Have set value <" + properties.get( pd) + "> on " + control.getName() + " for " + pd.getDisplayName());
    }
    }
    else
    {
    Err.pr( "property " + pd.getDisplayName() + " could not be set on " + control.getName());
    //Not in properties hopefully only b/c did not have a readMethod
    }
    }
    */

    /*
    public static HashMap getPropertiesOnControl( Component control)
    {
    HashMap result = new HashMap();
    BeanInfo bi = null;
    if(control == null)
    {
    Err.error( "ObjectFoundry.getPropertiesOnControl(), control param is null");
    }
    try
    {
    bi = Introspector.getBeanInfo( control.getClass());
    }
    catch(IntrospectionException ex)
    {
    Err.error( ex);
    }
    PropertyDescriptor[] pds = bi.getPropertyDescriptors();
    for(int i=0; i<=pds.length-1; i++)
    {
    if(pds[i].getReadMethod() != null)
    {
    Object empty[] = {};
    Object value = invoke( control, pds[i].getReadMethod(), empty);
    result.put( pds[i], value);
    }
    }
    return result;
    }
    */

    /*
    * Not used
    public static Field get_Field( Object obj, String fieldName) throws NoSuchFieldException
    {
    Class clazz = obj.getClass();
    return getField( clazz, fieldName);
    }
    */

    /*
    * Not used
    public static Class getFieldType( Class clazz, String fieldName)
    {
    Class result = null;
    Field field = null;
    try
    {
    field = clazz.getField( fieldName);
    }
    catch(NoSuchFieldException ex)
    {
    //nufin
    }
    if(field != null)
    {
    result = field.getDeclaringClass();
    }
    return result;
    }
    */

    static void iteError(InvocationTargetException e,
                         Object o,
                         Method method,
                         Object args[])
    {
        Print.pr("InvocationTargetException on dynamic method invocation of:");
        Print.pr(method.toString());
        //Print.pr("on object: " + o);
        Print.pr("of type: " + o.getClass());
        Print.pr("TARGET EXCEPTION: " + e.getTargetException());
        Print.pr("TARGET STACK TRACE: ");
        e.printStackTrace(System.out);
        if (args != null)
        {
            for (int i = 0; i <= args.length - 1; i++)
            {
                Err.pr(i + "./ arg: " + args[i]);
                if (args[i] != null)
                {
                    Err.pr("\tof type: " + args[i].getClass());
                }
            }
        }
        Err.error(e.toString());
    }
}
