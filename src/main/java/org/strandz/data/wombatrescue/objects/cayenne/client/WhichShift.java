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
package org.strandz.data.wombatrescue.objects.cayenne.client;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NameableI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.data.wombatrescue.objects.cayenne.client.auto._WhichShift;
import org.strandz.data.wombatrescue.objects.WhichShiftI;

import java.io.Serializable;

public class WhichShift extends _WhichShift implements Comparable, Serializable, NameableI, WhichShiftI
{
    private int pkId; // primary-key=true
    private transient static int times;
    private transient static int timesConstructed;
    private transient static final int maxConstructions = 2;
    public transient int id;
    private transient static boolean failNxt = false;

    private WhichShift(String name, int pkId)
    {
        this();
        setName( name);
        this.pkId = pkId;
    }

    public WhichShift()
    {
        timesConstructed++;
        id = timesConstructed;
        Err.pr( SdzNote.NV_PASTE_NOT_WORKING, "$$ Constructed, WhichShift " + id);
        /*
        if(id > maxConstructions)
        {
        Err.pr( "$$ Constructed too many times, WhichShift " + id);
        }
        else
        {
        Err.pr( "$$ Constructed, WhichShift " + id);
        }
        */
        /*
        if(failNxt)
        {
        Err.stack();
        }
        if(id == 3)
        {
        failNxt = true;
        }
        */
    }

    public String toString()
    {
        return getName()/* + ", " + id*/;
    }

    public int compareTo(Object obj)
    {
        return Utils.relativeRank(OPEN_VALUES, this, obj);
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof WhichShift))
        {// nufin
        }
        else
        {
            WhichShift test = (WhichShift) o;
            if((getName() == null ? test.getName() == null : getName().equals(test.getName())))
            {
                // if(pkId == test.pkId)
                {
                    result = true;
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (getName() == null ? 0 : getName().hashCode());
        // result = 37*result + pkId;
        return result;
    }

    public static final WhichShift NULL = new WhichShift();
    public static final WhichShift DINNER = new WhichShift("dinner", 1);
    public static final WhichShift OVERNIGHT = new WhichShift("overnight", 2);
    /*
    public static final WhichShift EVENING()
    {
    return new WhichShift( "dinner");
    }
    public static final WhichShift OVERNIGHT()
    {
    return new WhichShift( "overnight");
    }
    */

    public static final WhichShift[] OPEN_VALUES = {NULL, DINNER, OVERNIGHT};
    public static final WhichShift[] CLOSED_VALUES = {DINNER, OVERNIGHT};

    public String toShow()
    {
        return getName();
    }

//    public int getPkId()
//    {
//        return pkId;
//    }

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }
}