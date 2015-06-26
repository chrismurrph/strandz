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
package org.strandz.data.supersix.business;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.data.supersix.objects.Slot;
import org.strandz.data.supersix.objects.Combination;
import org.strandz.data.supersix.objects.Round;

import java.util.List;
import java.util.ArrayList;

/**
 * Given a list of teams this class will be able to tell you which
 * game will be the next one to be played, so that there is as much
 * diversity of play as possible. Fairness is reached when everyone
 * has played the same number of games. This is what a round is. Only
 * when at fairness can a league table be produced. 
 * 
 * Some teams may reject the draw they are given (for instance
 * because it is to be played early). Thus another combination will
 * have to fill in for them. Without this issue the order in which
 * combinations are returned using pickNextCombination() would always
 * be the same. Thus it would be unfair to always use the next combination
 * - the same two teams would be picked on all the time. To get who comes 
 * next a FillInReckoner is used. 
 * 
 * A round will usually be completed at a particular meet - we will just
 * add more KickOffTimes to ensure that that happens. The number of
 * KickOffTimes may even have to be varied 
 */
public class GameReckoner
{
    /**
     * All combinations are less than half of the matrix because the order
     * in the match is not important, and the matrix squares where you
     * play yourself do not exist.
     */
    private List<Combination> combinations = new ArrayList<Combination>();
    /**
     * 
     */
    private List<Round> rounds = new ArrayList<Round>();
    /**
     * What is the least number of instances of a combination
     */
    private int numTeams;
    private int minTimesPlay = 0;

    /**
     * Fairness is reached when everyone has played 
     * each other the same number of times. It will
     * be turned on and off as pickNextCombination()
     * is called. The last match should be at a time
     * when fairness has been reached.
     */
    //private boolean fairnessReached;

    GameReckoner( List<Combination> combinations, int numTeams)
    {
        this.combinations = combinations;
        //Print.prList( combinations, "All Combinations");
        this.numTeams = numTeams;
        formRounds();
        printRounds();
    }

    private boolean canCombinationBeUsed( Combination combination, boolean acceptSecond)
    {
        boolean result = false;
        if(acceptSecond)
        {
            if(combination.getRoundsPlayedIn().size() == 1)
            {
                result = true;
            }
        }
        else if(combination.getRoundsPlayedIn().size() == 0)
        {
            result = true;
        }
        return result;
    }

    private void formRounds()
    {
        boolean incompleteRoundOk = true;
        Round round = null;
        int totalSlots = 0;
        Err.pr( "Num combinations need to play: " + combinations.size());
        int combinationAlreadyPlayed = 0;
        while(combinationAlreadyPlayed < combinations.size())
        {
            int cantPlayTwiceInARound = 0;
            combinationAlreadyPlayed = 0;
            for(int i = 0; i < combinations.size(); i++)
            {
                Combination combination = combinations.get(i);
                if(canCombinationBeUsed( combination, round != null && round.acceptSecond))
                {
                    round = getCurrentRound( round);
                    if(round.isFilled())
                    {
                        break;
                    }
                    if(!round.teamAlreadyExistsIn( combination))
                    {
                        round.addSlot( new Slot( combination));
                        totalSlots++;
                    }
                    else
                    {
                        cantPlayTwiceInARound++;
                    }
                }
                else
                {
                    combinationAlreadyPlayed++;
                }
                if((cantPlayTwiceInARound + combinationAlreadyPlayed) == combinations.size())
                {
                    /*
                     * None of the combinations have been able to be used, so we must either
                     * go to a new round, or allow teams to play each other more than once
                     * during a season. 
                     */
                    if(combinationAlreadyPlayed < combinations.size())
                    {
                        if(incompleteRoundOk || round.acceptSecond)
                        {
                            round.acceptSecond = false;
                            round = new Round( numTeams, round.getOrdinal()+1);
                            rounds.add( round);
                            Err.pr( "Cant play > once: " + cantPlayTwiceInARound);
                            Err.pr( "Combin already played: " + combinationAlreadyPlayed);
                            Err.pr( "All unplayed, so advanced to next round - " + round);
                        }
                        else if(!round.acceptSecond)
                        {
                            round.acceptSecond = true;
                            //break;
                        }
                    }
                    else
                    {
                        //We are going to break out of the outer loop anyway so no point
                        //in doing a new round
                    }
                }
            }
            //Err.pr( "At round " + round + " the num of unplayed combinations is " + unplayedCombinations);
        }
    }

    private void printRounds()
    {
        Print.prList( rounds, "Rounds");
    }

    private Round getCurrentRound( Round round)
    {
        Round result;
        if(round == null || round.isFilled())
        {
            int ordinal;
            if(round != null)
            {
                ordinal = round.getOrdinal()+1;
            }
            else
            {
                ordinal = 1;
            }
            result = new Round( numTeams, ordinal);
            rounds.add( result);
        }
        else
        {
            result = round;
        }
        return result;
    }

    public Combination pickNextCombination(int roundNumber)
    {
        Combination result = null;
        Round round = rounds.get( roundNumber-1);
        Slot slot = pickSlot( round);
        if(slot != null)
        {
            result = slot.getCombination();
        }
        return result;
    }

    private Slot pickSlot( Round round)
    {
        Slot result = null;
        for(int i = 0; i < round.getSlots().size(); i++)
        {
            Slot slot = (Slot) round.getSlots().get(i);
            if(!slot.isPicked())
            {
                slot.setPicked( true);
                result = slot;
                break;
            }
        }
        return result;
    }

    /*
    public Combination pickNextCombination()
    {
        Combination result = null;
        for(int i = 0; i < combinations.size(); i++)
        {
            Combination combination = combinations.get(i);
            if(combination.timesPlayed == minTimesPlay)
            {
                if(i == combinations.size()-1)
                {
                    minTimesPlay++;
                }
                combination.timesPlayed++;
                fairnessReached = allCombinationsHavePlayedAnEqualNumberOfTimes();
                if(fairnessReached)
                {
                    Err.pr( "Fairness Reached");
                }
                result = combination;
                break;
            }
        }
        return result;
    }
    */

//    private boolean allCombinationsPlayed()
//    {
//        boolean result = true;
//        for(Iterator iterator = combinations.iterator(); iterator.hasNext();)
//        {
//            Combination combination = (Combination) iterator.next();
//            if(combination.getRoundPlayedIn() == null)
//            {
//                result = false;
//                Err.pr( "combination " + combination + " has not yet been played");
//                break;
//            }
//        }
//        return result;
//    }

    /**
     * This simulates looking at the matrix and seeing that all the numbers
     * are equal. This is actually unnecessary. As there is a strict order in
     * which combinations are returned, we can use maths to say every n calls
     * to pickNextCombination() will have the competition in a fair state.
     * If there were 5 teams then every 4+3+2+1 (==10) calls the competition
     * would be in a fair state. This is the same as the number of combinations. 
     * Could this fairness point be the real definition of a round in soccer?
     * (Have renamed the entity Round to Meet just in case)
     */
    /*
    private boolean allCombinationsHavePlayedAnEqualNumberOfTimes()
    {
        boolean result = true;
        int timesFirstPlayed = Utils.UNSET_INT;
        if(!combinations.isEmpty())
        {
            timesFirstPlayed = combinations.get( 0).timesPlayed; 
        }
        for(Iterator iterator = combinations.iterator(); iterator.hasNext();)
        {
            Combination combination = (Combination) iterator.next();
            if(combination.timesPlayed != timesFirstPlayed)
            {
                result = false;
                break;
            }
        }
        if(result)
        {
            Err.pr( "All combinations have played " + timesFirstPlayed + " times");
        }
        return result;
    }
    */

    /**
     * Intended to be called after pickNextCombination().  
     */
//    public boolean isFairnessReached()
//    {
//        return fairnessReached;
//    }

    public static void main( String args[])
    {
        Object teams[] = new Object[]{ "One", "Two", "Three",};
        List combinations = ReckonerUtils.findCombinationsFromTeams( Utils.asArrayList( teams));
        GameReckoner gameReckoner = new GameReckoner( combinations, teams.length);
//        Err.pr( "From " + teams.length + " num of combinations got is " + gameReckoner.combinations.size());
//        Print.prList( gameReckoner.combinations, "possible combinations");
//        for(int i = 0; i < 15; i++)
//        {
//            Combination combination = gameReckoner.pickNextCombination();
//            Err.pr( combination);
//        }
    }
}
