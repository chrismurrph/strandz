package org.strandz.applic.cayenne;

import org.strandz.data.gallery.domain.GalleryConnectionEnum;
import org.strandz.data.gallery.domain.GalleryDomainQueryEnum;
import org.strandz.lgpl.store.*;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.store.gallery.GalleryDataStoreFactory;
import org.strandz.store.gallery.GalleryConnections;

/**
 * To talk to Cayenne through Strandz
 *
 * User: Chris
 * Date: 29/07/2008
 * Time: 08:32:20
 */
public class TryQuery
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
        String name = "Vincent VanGough";
        GalleryDomainQueryEnum ofType = GalleryDomainQueryEnum.ARTIST_BY_NAME;
        Object obj = queries.executeRetObject( ofType, name);
        Err.pr( "Artist called " + name + ": " + obj);
        dataStore.commitTx();
    }
}
