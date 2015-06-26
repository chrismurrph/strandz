package org.strandz.store.gallery;

import org.strandz.data.gallery.domain.GalleryConnectionEnum;
import org.strandz.lgpl.store.CayenneByStringConnectionInfo;
import org.strandz.lgpl.store.ConnectionEnum;
import org.strandz.lgpl.store.Connections;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.EasyLoginAction;
import org.strandz.lgpl.view.LoginAction;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.util.Err;

/**
 * User: Chris
 * Date: 29/07/2008
 * Time: 11:04:14
 */
public class GalleryConnections extends Connections
{
    public static final GalleryConnectionEnum DEFAULT_DATABASE = GalleryConnectionEnum.DEV_GALLERY;

    abstract public static class GalleryCayenneByStringConnectionInfo extends CayenneByStringConnectionInfo
    {
        public GalleryCayenneByStringConnectionInfo(
            String fileName, ConnectionEnum connectionEnum, int estimatedConnectDuration,
            int loadLookupDataDuration, ORMTypeEnum ormTypeEnum)
        {
            super( fileName, connectionEnum, estimatedConnectDuration, loadLookupDataDuration, ormTypeEnum);
        }
        public String getSpringBeansFileName()
        {
            Err.error("getSpringBeansFileName()" + " not implemented");
            return null;
        }
    }

    public GalleryConnections()
    {
        //String fileName = "/some/path/to/my-cayenne.xml";
        //FileConfiguration conf = new FileConfiguration(new File(fileName));

        /**
         * Not portable, as just used to get used to Cayenne
         */
        //String files[] = new String[]{
        //        "C:\\dev\\cayenne.xml",
        //        "/usr/local/sdz-zone/property-files/gallery/cayenne.xml"
        //};
        initConnection(GalleryConnectionEnum.DEV_GALLERY,
                       new GalleryCayenneByStringConnectionInfo(
                           "cayenne-gallery.xml",
                           GalleryConnectionEnum.DEV_GALLERY,
                           750, 820, ORMTypeEnum.CAYENNE_SERVER)
                       {
                           public DataStore createDataStore()
                           {
                               return new CayenneGalleryDataStore(this);
                           }
                           public LoginAction createLoginAction( ConnectionEnum connectionEnum,
                                                                 DataStoreFactory dataStoreFactory
                                                                 )
                           {
                               LoginAction result = new EasyLoginAction();
                               return result;
                           }
                       });
        /*
        initConnection(GalleryConnectionEnum.DEV_GALLERY,
                       new GalleryCayenneByStringConnectionInfo(
                           ProdKpiConnectionEnum.PROD_KPI_MEMORY.getDescription(), 750, 820)
                       {
                           public DataStore createDataStore()
                           {
                               memoryProdKpiDataStore = new MemoryProdKpiDataStore(this);
                               return memoryProdKpiDataStore;
                           }
                           public LoginAction createLoginAction( ConnectionEnum connectionEnum,
                                                                 DataStoreFactory dataStoreFactory
                                                                 )
                           {
                               LoginAction result = new ProdKpiEasyLoginAction();
                               return result;
                           }
                       });
        */
    }
}
