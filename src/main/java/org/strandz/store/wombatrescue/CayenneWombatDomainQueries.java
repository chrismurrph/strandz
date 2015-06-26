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
import org.strandz.data.wombatrescue.calculated.CayenneWombatLookups;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.objects.cayenne.auto._Worker;
import org.strandz.data.wombatrescue.objects.cayenne.auto._RosterSlot;
import org.strandz.lgpl.store.DomainQueries;
import org.strandz.lgpl.store.CayenneInterpretedQuery;
import org.strandz.lgpl.store.MinimumDomainQuery;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NoTaskTimeBandMonitorI;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.MessageDlg;
import org.apache.cayenne.query.Ordering;

import javax.swing.JOptionPane;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 *
 */
public class CayenneWombatDomainQueries extends DomainQueries
{
    private List<String> workerPrefetches = new ArrayList<String>();

    private static final String ROSTERABLE_PREDICATE = "dummy != 1 and unknown = 0 and actualBelongsToGroup.dummy = 1";
    private static final String SLOTS_ROSTERABLE_PREDICATE = "worker.dummy != 1 and worker.unknown = 0 and worker.actualBelongsToGroup.dummy = 1";

    private void configurePrefetches()
    {
        workerPrefetches.add( _Worker.ACTUAL_BELONGS_TO_GROUP_PROPERTY);
        workerPrefetches.add( _Worker.ACTUAL_SENIORITY_PROPERTY);
        workerPrefetches.add( _Worker.ACTUAL_SEX_PROPERTY);
        workerPrefetches.add( _Worker.ACTUAL_SHIFT_PREFERENCE_PROPERTY);
        workerPrefetches.add( _Worker.ACTUAL_FLEXIBILITY_PROPERTY);
        workerPrefetches.add( _Worker.ROSTER_SLOTS_PROPERTY);
        workerPrefetches.add( _Worker.ROSTER_SLOTS_PROPERTY + "." + _RosterSlot.ACTUAL_NUM_DAYS_INTERVAL_PROPERTY);
        workerPrefetches.add( _Worker.ROSTER_SLOTS_PROPERTY + "." + _RosterSlot.ACTUAL_NOT_IN_MONTH_PROPERTY);
        workerPrefetches.add( _Worker.ROSTER_SLOTS_PROPERTY + "." + _RosterSlot.ACTUAL_ONLY_IN_MONTH_PROPERTY);
        workerPrefetches.add( _Worker.ROSTER_SLOTS_PROPERTY + "." + _RosterSlot.ACTUAL_OVERRIDES_OTHERS_PROPERTY);
    }

    public static class RelayLookupsQuery extends MinimumDomainQuery
    {
        private Object singleResult;
        private CayenneWombatLookups lookupsCayenne;

        RelayLookupsQuery( CayenneWombatDomainQueries cayenneWombatDomainQueries)
        {
            lookupsCayenne = new CayenneWombatLookups( cayenneWombatDomainQueries);
        }

        public Collection execute()
        {
            Collection result = Utils.formCollection(lookupsCayenne);
            formSingleResult( result);
            return result;
        }

        public void formSingleResult(Collection c)
        {
            if(!c.isEmpty())
            {
                List l = (List) c;
                setSingleResult(l.get(0));
            }
        }

        public Object getSingleResult()
        {
            Object result = singleResult;
            return result;
        }

        public void setSingleResult(Object singleResult)
        {
            this.singleResult = singleResult;
        }
    }

    /**
     * Comment out queries that are not needed by application. They can
     * be done by calling a seperate method as get errors.
     */
    public CayenneWombatDomainQueries( boolean client)
    {
        configurePrefetches();
        initQuery(WombatDomainQueryEnum.BUDDY_MANAGERS,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getBuddyManagerClass(),
                WombatDomainQueryEnum.BUDDY_MANAGERS.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.ALL_ROSTER_SLOT,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getRosterSlotClass(),
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
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWorkerClass(),
                WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription(),
                "dummy != 1 and unknown = 1 and actualBelongsToGroup.dummy = 1",
                null,
                null,
                loggingMonitor, 1391, workerPrefetches)
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
                  new RosterableWorkersCayenneInterpretedQuery( client));
        initQuery(WombatDomainQueryEnum.ROSTERABLE_WORKERS_EVICT,
                  new RosterableWorkersCayenneInterpretedQuery( client));
        initQuery(WombatDomainQueryEnum.GROUP_WORKERS,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWorkerClass(),
                WombatDomainQueryEnum.GROUP_WORKERS.getDescription(),
                "dummy != 1 and actualBelongsToGroup.dummy = 0",
                null,
                null,
                loggingMonitor, workerPrefetches)
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
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getRosterSlotClass(),
                WombatDomainQueryEnum.ALL_ROSTER_SLOT_O_BY_WKER.getDescription(),
                null,
                getWorkerOrdering(),
                null,
                loggingMonitor, 1187)
            {
            });
        initQuery(WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER,
                  new RosterableRosterSlotCayenneInterpretedQuery( client));
        initQuery(WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER_EVICT,
                  new RosterableRosterSlotCayenneInterpretedQuery( client));
        //initQuery(WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER_EVICT,
        //          new RosterableRosterSlotCayenneInterpretedQuery());
        List<Ordering> orderings = new ArrayList<Ordering>();
        orderings.add( new Ordering( "username", true));
        initQuery(WombatDomainQueryEnum.ALL_USER,
                  new CayenneInterpretedQuery(
                          CayenneWombatData.getInstance( client).getUserDetailsClass(),
                          WombatDomainQueryEnum.ALL_USER.getDescription(),
                          null,
                          orderings,
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
        initStableQueries( client);
        //Always doing these will probably do us little harm performance wise, especially
        //as we make sure that we don't compile these queries:
        initTaskQueries(loggingMonitor, client);
        initClassQuery(loggingMonitor);
        initQuery(WombatDomainQueryEnum.DAY_IN_WEEK,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getDayInWeekClass(),
                WombatDomainQueryEnum.DAY_IN_WEEK.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.FLEXIBILITY,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getFlexibilityClass(),
                WombatDomainQueryEnum.FLEXIBILITY.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.MONTH_IN_YEAR,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getMonthInYearClass(),
                WombatDomainQueryEnum.MONTH_IN_YEAR.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.NUM_DAYS_INTERVAL,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getIntervalClass(),
                WombatDomainQueryEnum.NUM_DAYS_INTERVAL.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.OVERRIDE,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getOverrideClass(),
                WombatDomainQueryEnum.OVERRIDE.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.SENIORITY,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getSeniorityClass(),
                WombatDomainQueryEnum.SENIORITY.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.SEX,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getSexClass(),
                WombatDomainQueryEnum.SEX.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.WEEK_IN_MONTH,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWeekInMonthClass(),
                WombatDomainQueryEnum.WEEK_IN_MONTH.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
        initQuery(WombatDomainQueryEnum.WHICH_SHIFT,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWhichShiftClass(),
                WombatDomainQueryEnum.WHICH_SHIFT.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 3094)
            {
            });
    }

    private void initClassQuery(NoTaskTimeBandMonitorI monitor)
    {
        initQuery(WombatDomainQueryEnum.CLASS_QUERY,
            new CayenneInterpretedQuery(null, //Will only know the class when the query is executed
                WombatDomainQueryEnum.CLASS_QUERY.getDescription(),
                null,
                null,
                null,
                null,
                false, false)
            {
            });
    }

    private void initTaskQueries(NoTaskTimeBandMonitorI monitor, boolean client)
    {
        initQuery(WombatDomainQueryEnum.WORKER_BY_GROUP_NAME,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWorkerClass(),
                WombatDomainQueryEnum.WORKER_BY_GROUP_NAME.getDescription(),
                "groupName = $groupNameParam",
                null,
                new String[]{"groupNameParam"},
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
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWorkerClass(),
                WombatDomainQueryEnum.WORKER_BY_NAMES.getDescription(),
                "christianName = $christianNameParam and surname = $surnameParam",
                null,
                new String[]{"christianNameParam", "surnameParam"},
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

    private void initStableQueries( boolean client)
    {
        initQuery(WombatDomainQueryEnum.ALL_WORKER,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWorkerClass(),
                WombatDomainQueryEnum.ALL_WORKER.getDescription(),
                "dummy != 1",
                null,
                null,
                loggingMonitor, 73704, workerPrefetches)
            {
            });
        initQuery(WombatDomainQueryEnum.WORKER_GROUPS,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWorkerClass(),
                WombatDomainQueryEnum.WORKER_GROUPS.getDescription(),
                "groupName != null",
                null,
                null,
                loggingMonitor, 3093, workerPrefetches)
            {
            });
        initQuery(WombatDomainQueryEnum.NULL_WORKER,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWorkerClass(),
                WombatDomainQueryEnum.NULL_WORKER.getDescription(),
                "dummy = 1",
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
                        //Does not work for JPOX, so when running TestCayenneInsert we temporarily comment this
                        //(Only theory - never bothered to run TestCayenneInsert)
                        //fixCorruptedDB( c);
                        String msgs[] = new String[1];
                        msgs[0] = "Database does not have one NULL Worker, has " + c.size();
                        new MessageDlg( msgs, JOptionPane.ERROR_MESSAGE);
                        Err.error( msgs);
                    }
                }
            });
        initQuery(WombatDomainQueryEnum.MULTIPLE_NULL_WORKERS,
            new CayenneInterpretedQuery(
                CayenneWombatData.getInstance( client).getWorkerClass(),
                WombatDomainQueryEnum.MULTIPLE_NULL_WORKERS.getDescription(),
                "dummy = 1",
                null,
                null,
                loggingMonitor)
            {
            });
        RelayLookupsQuery query1 = new RelayLookupsQuery( this);
        initQuery(WombatDomainQueryEnum.LOOKUPS, query1);
    }

    private class RosterableWorkersCayenneInterpretedQuery extends CayenneInterpretedQuery
    {

        public RosterableWorkersCayenneInterpretedQuery( boolean client)
        {
            super(CayenneWombatData.getInstance( client).getWorkerClass(),
                WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription(),
                  ROSTERABLE_PREDICATE,
                  null, null, CayenneWombatDomainQueries.this.loggingMonitor,
                54484, workerPrefetches);
        }

        public void chkResult(Collection c)
        {
            Utils.chkNoNulls(c, getId());
            for(Object o : c)
            {
                WorkerI worker = (WorkerI)o;
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
            /*
            for (Iterator iterator = c.iterator(); iterator.hasNext();)
            {
                Worker worker = (Worker) iterator.next();
                if(!worker.getBelongsToGroup().isDummy())
                {
                    //Exclude those who belong to a group
                    iterator.remove();
                    c.remove( worker);
                }
            }
            */
        }
    }

    private static List<Ordering> getWorkerOrdering()
    {
        List<Ordering> result = new ArrayList<Ordering>() {};
        result.add( new Ordering( "worker.surname", true));
        result.add( new Ordering( "worker.christianName", true));
        result.add( new Ordering( "worker.groupName", true));
        return result;
    }

    private class RosterableRosterSlotCayenneInterpretedQuery extends CayenneInterpretedQuery
    {

        public RosterableRosterSlotCayenneInterpretedQuery( boolean client)
        {
            super(CayenneWombatData.getInstance( client).getRosterSlotClass(), WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER.getDescription(),
                  SLOTS_ROSTERABLE_PREDICATE,
                  getWorkerOrdering(),
                  null, CayenneWombatDomainQueries.this.loggingMonitor, 1187);
        }
    }
}