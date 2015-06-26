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

import org.strandz.lgpl.note.SdzNote;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Method;

/**
 * This static class comprises utility functions for Components. Mostly
 * they concern containership and equality, however consider setUpCycle
 * which is to do with the function of a keyboard key.
 *
 * @author Chris Murphy
 */
public class ComponentUtils
{
    /**
     * Can be called for two maps of components that are keyed
     * on their names, where we are interested in whether these keys are
     * equal().
     * Normally equals() for a map would be done on the entrySet(). Here
     * we do it on keySet() instead. A useful time to do this is when the
     * entrySet() is made up of JComponents, which revert to the default
     * hashCode() implementation, which makes each instance different.
     *
     * @param map1 first map
     * @param map2 second map
     * @return true if the two maps are 'equal'
     */
    public static boolean mapComponentsEqual(Map map1, Map map2)
    {
        boolean result = true;
        if(map1 != map2)
        {
            if(map1.size() == map2.size())
            {
                result = map1.keySet().equals(map2.keySet());
            }
            else
            {
                result = false;
            }
        }
        return result;
    }

    /**
     * To assist with a hashCode() implementation that stays consistent with
     * mapComponentsEqual()
     *
     * @param map
     * @return hashed value
     * @see #mapComponentsEqual(java.util.Map, java.util.Map)
     */
    public static int mapComponentsHashCode(Map map)
    {
        int result = 0;
        if(map != null)
        {
            result = map.keySet().hashCode();
        }
        // Err.pr( "mapHashCode " + map + "\nreturning hashCode of " + result);
        return result;
    }

    /**
     * To assist with a hashCode() implementation that stays consistent with
     * equalsComponentArrays()
     *
     * @param array an array of JComponents
     * @return hashed value
     * @see #equalsComponentArrays(javax.swing.JComponent[], javax.swing.JComponent[])
     */
    public static int hashCodeComponentArrays(JComponent[] array)
    {
        int result = 0;
        if(array != null)
        {
            for(int i = 0; i < array.length; i++)
            {
                Object obj = array[i].getName();
                result += obj.hashCode();
            }
        }
        // Err.pr( "hashCodeComponentArrays " + array + "returning hashCode of " + result);
        return result;
    }

    /**
     * Recursively get all the controls that are contained in container
     *
     * @param container
     * @return all descendents
     */
    public static List getAllControls(Container container)
    {
        return getAllControls(container, null);
    }

    /**
     * Recursively get all the controls that are contained in container
     *
     * @param container
     * @param ofTypes   only interested in these class types (by equals())
     * @return all descendents
     */
    public static List getAllControls(Container container, List ofTypes)
    {
        // Err.pr( "getAllControls() for: " + container.hashCode());
        List controlsResult = new ArrayList();
        return getAllControls(container, controlsResult, ofTypes);
    }

    private static List getAllControls(Container container, List controlsResult, List ofTypes)
    {
        if(container != null)
        {
            Component controls[] = ((Container) container).getComponents();
            for(int i = 0; i < controls.length; i++)
            {
                if(controls[i] instanceof Container)
                {
                    controlsResult = getAllControls((Container) controls[i],
                                                    controlsResult, ofTypes);
                }
                if(ofTypes == null)
                {
                    // Err.pr( "getAllControls(), adding: " + controls[i].getName() + "," + controls[i].getClass().getName());
                    controlsResult.add(controls[i]);
                }
                else if(ofTypes.contains(controls[i].getClass()))
                {
                    // Err.pr( "getAllControls(), adding: " + controls[i].getName() + "," + controls[i].getClass().getName());
                    controlsResult.add(controls[i]);
                }
            }
        }
        return controlsResult;
    }

    private static Rectangle calculateInnerArea(JComponent comp, int pixels)
    {
        Rectangle result = new Rectangle();
        Rectangle compRect = comp.getBounds();
        result.x = pixels;
        result.y = pixels;
        result.height = compRect.height - (pixels * 2);
        result.width = compRect.width - (pixels * 2);
        return result;
    }

    /**
     * Depending on where you are on a component this method will return a
     * cursor appropriate to your location. For example if the mouse is on
     * the northern border then an up/down arrow will be returned
     *
     * @param comp   the component we want to change the cursor for
     * @param pixels determines how close the cursor can go before changing
     * @param point  the point that the mouse is at
     * @return the calculated cursor
     */
    public static Cursor getCursor(JComponent comp, int pixels, Point point)
    {
        Cursor result = null;
        //Rectangle rect = comp.getBounds();
        Rectangle inner = calculateInnerArea(comp, pixels);
        if(inner.contains(point))
        {
            result = ToolkitUtils.getMoveCursor();
        }
        else
        {
            if(point.y <= inner.y) // above
            {
                if(point.x <= inner.x) // west
                {
                    result = ToolkitUtils.getNorthWestCursor();
                }
                else if(point.x >= inner.width + inner.x) // east
                {
                    result = ToolkitUtils.getNorthEastCursor();
                }
                else
                {
                    result = ToolkitUtils.getNorthCursor();
                }
            }
            else if(point.y >= inner.height + inner.y) // below
            {
                if(point.x <= inner.x) // west
                {
                    result = ToolkitUtils.getSouthWestCursor();
                }
                else if(point.x >= inner.width + inner.x) // east
                {
                    result = ToolkitUtils.getSouthEastCursor();
                }
                else
                {
                    result = ToolkitUtils.getSouthCursor();
                }
            }
            else if(point.x <= inner.x) // west
            {
                result = ToolkitUtils.getWestCursor();
            }
            else // rest
            {
                result = ToolkitUtils.getEastCursor();
            }
            /*
            Err.pr( "");
            Err.pr( "Rect: " + rect);
            Err.pr( "Inner: " + inner);
            Err.pr( "Point: " + point);
            */
        }
        return result;
    }

    public static List getItems( JComboBox comboBox)
    {
        List result = new ArrayList();
        for(int i = 0; i < comboBox.getItemCount(); i++)
        {
            Object element =  comboBox.getItemAt(i);
            result.add( element);
        }
        return result;
    }

    /**
     * Method to use when we are needing to see if two sets of components are equal when we are
     * considering them as JavaBeans. If all the properties of each component are equal, then
     * the two sets are equal. 'all the properties' is determined by recursing down to the child
     * components
     *
     * @param components       first array of components
     * @param components2      second array of components
     * @param widgetClassifier Provides this method with a definition of a component
     */
    public static boolean equalsByProperties(JComponent[] components, JComponent[] components2, WidgetClassifierI widgetClassifier)
    {
        ReasonNotEquals.addClassVisiting("pUtils.equalsByProperties([])");

        boolean result = true;
        if(components.length != components2.length)
        {
            ReasonNotEquals.addReason("got lists of components of different sizes: " + components.length
                + " and " + components2.length);
            result = false;
        }
        else
        {
            for(int i = 0; i < components.length; i++)
            {
                JComponent comp1 = components[i];
                JComponent comp2 = components2[i];
                // not work due to diff classloaders having been used
                // if(comp1.getClass() != comp2.getClass())
                if(!comp1.getClass().getName().equals(comp2.getClass().getName()))
                {
                    ReasonNotEquals.addReason("two components were not of the same class: " + comp1.getClass()
                        + " and " + comp2.getClass());
                    result = false;
                    break;
                }
                else
                {
                    result = equalsByProperties(comp1, comp2, widgetClassifier);
                    if(!result)
                    {
                        ReasonNotEquals.addReason("equalsByProperties() failed for 2 classes of type: "
                            + comp1.getClass().getName());
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static boolean equalsByProperties(JComponent component1, JComponent component2, WidgetClassifierI widgetClassifier)
    {
        ReasonNotEquals.addClassVisiting("pUtils.equalsByProperties()");

        boolean result = true;
        List oneControls = getAllControls(component1);
        oneControls.add(0, component1);

        List twoControls = getAllControls(component2);
        twoControls.add(0, component2);
        if(oneControls.size() != twoControls.size())
        {
            ReasonNotEquals.addReason("got lists of controls of different sizes: " + oneControls.size()
                + " and " + twoControls.size());
            Print.prList(oneControls, "oneControls");
            Print.prList(twoControls, "twoControls");
            result = false;
        }
        else
        {
            for(int i = 0; i < oneControls.size(); i++)
            {
                Container comp1 = (Container) oneControls.get(i);
                // Err.pr( "Observing a " + comp1.getClass());
                Container comp2 = (Container) twoControls.get(i);
                if(comp1.getClass() != comp2.getClass())
                {
                    ReasonNotEquals.addReason("first component is of type " + comp1.getClass()
                        + " while second is of type " + comp2.getClass());
                    result = false;
                    break;
                }
                else if(!Utils.equals(comp1.getName(), comp2.getName()))
                {
                    ReasonNotEquals.addReason("first component has name " + comp1.getName()
                        + " while second has name " + comp2.getName());
                    result = false;
                    break;
                }
                else if(widgetClassifier != null
                    && !widgetClassifier.isAWidget(comp1.getClass()))
                {// If a comp1/comp2 is not a widget then end up here and we won't examine
                    // its properties
                    // Err.pr( comp1.getClass() + " is not a widget: " + widgetClassifier.getReason());
                }
                else
                {
                    List propertyNamesOne = new ArrayList();
                    List propertyNamesTwo = new ArrayList();
                    List propertyValuesOne = new ArrayList();
                    List propertyValuesTwo = new ArrayList();
                    loadPropertyNamesValues(comp1.getClass(), comp1, propertyNamesOne,
                                            propertyValuesOne);
                    loadPropertyNamesValues(comp2.getClass(), comp2, propertyNamesTwo,
                                            propertyValuesTwo);
                    if(propertyNamesOne.size() != propertyValuesOne.size())
                    {
                        Err.error("expect names and properties to be same size for "
                            + comp1.getClass());
                    }
                    else if(propertyNamesTwo.size() != propertyValuesTwo.size())
                    {
                        Err.error("expect names and properties to be same size for "
                            + comp2.getClass());
                    }
                    else if(propertyNamesOne.size() != propertyNamesTwo.size())
                    {
                        ReasonNotEquals.addReason("there should be an equal number of properties in "
                            + comp1.getClass() + " and " + comp2.getClass());
                        result = false;
                        break;
                    }
                    else
                    {
                        // Err.pr( "Will be examining properties from a " + comp1.getClass() + " against " + comp2.getClass());
                        for(int j = 0; j < propertyNamesOne.size(); j++)
                        {
                            String onePropName = (String) propertyNamesOne.get(j);
                            String twoPropName = (String) propertyNamesTwo.get(j);
                            if(!onePropName.equals(twoPropName))
                            {
                                // If they are of thge same type, how could they possibly have
                                // different property names, thus make this an error
                                String txt = "expect property names to be equal, got "
                                    + onePropName + " and " + twoPropName;
                                // ReasonNotEquals.addReason( txt);
                                Err.error(txt);
                                result = false;
                                break;
                            }
                            else
                            {
                                Object onePropValue = propertyValuesOne.get(j);
                                Object twoPropValue = propertyValuesTwo.get(j);
                                boolean debug = false;
                                /*
                                if(onePropName.equals( "text"))
                                {
                                Err.debug();
                                debug = true;
                                }
                                */
                                if(onePropValue.getClass() != twoPropValue.getClass())
                                {
                                    Err.error("Property values to be examining should be of the same type, got " +
                                        onePropValue.getClass().getName() + " and " + twoPropValue.getClass().getName());
                                    result = false;
                                    break;
                                }
                                else if(!Utils.isSimple(onePropValue.getClass()))
                                {
                                    // We only want to do value comparisons for simple types
                                    // Err.pr( "Not simple: <" + onePropValue + ">");
                                    if(debug)
                                    {
                                        Err.error("Only supposed to be debugging on a simple property value");
                                    }
                                }
                                else
                                {
                                    if(debug)
                                    {
                                        Err.pr("Doing cf between <" + onePropValue + "> and <"
                                            + twoPropValue + "> for " + onePropName + " in a "
                                            + comp1.getClass());
                                        debug = false;
                                    }
                                    if(!onePropValue.equals(twoPropValue))
                                    {
                                        ReasonNotEquals.addReason("expect property values to be equal, got <"
                                            + onePropValue + "> and <" + twoPropValue + ">");
                                        result = false;
                                        break;
                                    }
                                    else
                                    {// Err.pr( "\tconcordance, prop name " + onePropName + ", prop value " + onePropValue);
                                    }
                                }
                            }
                        }
                        if(result == false)
                        {
                            break; // so break out of outer loop too
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * JComponents are really equal if their names are the same (or this is better
     * than the Object.equals()/hashCode() that is used by default for JComponents).
     * Note that <code>ReasonNotEquals</code> can be used to determine the reason for a failure
     *
     * @param arr1 first array of components
     * @param arr2 second array of components
     * @return true if the arrays are equal
     */
    public static boolean equalsComponentArrays(JComponent[] arr1, JComponent[] arr2)
    {
        ReasonNotEquals.addClassVisiting("pUtils.equalsComponentArrays()");

        boolean result = true;
        if(arr1 != arr2)
        {
            // if(arr1.getClass() == arr2.getClass() &&
            // JComponent.class.isAssignableFrom( arr1.getClass()))
            // {
            if(arr1.length == arr2.length)
            {
                int size = arr1.length;
                for(int i = 0; i < size; i++)
                {
                    String e1 = arr1[i].getName();
                    String e2 = arr2[i].getName();
                    if(e1 == null ? e2 == null : e1.equals(e2))
                    {// keep on being true
                    }
                    else
                    {
                        ReasonNotEquals.addReason("component names not equal: " + e1 + ", " + e2);
                        result = false;
                        break;
                    }
                }
            }
            else
            {
                ReasonNotEquals.addReason("component arrays different sizes");
                result = false;
            }
            // }
            // else
            // {
            // result = false;
            // }
        }
        return result;
    }

    /**
     * Should not be used directly by Strandz. See package org.strandz.core.info.convert 
     * This must come out as Strandz needs control over assignment
     * @param comp
     * @param txt
     */
    public static void _setText( Object comp, Object txt)
    {
        Err.pr( SdzNote.DONT_USE_FROM_STRANDZ, "Don't use");
        if(comp instanceof JComponent)
        {
            String s = null;
            if(txt != null)
            {
                s = txt.toString();
            }
            if(comp instanceof JTextComponent)
            {
                ((JTextComponent)comp).setText( s);
            }
            else if(comp instanceof JLabel)
            {
                ((JLabel)comp).setText( s);
            }
            else
            {
                Err.error( "ComponentUtils.setText(), not yet support JComponent of type " + comp.getClass());
            }
        }
        else
        {
            Err.error( "ComponentUtils.setText(), not yet support " + comp.getClass());
        }
    }

    public static String _getName( Object comp)
    {
        String result = null;
        Err.pr( SdzNote.DONT_USE_FROM_STRANDZ, "Don't use");
        if(comp instanceof JComponent)
        {
            if(comp instanceof JTextComponent)
            {
                result = ((JTextComponent)comp).getName();
            }
            else if(comp instanceof JLabel)
            {
                result = ((JLabel)comp).getName();
            }
            else
            {
                Err.error( "ComponentUtils.getName(), not yet support JComponent of type " + comp.getClass());
            }
        }
        else
        {
            Err.error( "ComponentUtils.getName(), not yet support " + comp.getClass());
        }
        return result;
    }

    /**
     * Should not be used directly by Strandz. See package org.strandz.core.info.convert 
     */
    public static String _getText( Object comp)
    {
        String result = null;
        Err.pr( SdzNote.DONT_USE_FROM_STRANDZ, "Don't use");
        if(comp instanceof JComponent)
        {
            if(comp instanceof JTextComponent)
            {
                result = ((JTextComponent)comp).getText();
            }
            else if(comp instanceof JLabel)
            {
                result = ((JLabel)comp).getText();
            }
            else
            {
                Err.error( "ComponentUtils.getText(), not yet support JComponent of type " + comp.getClass());
            }
        }
        else
        {
            Err.error( "ComponentUtils.getText(), not yet support " + comp.getClass());
        }
        return result;
    }
    
    public static boolean hasEditableMethod( Object comp)
    {
        boolean result = true;
        String methodName = "isEditable";
        Class controlClass = comp.getClass();
        try
        {
            controlClass.getMethod(methodName, (Class[])null);
        }
        catch(NoSuchMethodException ex1)
        {
            result = false;
        }        
        catch(Exception ex2)
        {
            Err.error("Missing method " + methodName + " from " + controlClass + ", ex: " + ex2);
        }
        return result;
    }

    /**
     * TODO - Think about reason are not going to ControlSignatures for this.
     * Pros for not 
     * - ControlSignatures is part of strandz core, whereas this is lgpl
     * Pros for having in ControlSignatures
     * - Controls inside a table should be treated in exactly the same way as other field
     *   controls. (This would require changes to info package).
     * 
     * Of course providing a service interface would be the answer, but changing
     * info provides inertia. Bugs because of differences will help the case for 
     * change. 
     */
    public static boolean isEditable( Object comp)
    {
        boolean result = false;
        Method isEditableControlMethod = null;
        String methodName = "isEditable";
        Class controlClass = comp.getClass();
        boolean noMethod = false;
        try
        {
            isEditableControlMethod = controlClass.getMethod(methodName, (Class[])null);
        }
        catch(NoSuchMethodException ex1)
        {
            noMethod = true;
        }        
        catch(Exception ex2)
        {
            Err.error("Missing method " + methodName + " from " + controlClass + ", ex: " + ex2);
        }
        if(!noMethod)
        {
            result = ((Boolean) SelfReferenceUtils.invoke( comp, isEditableControlMethod)).booleanValue();
        }
        return result;
    }
    
    public static String getText( Object comp)
    {
        String result;
        Method getTextControlMethod = null;
        String methodName = "getText";
        Class controlClass = comp.getClass();
        try
        {
            getTextControlMethod = controlClass.getMethod(methodName, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method " + methodName + " from " + controlClass);
        }
        result = (String)SelfReferenceUtils.invoke( comp, getTextControlMethod);
        return result;
    }
    
    public static String getName( Object comp)
    {
        String result;
        Method getNameControlMethod = null;
        String methodName = "getName";
        Class controlClass = comp.getClass();
        try
        {
            getNameControlMethod = controlClass.getMethod(methodName, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method " + methodName + " from " + controlClass);
        }
        result = (String)SelfReferenceUtils.invoke( comp, getNameControlMethod);
        return result;
    }

    public static void setEditable( Object comp, boolean b)
    {
        Class[] args1 = new Class[1];
        args1[0] = Boolean.TYPE;
        Method setEditableControlMethod = null;
        String methodName = "setEditable";
        Class controlClass = comp.getClass();
        try
        {
            setEditableControlMethod = controlClass.getMethod(methodName, args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method " + methodName + " from " + controlClass);
        }
        SelfReferenceUtils.invoke(comp, setEditableControlMethod, Boolean.valueOf( b));
    }
    
    public static void setText( Object comp, Object txt)
    {
        Class[] args1 = new Class[1];
        args1[0] = String.class;
        Method setTextControlMethod = null;
        String methodName = "setText";
        Class controlClass = comp.getClass();
        try
        {
            setTextControlMethod = controlClass.getMethod(methodName, args1);
        }
        catch(Exception ex1)
        {
            args1[0] = Object.class;
            try
            {
                setTextControlMethod = controlClass.getMethod(methodName, args1);
            }   
            catch(Exception ex2)
            {
                Err.error("Missing method " + methodName + " from " + controlClass);
            }
        }
        String s = null;
        if(txt != null)
        {
            s = txt.toString();
        }
        SelfReferenceUtils.invoke(comp, setTextControlMethod, s);
    }

    private static void loadPropertyNamesValues(Class clazz, Object instance, List names, List values)
    {
        PropertyDescriptor pd;
        BeanInfo info = null;
        try
        {
            info = Introspector.getBeanInfo(clazz);
        }
        catch(IntrospectionException ex)
        {
            Err.error(ex);
        }

        PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
        for(int i = 0; i < propertyDescriptors.length; ++i)
        {
            pd = propertyDescriptors[i];

            Object value = SelfReferenceUtils.invokeReadProperty(info, pd, instance,
                                                                 pd.getName());
            if(value == null)
            {// Err.pr( "No property set for " + pd.getName() + " in " + clazz);
            }
            else
            {
                names.add(pd.getName());
                values.add(value);
            }
        }
        if(names.isEmpty())
        {
            Err.error("Strange that there are no properties with values in " + clazz);
        }
    }

    public static boolean controlNameIsUniqueIn(String name, List controls)
    {
        boolean result = false;
        if(name == null)
        {
            Err.error("controlNameIsUniqueIn() cannot be called with a null name");
        }

        int timesFound = 0;
        for(Iterator iter = controls.iterator(); iter.hasNext();)
        {
            Component element = (Component) iter.next();
            String elementName = element.getName();
            if(elementName != null)
            {
                if(name.equals(elementName))
                {
                    timesFound++;
                }
            }
        }
        if(timesFound == 1)
        {
            result = true;
        }
        else if(timesFound == 0)
        {
            Err.error("This method assumes that <" + name + "> exists at least once");
        }
        return result;
    }

    /**
     * See if a control is in a container, no matter how many levels down
     * @param control the control that might be in the container
     * @param container the container we will drill down into
     * @return true if control is in the container
     */
    public static boolean controlIsInContainer(Component control,
                                               Container container)
    {
        boolean result = false;
        List allControls = getAllControls(container, null);
        if(allControls.indexOf(control) != -1)
        {
            // Err.pr( "Have found " + control.getName() + " in " + container.getName());
            result = true;
        }
        return result;
    }

    /**
     * After XMLEncoding the handler has gone. Here we explicitly use 
     * JTextComponent.installDefaultTransferHandlerIfNecessary() to put one (back) in.
     * 
     * @param comps
     */
    public static void fixCopyPasteBug( JComponent comps[])
    {
        for(int i = 0; i < comps.length; i++)
        {
            JComponent comp = comps[i];
            List recursedControls = ComponentUtils.getAllControls( comp);
            for(Iterator iterator = recursedControls.iterator(); iterator.hasNext();)
            {
                Component component = (Component) iterator.next();
                if(component instanceof JComponent)
                {
                    ComponentUtils.fixCopyPasteBug( (JComponent)component);
                }
            }
        }
    }
    
    public static void fixCopyPasteBug( JComponent comp)
    {
        KeyUtils.installLostClipboardActions( comp);
    }
    
    public static void copyToClipboard( String content)
    {
        copyToClipboard( Utils.formArray( content));
    }

    public static void copyToClipboard( String contents[])
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        final Clipboard clipboard = kit.getSystemClipboard();
        String allContent = "";
        for(int i = 0; i < contents.length; i++)
        {
            String content = contents[i];
            allContent += content;
        }
        StringSelection stsel = new StringSelection( allContent);
        clipboard.setContents( stsel, stsel);
    }
    
    public static int getIndexOf( JTabbedPane tabbedPane, Component component)
    {
        int result = -1;
        for(int i = 0; i < tabbedPane.getComponentCount(); i++)
        {
            Component comp = tabbedPane.getComponentAt(i);
            if(comp == component)
            {
                result = i;
                break;
            }
        }
        return result;
    }

    public static JComponent getDeepest( JComponent outermostContainer, MouseEvent mouseEvent)
    {
        JComponent result;
        Assert.notNull( outermostContainer);
        Assert.notNull( mouseEvent);
        MouseEvent localizedMouseEvent = SwingUtilities.convertMouseEvent(
            mouseEvent.getComponent(), mouseEvent, outermostContainer);
        result = (JComponent)SwingUtilities.getDeepestComponentAt(
            outermostContainer, localizedMouseEvent.getPoint().x, localizedMouseEvent.getPoint().y);
        return result;
    }

}
