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
package org.strandz.lgpl.extent;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.EntityManagerProviderI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NamedI;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.IdentifierI;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class MethodTie extends Tie
{
    /**
     * Will fill either of these two sets - ie./ field is contained on either
     * child or parent side.
     */
    Method childReadMethod, childWriteMethod;
    Class childType;
    Method parentReadMethod, parentWriteMethod;
    Class parentType;
    //private Object[] args = new Object[1]; // used to call methods
    private static int times;

    MethodTie(MethodDescriptor md, EntityManagerProviderI entityManagerProviderI, 
             InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        super( entityManagerProviderI, addRemoveTrigger);
        if(md.type == PARENT_LIST)
        {
            parentReadMethod = md.readMethod;
            //Err.pr( "1Created tie with read method: " + parentReadMethod);
            parentWriteMethod = md.writeMethod;
            parentType = md.clazz;
            parentField = md.strField;
        }
        else if(md.type == CHILD_REFERENCE)
        {
            childReadMethod = md.readMethod;
            Err.pr( SdzNote.CELL_CLAZZ, "2Created tie with read method: " + childReadMethod);
            if(childReadMethod.toString().contains( "AbstractCriteria.getCriteriaEnum()"))
            {
                Err.stack();
            }
            childWriteMethod = md.writeMethod;
            childType = md.clazz;
            childField = md.strField;
        }
        else
        {
            Err.error("Must be PARENT_LIST or CHILD_REFERENCE");
        }
        parentObj = md.parentObject;
        childObj = md.childObject;
    }

    static MethodDescriptor canCreate(
        NamedI parentObj, // containerNode
        NamedI childObj, // fieldNode
        Class parentCell, // Container.class
        Class childCell, // Component.class
        String parentOrChildField) // "components"
    {
        /*
        if(Clazz.class.isAssignableFrom( parentCell))
        {
          Err.error( "Problem");
        }
        if(Clazz.class.isAssignableFrom( childCell))
        {
          Err.error( "Problem");
        }
        */
        MethodDescriptor result = new MethodDescriptor();
        if(parentObj == null || childObj == null)
        {
            Err.error(
                "Tie must be constructed with not null nodes par - " + parentObj);
        }
        if(childObj.toString().contains( "AbstractCriteria"))
        {
            Err.debug();
        }
        boolean collectRelationshipFound = false;
        boolean refRelationshipFound = false;
        /*
        * childField checking s/be done first (here), as ME to parent field checking,
        * and doesn't matter that parentObj might not yet have been validated.
        */
        if(childCell == null)
        {
            // this s/be impossible from Customizer:
            Err.error("Object " + childObj + " doesn't have an instantiable");
        }

        BeanInfo bi = null;
        try
        {
            bi = Introspector.getBeanInfo(parentCell);
        }
        catch(IntrospectionException ex)
        {
            Err.error(ex.toString());
        }

        PropertyDescriptor[] pds = bi.getPropertyDescriptors();
        /*
        * As this is called during validation, first of all check that
        * the property can be read and written:
        */
        for(int i = 0; i <= pds.length - 1; i++)
        {
            Method readMethod = pds[i].getReadMethod();
            // Err.pr( "Examining " + readMethod);
            // Err.pr( "To cf with " + parentOrChildField);
            if(pds[i].getName().equals(parentOrChildField)
                && isExtent(readMethod.getReturnType()))
            {
                // Err.pr("parent list: " + pds[i].getName());
                collectRelationshipFound = true;
                result.type = Tie.PARENT_LIST;
                result.readMethod = readMethod;
                result.writeMethod = pds[i].getWriteMethod();
                result.clazz = pds[i].getPropertyType();
                if(result.readMethod == null)
                {
                    /*
                    * Probably this s/not always be an error either.
                    */
                    Err.error(
                        "Cannot read property: " + parentOrChildField + " in cell "
                            + parentCell);
                }
                if(result.writeMethod == null)
                {/*
           * Will not always be an error. May need the concept of a RO tie.
           */// Err.pr("WARNING NOT ERROR (1) Cannot write method property: " + parentOrChildField +
                    // " in " + parentCell);
                }
                if(result.clazz == null)
                {
                    Err.error(
                        "Property <" + parentOrChildField
                            + "> is an indexed property that does not support non-indexed access");
                }
                break;
            }
            else
            {// Err.pr( "Examining read method " + readMethod +
                // " in " + pds[i].getName());
            }
        }
        bi = null;
        try
        {
            bi = Introspector.getBeanInfo(childCell);
        }
        catch(IntrospectionException ex)
        {
            Err.error(ex.toString());
        }
        pds = bi.getPropertyDescriptors();
        /*
        * As this is called during validation, first of all check that
        * the property can be read and written:
        */
        for(int i = 0; i <= pds.length - 1; i++)
        {
            // Err.pr("looking at possible child ref: " + pds[i].getName());
            Method readMethod = pds[i].getReadMethod();
            if(pds[i].getName().equals(parentOrChildField)
                && !isExtent(readMethod.getReturnType()))
            {
                refRelationshipFound = true;
                // Err.pr("child reference: " + pds[i].getName());
                result.type = Tie.CHILD_REFERENCE;
                result.readMethod = readMethod;
                result.writeMethod = pds[i].getWriteMethod();
                result.clazz = pds[i].getPropertyType();
                if(result.readMethod == null)
                {
                    Err.error(
                        "Cannot read property: " + parentOrChildField + " in " + childCell);
                }
                if(result.writeMethod == null)
                {/*
           * Will not always be an error. May need the concept of a RO tie.
           */// Err.pr("WARNING NOT ERROR (2) Cannot write method property: " + parentOrChildField +
                    // " in " + childCell);
                }
                if(result.clazz == null)
                {
                    Err.error(
                        "Property <" + parentOrChildField
                            + "> is an indexed property that does not support non-indexed access");
                }
                break;
            }
        }
        if(parentCell == null)
        {
            Err.error("Object " + parentObj + " doesn't have an instantiable");
        }
         /**/

        if(collectRelationshipFound && refRelationshipFound)
        {
            /*
            Err.error( "Yet to implement collectRelationshipFound && refRelationshipFound");
            result.type = Tie.BOTH;
            */
            /*
            * Lets assume it's a reference - for example the recursive
            * relationship is normally modelled this way. Note that
            * whilst both will be done, the child reference will overwrite.
            */
            Err.error(
                "WARNING NOT ERROR, assuming "
                    + "CHILD_REFERENCE when have PARENT_LIST " + "too for parentObj: "
                    + parentObj + " childObj: " + childObj + " over: "
                    + parentOrChildField);
            result.type = Tie.CHILD_REFERENCE;
            Err.stack();
        }
        if(!(collectRelationshipFound || refRelationshipFound))
        {
            result.type = Tie.CANT_CREATE;
        }
        result.strField = parentOrChildField;
        result.childObject = childObj;
        result.parentObject = parentObj;
        return result;
    }

    public Class getFieldType()
    {
        Class result = null;
        if(getType() == PARENT_LIST && parentType != null)
        {
            result = parentType;
        }
        else if(getType() == CHILD_REFERENCE && childType != null)
        {
            result = childType;
        }
        else
        {
            Err.error(
                "Only PARENT_LIST and CHILD_REFERENCE supported,"
                    + " and Fields must have been set in constructor");
        }
        return result;
    }

    private Method getReadMethod()
    {
        Method method = null;
        if(getType() == PARENT_LIST && parentReadMethod != null)
        {
            method = parentReadMethod;
        }
        else if(getType() == CHILD_REFERENCE && childReadMethod != null)
        {
            method = childReadMethod;
        }
        else
        {
            Err.error(
                "Only PARENT_LIST and CHILD_REFERENCE supported, and Read Method must be set");
        }
        return method;
    }

    private Method getWriteMethod()
    {
        Method method = null;
        int type = getType();
        if(type == PARENT_LIST && parentWriteMethod != null)
        {
            method = parentWriteMethod;
        }
        else if(type == CHILD_REFERENCE && childWriteMethod != null)
        {
            method = childWriteMethod;
        }
        else if(type != PARENT_LIST && type != CHILD_REFERENCE)
        {
            Err.error(
                "Only PARENT_LIST and CHILD_REFERENCE supported");
        }
        else
        {
            Err.pr( "type: <" + type + ">");
            Err.pr( "parentWriteMethod: <" + parentWriteMethod + ">");
            Err.pr( "childWriteMethod: <" + childWriteMethod + ">");
            Err.pr( "parentReadMethod: <" + parentReadMethod + ">");
            Err.pr( "childReadMethod: <" + childReadMethod + ">");
            Err.error( "Write method required for " + this);
        }
        return method;
    }

    /**
     * Supposed to be able to return null.
     */
    public Object getFieldValue(Object object)
    {
        super.getFieldValue(object);

        /*
        times++;
        Err.pr( "To getReadMethod() times " + times);
        if(times >= 0)
        {
        Err.debug();
        }
        */
        Object result = SelfReferenceUtils.invoke( object, getReadMethod());
        if(result == null)
        {
            Err.pr( SdzNote.BAD_READING, "Read Method not working on a <" + object.getClass().getName() + ">");
            Err.pr( SdzNote.BAD_READING, "ID: " + ((IdentifierI)object).getId());
            Err.pr( SdzNote.BAD_READING, "Using read method: <" + getReadMethod() + ">");
        }
        else
        {
            Err.pr( SdzNote.BAD_READING, "Read worked: <" + result + ">");
            Err.pr( SdzNote.BAD_READING, "On a <" + object.getClass().getName() + ">, using read method: <" + getReadMethod() + ">");
        }
        return result;
    }

    public void setFieldValue(Object object, Object value)
    {
        super.setFieldValue(object, value);
        //args[0] = value;
        SelfReferenceUtils.invoke(object, getWriteMethod(), value);
    }
    
    public void setChild(HasCombinationExtent childObj)
    {
        super.setChild(childObj);

        // Err.pr( "In setChild");
        int type = getType();
        if(type == PARENT_LIST)
        {
            Err.pr(SdzNote.EM_BECOMES_NULL, "To call setDependentExtent for " + childObj + ", rel done by " + parentType);
            setDependentExtent( childObj, parentType);
        }
        /*
        else
        {
        Err.error( "Not setting child b/c type is " + type);
        }
        */
    }
}
