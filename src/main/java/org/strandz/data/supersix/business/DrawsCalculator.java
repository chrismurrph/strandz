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

import org.strandz.data.supersix.domain.SuperSixDomainLookupEnum;
import org.strandz.data.supersix.objects.Combination;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.Game;
import org.strandz.data.supersix.objects.KickOffTime;
import org.strandz.data.supersix.objects.Meet;
import org.strandz.data.supersix.objects.Pitch;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.ValidationException;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DrawsCalculator
{
    private CompetitionSeason competitionSeason;
    private DataStore ds;
    private List pitches;
    private List kickOffTimes;
    private SuperSixManagerI superSixManager;
    private SuperSixLookups superSixLookups;
    
    DrawsCalculator( DataStore ds, CompetitionSeason competitionSeason, SuperSixManagerI superSixManager, SuperSixLookups superSixLookups)
    {
        this.ds = ds;
        this.competitionSeason = competitionSeason;
        this.superSixLookups = superSixLookups;
        //Note that these will be cached in the JDO situation
        pitches = superSixLookups.get( SuperSixDomainLookupEnum.PITCH_CLOSED); 
                //ds.getDomainQueries().executeRetList(SuperSixDomainQueryEnum.PITCH_CLOSED);
        kickOffTimes = superSixLookups.get( SuperSixDomainLookupEnum.KICK_OFF_TIME_CLOSED);
                //ds.getDomainQueries().executeRetList(SuperSixDomainQueryEnum.KICK_OFF_TIME_CLOSED);
        this.superSixManager = superSixManager;
    }
    
    void calculateDraws( DayInWeek matchNight) throws ValidationException
    {
        //DayInWeek matchNight = competitionSeason.getNightPlayGames();
        if(matchNight == null)
        {
            throw new ValidationException( "Do not which on which nights games are played for " + competitionSeason);
        }
        Date startDate = competitionSeason.getStartDate();
        if(startDate == null)
        {
            throw new ValidationException( "Do not know the Start Date for " + competitionSeason);
        }
        Date endDate = competitionSeason.getEndDate();
        if(endDate == null)
        {
            throw new ValidationException( "Do not know the End Date for " + competitionSeason);
        }
        List divisions = superSixLookups.get( SuperSixDomainLookupEnum.DIVISION);
        //(ds.getDomainQueries()).executeRetList(SuperSixDomainQueryEnum.DIVISION_CLOSED);
        int day = DayInWeek.toOrdinal( matchNight);
        Date firstMatch = TimeUtils.getNextDayFromDate( startDate, day);
        Date fromDate;
        //Date today = new Date();
        //if(today.getTime() <= firstMatch.getTime())
        {
            fromDate = firstMatch;
        }
        //else
        //{
        //    fromDate = TimeUtils.getNextDayFromToday( day); 
        //}
        Date toDate = TimeUtils.getPreviousDayFromDate( endDate, day);
        Err.pr( "To calc draws from " + fromDate + " to " + toDate);        
        for(Iterator iterator = divisions.iterator(); iterator.hasNext();)
        {
            Division division = (Division) iterator.next();
            calculateDraws( division, fromDate, toDate);
        }
    }
    
    private static void printDraws( List teamsInDivision)
    {
        for(Iterator iterator = teamsInDivision.iterator(); iterator.hasNext();)
        {
            TeamCompetitionSeason team = (TeamCompetitionSeason)iterator.next();
            Err.pr( "Team " + team + " will play " + team.getSlots().size() + " times");
        }
    }

    /**
     * NO Start is first match night after today (or firstMatch if today is before firstMatch)
     * YES Start is firstMatch
     * Loop thru each week (create Meet if doesn't exist), 
     *  thru each kick off time and thru each Pitch (create Match if doesn't exist)
     * Keep going until week is after lastMatch
     * Use GameReckoner to put teams into matches (check right teams are in already done matches) 
     */
    private void calculateDraws( Division division, Date fromDate, Date toDate)
    {
        CompetitionSeason competitionSeason = SuperSixManagerUtils.getCurrentCompetitionSeason();        
        List teamsInDivision = superSixManager.getCurrentTeams( competitionSeason.getSeasonYear(), competitionSeason.getCompetition(), division);
        if(teamsInDivision.size() > 1)
        {
            List<Combination> combinations = ReckonerUtils.findCombinationsFromTeams( teamsInDivision);
            GameReckoner gameReckoner = new GameReckoner( combinations, teamsInDivision.size());
            printDraws( teamsInDivision);
            Date dateToIncr = new Date( fromDate.getTime());
            for(int meetNum = 0; dateToIncr.getTime() <= toDate.getTime(); dateToIncr = TimeUtils.addDays( dateToIncr, 7), meetNum++)
            {
                Err.pr( "----looking at date: " + dateToIncr);
                Meet meet = findMeetAt( dateToIncr);
                if(meet == null)
                {
                    Meet newMeet = new Meet();
                    newMeet.setDate( new Date(dateToIncr.getTime()));
                    newMeet.setOrdinal( meetNum +1);
                    competitionSeason.addMeet( newMeet);
                    //We don't have to do this because we are on a detail relationship 
                    //((EntityManagedDataStore)ds).getEM().registerPersistent( newMeet);
                    meet = newMeet;
                }
                for(int i = 0; i < pitches.size(); i++)
                {
                    Pitch pitch = (Pitch) pitches.get(i);
                    for(int j = 0; j < kickOffTimes.size(); j++)
                    {
                        KickOffTime kickOffTime = (KickOffTime) kickOffTimes.get(j);
                        Game game = findMatch( meet, pitch, kickOffTime);
                        Game newGame = null;
                        if(game == null)
                        {
                            newGame = new Game();
                            newGame.setKickOffTime( kickOffTime);
                            newGame.setPitch( pitch);
                            newGame.setDivision( division);
                            meet.addMatch( newGame);
                            ((EntityManagedDataStore)ds).getEM().registerPersistent( newGame);
                            game = newGame;
                        }
                        Err.error( "Two concepts don't work together - we s/be picking combinations and then assigning them to resouces");
                        Combination combination = gameReckoner.pickNextCombination( meet.getOrdinal());
                        if(newGame == null)
                        {
                            Err.pr( "Match already exists: " + game);
                            Err.pr( "Retrospectively predicted: " + combination);
                            Err.pr( "");
                        }
                        else
                        {
                            Err.pr( "Just created a match, need to fill with: " + combination + " at " + kickOffTime + " on " + pitch);
                            newGame.setTeamOne( (TeamCompetitionSeason)combination.getTeam1());
                            newGame.setTeamTwo( (TeamCompetitionSeason)combination.getTeam2());
                        }
                    }
                }
                Err.pr( "----pitches and kick off times exhausted for date: " + dateToIncr);
            }
            Err.pr( "DONE for division " + division);
        }
    }
    
    private Game findMatch( Meet queryMeet, Pitch queryPitch, KickOffTime queryKickOffTime)
    {
        Game result = null;
        List meets = competitionSeason.getMeets();
        for(Iterator iterator = meets.iterator(); iterator.hasNext();)
        {
            Meet meet = (Meet) iterator.next();
            if(meet == queryMeet)
            {
                List matches = meet.getGames();
                for(Iterator iterator1 = matches.iterator(); iterator1.hasNext();)
                {
                    Game game = (Game) iterator1.next();
                    if(game.getKickOffTime() == queryKickOffTime && game.getPitch() == queryPitch)
                    {
                        result = game;
                        break;
                    }
                }
            }
        }
        return result;
    }
    
    private Meet findMeetAt( Date date)
    {
        Meet result = null;
        List meets = competitionSeason.getMeets();
        for(Iterator iterator = meets.iterator(); iterator.hasNext();)
        {
            Meet meet = (Meet) iterator.next();
            if(meet.getDate().equals( date))
            {
                result = meet;
                break;
            }
        }
        return result;
    }
}
