/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.note;

public class WombatNote extends Note
{
    public static final String NEWLINE = System.getProperty("line.separator");
    
    public static final Note APPLICATION_COMMIT = new WombatNote( "Wombat Application Commit");
    public static final Note UNENABLE_ALPHABET = new WombatNote( "Unenable Alphabet");
    public static final Note CONTACT_NAME = new WombatNote( "Contact name");
    public static final Note MANY_ORDERINGS = new WombatNote( "Many orderings");
    public static final Note EMAILS = new WombatNote( "Emails");
    public static final Note SEARCH_FAILURE = new WombatNote( "Search Failure");
    public static final Note ENT_QRY_CAUSES_VALIDATION = new WombatNote( "Enter Query causes validation failure");
    public static final Note FIELD_VALIDATION_NO_OVERNIGHTS = new WombatNote( "Field Validation NoOvernights");
    public static final Note PARTIC_DATE_SLOTS_NOT_ROSTERED = new WombatNote( "particular Date Slots not Rostered", HIDE);
    public static final Note DELETE_RECOGNITION = new WombatNote( "deleteRecognition");
    public static final Note NOT_CASCADE_DELETING = new WombatNote( "Not Cascade Deleting");
    public static final Note GENERIC = new WombatNote( "Generic Wombat Note");
    public static final Note CHK_ON_HOLIDAY_COMPARISON = new WombatNote( "Chk onHoliday() Comparison");
    public static final Note FIRST_LAST_DAYS_EMAILS = new WombatNote( "firstLastDaysEmails");
    public static final Note BAD_NAME_ROSTER_SLOT = new WombatNote( "RosterSlot needs to be changed to RosterRule");
    public static final Note VERSANT_TIMINGS = new WombatNote( "Versant Timings");
    public static final Note MIKES_BUG = new WombatNote( "Mike's bug");
    public static final Note CURRENT_MONTH = new WombatNote( "Current Month");
    public static final Note BAD_ROSTER_FROM_TOMCAT = new WombatNote( "Bad Roster from Tomcat");
    public static final Note NO_EM_WHEN_SOME_QUERY = new WombatNote( "No EM when do Some Query");
    public static final Note MISSING_EMAILS = new WombatNote( "Missing emails");
    public static final Note BAD_ALLOCATION_BM_EMAILS = new WombatNote( "Allocation of shifts to BMs all or nothing", HIDE);
    public static final Note CANT_ADD_NEW_VOL = new WombatNote( "Stack trace when add new vol");
    public static final Note TOO_MANY_HOLIDAYS = new WombatNote( "Too many holidays");
    public static final Note REFRESH_SENTENCE = new WombatNote( "Refresh Sentence");
    public static final Note PARTIC_SHIFT_HAS_BAD_ROSTER_SLOTS = new WombatNote( "Partic shift has bad roster slots");
    public static final Note NEW_WORKER_INSTANCE = new WombatNote( "New Worker Instance", HIDE);
    public static final Note NEW_ROSTERSLOT_INSTANCE = new WombatNote( "New RosterSlot Instance", HIDE);

    static
    {
        String desc = null;
        //
        TOO_MANY_HOLIDAYS.setDescription( "After midday holidays are printed, " +
            "firstLastDaysEmails related");
        //
        CANT_ADD_NEW_VOL.setDescription( "Live data has NOT become corrupted. workerCell.getLastNavigatedTo()" +
            " is not registering an add. See SdzNote.recordCurrentValue");
        CANT_ADD_NEW_VOL.setFixed( true);
        //
        BAD_ALLOCATION_BM_EMAILS.setDescription( "Caused by every ParticularShift that returned from server" +
                " being on the same (random) night");
        BAD_ALLOCATION_BM_EMAILS.setFixed( true);
        //
        MISSING_EMAILS.setDescription("BM emails are coming out, but others are not, associated with " + JDONote.ASSIGN_PM);
        //
        desc = "Supersix is doing something that this app is not";
        NO_EM_WHEN_SOME_QUERY.setDescription(desc);
        NO_EM_WHEN_SOME_QUERY.setFixed( true);
        NO_EM_WHEN_SOME_QUERY.setFix( "Mose well always do an init() at beginning of query in question");
        //
        desc = "Roster that comes back across wire repeats the same name over and over";
        BAD_ROSTER_FROM_TOMCAT.setDescription(desc);
        BAD_ROSTER_FROM_TOMCAT.setFixed(true);
        BAD_ROSTER_FROM_TOMCAT.setFix("MonthTransferObj ensures that not such a complicated object gets passed across");
        //
        desc = "Hard to repeat. Symptom is that not able to get calendar up when the 'known date' field" +
            " is on the wrong day. Perhaps focus has gone to the parent panel after a previous dialog";
        MIKES_BUG.setDescription(desc);
        //
        desc = "We want to see everything being loaded into mem on client. Trying JVM mem settings" +
            " as well as versant.pmCacheRefType=STRONG. All this on client (=remote) side. Does it" +
            " need to be on both sides?";
        VERSANT_TIMINGS.setDescription(desc);
        //
        desc = "The name RosterSlot is confusing to some people as the 'slot' does not actually " +
            "mean one slot in time. B/c changing this sort of thing while you have an O/R mapping " +
            "layer with already existing data, we will fix this at the same time as move to the Versant " +
            "OO DB.";
        BAD_NAME_ROSTER_SLOT.setDescription(desc);
        //
        FIRST_LAST_DAYS_EMAILS.setDescription("There may still be some problems with determining what a month " +
            "is, when it starts and ends. For the rostering emails, got this when doing May:" +
            "first day: Sun May 01 00:00:00 EST 2005\n" +
            "last day: Tue May 31 00:00:00 EST 2005\n" +
            "first day: Fri Apr 29 11:30:00 EST 2005\n" +
            "last day: Tue May 31 00:00:00 EST 2005\n" +
            "Will be easy to just run the method stand alone from NewMonth.main() when want to fix. Actually " +
            "still not fixed as someone rang up today to say the email didn't tell them about tonight's " +
            "shift. ");
        FIRST_LAST_DAYS_EMAILS.setFixed(false);
        FIRST_LAST_DAYS_EMAILS.setFix("clearHMS() was not being called properly");
        FIRST_LAST_DAYS_EMAILS.setOngoingConcerns("Doing a check for 00:00:00 so will catch if isn't really fixed");
        //
        GENERIC.setDescription("Catch all for any WR notes which are 'on their own'");
        //
        desc = "Got stack trace when doing a roster that said that a worker was deleted: " +
            "javax.jdo.JDOUserException: Not allowed to read/write to a instance marked for deletion" +
            "at org.strandz.data.wombatrescue.objects.RosterSlot.jdoGetworker(RosterSlot.java)" +
            "at org.strandz.data.wombatrescue.objects.RosterSlot.getWorker(RosterSlot.java:362)" +
            "at org.strandz.data.wombatrescue.objects.Worker.getRosterslots(Worker.java:358)" +
            "at org.strandz.data.wombatrescue.business.NewMonth.prepareUnavailables(NewMonth.java:669)" +
            "at org.strandz.data.wombatrescue.business.NewMonth.allocateRosterSlots(NewMonth.java:684)" +
            " Seems like the worker was deleted his rosterslots were not. Strandz is supposed " +
            "to do cascade deleting by default. Will do a data investigation of all unattached " +
            "roster slots - any delete of a RosterSlot will set this off";
        NOT_CASCADE_DELETING.setDescription(desc);
        //
        desc = "To repeat have a volunteer not available on a specific day" +
            " and see the night failing. Then remove this RosterSlot such that" +
            " the night should work. To actually get the night to work needed to" +
            " close then bring back the app";
        DELETE_RECOGNITION.setDescription(desc);
        //
        desc = "On Debian are getting a different roster outcome. For example" +
            " for Di Brogan's group who should be rostered on 13/11/04, they come out" +
            " as an unrostered volunteer.";
        PARTIC_DATE_SLOTS_NOT_ROSTERED.setDescription(desc);
        PARTIC_DATE_SLOTS_NOT_ROSTERED.setFixed(true);
        PARTIC_DATE_SLOTS_NOT_ROSTERED.setFix("Enforced the application to understand that" +
            " the timezone is Sydney, thus not needing to get any TZ for the user from" +
            " the machine. Did this by making sure every GregorianCalendar was created" +
            " with a TimeZone. Also, as this wasn't enough, made the default TimeZone" +
            " to be Sydney. This needed some extra tricks for Web Start. Quite possibly" +
            " the GregorianCalendar stuff is now not necessary.");
        //
        desc = "To repeat go to a volunteer with an overnight shift. Set him"
            + " to be No overnights, and then press EnterQuery. The only problem"
            + " this bug is concerned with is fact that stays in Enter Query";
        ENT_QRY_CAUSES_VALIDATION.setDescription(desc);
        //
        desc = "To repeat go to a volunteer with an overnight shift. Set him"
            + " to be No overnights, and then press EnterQuery. Can"
            + " get an infinite loop when try to change to something else. (Concept"
            + " of a field is actually wrong here - so try to make a new field"
            + " control).";
        FIELD_VALIDATION_NO_OVERNIGHTS.setDescription(desc);
        //
        desc = "When do a commit, for some names are inexplicably landed right"
            + " back at the beginning. For example commit when are on Ken Pritchett, also " +
            " happened for Mike Tighe";
        SEARCH_FAILURE.setDescription(desc);
        //
        desc = "Dialog should prevent the stack trace see, so test all fixed "
            + "for this. Another thing to do is to make sure buddies email has its own "
            + "title. Way to do this is probably to have a seperate emailer - maybe even "
            + "constructing one from the other. Got this error even when had a current month. "
            + "The bug may be due to having just done a query to find Tanith. Seems strange "
            + "that doing a query on workers would wipe out the rostered month, but try to "
            + "repeat anyway";
        EMAILS.setDescription(desc);
        EMAILS.setFix("Problem is was looking for the month of the CURRENT NewMonth"
            + " Fix was to not make current special but refer to a global "
            + " ParticularRoster.getGlobalCurrentParticularRoster()");
        EMAILS.setFixed(true);
        //
        desc = "Also have at least one Workers comparator, that does things differently, "
            + "and JDOQL ordering that different again! WombatNote.manyOrderings";
        MANY_ORDERINGS.setDescription(desc);
        //
        desc = "Having a new vol with only a contact name causes a stack trace when ordering";
        CONTACT_NAME.setDescription(desc);
        //
        desc = "When in enterQry mode do not want to be able to press a letter";
        UNENABLE_ALPHABET.setDescription(desc);
        //
        desc = "Get tabs rostered and unrostered. Make a change to an unrostered, "
            + "tab to rostered then tab back again. When try to commit will have lost the change";
        APPLICATION_COMMIT.setDescription(desc);
        String grandPlanToFix = "We need to specify, basically for all our queries," +
                " that they do not overwrite current data. With javax.jdo.option.IgnoreCache" +
                " having a default of false then this s/not happen - so let's POST and re-open" +
                " this bug for testing - the data in memory s/not be being overwritten - do a" +
                " little test of retaining values in the cache for Kodo"; 
        String howFixed = "POST was being done in RosterWorkersStrand.select() when "
            + "being un-selected. This altered the changed Worker from screen to memory "
            + "however querying again on the same data (as rostered and unrostered shared "
            + "the same data) overwrote all the workers. Easy fix that did was to do a "
            + "COMMIT instead of a POST. Performance now bad but hopefully this will improve "
            + "with JDO. Another solution would be to have two lots of data. This would imply "
            + "that MenuApplication would take a list of datas rather than one. Do this as a "
            + "next step. As is slow, switching code should have info of where going to available, "
            + "as this way could elect to POST if not going to a view that will wipe your data."
            + "(Unfortunately there is a transitive problem with this idea) " +
            NEWLINE + grandPlanToFix;
        APPLICATION_COMMIT.setFix(howFixed);
        APPLICATION_COMMIT.setFixed( false);
        //
    }
    
    public WombatNote()
    {
        super();
    }

    public WombatNote(String name)
    {
        super( name);
    }
    
    public WombatNote(String name, boolean visible)
    {
        super(name, visible);
    }
}
