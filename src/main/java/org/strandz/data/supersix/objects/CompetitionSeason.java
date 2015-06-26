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
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompetitionSeason implements Comparable, Serializable //for debugging even thou fetch groups not working
{
    private boolean dummy;
    public transient static CompetitionSeason NULL = new CompetitionSeason();
    private SeasonYear seasonYear;
    private Competition competition; 
    private Date startDate;
    private Date endDate;
    private List<Meet> meets = new ArrayList<Meet>();
    private List<TeamCompetitionSeason> teamCompetitionSeasons = new ArrayList<TeamCompetitionSeason>();
    private List<Division> divisions = new ArrayList<Division>(); 

    private static final String equalsPropNames[] = {
        "dummy",
        "seasonYear",
        "competition",
    };

    public CompetitionSeason()
    {

    }

    public int compareTo(Object o)
    {
        int result = 0;
        CompetitionSeason other = (CompetitionSeason) o;
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
        result = Utils.compareTo(seasonYear, other.getSeasonYear());
        return result;
    }

    public boolean equals(Object o)
    {
        boolean result = false;
        Utils.chkType(o, this.getClass());

        String txt = "CompetitionSeason " + this;
        ReasonNotEquals.addClassVisiting(txt);

        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof CompetitionSeason))
        {
            ReasonNotEquals.addReason("not an instance of a CompetitionSeason");
        }
        else
        {
            CompetitionSeason test = (CompetitionSeason) o;
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
        String result = null;
        result = "CompetitionSeason: <" + getSeasonYear() + ", " + getCompetition() + ">";
        return result;
    }

    public SeasonYear getSeasonYear()
    {
        return seasonYear;
    }

    public void setSeasonYear(SeasonYear seasonYear)
    {
        this.seasonYear = seasonYear;
    }

    public Competition getCompetition()
    {
        return competition;
    }

    public void setCompetition(Competition competition)
    {
        this.competition = competition;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

//    public DayInWeek getNightPlayGames()
//    {
//        return nightPlayGames;
//    }
//
//    public void setNightPlayGames(DayInWeek nightPlayGames)
//    {
//        this.nightPlayGames = nightPlayGames;
//    }

    public void addMeet(Meet meet)
    {
        meets.add(meet);
        meet.setCompetitionSeason(this);
    }

    public void addMeet(Meet meet, int index)
    {
        meets.add( index, meet);
        meet.setCompetitionSeason(this);
    }
    
    public boolean removeMeet(Meet meet)
    {
        boolean result;
        if(result = meets.remove(meet))
        {
            //TODO Will be orphans like this so research on how to best
            //delete the RosterSlot at the same time as this method is
            //called
            if(meet.getCompetitionSeason() == this)
            {
                meet.setCompetitionSeason(null);
            }
        }
        return result;
    }

    public List<Meet> getMeets()
    {
        //return Collections.unmodifiableList(meets);
        //InsteadOfAddRemoveTrigger
        return meets;
    }

    
    
    public void addTeamCompetitionSeason(TeamCompetitionSeason teamCompetitionSeason)
    {
        teamCompetitionSeasons.add(teamCompetitionSeason);
        teamCompetitionSeason.setCompetitionSeason(this);
    }

    public void addTeamCompetitionSeason(TeamCompetitionSeason teamCompetitionSeason, int index)
    {
        teamCompetitionSeasons.add( index, teamCompetitionSeason);
        teamCompetitionSeason.setCompetitionSeason(this);
    }
    
    public boolean removeTeamCompetitionSeason(TeamCompetitionSeason teamCompetitionSeason)
    {
        boolean result;
        if(result = teamCompetitionSeasons.remove(teamCompetitionSeason))
        {
            //TODO Will be orphans like this so research on how to best
            //delete the RosterSlot at the same time as this method is
            //called
            if(teamCompetitionSeason.getCompetitionSeason() == this)
            {
                teamCompetitionSeason.setCompetitionSeason(null);
            }
        }
        return result;
    }

    public List getTeamCompetitionSeasons()
    {
        //return Collections.unmodifiableList(teamCompetitionSeasons);
        //InsteadOfAddRemoveTrigger
        return teamCompetitionSeasons;
    }
    
//    public void addTeam( Team team)
//    {
//        getTeams().add( team);
//        //team.getSeasons().add( team);
//    }
//
    public List<Team> getTeams()
    {
        List<Team> result = new ArrayList<Team>();
        for(TeamCompetitionSeason teamCompetitionSeason : teamCompetitionSeasons)
        {
            result.add( teamCompetitionSeason.getTeam());
        }
        return result;
    }

    public List<TeamCompetitionSeason> getCurrentTeams()
    {
        List<TeamCompetitionSeason> result = new ArrayList<TeamCompetitionSeason>();
        for(TeamCompetitionSeason teamCompetitionSeason : teamCompetitionSeasons)
        {
            result.add( teamCompetitionSeason);
        }
        return result;
    }
    
    public void addDivision( Division division)
    {
        getDivisions().add( division);
        //team.getSeasons().add( team);
    }

    public List getDivisions()
    {
        return divisions;
    }
    
    public boolean isDummy()
    {
        return dummy;
    }

    public void setDummy(boolean dummy)
    {
        this.dummy = dummy;
    }    
}
