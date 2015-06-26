package org.strandz.data.wombatrescue.objects;

/**
 * User: Chris
 * Date: 31/08/2008
 * Time: 11:53:59
 */
public interface NumDaysIntervalI extends Comparable
{
    public String getName();
    public int getDays();
    public boolean equals( Object obj);
}