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

import org.strandz.core.domain.AbstractCell;
import org.strandz.core.domain.Independent;
import org.strandz.core.domain.SdzBeanI;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.lgpl.data.objects.Property;
import org.strandz.lgpl.data.objects.PropertyI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.note.SdzDsgnrNote;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Concrete implementations often represent the mapping between a control
 * that may be seen on the screen (called an Item), and a field in a Data
 * Object.
 * <p/>
 * There is actually a class hierarchy of Attributes. All belong to
 * a Cell and are associated with a DO field. When moving down the class
 * hierarchy it is in the RuntimeAttribute that an Item is first found.
 *
 * @author Chris Murphy
 */
abstract public class Attribute implements SdzBeanI, Comparable
{
    List validateBeanMsg = new ArrayList();
    String dataFieldName;
    Cell cell;
    String name;
    private Integer ordinal;
    
    private static int times;
    private static int timesName;
    private static int timesCC;
    private static int constructedTimes;
    int id;
    public static String NO_FIELD_NAME_STR = "(field name)";
    public static String NO_FIELD_TYPE_STR = "field type";

    public Attribute()
    {
        constructedTimes++;
        id = constructedTimes;
        /*
        Err.pr( "\t***** constructed Attribute ID: " + id);
        if(id == 0)
        {
        Err.stack();
        }
        */
        ordinal = new Integer(-1);
    }

    public int getId()
    {
        return id;
    }

    public Attribute(Attribute attrib)
    {
        this();
        if(attrib == null)
        {
            Err.error("Attribute constructor param cannot be null");
        }
        setDOField(attrib.getDOField());
        setCell(attrib.getCell());
        /*
        * Problem is that correctly doing the name is probably not being done
        * everywhere that create a new Attribute of every type - so debug this
        * sometime
        */
        // Do outside of here
        // setName( attrib.getCell().getName() + " " + attrib.getDOField());
        // Err.pr( "Attr copy-constructor will give name " +
        // attrib.getName() + " times " + timesCC);
        setName(attrib.getName());
        setOrdinal( attrib.getOrdinal());
    }

    public Attribute(String dataFieldName, Integer ordinal)
    {
        this();
        if(dataFieldName == null)
        {
            Err.error("dataFieldName constructor param cannot be null");
        }
        this.dataFieldName = dataFieldName;
        this.name = dataFieldName;
        setOrdinal( ordinal);
    }

    public void set(Attribute attribute)
    {
        setName(attribute.getName());
        setCell(attribute.getCell());
        setDOField(attribute.getDOField());
        setOrdinal( attribute.getOrdinal());
    }

    public int hashCode()
    {
        int result = 17;
        if(getName() != null)
        {
            result = 37 * result + getName().hashCode();
        }
        if(getDOField() != null)
        {
            result = 37 * result + getDOField().hashCode();
        }
        return result;
    }

    public boolean equals(Object o)
    {
        boolean result = false;
        Attribute test = (Attribute) o;
        if((getName() == null
            ? test.getName() == null
            : getName().equals(test.getName())))
        {
            if((getDOField() == null
                ? test.getDOField() == null
                : getDOField().equals(test.getDOField())))
            {
                result = true;
            }
        }
        return result;
    }

    public void setCell(Cell cell)
    {
        // Err.pr( "!! Attribute.setting cell to: " + cell);
        if(cell == null)
        {
        }
        this.cell = cell;
        /*
        if(name.equals( dataFieldName))
        {
        name = cell.getName() + dataFieldName;
        }
        */
    }

    public Cell getCell()
    {
        // Err.pr( "!! Attribute.getting cell: " + cell);
        /*
        if(cell == null)
        {
        times++;
        Err.pr( "!! Cell null for attribute with name: " + name + " times " + times);
        }
        */
        return cell;
    }

    public String getDOField()
    {
        return dataFieldName;
    }

    public void setDOField(String v)
    {
        /*
        //if(v.equals( "name"))
        {
        times++;
        Err.pr( "Attribute.setDOField: " + v + " ID: " + id + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        }
        */
        dataFieldName = v;
    }

    public Class getDOFieldType()
    {
        Class result = null;
        AbstractCell cell = getCell();
        Class clazz = cell.getClazz().getClassObject();
        if(clazz == null)
        {
            Err.error(
                "This attribute's (" + toString()
                    + ") cell does not have a clazz, so the DOs field type will not be able to be determined");
        }

        String dOField = getDOField();
        if(dOField == null)
        {// Unless can think of something else, this will always
            // occur when are adding a brand new attribute.
            // Err.error( "This attribute (" + toString() + ") has not been assigned a DO field, so can't determine the field's type");
        }
        result = SelfReferenceUtils.getPropertyType(clazz, dOField);
        return result;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String s)
    {
        /*
        timesName++;
        Err.pr( "Attr will set name to " + s + " times " + timesName);
        if( s.equals( "RosterSlot DayInWeek")
        //timesName == 0
        )
        {
        Err.debug();
        }
        */
        name = s;
    }

    public boolean inDetailNode()
    {
        return cell.inDetailNode();
    }

    public boolean notMasterNode(Object node)
    {
        return cell.notMasterNode(node);
    }

    public String toString()
    {
        String res = new String();
        res = res + "[Attribute dataFieldName: <" + dataFieldName + ">, "
            + "cell: <" + cell + ">, " + "name: <" + name + ">]";
        return res;
    }

    /**
     * If we are putting a control onto an Attribute then we need to change
     * the type of Attribute into a FieldAttribute. By the time are here will
     * have already validated that Node does not have a TableControl.
     */
    public static Attribute setControlOnAttribute(Component comp, Attribute attribute)
    {
        Attribute result = null;
        // Err.pr( attribute.getClass().getName() + " needs a " + comp.getClass().getName());
        Cell cell = attribute.getCell();
        FieldAttribute fa = null;
        StemAttribute sa = null;
        if(comp != null)
        {
            if(attribute.getClass() == StemAttribute.class)
            {
                fa = new FieldAttribute((StemAttribute) attribute);
                // Looks like being given a default name elsewhere, so lets always
                // do it.
                // if(fa.getName() == null)
                /*
                * Just confusing to not keep the name of the StemAttribute.
                * BUT - was done to stop name conflicts, thus we need to also
                * put the name back to attribute.getDOField() when we create
                * a StemAttribute from a RuntimeAttribute.
                */
                // attribute naming convention change
                /*
                fa.setName(
                pUtils.firstWord( attribute.getCell().getName()) + " "
                + pUtils.upperCaseFirstChar( attribute.getDOField()));
                */
            }
            else
            {
                fa = (FieldAttribute) attribute;
            }
            fa.setItem(comp);
            result = fa;
        }
        else
        {
            if(Utils.instanceOf(attribute, FieldAttribute.class))
            {
                sa = new StemAttribute(attribute);
            }
            else
            {
                sa = (StemAttribute) attribute;
            }
            result = sa;
        }
        /*
        * Need replace attribute with fa from Cell's pov
        * (Only when created a new FieldAttribute)
        */
        // Err.pr( "Will be replacing attribute " + attribute + " with " + result);
        cell.replaceAttribute(attribute, result);
        // Err.pr( "Done replacing");
        return result;
    }

    public static StemAttribute toStemAttribute(Attribute attribute, String reason)
    {
        Err.pr(SdzDsgnrNote.TO_STEM, 
               "Creating a StemAttribute from <" + attribute.getName() + "> because <" + reason + ">");
        StemAttribute stemAttribute = new StemAttribute(attribute);
        Cell cell = attribute.getCell(); // could get from either
        cell.replaceAttribute(attribute, stemAttribute);
        // See ReferenceContainer.setControlOnAttribute() for where
        // the opposite of this occurs ie. the name is set to a longer
        // version which includes the name of the cell.
        // attribute naming convention change
        /*
        stemAttribute.setName( attribute.getDOField());
        */
        return stemAttribute;
    }

    public static TableAttribute toTableAttribute(Attribute attribute, String reason)
    {
        Err.pr(SdzDsgnrNote.TO_TABLE, 
               "Creating a TableAttribute from <" + attribute.getName() + "> because <" + reason + ">");
        TableAttribute tableAttribute = new TableAttribute(attribute);
        Cell cell = tableAttribute.getCell();
        cell.replaceAttribute(attribute, tableAttribute);
        /*
        if(tableAttribute.getAdapter() == null)
        {
        Err.error( "Can't create a TableAttribute that doesn't have an Adapter");
        }
        */
        return tableAttribute;
    }

    public static ReferenceLinkAttribute toReferenceLinkAttribute(
        Attribute attribute, Independent independent)
    {
        ReferenceLinkAttribute referenceLinkAttribute = new ReferenceLinkAttribute(
            attribute, independent);
        Cell cell = referenceLinkAttribute.getCell();
        cell.replaceAttribute(attribute, referenceLinkAttribute);
        return referenceLinkAttribute;
    }

    /*
    * Not yet done thinking/coding for this to work.
    public static FieldAttribute toFieldAttribute(
    Attribute attribute, Independent independent)
    {
    FieldAttribute fieldAttribute =
    new FieldAttribute( attribute, independent);
    Cell cell = fieldAttribute.getCell();
    cell.replaceAttribute( attribute, fieldAttribute);
    return fieldAttribute;
    }
    */

    public static ReferenceLookupAttribute toReferenceLookupAttribute(
        Attribute attribute)
    {
        ReferenceLookupAttribute referenceLookupAttribute = new ReferenceLookupAttribute(
            attribute);
        Cell cell = referenceLookupAttribute.getCell();
        cell.replaceAttribute(attribute, referenceLookupAttribute);
        return referenceLookupAttribute;
    }

    public static CollectionLinkAttribute toCollectionLinkAttribute(
        Attribute attribute, Independent independent)
    {
        CollectionLinkAttribute collectionLinkAttribute = new CollectionLinkAttribute(
            attribute, independent);
        Cell cell = collectionLinkAttribute.getCell();
        cell.replaceAttribute(attribute, collectionLinkAttribute);
        return collectionLinkAttribute;
    }

    public static NonVisualAttribute toNonVisualAttribute(
        Attribute attribute)
    {
        Assert.notNull( attribute);
        NonVisualAttribute nonVisualAttribute = null;
        if(attribute instanceof TableAttribute)
        {
            nonVisualAttribute = new NonVisualTableAttribute(
                attribute);
        }
        else if(attribute instanceof FieldAttribute)
        {
            nonVisualAttribute = new NonVisualFieldAttribute(
                attribute);
        }
        else
        {
            Err.error( "Expected TableAttribute or FieldAttribute but got " + attribute.getClass().getName());
        }
        Cell cell = nonVisualAttribute.getCell();
        cell.replaceAttribute(attribute, nonVisualAttribute);
        return nonVisualAttribute;
    }
    
    public boolean validateBean(boolean childrenToo)
    {
        return validateBean();
    }

    public boolean validateBean()
    {
        validateBeanMsg.clear();

        boolean ok = true;
        if(getDOField() == null)
        {
            ok = false;
            validateBeanMsg.add(
                "Attribute " + getName() + " in cell " + cell + " does not have"
                    + " a DO field");
        }
        return ok;
    }

    public List retrieveValidateBeanMsg()
    {
        return validateBeanMsg;
    }

    public static List<RuntimeAttribute> getNonBlankChangedAttributes(Attribute attribs[])
    {
        List result = new ArrayList();
        List workWith = getChangedAttributes(attribs);
        for(Iterator iter = workWith.iterator(); iter.hasNext();)
        {
            RuntimeAttribute attribute = (RuntimeAttribute) iter.next();
            Object value = attribute.getItemValue();
            if(value instanceof String)
            {
                String strValue = (String) value;
                if(strValue != null && !strValue.equals(""))
                {
                    result.add(attribute);
                }
            }
        }
        return result;
    }

    public static List<RuntimeAttribute> getNonBlankAttributes(Attribute attribs[])
    {
        List result = new ArrayList();
        List workWith = Utils.getSubList( attribs, RuntimeAttribute.class);
        for(Iterator iter = workWith.iterator(); iter.hasNext();)
        {
            RuntimeAttribute attribute = (RuntimeAttribute) iter.next();
            Object value = attribute.getItemValue();
            if(value instanceof String)
            {
                String strValue = (String) value;
                if(!Utils.isBlank( strValue))
                {
                    result.add(attribute);
                }
            }
        }
        return result;
    }
    
    public static List<ItemAdapter> getAdapters(List attribs)
    {
        List result = new ArrayList();
        for(Iterator iter = Utils.getSubList(attribs, RuntimeAttribute.class).iterator(); iter.hasNext();)
        {
            RuntimeAttribute attribute = (RuntimeAttribute) iter.next();
            result.add(attribute.getItemAdapter());
        }
        return result;
    }

    public static List<RuntimeAttribute> getChangedAttributes(Attribute attribs[])
    {
        List result = new ArrayList();
        // Attribute attribs[] = getAttributes();
        for(Iterator iter = Utils.getSubList(attribs, RuntimeAttribute.class).iterator(); iter.hasNext();)
        {
            RuntimeAttribute attribute = (RuntimeAttribute) iter.next();
            if(true/* !(attribute instanceof ReferenceLookupAttribute)*/)
            {
                /*
                times++;
                Err.pr( "Examining " + attribute + " for change, times " + times);
                if(times == 14 || times == 27)
                {
                Err.debug();
                }
                */
                if(attribute.hasChanged())
                {
                    // Err.pr( attribute.getName() + " has changed (" + attribute.getDOField() + ")");
                    Object bi = attribute.getB4ImageValue();
                    Object ai = attribute.getItemValue();
                    /*
                    String biTxt = "";
                    String aiTxt = "";
                    if(bi != null)
                    {
                    biTxt = bi.getClass().getName();
                    }
                    if(ai != null)
                    {
                    aiTxt = ai.getClass().getName();
                    }
                    Err.pr( "\t BI: <" + bi + "> " + biTxt);
                    Err.pr( "\t AI: <" + ai + "> " + aiTxt);
                    Err.pr( ", ID: " + attribute.getAdapter().id);
                    */
                    if(bi == null && ai.toString() == null)
                    {
                        Err.error("Has changed supposed to make sure this never happens");
                    }
                    result.add(attribute);
                }
            }
        }
        return result;
    }

    public static List getProgrammaticallyChangedAttributes(Attribute attribs[])
    {
        List result = new ArrayList();
        for(Iterator iter = Utils.getSubList(attribs, RuntimeAttribute.class).iterator(); iter.hasNext();)
        {
            RuntimeAttribute attribute = (RuntimeAttribute) iter.next();
            if(attribute.hasProgrammaticallyChanged())
            {
                result.add(attribute);
            }
        }
        return result;
    }

    public String toShow()
    {
        /*
        times++;
        Err.pr( "Attribute.toShow() times " + times);
        if(times == 0)
        {
        Err.debug();
        }
        */
        String result = null;
        String fieldTypeTxt = "";
        if(getDOFieldType() != null)
        {
            // Err.pr( "doFieldType " + getDOFieldType() + " has name <" + getDOFieldType().getName() + ">");
            fieldTypeTxt = getDOFieldType().getName();
        }

        StringBuffer buf = new StringBuffer();
        buf.append("<html>");
        buf.append("<b>");
        if(getName() == null)
        {
            // Below not true
            // Err.alarm( "Think only case of this is where manually creating an attribute, got for " + this);
            // A case where have no name is ReferenceLookupAttribute
            // buf.append( getDOField());
            buf.append(NO_FIELD_NAME_STR);
        }
        else
        {
            buf.append(getName());
        }
        buf.append("</b>");
        buf.append(" [");
        // Err.pr( "fieldTypeTxt " + fieldTypeTxt);
        if(fieldTypeTxt.equals(""))
        {
            // For 3rd time, this will happen when inserting a new attribute!
            // Err.error( "Why no fieldTypeTxt for " + this);
            buf.append(NO_FIELD_TYPE_STR);
        }
        buf.append(fieldTypeTxt);
        buf.append("]");
        buf.append("</html>");
        result = new String(buf);
        // Happens all the time with right result - thus when the
        // type doesn't come out or when naming is bad, must be
        // due to a Swing note.
        // Err.pr( result);
        return result;
    }

    public Integer getOrdinal()
    {
        Assert.isTrue(ordinal >= -1);
        return ordinal;
    }

    public void setOrdinal(Integer ordinal)
    {
        Assert.isTrue(ordinal >= -1, "Ordinal for an attribute must be >= -1");
        this.ordinal = ordinal;
    }
    
    public int compareTo(Object o)
    {
        int result = 0;
        Attribute other = (Attribute) o;
        result = this.ordinal - other.ordinal;
        return result;
    }

    /**
     * Template property that when copy-constructed will be a value-holder  
     */
    public PropertyI getPropertyValue()
    {
        return null;
    }
}
