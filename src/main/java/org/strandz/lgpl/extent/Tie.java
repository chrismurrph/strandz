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
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.NamedI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.note.SdzNote;

/**
 * General class for linking 2 objects together. Used to provide the links
 * for both M/D and lookup relationships.
 * <p/>
 */
abstract public class Tie
{
    String parentField;
    String childField;
    static final int CANT_CREATE = -1;
    public static final int PARENT_LIST = 1;
    public static final int CHILD_REFERENCE = 2;
    public static final int BOTH = 3;
    public static final int TOP_LEVEL = 4;
    // no value needed private int type;

    NamedI parentObj;
    NamedI childObj;
    // When we want to dynamically get the child extent ie fire the
    // trigger, we use this interface
    ChildExtentGetterI childExtentGetter;
    DependentExtent dependentExtent; // only used by Tie that has a many side
    EntityManagerProviderI entityManagerProviderI;
    private InsteadOfAddRemoveTrigger addRemoveTrigger;
    boolean topLevel = false;
    private static int times = 0;

    Tie()
    {
        //Used by OrphanTie
    }
    
    Tie(EntityManagerProviderI entityManagerProviderI, InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        this.entityManagerProviderI = entityManagerProviderI;
        this.addRemoveTrigger = addRemoveTrigger;
    }

    /**
     *
     */
    public static Tie createTie(NamedI parentObj, // containerNode
                                NamedI childObj, // fieldNode
                                Class parentCell, // Container.class
                                Class childCell, // Component.class
                                String parentOrChildField, // "components"
                                EntityManagerProviderI entityManagerProvider,
                                InsteadOfAddRemoveTrigger insteadOfAddRemoveTrigger)
    {
        Tie tie = null;
        FieldDescriptor fd = FieldTie.canCreate(parentObj, childObj, parentCell,
                                                childCell, parentOrChildField);
        if(fd.type == CANT_CREATE)
        {
            MethodDescriptor md = MethodTie.canCreate(parentObj, childObj, parentCell,
                                                      childCell, parentOrChildField);
            if(md.type != CANT_CREATE)
            {
                tie = new MethodTie(md, entityManagerProvider, insteadOfAddRemoveTrigger);
            }
            else
            {
                new MessageDlg(
                    "Have not found fields/methods for " + parentOrChildField + " in "
                        + parentCell + " or " + childCell);
                Err.error(
                    "Have not found fields/methods for " + parentOrChildField + " in "
                        + parentCell + " or " + childCell);
            }
        }
        else
        {
            tie = new FieldTie(fd, entityManagerProvider, insteadOfAddRemoveTrigger);
        }
        // Err.pr( "### CREATED Tie: " + tie);
        return tie;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        String ref = null;
        if(!(this instanceof OrphanTie))
        {
            if(getType() == CHILD_REFERENCE)
            {
                ref = "C:" + childField;
            }
            else
            {
                ref = "P:" + parentField;
            }
            result.append("[Tie, parent<" + parentObj.getName() + ">");
            // result.append(pUtils.separator);
            result.append(",");
            result.append("child<" + childObj.getName() + ">");
            result.append(",");
            result.append("ref<" + ref + ">]");
        }
        else
        {
            result.append("[Tie, ORPHAN]");
        }
        return result.toString();
    }

    public Object getParent()
    {
        return parentObj;
    }

    /**
     * setParent and setChild are used to interchange the current nodes with
     * another set of nodes.
     */
    public void setParent(NamedI parentObj)
    {
        if(parentObj == null)
        {
            Err.error("Tie: parentObj cannot be set to null");
        }
        this.parentObj = parentObj;
    }

    public Object getChild()
    {
        return childObj;
    }

    /**
     * It is more likely that when reset the node that the
     * replacement child object will have the combination extent.
     * This version is used for the 'multiple parent' uses of a
     * Tie.
     */
    public void setChild(HasCombinationExtent childObj)
    {
        if(childObj == null)
        {
            Err.error("Tie: childObj cannot be set to null");
        }
        this.childObj = childObj;
    }

    /**
     * Used for simpler Ties where DependentExtent is not used.
     */
     /**/
    public void setChild(NamedI childObj)
    {
        if(childObj == null)
        {
            Err.error("Tie: childObj cannot be set to null");
        }
        this.childObj = childObj;
    }
     /**/

    /**
     * Called internally for an aggregate Tie, and externally from user
     * calling setInitialData for a reference Tie. Setting the MANY side.
     * Is called both where the master does not have a collection
     * ie./ when the master-detail relationship is a fk style one.
     * (Tie.CHILD_REFERENCE) AND Tie.PARENT_LIST where what is passed in
     * is an AggregationExtent.
     */
     /**/
    public void setDependentExtent(DependentExtent de)
    {
        if(de == null)
        {
            Err.error("setDependentExtent cannot be called with null param");
        }
        this.dependentExtent = de;
    }

     /**/

    DEErrorContainer getDependentExtentErrorContainer()
    {
        DEErrorContainer result = new DEErrorContainer(this);
        if(childExtentGetter != null)
        {
            //This causes a data flow event to make dependentExtent be filled up
            //Won't have one for a Tie.PARENT_LIST and the start of complex stuff
            // for a Tie.CHILD_REFERENCE
            childExtentGetter.preCauseToSetChildsDependentExtent();
        }
        if(dependentExtent == null)
        {
            /*
            * Lets allow to return null and
            *
            Err.error("setDependentExtent not yet been called" +
            " - setInitialData may not have been called on child of relationship:" + this);
            */
            result.setInError(true);
            result.setProblemChild(getChild());
        }
        else
        {
            result.setDependentExtent(dependentExtent);
            if(childExtentGetter != null)
            {
                childExtentGetter.postCauseToSetChildsDependentExtent();
            }
        }
        return result;
    }

    public String getColumnName()
    {
        String result = null;
        if(getType() == CHILD_REFERENCE)
        {
            result = childField;
        }
        else if(getType() == PARENT_LIST)
        {
            result = parentField;
        }
        return result;
    }

    public int getType()
    {
        int result = -99;
        if(parentField == null && childField == null)
        {
            if(topLevel)
            {
                return TOP_LEVEL;
            }
            else
            {
                Err.error("To early to call getType");
            }
        }
        else if(parentField != null && childField != null)
        {
            Err.error("BOTH not yet supported");
            result = BOTH;
        }
        else if(parentField != null && childField == null)
        {
            result = PARENT_LIST;
        }
        else if(parentField == null && childField != null)
        {
            result = CHILD_REFERENCE;
        }
        return result;
    }

    abstract public Class getFieldType();

    /**
     * This method always called from a subclass
     */
    public Object getFieldValue(Object object)
    {
        if(object == null)
        {
            Err.error("No point calling getItemValue() with a null object");
        }
        return null; // for compiler
    }

    public void setFieldValue(Object object, Object value)
    {
        // new MessageDlg("In tie.setFieldValue using <"
        // + object + "> and <" + value + ">");
        if(object == null)
        {
            Err.error("No point calling setFieldValue for a NULL object");
        }
        if(value == null)
        {
            Err.error("No point calling setFieldValue with a NULL value");
        }
    }

    public static boolean isExtent(Class clazz)
    {
        boolean result = false;
        if(NavExtent.ascertainType(clazz) != -1)
        {
            result = true;
        }
        return result;
    }

    public void setChildExtentGetter(ChildExtentGetterI childExtentGetter)
    {
        this.childExtentGetter = childExtentGetter;
    }

    public String getChildField()
    {
        return childField;
    }

    public InsteadOfAddRemoveTrigger getAddRemoveTrigger()
    {
        return addRemoveTrigger;
    }
    
    void setDependentExtent( HasCombinationExtent childObj, Class parentType)
    {
        NodeGroup nodeGroup = childObj.getNodeGroup();
        AggregationExtent aggregationExtent = null;
        CombinationExtent combinationExtent = null;
        if(nodeGroup != null)
        {
            aggregationExtent = nodeGroup.getAggregationExtent();
            combinationExtent = nodeGroup.getCombinationExtent(); 
        }
        if(aggregationExtent == null)
        {
            aggregationExtent = NavExtent.createAggregation(parentType, childObj, entityManagerProviderI, 
                                                            getAddRemoveTrigger());
            combinationExtent = new CombinationExtent();
            Err.pr( SdzNote.NODE_GROUP, "New aggregationExtent used for " + childObj + " named <" + childObj.getName() + ">");
            if(nodeGroup != null)
            {
                Assert.isNull( nodeGroup.getAggregationExtent());
                nodeGroup.setAggregationExtent( aggregationExtent);
                nodeGroup.setCombinationExtent( combinationExtent);
            }
        }
        else
        {
            Err.pr( SdzNote.NODE_GROUP, "Existing aggregationExtent used for " + childObj + " named <" + childObj.getName() + ">");
            //found an already created AggregationExtent in the NodeGroup    
        }
        Assert.notNull( aggregationExtent);
        setDependentExtent( aggregationExtent);
        childObj.setCombinationExtent( combinationExtent);
        //Err.stack( SdzNote.NODE_GROUP);
    }
}
