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
package org.strandz.view.wombatrescue;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.view.ScrollPanel;
import org.strandz.lgpl.widgets.LargeBlueWhiteBgLabel;
import org.strandz.lgpl.widgets.WhiteBgScrollPane;
import org.strandz.lgpl.widgets.table.ComponentTableView;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.Color;

public class NightsTablePanel extends JPanel
{
    private ComponentTableView nightsComponentTable;
    private ComponentTableView clashesComponentTable;
    private ComponentTableView unfilledNightsComponentTable;
    private ComponentTableView unrosteredComponentTable;

    public NightsTablePanel()
    {
    }

    public void init()
    {
        setOpaque( true);
        setBackground( Color.WHITE);
        setName("NightsTablePanel");
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.init();
        
        nightsComponentTable = new ComponentTableView();
        nightsComponentTable.setCellComponentCreator( new NightsCellComponentCreator());
        nightsComponentTable.setColumnWidthsInformer( new NightsColumnWidthsInformer());
        //nightsComponentTable.setFocusListener( new NullFocusListener());
        nightsComponentTable.setName("NightsComponentTable");
        nightsComponentTable.setReadOnly( true);
        nightsComponentTable.setOpaque( true);
        nightsComponentTable.setBackground( Color.WHITE);
        
        clashesComponentTable = new ComponentTableView();
        clashesComponentTable.setCellComponentCreator( new ClashesCellComponentCreator());
        clashesComponentTable.setColumnWidthsInformer( new NightsColumnWidthsInformer());
        //clashesComponentTable.setFocusListener( new NullFocusListener());
        clashesComponentTable.setName( "ClashesComponentTable");
        clashesComponentTable.setReadOnly( true);
        clashesComponentTable.setOpaque( true);
        clashesComponentTable.setBackground( Color.WHITE);
        
        unfilledNightsComponentTable = new ComponentTableView();
        unfilledNightsComponentTable.setCellComponentCreator( new NightsCellComponentCreator());
        unfilledNightsComponentTable.setColumnWidthsInformer( new NightsColumnWidthsInformer());
        //unfilledNightsComponentTable.setFocusListener( new NullFocusListener());
        unfilledNightsComponentTable.setName( "UnfilledNightsComponentTable");
        unfilledNightsComponentTable.setReadOnly( true);
        unfilledNightsComponentTable.setOpaque( true);
        unfilledNightsComponentTable.setBackground( Color.WHITE);
        
        unrosteredComponentTable = new ComponentTableView();
        unrosteredComponentTable.setCellComponentCreator( new WorkersCellComponentCreator());
        unrosteredComponentTable.setColumnWidthsInformer( new WorkersColumnWidthsInformer());
        //unrosteredComponentTable.setFocusListener( new NullFocusListener());
        unrosteredComponentTable.setName( "UnrosteredComponentTable");
        unrosteredComponentTable.setReadOnly( true);
        unrosteredComponentTable.setOpaque( true);
        unrosteredComponentTable.setBackground( Color.WHITE);
        
        double size[][] = {
            { ModernTableLayout.FILL}, // Columns
            { ModernTableLayout.PREFERRED,
              ModernTableLayout.PREFERRED,
              ModernTableLayout.PREFERRED,
              ModernTableLayout.PREFERRED,
              ModernTableLayout.PREFERRED,
              ModernTableLayout.PREFERRED,
              ModernTableLayout.PREFERRED,
              ModernTableLayout.PREFERRED} // Rows
        };
        scrollPanel.setLayout( new ModernTableLayout(size));
        
        LargeBlueWhiteBgLabel label = new LargeBlueWhiteBgLabel();
        label.setText( "Roster");
        label.setName( "lRoster");
        scrollPanel.add( label, "0,0");
        scrollPanel.add( nightsComponentTable, "0,1");
        
        label = new LargeBlueWhiteBgLabel();
        label.setText( "Clashed Nights");
        label.setName( "lClashedNights");
        scrollPanel.add( label, "0,2");
        scrollPanel.add( clashesComponentTable, "0,3");

        label = new LargeBlueWhiteBgLabel();
        label.setText( "Unfilled Nights");
        label.setName( "lUnfilledNights");
        scrollPanel.add( label, "0,4");
        scrollPanel.add( unfilledNightsComponentTable, "0,5");

        label = new LargeBlueWhiteBgLabel();
        label.setText( "Unrostered Workers");
        label.setName( "lUnrosteredWorkers");
        scrollPanel.add( label, "0,6");
        scrollPanel.add( unrosteredComponentTable, "0,7");
        
        JScrollPane scrollpane = new WhiteBgScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollpane.setOpaque( true);
        scrollpane.setBackground( Color.WHITE);
        setLayout(new BorderLayout());
        add(scrollpane, BorderLayout.CENTER);
        //
        setNightsComponentTable(nightsComponentTable);
        setClashesComponentTable(clashesComponentTable);
        setUnfilledNightsComponentTable(unfilledNightsComponentTable);
        setUnrosteredComponentTable(unrosteredComponentTable);
    }    
    
    public ComponentTableView getNightsComponentTable()
    {
        return nightsComponentTable;
    }

    public void setNightsComponentTable(ComponentTableView nightsComponentTable)
    {
        this.nightsComponentTable = nightsComponentTable;
    }

    public ComponentTableView getClashesComponentTable()
    {
        return clashesComponentTable;
    }

    public void setClashesComponentTable(ComponentTableView clashesComponentTable)
    {
        this.clashesComponentTable = clashesComponentTable;
    }

    public ComponentTableView getUnfilledNightsComponentTable()
    {
        return unfilledNightsComponentTable;
    }

    public void setUnfilledNightsComponentTable(ComponentTableView unfilledNightsComponentTable)
    {
        this.unfilledNightsComponentTable = unfilledNightsComponentTable;
    }

    public ComponentTableView getUnrosteredComponentTable()
    {
        return unrosteredComponentTable;
    }

    public void setUnrosteredComponentTable(ComponentTableView unrosteredComponentTable)
    {
        this.unrosteredComponentTable = unrosteredComponentTable;
    }
}
