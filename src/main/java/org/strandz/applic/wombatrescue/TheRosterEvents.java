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

import org.strandz.core.interf.ApplicationHelper;
import org.strandz.core.interf.NonVisualTableAttribute;
import org.strandz.core.interf.TableAttribute;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.ToolTipProviderI;
import org.strandz.lgpl.widgets.UnBorderedLabel;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.lgpl.widgets.table.DataRendererI;
import org.strandz.view.wombatrescue.ClashesCellComponentCreator;

import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheRosterEvents
{
    private Map<Integer, DayInWeek> annotedRows = new HashMap<Integer,DayInWeek>();
    
    public TheRosterEvents( TheRosterDT dt, RosterWorkersStrand transferingVisibleStrand, 
                            TheRosterStrand callingStrand, ApplicationHelper applicationHelper)
    {
        List<TableAttribute> rightClickables = new ArrayList<TableAttribute>();
        rightClickables.add( (TableAttribute)dt.firsteveningworkertoShortAttribute);
        rightClickables.add( (TableAttribute)dt.secondeveningworkertoShortAttribute);
        rightClickables.add( (TableAttribute)dt.thirdeveningworkertoShortAttribute);
        rightClickables.add( (TableAttribute)dt.firstovernightworkertoShortAttribute);
        rightClickables.add( (TableAttribute)dt.secondovernightworkertoShortAttribute);
        ComponentTableView nightsTableView = dt.ui0.getNightsTablePanel().getNightsComponentTable();
        nightsTableView.setClickListener( 
                new RosterRightClickPopup( nightsTableView, transferingVisibleStrand, dt.nightsListDetailNode, 
                                           rightClickables, callingStrand, applicationHelper, true, this));
        nightsTableView.addDataRenderer( new NightsDataRenderer( dt, nightsTableView, this));       
        nightsTableView.addDataRenderer( new WeekOfMonthContrastRenderer( this));
        Map<String, Integer> headings = new HashMap<String, Integer>();
        headings.put( dt.nightformattedDateAttribute.getName(), 1);
        nightsTableView.setHeadings( headings);
        /* Sticking with original attributes will give same results
        rightClickables = new ArrayList<TableAttribute>();
        rightClickables.add( (TableAttribute)dt.clashesfirsteveningworkertoShortAttribute);
        rightClickables.add( (TableAttribute)dt.clashessecondeveningworkertoShortAttribute);
        rightClickables.add( (TableAttribute)dt.clashesthirdeveningworkertoShortAttribute);
        rightClickables.add( (TableAttribute)dt.clashesfirstovernightworkertoShortAttribute);
        rightClickables.add( (TableAttribute)dt.clashessecondovernightworkertoShortAttribute);
        */
        ComponentTableView clashesTableView = dt.ui0.getNightsTablePanel().getClashesComponentTable();
        clashesTableView.setClickListener( 
                new RosterRightClickPopup( clashesTableView, transferingVisibleStrand, dt.clashesListDetailNode, 
                                           rightClickables, callingStrand, applicationHelper, true, this));
        
        ClashesCellComponentCreator creator = 
                (ClashesCellComponentCreator)clashesTableView.getCellComponentCreator();
        clashesTableView.addDataRenderer( new WeekOfMonthContrastRenderer( this));
        creator.setToolTipProviderI( new ToolTipProvider( dt, clashesTableView));
        
        ComponentTableView unfilledNightsTableView = dt.ui0.getNightsTablePanel().getUnfilledNightsComponentTable();
        unfilledNightsTableView.addDataRenderer( new WeekOfMonthContrastRenderer( this));
        unfilledNightsTableView.setClickListener( 
                new RosterRightClickPopup( unfilledNightsTableView, transferingVisibleStrand, dt.failedNightsListDetailNode, 
                                           rightClickables, callingStrand, applicationHelper, true, this));

        rightClickables = new ArrayList<TableAttribute>();
        rightClickables.add( (TableAttribute)dt.workertoShortAttribute);
        ComponentTableView unrosteredTableView = dt.ui0.getNightsTablePanel().getUnrosteredComponentTable();
        unrosteredTableView.setClickListener( 
                new RosterRightClickPopup( unrosteredTableView, transferingVisibleStrand, dt.unrosteredAvailableWorkersListDetailNode, 
                                           rightClickables, callingStrand, applicationHelper, true, this));
    }
    
    boolean isAnnotated( int row, ComponentTableView tableView)
    {
        boolean result = false;
        DayInWeek dayInWeek = getAnnotated( row, tableView);
        if(dayInWeek != DayInWeek.NULL)
        {
            result = true;
        }
        return result;
    }
    
    /**
     * Do once only per row, rather than for every column 
     */
    DayInWeek getAnnotated( int row, ComponentTableView tableView)
    {
        DayInWeek result = DayInWeek.NULL;
        if(!annotedRows.containsKey( row))
        {
            TableModel model = tableView.getModel();
            String dateCellValue = (String)model.getValueAt( row, 1);
            boolean onSunday = dateCellValue.contains( "Sun");
            boolean onThursday = dateCellValue.contains( "Thu");
            if(onSunday || onThursday)
            {
                if(onSunday)
                {
                    result = DayInWeek.SUNDAY;
                }
                else
                {
                    result = DayInWeek.THURSDAY;
                }
            }
            annotedRows.put( row, result);
        }
        else
        {
            result = annotedRows.get( row);
        }
        return result;
    }
        
    private static class WeekOfMonthContrastRenderer implements DataRendererI
    {
        Object identifier;

        WeekOfMonthContrastRenderer( Object identifier)
        {
            this.identifier = identifier;
        }

        public void writeToCell(Object comp, Object text, int row, int column)
        {
            if(column == 0)
            {
                Integer number = (Integer)text;
                UnBorderedLabel label = (UnBorderedLabel)comp;
                boolean isEven = Utils.isEven( number);
                //Err.pr( "Txt is " + text + " so even is " + isEven);
                if(isEven)
                {
                    label.setBackground( Color.PINK);
                }
                else
                {
                    label.setBackground( Color.PINK.darker());
                }
            }
        }

        public Object getIdentifier()
        {
            return identifier;
        }
    }
        
    private static class ToolTipProvider implements ToolTipProviderI
    {
        private TheRosterDT dt;
        private ComponentTableView tableView;
        
        public ToolTipProvider( TheRosterDT dt, ComponentTableView tableView)
        {
            this.dt = dt;
            this.tableView = tableView;
        }

        public String getToolTip( Component source)
        {
            List<String> sentences = (List<String>)((NonVisualTableAttribute)dt.clashtoSentenceAttribute).getItemList();
            List<String> reasons = (List<String>)((NonVisualTableAttribute)dt.clashtoReasonAttribute).getItemList();
            int idx = tableView.getRow( source);
            Err.pr( SdzNote.MANY_NON_VISUAL_ATTRIBS, "Row hovering over is " + idx);
            return sentences.get( idx) + ": " + reasons.get( idx);
        }
    }
}
