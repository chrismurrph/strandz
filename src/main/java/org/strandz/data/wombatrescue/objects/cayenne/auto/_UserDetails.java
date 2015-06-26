package org.strandz.data.wombatrescue.objects.cayenne.auto;

import org.apache.cayenne.CayenneDataObject;

/**
 * Class _UserDetails was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _UserDetails extends CayenneDataObject {

    public static final String DATABASEPASSWORD_PROPERTY = "databasepassword";
    public static final String DATABASEUSERNAME_PROPERTY = "databaseusername";
    public static final String EMAILADDRESS_PROPERTY = "emailaddress";
    public static final String JDOCLASS_PROPERTY = "jdoclass";
    public static final String JDOVERSION_PROPERTY = "jdoversion";
    public static final String PASSWORD_PROPERTY = "password";
    public static final String READONLY_PROPERTY = "readonly";
    public static final String USERNAME_PROPERTY = "username";

    public static final String JDOID_PK_COLUMN = "JDOID";

    public void setDatabasepassword(String databasepassword) {
        writeProperty("databasepassword", databasepassword);
    }
    public String getDatabasepassword() {
        return (String)readProperty("databasepassword");
    }

    public void setDatabaseusername(String databaseusername) {
        writeProperty("databaseusername", databaseusername);
    }
    public String getDatabaseusername() {
        return (String)readProperty("databaseusername");
    }

    public void setEmailaddress(String emailaddress) {
        writeProperty("emailaddress", emailaddress);
    }
    public String getEmailaddress() {
        return (String)readProperty("emailaddress");
    }

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

    public void setPassword(String password) {
        writeProperty("password", password);
    }
    public String getPassword() {
        return (String)readProperty("password");
    }

    public void setReadonly(Byte readonly) {
        writeProperty("readonly", readonly);
    }
    public Byte getReadonly() {
        return (Byte)readProperty("readonly");
    }

    public void setUsername(String username) {
        writeProperty("username", username);
    }
    public String getUsername() {
        return (String)readProperty("username");
    }

}
