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
package org.strandz.data.wombatrescue.business;

import org.strandz.data.wombatrescue.calculated.ParticularShift;
import org.strandz.data.wombatrescue.objects.BuddyManagerI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.data.wombatrescue.objects.WhichShiftI;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.Seniority;
import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.data.wombatrescue.objects.Sex;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.data.wombatrescue.objects.WorkerHelper;
import org.strandz.data.wombatrescue.util.RosterUtils;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.data.objects.DayInWeekI;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.text.CSVLineFormatter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Reports
{
    private static final int YAHOO_MAX_INVITE = 10;
    static final String YAHOO_HELP_MSG = "See help for instructions on how to download the list of TH Yahoo! group emails.";

    public Reports()
    {
    }

    public static class VolBuddies
    {
        public WorkerI volunteer;
        public List buddies;

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            result.append(volunteer.getToValidate() + FileUtils.DOCUMENT_SEPARATOR);
            for(Iterator iter = buddies.iterator(); iter.hasNext();)
            {
                WorkerI vol = (WorkerI) iter.next();
                result.append("\t" + vol.getToValidate() + FileUtils.DOCUMENT_SEPARATOR);
            }
            return result.toString();
        }
    }


    public static class PhoneInfo implements Comparable
    {
        public String name;
        public String home;
        public String mobile;
        public String work;

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            result.append(name + FileUtils.DOCUMENT_SEPARATOR);
            if(home != null)
            {
                result.append("H: " + home + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(mobile != null)
            {
                result.append("M: " + mobile + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(work != null)
            {
                result.append("W: " + work + FileUtils.DOCUMENT_SEPARATOR);
            }
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result = 0;
            PhoneInfo other = (PhoneInfo) o;
            result = this.name.compareTo(other.name);
            return result;
        }
    }


    public static class EmailInfo implements Comparable
    {
        public String name;
        public String email;

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(email);
            if(name != null)
            {
                result.append( " " + name);
            }
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result = 0;
            EmailInfo other = (EmailInfo) o;
            if(name != null)
            {
                result = Utils.compareTo( name, other.name);
            }
            if(result == 0)
            {
                result = Utils.compareTo( email, other.email);
            }
            return result;
        }

        public static String simpleFormat(List contacts)
        {
            StringBuffer result = new StringBuffer();
            int i = 1;
            for(Iterator iter = contacts.iterator(); iter.hasNext(); i++)
            {
                EmailInfo emailInfo = (EmailInfo) iter.next();
                result.append(emailInfo.email);
                result.append(" ");
                result.append(emailInfo.name);
                result.append(FileUtils.DOCUMENT_SEPARATOR);

                int remainder = Utils.mod(i, YAHOO_MAX_INVITE);
                if(remainder == 0)
                {
                    result.append("-----------");
                    result.append(FileUtils.DOCUMENT_SEPARATOR);
                }
            }
            return result.toString();
        }
    }


    public static class ContactInfo implements Comparable
    {
        public String name; //If null then 'email only'
        public String email;
        public String home;
        public String mobile;
        public String work;
        public String formattedWithHolidays;

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            if(name != null)
            {
                result.append(FileUtils.DOCUMENT_SEPARATOR);
                result.append(name + FileUtils.DOCUMENT_SEPARATOR);
            }
            result.append(email + FileUtils.DOCUMENT_SEPARATOR);
            if(name != null)
            {
                if(home != null)
                {
                    result.append("H: " + home + FileUtils.DOCUMENT_SEPARATOR);
                }
                if(mobile != null)
                {
                    result.append("M: " + mobile + FileUtils.DOCUMENT_SEPARATOR);
                }
                if(work != null)
                {
                    result.append("W: " + work + FileUtils.DOCUMENT_SEPARATOR);
                }
            }
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result = 0;
            ContactInfo other = (ContactInfo) o;
            if(name != null)
            {
                result = Utils.compareTo( name, other.name);
            }
            if(result == 0)
            {
                result = Utils.compareTo( email, other.email);
            }
            return result;
        }
    }


    public static class UnrosteredContactInfo extends ContactInfo
    {
        String holidayText;

        public String toString()
        {
            String result = super.toString();
            if(holidayText != null)
            {
                result += "Unavailable dates: " + holidayText;
            }
            return result;
        }
    }


    public static class BuddyInfo implements Comparable
    {
        public ContactInfo contactInfo;
        public DayInWeekI dayInWeek;
        public WhichShiftI whichShift;

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(contactInfo.toString());
            result.append(dayInWeek + FileUtils.DOCUMENT_SEPARATOR);
            result.append(whichShift + FileUtils.DOCUMENT_SEPARATOR);
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result = 0;
            BuddyInfo other = (BuddyInfo) o;
            result = dayInWeek.compareTo(other.dayInWeek);
            if(result == 0)
            {
                result = whichShift.compareTo(other.whichShift);
            }
            return result;
        }
    }


    public static class BuddyVols
    {
        public BuddyManagerI buddyManager;
        private List<WorkerI> volunteers;
        /*
        * Stuff a bit like this will be used in email
        *
        public String toString()
        {
        StringBuffer result = new StringBuffer();
        result.append( pUtils.DOCUMENT_SEPARATOR);
        result.append( buddy.getToValidate() + pUtils.DOCUMENT_SEPARATOR);
        for(Iterator iter = volunteers.iterator(); iter.hasNext();)
        {
        Worker vol = (Worker)iter.next();
        result.append( "\t" + vol.getToValidate() + pUtils.DOCUMENT_SEPARATOR);
        }
        return result.toString();
        }
        */

        public List<WorkerI> getVolunteers()
        {
            return volunteers;
        }

        public void setVolunteers(List<WorkerI> volunteers)
        {
            Utils.chkNoNulls( volunteers, "Null volunteer in list");
            this.volunteers = volunteers;
        }
    }


    public static class AllInfo implements Comparable
    {
        public String name;
        public String street;
        public String suburb;
        public String postcode;
        public String home;
        public String mobile;
        public String work;
        public String email;
        public String group;

        public String[] asArray()
        {
            String result[] = new String[9];
            result[0] = name;
            result[1] = street;
            result[2] = suburb;
            result[3] = postcode;
            result[4] = home;
            result[5] = mobile;
            result[6] = work;
            result[7] = email;
            result[8] = group;
            return result;
        }

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            result.append(name + FileUtils.DOCUMENT_SEPARATOR);
            if(street != null)
            {
                result.append(street + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(suburb != null)
            {
                result.append(suburb + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(postcode != null)
            {
                result.append(postcode + FileUtils.DOCUMENT_SEPARATOR);
            }
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            if(home != null)
            {
                result.append("H: " + home + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(mobile != null)
            {
                result.append("M: " + mobile + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(work != null)
            {
                result.append("W: " + work + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(email != null)
            {
                result.append(email + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(group != null)
            {
                result.append(group + FileUtils.DOCUMENT_SEPARATOR);
            }
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result = 0;
            AllInfo other = (AllInfo) o;
            result = this.name.compareTo(other.name);
            return result;
        }

        public static String csvFormat( List<Reports.AllInfo> allInfo)
        {
            StringBuffer result = new StringBuffer();
            CSVLineFormatter csvLineFormatter = new CSVLineFormatter();
            List<Integer[]> columns = new ArrayList<Integer[]>();
            columns.add( new Integer[]{0});
            columns.add( new Integer[]{1,2,3});
            columns.add( new Integer[]{4});
            columns.add( new Integer[]{5});
            columns.add( new Integer[]{6});
            columns.add( new Integer[]{7});
            csvLineFormatter.setColumns( columns);
            for (int i = 0; i < allInfo.size(); i++)
            {
                Reports.AllInfo info = allInfo.get(i);
                csvLineFormatter.setValues( info.asArray());
                result.append( csvLineFormatter.getLine());
                result.append( FileUtils.DOCUMENT_SEPARATOR);
            }
            return result.toString();
        }
    }


    public static class Address implements Comparable
    {
        public String name;
        public String street;
        public String suburb;
        public String postcode;
        public String txt;
        // only used for ordering, not displaying
        public int noOfBooks = -1;

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            result.append(name + FileUtils.DOCUMENT_SEPARATOR);
            result.append(street + FileUtils.DOCUMENT_SEPARATOR);
            result.append(suburb + FileUtils.DOCUMENT_SEPARATOR);
            if(postcode != null)
            {
                result.append(postcode + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(txt != null)
            {
                result.append(txt + FileUtils.DOCUMENT_SEPARATOR);
            }
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result = 0;
            Address other = (Address) o;
            result = other.noOfBooks - this.noOfBooks;
            return result;
        }
    }

    public List reportAddresses()
    {
        List result = new ArrayList();
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext();)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(vol.getStreet() != null)
            {
                Address address = new Address();
                address.name = vol.getToValidate();
                address.street = vol.getStreet();
                address.suburb = vol.getSuburb();
                address.postcode = vol.getPostcode();

                String comments = vol.getComments();
                if(comments != null)
                {
                    address.txt = Utils.findFirstLineContaining("book", comments);

                    int num = Utils.findFirstPositiveNumberInTxt(address.txt);
                    if(num != -99)
                    {
                        address.noOfBooks = num;
                    }
                }
                result.add(address);
            }
        }
        Collections.sort(result);
        return result;
    }

    public List<AllInfo> reportAll()
    {
        List<AllInfo> result = new ArrayList<AllInfo>();
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext();)
        {
            AllInfo allInfo = new AllInfo();
            WorkerI vol = (WorkerI) iter.next();
            allInfo.name = vol.getToValidate();
            allInfo.street = vol.getStreet();
            allInfo.suburb = vol.getSuburb();
            allInfo.postcode = vol.getPostcode();
            allInfo.home = vol.getHomePhone();
            allInfo.mobile = vol.getMobilePhone();
            allInfo.work = vol.getWorkPhone();
            allInfo.email = vol.getEmail1();
            result.add(allInfo);
        }
        Collections.sort(result);
        return result;
    }

    public List<AllInfo> reportAllRosterable()
    {
        List<AllInfo> result = new ArrayList<AllInfo>();
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext();)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(!vol.isUnknown())
            {
                AllInfo allInfo = new AllInfo();
                allInfo.name = vol.getToValidate();
                allInfo.street = vol.getStreet();
                allInfo.suburb = vol.getSuburb();
                allInfo.postcode = vol.getPostcode();
                allInfo.home = vol.getHomePhone();
                allInfo.mobile = vol.getMobilePhone();
                allInfo.work = vol.getWorkPhone();
                allInfo.email = vol.getEmail1();
                result.add(allInfo);
            }
        }
        Collections.sort(result);
        return result;
    }

    public List reportGroupVolunteers()
    {
        List result = new ArrayList();
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getGroupWorkers().iterator(); iter.hasNext();)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(!vol.isUnknown())
            {
                AllInfo allInfo = new AllInfo();
                allInfo.name = vol.getToValidate();
                allInfo.street = vol.getStreet();
                allInfo.suburb = vol.getSuburb();
                allInfo.postcode = vol.getPostcode();
                allInfo.home = vol.getHomePhone();
                allInfo.mobile = vol.getMobilePhone();
                allInfo.work = vol.getWorkPhone();
                allInfo.email = vol.getEmail1();
                allInfo.group = vol.getBelongsToGroup().getToLong();
                result.add(allInfo);
            }
        }
        Collections.sort(result);
        return result;
    }

    /**
     * For every BM go thru all the shifts of the current month and
     * collect a list of BuddyVols (represents one chief and a few
     * indians) 
     */
    public List<BuddyVols> reportBuddyVols(Date begin, Date end)
    {
        List<BuddyVols> result = new ArrayList<BuddyVols>();
        if(RosterSessionUtils.getGlobalCurrentMonth() == null)
        {
            new MessageDlg("Do not have a current month");
        }
        else
        {
            RostererSessionI bo = RosterSessionUtils.getGlobalRostererSession();
            ParticularRosterI particularRoster = RosterSessionUtils.getGlobalCurrentParticularRoster();
            List<BuddyManagerI> buddyManagers = bo.getBuddyManagers();
            if(WombatNote.BAD_ALLOCATION_BM_EMAILS.isVisible())
            {
                Print.prList( buddyManagers, "buddyManagers - want to see all");
            }
            for(Iterator iter1 = buddyManagers.iterator(); iter1.hasNext();)
            {
                List indians = new ArrayList();
                BuddyManagerI buddyManager = (BuddyManagerI) iter1.next();
                WorkerI chief = buddyManager.getWorker();
                if(WombatNote.BAD_ALLOCATION_BM_EMAILS.isVisible() && chief.getToValidate().equals( "Daniel Koo"))
                {
                    Err.debug();
                }
                List<ParticularShift> particularShifts = particularRoster.getParticularShifts( 
                        begin, end, buddyManager.getWhichShift(), buddyManager.getDayInWeek());
                for(Iterator iter2 = particularShifts.iterator(); iter2.hasNext();)
                {
                    ParticularShift shift = (ParticularShift) iter2.next();
                    WorkerI chiefOfShift = shift.getBuddyManager(
                        buddyManagers
                    );
                    //Err.pr( WombatNote.badAllocationBMEmails, "chiefOfShift is " + chiefOfShift + " for " + shift);
                    WorkerI indianOfShift = shift.getWorker();
//                    SdzEMAssert.isEntityManaged( chief, "chief not entity managed");
//                    SdzEMAssert.isEntityManaged( chiefOfShift, "chiefOfShift not entity managed");
//                    SdzEMAssert.isEntityManaged( indianOfShift, "indianOfShift not entity managed");
                    if(chief.equals(chiefOfShift))
                    {
                        if(chief != chiefOfShift)
                        {
                            Err.error("If get this, then had to use equals() method");
                        }
                        if(!indianOfShift.isDummy())
                        {
                            indians.add(indianOfShift);
                        }
                    }
                }

                BuddyVols buddyVols = new BuddyVols();
                buddyVols.buddyManager = buddyManager;
                buddyVols.setVolunteers(indians);
                result.add(buddyVols);
            }
        }
        return result;
    }

    public List reportManyBuddies()
    {
        List result = new ArrayList();
        if(RosterSessionUtils.getGlobalCurrentMonth() == null)
        {
            new MessageDlg("Do not have a current month");
        }
        else
        {
            ParticularRosterI particularRoster = RosterSessionUtils.getGlobalCurrentParticularRoster();
            for(Object o : RosterSessionUtils.getGlobalRostererSession().getWorkers())
            {
                WorkerI vol = (WorkerI) o;
                List buddies = new ArrayList();
                List shifts = particularRoster.getParticularShifts(
                        vol);
                for(Object shift1 : shifts)
                {
                    ParticularShift shift = (ParticularShift) shift1;
                    WorkerI buddy = shift.getBuddyManager(
                            RosterSessionUtils.getGlobalRostererSession().getBuddyManagers()
                    );
                    if(buddy != null && !buddies.contains(buddy))
                    {
                        buddies.add(buddy);
                    }
                }
                if(buddies.size() > 1)
                {
                    VolBuddies volBuddies = new VolBuddies();
                    volBuddies.volunteer = vol;
                    volBuddies.buddies = buddies;
                    result.add(volBuddies);
                }
            }
        }
        return result;
    }
    
    private void noYahooFileMessage()
    {
        String msg[] = new String[2];
        msg[0] = "File <" + RosteringConstants.YAHOO_FILENAME + "> needs to exist and contain a list of yahoo emails";
        msg[1] = YAHOO_HELP_MSG;
        new MessageDlg( msg, JOptionPane.ERROR_MESSAGE);
    }

    private void haveYahooFileMessage()
    {
        String msg[] = new String[2];
        msg[0] = "File <" + RosteringConstants.YAHOO_FILENAME + "> contains the list of yahoo emails that will";
        msg[1] = "be used for this report";
        msg[2] = YAHOO_HELP_MSG;
        new MessageDlg( msg, JOptionPane.ERROR_MESSAGE);
    }
    
    /*
    public List reportNotInDB()
    {
        List result = new ArrayList();
        if(RosterSessionUtils.noYahooFileExists())
        {
            noYahooFileMessage();
        }
        else
        {
            Err.error( "Not yet implemented: List reportNotInDB()");
        }
        return result;
    }
    */

    public List reportNotOnYahoo()
    {
        List result = new ArrayList();
        if(RosterSessionUtils.noYahooFileExists())
        {
            noYahooFileMessage();
        }
        else
        {
            haveYahooFileMessage();
            int i = 0;
            for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext(); i++)
            {
                WorkerI vol = (WorkerI) iter.next();
                Assert.notNull( vol.getBelongsToGroup(), vol.getToLong() + " has no group to belong to (not even dummy) - " +
                        "a database update is required");
                if(!vol.isUnknown() && vol.getBelongsToGroup().isDummy())
                {
                    if(!vol.hasEmailOnList(RosterSessionUtils.fromYahooEmails))
                    {
                        ContactInfo contactInfo = new ContactInfo();
                        contactInfo.name = vol.getToValidate();
                        contactInfo.email = vol.getEmail1();
                        contactInfo.home = vol.getHomePhone();
                        contactInfo.mobile = vol.getMobilePhone();
                        contactInfo.work = vol.getWorkPhone();
                        result.add(contactInfo);
                    }
                }
            }
            Collections.sort(result);
        }
        return result;
    }

    public List reportNewVolunteers()
    {
        List result = new ArrayList();
        if(RosterSessionUtils.getGlobalCurrentMonth() == null)
        {
            new MessageDlg("Do not have a current month");
        }
        else
        {
            int i = 0;
            ParticularRosterI particularRoster = RosterSessionUtils.getGlobalCurrentParticularRoster();
            for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext(); i++)
            {
                WorkerI vol = (WorkerI) iter.next();
                //Err.pr( "vol: " + vol);
                //Err.pr( "Seniority: " + vol.getSeniority());
                Assert.notNull( vol.isUnknown());
                Assert.notNull( vol.getSeniority(), "seniority s/not be null, but is for <" + vol.getToLong() + ">");
                if(!vol.isUnknown() && vol.getSeniority().equals(Seniority.NEWBIE)
                    && particularRoster.isRosteredInCurrentMonth(vol))
                {
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.name = vol.getToValidate();
                    contactInfo.email = vol.getEmail1();
                    contactInfo.home = vol.getHomePhone();
                    contactInfo.mobile = vol.getMobilePhone();
                    contactInfo.work = vol.getWorkPhone();
                    result.add(contactInfo);
                }
            }
            Collections.sort(result);
        }
        return result;
    }

    private static ContactInfo createContactInfo(
        WorkerI vol, Date first, Date last, boolean emailOnly)
    {
        ContactInfo result = new ContactInfo();
        if(!emailOnly)
        {
            result.name = vol.getToValidate();
            result.home = vol.getHomePhone();
            result.mobile = vol.getMobilePhone();
            result.work = vol.getWorkPhone();
            result.formattedWithHolidays = vol.formatWithHolidays( first, last);
        }
        result.email = vol.getEmail1();
        return result;
    }

    public List<ContactInfo> reportUnrosteredWorkers( boolean emailOnly)
    {
        List<ContactInfo> result = new ArrayList<ContactInfo>();
        if(RosterSessionUtils.getGlobalCurrentMonth() == null)
        {
            new MessageDlg("Do not have a current month");
        }
        else
        {
            int i = 0;
            ParticularRosterI particularRoster = RosterSessionUtils.getGlobalCurrentParticularRoster();
            List workers = particularRoster.getRosterTransferObj().getRosterMonth().getUnrosteredAvailableWorkers();
            Date first = new Date();
            Date last = new Date();
            particularRoster.getCurrentMonth().obtainFirstLastDay(first, last, false);
            for(Iterator iter = workers.iterator(); iter.hasNext(); i++)
            {
                WorkerI vol = (WorkerI) iter.next();
                if( vol.isAvailable( first, last))
                {
                    ContactInfo contactInfo = createContactInfo( vol, first, last, emailOnly);
                    result.add( contactInfo);
                }
            }
            Collections.sort(result);
        }
        return result;
    }

    public List reportUnknownFlexibility( boolean emailOnly)
    {
        List result = new ArrayList();
        int i = 0;
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext(); i++)
        {
            WorkerI vol = (WorkerI) iter.next();
            // Err.pr( "Seniority: " + vol.getSeniority());
            if(!vol.isUnknown())
            {
                //if(!vol.isNoEvenings() && !vol.isNoOvernights() && !vol.isFlexible())
                Assert.notNull( vol.getFlexibility(), vol.getToLong() + " has no flexibility - a database update is required");
                Assert.notNull( vol.getBelongsToGroup());
                if(vol.getFlexibility().equals(Flexibility.NULL) && vol.getBelongsToGroup().isDummy())
                {
                    EmailInfo emailInfo = new EmailInfo();
                    if(!emailOnly)
                    {
                        emailInfo.name = vol.getToValidate();
                    }
                    emailInfo.email = vol.getEmail1();
                    result.add(emailInfo);
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    public List reportOvernightCapable( boolean emailOnly)
    {
        List result = new ArrayList();
        int i = 0;
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext(); i++)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(!vol.isUnknown())
            {
                //if(!vol.isNoEvenings() && !vol.isNoOvernights() && !vol.isFlexible())
                Assert.notNull( vol.getFlexibility(), vol.getToLong() + " has no flexibility - a database update is required");
                Assert.notNull( vol.getBelongsToGroup());
                if((vol.getFlexibility().equals(Flexibility.NULL) ||
                    vol.getFlexibility().equals(Flexibility.FLEXIBLE) ||
                    vol.getFlexibility().equals(Flexibility.NO_EVENINGS))
                    && vol.getBelongsToGroup().isDummy())
                {
                    EmailInfo emailInfo = new EmailInfo();
                    if(!emailOnly)
                    {
                        emailInfo.name = vol.getToValidate();
                    }
                    emailInfo.email = vol.getEmail1();
                    result.add(emailInfo);
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    public List reportMaleEmails()
    {
        List result = new ArrayList();
        int i = 0;
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext(); i++)
        {
            WorkerI vol = (WorkerI) iter.next();
            Assert.notNull( vol.getBelongsToGroup(), vol.getToLong() + " has no group to belong to (not even dummy) - " +
                    "a database update is required");
            if(!vol.isUnknown() && vol.getBelongsToGroup().isDummy() && vol.getSex().equals( Sex.MALE))
            {
                String email = vol.getEmail1();
                if(email != null)
                {
                    result.add(vol.getEmail1());
                }
            }
        }
        //Just tidy to have then alphabetical
        Collections.sort(result);
        return result;
    }

    public List reportNotOnYahooEmailOnly()
    {
        List result = new ArrayList();
        if(RosterSessionUtils.noYahooFileExists())
        {
            noYahooFileMessage();
        }
        else
        {
            int i = 0;
            for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext(); i++)
            {
                WorkerI vol = (WorkerI) iter.next();
                Assert.notNull( vol.getBelongsToGroup(), vol.getToLong() + " has no group to belong to (not even dummy) - " +
                        "a database update is required");
                if(!vol.isUnknown() && vol.getBelongsToGroup().isDummy())
                {
                    if(!vol.hasEmailOnList(RosterSessionUtils.fromYahooEmails))
                    {
                        String email = vol.getEmail1();
                        if(email != null)
                        {
                            EmailInfo emailInfo = new EmailInfo();
                            emailInfo.email = email;
                            emailInfo.name = vol.getToValidate();
                            result.add(emailInfo);
                        }
                    }
                }
            }
        }
        return result;
    }

    public List reportPhoneNumbers(boolean rosterableOnly)
    {
        List result = new ArrayList();
        int i = 0;
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext(); i++)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(rosterableOnly && !vol.isUnknown())
            {
                if(!vol.getBestPhone().equals(Worker.NO_PHONE_SIGNIFIER))
                {
                    PhoneInfo phoneInfo = new PhoneInfo();
                    phoneInfo.name = vol.getToValidate();
                    phoneInfo.home = vol.getHomePhone();
                    phoneInfo.mobile = vol.getMobilePhone();
                    phoneInfo.work = vol.getWorkPhone();
                    result.add(phoneInfo);
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    public List reportBuddyManagers()
    {
        List result = new ArrayList();
        List managers = RosterSessionUtils.getGlobalRostererSession().getBuddyManagers();
        Err.pr("Have " + managers.size() + " managers");
        for(Iterator iter = managers.iterator(); iter.hasNext();)
        {
            BuddyManagerI bm = (BuddyManagerI) iter.next();
            WorkerI vol = bm.getWorker();
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.name = vol.getToValidate();
            contactInfo.email = vol.getEmail1();
            contactInfo.home = vol.getHomePhone();
            contactInfo.mobile = vol.getMobilePhone();
            contactInfo.work = vol.getWorkPhone();

            BuddyInfo buddyInfo = new BuddyInfo();
            buddyInfo.contactInfo = contactInfo;
            buddyInfo.dayInWeek = bm.getDayInWeek();
            buddyInfo.whichShift = bm.getWhichShift();
            result.add(buddyInfo);
        }
        Collections.sort(result);
        return result;
    }

    public List reportBuddyManagersEmails()
    {
        List result = new ArrayList();
        int i = 0;
        List buddyManagers = RosterSessionUtils.getGlobalRostererSession().getBuddyManagers();
        for(Iterator iter = buddyManagers.iterator(); iter.hasNext(); i++)
        {
            BuddyManagerI bm = (BuddyManagerI) iter.next();
            WorkerI vol = bm.getWorker();
            EmailInfo emailInfo = new EmailInfo();
            emailInfo.name = vol.getToValidate();
            emailInfo.email = vol.getEmail1();
            result.add(emailInfo);
        }
        //Print.pr( i + " workers are buddy managers");
        Collections.sort(result);
        return result;
    }

    public List reportWeekendOvernighters()
    {
        List result = new ArrayList();
        int i = 0;
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getWorkers().iterator(); iter.hasNext(); i++)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(!vol.isUnknown())
            {
                List slots = getAllSlotsOfVolunteer(vol);
                if(containsWEOvernight(slots))
                {
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.name = vol.getToValidate();
                    contactInfo.email = vol.getEmail1();
                    contactInfo.home = vol.getHomePhone();
                    contactInfo.mobile = vol.getMobilePhone();
                    contactInfo.work = vol.getWorkPhone();
                    result.add(contactInfo);
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    List getAllSlotsOfVolunteer(WorkerI vol)
    {
        List result = new ArrayList();
        for(Iterator iter = RosterSessionUtils.getGlobalRostererSession().getRosterSlots().iterator(); iter.hasNext();)
        {
            RosterSlotI slot = (RosterSlotI) iter.next();
            if(slot.getWorker().equals(vol))
            {
                result.add(slot);
            }
        }
        return result;
    }

    private boolean containsWEOvernight(List slots)
    {
        boolean result = false;
        for(Iterator iter = slots.iterator(); iter.hasNext();)
        {
            RosterSlotI slot = (RosterSlotI) iter.next();
            if((slot.getDayInWeek().equals(DayInWeek.FRIDAY)
                || slot.getDayInWeek().equals(DayInWeek.SATURDAY))
                && slot.getWhichShift().equals(WhichShift.OVERNIGHT)
                )
            {
                result = true;
                break;
            }
        }
        return result;
    }
    
    private static List getPartnersVolunters( BuddyManagerI partner, List<BuddyVols> buddyVolsList)
    {
        List result = null;
        for(Iterator iter = buddyVolsList.iterator(); iter.hasNext();)
        {
            BuddyVols bVols = (BuddyVols) iter.next();
            if(bVols.buddyManager.equals( partner))
            {
                result = bVols.getVolunteers();
                break;
            }
        }        
        return result;
    }

    public MessagesContainer writeAllBuddyMsgs(Date begin, Date end)
    {
        MessagesContainer result = new MessagesContainer();
        List<BuddyVols> buddyVolsList = reportBuddyVols(begin, end);
        if(!buddyVolsList.isEmpty())
        {
            ParticularRosterI particularRoster = RosterSessionUtils.getGlobalCurrentParticularRoster();
            List buddyManagers = RosterSessionUtils.getGlobalRostererSession().getBuddyManagers();
            for(Iterator iter = buddyVolsList.iterator(); iter.hasNext();)
            {
                BuddyVols bVols = (BuddyVols) iter.next();
                Err.pr( WombatNote.BAD_ALLOCATION_BM_EMAILS, 
                        "Num vols: " + bVols.getVolunteers().size() + 
                                ", for BM: " + bVols.buddyManager.getWorker() + " on " + bVols.buddyManager.getDayInWeek());
                List<ParticularShift> shifts = particularRoster.getParticularShifts(
                    bVols.getVolunteers(), begin, end, false);
                Err.pr( WombatNote.BAD_ALLOCATION_BM_EMAILS, 
                        "Gives num shifts: " + shifts.size());
                if(!shifts.isEmpty())
                {
                    BuddyManagerI partner = RosterUtils.getOppositeBM(bVols.buddyManager, buddyManagers);
                    List partnerVolunters = getPartnersVolunters( partner, buddyVolsList);
                    //if(!bVols.buddyManager.getWorker().isDummy())
                    {
                        String txt = CommunicationTextUtils.notifyingBuddiesEmail(bVols.buddyManager,
                            bVols.getVolunteers(), shifts, particularRoster, partner, partnerVolunters);
                        if(bVols.buddyManager.getWorker().getEmail1() != null)
                        {
                            result.emails.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), bVols.buddyManager.getWorker().getEmail1(), txt));
                            // Err.pr( txt);
                            // Err.pr( "==============================================");
                        }
                        else if(!bVols.buddyManager.getWorker().getBestPhone().equals(
                            Worker.NO_PHONE_SIGNIFIER))
                        {
                            result.phoneCalls.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), bVols.buddyManager.getWorker().getBestPhone(), txt));
                        }
                    }
                }
            }
        }
        return result;
    }
}
