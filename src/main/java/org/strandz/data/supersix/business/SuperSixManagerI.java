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

import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.Global;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.data.supersix.objects.SeasonYear;
import org.strandz.data.supersix.objects.Competition;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.data.objects.DayInWeek;

import java.util.List;

public interface SuperSixManagerI
{
    Global getGlobal();
    List<TeamCompetitionSeason> getCurrentTeams( SeasonYear seasonYear, Competition competition);
    List<TeamCompetitionSeason> getCurrentTeamsIncludingNull( SeasonYear seasonYear, Competition competition);
    List<TeamCompetitionSeason> getCurrentTeams( SeasonYear seasonYear, Competition competition, Division division);
    CompetitionSeason getCurrentSeason( Global global);
    SuperSixLookups getLookups(); 
    void calculateDraws( CompetitionSeason competitionSeason, SuperSixLookups superSixLookups, DayInWeek matchNight) throws ValidationException;
    void calculateMeets( CompetitionSeason competitionSeason, DayInWeek matchNight) throws ValidationException;
    void init(DataStore ds);
    DataStore getDataStore();
}
