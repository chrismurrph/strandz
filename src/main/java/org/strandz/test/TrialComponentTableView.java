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
package org.strandz.test;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.widgets.table.CellComponentCreatorI;
import org.strandz.lgpl.widgets.table.ClickListenerI;
import org.strandz.lgpl.widgets.table.ColumnWidthsInformerI;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.view.test.CTVTrialPanel;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class TrialComponentTableView
{
    private CTVTrialPanel ctvTrialPanel;

    private class PersonTableColumnWidthsInformer implements ColumnWidthsInformerI
    {
        public double getColumnWidthAt(int column, int numOfColumns)
        {
            double result = 80;
            if(column == 6)
            {
                result = 180;
            }
            return result;
        }
    }

    private class PersonTableClickListener implements ClickListenerI
    {
        public void outerDoubleClicked(int row, int column, MouseEvent e)
        {
            Err.pr( "outer doubleClicked() at " + row + ", " + column);
        }

        public void outerSingleClicked(int row, int column, MouseEvent e)
        {
            Err.pr( "outer singleClicked() at " +  + row + ", " + column);
        }
    
        public void innerDoubleClicked(int row, int column, MouseEvent e)
        {
            Err.pr( "inner doubleClicked() at " + row + ", " + column);
        }

        public void innerSingleClicked(int row, int column, MouseEvent e)
        {
            Err.pr( "inner singleClicked() at " +  + row + ", " + column);
        }
    }

    private class PersonTableCellComponentCreator implements CellComponentCreatorI
    {
        public JComponent createCell(int row, int col, boolean editableRow, String reason, boolean spacerRow)
        {
            JComponent result;
            if(editableRow)
            {
                result = new JTextField();
            }
            else
            {
                result = new JLabel();
            }
            Err.pr( SdzNote.CTV_ADD_CELL, "In " + this.getClass().getName() + " have created a " + 
                    result.getClass().getName() + " because " + reason);
            result.setName( "PersonTableModel textfield at col " + col + ", row " + row);
            return result;
        }
    }

    private class DebugAction implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //See that the wrapper controls are named correctly
            ComponentTableView tableView = ctvTrialPanel.getTablePanel().getTable();
            Print.prList( tableView.getListOfEditableRowControls(), "Editables");
            Print.prList( tableView.getListOfNonEditableRowControls(), "Non-Editables");
        }
    }

    public static void main(String[] args)
    {
        new TrialComponentTableView();
    }

    public TrialComponentTableView()
    {
        JFrame frame = new JFrame();
        MessageDlg.setFrame( frame);
        PersonTableColumnWidthsInformer personTableModelColumnWidthsInformer = new PersonTableColumnWidthsInformer();
        CellComponentCreatorI personTableModelCellComponentCreator = 
                new PersonTableCellComponentCreator();
                //new ROCellComponentCreator();
        ctvTrialPanel = new CTVTrialPanel();
        ctvTrialPanel.init();
        MessageDlg.setDlgParent( ctvTrialPanel);
        ctvTrialPanel.getTablePanel().getTable().setColumnWidthsInformer( personTableModelColumnWidthsInformer);
        ctvTrialPanel.getTablePanel().getTable().setCellComponentCreator( personTableModelCellComponentCreator);
        //componentTableView.addClickListener( new PersonTableModelClickListener());
        PersonTableModel tableModel = new PersonTableModel();
        tableModel.setTable( ctvTrialPanel);
        ctvTrialPanel.getTablePanel().getTable().setModel( tableModel);
        ctvTrialPanel.getTablePanel().getTable().tableChanged( null);
        DebugAction debugAction = new DebugAction();
        ctvTrialPanel.getButtonPanel().getBDebug().addActionListener( debugAction);
        DisplayUtils.display( frame, ctvTrialPanel);
    }
}
