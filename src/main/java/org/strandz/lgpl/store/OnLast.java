package org.strandz.lgpl.store;

public enum OnLast
{
    //INCREASE,
    //DECREASE,
    //NOTHING,
    //YOU_DECIDE,
    /**
     * When picking out one by one, without replacement
     */
    FROM_LAST,
    /**
     * Useful when just want any value from the sequence
     */
    RANDOM,
}
