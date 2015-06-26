package org.strandz.lgpl.util;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * When you come across a huge list of Strings you may want to
 * divide them up into alphabetical clumps.
 *
 * User: Chris
 * Date: 12/08/2009
 * Time: 9:20:43 AM
 */
public class AlphabeticalDivider
{
    private List<List<String>> clumps = new ArrayList<List<String>>();

    /**
     * May add a comparator to this constructor later
     */
    public AlphabeticalDivider( List<String> bigList, int clumpSize)
    {
        Collections.sort( bigList);
        List<String> currentClump = new ArrayList<String>();
        for (int i = 0; i < bigList.size(); i++)
        {
            String str = bigList.get(i);
            if(currentClump.size() == clumpSize-1) //about to fill
            {
                currentClump.add( str);
                clumps.add( currentClump);
                currentClump = new ArrayList<String>();
            }
            else
            {
                currentClump.add( str);
            }
        }
        if(!currentClump.isEmpty())
        {
            clumps.add( currentClump);
        }
    }

    public int getClumpCount()
    {
        return clumps.size();
    }

    public List<String> getClump( int idx)
    {
        return clumps.get( idx);
    }

    public String getClumpTitle( int idx)
    {
        String result;
        List<String> clump = getClump( idx);
        result = clump.get( 0) + " - " + clump.get( clump.size()-1);
        return result;
    }

    public static void main( String[] args)
    {
        List<String> bigList = new ArrayList<String>();
        bigList.add( "one");
        bigList.add( "two");
        bigList.add( "three");
        bigList.add( "four");
        bigList.add( "five");
        bigList.add( "six");
        bigList.add( "seven");
        bigList.add( "eight");
        bigList.add( "nine");
        bigList.add( "ten");
        AlphabeticalDivider divider = new AlphabeticalDivider( bigList, 3);
        for (int i = 0; i < divider.getClumpCount(); i++)
        {
            List<String> clump = divider.getClump( i);
            String title = divider.getClumpTitle( i);
            Print.prList( clump, title);
        }
    }
}
