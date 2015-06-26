package org.strandz.data.wombatrescue.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.data.objects.DayInWeek;

import java.util.List;
import java.util.ArrayList;

/**
 * User: Chris
 * Date: 31/08/2008
 * Time: 17:22:42
 */
public class RosterSlotHelper
{
    private RosterSlotI rosterSlot;
    private transient int id;
    // Info from all notAvailable slots of a vol gets apportioned
    // to all the other slots
    private transient List unavailableDates = new ArrayList();
    public static transient boolean ownHashEquals = true;
    private transient static int constructedTimes;

    public RosterSlotHelper( RosterSlotI rosterSlot)
    {
        constructedTimes++;
        id = constructedTimes;
        /*
        Err.pr( "$$$ Constructed RosterSlot" + id);
        if(id == 0)
        {
        Err.stack();
        }
        */
        this.rosterSlot = rosterSlot;
    }

    public String getToSentence( Object nullWeekInMonth, Object nullWhichShift,
                                 Object nullMonthInYear, Object nullDayInWeek, Object weeklyNumDaysInterval)
    {
        StringBuffer buf = new StringBuffer();
        Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "toSentence() on a " + rosterSlot);
        if(rosterSlot.getStartDate() == null && rosterSlot.getWeekInMonth().equals( nullWeekInMonth) &&
            rosterSlot.getDayInWeek().equals(nullDayInWeek) && rosterSlot.getWhichShift().equals( nullWhichShift)
            && !rosterSlot.isNotAvailable())
        {
            buf.append("");
        }
        else
        {
            Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "startDate " + rosterSlot.getStartDate());
            Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "weekInMonth " + rosterSlot.getWeekInMonth());
            Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "dayInWeek " + rosterSlot.getDayInWeek());
            Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "whichShift " + rosterSlot.getWhichShift());
            if(rosterSlot.getSpecificDate() != null)
            {
                if(rosterSlot.isNotAvailable())
                {
                    buf.append("NOT AVAILABLE");
                    buf.append(" for a specific shift on ");
                    buf.append(TimeUtils.DATE_FORMAT.format(rosterSlot.getSpecificDate()));
                }
                else
                {
                    buf.append("'Specific' " + notNullBut( rosterSlot.getWhichShift(), "<Which Shift>"));
                    buf.append(" shift on ");
                    buf.append(TimeUtils.DATE_FORMAT.format(rosterSlot.getSpecificDate()));
                }
            }
            else if(rosterSlot.isMonthlyRestart())
            {
                buf.append("'Monthly Restart' " + notNullBut( rosterSlot.getWhichShift(), "<Which Shift>"));
                buf.append(" shifts on ");
                buf.append( notNullBut( rosterSlot.getDayInWeek(), "<Day>"));
                buf.append(", ");
                buf.append( notNullBut( rosterSlot.getWeekInMonth(), "<Week In Month>"));
            }
            else
            {
                buf.append("'Continuous' " + notNullBut( rosterSlot.getWhichShift(), "<Which Shift>"));
                buf.append(" shifts on ");
                buf.append( notNullBut( rosterSlot.getDayInWeek(), "<Day>"));
                buf.append(", ");
                buf.append( notNullBut( rosterSlot.getNumDaysInterval(), "<Interval>"));
                if(!rosterSlot.getNumDaysInterval().equals( weeklyNumDaysInterval))
                {
                    if(rosterSlot.getStartDate() == null)
                    {
                        //Is called while creating a RS so erroring not helpful
                        //Err.error("Expect a startDate for " + this);
                    }
                    else
                    {
                        buf.append(" (where first time ever was "
                            + TimeUtils.DATE_FORMAT.format(rosterSlot.getStartDate()) + ")");
                    }
                }
            }
            if(!rosterSlot.getOnlyInMonth().equals( nullMonthInYear))
            {
                buf.append(", in " + rosterSlot.getOnlyInMonth() + " only");
            }
            if(!rosterSlot.getNotInMonth().equals( nullMonthInYear))
            {
                buf.append(", not in " + rosterSlot.getNotInMonth());
            }
        }
        return buf.toString();
    }

    private Object notNullBut( Object couldBeNull, String substitute)
    {
        Object result;
        if(couldBeNull != null && couldBeNull.toString() == null)
        {
            result = new StringBuffer( substitute);
        }
        else
        {
            result = couldBeNull;
        }
        return result;
    }

    public int helperHashCode()
    {
        int result = 17;
        if(!ownHashEquals)
        {
            result = rosterSlot.getClass().getSuperclass().hashCode();
        }
        else
        {
            result = Utils.hashCode(result, rosterSlot.getDayInWeek());
            result = Utils.hashCode(result, rosterSlot.getWhichShift());
            result = Utils.hashCode(result, rosterSlot.isMonthlyRestart());
            result = Utils.hashCode(result, rosterSlot.getNumDaysInterval());
            result = Utils.hashCode(result, rosterSlot.getStartDate());
            result = Utils.hashCode(result, rosterSlot.getWeekInMonth());
            result = Utils.hashCode(result, rosterSlot.getSpecificDate());
            result = Utils.hashCode(result, rosterSlot.getOverridesOthers());
            result = Utils.hashCode(result, rosterSlot.isDisabled());
            result = Utils.hashCode(result, rosterSlot.isNotAvailable());
            result = Utils.hashCode(result, rosterSlot.getOnlyInMonth());
            result = Utils.hashCode(result, rosterSlot.getNotInMonth());
            result = Utils.hashCode(result, rosterSlot.getWorker());
        }
        return result;
    }

    //equals is done after a jdoDelete, and can't access anything that
    //has been deleted TODO - There must be a way to allow access for a short period of time?
    //Defering the delete is the best solution, but not as easy as it sounds
    //Thus for now set ownHashEquals to true when comparing two databases and false when
    //will need to delete. Same thing with worker.
    public boolean helperEquals(Object o)
    {
        boolean result = false;
        if(!ownHashEquals)
        {
            //result = rosterSlot.getClass().getSuperclass().equals(o);
            result = rosterSlot == o;
        }
        else
        {
            Utils.chkType(o, RosterSlotI.class);

            String txt = "RosterSlot " + this;
            ReasonNotEquals.addClassVisiting(txt);

            if(o == this)
            {
                result = true;
            }
            else if(!(o instanceof RosterSlotI))
            {
                ReasonNotEquals.addReason("not an instance of a RosterSlot");
            }
            else
            {
                RosterSlotI test = (RosterSlotI) o;
                if(Utils.equals(rosterSlot.getDayInWeek(), test.getDayInWeek()))
                {
                    if(Utils.equals(rosterSlot.getWhichShift(), test.getWhichShift()))
                    {
                        if(Utils.equals(rosterSlot.isMonthlyRestart(), test.isMonthlyRestart()))
                        {
                            if(Utils.equals(rosterSlot.getNumDaysInterval(), test.getNumDaysInterval()))
                            {
                                if(Utils.equals(rosterSlot.getStartDate(), test.getStartDate()))
                                {
                                    if(Utils.equals(rosterSlot.getWeekInMonth(), test.getWeekInMonth()))
                                    {
                                        if(Utils.equals(rosterSlot.getSpecificDate(), test.getSpecificDate()))
                                        {
                                            if(Utils.equals(rosterSlot.getOverridesOthers(), test.getOverridesOthers()))
                                            {
                                                if(Utils.equals(rosterSlot.isDisabled(), test.isDisabled()))
                                                {
                                                    if(Utils.equals(rosterSlot.isNotAvailable(), test.isNotAvailable()))
                                                    {
                                                        if(Utils.equals(rosterSlot.getOnlyInMonth(), test.getOnlyInMonth()))
                                                        {
                                                            if(Utils.equals(rosterSlot.getNotInMonth(), test.getNotInMonth()))
                                                            {
                                                                /*
                                                                if(Utils.equals(worker, test.getWorker()))
                                                                {
                                                                    result = true;
                                                                }
                                                                else
                                                                {
                                                                    ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("worker", test, this));
                                                                }
                                                                */
                                                                result = true;
                                                            }
                                                            else
                                                            {
                                                                ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("notInMonth", test, rosterSlot));
                                                            }
                                                        }
                                                        else
                                                        {
                                                            ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("onlyInMonth", test, rosterSlot));
                                                        }
                                                    }
                                                    else
                                                    {
                                                        ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("notAvailable", test, rosterSlot));
                                                    }
                                                }
                                                else
                                                {
                                                    ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("disabled", test, rosterSlot));
                                                }
                                            }
                                            else
                                            {
                                                ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("overridesOthers", test, rosterSlot));
                                            }
                                        }
                                        else
                                        {
                                            ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("specificDate", test, rosterSlot));
                                        }
                                    }
                                    else
                                    {
                                        ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("weekInMonth", test, rosterSlot));
                                    }
                                }
                                else
                                {
                                    ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("startDate", test, rosterSlot));
                                }
                            }
                            else
                            {
                                ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("numDaysInterval", test, rosterSlot));
                            }
                        }
                        else
                        {
                            ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("monthlyRestart", test, rosterSlot));
                        }
                    }
                    else
                    {
                        ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("whichShift", test, rosterSlot));
                    }
                }
                else
                {
                    ReasonNotEquals.addReason(SelfReferenceUtils.getDifference("dayInWeek", test, rosterSlot));
                }
            }
        }
        return result;
    }

    public int compareTo(Object o)
    {
        int result = 0;
        RosterSlotI other = (RosterSlotI)o;
        /**/
        Assert.notNull( other, "Can't cf " + this + " to null other in compareTo()");
        result = Utils.compareTo(rosterSlot.getWorker(), other.getWorker());
        if(result == 0)
        {
            result = Utils.compareTo(rosterSlot.getDayInWeek(), other.getDayInWeek());
            if(result == 0)
            {
                result = Utils.compareTo(rosterSlot.getWhichShift(), other.getWhichShift());
                if(result == 0)
                {
                    result = Utils.compareTo(rosterSlot.isMonthlyRestart(), other.isMonthlyRestart());
                    if(result == 0)
                    {
                        result = Utils.compareTo(rosterSlot.getNumDaysInterval(), other.getNumDaysInterval());
                        if(result == 0)
                        {
                            result = Utils.compareTo(rosterSlot.getStartDate(), other.getStartDate());
                            if(result == 0)
                            {
                                result = Utils.compareTo(rosterSlot.getWeekInMonth(), other.getWeekInMonth());
                                if(result == 0)
                                {
                                    result = Utils.compareTo(rosterSlot.getSpecificDate(), other.getSpecificDate());
                                    if(result == 0)
                                    {
                                        result = Utils.compareTo(rosterSlot.getOverridesOthers(), other.getOverridesOthers());
                                        if(result == 0)
                                        {
                                            result = Utils.compareTo(rosterSlot.isDisabled(), other.isDisabled());
                                            if(result == 0)
                                            {
                                                result = Utils.compareTo(rosterSlot.isNotAvailable(), other.isNotAvailable());
                                                if(result == 0)
                                                {
                                                    result = Utils.compareTo(rosterSlot.getOnlyInMonth(), other.getOnlyInMonth());
                                                    if(result == 0)
                                                    {
                                                        result = Utils.compareTo(rosterSlot.getNotInMonth(), other.getNotInMonth());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public String helperToString()
    {
        String result;
        String variableBit = null;
        String startDateTxt = null;
        if(rosterSlot.getStartDate() != null)
        {
            startDateTxt = TimeUtils.DATE_FORMAT.format(rosterSlot.getStartDate());
        }
        if(rosterSlot.isMonthlyRestart())
        {
            if(rosterSlot.getWeekInMonth() != null) // like this would fail validation!
            {
                variableBit = rosterSlot.getWeekInMonth().toString();
            }
        }
        else
        {
            variableBit = rosterSlot.getNumDaysInterval() + " " + startDateTxt;
        }
        result = rosterSlot.getDayInWeek() + ", [" + variableBit + "], " + rosterSlot.getWhichShift();
        // Had to remove when deleting DOs
        //+ " (" + worker + ")";
        result += "-" + rosterSlot.getId();
        return result;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return "" + getId();
    }

    public List getUnavailableDates()
    {
        return unavailableDates;
    }

    public void clearUnavailableDates()
    {
        unavailableDates = new ArrayList();
    }
}