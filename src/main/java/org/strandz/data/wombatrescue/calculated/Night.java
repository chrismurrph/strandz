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
package org.strandz.data.wombatrescue.calculated;

import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.data.wombatrescue.objects.WeekInMonth;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Night implements Serializable
{
    // pk
    private Date shiftDate;
    //
    private ParticularShift eveningShifts[] = new ParticularShift[3];
    private ParticularShift overnightShifts[] = new ParticularShift[2];
    private boolean unrostered = false;
    // generated
    private MonthInYear monthWithDate;
    private int yearWithDate = -99;
    // Used for convenience of notation, call formWorkers() to use
    private WorkerI eveningVols[] = new WorkerI[3];
    private WorkerI overnightVols[] = new WorkerI[2];
    // Similar
    private String eveningVolsStr[] = new String[3];
    private String overnightVolsStr[] = new String[2];
    //
    private List validateBeanMsg = new ArrayList();
    private CalendarHelper calendarHelper = new CalendarHelper();
    private transient ClientObjProvider clientObjProvider;
    //
    private static final WorkerComparator comparator = new WorkerComparator();
    public static final int NAME_WIDTH = 17;
    public static final String THURSDAY = "THURSDAY";
    private static int times;
    private static int constructedTimes;
    private int id;

    public Night(Date shiftDate)
    {
        constructedTimes++;
        id = constructedTimes;
        if(shiftDate == null)
        {
            Err.error("shiftDate == null");
        }
        setShiftDate(shiftDate);
    }

    public void setClientObjProvider(ClientObjProvider clientObjProvider)
    {
        this.clientObjProvider = clientObjProvider;
    }

    /**
     * Convenience method used when ParticularShifts are presented on one row
     * @param idx
     * @return
     */
    public ParticularShift getParticularShift( int idx)
    {
        ParticularShift result;
        Assert.isTrue( idx >= 0 && idx <= 4, "There are only 5 particularShifts in a Night, you requested number " + idx);
        if(idx > 2)
        {
            result = overnightShifts[idx-3];
        }
        else
        {
            result = eveningShifts[idx];
        }
        return result;
    }

    /**
     * This copy is only used temporarily, for printing
     */
    public Night(Night other)
    {
        this.setShiftDate(other.getShiftDate());
        this.setFirstEvening(other.getFirstEvening());
        this.setSecondEvening(other.getSecondEvening());
        this.setThirdEvening(other.getThirdEvening());
        this.setFirstOvernight(other.getFirstOvernight());
        this.setSecondOvernight(other.getSecondOvernight());
    }

    public String getPartnersTxt(ParticularShift shift)
    {
        String result = null;
        WorkerI vol1 = null;
        WorkerI vol2 = null;
        if(shift == eveningShifts[0])
        {
            vol1 = eveningShifts[1].getWorker();
            vol2 = eveningShifts[2].getWorker();
        }
        else if(shift == eveningShifts[1])
        {
            vol1 = eveningShifts[0].getWorker();
            vol2 = eveningShifts[2].getWorker();
        }
        else if(shift == eveningShifts[2])
        {
            vol1 = eveningShifts[0].getWorker();
            vol2 = eveningShifts[1].getWorker();
        }
        else if(shift == overnightShifts[0])
        {
            vol1 = overnightShifts[1].getWorker();
        }
        else if(shift == overnightShifts[1])
        {
            vol1 = overnightShifts[0].getWorker();
        }
        else
        {
            Print.prArray(eveningShifts, "Night.getPartnersTxt()");
            Print.prArray(overnightShifts, "Night.getPartnersTxt()");
            Err.error("Unknown shift: " + shift);
        }
        if(vol1 == null && vol2 == null)
        {
            result = "no one";
        }
        else if(vol1 != null && vol2 != null)
        {
            result = vol1.formatWithPhone() + " and " + vol2.formatWithPhone();
        }
        else if(vol2 == null)
        {
            result = vol1.formatWithPhone();
        }
        else
        {
            Err.error();
        }
        return result;
    }

    /**
     * Consider calling this method to be like a RO transaction.
     * Once start altering which volunteer is on a particular
     * shift then must call this again.
     */
    public void formWorkers()
    {
        if(!eveningShifts[0].getWorker().equals( Worker.NULL))
        {
            eveningVols[0] = eveningShifts[0].getWorker();
        }
        if(!eveningShifts[1].getWorker().equals( Worker.NULL))
        {
            eveningVols[1] = eveningShifts[1].getWorker();
        }
        if(!eveningShifts[0].getWorker().equals( Worker.NULL))
        {
            eveningVols[2] = eveningShifts[2].getWorker();
        }
        if(!overnightShifts[0].getWorker().equals( Worker.NULL))
        {
            overnightVols[0] = overnightShifts[0].getWorker();
        }
        if(!overnightShifts[1].getWorker().equals( Worker.NULL))
        {
            overnightVols[1] = overnightShifts[1].getWorker();
        }
    }

    private boolean noneToUnRoster()
    {
        boolean result = false;
        if(eveningVols[0] == null && eveningVols[1] == null
            && eveningVols[2] == null && overnightVols[0] == null
            && overnightVols[1] == null)
        {
            result = true;
        }
        return result;
    }

    /**
     * Before do this will have taken a copy of the
     * night. Note here that the particularShifts
     * themselves are not altered, thus fairly shallow
     * night copying can be done.
     */
    private void unRosterAllVols()
    {
        if(!noneToUnRoster())
        {
            // Err.pr( "Have unrostered the following volunteers:");
            // printUnrosteredVols();
            /*
            firstEvening.setWorkerNoCheck( null);
            secondEvening.setWorkerNoCheck( null);
            firstOvernight.setWorkerNoCheck( null);
            secondOvernight.setWorkerNoCheck( null);
            */
             /**/
            eveningShifts[0].setFailed(true);
            eveningShifts[1].setFailed(true);
            eveningShifts[2].setFailed(true);
            overnightShifts[0].setFailed(true);
            overnightShifts[1].setFailed(true);
            /*
            eveningShifts[0] = null;
            eveningShifts[1] = null;
            eveningShifts[2] = null;
            overnightShifts[0] = null;
            overnightShifts[1] = null;
            */
        }
        unrostered = true;
    }

    /**
     * If have not rostered enough people for one shift
     * then the night is closed. The people that may have
     * already been rostered need to be taken off of their
     * particular shift. Need at least two people for the
     * evening shift, and NOW TWO for the overnight.
     */
    public boolean isNotComplete()
    {
        boolean unRosteredThis = false;
        if(eveningVols[0] == null || eveningVols[1] == null || eveningVols[0].equals( Worker.NULL) || eveningVols[1].equals( Worker.NULL))
        {
            unRosteredThis = true;
        }
        else if(overnightVols[0] == null || overnightVols[1] == null || overnightVols[0].equals( Worker.NULL) || overnightVols[1].equals( Worker.NULL))
        {
            unRosteredThis = true;
        }
        /*
        else if(overnightVols[0] == null)
        {
        unRosteredThis = true;
        }
        else
        {
        if(overnightVols[0] != null && overnightVols[1] == null
        && overnightVols[0].getSex().equals( Sex.NO_EVENINGS))
        {
        unRosteredThis = true;
        }
        }
        */
        return unRosteredThis;
    }

    /**
     * TODO See why this method did not need to guard against nulls and isNotComplete() did. Maybe we could do
     * with a separate class with startup and shutdown code...
     */
    public boolean isNotFullyComplete()
    {
        boolean notFullUp = false;
        if(eveningVols[0].equals( Worker.NULL) || eveningVols[1].equals( Worker.NULL)
            || eveningVols[2].equals( Worker.NULL))
        {
            notFullUp = true;
        }
        if(overnightVols[0].equals( Worker.NULL) || overnightVols[1].equals( Worker.NULL))
        {
            notFullUp = true;
        }
        return notFullUp;
    }

    public void unRosterAsNotComplete()
    {
        unRosterAllVols();
    }

    public List retrieveValidateBeanMsg()
    {
        return validateBeanMsg;
    }

    public boolean validate()
    {
        boolean ok = true;
        validateBeanMsg.clear();
        if(overnightVols[0] != null && overnightVols[1] != null)
        {/*
       * No longer an error
       if(firstOvernightVol.getSex().equals( Sex.NO_EVENINGS) &&
       secondOvernightVol.getSex().equals( Sex.NO_EVENINGS))
       {
       validateBeanMsg.add( "Two females together overnight on " + formattedDate());
       validateBeanMsg.add( firstOvernightVol.toString());
       validateBeanMsg.add( secondOvernightVol.toString());
       ok = false;
       }
       */
        }
        if(ok && eveningVols[0] != null && !eveningVols[0].isDummy() && eveningVols[0].getGroupName() == null
            && eveningVols[0].getSeniority() == null)
        {
            validateBeanMsg.add(eveningVols[0] + " has no seniority specified");
            ok = false;
        }
        if(ok && eveningVols[1] != null && !eveningVols[1].isDummy() && eveningVols[1].getGroupName() == null
            && eveningVols[1].getSeniority() == null)
        {
            validateBeanMsg.add(eveningVols[1] + " has no seniority specified");
            ok = false;
        }
        if(ok && eveningVols[2] != null && !eveningVols[2].isDummy() && eveningVols[2].getGroupName() == null
            && eveningVols[2].getSeniority() == null)
        {
            validateBeanMsg.add(eveningVols[2] + " has no seniority specified");
            ok = false;
        }
        if(ok && overnightVols[0] != null && !overnightVols[0].isDummy() && overnightVols[0].getGroupName() == null
            && overnightVols[0].getSeniority() == null)
        {
            validateBeanMsg.add(overnightVols[0] + " has no seniority specified");
            ok = false;
        }
        if(ok && overnightVols[1] != null && !overnightVols[1].isDummy() && overnightVols[1].getGroupName() == null
            && overnightVols[1].getSeniority() == null)
        {
            validateBeanMsg.add(overnightVols[1] + " has no seniority specified");
            ok = false;
        }
        return ok;
    }

    private WorkerI getClientWorker( WorkerI worker)
    {
        WorkerI result = worker;
        if(clientObjProvider != null)
        {
            result = clientObjProvider.getClientWorker( worker);
        }
        return result;
    }

    public String toSentence()
    {
        StringBuffer result = new StringBuffer();
        result.append(Utils.NEWLINE);
        result.append(getFormattedDate());
        result.append(Utils.NEWLINE);
        for(int i = 0; i < eveningVols.length; i++)
        {
            if(!getClientWorker( eveningVols[i]).isDummy())
            {
                result.append("Evening, " + getClientWorker( eveningVols[i]).formatWithPhones());
                result.append(Utils.NEWLINE);
            }
        }
        for(int i = 0; i < overnightVols.length; i++)
        {
            if(!getClientWorker( overnightVols[i]).isDummy())
            {
                result.append("Overnight, " + getClientWorker( overnightVols[i]).formatWithPhones());
                result.append(Utils.NEWLINE);
            }
        }
        return result.toString();
    }

    /**
     * To include the third in this, really need to write a comparator that
     * takes a Worker. Into list, be sorted, put back. Once each for evening
     * and overnight. Comparator would put a senior b4 a junior then quit. If
     * both same would have to put a male b4 a female.
     */
    private static class WorkerComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            int result = 0;
            WorkerI vol1 = (WorkerI) o1;
            WorkerI vol2 = (WorkerI) o2;
            /*
            times++;
            Err.pr( "To cf " + vol1 + " with " + vol2 + " times " + times);
            if(times == 7)
            {
            Err.debug();
            }
            */
            if(!Utils.equals(vol1, vol2))
            {
                if(Utils.equals(vol1.getSeniority(), vol2.getSeniority()))
                {
                    result = vol1.getSex().compareTo(vol2.getSex());
                }
                else
                {
                    result = vol1.getSeniority().compareTo(vol2.getSeniority());
                }
                if(result == 0)
                {
                    result = vol1.compareTo(vol2);
                }
                else if(vol1.equals( Worker.NULL) && !vol2.equals( Worker.NULL))
                {
                    //sex and seniority will order NULLs first, but we want them on the end
                    result = -1;
                }
            }
            return result;
        }
    }

    public void orderBySeniorityAndSex()
    {
        List dinnerShifters = new ArrayList();
        dinnerShifters.add(eveningVols[0]);
        dinnerShifters.add(eveningVols[1]);
        dinnerShifters.add(eveningVols[2]);
        Collections.sort(dinnerShifters, comparator);
        Utils.updateArrayFromList(dinnerShifters, eveningVols);
        for(int i = 0; i <= eveningShifts.length - 1; i++)
        {
            eveningShifts[i].setWorker(null);
            eveningShifts[i].setWorker(eveningVols[i]);
        }

        List overnighters = new ArrayList();
        overnighters.add(overnightVols[0]);
        overnighters.add(overnightVols[1]);
        Collections.sort(overnighters, comparator);
        Utils.updateArrayFromList(overnighters, overnightVols);
        for(int i = 0; i <= overnightShifts.length - 1; i++)
        {
            overnightShifts[i].setWorker(null);
            overnightShifts[i].setWorker(overnightVols[i]);
        }
    }

    /**
     * Prints out date in form "Wed 19"
     */
    public String getFormattedDate()
    {
        String result = TimeUtils.getFormattedDay(shiftDate) + " "
            + TimeUtils.getFormattedDayInMonth(shiftDate);
        //Err.pr( "formatted date to ret " + result + " from " + shiftDate);
        return result;
    }
    
    public String formattedNight( String notOpenSundayText, String closedText)
    {
        String result;
        if(unrostered)
        {
            String txt;
            if(getDayInWeek().equals(DayInWeek.SUNDAY))
            {
                txt = notOpenSundayText;
            }
            else if(getDayInWeek().equals(DayInWeek.THURSDAY))
            {
                txt = THURSDAY;
            }
            else
            {
                txt = closedText;
            }
            result = "[" + getFormattedDate() + "]" + "                         "
                + "                         " + "                   ----- " + txt;
        }
        else
        {
            formRosteredPrintout();
            result = "[" + getFormattedDate() + "]"
                + Utils.rightPadSpace(eveningVolsStr[0], NAME_WIDTH) + ","
                + Utils.rightPadSpace(eveningVolsStr[1], NAME_WIDTH) + ","
                + Utils.rightPadSpace(eveningVolsStr[2], NAME_WIDTH) + "|"
                + Utils.rightPadSpace(overnightVolsStr[0], NAME_WIDTH) + ","
                + Utils.rightPadSpace(overnightVolsStr[1], NAME_WIDTH);
        }
        return result;
    }

    private void formRosteredPrintout()
    {
        eveningVolsStr[0] = null;
        eveningVolsStr[1] = null;
        eveningVolsStr[2] = null;
        overnightVolsStr[0] = null;
        overnightVolsStr[1] = null;
        if(!eveningShifts[0].getWorker().equals( Worker.NULL))
        {
            eveningVolsStr[0] = eveningShifts[0].getWorker().toString();
        }
        else
        {
            eveningVolsStr[0] = "";
        }
        if(!eveningShifts[1].getWorker().equals( Worker.NULL))
        {
            eveningVolsStr[1] = eveningShifts[1].getWorker().toString();
        }
        else
        {
            eveningVolsStr[1] = "";
        }
        if(!eveningShifts[2].getWorker().equals( Worker.NULL))
        {
            eveningVolsStr[2] = eveningShifts[2].getWorker().toString();
        }
        else
        {
            eveningVolsStr[2] = "";
        }
        if(!overnightShifts[0].getWorker().equals( Worker.NULL))
        {
            overnightVolsStr[0] = overnightShifts[0].getWorker().toString();
        }
        else
        {
            overnightVolsStr[0] = "";
        }
        if(!overnightShifts[1].getWorker().equals( Worker.NULL))
        {
            overnightVolsStr[1] = overnightShifts[1].getWorker().toString();
        }
        else
        {
            overnightVolsStr[1] = "";
        }
    }
    
    public DayInWeek getDayInWeek()
    {
        DayInWeek result;
        if(shiftDate == null)
        {
            Err.error("shiftDate == null");
        }

        result = calendarHelper.getDayInWeek();        
        return result;
    }

    public MonthInYear getMonth()
    {
        return monthWithDate;
    }

    public int getYear()
    {
        return yearWithDate;
    }

    public WeekInMonth getWeekInMonth()
    {
        WeekInMonth result = null;
        if(shiftDate == null)
        {
            Err.error("shiftDate == null");
        }

        int week = 1;
        int day = calendarHelper.getCalendarWithDate().get(Calendar.DAY_OF_MONTH);
        if(day > 7)
        {
            week = 2;
            if(day > 14)
            {
                week = 3;
                if(day > 21)
                {
                    week = 4;
                    if(day > 28)
                    {
                        week = 5;
                    }
                }
            }
        }
        result = WeekInMonth.fromOrdinal(week);
        /*
        int week = getCalendarWithDate().get( Calendar.WEEK_OF_MONTH);
        result = WeekInMonth.fromOrdinal( week);
        Err.pr( "From " + week + " got WEEK_OF_MONTH " + result);
        Err.pr( "Where first: " + 1);
        Err.pr( "Where second: " + 2);
        Err.pr( "Where calendarWithDate is " + calendarWithDate.getTime());
        */
        return result;
    }

    public boolean isAlreadyOnEvening(WorkerI vol)
    {
        boolean result = false;
        if(vol.equals(eveningShifts[0].getWorker())
            || vol.equals(eveningShifts[1].getWorker())
            || vol.equals(eveningShifts[2].getWorker()))
        {
            result = true;
        }
        return result;
    }

    public boolean isAlreadyOnOvernight(WorkerI vol)
    {
        boolean result = false;
        if(vol.equals(overnightShifts[0].getWorker())
            || vol.equals(overnightShifts[1].getWorker()))
        {
            result = true;
        }
        return result;
    }

    /**
     * Returns the date.
     *
     * @return Date
     */
    public Date getShiftDate()
    {
        return shiftDate;
    }

    /**
     * Sets the date.
     *
     * @param shiftDate The date to set
     */
    public void setShiftDate(Date shiftDate)
    {
        this.shiftDate = shiftDate;
        GregorianCalendar calendar = calendarHelper.getCalendarWithDate(); 
        calendar.setTime(shiftDate);
        monthWithDate = MonthInYear.fromOrdinal(calendar.get(GregorianCalendar.MONTH));
        yearWithDate = calendar.get(GregorianCalendar.YEAR);
    }

    public ParticularShift getFirstEvening()
    {
        return eveningShifts[0];
    }

    public ParticularShift getSecondEvening()
    {
        return eveningShifts[1];
    }

    public ParticularShift getThirdEvening()
    {
        return eveningShifts[2];
    }

    public ParticularShift getFirstOvernight()
    {
        return overnightShifts[0];
    }

    public ParticularShift getSecondOvernight()
    {
        return overnightShifts[1];
    }

    private void debug(ParticularShift ps)
    {
        if(ps.getWorker() != null)
        {
            if(ps.getWorker().getChristianName() != null)
            {
                if(ps.getWorker().getChristianName().equals("Brandy"))
                {// Err.stack( "Brandy");
                }
            }
        }
    }

    public void setFirstEvening(ParticularShift firstEvening)
    {
        debug(firstEvening);
        this.eveningShifts[0] = firstEvening;
    }

    public void setSecondEvening(ParticularShift secondEvening)
    {
        this.eveningShifts[1] = secondEvening;
    }

    public void setThirdEvening(ParticularShift thirdEvening)
    {
        debug(thirdEvening);
        this.eveningShifts[2] = thirdEvening;
    }

    public void setFirstOvernight(ParticularShift firstOvernight)
    {
        this.overnightShifts[0] = firstOvernight;
    }

    public void setSecondOvernight(ParticularShift secondOvernight)
    {
        this.overnightShifts[1] = secondOvernight;
    }    
}
