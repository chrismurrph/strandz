/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.util;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Ideal for going thru a list randomly, but with determinate results (ie always same from same seed) 
 */
public class SequenceGenerator
{
    private Random random;
    private int sequenceLength;
    private int timesThru;
    private List<Integer> alreadyUsed = new ArrayList<Integer>();

    /**
     * 
     * @param seed Use to get a different arrangement of the returned numbers
     * @param sequenceLength If 20 will return a list that includes 0 and 19, and all inbetween
     */
    public SequenceGenerator( int seed, int sequenceLength)
    {
        random = new Random( seed);
        this.sequenceLength = sequenceLength;
    }
    
    public Integer next()
    {
        Integer result;
        if(timesThru == sequenceLength)
        {
            result = null;
        }
        else
        {
            result = grabWithoutReplacement();
            timesThru++;
        }
        return result;
    }
    
    private Integer grabWithReplacement()
    {
        Integer result;
        Integer next = random.nextInt();
        result = Utils.mod( next, sequenceLength);
        return result;
    }

    private Integer grabWithoutReplacement()
    {
        Integer result;
        Integer perhaps = grabWithReplacement();
        while(true)
        {
            if(!alreadyUsed.contains( perhaps))
            {
                alreadyUsed.add( perhaps);
                break;
            }
            else
            {
                perhaps = grabWithReplacement();
            }
        }
        result = perhaps;
        return result;
    }

    public int getTimesThru()
    {
        return timesThru;
    }

    public static void main(String[] args)
    {
        int size = 20;
        int seed = 0;
        SequenceGenerator sequenceGenerator = new SequenceGenerator( seed, size);
        Integer index;
        while( (index = sequenceGenerator.next()) != null)
        {
            Err.pr( index);
        }
    }
}
