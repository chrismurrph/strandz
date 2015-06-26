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
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Volunteer has been replaced by Worker. Only conversion programs
 * reference this DO.
 * <p/>
 * In the case where groupName is filled in, the members of the group
 * will be expected to refer to the group via belongsToGroup. If the
 * group member is also a leader then groupContactPerson will be true.
 * For 'group' Volunteers expect groupName to be filled in but nothing
 * else. However the 'group' would have alot of RosterSlots.
 */
public class Volunteer implements Comparable
{
    // private int pkId; // primary-key=true
    private boolean dummy;
    public transient static Volunteer NULL = new Volunteer();
    public transient static String NO_PHONE_SIGNIFIER = "n/a";
    // PK (although some may be null!)
    private String christianName;
    private String surname;
    private String groupName;
    //
    private Volunteer belongsToGroup;
    private boolean groupContactPerson = false;
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
    private boolean noEvenings;
    private boolean noOvernights;
    private boolean flexible;
    private Seniority seniority;
    private Sex sex;
    //private Flexibility flexibility;
    private Date birthday;
    private Date away1Start;
    private Date away1End;
    private Date away2Start;
    private Date away2End;
    // Not done two way relationship yet:
    // private List rosterSlots = new ArrayList();
    //
    private String comments;
    //
    private transient static int constructedTimes;
    public transient int id;
    private transient static int timess1;
    private transient static int timess2;
    private transient static int timesg1;
    private transient static int timesg2;

    /**
     * Don't use this constructor from JDO as if do then will always
     * be creating two volunteers when expecting to create one.
     */
    public Volunteer()
    {
        this(null);
    }

    public Volunteer(Volunteer nullVolunteer)
    {
        constructedTimes++;
        id = constructedTimes;
        /*
        Err.pr( "Constructed VOLUNTEER ID " + id);
        if(id == 124)
        {
        Err.stack();
        }
        */
        if(nullVolunteer == null)
        {
            belongsToGroup = NULL;
        }
        else // this is for the JDO situation
        {
            belongsToGroup = nullVolunteer;
        }
    }

    /**
     * Also have at least one Volunteers comparator, that does things differently,
     * and JDOQL ordering that different again! WombatBug.manyOrderings
     */
    public int compareTo(Object o)
    {
        int result = 0;
        Volunteer other = (Volunteer) o;
         /**/
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
        result = Utils.compareTo(christianName, other.getChristianName());
        if(result == 0)
        {
            result = Utils.compareTo(surname, other.getSurname());
            if(result == 0)
            {
                result = Utils.compareTo(groupName, other.getGroupName());
            }
        }
         /**/
        // Was removing from uniq list if only had same surname
        // result = XMLWombatData.GroupNameSurnameOrder.volunteerCf( this, other);
        return result;
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Volunteer))
        {// nufin
        }
        else
        {
            Volunteer test = (Volunteer) o;
            if((christianName == null
                ? test.christianName == null
                : christianName.equals(test.christianName)))
            {
                if((surname == null
                    ? test.surname == null
                    : surname.equals(test.surname)))
                {
                    if((groupName == null
                        ? test.groupName == null
                        : groupName.equals(test.groupName)))
                    {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result
            + (christianName == null ? 0 : christianName.hashCode());
        result = 37 * result + (surname == null ? 0 : surname.hashCode());
        result = 37 * result + (groupName == null ? 0 : groupName.hashCode());
        return result;
    }

    /*
    * class XMLWombatData.GroupNameSurnameOrder operates
    * by the same rules, enabling SET_ROW to give the
    * right result.
    */
    public boolean startsWith(String str)
    {
        boolean result = false;
        if(surname != null)
        {
            if(surname.startsWith(str))
            {
                result = true;
            }
        }
        /*
        * These others are right at the beginning now. Thus to find
        * them go to 'A' and previous
        else if(christianName != null)
        {
        if(christianName.startsWith( str))
        {
        result = true;
        }
        }
        else if(groupName != null)
        {
        if(groupName.startsWith( str))
        {
        result = true;
        }
        }
        */
        return result;
    }

    public String toString()
    {
        String result = null;
        if(christianName != null || surname != null)
        {
            if(christianName == null && surname != null)
            {
                result = surname;
            }
            else if(christianName != null && surname == null)
            {
                result = christianName;
            }
            else
            {
                result = christianName + " " + surname;
            }
        }
        else
        {
            result = groupName;
        }
        if(dummy)
        {
            result += " [NULL]";
        }
        // think causing NPE
        // if(result.indexOf( "null") != -1 && !result.equals( "null [NULL]"))
        // {
        // Err.error( "toString() not formatting well: <" + result + ">");
        // }
        return result;
    }

    public List getRosterslots(List allRosterSlots)
    {
        List result = new ArrayList();
        for(Iterator iter = allRosterSlots.iterator(); iter.hasNext();)
        {
            RosterSlot slot = (RosterSlot) iter.next();
            if(slot.getWorker().equals(this))
            {
                result.add(slot);
            }
        }
        return result;
    }

    public String formatWithPhone()
    {
        String result = toString();
        result += " (PH: " + getBestPhone() + ")";
        return result;
    }

    public String formatWithPhones()
    {
        String result = toString();
        result += formatAllPhones();
        return result;
    }

    public String formatAllPhones()
    {
        String result = " (W: " + formatWorkPhone() + " H: " + formatHomePhone()
            + " M: " + formatMobilePhone() + ")";
        return result;
    }

    public String formatAllPhonesAndEmail()
    {
        String result = " (W: " + formatWorkPhone() + " H: " + formatHomePhone()
            + " M: " + formatMobilePhone() + " Email: " + getEmail1() + ")";
        return result;
    }

    public String formatMobilePhone()
    {
        String result = getMobilePhone();
        if(result == null)
        {
            result = NO_PHONE_SIGNIFIER;
        }
        return result;
    }

    public String formatWorkPhone()
    {
        String result = getWorkPhone();
        if(result == null)
        {
            result = NO_PHONE_SIGNIFIER;
        }
        return result;
    }

    public String formatHomePhone()
    {
        String result = getHomePhone();
        if(result == null)
        {
            result = NO_PHONE_SIGNIFIER;
        }
        return result;
    }

    /*
    public String formatWithHolidays()
    {
    String result = toString();
    if(getAway1Start() != null)
    {
    result += " (HOL1: " + pTimeUtils.dateFormatter.format( getAway1Start()) + ", " +
    pTimeUtils.dateFormatter.format( getAway1End()) + ")";
    }
    if(getAway2Start() != null)
    {
    result += " (HOL2: " + pTimeUtils.dateFormatter.format( getAway2Start()) + ", " +
    pTimeUtils.dateFormatter.format( getAway2End()) + ")";
    }
    return result;
    }
    */

    public String formatWithHolidays(Date first, Date last)
    {
        String result = toString();
        String hols = formatHolidays(first, last);
        if(hols != null)
        {
            result += hols;
        }
        if(result.indexOf("null") != -1)
        {
            Err.error("formatWithHolidays() not formatting well: <" + result + ">");
        }
        return result;
    }

    public String formatHolidays(Date first, Date last)
    {
        String result = "";
        if(getAway1Start() != null)
        {
            if(TimeUtils.periodsOverlap(first, last, getAway1Start(), getAway1End(),
                toString()))
            {
                result += " (HOL1: " + TimeUtils.DATE_FORMAT.format(getAway1Start())
                    + ", " + TimeUtils.DATE_FORMAT.format(getAway1End()) + ")";
            }
        }
        if(getAway2Start() != null)
        {
            if(TimeUtils.periodsOverlap(first, last, getAway2Start(), getAway2End(),
                toString()))
            {
                result += " (HOL2: " + TimeUtils.DATE_FORMAT.format(getAway2Start())
                    + ", " + TimeUtils.DATE_FORMAT.format(getAway2End()) + ")";
            }
        }
        return result;
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

    public String getBestPhone()
    {
        String result = getWorkPhone();
        if(result == null)
        {
            result = getHomePhone();
        }
        if(result == null)
        {
            result = getMobilePhone();
        }
        if(result == null)
        {
            result = NO_PHONE_SIGNIFIER;
        }
        return result;
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
    public Volunteer getBelongsToGroup()
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
    public void setBelongsToGroup(Volunteer belongsToGroup)
    {
        if(belongsToGroup == null)
        {
            Err.error("Should be using the NULL volunteer rather than null");
        }

        String groupName = belongsToGroup.getGroupName();
        if(groupName == null || groupName.equals(""))
        {
            // Err.pr( "Potential Child vol: " + this);
            // Err.pr( "Potential Parent vol: " + belongsToGroup);
            if(!belongsToGroup.equals(Volunteer.NULL))
            {
                Err.error(
                    "Cannot call setBelongsToGroup() where parent does not have a group name");
            }
        }
        this.belongsToGroup = belongsToGroup;
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

    public WhichShift getShiftPreference()
    {
        return shiftPreference;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public void setShiftPreference(WhichShift shiftPreference)
    {
        this.shiftPreference = shiftPreference;
    }

    public Seniority getSeniority()
    {
        return seniority;
    }

    public Sex getSex()
    {
        return sex;
    }

    public void setSeniority(Seniority seniority)
    {
        this.seniority = seniority;
    }

    public void setSex(Sex sex)
    {
        this.sex = sex;
    }

    /*
    public Flexibility getFlexibility()
    {
      return flexibility;
    }

    public void setFlexibility( Flexibility flexibility )
    {
      this.flexibility = flexibility;
    }
    */

    public boolean hasEmailOnList(List emailList)
    {
        boolean result = false;
        if(Utils.containsIgnoreCase(emailList, getEmail1())
            || Utils.containsIgnoreCase(emailList, getEmail2()))
        {
            result = true;
        }
        return result;
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

    public String getToValidate()
    {
        String result = getTo();
        // sanity check
        if(result == null || result.equals("") || result.equals(" ")
            || result.toString().equals("null"))
        {
            Err.error(
                "Would be impolite result send an email result <" + result + ">");
        }
        return result;
    }

    public String getTo()
    {
        String result = null;
        String surname = getSurname();
        String christianName = getChristianName();
        String contact = getContactName();
        String group = getGroupName();
        if(surname == null)
        {
            surname = "";
        }
        if(christianName == null)
        {
            christianName = "";
        }
        if(!christianName.equals("") && !surname.equals(""))
        {
            result = christianName + " " + surname;
        }
        else if(!christianName.equals(""))
        {
            result = christianName;
        }
        else if(!surname.equals(""))
        {
            Err.error("Would be impolite to send to just a surname");
        }
        if(contact != null)
        {
            if(result == null && group != null)
            {
                result = contact + " (on behalf of " + group + ")";
            }
            else
            {
                result = contact + " (on behalf of " + result + ")";
            }
        }
        else if(group != null)
        {
            result = group;
        }
        // if(result.indexOf( "null") != -1)
        // {
        // Err.error( "getTo() not formatting well!");
        // }
        return result;
    }

    public boolean onHoliday(Date date)
    {
        boolean result = false;
        boolean show = false;
        if(toString().equals("David Ellis"))
        {
            Err.pr("Seeing if David on holiday for " + date);
            show = true;
        }
        if(away1Start != null && away1End != null)
        {
            if(show)
            {
                Err.pr("cfing with start " + away1Start + " and end " + away1End);
            }
            if((away1Start.before(date) && away1End.after(date))
                || away1Start.equals(date) || away1End.equals(date))
            {
                result = true;
            }
        }
        if(!result && away2Start != null && away2End != null)
        {
            if((away2Start.before(date) && away2End.after(date))
                || away2Start.equals(date) || away2End.equals(date))
            {
                result = true;
            }
        }
        return result;
    }

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

    public boolean isNoEvenings()
    {
        return noEvenings;
    }

    public void setNoEvenings(boolean noEvenings)
    {
        this.noEvenings = noEvenings;
    }

    public boolean isNoOvernights()
    {
        return noOvernights;
    }

    public void setNoOvernights(boolean noOvernights)
    {
        this.noOvernights = noOvernights;
    }

    public boolean isFlexible()
    {
        return flexible;
    }

    public void setFlexible(boolean flexible)
  {
    this.flexible = flexible;
  }
}
