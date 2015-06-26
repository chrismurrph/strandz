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
package org.strandz.data.wombatrescue.domain;

import org.strandz.lgpl.store.DomainQueryEnum;

public class WombatDomainQueryEnum extends DomainQueryEnum
{
    //Designer generated code will refer to DEFAULT_QUERY
    public static final WombatDomainQueryEnum DEFAULT_QUERY = null;
    
    WombatDomainQueryEnum(String name, String description)
    {
        super(name, description);
    }

    /*
    public static WombatDomainQueryEnum getFromName( String name)
    {
      return (WombatDomainQueryEnum)pUtils.getByStringFromArray( QUERIES, name );
    }
    */

    public static final WombatDomainQueryEnum VOLUNTEER_BY_NAMES =
        new WombatDomainQueryEnum("VOLUNTEER_BY_NAMES", "Volunteer by christianName and surname");
    public static final WombatDomainQueryEnum WORKER_BY_NAMES =
        new WombatDomainQueryEnum("WORKER_BY_NAMES", "Worker by christianName and surname");
    public static final WombatDomainQueryEnum WORKER_BY_GROUP_NAME =
        new WombatDomainQueryEnum("WORKER_BY_GROUP_NAME", "Worker by groupName");
//    public static final WombatDomainQueryEnum DAY_IN_WEEK =
//        new WombatDomainQueryEnum("DAY_IN_WEEK", "Day in the week");
//    public static final WombatDomainQueryEnum WHICH_SHIFT =
//        new WombatDomainQueryEnum("WHICH_SHIFT", "Which Shift");
    public static final WombatDomainQueryEnum ALL_VOLUNTEER =
        new WombatDomainQueryEnum("ALL_VOLUNTEER", "All Volunteers");
    public static final WombatDomainQueryEnum ALL_WORKER =
        new WombatDomainQueryEnum("ALL_WORKER", "All Workers");

    //public static final WombatDomainQueryEnum ALL_ROSTER_SLOT_O_BY_VOL =
    //    new WombatDomainQueryEnum("ALL_ROSTER_SLOT_O_BY_VOL", "All Roster Slot ordered by Volunteer");
    public static final WombatDomainQueryEnum ALL_ROSTER_SLOT_O_BY_WKER =
        new WombatDomainQueryEnum("ALL_ROSTER_SLOT_O_BY_WKER", "All Roster Slot ordered by Worker");
    public static final WombatDomainQueryEnum ROSTERABLE_ROSTER_SLOT_O_BY_WKER =
        new WombatDomainQueryEnum("ALL_ROSTER_SLOT_O_BY_WKER", "Rosterable Roster Slots ordered by Worker");
    public static final WombatDomainQueryEnum ROSTERABLE_ROSTER_SLOT_O_BY_WKER_EVICT =
        new WombatDomainQueryEnum("ALL_ROSTER_SLOT_O_BY_WKER_EVICT", "Rosterable Roster Slots ordered by Worker (Evict)");
    public static final WombatDomainQueryEnum ALL_ROSTER_SLOT =
        new WombatDomainQueryEnum("ALL_ROSTER_SLOT", "All Roster Slot");

    public static final WombatDomainQueryEnum ROSTERABLE_WORKERS =
        new WombatDomainQueryEnum("ROSTERABLE_WORKERS", "Rosterable Workers");
    public static final WombatDomainQueryEnum ROSTERABLE_WORKERS_EVICT =
        new WombatDomainQueryEnum("ROSTERABLE_WORKERS_EVICT", "Rosterable Workers (Evict)");
    public static final WombatDomainQueryEnum GROUP_WORKERS =
        new WombatDomainQueryEnum("GROUP_WORKERS", "Group Workers");
    public static final WombatDomainQueryEnum WORKER_GROUPS =
        new WombatDomainQueryEnum("WORKER_GROUPS", "The umbrella groups");
    public static final WombatDomainQueryEnum UNROSTERABLE_WORKERS =
        new WombatDomainQueryEnum("UNROSTERABLE_WORKERS", "Unrosterable Workers");
    public static final WombatDomainQueryEnum BUDDY_MANAGERS =
        new WombatDomainQueryEnum("BUDDY_MANAGERS", "Buddy Managers");
    public static final WombatDomainQueryEnum NULL_WORKER =
        new WombatDomainQueryEnum("NULL_WORKER", "Null Worker");
    public static final WombatDomainQueryEnum MULTIPLE_NULL_WORKERS =
        new WombatDomainQueryEnum("NULL_WORKER", "Multiple Null Workers");
    public static final WombatDomainQueryEnum ALL_USER =
        new WombatDomainQueryEnum("ALL_USER", "All Users");    
    public static final WombatDomainQueryEnum LOOKUPS =
        new WombatDomainQueryEnum("LOOKUPS", "Rosterer Lookups");
    //Only used by Cayenne:
    public static final WombatDomainQueryEnum DAY_IN_WEEK =
        new WombatDomainQueryEnum("DAY_IN_WEEK", "Day in Week LOVs");
    public static final WombatDomainQueryEnum FLEXIBILITY =
        new WombatDomainQueryEnum("FLEXIBILITY", "Flexibility LOVs");
    public static final WombatDomainQueryEnum MONTH_IN_YEAR =
        new WombatDomainQueryEnum("MONTH_IN_YEAR", "Month in Year LOVs");
    public static final WombatDomainQueryEnum NUM_DAYS_INTERVAL =
        new WombatDomainQueryEnum("NUM_DAYS_INTERVAL", "Num Days Interval LOVs");
    public static final WombatDomainQueryEnum OVERRIDE =
        new WombatDomainQueryEnum("OVERRIDE", "Override LOVs");
    public static final WombatDomainQueryEnum SENIORITY =
        new WombatDomainQueryEnum("SENIORITY", "Seniority LOVs");
    public static final WombatDomainQueryEnum SEX =
        new WombatDomainQueryEnum("SEX", "Sex LOVs");
    public static final WombatDomainQueryEnum WEEK_IN_MONTH =
        new WombatDomainQueryEnum("WEEK_IN_MONTH", "Week in Month LOVs");
    public static final WombatDomainQueryEnum WHICH_SHIFT =
        new WombatDomainQueryEnum("WHICH_SHIFT", "Which Shift LOVs");

    public static final WombatDomainQueryEnum CLASS_QUERY =
        new WombatDomainQueryEnum("CLASS_QUERY", "Class Query");

    /*
    public static final WombatDomainQueryEnum[] QUERIES = {
      VOLUNTEER_BY_NAMES, WORKER_BY_NAMES, WORKER_BY_GROUP_NAME, DAY_IN_WEEK, WHICH_SHIFT,
      ALL_VOLUNTEER, ALL_WORKER, ALL_ROSTER_SLOT_O_BY_VOL, ALL_ROSTER_SLOT_O_BY_WKER,
      ROSTERABLE_WORKERS, GROUP_WORKERS, WORKER_GROUPS, UNROSTERABLE_WORKERS, BUDDY_MANAGERS,
      NULL_WORKER,
    };
    */
}
