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
package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.data.objects.Performance;
import org.strandz.lgpl.note.SdzNote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Scorecard implements Comparable
{
    private List<BigDecimal> thresholds;
    /**
     * Always go from good to bad, so if values are rising, then lowValues are good
     */
    private boolean lowValuesGood;
    /**
     * Last time ascertainPerformance() was called
     */
    private Performance performance;
    
    private static int constructedTimes;
    private int id;

    /**
     * 
     * @param thresholds Start with good, going to bad - so will either be going up or down
     */
    private Scorecard( List<BigDecimal> thresholds)
    {
        constructedTimes++;
        id = constructedTimes;
        if(thresholds.size() >= 2)
        {
            boolean goingDown =
                    Utils.greaterThan( thresholds.get(0), thresholds.get(1));
                    //&& Utils.greaterThan( thresholds.get(1), thresholds.get(2)
                    //);
            boolean goingUp =
                    Utils.lessThan( thresholds.get(0), thresholds.get(1));
                    //&& Utils.lessThan( thresholds.get(1), thresholds.get(2)
                    //);
            Assert.isTrue( (goingUp || goingDown) && !(goingUp && goingDown), 
                "Muddled thresholds - they must be always going either up or down - goingUp: " + 
                    goingUp + ", goingDown: " + goingDown + ", thresholds: <" + thresholds + ">");
            /*
             * TODO
             * Need to check that they are increasing
             */
            this.lowValuesGood = goingUp;
            this.thresholds = thresholds;
            if(id == 0)
            {
                Err.stack();
            }
        }
        else
        {
            Err.pr( "thresholds is " + thresholds);
            Err.error( "thresholds size is " + thresholds.size());
        }
    }

    public static Scorecard newInstance( String asString)
    {
        Scorecard result = null;
        List<BigDecimal> thresholds = new ArrayList<BigDecimal>();
        String numbers[] = asString.split( ",");
        for (int i = 0; i < numbers.length; i++)
        {
            String numberStr = numbers[i];
            thresholds.add( new BigDecimal( numberStr.trim()));
        }
        result = new Scorecard( thresholds);
        return result;
    }

    public static Scorecard newInstance( int good, int bad)
    {
        List<BigDecimal> thresholds = new ArrayList<BigDecimal>();
        thresholds.add( new BigDecimal( good));
        //thresholds.add( new BigDecimal( ok));
        thresholds.add( new BigDecimal( bad));
        return new Scorecard( thresholds);
    }

    public static Scorecard newInstance( String good, String bad)
    {
        List<BigDecimal> thresholds = new ArrayList<BigDecimal>();
        thresholds.add( new BigDecimal( good));
        //thresholds.add( new BigDecimal( ok));
        thresholds.add( new BigDecimal( bad));
        return new Scorecard( thresholds);
    }
    
    public static Scorecard newInstance( BigDecimal good, BigDecimal bad)
    {
        List<BigDecimal> thresholds = new ArrayList<BigDecimal>();
        thresholds.add( good);
        //thresholds.add( ok);
        thresholds.add( bad);
        return new Scorecard( thresholds);
    }

    public static Scorecard newInstance( List<BigDecimal> thresholds)
    {
        return new Scorecard( thresholds);
    }

    public Performance ascertainPerformance( BigDecimal value)
    {
        Performance result = Performance.BAD;
        for(int i = 0; i < thresholds.size(); i++)
        {
            BigDecimal threshold = thresholds.get(i);
            if(lowValuesGood)
            {
                if(Utils.lessThan( value, threshold))
                {
                    result = Performance.GOOD_TO_BAD[i];
                    break;
                }
            }
            else
            {
                if(Utils.greaterThan( value, threshold))
                {
                    result = Performance.GOOD_TO_BAD[i];
                    break;
                }
            }
        }    
        //Err.pr( "What of " + value + ": " + result);
        //Graphs done again just when refocus
        //Assert.isNull( performance, "One use scorecard for now");
        performance = result;
        Err.pr( SdzNote.SCORECARD, "performance set on scorecard with ID: " + id);
        //Err.stack();
        return result;
    }
    
    public void setPerformanceNull()
    {
        performance = null;
    }
    
    public Performance getPerformance()
    {
        //Assert.notNull
        //        ( performance,
        if(performance == null)
        {
            Err.pr( SdzNote.SCORECARD,
                      "null result from getPerformance() on scorecard with ID: " + id + 
                    ", thresholds: " + thresholds);
        }
        return performance;
    }
    
    public String toString()
    {
        return "perf: " + performance + ", thresholds: " + thresholds;
    }

    /**
     * Returns a comma separated version for storage in the DB
     */
    public String asString()
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < thresholds.size(); i++)
        {
            BigDecimal bigDecimal = thresholds.get(i);
            result.append( bigDecimal.toString());
            if(i < thresholds.size()-1)
            {
                result.append( ",");
            }
        }
        return result.toString();
    }

    public int compareTo( Object o)
    {
        int result;
        Scorecard thisScorecard = this;
        Scorecard otherScorecard = (Scorecard)o;
        if(thisScorecard.getPerformance() == null)
        {
            Err.error( "What is the point of comparing a scorecard to another if performance not been set");
        }
        result = Utils.compareTo( thisScorecard.getPerformance(), otherScorecard.getPerformance());
        return result;
    }

    public List<BigDecimal> getThresholds()
    {
        return thresholds;
    }

    public static void main( String[] args)
    {
        String asString = "3.1,4,7.55 , 9";
        Scorecard scorecard = Scorecard.newInstance( asString);
        Err.pr( "scorecard is " + scorecard);
        Err.pr( "scorecard asString() is <" + scorecard.asString() + ">");
    }
}
