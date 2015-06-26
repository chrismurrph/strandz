/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.extent;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import java.util.ArrayList;
import java.util.List;

/**
 * VisibleExtent could be named Non-Tie Extent, and was originally called
 * BlockExtent, until it was discovered that lookups that could be related by
 * their List had an IndependentExtent as their List. BlockExtent seemed good
 * at first because a Block's dataRecords is either an IndependentExtent or a
 * CombinationExtent.
 * <p/>
 * All the inserted stuff keeps track of which objects have been inserted
 * since the last commit. If the user has specified a restriction on updating
 * and deleting for a particular Node, then these restrictions will not apply
 * to these objects. VisibleExtent has been created to put this inserteds stuff
 * into.
 */
abstract public class VisibleExtent extends NavExtent
{
    /**
     * All objects that are inserted during a particular transaction
     * will go here.
     */
    private List inserteds = new ArrayList();
    private List deletes = new ArrayList();
    public int id = -99;
    private static int constructedTimes = 0;

    /**
     * Only to be used by CombinationExtent
     */
    VisibleExtent()
    {
        constructedTimes++;
        id = constructedTimes;
        Err.pr(SdzNote.BG_ADD.isVisible() || SdzNote.NODE_GROUP.isVisible(), "Created " + getClass().getName() + " ID: " + id);
        if(id == 0)
        {
            Err.stack();
        }
    }

    public void emptyInsertedList()
    {
        // Err.error( "Is called?");
        inserteds = new ArrayList();
        deletes = new ArrayList();
    }

    void insertIntoInserted(int index)
    {
        Object obj = get(index);
        inserteds.add(obj);
        if(deletes.contains(obj))
        {
            deletes.remove(obj);
        }
    }

    void insertIntoDeletes(int index)
    {
        Object obj = get(index);
        deletes.add(obj);
        inserteds.remove(obj);
    }

    void insertIntoDeletes(Object obj)
    {
        deletes.add(obj);
        inserteds.remove(obj);
    }

    public List getDeletes()
    {
        return deletes;
    }

    /*
    void removeFromInserted( int index)
    {
    inserteds.remove( get( index)); //if not there won't be removed
    }
    void removeFromInserted( Object obj)
    {
    inserteds.remove( obj); //if not there won't be removed
    }
    */
    public boolean isInserted(int index)
    {
        boolean result = false;
        // new MessageDlg("Trying to get value for " + index);
        int isThisOldInsert = inserteds.indexOf(get(index));
        if(isThisOldInsert != -1)
        {
            result = true;
        }
        else
        {
            result = false;
        }
        // new MessageDlg("element at " + index + " is an insert: " + result);
        return result;
    }

//  public void setEntityManagerProvider( EntityManagerProviderI entityManagerProvider)
//  {
//    if(entityManagerProvider == null)
//    {
//      Err.error( "Trying to set entityManagerProvider to null in " + getClass().getName() + " ID: " + id);
//    }
//    else if(this.entityManagerProvider != null)
//    {
//      Err.error( "Already set entityManagerProvider to: " + this.entityManagerProvider + " in " + this);
//    }
//    this.entityManagerProvider = entityManagerProvider;
//    Err.pr( SdzNote.bgAdd, "entityManagerProvider set on " + getClass().getName() + " ID: " + id);
//  }
}
