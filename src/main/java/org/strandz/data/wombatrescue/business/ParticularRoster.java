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
import org.strandz.data.wombatrescue.calculated.MonthTransferObj;
import org.strandz.data.wombatrescue.calculated.RosterTransferObj;
import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.WhichShiftI;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.persist.SdzEMAssert;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.data.objects.DayInWeekI;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Basically an overall session object for all the things the rosterer does.
 * <p/>
 */
public class ParticularRoster implements ParticularRosterI
{
    private DataStore ds;
    private MonthTransferObj currentMonth;
    /**
     * Refreshed each time we do a roster
     */
    //private transient ClientObjProvider clientObjProvider;
    private StringBuffer roster;
    private Date whenDidInit;
    private MonthInYearI whenMonth;
    private int whenYear;
    private List validateBeanMsg = new ArrayList();
    private String name;
    //
    private RostererSessionI rostererSession;
    private RosterTransferObj rosterTransferObj;
    
    private static int constructedTimes;
    private int id;
    
    public ParticularRoster( String name)
    {
        constructedTimes++;
        id = constructedTimes;
        this.name = name;
        //Err.pr( "Created ParticularRoster ID: " + id + " named " + name);
    }
            
    /**
     * Inform a volunteer of his shifts for the coming time period.
     * First we get a beginning date and an end date. We only
     * accept shifts that come inclusively between these dates.
     */
    List<ParticularShift> getParticularShifts(WorkerI vol, Date begin, Date end)
    {
        List<WorkerI> vols = new ArrayList<WorkerI>();
        vols.add(vol);
        return getParticularShifts(vols, begin, end, false);
    }

    public List<ParticularShift> getParticularShifts(Date begin, Date end)
    {
        List vols = new ArrayList();
        return getParticularShifts(vols, begin, end, true);
    }
    
    public List<ParticularShift> getParticularShifts(Date begin, Date end, 
                                                     WhichShiftI whichShift,
                                                     DayInWeekI dayInWeek)
    {
        List<ParticularShift> result = new ArrayList();
        List vols = new ArrayList();
        List<ParticularShift> allShifts = getParticularShifts(vols, begin, end, true);
        for(ParticularShift particularShift : allShifts)
        {
            if(particularShift.getWhichShift().equals( whichShift) && 
                    particularShift.getDayInWeek().equals( dayInWeek))
            {
                result.add( particularShift);
            }
        }
        return result;
    }

    //We have made the default that inclusive is true, but this could
    //be a major change to the code.
    public List<ParticularShift> getParticularShifts(List<WorkerI> vols, Date begin, Date end, boolean all)
    {
        return getParticularShifts(vols, begin, end, true, all);
    }

    List<ParticularShift> getParticularShifts(List vols, Date begin, Date end, boolean inclusive, boolean all)
    {
        if(all)
        {
            //Err.pr( "Getting ALL particularShifts");
        }
        else
        {
            //Err.pr( "Getting particularShifts for vols list size " + vols.size() + " " + vols.get( 0));
        }
        List<ParticularShift> result = new ArrayList<ParticularShift>();
        /*
        if(clientObjProvider == null)
        {
            Err.pr( "Global Current Particular Roster: " + RosterSessionUtils.getGlobalCurrentParticularRoster().getName());
        }
        Assert.notNull(clientObjProvider, "clientObjProvider is null for ParticularRoster ID: " + id
            + " named " + getName());
        */
        Assert.notEmpty( rosterTransferObj.getRosterMonth().particularShifts);
        List shifts = RosterBusinessUtils.getParticularShifts(
                vols, rosterTransferObj.getRosterMonth().particularShifts, ds,
                RosterSessionUtils.getProperty( "memory").equals( "true"), all
        );
        //vols is sometimes just one vol, and it is common for a volunteer not
        //to be doing a shift
        //Assert.notEmpty( shifts);
        for(Iterator iterator = shifts.iterator(); iterator.hasNext();)
        {
            ParticularShift shift = (ParticularShift) iterator.next();
            if(!shift.isFailed())
            {
                Date shiftDate = shift.getShiftDate();
                if(shiftDate.after(begin) && shiftDate.before(end))
                {
                    result.add(shift);
                }
                else if(inclusive && (shiftDate.equals(begin) || shiftDate.equals(end)))
                {
                    result.add(shift);
                }
                else
                {
                    //Err.pr( "shift not within the dates: " + shiftDate);
                    //Err.pr( "begin: " + begin);
                    //Err.pr( "end: " + end);
                }
            }
            else
            {
                //Err.pr( "Failed shift: " + shift);
            }
        }
        return result;
    }
        
    public List getParticularShifts(WorkerI vol)
    {
        List vols = Utils.formList( vol);
        return RosterBusinessUtils.getParticularShifts(
                vols, rosterTransferObj.getRosterMonth().particularShifts,
                ds, RosterSessionUtils.getProperty( "memory").equals( "true"), false);
    }
    
    public static void main(String s[])
    {
        /*
        ParticularRoster obj = new ParticularRoster();
        obj.init( WombatDataFactory.getNewInstance().getData() );

        Date today = new Date();
        Date begin = DayInWeek.getBeginningDate( today );
        // Err.pr( "BEGIN: " + begin);
        Date end = DayInWeek.getEightDaysOn( begin );
        // Err.pr( "END:   " + end);
        String periodTxt = "the coming month of " + obj.getCurrentMonth().getMonth();
        obj.writeAllRosteringMsgs( begin, end, periodTxt );
        */
    }

    public void init(RostererSessionI rostererSession, DataStore ds)
    {
        whenDidInit = new Date();
        this.ds = ds;
        if(rostererSession == null)
        {
            //This will only become a problem if rostererSession is needed    
        }
        else
        {
            this.rostererSession = rostererSession;
        }
        init();
    }

    private void init()
    {
        Err.pr( WombatNote.NO_EM_WHEN_SOME_QUERY, "Doing init in " + this.getClass().getName());
        if(!ds.isOnTx())
        {
            ds.startTx( "Doing init in " + this.getClass().getName());
            Err.pr(SdzNote.BO_STUFF, "We have needed to force the start of a transaction for " + this + " using " + ds);
        }
        else
        {
            Err.pr( WombatNote.NO_EM_WHEN_SOME_QUERY, "... but are already on a transaction");            
        }
    }

    /*
    DayInWeek getDayInWeekOfBM( BuddyManager bm)
    {
      DayInWeek result = null;
      init();

      result = bm.getDayInWeek();
      return result;
    }
    */

     /**/
    public MonthInYearI getWhenMonth()
    {
        return whenMonth;
    }

    public int getWhenYear()
    {
        return whenYear;
    }

    private static MonthInYearI getMonth(int which, GregorianCalendar cal)
    {
        int month = cal.get(GregorianCalendar.MONTH);
        if(which == RosteringConstants.CURRENT)
        {// nufin
        }
        else if(which == RosteringConstants.NEXT)
        {
            month++;
        }
        else if(which == RosteringConstants.ONE_AFTER)
        {
            month += 2;
        }
        else if(which == RosteringConstants.JUST_GONE)
        {
            month--;
        }
        month = Utils.mod(month, 12);
        return MonthInYear.fromOrdinal(month);
    }

    public MonthInYearI getMonth(int which)
    {
        GregorianCalendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
        cal.setTime(whenDidInit);
        return getMonth(which, cal);
    }

    public void setAtMonth(int which)
    {
        GregorianCalendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
        Assert.notNull( whenDidInit, "init() has not been called on " + this);
        cal.setTime(whenDidInit);

        int year = cal.get(GregorianCalendar.YEAR);
        MonthInYearI whichMonthInYear = getMonth(which, cal);
        // Print.pr( "Month to display is " + whichMonthInYear);
        if(whichMonthInYear.equals(MonthInYear.JANUARY))
        {
            if(which == RosteringConstants.NEXT || which == RosteringConstants.ONE_AFTER)
            {
                year++;
            }
        }
        else if(whichMonthInYear.equals(MonthInYear.FEBRUARY))
        {
            if(which == RosteringConstants.ONE_AFTER)
            {
                year++;
            }
        }
        else if(whichMonthInYear.equals(MonthInYear.DECEMBER))
        {
            if(which == RosteringConstants.JUST_GONE)
            {
                year--;
            }
        }
        whenMonth = whichMonthInYear;
        whenYear = year;
        //whichMonth = which;
        // To force
        setCurrentMonth(null);
    }

    /*
    public static Worker findBuddy( ParticularShift shift)
    {
    Worker result = null;

    return result;
    }
    */

    String formatShifts(List<ParticularShift> shifts, List buddyManagers)
    {
        StringBuffer buf = new StringBuffer();
        for(ParticularShift shift : shifts)
        {
            buf.append(shift.toSentence() + Utils.NEWLINE);
            buf.append(shift.toBuddySentence( buddyManagers) + Utils.NEWLINE);
        }
        return buf.toString();
    }

    String formatSlots(List<RosterSlotI> slots)
    {
        StringBuffer buf = new StringBuffer();
        for(Iterator iter = slots.iterator(); iter.hasNext();)
        {
            RosterSlotI slot = (RosterSlotI) iter.next();
            buf.append(" -> " + slot.getToSentence() + Utils.NEWLINE);
        }
        return buf.toString();
    }
    
    public MessagesContainer writeAllRosteringMsgs(Date begin, Date end, String periodTxt)
    {
        MessagesContainer result = new MessagesContainer();
        if(RosterSessionUtils.getGlobalCurrentMonth() == null)
        {
            new MessageDlg("Must have a month that will be the subject of the emails");
        }
        else
        {
            List workers = getRostererSession().getRosterableWorkers();
            Assert.notEmpty( workers, "getRosterableWorkers().isEmpty() in " + getName());
            if(RosterSessionUtils.noYahooFileExists())
            {
                //Here we could also check the time of the file
                String msg[] = new String[2];
                msg[0] = "The file <" + RosteringConstants.YAHOO_FILENAME + "> does not exist, so emails will not request users to join the yahoo group.";
                msg[1] = Reports.YAHOO_HELP_MSG;
                new MessageDlg( msg, JOptionPane.ERROR_MESSAGE);
            }
            List buddyManagers = getRostererSession().getBuddyManagers();
            Assert.notEmpty( buddyManagers, "getBuddyManagers().isEmpty() in " + getName());
            List rosterSlots = getRostererSession().getRosterSlots();
            Assert.notEmpty( rosterSlots, "getRosterSlots().isEmpty() in " + getName());
            for(Iterator iter = workers.iterator(); iter.hasNext();)
            {
                WorkerI vol = (WorkerI) iter.next();
                SdzEMAssert.isEntityManaged( vol, "writeAllRosteringMsgs()");
                if(!vol.isUnknown())
                {
                    if(WombatNote.MISSING_EMAILS.isVisible() && vol.getToLong().equals( "Michael Gill"))
                    {
                        Err.debug();
                    }
                    List<ParticularShift> shifts = getParticularShifts(vol, begin, end);
                    if(!shifts.isEmpty())
                    {
                        String txt = CommunicationTextUtils.rosteringAWorkerEmail(vol,
                            shifts, this, periodTxt, buddyManagers, rosterSlots);
                        if(vol.getEmail1() != null)
                        {
                            result.emails.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getEmail1(), txt));
                            Err.pr( WombatNote.MISSING_EMAILS, txt);
                            Err.pr( WombatNote.MISSING_EMAILS, "==============================================");
                        }
                        else if(!vol.getBestPhone().equals(Worker.NO_PHONE_SIGNIFIER))
                        {
                            result.phoneCalls.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getBestPhone(), txt));
                        }
                        else
                        {
                            Err.pr( WombatNote.MISSING_EMAILS, "^^" + vol.getToLong() + " misses out on an email or call");
                        }
                    }
                    else
                    {
                        Err.pr( WombatNote.MISSING_EMAILS, "^^" + vol.getToLong() + " has no shifts between " + begin + " and " + end);
                    }
                }
                else
                {
                    Err.pr( WombatNote.MISSING_EMAILS, "^^" + vol.getToLong() + " unknown so no email");
                }
            }
            /*
            for(Iterator iter = phoneCalls.iterator(); iter.hasNext();)
            {
            Err.pr( "CALL   ==============================================");
            Err.pr( iter.next());
            Err.pr( "HANGUP ==============================================");
            }
            */
        }
        return result;
    }

    /**
     * The email that goes out will only go to those of unknown flexibility and will be pretending that
     * we think that the volunteer is category 'C', no fussed.
     */
    public MessagesContainer writeNoOvernightsRequestMsgs()
    {
        MessagesContainer result = new MessagesContainer();
        for(Iterator iter = getRostererSession().getWorkers().iterator(); iter.hasNext();)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(!vol.isUnknown() && vol.getFlexibility().equals(Flexibility.NULL) && vol.getBelongsToGroup().isDummy())
            {
                String txt = CommunicationTextUtils.noOvernightsEmail(vol);
                if(vol.getEmail1() != null)
                {
                    result.emails.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getEmail1(), txt));
                    // Err.pr( txt);
                    // Err.pr( "==============================================");
                }
            }
        }
        return result;
    }
    
    public MessagesContainer writeStatusRequestMsgs()
    {
        MessagesContainer result = new MessagesContainer();
        if(RosterSessionUtils.getGlobalCurrentMonth() == null)
        {
            new MessageDlg("Must have a month that will be the subject of the emails");
        }
        else
        {
            Err.pr( WombatNote.CURRENT_MONTH, "writeStatusRequestMsgs() BO will use is " + getName());
            StringBuffer buf = display(RosteringConstants.UNROSTERED_STATUS);
            for(Iterator iter = getRostererSession().getWorkers().iterator(); iter.hasNext();)
            {
                WorkerI vol = (WorkerI) iter.next();
//                if(vol.getTo().equals( "Lynda Hamilton"))
//                {
//                    Err.debug();
//                }
                if(!vol.isUnknown() && vol.getEmail1() != null && !isRosteredInCurrentMonth( vol) &&
                    !vol.onHolidayWholePeriod( RosterSessionUtils.getGlobalCurrentMonth().month,
                        RosterSessionUtils.getGlobalCurrentMonth().year) &&
                    vol.getBelongsToGroup().isDummy())
                {
                    String txt = CommunicationTextUtils.statusEmail(vol, RosterSessionUtils.getGlobalCurrentMonth().month, buf.toString());
                    {
                        result.emails.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getEmail1(), txt));
                        // Err.pr( txt);
                        // Err.pr( "==============================================");
                    }
                }
            }
        }
        return result;
    }

    public MessagesContainer writeAllPhoneRequestMsgs()
    {
        MessagesContainer result = new MessagesContainer();
        for(Iterator iter = getRostererSession().getWorkers().iterator(); iter.hasNext();)
        {
            WorkerI vol = (WorkerI) iter.next();
            /*
            if(vol.getChristianName() != null && vol.getChristianName().equals( "Michael"))
            {
                Err.debug();
            }
            */
            boolean canBePhoned = !vol.getBestPhone().equals(Worker.NO_PHONE_SIGNIFIER);
            if(!vol.isUnknown())
            {
                String txt = CommunicationTextUtils.phoneEmail(vol);
                if(!canBePhoned && vol.getEmail1() != null)
                {
                    result.emails.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getEmail1(), txt));
                    // Err.pr( txt);
                    // Err.pr( "==============================================");
                }
            }
        }
        /*
        for(Iterator iter = phoneCalls.iterator(); iter.hasNext();)
        {
        Err.pr( "CALL   ==============================================");
        Err.pr( iter.next());
        Err.pr( "HANGUP ==============================================");
        }
        */
        return result;
    }

    public MessagesContainer writeAllAddressAndRaffleMsgs()
    {
        MessagesContainer result = new MessagesContainer();
        for(Iterator iter = getRostererSession().getWorkers().iterator(); iter.hasNext();)
        {
            WorkerI vol = (WorkerI) iter.next();
            if(vol.getStreet() == null)
            {
                String txt = CommunicationTextUtils.addressEmail(vol);
                if(vol.getEmail1() != null)
                {
                    result.emails.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getEmail1(), txt));
                }
                else if(!vol.getBestPhone().equals(Worker.NO_PHONE_SIGNIFIER))
                {
                    result.phoneCalls.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getBestPhone(), txt));
                }
                txt = CommunicationTextUtils.raffleEmail(vol);
                if(vol.getEmail1() != null)
                {
                    result.emails.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getEmail1(), txt));
                }
                else if(!vol.getBestPhone().equals(Worker.NO_PHONE_SIGNIFIER))
                {
                    result.phoneCalls.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getBestPhone(), txt));
                }
            }
            else
            {
                String txt = CommunicationTextUtils.raffleEmail(vol);
                if(vol.getEmail1() != null)
                {
                    result.emails.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getEmail1(), txt));
                }
                else if(!vol.getBestPhone().equals(Worker.NO_PHONE_SIGNIFIER))
                {
                    result.phoneCalls.add(new Msg(RosterSessionUtils.getProperty( "rostererEmailAddress"), vol.getBestPhone(), txt));
                }
            }
        }
        return result;
    }

    public List retrieveValidateBeanMsg()
    {
        return validateBeanMsg;
    }
    
    public String uploadRoster()
    {
        return getRostererSession().getUploadRosterService().uploadRoster(
            roster.toString(), RosteringConstants.CURRENT, currentMonth.month);
    }

    public String uploadRosterAsOld()
    {
        return getRostererSession().getUploadRosterService().uploadRoster(
            roster.toString(), RosteringConstants.JUST_GONE, currentMonth.month);
    }

    /**
     * The first part of a GUI-driven process.
     */
    public StringBuffer display(int what)
    {
        StringBuffer result = null;
        if(currentMonth == null || what == RosteringConstants.ROSTER)
        {
            setCurrentMonth(new MonthTransferObj(whenMonth, whenYear));
        }
        RosterTransferObj received = getRostererSession().getRosterService().
                getRoster( what, currentMonth);
        if(received != null)
        {
            result = received.getRoster();
            //clientObjProvider = received.getRosterMonth().getClientObjProvider();
            //Assert.notNull(clientObjProvider, "calculatedObjectsServer did not make it across");
            //Err.pr( "calculatedObjectsServer obtained for ID: " + id);
            roster = received.getRoster();
            if(Utils.isBlank( result.toString()))
            {
                Err.error( "Roster came back blank");
            }
            if(what == RosteringConstants.ROSTER)
            {
                RosterSessionUtils.setGlobalCurrentParticularRoster( this);
            }
            RosterBusinessUtils.checkSpreadOfDays( received.getRosterMonth().particularShifts,
                currentMonth.month);
        }
        rosterTransferObj = received;
        return result;
    }
        
    public boolean isRosteredInCurrentMonth(WorkerI vol)
    {
        boolean result = false;
        /*
        if(calculatedObjectsServer == null)
        {
            calculatedObjectsServer = new CalculatedObjectsServer();
        }
        */
        for(Iterator iterator = rosterTransferObj.getRosterMonth().particularShifts.iterator(); iterator.hasNext();)
        {
            ParticularShift particularShift = (ParticularShift) iterator.next();
            if(particularShift.getWorker() != null)
            {
                if(particularShift.getWorker().equals( vol))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    List<RosterSlotI> getNonDisabledSlotsOfWorker(WorkerI vol, List<RosterSlotI> rosterSlots)
    {
        List<RosterSlotI> result = new ArrayList<RosterSlotI>();
        Assert.notNull( rosterSlots, "Null rosterSlots param not expected to be passed into getNonDisabledSlotsOfWorker()");
        Assert.notNull( vol, "Null worker param not expected to be passed into getNonDisabledSlotsOfWorker()");
        for(Iterator iter = rosterSlots.iterator(); iter.hasNext();)
        {
            RosterSlotI slot = (RosterSlotI) iter.next();
            Assert.notNull( slot, "No null rosterSlots expected to be passed into getNonDisabledSlotsOfWorker()");
            Assert.notNull( slot.getWorker(), "A rosterSlot has a null worker: " + slot);
            if(slot.getWorker().equals(vol) && !slot.isDisabled())
            {
                result.add(slot);
            }
        }
        return result;
    }

    private void setCurrentMonth(MonthTransferObj m)
    {
        this.currentMonth = m;
        Err.pr(WombatNote.CURRENT_MONTH, "## Set current month to " + m + " on " + getName());
    }

    public MonthTransferObj getCurrentMonth()
    {
        Err.pr(WombatNote.CURRENT_MONTH, "## Get current month returning " + currentMonth + " from " + getName());
        return currentMonth;
    }

    public String getName()
    {
        return name;
    }

    /*
    public void setName(String name)
    {
        this.name = name;
    }
    */
        
    public DataStore getDataStore()
    {
        return ds;
    }
    
    private RostererSessionI getRostererSession()
    {
        RostererSessionI result = rostererSession;
        if(result == null)
        {
            Err.error( "Try to init() with a rostererSession, as last resort call " +
                    "RosterSessionUtils.getGlobalRostererSession()");
            //result = RosterSessionUtils.getGlobalRostererSession(); 
        }
        Assert.notNull( result);
        return result;
    }

    public RosterTransferObj getRosterTransferObj()
    {
        return rosterTransferObj;
    }
}
