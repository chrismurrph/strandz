package org.strandz.lgpl.util;

/**
 * User: Chris
 * Date: 06/03/2009
 * Time: 2:36:03 AM
 */
public interface CriteriaI
{
    boolean meetsCriteria( String name);
    String getBaseName();
    void setBaseName( String baseName);
    CriteriaEnum getCriteriaEnum();
    void setCriteriaEnum( CriteriaEnum criteriaEnum);
}
