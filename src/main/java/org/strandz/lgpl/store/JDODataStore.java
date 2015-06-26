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

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.persist.JDOUtils;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.TaskI;
import org.strandz.lgpl.util.TaskTimeBandMonitorI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.widgets.WidgetUtils;

import javax.jdo.Extent;
import javax.jdo.JDOException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

abstract public class JDODataStore extends EntityManagedDataStore
{
    protected Properties props;
    private PersistenceManager jdoPM;
    private PersistenceManager readPM; // for debug check
    private JDODataStore outer;
    private boolean newPMEachTransaction = false;

    private int commitTimes;
    private int rollbackTimes;
    private int beginTimes;
    
    private static int setEMTimes;

    public JDODataStore()
    {
        outer = this;
        setEstimatedConnectDuration(28000);
        setEstimatedLookupDataDuration(10000);
    }

    public void startTx( String reason)
    {
        super.startTx( reason);
        if(jdoPM != null)
        {
            if(jdoPM.currentTransaction().isActive())
            {
                _rollbackTx();
            }
        }
        _beginTx();
    }
    
    private ErrorMsgContainer resetState( ErrorMsgContainer err)
    {
        ErrorMsgContainer result = new ErrorMsgContainer( err); 
        err = null;
        decReadingTimes(); //Even when have errored need to keep the state of this object
        return result;
    }

    private void _beginTx()
    {
        beginTimes++;
        Err.pr(SdzNote.TX, "beginTx times " + beginTimes);
        if (beginTimes == 0)
        {
           Err.stack();
        }
        if(jdoPM == null || newPMEachTransaction)
        {
            //To test with no job at all
            //new ConnectionTask(this).newTask();
            /**/
            TaskTimeBandMonitorI monitor = WidgetUtils.getTimeBandMonitor(getEstimatedConnectDuration());
            TaskI task = new ConnectionTask(this, ORMTypeEnum.JDO);
            ErrorMsgContainer err = new ErrorMsgContainer( "First time connecting for " + getName());  
            monitor.start(task, err);
            if(err.isInError)
            {
                //Even when have errored need to keep the state of this object.
                //When readingTimes is back to zero then for next time we won't 
                //have started at transaction
                err = resetState( err); 
                monitor.stop();
                Err.error( err.id + ", " + err.message, err.exception);
            }
            if(jdoPM == null)
            {
                resetState( err); 
                Err.error("Should never happen when the setAsynchronous( false) call is made");
            }
            monitor.stop();
            /**/
        }
        else
        {
            postConnection();
        }
    }

    protected void postConnection()
    {
        jdoPM.currentTransaction().begin();
        extents.clear();
        for(int i = 0; i < classes.length; i++)
        {
            Class clazz = classes[i];
            Extent extent = jdoPM.getExtent(clazz, false);
            extents.add(extent);
        }
        readPM = jdoPM;
    }

    private void _rollbackTx()
    {
        rollbackTimes++;
        Err.pr(SdzNote.TX, "rollbackTx times " + rollbackTimes);
        if(rollbackTimes == 0)
        {
            Err.stack();
        }
        if(jdoPM != null)
        {
            Transaction tx = jdoPM.currentTransaction();
            if(tx.isActive())
            {
                tx.rollback();
            }
            else
            {// Err.error( "No active transaction so nothing will be rolled back");
            }
        }
        else
        {// Err.error( "No pm, so can't roll back");
        }
    }

    //private void fireConcurrentExceptionListener( JDOGenieConcurrentUpdateException ex )
//    private void fireCommitExceptionListener(JDOException ex)
//    {
//        Err.error(ex, "Not able to commit, got " + ex);
//    }

    private boolean _commitTx()
    {
        // Err.error( "Before commit JDO make sure right list has been madePersistent " +
        // "and that doing it only happens once per PM");
        //int result = classes.length;
        boolean result = true;

        commitTimes++;
        Err.pr(SdzNote.TX, "commitTx times " + commitTimes);
        if(commitTimes == 0)
        {
            Err.stack();
        }
        if(jdoPM != null)
        {
            Transaction tx = jdoPM.currentTransaction();
            if(tx.isActive())
            {
//                try
//                {
                    tx.commit();
//                }
//                catch(JDOException ex)
//                {
//                    Session.getErrorThrower().throwApplicationError(ex.getMessage(),
//                        ApplicationErrorEnum.INTERNAL);
//                    Err.error( "Will never get to here!");
//                    result = false;
//                }
            }
            else
            {
                Err.error("No active transaction so nothing will be committed");
            }
        }
        else
        {
            Err.error("No pm, so can't commit");
        }
        Err.pr(SdzNote.TX, "COMMIT of " + this + " result is " + result);
        return result;
    }

    /**
     * Note that the call to makePersistentAll will cause a
     * blowout when first import from an XML file, as for instance
     * dates will be changed to the vendor's type of date.
     */
    public void set(int whichExtent, final Object obj)
    {
        extents.set( whichExtent, obj);
        // Print.pr( "%% To set " + obj + " at " + whichExtent);
        /*
        Extent extent = (Extent)extents.get( whichExtent);
        if(extent == null)
        {
        Err.error( "Can't call set() when extent is null");
        }
        if(obj == null)
        {
        Err.error("Trying to insert null into: " + whichExtent);
        }
        */
        /*
        Class clazz = classes[whichExtent];
        Class interfaces[] = clazz.getInterfaces();
        boolean comparable = false;
        for (int i = 0; i < interfaces.length; i++)
        {
          if (interfaces[i] == Comparable.class)
          {
            comparable = true;
            break;
          }
        }
        if (comparable && (obj instanceof List))
        {
          // Thought would be necessary to get them in
          // the right order, but source of the problem
          // was that was storing an array.
          Collections.sort( (List) obj );
        }
        else
        {
          if (!comparable)
          {
            Err.pr( clazz + " is not comparable" );
          }
          else
          {
            Err.pr( obj.getClass() + " is not a List" );
          }
        }
        */
        JDOUtils.makePersistentAll(jdoPM, obj);
        //Print.prList((List) obj, obj.getClass().getName() + " that making persistent in " + this);
    }

    /*
    * Don't need these two, as can put a Class into a query
    public Extent getExtent( Class clazz)
    {
    return (Extent)extents.get( indexOfClass( clazz));
    }
    public Extent getExtent( int whichExtent)
    {
    return getExtent( classes[whichExtent]);
    }
    */

    public Object get(Class clazz)
    {
        return query(clazz);
    }

    /**
     * Where a DO refers to itself is a good example of where a fk integrity constraint
     * will mean that an SQLException will be thrown.
     * "Cannot delete or update a parent row: a foreign key constraint fails"
     * On MySql dropping the constraints doesn't work on the version using, so drop all the
     * tables then use SchemaTool to get them back empty. On a DB that supports it:
     * alter table worker drop foreign key worker_fk1
     */
    public void flush()
    {
        flush( classes);
    }

    public boolean commitTx()
    {
        boolean result = false;
        if(jdoPM != readPM)
        {
            Err.pr("read PM: " + readPM);
            Err.pr("now PM: " + jdoPM);
            Err.error("Are not using the same pm that read with");
        }
        super.commitTx();

        try
        {
            result = _commitTx();
        }
        catch(JDOException ex)
        {
            //Won't work when the server is down - and will be done/attempted anyway
            //_rollbackTx();
//            Err.pr("ERROR MSG " + ex.getMessage());
//            Err.pr("ERROR STR " + ex.toString());
//            Err.error(ex);
            //super.startTx(); //so user can keep trying to commit - 
            //but JDO will have taken off having a transaction anyway - so will still get a stack trace
            /*
             * Assumption here is that the failure is due to a connection problem. An automatic rollback will
             * have occurred. The user will have no way of commiting changes. As there is no connection we 
             * cannot start a transaction again. So that the user does not lose changes one solution might 
             * be to have a secret keystroke that starts a new transaction (not commiting the last).
             */
            Err.pr( ex.getMessage());
            List msg = new ArrayList();
            msg.add( "Unable to connect to the server to commit. ");
            if(getSupportPhone() != null)
            {
                msg.add( "Phone support on " + getSupportPhone() + ". Support will ask you to restart the application.");
            }
            else
            {
                msg.add( "Phone support. Support will ask you to restart the application.");
            }
            //Session.getErrorThrower().throwApplicationError(msg,
            //    ApplicationErrorEnum.INTERNAL);
            //Err.error( "Commented out code above causes dependency problem!");
            if(getErrorThrower() != null)
            {
                getErrorThrower().throwApplicationError( msg);
            }
            else
            {
                Err.error( msg);
            }
            result = false;
        }
        // debug();
        return result;
    }
    
    /*
    * Need to override this if want to use caching
    */
    public List query(Class clazz)
    {
        Assert.notNull( jdoPM, "Cannot query all of " + clazz + " until have started a transaction"); 
        Query q = jdoPM.newQuery(clazz);
        Collection c = (Collection) q.execute();
        return new ArrayList(c);
    }
    
    public void flush( Class classes[])
    {
        String reason = "To delete " + classes.length + " tables";
        Err.pr( reason);
        for(int i = 0; i < classes.length; i++)
        {
            startTx( reason);
            Class clazz = classes[i];
            // pm.deletePersistent( extent);
            Print.pr("To delete all of class " + clazz.getName());
            /*
            List list = new ArrayList();
            for(Iterator iter = extent.iterator(); iter.hasNext(); count++)
            {
                Object element = iter.next();
                //gives fk constraint problems trying to avoid
                //pm.deletePersistent( element );
                list.add(element);
            }
            */
            Query q = jdoPM.newQuery(clazz);
            Collection c = (Collection) q.execute();
            int count = c.size();
            //Still gave fk constraint problems
            //
            try
            {
                //Print.prList( list, "About to delete");
            }
            catch(JDOUserException ex)
            {
                if(ex.getMessage().equals("Cannot read fields from a deleted object"))
                {
                    Err.pr("About to delete " + clazz + " but cannot show the contents (no big deal)");
                }
            }
            jdoPM.deletePersistentAll(c);
            Print.pr("Have deleted " + count + " of type " + clazz.getName());
            commitTx();
        }
    }

    public void setEM(SdzEntityManagerI em, ErrorMsgContainer err)
    {
        super.setEM(em, err);
        if(err == null || !err.isInError)
        {
            jdoPM = (PersistenceManager) em.getActualEM();
            setEMTimes++;
            Err.pr( JDONote.RELOAD_PM_KODO_BUG, "jdoPM has been set to " + jdoPM + " times " + setEMTimes);
            if(setEMTimes == 0)
            {
                Err.stack();
            }
        }
    }

    public void setNewPMEachTransaction(boolean newPMEachTransaction)
    {
        this.newPMEachTransaction = newPMEachTransaction;
    }

    public void setConnectionProperties(Properties props)
    {
        this.props = props;
    }

    public void setConnectionProperties(Properties props, ConnectionInfo connectionInfo)
    {
        this.props = props;
        Err.pr( "Some old, eg StartupLocalDemo, do not use a secure subclass");
    }

    public Properties getConnectionProperties()
    {
        return props;
    }

    /*
    public void setRefinedList( Class clazz, Object list )
    {// As same list that hand to cell, then there is actually
      // no need to do this, as cell.setData() performs
      // registerPersistentAll().
      // Err.error( "This is the list need to registerPersistent(): " + list);
    }
    */

    /**
     * As this class holds onto a pm, is can be closed/gced here
     */
    /*
    public void close()
    {
    if(pm != null)
    {
    if(howDealWithLastTxn == NO_CURRENT_TXN &&
    pm.currentTransaction().isActive())
    {
    Err.error( "There is an active transaction");
    }
    }
    switch( howDealWithLastTxn)
    {
    case ABORT_CURRENT_TXN_IF:
    rollbackTx();
    break;
    case KEEP_CURRENT_TXN_IF:
    case ONLY_READ:
    commitTx();
    break;
    case NO_CURRENT_TXN:
    break;
    default:
    Err.error( "Not yet written code for " + howDealWithLastTxn);
    break;
    }
    if(pm != null)
    {
    if(pm.currentTransaction().isActive())
    {
    Print.pr( "!!!!!!! actually useful to close th pm");
    pm.close();
    }
    pm = null;
    }
    }
    */
}
