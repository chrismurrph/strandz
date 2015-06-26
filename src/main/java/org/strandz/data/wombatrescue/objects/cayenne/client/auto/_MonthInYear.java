package org.strandz.data.wombatrescue.objects.cayenne.client.auto;

import org.apache.cayenne.PersistentObject;

/**
 * A generated persistent class mapped as "MonthInYear" Cayenne entity. It is a good idea to
 * avoid changing this class manually, since it will be overwritten next time code is
 * regenerated. If you need to make any customizations, put them in a subclass.
 */
public abstract class _MonthInYear extends PersistentObject {

    public static final String JDOCLASS_PROPERTY = "jdoclass";
    public static final String JDOVERSION_PROPERTY = "jdoversion";
    public static final String NAME_PROPERTY = "name";
    public static final String ORDINAL_PROPERTY = "ordinal";

    protected String jdoclass;
    protected Integer jdoversion;
    protected String name;
    protected Integer ordinal;

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

    public String getName() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "name", false);
        }

        return name;
    }
    public void setName(String name) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "name", false);
        }

        Object oldValue = this.name;
        this.name = name;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "name", oldValue, name);
        }
    }

    public Integer getOrdinal() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "ordinal", false);
        }

        return ordinal;
    }
    public void setOrdinal(Integer ordinal) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "ordinal", false);
        }

        Object oldValue = this.ordinal;
        this.ordinal = ordinal;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "ordinal", oldValue, ordinal);
        }
    }

}
