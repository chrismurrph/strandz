package org.strandz.data.wombatrescue.objects.cayenne.auto;

import org.apache.cayenne.CayenneDataObject;

/**
 * Class _Seniority was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Seniority extends CayenneDataObject {

    public static final String JDOCLASS_PROPERTY = "jdoclass";
    public static final String JDOVERSION_PROPERTY = "jdoversion";
    public static final String NAME_PROPERTY = "name";

    public static final String PKID_PK_COLUMN = "PKID";

    public void setJdoclass(String jdoclass) {
        writeProperty("jdoclass", jdoclass);
    }
    public String getJdoclass() {
        return (String)readProperty("jdoclass");
    }

    public void setJdoversion(Integer jdoversion) {
        writeProperty("jdoversion", jdoversion);
    }
    public Integer getJdoversion() {
        return (Integer)readProperty("jdoversion");
    }

    public void setName(String name) {
        writeProperty("name", name);
    }
    public String getName() {
        return (String)readProperty("name");
    }

}
