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
package org.strandz.applic.wombatrescue;

import org.strandz.lgpl.widgets.table.DataRendererI;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.extent.VisibleExtent;
import org.strandz.data.wombatrescue.business.AbstractRosterService;
import org.strandz.data.wombatrescue.calculated.Night;

import javax.swing.JLabel;
import java.awt.Color;
import java.util.List;

/**
 * Looks at data for the roster and using it sets colours and boilerplate text. For example
 * 'Not open Sunday' 
 */
class NightsDataRenderer implements DataRendererI
{
    private TheRosterDT dt;
    private ComponentTableView tableView;
    private TheRosterEvents theRosterEvents;
    
    NightsDataRenderer( TheRosterDT dt, 
                  ComponentTableView componentTableView,
                  TheRosterEvents theRosterEvents)
    {
        this.dt = dt;
        this.tableView = componentTableView;
        this.theRosterEvents = theRosterEvents;
    }
            
    public void writeToCell(Object comp, Object text, int row, int column)
    {
        if(column > 1)
        {
            if(theRosterEvents.isAnnotated( row, tableView))
            {
                JLabel label = (JLabel)comp;
                label.setBackground( Color.GRAY.brighter());
                if(column == 6)
                {
                    DayInWeek dayInWeek = theRosterEvents.getAnnotated( row, tableView);
                    if(dayInWeek == DayInWeek.SUNDAY)
                    {
                        label.setText( AbstractRosterService.notOpenSundayText);
                    }
                    else if(dayInWeek == DayInWeek.THURSDAY)
                    {
                        label.setText( Night.THURSDAY);
                    }
                }
            }
        }
        if(!theRosterEvents.isAnnotated( row, tableView) && column > 1)
        {
            VisibleExtent extent = dt.nightsListDetailCell.getDataRecords();
            List<Night> nights = extent.getList();
            Night night = nights.get( row);
            if(night.isNotComplete())
            {
                JLabel label = (JLabel)comp;
                //label.setText( AbstractRosterService.closedText);
                label.setBackground( Color.ORANGE);
            }
        }
    }

    public Object getIdentifier()
    {
        return dt;
    }
}
