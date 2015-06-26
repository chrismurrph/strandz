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
package org.strandz.data.supersix.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.util.Assert;

import java.util.Date;
import java.util.List;
import java.io.Serializable;

public class Game implements Comparable, Serializable //for debugging even thou fetch groups not working
{
    private boolean dummy;
    public transient static Game NULL = new Game();
    private TeamCompetitionSeason teamOne;
    private TeamCompetitionSeason teamTwo;
    private Integer teamOneGoals;
    private Integer teamTwoGoals;
    private Division division;
    private Meet meet;
    private KickOffTime kickOffTime;
    private Pitch pitch;
    private transient boolean beenPlayedWarningIssued;

    static final int WIN_POINTS = 3;
    static final int DRAW_POINTS = 1;
    static final int LOSS_POINTS = 0;
    static final int MATCH_DURATION_MINUTES = 34;

    public static int WITHDRAWN_SCORE = -1;
    public static int UNKNOWN_SCORE = -2;
    
    private static final String EQUALS_PROP_NAMES[] = {
        "dummy",
        "teamOne",
        "teamTwo",
        "division",
        "meet",
        "kickOffTime",
        "pitch",
    };

    public Game()
    {

    }

    public int compareTo(Object o)
    {
        int result = 0;
        Game other = (Game) o;
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
        result = Utils.compareTo(teamOne, other.getTeamOne());
        if(result == 0)
        {
            result = Utils.compareTo(teamTwo, other.getTeamTwo());
            if(result == 0)
            {
                result = Utils.compareTo(division, other.getDivision());
                if(result == 0)
                {
                    result = Utils.compareTo(meet, other.getMeet());
                    if(result == 0)
                    {
                        result = Utils.compareTo(kickOffTime, other.getKickOffTime());
                        if(result == 0)
                        {
                            result = Utils.compareTo(pitch, other.getPitch());
                        }
                    }
                }
            }
        }
        return result;
    }

    public boolean equals(Object o)
    {
        boolean result = false;
        Utils.chkType(o, this.getClass());

        String txt = "Match " + this;
        ReasonNotEquals.addClassVisiting(txt);

        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Game))
        {
            ReasonNotEquals.addReason("not an instance of a Match");
        }
        else
        {
            Game test = (Game) o;
            result = SelfReferenceUtils.equalsByProperties(Game.EQUALS_PROP_NAMES, this, test);
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = SelfReferenceUtils.hashCodeByProperties(result, Game.EQUALS_PROP_NAMES, this);
        return result;
    }

    public String toString()
    {
        String result = null;
        result = "Game: [<" + getTeamOne() + "> <" + getTeamTwo() + ">" + "<" + getDivision() + ">"+ "<" + getMeet() + ">]";
        return result;
    }

    public TeamCompetitionSeason getTeamOne()
    {
        return teamOne;
    }

    public void setTeamOne(TeamCompetitionSeason teamOne)
    {
        Assert.notNull( teamOne, "Can't put a null TeamCompetitionSeason into a game");
        Assert.isFalse( teamOne.isDummy(), "Can't put a dummy TeamCompetitionSeason into a game");
        this.teamOne = teamOne;
    }

    public TeamCompetitionSeason getTeamTwo()
    {
        return teamTwo;
    }

    public void setTeamTwo(TeamCompetitionSeason teamTwo)
    {
        Assert.notNull( teamTwo, "Can't put a null TeamCompetitionSeason into a game");
        Assert.isFalse( teamTwo.isDummy(), "Can't put a dummy TeamCompetitionSeason into a game");
        this.teamTwo = teamTwo;
    }

    public Integer getTeamOneGoals()
    {
        return teamOneGoals;
    }

    public void setTeamOneGoals(Integer teamOneGoals)
    {
        this.teamOneGoals = teamOneGoals;
    }

    public Integer getTeamTwoGoals()
    {
        return teamTwoGoals;
    }

    public void setTeamTwoGoals(Integer teamTwoGoals)
    {
        this.teamTwoGoals = teamTwoGoals;
    }

    public Division getDivision()
    {
        return division;
    }

    public void setDivision(Division division)
    {
        this.division = division;
    }

    public Meet getMeet()
    {
        return meet;
    }

    public void setMeet(Meet meet)
    {
        this.meet = meet;
    }

    public KickOffTime getKickOffTime()
    {
        return kickOffTime;
    }

    public void setKickOffTime(KickOffTime kickOffTime)
    {
        this.kickOffTime = kickOffTime;
    }

    public Pitch getPitch()
    {
        return pitch;
    }

    public void setPitch(Pitch pitch)
    {
        this.pitch = pitch;
    }

    public boolean isDummy()
    {
        return dummy;
    }

    public void setDummy(boolean dummy)
    {
        this.dummy = dummy;
    }

    /**
     * We can calculate the finish time of a game as we know the date of the meet,
     * the kick off time and the match duration 
     */
    public Date getFinishTime()
    {
        Date result;
        Date dayOccured = meet.getDate();
        Date matchStartTime = TimeUtils.addMinutes( 
                dayOccured, getKickOffTime().getMinutesFromStartOfDay());
        result = TimeUtils.addMinutes( matchStartTime, MATCH_DURATION_MINUTES);
        return result;
    }
    
    //To do this check would need three types of withdrawal - past, future, all. eg in future all games past a
    //withdrawal date would be forfeits for the opposition.
    //For now have disabled this check
    private boolean scoreOK( TeamCompetitionSeason team, int goals)
    {
        boolean result = false;
        if(goals >= 0)
        {
            result = true;
        }
        else if(goals == WITHDRAWN_SCORE)
        {
            if(team.isWithdrawnFromDivision())
            {
                result = true;
            }
        }
        else if(goals == UNKNOWN_SCORE)
        {
            result = true;
        }
        if(team.isWithdrawnFromDivision())
        {
            if(goals != WITHDRAWN_SCORE)
            {
                //disabled
                //Err.error( team.getName() + " has withdrawn, so goals needs to be -1, not " + goals + " for " + this.getMeet());
            }
        }
        return result;
    }
    
    private boolean isForfeited()
    {
        boolean result = false;
        if(getTeamOneGoals() == -1 || getTeamTwoGoals() == -1)
        {
            result = true;
        }
        return result;
    }

    public boolean beenPlayed() throws ValidationException
    {
        boolean result = false;
        boolean forfeited = isForfeited();
        if(forfeited)
        {
            //nufin
        }
        else if(getTeamOneGoals() != null && scoreOK( getTeamOne(), getTeamOneGoals()) &&
                getTeamOneGoals() != null && scoreOK( getTeamTwo(), getTeamTwoGoals()))
        {
            result = true;
        }
        if(!result && !forfeited)
        {
            Date now = new Date();
            if(now.getTime() > getFinishTime().getTime())
            {
                if(!beenPlayedWarningIssued)
                {
                    String msgs[] = new String[]{
                            "WARNING: You have not entered the scores for a past game:",
                            this.toString()};
                    List messages = Utils.asArrayList( msgs);
                    beenPlayedWarningIssued = true;
                    throw new ValidationException( messages);
                }
            }
        }
        return result;
    }

    private void chkPlayedIn( TeamCompetitionSeason team)
    {
        if(team != getTeamOne() && team != getTeamTwo())
        {
            Err.error( team + " did not play in " + this);
        }
    }

    private void chkBeenPlayed( String msg) throws ValidationException
    {
        if(!beenPlayed())
        {
            String msgs[] = new String[]{
                    this + " does not yet have any scores, so cannot " + msg,
                    "Please update the scores for this game",
            };
            throw new ValidationException( msgs);
        }
    }

    public boolean wasWonBy( TeamCompetitionSeason team) throws ValidationException
    {
        boolean result = false;
        chkPlayedIn( team);
        //chkBeenPlayed( "say which team has won");
        if(beenPlayed())
        {
            if(team == getTeamOne())
            {
                result = getTeamOneGoals() > getTeamTwoGoals();
            }
            else if(team == getTeamTwo())
            {
                result = getTeamTwoGoals() > getTeamOneGoals();
            }
        }
        return result;
    }

    public boolean wasLostBy( TeamCompetitionSeason team) throws ValidationException
    {
        boolean result = false;
        chkPlayedIn( team);
        //chkBeenPlayed( "say which team has lost");
        if(beenPlayed())
        {
            if(team == getTeamOne())
            {
                result = getTeamOneGoals() < getTeamTwoGoals();
            }
            else if(team == getTeamTwo())
            {
                result = getTeamTwoGoals() < getTeamOneGoals();
            }
        }
        return result;
    }

    public boolean wasDrawnBy( TeamCompetitionSeason team) throws ValidationException
    {
        boolean result = false;
        chkPlayedIn( team);
        //chkBeenPlayed( "determine if it was a draw");
        if(beenPlayed())
        {
            result = getTeamOneGoals() == getTeamTwoGoals();
        }
        return result;
    }

    public int getGoalsFor( TeamCompetitionSeason team) throws ValidationException
    {
        int result = Utils.UNSET_INT;
        chkPlayedIn( team);
        //chkBeenPlayed( "determine the number of goals scored by " + team);
        if(beenPlayed())
        {
            if(getTeamOne().isWithdrawnFromDivision() || getTeamTwo().isWithdrawnFromDivision())
            {
                Err.error();
                result = 0;
            }
            else
            {
                if(team == getTeamOne())
                {
                    result = getTeamOneGoals();
                }
                else if(team == getTeamTwo())
                {
                    result = getTeamTwoGoals();
                }
            }
        }
        else
        {
            result = 0;
        }
        return result;
    }

    public int getGoalsAgainst( TeamCompetitionSeason team) throws ValidationException
    {
        int result = Utils.UNSET_INT;
        chkPlayedIn( team);
        //chkBeenPlayed( "determine the number of goals scored against " + team);
        if(beenPlayed())
        {
            if(getTeamOne().isWithdrawnFromDivision() || getTeamTwo().isWithdrawnFromDivision())
            {
                Err.error();
                result = 0;
            }
            else
            {
                if(team == getTeamOne())
                {
                    result = getTeamTwoGoals();
                }
                else if(team == getTeamTwo())
                {
                    result = getTeamOneGoals();
                }
            }
        }
        else
        {
            result = 0;
        }
        return result;
    }
}
