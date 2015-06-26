/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.store;

import org.strandz.lgpl.persist.JDOVendorOpsFactory;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.Utils;

import javax.jdo.JDOFatalDataStoreException;
import java.util.ArrayList;
import java.util.List;

public class DataStoreOpsUtils
{
    public static void chkSizeAndType(DataStore from, DataStore to)
    {
        if(from.classes.length != to.classes.length)
        {
            Err.error("Cannot transfer data when differing number of extents");
        }
        for(int i = 0; i < from.classes.length; i++)
        {
            Class fromType = from.classes[i];
            Class toType = to.classes[i];
            if(fromType != toType)
            {
                Err.error("Classes in order need to be all of the same type");
            }
        }
        if(from.classes.length == 0)
        {
            Err.error("Empty dataStore: " + from.getClass().getName());
        }
    }

    public static void chkEMs(EntityManagedDataStore from, EntityManagedDataStore to)
    {
        if(from.getEM() == null)
        {
            Err.error( "from.getPM() == null");
        }
        if(to.getEM() == null)
        {
            Err.error( "to.getPM() == null");
        }
    }

    public static void transferData(EntityManagedDataStore from, EntityManagedDataStore to)
    {
        transferDataFromVersant(from, to, new ArrayList());
    }

    public static class CopiedResult
    {
        public List source = new ArrayList();
        public List target = new ArrayList();
    }

    /**
     * Useful to for example backup from a JDO database to
     * an XML file. See flush for comments as to why it may
     * not work for an O/R style DataStore. Solution is to
     * drop all the tables and use SchemaTool to get them
     * back again.
     * <p/>
     * REASON (not necessarily only one)  Will not work going JDOGenie -> JPOX is that
     * JDOGenie/Versant enhances with non-standard methods. The target PM will be
     * expecting to be able to call this method:
     * Exception in thread "main" java.lang.AbstractMethodError: org.strandz.data.wombatrescue.objects.Worker.jdoIsDetached()Z
     * , but won't find it as the Versant enhancement process will go for something like versantJdoIsDetached().
     * Why not just enhance with JPOX then? When try get:
     * Exception in thread "main" javax.jdo.JDOFatalUserException: 'org.strandz.data.wombatrescue.objects.WeekInMonth' is not detachable please enhance classes with the detach option set to true
     * Basically this is Versant recognising that one of 'their' methods does not exist.
     * <p/>
     * Thus stopped this and went to transferDataManually()
     */
    public static CopiedResult transferDataFromVersant(EntityManagedDataStore from, EntityManagedDataStore to, List excludeClasses)
    {
        CopiedResult result = new CopiedResult();
        Err.pr("%% Transfer FROM: " + from);
        Err.pr("%% Transfer TO: " + to);
        chkSizeAndType(from, to);
        from.startTx();
        for(int i = 0; i < from.classes.length; i++)
        {
            if(!excludeClasses.contains(from.classes[i]))
            {
                List fromList = from.query(from.classes[i]);
                Utils.chkNoNulls(fromList, "transferData()");
                result.source.add( fromList);
                //Will be transient at end of transaction anyway
                //Can't be, as needed to do this
                //Without get loads of:
                //javax.jdo.JDOUserException: Object managed by a different Persistence Manager
                //from.getPM().registerTransientAll( fromList);
                //registerTransientAll() gives nulls as references to other objects, b/c the DB identity
                //has been lost, so this will fix that (?):
                //Err.error( "Need to have JDO spec around to comment out the following two lines:");
                //List copiedList = (List) from.getEM().detachCopyAll( fromList);
                //result.target.add( copiedList);
                //Err.error("When properly moved to JDO 2.0, this won't be proprietary");
                /**/
                //List copiedList = (List) ((VersantPersistenceManager)from.getEM().getActualEM()).versantDetachCopy( fromList, "MASTER copy of all " + from.classes[i]);
                List copiedList = JDOVendorOpsFactory.newVendorOps( JDOVendorOpsFactory.VERSANT).detachCopy( from.getEM(), fromList);
                result.target.add( copiedList);
                /**/
                //seeIfWorkersHaveRSs( copiedList);
            }
        }
        from.commitTx();
        for(int i = 0; i < result.target.size(); i++)
        {
            if(!to.isOnTx())
            {
                to.startTx();
            }
            List fromList = (List) result.target.get(i);
            to.set(from.classes[i], fromList); //basically does registerPersistentAll()
            to.commitTx();
        }
        return result;
    }

    //commented out as will prolly contravean package dep rules
    //Can't investigate due to com.versant.core.jdo.VersantDetachedFieldAccessException
//    private static void seeIfWorkersHaveRSs( List list)
//    {
//        if(!list.isEmpty() && list.get(0) instanceof Worker)
//        {
//            for(Iterator iterator = list.iterator(); iterator.hasNext();)
//            {
//                Worker worker = (Worker) iterator.next();
//                Err.pr( "worker " + worker + " has " + worker.getPlayers().size() + " roster slots");
//                if(!worker.getPlayers().isEmpty())
//                {
//                    Print.prList( worker.getPlayers(), "RSs of " + worker.getSurname());
//                }
//            }
//        }
//    }
    
    public static void viewData(DataStore toView)
    {
        List<Class> excludedClasses = Utils.formList( (Class)null);
        viewData( toView, excludedClasses);
    }

    public static void viewData(DataStore toView, List<Class> excludedClasses)
    {
        toView.startTx();
        Err.pr( "Viewing " + toView);
        for(int i = toView.classes.length - 1; i >= 0; i--)
        {
            if(!excludedClasses.contains( toView.classes[i]))
            {
                List list = toView.query(toView.classes[i]);
                Print.prList(list, "To view: " + toView.classes[i]);
            }
        }
        toView.commitTx();
    }

    public static void compareData(DataStore ds1, DataStore ds2)
    {
        DataStoreOpsUtils.chkSizeAndType(ds1, ds2);
        ds1.startTx();
        ds2.startTx();
        for(int i = 0; i < ds1.classes.length; i++)
        {
            boolean skip = false;
            List list1 = null;
            try
            {
                list1 = ds1.query(ds1.classes[i]);
            }
            catch(JDOFatalDataStoreException ex)
            {
                Err.pr("Problem with class " + ds1.classes[i] + " on " + ds1 + ": " + ex.getMessage());
                skip = true;
            }
            catch(Exception ex)
            {
                Err.error(ex, "Problem with class " + ds1.classes[i] + " on " + ds1);
            }
            List list2 = null;
            try
            {
                list2 = ds2.query(ds2.classes[i]);
            }
            catch(JDOFatalDataStoreException ex)
            {
                Err.pr("Problem with class " + ds1.classes[i] + " on " + ds1 + ": " + ex.getMessage());
                skip = true;
            }
            catch(Exception ex)
            {
                Err.error(ex, "Problem with class " + ds2.classes[i] + " on " + ds2);
            }

            if(!skip)
            {
                Utils.chkNoNulls(list1, "compareData()");
                Utils.chkNoNulls(list2, "compareData()");
                ReasonNotEquals.turnOn(true);
                Err.pr("About to cf class " + ds2.classes[i].getName());
                if(!list1.equals(list2))
                {
                    if(!ReasonNotEquals.hasReasons())
                    {
                        if(list1.size() != list2.size())
                        {
                            Err.pr("Data is ------ NOT ------ equal for the class: " + ds2.classes[i] + " because " +
                                ds1 + " is of size " + list1.size() + " while " + ds2 + " is of size " + list2.size());
                        }
                        else
                        {
                            Err.pr(ds1 + " ------ not ------ equal to " + ds2 + " for class " + ds2.classes[i] + " for an unknown reason");
                        }
                    }
                    else //have reasons want to show
                    {
                        Err.pr("Data is ------ NOT ------ equal for the class: " + ds2.classes[i] + " because " + ReasonNotEquals.formatReasons());
                    }
                }
                else
                {
                    Err.pr("Data IS equal for the class: " + ds2.classes[i]);
                }
                ReasonNotEquals.turnOn(false);
            }
        }
        /*
        * Rollback better than commit because even if a transaction aborts because of
        * an error in what happens above, we won't get a stack trace.
        */
        ds1.rollbackTx();
        ds2.rollbackTx();
    }
}
