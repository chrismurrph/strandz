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
package org.strandz.applic.supersix;

import org.strandz.data.supersix.business.SuperSixManagerUtils;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.SuperSixNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.view.util.DTUtils;

import javax.swing.JComboBox;
import java.util.ArrayList;
import java.util.List;

public class DivisionTeamLookup
{
    private MaintainSeasonDT dt;
    private List<TeamCompetitionSeason> allTeams;
    private TeamCompetitionSeason nullTeam;
    private Division lastDivision;
    private boolean firstTime = true;
    JComboBox divisionCombo;

    public DivisionTeamLookup( MaintainSeasonDT dt)
    {
        this.dt = dt;
        
        DTUtils.chkNotNull( dt.ui2.getOtherMatchDetailsPanel());
        divisionCombo = dt.ui2.getOtherMatchDetailsPanel().getCbDivision();
        DTUtils.chkNotNull( divisionCombo);
    }

    public void flush()
    {
        allTeams = null;
        firstTime = true;
        lastDivision = null;
        Err.pr( SdzNote.LOVS_CHANGE_DATA_SET, "Done flush");
    }
        
    private static TeamCompetitionSeason obtainNullTeam( List<TeamCompetitionSeason> allTeams)
    {
        TeamCompetitionSeason result = allTeams.get(0);
        if(!result.isDummy())
        {
            TeamCompetitionSeason lastTeam = allTeams.get(allTeams.size()-1);
            if(lastTeam.isDummy())
            {
                //Happened when moved to JDO 2.0 - different idea of ordering (or maybe a Kodo bug)
                allTeams = new ArrayList( allTeams);
                Utils.putFirstLast( allTeams);
            }
            else
            {
                Print.prList( allTeams, "first (or last) s/be dummy");
                Err.error( "First (or last) Team from getTeamsIncludingNull() is expected to be the dummy team, got " + result);
            }
        }
        //Err.pr( "allTeams size: " + allTeams.size());
        return result;
    }
    
    private boolean noteVisible()
    {
        boolean result = SdzNote.LOVS_CHANGE_DATA_SET.isVisible() || SuperSixNote.TEAMS_NOT_SHOWING.isVisible();
        return result;
    }
    
    public void performLookup( boolean displayRequired, String id)
    {
        performLookup( displayRequired, null, id);
    }
    
    /**
     * 
     * @param displayRequired
     * @param division
     * Division division = (Division)dt.divisionLookupCell.getItemValue();
     */
    public void performLookup( boolean displayRequired, Division division, String id)
    {
        if(allTeams == null)
        {
            CompetitionSeason competitionSeason = SuperSixManagerUtils.getCurrentCompetitionSeason();
            allTeams = SuperSixManagerUtils.getGlobalCurrentSuperSixManager().getCurrentTeamsIncludingNull( 
                    competitionSeason.getSeasonYear(), competitionSeason.getCompetition());
            nullTeam = obtainNullTeam( allTeams);
        }
        if(division == null)
        {
            division = (Division)dt.divisionLookupCell.getItemValue();
            Err.pr( noteVisible(), "Division from screen is " + division + " when " + id);
        }
        else
        {
            Err.pr( noteVisible(), "Division param is " + division + " when " + id);            
        }
        if(firstTime || (division != null && !division.equals(lastDivision)))
        {
            if(firstTime)
            {
                Err.pr( noteVisible(), "firstTime when " + id);
            }
            else
            {
                Err.pr( noteVisible(), division + " not eq to lastDivsion: " + lastDivision);
            }
            setTeamsFromDivision( division, displayRequired);
        }
        if(firstTime)
        {
            firstTime = false;
        }
    }
    
    private void setTeamsFromDivision( Division division, boolean displayRequired)
    {
        List teamsToSet = null;
        if(division != null && division.equals( Division.NULL)) //We have to use equals() here because getItemValue() manufactures 
        {
            teamsToSet = new ArrayList( allTeams);
        }
        else if(division != null)
        {
            teamsToSet = new ArrayList( obtainTeams( division));
            Err.pr( SdzNote.LOVS_CHANGE_DATA_SET, "setLOV on teams will be based on division <" + division + ">");
        }
        else
        {
            Err.pr( "null division so will do all teams");
            teamsToSet = new ArrayList( allTeams);
        }
        dt.teamOneLookupCell.setLOV( teamsToSet);
        dt.teamTwoLookupCell.setLOV( teamsToSet);
        if(displayRequired)
        {
            dt.teamOneLookupCell.displayLovObjects();
            dt.teamTwoLookupCell.displayLovObjects();
        }
        lastDivision = division;
        Err.pr( SdzNote.LOVS_CHANGE_DATA_SET, "set lastDivision to " + lastDivision);
    }

    private List obtainTeams( Division division)
    {
        List result;
        if(division == null)
        {
            Err.error( "division == null");
        }
        result = new ArrayList();
        for(int i = 0; i < allTeams.size(); i++)
        {
            TeamCompetitionSeason team = allTeams.get(i);
            Division teamDivision = team.getDivision();
            if(teamDivision != null)
            {
                if(teamDivision.equals( division)) //We have to use equals() here because getItemValue() manufactures
                {
                    result.add( team);
                }
            }
        }
        result.add( 0, nullTeam);
        if(result.size() <= 1)
        {
            Err.error( "Using " + division + " only obtained " + result.size() + " teams");
        }
        return result;
    }

//    private static Team obtainNullTeam( List allTeams)
//    {
//        Team result = null;
//        for(int i = 0; i < allTeams.size(); i++)
//        {
//            Team team = (Team) allTeams.get(i);
//            if(team.isDummy())
//            {
//                result = team;
//                break;
//            }
//        }
//        return result;
//    }
}
