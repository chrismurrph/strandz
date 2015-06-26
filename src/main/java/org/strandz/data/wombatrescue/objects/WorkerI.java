package org.strandz.data.wombatrescue.objects;

import org.strandz.lgpl.util.NullVerifiable;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.util.IdentifierI;
import org.strandz.lgpl.data.objects.MonthInYearI;

import java.util.List;
import java.util.Date;

/**
 * User: Chris
 * Date: 29/08/2008
 * Time: 08:21:04
 */
public interface WorkerI extends NullVerifiable, Comparable, IdentifierI
{
    void init( WorkerI nullWorkerToSetGroup);
    void installHelper();
    String getToLong();
    String getToShort();
    String getToValidate();
    String getBestPhone();
    String formatAllPhonesAndEmail();
    boolean isStrange();
    SeniorityI getSeniority();
    void setSeniority( SeniorityI seniority);
    SexI getSex();
    void setSex( SexI sex);
    WhichShiftI getShiftPreference();
    void setShiftPreference( WhichShiftI whichShift);
    FlexibilityI getFlexibility();
    void setFlexibility( FlexibilityI flexibility);
    WorkerI getBelongsToGroup();
    void setBelongsToGroup( WorkerI worker);
    String getChristianName();
    void setChristianName( String christianName);
    String getSurname();
    void setSurname( String surname);
    String getGroupName();
    void setGroupName( String groupName);
    List getRosterSlots();
    boolean isUnknown();
    void setUnknown(boolean unknown);
    String formatWithPhone();
    String formatWithPhones();
    Date getBirthday();
    String getComments();
    void setComments( String comments);
    String getContactName();
    void setContactName( String contactName);
    String getEmail1();
    void setEmail1( String email1);
    String getEmail2();
    boolean isGroupContactPerson();
    String getHomePhone();
    String getMobilePhone();
    String getWorkPhone();
    void setWorkPhone( String workPhone);
    void setMobilePhone( String mobilePhone);
    String getPostcode();
    String getStreet();
    String getSuburb();
    Date getAway1Start();
    Date getAway1End();
    Date getAway2Start();
    Date getAway2End();
    String toString();
    boolean equals(Object o);
    int hashCode();
    boolean onHoliday(Date date);
    boolean isAvailable( Date first, Date last);
    String getOrderBy();
    String formatWithHolidays(Date first, Date last);
    boolean onHolidayWholePeriod(MonthInYearI month, int year);
    List getRosterslots(List allRosterSlots);
    void addRosterSlot(RosterSlotI rosterSlot);
    void addRosterSlot(RosterSlotI rosterSlot, int index);
    boolean removeRosterSlot(RosterSlotI rosterSlot);
    void validate() throws ValidationException;
    int getId();
    boolean hasEmailOnList( List emailList);
    void setDummy( boolean dummy);
    void setAway1Start( Date date);
    void setAway1End( Date date);
    void setGroupContactPerson( boolean b);
    void setStreet( String street);
    void setSuburb( String suburb);
    void setPostcode( String postcode);
    void setHomePhone( String homePhone);
}
