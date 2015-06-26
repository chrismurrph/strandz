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

import org.strandz.lgpl.util.ValidationException;
import org.strandz.data.supersix.domain.SuperSixDomainQueryEnum;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.Global;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.data.supersix.objects.SeasonYear;
import org.strandz.data.supersix.objects.Competition;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.SuperSixNote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.lgpl.data.objects.DayInWeek;

import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * By convention this application will use the beginning of the day for all dates,
 * as such dates will display as being on the right date.
 */
public class SuperSixManager implements SuperSixManagerI
{
    private DataStore ds;
    private Date whenDidInit;
    private SuperSixServiceI superSixService;

    private void populateDetails()
    {
        if(SuperSixManagerProperties.nightsClosedText == null)
        {
            String propFileName = "property-files/supersix";
            Properties props = PropertyUtils.getPortableProperties(propFileName, this);
            //Example
            //PropertyUtils.getProperty("nightsClosedText", props);
        }
    }

    public SuperSixManager()
    {
        populateDetails();
        superSixService = SuperSixServiceFactory.newSuperSixService();
    }

    public List<TeamCompetitionSeason> getCurrentTeams( SeasonYear seasonYear, Competition competition)
    {
        List<TeamCompetitionSeason> result;
        init();
        result = ds.getDomainQueries().executeRetList(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM, 
                                                        seasonYear.getName(), competition.getName());
        return result;
    }

    public List<TeamCompetitionSeason> getCurrentTeamsIncludingNull( SeasonYear seasonYear, Competition competition)
    {
        List<TeamCompetitionSeason> result;
        init();
        result = ds.getDomainQueries().executeRetList(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_INCL_NULL, 
                                                        seasonYear.getName(), competition.getName());
        return result;
    }
    
    public List<TeamCompetitionSeason> getCurrentTeams( SeasonYear seasonYear, Competition competition, Division division)
    {
        List<TeamCompetitionSeason> result;
        init();
        result = ds.getDomainQueries().executeRetList(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_BY_DIVISION, 
                                                        seasonYear.getName(), competition.getName(), division.getName());
        //Err.pr( "Num of teams in division <" + division + "> is " + result.size());
        return result;
    }

    public Global getGlobal()
    {
        Global result = (Global) (ds.getDomainQueries()).executeRetObject(SuperSixDomainQueryEnum.SINGLE_GLOBAL);
        return result;
    }

    public SuperSixLookups getLookups()
    {
        init();
        Err.pr( WombatNote.NO_EM_WHEN_SOME_QUERY, "Doing getLookups() in SuperSixManager");
        SuperSixLookups result = (SuperSixLookups) (ds.getDomainQueries()).executeRetObject(SuperSixDomainQueryEnum.LOOKUPS);
        return result;
    }
    
    public DataStore getDataStore()
    {
        return ds;
    }
    
    public CompetitionSeason getCurrentSeason( Global global)
    {
        CompetitionSeason result;
        init();
        if(global == null)
        {
            Err.error( "SuperSix does not have an data - at the least need a Global with currentSeasonYear");
        }
        Err.pr( SuperSixNote.CURRENT_SEASON, "global.getCurrentSeasonYear() that will be used in season query is " +
                global.getCurrentSeasonYear());
        result = (CompetitionSeason) ds.getDomainQueries().executeRetObject
                (SuperSixDomainQueryEnum.SEASON_BY_SEASONYEAR_COMP, global.getCurrentSeasonYear().getName(), global.getCurrentCompetition().getName());
        if(result == null)
        {
            //We have pre-loaded a SeasonYear, but not a CompetitionSeason, so on first having this app up
            //we will need to create a CompetitionSeason. (In final version we prolly will load a CompetitionSeason
            //as well, so this code won't run).
            Err.error( "Expect seed data to have a season that can be accessed by "
                     + global.getCurrentSeasonYear() + " (SeasonYear) and " + global.getCurrentCompetition() + " (Competition)");
            /*
            result = SuperSixDemoData.createDefaultSeason();
            result.setSeasonYear( global.getCurrentSeasonYear());
            Err.pr( "New CompetitionSeason created for " + global.getCurrentSeasonYear());
            ((EntityManagedDataStore)ds).getEM().registerPersistent( result);
            */
        }
        else
        {
            Err.pr( SuperSixNote.CURRENT_SEASON, "DB already has a global season that are going to use: " + result);
        }
        return result;
    }

    public void init(DataStore ds)
    {
        whenDidInit = new Date();
        SuperSixServiceFactory.init( superSixService, ds);
        this.ds = ds;
    }

    private void init()
    {
        Err.pr( WombatNote.NO_EM_WHEN_SOME_QUERY, "Doing init in SuperSixManager");
        //Err.stack();
        if(!ds.isOnTx())
        {
            ds.startTx();
            Err.pr(SdzNote.BO_STUFF, "We have needed to force the start of a transaction for " + this + " using " + ds);
        }
    }

    /**
     * Finds all the games (from today) that will be played for the rest of the competitionSeason
     * Thus the games and meets for the rest of the competitionSeason can be created
     * Uses an algorithm that ensures variability of teams against each other
     * (which needs to take into account all the games - past and future) 
     */
    public void calculateDraws( CompetitionSeason competitionSeason, SuperSixLookups superSixLookups, DayInWeek matchNight) throws ValidationException
    {
        init();
        DrawsCalculator drawsCalculator = new DrawsCalculator( ds, competitionSeason, this, superSixLookups);
        drawsCalculator.calculateDraws( matchNight);
    }

    public void calculateMeets( CompetitionSeason competitionSeason, DayInWeek matchNight) throws ValidationException
    {
        init();
        MeetsCalculator meetsCalculator = new MeetsCalculator( ds, competitionSeason, this);
        meetsCalculator.calculateMeets( matchNight);
    }

//Commented out because stuffs up dependency cycles - made task called CalculateDraws     
//    public static void main( String args[])
//    {
//        SuperSixManager obj = new SuperSixManager();
//        SuperSixDataStoreFactory dataStoreFactory = new SuperSixDataStoreFactory();
//        dataStoreFactory.addConnection(SuperSixConnectionEnum.DEMO_MEMORY);
//        DataStore ds = dataStoreFactory.getDataStore();
//        obj.init( ds);
//        SuperSixLookups superSixLookups = (SuperSixLookups)ds.getLookups();
//        try
//        {
//            CompetitionSeason season = obj.getCurrentSeason();
//            obj.calculateDraws( season, superSixLookups);
//            obj.calculateDraws( season, superSixLookups);
//        }
//        catch(ValidationException e)
//        {
//            Err.error((String)e.getMsg(), e);
//        }
//    }
}
