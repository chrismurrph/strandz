package org.strandz.data.wombatrescue.objects.cayenne.client.auto;

import org.apache.cayenne.PersistentObject;

/**
 * A generated persistent class mapped as "UserDetails" Cayenne entity. It is a good idea to
 * avoid changing this class manually, since it will be overwritten next time code is
 * regenerated. If you need to make any customizations, put them in a subclass.
 */
public abstract class _UserDetails extends PersistentObject {

    public static final String DATABASEPASSWORD_PROPERTY = "databasepassword";
    public static final String DATABASEUSERNAME_PROPERTY = "databaseusername";
    public static final String EMAILADDRESS_PROPERTY = "emailaddress";
    public static final String JDOCLASS_PROPERTY = "jdoclass";
    public static final String JDOVERSION_PROPERTY = "jdoversion";
    public static final String PASSWORD_PROPERTY = "password";
    public static final String READONLY_PROPERTY = "readonly";
    public static final String USERNAME_PROPERTY = "username";

    protected String databasepassword;
    protected String databaseusername;
    protected String emailaddress;
    protected String jdoclass;
    protected Integer jdoversion;
    protected String password;
    protected Byte readonly;
    protected String username;

    public String getDatabasepassword() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "databasepassword", false);
        }

        return databasepassword;
    }
    public void setDatabasepassword(String databasepassword) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "databasepassword", false);
        }

        Object oldValue = this.databasepassword;
        this.databasepassword = databasepassword;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "databasepassword", oldValue, databasepassword);
        }
    }

    public String getDatabaseusername() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "databaseusername", false);
        }

        return databaseusername;
    }
    public void setDatabaseusername(String databaseusername) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "databaseusername", false);
        }

        Object oldValue = this.databaseusername;
        this.databaseusername = databaseusername;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "databaseusername", oldValue, databaseusername);
        }
    }

    public String getEmailaddress() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "emailaddress", false);
        }

        return emailaddress;
    }
    public void setEmailaddress(String emailaddress) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "emailaddress", false);
        }

        Object oldValue = this.emailaddress;
        this.emailaddress = emailaddress;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "emailaddress", oldValue, emailaddress);
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

    public String getPassword() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "password", false);
        }

        return password;
    }
    public void setPassword(String password) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "password", false);
        }

        Object oldValue = this.password;
        this.password = password;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "password", oldValue, password);
        }
    }

    public Byte getReadonly() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "readonly", false);
        }

        return readonly;
    }
    public void setReadonly(Byte readonly) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "readonly", false);
        }

        Object oldValue = this.readonly;
        this.readonly = readonly;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "readonly", oldValue, readonly);
        }
    }

    public String getUsername() {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "username", false);
        }

        return username;
    }
    public void setUsername(String username) {
        if(objectContext != null) {
            objectContext.prepareForAccess(this, "username", false);
        }

        Object oldValue = this.username;
        this.username = username;

        // notify objectContext about simple property change
        if(objectContext != null) {
            objectContext.propertyChanged(this, "username", oldValue, username);
        }
    }

}
