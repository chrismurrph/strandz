package org.strandz.data.wombatrescue.objects.cayenne;

import org.strandz.data.wombatrescue.objects.FlexibilityI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.data.wombatrescue.objects.SeniorityI;
import org.strandz.data.wombatrescue.objects.SexI;
import org.strandz.data.wombatrescue.objects.WhichShiftI;
import org.strandz.data.wombatrescue.objects.WorkerHelper;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.data.wombatrescue.objects.cayenne.auto._Worker;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.note.WombatNote;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.reflect.ClassDescriptor;
import org.apache.cayenne.reflect.Property;
import org.apache.cayenne.reflect.ToManyMapProperty;
import org.apache.cayenne.map.EntityResolver;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Map;

public class Worker extends _Worker implements WorkerI
{
    private transient WorkerHelper workerHelper;
    private transient static int constructedTimes;
    public transient int incrementId;

    /**
     * Will be called by Cayenne when it is doing a query
     */
    public Worker()
    {
        constructedTimes++;
        incrementId = constructedTimes;
        installHelper();
        //Err.stack();
    }

    /**
     * When Spring has transferred one across it will no longer have its workerHelper
     */
    public void installHelper()
    {
        workerHelper = new WorkerHelper( this);
    }

    /**
     *
     * @param nullWorkerToSetGroup - Comes from the DB where there is only one 'null/dummy' worker
     */
    public Worker(WorkerI nullWorkerToSetGroup)
    {
        init( nullWorkerToSetGroup);
    }
    
    public void init( WorkerI nullWorkerToSetGroup)
    {
        Assert.notNull( workerHelper);
        Err.pr(WombatNote.NEW_WORKER_INSTANCE, "Constructing VOLUNTEER ID " + incrementId);
        if(incrementId == 0)
        {
            Err.stack();
        }
        if(nullWorkerToSetGroup != null)
        {
            Assert.notNull( ((DataObject)nullWorkerToSetGroup).getObjectContext(),
                "Won't be able to set a DO as a property if that DO does not have a data context");
            setActualBelongsToGroup( (Worker)nullWorkerToSetGroup);
            setDummy( false);
            setGroupContactPerson( false);
            setUnknown( false);
        }
        //As the DB was originally a JDO one, and we want the JDO client to continue to work:
        setJdoclass( "org.strandz.data.wombatrescue.objects.Worker");
        workerHelper = new WorkerHelper( this);
        Err.pr(WombatNote.NEW_WORKER_INSTANCE, "Finished constructing VOLUNTEER ID " + incrementId);
    }

    public int getId()
    {
        return incrementId;
    }

    public String getName()
    {
        return "" + getId();
    }

    public SeniorityI getSeniority()
    {
        return getActualSeniority();
    }

    public void setSeniority( SeniorityI seniority)
    {
        setActualSeniority( (Seniority)seniority);
    }

    public SexI getSex()
    {
        return getActualSex();
    }

    public void setSex( SexI sex)
    {
        setActualSex( (Sex)sex);
    }

    public WorkerI getBelongsToGroup()
    {
        return getActualBelongsToGroup();
    }

    public void setBelongsToGroup( WorkerI worker)
    {
        setActualBelongsToGroup( (Worker)worker);
    }

    public WhichShiftI getShiftPreference()
    {
        return getActualShiftPreference();
    }

    public void setShiftPreference( WhichShiftI whichShift)
    {
        setActualShiftPreference( (WhichShift)whichShift);
    }

    public FlexibilityI getFlexibility()
    {
        return getActualFlexibility();
    }

    public void setFlexibility( FlexibilityI flexibility)
    {
        //Err.pr( "Flexibility of " + getChristianName() + " is being set to " + flexibility);
        //Err.pr( "Is null: " + (flexibility == null));
        if(flexibility != null)
        {
            //Err.pr( "Type of Flexibility using is " + flexibility.getClass());
            //Err.pr( "name is " + flexibility.getName());
            //Err.pr( "id is " + flexibility.getId());
        }
        else
        {
            Err.pr( "WARNING: It is a problem in any database environment to set a lookup to null - " +
                "here we would expect the dummy flexibility");
        }
        setActualFlexibility( (Flexibility)flexibility);
    }

    public String getToLong()
    {
        return workerHelper.getToLong();
    }

    public String getToShort()
    {
        return workerHelper.getToShort();
    }

    public String formatWithPhone()
    {
        return workerHelper.formatWithPhone();
    }

    public String formatWithPhones()
    {
        return workerHelper.formatWithPhones();
    }

    public String getToValidate()
    {
        return workerHelper.getToValidate();
    }

    public String getBestPhone()
    {
        return workerHelper.getBestPhone();
    }

    public String formatAllPhonesAndEmail()
    {
        return workerHelper.formatAllPhonesAndEmail();
    }

    public boolean isStrange()
    {
        return workerHelper.isStrange();
    }

    public boolean onHoliday(Date date)
    {
        return workerHelper.onHoliday( date);
    }

    public boolean isAvailable( Date first, Date last)
    {
        return workerHelper.isAvailable( first, last);
    }

    public int compareTo( Object obj)
    {
        return workerHelper.compareTo( obj);
    }

    public String toString()
    {
        return workerHelper.helperToString();
    }

    public boolean equals(Object o)
    {
        return workerHelper.helperEquals( o);
    }

    public int hashCode()
    {
        return workerHelper.helperHashCode();
    }

    public String getOrderBy()
    {
        return workerHelper.getOrderBy();
    }

    public String formatWithHolidays(Date first, Date last)
    {
        return workerHelper.formatWithHolidays( first, last);
    }

    public List getRosterslots(List allRosterSlots)
    {
        return workerHelper.getRosterslots( allRosterSlots);
    }

    public boolean removeRosterSlot( RosterSlotI rosterSlot)
    {
        removeFromRosterSlots( (RosterSlot)rosterSlot);
        return true;
    }

    public void addRosterSlot( RosterSlotI rosterSlot)
    {
        addToRosterSlots( (RosterSlot)rosterSlot);
    }

    public void validate() throws ValidationException
    {
        workerHelper.validate();
    }

    public void addRosterSlot(RosterSlotI rosterSlot, int index)
    {
        addToManyTarget("rosterSlots", (DataObject)rosterSlot, true, index);
    }

    /**
     * Copied from super and altered to allow adding at a particular point
     * @param relName
     * @param value
     * @param setReverse
     */
    /*
    public void addToManyTarget(String relName, DataObject value, boolean setReverse, int index)
    {
        if (value == null) {
            throw new NullPointerException("Attempt to add null target DataObject.");
        }

        willConnect(relName, value);

        // Now do the rest of the normal handling (regardless of whether it was
        // flattened or not)
        List list = (List) readProperty(relName);

        // call 'recordArcCreated' AFTER readProperty as readProperty ensures that this
        // object fault is resolved
        getDataContext().getObjectStore().recordArcCreated(
                this,
                value.getObjectId(),
                relName);

        list.add(index, value);

        if (value != null && setReverse) {
            setReverseRelationship(relName, value);
        }
    }
    */

    /**
     * Returns a map key for a given object and relationship.
     *
     * @since 3.0
     */
    /* Just wrong to have this
    private Object getMapKey(String relationshipName, Object value) {

        EntityResolver resolver = objectContext.getEntityResolver();
        ClassDescriptor descriptor = resolver
                .getClassDescriptor(objectId.getEntityName());

        if (descriptor == null) {
            throw new IllegalStateException("DataObject's entity is unmapped, objectId: "
                    + objectId);
        }

        Property property = descriptor.getProperty(relationshipName);
        if (property instanceof ToManyMapProperty) {
            return ((ToManyMapProperty) property).getMapKey(value);
        }

        throw new IllegalArgumentException("Relationship '"
                + relationshipName
                + "' is not a to-many Map");
    }
    */

    public void addToManyTarget(String relName, DataObject value, boolean setReverse, int index)
    {
        if (value == null) {
            throw new NullPointerException("Attempt to add null target DataObject.");
        }

        willConnect(relName, value);

        // Now do the rest of the normal handling (regardless of whether it was
        // flattened or not)
        Object holder = readProperty(relName);

        // call 'propertyChanged' AFTER readProperty as readProperty ensures that this
        // object fault is resolved
        getObjectContext().propertyChanged(this, relName, null, value);

        // TODO: andrus 8/20/2007 - can we optimize this somehow, avoiding type checking??
        if (holder instanceof List) {
            ((List<Object>) holder).add( index, value);
        }
        else if (holder instanceof Collection) {
            ((Collection<Object>) holder).add( value);
        }
        else if (holder instanceof Map) {
            ((Map<Object, Object>) holder).put(getMapKey(relName, value), value);
        }

        if (setReverse) {
            setReverseRelationship(relName, value);
        }
    }

    public boolean hasEmailOnList( List emailList)
    {
        return workerHelper.hasEmailOnList( emailList);
    }

    public boolean onHolidayWholePeriod(MonthInYearI month, int year)
    {
        return workerHelper.onHolidayWholePeriod( month, year);
    }

    public void setWorkerHelper( WorkerHelper workerHelper)
    {
        this.workerHelper = workerHelper;
    }
}



