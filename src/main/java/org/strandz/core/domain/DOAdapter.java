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

import org.strandz.core.info.convert.AbstractDOInterrogate;
import org.strandz.lgpl.extent.Tie;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.StopWatch;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.data.objects.Property;
import org.strandz.lgpl.data.objects.SdzPropertySetableI;
import org.strandz.lgpl.data.objects.PropertyI;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class DOAdapter
{
    private String doFieldName;
    private PropertyI property;
    //private Field field;
    private Method primaryReadMethod;
    private Method primaryWriteMethod;
    private Method secondaryReadMethod;
    private Method secondaryWriteMethod;
    private Object[] args = new Object[1]; // used by setFieldValue
    /**
     * The Clazz of the enclosing DO
     */
    Clazz parentClazz;
    private AbstractCell cell;
    RecorderI recorder;
    private SetListener setListener;
    private LookupTiesManagerI btm;
    private ItemAdapter itemAdapter;
    private int blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
    private boolean blankingPolicySet = false;
    
    private static final boolean DEBUG = false;
    private static int times;
    static int constructedTimes;
    public int id;

    public DOAdapter(
        String doFieldName, RecorderI recorder,
        LookupTiesManagerI btm, AbstractCell cell, PropertyI property)
    {
        if(doFieldName == null)
        {
            Err.error("doFieldName in a DOAdapter must be not null");
        }
        this.doFieldName = doFieldName;
        this.property = property;
        this.recorder = recorder;
        this.btm = btm;
        this.cell = cell;
        constructedTimes++;
        id = constructedTimes;
        /*
        pr("$$# Created DOAdapter " + getDOFieldName() + " with id " + id);
        if(id == 0)
        {
            Err.stackOff();
        }
        */
    }
    
    public String toString()
    {
        return getDOFieldName();
    }

    public String getDOFieldName()
    {
        return doFieldName;
    }

    public void setParentClazz( Clazz clazz)
    {
        Assert.notNull( clazz);
        this.parentClazz = clazz;
        //Err.pr( "parentClazz for " + this + " set to " + parentClazz);
        if(isEndUserRuntime())
        {
            Assert.isTrue( SelfReferenceUtils.implementsInterface( parentClazz.getClassObject(), SdzPropertySetableI.class),
                parentClazz.getClassObject().getName() + " needs to implement SdzPropertySetableI in " + this);
        }
        Err.pr( SdzNote.WRITE_TO_DATAOBJ, "Field has been set for <" + doFieldName + ">");
    }

    public void setField(PropertyDescriptor pd,
                         PropertyDescriptor pdToConstruct,
                         PropertyDescriptor pdToSecondarilyConstruct)
    {
        if(SdzNote.TABLE_BLANKING_POLICY.isVisible())
        {
            /*
            if(clazz.getClassObject() == Expense.class && id == 2)
            {
                Err.stackOff();
            }
            else
            {
                Err.pr( "DOAdapter getting " + clazz.getClassObject());
            }
            */
        }
        /*
        * As this is called during validation, first of all check that
        * the property can be read and written:
        */
        boolean found = false;
        if(pd.getName().equals(doFieldName))
        {
            found = true;
            obtainReadAndWriteMethods( pd, pdToConstruct, pdToSecondarilyConstruct);
        }
        else
        {
            Err.error("property name and doFieldName do not match");
        }
    }
    
    private void obtainReadAndWriteMethods(
            PropertyDescriptor pd,
            PropertyDescriptor pdToConstruct,
            PropertyDescriptor pdToSecondarilyConstruct)
    {
        primaryReadMethod = null;
        primaryWriteMethod = null;
        secondaryReadMethod = null;
        secondaryWriteMethod = null;
        if(SdzNote.INVOKE_WRONG_FIELD.isVisible() && id == 5)
        {
            Err.debug();
        }
        PropertyDescriptor propertyDescriptor;
        if(pdToConstruct != null)
        {
            propertyDescriptor = pdToConstruct; 
            primaryReadMethod = pd.getReadMethod();
        }
        else
        {
            propertyDescriptor = pd; 
        }
        if(primaryReadMethod == null)
        {
            //If we can't get a read method from the interface then try to get one
            //from the concrete class will construct
            primaryReadMethod = propertyDescriptor.getReadMethod();
        }
        primaryWriteMethod = propertyDescriptor.getWriteMethod();
        if(primaryReadMethod == null)
        {
            Err.error( "Cannot read property: " + doFieldName);
        }
        if(primaryWriteMethod == null)
        {
            String msg = "Cannot write property: <" +
                doFieldName + "> in DOApater ID: " + id + " on <" + cell +
                ">, for <" + cell.getClazz() + ">";
            if(SdzNote.WRITE_TO_DATAOBJ.isVisible())
            {
                if(id == 0)
                {
                    Err.stack( msg);
                }
                else
                {
                    Err.pr( msg);
                }
            }
            else
            {
                //Bit annoying to be seeing this all the time,
                //although sometimes it will be useful
                //Err.pr( "WARNING: " + msg);
            }
        }
        else
        {
            String msg = "Will use write property: <" +
                doFieldName + "> in DOApater ID: " + id + " on <" + cell +
                ">, for <" + cell.getClazz() + ">";
            Err.pr( SdzNote.WRITE_TO_DATAOBJ, msg);
        }
        if(pdToSecondarilyConstruct != null)
        {
            secondaryReadMethod = pdToSecondarilyConstruct.getReadMethod();
            secondaryWriteMethod = pdToSecondarilyConstruct.getWriteMethod();
        }
    }

    public void recordSetValue(Object value)
    {
        recorder.recordSetValue(getCell().getNode().getName(), getCell().getName(),
            doFieldName, primaryWriteMethod, value);
    }

    // public void setCell( AbstractCell cell)
    // {
    // this.cell = cell;
    // }

    public AbstractCell getCell()
    {
        return cell;
    }

    public Tie getTie()
    {
        Tie result = null;
        result = cell.getTie();
        return result;
    }

    /**
     * Concerned with conversions of these 'edge' values. See ControlInfo.
     *
     * @param clazz Recently added for the sake of tables. Passing in null for
     * the times when didn't before require this parameter
     * @return See ControlInfo.NEED_CONSTRUCT_BLANK_OBJ_READ etc for the types
     */
    int getBlankingPolicy( Class clazz)
    {
        if(SdzNote.TABLE_BLANKING_POLICY.isVisible() && id == 2)
        {
            Err.debug();
        }
        /*
        * This change is pertinent when the
        * application programmer has given us a default
        * element that contains nulls, for a component set
        * that does not normally need to construct blank
        * objects on reading nulls.
        */
        if(!blankingPolicySet && itemAdapter != null)
        {
            if(clazz == null)
            {
                //Way we were always doing this in the past
                blankingPolicy = ControlSignatures.getBlankingPolicy(itemAdapter.createIdEnum( toString()));
            }
            else
            {
                blankingPolicy = ControlSignatures.getBlankingPolicy(itemAdapter.createIdEnum( clazz));
            }
        }
        return blankingPolicy;
        // ControlInfo.NEED_CONSTRUCT_BLANK_OBJ_READ
    }

//A blanking policy is never set so let's remove this fiction
//    public void setBlankingPolicy(int blankingPolicy)
//    {
//        if(SdzNote.tableBlankingPolicy.isVisible() && id == 2)
//        {
//            Err.debug();
//        }
//        this.blankingPolicy = blankingPolicy;
//        blankingPolicySet = true;
//    }

    public RecorderI getRecorderI()
    {
        return getCell().getNode().getRecorderI();
    }

    LookupTiesManagerI getTiesManager()
    {
        return btm;
    }

    public void addSetListener(SetListener l)
    {
        this.setListener = l;
    }

    void validate(Object element)
    {
        String txt = "testValue";
        setFieldValue(element, txt);

        String out = (String) getFieldValue(element);
        if(txt != out)
        {
            Err.error("Adapter validation failed");
        }
    }

    public Object getFieldValueFromAny(Object element)
    {
        Object result;
        // go out as narrow down to outermost target which is this itemAdapter
        Object narrowed = element;
        List ties = btm.getRouteTo(this, element.getClass());
        // ties will be in order of inner to outer - and are wanting outer object
        // Err.pr( "Got " + ties.size() + " ties");
        for(Iterator iter = ties.iterator(); iter.hasNext();)
        {
            Tie tie = (Tie) iter.next();
            // Err.pr( "On tie " + tie);
            narrowed = tie.getFieldValue(narrowed);
        }
        result = getFieldValue(narrowed);
        return result;
    }

    /**
     * Grabbing off from the screen. Note that if RO this method won't be called
     *
     * @param value
     * @return
     */
    public Object convertToDOFieldValue(Object value)
    {
        if(value != null)
        {
            //Err.pr( "Grabbing " + value + " of type " + value.getClass() + " off from screen");
            int blankingPolicy = getBlankingPolicy( value.getClass());
            Class gotClass = value.getClass();
            if(blankingPolicy == AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE
                && gotClass != String.class)
            {
                Err.error(
                    "NEED_PUT_NULL_OBJ_WRITE only intended to convert Strings, not "
                        + gotClass + ", prob is with DOAdapter ID " + id);
            }
            Class returnType;
            if(isEndUserRuntime())
            {
                returnType = property.getType();
            }
            else
            {
                returnType = getChkReadMethod( primaryReadMethod).getReturnType();
            }
            if(blankingPolicy == AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE
                && gotClass == String.class && value.equals(""))
            {
                /*
                * This will mean no errors for eg Integers, but is also
                * the right thing to do for Strings - just because the
                * control has created a "" does not mean this is what
                * we should save to the database.
                */
                value = null;
            }
            /*
            * If the value was nulled (above) then doing a convert will not
            * be necessary. An example of a convert is where a cell in a table
            * gives you a String back after you were editing a Number.
            */
            else if(!Utils.isClassOfType(gotClass, returnType))
            {
                // Primitive is ok, don't need to do any special conversions, invoke will handle it
                if(!returnType.isPrimitive())
                {
                    /*
                    Err.pr( "Doing special 'write to a data object' when primaryReadMethod.getReturnType() gives: " +
                    primaryReadMethod.getReturnType());
                    Err.pr( "And gotClass (the class of the value) gives: " + gotClass);
                    Err.pr( "And value gives: " + value);
                    */
                    value = ControlSignatures.convertFromDisplaySpecialControlType(value,
                        returnType);
                }
            }
        }
        return value;
    }

    /**
     * BAD method corrected above
     * Grabbing off from the screen. Note that if RO this method won't be called
     * 
     * @param value
     * @return
     */
    public Object _convertToDOFieldValue(Object value)
    {
        if(value != null)
        {
            //Err.pr( "Grabbing " + value + " of type " + value.getClass() + " off from screen");
            int blankingPolicy = getBlankingPolicy( value.getClass());
            Class gotClass = value.getClass();
            if(blankingPolicy == AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE
                && gotClass != String.class)
            {
                Err.error(
                    "NEED_PUT_NULL_OBJ_WRITE only intended to convert Strings, not "
                        + gotClass + ", prob is with DOAdapter ID " + id);
            }
            if(blankingPolicy == AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE
                && gotClass == String.class && value.equals(""))
            {
                /*
                * This will mean no errors for eg Integers, but is also
                * the right thing to do for Strings - just because the
                * control has created a "" does not mean this is what
                * we should save to the database.
                */
                value = null;
            }
            /*
            * If the value was nulled (above) then doing a convert will not
            * be necessary. An example of a convert is where a cell in a table
            * gives you a String back after you were editing a Number.
            */
            else
            {
                Class returnType;
                if(isEndUserRuntime())
                {
                    returnType = property.getType();
                }
                else
                {
                    returnType = getChkReadMethod( primaryReadMethod).getReturnType();
                }
                if(!Utils.isClassOfType(gotClass, returnType));
                {
                    // Primitive is ok, don't need to do any special conversions, invoke will handle it
                    if(!returnType.isPrimitive())
                    {
                        /*
                        Err.pr( "Doing special 'write to a data object' when primaryReadMethod.getReturnType() gives: " +
                        primaryReadMethod.getReturnType());
                        Err.pr( "And gotClass (the class of the value) gives: " + gotClass);
                        Err.pr( "And value gives: " + value);
                        */
                        value = ControlSignatures.convertFromDisplaySpecialControlType(value,
                            returnType);
                    }
                }
            }
        }
        return value;
    }
    
    private Method getChkReadMethod( Method readMethod)
    {
        Method result = readMethod;
        Assert.notNull( result, "DOAdapter <" + this + "> in <" + cell + "> has no read method");
        return result;
    }

    /**
     * nullFine - true for JTable which automatically creates Strings
     * (don't know if would call the no param constructor for any other
     * type) when the data is pumped into it.
     */
    private Object create(Class classToConstruct, boolean nullFine)
    {
        Object result;
        Err.pr( SdzNote.CREATING_DOS, "constructing " + classToConstruct + ": " + !nullFine);
        if(SelfReferenceUtils.hasNullConstructor(classToConstruct))
        {
            if(nullFine)
            {
                result = null;
            }
            else
            {
                result = ObjectFoundryUtils.factory(classToConstruct);
            }
        }
        else if(SelfReferenceUtils.isInterface(classToConstruct))
        {
            /*
            * Here can either construct call ConstructorSignatures.constructInterface( classToConstruct)
            * which will use the right InterfaceConstructorDescriptor, or specify NEED_PUT_NULL_OBJ_WRITE,
            * or just assume it is ok to put a null into the data object, as are doing here.
            */
            // Err.error( "Cannot construct interface " + classToConstruct);
            result = null;
        }
        else
        {
            if(nullFine)
            {
                Err.pr( SdzNote.CREATING_DOS, "Strange case that null is fine yet no nullary constructor");
                result = null;
            }
            else
            {
                result = OtherSignatures.constructWithParams(classToConstruct);
            }
        }
        if(result == null && !nullFine)
        {
            Err.error( "Trouble creating " + classToConstruct);
        }
        return result;
    }

    public void setFieldValue(Object element,
                              Object value)
    {
        SdzNote.WRITE_TO_DATAOBJ.incTimes();
        Err.pr( SdzNote.WRITE_TO_DATAOBJ, "In setFieldValue and to set <" + value + 
            "> on DO, times " + SdzNote.WRITE_TO_DATAOBJ.getTimes());
        if(SdzNote.WRITE_TO_DATAOBJ.getTimes() == 1)
        {
            Err.debug();
        }
        args[0] = convertToDOFieldValue(value);
        if(isEndUserRuntime())
        {
            writeEndUserPropertyValue( element, args[0]);
        }
        else
        {
            if(primaryWriteMethod == null)
            {
                Err.pr( "-----------");
                Err.pr( "element: " + element + ", of type: " + element.getClass().getName());
                if(value != null)
                {
                    Err.pr( "value: " + value + ", of type: " + value.getClass().getName());
                }
                Err.pr( "primaryReadMethod: " + primaryReadMethod);
                Err.pr( /*SdzNote.WRITE_TO_DATAOBJ,*/
                    "No primaryWriteMethod so won't actually write!! - on DOAdapter ID: " + id +
                        " fieldName: " + doFieldName);
                Err.pr( "-----------");
                return;
            }
            Err.pr( SdzNote.WRITE_TO_DATAOBJ, "to invoke write: " + primaryWriteMethod);
            if(value == null)
            {
                Err.pr( SdzNote.WRITE_TO_DATAOBJ, "where value NULL!");
            }
            else
            {
                Err.pr( SdzNote.WRITE_TO_DATAOBJ, "with args type: " + value.getClass().getName());
            }
            Err.pr( SdzNote.WRITE_TO_DATAOBJ, "with args value: " + value);
            /*
            if(id == 120)
            {
              if(value != null)
              {
                Err.pr(
                    "### Setting " + id + " to <" + value + ">, ["
                    + value.getClass().getName() + "]");
              }
              else
              {
                Err.pr( "### null Setting " + id + " to <" + value + ">");
              }
            }
            */
            SelfReferenceUtils.invoke(element, primaryWriteMethod, args);
        }
        if(setListener != null)
        {
            setListener.set(doFieldName, value, element);
        }
    }

    public void setSecondaryFieldValue(Object element,
                                       Object value)
    {
        args[0] = convertToDOFieldValue(value);
        if(secondaryWriteMethod == null)
        {
            setFieldValue( element, value);
        }
        else
        {
            SelfReferenceUtils.invoke(element, secondaryWriteMethod, args);
        }
    }

    public Clazz getParentClazz()
    {
        return parentClazz;
    }

    public Class getClassType()
    {
        Class result = null;
        if(primaryReadMethod != null)
        {
            result = primaryReadMethod.getReturnType();
        }
        return result;
    }

    private static void pr(String txt)
    {
        if(DEBUG)
        {
            Err.pr(txt);
        }
    }

    public Object getFieldValue(Object element)
    {
        Object result;
        if(element == null)
        {
            if(id == 4)
            {
                pr("Ele: --NULL--");
            }
            result = null;
        }
        else if(itemAdapter.isDataReadExternally())
        {
            result = null;
        }        
        else if(itemAdapter.isCalculated())
        {
            //Calculated values are derived after all other values are done. For now we 
            //just return null
            result = null;
        }
        else
        {
            if(SdzNote.INVOKE_WRONG_FIELD.isVisible() && id == 5)
            {
                pr("ID:" + id + ", " + doFieldName + " - " + " Ele: " + element);
                pr("Ele class: " + element.getClass().getName());
                pr("Method: " + primaryReadMethod);
                pr("");
            }
            StopWatch stopWatch = new StopWatch();
            stopWatch.startTiming();
            Assert.notNull( parentClazz, this + " does not have a parentClazz");
            if(isEndUserRuntime())
            {
                result = readEndUserPropertyValue( element);
                stopWatch.stopTiming();
                if(SdzNote.PERFORMANCE_TUNING.isVisible())
                {
                    Err.pr( stopWatch.getResult() > 1,
                            "Getting field " + doFieldName +
                                    " in a " + parentClazz.getClassObject().getName() + ") took " + stopWatch.getElapsedTimeStr());
                }
            }
            else
            {
                Assert.notNull(primaryReadMethod, "No primaryReadMethod for <" + this + ">, where parent class is a " + parentClazz);
                result = SelfReferenceUtils.invoke(element, primaryReadMethod);
                if(id == 1 && result != null)
                {
                    pr("result: <" + result + "> of type " + result.getClass().getName());
                }
                stopWatch.stopTiming();
                if(SdzNote.PERFORMANCE_TUNING.isVisible())
                {
                    Err.pr( stopWatch.getResult() > 1,
                            "Getting field " + doFieldName + " (type " + primaryReadMethod.getReturnType().getName() +
                                    " in a " + parentClazz.getClassObject().getName() + ") took " + stopWatch.getElapsedTimeStr());
                }
                result = defaultValueAfterDORead( result);
            }
        }
        /*
        timesGot++;
        if(timesGot == 97)
        {
            Err.pr( "### HAVE GOT <" + result + "> ID: " + id + " name: " + getDOFieldName() + " times " + timesGot);
            Err.pr( "itemAdapter: " + itemAdapter);
            Err.pr( "itemAdapter ID: " + itemAdapter.id);
            Err.pr( "isDataReadExternally: " + itemAdapter.isDataReadExternally());
            Err.pr( "isCalculated: " + itemAdapter.isCalculated());
            Err.debug();
        }
        */
        return result;
    }

    private Object defaultValueAfterDORead( Object result)
    {
        if(result == null)
        {
            int blankingPolicy = getBlankingPolicy( null);
            Class classToConstruct = primaryReadMethod.getReturnType();
            // initially control problem, but could still need to construct
            // a Boolean:
            if(blankingPolicy == AbstractDOInterrogate.NEED_CONSTRUCT_BLANK_OBJ_READ)
            {
                result = create(classToConstruct, false);
            }
            // actual field could be of a class that doesn't have null constructor
            else
            {
                result = create(classToConstruct, true);
            }
        }
        return result;
    }

    /*
    private Object defaultEndUserRuntimeValueAfterDORead( Object result)
    {
        if(result == null)
        {
            Err.error( "Seems like no need to do");
            SdzPropertySetableI propGetter = (SdzPropertySetableI)result;
            propGetter.setValue( property.getName(), property.newZeroValue());
            result = propGetter.getValue( property.getName());
        }
        return result;
    }
    */

    /*
     * New style read ensures no summing.
     */
    private Object readEndUserPropertyValue( Object element)
    {
        Object result;
        //This will be true, but have tested already
        //SelfReferenceUtils.implementsInterface( parentClazz.getClassObject(), SdzPropertySetableI.class)
        SdzPropertySetableI propGetter = (SdzPropertySetableI)element;
        /*
        times++;
        Err.pr( "times " + times);
        if(times == 1)
        {
            Err.debug();
        }
        */
        if(!propGetter.propertyExists( doFieldName))
        {
            Assert.isTrue( doFieldName.equals( property.getName()));
            /*
             * Now the property value is being asked for, and has never existed before.
             * Time to add it to the DO and give it a default value. We need a newInstance()
             * so that each different DO has its own property value.
             */
            /* May not need to actually add the property...
            Err.pr( SdzNote.READ_USER_PROPERTY, "To add non-existant property - " + property + " to " + propGetter);
            propGetter.addProperty( property.copyConstruct( propGetter.getId()));
            propGetter.setValue( property.getName(), property.newDefaultInstance());
            */
            result = property.newZeroValue();
        }
        else
        {
            if(!propGetter.propertyExists( doFieldName, false))
            {
                //Property may exist when you include the children, but not
                //otherwise, so we create it here
                propGetter.addProperty( property.copyConstruct( propGetter.getId()));
                //propGetter.setValue( property.getName(), property.newDefaultInstance());
            }
            result = propGetter.getValue( doFieldName);
        }
        if(result == null)
        {
            Err.error( "Problem with <" + element + ">, type <" +
                element.getClass().getName() + "> for property <" + doFieldName + ">");
        }
        return result;
    }

    private void writeEndUserPropertyValue( Object element, Object value)
    {
        SdzPropertySetableI propSetter = (SdzPropertySetableI)element;
        if(!propSetter.propertyExists( doFieldName))
        {
            Assert.isTrue( doFieldName.equals( property.getName()));
            if(value == null)
            {
                /* Is now happening again when focus away from a CTV with a spacer
                Err.error( "Perhaps caused by confused threading");
                */
                //Happens when set a MoneyAmount property value back to 0.00
                //This is an interesting situation where we need to have a property
                //that is "0.00". After commit we will ensure that the property is
                //removed, so it doesn't linger around
                /*
                propSetter.addProperty( property.copyConstruct( propSetter.getId()));
                propSetter.setValue( property.getName(), property.newZeroValue());
                */
            }
            else if(!value.equals( property.zeroValue()))
            {
                /*
                 * Now the property value is being asked for, and has never existed before.
                 * Time to add it to the DO.
                 */
                Err.pr( SdzNote.READ_USER_PROPERTY, "To add property so can write value");
                propSetter.addProperty( property.copyConstruct( propSetter.getId()));
                propSetter.setValue( property.getName(), value);
            }
            else
            {
                //no point in adding a property just to set its default value
                propSetter.addProperty( property.copyConstruct( propSetter.getId()));
                propSetter.setValue( property.getName(), value);
            }
        }
        else
        {
            propSetter.setValue( property.getName(), value);
        }
    }

    //Old style read is necessary, but unnecessarily adds properties
//    private Object readEndUserPropertyValue( Object element)
//    {
//        Object result;
//        //This will be true, but have tested already
//        //SelfReferenceUtils.implementsInterface( parentClazz.getClassObject(), SdzPropertySetableI.class)
//        SdzPropertySetableI propGetter = (SdzPropertySetableI)element;
//        /*
//        times++;
//        Err.pr( "times " + times);
//        if(times == 1)
//        {
//            Err.debug();
//        }
//        */
//        if(!propGetter.propertyExists( doFieldName))
//        {
//            Assert.isTrue( doFieldName.equals( property.getName()));
//            /*
//             * Now the property value is being asked for, and has never existed before.
//             * Time to add it to the DO and give it a default value. We need a newInstance()
//             * so that each different DO has its own property value.
//             */
//            Err.pr( SdzNote.READ_USER_PROPERTY, "To add non-existant property - " + property + " to " + propGetter);
//            propGetter.addProperty( property.copyConstruct( propGetter.getId()));
//            propGetter.setValue( property.getName(), property.newDefaultInstance());
//        }
//        result = propGetter.getValue( doFieldName);
//        if(result == null)
//        {
//            Err.error( "Problem with <" + element + ">, type <" +
//                element.getClass().getName() + "> for property <" + doFieldName + ">");
//        }
//        return result;
//    }

//Old style write
//    private void writeEndUserPropertyValue( Object element, Object value)
//    {
//        SdzPropertySetableI propSetter = (SdzPropertySetableI)element;
//        if(!propSetter.propertyExists( doFieldName))
//        {
//            Assert.isTrue( doFieldName.equals( property.getName()));
//            /*
//             * Now the property value is being asked for, and has never existed before.
//             * Time to add it to the DO.
//             */
//            Err.pr( SdzNote.READ_USER_PROPERTY, "To add property so can write value");
//            propSetter.addProperty( property.copyConstruct( propSetter.getId()));
//        }
//        propSetter.setValue( property.getName(), value);
//    }

    public void setItemAdapter(ItemAdapter itemAdapter)
    {
        this.itemAdapter = itemAdapter;
    }

    public ItemAdapter getItemAdapter()
    {
        return itemAdapter;
    }

    PropertyI getPropertyValue()
    {
        return property;
    }

    public boolean isEndUserRuntime()
    {
        return property != null;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DOAdapter doAdapter = (DOAdapter) o;

        if (cell.getName() != null ? !cell.getName().equals(doAdapter.cell.getName()) : doAdapter.cell.getName() != null)
            return false;
        if (doFieldName != null ? !doFieldName.equals(doAdapter.doFieldName) : doAdapter.doFieldName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = doFieldName != null ? doFieldName.hashCode() : 0;
        result = 31 * result + (cell.getName() != null ? cell.getName().hashCode() : 0);
        return result;
    }
}
