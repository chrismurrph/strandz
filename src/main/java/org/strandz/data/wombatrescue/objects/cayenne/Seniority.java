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
package org.strandz.data.wombatrescue.objects.cayenne;

import org.strandz.lgpl.util.Utils;
import org.strandz.data.wombatrescue.objects.cayenne.auto._Seniority;
import org.strandz.data.wombatrescue.objects.SeniorityI;

import java.io.Serializable;

public class Seniority extends _Seniority implements Comparable, Serializable, SeniorityI
{
    private int pkId; // primary-key=true
    private transient static int timesConstructed;
    public transient int id;

    private Seniority(String name, int pkId)
    {
        this();
        setName( name);
        this.pkId = pkId;
    }

    public Seniority()
    {
        timesConstructed++;
        id = timesConstructed;
    }

    public String toString()
    {
        return getName() /*+ ", pkId: " + pkId*/;
    }

    public int compareTo(Object obj)
    {
        return Utils.relativeRank(OPEN_VALUES, this, obj);
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, SeniorityI.class);

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof SeniorityI))
        {// nufin
        }
        else
        {
            SeniorityI test = (SeniorityI) o;
            if((getName() == null ? test.getName() == null : getName().equals(test.getName())))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (getName() == null ? 0 : getName().hashCode());
        return result;
    }

    public static final Seniority NULL = new Seniority();
    public static final Seniority KEYLESS = new Seniority("keyless", 1);
    public static final Seniority NEWBIE = new Seniority("newbie", 2);
    public static final Seniority JUNIOR = new Seniority("junior", 3);
    public static final Seniority EXPERIENCED = new Seniority("experienced", 4);
    public static final Seniority[] OPEN_VALUES = {
        NULL, KEYLESS, NEWBIE, JUNIOR, EXPERIENCED};

//    public int getPkId()
//    {
//        return pkId;
//    }

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }
}