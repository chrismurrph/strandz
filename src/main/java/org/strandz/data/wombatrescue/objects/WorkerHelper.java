package org.strandz.data.wombatrescue.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.data.objects.MonthInYearI;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Chris
 * Date: 31/08/2008
 * Time: 17:22:42
 */
public class WorkerHelper
{
    private WorkerI worker;

    private transient static final String EQUALS_PROP_NAMES[] = {
        "dummy",
        "christianName",
        "surname",
        "groupName",
        //"belongsToGroup", //avoid recursion
        "groupContactPerson",
        "street",
        "suburb",
        "postcode",
        "homePhone",
        "workPhone",
        "mobilePhone",
        "contactName",
        "email1",
        "email2",
        "unknown",
        "shiftPreference",
        "seniority",
        "sex",
        "flexibility",
        "birthday",
        "away1Start",
        "away1End",
        "away2Start",
        "away2End",
        "comments",
    };
    //
    public static transient boolean ownHashEquals = true;

    public WorkerHelper( WorkerI worker)
    {
        this.worker = worker;
    }

    public String getToValidate()
    {
        String result = getToLong();
        // sanity check
        if(result == null || result.equals("") || result.equals(" ")
            || result.toString().equals("null"))
        {
            Err.error(
                "Would be impolite result send an email result to worker <" + result + ">, ID: <" +
                    worker.getId() + ">, class: " + worker.getClass().getName() + ", isDummy: " + worker.isDummy());
        }
        return result;
    }

    public String getToShort()
    {
        return helperToString();
    }

    public String getToLong()
    {
        String result = null;
        String surname = worker.getSurname();
        String christianName = worker.getChristianName();
        String contact = worker.getContactName();
        String group = worker.getGroupName();
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
            Err.error("Would be impolite to send to just a surname: <" + surname + ">");
        }
        if(contact != null)
        {
            if(result == null && group != null)
            {
                result = contact + " (on behalf of " + group + ")";
            }
            else
            {
                if(result == null)
                {
                    Err.error( "Need validation that a contact name is only appropriate for a group");
                }
                else
                {
                    result = contact + " (on behalf of " + result + ")";
                }
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

    public boolean onHolidayWholePeriod(MonthInYearI month, int year)
    {
        boolean result;
        Date beginOfThisMonth = TimeUtils.getPeriodAsBeginDate( month.getOrdinal(), year);
        //Err.pr( "beginOfThisMonth: " + beginOfThisMonth);
        /*
         * The end date for the holiday for a worker has the time portion always as irrelevant,
         * thus it is an error to add a day to get the time portion correct. If we did we would
         * get workers who are actually on holiday for the whole period being reported as not on
         * holiday for the last 24 hours of the month.
         */
        //Date endOfThisMonth = TimeUtils.addDays( TimeUtils.getPeriodAsEndDate( month.getOrdinal(), year), 1);
        Date endOfThisMonth = TimeUtils.getPeriodAsEndDate( month.getOrdinal(), year);
        //Err.pr( "endOfThisMonth: " + endOfThisMonth);
        result = onHolidayWholePeriod( beginOfThisMonth, endOfThisMonth);
        return result;
    }

    public boolean onHoliday(Date date)
    {
        boolean result = false;
        boolean show = false;
        if(WombatNote.CHK_ON_HOLIDAY_COMPARISON.isVisible())
        {
            if(helperToString().equals("David Ellis"))
            {
                Err.pr(WombatNote.CHK_ON_HOLIDAY_COMPARISON, "Seeing if David on holiday for " + date);
                show = true;
            }
        }
        if(worker.getAway1Start() != null && worker.getAway1End() != null)
        {
            if(show)
            {
                Err.pr(WombatNote.CHK_ON_HOLIDAY_COMPARISON, "cfing with start " +
                    worker.getAway1Start() + " and end " + worker.getAway1End());
            }
            if((worker.getAway1Start().before(date) && worker.getAway1End().after(date))
                || worker.getAway1Start().equals(date) || worker.getAway1End().equals(date))
            {
                result = true;
            }
        }
        if(!result && worker.getAway2Start() != null && worker.getAway2End() != null)
        {
            if((worker.getAway2Start().before(date) && worker.getAway2End().after(date))
                || worker.getAway2Start().equals(date) || worker.getAway2End().equals(date))
            {
                result = true;
            }
        }
        return result;
    }

    public boolean onHolidayWholePeriod(Date periodStart, Date periodEnd)
    {
        boolean result = false;
        if(worker.getAway1Start() != null && worker.getAway1End() != null)
        {
            if(((worker.getAway1Start().before(periodStart) || worker.getAway1Start().equals(periodStart))
                && (worker.getAway1End().after(periodEnd)) || worker.getAway1End().equals(periodEnd)))
            {
                result = true;
            }
        }
        if(!result && worker.getAway2Start() != null && worker.getAway2End() != null)
        {
            if(((worker.getAway2Start().before(periodStart) || worker.getAway2Start().equals(periodStart))
                && (worker.getAway2End().after(periodEnd)) || worker.getAway2End().equals(periodEnd)))
            {
                result = true;
            }
        }
        return result;
    }

    public String formatWithPhone()
    {
        String result = helperToString();
        result += " (PH: " + getBestPhone() + ")";
        return result;
    }

    public String formatWithPhones()
    {
        String result = helperToString();
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
            + " M: " + formatMobilePhone() + " Email: " + worker.getEmail1() + ")";
        return result;
    }

    public String formatMobilePhone()
    {
        String result = worker.getMobilePhone();
        if(result == null)
        {
            result = Worker.NO_PHONE_SIGNIFIER;
        }
        return result;
    }

    public String formatWorkPhone()
    {
        String result = worker.getWorkPhone();
        if(result == null)
        {
            result = Worker.NO_PHONE_SIGNIFIER;
        }
        return result;
    }

    public String formatHomePhone()
    {
        String result = worker.getHomePhone();
        if(result == null)
        {
            result = Worker.NO_PHONE_SIGNIFIER;
        }
        return result;
    }

    public String getBestPhone()
    {
        String result = worker.getWorkPhone();
        if(result == null)
        {
            result = worker.getHomePhone();
        }
        if(result == null)
        {
            result = worker.getMobilePhone();
        }
        if(result == null)
        {
            result = Worker.NO_PHONE_SIGNIFIER;
        }
        return result;
    }

    public boolean isStrange()
    {
        boolean result = helperToString() == null;
        return result;
    }

    /**
     * Also have at least one Volunteers comparator, that does things differently,
     * and JDOQL ordering that different again! WombatBug.manyOrderings
     */
    public int compareTo(Object o)
    {
        int result;
        WorkerI other = (WorkerI) o;
        /**/
        Assert.notNull( other, "Can't cf <" + helperToString() + "> to null other in compareTo()");
        result = Utils.compareTo(worker.getChristianName(), other.getChristianName());
        if(result == 0)
        {
            result = Utils.compareTo(worker.getSurname(), other.getSurname());
            if(result == 0)
            {
                result = Utils.compareTo(worker.getGroupName(), other.getGroupName());
            }
        }
        /**/
        // Was removing from uniq list if only had same surname
        // result = XMLWombatData.GroupNameSurnameOrder.volunteerCf( this, other);
        return result;
    }

    public boolean helperEquals(Object o)
    {
        boolean result;
        if(!ownHashEquals)
        {
            //How this wasn't working I have no idea, as they are ==
            //result = worker.getClass().getSuperclass().equals(o);
            result = worker == o;
        }
        else
        {
            String txt = "Worker <" + worker.getToShort() + ">";
            ReasonNotEquals.addClassVisiting(txt);

            if(o == worker)
            {
                result = true;
            }
            else if(!(o instanceof WorkerI))
            {
                ReasonNotEquals.addReason("not an instance of a Worker");
                result = false;
            }
            else
            {
                WorkerI test = (WorkerI) o;
                result = SelfReferenceUtils.equalsByProperties(EQUALS_PROP_NAMES, worker, test);
            }
        }
        return result;
    }

    public int helperHashCode()
    {
        int result = 17;
        if(!ownHashEquals)
        {
            result = worker.getClass().getSuperclass().hashCode();
        }
        else
        {
            result = SelfReferenceUtils.hashCodeByProperties(result, EQUALS_PROP_NAMES, worker);
        }
        return result;
    }

    public String getOrderBy()
    {
        String result = null;
        if(worker.getGroupName() == null) //Only way of telling if it is not a group
        {
            if(worker.getSurname() != null)
            {
                result = worker.getSurname();
            }
            else if(worker.getChristianName() != null)
            {
                result = worker.getChristianName();
            }
            else if(worker.getContactName() != null)
            {
                result = worker.getContactName();
            }
            else
            {
                Err.error( "Some kind of name expected for <" + this + ">");
            }
        }
        else
        {
            result = worker.getGroupName();
        }
        return result;
    }

    public String toString()
    {
        Err.error( "Should be calling helperToString() rather than toString()");
        return null;
    }

    public String helperToString()
    {
        String result = null;
        if(worker.getChristianName() != null || worker.getSurname() != null)
        {
            if(worker.getChristianName() == null && worker.getSurname() != null)
            {
                result = worker.getSurname();
            }
            else if(worker.getChristianName() != null && worker.getSurname() == null)
            {
                result = worker.getChristianName();
            }
            else
            {
                result = worker.getChristianName() + " " + worker.getSurname();
            }
        }
        else
        {
            if(worker.getGroupName() != null)
            {
                result = worker.getGroupName();
            }
            /*
            else
            {
                Worker group = getBelongsToGroup();
                if(group != null)
                {
                    String contactName = group.getContactName();
                    if(contactName != null)
                    {
                        result = contactName;
                    }
                }
            }
            */
        }
        if(worker.isDummy())
        {
            result = "";
        }
        //Assert.notNull( result);
        // think causing NPE
        // if(result.indexOf( "null") != -1 && !result.equals( "null [NULL]"))
        // {
        // Err.error( "helperToString() not formatting well: <" + result + ">");
        // }
        return result;
    }

    public List getRosterslots( List allRosterSlots)
    {
        List result = new ArrayList();
        for(Iterator iter = allRosterSlots.iterator(); iter.hasNext();)
        {
            RosterSlotI slot = (RosterSlotI) iter.next();
            Err.pr(WombatNote.NOT_CASCADE_DELETING, "RS that are getting the worker for has id " + slot.getId());
            if(slot.getWorker().equals( worker))
            {
                result.add(slot);
            }
        }
        return result;
    }

    public String formatWithHolidays(Date first, Date last)
    {
        String result = helperToString();
        String hols = formatHolidays(first, last);
        if(hols != null)
        {
            result += hols;
        }
        /* This can be triggered - have since put in validation */
        Assert.isTrue( result.indexOf("null") == -1, "formatWithHolidays() not formatting well " +
                "(last time due to only a contact name being entered)");
        return result;
    }

    public String formatHolidays(Date first, Date last)
    {
        String result = "";
        Err.pr( WombatNote.TOO_MANY_HOLIDAYS, "formatting holidays (after lunch bug suspected)");
        Err.pr( WombatNote.TOO_MANY_HOLIDAYS, "first " + first);
        Err.pr( WombatNote.TOO_MANY_HOLIDAYS, "last " + last);
        Err.pr( WombatNote.TOO_MANY_HOLIDAYS, "getAway1Start()" + worker.getAway1Start());
        Err.pr( WombatNote.TOO_MANY_HOLIDAYS, "getAway1End()" + worker.getAway1End());
        TimeUtils.chkTimeZero( first);
        TimeUtils.chkTimeZero( last);
        TimeUtils.chkTimeZero( worker.getAway1Start(), "worker has a start period of first holiday problem for " + getToLong());
        TimeUtils.chkTimeZero( worker.getAway1End(), "worker has an end period of first holiday problem for " + getToLong());
        if(worker.getAway1Start() != null)
        {
            if(!TimeUtils.coversPeriod(worker.getAway1Start(), worker.getAway1End(), first, last,
                                        helperToString()))
            {
                result += " (HOL1: " + TimeUtils.DATE_FORMAT.format(worker.getAway1Start())
                    + ", " + TimeUtils.DATE_FORMAT.format(worker.getAway1End()) + ")";
            }
        }
        if(worker.getAway2Start() != null)
        {
            if(!TimeUtils.coversPeriod(worker.getAway2Start(), worker.getAway2End(), first, last,
                                        helperToString()))
            {
                result += " (HOL2: " + TimeUtils.DATE_FORMAT.format(worker.getAway2Start())
                    + ", " + TimeUtils.DATE_FORMAT.format(worker.getAway2End()) + ")";
            }
        }
        return result;
    }

    public void validate() throws ValidationException
    {
        validateHaveBothDates( worker.getAway1Start(), worker.getAway1End());
        validateHaveBothDates( worker.getAway2Start(), worker.getAway2End());
    }

    private void validateHaveBothDates( Date firstDate, Date secondDate) throws ValidationException
    {
        if(!(firstDate == null && secondDate == null))
        {
            //at least one of them is filled in
            if(!(firstDate != null && secondDate != null))
            {
                throw new ValidationException("There must be both an <Away Start> and an <Away End>");
            }
        }
    }

    public boolean hasEmailOnList( List emailList)
    {
        boolean result = false;
        if(Utils.containsIgnoreCase( emailList, worker.getEmail1())
            || Utils.containsIgnoreCase( emailList, worker.getEmail2()))
        {
            result = true;
        }
        return result;
    }

    /**
     * Called in two ways, once for an on-line report, at another time
     * for every time a roster is done.
     */
    public boolean isAvailable( Date first, Date last)
    {
        boolean result = false;
        Assert.notNull( worker.isUnknown());
        //Assert.notNull( vol.getSeniority(), "seniority s/not be null, but is for <" + vol.getToLong() + ">");
        if(worker.getAway1Start() != null)
        {
            boolean periodEnclosed = TimeUtils.periodEnclosed(first, last,
                                                              worker.getAway1Start(), worker.getAway1End(), worker.getChristianName());
            if(!periodEnclosed)
            {
                if(worker.getAway2Start() != null)
                {
                    periodEnclosed = TimeUtils.periodEnclosed(first, last,
                                                              worker.getAway2Start(), worker.getAway2End(), worker.getChristianName());
                    if(!periodEnclosed)
                    {
                        result = true;
                    }
                    else
                    {
                        //nufin - is away for the whole period
                    }
                }
                else
                {
                    result = true;
                }
            }
            else
            {
                //nufin - is away for the whole period
            }
        }
        else
        {
            result = true;
        }
        return result;
    }
}
