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
package org.strandz.core.prod;

import org.strandz.core.prod.view.FieldObj;
import org.strandz.lgpl.extent.OrphanTie;
import org.strandz.lgpl.extent.Tie;
import org.strandz.lgpl.extent.TiesManager;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This TiesManager is all about Master/Detail
 */
public class MasterDetailTiesManager extends TiesManager
{
    /**
     * See comments at SubRecordObj.OWN_HAS_CHANGED and SubRecordObj.MASTER_HAS_CHANGED
     */
    boolean recursingSyncDetail = false;
    private static int constructedTimes;
    private int id;
    private static int times;

    public MasterDetailTiesManager(Class enforceType)
    {
        super(enforceType);
        constructedTimes++;
        id = constructedTimes;
        if(id > 1)
        {
            //There may be many Strands in same JVM at once
            //Each time consumeNodesIntoRT() another
            //Err.error( "Why isn't MasterDetailTiesManager a singleton?, call purge instead");
        }
        Err.pr(SdzNote.TIES_ENFORCE_TYPE, "Constructed MasterDetailTiesManager ID: " + id + " enforcing type " + enforceType);
        Err.pr(SdzNote.GENERIC, "#-# constructed MasterDetailTiesManager");
    }

    public void addTies(List ties)
    {
        times++;
        Err.pr(SdzNote.TIES_ENFORCE_TYPE, 
            "#-# Adding to MasterDetailTiesManager ties: " + ties + " times " + times);
        if(times == 0)
        {
            Err.stack();
        }
        super.addTies(ties);
    }

    public void purgeOldNode(ProdNodeI node)
    {
        List forRemoval = new ArrayList();
        for(Iterator en = ties.iterator(); en.hasNext();)
        {
            Tie tie = (Tie) en.next();
            if(tie.getChild() == node)
            {
                forRemoval.add(tie);
            }
            if(tie.getParent() == node)
            {
                forRemoval.add(tie);
            }
        }
        for(Iterator iterator = forRemoval.iterator(); iterator.hasNext();)
        {
            Tie tie = (Tie) iterator.next();
            boolean ok = ties.remove(tie);
            if(ok)
            {
                //Err.error( "Didn't think this necessary");
                Err.pr( "@-@ Have successfully removed tie " + tie + " from MasterDetailTiesManager");
            }
        }
        Err.pr( SdzNote.TIES_ENFORCE_TYPE, "#-# purge of MasterDetailTiesManager done");
    }

    public boolean isTopLevel(Object obj)
    {
        boolean result = false;
        result = super.isTopLevel(obj);
        return result;
    }

    private List recursiveGetRouteTo(
        List tiesResult, Tie targetTie, Class objType)
    {
        for(Iterator en = ties.iterator(); en.hasNext();)
        {
            Tie tie = (Tie) en.next();
            if(!(tie instanceof OrphanTie))
            {
                if(tie.getChild() != null)
                {
                    Err.pr("tie has child of type " + tie.getChild().getClass().getName());
                }
                else
                {
                    Err.pr("No child " + tie);
                }
                if(tie.getParent() != null)
                {
                    Err.pr(
                        "tie has parent of type " + tie.getParent().getClass().getName());
                }
                else
                {
                    Err.pr("No parent " + tie);
                }
                if(((FieldObj) tie.getChild()).getInstantiable().getClassObject() == objType)
                {
                    if(((FieldObj) targetTie.getChild()).getInstantiable()
                        == ((FieldObj) tie.getParent()).getInstantiable())
                    {
                        tiesResult.add(tie);
                        break;
                    }
                    else
                    {
                        List list = new ArrayList();
                        list.addAll(tiesResult);
                        return recursiveGetRouteTo(list, targetTie,
                            ((FieldObj) tie.getParent()).getInstantiable().getClassObject());
                    }
                }
            }
        }
        return tiesResult;
    }

    /**
     * Guess this method might work for something someday, but is currently
     * useless. Was written using the incorrect assumption that these ties
     * are for all the lookup relationships. They are for master-detail
     * only!
     * Get the route to an itemAdapter from a particular data object type.
     */
    /*
    public List _getRouteTo( ItemAdapter ad, Class objType)
    {
      List result = new ArrayList();
      Tie tie = ad.getDoAdapter().getTie();
      if(tie != null)
      {
        if(objType == ((FieldObj)tie.getChild()).getInstantiable())
        {
          result.add( tie);
        }
        else
        {
          result = recursiveGetRouteTo( result, tie, objType);
          result.add( tie);
        }
      }
      return result;
    }
    */
}
