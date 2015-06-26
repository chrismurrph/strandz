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

import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.Player;
import org.strandz.data.supersix.objects.Meet;
import org.strandz.data.supersix.objects.KickOffTime;
import org.strandz.data.supersix.objects.Pitch;
import org.strandz.data.supersix.objects.Team;
import org.strandz.data.supersix.objects.Game;
import org.strandz.lgpl.util.TimeUtils;

import java.util.Calendar;
import java.util.Date;

public class DemoDataFor2006
{
    private SuperSixDemoData demoData;
    private static String BURWOOD = "NK Burwood";        
    private static String HIGH = "High Rollers";
    private static String BOCCA = "Bocca Seniors";
    private static String CASE_CLOSED = "Case Closed";
    private static String SYDNEY = "Sydney FC";
    private static String FANFALLI = "Fanfalli";
    private static String AZZURI = "Azzuri";
    private static String SUMMER = "Summer Hill";
    private static String MACHINES = "The Machines";
    private static String SHORT_AND_CURLY = "The Short and Curly";
    
    DemoDataFor2006( SuperSixDemoData demoData)
    {
        this.demoData = demoData;
    }
    
    private Team createTeam( String name)
    {
        return demoData.createTeam( name);
    }
    
    private Player createPlayer( String firstName, String surname, Date dob, int ordinal)
    {
        return demoData.createPlayer( firstName, surname, dob, ordinal);
    }

    private Player createPlayer( String firstName, String surname, Date dob, int ordinal, String ph)
    {
        return demoData.createPlayer( firstName, surname, dob, ordinal, ph);
    }
    
    private Meet createMeet( int roundNum, Date date)
    {
        return demoData.createMeet( roundNum, date);
    }

    private Game createMatch( Division division, String t1, String t2, KickOffTime kot, Pitch pitch, int t1Score, int t2Score)
    {
        return demoData.createMatch(division, t1, t2, kot, pitch, t1Score, t2Score);
    }
    
    private TeamCompetitionSeason createTeamCompetitionSeason(String name, Division division, CompetitionSeason competitionSeason)
    {
        return demoData.createTeamCompetitionSeason( name, division, competitionSeason);
    }
    
    public void do2006TuesPopulation( CompetitionSeason tuesComp2006)
    {
        demoData.currentCompetition = tuesComp2006.getCompetition();
        demoData.currentSeasonYear = tuesComp2006.getSeasonYear();
        
        demoData.newSeasons.add(tuesComp2006);
        
        TeamCompetitionSeason nullTeam = demoData.createNullTeam();
        demoData.newTeams.add(nullTeam); //0
        TeamCompetitionSeason team1 = createTeamCompetitionSeason( "Balmain Bugs", Division.DIVISION_MALE, tuesComp2006);
        demoData.newTeams.add( team1);
        TeamCompetitionSeason team2 = createTeamCompetitionSeason( SuperSixDemoData.LATE_NIGHT_SBS, Division.DIVISION_MALE, tuesComp2006);
        demoData.newTeams.add( team2);
        TeamCompetitionSeason team3 = createTeamCompetitionSeason( "Storm Troopers", Division.DIVISION_MALE, tuesComp2006);
        demoData.newTeams.add( team3);
        int ordinal = 0;
        TeamCompetitionSeason team4 = createTeamCompetitionSeason( "The Undead Kenyans", Division.DIVISION_MALE, tuesComp2006);
        Player kenyansCaptain = createPlayer( "Chris", "Murphy",
                                              TimeUtils.getDate( 4, Calendar.JULY, 1967), ++ordinal,
                                              "0403 162 669");
        team4.setCaptain( kenyansCaptain);
        team4.addPlayer( kenyansCaptain);
        team4.addPlayer( createPlayer( "Mike", "Daughton", TimeUtils.getDate( 8, Calendar.NOVEMBER, 1969), ++ordinal));
        team4.addPlayer( createPlayer( "Steve", "Rogers", TimeUtils.getDate( 15, Calendar.OCTOBER, 1973), ++ordinal));
        team4.addPlayer( createPlayer( "Richard", "Mallesch", TimeUtils.getDate( 19, Calendar.NOVEMBER, 1973), ++ordinal));
        team4.addPlayer( createPlayer( "James", "Elphick",  TimeUtils.getDate( 20, Calendar.JANUARY, 1981), ++ordinal));
        team4.addPlayer( createPlayer( "Luke", "Shelton", TimeUtils.getDate( 30, Calendar.MARCH, 1972), ++ordinal));
        team4.addPlayer( createPlayer( "Matt", "Longbottom", TimeUtils.getDate( 23, Calendar.NOVEMBER, 1971), ++ordinal));
        team4.addPlayer( createPlayer( "Andrew", "Curnow", TimeUtils.getDate( 26, Calendar.NOVEMBER, 1969), ++ordinal));
        demoData.newTeams.add( team4);
        TeamCompetitionSeason team5 = createTeamCompetitionSeason( "That's Not Cricket", Division.DIVISION_MALE, tuesComp2006);
        TeamCompetitionSeason team6 = createTeamCompetitionSeason( "Croydon Massive", Division.DIVISION_MALE, tuesComp2006);
        TeamCompetitionSeason team7 = createTeamCompetitionSeason( "Portugal", Division.DIVISION_MALE, tuesComp2006);
        TeamCompetitionSeason team8 = createTeamCompetitionSeason( SuperSixDemoData.ITALIAN_WARRIORS, Division.DIVISION_MALE, tuesComp2006);
        TeamCompetitionSeason team9 = createTeamCompetitionSeason( SuperSixDemoData.IBERIA, Division.DIVISION_MALE, tuesComp2006);
        TeamCompetitionSeason team10 = createTeamCompetitionSeason( "Melchester Old Boys", Division.DIVISION_MALE, tuesComp2006);
        TeamCompetitionSeason team11 = createTeamCompetitionSeason( "Balmain Wanderers 1", Division.DIVISION_MALE, tuesComp2006);
        TeamCompetitionSeason team12 = createTeamCompetitionSeason( SuperSixDemoData.MAGPIES, Division.DIVISION_MALE, tuesComp2006);
        team12.setWithdrawnFromDivision( true);
        TeamCompetitionSeason team13 = createTeamCompetitionSeason( "Balmain Wanderers 2", Division.DIVISION_MALE, tuesComp2006);
        TeamCompetitionSeason team14 = createTeamCompetitionSeason( "Sydney Uni", Division.DIVISION_MALE, tuesComp2006);
        demoData.newTeams.add( team5);
        demoData.newTeams.add( team6);
        demoData.newTeams.add( team7);
        demoData.newTeams.add( team8);
        demoData.newTeams.add( team9);
        demoData.newTeams.add( team10);
        demoData.newTeams.add( team11);
        demoData.newTeams.add( team12);
        demoData.newTeams.add( team13);
        demoData.newTeams.add( team14);

        team1 = createTeamCompetitionSeason( "Feisty Divas", Division.DIVISION_FEMALE, tuesComp2006);
        team2 = createTeamCompetitionSeason( "Absolutely Fabulous", Division.DIVISION_FEMALE, tuesComp2006);
        team3 = createTeamCompetitionSeason( "Balmain Bandits", Division.DIVISION_FEMALE, tuesComp2006);
        team4 = createTeamCompetitionSeason( "Shezzas", Division.DIVISION_FEMALE, tuesComp2006);
        team5 = createTeamCompetitionSeason( "Balmain Broads", Division.DIVISION_FEMALE, tuesComp2006);
        team6 = createTeamCompetitionSeason( "Balmain Tigers", Division.DIVISION_FEMALE, tuesComp2006);
        team7 = createTeamCompetitionSeason( "The Coronas", Division.DIVISION_FEMALE, tuesComp2006);
        team8 = createTeamCompetitionSeason( "Wo-Man United", Division.DIVISION_FEMALE, tuesComp2006);
        team9 = createTeamCompetitionSeason( "Chilli Cats", Division.DIVISION_FEMALE, tuesComp2006);
        team10 = createTeamCompetitionSeason( "Fyshwick FC", Division.DIVISION_FEMALE, tuesComp2006);
        demoData.newTeams.add( team1);
        demoData.newTeams.add( team2);
        demoData.newTeams.add( team3);
        demoData.newTeams.add( team4);
        demoData.newTeams.add( team5);
        demoData.newTeams.add( team6);
        demoData.newTeams.add( team7);
        demoData.newTeams.add( team8);
        demoData.newTeams.add( team9);
        demoData.newTeams.add( team10);
        
        Meet tuesRound1 = createMeet( 1, TimeUtils.getDate( 29, Calendar.AUGUST, 2006));
        tuesComp2006.addMeet( tuesRound1);
        Meet tuesRound2 = createMeet( 2, TimeUtils.getDate( 5, Calendar.SEPTEMBER, 2006));
        tuesComp2006.addMeet(tuesRound2);
        Meet tuesRound3 = createMeet( 3, TimeUtils.getDate( 19, Calendar.SEPTEMBER, 2006));
        tuesComp2006.addMeet( tuesRound3);
        Meet tuesRound4 = createMeet( 4, TimeUtils.getDate( 26, Calendar.SEPTEMBER, 2006));
        tuesComp2006.addMeet( tuesRound4);
        Meet tuesRound5 = createMeet( 5, TimeUtils.getDate( 3, Calendar.OCTOBER, 2006));
        tuesComp2006.addMeet( tuesRound5);        
        Meet tuesRound6 = createMeet( 6, TimeUtils.getDate( 10, Calendar.OCTOBER, 2006));
        tuesComp2006.addMeet( tuesRound6);
        Meet tuesRound7 = createMeet( 7, TimeUtils.getDate( 17, Calendar.OCTOBER, 2006));
        tuesComp2006.addMeet( tuesRound7);
        Meet tuesRound8 = createMeet( 8, TimeUtils.getDate( 24, Calendar.OCTOBER, 2006));
        tuesComp2006.addMeet( tuesRound8);
        Meet tuesRound9 = createMeet( 9, TimeUtils.getDate( 31, Calendar.OCTOBER, 2006));
        tuesComp2006.addMeet( tuesRound9);
//        Meet tuesRound10 = createMeet( 10, TimeUtils.getDate( 7, Calendar.NOVEMBER, 2006));
//        tuesComp.addMeet( tuesRound10);
        Meet tuesRound10 = createMeet( 10, TimeUtils.getDate( 14, Calendar.NOVEMBER, 2006));
        tuesComp2006.addMeet( tuesRound10);
        Meet tuesRound11 = createMeet( 11, TimeUtils.getDate( 21, Calendar.NOVEMBER, 2006));
        tuesComp2006.addMeet( tuesRound11);
        Meet tuesRound12 = createMeet( 12, TimeUtils.getDate( 28, Calendar.NOVEMBER, 2006));
        tuesComp2006.addMeet( tuesRound12);
        Meet tuesRound13 = createMeet( 13, TimeUtils.getDate( 5, Calendar.DECEMBER, 2006));
        tuesComp2006.addMeet( tuesRound13);
                
        //MALES
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Bugs", SuperSixDemoData.LATE_NIGHT_SBS, 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 1, 2));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, "Storm Troopers", "The Undead Kenyans", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 8, 1));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, "That's Not Cricket", "Croydon Massive", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 4, 3));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, "Portugal", SuperSixDemoData.ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 4, 3));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.IBERIA, "Melchester Old Boys", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 4, 0));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 1", SuperSixDemoData.MAGPIES, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 0, SuperSixDemoData.WITHDRAWN_SCORE));
        tuesRound1.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 2", "Sydney Uni", 
                                     KickOffTime.KICK_OFF_0950, Pitch.PITCH_ONE, 2, 3));
        
        tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, "Portugal", "Croydon Massive", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 7, 1));
        tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.ITALIAN_WARRIORS, "Storm Troopers", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 2, 0));
        tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 1", "That's Not Cricket", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 4, 1));
        tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, "Melchester Old Boys", "The Undead Kenyans", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 6, 0));
        //tuesRound2.addMatch( createMatch( Division.DIVISION_MALE, MAGPIES, "Sydney Uni", 
        //                             KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 2, 1));
        
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, "Storm Troopers", "Portugal", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 2, 2));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, "That's Not Cricket", SuperSixDemoData.ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 5));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, "The Undead Kenyans", "Balmain Wanderers 1", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 0, 8));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, "Sydney Uni", "Melchester Old Boys", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 0, 3));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.LATE_NIGHT_SBS, SuperSixDemoData.MAGPIES, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 0, SuperSixDemoData.WITHDRAWN_SCORE));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Bugs", SuperSixDemoData.IBERIA, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 0, 3));
        tuesRound3.addMatch( createMatch( Division.DIVISION_MALE, "Croydon Massive", "Balmain Wanderers 2", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 1, 3));
        
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.IBERIA, "Croydon Massive", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 3, 1));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 2", "Storm Troopers", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 0, 2));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, "Portugal", "That's Not Cricket", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 6, 1));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.ITALIAN_WARRIORS, "The Undead Kenyans", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 4, 1));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 1", "Sydney Uni", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 3, 1));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, "Melchester Old Boys", SuperSixDemoData.LATE_NIGHT_SBS, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 2, 3));
        tuesRound4.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.MAGPIES, "Balmain Bugs", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, SuperSixDemoData.WITHDRAWN_SCORE, 0));
        
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, "The Undead Kenyans", "Portugal", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 0, 4));
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, "Sydney Uni", SuperSixDemoData.ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 2, 3));
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.LATE_NIGHT_SBS, "Balmain Wanderers 1", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 3, 1));
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Bugs", "Melchester Old Boys", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 1, 1));
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, "Croydon Massive", SuperSixDemoData.MAGPIES, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 0, SuperSixDemoData.WITHDRAWN_SCORE));
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, "Storm Troopers", SuperSixDemoData.IBERIA, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 0, 4));
        tuesRound5.addMatch( createMatch( Division.DIVISION_MALE, "That's Not Cricket", "Balmain Wanderers 2", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 1, 5));

        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.ITALIAN_WARRIORS, SuperSixDemoData.LATE_NIGHT_SBS, 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 3, 2));
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 1", "Balmain Bugs", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 4, 2));
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, "Melchester Old Boys", "Croydon Massive", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 4, 1));
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.MAGPIES, "Storm Troopers", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, SuperSixDemoData.WITHDRAWN_SCORE, 0));
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.IBERIA, "That's Not Cricket", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 5, 0));
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 2", "The Undead Kenyans", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 6, 1));
        tuesRound6.addMatch( createMatch( Division.DIVISION_MALE, "Portugal", "Sydney Uni", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 3, 0));

        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.IBERIA, "Balmain Wanderers 2", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 4, 0));
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, "Croydon Massive", "Balmain Bugs", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 0, 2));
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, "That's Not Cricket", "Storm Troopers", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 2, 4));
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, "Sydney Uni", "The Undead Kenyans", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 4, 2));
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, "Portugal", SuperSixDemoData.LATE_NIGHT_SBS, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 1, 3));
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.ITALIAN_WARRIORS, "Balmain Wanderers 1", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 0, 2));
        tuesRound7.addMatch( createMatch( Division.DIVISION_MALE, "Melchester Old Boys", SuperSixDemoData.MAGPIES, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 0, SuperSixDemoData.WITHDRAWN_SCORE));

        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.LATE_NIGHT_SBS, "Croydon Massive", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 6, 1));
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, "Sydney Uni", "Storm Troopers",
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 1));
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, "The Undead Kenyans", "That's Not Cricket", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 1, 1));
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Bugs", SuperSixDemoData.ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 1, 5));
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 1", "Portugal", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 2, 2));
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, "Melchester Old Boys", "Balmain Wanderers 2", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 0, 2));
        tuesRound8.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.MAGPIES, SuperSixDemoData.IBERIA,
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, SuperSixDemoData.WITHDRAWN_SCORE, 0));

        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, "That's Not Cricket", "Sydney Uni", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 2, 2));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.IBERIA, SuperSixDemoData.ITALIAN_WARRIORS,
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 2));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, "Storm Troopers", SuperSixDemoData.LATE_NIGHT_SBS, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 1, 2));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Bugs", "The Undead Kenyans", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 4, 1));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 1", "Croydon Massive", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 3, 2));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, "Portugal", "Melchester Old Boys", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 2, 1));
        tuesRound9.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 2", SuperSixDemoData.MAGPIES,
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 0, SuperSixDemoData.WITHDRAWN_SCORE));

        tuesRound10.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.LATE_NIGHT_SBS, "That's Not Cricket", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 2, 1));
        tuesRound10.addMatch( createMatch( Division.DIVISION_MALE, "Storm Troopers", "Balmain Bugs",
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 2));
        tuesRound10.addMatch( createMatch( Division.DIVISION_MALE, "Croydon Massive", "Sydney Uni", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 0, 5));
//        tuesRound10.addMatch( createMatch( Division.DIVISION_MALE, "The Undead Kenyans", IBERIA, 
//                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, FORFEIT_SCORE, FORFEIT_SCORE));
        tuesRound10.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.MAGPIES, "Portugal", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, SuperSixDemoData.WITHDRAWN_SCORE, 0));
        tuesRound10.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.ITALIAN_WARRIORS, "Melchester Old Boys", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 5, 1));
        tuesRound10.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 2", "Balmain Wanderers 1",
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 1, 3));

        tuesRound11.addMatch( createMatch( Division.DIVISION_MALE, "Storm Troopers", "Croydon Massive", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 4, 0));
        tuesRound11.addMatch( createMatch( Division.DIVISION_MALE, "Melchester Old Boys", "Balmain Wanderers 1",
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 3));
        tuesRound11.addMatch( createMatch( Division.DIVISION_MALE, "Portugal", SuperSixDemoData.IBERIA, 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 2, 1));
        tuesRound11.addMatch( createMatch( Division.DIVISION_MALE, "That's Not Cricket", SuperSixDemoData.MAGPIES, 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 0, SuperSixDemoData.WITHDRAWN_SCORE));
        tuesRound11.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.LATE_NIGHT_SBS, "The Undead Kenyans", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 0, 1));
        tuesRound11.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 2", SuperSixDemoData.ITALIAN_WARRIORS, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 1, 3));
        tuesRound11.addMatch( createMatch( Division.DIVISION_MALE, "Sydney Uni", "Balmain Bugs",
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 3, 2));
        
        tuesRound12.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 2", "Portugal", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 3, 4));
        tuesRound12.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 1", SuperSixDemoData.IBERIA,
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_TWO, 1, 2));
        tuesRound12.addMatch( createMatch( Division.DIVISION_MALE, "Storm Troopers", "Melchester Old Boys", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_ONE, 1, 1));
        tuesRound12.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.LATE_NIGHT_SBS, SuperSixDemoData.IBERIA, 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_TWO, 0, 3));
        tuesRound12.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Wanderers 2", "Balmain Bugs", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 1, 2));
        tuesRound12.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.LATE_NIGHT_SBS, "Sydney Uni", 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 3, 0));
        tuesRound12.addMatch( createMatch( Division.DIVISION_MALE, "The Undead Kenyans", "Croydon Massive",
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 1, 4));
                
        tuesRound13.addMatch( createMatch( Division.DIVISION_MALE, "Balmain Bugs", "Portugal", 
                                     KickOffTime.KICK_OFF_0600, Pitch.NULL, 0, 6));
        tuesRound13.addMatch( createMatch( Division.DIVISION_MALE, "Croydon Massive", SuperSixDemoData.ITALIAN_WARRIORS,
                                     KickOffTime.KICK_OFF_0600, Pitch.NULL, 1, 4));
        tuesRound13.addMatch( createMatch( Division.DIVISION_MALE, "Storm Troopers", "Balmain Wanderers 1", 
                                     KickOffTime.KICK_OFF_0600, Pitch.NULL, 2, 4));
        tuesRound13.addMatch( createMatch( Division.DIVISION_MALE, "That's Not Cricket", "Melchester Old Boys", 
                                     KickOffTime.KICK_OFF_0640, Pitch.NULL, 1, 3));
//        tuesRound13.addMatch( createMatch( Division.DIVISION_MALE, "The Undead Kenyans", MAGPIES, 
//                                     KickOffTime.KICK_OFF_0640, Pitch.NULL, FORFEIT_SCORE, FORFEIT_SCORE));
        tuesRound13.addMatch( createMatch( Division.DIVISION_MALE, "Sydney Uni", SuperSixDemoData.IBERIA, 
                                     KickOffTime.KICK_OFF_0640, Pitch.NULL, 0, 4));
        tuesRound13.addMatch( createMatch( Division.DIVISION_MALE, SuperSixDemoData.LATE_NIGHT_SBS, "Balmain Wanderers 2",
                                     KickOffTime.KICK_OFF_0720, Pitch.NULL, 0, 1));
        tuesRound13.addMatch( createMatch( Division.DIVISION_MALE, "The Undead Kenyans", SuperSixDemoData.IBERIA,
                                     KickOffTime.KICK_OFF_0800, Pitch.NULL, 1, 9));
        //FEMALES        
        
        tuesRound1.addMatch( createMatch( Division.DIVISION_FEMALE, "Feisty Divas", "Absolutely Fabulous", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 0, 3));
        tuesRound1.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Bandits", "Shezzas", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 0, 0));
        tuesRound1.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Broads", "Balmain Tigers", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 0, 3));
        tuesRound1.addMatch( createMatch( Division.DIVISION_FEMALE, "The Coronas", "Wo-Man United", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_ONE, 0, 6));
        tuesRound1.addMatch( createMatch( Division.DIVISION_FEMALE, "Chilli Cats", "Fyshwick FC", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 0, 5));
        
        tuesRound2.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Tigers", "Chilli Cats", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 1, 1));
        tuesRound2.addMatch( createMatch( Division.DIVISION_FEMALE, "Shezzas", "Wo-Man United", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 1, 1));
        tuesRound2.addMatch( createMatch( Division.DIVISION_FEMALE, "The Coronas", "Balmain Bandits", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 0, 2));
        tuesRound2.addMatch( createMatch( Division.DIVISION_FEMALE, "Fyshwick FC", "Feisty Divas", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_ONE, 9, 0));
        tuesRound2.addMatch( createMatch( Division.DIVISION_FEMALE, "Absolutely Fabulous", "Balmain Broads", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 1, 1));

        tuesRound3.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Broads", "Shezzas", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 0, 3));
        tuesRound3.addMatch( createMatch( Division.DIVISION_FEMALE, "Feisty Divas", "Wo-Man United", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 0, 5));
        tuesRound3.addMatch( createMatch( Division.DIVISION_FEMALE, "Chilli Cats", "Absolutely Fabulous", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 1));
        tuesRound3.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Bandits", "Fyshwick FC", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_ONE, 0, 5));
        tuesRound3.addMatch( createMatch( Division.DIVISION_FEMALE, "The Coronas", "Balmain Tigers", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 2, 7));
        
        tuesRound4.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Broads", "Balmain Bandits", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 0, 2));
        tuesRound4.addMatch( createMatch( Division.DIVISION_FEMALE, "Wo-Man United", "Balmain Tigers", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 2, 6));
        tuesRound4.addMatch( createMatch( Division.DIVISION_FEMALE, "Shezzas", "The Coronas", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 3));
        tuesRound4.addMatch( createMatch( Division.DIVISION_FEMALE, "Feisty Divas", "Chilli Cats", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_ONE, 0, 1));
        tuesRound4.addMatch( createMatch( Division.DIVISION_FEMALE, "Absolutely Fabulous", "Fyshwick FC", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 0, 5));

        tuesRound5.addMatch( createMatch( Division.DIVISION_FEMALE, "Absolutely Fabulous", "The Coronas", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 2, 2));
        tuesRound5.addMatch( createMatch( Division.DIVISION_FEMALE, "Feisty Divas", "Balmain Broads", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 2, 0));
        tuesRound5.addMatch( createMatch( Division.DIVISION_FEMALE, "Shezzas", "Chilli Cats", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 2));
        tuesRound5.addMatch( createMatch( Division.DIVISION_FEMALE, "Fyshwick FC", "Balmain Tigers", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_ONE, 6, 0));
        tuesRound5.addMatch( createMatch( Division.DIVISION_FEMALE, "Wo-Man United", "Balmain Bandits", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 3, 0));

        tuesRound6.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Broads", "Wo-Man United", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 0, 3));
        tuesRound6.addMatch( createMatch( Division.DIVISION_FEMALE, "Shezzas", "Absolutely Fabulous", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 2, 3));
        tuesRound6.addMatch( createMatch( Division.DIVISION_FEMALE, "The Coronas", "Fyshwick FC", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 0, 7));
        tuesRound6.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Tigers", "Feisty Divas", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_ONE, 7, 1));
        tuesRound6.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Bandits", "Chilli Cats", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 0, 3));

        tuesRound7.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Tigers", "Shezzas", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 2, 0));
        tuesRound7.addMatch( createMatch( Division.DIVISION_FEMALE, "The Coronas", "Feisty Divas", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 1, 0));
        tuesRound7.addMatch( createMatch( Division.DIVISION_FEMALE, "Chilli Cats", "Balmain Broads", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 2, 0));
        tuesRound7.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Bandits", "Absolutely Fabulous", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_ONE, 1, 3));
        tuesRound7.addMatch( createMatch( Division.DIVISION_FEMALE, "Fyshwick FC", "Wo-Man United", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 6, 0));

        tuesRound8.addMatch( createMatch( Division.DIVISION_FEMALE, "Feisty Divas", "Balmain Bandits", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 1, 2));
        tuesRound8.addMatch( createMatch( Division.DIVISION_FEMALE, "Wo-Man United", "Chilli Cats", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 0, 2));
        tuesRound8.addMatch( createMatch( Division.DIVISION_FEMALE, "Absolutely Fabulous", "Balmain Tigers", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 2));
        tuesRound8.addMatch( createMatch( Division.DIVISION_FEMALE, "Shezzas", "Fyshwick FC", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_ONE, 0, 10));
        tuesRound8.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Broads", "The Coronas", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 0, 4));

        tuesRound9.addMatch( createMatch( Division.DIVISION_FEMALE, "Wo-Man United", "Absolutely Fabulous", 
                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, 3, 2));
        tuesRound9.addMatch( createMatch( Division.DIVISION_FEMALE, "Fyshwick FC", "Balmain Broads", 
                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, 6, 0));
        tuesRound9.addMatch( createMatch( Division.DIVISION_FEMALE, "Feisty Divas", "Shezzas", 
                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, 1, 2));
        tuesRound9.addMatch( createMatch( Division.DIVISION_FEMALE, "Chilli Cats", "The Coronas", 
                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_ONE, 1, 0));
        tuesRound9.addMatch( createMatch( Division.DIVISION_FEMALE, "Balmain Bandits", "Balmain Tigers", 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 0, 1));

//        tuesRound10.addMatch( createMatch( Division.DIVISION_FEMALE, "", "", 
//                                     KickOffTime.KICK_OFF_0610, Pitch.PITCH_TWO, FORFEIT_SCORE, FORFEIT_SCORE));
//        tuesRound10.addMatch( createMatch( Division.DIVISION_FEMALE, "", "", 
//                                     KickOffTime.KICK_OFF_0650, Pitch.PITCH_ONE, FORFEIT_SCORE, FORFEIT_SCORE));
//        tuesRound10.addMatch( createMatch( Division.DIVISION_FEMALE, "", "", 
//                                     KickOffTime.KICK_OFF_0725, Pitch.PITCH_TWO, FORFEIT_SCORE, FORFEIT_SCORE));
//        tuesRound10.addMatch( createMatch( Division.DIVISION_FEMALE, "", "", 
//                                     KickOffTime.KICK_OFF_0805, Pitch.PITCH_ONE, FORFEIT_SCORE, FORFEIT_SCORE));
//        tuesRound10.addMatch( createMatch( Division.DIVISION_FEMALE, "", "", 
//                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, FORFEIT_SCORE, FORFEIT_SCORE));
    }
    
    public void do2006WedsPopulation( CompetitionSeason wedsComp2006)
    {
        demoData.currentCompetition = wedsComp2006.getCompetition();
        demoData.currentSeasonYear = wedsComp2006.getSeasonYear();
        
        demoData.newSeasons.add(wedsComp2006);
        
        TeamCompetitionSeason team1 = createTeamCompetitionSeason( BURWOOD, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team2 = createTeamCompetitionSeason( SuperSixDemoData.OTHER_TEAM, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team3 = createTeamCompetitionSeason( SuperSixDemoData.RED_DEVILS, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team4 = createTeamCompetitionSeason( HIGH, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team5 = createTeamCompetitionSeason( BOCCA, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team6 = createTeamCompetitionSeason( CASE_CLOSED, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team7 = createTeamCompetitionSeason( SYDNEY, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team8 = createTeamCompetitionSeason( FANFALLI, Division.FIRST_DIVISION, wedsComp2006);
        team8.setWithdrawnFromDivision( true);
        TeamCompetitionSeason team9 = createTeamCompetitionSeason( SuperSixDemoData.LIGEM_AGAIN, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team10 = createTeamCompetitionSeason( AZZURI, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team11 = createTeamCompetitionSeason( SUMMER, Division.FIRST_DIVISION, wedsComp2006);
        team11.setWithdrawnFromDivision( true);
        TeamCompetitionSeason team12 = createTeamCompetitionSeason( SuperSixDemoData.INTER, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team13 = createTeamCompetitionSeason( SuperSixDemoData.WANDERERS_A, Division.FIRST_DIVISION, wedsComp2006);        
        TeamCompetitionSeason team14 = createTeamCompetitionSeason( MACHINES, Division.FIRST_DIVISION, wedsComp2006);
        team14.setWithdrawnFromDivision( true);
        TeamCompetitionSeason team15 = createTeamCompetitionSeason( SHORT_AND_CURLY, Division.FIRST_DIVISION, wedsComp2006);        
        demoData.newTeams.add( team1);
        demoData.newTeams.add( team2);
        demoData.newTeams.add( team3);
        demoData.newTeams.add( team4);
        demoData.newTeams.add( team5);
        demoData.newTeams.add( team6);
        demoData.newTeams.add( team7);
        demoData.newTeams.add( team8);
        demoData.newTeams.add( team9);
        demoData.newTeams.add( team10);
        demoData.newTeams.add( team11);
        demoData.newTeams.add( team12);
        demoData.newTeams.add( team13);
        demoData.newTeams.add( team14);
        demoData.newTeams.add( team15);
        
        Meet wedsRound1 = createMeet( 1, TimeUtils.getDate( 4, Calendar.OCTOBER, 2006));
        wedsComp2006.addMeet( wedsRound1);        
        Meet wedsRound2 = createMeet( 2, TimeUtils.getDate( 11, Calendar.OCTOBER, 2006));
        wedsComp2006.addMeet( wedsRound2);        
        Meet wedsRound3 = createMeet( 3, TimeUtils.getDate( 18, Calendar.OCTOBER, 2006));
        wedsComp2006.addMeet( wedsRound3);        
        Meet wedsRound4 = createMeet( 4, TimeUtils.getDate( 25, Calendar.OCTOBER, 2006));
        wedsComp2006.addMeet( wedsRound4);        
        Meet wedsRound5 = createMeet( 5, TimeUtils.getDate( 1, Calendar.NOVEMBER, 2006));
        wedsComp2006.addMeet( wedsRound5);        
        Meet wedsRound6 = createMeet( 6, TimeUtils.getDate( 8, Calendar.NOVEMBER, 2006));
        wedsComp2006.addMeet( wedsRound6);        
        Meet wedsRound7 = createMeet( 7, TimeUtils.getDate( 15, Calendar.NOVEMBER, 2006));
        wedsComp2006.addMeet( wedsRound7);        
        Meet wedsRound8 = createMeet( 8, TimeUtils.getDate( 22, Calendar.NOVEMBER, 2006));
        wedsComp2006.addMeet( wedsRound8);        
        Meet wedsRound9 = createMeet( 9, TimeUtils.getDate( 29, Calendar.NOVEMBER, 2006));
        wedsComp2006.addMeet( wedsRound9);        
        Meet wedsRound10 = createMeet( 10, TimeUtils.getDate( 6, Calendar.DECEMBER, 2006));
        wedsComp2006.addMeet( wedsRound10);        
        Meet wedsRound11 = createMeet( 11, TimeUtils.getDate( 13, Calendar.DECEMBER, 2006));
        wedsComp2006.addMeet( wedsRound11);        
        Meet wedsRound12 = createMeet( 12, TimeUtils.getDate( 20, Calendar.DECEMBER, 2006));
        wedsComp2006.addMeet( wedsRound12);
        
        //WED COMP

        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, BURWOOD, SuperSixDemoData.OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0700, Pitch.PITCH_ONE, 1, 0));
        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.RED_DEVILS, HIGH, 
                                     KickOffTime.KICK_OFF_0700, Pitch.PITCH_TWO, 1, 4));
        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, BOCCA, CASE_CLOSED, 
                                     KickOffTime.KICK_OFF_0740, Pitch.PITCH_ONE, 3, 1));
        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, SYDNEY, FANFALLI, 
                                     KickOffTime.KICK_OFF_0740, Pitch.PITCH_TWO, 0, SuperSixDemoData.WITHDRAWN_SCORE));
        wedsRound1.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.LIGEM_AGAIN, AZZURI, 
                                     KickOffTime.KICK_OFF_0820, Pitch.PITCH_TWO, 2, 2));
        
        
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.OTHER_TEAM, SuperSixDemoData.RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0700, Pitch.PITCH_ONE, 1, 1));
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, HIGH, BOCCA, 
                                     KickOffTime.KICK_OFF_0700, Pitch.PITCH_TWO, 1, 2));
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, CASE_CLOSED, SYDNEY, 
                                     KickOffTime.KICK_OFF_0740, Pitch.PITCH_ONE, 0, 1));
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, FANFALLI, SuperSixDemoData.LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0740, Pitch.PITCH_TWO, SuperSixDemoData.WITHDRAWN_SCORE, 0));
//        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, "", "", 
//                                     KickOffTime.KICK_OFF_0820, Pitch.PITCH_ONE, FORFEIT_SCORE, FORFEIT_SCORE));
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, SUMMER, SuperSixDemoData.INTER, 
                                     KickOffTime.KICK_OFF_0820, Pitch.PITCH_TWO, SuperSixDemoData.WITHDRAWN_SCORE, 0));
        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.WANDERERS_A, BURWOOD, 
                                     KickOffTime.KICK_OFF_0910, Pitch.PITCH_ONE, 0, 5));
//        wedsRound2.addMatch( createMatch( Division.FIRST_DIVISION, "", "", 
//                                     KickOffTime.KICK_OFF_0910, Pitch.PITCH_TWO, FORFEIT_SCORE, FORFEIT_SCORE));

//        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, "", "", 
//                                     KickOffTime.KICK_OFF_0700, Pitch.PITCH_ONE, FORFEIT_SCORE, FORFEIT_SCORE));
//        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, "", "", 
//                                     KickOffTime.KICK_OFF_0700, Pitch.PITCH_TWO, FORFEIT_SCORE, FORFEIT_SCORE));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, BURWOOD, BOCCA, 
                                     KickOffTime.KICK_OFF_0740, Pitch.PITCH_ONE, 2, 1));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, MACHINES, SuperSixDemoData.WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0740, Pitch.PITCH_TWO, SuperSixDemoData.WITHDRAWN_SCORE, 0));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, SHORT_AND_CURLY, AZZURI, 
                                     KickOffTime.KICK_OFF_0820, Pitch.PITCH_ONE, 2, 2));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, FANFALLI, HIGH, 
                                     KickOffTime.KICK_OFF_0820, Pitch.PITCH_TWO, SuperSixDemoData.WITHDRAWN_SCORE, 0));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.OTHER_TEAM, CASE_CLOSED, 
                                     KickOffTime.KICK_OFF_0910, Pitch.PITCH_ONE, 3, 0));
        wedsRound3.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.LIGEM_AGAIN, MACHINES, 
                                     KickOffTime.KICK_OFF_0910, Pitch.PITCH_TWO, 0, SuperSixDemoData.WITHDRAWN_SCORE));

        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.RED_DEVILS, CASE_CLOSED, 
                                     KickOffTime.KICK_OFF_0700, Pitch.PITCH_ONE, 5, 2));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.INTER, SYDNEY, 
                                     KickOffTime.KICK_OFF_0700, Pitch.PITCH_TWO, 3, 1));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, HIGH, SuperSixDemoData.OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0740, Pitch.PITCH_ONE, 5, 1));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, AZZURI, FANFALLI, 
                                     KickOffTime.KICK_OFF_0740, Pitch.PITCH_TWO, 0, SuperSixDemoData.WITHDRAWN_SCORE));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.WANDERERS_A, SHORT_AND_CURLY, 
                                     KickOffTime.KICK_OFF_0820, Pitch.PITCH_ONE, 0, 1));
//        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, "", "", 
//                                     KickOffTime.KICK_OFF_0820, Pitch.PITCH_TWO, FORFEIT_SCORE, FORFEIT_SCORE));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.INTER, BURWOOD, 
                                     KickOffTime.KICK_OFF_0910, Pitch.PITCH_ONE, 2, 3));
        wedsRound4.addMatch( createMatch( Division.FIRST_DIVISION, SHORT_AND_CURLY, MACHINES, 
                                     KickOffTime.KICK_OFF_0910, Pitch.PITCH_TWO, 0, SuperSixDemoData.WITHDRAWN_SCORE));

        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, FANFALLI, SuperSixDemoData.WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_ONE, SuperSixDemoData.WITHDRAWN_SCORE, 0));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.LIGEM_AGAIN, BOCCA, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_TWO, 5, 0));
//        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, "", "", 
//                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_ONE, FORFEIT_SCORE, FORFEIT_SCORE));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.OTHER_TEAM, AZZURI, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_TWO, 4, 2));
//        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, SUMMER, MACHINES, 
//                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_ONE, FORFEIT_SCORE, FORFEIT_SCORE));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, CASE_CLOSED, HIGH, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_TWO, 0, 3));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, BURWOOD, SuperSixDemoData.RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 3, 0));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, SHORT_AND_CURLY, SYDNEY, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 3, 0));
//        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, MACHINES, THE_OTHER_TEAM, 
//                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, FORFEIT_SCORE, FORFEIT_SCORE));
        wedsRound5.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.RED_DEVILS, SuperSixDemoData.INTER, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 1, 1));
        
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.WANDERERS_A, SuperSixDemoData.LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_ONE, 3, 2));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, BURWOOD, CASE_CLOSED, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_TWO, 2, 1));
//        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, "", "", 
//                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_ONE, FORFEIT_SCORE, FORFEIT_SCORE));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.INTER, HIGH, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_TWO, 2, 0));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, CASE_CLOSED, SHORT_AND_CURLY, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_ONE, 0, 4));
//        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, "", "", 
//                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_TWO, FORFEIT_SCORE, FORFEIT_SCORE));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.RED_DEVILS, BOCCA, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 3, 1));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, AZZURI, SYDNEY, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 3, 1));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, FANFALLI, SuperSixDemoData.OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, SuperSixDemoData.WITHDRAWN_SCORE, 0));
        wedsRound6.addMatch( createMatch( Division.FIRST_DIVISION, SHORT_AND_CURLY, SuperSixDemoData.LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 3, 0));

        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, BOCCA, SuperSixDemoData.WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_ONE, 4, 1));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.LIGEM_AGAIN, CASE_CLOSED, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_TWO, 4, 0));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, BOCCA, SuperSixDemoData.INTER, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_ONE, 1, 1));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, BURWOOD, AZZURI, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_TWO, 2, 0));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, SYDNEY, SuperSixDemoData.WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_ONE, 2, 1));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, FANFALLI, SuperSixDemoData.RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_TWO, SuperSixDemoData.WITHDRAWN_SCORE, 0));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, BURWOOD, HIGH, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 1, 0));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.OTHER_TEAM, SHORT_AND_CURLY, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 0, 1));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, AZZURI, SuperSixDemoData.INTER, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 0, 9));
        wedsRound7.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.RED_DEVILS, SHORT_AND_CURLY, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_TWO, 0, 2));

        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, HIGH, SuperSixDemoData.LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_ONE, 2, 4));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, SYDNEY, BURWOOD, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_TWO, 0, 6));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.LIGEM_AGAIN, SuperSixDemoData.RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_ONE, 2, 0));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.WANDERERS_A, HIGH, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_TWO, 1, 5));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, SYDNEY, SuperSixDemoData.OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_ONE, 1, 1));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, BOCCA, AZZURI, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_TWO, 1, 3));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.INTER, CASE_CLOSED, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 5, 0));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.WANDERERS_A, SuperSixDemoData.OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_TWO, 3, 4));
        wedsRound8.addMatch( createMatch( Division.FIRST_DIVISION, SHORT_AND_CURLY, SuperSixDemoData.INTER, 
                                     KickOffTime.KICK_OFF_0915, Pitch.PITCH_ONE, 4, 2));
        
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.LIGEM_AGAIN, SuperSixDemoData.OTHER_TEAM, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_ONE, 1, 0));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, SYDNEY, SuperSixDemoData.RED_DEVILS, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_TWO, 0, 0));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, AZZURI, HIGH, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_ONE, 2, 1));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.WANDERERS_A, SuperSixDemoData.INTER, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_TWO, 2, 4));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, BOCCA, SHORT_AND_CURLY, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_ONE, 3, 3));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, BURWOOD, SuperSixDemoData.LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_TWO, 1, 0));
        wedsRound9.addMatch( createMatch( Division.FIRST_DIVISION, CASE_CLOSED, SuperSixDemoData.WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 2, 3));

        wedsRound10.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.RED_DEVILS, AZZURI, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_ONE, 2, 1));
        wedsRound10.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.INTER, SuperSixDemoData.LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_TWO, 2, 3));
        wedsRound10.addMatch( createMatch( Division.FIRST_DIVISION, SHORT_AND_CURLY, BURWOOD, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_ONE, 1, 0));
        wedsRound10.addMatch( createMatch( Division.FIRST_DIVISION, SYDNEY, HIGH, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_TWO, 5, 1));
        wedsRound10.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.OTHER_TEAM, BOCCA, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_ONE, 2, 1));
        wedsRound10.addMatch( createMatch( Division.FIRST_DIVISION, CASE_CLOSED, AZZURI, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_TWO, 1, 0));
        wedsRound10.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.RED_DEVILS, SuperSixDemoData.WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0840, Pitch.PITCH_ONE, 3, 1));
        
        wedsRound11.addMatch( createMatch( Division.FIRST_DIVISION, SHORT_AND_CURLY, HIGH, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_ONE, 5, 2));
        wedsRound11.addMatch( createMatch( Division.FIRST_DIVISION, SYDNEY, SuperSixDemoData.LIGEM_AGAIN, 
                                     KickOffTime.KICK_OFF_0645, Pitch.PITCH_TWO, 0, 3));
        wedsRound11.addMatch( createMatch( Division.FIRST_DIVISION, BOCCA, SYDNEY, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_ONE, 0, 1));
        wedsRound11.addMatch( createMatch( Division.FIRST_DIVISION, AZZURI, SuperSixDemoData.WANDERERS_A, 
                                     KickOffTime.KICK_OFF_0720, Pitch.PITCH_TWO, 0, 2));
        wedsRound11.addMatch( createMatch( Division.FIRST_DIVISION, SuperSixDemoData.OTHER_TEAM, SuperSixDemoData.INTER, 
                                     KickOffTime.KICK_OFF_0800, Pitch.PITCH_ONE, 2, 4));        
    }    
}
