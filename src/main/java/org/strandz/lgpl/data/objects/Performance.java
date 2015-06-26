package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Utils;

public class Performance implements Comparable
{
    public int ordinal;
    private String name;

    private Performance( String name, int ordinal)
    {
        this.name = name;
        this.ordinal = ordinal;
    }
    
    public String toString()
    {
        return name;
    }
    
    public int compareTo(Object obj)
    {
        return Utils.relativeRank( GOOD_TO_BAD, this, obj);
    }    

    public static final Performance GOOD = new Performance( "Good", 0);
    public static final Performance OK = new Performance( "Ok", 1);
    public static final Performance BAD = new Performance( "Bad", 2);

    public static final Performance[] GOOD_TO_BAD = {GOOD, OK, BAD};
    public static final Performance[] BAD_TO_GOOD = {BAD, OK, GOOD};
}
