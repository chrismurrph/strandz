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

import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.NoTaskTimeBandMonitorI;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Print;

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

abstract public class JDOInterpretedQuery extends WordyInterpretedQuery
{
    private Query q;
    private String ordering;
    private boolean alreadyDoneCopyTrick;
    private PersistenceManagerFactory jdoPMF;
    //Convenient for fixing corrupted DB
    private PersistenceManager jdoPM;

    //Always leave on, off for debugging only
    private static final boolean CHECK_RESULT = true;

    public JDOInterpretedQuery(Class queryOn,
                               String id,
                               NoTaskTimeBandMonitorI monitor,
                               boolean wrapInList)
    {
        super(queryOn, id, monitor, wrapInList);
    }

    public JDOInterpretedQuery(Class queryOn,
                               String id,
                               String filter,
                               String ordering,
                               String paramDeclaration,
                               NoTaskTimeBandMonitorI monitor)
    {
        super(queryOn, id, filter, paramDeclaration, monitor);
        this.ordering = ordering;
    }

    public JDOInterpretedQuery(Class queryOn,
                               String id,
                               String filter,
                               String ordering,
                               String paramDeclaration,
                               NoTaskTimeBandMonitorI monitor,
                               int estimatedDuration)
    {
        super(queryOn, id, filter, paramDeclaration, monitor, estimatedDuration);
        this.ordering = ordering;
    }
    
    public JDOInterpretedQuery(Class queryOn,
                               String id,
                               String filter,
                               String ordering,
                               String paramDeclaration,
                               NoTaskTimeBandMonitorI monitor,
                               boolean wrapInList,
                               boolean compileWhenConnect)
    {
        super(queryOn, id, filter, paramDeclaration, monitor, wrapInList, compileWhenConnect);
        this.ordering = ordering;
    }

    public JDOInterpretedQuery(Class queryOn,
                               String id,
                               String filter,
                               String ordering,
                               String paramDeclaration,
                               NoTaskTimeBandMonitorI monitor,
                               boolean wrapInList,
                               boolean compileWhenConnect,
                               int estimatedDuration)
    {
        super(queryOn, id, filter, paramDeclaration, monitor, wrapInList, compileWhenConnect, estimatedDuration);
        this.ordering = ordering;
    }
    
    /*
    * Can create the query at the time the PM is set,
    * which will be at connect time. Thus DBs will be
    * able to do some PREPARE work.
    */
    public void setEM(SdzEntityManagerI em, ErrorMsgContainer err)
    {
        jdoPM = (PersistenceManager) em.getActualEM();
        jdoPMF = (PersistenceManagerFactory)em.getActualEMF();
        if(queryOn != null)
        {
            q = jdoPM.newQuery(queryOn); //Won't know what to query on until it happens
        }
        else
        {
            q = jdoPM.newQuery();
        }
        Err.pr( WombatNote.NO_EM_WHEN_SOME_QUERY, "setEM() HAS been called on type <" + this.getClass().getName() + 
                "> described as <" + this.getDescription() + ">");
        q.setFilter(wordyFilter);
        q.setOrdering(ordering);
        q.declareParameters(paramDeclaration);
        if(queryOn != null && compileWhenConnect)
        {
            //Err.pr( "About to compile for query");
            try
            {
                q.compile();
                //Err.pr( "\tDone compile of " + this);
            }
            catch(JDOFatalUserException ex)
            {
                //Err.error(ex, id);
                err.isInError = true;
                err.message = id;
                err.exception = ex;
            }
            catch(Exception ex) //With vendor specific exceptions what else to do?
            {
                if(err != null)
                {
                    err.isInError = true;
                    err.message = id;
                    err.exception = ex;
                }
                else
                {
                    Err.error(ex, id);
                }
            }
        }
    }

    /**
     * Place to do something with the query before it is executed,
     * for example add a fetch group to it.
     */
    public void preExecute()
    {

    }

    /**
     * Place to do something with the query after it is executed,
     * for example to fetch something from a hollow object, 
     * which will trigger the rest of the fetch group to be fetched.
     */
    public void postExecute(Collection c)
    {

    }

    public Collection execute(Object[] parameters)
    {
        Collection<Object> result;
        chkSimpleParams( parameters);
        singleResult = null;
        preExecute();
        start(id);
        if(q == null)
        {
            Err.error( WombatNote.NO_EM_WHEN_SOME_QUERY, "setEM() has not been called on type <" + this.getClass().getName() + "> described as <" + this.getDescription() + ">");
        }
        if(parameters == NO_PARAM)
        {
            result = (Collection)q.execute();
        }
        else
        {
            result = (Collection)q.executeWithArray(parameters);
        }
        if(CHECK_RESULT)
        {
            chkResult(result);
        }
        if(isPerformCopyTrick())
        {
            copyTrick( result);
        }
        postExecute(result);
        formSingleResult(result);
        if(wrapInList)
        {
            result = new ArrayList<Object>(result);
        }
        stop();
        return result;
    }
    
    protected boolean isPerformCopyTrick()
    {
        return true;
    }
    
    private void copyTrick(Collection c)
    {
        if(!alreadyDoneCopyTrick && isPerformCopyTrick())
        {
            Utils.copyTrick( c);
            alreadyDoneCopyTrick = true;
        }
        else
        {
            Err.pr( JDONote.DEEP_COPY_TO_RETRIEVE, "Not doing deep copying");
        }
    }
    
//    public void postExecution()
//    {
//        
//    }

    public Query getQuery()
    {
        return q;
    }
    
    protected PersistenceManagerFactory getPMF()
    {
        return jdoPMF;
    }

    /*
     * Only time openjpa jar is used, so we will get rid of this...
     *
    protected void fixCorruptedDB(Collection c)
    {
        Transaction tx = jdoPM.currentTransaction();
        Err.pr( "Current txn is " + tx);
        List<Object> objects = new ArrayList<Object>();
        Long lowestId = null;
        for(Iterator iterator = c.iterator(); iterator.hasNext();)
        {
            Object o = iterator.next();
            Id id = (Id)JDOHelper.getObjectId( o);
            Long idNumber = id.getId();
            if(lowestId == null)
            {
                lowestId = idNumber;
            }
            else if(idNumber < lowestId)
            {
                lowestId = idNumber;
            }
            objects.add( o);
        }
        Print.prList( objects, "Lowest id is " + lowestId);
        for(Iterator<Object> iterator = objects.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            Long idNumber = ((Id)JDOHelper.getObjectId( obj)).getId();
            if(!idNumber.equals( lowestId))
            {
                Err.pr( "Deleting obj with id " + idNumber);
                jdoPM.deletePersistent( obj);        
            }
        }
        jdoPM.currentTransaction().commit();
    }
    */
}
