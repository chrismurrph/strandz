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

import org.strandz.lgpl.persist.EntityManagerProviderI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NamedI;
import org.strandz.lgpl.util.SelfReferenceUtils;

import java.lang.reflect.Field;

public class FieldTie extends Tie
{
    private Field parentFieldField, childFieldField;

    /**
     * Note that the Object params are the placeholders in the hierarchy, and
     * the class params are the classes of the objects that the placeholders
     * represent.
     */
    FieldTie(FieldDescriptor fd, 
             EntityManagerProviderI entityManagerProviderI, 
             InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        super( entityManagerProviderI, addRemoveTrigger);
        if(fd.type == PARENT_LIST)
        {
            parentFieldField = fd.field;
            parentField = fd.strField;
        }
        else if(fd.type == CHILD_REFERENCE)
        {
            childFieldField = fd.field;
            childField = fd.strField;
        }
        else
        {
            Err.error("Must be PARENT_LIST or CHILD_REFERENCE");
        }
        parentObj = fd.parentObject;
        childObj = fd.childObject;
    }

    static FieldDescriptor canCreate(
        NamedI parentObj,
        NamedI childObj,
        Class parentCell,
        Class childCell,
        String parentOrChildField)
    {
        FieldDescriptor result = new FieldDescriptor();
        if(parentObj == null || childObj == null)
        {
            Err.error(
                "Tie must be constructed with not null nodes par - " + parentObj);
        }

        boolean collectRelationshipFound = false;
        boolean refRelationshipFound = false;
        /*
        * childField checking s/be done first (here), as ME to parent field checking,
        * and doesn't matter that parentObj might not yet have been validated.
        */
        if(parentCell == null)
        {
            Err.error("Object " + parentObj + " doesn't have an instantiable");
        }

        Field parentFields[] = parentCell.getFields();
        // Err.pr( "Declared parentFields for " + c.toString() + " altogether " + parentFields.length);
        for(int i = 0; i <= parentFields.length - 1; i++)
        {
            // Err.pr( parentFields[i].getName() + ", " + parentFields[i].getType());
            if(parentFields[i].getName().equals(parentOrChildField)
                && isExtent(parentFields[i].getClass()))
            {
                // Err.pr( parentFields[i].getType().toString());
                collectRelationshipFound = true;
                result.type = Tie.PARENT_LIST;
                result.field = parentFields[i];
                break;
            }
        }
        if(childCell == null)
        {
            // this s/be impossible from Customizer:
            Err.error("Object " + childObj + " doesn't have an instantiable");
        }

        Field childFields[] = childCell.getFields();
        // new MessageDlg( "Declared childFields for " + c.toString() + " altogether " + childFields.length);
        for(int i = 0; i <= childFields.length - 1; i++)
        {
            if(childFields[i].getName().equals(parentOrChildField)
                && !isExtent(childFields[i].getClass()))
            {
                // new MessageDlg( "Reference Relationship: " +
                // childFields[i].getName() + ", " + parentOrChildField);
                refRelationshipFound = true;
                result.type = Tie.CHILD_REFERENCE;
                result.field = childFields[i];
                break;
            }
        }
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
                "WARNING NOT ERROR, assuming CHILD_REFERENCE when have PARENT_LIST too");
            result.type = Tie.CHILD_REFERENCE;
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

    private Field getField()
    {
        Field field = null;
        if(getType() == PARENT_LIST && parentFieldField != null)
        {
            field = parentFieldField;
        }
        else if(getType() == CHILD_REFERENCE && childFieldField != null)
        {
            field = childFieldField;
        }
        else
        {
            Err.error(
                "Only PARENT_LIST and CHILD_REFERENCE supported, and Field must be set");
        }
        return field;
    }

    public Class getFieldType()
    {
        return getField().getType();
    }

    public Object getFieldValue(Object object)
    {
        super.getFieldValue(object);
        return SelfReferenceUtils.getFieldValue(object, getField());
    }

    public void setFieldValue(Object object, Object value)
    {
        super.setFieldValue(object, value);
        SelfReferenceUtils.setFieldValue(object, getField(), value);
    }

    public void setChild(HasCombinationExtent childObj)
    {
        super.setChild(childObj);
        if(getType() == PARENT_LIST)
        {
            //new MessageDlg("To call setDependentExtent for a parent list");
            //setDependentExtent(
            //    NavExtent.createAggregation(parentFieldField.getType(), childObj, entityManagerProviderI, getAddRemoveTrigger()));
            setDependentExtent( childObj, parentFieldField.getType());            
        }
    }
}
