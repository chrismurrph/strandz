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
package org.strandz.data.wombatrescue.business;

import org.strandz.data.wombatrescue.objects.WhichShiftI;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.data.wombatrescue.calculated.ParticularShift;
import org.strandz.data.wombatrescue.calculated.MonthTransferObj;
import org.strandz.data.wombatrescue.calculated.RosterTransferObj;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.data.objects.DayInWeekI;

import java.util.Date;
import java.util.List;

/**
 * Client facing, all the things that the client can do on a particular roster.
 */
public interface ParticularRosterI
{
    String getName();
    void init(RostererSessionI rostererSession, DataStore dataStore);
    DataStore getDataStore();
    MonthTransferObj getCurrentMonth();
    MessagesContainer writeAllPhoneRequestMsgs();
    void setAtMonth(int month);
    //At some stage combine these two (at moment must call one then the other):
    StringBuffer display(int roster);
    RosterTransferObj getRosterTransferObj();
    MessagesContainer writeAllRosteringMsgs(Date begin, Date end, String periodTxt);
    MessagesContainer writeAllAddressAndRaffleMsgs();
    MonthInYearI getWhenMonth();
    int getWhenYear();
    MessagesContainer writeNoOvernightsRequestMsgs();
    MessagesContainer writeStatusRequestMsgs();
    MonthInYearI getMonth(int current);
    boolean isRosteredInCurrentMonth(WorkerI vol);
    List<ParticularShift> getParticularShifts(Date begin, Date end);
    List<ParticularShift> getParticularShifts(Date begin, Date end, WhichShiftI whichShift, DayInWeekI dayInWeek);
    List<ParticularShift> getParticularShifts(List<WorkerI> volunteers, Date begin, Date end, boolean all);
    List<ParticularShift> getParticularShifts(WorkerI vol);
    String uploadRoster();
    String uploadRosterAsOld();
}
