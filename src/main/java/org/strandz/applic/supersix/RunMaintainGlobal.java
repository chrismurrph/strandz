package org.strandz.applic.supersix;

import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.applichousing.SimpleApplication;
import org.strandz.core.applichousing.SimplestApplicationStrandRunner;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.store.supersix.SuperSixDataStoreFactory;

public class RunMaintainGlobal
{
    public static void main( String[] args)
    {
        Err.setBatch( false);
        SimpleApplication simple = new SimpleApplication();
        DataStore dataStore = new SuperSixDataStoreFactory( true).getDataStore();
        simple.setDataStore( dataStore);
        MaintainGlobalStrand strand = new MaintainGlobalStrand( simple, null);
        VisibleStrandAction vsa = new VisibleStrandAction( "MaintainGlobal", "MaintainGlobal");
        vsa.setVisibleStrand( strand);
        simple.addItem( vsa);
        SimplestApplicationStrandRunner strandRunner = new SimplestApplicationStrandRunner( simple);
        strand.sdzInit();
        strandRunner.execute( vsa);
    }
}