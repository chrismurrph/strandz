package org.strandz.applic.books;

import org.strandz.lgpl.util.Err;
import org.strandz.core.applichousing.SimplestApplicationStrandRunner;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.applichousing.SimpleApplication;
import org.strandz.lgpl.store.DataStore;
import org.strandz.store.books.BooksDataStoreFactory;


public class RunListExpenses
{
    public static void main( String[] args)
    {
        Err.setBatch( false);
        SimpleApplication simple = new SimpleApplication();
        DataStore dataStore = new BooksDataStoreFactory( true).getDataStore();
        simple.setDataStore( dataStore);
        ListExpensesStrand strand = new ListExpensesStrand( simple);
        VisibleStrandAction vsa = new VisibleStrandAction( "ListExpenses", "ListExpenses");
        vsa.setVisibleStrand( strand);
        simple.addItem( vsa);
        SimplestApplicationStrandRunner strandRunner = new SimplestApplicationStrandRunner( simple);
        strand.sdzInit();
        strandRunner.execute( vsa);
    }
}