package org.strandz.data.wombatrescue.objects.cayenne.auto;

/** Class _WombatLookups was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _WombatLookups extends org.apache.cayenne.CayenneDataObject {

    public static final String JDOCLASS_PROPERTY = "jdoclass";
    public static final String JDOVERSION_PROPERTY = "jdoversion";

    public static final String JDOID_PK_COLUMN = "JDOID";

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
    
    
}