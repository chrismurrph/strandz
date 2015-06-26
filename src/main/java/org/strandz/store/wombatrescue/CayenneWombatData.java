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

abstract public class CayenneWombatData
{
    //Not a database table in Cayenne
    //static final Class LOOKUPS = WombatLookups.class;

    abstract public Class[] getClasses();

    abstract public Class getWorkerClass();
    abstract public Class getBuddyManagerClass();
    abstract public Class getRosterSlotClass();

    /* Not a database table in Cayenne
    public Class getLooksupsClass()
    {
        return LOOKUPS;
    }
    */
    abstract public Class getUserDetailsClass();
    abstract public Class getDayInWeekClass();
    abstract public Class getIntervalClass();
    abstract public Class getWeekInMonthClass();
    abstract public Class getWhichShiftClass();
    abstract public Class getMonthInYearClass();
    abstract public Class getSeniorityClass();
    abstract public Class getSexClass();
    abstract public Class getFlexibilityClass();
    abstract public Class getOverrideClass();

    private static CayenneWombatData SERVER_INSTANCE = new CayenneServerWombatData();
    private static CayenneWombatData CLIENT_INSTANCE = new CayenneClientWombatData();

    static CayenneWombatData getServerInstance()
    {
        return SERVER_INSTANCE;
    }
    static CayenneWombatData getClientInstance()
    {
        return CLIENT_INSTANCE;
    }
    public static CayenneWombatData getInstance( boolean client)
    {
        CayenneWombatData result = SERVER_INSTANCE;
        if(client)
        {
            result = CLIENT_INSTANCE;
        }
        return result;
    }
}