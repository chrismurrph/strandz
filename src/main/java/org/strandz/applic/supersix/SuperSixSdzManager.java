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

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.applichousing.MenuTabApplication;
import org.strandz.data.supersix.business.SuperSixManagerUtils;
import org.strandz.data.supersix.objects.Competition;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.Global;
import org.strandz.data.supersix.objects.SeasonYear;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.ValidationException;

import javax.swing.JOptionPane;
import java.util.Iterator;
import java.util.List;

/**
 * Does everything that is global to supersix Strands (and thus is Strandz specific). If it is not
 * Strandz specific then it will go into SuperSixManager, which is the BO.
 * 
 * For when one Strand needs access to another Strand's Design Time info
 * 
 * SuperSixSdzManager - the Strandz BO (we already have a client
 * side one called SuperSixManager - but that one doesn't need to know anything about
 * Strandz - it just needs an EM). SuperSixSdzManager will house anything that requires
 * inter-Strand communication, and will have access to the EM for the app. (If the code
 * just required an EM but no inter-Strand communication then it would go in SuperSixManager).
 * However SuperSixManager should not have any Dialogs coming up, so often a call to SuperSixManager
 * will be routed thru here. 
 */
public class SuperSixSdzManager
{
    private MaintainTeamDT maintainTeamDT;
    private MaintainSeasonDT maintainSeasonDT;
    private EntityManagedDataStore dataStore;
    private MenuTabApplication aSupersix;
    private TeamsAndPlayers teamsAndPlayers;
    private DivisionTeamLookup divisionTeamLookup;

    public SuperSixSdzManager( DataStore dataStore, MenuTabApplication aSupersix)
    {
        this.dataStore = (EntityManagedDataStore)dataStore;
        this.aSupersix = aSupersix;
        this.teamsAndPlayers = new TeamsAndPlayers( aSupersix, this); 
    }

    public void setDivisionTeamLookup(DivisionTeamLookup divisionTeamLookup)
    {
        this.divisionTeamLookup = divisionTeamLookup;
    }

    public void setMaintainTeamDT(MaintainTeamDT maintainTeamDT)
    {
        this.maintainTeamDT = maintainTeamDT;
    }

    public void setMaintainSeasonDT(MaintainSeasonDT maintainSeasonDT)
    {
        this.maintainSeasonDT = maintainSeasonDT;
    }
    
    public void calculateDraws() throws ValidationException
    {
        //We need to POST in case someone has changed something about the current CompetitionSeason
        //but has not yet committed (no POST option for the user).
        if(maintainSeasonDT.seasonNode.getState() != StateEnum.FROZEN)
        {
            if(maintainSeasonDT.strand.getCurrentNode() != maintainSeasonDT.seasonNode)
            {
                maintainSeasonDT.seasonNode.GOTO();
            }
            maintainSeasonDT.strand.POST();
        }
        Err.error( "Assuming Tuesday in calculateDraws() but really ought to get the day from the screen or something");
        SuperSixManagerUtils.getGlobalCurrentSuperSixManager().calculateDraws( 
                (CompetitionSeason)maintainSeasonDT.seasonCell.getDataRecords().get( 0), 
                (SuperSixLookups)dataStore.getLookups(), DayInWeek.TUESDAY);
        String msgs[] = new String[]{
                "This application is a Work In Progress.",
                "Output in the Java Console shows the Meets and Matches",
                "that will be created. [Action/Calculate Draws] uses",
                "information from CompetitionSeason. In the next version of",
                "this application to be released you will see",
                "them created by navigating CompetitionSeason -> Meet -> Match under",
                "the [Maintain/CompetitionSeason] menu.",
        };
        new MessageDlg( msgs, JOptionPane.ERROR_MESSAGE);
    }

    public void calculateMeets() throws ValidationException
    {
        //When this is called the user has just made changes on the screen
        //that do not exist in the underlying DO. Actually don't have to
        //POST - could use
        //the season from the screen for the values, yet make sure update
        //the actual season. However we will POST for now ...
        if(maintainSeasonDT.seasonNode.getState() != StateEnum.FROZEN)
        {
            if(maintainSeasonDT.strand.getCurrentNode() != maintainSeasonDT.seasonNode)
            {
                maintainSeasonDT.seasonNode.GOTO();
            }
            maintainSeasonDT.strand.POST();
        }
        Err.error( "Assuming Tuesday in calculateMeets() but really ought to get the day from the screen or something");
        SuperSixManagerUtils.getGlobalCurrentSuperSixManager().calculateMeets( 
                (CompetitionSeason)maintainSeasonDT.seasonCell.getDataRecords().get( 0), DayInWeek.TUESDAY);
        Err.pr( "When change to JDO we will/may need to do a flush here - see comments in code");
        maintainSeasonDT.strand.EXECUTE_QUERY();
    }

    void changeCurrentSeasonYear( SeasonYear seasonYear)
    {
        changeCurrentSeason( seasonYear, null);
    }

    void changeCurrentCompetition( Competition competition)
    {
        changeCurrentSeason( null, competition);
    }

    private void changeCurrentSeason( SeasonYear seasonYear, Competition competition)
    {
        //Err.pr( SdzNote.lovsChangeDataSet, "current strand b4 changeCurrentSeason is " + 
        //        aSupersix.getCurrentVisibleStrand().getClass().getName());
        Assert.oneOnly( seasonYear, competition);
        divisionTeamLookup.flush();
        Global global = SuperSixManagerUtils.getGlobalCurrentSuperSixManager().getGlobal();
        if(seasonYear != null)
        {
            global.setCurrentSeasonYear( seasonYear);
        }
        else if(competition != null)
        {
            global.setCurrentCompetition( competition);
        }
        SuperSixManagerUtils.setCurrentCompetitionSeason( SuperSixManagerUtils.getGlobalCurrentSuperSixManager().getCurrentSeason( global)); 
        removeTeamsAndPlayersSubMenus();
        List divisions = SuperSixManagerUtils.getCurrentCompetitionSeason().getDivisions();
        for(Iterator iterator = divisions.iterator(); iterator.hasNext();)
        {
            Division division = (Division) iterator.next();
            addTeamsAndPlayersSubMenu( division);
        }
        refreshTeamsAndPlayersSubMenus();
        //Err.pr( SdzNote.lovsChangeDataSet, "current strand after changeCurrentSeason is " + aSupersix.getCurrentVisibleStrand());
    }
    
    /**
     * Now that when making a global change we get rid of all the tabs except 'global', we don't
     * need to re-query in those tabs - that will happen when the user brings them up again.
     * Also there is no business case for recording payments, and transferring teams across to
     * another season (where for starters they will not have paid) ought to be done explicitly
     * by the user. 
     */
    private void _changeCurrentSeason( SeasonYear seasonYear, Competition competition)
    {
        Assert.oneOnly( seasonYear, competition);
        if(seasonYear != null)
        {
            List teams = SuperSixManagerUtils.getGlobalCurrentSuperSixManager().getCurrentTeams( seasonYear, competition);
            for(Iterator iterator = teams.iterator(); iterator.hasNext();)
            {
                TeamCompetitionSeason team = (TeamCompetitionSeason) iterator.next();
                team.setPaidForCurrentSeasonYear( false);
            }
            //If don't do this then the team screen will be left as it was before
            if(maintainTeamDT.strand.getCurrentNode() != maintainTeamDT.team)
            {
                maintainTeamDT.team.GOTO();
            }
            maintainTeamDT.team.execute( OperationEnum.EXECUTE_QUERY);
            Err.pr( "Players are all now unpaid");
        }
        //Same for the season screen, where the current season comes from a global
        Global global = SuperSixManagerUtils.getGlobalCurrentSuperSixManager().getGlobal();
        if(seasonYear != null)
        {
            global.setCurrentSeasonYear( seasonYear);
        }
        else if(competition != null)
        {
            global.setCurrentCompetition( competition);
        }
        Err.pr( "When change to JDO we will/may need to pin Global here, " +
                "so change we have made doesn't get overwritten by query");
        if(maintainSeasonDT.strand.getCurrentNode() !=
                maintainSeasonDT.seasonNode)
        {
            maintainSeasonDT.seasonNode.GOTO();
        }
        maintainSeasonDT.strand.EXECUTE_QUERY();
        if(seasonYear != null)
        {
            Err.pr( "CompetitionSeason in Maintain CompetitionSeason will now refer to the changed Global Year, " + global.getCurrentSeasonYear());
        }
        else
        {
            Err.pr( "CompetitionSeason in Maintain CompetitionSeason will now refer to the changed Global Competition, " + global.getCurrentCompetition());
        }
        SuperSixManagerUtils.setCurrentCompetitionSeason( SuperSixManagerUtils.getGlobalCurrentSuperSixManager().getCurrentSeason( global)); 
        removeTeamsAndPlayersSubMenus();
        List divisions = SuperSixManagerUtils.getCurrentCompetitionSeason().getDivisions();
        for(Iterator iterator = divisions.iterator(); iterator.hasNext();)
        {
            Division division = (Division) iterator.next();
            addTeamsAndPlayersSubMenu( division);
        }
        refreshTeamsAndPlayersSubMenus();
    }
    
    void removeTeamsAndPlayersSubMenus()
    {
        teamsAndPlayers.removeAllSubMenus();    
    }

    private void refreshTeamsAndPlayersSubMenus()
    {
        teamsAndPlayers.refresh();    
    }
    
    void addTeamsAndPlayersSubMenu( Division division)
    {
        teamsAndPlayers.addSubMenu( division);    
    }
}
