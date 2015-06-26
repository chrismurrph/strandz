package org.strandz.data.wombatrescue.objects.cayenne.client.auto;

import org.apache.cayenne.PersistentObject;
import org.apache.cayenne.ValueHolder;
import org.strandz.data.wombatrescue.objects.cayenne.client.DayInWeek;
import org.strandz.data.wombatrescue.objects.cayenne.client.WhichShift;
import org.strandz.data.wombatrescue.objects.cayenne.client.Worker;

/**
 * A generated persistent class mapped as "BuddyManager" Cayenne entity. It is a good idea to
 * avoid changing this class manually, since it will be overwritten next time code is
 * regenerated. If you need to make any customizations, put them in a subclass.
 */
public abstract class _BuddyManager extends PersistentObject {

    public static final String DAYINWEEK_PKID_PROPERTY = "dayinweekPkid";
    public static final String JDOCLASS_PROPERTY = "jdoclass";
    public static final String JDOVERSION_PROPERTY = "jdoversion";
    public static final String WHICHSHIFT_PKID_PROPERTY = "whichshiftPkid";
    public static final String WORKER_JDOID_PROPERTY = "workerJdoid";
    public static final String ACTUAL_DAY_IN_WEEK_PROPERTY = "actualDayInWeek";
    public static final String ACTUAL_WHICH_SHIFT_PROPERTY = "actualWhichShift";
    public static final String ACTUAL_WORKER_PROPERTY = "actualWorker";

    protected Integer dayinweekPkid;
    protected String jdoclass;
    protected Integer jdoversion;
    protected Integer whichshiftPkid;
    protected Long workerJdoid;
    protected ValueHolder actualDayInWeek;
    protected ValueHolder actualWhichShift;
    protected ValueHolder actualWorker;

    public Integer getDayinweekPkid() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "dayinweekPkid", false);
        }

        return dayinweekPkid;
    }
    public void setDayinweekPkid(Integer dayinweekPkid) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "dayinweekPkid", false);
        }

        Object oldValue = this.dayinweekPkid;
        this.dayinweekPkid = dayinweekPkid;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "dayinweekPkid", oldValue, dayinweekPkid);
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

    public Integer getWhichshiftPkid() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "whichshiftPkid", false);
        }

        return whichshiftPkid;
    }
    public void setWhichshiftPkid(Integer whichshiftPkid) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "whichshiftPkid", false);
        }

        Object oldValue = this.whichshiftPkid;
        this.whichshiftPkid = whichshiftPkid;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "whichshiftPkid", oldValue, whichshiftPkid);
        }
    }

    public Long getWorkerJdoid() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "workerJdoid", false);
        }

        return workerJdoid;
    }
    public void setWorkerJdoid(Long workerJdoid) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "workerJdoid", false);
        }

        Object oldValue = this.workerJdoid;
        this.workerJdoid = workerJdoid;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "workerJdoid", oldValue, workerJdoid);
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

    public Worker getActualWorker() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualWorker", true);
        }

        return (Worker) actualWorker.getValue();
    }
    public void setActualWorker(Worker actualWorker) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "actualWorker", true);
        }

        this.actualWorker.setValue(actualWorker);
    }

}
