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
import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.ControlSignatures;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.LookupTiesManagerI;
import org.strandz.core.domain.NewInstanceTrigger;
import org.strandz.core.domain.NodeI;
import org.strandz.core.domain.SdzBeanI;
import org.strandz.core.domain.CalculationPlace;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.prod.Session;
import org.strandz.core.prod.view.CreatableI;
import org.strandz.core.prod.view.CellI;
import org.strandz.core.relational.ORMapExtent;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.lgpl.extent.IndependentExtent;
import org.strandz.lgpl.extent.InsteadOfAddRemoveTrigger;
import org.strandz.lgpl.extent.Tie;
import org.strandz.lgpl.extent.VisibleExtent;
import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MsgSubstituteUtils;
import org.strandz.lgpl.util.NullVerifiable;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.TaskI;
import org.strandz.lgpl.util.TaskTimeBandMonitorI;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.IconEnum;
import org.strandz.lgpl.widgets.WidgetUtils;

import javax.swing.event.EventListenerList;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a Data Object. It has Lookups to other cells, and contains all
 * the Attributes that are associated with the Data Object. Data, in the form of a list
 * of Data Objects, is read out of a database and placed into a cell. This usually occurs
 * as part of a data flow event.
 *
 * @author Chris Murphy
 */
public class Cell extends AbstractCell
    implements CellI, SdzBeanI, org.strandz.core.domain.CellI
{
    private List validateBeanMsg = new ArrayList();
    private CreatableI creatable;
    private Clazz clazz;
    /**
     * If present will override above for constructing only (ie. getItemValue())
     */
    private Clazz clazzToConstruct;
    private Clazz secondaryClazzToConstruct;
    private ArrayList lookups = new ArrayList();
    private boolean validated = false;
    private List<Attribute> attributes = new ArrayList<Attribute>();
    private Node node;
    private boolean isChief = false;
    private boolean alreadyCalledSetIndependent;
    private Attribute problemAttribute;
    private String name;
    private String refField;
    private IndependentExtent ie;
    private NewInstanceTrigger newInstanceTrigger;
    private InsteadOfAddRemoveTrigger insteadOfAddRemoveTrigger;
    /**
     * A sop to sdzdsgnr, so theoretically sdzdsgnr.BeansTreeModel s/deal with
     * a wrapper around this Data Object
     */
    private boolean alreadyBeenCustomized = false;
    private transient boolean singleThread = false;
    private transient boolean asynchronous = false;
    private transient PropertyChangeSupport propChangeSupp;
    // private ORMapExtent orMapExtent;
    private transient Object currentValue;
    private transient Object lhsCurrentValue;
    private transient NullVerifiable nullVerifiable;
    private static final transient IconEnum CELL_ICON = IconEnum.YELLOW_CIRCLE;
    public static final String DEFAULT_NAME = "New Cell";
    public static final String NO_CLAZZ_TO_INSTANTIATE = "Cell <$> must have a class to instantiate";
    public static final String NO_ATTRIBS_OR_LOOKUPS = "The following class does not have any associated attributes or lookups: $";
    public static final String ATTRIBS_NOT_UNIQUE = "Cell <$> must contain uniquely named attributes, the problem is with <$>";
    public static final String BAD_LOOKUP = "Lookup cell <$> is in error";
    private static int timesEvent;
    private static int derivedTimes;
    private static int timesRecord;
    private static boolean debugging = false;

    public IconEnum getIconEnum()
    {
        return CELL_ICON;
    }

    public Cell()
    {
        /*
        times++;
        Err.pr( "### Constructed Cell " + times + " times");
        if(times == 5)
        {
        //Err.stack();
        }
        */
        propChangeSupp = new PropertyChangeSupport(this);
    }

    public void setDefaults(String nodeName)
    {
        if(nodeName == null)
        {
            setName(DEFAULT_NAME);
        }
        else
        {
            String cellName = nodeName.replaceFirst("Node", "Cell");
            setName(cellName);
        }
    }

    public void clearCreatable()
    {
        creatable = null;
    }

    public boolean inDetailNode()
    {
        return node.isDetail();
    }

    public boolean notMasterNode(Object node)
    {
        return this.node != node;
    }

    public void setNewInstanceTrigger(NewInstanceTrigger vl)
    {
        this.newInstanceTrigger = vl;
    }

    NewInstanceTrigger getNewInstanceTrigger()
    {
        return newInstanceTrigger;
    }

    InsteadOfAddRemoveTrigger getInsteadOfAddRemoveTrigger()
    {
        return insteadOfAddRemoveTrigger;
    }
    
    public void setInsteadOfAddRemoveTrigger(InsteadOfAddRemoveTrigger insteadOfAddRemoveTrigger)
    {
        this.insteadOfAddRemoveTrigger = insteadOfAddRemoveTrigger;
    }

    public void addPropertyChangeListener(
        String property,
        PropertyChangeListener l)
    {
        propChangeSupp.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(
        String property,
        PropertyChangeListener l)
    {
        propChangeSupp.removePropertyChangeListener(l);
    }

    public void set(Cell cell)
    {
        setName(cell.getName());
        setClazz(cell.getClazz());
        setRefField(cell.getRefField());
        setCells(cell.getCells());
        setAttributes(cell.getAttributes());
        setNonJoinAttributes(cell.getNonJoinAttributes());
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        String txt = "Cell " + this.getName();
        ReasonNotEquals.addClassVisiting(txt);

        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Cell))
        {
            ReasonNotEquals.addReason("not an instance of a Cell");
            result = false;
        }
        else
        {
            result = false;

            Cell test = (Cell) o;
            if((name == null ? test.name == null : name.equals(test.name))
                && (refField == null
                ? test.refField == null
                : refField.equals(test.refField)))
            {
                result = true;
                if(lookups.size() == test.lookups.size())
                {
                    for(int i = 0; i <= lookups.size() - 1; i++)
                    {
                        Cell cell = (Cell) lookups.get(i);
                        Cell testCell = (Cell) test.lookups.get(i);
                        if(!cell.equals(testCell))
                        {
                            result = false;
                            ReasonNotEquals.addReason(
                                "look up comparison " + i + " failed: " + cell + " with "
                                    + testCell);
                            break;
                        }
                    }
                }
                else
                {
                    ReasonNotEquals.addReason("lookups of different sizes");
                    result = false;
                }
                if(result == true)
                {
                    if(attributes.size() == test.attributes.size())
                    {
                        for(int i = 0; i <= attributes.size() - 1; i++)
                        {
                            Attribute attribute = (Attribute) attributes.get(i);
                            Attribute testAttribute = (Attribute) test.attributes.get(i);
                            if(!attribute.equals(testAttribute))
                            {
                                ReasonNotEquals.addReason("attribute");
                                result = false;
                                break;
                            }
                        }
                    }
                    else
                    {
                        ReasonNotEquals.addReason(
                            "attributes of different sizes " + this.getName() + ", "
                                + test.getName());
                        result = false;
                    }
                }
            }
            else
            {// Err.pr( "name comparison failed: " + name + " with " + test.name);
                // Err.pr( "\tOR refField comparison failed: " + refField + " with " + test.refField);
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (name == null ? 0 : name.hashCode());
        result = 37 * result + (refField == null ? 0 : refField.hashCode());
        for(int i = 0; i <= lookups.size() - 1; i++)
        {
            Cell cell = (Cell) lookups.get(i);
            result = 37 * result + cell.hashCode();
        }
        for(int i = 0; i <= attributes.size() - 1; i++)
        {
            Attribute attribute = attributes.get(i);
            result = 37 * result + attribute.hashCode();
        }
        return result;
    }
    
    public void displayLovObjects()
    {
        creatable.displayLovObjects();
    }
    
    public List getLOV()
    {
        List result = null;
        if(creatable != null)
        {
            result = creatable.getLOV();
        }
        return result;
    }

    private void prDetails()
    {
        Err.pr( "strand: " + getNode().getStrandDebugInfo());
        Err.pr( "node: " + getNode());
        Err.pr( "cell: " + this);
    }

    public void setLOV(List list)
    {
        if(creatable != null)
        {
            if(ie != null)
            {
                prDetails();
                Session.error( "Cannot call setLOV() after the data has been queried");
            }
            if(list.size() > 0)
            {
                if(list.get(0) != null)
                {
                    if(list.get(0).getClass() == String.class)
                    {
                        prDetails();
                        String txt = "LOV is supposed to be of objects that are not Strings";
                        Print.prList(list, txt);
                        Session.error(txt);
                    }
                }
                else
                {
                    prDetails();
                    String txt = "For LOVs, use a special NULL value of an object rather than null";
                    Print.prList(list, txt);
                    Session.error(txt);
                }
            }
            creatable.setLOV(list);
        }
        else
        {
            prDetails();
            Session.error("Cell " + getName() + " has not yet reached runtime");
        }
    }

    /**
     * Called in interf method so that setData, setDefaultElement etc
     * calls will get thru when live. ie./ a permanent relationship between
     * run time Creatable and design time Cell.
     */
    public void setCreatable(CreatableI creatable)
    {
        this.creatable = creatable;
    }

    public CalculationPlace getCalculationPlace()
    {
        CalculationPlace result = null;
        if(creatable != null)
        {
            Assert.notNull( creatable);
            Assert.notNull( creatable.getAdapters());
            result = creatable.getAdapters().getCalculationPlace();
        }
        return result;
    }

    public LookupTiesManagerI getTiesManager()
    {
        return creatable.getTiesManager();
    }

    public void setClazz(Clazz c)
    {
        propChangeSupp.firePropertyChange("clazz", this.clazz, c);
        Err.pr( SdzNote.INVOKE_WRONG_FIELD, "Resolved from Bean (xml file)");
        this.clazz = c;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    public Clazz getClazzToConstruct()
    {
        return clazzToConstruct;
    }

    public void setClazzToConstruct(Clazz clazzToConstruct)
    {
        this.clazzToConstruct = clazzToConstruct;
    }

    /**
     * For ORMs that are not POJOs another POJO type can be used for validation etc. With non-POJO
     * DOs the problems are:
     * 1./ These objects created for once-only validation purposes tend to end up in the DB,
     *     presumably thru connections to registered DOs.
     * 2./ You can't always work with these DOs when they are unregistered - for instance you
     *     can't set an unregistered worker as the belongsToGroup of another worker.
     *
     * @return
     */
    public Clazz getSecondaryClazzToConstruct()
    {
        return secondaryClazzToConstruct;
    }

    public void setSecondaryClazzToConstruct(Clazz secondaryClazzToConstruct)
    {
        this.secondaryClazzToConstruct = secondaryClazzToConstruct;
    }

    public void setRefField(String s)
    {
        this.refField = s;
    }

    public String getRefField()
    {
        return refField;
    }

    /**
     * public void addCell( Cell lookup)
     * {
     * if(getNode() != null) //else will do following for all lookups
     * //when setNode called
     * {
     * lookup.setNode( getNode());
     * }
     * lookups.add( lookup);
     * }
     * <p/>
     * public List getCells()
     * {
     * return lookups;
     * }
     */
    public Cell getCellByName(String name)
    {
        Cell result = null;
        for(Iterator iter = lookups.iterator(); iter.hasNext();)
        {
            Cell cell = (Cell) iter.next();
            if(cell.getName().equals(name))
            {
                result = cell;
                break;
            }
        }
        return result;
    }

    /* ReferenceLookupAttribute is optional in Strandz, so no need to make it
     * important by creating a method with it in. Anything that can be achieved
     * with one of these can also be achieved with the lookup cell
    public Cell getCellByAttribute( ReferenceLookupAttribute rlAttribute)
    {
      Cell result = null;
      for(Iterator iter = lookups.iterator(); iter.hasNext();)
      {
        Cell cell = (Cell)iter.next();
        if(cell.getName().equals( name))
        {
          result = cell;
          break;
        }
      }
      return result;
    }
    */

     /**/
    public Cell[] getCells()
    {
        return (Cell[]) lookups.toArray(new Cell[0]);
    }

    public void setCells(Cell[] cells)
    {
        // Err.pr( "setCells() being called for Cell");
        lookups.clear();
        for(int i = 0; i <= cells.length - 1; i++)
        {
            if(getNode() != null)
            {
                cells[i].setNode((Node) getNode());
            }
            lookups.add(cells[i]);
        }
    }

    public Cell getCell(int index)
    {
        return (Cell) lookups.get(index);
    }

    public void setCell(int index, Cell cell)
    {
        if(lookups.size() == index)
        {
            lookups.add(index, cell);
        }
        else
        {
            lookups.set(index, cell);
        }
        if(getNode() != null)
        {
            cell.setNode((Node) getNode());
        }
    }

    public boolean removeCell(Cell cell)
    {
        return lookups.remove(cell);
    }

    public Tie getTie()
    {
        Tie result;
        result = creatable.getTie();
        return result;
    }

    public Object getItemValues()
    {
        Object result = null;
        if(!node.isDataDetached())
        {
            if(node.getTableControl() != null)
            {
                result = getNewObjectsFromControl();
            }
            else
            {
                Session.error(
                    "Cell.getValue() to be called where node has a TableControl");
            }
        }
        return result;
    }
    
    public List getItemValuesList()
    {
        List result = null;
        if(!node.isDataDetached())
        {
            if(node.getTableControl() != null)
            {
                result = getNewObjectsFromControl();
            }
            else
            {
                Session.error(
                    "Cell.getItemValuesList() to only be called where node has a TableControl");
            }
        }
        return result;
    }

    /**
     * No object actually exists, yet one is manufactured from
     * all the items - and that includes many DOs. So you end up
     * with a 'deep' say roster slot with all its object 
     * references. If instead you want to get an actual DO then
     * use the method getLastNavigatedToDO() instead. 
     * 
     * This method is required because value binding is not instant -
     * (as soon as move away from an item) but quite sensibly occurs
     * when a user moves away from a 'visual record' - when there
     * will be enough filled in to supply the backing DO (or group
     * of DOs if lookups)
     * 
     * This method will be used mainly by validation routines. 
     */
    public Object getItemValue()
    {
        Object result = null;
        if(!node.isDataDetached())
        {
            Clazz clazz;
            if(getSecondaryClazzToConstruct() != null)
            {
                clazz = getSecondaryClazzToConstruct();
            }
            else if(getClazzToConstruct() == null)
            {
                clazz = getClazz();
                Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "Using class cell made with: " + clazz);
            }
            else
            {
                clazz = getClazzToConstruct();
                Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "Using special class to construct with: " + clazz);
            }
            result = getNewObjectFromControlValues( this, clazz, attributes);
        }
        else
        {
            Err.pr( "Returning null from Cell.getItemValue() because data is detached");
        }
        return result;
    }

    public RuntimeAttribute getFocused()
    {
        RuntimeAttribute result = null;
        ItemAdapter focusedItemAdapter = ControlSignatures.getFocused();
        Assert.notNull( focusedItemAdapter, "No control currently focused");
        Err.pr( SdzNote.FIELD_VALIDATION, "focusedItemAdapter: " + focusedItemAdapter);
        List attributes = getAllAttributes();
        List<RuntimeAttribute> runtimeAttributes = Utils.getSubList( attributes, RuntimeAttribute.class); 
        for(Iterator iterator = runtimeAttributes.iterator(); iterator.hasNext();)
        {
            RuntimeAttribute attribute = (RuntimeAttribute)iterator.next();
            if(attribute.getItemAdapter().equals( focusedItemAdapter))
            {
                result = attribute;
                break;
            }
        }
        if(result == null)
        {
            Print.prList( runtimeAttributes, "All RuntimeAttributes in cell " + this.getName());
            Err.pr( "There is a focused control: " + focusedItemAdapter.getItemName() + 
                    ", but it is not an attribute (or even a looked up attribute) of cell " + this.getName());
        }
        return result;
    }

    /**
     * Not only gets/creates the object, but fills it in as well. The source
     * for all the filling in is the item values.
     *
     * @param clazz
     * @param attributes
     */
    private static Object getNewObjectFromControlValues(Cell cell, Clazz clazz, List attributes)
    {
        if(SdzNote.SECONDARY.isVisible())
        {
            Err.pr( "clazzToConstruct: " + cell.getSecondaryClazzToConstruct());
            Err.pr( "clazz: " + cell.getClazz());
            Err.pr( "In cell: " + cell);
            Err.pr( "What will be factorying with: " + clazz);
            Err.pr( "");
            SdzNote.SECONDARY.incTimes();
            if(SdzNote.SECONDARY.getTimes() == 2)
            {
                Err.debug();
            }
        }
        /**/
        Object result;
        if(cell.creatable.hasNewInstanceTrigger())
        {
            result = cell.creatable.fireSecondaryNewInstanceTrigger();
        }
        else
        {
            /*
             * Value of clazz may have been configured by the user by setting cell.setClazzToConstruct()
             * to override cell.setClazz(). See callers to this method.
             */
            result = ObjectFoundryUtils.factoryClazz( clazz, "getNewObjectFromControlValues(), cell: <" +
                cell.getName() + ">");
        }
        if(result == null)
        {
            //
        }
        else
        {
            Err.pr( SdzNote.TOO_MANY_FILL_INS, "Done factory and created a " +
                result.getClass().getName());
            Err.pr( SdzNote.TIGHTEN_RECORD_VALIDATION, "** doing " + result.getClass());
            boolean useNullVerifiable = false;
            if(Utils.isClassOfType( result.getClass(), Worker.class))
            {
                Err.pr( SdzNote.TIGHTEN_RECORD_VALIDATION, "** cell: " + cell);
                Err.pr( SdzNote.TIGHTEN_RECORD_VALIDATION, "** num attributes: " + attributes.size());
                if(attributes.size() == 1 &&
                        Utils.isClassOfType( clazz.getClassObject(), NullVerifiable.class) &&
                        ((RuntimeAttribute)attributes.get( 0)).getItemValue() == null)
                {
                    if(cell.nullVerifiable == null)
                    {
                        Err.error( "Set a NullVerifiable on cell: <" + cell.getName() + ">");
                    }
                    else
                    {
                        useNullVerifiable = true;
                    }
                }
            }
            /*
            * Now that we have created a new object, we can use the framework
            * to populate each of its DO fields. We do this by getting each
            * item from the screen and placing it into the corresponding DO
            * field
            */
            for(Iterator it = attributes.iterator(); it.hasNext();)
            {
                Attribute attribute = (Attribute) it.next();
                if(attribute instanceof ReferenceLookupAttribute)
                {
                    //Don't do anything here, these will be examined by
                    //recursively calling this method through lookups
                }
                else if(attribute instanceof RuntimeAttribute)
                {
                    Object obj;
                    if(useNullVerifiable)
                    {
                        obj = cell.nullVerifiable;
                        Err.pr( SdzNote.TIGHTEN_RECORD_VALIDATION, "Will be setting field value to a dummy for <" + attribute.getName() +
                                "> when creating a " + cell.nullVerifiable.getClass().getName());
                    }
                    else
                    {
                        obj = ((RuntimeAttribute) attribute).getItemValue();
                    }
                    Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, attribute.getName() + " item value: " + obj);
                    // Adapter tied to data's class:
                    ItemAdapter ad = ((RuntimeAttribute) attribute).getItemAdapter();
                    Assert.notNull( ad, 
                        "Attribute has no ItemAdapter: <" + attribute + ">, is in cell <" + cell + ">");
                    if(!ad.isCalculated())
                    {
                        Assert.notNull( ad.getDoAdapter());
                        ad.getDoAdapter().setSecondaryFieldValue(result, obj);
                    }
                }
            }
            for(Iterator it = cell.lookups.iterator(); it.hasNext();)
            {
                Cell lookup = (Cell) it.next();
                Clazz clazz1;
                if(lookup.getSecondaryClazzToConstruct() != null)
                {
                    clazz1 = lookup.getSecondaryClazzToConstruct();
                }
                else if(lookup.getClazzToConstruct() == null)
                {
                    clazz1 = lookup.getClazz();
                    Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "Using class cell made with: " + clazz1);
                }
                else
                {
                    clazz1 = lookup.getClazzToConstruct();
                    Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "Using special class to construct with: " + clazz1);
                }
                Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "Which obj expect to get from these control values?");
                Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "lookup: " + lookup);
                Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "class1: " + clazz1);
                Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "lookup.getAttributes(): " + lookup.getAttributes());
                Object obj = getNewObjectFromControlValues(lookup, clazz1, lookup.getAttributes());
                Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "obj got is a: " + obj.getClass().getName());
                Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "");
                SelfReferenceUtils.setReferenceValue(result, obj, lookup.getRefField(),
                    lookup.getClazz().getClassObject());
            }
        }
        return result;
    }

    private List getNewObjectsFromControl()
    {
        List result = null;
        for(Iterator en = Utils.getSubList(attributes, TableAttribute.class).iterator(); en.hasNext();)
        {
            TableAttribute attribute = (TableAttribute) en.next();
            AbstractTableItemAdapter ad = (AbstractTableItemAdapter) attribute.getItemAdapter();
            //int blankingPolicy = ControlSignatures.getBlankingPolicy(
            //    ad.createIdEnum());
            List columnValues = attribute.getItemList();
            if(result == null)
            {
                result = new ArrayList(columnValues.size());
                for(int j = 0; j <= columnValues.size() - 1; j++)
                {
                    Object newInstance = ObjectFoundryUtils.factoryClazz( clazz, "getNewObjectsFromControl()");
                    Err.pr( SdzNote.TOO_MANY_FILL_INS, "Done factory and created a " +
                        clazz.getClassObject().getName() + ", " + result);
                    result.add(newInstance);
                }
            }
            else if(result.size() != columnValues.size())
            {
                Session.error();
            }

            int i = 0;
            for(Iterator it = columnValues.iterator(); it.hasNext(); i++)
            {
                Object value = it.next();
                // Adapter tied to data's class:
                ad.getDoAdapter().setFieldValue(result.get(i), value);
            }
        }
        return result;
    }

    public void setChief(boolean b)
    {
        isChief = b;
    }

    public boolean isChief()
    {
        return isChief;
    }

    /**
     * Called by user when an Cell is associated with a Node, and
     * Cells are associated with one another via addCell
     */
    public void setNode(Node node)
    {
        if(node == null)
        {
            Session.error("Cannot set Node null");
        }
        this.node = node;
        /*
        for(Iterator en = attributes.iterator(); en.hasNext(); )
        {
        Attribute attribute = (Attribute)en.next();
        setTableControlForAttribute( attribute); //this.node needs to be set by here
        }
        */
        for(Iterator it = lookups.iterator(); it.hasNext();)
        {
            Cell lookup = (Cell) it.next();
            lookup.setNode(node);
        }
    }

    public NodeI getNode()
    {
        if(node == null)
        {/*
       Now worked around this!
       if(!pBeans.isDesignTime()) //thus can instantiate in beanbox
       {
       Session.error( getName() + " has no node: could see this happening if called addCell on" +
       " it b4 have called setCell" +
       " on the Node" );
       }
       */}
        return node;
    }

    /**
     * Not depreciated, but way of not having to remember
     * the current index. Is not part of the methods used
     * when XML encoding being done.
     */
    public void addAttribute(Attribute attribute)
    {
        /*
        attribute.setCell( (AbstractCell)this);
        attributes.add( attribute);
        setTableControlForAttribute( attribute);
        */
        int lastAttribute = getAttributes().size();
        setAttribute(lastAttribute, attribute, true);
    }

    /**
     * You probably don't want to call this one - see getAllAttributes() instead  
     */
    public List<Attribute> getAttributes()
    {
        return attributes;
    }

    public boolean hasChanged()
    {
        boolean result = false;
        for(Iterator iter = Utils.getSubList(attributes, RuntimeAttribute.class).iterator(); iter.hasNext();)
        {
            RuntimeAttribute element = (RuntimeAttribute) iter.next();
            if(element.hasChanged())
            {
                // Err.pr( element + " has changed!!!!!!!");
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean hasProgrammaticallyChanged()
    {
        boolean result = false;
        for(Iterator iter = Utils.getSubList(attributes, RuntimeAttribute.class).iterator(); iter.hasNext();)
        {
            RuntimeAttribute element = (RuntimeAttribute) iter.next();
            if(element.hasProgrammaticallyChanged())
            {
                // Err.pr( element + " has changed!!!!!!!");
                result = true;
                break;
            }
        }
        return result;
    }

    public List getNonJoinAttributes()
    {
        List result = Utils.getSubList(attributes, LinkAttribute.class, Utils.EXCLUDE);
        result = Utils.getSubList(result, NonVisualAttribute.class, Utils.EXCLUDE);
        return result;
    }

    /**
     * Pretty silly PropertyDescriptor does not seem to support RO
     * properties! If want to support will have to not use reflection
     * but introspection. Tie.createTie() is about where look. (For this
     * comment a cell was being used as data).
     */
    public void setNonJoinAttributes(List as)
    {
        Session.error("setNonJoinAttributes() called");
    }

    /*
    * Is called by customiser, but will not be called by
    * xml unless BeanInfo changed
    */
    public void setAttributes(List as)
    {
        for(Iterator it = as.iterator(); it.hasNext();)
        {
            Attribute attr = (Attribute) it.next();
            attr.setCell(this);
            // setTableControlForAttribute( attr);
        }
        this.attributes = as;
    }

    /**
     * Depreciated
     */
    /*
    public ArrayList getAttributes()
    {
    return attributes;
    }
    */

    /*
    * all sets here s/be checking if Cell has a node that the node
    * has a TableControl when the Attribute is a TableAttribute. If don't
    * insist on there being a node then will have to check the other way
    * round as well. ie./ When set TableControl of a Node will have to check
    * that IF the node has cells that they have TableAttributes. Due to IF
    * will also have to check when Cell is added to a Node that IF node has
    * a TableControl and IF Cells have AdaptersList that they are TableAttribtes.
    * ALL THIS TOO difficult to check esp. when user will be going thru UI
    * most of the time.
    */
    /*
    public Attribute[] getAttributes()
    {
    //Err.pr( "Calling getAttributes() for <" + this + "> and have " + attributes.size());
    return (Attribute[])attributes.toArray( new Attribute[0]);
    }
    public void setAttributes( Attribute[] as)
    {
    **
    for(int i=0; i<=as.length-1; i++)
    {
    as[i].setCell( this);
    setTableControlForAttribute( as[i]);
    }
    What returned from this line had remove as an unsupported operation!
    attributes = Arrays.asList( as);
    **
    //Err.pr( "Attributes being cleared for <" + this + "> and set to a list of size " + as.length);
    **
    if(as.length == 0)
    {
    times++;
    }
    if(times == 2)
    {
    Err.stack();
    }
    **
    attributes.clear();
    for(int i=0; i<=as.length-1; i++)
    {
    as[i].setCell( this);
    setTableControlForAttribute( as[i]);
    attributes.add( as[i]);
    }
    }
    */
    public Attribute getAttribute(int index)
    {
        return (Attribute) attributes.get(index);
    }

    public Attribute getNonJoinAttribute(int index)
    {
        return (Attribute) getNonJoinAttributes().get(index);
    }
    
    public void setAttribute(int index, Attribute a)
    {
        setAttribute(index, a, true);
    }

    private void setAttribute(int index, Attribute a, boolean chkNotDup)
    {
        a.setCell(this);
        // setTableControlForAttribute( a);
        if(chkNotDup)
        {
            chkNameNotAlready( a);
        }
        if(attributes.size() == index)
        {
            attributes.add(index, a);
            //Err.pr( "Now have " + attributes.size() + " attributes");
        }
        else
        {
            attributes.set(index, a);
        }
    }
    
    private void chkNameNotAlready( Attribute outsideListAttribute)
    {
        for(Attribute attribute : attributes)
        {
            Assert.notNull( outsideListAttribute, "outsideListAttribute is null");
            Assert.notNull( attribute.getName(), "attribute does not have a name: " + attribute);
            if(attribute.getName().equalsIgnoreCase( outsideListAttribute.getName()))
            {
                Print.prList( attributes, "All attributes");
                Err.error( "(Ignoring case) already have an attribute called <" + attribute.getName() +
                    "> in <" + this + ">");
            }
        }
    }

    public void replaceAttribute(Attribute oldAttr, Attribute newAttr)
    {
        if(oldAttr == null || newAttr == null)
        {
            Session.error("Cannot replace where either old or new are null");
        }

        int index = attributes.indexOf(oldAttr);
        if(index == -1)
        {
            /*
            * The attributes in a cell may dynamically change type, for instance
            * when delete a control from an Attribute. As indexOf uses equals() (
            * and equals depends on all the attributes being equal as well) we
            * will get to here. oldAttr's DO field will not have changed in
            * this case.
            */
            String cfn = oldAttr.getDOField();
            for(Iterator en = attributes.iterator(); en.hasNext();)
            {
                Attribute attribute = (Attribute) en.next();
                if(attribute.getDOField().equals(cfn))
                {
                    index = attributes.indexOf(attribute);
                    break;
                }
            }
            if(index == -1)
            {
                Session.error(
                    "In Cell.replaceAttribute, could not find old Attribute: " + cfn);
            }
        }
        {
            /*
            times++;
            Err.pr( "### Replacing " + oldAttr.getDataFieldName() + " in cell " +
            hashCode() + " (" + getName() + ")" + " times " + times);
            */
            setAttribute(index, newAttr, false);
        }
    }

    public boolean removeAttribute(Attribute attr)
    {
        boolean ok = attributes.remove(attr);
        if(!ok)
        {
            // Session.error( "Was unable to remove: " + attr + " from " + this);
            Err.error(org.strandz.lgpl.note.SdzDsgnrNote.ERROR_REMOVING_ATTRIBUTE,
                "Was unable to remove: " + attr + " from " + this);
        }
        // return ok;
        return true;
    }

    /**
     * 05/07/01. Started to code when realised that user needs
     * at runtime to call Attribute.getControlValue().
     * User has full access to data so the method
     * Attribute.getDataValue() is not necessary.
     */
    public RuntimeAttribute getAttributeByName(String name)
    {
        RuntimeAttribute result = null;
        for(Iterator en = Utils.getSubList(attributes, RuntimeAttribute.class).iterator(); en.hasNext();)
        // for(Iterator en = attributes.iterator(); en.hasNext();)
        // 25/08/04 need to get ReferenceLookupAttribute as well, which not RuntimeAttribute
        {
            result = (RuntimeAttribute) en.next();

            // Err.pr( "Looking at RuntimeAttribute: " + result);
            // Err.pr( "HAS getName(): " + result.getName());
            /*
            if("preferredControllerInterface".equals( result.getName()))
            {
            Err.debug();
            }
            */
            String display = result.getName();
            if(display != null && display.equals(name))
            {
                break;
            }
            else if(result.getDOField() != null && // when testing SourceFileWriter
                result.getDOField().equals(name))
            {
                break;
            }
            result = null;
        }
        /*
        for(Iterator en = attributes.iterator(); en.hasNext(); )
        {
        Attribute attribute = (Attribute)en.next();
        if(attribute instanceof RuntimeAttribute)
        {
        result = (RuntimeAttribute)attribute;
        String display = result.getName();
        if(display != null && display.equals( name))
        {
        break;
        }
        else if(result.getDataFieldName().equals( name))
        {
        break;
        }
        result = null;
        }
        }
        */
        if(result == null)
        {
            Print.prList(attributes, "Cell");
            for(Iterator en = Utils.getSubList(attributes, RuntimeAttribute.class).iterator(); en.hasNext();)
            {
                RuntimeAttribute attr = (RuntimeAttribute) en.next();
                Err.pr(
                    "Name: <" + attr.getName() + ">\t DOField: <" + attr.getDOField()
                        + ">");
            }
            Err.pr("");
            Err.error("There is no attribute called <" + name + "> inside " + this);
        }
        return result;
    }

    /*
    boolean isValidated()
    {
    return validated;
    }
    */

     /**/
    private boolean attributesNamesUnique()
    {
        boolean result = true;
        HashSet hashSet = new HashSet();
        for(Iterator en = attributes.iterator(); en.hasNext();)
        {
            problemAttribute = (Attribute) en.next();
            result = hashSet.add(problemAttribute.getDOField());
            if(!result)
            {
                break;
            }
        }
        // Err.pr( "");
        return result;
    }

     /**/

    private String validateAttributes()
    {
        String result = null;
        for(Iterator en = attributes.iterator(); en.hasNext();)
        {
            Attribute attribute = (Attribute) en.next();
            boolean ok = attribute.validateBean(true);
            if(!ok)
            {
                List list = attribute.retrieveValidateBeanMsg();
                /*
                if(list.size() != 0)
                {
                Session.error( "Expected no errors when validating an attribute");
                }
                */
                result = (String) list.get(0);
                break;
            }
        }
        return result;
    }

    public boolean validateBean()
    {
        return validateBean(true);
    }

    public boolean validateBean(boolean childrenToo)
    {
        boolean ok = true;
        validateBeanMsg.clear();

        //Err.pr( "Num attributes: " + attributes.size());
        //Err.pr( "Num lookups: " + lookups.size());
        String errMsg = null;
        if(clazz == null)
        {
            // errMsg = "Cell <" + this + "> must have a class to instantiate";
            errMsg = MsgSubstituteUtils.formMsg(NO_CLAZZ_TO_INSTANTIATE, this);
        }
        /* We might assign all the attributes at RT
        else if(attributes.isEmpty() && lookups.isEmpty())
        {
            // errMsg = "The following class does not have"
            // + " any associated attributes or lookups: " + clazz;
            errMsg = MsgSubstituteUtils.formMsg(NO_ATTRIBS_OR_LOOKUPS, clazz.getClassObject());
        }
        */
        if(errMsg == null)
        {
            errMsg = validateAttributes();
            if(errMsg == null)
            {
                if(!attributesNamesUnique())
                {
                    // errMsg = "Cell <" + this
                    // + "> must contain uniquely named attributes, the problem is with <"
                    // + problemAttribute.getDOField() + ">";
                    String params[] = {this.toString(), problemAttribute.getDOField()};
                    errMsg = MsgSubstituteUtils.formMsg(ATTRIBS_NOT_UNIQUE, params);
                }
                if(errMsg == null)
                {
                    /*
                    * Recursively validate all the other instantiables that are
                    * in the one Node (this validate is called from within Node.validate)
                    */
                    for(Iterator en = lookups.iterator(); en.hasNext();)
                    {
                        Cell cell = (Cell) en.next();
                        boolean lok = cell.validateBean(true);
                        if(!lok)
                        {
                            String txt = cell.getName();
                            if(txt == null)
                            {
                                txt = cell.toString();
                            }
                            // errMsg = "Lookup cell <" + txt + "> is in error";
                            errMsg = MsgSubstituteUtils.formMsg(BAD_LOOKUP, txt);
                            break;
                        }
                        else
                        {
                            errMsg = cell.checkPropertyAgreesWithLookup(getClazz());
                            if(errMsg != null)
                            {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if(errMsg != null)
        {
            ok = false;
            validateBeanMsg.add(errMsg);
        }
        validated = true;
        return ok;
    }
    
    public void removeAllAttributes()
    {
        List attribs = new ArrayList( attributes);
        for(Iterator<Attribute> iterator = attribs.iterator(); iterator.hasNext();)
        {
            Attribute attribute = iterator.next();
            removeAttribute( attribute);
        }
    }

    /**
     * 'All Attributes' means we recurse down thru the lookups to collect their
     * attributes as well. 
     * Needed to code this when wanted Node to be capable of getting a list of
     * all the AdaptersList it contained. This list of AdaptersList must be able to
     * appear at design-time.
     */
    public List getAllAttributes()
    {
        ArrayList fields = new ArrayList();
        fields.addAll(attributes);
        for(Iterator e = lookups.iterator(); e.hasNext();)
        {
            Cell lookup = (Cell) e.next();
            fields.addAll(lookup.getAllAttributes());
        }
        // Err.pr( "getAttributesForNode() to ret " + fields);
        return fields;
    }

    public List getNonBlankChangedAttributes()
    {
        List result = new ArrayList();
        List workWith = getChangedAttributes();
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

    public List getChangedAttributes()
    {
        List result = new ArrayList();
        List attribs = getAttributes();
        // for (int i = 0; i < attribs.length; i++)
        for(Iterator iter = Utils.getSubList(attribs, RuntimeAttribute.class).iterator(); iter.hasNext();)
        {
            RuntimeAttribute attribute = (RuntimeAttribute) iter.next();
            if(attribute.hasChanged())
            {
                // Err.pr( attribute.getName() + " has changed");
                // Err.pr( "\t BI: <" + attribute.getBIValue() + ">");
                // Err.pr( "\t AI: <" + attribute.getItemValue() + ">");
                result.add(attribute);
            }
        }
        return result;
    }

    public List getProgrammaticallyChangedAttributes()
    {
        List result = new ArrayList();
        List attribs = getAttributes();
        // for (int i = 0; i < attribs.length; i++)
        for(Iterator iter = Utils.getSubList(attribs, RuntimeAttribute.class).iterator(); iter.hasNext();)
        {
            RuntimeAttribute attribute = (RuntimeAttribute) iter.next();
            if(attribute.hasProgrammaticallyChanged())
            {
                // Err.pr( attribute.getName() + " has changed");
                // Err.pr( "\t BI: <" + attribute.getBIValue() + ">");
                // Err.pr( "\t AI: <" + attribute.getItemValue() + ">");
                result.add(attribute);
            }
        }
        return result;
    }

    /**
     * Only supposed to be called from a PRE_QUERY trigger as part
     * of leaving ENTER_QUERY state.
     */
    /*
    public List getEnterQueryAttributes()
    {
    List result = null;
    //Err.pr( "current state: " + getState());
    //Err.pr( "previous state: " + getStrand().getOPor().getPreviousState());
    StateEnum currentState = getNode().getState();
    if(currentState == StateEnum.ENTER_QUERY)
    {
    result = getNonBlankChangedAttributes();
    }
    else //if(currentState == StateEnum.FROZEN)
    {
    result = new ArrayList();
    }
    if(result == null)
    {
    Session.error();
    }
    return result;
    }
    */

    /**
     * TODO - data accumulated here is currently disgarded, but
     * really s/be kept as FieldTie.canCreate() and
     * MethodTie.canCreate() could be passed this data.
     */
    String checkPropertyAgreesWithLookup(Clazz lookupUser)
    {
        String result = null;
        Class unmatchedClass = null;
        boolean foundMatch = false;
        //Err.pr( "\tclass contains lookup is " + lookupUser);
        //Err.pr( "\tclass being looked up is " + clazz);
        //Err.pr( "\tstring property need find in it is " + refField);
        if(refField == null)
        {
            result = "Do not know the name of the property of type " + clazz + "\n"
                + "in " + lookupUser + " as setRefField() has not been called\n"
                + "for " + getName();
        }
        else
        {
            Field childFields[] = lookupUser.getClassObject().getFields();
            for(int i = 0; i <= childFields.length - 1; i++)
            {
                // Err.pr( "Comparing " + childFields[i].getName() + " with " + lookup.refField);
                if(childFields[i].getName().equals(refField))
                {
                    if(childFields[i].getType() == clazz.getClassObject())
                    {
                        foundMatch = true;
                        break;
                    }
                    else
                    {
                        unmatchedClass = childFields[i].getType();
                    }
                }
            }
            if(!foundMatch)
            {
                BeanInfo bi = null;
                try
                {
                    bi = Introspector.getBeanInfo(lookupUser.getClassObject());
                }
                catch(IntrospectionException ex)
                {
                    Session.error(ex);
                }

                PropertyDescriptor pds[] = bi.getPropertyDescriptors();
                /*
                * As this is called during validation, first of all check that
                * the property can be read and written:
                */
                for(int i = 0; i <= pds.length - 1; i++)
                {
                    // Err.pr( "Comparing " + pds[i].getName() + " with " + lookup.refField);
                    // Err.pr("looking at " + pds[i].getName());
                    if(pds[i].getName().equals(refField))
                    {
                        if(pds[i].getPropertyType() == clazz.getClassObject())
                        {
                            foundMatch = true;
                            break;
                        }
                        else
                        {
                            unmatchedClass = pds[i].getPropertyType();
                        }
                    }
                }
            }
            if(!foundMatch)
            {
                /*
                if(refField == null && unmatchedClass == null)
                {
                Session.error( "A property of type " + clazz + " has not been found in " +
                lookupUser);
                }
                else
                */
                {
                    /*
                    if(lookupUser == null)
                    {
                      Err.error( "lookupUser == null");
                    }
                    if(clazz == null)
                    {
                      Err.error( "clazz == null");
                    }
                    */
                    if(unmatchedClass == null)
                    {
                        result =
                            //"DO property " + refField + " " + pUtils.separator + "in DO "
                            //lookupUser.getName() + " was not found." + pUtils.separator
                            "Was not able to look up " + clazz.getClassObject().getName() + Utils.NEWLINE
                                + "because the property " + refField + " no longer exists in" + Utils.NEWLINE
                                + lookupUser.getClassObject().getName();
                    }
                    else
                    {
                        result = "DO property " + refField + " " + Utils.NEWLINE + "in DO "
                            + lookupUser.getClassObject().getName() + " " + Utils.NEWLINE
                            + "has not been found to be looking up a type " + clazz.getClassObject().getName() + Utils.NEWLINE
                            + "but " + unmatchedClass.getName();
                        Err.pr(SdzNote.LOOKING_UP_WRONG_TYPE, result);
                    }
                    //result = null; // Did not want to see for demo
                }
            }
        }
        return result;
    }

    /**
     * This could be for the transition to runtime in circumstances where
     * the user is frustrated with calls in wrong order. Would have to call
     * Strand.void consumeNodesIntoRT( Node newNode) and mark as done so the
     * insert() or execute( OperationEnum.EXECUTE_QUERY) that will follow will not do the same. Issue
     * here as to whether we need re-querying to transition to runtime again
     * as does now. Probably not.
     * To test use TestORMapping.testInsertToNull(), and make the insert() not
     * the first thing.
     */
    private void noCreatable()
    {
        Session.error(
            "Must call execute( OperationEnum.EXECUTE_QUERY) or insert() before setData()/Query()/Procedure()");
    }
        
    /**
     * To be called when running
     */
    public void setData(Object displayData)
    {
        timesEvent++;
        Err.pr( SdzNote.DISABLE_ATTRIBUTE_CHANGES, "EVENT - in Cell.setData() " + this + ", where singleThread is " + singleThread + " times " + timesEvent);
        if(timesEvent == 5)
        {
            //Err.stack();
        }
        int duration = 110;
        TaskTimeBandMonitorI setDataMonitor = WidgetUtils.getTimeBandMonitor( duration);
        TaskI task = new SetDataTask( duration, this, displayData);
        task.setSingleThread( singleThread);
        task.setAsynchronous( asynchronous);
        setDataMonitor.start( task, null);
        setDataMonitor.stop();
    }

    void setDataInternal(Object displayData)
    {
        if(creatable == null)
        {
            noCreatable();
        }
        else
        {
            if(displayData == null)
            {
                Session.error(
                    "Cannot call setData() with null data - use an empty list instead, in " + getName());
            }
            // Might be better to do this later, when user views
            // a record, in which case would only need to do it to
            // an element.
            Err.pr(JDONote.SHOULD_ONLY_PERSIST_TO_INSERT,
                "Why do pUtil.registerPersistentAll( pm, displayData);");
            // pUtil.registerPersistentAll( pm, displayData);
            boolean hasOne = false;
            if(creatable.getInsteadOfAddRemoveTrigger() != null)
            {
                //Err.pr( "Has AddRemoveTrigger: <" + creatable.getCell() + ">");
                //hasOne = true;
            }
            ie = IndependentExtent.createIndependent(displayData, creatable.getEntityManagerProvider(),
                creatable.getInsteadOfAddRemoveTrigger());
            if(hasOne)
            {
                Err.pr( "ID of IndependentExtent is " + ie.id);
                Err.pr( "Type is " + ie.getClass().getName());
            }
            if(ie == null)
            {
                Err.error("Could not create an IndependentExtent from " + displayData);
            }
            creatable.setInitialData(ie);
            // Err.pr( "===============orig data of type " + ie.getActualList().getClass());
            // Err.pr( "===============wrapped data of type " + ie.getClass());
            // lookups never here!
            // creatable.applyLovSubstitutions( ie);
        }
    }

    public ORMapExtent setQuery(
        List displayData, List pkColNames)
    {
        return setQueryInternal(displayData, pkColNames);
    }

    private ORMapExtent setQueryInternal(
        List displayData, List pkColNames)
    {
        ORMapExtent result = null;
        if(creatable == null)
        {
            noCreatable();
        }
        else
        {
            List list = creatable.getAdapters().getAllAdapters();
            ORMapExtent orMapExtent = new ORMapExtent(list);
            orMapExtent.setInitialState(getNode().getState());
            ((Node)getNode()).getStrand().addStateChangeTrigger(orMapExtent);
            orMapExtent.setDBTableName(getNode().getName()); //only used for instrumentation
            orMapExtent.setPkColNames(pkColNames);
            orMapExtent.addAll(displayData);
            creatable.setInitialData(
                IndependentExtent.createIndependent(orMapExtent, creatable.getEntityManagerProvider(), creatable.getInsteadOfAddRemoveTrigger()));
            result = orMapExtent;
        }
        return result;
    }

//  private SdzEntityManagerI getEM()
//  {
//    return ((Node)getNode()).getStrand().getEntityManager();
//  }

    /**
     * To be called when running
     */
    public void setDefaultElement(Object defaultElement)
    {
        if(creatable == null)
        {
            Session.error(
                "Must insert() or execute( OperationEnum.EXECUTE_QUERY) "
                    + "before calling setDefaultElement()");
        }
        else
        {
            creatable.setDefaultElement(defaultElement);
        }
    }

    /*
    Iterator it1 = pUtils.getSubList(
    attributes, RuntimeAttribute.class).iterator();
    Iterator it2 = pUtils.getSubList(
    cell.getAttributes(), RuntimeAttribute.class).iterator();
    for(;it1.hasNext() && it2.hasNext();)
    {
    RuntimeAttribute assignAttribute = (RuntimeAttribute)it2.next();
    RuntimeAttribute currentAttribute = (RuntimeAttribute)it1.next();
    currentAttribute.setItemValue( assignAttribute.getItemLabel());
    }
    */

    public String toString()
    {
        String result;
        if(getName() != null)
        {
            result = getName();
        }
        else if(clazz != null)
        {
            result = "[Cell name: <" + clazz.getClassObject().getName() + "]";
        }
        else
        {
            result = "[Cell " + super.toString() + "]";
        }
        return result;
    }

    /**
     * Put these in for Node and Cell and Strand when 'getting into' design time.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /*
    public String getDefaultName()
    {
    //String result = toString();
    String result = "New";
    result = pUtils.spaceOutClassName( result);
    if(refField == null)
    {
    result = result + " Cell";
    }
    else
    {
    result = result + " Lookup Cell";
    }
    return result;
    }
    */

    public String getName()
    {
        return name;
    }

    /**
     * So beanbox won't crash
     */
    protected EventListenerList listenerList = new EventListenerList();

    public void addActionListener(ActionListener l)
    {
        listenerList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l)
    {
        listenerList.remove(ActionListener.class, l);
    }

    /*
    private static List mappableAttributes( List attributes)
    {
    List result = new ArrayList();
    for (Iterator iter = attributes.iterator(); iter.hasNext();)
    {
    Attribute attr = (Attribute) iter.next();
    //Err.pr( attr.getClass().getName());
    if(attr instanceof LinkAttribute)
    {
    //Err.pr( attr.getClass().getName() + " consumed");
    }
    else
    {
    //Err.pr( attr.getClass().getName() + " NOT consumed");
    result.add( attr);
    }
    }
    return result;
    }
    */
    public boolean isAlreadyBeenCustomized()
    {
        return alreadyBeenCustomized;
    }

    public void setAlreadyBeenCustomized(boolean alreadyBeenCustomized)
    {
        this.alreadyBeenCustomized = alreadyBeenCustomized;
    }

    public List retrieveValidateBeanMsg()
    {
        return validateBeanMsg;
    }

    public void chkOnCurrentNode(String opName, String extraTxt)
    {
        NodeI nodeWithin = getNode();
        NodeI currentNode = nodeWithin.getCurrentNode();
        if(nodeWithin != currentNode)
        {
            String txt = "Can only perform " + opName + " when have navigated to "
                + nodeWithin.getName() + ". Are currently on " + currentNode.getName() + ".";
            if(extraTxt != null)
            {
                txt += " " + extraTxt + ".";
            }
            Session.error(txt);
        }
    }

    public VisibleExtent getDataRecords()
    {
        VisibleExtent extent = null;
        if(creatable != null)
        {
            extent = (VisibleExtent) creatable.getDataRecords();
        }
        return extent;
    }

//  public void _setEntityManagerProvider( EntityManagerProviderI emp)
//  {
//    creatable.getSubRecordObj().setEntityManagerProvider( emp);
//  }

    // Not sure why have this when doesn't work
    // public Object getValue( int index)
    // {
    // Object result = null;
    // NavExtent extent = (NavExtent)creatable.getActualList();
    // if(extent == null)
    // {
    // Err.error( "creatable.getActualList() is returning null");
    // }
    // Err.pr( extent /*.getClass().getName()*/);
    // result = extent.get( index);
    // return result;
    // }

    public String toShow()
    {
        return getName();
    }
    
    private static boolean willBeBadIndexResult( VisibleExtent ve, int row, Cell cell)
    {
        boolean result = false;
        int extentSize = ve.size();
        if(row == extentSize)
        {
            //When inserting into brand new row then this does happen, which is why we
            //must record the current value after backgroundAdd - at that time we will
            //have something in the extent we can save
            result = true;
            Err.pr( SdzNote.ROW_AND_EXTENT_NOT_SYNCED, "Bad combination - not a serious problem but should look at sometime, " +
                    "extent size " + extentSize + ", current row " + row);
        }
        else if(row > extentSize)
        {
            Err.error( "Cannot index into " + row + " when extentSize is " + extentSize);
        }
        return result;
    }
    
    private static boolean isNullExtent( VisibleExtent ve)
    {
        boolean result = false;
        if(ve == null)
        {
            result = true;
            //Err.pr( SdzNote.ROW_AND_EXTENT_NOT_SYNCED, "Why is extent null (in isNullExtent)?");
        }
        return result;
    }
    
    void recordCurrentValue( String id, int row, StateEnum origState, OperationEnum operation)
    {
        VisibleExtent ve = getDataRecords();
        if(row == Utils.UNSET_INT)
        {
            row = node.getRow();
        }
        else
        {
            /*
             * This is for special case where have touched on a node but the row
             * of the node has not yet been set - so instead it is passed in here.
             */
            Err.pr( SdzNote.RECORD_CURRENT_VALUE, "special case where row comes by param: " + row);
        }
        timesRecord++;
        Err.pr( SdzNote.DERIVED_DATA, "row using to recordCurrentValue: " + row + " because <" + id + ">" +
                ", origState: " + origState + ", operation: " + operation + " in <" + this + ">, times " + timesRecord);
        if(timesRecord == 0 /*69*/)
        {
            Err.debug();
        }
        if(row != -1) //when stateId == StateEnum.FROZEN/UNKNOWN row will be -1
        {
            boolean nullExtent = isNullExtent( ve);
            boolean willBeBadIndex = false;
            if(!nullExtent)
            {
                willBeBadIndex = willBeBadIndexResult( ve, row, this);
            }
            if(!nullExtent && !willBeBadIndex)
            {
                currentValue = ve.get(row);
                creatable.getSubRecordObj().setDerivedAt( row, currentValue);
                Err.pr( SdzNote.RECORD_CURRENT_VALUE, "** - " + id + " <" + currentValue + "> in " + this);
            }
            else
            {
                if(nullExtent && !willBeBadIndex)
                {
                    /*
                     * Theory is that when chief getDerivedAt is called that it recurses and puts values in
                     * all the lookups derived. Thus when come to do it in one of the lookups it will already
                     * be there.
                     */
                    if(isChief())
                    {
                        currentValue = creatable.getSubRecordObj().getDerivedAt( row, getDataRecords().get( row));
                    }
                    else
                    {
                        currentValue = creatable.getSubRecordObj().
                                getDerivedAt( row, 
                                              null
                                              //creatable.getSubRecordObj().getParent().getDerivedAt( row, null)
                                );
                    }
                    Err.pr( SdzNote.RECORD_CURRENT_VALUE, "\t<" + currentValue + ">");
                }
                else //if(willBeBadIndex)
                {
                    //Err.pr( /*SdzNote.RECORD_CURRENT_VALUE,*/ "Unable to obtain the current value (bad combination) " + id + " in " + this);
                    int insertRow = row-1;
                    if(insertRow >= 0)
                    {
                        //Err.pr( /*SdzNote.RECORD_CURRENT_VALUE,*/ "To hack when operation " + operation + ", when origState was " + origState + " in " + this);
                        //Err.pr( "row been changed to " + insertRow + ", when size is " + ve.size() + ", orig row was " + row);
                        //currentValue = creatable.getSubRecordObj().getDerivedAt( insertRow);
                        //Err.pr( "Hack makes currentValue of <" + this + "> <" + currentValue + ">");
                    }
                }
            }
        }
        else
        {
            Err.pr( SdzNote.RECORD_CURRENT_VALUE, "Unable to obtain the current value (not on a row) " + id + " in " + this);
        }
        Err.pr( SdzNote.RECORD_CURRENT_VALUE_DETAILS, "\tcell <" + getName() + 
                "> has SubRecordObj " + creatable.getSubRecordObj() + " of type <" + 
                creatable.getSubRecordObj().getClass().getName() + ">");
    }
    
    public Object getLastNavigatedToDO()
    {
        return currentValue;
    }

    /**
     * Called recursively as get all the derived values 
     */
    void setLHSCurrentValue( Object currentValue)
    {
        this.lhsCurrentValue = currentValue;
    }

    public void setNullVerifiable( NullVerifiable nullVerifiable)
    {
        this.nullVerifiable = nullVerifiable;
    }

    public NullVerifiable getNullVerifiable()
    {
        return nullVerifiable;
    }

    /**
     * Used in a situation where AttributeFocusListener.focusLost was then causing the button
     * press event to occur. Use if you do not want events to ride into each other.
     * Fixed this problem by disabling/enabling focus listener at the right times - for
     * example when depress and un-depress a button
     */
    public void setSingleThread(boolean singleThread)
    {
        this.singleThread = singleThread;
        //Err.pr( "In cell " + this + ", single thread set to " + singleThread);
    }

    /**
     * Not used
     */
    public void setAsynchronous(boolean asynchronous)
    {
        this.asynchronous = asynchronous;
    }
}
