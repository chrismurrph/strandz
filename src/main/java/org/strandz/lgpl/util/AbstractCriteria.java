package org.strandz.lgpl.util;

/**
 * User: Chris
 * Date: 22/02/2009
 * Time: 12:01:40 AM
 */
public class AbstractCriteria implements CriteriaI, IdentifierI
{
    /**
     * The essential text we want to use as part of seeing if the criteria
     * is passed. It might even be code.
     */
    private String baseName;
    private CriteriaEnum criteriaEnum;

    public static AbstractCriteria NULL;

    static
    {
        NULL = new AbstractCriteria( "");
        NULL.setCriteriaEnum( CriteriaEnum.NULL);
    }

    private static int constructedTimes;
    private int id;

    public static CriteriaI newInstance()
    {
        return new AbstractCriteria();
    }

    public AbstractCriteria()
    {
        init();
    }

    public AbstractCriteria( String baseName)
    {
        this.baseName = baseName;
        init();
    }

    private void init()
    {
        constructedTimes++;
        id = constructedTimes;
        if(id == 1)
        {
            //Err.stack();
        }
    }

    public boolean meetsCriteria( String name)
    {
        criteriaEnum.setBaseName( baseName);
        return criteriaEnum.meetsCriteria( name);
    }

    public CriteriaEnum getCriteriaEnum()
    {
        return criteriaEnum;
    }

    public void setCriteriaEnum(CriteriaEnum criteriaEnum)
    {
        //Err.pr( "criteriaEnum being set to <" + criteriaEnum + "> in ID: " + id);
        this.criteriaEnum = criteriaEnum;
    }

    public String getBaseName()
    {
        return baseName;
    }

    public void setBaseName( String baseName)
    {
        this.baseName = baseName;        
    }

    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return baseName;
    }
    public String toString()
    {
        return getName();
    }
}
