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
import org.strandz.data.supersix.objects.Meet;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ValidationException;

import java.util.Date;

public class MeetsCalculator
{
    private CompetitionSeason competitionSeason;
    private DataStore ds;
    private SuperSixManagerI superSixManager;

    MeetsCalculator( DataStore ds, CompetitionSeason competitionSeason, SuperSixManagerI superSixManager)
    {
        this.ds = ds;
        this.competitionSeason = competitionSeason;
        this.superSixManager = superSixManager;
    }

    void calculateMeets( DayInWeek matchNight) throws ValidationException
    {
//        DayInWeek matchNight = competitionSeason.getNightPlayGames();
        if(matchNight == null)
        {
            throw new ValidationException( "Do not know on which nights games are played for " + competitionSeason);
        }
        Date startDate = competitionSeason.getStartDate();
        if(startDate == null)
        {
            throw new ValidationException( "Do not know the Start Date for " + competitionSeason);
        }
        Date endDate = competitionSeason.getEndDate();
        if(endDate == null)
        {
            throw new ValidationException( "Do not know the End Date for " + competitionSeason);
        }
        int day = DayInWeek.toOrdinal( matchNight);
        Date firstMatch = TimeUtils.getNextDayFromDate( startDate, day);
        Date fromDate;
        //Date today = new Date();
        //if(today.getTime() <= firstMatch.getTime())
        {
            fromDate = firstMatch;
        }
        //else
        //{
        //    fromDate = TimeUtils.getNextDayFromToday( day); 
        //}
        Date toDate = TimeUtils.getPreviousDayFromDate( endDate, day);
        Err.pr( "To calc meets from " + fromDate + " to " + toDate);
        calculateMeets( fromDate, toDate);
    }

    private void calculateMeets( Date fromDate, Date toDate)
    {
        Date dateToIncr = new Date( fromDate.getTime());
        for(int meetNum = 0; dateToIncr.getTime() <= toDate.getTime(); 
            dateToIncr = TimeUtils.addDays( dateToIncr, 7), meetNum++)
        {
            Meet newMeet = new Meet();
            newMeet.setDate( new Date(dateToIncr.getTime()));
            newMeet.setOrdinal( meetNum +1);
            competitionSeason.addMeet( newMeet);
            //We don't have to do this because we are on a detail relationship 
            //((EntityManagedDataStore)ds).getEM().registerPersistent( newMeet);
        }
    }
}
