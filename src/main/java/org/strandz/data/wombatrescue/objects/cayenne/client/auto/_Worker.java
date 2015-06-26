package org.strandz.data.wombatrescue.objects.cayenne.client.auto;

import java.util.Date;
import java.util.List;

import org.apache.cayenne.PersistentObject;
import org.apache.cayenne.ValueHolder;
import org.strandz.data.wombatrescue.objects.cayenne.client.BuddyManager;
import org.strandz.data.wombatrescue.objects.cayenne.client.Flexibility;
import org.strandz.data.wombatrescue.objects.cayenne.client.RosterSlot;
import org.strandz.data.wombatrescue.objects.cayenne.client.Seniority;
import org.strandz.data.wombatrescue.objects.cayenne.client.Sex;
import org.strandz.data.wombatrescue.objects.cayenne.client.WhichShift;
import org.strandz.data.wombatrescue.objects.cayenne.client.Worker;

/**
 * A generated persistent class mapped as "Worker" Cayenne entity. It is a good idea to
 * avoid changing this class manually, since it will be overwritten next time code is
 * regenerated. If you need to make any customizations, put them in a subclass.
 */
public abstract class _Worker extends PersistentObject {

    public static final String AWAY1END_PROPERTY = "away1End";
    public static final String AWAY1START_PROPERTY = "away1Start";
    public static final String AWAY2END_PROPERTY = "away2End";
    public static final String AWAY2START_PROPERTY = "away2Start";
    public static final String BIRTHDAY_PROPERTY = "birthday";
    public static final String CHRISTIAN_NAME_PROPERTY = "christianName";
    public static final String COMMENTS_PROPERTY = "comments";
    public static final String CONTACT_NAME_PROPERTY = "contactName";
    public static final String DUMMY_PROPERTY = "dummy";
    public static final String EMAIL1_PROPERTY = "email1";
    public static final String EMAIL2_PROPERTY = "email2";
    public static final String GROUP_CONTACT_PERSON_PROPERTY = "groupContactPerson";
    public static final String GROUP_NAME_PROPERTY = "groupName";
    public static final String HOME_PHONE_PROPERTY = "homePhone";
    public static final String JDOCLASS_PROPERTY = "jdoclass";
    public static final String JDOVERSION_PROPERTY = "jdoversion";
    public static final String MOBILE_PHONE_PROPERTY = "mobilePhone";
    public static final String POSTCODE_PROPERTY = "postcode";
    public static final String STREET_PROPERTY = "street";
    public static final String SUBURB_PROPERTY = "suburb";
    public static final String SURNAME_PROPERTY = "surname";
    public static final String UNKNOWN_PROPERTY = "unknown";
    public static final String WORK_PHONE_PROPERTY = "workPhone";
    public static final String ACTUAL_BELONGS_TO_GROUP_PROPERTY = "actualBelongsToGroup";
    public static final String ACTUAL_FLEXIBILITY_PROPERTY = "actualFlexibility";
    public static final String ACTUAL_SENIORITY_PROPERTY = "actualSeniority";
    public static final String ACTUAL_SEX_PROPERTY = "actualSex";
    public static final String ACTUAL_SHIFT_PREFERENCE_PROPERTY = "actualShiftPreference";
    public static final String BUDDY_MANAGER_PROPERTY = "buddyManager";
    public static final String ROSTER_SLOTS_PROPERTY = "rosterSlots";
    public static final String WORKERS_IN_GROUP_PROPERTY = "workersInGroup";

    protected Date away1End;
    protected Date away1Start;
    protected Date away2End;
    protected Date away2Start;
    protected Date birthday;
    protected String christianName;
    protected String comments;
    protected String contactName;
    protected boolean dummy;
    protected String email1;
    protected String email2;
    protected boolean groupContactPerson;
    protected String groupName;
    protected String homePhone;
    protected String jdoclass;
    protected Integer jdoversion;
    protected String mobilePhone;
    protected String postcode;
    protected String street;
    protected String suburb;
    protected String surname;
    protected boolean unknown;
    protected String workPhone;
    protected ValueHolder actualBelongsToGroup;
    protected ValueHolder actualFlexibility;
    protected ValueHolder actualSeniority;
    protected ValueHolder actualSex;
    protected ValueHolder actualShiftPreference;
    protected List<BuddyManager> buddyManager;
    protected List<RosterSlot> rosterSlots;
    protected List<Worker> workersInGroup;

    public Date getAway1End() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "away1End", false);
        }

        return away1End;
    }
    public void setAway1End(Date away1End) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "away1End", false);
        }

        Object oldValue = this.away1End;
        this.away1End = away1End;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "away1End", oldValue, away1End);
        }
    }

    public Date getAway1Start() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "away1Start", false);
        }

        return away1Start;
    }
    public void setAway1Start(Date away1Start) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "away1Start", false);
        }

        Object oldValue = this.away1Start;
        this.away1Start = away1Start;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "away1Start", oldValue, away1Start);
        }
    }

    public Date getAway2End() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "away2End", false);
        }

        return away2End;
    }
    public void setAway2End(Date away2End) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "away2End", false);
        }

        Object oldValue = this.away2End;
        this.away2End = away2End;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "away2End", oldValue, away2End);
        }
    }

    public Date getAway2Start() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "away2Start", false);
        }

        return away2Start;
    }
    public void setAway2Start(Date away2Start) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "away2Start", false);
        }

        Object oldValue = this.away2Start;
        this.away2Start = away2Start;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "away2Start", oldValue, away2Start);
        }
    }

    public Date getBirthday() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "birthday", false);
        }

        return birthday;
    }
    public void setBirthday(Date birthday) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "birthday", false);
        }

        Object oldValue = this.birthday;
        this.birthday = birthday;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "birthday", oldValue, birthday);
        }
    }

    public String getChristianName() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "christianName", false);
        }

        return christianName;
    }
    public void setChristianName(String christianName) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "christianName", false);
        }

        Object oldValue = this.christianName;
        this.christianName = christianName;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "christianName", oldValue, christianName);
        }
    }

    public String getComments() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "comments", false);
        }

        return comments;
    }
    public void setComments(String comments) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "comments", false);
        }

        Object oldValue = this.comments;
        this.comments = comments;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "comments", oldValue, comments);
        }
    }

    public String getContactName() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "contactName", false);
        }

        return contactName;
    }
    public void setContactName(String contactName) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "contactName", false);
        }

        Object oldValue = this.contactName;
        this.contactName = contactName;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "contactName", oldValue, contactName);
        }
    }

    public boolean isDummy() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "dummy", false);
        }

        return dummy;
    }
    public void setDummy(boolean dummy) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "dummy", false);
        }

        Object oldValue = this.dummy;
        this.dummy = dummy;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "dummy", oldValue, dummy);
        }
    }

    public String getEmail1() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "email1", false);
        }

        return email1;
    }
    public void setEmail1(String email1) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "email1", false);
        }

        Object oldValue = this.email1;
        this.email1 = email1;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "email1", oldValue, email1);
        }
    }

    public String getEmail2() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "email2", false);
        }

        return email2;
    }
    public void setEmail2(String email2) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "email2", false);
        }

        Object oldValue = this.email2;
        this.email2 = email2;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "email2", oldValue, email2);
        }
    }

    public boolean isGroupContactPerson() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "groupContactPerson", false);
        }

        return groupContactPerson;
    }
    public void setGroupContactPerson(boolean groupContactPerson) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "groupContactPerson", false);
        }

        Object oldValue = this.groupContactPerson;
        this.groupContactPerson = groupContactPerson;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "groupContactPerson", oldValue, groupContactPerson);
        }
    }

    public String getGroupName() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "groupName", false);
        }

        return groupName;
    }
    public void setGroupName(String groupName) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "groupName", false);
        }

        Object oldValue = this.groupName;
        this.groupName = groupName;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "groupName", oldValue, groupName);
        }
    }

    public String getHomePhone() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "homePhone", false);
        }

        return homePhone;
    }
    public void setHomePhone(String homePhone) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "homePhone", false);
        }

        Object oldValue = this.homePhone;
        this.homePhone = homePhone;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "homePhone", oldValue, homePhone);
        }
    }

    public String getJdoclass() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "jdoclass", false);
        }

        return jdoclass;
    }
    public void setJdoclass(String jdoclass) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "jdoclass", false);
        }

        Object oldValue = this.jdoclass;
        this.jdoclass = jdoclass;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "jdoclass", oldValue, jdoclass);
        }
    }

    public Integer getJdoversion() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "jdoversion", false);
        }

        return jdoversion;
    }
    public void setJdoversion(Integer jdoversion) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "jdoversion", false);
        }

        Object oldValue = this.jdoversion;
        this.jdoversion = jdoversion;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "jdoversion", oldValue, jdoversion);
        }
    }

    public String getMobilePhone() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "mobilePhone", false);
        }

        return mobilePhone;
    }
    public void setMobilePhone(String mobilePhone) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "mobilePhone", false);
        }

        Object oldValue = this.mobilePhone;
        this.mobilePhone = mobilePhone;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "mobilePhone", oldValue, mobilePhone);
        }
    }

    public String getPostcode() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "postcode", false);
        }

        return postcode;
    }
    public void setPostcode(String postcode) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "postcode", false);
        }

        Object oldValue = this.postcode;
        this.postcode = postcode;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "postcode", oldValue, postcode);
        }
    }

    public String getStreet() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "street", false);
        }

        return street;
    }
    public void setStreet(String street) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "street", false);
        }

        Object oldValue = this.street;
        this.street = street;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "street", oldValue, street);
        }
    }

    public String getSuburb() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "suburb", false);
        }

        return suburb;
    }
    public void setSuburb(String suburb) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "suburb", false);
        }

        Object oldValue = this.suburb;
        this.suburb = suburb;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "suburb", oldValue, suburb);
        }
    }

    public String getSurname() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "surname", false);
        }

        return surname;
    }
    public void setSurname(String surname) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "surname", false);
        }

        Object oldValue = this.surname;
        this.surname = surname;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "surname", oldValue, surname);
        }
    }

    public boolean isUnknown() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "unknown", false);
        }

        return unknown;
    }
    public void setUnknown(boolean unknown) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "unknown", false);
        }

        Object oldValue = this.unknown;
        this.unknown = unknown;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "unknown", oldValue, unknown);
        }
    }

    public String getWorkPhone() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "workPhone", false);
        }

        return workPhone;
    }
    public void setWorkPhone(String workPhone) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "workPhone", false);
        }

        Object oldValue = this.workPhone;
        this.workPhone = workPhone;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "workPhone", oldValue, workPhone);
        }
    }

    public Worker getActualBelongsToGroup() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualBelongsToGroup", true);
        }

        return (Worker) actualBelongsToGroup.getValue();
    }
    public void setActualBelongsToGroup(Worker actualBelongsToGroup) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualBelongsToGroup", true);
        }

        this.actualBelongsToGroup.setValue(actualBelongsToGroup);
    }

    public Flexibility getActualFlexibility() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualFlexibility", true);
        }

        return (Flexibility) actualFlexibility.getValue();
    }
    public void setActualFlexibility(Flexibility actualFlexibility) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualFlexibility", true);
        }

        this.actualFlexibility.setValue(actualFlexibility);
    }

    public Seniority getActualSeniority() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualSeniority", true);
        }

        return (Seniority) actualSeniority.getValue();
    }
    public void setActualSeniority(Seniority actualSeniority) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualSeniority", true);
        }

        this.actualSeniority.setValue(actualSeniority);
    }

    public Sex getActualSex() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualSex", true);
        }

        return (Sex) actualSex.getValue();
    }
    public void setActualSex(Sex actualSex) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualSex", true);
        }

        this.actualSex.setValue(actualSex);
    }

    public WhichShift getActualShiftPreference() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualShiftPreference", true);
        }

        return (WhichShift) actualShiftPreference.getValue();
    }
    public void setActualShiftPreference(WhichShift actualShiftPreference) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualShiftPreference", true);
        }

        this.actualShiftPreference.setValue(actualShiftPreference);
    }

    public List<BuddyManager> getBuddyManager() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "buddyManager", true);
        }

        return buddyManager;
    }
    public void addToBuddyManager(BuddyManager object) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "buddyManager", true);
        }

        this.buddyManager.add(object);
    }
    public void removeFromBuddyManager(BuddyManager object) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "buddyManager", true);
        }

        this.buddyManager.remove(object);
    }

    public List<RosterSlot> getRosterSlots() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "rosterSlots", true);
        }

        return rosterSlots;
    }
    public void addToRosterSlots(RosterSlot object) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "rosterSlots", true);
        }

        this.rosterSlots.add(object);
    }
    public void removeFromRosterSlots(RosterSlot object) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "rosterSlots", true);
        }

        this.rosterSlots.remove(object);
    }

    public List<Worker> getWorkersInGroup() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "workersInGroup", true);
        }

        return workersInGroup;
    }
    public void addToWorkersInGroup(Worker object) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "workersInGroup", true);
        }

        this.workersInGroup.add(object);
    }
    public void removeFromWorkersInGroup(Worker object) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "workersInGroup", true);
        }

        this.workersInGroup.remove(object);
    }

}
