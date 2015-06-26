package org.strandz.lgpl.util;

/**
 * User: Chris
 * Date: 11/01/2009
 * Time: 11:14:41 PM
 */
public class StringIdentifier implements IdentifierI
{
    private static int constructedTimes;
    private int id;
    private String name;

    public StringIdentifier( String name)
    {
        constructedTimes++;
        id = constructedTimes;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return "<" + name + "> ID: " + getId();
    }
}
