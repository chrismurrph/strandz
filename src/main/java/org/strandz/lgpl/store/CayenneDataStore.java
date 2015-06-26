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

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.query.SelectQuery;
import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.widgets.WidgetUtils;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TaskI;
import org.strandz.lgpl.util.TaskTimeBandMonitorI;
import org.strandz.lgpl.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

abstract public class CayenneDataStore extends EntityManagedDataStore
{
    private ObjectContext objectContext;
    private ObjectContext readObjectContext; // for debug check
    private boolean newDCEachTransaction = false;
    private ORMTypeEnum ormTypeEnum;

    private int commitTimes;
    private int beginTimes;

    private static final boolean SAVE_EXTENTS = false;
    private static int setEMTimes;

    public CayenneDataStore()
    {
        setEstimatedConnectDuration(28000);
        setEstimatedLookupDataDuration(10000);
    }

    public void startTx( String reason)
    {
        super.startTx( reason);
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
        if(objectContext == null || newDCEachTransaction)
        {
            //To test with no job at all
            //new ConnectionTask(this).newTask();
            /**/
            TaskTimeBandMonitorI monitor = WidgetUtils.getTimeBandMonitor(getEstimatedConnectDuration());
            //Only after the task is done will we have an EM
            TaskI task = new ConnectionTask(this, getCayenneORMType());
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
            if(objectContext == null)
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
        if(SAVE_EXTENTS)
        {
            extents.clear();
            for(int i = 0; i < classes.length; i++)
            {
                Class clazz = classes[i];
                SelectQuery selectQuery = new SelectQuery( clazz);
                List list = objectContext.performQuery( selectQuery);
                extents.add( list);
            }
        }
        readObjectContext = objectContext;
    }

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
        if(objectContext != null)
        {
            objectContext.commitChanges();
        }
        else
        {
            Err.error("No pm, so can't commit");
        }
        Err.pr(SdzNote.TX, "COMMIT of " + this + " result is " + result);
        return result;
    }

    public void set(int whichExtent, final Object obj)
    {
        if(SAVE_EXTENTS)
        {
            extents.set( whichExtent, obj);
        }
        Err.pr( "To register new object: " + obj);
        registerNewObject( objectContext, (DataObject)obj);
    }

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
        boolean result;
        if(objectContext != readObjectContext)
        {
            Err.pr("read PM: " + readObjectContext);
            Err.pr("now PM: " + objectContext);
            Err.error("Are not using the same pm that read with");
        }
        super.commitTx();

        try
        {
            result = _commitTx();
        }
        catch(CayenneRuntimeException ex)
        {
            Err.error( ex);
            result = false;
        }
        catch(Exception ex)
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

    public List query(Class clazz)
    {
        Assert.notNull(objectContext, "Cannot query all of " + clazz + " until have started a transaction");
        SelectQuery selectQuery = new SelectQuery( clazz);
        List list = objectContext.performQuery( selectQuery);
        return list;
    }

    public void flush( Class classes[])
    {
        String reason = "To delete " + classes.length + " tables";
        Err.pr( reason);
        for(int i = 0; i < classes.length; i++)
        {
            startTx( reason);
            Class clazz = classes[i];
            Err.pr("To delete all of class " + clazz.getName());
            SelectQuery selectQuery = new SelectQuery( clazz);
            List list = objectContext.performQuery( selectQuery);
            int count = list.size();
            //Still gave fk constraint problems
            //
            try
            {
                //Print.prList( list, "About to delete");
            }
            catch(Exception ex)
            {
                if(ex.getMessage().equals("Cannot read fields from a deleted object"))
                {
                    Err.pr("About to delete " + clazz + " but cannot show the contents (no big deal)");
                }
            }
            deleteObjects( objectContext, list);
            Err.pr("Have deleted " + count + " of type " + clazz.getName());
            commitTx();
        }
    }

    abstract void deleteObjects( ObjectContext objectContext, Collection collection);
    abstract void registerNewObject( ObjectContext objectContext, DataObject dataObject);

    public void setEM(SdzEntityManagerI em, ErrorMsgContainer err)
    {
        super.setEM(em, err);
        if(err == null || !err.isInError)
        {
            objectContext = (ObjectContext)em.getActualEM();
            setEMTimes++;
            Err.pr( JDONote.RELOAD_PM_KODO_BUG, "objectContext has been set to " + objectContext + " times " + setEMTimes);
            if(setEMTimes == 0)
            {
                Err.stack();
            }
        }
    }

    public void setNewDCEachTransaction(boolean newDCEachTransaction)
    {
        this.newDCEachTransaction = newDCEachTransaction;
    }

    protected ORMTypeEnum getCayenneORMType()
    {
        return ormTypeEnum;
    }

    protected void setCayenneORMType(ORMTypeEnum ormTypeEnum)
    {
        this.ormTypeEnum = ormTypeEnum;
    }
}