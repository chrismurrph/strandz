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
package org.strandz.applic.timesheet2;

import org.strandz.core.interf.Cell;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NonVisualAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.VisualAttribute;
import org.strandz.view.timesheet.TimesheetPanel2;

public class TimesheetDT
{
    public TimesheetPanel2 ui0;
    public Strand strand;
    public Node timesheetNode;
    public Cell timesheetCell;
    private NonVisualAttribute fridayHoursAttribute;
    private NonVisualAttribute mondayHoursAttribute;
    private NonVisualAttribute saturdayHoursAttribute;
    private NonVisualAttribute sundayHoursAttribute;
    private NonVisualAttribute taskAttribute;
    private NonVisualAttribute thursdayHoursAttribute;
    private NonVisualAttribute tuesdayHoursAttribute;
    private NonVisualAttribute wednesdayHoursAttribute;
    public Cell taskLookupCell;
    public VisualAttribute taskNameAttribute;
    public Cell fridayHoursLookupCell;
    public VisualAttribute fridayHoursTimeSpentAttribute;
    public Cell mondayHoursLookupCell;
    public VisualAttribute mondayHoursTimeSpentAttribute;
    public Cell saturdayHoursLookupCell;
    public VisualAttribute saturdayHoursTimeSpentAttribute;
    public Cell sundayHoursLookupCell;
    public VisualAttribute sundayHoursTimeSpentAttribute;
    public Cell thursdayHoursLookupCell;
    public VisualAttribute thursdayHoursTimeSpentAttribute;
    public Cell tuesdayHoursLookupCell;
    public VisualAttribute tuesdayHoursTimeSpentAttribute;
    public Cell wednesdayHoursLookupCell;
    public VisualAttribute wednesdayHoursTimeSpentAttribute;

    public TimesheetDT(SdzBagI sdzBag)
    {
        ui0 = (TimesheetPanel2) sdzBag.getPane(0);
        strand = sdzBag.getStrand();
        timesheetNode = strand.getNodeByName("Timesheet Node");
        timesheetCell = timesheetNode.getCell();
        fridayHoursAttribute = (NonVisualAttribute) timesheetCell.getAttributeByName(
            "fridayHours");
        mondayHoursAttribute = (NonVisualAttribute) timesheetCell.getAttributeByName(
            "mondayHours");
        saturdayHoursAttribute = (NonVisualAttribute) timesheetCell.getAttributeByName(
            "saturdayHours");
        sundayHoursAttribute = (NonVisualAttribute) timesheetCell.getAttributeByName(
            "sundayHours");
        taskAttribute = (NonVisualAttribute) timesheetCell.getAttributeByName("task");
        thursdayHoursAttribute = (NonVisualAttribute) timesheetCell.getAttributeByName(
            "thursdayHours");
        tuesdayHoursAttribute = (NonVisualAttribute) timesheetCell.getAttributeByName(
            "tuesdayHours");
        wednesdayHoursAttribute = (NonVisualAttribute) timesheetCell.getAttributeByName(
            "wednesdayHours");
        taskLookupCell = timesheetCell.getCellByName("task Lookup Cell");
        taskNameAttribute = (VisualAttribute) taskLookupCell.getAttributeByName(
            "task Name");
        fridayHoursLookupCell = timesheetCell.getCellByName(
            "fridayHours Lookup Cell");
        fridayHoursTimeSpentAttribute = (VisualAttribute) fridayHoursLookupCell.getAttributeByName(
            "fridayHours TimeSpent");
        mondayHoursLookupCell = timesheetCell.getCellByName(
            "mondayHours Lookup Cell");
        mondayHoursTimeSpentAttribute = (VisualAttribute) mondayHoursLookupCell.getAttributeByName(
            "mondayHours TimeSpent");
        saturdayHoursLookupCell = timesheetCell.getCellByName(
            "saturdayHours Lookup Cell");
        saturdayHoursTimeSpentAttribute = (VisualAttribute) saturdayHoursLookupCell.getAttributeByName(
            "saturdayHours TimeSpent");
        sundayHoursLookupCell = timesheetCell.getCellByName(
            "sundayHours Lookup Cell");
        sundayHoursTimeSpentAttribute = (VisualAttribute) sundayHoursLookupCell.getAttributeByName(
            "sundayHours TimeSpent");
        thursdayHoursLookupCell = timesheetCell.getCellByName(
            "thursdayHours Lookup Cell");
        thursdayHoursTimeSpentAttribute = (VisualAttribute) thursdayHoursLookupCell.getAttributeByName(
            "thursdayHours TimeSpent");
        tuesdayHoursLookupCell = timesheetCell.getCellByName(
            "tuesdayHours Lookup Cell");
        tuesdayHoursTimeSpentAttribute = (VisualAttribute) tuesdayHoursLookupCell.getAttributeByName(
            "tuesdayHours TimeSpent");
        wednesdayHoursLookupCell = timesheetCell.getCellByName(
            "wednesdayHours Lookup Cell");
        wednesdayHoursTimeSpentAttribute = (VisualAttribute) wednesdayHoursLookupCell.getAttributeByName(
            "wednesdayHours TimeSpent");
    }
}
