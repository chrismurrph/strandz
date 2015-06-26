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

import java.io.Serializable;

public class SeasonYear implements Comparable, Serializable
{
    private int pkId; // primary-key=true
    private String name;
    private transient static int timesConstructed;
    public transient int id;

    private SeasonYear(String name, int pkId)
    {
        this();
        this.name = name;
        this.pkId = pkId;
    }

    public SeasonYear()
    {
        SeasonYear.timesConstructed++;
        id = SeasonYear.timesConstructed;
    }
    
    public static SeasonYear getFromName(String name)
    {
        return (SeasonYear) Utils.getByStringFromArray(SeasonYear.OPEN_VALUES, name);
    }

    public String toString()
    {
        return name;
    }

    public int compareTo(Object obj)
    {
        return Utils.relativeRank(SeasonYear.OPEN_VALUES, this, obj);
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof SeasonYear))
        {// nufin
        }
        else
        {
            SeasonYear test = (SeasonYear) o;
            if((name == null ? test.name == null : name.equals(test.name)))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    public static final transient SeasonYear NULL = new SeasonYear();
    public static final transient SeasonYear SEASON_2006 = new SeasonYear("2006", 1);
    public static final transient SeasonYear SEASON_2007 = new SeasonYear("2007", 2);
    public static final transient SeasonYear[] OPEN_VALUES = {
        SeasonYear.NULL, SeasonYear.SEASON_2006, SeasonYear.SEASON_2007};
    public static final transient SeasonYear[] CLOSED_VALUES = {
        SeasonYear.SEASON_2006, SeasonYear.SEASON_2007};

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public static class ID implements Serializable
    {
        public int pkId;

        public ID()
        {
        }

        public ID(String pkId)
        {
            this.pkId = Integer.parseInt(pkId);
        }

        public boolean equals(Object o)
        {
            boolean result = false;
            if(o == this)
            {
                result = true;
            }
            else if(!(o instanceof SeasonYear.ID))
            {// nufin
            }
            else
            {
                SeasonYear.ID test = (SeasonYear.ID) o;
                if(test.pkId == pkId)
                {
                    result = true;
                }
            }
            return result;
        }

        public int hashCode()
        {
            int result = 17;
            result = 37 * result + pkId;
            return result;
        }

        public String toString()
        {
            return "" + pkId;
        }
    }

    public int getPkId()
    {
        return pkId;
    }

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }
}
