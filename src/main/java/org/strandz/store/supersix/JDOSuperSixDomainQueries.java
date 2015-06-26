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
import org.strandz.lgpl.store.DomainQueries;
import org.strandz.lgpl.store.JDOInterpretedQuery;
import org.strandz.lgpl.util.Utils;

import java.util.Collection;
import java.util.List;

public class JDOSuperSixDomainQueries extends DomainQueries
{
//    private static boolean USE_COPY_TRICK = true;
//    
//    protected boolean isPerformCopyTrick()
//    {
//        return USE_COPY_TRICK;    
//    }
    
    public JDOSuperSixDomainQueries()
    {
        initQuery(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM,
                  new JDOInterpretedQuery(
                          SuperSixData.TEAM_COMPETITION_SEASON,
                          SuperSixDomainQueryEnum.ALL_CURRENT_TEAM.getDescription(),
                          "competitionSeason.seasonYear.name == seasonYearParam && " +
                                  "competitionSeason.competition.name == competitionParam", 
                          "name ascending",
                          "String seasonYearParam, String competitionParam",                          
//                          "dummy != true",
//                          "name ascending",
//                          null,
                          loggingMonitor)
                  {
                      public void chkResult(Collection c)
                      {
                          Utils.chkNoNulls(c, getId());
                      }
                  });
        initQuery(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_INCL_NULL,
                  new JDOInterpretedQuery(
                          SuperSixData.TEAM_COMPETITION_SEASON,
                          SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_INCL_NULL.getDescription(),
                          "dummy = true || (competitionSeason.seasonYear.name == seasonYearParam && " +
                                  "competitionSeason.competition.name == competitionParam)", 
                          "name ascending",
                          "String seasonYearParam, String competitionParam",                          
//                          null,
//                          "name ascending",
//                          null,
                          loggingMonitor)
                  {
                      public void chkResult(Collection c)
                      {
                          Utils.chkNoNulls(c, getId());
                      }
                  });
        initQuery(SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_BY_DIVISION,
                  new JDOInterpretedQuery(
                      SuperSixData.TEAM_COMPETITION_SEASON,
                      SuperSixDomainQueryEnum.ALL_CURRENT_TEAM_BY_DIVISION.getDescription(),
                      "dummy != true && " +
                              "division.name == divisionNameParam && " +
                              "competitionSeason.seasonYear.name == seasonYearParam && " +
                              "competitionSeason.competition.name == competitionParam",
                      "name ascending",
                      "String divisionNameParam, String seasonYearParam, String competitionParam",
                      loggingMonitor)
                  {
                      public void chkResult(Collection c)
                      {
                          Utils.chkNoNulls(c, getId());
                      }
                  });
        initQuery(SuperSixDomainQueryEnum.SINGLE_GLOBAL,
            new JDOInterpretedQuery(
                SuperSixData.GLOBAL,
                SuperSixDomainQueryEnum.SINGLE_GLOBAL.getDescription(),
                null,
                null,
                null,
                loggingMonitor,
                false, false, 1844)
            {
                public void formSingleResult(Collection c)
                {
                    if(!c.isEmpty())
                    {
                        List l = (List) c;
                        setSingleResult(l.get(0));
                    }
                }
            });
        initQuery(SuperSixDomainQueryEnum.SEASON_BY_SEASONYEAR_COMP,
            new JDOInterpretedQuery(
                SuperSixData.COMPETITION_SEASON,
                SuperSixDomainQueryEnum.SEASON_BY_SEASONYEAR_COMP.getDescription(),
                "seasonYear.name == seasonYearParam && competition.name == competitionParam", 
                "seasonYear ascending, competition ascending",
                "String seasonYearParam, String competitionParam",
                loggingMonitor,
                false, false, 41516)
            {
                public void formSingleResult(Collection c)
                {
                    if(!c.isEmpty())
                    {
                        List l = (List) c;
                        setSingleResult(l.get(0));
                    }
                }
            });
        initQuery(SuperSixDomainQueryEnum.ALL_USER,
                  new JDOInterpretedQuery(
                          SuperSixData.USER,
                          SuperSixDomainQueryEnum.ALL_USER.getDescription(),
                          null,
                          "username ascending",
                          null,
                          loggingMonitor)
                  {
                      public void chkResult(Collection c)
                      {
                          Utils.chkNoNulls(c, getId());
                      }
                  });        
        JDOInterpretedQuery query1 = new JDOInterpretedQuery(
                SuperSixData.LOOKUPS,
                SuperSixDomainQueryEnum.LOOKUPS.getDescription(),
                null,
                null,
                null,
                loggingMonitor,
                false, false, 5656)
        {
            public void chkResult(Collection c)
            {
               /* 
                * Good way of finding out if what is returned is persistent clean or hollow
                */
               /* 
               SuperSixLookups supersixLookups = (SuperSixLookups)((List)c).get(0);
               //If do this we force them to show up - but we want them to
               //show up from the time of the query
               //supersixLookups.get( WombatDomainLookupEnum.ALL_SEX);
               Query q = getQuery();
               PersistenceManager pm = q.getPersistenceManager();
               pm.makeTransient(supersixLookups);
               List sexes = supersixLookups.get( SuperSixDomainLookupEnum.DAY_IN_WEEK);
               Print.prList( sexes, "Presumably not hollow, so these will show", false);
               */
            }

            public void formSingleResult(Collection c)
            {
                if(!c.isEmpty())
                {
                    List l = (List) c;
                    setSingleResult(l.get(0));
                }
            }
        };
        //Keeping this line, just so we know it's here
        //JDOFetchGroupQuery query2 = new JDOFetchGroupQuery(query1, "lookups");
        initQuery(SuperSixDomainQueryEnum.LOOKUPS, query1);
    }
}
