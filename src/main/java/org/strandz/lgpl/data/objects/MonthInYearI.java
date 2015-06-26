package org.strandz.lgpl.data.objects;

/**
 * User: Chris
 * Date: 31/08/2008
 * Time: 11:53:59
 */
public interface MonthInYearI extends Comparable
{
    String getName();
    Integer getOrdinal();
    public boolean equals( Object obj);
}