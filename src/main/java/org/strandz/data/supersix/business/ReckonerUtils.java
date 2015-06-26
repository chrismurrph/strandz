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

import org.strandz.data.supersix.objects.Combination;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ReckonerUtils
{
    static List<Combination> findCombinationsFromTeams( List teams)
    {
        List<Combination> result = new ArrayList<Combination>();
        for(int i = 0; i < teams.size(); i++)
        {
            Object teamA = teams.get(i);
            for(int j = 0; j < teams.size(); j++)
            {
                Object teamB = teams.get(j);
                if(teamA != teamB)
                {
                    if(!combinationAlreadyExists( result, teamA, teamB))
                    {
                        Combination combination = new Combination( teamA, teamB);
                        result.add( combination);
                    }
                }
            }
        }
        return result;
    }

    private static boolean combinationAlreadyExists( List combinations, Object team1, Object team2)
    {
        boolean result = false;
        for(Iterator iterator = combinations.iterator(); iterator.hasNext();)
        {
            Combination combination = (Combination) iterator.next();
            if((combination.getTeam1() == team1 && combination.getTeam2() == team2) || (combination.getTeam2() == team1 && combination.getTeam1() == team2))
            {
                result = true;
                break;
            }
        }
        return result;
    }
}
