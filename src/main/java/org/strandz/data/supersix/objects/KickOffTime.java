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

public class KickOffTime implements Comparable, Serializable
{
    private int pkId; // primary-key=true
    private String name;
    private int minutesFromStartOfDay; 
    private transient static int timesConstructed;
    public transient int id;

    private KickOffTime(String name, int minutesIntoDay, int pkId)
    {
        this();
        this.name = name;
        this.minutesFromStartOfDay = minutesIntoDay;
        this.pkId = pkId;
    }

    public KickOffTime()
    {
        KickOffTime.timesConstructed++;
        id = KickOffTime.timesConstructed;
    }

    public String toString()
    {
        return name;
    }

    public int compareTo(Object obj)
    {
        return Utils.relativeRank(KickOffTime.OPEN_VALUES, this, obj);
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof KickOffTime))
        {// nufin
        }
        else
        {
            KickOffTime test = (KickOffTime) o;
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

    public static final transient KickOffTime NULL = new KickOffTime();
    private static int inc = 0;
    public static final transient KickOffTime KICK_OFF_0545 = new KickOffTime("5:45", 1065, ++inc);
    public static final transient KickOffTime KICK_OFF_0600 = new KickOffTime("6:00", 1080, ++inc);
    public static final transient KickOffTime KICK_OFF_0610 = new KickOffTime("6:10", 1090, ++inc);
    public static final transient KickOffTime KICK_OFF_0615 = new KickOffTime("6:15", 1095, ++inc);
    public static final transient KickOffTime KICK_OFF_0620 = new KickOffTime("6:20", 1100, ++inc);
    public static final transient KickOffTime KICK_OFF_0630 = new KickOffTime("6:30", 1110, ++inc);
    public static final transient KickOffTime KICK_OFF_0640 = new KickOffTime("6:40", 1120, ++inc);
    public static final transient KickOffTime KICK_OFF_0645 = new KickOffTime("6:45", 1125, ++inc);
    public static final transient KickOffTime KICK_OFF_0650 = new KickOffTime("6:50", 1130, ++inc);
    public static final transient KickOffTime KICK_OFF_0700 = new KickOffTime("7:00", 1140, ++inc);
    public static final transient KickOffTime KICK_OFF_0710 = new KickOffTime("7:10", 1150, ++inc);
    public static final transient KickOffTime KICK_OFF_0720 = new KickOffTime("7:20", 1160, ++inc);
    public static final transient KickOffTime KICK_OFF_0725 = new KickOffTime("7:25", 1165, ++inc);
    public static final transient KickOffTime KICK_OFF_0740 = new KickOffTime("7:40", 1180, ++inc);
    public static final transient KickOffTime KICK_OFF_0750 = new KickOffTime("7:50", 1190, ++inc);
    public static final transient KickOffTime KICK_OFF_0800 = new KickOffTime("8:00", 1200, ++inc);
    public static final transient KickOffTime KICK_OFF_0805 = new KickOffTime("8:05", 1205, ++inc);
    public static final transient KickOffTime KICK_OFF_0820 = new KickOffTime("8:20", 1220, ++inc);
    public static final transient KickOffTime KICK_OFF_0830 = new KickOffTime("8:30", 1230, ++inc);
    public static final transient KickOffTime KICK_OFF_0840 = new KickOffTime("8:40", 1240, ++inc);
    public static final transient KickOffTime KICK_OFF_0910 = new KickOffTime("9:10", 1270, ++inc);
    public static final transient KickOffTime KICK_OFF_0915 = new KickOffTime("9:15", 1275, ++inc);
    public static final transient KickOffTime KICK_OFF_0950 = new KickOffTime("9:50", 1310, ++inc);
    
    public static final transient KickOffTime[] OPEN_VALUES = {
        NULL, KICK_OFF_0545, KICK_OFF_0600, KICK_OFF_0610, KICK_OFF_0615,KICK_OFF_0620, KICK_OFF_0630, KICK_OFF_0640, KICK_OFF_0645, KICK_OFF_0650, 
            KICK_OFF_0700, KICK_OFF_0710, KICK_OFF_0720, KICK_OFF_0725, KICK_OFF_0740, KICK_OFF_0750, KICK_OFF_0800, KICK_OFF_0805, 
            KICK_OFF_0820, KICK_OFF_0830,KICK_OFF_0840, KICK_OFF_0910, KICK_OFF_0915, KICK_OFF_0950};

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getMinutesFromStartOfDay()
    {
        return minutesFromStartOfDay;
    }

    public void setMinutesFromStartOfDay(int minutesFromStartOfDay)
    {
        this.minutesFromStartOfDay = minutesFromStartOfDay;
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
            else if(!(o instanceof KickOffTime.ID))
            {// nufin
            }
            else
            {
                KickOffTime.ID test = (KickOffTime.ID) o;
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
