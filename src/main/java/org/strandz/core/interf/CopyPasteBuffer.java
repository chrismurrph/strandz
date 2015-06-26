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

import org.strandz.core.prod.AttributeValueSaverI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used as a storage buffer when you want to perform
 * copy and paste operations on strandz screens. For example this code would
 * take a copy of all the values on the current screen:
 * <p/>
 * <code>
 * Node node = strand.getCurrentNode();
 * List attribs = pPaneControlsUtils.getAllAttributes( nodes);
 * copyPasteBuffer.copyItemValues( attribs, node);
 * </code>
 * <p/>
 * To paste just call <code>pasteItemValues()</code>
 *
 * @author Chris Murphy
 * @see #copyItemValues( List allAttributesForCopy, Node currentNode)
 * @see #pasteItemValues()
 */
public class CopyPasteBuffer implements AttributeValueSaverI
{
    // These 2 s/be changed to a HashSet keyed by attribute
    private List allAttributesForCopy;
    private List copiedControlValues;
    private TableDataCopyPasteHelper tdm = new TableDataCopyPasteHelper();
    private static boolean debug = SdzNote.NV_PASTE_NOT_WORKING.isVisible();
    private static int times;

    private static boolean attribOnNode(TableAttributeI tableAttribute,
                                        Object node)
    {
        boolean result = false;
        if(tableAttribute.getCell().getNode() == node)
        {
            result = true;
        }
        return result;
    }

    private static void pr(String s)
    {
        if(debug)
        {
            Err.pr(s);
        }
    }

    private static List getItemList(TableAttributeI attribute)
    {
        List result = null;
        /*
        times++;
        if(times == 4)
        {
          Err.debug();
        }
        */
        result = attribute.getItemList();
        if(result == null)
        {
            Err.pr("times: " + times);
            Err.error("Failed to getItemList() (null return) from " + attribute);
        }
        else if(result.isEmpty())
        {
            //Won't stay as an error
            Err.pr("Failed to getItemList() (empty return) from " + attribute);
        }
        return result;
    }
    
    public String toString()
    {
        String result;
        String attributes = Utils.getStringBufferFromList(Print.getPrList(allAttributesForCopy, "Attributes", new ArrayList())).toString();
        String values = Utils.getStringBufferFromList(Print.getPrList(copiedControlValues, "Values", new ArrayList())).toString();
        result = attributes + Utils.NEWLINE + values;
        return result;
    }

    /**
     * If the attribute we are copying is on a detail node then we add the
     * whole list to copiedControlValues
     *
     * @param tableAttribute
     * @param currentNode
     */
    private void addToValueStore(TableAttributeI tableAttribute, Node currentNode)
    {
        if(!attribOnNode(tableAttribute, currentNode))
        {
            copiedControlValues.add(getItemList(tableAttribute));
        }
        else
        {
            List list = getItemList(tableAttribute);
            if(!list.isEmpty())
            {
                copiedControlValues.add(list.get(0));
            }
        }
    }

    public void copyItemValues(List allAttributesForCopy,
                               Node currentNode)
    {
        copiedControlValues = new ArrayList();

        //List attribs = pUtils.getSubList( allAttributesForCopy,
        //    RuntimeAttribute.class);
        List attribs = removeSuperflousAttributes(allAttributesForCopy);
        if(attribs.isEmpty())
        {// If no non-blank changed attributes then this will happen
            // Err.error( "How come no attributes to copy?, started off with " + allAttributesForCopy.size() + " in " + currentNode);
        }
        for(Iterator iter = attribs.iterator(); iter.hasNext();)
        {
            Object obj = iter.next();
            /*
            if(!(obj instanceof RuntimeAttribute))
            {
            Session.error( "Do not support copy for a " +
            obj.getClass().getName() +
            " called " + ((Attribute)obj).getDOField());
            }
            */
            RuntimeAttribute attribute = (RuntimeAttribute) obj;
            pr("## TO COPY, attribute called: " + attribute.getName());
            if(attribute.getName() != null && attribute.getName().equals("weekInMonth Name"))
            {
                pr("Copying weekInMonth Name");
            }
            pr("## TO COPY, attribute type: " + attribute.getClass());
            /*
            if(pUtils.instanceOf( attribute, TableAttribute.class))
            {
              TableAttribute tableAttribute = (TableAttribute)attribute;
              if(!attribOnNode( tableAttribute, currentNode))
              {
                copiedControlValues.add( getItemList( tableAttribute));
              }
              else
              {
                List list = getItemList( tableAttribute);
                if(!list.isEmpty())
                {
                  copiedControlValues.add( list.get( 0));
                }
              }
            }
            else if(pUtils.instanceOf( attribute, NonVisualTableAttribute.class))
            {
              NonVisualTableAttribute tableAttribute = (NonVisualTableAttribute)attribute;
              if(!attribOnNode( tableAttribute, currentNode))
              {
                copiedControlValues.add( getItemList( tableAttribute));
              }
              else
              {
                List list = getItemList( tableAttribute);
                if(!list.isEmpty())
                {
                  copiedControlValues.add( list.get( 0));
                }
              }
            }
            */
            if(Utils.instanceOf(attribute, TableAttributeI.class))
            {
                addToValueStore((TableAttributeI) attribute, currentNode);
            }
            else //if(!(attribute instanceof ReferenceLookupAttribute))
            //won't be as have removed superfluous
            {
                Object value = attribute.getItemValue();
                String extra = " in " + attribute.getItemAdapter().getName() + ", TYPE: " + attribute.getClass();
                if(value == null)
                {
                    pr("## NULL ITEM VAL: " + value + extra);
                }
                else
                {
                    pr("## ITEM VAL: " + value + " of type " + value.getClass().getName() + extra);
                }
                if(value != null && value.toString() != null)
                {
                    if(value.toString().equals("ml.applichousing.OneRowSdzBag"))
                    {
                        Err.error("STACK to fix problem");
                    }
                }
                copiedControlValues.add(value);
            }
        }
        // if(attribs.size() != copiedControlValues.size())
        // {
        // Err.error( "Strange that not coping same number of attributes that have");
        // }
        this.allAttributesForCopy = attribs;
    }

    private static List removeSuperflousAttributes(List result)
    {
        //Actually everything is assignable from a StemAttribute so this method would not
        //be the way to try and achieve what we were trying to do here
        //result = pUtils.getSubList( result, StemAttribute.class, false);
        result = Utils.getSubList(result, RuntimeAttribute.class);
        result = Utils.getSubList(result, ReferenceLookupAttribute.class, Utils.EXCLUDE);
        if(debug) Print.prList(result, "Does exclude ReferenceLookupAttributes and StemAttributes?");
        return result;
    }

    private static class AttributeValue implements Comparable
    {
        private RuntimeAttribute attribute;
        private Object value;

        private AttributeValue(RuntimeAttribute attribute, Object value)
        {
            this.attribute = attribute;
            this.value = value;
            //Causes JDO to have a nonTransactionalRead
            //This happens just when commit/reload a volunteer panel
            //## ITEM VAL: null [NULL] of type org.strandz.data.wombatrescue.objects.Volunteer in belongsToGroup, TYPE: class org.strandz.core.interf.ReferenceLookupAttribute
            //The item parts of some attributes is in fact a DO. Whether this should be the case is a matter for
            //an investigation. In this experiment don't copy across the ReferenceLookupAttributes and see
            //if form is still functional.  Also look at places where ReferenceLookupAttribute is actually
            //relied upon in code. See removeSuperflousAttributes - experiment already happening.
            //NOW THINK - Should be fine as ReferenceLookupAttribute will be in place from say INSERT
            //when doing a copy/paste, and is prolly meaningless in that special Enter mode.
            if(debug) chk(true);
        }

        private void chk(boolean warnOnly)
        {
            Err.pr("Trying: " + value + " of type " + attribute.getClass().getName());
            if(value == null)
            {
                if(
                    Utils.instanceOf(attribute, TableAttributeI.class)
                    )
                {
                    String txt = "Expect the value to paste to be a List for a " + attribute.getClass().getName() + ", got null";
                    if(warnOnly)
                    {
                        Err.alarm(txt); //This happens when have no data then decide to execute query. Seems
                        //like executing the query would be pointless in this circumstance, but
                        //that is another note to fix another day TODO
                    }
                    else
                    {
                        Err.error(txt);
                    }
                }
            }
            else
            {
                Err.pr("TYPE IS: " + value.getClass().getName());
            }
        }

        public int compareTo(Object o)
        {
            AttributeValue other = (AttributeValue) o;
            RuntimeAttribute otherAttr = other.attribute;
            return attribute.compareTo(otherAttr);
        }

        public String toString()
        {
            return attribute + "-||-" + value;
        }
    }

    /**
     * Useful for debugging so can see the names of the attributes
     * before call say getItemList().
     */
    public void prAllAttributesForCopy()
    {
        Print.prList(allAttributesForCopy, "These attributes are in the buffer");
    }

    public void pasteItemValues()
    {
        tdm.clear();
        if(copiedControlValues != null)
        {
            // Some items may have an effect on others, for example
            // enabling/disabling them. For this reason we sort by
            // the ordinal. User can specify the attribute's ordinal
            // at design time.
            List<AttributeValue> together = new ArrayList<AttributeValue>();
            int i = 0;
            List attribs = allAttributesForCopy;
            //Use method removeLookups() to achieve anything like this:
            // List attribs = pUtils.getSubList( allAttributesForCopy, RuntimeAttribute.class);
            // attribs = pUtils.getSubList( attribs, ReferenceLookupAttribute.class, false);
            if(attribs.size() != copiedControlValues.size())
            {
                Err.error("attribs.size is " + attribs.size() + " while copiedControlValues.size() is " + copiedControlValues.size());
            }
            for(Iterator iter = copiedControlValues.iterator(); iter.hasNext(); i++)
            {
                Object val = iter.next();
                RuntimeAttribute attr = (RuntimeAttribute) attribs.get(i);
                AttributeValue attVal = new AttributeValue(attr, val);
                together.add(attVal);
            }
            Collections.sort(together);
            // Print.prList( together);
            for(Iterator iter = together.iterator(); iter.hasNext();)
            {
                AttributeValue attVal = (AttributeValue) iter.next();
                if(Utils.instanceOf(attVal.value, List.class))
                {
                    /*
                    if(((List) attVal.value).isEmpty())
                    {
                        Err.pr(attVal.attribute.getClass().getName() + ", " + attVal.attribute.getDOField() + " is empty");
                    }
                    else
                    {
                        Err.pr(attVal.attribute.getClass().getName() + ", " + attVal.attribute.getDOField() + " is NOT empty");
                    }
                    */
                    tdm.addColumnData((List) attVal.value, (TableAttributeI)attVal.attribute);
                }
                else
                {
                    attVal.attribute.setItemValue(attVal.value);
                }
            }
        }
        tdm.setControlValues();
        copiedControlValues = null;
    }

    /**
     * First of a few convenience methods can use to get access to the
     * buffered data, and thus change it before it is pasted back to the
     * panel.
     * <p/>
     * Here we collect a list of the data in a particular table column.
     */
    public List getTableColumn(String attrName)
    {
        List result = null;
        if(copiedControlValues != null)
        {
            Iterator iter1 = copiedControlValues.iterator();
            for(Iterator iter2 = allAttributesForCopy.iterator(); iter2.hasNext();)
            {
                Object value = iter1.next();
                Attribute attribute = (Attribute) iter2.next();
                /*
                Err.pr( "Trying: " + value);
                Err.pr( "Is a: " + value.getClass().getName());
                Err.pr( "Attribute (getDOField()): " + attribute.getDOField());
                Err.pr( "Attribute (getName()): " + attribute.getName());
                */
                if(Utils.instanceOf(value, List.class))
                {
                    if(attribute.getDOField().equals(attrName)
                        || attribute.getName().equals(attrName))
                    {
                        result = (List) value;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public Object getItemValue(String attrName)
    {
        Object result = null;
        if(copiedControlValues != null)
        {
            Iterator iter1 = copiedControlValues.iterator();
            for(Iterator iter2 = allAttributesForCopy.iterator(); iter2.hasNext();)
            {
                Object value = iter1.next();
                Attribute attribute = (Attribute) iter2.next();
                /*
                Err.pr( "Trying: " + value);
                Err.pr( "Is a: " + value.getClass().getName());
                Err.pr( "Attribute (getDOField()): " + attribute.getDOField());
                Err.pr( "Attribute (getName()): " + attribute.getName());
                */
                if(!Utils.instanceOf(value, List.class))
                {
                    if(// attribute.getDOField().equals( attrName) ||
                        attribute.getName().equals(attrName))
                    {
                        result = value;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public void setFieldValue(String attrName, Object value)
    {
        if(copiedControlValues != null)
        {
            int i = 0;
            for(Iterator iter1 = allAttributesForCopy.iterator(); iter1.hasNext(); i++)
            {
                Attribute attribute = (Attribute) iter1.next();
                /*
                Err.pr( "Attribute (getDOField()): " + attribute.getDOField());
                Err.pr( "Attribute (getName()): " + attribute.getName());
                */
                if(attribute.getName() == null)
                {
                    Err.error("Attribute has not been named: " + attribute.getDOField());
                }
                if(attribute.getDOField().equals(attrName)
                    || attribute.getName().equals(attrName))
                {
                    copiedControlValues.set(i, value);
                }
            }
        }
    }
}
