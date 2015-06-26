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
package org.strandz.task.data.wombatrescue;

import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.business.LookupsProvider;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DataStoreOpsUtils;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Print;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

/**
 * This didn't work as got 'object is managed by' exceptions.
 * (The flushing part did however work, so after running this the target DB
 * will be blanked out).
 * 
 * Needed to do this afterwards:
 * mysql --user=root kodo
 * mysql> delete from worker where belongstogroup_jdoid IS NULL;
 */
public class CopyDBtoDB
{
    private static final boolean FILL_LOOKUP_TABLES = true;
    
    public static void main(String s[])
    {
        if(s.length != 0)
        {
            Err.error("CopyDBtoDB requires no command line parameters");
        }
        else
        {
            process();
        }
        System.exit(0);
    }

    private static void process()
    {
        DataStoreFactory dataStoreFactory = new WombatDataStoreFactory();
        //Source no longer exists
        dataStoreFactory.addConnection(null /*WombatConnectionEnum.CLIENT_PROD_TERESA*/);
        dataStoreFactory.addConnection(WombatConnectionEnum.WOMBAT_KODO);
        EntityManagedDataStore from = dataStoreFactory.getEntityManagedDataStoreSafely(0);
        EntityManagedDataStore to = dataStoreFactory.getEntityManagedDataStoreSafely(1);
        //kodo.util.OptimisticVerificationException when not enhanced with Kodo (which cannot be to detach
        //from Versant). So if want to get rid of all need to enhance for Kodo then flush, then enhance for
        //openaccess to run this again
        //to.flush( POJOWombatData.LOOKUP_CLASSES);
        to.flush( POJOWombatData.NON_LOOKUP_CLASSES);
        //WombatOpsUtils.transferDataManually(from, to);
        //Exclude RosterSlot because they are all attached to Worker, and want to keep this relationship
        //Above didn't work - looks like detachment (?for versant only) is only for first level, so no point
        //excluding any first class DOs
        Object excludedClasses[] = new Object[POJOWombatData.LOOKUP_CLASSES.length+2];
        Utils.assignArray( excludedClasses, POJOWombatData.LOOKUP_CLASSES);
        excludedClasses[POJOWombatData.LOOKUP_CLASSES.length] = POJOWombatData.USER;
        excludedClasses[POJOWombatData.LOOKUP_CLASSES.length+1] = POJOWombatData.LOOKUPS;
        Print.prArray( excludedClasses, "excludedClasses");
        DataStoreOpsUtils.CopiedResult copiedResult = DataStoreOpsUtils.transferDataFromVersant(
                from, to, 
                Utils.asArrayList(excludedClasses));
        to.setLookupsProvider( new LookupsProvider( to));
        /*
         * Now easiest is to do a Flush first which fills the lookup tables, so don't need to
         * do here 
         */
        if(FILL_LOOKUP_TABLES)
        {
            //Easiest to go from a completely fresh DB, to avoid kodo.util.OptimisticVerificationException
            //Exceptions ignored
            fillLookupTables( to);
        }
        new ReferenceFixer( copiedResult).fixReferences( from, to);
    }

    private static void fillLookupTables( EntityManagedDataStore dataStore)
    {
        dataStore.startTx();
        PopulateUpdateLOV pul = new PopulateUpdateLOV();
        for(int i = 0; i < POJOWombatData.LOOKUP_CLASSES.length; i++)
        {
            Object[] values = POJOWombatData.getLookupValues( POJOWombatData.LOOKUP_CLASSES[i]);
            pul.doPopulation(values, dataStore);
        }
        dataStore.commitTx();
    }
}
