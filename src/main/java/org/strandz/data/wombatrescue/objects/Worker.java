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
package org.strandz.data.wombatrescue.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NullVerifiable;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.lgpl.data.objects.MonthInYear;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * In the case where groupName is filled in, the members of the group
 * will be expected to refer to the group via belongsToGroup. If the
 * group member is also a leader then groupContactPerson will be true.
 * For 'group' Volunteers expect groupName to be filled in but nothing
 * else. However the 'group' would have alot of RosterSlots.
 */
public class Worker implements Comparable, Serializable, NullVerifiable, WorkerI
{
    // private int pkId; // primary-key=true
    private boolean dummy;
    // PK (although some may be null!)
    private String christianName;
    private String surname;
    private String groupName;
    //
    private Worker belongsToGroup;
    private boolean groupContactPerson;
    private String street;
    private String suburb;
    private String postcode;
    private String homePhone;
    private String workPhone;
    private String mobilePhone;
    private String contactName;
    private String email1;
    private String email2;
    private boolean unknown;
    private WhichShift shiftPreference;
    /**
     * Even thou is a calculated field we have it here for the sake of being
     * able to do a JDO query
     */
    //private String searchBy;
    /*
    private boolean noEvenings;
    private boolean noOvernights;
    private boolean flexible;
    */
    private Seniority seniority;
    private Sex sex;
    private Flexibility flexibility;
    private Date birthday;
    private Date away1Start;
    private Date away1End;
    private Date away2Start;
    private Date away2End;
    private List<RosterSlotI> rosterSlots = new ArrayList<RosterSlotI>();
    //
    private String comments;
    private transient WorkerHelper workerHelper;
    //
    public transient static final Worker NULL = new Worker();
    public transient static final String NO_PHONE_SIGNIFIER = "n/a";
    
    public static final transient boolean ONLY_FOR_JDO = true;

    private transient static int constructedTimes;
    public transient int incrementId;
    private transient static int timess1;
    private transient static int timess2;
    private transient static int timesg1;
    private transient static int timesg2;
    
    static
    {
        NULL.setDummy( true);
    }

    public static void main(String[] args)
    {
        Worker worker = new Worker();
        boolean onHoliday = worker.onHolidayWholePeriod( MonthInYear.JULY, 2006);
    }

    public Worker()
    {
        constructedTimes++;
        incrementId = constructedTimes;
        workerHelper = new WorkerHelper( this);
        if(incrementId > 1) //Having the null one is still fine
        {
            //Err.stack();
        }
    }

    public void installHelper()
    {
        Err.error();
    }

    public int getId()
    {
        return incrementId;
    }

    public String getName()
    {
        return "" + getId();
    }

    public boolean isStrange()
    {
        return workerHelper().isStrange();
    }

    public Worker(WorkerI nullWorker)
    {
        this();
        init( nullWorker);
    }

    public void init( WorkerI nullWorkerToSetGroup)
    {
        if(workerHelper == null)
        {
            Err.error( "Will never happen");
            constructedTimes++;
            incrementId = constructedTimes;
        }
        /*
        Err.pr( "Constructed VOLUNTEER ID " + id);
        if(id == 124)
        {
        Err.stack();
        }
        */
        if(nullWorkerToSetGroup == null)
        {
            /*
            ACTUAL REASON for below problem was that jdogenie.jar was still in the classpath
            when was working with jpox-1.1.0-beta-1.jar - incedently this is the one that has
            its own jdo.jar, thus the real jdo.jar should also be out of the classpath
            */
            //JPOX enhancer constructs a Worker, and the null assignment gave
            //a strange no such method error:
            //[jpoxenhancer] Exception in thread "main" java.lang.NoSuchMethodError: javax.jdo.spi.PersistenceCapable.jdoIsDetached()Z
            //if(!onlyForJDO)
            {
                belongsToGroup = NULL;
            }
        }
        else // this is for the JDO situation
        {
            belongsToGroup = (Worker)nullWorkerToSetGroup;
        }
        workerHelper = new WorkerHelper( this);
    }

    /**
     * Returns the christianName.
     *
     * @return String
     */
    public String getChristianName()
    {
        return christianName;
    }

    /**
     * Returns the surname.
     *
     * @return String
     */
    public String getSurname()
    {
        return surname;
    }

    /**
     * Sets the christianName.
     *
     * @param christianName The christianName to set
     */
    public void setChristianName(String christianName)
    {
        this.christianName = christianName;
    }

    /**
     * Sets the surname.
     *
     * @param surname The surname to set
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    /**
     * Returns the contactName.
     *
     * @return String
     */
    public String getContactName()
    {
        return contactName;
    }

    /**
     * Returns the groupName.
     *
     * @return String
     */
    public String getGroupName()
    {
        return groupName;
    }

    /**
     * Returns the homePhone.
     *
     * @return String
     */
    public String getHomePhone()
    {
        return homePhone;
    }

    /**
     * Returns the mobilePhone.
     *
     * @return String
     */
    public String getMobilePhone()
    {
        return mobilePhone;
    }

    /**
     * Returns the postcode.
     *
     * @return String
     */
    public String getPostcode()
    {
        return postcode;
    }

    /**
     * Returns the street.
     *
     * @return String
     */
    public String getStreet()
    {
        return street;
    }

    /**
     * Returns the suburb.
     *
     * @return String
     */
    public String getSuburb()
    {
        return suburb;
    }

    /**
     * Returns the workPhone.
     *
     * @return String
     */
    public String getWorkPhone()
    {
        return workPhone;
    }

    /**
     * Sets the contactName.
     *
     * @param contactName The contactName to set
     */
    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    /**
     * Sets the groupName.
     *
     * @param groupName The groupName to set
     */
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    /**
     * Sets the homePhone.
     *
     * @param homePhone The homePhone to set
     */
    public void setHomePhone(String homePhone)
    {
        this.homePhone = homePhone;
    }

    /**
     * Sets the mobilePhone.
     *
     * @param mobilePhone The mobilePhone to set
     */
    public void setMobilePhone(String mobilePhone)
    {
        this.mobilePhone = mobilePhone;
    }

    /**
     * Sets the postcode.
     *
     * @param postcode The postcode to set
     */
    public void setPostcode(String postcode)
    {
        this.postcode = postcode;
    }

    /**
     * Sets the street.
     *
     * @param street The street to set
     */
    public void setStreet(String street)
    {
        this.street = street;
    }

    /**
     * Sets the suburb.
     *
     * @param suburb The suburb to set
     */
    public void setSuburb(String suburb)
    {
        this.suburb = suburb;
    }

    /**
     * Sets the workPhone.
     *
     * @param workPhone The workPhone to set
     */
    public void setWorkPhone(String workPhone)
    {
        this.workPhone = workPhone;
    }

    /**
     * Returns the belongsToGroup.
     *
     * @return Volunteer
     */
    public WorkerI getBelongsToGroup()
    {
        return belongsToGroup;
    }

    /**
     * Returns the groupContactPerson.
     *
     * @return boolean
     */
    public boolean isGroupContactPerson()
    {
        return groupContactPerson;
    }

    /**
     * Sets the belongsToGroup.
     *
     * @param belongsToGroup The belongsToGroup to set
     */
    public void setBelongsToGroup( WorkerI belongsToGroup)
    {
        if(belongsToGroup == null)
        {
            Err.error("Should be using the NULL worker rather than null");
        }

        String groupName = belongsToGroup.getGroupName();
        if(Utils.isBlank( groupName))
        {
            // Err.pr( "Potential Child vol: " + this);
            // Err.pr( "Potential Parent vol: " + belongsToGroup);
            //if(!belongsToGroup.equals(Worker.NULL))
            if(!belongsToGroup.isDummy())
            {
                /* This is a correct error message, however it is also ok to have a worker with a
                   theoretically bad state being used for validation
                Err.error(
                    "Cannot call setBelongsToGroup() where parent does not have a group name: <" + belongsToGroup + ">");
                */
            }
        }
        this.belongsToGroup = (Worker)belongsToGroup;
    }

    /**
     * Sets the groupContactPerson.
     *
     * @param groupContactPerson The groupContactPerson to set
     */
    public void setGroupContactPerson(boolean groupContactPerson)
    {
        this.groupContactPerson = groupContactPerson;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public WhichShiftI getShiftPreference()
    {
        return shiftPreference;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public void setShiftPreference(WhichShiftI shiftPreference)
    {
        this.shiftPreference = (WhichShift)shiftPreference;
    }

    public SeniorityI getSeniority()
    {
        return seniority;
    }

    public SexI getSex()
    {
        return sex;
    }

    public void setSeniority(SeniorityI seniority)
    {
        this.seniority = (Seniority)seniority;
    }

    public void setSex(SexI sex)
    {
        this.sex = (Sex)sex;
    }

    public FlexibilityI getFlexibility()
    {
        return flexibility;
    }

    public void setFlexibility(FlexibilityI flexibility)
    {
        if(this.flexibility != null && !this.flexibility.equals(Flexibility.NULL))
        {
            if(flexibility.equals(Flexibility.NULL))
            {
                //over-zealous erroring? NO. At the moment the UI does not allow this
                Err.error("Why plugging a NULL into flexibility that used to be " + this.flexibility);
            }
        }
        if(flexibility == null)
        {
            Err.error("Why setting flexibility to " + flexibility + " from " + this.flexibility);
        }
        this.flexibility = (Flexibility)flexibility;
    }

    public boolean hasEmailOnList(List emailList)
    {
        return workerHelper().hasEmailOnList( emailList);
    }

    public String getEmail1()
    {
        return email1;
    }

    public String getEmail2()
    {
        return email2;
    }

    public void setEmail1(String email1)
    {
        this.email1 = email1;
    }

    public void setEmail2(String email2)
    {
        this.email2 = email2;
    }

    public boolean isUnknown()
    {
        return unknown;
    }

    public void setUnknown(boolean unknown)
    {
        this.unknown = unknown;
    }

    /*
    public List getPlayers() {
    return rosterSlots;
    }

    public void setRosterSlots(List rosterSlots) {
    this.rosterSlots = rosterSlots;
    }
    */

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    /*
    * duplicate method
    public boolean hasAnEmailOn( List fromYahooEmails)
    {
    boolean result = false;
    if(pUtils.containsIgnoreCase( fromYahooEmails, email1) ||
    pUtils.containsIgnoreCase( fromYahooEmails, email2))
    {
    result = true;
    }
    return result;
    }
    */

    public Date getAway1End()
    {
        return away1End;
    }

    public void setAway1End(Date away1End)
    {
        this.away1End = away1End;
    }

    public Date getAway1Start()
    {
        return away1Start;
    }

    public void setAway1Start(Date away1Start)
    {
        this.away1Start = away1Start;
    }

    public Date getAway2End()
    {
        return away2End;
    }

    public void setAway2End(Date away2End)
    {
        this.away2End = away2End;
    }

    public Date getAway2Start()
    {
        return away2Start;
    }

    public void setAway2Start(Date away2Start)
    {
        this.away2Start = away2Start;
    }
        
    public void addRosterSlot(RosterSlotI rosterSlot)
    {
        rosterSlots.add( rosterSlot);
        rosterSlot.setWorker(this);
    }
    
    public void addRosterSlot(RosterSlotI rosterSlot, int index)
    {
        rosterSlots.add( index, rosterSlot);
        rosterSlot.setWorker(this);
    }

    public boolean removeRosterSlot(RosterSlotI rosterSlot)
    {
        boolean result;
        try
        {
            result = rosterSlots.remove(rosterSlot);
        }
        catch(Exception ex)
        {
            /*
             * JDO may not let you touch a deleted object. So perhaps a RosterSlot
             * will be exist that points to a deleted Worker - of course cascade
             * delete will ensure this doesn't happen - must have a task to check
             * for these things ... TODO
             */
            result = false;
        }
        if(result)
        {
            //TODO Will be orphans like this so research on how to best
            //delete the RosterSlot at the same time as this method is
            //called - cascade delete is the answer, see above
            if(rosterSlot.getWorker() == this)
            {
                rosterSlot.setWorker(null);
            }
        }
        return result;
    }
    
    //When get an error from this one, then do the one below instead, with an InsteadOfAddRemoveTrigger:
//    public List getRosterSlots()
//    {
//        return Collections.unmodifiableList(rosterSlots);
//    }
    
    public List getRosterSlots()
    {
        //Collections.sort( rosterSlots, MaleThenFemaleComparator.INSTANCE);
        //InsteadOfAddRemoveTrigger
        //return Collections.unmodifiableList(games);
        /* tmp comment
        SdzEMAssert.isEntityManaged( rosterSlots, "getRosterSlot()");
        */
        return rosterSlots;
    }
    
//    private static class MaleThenFemaleComparator implements Comparator
//    {
//        private static final Comparator INSTANCE = new MaleThenFemaleComparator();
//
//        public int compare(Object one, Object two)
//        {
//            int result = 0;
//            if(!(one instanceof Game))
//            {
//                return 1;
//            }
//            if(!(two instanceof Game))
//            {
//                return 1;
//            }
//
//            Game m1 = (Game) one;
//            Game m2 = (Game) two;
//            result = matchCf(m1, m2);
//            return result;
//        }
//
//        private static int matchCf(Game m1, Game m2)
//        {
//            int result = Utils.UNSET_INT;
//            Division div1 = m1.getDivision();
//            Division div2 = m2.getDivision();
//            result = Utils.compareTo(div1, div2);
//            if(result == 0)
//            {
//                KickOffTime kot1 = m1.getKickOffTime();
//                KickOffTime kot2 = m2.getKickOffTime();
//                result = Utils.compareTo(kot1, kot2);
//            }
//            return result;
//        }
//    }

//TODO Introduce this again later and even get get of the
// add/remove signature methods and make this do the same
// setWorker() trick
//  public void setRosterSlots(List rosterSlots)
//  {
//    this.rosterSlots = rosterSlots;
//  }

    /*
    public static class ID implements Serializable
    {
    public int pkId;

    public ID()
    {
    }

    public ID( String pkId)
    {
    this.pkId = Integer.parseInt( pkId);
    }

    public boolean equals( Object that)
    {
    return this.equals( (ID)that);
    }

    public boolean equals( ID o)
    {
    boolean result = false;
    if(o == this)
    {
    result = true;
    }
    else if(!(o instanceof ID))
    {
    //nufin
    }
    else
    {
    ID test = (ID)o;
    if(test.pkId == pkId)
    {
    result = true;
    }
    }
    return result;
    }

    public int hashCode()
    {
    int result = 17;
    result = 37*result+pkId;
    return result;
    }

    public String toString()
    {
    return "" + pkId;
    }
    }
    */

    public boolean isDummy()
    {
        return dummy;
    }

    public void setDummy(boolean dummy)
    {
        this.dummy = dummy;
        //Err.pr( "Have set dummy for " + this);
    }
    
    public void validate() throws ValidationException
    {
        workerHelper.validate();
    }

    public static Worker getByName(List<Worker> workers, String name)
    {
        Worker result = null;
        for(Iterator<Worker> iterator = workers.iterator(); iterator.hasNext();)
        {
            Worker worker = iterator.next();
            if(worker.toString().equals( name))
            {
                result = worker;
                break;
            }
        }
        return result;
    }

    public String getToLong()
    {
        return workerHelper().getToLong();
    }

    public String getToShort()
    {
        return workerHelper().getToShort();
    }
    
    public String formatWithPhone()
    {
        return workerHelper().formatWithPhone();
    }

    public String formatWithPhones()
    {
        return workerHelper().formatWithPhones();
    }

    public String getToValidate()
    {
        return workerHelper().getToValidate();
    }

    public String getBestPhone()
    {
        return workerHelper().getBestPhone();
    }

    public String formatAllPhonesAndEmail()
    {
        return workerHelper().formatAllPhonesAndEmail();
    }

    public boolean onHoliday(Date date)
    {
        return workerHelper().onHoliday( date);
    }

    public boolean isAvailable( Date first, Date last)
    {
        return workerHelper().isAvailable( first, last);
    }

    public int compareTo( Object obj)
    {
        return workerHelper().compareTo( obj);
    }

    public String toString()
    {
        return workerHelper().helperToString();
    }

    public boolean equals(Object o)
    {
        return workerHelper().helperEquals( o);
    }

    public int hashCode()
    {
        return workerHelper().helperHashCode();
    }

    public String getOrderBy()
    {
        return workerHelper().getOrderBy();
    }

    public String formatWithHolidays(Date first, Date last)
    {
        return workerHelper().formatWithHolidays( first, last);
    }

    public List getRosterslots(List allRosterSlots)
    {
        return workerHelper().getRosterslots( allRosterSlots);
    }

    public boolean onHolidayWholePeriod(Date periodStart, Date periodEnd)
    {
        return workerHelper().onHolidayWholePeriod( periodStart, periodEnd);
    }

    public boolean onHolidayWholePeriod(MonthInYearI month, int year)
    {
        return workerHelper().onHolidayWholePeriod( month, year);
    }

    /*
     * copyTrick made an NPE - b/c WorkerHelper is transient
     */
    private WorkerHelper workerHelper()
    {
        if(workerHelper == null)
        {
            workerHelper = new WorkerHelper( this);
        }
        return workerHelper;
    }
}
