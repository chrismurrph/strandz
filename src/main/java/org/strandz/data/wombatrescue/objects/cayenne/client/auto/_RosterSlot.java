package org.strandz.data.wombatrescue.objects.cayenne.client.auto;

import java.util.Date;

import org.apache.cayenne.PersistentObject;
import org.apache.cayenne.ValueHolder;
import org.strandz.data.wombatrescue.objects.cayenne.client.DayInWeek;
import org.strandz.data.wombatrescue.objects.cayenne.client.MonthInYear;
import org.strandz.data.wombatrescue.objects.cayenne.client.NumDaysInterval;
import org.strandz.data.wombatrescue.objects.cayenne.client.Override;
import org.strandz.data.wombatrescue.objects.cayenne.client.WeekInMonth;
import org.strandz.data.wombatrescue.objects.cayenne.client.WhichShift;
import org.strandz.data.wombatrescue.objects.cayenne.client.Worker;

/**
 * A generated persistent class mapped as "RosterSlot" Cayenne entity. It is a good idea to
 * avoid changing this class manually, since it will be overwritten next time code is
 * regenerated. If you need to make any customizations, put them in a subclass.
 */
public abstract class _RosterSlot extends PersistentObject {

    public static final String DISABLED_PROPERTY = "disabled";
    public static final String JDOCLASS_PROPERTY = "jdoclass";
    public static final String JDOVERSION_PROPERTY = "jdoversion";
    public static final String MONTHLY_RESTART_PROPERTY = "monthlyRestart";
    public static final String NOT_AVAILABLE_PROPERTY = "notAvailable";
    public static final String SPECIFIC_DATE_PROPERTY = "specificDate";
    public static final String START_DATE_PROPERTY = "startDate";
    public static final String ACTUAL_DAY_IN_WEEK_PROPERTY = "actualDayInWeek";
    public static final String ACTUAL_NOT_IN_MONTH_PROPERTY = "actualNotInMonth";
    public static final String ACTUAL_NUM_DAYS_INTERVAL_PROPERTY = "actualNumDaysInterval";
    public static final String ACTUAL_ONLY_IN_MONTH_PROPERTY = "actualOnlyInMonth";
    public static final String ACTUAL_OVERRIDES_OTHERS_PROPERTY = "actualOverridesOthers";
    public static final String ACTUAL_WEEK_IN_MONTH_PROPERTY = "actualWeekInMonth";
    public static final String ACTUAL_WHICH_SHIFT_PROPERTY = "actualWhichShift";
    public static final String WORKER_PROPERTY = "worker";

    protected boolean disabled;
    protected String jdoclass;
    protected Integer jdoversion;
    protected boolean monthlyRestart;
    protected boolean notAvailable;
    protected Date specificDate;
    protected Date startDate;
    protected ValueHolder actualDayInWeek;
    protected ValueHolder actualNotInMonth;
    protected ValueHolder actualNumDaysInterval;
    protected ValueHolder actualOnlyInMonth;
    protected ValueHolder actualOverridesOthers;
    protected ValueHolder actualWeekInMonth;
    protected ValueHolder actualWhichShift;
    protected ValueHolder worker;

    public boolean isDisabled() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "disabled", false);
        }

        return disabled;
    }
    public void setDisabled(boolean disabled) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "disabled", false);
        }

        Object oldValue = this.disabled;
        this.disabled = disabled;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "disabled", oldValue, disabled);
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

    public boolean isMonthlyRestart() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "monthlyRestart", false);
        }

        return monthlyRestart;
    }
    public void setMonthlyRestart(boolean monthlyRestart) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "monthlyRestart", false);
        }

        Object oldValue = this.monthlyRestart;
        this.monthlyRestart = monthlyRestart;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "monthlyRestart", oldValue, monthlyRestart);
        }
    }

    public boolean isNotAvailable() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "notAvailable", false);
        }

        return notAvailable;
    }
    public void setNotAvailable(boolean notAvailable) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "notAvailable", false);
        }

        Object oldValue = this.notAvailable;
        this.notAvailable = notAvailable;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "notAvailable", oldValue, notAvailable);
        }
    }

    public Date getSpecificDate() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "specificDate", false);
        }

        return specificDate;
    }
    public void setSpecificDate(Date specificDate) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "specificDate", false);
        }

        Object oldValue = this.specificDate;
        this.specificDate = specificDate;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "specificDate", oldValue, specificDate);
        }
    }

    public Date getStartDate() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "startDate", false);
        }

        return startDate;
    }
    public void setStartDate(Date startDate) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "startDate", false);
        }

        Object oldValue = this.startDate;
        this.startDate = startDate;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "startDate", oldValue, startDate);
        }
    }

    public DayInWeek getActualDayInWeek() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualDayInWeek", true);
        }

        return (DayInWeek) actualDayInWeek.getValue();
    }
    public void setActualDayInWeek(DayInWeek actualDayInWeek) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualDayInWeek", true);
        }

        this.actualDayInWeek.setValue(actualDayInWeek);
    }

    public MonthInYear getActualNotInMonth() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualNotInMonth", true);
        }

        return (MonthInYear) actualNotInMonth.getValue();
    }
    public void setActualNotInMonth(MonthInYear actualNotInMonth) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualNotInMonth", true);
        }

        this.actualNotInMonth.setValue(actualNotInMonth);
    }

    public NumDaysInterval getActualNumDaysInterval() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualNumDaysInterval", true);
        }

        return (NumDaysInterval) actualNumDaysInterval.getValue();
    }
    public void setActualNumDaysInterval(NumDaysInterval actualNumDaysInterval) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualNumDaysInterval", true);
        }

        this.actualNumDaysInterval.setValue(actualNumDaysInterval);
    }

    public MonthInYear getActualOnlyInMonth() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualOnlyInMonth", true);
        }

        return (MonthInYear) actualOnlyInMonth.getValue();
    }
    public void setActualOnlyInMonth(MonthInYear actualOnlyInMonth) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualOnlyInMonth", true);
        }

        this.actualOnlyInMonth.setValue(actualOnlyInMonth);
    }

    public Override getActualOverridesOthers() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualOverridesOthers", true);
        }

        return (Override) actualOverridesOthers.getValue();
    }
    public void setActualOverridesOthers(Override actualOverridesOthers) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualOverridesOthers", true);
        }

        this.actualOverridesOthers.setValue(actualOverridesOthers);
    }

    public WeekInMonth getActualWeekInMonth() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualWeekInMonth", true);
        }

        return (WeekInMonth) actualWeekInMonth.getValue();
    }
    public void setActualWeekInMonth(WeekInMonth actualWeekInMonth) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualWeekInMonth", true);
        }

        this.actualWeekInMonth.setValue(actualWeekInMonth);
    }

    public WhichShift getActualWhichShift() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualWhichShift", true);
        }

        return (WhichShift) actualWhichShift.getValue();
    }
    public void setActualWhichShift(WhichShift actualWhichShift) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualWhichShift", true);
        }

        this.actualWhichShift.setValue(actualWhichShift);
    }

    public Worker getWorker() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "worker", true);
        }

        return (Worker) worker.getValue();
    }
    public void setWorker(Worker worker) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "worker", true);
        }

        this.worker.setValue(worker);
    }

}
