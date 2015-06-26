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
package org.strandz.task.data.supersix;

import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.store.supersix.SuperSixDataStoreFactory;
import org.strandz.data.supersix.domain.SuperSixConnectionEnum;
import org.strandz.data.supersix.objects.Division;

import javax.jdo.JDOException;
import java.util.List;
import java.util.ArrayList;

/**
 * This method will populate a new Lookup/LOV table with the values
 * <p/>
 * Update part not yet tested (is commented-out). Just drop then create
 * the table then run this if need to re-populate.
 */
public class PopulateUpdateLOV
{
    private static EntityManagedDataStore dataStore;

    private static final Division TO_CREATE_LIST[] = Division.VALUES;
    private static boolean commitPopulation = true;

    public static void main(String s[])
    {
        PopulateUpdateLOV obj = new PopulateUpdateLOV();
        if(s.length != 0)
        {
            obj.processParams(s);
        }
        else
        {
            String str[] = {SuperSixConnectionEnum.DEMO_SUPERSIX_JPOX.getName()};
            obj.processParams(str);
        }
        System.exit(0);
    }

    private void processParams(String s[])
    {
        if(s.length != 1)
        {
            Err.error("Need to explicitly specify a SuperSixConnectionEnum database, and that's all");
        }
        /*
        * Comment out below when you are sure you want to do it on a
        * PROD DB.
        */
        if(SuperSixConnectionEnum.getFromName(s[0]).isProduction())
        {
            Err.error(
                "Cannot work with the " + SuperSixConnectionEnum.getFromName(s[0])
                    + " database");
        }
        DataStoreFactory dataStoreFactory = new SuperSixDataStoreFactory();
        dataStoreFactory.addConnection(SuperSixConnectionEnum.getFromName(s[0]));
        PopulateUpdateLOV.dataStore = dataStoreFactory.getEntityManagedDataStore();
        PopulateUpdateLOV.dataStore.startTx();
        makePersistent(PopulateUpdateLOV.TO_CREATE_LIST, PopulateUpdateLOV.dataStore);
        if(!PopulateUpdateLOV.commitPopulation)
        {
            dataStore.rollbackTx();
        }
        else
        {
            dataStore.commitTx();
        }
    }

    List makePersistent(Object toCreateList[], EntityManagedDataStore dataStore)
    {
        List result = new ArrayList();
        for(int i = 0; i < toCreateList.length; i++)
        {
            Object lovItem = toCreateList[i];
            try
            {
                dataStore.getEM().registerPersistent(lovItem);
            }
            catch(JDOException ex)
            {
                //nufin
            }
            result.add(lovItem);
        }
        return result;
    }

}
