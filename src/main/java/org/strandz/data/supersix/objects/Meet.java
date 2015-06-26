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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.ValidationException;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.io.Serializable;

public class Meet implements Comparable, Serializable //for debugging even thou fetch groups not working
{
    private boolean dummy;
    public transient static Meet NULL = new Meet();
    private Date date;
    private Integer ordinal;
    private CompetitionSeason competitionSeason;
    private List games = new ArrayList();

    private static final String equalsPropNames[] = {
        "dummy",
        "date",
    };

    public Meet()
    {

    }

    public int compareTo(Object o)
    {
        int result = 0;
        Meet other = (Meet) o;
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
        result = Utils.compareTo(date, other.getDate());
        if(result == 0)
        {
            result = Utils.compareTo(ordinal, other.getOrdinal());
        }
        return result;
    }

    public boolean equals(Object o)
    {
        boolean result = false;
        Utils.chkType(o, this.getClass());

        String txt = "Meet " + this;
        ReasonNotEquals.addClassVisiting(txt);

        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Meet))
        {
            ReasonNotEquals.addReason("not an instance of an Meet");
        }
        else
        {
            Meet test = (Meet) o;
            result = SelfReferenceUtils.equalsByProperties(Meet.equalsPropNames, this, test);
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = SelfReferenceUtils.hashCodeByProperties(result, Meet.equalsPropNames, this);
        return result;
    }

    public String toString()
    {
        String result;
        if(getDate() == null)
        {
            result = "Round: <null getDate()> <" +
                getOrdinal() + ">";
        }
        else
        {
            result = "Round: <" + TimeUtils.getDisplayedFromDate( getDate(), TimeUtils.DATE_PARSE_STRING)  + "> <" +
                getOrdinal() + ">";
        }
        result += ", CompetitionSeason: <" + getCompetitionSeason() + ">";
        return result;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Integer getOrdinal()
    {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal)
    {
        this.ordinal = ordinal;
    }

    public CompetitionSeason getCompetitionSeason()
    {
        return competitionSeason;
    }

    public void setCompetitionSeason(CompetitionSeason competitionSeason)
    {
        this.competitionSeason = competitionSeason;
    }

    public void addMatch(Game game)
    {
        games.add(game);
        game.setMeet(this);
    }

    public void addMatch(Game game, int index)
    {
        games.add( index, game);
        game.setMeet(this);
    }

    public boolean removeMatch(Game game)
    {
        boolean result;
        if(result = games.remove(game))
        {
            //TODO Will be orphans like this so research on how to best
            //delete the RosterSlot at the same time as this method is
            //called
            if(game.getMeet() == this)
            {
                game.setMeet(null);
            }
        }
        return result;
    }

    private static class MaleThenFemaleComparator implements Comparator
    {
        private static final Comparator INSTANCE = new MaleThenFemaleComparator();

        public int compare(Object one, Object two)
        {
            int result = 0;
            if(!(one instanceof Game))
            {
                return 1;
            }
            if(!(two instanceof Game))
            {
                return 1;
            }

            Game m1 = (Game) one;
            Game m2 = (Game) two;
            result = matchCf(m1, m2);
            return result;
        }

        private static int matchCf(Game m1, Game m2)
        {
            int result = Utils.UNSET_INT;
            Division div1 = m1.getDivision();
            Division div2 = m2.getDivision();
            result = Utils.compareTo(div1, div2);
            if(result == 0)
            {
                KickOffTime kot1 = m1.getKickOffTime();
                KickOffTime kot2 = m2.getKickOffTime();
                result = Utils.compareTo(kot1, kot2);
            }
            return result;
        }
    }

    public List<Game> getGames()
    {
        Collections.sort( games, MaleThenFemaleComparator.INSTANCE);
        //InsteadOfAddRemoveTrigger
        //return Collections.unmodifiableList(games);
        return games;
    }

    public int getNumPlayed() throws ValidationException
    {
        int result = 0;
        for(Iterator iterator = games.iterator(); iterator.hasNext();)
        {
            Game game = (Game) iterator.next();
            if(game.beenPlayed())
            {
                result++;
            }
        }
        return result;
    }

    public boolean isDummy()
    {
        return dummy;
    }

    public void setDummy(boolean dummy)
    {
        this.dummy = dummy;
    }
}
