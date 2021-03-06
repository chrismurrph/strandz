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
package org.strandz.data.wombatrescue.business;

import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;
import org.strandz.data.wombatrescue.calculated.RosterTransferObj;
import org.strandz.data.wombatrescue.calculated.MonthTransferObj;
import org.strandz.data.wombatrescue.calculated.ClientObjProvider;
import org.strandz.data.wombatrescue.calculated.Night;
import org.strandz.data.wombatrescue.calculated.ParticularShift;

import java.util.List;
import java.util.Iterator;

public class ThickClientRosterService extends AbstractRosterService
{
    public void init(DataStore ds)
    {
        if(ds == null)
        {
            Err.error("ParticularRoster must be initialized with a DataStore");
        }
        else
        {
            setDataStore( ds);
        }
    }

    public RosterTransferObj getRoster( int what, MonthTransferObj month)
    {
        RosterTransferObj result = super.getRoster( what, month);
        RosterBusinessUtils.prepareForClientUse( result, (EntityManagedDataStore)getDataStore());
        return result;
    }

    public String getName()
    {
        return "Roster (Thick Client)";
    }
}
