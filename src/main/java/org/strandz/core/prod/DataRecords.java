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

import org.strandz.lgpl.extent.VisibleExtent;
import org.strandz.lgpl.extent.CombinationExtent;
import org.strandz.lgpl.extent.AddFailer;
import org.strandz.lgpl.extent.HasCombinationExtent;
import org.strandz.lgpl.extent.NodeGroup;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.core.domain.exception.ApplicationErrorEnum;

/**
 * The list of data elements connected to a Block. An instance of
 * one of these can be shared between blocks. This is important when 
 * two blocks need to share the same data. The elements will always
 * be the same, but we also want an add or a delete from one block
 * to be reflected in the other block. For an example see the nodes
 * rosterSlotsListDetailNode and rosterSlotsQuickListDetailNode. 
 */
public class DataRecords implements HasCombinationExtent
{
    /**
     * This is the whole list of objects that we may change.
     * User can start off with a query() or an add(). If
     * starts off with an add(), then an empty ArrayList s/be
     * passed in when call setData for node.
     * When this node is in a child relationship, then this
     * dataRecords may be the combination of others, held in
     * each tie to a parent node. Ties.getNewListVector
     * provides explanation.
     * <p/>
     * For top-level blocks, dataRecords will be overwritten
     * with what the user passes in.
     * Non top-level blocks will have this var maintained by
     * their parents in the network.
     */
    private VisibleExtent dataRecords;
    private VisibleExtent revivableDataRecords;
    private String name;
    private NodeGroup nodeGroup;
    
    private static int times1 = 0;
    
    DataRecords( String name, NodeGroup nodeGroup)
    {
        this.name = name;
        this.nodeGroup = nodeGroup;
    }
    
    public void setCombinationExtent( CombinationExtent combinationExtent)
    {
        dataRecords = combinationExtent;
        Err.pr( SdzNote.NODE_GROUP, "setCombinationExtent() done for <" + getName() + "> with: " + combinationExtent);
    }
    
    public VisibleExtent getDataRecords()
    {
        // times++;
        // Err.pr( "dataRecords getDataRecords()" + times + " times");
        return dataRecords;
    }

    public Object getDataRecord(int idx)
    {
        Object result = dataRecords.get(idx);
        return result;
    }

    public void removeDataRecord(int idx)
    {
        dataRecords.removeElementAt(idx);
        // Print.pr( "*** removeDataRecord done at " + idx + " for " + this);
    }

    public boolean dataRecordsEmpty()
    {
        /*
        times3++;
        Err.pr( "*** in dataRecordsEmpty() times " + times3);
        if(times3 == 29)
        {
        Err.debug();
        }
        */
        boolean result = true;
        if(!dataRecordsNull())
        {
            result = dataRecords.isEmpty();
        }
        /*
        else
        {
        Print.pr( "*********dataRecordsNull()");
        }
        if(result)
        {
        Print.pr( "*********dataRecords.isEmpty() for " + this);
        }
        else
        {
        times1++;
        Print.pr( "*********dataRecords.is NOT Empty/Null() times " + times1);
        if(times1 == 0)
        {
        Err.stack();
        }
        }
        */
        return result;
    }

    public boolean dataRecordsNull()
    {
        return dataRecords == null;
    }

    public int dataRecordsSize()
    {
        int result = 0;
        if(dataRecords != null)
        {
            result = dataRecords.size();
            Err.pr( SdzNote.NODE_GROUP, "dataRecordsSize() will return " + result + " from " + dataRecords.getClass().getName() + " with ID: " + dataRecords.id);
        }
        return result;
    }

    public void insertDataRecord(int idx, Object obj)
    {
        Err.pr((SdzNote.CANT_ADD_RS.isVisible() || SdzNote.WANT_ALL_ON_DETAIL.isVisible()),
            "*** For " + getName() + " a dataRecord being inserted at " + idx + ", with "
                + obj.getClass().getName());
        int b4size = dataRecords.size();
        dataRecords.insert(obj, idx);
        int afterSize = dataRecords.size();
        if(dataRecords instanceof AddFailer)
        {
            AddFailer failer = (AddFailer) dataRecords;
            String err = failer.getDupPKError();
            if(err != null)
            {
                Session.getErrorThrower().throwApplicationError(failer.getDupPKError(),
                    ApplicationErrorEnum.INTERNAL);
            }
        }
        if(b4size + 1 != afterSize)
        {
            Err.error(SdzNote.BG_ADD, "Have not been able to insert a record for " + this);
        }
        else
        {
            Err.pr(SdzNote.BG_ADD, "Insert went fine for " + this);
        }
    }

    public void setFreshDataRecords()
    {
        Err.pr( SdzNote.NODE_GROUP, "setFreshDataRecords() for " + getName() + " not being done");
        //setDataRecords(new CombinationExtent());
    }

    public void setDataRecordsNull()
    {
        revivableDataRecords = dataRecords;
        setDataRecords(null);
    }

    /**
     * dataRecords is a local var operated on almost 'in the background'.
     * It is operated on for example when Ties.setNewListVector() is called
     * from this class. We bring dataRecords back after come out of insert
     * mode, when they will again be relevant.
     */
    public void reviveNulledDataRecords()
    {
        if(dataRecords != null)
        {
            Err.error("No need to call reviveNulledDataRecords()");
        }
        setDataRecords(revivableDataRecords);
    }

    public void setDataRecords(VisibleExtent extent)
    {
        if(SdzNote.WANT_ALL_ON_DETAIL.isVisible())
        {
            if(extent != null)
            {
                times1++;
                Print.pr("\tooo For " + getName() + " dataRecords [ID:" + extent.id + "] being set to"
                        + extent.getClass() + " size " + extent.size() + ", " + times1 + " times");
                if(times1 == 4)
                {
                    Err.debug();
                }
            }
            else
            {
                times1++;
                Print.pr("ooo For " + getName() + " dataRecords being set to NULL, " + times1 + " times");
            }
        }
        dataRecords = extent;
    }

    /**/
    public CombinationExtent getCombinationExtent()
    {
        /*
        times2++;
        Err.pr( "*** dataRecords getCombinationExtent() " + times);
        if(times2 == 0)
        {
        Err.debug();
        }
        */
        if(dataRecords == null)
        {
            Err.pr( true /*SdzNote.NODE_GROUP*/, "Would (if not now lazyload) return null CombinationExtent from " + getName());
            setDataRecords(new CombinationExtent());
        }
        return (CombinationExtent) dataRecords;
    }

    public NodeGroup getNodeGroup()
    {
        return nodeGroup;
    }

    public void emptyInsertedList()
    {
        dataRecords.emptyInsertedList();
    }
    
    public int size()
    {
        return dataRecords.size();
    }
    
    public Object get(int index)
    {
        return dataRecords.get( index);
    }

    public String getName()
    {
        return name;
    }
}
