package org.strandz.applic.supersix;

import org.strandz.lgpl.util.Err;
import org.strandz.core.applichousing.SimplestApplicationStrandRunner;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.applichousing.SimpleApplication;
import org.strandz.lgpl.store.DataStore;
import org.strandz.store.supersix.SuperSixDataStoreFactory;
import org.strandz.data.supersix.business.SuperSixManagerUtils;


public class RunMaintainSeason
{
    public static void main( String[] args)
    {
        Err.setBatch( false);
        SimpleApplication simple = new SimpleApplication();
        DataStore dataStore = new SuperSixDataStoreFactory( true).getDataStore();
        SuperSixManagerUtils.setDataStore( dataStore);
        simple.setDataStore( dataStore);
        MaintainSeasonStrand strand = new MaintainSeasonStrand( simple);
        VisibleStrandAction vsa = new VisibleStrandAction( "MaintainSeason", "MaintainSeason");
        vsa.setVisibleStrand( strand);
        simple.addItem( vsa);
        SimplestApplicationStrandRunner strandRunner = new SimplestApplicationStrandRunner( simple);
        strand.sdzInit();
        strandRunner.execute( vsa);
    }
}