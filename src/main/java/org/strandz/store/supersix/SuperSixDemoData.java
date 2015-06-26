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
package org.strandz.store.supersix;

import org.strandz.data.supersix.business.Reports;
import org.strandz.data.supersix.domain.SuperSixDomainLookupEnum;
import org.strandz.data.supersix.objects.Competition;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.Game;
import org.strandz.data.supersix.objects.Global;
import org.strandz.data.supersix.objects.KickOffTime;
import org.strandz.data.supersix.objects.Meet;
import org.strandz.data.supersix.objects.Pitch;
import org.strandz.data.supersix.objects.Player;
import org.strandz.data.supersix.objects.SeasonYear;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.data.supersix.objects.Team;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.lgpl.data.objects.UserDetails;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * The natural place for this class would be in the task package
 * however we don't want store.supersix depending back on a task
 * package, and there is just no need to go to the complication of
 * using interfaces to solve this problem.
 * http://www.super6soccer.com.au/
 */
public class SuperSixDemoData
{
    private DemoDataFor2006 demoDataFor2006 = new DemoDataFor2006( this);
    public List<Global> newGlobals = new ArrayList<Global>();
    public List<Player> newPlayers = new ArrayList<Player>();
    public List<CompetitionSeason> newSeasons = new ArrayList<CompetitionSeason>();
    public List<TeamCompetitionSeason> newTeams = new ArrayList<TeamCompetitionSeason>();
    public List newUsers = new ArrayList();
    public SuperSixLookups superSixLookups;
    Competition currentCompetition;
    SeasonYear currentSeasonYear;
    private static SuperSixDemoData instance;
    private static int UNKNOWN_SCORE = Reports.UNKNOWN_SCORE;
    static int WITHDRAWN_SCORE = Reports.WITHDRAWN_SCORE;

    /**
     * Teams that are involved in > 1 CompetitionSeason
     */
    static String OTHER_TEAM = "The Other Team";
    static String MAGPIES = "The Magpies";
    static String INTER = "Inter";
    static String IBERIA = "Iberia";
    static String LATE_NIGHT_SBS = "Late Night SBS";
    static String ITALIAN_WARRIORS = "Italian Warriors";
    static String LIGEM_AGAIN = "Lig'em Again";
    static String WANDERERS_A = "Wanderers A";
    static String RED_DEVILS = "Red Devils";
    static String FEISTY = "Feisty Divas";
    static String CHILLI = "Chilli Cats";
    static String AB_FAB = "Absolutely Fabulous";
    private Team knownTeams[] = new Team[12];
    
    private static String SOCCERWHOS = "Soccerwhos";
    private static String VLAD = "Vladivostock";
    private static String INCOGNITO = "Incognito";
    private static String DEAD_DINGOS = "Dead Dingo's Donga";
    private static String RANDOM = "Random Yobos";
    private static String JUST = "Just Johnnie";
    private static String OLD_BOYS = "Wanderers Old Boys";
    private static String SUBLIME = "Sublime";
    private static String DIRTY = "Dirty Sanchez";
    private static String JOHN = "John United FC";
    private static String LONERS = "Loners with Boners";
    private static String RANDOMS_GIRLS = "The Randoms";
    private static String CHOCOLATE = "Chocolate Frogs";
    
    private SuperSixDemoData()
    {
        knownTeams[0] = new Team(OTHER_TEAM);
        knownTeams[1] = new Team(MAGPIES);
        knownTeams[2] = new Team( INTER);
        knownTeams[3] = new Team( IBERIA);
        knownTeams[4] = new Team( LATE_NIGHT_SBS);
        knownTeams[5] = new Team( ITALIAN_WARRIORS);
        knownTeams[6] = new Team( LIGEM_AGAIN);
        knownTeams[7] = new Team( WANDERERS_A);
        knownTeams[8] = new Team( RED_DEVILS);
        knownTeams[9] = new Team( FEISTY);
        knownTeams[10] = new Team( CHILLI);
        knownTeams[11] = new Team( AB_FAB);
    }
    
    private Team getKnownTeam( String name)
    {
        Team result = null;
        for(int i = 0; i < knownTeams.length; i++)
        {
            Team knownTeam = knownTeams[i];
            if(knownTeam.getName().equals( name))
            {
                result = knownTeam;
                break;
            }
        }
        return result;
    }
    
    public static SuperSixDemoData getInstance()
    {
        SuperSixDemoData result;
        if(SuperSixDemoData.instance == null)
        {
            result = new SuperSixDemoData();
            result.superSixLookups = new SuperSixLookups();
            result.superSixLookups.initValues();
            CompetitionSeason tuesComp2006 = createTuesday2006Season();
            result.demoDataFor2006.do2006TuesPopulation( tuesComp2006);
            CompetitionSeason wedsComp2006 = createWednesday2006Season();
            result.demoDataFor2006.do2006WedsPopulation( wedsComp2006);
            CompetitionSeason tuesComp2007 = createTuesday2007Season();
            result.do2007TuesChampionshipPopulation( tuesComp2007);
            CompetitionSeason wedsComp2007 = createWednesday2007Season();
            result.do2007WedsPremiershipPopulation( wedsComp2007);
            result.doCreateGlobals();
            SuperSixDemoData.instance = result;
        }
        else
        {
            result = SuperSixDemoData.instance;
        }
        return result;
    }

    private TeamCompetitionSeason getTeamCompetitionSeason( String name)
    {
        TeamCompetitionSeason result = null;
        for(Iterator iterator = newTeams.iterator(); iterator.hasNext();)
        {
            TeamCompetitionSeason team = (TeamCompetitionSeason) iterator.next();
            if(!team.isDummy())
            {
                CompetitionSeason competitionSeason = team.getCompetitionSeason();
                if(competitionSeason.getCompetition().equals( currentCompetition) &&
                        competitionSeason.getSeasonYear().equals( currentSeasonYear))
                {
                    if(team.getName().equals( name))
                    {
                        result = team;
                        break;
                    }
                }
            }
        }
        if(result == null)
        {
            Print.prList( newTeams, "Teams to choose from");
            Err.error( "No Team with name: <" + name + ">");
        }
        return result;
    }

    /**
     * Convenient to call when attaching teams and seasons to each other
     * (Which do by simply calling season.addTeam()) 
     */
//    private CompetitionSeason getCurrentSeason()
//    {
//        return (CompetitionSeason)newSeasons.get( 0);
//    }

    /**
     * PREMIER LEAGUE
     * @param wedsComp2007
     */
    public void do2007WedsPremiershipPopulation( CompetitionSeason wedsComp2007)
    {
        currentCompetition = wedsComp2007.getCompetition();
        currentSeasonYear = wedsComp2007.getSeasonYear();
        
        newSeasons.add( wedsComp2007);
        
        TeamCompetitionSeason team1 = createTeamCompetitionSeason( JUST, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team1);
        TeamCompetitionSeason team2 = createTeamCompetitionSeason( OLD_BOYS, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team2);
        TeamCompetitionSeason team3 = createTeamCompetitionSeason( RANDOM, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team3);
        TeamCompetitionSeason team4 = createTeamCompetitionSeason( IBERIA, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team4);
        TeamCompetitionSeason team5 = createTeamCompetitionSeason( DIRTY, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team5);
        TeamCompetitionSeason team6 = createTeamCompetitionSeason( WANDERERS_A, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team6);
        TeamCompetitionSeason team7 = createTeamCompetitionSeason( LATE_NIGHT_SBS, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team7);
        TeamCompetitionSeason team9 = createTeamCompetitionSeason( ITALIAN_WARRIORS, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team9);
        TeamCompetitionSeason team10 = createTeamCompetitionSeason( LIGEM_AGAIN, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team10);
        TeamCompetitionSeason team11 = createTeamCompetitionSeason( RED_DEVILS, Division.FIRST_DIVISION, wedsComp2007);
        newTeams.add( team11);
        
        Meet wedsRound1 = createMeet( 1, TimeUtils.getDate( 17, Calendar.JANUARY, 2007));
        wedsComp2007.addMeet( wedsRound1);        
        
        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, JUST, OLD_BOYS, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 1, 0));
        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, RANDOM, IBERIA, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 3));
        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, DIRTY, WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 5, 0));
        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, LATE_NIGHT_SBS, ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 0));
        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, LIGEM_AGAIN, RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 5, 1));
        
        Meet wedsRound2 = createMeet( 2, TimeUtils.getDate( 24, Calendar.JANUARY, 2007));
        wedsComp2007.addMeet( wedsRound2);        
        
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, WANDERERS_A, JUST, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 1, 1));
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, IBERIA, ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 1));
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, LATE_NIGHT_SBS, RANDOM, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 3, 0));
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, OLD_BOYS, LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 1));
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, RED_DEVILS, DIRTY, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 1, 7));
        
        Meet wedsRound3 = createMeet( 3, TimeUtils.getDate( 31, Calendar.JANUARY, 2007));
        wedsComp2007.addMeet( wedsRound3);        
        
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, DIRTY, IBERIA, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 1, 3));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, LIGEM_AGAIN, ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 4, 3));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, JUST, RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 1, 2));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, RANDOM, OLD_BOYS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 2, 0));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, LATE_NIGHT_SBS, WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 3, 1));

        Meet wedsRound4 = createMeet( 4, TimeUtils.getDate( 7, Calendar.FEBRUARY, 2007));
        wedsComp2007.addMeet( wedsRound4);        
        
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, DIRTY, RANDOM, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 3, 1));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, ITALIAN_WARRIORS, WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 2));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, IBERIA, LATE_NIGHT_SBS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 0, 0));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, LIGEM_AGAIN, JUST, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 3, 0));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, RED_DEVILS, OLD_BOYS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 0, 3));

        Meet wedsRound5 = createMeet( 5, TimeUtils.getDate( 14, Calendar.FEBRUARY, 2007));
        wedsComp2007.addMeet( wedsRound5);        
        
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, OLD_BOYS, WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 1, 1));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, LIGEM_AGAIN, DIRTY, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 2, 1));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, IBERIA, JUST, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 3, 1));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, RED_DEVILS, LATE_NIGHT_SBS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 3));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, ITALIAN_WARRIORS, RANDOM, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 2, 0));

        Meet wedsRound6 = createMeet( 6, TimeUtils.getDate( 21, Calendar.FEBRUARY, 2007));
        wedsComp2007.addMeet( wedsRound6);        
        
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, DIRTY, ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 1, 4));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, IBERIA, RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 5, 0));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, LATE_NIGHT_SBS, OLD_BOYS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 2, 1));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, WANDERERS_A, LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 3));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, RANDOM, JUST, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 3, 3));
        
        Meet wedsRound7 = createMeet( 7, TimeUtils.getDate( 28, Calendar.FEBRUARY, 2007));
        wedsComp2007.addMeet( wedsRound7);        
        
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, WANDERERS_A, IBERIA, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 0, 5));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, LATE_NIGHT_SBS, LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 2, 1));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, JUST, DIRTY, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 0, 4));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, RANDOM, RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 2, 1));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, OLD_BOYS, ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 0, 3));

        Meet wedsRound8 = createMeet( 8, TimeUtils.getDate( 7, Calendar.MARCH, 2007));
        wedsComp2007.addMeet( wedsRound8);        
        
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, LIGEM_AGAIN, RANDOM, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 4, 1));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, ITALIAN_WARRIORS, JUST, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 4, 1));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, RED_DEVILS, WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 1, 2));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, IBERIA, OLD_BOYS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 2, 2));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, DIRTY, LATE_NIGHT_SBS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 2, 1));

        Meet wedsRound9 = createMeet( 9, TimeUtils.getDate( 14, Calendar.MARCH, 2007));
        wedsComp2007.addMeet( wedsRound9);        
        
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, ITALIAN_WARRIORS, RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 4, 0));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, OLD_BOYS, DIRTY, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 2, 7));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, LIGEM_AGAIN, IBERIA, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 3, 0));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, JUST, LATE_NIGHT_SBS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 2));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, RANDOM, WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 4, 1));
    }
    
    public void do2007TuesChampionshipPopulation( CompetitionSeason tuesComp2007)
    {
        currentCompetition = tuesComp2007.getCompetition();
        currentSeasonYear = tuesComp2007.getSeasonYear();
        
        newSeasons.add( tuesComp2007);
        
        TeamCompetitionSeason team1 = createTeamCompetitionSeason( SOCCERWHOS, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team1);
        TeamCompetitionSeason team2 = createTeamCompetitionSeason( VLAD, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team2);
        TeamCompetitionSeason team3 = createTeamCompetitionSeason(OTHER_TEAM, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team3);
        TeamCompetitionSeason team4 = createTeamCompetitionSeason( INCOGNITO, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team4);
        TeamCompetitionSeason team5 = createTeamCompetitionSeason(DEAD_DINGOS, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team5);
        TeamCompetitionSeason team6 = createTeamCompetitionSeason( JOHN, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team6);
        TeamCompetitionSeason team7 = createTeamCompetitionSeason( LONERS, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team7);
        TeamCompetitionSeason team8 = createTeamCompetitionSeason( INTER, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team8);
        TeamCompetitionSeason team9 = createTeamCompetitionSeason( SUBLIME, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team9);
        TeamCompetitionSeason team10 = createTeamCompetitionSeason(MAGPIES, Division.DIVISION_MALE, tuesComp2007);
        newTeams.add( team10);
        TeamCompetitionSeason team11 = createTeamCompetitionSeason(FEISTY, Division.DIVISION_FEMALE, tuesComp2007);
        newTeams.add( team11);
        TeamCompetitionSeason team12 = createTeamCompetitionSeason(CHILLI, Division.DIVISION_FEMALE, tuesComp2007);
        newTeams.add( team12);
        TeamCompetitionSeason team13 = createTeamCompetitionSeason(AB_FAB, Division.DIVISION_FEMALE, tuesComp2007);
        newTeams.add( team13);
        TeamCompetitionSeason team14 = createTeamCompetitionSeason(RANDOMS_GIRLS, Division.DIVISION_FEMALE, tuesComp2007);
        newTeams.add( team14);
        TeamCompetitionSeason team15 = createTeamCompetitionSeason(CHOCOLATE, Division.DIVISION_FEMALE, tuesComp2007);
        newTeams.add( team15);
                
        Meet tuesRound1 = createMeet( 1, TimeUtils.getDate( 16, Calendar.JANUARY, 2007));
        tuesComp2007.addMeet( tuesRound1);
                
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, SOCCERWHOS, VLAD, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 0, 4));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, OTHER_TEAM, INCOGNITO, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 0));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, DEAD_DINGOS, JOHN, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 0, 1));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, LONERS, INTER, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 2, 6));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, SUBLIME, MAGPIES, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 1, 0));

        Meet tuesRound2 = createMeet( 2, TimeUtils.getDate( 23, Calendar.JANUARY, 2007));
        tuesComp2007.addMeet( tuesRound2);
        
        tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, JOHN, SOCCERWHOS, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 1, 5));
        tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, INCOGNITO, INTER, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 0, 4));
        tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, LONERS, OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 2, 1));
        tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, VLAD, SUBLIME, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 2, 2));
        tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, MAGPIES, DEAD_DINGOS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 6, 1));        

        Meet tuesRound3 = createMeet( 3, TimeUtils.getDate( 30, Calendar.JANUARY, 2007));
        tuesComp2007.addMeet( tuesRound3);
        
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, DEAD_DINGOS, INCOGNITO, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 1, 2));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, SUBLIME, INTER, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 1));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, SOCCERWHOS, MAGPIES, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 0, 9));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, OTHER_TEAM, VLAD, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 2, 3));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, LONERS, JOHN, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 1, 1));
        
        tuesRound3.addMatch( createMatch( Division.DIVISION_FEMALE, FEISTY, CHOCOLATE, 
                                     KickOffTime.KICK_OFF_0615, Pitch.PITCH_ONE, 0, 4));        
        
        Meet tuesRound4 = createMeet( 4, TimeUtils.getDate( 6, Calendar.FEBRUARY, 2007));
        tuesComp2007.addMeet( tuesRound4);
        
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, DEAD_DINGOS, OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_ONE, 2, 1));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, INTER, JOHN, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_THREE, 2, 1));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, INCOGNITO, LONERS, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_ONE, 2, 2));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, SUBLIME, SOCCERWHOS, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_THREE, 6, 2));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, MAGPIES, VLAD, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_ONE, 2, 0));
        
        tuesRound4.addMatch( createMatch( Division.DIVISION_FEMALE, CHILLI, RANDOMS_GIRLS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_THREE, 4, 0));        
        tuesRound4.addMatch( createMatch( Division.DIVISION_FEMALE, AB_FAB, FEISTY, 
                                     KickOffTime.KICK_OFF_0830, Pitch.PITCH_ONE, 0, 1));        

        Meet tuesRound5 = createMeet( 5, TimeUtils.getDate( 13, Calendar.FEBRUARY, 2007));
        tuesComp2007.addMeet( tuesRound5);
        
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, VLAD, JOHN, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_THREE, 1, 2));
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, SUBLIME, DEAD_DINGOS, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_ONE, 4, 0));
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, INCOGNITO, SOCCERWHOS, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_THREE, 0, 1));
        tuesRound5.addMatch( createMatch( Division.DIVISION_FEMALE, CHILLI, CHOCOLATE, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_ONE, 0, 1));
        tuesRound5.addMatch( createMatch( Division.DIVISION_FEMALE, AB_FAB, RANDOMS_GIRLS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_THREE, 3, 0));
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, MAGPIES, LONERS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_ONE, 2, 2));        
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, INTER, OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0830, Pitch.PITCH_ONE, 4, 1));        
        
        Meet tuesRound6 = createMeet( 6, TimeUtils.getDate( 20, Calendar.FEBRUARY, 2007));
        tuesComp2007.addMeet( tuesRound6);
        
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, DEAD_DINGOS, INTER, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_ONE, 0, 6));
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, INCOGNITO, MAGPIES, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_THREE, 0, 2));
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, LONERS, VLAD, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_ONE, 6, 0));
        tuesRound6.addMatch( createMatch( Division.DIVISION_FEMALE, CHILLI, FEISTY, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_TWO, 4, 0));
        tuesRound6.addMatch( createMatch( Division.DIVISION_FEMALE, CHOCOLATE, RANDOMS_GIRLS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_THREE, 5, 0));
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, JOHN, SUBLIME, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_ONE, 1, 3));        
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, OTHER_TEAM, SOCCERWHOS, 
                                     KickOffTime.KICK_OFF_0830, Pitch.PITCH_ONE, 2, 1));        
        tuesRound6.addMatch( createMatch( Division.DIVISION_FEMALE, CHILLI, AB_FAB, 
                                     KickOffTime.KICK_OFF_0830, Pitch.PITCH_THREE, 3, 0));

        Meet tuesRound7 = createMeet( 7, TimeUtils.getDate( 27, Calendar.FEBRUARY, 2007));
        tuesComp2007.addMeet( tuesRound7);
        
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, JOHN, INCOGNITO, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_ONE, 1, 0));
        tuesRound7.addMatch( createMatch( Division.DIVISION_FEMALE, AB_FAB, CHOCOLATE, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_TWO, 0, 9));
        tuesRound7.addMatch( createMatch( Division.DIVISION_FEMALE, FEISTY, RANDOMS_GIRLS, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_ONE, 1, 1));
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, LONERS, SUBLIME, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_TWO, 1, 6));
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, SOCCERWHOS, DEAD_DINGOS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 1, 3));
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, OTHER_TEAM, MAGPIES, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_ONE, 3, 7));        
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, VLAD, INTER, 
                                     KickOffTime.KICK_OFF_0830, Pitch.PITCH_TWO, 0, 3));        
        
        Meet tuesRound8 = createMeet( 8, TimeUtils.getDate( 6, Calendar.MARCH, 2007));
        tuesComp2007.addMeet( tuesRound8);
        
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, SUBLIME, OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_ONE, 4, 1));
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, INTER, SOCCERWHOS, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_TWO, 2, 2));
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, MAGPIES, JOHN, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_ONE, 0, 3));
        tuesRound8.addMatch( createMatch( Division.DIVISION_FEMALE, CHILLI, AB_FAB, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_TWO, 4, 0));
        tuesRound8.addMatch( createMatch( Division.DIVISION_FEMALE, FEISTY, CHOCOLATE, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 0, 4));
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, INCOGNITO, VLAD, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_ONE, 1, 3));        
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, DEAD_DINGOS, LONERS, 
                                     KickOffTime.KICK_OFF_0830, Pitch.PITCH_TWO, 1, 1));        

        Meet tuesRound9 = createMeet( 9, TimeUtils.getDate( 13, Calendar.MARCH, 2007));
        tuesComp2007.addMeet( tuesRound9);
        
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, INTER, MAGPIES, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_ONE, 3, 0));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, VLAD, DEAD_DINGOS, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_TWO, 2, 0));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, SUBLIME, INCOGNITO, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_ONE, 2, 1));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, SOCCERWHOS, LONERS, 
                                     KickOffTime.KICK_OFF_0710, Pitch.PITCH_TWO, 0, 4));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, OTHER_TEAM, JOHN, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_TWO, 1, 4));
        
        tuesRound9.addMatch( createMatch( Division.DIVISION_FEMALE, CHILLI, RANDOMS_GIRLS, 
                                     KickOffTime.KICK_OFF_0750, Pitch.PITCH_ONE, 4, 1));        
        tuesRound9.addMatch( createMatch( Division.DIVISION_FEMALE, AB_FAB, FEISTY, 
                                     KickOffTime.KICK_OFF_0830, Pitch.PITCH_TWO, 1, 1));
        
        Meet tuesRound10 = createMeet( 10, TimeUtils.getDate( 20, Calendar.MARCH, 2007));
        tuesComp2007.addMeet( tuesRound10);
        tuesRound10.addMatch( createMatch( Division.DIVISION_FEMALE, CHILLI, CHOCOLATE, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_ONE, 0, 1));        
        tuesRound10.addMatch( createMatch( Division.DIVISION_FEMALE, AB_FAB, RANDOMS_GIRLS, 
                                     KickOffTime.KICK_OFF_0630, Pitch.PITCH_TWO, 2, 0));
    }
        
    private void doCreateGlobals()
    {
        newUsers.add( createUser( "Leo", "Leo"));
        
        if(superSixLookups == null)
        {
            Err.error( "lookups == null");
        }
        
        Global global = new Global();
        global.setCurrentSeasonYear( SeasonYear.SEASON_2007);
        global.setCurrentCompetition( Competition.CHAMPIONSHIP_TUESDAY);
        newGlobals.add( global);
    }
    
    /*
    public static void createSeedMatchResults( CompetitionSeason season, Date date)
    {
        for(int i = 0; i < season.getMeets().size(); i++)
        {
            Meet meet = (Meet) season.getMeets().get(i);
            if(meet.getDate().equals( date))
            {
                for(int j = 0; j < meet.getGames().size(); j++)
                {
                    Game game = (Game) meet.getGames().get(j);
                    if(teamsPlaying( "Balmain Bugs", LATE_NIGHT_SBS, game))
                    {game.setTeamOneGoals( 4);game.setTeamTwoGoals( 2);}
                    else if(teamsPlaying( "Storm Troopers", "The Undead Kenyans", game))
                    {game.setTeamOneGoals( 4);game.setTeamTwoGoals( 2);}
                    else if(teamsPlaying( "That's Not Cricket", "Croydon Massive", game))
                    {game.setTeamOneGoals( 4);game.setTeamTwoGoals( 2);}
                    else if(teamsPlaying( "Portugal", ITALIAN_WARRIORS, game))
                    {game.setTeamOneGoals( 4);game.setTeamTwoGoals( 2);}
                    else if(teamsPlaying( IBERIA, "Melchester Old Boys", game))
                    {game.setTeamOneGoals( 4);game.setTeamTwoGoals( 2);}
                    else if(teamsPlaying( "Balmain Wanderers", MAGPIES, game))
                    {game.setTeamOneGoals( 4);game.setTeamTwoGoals( 2);}
                    else if(teamsPlaying( "Balmain Wanderers 2", "Sydney Uni", game))
                    {game.setTeamOneGoals( 4);game.setTeamTwoGoals( 2);}
                }
            }
        }
    }
    */

    Player createPlayer( String firstName, String surname, Date dob, int ordinal)
    {
        Player result = null;
        result = createPlayer( firstName, surname, dob, ordinal, null);
        return result;
    }

    Player createPlayer( String firstName, String surname, Date dob, int ordinal, String ph)
    {
        Player result = new Player();
        result.setFirstName( firstName);
        result.setSurname( surname);
        result.setOrdinal( ordinal);
        result.setEmailAddress( firstName + "." + surname + "@" + "nonexistent.com");
        result.setDateOfBirth( dob);
        result.setContactPhoneNumber( ph);
        newPlayers.add( result);
        return result;
    }

    /**
     * Initially set lookups to NULL to avoid NPEs.
     */
//    private Team createTeam()
//    {
//        Team result = new Team();
//        result.setDivision((Division) Utils.getFromList(superSixLookups.get( SuperSixDomainLookupEnum.DIVISION), ExpenseType.NULL));
//        getCurrentSeason().addTeam( result);
//        return result;
//    }

    TeamCompetitionSeason createTeamCompetitionSeason(String name, Division division, CompetitionSeason competitionSeason)
    {
        TeamCompetitionSeason result = new TeamCompetitionSeason();
        result.setTeam( 
                createTeam( name));
        result.setDivision( division);
        competitionSeason.addTeamCompetitionSeason( result);
        return result;
    }
    
    Team createTeam( String name)
    {
        Team result = getKnownTeam( name);
        if(result == null)
        {
            result = new Team( name);
            result.setName( name);
        }
        return result;
    }

    TeamCompetitionSeason createNullTeam()
    {
        TeamCompetitionSeason result = TeamCompetitionSeason.NULL; //Never use NULL again, always use isDummy()
        result.setDummy(true);
        return result;
    }

    private static CompetitionSeason createTuesday2006Season()
    {
        CompetitionSeason result = new CompetitionSeason();
        result.setStartDate( TimeUtils.getDate( 23, Calendar.AUGUST, 2006));
        result.setEndDate( TimeUtils.getDate( 12, Calendar.DECEMBER, 2006));
        result.addDivision( Division.DIVISION_MALE);
        result.addDivision( Division.DIVISION_FEMALE);
        result.setCompetition( Competition.CHAMPIONSHIP_TUESDAY);
        result.setSeasonYear( SeasonYear.SEASON_2006);
        return result;
    }

    private static CompetitionSeason createTuesday2007Season()
    {
        CompetitionSeason result = new CompetitionSeason();
        result.setStartDate( TimeUtils.getDate( 16, Calendar.JANUARY, 2007));
        result.setEndDate( TimeUtils.getDate( 13, Calendar.MARCH, 2007));
        result.addDivision( Division.DIVISION_MALE);
        result.addDivision( Division.DIVISION_FEMALE);
        result.setCompetition( Competition.CHAMPIONSHIP_TUESDAY);
        result.setSeasonYear( SeasonYear.SEASON_2007);
        return result;
    }
    
    private static CompetitionSeason createWednesday2006Season()
    {
        CompetitionSeason result = new CompetitionSeason();
        result.setStartDate( TimeUtils.getDate( 4, Calendar.OCTOBER, 2006));
        result.setEndDate( TimeUtils.getDate( 20, Calendar.DECEMBER, 2006));
        result.addDivision( Division.FIRST_DIVISION);
        result.setCompetition( Competition.PREMIER_LEAGUE_WEDNESDAY);
        result.setSeasonYear( SeasonYear.SEASON_2006);
        return result;
    }

    private static CompetitionSeason createWednesday2007Season()
    {
        CompetitionSeason result = new CompetitionSeason();
        result.setStartDate( TimeUtils.getDate( 17, Calendar.JANUARY, 2007));
        result.setEndDate( TimeUtils.getDate( 14, Calendar.MARCH, 2007));
        result.addDivision( Division.FIRST_DIVISION);
        result.setCompetition( Competition.PREMIER_LEAGUE_WEDNESDAY);
        result.setSeasonYear( SeasonYear.SEASON_2007);
        return result;
    }
    
    Meet createMeet( int roundNum, Date date)
    {
        Meet result = new Meet();
        result.setOrdinal(roundNum);
        result.setDate( date);
        return result;
    }

    Game createMatch( Division division, String t1, String t2, KickOffTime kot, Pitch pitch, int t1Score, int t2Score)
    {
        Game result = new Game();
        TeamCompetitionSeason teamOne = getTeamCompetitionSeason( t1);
        TeamCompetitionSeason teamTwo = getTeamCompetitionSeason( t2);
        //To do this check would need three types of withdrawal - past, future, all. eg in future all games past a
        //withdrawal date would be forfeits for the opposition. 
        chkWithdrawnScore( teamOne, teamTwo, t1Score, t2Score);
        result.setDivision( division);
        teamOne.addTeamOneMatch( result);
        teamTwo.addTeamTwoMatch( result);
        result.setKickOffTime( kot);
        result.setPitch( pitch);
        result.setTeamOneGoals( t1Score);
        result.setTeamTwoGoals( t2Score);
        return result;
    }
    
    private static void chkWithdrawnScore( TeamCompetitionSeason teamOne, TeamCompetitionSeason teamTwo, int t1Score, int t2Score)
    {
        if(teamOne.isWithdrawnFromDivision())
        {
            chkWithdrawnScoreOk( teamOne, teamTwo, t1Score, t2Score);
        }
        else if(teamTwo.isWithdrawnFromDivision())
        {
            chkWithdrawnScoreOk( teamTwo, teamOne, t2Score, t1Score);
        }
    }

    private static void chkWithdrawnScoreOk( TeamCompetitionSeason withdrawnTeam, TeamCompetitionSeason otherTeam, int withdrawScore, int otherScore)
    {
        if(withdrawScore != WITHDRAWN_SCORE)
        {
            Err.error( "Withdrawn team " + withdrawnTeam + " must be given score of FORFEIT_SCORE against " + otherTeam);
        }
        if(otherScore != 0)
        {
            Err.error( "Other team " + otherTeam + " must be given score of 0 against withdrawn " + withdrawnTeam);
        }
    }
    
    private UserDetails createUser( String user, String pass)
    {
        UserDetails result = new UserDetails();
        result.setUsername( user);
        result.setPassword( pass);
        result.setDatabaseUsername( "ro-ot");
        return result;
    }
    
    private static boolean teamsPlaying( String t1, String t2, Game game)
    {
        boolean result = false;
        if(game.getTeamOne().getName().equals( t1) && game.getTeamTwo().getName().equals( t2))
        {
            result = true;
        }
        else if(game.getTeamOne().getName().equals( t2) && game.getTeamTwo().getName().equals( t1))
        {
            result = true;
        }
        return result;
    }
    
    /**
     * Only exists to make calling code easier to read
     */
    Object obtainLOV(Object typeWant)
    {
        Object result = null;
        if(typeWant instanceof Division)
        {
            result = Utils.getFromList(superSixLookups.get( SuperSixDomainLookupEnum.DIVISION), typeWant);
        }
        else
        {
            Err.error("Populate not yet supported for " + typeWant);
        }
        if(result == null)
        {
            Err.error("Got a null when wanted a " + typeWant);
        }
        return result;
    }
}
