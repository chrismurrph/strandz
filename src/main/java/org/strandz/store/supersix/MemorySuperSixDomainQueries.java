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

import org.strandz.data.supersix.domain.SuperSixDomainQueryEnum;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.lgpl.store.DomainQueries;
import org.strandz.lgpl.store.LoopyQuery;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.note.SuperSixNote;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class MemorySuperSixDomainQueries extends DomainQueries
{
    public MemorySuperSixDomainQueries()
    {
        initStableQueries();
    }

    private void initStableQueries()
    {
        initQuery(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM,
                  new LoopyQuery(
                      SuperSixData.TEAM_COMPETITION_SEASON,
                      SuperSixDomainQueryEnum.ALL_CURRENT_TEAM.getDescription())
                  {
                      public boolean match(Object row, Object params[])
                      {
                          boolean result = false;
                          TeamCompetitionSeason teamCompetitionSeason = (TeamCompetitionSeason) row;
                          Assert.notNull( teamCompetitionSeason.getCompetitionSeason().getSeasonYear(), 
                                          "teamCompetitionSeason.getSeasonYear() == null");
                          Assert.notNull( teamCompetitionSeason.getCompetitionSeason().getCompetition(), 
                                          "teamCompetitionSeason.getCompetition() == null");
                          if(teamCompetitionSeason.getCompetitionSeason().getSeasonYear().getName().equals(params[0]) && 
                                  teamCompetitionSeason.getCompetitionSeason().getCompetition().getName().equals(params[1]))
                          {
                              result = true;
                          }
                          return result;
                      }
                  });
        initQuery(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_INCL_NULL,
                  new LoopyQuery(
                      SuperSixData.TEAM_COMPETITION_SEASON,
                      SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_INCL_NULL.getDescription())
                  {
                      public boolean match(Object row, Object params[])
                      {
                          boolean result = false;
                          TeamCompetitionSeason teamCompetitionSeason = (TeamCompetitionSeason) row;
                          if(teamCompetitionSeason.isDummy() ||
                                  (teamCompetitionSeason.getCompetitionSeason().getSeasonYear().getName().equals(params[0]) && 
                                  teamCompetitionSeason.getCompetitionSeason().getCompetition().getName().equals(params[1]))
                                  )
                          {
                              result = true;
                          }
                          return result;
                      }
                  });
        initQuery(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_BY_DIVISION,
                  new LoopyQuery(
                      SuperSixData.TEAM_COMPETITION_SEASON,
                      SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_BY_DIVISION.getDescription())
                  {
                      public boolean match(Object row, Object params[])
                      {
                          boolean result = false;
                          TeamCompetitionSeason teamCompetitionSeason = (TeamCompetitionSeason) row;
                          //Assert.notNull( teamCompetitionSeason.getCompetitionSeason().getSeasonYear(), "competitionSeason.getSeasonYear() == null");
                          //Assert.notNull( teamCompetitionSeason.getCompetitionSeason().getCompetition(), "competitionSeason.getCompetition() == null");
                          if(!teamCompetitionSeason.isDummy() &&
                                  teamCompetitionSeason.getCompetitionSeason().getSeasonYear().getName().equals(params[0]) && 
                                  teamCompetitionSeason.getCompetitionSeason().getCompetition().getName().equals(params[1]) &&
                                  teamCompetitionSeason.getDivision().getName().equals(params[2]))
                          {
                              result = true;
                          }
                          else if(SuperSixNote.TEAMS_NOT_SHOWING.isVisible())
                          {
                              Print.prArray( params, "Params for query");
                              if(teamCompetitionSeason.isDummy())
                              {
                                  Err.pr( "filtered out: " + "isDummy()");
                              }
                              else if(!(teamCompetitionSeason.getCompetitionSeason().getSeasonYear().getName().equals(params[0])))
                              {
                                  Err.pr( "filtered out seasonYear: <" + params[0] + "> not in <" + teamCompetitionSeason.getCompetitionSeason().getSeasonYear().getName() + ">");
                              }
                              else if(!(teamCompetitionSeason.getCompetitionSeason().getCompetition().getName().equals(params[1])))
                              {
                                  Err.pr( "filtered out competition: <" + params[1] + "> not in <" + teamCompetitionSeason.getCompetitionSeason().getCompetition().getName() + ">");
                              }
                              else if(!(teamCompetitionSeason.getDivision().getName().equals(params[2])))
                              {
                                  Err.pr( "filtered out division: <" + params[2] + "> not in <" + teamCompetitionSeason.getDivision().getName() + ">");
                              }
                          }
                          return result;
                      }
                  });
        initQuery(SuperSixDomainQueryEnum.SINGLE_GLOBAL,
                  new LoopyQuery(
                      SuperSixData.GLOBAL,
                      SuperSixDomainQueryEnum.SINGLE_GLOBAL.getDescription())
                  {
                      public void formSingleResult(Collection c)
                      {
                          if(!c.isEmpty())
                          {
                              List l = (List) c;
                              //Err.pr( "Will set single result to " + l.get(0));
                              setSingleResult(l.get(0));
                          }
                          else
                          {
                              Err.error( "No Global instance found");
                          }
                          if(c.size() > 1)
                          {
                              Err.error( "Only one Global instance expected, instead found " + c.size());
                          }
                      }
                  });
        initQuery(SuperSixDomainQueryEnum.SEASON_BY_SEASONYEAR_COMP,
                  new LoopyQuery(
                      SuperSixData.COMPETITION_SEASON,
                      SuperSixDomainQueryEnum.SEASON_BY_SEASONYEAR_COMP.getDescription())
                  {
                      public boolean match(Object row, Object params[])
                      {
                          boolean result = false;
                          CompetitionSeason competitionSeason = (CompetitionSeason) row;
                          Assert.notNull( competitionSeason.getSeasonYear(), "competitionSeason.getSeasonYear() == null");
                          Assert.notNull( competitionSeason.getCompetition(), "competitionSeason.getCompetition() == null");
                          if(competitionSeason.getSeasonYear().getName().equals(params[0]) && 
                                  competitionSeason.getCompetition().getName().equals(params[1]))
                          {
                              result = true;
                          }
                          return result;
                      }

                      public void formSingleResult(Collection c)
                      {
                          if(!c.isEmpty())
                          {
                              List l = (List) c;
                              setSingleResult(l.get(0));
                          }
                          else
                          {
                              //Not an error. If there are none then the app should create one
                              //based on the current SeasonYear which will be preloaded
                              //Err.error( "No matching seasonYear found found in CompetitionSeason");
                          }
                          Assert.isFalse( c.size() > 1, "Too many matching seasonYears found in CompetitionSeason");
                      }
                  });
        initQuery(SuperSixDomainQueryEnum.ALL_USER,
                  new LoopyQuery(
                      SuperSixData.USER,
                      SuperSixDomainQueryEnum.ALL_USER.getDescription())
                  {
                  });

        initQuery(SuperSixDomainQueryEnum.LOOKUPS,
                  new LoopyQuery(
                      SuperSixData.LOOKUPS,
                      SuperSixDomainQueryEnum.LOOKUPS.getDescription())
                  {
                      public void formSingleResult(Collection c)
                      {
                          if(!c.isEmpty())
                          {
                              List l = (List) c;
                              //Err.pr( "Will set single result to " + l.get(0));
                              setSingleResult(l.get(0));
                          }
                          else
                          {
                              Err.error( "No Lookups instance found");
                          }
                          if(c.size() > 1)
                          {
                              Err.error( "Only one Lookups instance expected, instead found " + c.size());
                          }
                      }
                  });
        
        
        //Lookups
//        initQuery(SuperSixDomainQueryEnum.PITCH,
//                  new LoopyQuery(
//                      SuperSixData.PITCH,
//                      SuperSixDomainQueryEnum.PITCH.getDescription())
//                  {
//                  });
//        initQuery(SuperSixDomainQueryEnum.PITCH_CLOSED,
//                  new LoopyQuery(
//                      SuperSixData.PITCH,
//                      SuperSixDomainQueryEnum.PITCH_CLOSED.getDescription())
//                  {
//                      public boolean match(Object row)
//                      {
//                          boolean result = false;
//                          Pitch pitch = (Pitch) row;
//                          if(pitch != Pitch.NULL)
//                          {
//                              result = true;
//                          }
//                          return result;
//                      }
//                  });
//        initQuery(SuperSixDomainQueryEnum.DIVISION_CLOSED,
//                  new LoopyQuery(
//                      SuperSixData.DIVISION,
//                      SuperSixDomainQueryEnum.DIVISION_CLOSED.getDescription())
//                  {
//                  });
//        initQuery(SuperSixDomainQueryEnum.SEASON_YEAR,
//                  new LoopyQuery(
//                      SuperSixData.SEASON_YEAR,
//                      SuperSixDomainQueryEnum.SEASON_YEAR.getDescription())
//                  {
//                  });
//        initQuery(SuperSixDomainQueryEnum.DAY_IN_WEEK,
//                  new LoopyQuery(
//                      SuperSixData.DAY_IN_WEEK,
//                      SuperSixDomainQueryEnum.DAY_IN_WEEK.getDescription())
//                  {
//                  });
//        initQuery(SuperSixDomainQueryEnum.KICK_OFF_TIME,
//                  new LoopyQuery(
//                      SuperSixData.KICK_OFF_TIME,
//                      SuperSixDomainQueryEnum.KICK_OFF_TIME.getDescription())
//                  {
//                  });
//        initQuery(SuperSixDomainQueryEnum.KICK_OFF_TIME_CLOSED,
//                  new LoopyQuery(
//                      SuperSixData.KICK_OFF_TIME,
//                      SuperSixDomainQueryEnum.KICK_OFF_TIME_CLOSED.getDescription())
//                  {
//                      public boolean match(Object row)
//                      {
//                          boolean result = false;
//                          KickOffTime kickOffTime = (KickOffTime) row;
//                          if(kickOffTime != KickOffTime.NULL)
//                          {
//                              result = true;
//                          }
//                          return result;
//                      }
//                  });
    }

    protected boolean isPerformCopyTrick()
    {
        Err.error("isPerformCopyTrick()" + " not needed to be called in " + this.getClass().getName());
        return false;
    }
}
