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
import org.strandz.data.wombatrescue.objects.cayenne.client.auto._Flexibility;
import org.strandz.data.wombatrescue.objects.FlexibilityI;

import java.io.Serializable;

public class Flexibility extends _Flexibility implements Comparable, Serializable, FlexibilityI
{
    private int pkId; // primary-key=true
    private transient static int timesConstructed;
    private transient int id;

    private Flexibility(String name, int pkId)
    {
        this();
        this.setName( name);
        this.pkId = pkId;
    }

    public Flexibility()
    {
        timesConstructed++;
        id = timesConstructed;
        /*
        Err.pr( "Flexibility(Cayenne) ### CREATED id: " + id );
        if(id == 9)
        {
            Err.stackOff();
        }
        */
    }

    public int getId()
    {
        return id;
    }

    public String toString()
    {
        return getName();
    }

    public int compareTo(Object obj)
    {
        return Utils.relativeRank(OPEN_VALUES, this, obj);
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, FlexibilityI.class);

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof FlexibilityI))
        {// nufin
        }
        else
        {
            FlexibilityI test = (FlexibilityI) o;
            if((getName() == null ? test.getName() == null : getName().equals(test.getName())))
            {
                result = true;
            }
        }
        return result;
    }

    /*
     * pUtils has this method
    public static Flexibility getFromList( Flexibility flexibility, List list)
    {
      Flexibility result = null;
      for (Iterator iterator = list.iterator(); iterator.hasNext();)
      {
        Flexibility flex = (Flexibility) iterator.next();
        if(flex.equals( flexibility))
        {
          result = flex;
          break;
        }
      }
      return result;
    }
    */

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (getName() == null ? 0 : getName().hashCode());
        return result;
    }

    public static final Flexibility NULL = new Flexibility();
    public static final Flexibility NO_OVERNIGHTS = new Flexibility("no overnights", 1);
    public static final Flexibility NO_EVENINGS = new Flexibility("no evenings", 2);
    public static final Flexibility FLEXIBLE = new Flexibility("flexible", 3);
    public static final Flexibility[] OPEN_VALUES = {NULL, NO_OVERNIGHTS, NO_EVENINGS, FLEXIBLE};

//    public int getPkId()
//    {
//        return pkId;
//    }

    public void setPkId(int pkId)
    {
        this.pkId = pkId;
    }
}