/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.store.wombatrescue;

import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.lgpl.persist.JDOVendorOpsFactory;
import org.strandz.lgpl.store.DomainQueries;
import org.strandz.lgpl.store.JDOInterpretedQuery;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NoTaskTimeBandMonitorI;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.MessageDlg;

import javax.swing.JOptionPane;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class JDOWombatDomainQueries extends DomainQueries
{
    //Can't order by a calculated field even thou would want to, see RosterUtils.workerCf()
    //private static final String WORKER_ORDERING = "orderBy ascending";
    private static final String WORKER_ORDERING = null;
    private static final String ROSTERABLE_PREDICATE =
        "dummy != true && unknown == false && belongsToGroup.dummy == true";
    private static final String SLOTS_ROSTERABLE_PREDICATE =
        "worker.dummy != true && worker.unknown == false && worker.belongsToGroup.dummy == true";
//    private static boolean USE_COPY_TRICK = true;
//    
//    protected boolean isPerformCopyTrick()
//    {
//        return USE_COPY_TRICK;    
//    }

    /**
     * Comment out queries that are not needed by application. They can
     * be done by calling a seperate method as get errors.
     */
    public JDOWombatDomainQueries()
    {
        initQuery(WombatDomainQueryEnum.BUDDY_MANAGERS,
            new JDOInterpretedQuery(
                POJOWombatData.BUDDY_MANAGER,
                WombatDomainQueryEnum.BUDDY_MANAGERS.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.ALL_ROSTER_SLOT,
            new JDOInterpretedQuery(
                POJOWombatData.ROSTER_SLOT,
                WombatDomainQueryEnum.ALL_ROSTER_SLOT.getDescription(),
                null,
                null,
                null,
                //have not tested whether need 2nd last var to be true, was only true for LOV
                //queries because values were substituted. Found from unit testing that
                //needs to be true if are modifying the list - for example when inserting
                //a roster slot. Thus false would only ever be for reports. Interesting
                //conclusion as most of the other queries have false. This means that there
                //are errors lurking. For example when modify a Buddy Manager expect to get
                //a modification exception. (BMs is a bad example because will never be
                // adding to or removing from that list, so will never get the exception).
                loggingMonitor, true, true)
            {
            });
        initQuery(WombatDomainQueryEnum.UNROSTERABLE_WORKERS,
            new JDOInterpretedQuery(
                POJOWombatData.WORKER,
                WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription(),
                "dummy != true && unknown == true && belongsToGroup.dummy == true",
                WORKER_ORDERING,
                null,
                loggingMonitor, 1391)
            {
                public void chkResult(Collection c)
                {
                    Utils.chkNoNulls(c, getId());
                }
                public void postExecute(Collection c)
                {
                    //Err.pr( "To achieve " + WORKER_ORDERING);
                    //Collections.sort( (List)c, SEARCH_BY);
                }
            });
        initQuery(WombatDomainQueryEnum.ROSTERABLE_WORKERS,
                  new RosterableWorkersJDOInterpretedQuery());
        initQuery(WombatDomainQueryEnum.ROSTERABLE_WORKERS_EVICT,
                  new EvictRosterableWorkersJDOInterpretedQuery());
        initQuery(WombatDomainQueryEnum.GROUP_WORKERS,
            new JDOInterpretedQuery(
                POJOWombatData.WORKER,
                WombatDomainQueryEnum.GROUP_WORKERS.getDescription(),
                "dummy != true && belongsToGroup.dummy == false",
                WORKER_ORDERING,
                null,
                loggingMonitor)
            {
                public void chkResult(Collection c)
                {
                    Utils.chkNoNulls(c, getId());
                }                
                public void postExecute(Collection c)
                {
                    //Err.pr( "To achieve " + WORKER_ORDERING);
                    //Collections.sort( (List)c, SEARCH_BY);
                }
            });
        initQuery(WombatDomainQueryEnum.ALL_ROSTER_SLOT_O_BY_WKER,
            new JDOInterpretedQuery(
                POJOWombatData.ROSTER_SLOT,
                WombatDomainQueryEnum.ALL_ROSTER_SLOT_O_BY_WKER.getDescription(),
                null,
                "worker.surname ascending, worker.christianName ascending, worker.groupName ascending",
                null,
                loggingMonitor, 1187)
            {
            });
        initQuery(WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER,
                  new RosterableRosterSlotJDOInterpretedQuery());
        initQuery(WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER_EVICT,
                  new EvictRosterableRosterSlotJDOInterpretedQuery());
        initQuery(WombatDomainQueryEnum.ALL_USER,
                  new JDOInterpretedQuery(
                          POJOWombatData.USER,
                          WombatDomainQueryEnum.ALL_USER.getDescription(),
                          null,
                          "username ascending",
                          null,
                          loggingMonitor,
                          false,
                          false)
                  {
                      public void chkResult(Collection c)
                      {
                          Utils.chkNoNulls(c, getId());
                      }
                  });
        initStableQueries();
        //Always doing these will probably do us little harm performance wise, especially
        //as we make sure that we don't compile these queries:
        initTaskQueries(loggingMonitor);
        initClassQuery(loggingMonitor);
    }

    private void initClassQuery(NoTaskTimeBandMonitorI monitor)
    {
        initQuery(WombatDomainQueryEnum.CLASS_QUERY,
            new JDOInterpretedQuery(null, //Will only know the class when the query is executed
                WombatDomainQueryEnum.CLASS_QUERY.getDescription(),
                null,
                null,
                null,
                null,
                false, false)
            {
            });
    }

    private void initTaskQueries(NoTaskTimeBandMonitorI monitor)
    {
        initQuery(WombatDomainQueryEnum.WORKER_BY_GROUP_NAME,
            new JDOInterpretedQuery(
                POJOWombatData.WORKER,
                WombatDomainQueryEnum.WORKER_BY_GROUP_NAME.getDescription(),
                "groupName == groupNameParam",
                WORKER_ORDERING,
                "String groupNameParam",
                monitor,
                false, false)
            {
                public void formSingleResult(Collection c)
                {
                    if(!c.isEmpty())
                    {
                        List l = (List) c;
                        //Err.pr("Will set single result to " + l.get(0));
                        setSingleResult(l.get(0));
                    }
                    else
                    {
                        Err.pr("No matching group found");
                    }
                }
            });
        initQuery(WombatDomainQueryEnum.WORKER_BY_NAMES,
            new JDOInterpretedQuery(
                POJOWombatData.WORKER,
                WombatDomainQueryEnum.WORKER_BY_NAMES.getDescription(),
                "christianName == christianNameParam && surname == surnameParam",
                WORKER_ORDERING,
                "String christianNameParam, String surnameParam",
                monitor,
                false, false)
            {
                public void formSingleResult(Collection c)
                {
                    if(!c.isEmpty())
                    {
                        List l = (List) c;
                        setSingleResult(l.get(0));
                    }
                }
            });
    }

    private void initStableQueries()
    {
        initQuery(WombatDomainQueryEnum.ALL_WORKER,
            new JDOInterpretedQuery(
                POJOWombatData.WORKER,
                WombatDomainQueryEnum.ALL_WORKER.getDescription(),
                "dummy != true",
                WORKER_ORDERING,
                null,
                loggingMonitor, 73704)
            {
                public void postExecute(Collection c)
                {
                    //Err.pr( "To achieve " + WORKER_ORDERING);
                    //Collections.sort( (List)c, SEARCH_BY);
                }
            });
        initQuery(WombatDomainQueryEnum.WORKER_GROUPS,
            new JDOInterpretedQuery(
                POJOWombatData.WORKER,
                WombatDomainQueryEnum.WORKER_GROUPS.getDescription(),
                "groupName != null",
                WORKER_ORDERING,
                null,
                loggingMonitor, 3093)
            {
                public void postExecute(Collection c)
                {
                    //Err.pr( "To achieve " + WORKER_ORDERING);
                    getQuery().getPersistenceManager().retrieveAll(c);
                    //Collections.sort( (List)c, SEARCH_BY);
                }
            });
        initQuery(WombatDomainQueryEnum.NULL_WORKER,
            new JDOInterpretedQuery(
                POJOWombatData.WORKER,
                WombatDomainQueryEnum.NULL_WORKER.getDescription(),
                "dummy == true",
                null,
                null,
                loggingMonitor)
            {
                public void formSingleResult(Collection c)
                {
                    if(!c.isEmpty())
                    {
                        Object vols[] = c.toArray();
                        //Err.pr( "Class of what returned is " + vols[0].getClass() );
                        setSingleResult(vols[0]);
                    }
                }

                public void chkResult(Collection c)
                {
                    if(c.size() == 0)
                    {
                        Err.error( "No null worker in database");
                    }
                    else if(c.size() != 1)
                    {
                        Err.pr( "No longer fixing corrupted DB as no longer using Kodo");
                        //fixCorruptedDB( c);
                        String msgs[] = new String[3];
                        msgs[0] = "Database does not have one NULL Worker, has " + c.size();
                        msgs[1] = "The problem has been fixed";
                        msgs[2] = "You should restart the application";
                        new MessageDlg( msgs, JOptionPane.ERROR_MESSAGE);
                        Err.error( msgs);
                    }
                }
            });
        initQuery(WombatDomainQueryEnum.MULTIPLE_NULL_WORKERS,
            new JDOInterpretedQuery(
                POJOWombatData.WORKER,
                WombatDomainQueryEnum.MULTIPLE_NULL_WORKERS.getDescription(),
                "dummy == true",
                null,
                null,
                loggingMonitor)
            {
            });
        JDOInterpretedQuery query1 = new JDOInterpretedQuery(
                POJOWombatData.LOOKUPS,
                WombatDomainQueryEnum.LOOKUPS.getDescription(),
                null,
                null,
                null,
                loggingMonitor,
                false, false, 12359)
        {
            public void chkResult(Collection c)
            {
                /* Good way of finding out if what is returned is persistent clean or hollow
                *
               WombatLookups wombatLookups = (WombatLookups)((List)c).get(0);
               //If do this we force them to show up - but we want them to
               //show up from the time of the query
               //wombatLookups.get( WombatDomainLookupEnum.ALL_SEX);
               Query q = getQuery();
               PersistenceManager pm = q.getPersistenceManager();
               pm.makeTransient( wombatLookups);
               List sexes = wombatLookups.get( WombatDomainLookupEnum.ALL_SEX);
               Print.prList( sexes, "Presumably not hollow, so these will show", false);
                */
            }

            /**
             * This hardly ideal as will involve another trip to the DB, but
             * will ensure that the whole fetch group is fetched ie. the
             * objective is to do a get on a persistent field.
             */
            public void postExecute(Collection c)
            {
                if(!c.isEmpty())
                {
                }
            }

            public void formSingleResult(Collection c)
            {
                if(!c.isEmpty())
                {
                    List l = (List) c;
                    setSingleResult(l.get(0));
                }
            }
        };
        //Keeping this line, just so we know it's here
        //JDOFetchGroupQuery query2 = new JDOFetchGroupQuery(query1, "lookups");
        initQuery(WombatDomainQueryEnum.LOOKUPS, query1);
    }

    private class RosterableWorkersJDOInterpretedQuery extends JDOInterpretedQuery
    {
        public RosterableWorkersJDOInterpretedQuery()
        {
            super(POJOWombatData.WORKER, WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription(),
                  ROSTERABLE_PREDICATE, 
                  WORKER_ORDERING, null, JDOWombatDomainQueries.this.loggingMonitor, 54484);
        }

        public void chkResult(Collection c)
        {
            Utils.chkNoNulls(c, getId());
            for(Object o : c)
            {
                Worker worker = (Worker)o;
                if(!worker.isDummy())
                {
                    if(worker.getChristianName() == null && worker.getSurname() == null && 
                            worker.getGroupName() == null)
                    {
                        Err.error( "Bad rosterable worker fetched: <" + worker + ">");        
                    }
                }
            }
        }
        
        public void postExecute(Collection c)
        {
            //Err.pr( "To achieve " + WORKER_ORDERING);
            //Collections.sort( (List)c, SEARCH_BY);
        }
    }
    
    private class EvictRosterableWorkersJDOInterpretedQuery extends RosterableWorkersJDOInterpretedQuery 
    {
        public void preExecute()
        {
            JDOVendorOpsFactory.newVendorOps( JDOVendorOpsFactory.KODO).evict( getQuery(), getPMF());
        }
    }

    private class RosterableRosterSlotJDOInterpretedQuery extends JDOInterpretedQuery
    {
        public RosterableRosterSlotJDOInterpretedQuery()
        {
            super(POJOWombatData.ROSTER_SLOT, WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER.getDescription(),
                  SLOTS_ROSTERABLE_PREDICATE, "worker.surname ascending, worker.christianName ascending, worker.groupName ascending", 
                  null, JDOWombatDomainQueries.this.loggingMonitor, 1187);
        }
    }
    
    private class EvictRosterableRosterSlotJDOInterpretedQuery extends RosterableRosterSlotJDOInterpretedQuery 
    {
        public void preExecute()
        {
            JDOVendorOpsFactory.newVendorOps( JDOVendorOpsFactory.KODO).evict( getQuery(), getPMF());
        }
    }    
}
