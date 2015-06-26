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

import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.Game;
import org.strandz.data.supersix.objects.KickOffTime;
import org.strandz.data.supersix.objects.Meet;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Reports
{
    public static String TAB = "  ";
    public static int WITHDRAWN_SCORE = Game.WITHDRAWN_SCORE;
    public static int UNKNOWN_SCORE = Game.UNKNOWN_SCORE;

    public static class MeetInfo implements Comparable
    {
        private int roundNum;
        public String roundDate;
        //public List menMatchInfos = new ArrayList();
        //public List womenMatchInfos = new ArrayList();
        //Done by division
        //public List matchInfos[] = new List[2];
        public Map<Division, List<MatchInfo>> matchInfos = new HashMap<Division, List<MatchInfo>>();

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            result.append("Round " + roundNum);
            result.append(", " + roundDate + FileUtils.DOCUMENT_SEPARATOR);
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result;
            MeetInfo other = (MeetInfo) o;
            result = Utils.compareTo(this.roundNum, other.roundNum);
            return result;
        }
    }

    public static class MatchInfo implements Comparable
    {
        public String team1;
        public String team2;
        private KickOffTime koTime;
        public String koTimeStr;
        public String pitch;
        public String score;

        public static int NAME_WIDTH = 19;
        public static int SMALL_WIDTH = 10;

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            result.append( Utils.rightPadSpace(team1, NAME_WIDTH) + TAB + Utils.rightPadSpace(team2, NAME_WIDTH));
            if(pitch == null)
            {
                pitch = "Pitch -";
            }
            String formattedPitchStr = Utils.rightPadSpace(pitch, SMALL_WIDTH);
            result.append(TAB + Utils.rightPadSpace(koTimeStr, SMALL_WIDTH) + TAB +
                    formattedPitchStr + TAB + Utils.rightPadSpace( score, SMALL_WIDTH));
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result;
            MatchInfo other = (MatchInfo) o;
            result = Utils.compareTo(this.koTime, other.koTime);
            return result;
        }
    }
    
    public class LeagueTableInfo implements Comparable
    {
        public Division division;
        public List<Reports.TeamPointsInfo> pointsInfos = new ArrayList<Reports.TeamPointsInfo>();
        
        private LeagueTableInfo( Division division) throws ValidationException
        {
            this.division = division;
            pointsInfos = findTeamPointsInfos( division);
        }
        
        public int compareTo(Object o)
        {
            int result;
            LeagueTableInfo other = (LeagueTableInfo) o;
            result = Utils.compareTo(this.division, other.division);
            return result;
        }

        public List<String> getTeamNames()
        {
            List<String> result = new ArrayList<String>();
            for(Iterator<TeamPointsInfo> iterator = pointsInfos.iterator(); iterator.hasNext();)
            {
                TeamPointsInfo teamPointsInfo = iterator.next();
                result.add( teamPointsInfo.team);
            }
            return result;
        }
    }

    public static class TeamPointsInfo implements Comparable
    {
        public String team;
        public String played;
        public String won;
        public String drawn;
        public String lost;
        public String goalsFor;
        public String goalsAgainst;
        public String goalDifferenceStr;
        private int goalDifference;
        public String totalPointsStr;
        private int totalPoints;

        public static int NAME_WIDTH = 19;
        //public static int SMALL_WIDTH = 5;
        public static int SMALLEST_WIDTH = 4;

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            result.append( Utils.leftPadSpace(team, NAME_WIDTH) + TAB);
            result.append( Utils.leftPadSpace(played, SMALLEST_WIDTH) + TAB);
            result.append( Utils.leftPadSpace(won, SMALLEST_WIDTH) + TAB);
            result.append( Utils.leftPadSpace(drawn, SMALLEST_WIDTH) + TAB);
            result.append( Utils.leftPadSpace(lost, SMALLEST_WIDTH) + TAB);
            result.append( Utils.leftPadSpace(goalsFor, SMALLEST_WIDTH) + TAB);
            result.append( Utils.leftPadSpace(goalsAgainst, SMALLEST_WIDTH) + TAB);
            result.append( Utils.leftPadSpace(goalDifferenceStr, SMALLEST_WIDTH) + TAB);
            result.append( Utils.leftPadSpace(totalPointsStr, SMALLEST_WIDTH) + TAB);
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result;
            TeamPointsInfo other = (TeamPointsInfo) o;
            result = Utils.compareToReverse(this.totalPoints, other.totalPoints);
            if(result == 0)
            {
                result = Utils.compareToReverse(this.goalDifference, other.goalDifference);
            }
            return result;
        }
    }
    
    public List reportLeagueTables() throws ValidationException
    {
        List<LeagueTableInfo> result = new ArrayList<LeagueTableInfo>();
        CompetitionSeason currentCompetitionSeason = SuperSixManagerUtils.getCurrentCompetitionSeason();
        List divisions = currentCompetitionSeason.getDivisions();
        for(int i = 0; i < divisions.size(); i++)
        {
            Division division = (Division)divisions.get(i);
            LeagueTableInfo league = new LeagueTableInfo( division);
            result.add( league);
        }        
        Collections.sort(result);
        return result;
    }
    
    public List reportMatchResults()
    {
        List result = new ArrayList();
        CompetitionSeason currentCompetitionSeason = SuperSixManagerUtils.getCurrentCompetitionSeason();
        List meets = currentCompetitionSeason.getMeets();
        List divisions = currentCompetitionSeason.getDivisions();
        for(Iterator iter = meets.iterator(); iter.hasNext();)
        {
            Meet meet = (Meet) iter.next();
            MeetInfo meetInfo = new MeetInfo();
            meetInfo.roundNum = meet.getOrdinal();
            meetInfo.roundDate = TimeUtils.getDisplayedFromDate( meet.getDate(), TimeUtils.DATE_PARSE_STRING);
            for(int i = 0; i < divisions.size(); i++)
            {
                Division division = (Division)divisions.get(i);
                meetInfo.matchInfos.put( division, findMatchInfos( meet, division));
            }
            //meetInfo.matchInfos.put( Division.DIVISION_MALE, findMatchInfos( meet, Division.DIVISION_MALE));
            //meetInfo.matchInfos.put( Division.DIVISION_FEMALE, findMatchInfos( meet, Division.DIVISION_FEMALE));
            result.add( meetInfo);
        }
        Collections.sort(result);
        return result;
    }
    
    private List<TeamPointsInfo> findTeamPointsInfos( Division division) throws ValidationException
    {
        List<TeamPointsInfo> result = new ArrayList<TeamPointsInfo>();
        for(Iterator iterator = SuperSixManagerUtils.getCurrentCompetitionSeason().getCurrentTeams().iterator(); iterator.hasNext();)
        {
            TeamCompetitionSeason team = (TeamCompetitionSeason)iterator.next();
            if(team.getDivision() == division)
            {
                TeamPointsInfo teamPointsInfo = new TeamPointsInfo();
                teamPointsInfo.team = team.getName();
                teamPointsInfo.played = Integer.toString( team.getNumPlayed());                
                teamPointsInfo.won = Integer.toString( team.getNumWon());                
                teamPointsInfo.drawn = Integer.toString( team.getNumDrawn());                
                teamPointsInfo.lost = Integer.toString( team.getNumLost());                
                teamPointsInfo.goalsFor = Integer.toString( team.getNumGoalsFor());                
                teamPointsInfo.goalsAgainst = Integer.toString( team.getNumGoalsAgainst());                
                teamPointsInfo.goalDifferenceStr = Integer.toString( team.getNumGoalDifference());                
                teamPointsInfo.goalDifference = team.getNumGoalDifference();                
                teamPointsInfo.totalPointsStr = Integer.toString( team.getNumTotalPoints());                
                teamPointsInfo.totalPoints = team.getNumTotalPoints();                
                result.add( teamPointsInfo);
            }
        }
        Collections.sort( result);
        return result;
    }

    private List<MatchInfo> findMatchInfos( Meet meet, Division division)
    {
        List<MatchInfo> result = new ArrayList<MatchInfo>();
        for(Iterator iterator = meet.getGames().iterator(); iterator.hasNext();)
        {
            Game game = (Game) iterator.next();
            if(game.getDivision() == division)
            {
                MatchInfo matchInfo = new MatchInfo();
                matchInfo.team1 = game.getTeamOne().getName();
                Assert.notNull( matchInfo.team1, "No name for team " + game.getTeamOne().id + 
                        ", isDummy " + game.getTeamOne().isDummy());
                matchInfo.team2 = game.getTeamTwo().getName();
                Assert.notNull( matchInfo.team2, "No name for team " + game.getTeamTwo().id + 
                        ", isDummy " + game.getTeamTwo().isDummy());
                matchInfo.koTime = game.getKickOffTime();
                Assert.isFalse(matchInfo.koTime.equals( KickOffTime.NULL), "No kick-off time");
                matchInfo.koTimeStr = game.getKickOffTime().getName();
                Assert.notNull( matchInfo.koTimeStr, "No kick-off time found from " + game.getKickOffTime());
                matchInfo.pitch = game.getPitch().getName();
                if(game.getTeamOneGoals() == null || game.getTeamTwoGoals() == null)
                {
                    matchInfo.score = "-, -";
                }
                else
                {
                    if(game.getTeamOneGoals() == WITHDRAWN_SCORE || game.getTeamTwoGoals() == WITHDRAWN_SCORE)
                    {
                        Assert.isTrue( game.getTeamOne().isWithdrawnFromDivision() || 
                                game.getTeamTwo().isWithdrawnFromDivision());
                        matchInfo.score = "-, -";                                
                    }
                    else if(game.getTeamOneGoals() == UNKNOWN_SCORE && game.getTeamTwoGoals() == UNKNOWN_SCORE)
                    {
                        matchInfo.score = "*, *";                                
                    }
                    else if(game.getTeamOneGoals() == UNKNOWN_SCORE || game.getTeamTwoGoals() == UNKNOWN_SCORE)
                    {
                        if(game.getTeamOneGoals() == UNKNOWN_SCORE)
                        {
                            matchInfo.score = "*, " + game.getTeamTwoGoals();
                        }
                        else
                        {
                            matchInfo.score = game.getTeamOneGoals() + ", *";
                        }
                    }
                    else
                    {
                        matchInfo.score = game.getTeamOneGoals() + ", " + game.getTeamTwoGoals();
                    }
                }
                result.add( matchInfo);
            }
        }
        Collections.sort( result);
        return result;
    }

    public static class TeamInfo implements Comparable
    {
        public String teamName;
        public String captainName;
        public String captainEmail;
        public String captainPhone;

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            result.append(FileUtils.DOCUMENT_SEPARATOR);
            if(teamName != null)
            {
                result.append("Team Name: " + teamName + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(captainName != null)
            {
                result.append("Captain: " + captainName + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(captainEmail != null)
            {
                result.append("Email: " + captainEmail + FileUtils.DOCUMENT_SEPARATOR);
            }
            if(captainPhone != null)
            {
                result.append("Email: " + captainPhone + FileUtils.DOCUMENT_SEPARATOR);
            }
            return result.toString();
        }

        public int compareTo(Object o)
        {
            int result = 0;
            TeamInfo other = (TeamInfo) o;
            result = this.teamName.compareTo(other.teamName);
            return result;
        }
    }

    public List reportUnpaidTeams()
    {
        List result = new ArrayList();
        int i = 0;
        CompetitionSeason competitionSeason = SuperSixManagerUtils.getCurrentCompetitionSeason();        
        for(Iterator iter = SuperSixManagerUtils.getGlobalCurrentSuperSixManager().getCurrentTeams(
                competitionSeason.getSeasonYear(), competitionSeason.getCompetition()).iterator(); iter.hasNext(); i++)
        {
            TeamCompetitionSeason team = (TeamCompetitionSeason) iter.next();
            TeamInfo teamInfo = new TeamInfo();
            teamInfo.teamName = team.getName();
            if(team.getCaptain() != null)
            {
                teamInfo.captainName = team.getCaptain().getFirstName() + " " +  team.getCaptain().getSurname();
                teamInfo.captainEmail = team.getCaptain().getEmailAddress();
                teamInfo.captainPhone = team.getCaptain().getContactPhoneNumber();
            }
            if(!team.isPaidForCurrentSeasonYear())
            {
                result.add( teamInfo.toString());
            }
        }
        //Just tidy to have then alphabetical
        Collections.sort(result);
        return result;
    }
}
