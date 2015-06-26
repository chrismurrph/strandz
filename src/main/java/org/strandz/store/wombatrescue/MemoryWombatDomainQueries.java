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
import org.strandz.data.wombatrescue.objects.RosterSlot;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.util.RosterUtils;
import org.strandz.lgpl.store.DomainQueries;
import org.strandz.lgpl.store.LoopyQuery;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class MemoryWombatDomainQueries extends DomainQueries
{    
    private static class RosterableWorkers extends LoopyQuery
    {
        public RosterableWorkers(Class clazz, String id)
        {
            super(clazz, id);
        }

        public boolean match(Object row)
        {
            boolean result = false;
            Worker worker = (Worker) row;
//            if(worker.getBelongsToGroup() == null)
//            {
//              Err.error( "getBelongsToGroup() not plugged in for " + worker);
//            }
            if(!worker.isDummy() && !worker.isUnknown() && worker.getBelongsToGroup().isDummy())
            {
                result = true;
            }
            return result;
        }

        public int compare(Object one, Object two)
        {
            return RosterUtils.workerCf(one, two);
        }

        public void chkResult(Collection c)
        {
            Utils.chkNoNulls(c, getId());
        }
    }

    public MemoryWombatDomainQueries()
    {
        initQuery(WombatDomainQueryEnum.BUDDY_MANAGERS,
                  new LoopyQuery(
                          POJOWombatData.BUDDY_MANAGER,
                          WombatDomainQueryEnum.BUDDY_MANAGERS.getDescription())
                  {
                  });
        initQuery(WombatDomainQueryEnum.ALL_ROSTER_SLOT,
                  new LoopyQuery(
                          POJOWombatData.ROSTER_SLOT,
                          WombatDomainQueryEnum.ALL_ROSTER_SLOT.getDescription())
                  {
                  });
        initQuery(WombatDomainQueryEnum.UNROSTERABLE_WORKERS,
                  new LoopyQuery(
                          POJOWombatData.WORKER,
                          WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription())
                  {
                      public boolean match(Object row)
                      {
                          boolean result = false;
                          Worker worker = (Worker) row;
                          if(!worker.isDummy() && worker.isUnknown() && worker.getBelongsToGroup().isDummy())
                          {
                              result = true;
                          }
                          return result;
                      }

                      //"surname ascending, christianName ascending, groupName ascending"
                      public int compare(Object one, Object two)
                      {
                          return RosterUtils.workerCf(one, two);
                      }

                      public void chkResult(Collection c)
                      {
                          Utils.chkNoNulls(c, getId());
                      }
                  });
        initQuery(WombatDomainQueryEnum.ROSTERABLE_WORKERS, 
                  new RosterableWorkers( POJOWombatData.WORKER,
                          WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription()));
        initQuery(WombatDomainQueryEnum.ROSTERABLE_WORKERS_EVICT, 
                  new RosterableWorkers( POJOWombatData.WORKER,
                          WombatDomainQueryEnum.ROSTERABLE_WORKERS_EVICT.getDescription()));
        initQuery(WombatDomainQueryEnum.GROUP_WORKERS,
                  new LoopyQuery(
                          POJOWombatData.WORKER,
                          WombatDomainQueryEnum.GROUP_WORKERS.getDescription())
                  {
                      public boolean match(Object row)
                      {
                          boolean result = false;
                          Worker worker = (Worker) row;
                          if(!worker.isDummy() && !worker.getBelongsToGroup().isDummy())
                          {
                              result = true;
                          }
                          return result;
                      }

                      public int compare(Object one, Object two)
                      {
                          return RosterUtils.workerCf(one, two);
                      }

                      public void chkResult(Collection c)
                      {
                          Utils.chkNoNulls(c, getId());
                      }
                  });
        initQuery(WombatDomainQueryEnum.ALL_ROSTER_SLOT_O_BY_WKER,
                  new LoopyQuery(
                          POJOWombatData.ROSTER_SLOT,
                          WombatDomainQueryEnum.ALL_ROSTER_SLOT_O_BY_WKER.getDescription())
                  {
                      public int compare(Object one, Object two)
                      {
                          WorkerI w1 = ((RosterSlot) one).getWorker();
                          WorkerI w2 = ((RosterSlot) two).getWorker();
                          return RosterUtils.workerCf(w1, w2);
                      }
                  });
        RosterableRosterSlotLoopyQuery query = new RosterableRosterSlotLoopyQuery( POJOWombatData.ROSTER_SLOT,
                          WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER.getDescription());
        initQuery(WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER, query);
        query = new RosterableRosterSlotLoopyQuery( POJOWombatData.ROSTER_SLOT,
                          WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER_EVICT.getDescription());
        initQuery(WombatDomainQueryEnum.ROSTERABLE_ROSTER_SLOT_O_BY_WKER_EVICT, query);
        initStableQueries();
        initTaskQueries();
    }
    
    private static class RosterableRosterSlotLoopyQuery extends LoopyQuery
    {
        public RosterableRosterSlotLoopyQuery(Class clazz, String id)
        {
            super(clazz, id);
        }

        public boolean match(Object row)
        {
            boolean result = false;
            RosterSlot rosterSlot = (RosterSlot) row;
            //First: excludes rosterSlots of workers that are now 'unknown'
            //Second: excludes rosterSlots of workers who have since been put into a group
            //If someone wants to be both then we could give them two identities, but the smart
            //thing to do would be to not have them in the group - group members are only used
            //so that we know who they are - they have no rostering connotations.
            if(!rosterSlot.getWorker().isUnknown() && rosterSlot.getWorker().getBelongsToGroup().isDummy())
            {
                result = true;
            }
            return result;
        }

        public int compare(Object one, Object two)
        {
            WorkerI w1 = ((RosterSlot) one).getWorker();
            WorkerI w2 = ((RosterSlot) two).getWorker();
            return RosterUtils.workerCf(w1, w2);
        }
    }

    private void initTaskQueries()
    {
//        initQuery(WombatDomainQueryEnum.WHICH_SHIFT,
//            new LoopyQuery(
//                POJOWombatData.WHICH_SHIFT,
//                WombatDomainQueryEnum.WHICH_SHIFT.getDescription())
//            {
//                public boolean match(Object row, Object params[])
//                {
//                    boolean result = false;
//                    WhichShift whichShift = (WhichShift) row;
//                    if(whichShift.getName().equals(params[0]))
//                    {
//                        result = true;
//                    }
//                    return result;
//                }
//
//                public void formSingleResult(Collection c)
//                {
//                    if(!c.isEmpty())
//                    {
//                        List l = (List) c;
//                        setSingleResult(l.get(0));
//                    }
//                }
//            });
//        initQuery(WombatDomainQueryEnum.DAY_IN_WEEK,
//            new LoopyQuery(
//                POJOWombatData.DAY_IN_WEEK,
//                WombatDomainQueryEnum.DAY_IN_WEEK.getDescription())
//            {
//                public boolean match(Object row, Object params[])
//                {
//                    boolean result = false;
//                    DayInWeek dayInWeek = (DayInWeek) row;
//                    if(dayInWeek.getName().equals(params[0]))
//                    {
//                        result = true;
//                    }
//                    return result;
//                }
//
//                public void formSingleResult(Collection c)
//                {
//                    if(!c.isEmpty())
//                    {
//                        List l = (List) c;
//                        setSingleResult(l.get(0));
//                    }
//                }
//            });
        initQuery(WombatDomainQueryEnum.WORKER_BY_GROUP_NAME,
                  new LoopyQuery(
                          POJOWombatData.WORKER,
                          WombatDomainQueryEnum.WORKER_BY_GROUP_NAME.getDescription())
                  {
                      public boolean match(Object row, Object params[])
                      {
                          boolean result = false;
                          Worker worker = (Worker) row;
                          if(worker.getGroupName().equals(params[0]))
                          {
                              result = true;
                          }
                          return result;
                      }

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
                  new LoopyQuery(
                          POJOWombatData.WORKER,
                          WombatDomainQueryEnum.WORKER_BY_NAMES.getDescription())
                  {
                      public boolean match(Object row, Object params[])
                      {
                          boolean result = false;
                          Worker worker = (Worker) row;
                          if(Utils.equals(worker.getChristianName(), params[0]) && Utils.equals(worker.getSurname(), params[1]))
                          {
                              result = true;
                          }
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
                  });
    }
    
    private void initStableQueries()
    {
        initQuery(WombatDomainQueryEnum.ALL_WORKER,
                  new LoopyQuery(
                          POJOWombatData.WORKER,
                          WombatDomainQueryEnum.ALL_WORKER.getDescription())
                  {
                      public boolean match(Object row)
                      {
                          boolean result = false;
                          Worker worker = (Worker) row;
                          if(!worker.isDummy())
                          {
                              result = true;
                          }
                          return result;
                      }

                      public int compare(Object one, Object two)
                      {
                          return RosterUtils.workerCf(one, two);
                      }
                  });
        initQuery(WombatDomainQueryEnum.WORKER_GROUPS,
                  new LoopyQuery(
                          POJOWombatData.WORKER,
                          WombatDomainQueryEnum.WORKER_GROUPS.getDescription())
                  {
                      public boolean match(Object row)
                      {
                          boolean result = false;
                          Worker worker = (Worker) row;
                          if(worker.getGroupName() != null)
                          {
                              result = true;
                          }
                          return result;
                      }

                      public int compare(Object one, Object two)
                      {
                          return RosterUtils.workerCf(one, two);
                      }
                  });
        initQuery(WombatDomainQueryEnum.NULL_WORKER,
                  new LoopyQuery(
                          POJOWombatData.WORKER,
                          WombatDomainQueryEnum.NULL_WORKER.getDescription())
                  {
                      public boolean match(Object row)
                      {
                          boolean result = false;
                          Worker worker = (Worker) row;
                          if(worker.isDummy())
                          {
                              result = true;
                          }
                          return result;
                      }

                      public void formSingleResult(Collection c)
                      {
                          Object vols[] = c.toArray();
                          setSingleResult(vols[0]);
                      }

                      public void chkResult(Collection c)
                      {
                          if(c.size() != 1)
                          {
                              Err.error("Database does not have a NULL Worker, has " + c.size() + " workers returned for this query");
                          }
                      }
                  });
//        initQuery(WombatDomainQueryEnum.ALL_DAY_IN_WEEK,
//            new LoopyQuery(
//                POJOWombatData.DAY_IN_WEEK,
//                WombatDomainQueryEnum.ALL_DAY_IN_WEEK.getDescription())
//            {
//            });

        initQuery(WombatDomainQueryEnum.LOOKUPS,
                  new LoopyQuery(
                          POJOWombatData.LOOKUPS,
                          WombatDomainQueryEnum.LOOKUPS.getDescription())
                  {
                      public void formSingleResult(Collection c)
                      {
                          if(!c.isEmpty())
                          {
                              List l = (List) c;
                              //Err.pr( "Will set single result to " + l.get(0));
                              setSingleResult(l.get(0));
                          }
                          else
                          {
                              Err.error("No Lookups instance found");
                          }
                          if(c.size() > 1)
                          {
                              Err.error("Only one Lookups instance expected, instead found " + c.size());
                          }
                      }
                  });

//        initQuery(WombatDomainQueryEnum.ALL_WHICH_SHIFT,
//            new LoopyQuery(
//                POJOWombatData.WHICH_SHIFT,
//                WombatDomainQueryEnum.ALL_WHICH_SHIFT.getDescription())
//            {
//            });
        /*
         * Most larger applications do not need these queries as they go thru LOOKUPS.
         * However HelloWorkerSdzExample0/1 are showing the simplest way for ease of
         * understanding.
         */
        initQuery(WombatDomainQueryEnum.FLEXIBILITY,
            new LoopyQuery(
                POJOWombatData.FLEXIBILITY,
                WombatDomainQueryEnum.FLEXIBILITY.getDescription())
            {
            });
//        initQuery(WombatDomainQueryEnum.ALL_SENIORITY,
//            new LoopyQuery(
//                POJOWombatData.SENIORITY,
//                WombatDomainQueryEnum.ALL_SENIORITY.getDescription())
//            {
//            });
//        initQuery(WombatDomainQueryEnum.ALL_SEX,
//            new LoopyQuery(
//                POJOWombatData.SEX,
//                WombatDomainQueryEnum.ALL_SEX.getDescription())
//            {
//            });
//        initQuery(WombatDomainQueryEnum.ALL_WEEK_IN_MONTH,
//            new LoopyQuery(
//                POJOWombatData.WEEK_IN_MONTH,
//                WombatDomainQueryEnum.ALL_WEEK_IN_MONTH.getDescription())
//            {
//            });
//        initQuery(WombatDomainQueryEnum.ALL_INTERVAL,
//            new LoopyQuery(
//                POJOWombatData.INTERVAL,
//                WombatDomainQueryEnum.ALL_INTERVAL.getDescription())
//            {
//            });
//        initQuery(WombatDomainQueryEnum.ALL_OVERRIDE,
//            new LoopyQuery(
//                POJOWombatData.OVERRIDE,
//                WombatDomainQueryEnum.ALL_OVERRIDE.getDescription())
//            {
//            });
//        initQuery(WombatDomainQueryEnum.ALL_MONTH_IN_YEAR,
//            new LoopyQuery(
//                POJOWombatData.MONTH_IN_YEAR,
//                WombatDomainQueryEnum.ALL_MONTH_IN_YEAR.getDescription())
//            {
//            });
    }

}
