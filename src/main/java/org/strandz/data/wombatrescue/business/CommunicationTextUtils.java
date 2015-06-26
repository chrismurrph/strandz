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

import org.strandz.data.wombatrescue.calculated.Night;
import org.strandz.data.wombatrescue.calculated.ParticularShift;
import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.data.wombatrescue.objects.Seniority;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.data.wombatrescue.objects.BuddyManagerI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Print;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class CommunicationTextUtils
{
    static String addressEmail(WorkerI vol)
    {
        String result;
        StringBuffer email = new StringBuffer();
        String to = vol.getToValidate();
        email.append("Hi " + to + "," + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(
            "\"" + getProperty( "organisationName") + "\" does not currently have your home address."
                + Utils.NEWLINE);
        email.append("Would you be able to email it back to me?" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(getProperty( "messageSignature") + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("M: " + getProperty( "rostererMobilePhone") + Utils.NEWLINE);
        email.append("H: " + getProperty( "rostererHomePhone") + Utils.NEWLINE);
        result = email.toString();
        return result;
    }

    private static String getCategory(WorkerI vol)
    {
        String result = "C";
        if(vol.getFlexibility().equals(Flexibility.NO_EVENINGS))
        {
            result = "A";
        }
        else if(vol.getFlexibility().equals(Flexibility.NO_OVERNIGHTS))
        {
            result = "B";
        }
        return result;
    }
    
    private static String getProperty( String key)
    {
        return RosterSessionUtils.getProperty( key);
    }

    static String noOvernightsEmail(WorkerI vol)
    {
        String result;
        StringBuffer email = new StringBuffer();
        String to = vol.getToValidate();
        email.append("Hi " + to + "," + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(
            "For rostering purposes there are three types of volunteer:"
                + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("A/ Will not do evening shifts" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("B/ Will not do overnight shifts" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("C/ Not fussed about evening/overnights" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(
            "At the moment I have you down as being in category "
                + getCategory(vol) + ".");
        email.append(Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(
            "If this is not the right one for you then please let me know.");
        email.append(Utils.NEWLINE);
        email.append(
            "Note that if for example you are available for overnights, but");
        email.append(Utils.NEWLINE);
        email.append(
            "for emergencies only, then your category would still be B.");
        email.append(Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(getProperty( "messageSignature") + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("M: " + getProperty( "rostererMobilePhone") + Utils.NEWLINE);
        email.append("H: " + getProperty( "rostererHomePhone") + Utils.NEWLINE);
        result = email.toString();
        return result;
    }

    static String phoneEmail(WorkerI vol)
    {
        String result = null;
        StringBuffer email = new StringBuffer();
        String to = vol.getToValidate();
        email.append("Hi " + to + "," + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(
            getProperty( "organisationName") + " does not currently have any phone numbers for you."
                + Utils.NEWLINE);
        email.append(
            "Would you be able to email your work/home/mob numbers back to me?"
                + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(getProperty( "messageSignature") + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("M: " + getProperty( "rostererMobilePhone") + Utils.NEWLINE);
        email.append("H: " + getProperty( "rostererHomePhone") + Utils.NEWLINE);
        result = email.toString();
        return result;
    }

    static String statusEmail(WorkerI vol, MonthInYearI monthInYear, String rosterText)
    {
        String result = null;
        StringBuffer email = new StringBuffer();
        String to = vol.getToValidate();
        email.append("Hi " + to + "," + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(
            getProperty( "organisationName") + " has you as unrostered for " + monthInYear + " - are there any spaces you can fill?"
                + Utils.NEWLINE);
        email.append(
            "As an option we can take you off as a candidate for rostering - just reply to this email"
                + Utils.NEWLINE);
        email.append(
            "with your request - we can always put you on again in the future ..."
                + Utils.NEWLINE + Utils.NEWLINE);
        email.append(
            //I'm hoping that if you 'arrow quote' some text it won't then be awfully formatted by MUA
            Utils.arrowQuote( rosterText)
                + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(getProperty( "messageSignature") + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("M: " + getProperty( "rostererMobilePhone") + Utils.NEWLINE);
        result = email.toString();
        return result;
    }

    static String raffleEmail(WorkerI vol)
    {
        String result = null;
        StringBuffer email = new StringBuffer();
        String to = vol.getToValidate();
        email.append("Dear " + to + "," + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(
            "The Wombat Rescuers Association is having a raffle to be drawn on November 28th."
                + Utils.NEWLINE);
        email.append("The PRIZES are as follows:" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("FIRST\t " + Utils.NEWLINE);
        email.append(
            "Business class return airfare for two from Sydney to Rome"
                + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("SECOND\t " + Utils.NEWLINE);
        email.append("Sydney Harbour Bridge Climb for two" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("THIRD\t " + Utils.NEWLINE);
        email.append(
            "One weeks free accomodation at Lennox Head, NSW" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("FORTH\t " + Utils.NEWLINE);
        email.append("Case of Sevenhill wine" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("FIFTH\t " + Utils.NEWLINE);
        email.append("$100 in cash" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(
            "If you would like to be sent some books of tickets, can you"
                + Utils.NEWLINE);
        email.append(
            "please let me know how many you would like to receive? One book"
                + Utils.NEWLINE);
        email.append(
            "contains ten tickets, and each ticket is for $2.00. If you"
                + Utils.NEWLINE);
        email.append(
            "don't want to be involved please let me know too, and I will"
                + Utils.NEWLINE);
        email.append("make sure you are not sent any!" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(getProperty( "messageSignature") + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("M: " + getProperty( "rostererMobilePhone") + Utils.NEWLINE);
        email.append("H: " + getProperty( "rostererHomePhone") + Utils.NEWLINE);
        result = email.toString();
        return result;
    }

    static String notifyingBuddiesEmail(
        BuddyManagerI buddyManager, List<WorkerI> vols, List<ParticularShift> shifts, ParticularRosterI bo,
        BuddyManagerI partner, List partnerVolunteers)
    {
        String result;
        if(bo.getCurrentMonth() == null)
        {
            Err.error(WombatNote.EMAILS, "Should have already shown dialog!");
        }

        StringBuffer email = new StringBuffer();
        String to = buddyManager.getWorker().getToValidate();
        email.append("Hi " + to + "," + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(
            "For " + bo.getCurrentMonth().month + " your " + buddyManager.getDayInWeek() + " " + 
                    buddyManager.getWhichShift() + " buddies are:"
                + Utils.NEWLINE);

        TreeSet uniqVols = new TreeSet(vols); // removes repeats
        if(uniqVols.isEmpty())
        {
            Print.prList( shifts, "How come shifts but no vols?");
            Err.pr( WombatNote.BAD_ALLOCATION_BM_EMAILS, "BM: " + buddyManager + " was passed " + vols.size() + " to manage");
            Err.error( "A BM has no volunteers to manage: " + buddyManager);
        }
        for(Iterator iter = uniqVols.iterator(); iter.hasNext();)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(!vol.isDummy())
            {
                email.append(
                    "\t" + vol.getToValidate() + vol.formatAllPhonesAndEmail());
                email.append(Utils.NEWLINE);
            }
        }
        email.append(Utils.NEWLINE);
        email.append(
            "The shifts that your buddies will be doing are:"
                + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(formatShiftsForBuddy(shifts));
        email.append(Utils.NEWLINE);

        email.append(
            "Your partner for " + buddyManager.getDayInWeek() + "s is " + partner.getWorker().getToValidate()
                + partner.getWorker().formatAllPhonesAndEmail());
        email.append(Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append( "Your partner's buddies details are:" + Utils.NEWLINE);
        uniqVols = new TreeSet( partnerVolunteers); // removes repeats
        for(Iterator iter = uniqVols.iterator(); iter.hasNext();)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(!vol.isDummy())
            {
                email.append(
                    "\t" + vol.getToValidate() + vol.formatAllPhonesAndEmail());
                email.append(Utils.NEWLINE);
            }
        }        
        email.append(Utils.NEWLINE);
        email.append(getProperty( "messageSignature") + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("M: " + getProperty( "rostererMobilePhone") + Utils.NEWLINE);
        email.append("H: " + getProperty( "rostererHomePhone") + Utils.NEWLINE);
        result = email.toString();
        return result;
    }
    
    private static String formatShiftsForBuddy(List shifts)
    {
        StringBuffer buf = new StringBuffer();
        for(Iterator iter = shifts.iterator(); iter.hasNext();)
        {
            ParticularShift shift = (ParticularShift) iter.next();
            buf.append(shift.forBuddySentence() + Utils.NEWLINE);
        }
        return buf.toString();
    }

    static String rosteringAWorkerEmail(
        WorkerI vol, List<ParticularShift> shifts, ParticularRoster bo, String periodTxt,
        List buddyManagers, List<RosterSlotI> rosterSlots)
    {
        String result;
        StringBuffer email = new StringBuffer();
        String to = vol.getToValidate();
        email.append("Hi " + to + "," + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("For " + periodTxt + " you are rostered:" + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(bo.formatShifts(shifts, buddyManagers));
        email.append(Utils.NEWLINE);
        email.append(
            "The following 'rule' information was used to determine your shift/s:"
                + Utils.NEWLINE);

        List slots = bo.getNonDisabledSlotsOfWorker(vol, rosterSlots);
        email.append(Utils.NEWLINE);
        email.append(bo.formatSlots(slots));
        email.append(Utils.NEWLINE);
        /*
        *
        String comments = vol.getComments();
        if(comments != null)
        {
        email.append( pUtils.separator);
        email.append( "Additional comments:");
        email.append( pUtils.separator);
        email.append( comments);
        email.append( pUtils.separator);
        email.append( pUtils.separator);
        }
        */
        if(vol.getSeniority().equals(Seniority.NEWBIE))
        {
            email.append("Please ensure that you have a set of keys for " + getProperty( "organisationAcroymn") + ".");
            email.append(Utils.NEWLINE);
            email.append("If you require a set of keys please phone one of your");
            email.append(Utils.NEWLINE);
            email.append(
                "fellow workers or your buddy manager. He/she should be able to get a set");
            email.append(Utils.NEWLINE);
            email.append("cut. If all else fails phone " + getProperty( "keyManagerName") +
                " on " + getProperty( "keyManagerMobile") + ".");
            email.append(Utils.NEWLINE);
            email.append(Utils.NEWLINE);
            email.append("In case you need them, these are the numbers of the other ");
            email.append(Utils.NEWLINE);
            email.append("workers doing the same night/s as you:");
            email.append(Utils.NEWLINE);
        }

        List nights = RosterSessionUtils.findNightsFromShifts(shifts);
        for(Iterator iter = nights.iterator(); iter.hasNext();)
        {
            Night night = (Night) iter.next();
            email.append(night.toSentence());
        }
        email.append(Utils.NEWLINE);
        email.append(
            "If you find you cannot make a particular shift then please phone your");
        email.append(Utils.NEWLINE);
        email.append(
            "buddy manager for that shift. He/she may be able to coordinate");
        email.append(Utils.NEWLINE);
        email.append("a swap with someone else.");
        email.append(Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        /*
        email.append( "Message from Nick: \"to avoid no-shows,  make sure");
        email.append( pUtils.separator);
        email.append( "to call your partner two days before the shift to");
        email.append( pUtils.separator);
        email.append( "check they have read the roster\"");
        */
        email.append(getProperty( "extraMessage"));
        email.append(Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append(getProperty( "messageSignature") + Utils.NEWLINE);
        email.append(Utils.NEWLINE);
        email.append("M: " + getProperty( "rostererMobilePhone") + Utils.NEWLINE);
        email.append("H: " + getProperty( "rostererHomePhone") + Utils.NEWLINE);
        email.append(Utils.NEWLINE);

        String ps = "PS./";
        if(!RosterSessionUtils.noYahooFileExists() && !vol.hasEmailOnList(RosterSessionUtils.fromYahooEmails))
        {
            email.append(ps + " Please join our email group!");
            email.append(Utils.NEWLINE);
            email.append(
                "     If you have various work and home emails, then you can");
            email.append(Utils.NEWLINE);
            email.append(
                "     subscribe/unsubscribe them with the following two commands.");
            email.append(Utils.NEWLINE);
            email.append(
                "     For example if you were at work and wanted to subscribe");
            email.append(Utils.NEWLINE);
            email.append(
                "     your work email address, then send an empty, subject-less");
            email.append(Utils.NEWLINE);
            email.append("     email to the following:");
            email.append(Utils.NEWLINE);
            email.append(Utils.NEWLINE);
            email.append("     \t" + getProperty( "subscribeEmail"));
            email.append(Utils.NEWLINE);
            email.append(Utils.NEWLINE);
            email.append(
                "     If you were at home and did not want to receive " + getProperty( "organisationName"));
            email.append(Utils.NEWLINE);
            email.append(
                "     emails, then whilst sitting at home, send an email to the following:");
            email.append(Utils.NEWLINE);
            email.append(Utils.NEWLINE);
            email.append("     \t" + getProperty( "unSubscribeEmail"));
            ps = "PPS./";
            email.append(Utils.NEWLINE);
        }
        /*
        if(vol.getSeniority().equals(Seniority.NEWBIE) || vol.getSeniority().equals(Seniority.JUNIOR))
        {
            email.append(Utils.SEPARATOR);
            email.append(ps + " Simple FIRE RULES");
            email.append(Utils.SEPARATOR);
            email.append("     \t1./ Grab the list.");
            email.append(Utils.SEPARATOR);
            email.append("     \t2./ Tell everyone to meet in the park out the back.");
            email.append(Utils.SEPARATOR);
            email.append("     \t3./ When in the park, tick everyone off the list.");
        }
        */
        result = email.toString();
        return result;
    }
}
