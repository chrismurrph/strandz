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

import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.NoTaskTimeBandMonitorI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Print;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionException;
import org.apache.cayenne.ObjectContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

abstract public class CayenneInterpretedQuery extends InterpretedQuery implements DomainQueryI
{
    private SelectQuery q;
    public String cayenneFilter;
    private List<Ordering> orderings;
    private String paramDeclarations[];
    private ObjectContext objectContext;
    private List<String> prefetches;
    //

    //Always leave on, off for debugging only
    private static final boolean CHECK_RESULT = true;

    public CayenneInterpretedQuery(Class queryOn,
                               String id,
                               NoTaskTimeBandMonitorI monitor,
                               boolean wrapInList)
    {
        super(queryOn, id, monitor, wrapInList);
        chkClassIsCayenne( queryOn);
    }

    public CayenneInterpretedQuery(Class queryOn,
                               String id,
                               String cayenneFilter,
                               List<Ordering> orderings,
                               String paramDeclarations[],
                               NoTaskTimeBandMonitorI monitor,
                               List<String> prefetches)
    {
        super(queryOn, id, monitor);
        chkClassIsCayenne( queryOn);
        this.cayenneFilter = cayenneFilter;
        this.paramDeclarations = paramDeclarations;
        this.orderings = orderings;
        this.prefetches = prefetches;
    }

    public CayenneInterpretedQuery(Class queryOn,
                               String id,
                               String cayenneFilter,
                               List<Ordering> orderings,
                               String paramDeclarations[],
                               NoTaskTimeBandMonitorI monitor)
    {
        this( queryOn, id, cayenneFilter, orderings, paramDeclarations, monitor, null);
    }

    public CayenneInterpretedQuery(Class queryOn,
                               String id,
                               String cayenneFilter,
                               List<Ordering> orderings,
                               String paramDeclarations[],
                               NoTaskTimeBandMonitorI monitor,
                               int estimatedDuration,
                               List<String> prefetches)
    {
        super(queryOn, id, monitor, estimatedDuration);
        chkClassIsCayenne( queryOn);
        this.cayenneFilter = cayenneFilter;
        this.paramDeclarations = paramDeclarations;
        this.orderings = orderings;
        this.prefetches = prefetches;
    }

    public CayenneInterpretedQuery(Class queryOn,
                               String id,
                               String cayenneFilter,
                               List<Ordering> orderings,
                               String paramDeclarations[],
                               NoTaskTimeBandMonitorI monitor,
                               int estimatedDuration)
    {
        this( queryOn, id, cayenneFilter, orderings, paramDeclarations, monitor, estimatedDuration, null);
    }

    public CayenneInterpretedQuery(Class queryOn,
                               String id,
                               String cayenneFilter,
                               List<Ordering> orderings,
                               String paramDeclarations[],
                               NoTaskTimeBandMonitorI monitor,
                               boolean wrapInList,
                               boolean compileWhenConnect)
    {
        super(queryOn, id, monitor, wrapInList, compileWhenConnect);
        this.cayenneFilter = cayenneFilter;
        this.paramDeclarations = paramDeclarations;
        this.orderings = orderings;
        if(queryOn != null)
        {
            chkClassIsCayenne( queryOn);
        }
        this.orderings = orderings;
    }

    public CayenneInterpretedQuery(Class queryOn,
                               String id,
                               String cayenneFilter,
                               List<Ordering> orderings,
                               String paramDeclarations[],
                               NoTaskTimeBandMonitorI monitor,
                               boolean wrapInList,
                               boolean compileWhenConnect,
                               int estimatedDuration)
    {
        super(queryOn, id, monitor, wrapInList, compileWhenConnect, estimatedDuration);
        chkClassIsCayenne( queryOn);
        this.cayenneFilter = cayenneFilter;
        this.paramDeclarations = paramDeclarations;
        this.orderings = orderings;
    }

    private static void chkClassIsCayenne( Class queryOn)
    {
        /*
        String mainClassName = queryOn.getName();
        Assert.isTrue( mainClassName.contains( "cayenne"),
            "S/not be executing a cayenne style query on a <" + mainClassName + ">");
        */
    }

    /*
    * Can create the query at the time the PM is set,
    * which will be at connect time. Thus DBs will be
    * able to do some PREPARE work.
    */
    public void setEM(SdzEntityManagerI em, ErrorMsgContainer err)
    {
        objectContext = (ObjectContext) em.getActualEM();
        //dataContextFactory = (DataContextFactory)em.getActualEMF();
        if(cayenneFilter != null)
        {
            Assert.notNull( queryOn);
            Expression qualifier = null;
            try
            {
                qualifier = Expression.fromString(cayenneFilter);
            }
            catch(ExpressionException ex)
            {
                Err.error( "Cayenne cannot parse filter expression: <" + cayenneFilter + "> because " + ex.toString());
            }
            q = new SelectQuery( queryOn, qualifier);
        }
        else if(queryOn != null)
        {
            q = new SelectQuery(queryOn); //Won't know what to query on until it happens
        }
        else
        {
            q = new SelectQuery();
        }
        Err.pr( WombatNote.NO_EM_WHEN_SOME_QUERY, "setEM() HAS been called on type <" + this.getClass().getName() +
                "> described as <" + this.getDescription() + ">");
        //Assert.isNull( filter);
        //q.setFilter(filter);
        //Assert.isNull( orderings, "Asking to order: <" + orderings + ">");
        if(orderings != null)
        {
            q.addOrderings(orderings);
        }
        if(prefetches != null)
        {
            Assert.notEmpty( prefetches, "prefetches cannot be an empty list - " +
                "use null if you don't want any on <" + getDescription() + ">");
            for (int i = 0; i < prefetches.size(); i++)
            {
                String s = prefetches.get(i);
                q.addPrefetch(s)
                //.setSemantics( PrefetchTreeNode.JOINT_PREFETCH_SEMANTICS)
                ;
            }
            if(SdzNote.PERFORMANCE_TUNING.isVisible())
            {
                Print.prList( prefetches, "prefetches for <" + this.getDescription() + ">");
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
            /*
             * Need to start a transaction!
             */
            Err.error( WombatNote.NO_EM_WHEN_SOME_QUERY,
                "setEM() has not been called on type <" + this.getClass().getName() + "> described as <" + this.getDescription() + ">");
        }
        chkClassIsCayenne( queryOn);
        Err.pr( SdzNote.PERFORMANCE_TUNING, "Query on: <" + queryOn + "> using query: <" + this.getDescription() + ">");
        if(parameters == NO_PARAM)
        {
            result = objectContext.performQuery( q);
        }
        else
        {
            Map params = new HashMap();
            //Err.error( "Not known how to do...");
            //q.
            //Assert.isTrue( parameters.length == 1);
            for (int i = 0; i < parameters.length; i++)
            {
                Object paramValue = parameters[i];
                //params.put("aname", "Dali");
                Assert.notNull( paramDeclarations, "Calling a query that has no parameters, as if it has some");
                if(i >= paramDeclarations.length)
                {
                    Err.pr( "Have formally declared less parameters than are passing in");
                    //Print.prArray( parameters, "");
                }
                else
                {
                    params.put( paramDeclarations[i], paramValue);
                    //Err.pr( "Have set <" + paramDeclarations[i] + "> to <" + paramValue + ">");
                }
            }
            SelectQuery query1 = q.queryWithParameters(params);
            result = objectContext.performQuery( query1);
        }
        if(CHECK_RESULT)
        {
            chkResult(result);
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

    public SelectQuery getQuery()
    {
        return q;
    }

//    protected PersistenceManagerFactory getPMF()
//    {
//        return dataContextFactory;
//    }

}