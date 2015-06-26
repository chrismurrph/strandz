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

import org.strandz.lgpl.util.NullVerifiable;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.note.SuperSixNote;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Business rules for SuperSix domain model:
 * 
 * -> All divisions in a competition share a set of pitches over a particular
 *    time period, usually a night.
 * -> For a particular CompetitionSeason a team can only be in one division. (Otherwise
 *    the team might be playing at the same time as itself on different pitches).
 * -> Competitions do not share reasources (same pitch at same time).
 * -> Thus one team could be in many competitions.
 * -> Teams play only within their division.
 */
public class TeamCompetitionSeason implements Comparable, NullVerifiable, Serializable //for debugging even thou fetch groups not working
{
    private boolean dummy;
    public transient static TeamCompetitionSeason NULL = new TeamCompetitionSeason();
    private Team team;
    private CompetitionSeason competitionSeason;
    private Division division;
    private List players = new ArrayList();
    private List matchesTeamOne = new ArrayList();
    private List matchesTeamTwo = new ArrayList();
    private Player captain;
    private boolean withdrawnFromDivision;
    private transient List allMatches = new ArrayList();
    private transient List slots = new ArrayList();
    //private List seasons = new ArrayList();
    private boolean paidForCurrentSeasonYear;
    
    private transient static int times;
    private transient static int timesConstructed;
    public transient int id;

    private static final String equalsPropNames[] = {
        "dummy",
        "name",
        "division",
        "competitionSeason",
    };

    public TeamCompetitionSeason()
    {
        timesConstructed++;
        id = timesConstructed;
        if(id == 0)
        {
            Err.stack();
        }
    }

    public boolean equals(Object o)
    {
        boolean result = false;
        Utils.chkType(o, this.getClass());

        String txt = "TeamCompetitionSeason " + this;
        ReasonNotEquals.addClassVisiting(txt);

        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof TeamCompetitionSeason))
        {
            ReasonNotEquals.addReason("not an instance of a TeamCompetitionSeason");
        }
        else
        {
            TeamCompetitionSeason test = (TeamCompetitionSeason) o;
            result = SelfReferenceUtils.equalsByProperties(equalsPropNames, this, test);
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = SelfReferenceUtils.hashCodeByProperties(result, equalsPropNames, this);
        return result;
    }

    public String toString()
    {
        String result;
        String name = getName();
        if(isDummy()) //for automatic lookup recognition
        {
            result = null;
        }
        else
        {
            result = "TeamCompetitionSeason: <" + name + ">";
        }
        return result;
    }
    

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(Team team)
    {
        this.team = team;
    }
    
    public String getName()
    {
        String result = null;
        if(team != null)
        {
            result = team.getName();
        }
        else if(isDummy())
        {
            //nufin
            times++;
            if(times == 0)
            {
                Err.stack(SuperSixNote.TEAMS_NOT_SHOWING, "Will ret null for dummy team");
            }
        }
        else
        {
            Err.error( "No team in: " + id);
        }
        return result;
    }
    
    public int compareTo(Object o)
    {
        int result = 0;
        TeamCompetitionSeason other = (TeamCompetitionSeason) o;
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
        result = Utils.compareTo(getName(), other.getName());
        if(result == 0)
        {
            result = Utils.compareTo(division, other.getDivision());
        }

        return result;
    }
    
    public CompetitionSeason getCompetitionSeason()
    {
        return competitionSeason;
    }

    public void setCompetitionSeason(CompetitionSeason competitionSeason)
    {
        this.competitionSeason = competitionSeason;
    }

    public Division getDivision()
    {
        return division;
    }

    public void setDivision(Division division)
    {
        this.division = division;
    }
    
    public boolean isDummy()
    {
        return dummy;
    }

    public void setDummy(boolean dummy)
    {
        this.dummy = dummy;
        //name = "NULL"; //This is required for lookup recognition
    }
    
    public void addPlayer(Player player)
    {
        players.add(player);
        player.setTeamCompetitionSeason( this);
    }
    
    public void addPlayer(Player player, int index)
    {
        players.add( index, player);
        player.setTeamCompetitionSeason(this);
    }

    public boolean removePlayer(Player player)
    {
        boolean result;
        if(result = players.remove(player))
        {
            //TODO Will be orphans like this so research on how to best
            //delete the RosterSlot at the same time as this method is
            //called
            if(player.getTeamCompetitionSeason() == this)
            {
                player.setTeamCompetitionSeason(null);
            }
        }
        return result;
    }
    
    public List getPlayers()
    {
        return Collections.unmodifiableList(players);
    }    

    public Player getCaptain()
    {
        return captain;
    }

    public void setCaptain(Player captain)
    {
        this.captain = captain;
    }

    public boolean isPaidForCurrentSeasonYear()
    {
        return paidForCurrentSeasonYear;
    }

    public void setPaidForCurrentSeasonYear(boolean paidForCurrentSeasonYear)
    {
        this.paidForCurrentSeasonYear = paidForCurrentSeasonYear;
    }

    public List getSlots()
    {
        return slots;
    }
    
//    public List getSeasons()
//    {
//        return seasons;
//    }

    public void addTeamOneMatch(Game game)
    {
        matchesTeamOne.add(game);
        game.setTeamOne(this);
        allMatches.add(game);
    }

    public void addTeamTwoMatch(Game game)
    {
        matchesTeamTwo.add(game);
        game.setTeamTwo(this);
        allMatches.add(game);
    }

    public void removeMatch(Game game)
    {
        if(allMatches.remove(game))
        {
            //TODO Will be orphans like this so research on how to best
            //delete the RosterSlot at the same time as this method is
            //called
            int i = 0;
            if(game.getTeamOne() == this)
            {
                game.setTeamOne(null); i++;
                matchesTeamOne.remove(game);
            }
            if(game.getTeamTwo() == this)
            {
                game.setTeamTwo(null); i++;
                matchesTeamTwo.remove(game);
            }
            if(i == 2)
            {
                Err.error( "Surely a team cannot have been playing itself in the same match");
            }
            else if(i < 1)
            {
                Err.error( "A Game being removed from a Team did not exist on the Game side");
            }
        }
    }

    public List getMatches()
    {
        return Collections.unmodifiableList(allMatches);
    }

    public int getNumPlayed() throws ValidationException
    {
        int result = 0;
        for(Iterator iterator = allMatches.iterator(); iterator.hasNext();)
        {
            Game game = (Game) iterator.next();
            if(game.beenPlayed())
            {
                result++;
            }
        }
        return result;
    }

    public int getNumWon() throws ValidationException
    {
        int result = 0;
        for(Iterator iterator = allMatches.iterator(); iterator.hasNext();)
        {
            Game game = (Game) iterator.next();
            if(game.wasWonBy( this))
            {
                result++;
            }
        }
        return result;
    }
    
    public int getNumLost() throws ValidationException
    {
        int result = 0;
        for(Iterator iterator = allMatches.iterator(); iterator.hasNext();)
        {
            Game game = (Game) iterator.next();
            if(game.wasLostBy( this))
            {
                result++;
            }
        }
        return result;
    }
    
    public int getNumDrawn() throws ValidationException
    {
        int result = 0;
        for(Iterator iterator = allMatches.iterator(); iterator.hasNext();)
        {
            Game game = (Game) iterator.next();
            if(game.wasDrawnBy( this))
            {
                result++;
            }
        }
        return result;
    }
    
    public int getNumTotalPoints() throws ValidationException
    {
        int result = (getNumWon() * Game.WIN_POINTS);
        result += (getNumDrawn() * Game.DRAW_POINTS);
        result += (getNumLost() * Game.LOSS_POINTS);
        return result;
    }
    
    public int getNumGoalsFor() throws ValidationException
    {
        int result = 0;
        for(Iterator iterator = allMatches.iterator(); iterator.hasNext();)
        {
            Game game = (Game) iterator.next();
            result += game.getGoalsFor( this);
        }
        return result;
    }
    
    public int getNumGoalsAgainst() throws ValidationException
    {
        int result = 0;
        for(Iterator iterator = allMatches.iterator(); iterator.hasNext();)
        {
            Game game = (Game) iterator.next();
            result += game.getGoalsAgainst( this);
        }
        return result;
    }
    
    public int getNumGoalDifference() throws ValidationException
    {
        int result = 0;
        for(Iterator iterator = allMatches.iterator(); iterator.hasNext();)
        {
            Game game = (Game) iterator.next();
            result += game.getGoalsFor( this) - game.getGoalsAgainst( this);
        }
        return result;
    }

    public boolean isWithdrawnFromDivision()
    {
        return withdrawnFromDivision;
    }

    public void setWithdrawnFromDivision(boolean withdrawnFromDivision)
    {
        this.withdrawnFromDivision = withdrawnFromDivision;
    }

//    public List getPlayers()
//    {
//        //Below rubbish - of course they will be deleted - Strandz will do its own
//        //players.remove(). The only time a problem could occur is if the above method
//        //did more. This is a single direction relationship so is nothing out of the
//        //ordinary - in fact let's remove the above (addPlayer, removePlayer) methods.
//        //
//        //Obviously this was here to force removePlayer above to be used.
//        //However Strandz is dumb without InsteadOfAddRemoveTrigger, and is just deleting 
//        //from the collection
//        //return Collections.unmodifiableList(players);
//        //With above commented-out, players may not be deleted at all
//        return players;
//    }
    
}
