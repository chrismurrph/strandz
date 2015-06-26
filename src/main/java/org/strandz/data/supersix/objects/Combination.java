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

import org.strandz.lgpl.util.Utils;

import java.util.List;
import java.util.ArrayList;

/**
 * Consider the combination class to be a square in a matrix
 * Each pickNextCombination() represents a game (so the same object is returned several times).
 */
public class Combination
{
    private Object team1;
    private Object team2;
    //int timesPlayed;
    //boolean filledIn;
    private List roundsPlayedIn = new ArrayList();

    public Combination( Object team1, Object team2)
    {
        this.team1 = team1;
        this.team2 = team2;
    }

    public String toString()
    {
        return "<team1 " + team1 + ">, <team2 " + team2 + ">, played " + roundsPlayedIn.size();
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Combination))
        {// nufin
        }
        else
        {
            Combination test = (Combination) o;
            if(Utils.equals( test.team1, team1) && Utils.equals( test.team2, team2))
            {
                result = true;
            }
            else if(Utils.equals( test.team1, team2) && Utils.equals( test.team2, team1))
            {
                result = true;
            }
        }
        return result;
    }

    public List getRoundsPlayedIn()
    {
        return roundsPlayedIn;
    }

    public void addRoundPlayedIn(Round roundPlayedIn)
    {
        roundsPlayedIn.add( roundPlayedIn);
    }

    public Object getTeam1()
    {
        return team1;
    }

    public Object getTeam2()
    {
        return team2;
    }
}
