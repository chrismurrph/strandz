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

import org.strandz.data.supersix.domain.SuperSixDomainLookupEnum;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.store.DomainLookupEnum;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.lgpl.util.Err;

import java.util.Arrays;
import java.util.List;
import java.io.Serializable;

public class SuperSixLookups implements LookupsI, Serializable
{
    private List pitches;
    private List kickOffTimes;
    private List seasonYears;
    private List divisions;
    private List dayInWeeks;
    private List competitions;
    
    public SuperSixLookups()
    {
        //Err.stack();
        //Err.pr( "In SuperSixLookups() constructor");
    }

    /**
     * Only to be called when creating DemoData - always need to get these objects
     * from the DB otherwise
     */
    public void initValues()
    {
        pitches = Arrays.asList(Pitch.OPEN_VALUES);
        kickOffTimes = Arrays.asList(KickOffTime.OPEN_VALUES);
        //Having no season makes no business sense - thus app s/be shipped
        //with a global current season - and this will ensure can never
        //select no season at all. Well - having to have a NULL at start of
        //a LOVs (see Globals) means have to have OPEN_VALUES
        seasonYears = Arrays.asList(SeasonYear.OPEN_VALUES);
        divisions = Arrays.asList(Division.VALUES);
        dayInWeeks = Arrays.asList(DayInWeek.OPEN_VALUES);
        competitions = Arrays.asList(Competition.OPEN_VALUES);
    }

    private List getPitches()
    {
        return pitches;
    }

    private List getKickOffTimes()
    {
        return kickOffTimes;
    }

    private List getSeasonYears()
    {
        return seasonYears;
    }

    private List getDivisions()
    {
        return divisions;
    }

    private List getDayInWeeks()
    {
        return dayInWeeks;
    }

    private List getCompetitions()
    {
        return competitions;
    }

    public List get(DomainLookupEnum enumId)
    {
        List result = null;
        if(enumId == SuperSixDomainLookupEnum.PITCH)
        {
            result = getPitches();
        }
        else if(enumId == SuperSixDomainLookupEnum.PITCH_CLOSED)
        {
            Err.pr( "Reting all pitches even thou asked for closed (ie no null pitches)");
            result = getPitches();
        }
        else if(enumId == SuperSixDomainLookupEnum.KICK_OFF_TIME)
        {
            result = getKickOffTimes();
        }
        else if(enumId == SuperSixDomainLookupEnum.SEASON_YEAR)
        {
            result = getSeasonYears();
        }
        else if(enumId == SuperSixDomainLookupEnum.DIVISION)
        {
            result = getDivisions();
        }
        else if(enumId == SuperSixDomainLookupEnum.DAY_IN_WEEK)
        {
            result = getDayInWeeks();
        }
        else if(enumId == SuperSixDomainLookupEnum.COMPETITION)
        {
            result = getCompetitions();
        }
        else
        {
            Err.error( "Not yet implemented - " + enumId);
        }
        return result;
    }
}
