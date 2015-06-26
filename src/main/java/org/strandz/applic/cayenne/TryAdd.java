package org.strandz.applic.cayenne;

import org.strandz.data.gallery.domain.GalleryConnectionEnum;
import org.strandz.data.gallery.domain.GalleryDomainQueryEnum;
import org.strandz.data.gallery.objects.Artist;
import org.strandz.lgpl.store.*;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.store.gallery.GalleryDataStoreFactory;
import org.strandz.store.gallery.GalleryConnections;

import java.util.List;

/**
 * To talk to Cayenne through Strandz
 *
 * User: Chris
 * Date: 29/07/2008
 * Time: 08:32:20
 */
public class TryAdd
{
    private static EntityManagedDataStore dataStore;

    public static void main(String s[])
    {
        processParams();
    }

    public static DataStore getData()
    {
        if(dataStore == null)
        {
            String str[] = {};
            main(str);
        }
        return dataStore;
    }

    private static void processParams()
    {
        DataStoreFactory factory = new GalleryDataStoreFactory();
        factory.addConnection(GalleryConnectionEnum.getFromName(
            GalleryConnections.DEFAULT_DATABASE.getName()));
        dataStore = (EntityManagedDataStore)factory.getDataStore();
        ConnectionTask task = new ConnectionTask( dataStore, dataStore.getEM().getORMType());
        task.newTask();
        //Err.pr( "em: " + dataStore.getEM());
        dataStore.startTx();
        DomainQueriesI queries = dataStore.getDomainQueries();
        GalleryDomainQueryEnum ofType = GalleryDomainQueryEnum.ARTISTS;
        List list = (List)queries.executeRetCollection( ofType);
        Print.prList( list, "All before add any " + ofType);
        Artist newArtist = new Artist();
        newArtist.setArtistName( "Vincent VanGough");
        dataStore.getEM().registerPersistent( newArtist);
        dataStore.commitTx();
    }
}