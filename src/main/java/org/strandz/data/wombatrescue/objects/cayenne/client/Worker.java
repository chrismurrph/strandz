package org.strandz.data.wombatrescue.objects.cayenne.client;

import org.strandz.data.wombatrescue.objects.FlexibilityI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.data.wombatrescue.objects.SeniorityI;
import org.strandz.data.wombatrescue.objects.SexI;
import org.strandz.data.wombatrescue.objects.WhichShiftI;
import org.strandz.data.wombatrescue.objects.WorkerHelper;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.data.wombatrescue.objects.cayenne.client.auto._Worker;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.note.WombatNote;
import org.apache.cayenne.PersistentObject;

import java.util.Date;
import java.util.List;

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
        /**/
        Err.pr(WombatNote.NEW_WORKER_INSTANCE, "Constructing VOLUNTEER ID " + incrementId);
        if(incrementId == 0 /*214*/)
        {
            Err.stack();
        }
        /**/
        workerHelper = new WorkerHelper( this);
    }

    /**
     *
     * @param nullWorkerToSetGroup - Comes from the DB where there is only one 'null/dummy' worker
     */
    public Worker(WorkerI nullWorkerToSetGroup)
    {
        this();
        init( nullWorkerToSetGroup);
    }

    public void installHelper()
    {
        Err.error();
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
            Assert.notNull( ((PersistentObject)nullWorkerToSetGroup).getObjectContext(),
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

    /*
    public boolean isDummy()
    {
        boolean result = true;
        //dummy is a part of toString(), so we don't want to be too strict with error checking
        //Assert.notNull( getDummy(), "dummy should never be null, only either 0 or 1, problem with " + getToLong());
        if(getDummy() != null)
        {
            if(getDummy().intValue() == 0)
            {
                result = false;
            }
        }
        else
        {
            //ok for the one using for secondary purposes
            //Err.error( "null for dummy found for worker ID: " + incrementId);
        }
        return result;
    }

    public void setDummy( boolean b)
    {
        if(b)
        {
            setDummy( Utils.ONE.byteValue());
        }
        else
        {
            setDummy( Utils.ZERO.byteValue());
        }
    }
    */

    /*
    public boolean isGroupContactPerson()
    {
        boolean result = true;
        if(getGroupContactPerson().intValue() == 0)
        {
            result = false;
        }
        return result;
    }

    public void setGroupContactPerson( boolean b)
    {
        if(b)
        {
            setGroupContactPerson( Utils.ONE.byteValue());
        }
        else
        {
            setGroupContactPerson( Utils.ZERO.byteValue());
        }
    }
    */

    /*
    public boolean isUnknown()
    {
        boolean result = true;
        if(getUnknown().intValue() == 0)
        {
            result = false;
        }
        return result;
    }

    public void setUnknown( boolean b)
    {
        if(b)
        {
            setUnknown( Utils.ONE.byteValue());
        }
        else
        {
            setUnknown( Utils.ZERO.byteValue());
        }
    }
    */

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
        /*
        Err.pr( "Flexibility of " + getChristianName() + " is being set to " + flexibility);
        Err.pr( "Is null: " + (flexibility == null));
        if(flexibility != null)
        {
            Err.pr( "Type of Flexibility using is " + flexibility.getClass());
            Err.pr( "name is " + flexibility.getName());
            Err.pr( "id is " + flexibility.getId());
        }
        */
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

    public void addRosterSlot(RosterSlotI rosterSlot, int index)
    {
        addToRosterSlots( (RosterSlot)rosterSlot, index);
    }

    public void validate() throws ValidationException
    {
        workerHelper.validate();
    }

    /**
     * Copied from super and altered to allow adding at a particular point
     */
    public void addToRosterSlots(RosterSlot object, int index)
    {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "rosterSlots", true);
        }

        this.rosterSlots.add( index, object);
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



