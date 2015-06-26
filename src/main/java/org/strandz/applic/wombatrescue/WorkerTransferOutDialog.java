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

import org.strandz.core.interf.Cell;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.UnadornedTransferOutDialog;
import org.strandz.core.interf.ApplicationHelper;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.widgets.AlphabetScrollPane;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.lgpl.widgets.table.ClickListenerI;
import org.strandz.view.wombatrescue.NarrowWorkerPanel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;

public class WorkerTransferOutDialog extends UnadornedTransferOutDialog
{
    /**
     * Whether the actor was previously displayed. As a matter of course we un-display it
     * while the new form of display is the current form. This indicates that after the
     * current form is complete we must re-display it in the old form.
     */
    private AlphabetScrollPane alphabetScrollPane;
    /**
     * Panel that will need to transfer out onto another panel
     * and then back again onto the panel it was taken from.
     */
    private NarrowWorkerPanel narrowWorkerPanel;    
    private WRTransferOutHelper helper; 
    private ClickListenerI savedEditOnRClick;
    
    public WorkerTransferOutDialog(Frame owner, String title, boolean modal, VisibleStrand transferVisibleStrand, 
                                   RuntimeAttribute clickedOnAttribute, TheRosterStrand callingStrand, ApplicationHelper applicationHelper)
            throws HeadlessException
    {
        super(owner, title, modal, transferVisibleStrand, clickedOnAttribute, callingStrand, applicationHelper);
        helper = new WRTransferOutHelper( transferVisibleStrand);
        relocationOutCallbacks.performSearch();
    }

    public void relocatePanelGoingOut( SdzBagI sbI)
    {
        alphabetScrollPane = (AlphabetScrollPane)sbI.getPane( 0);
        narrowWorkerPanel = (NarrowWorkerPanel)alphabetScrollPane.getContentPanel().getComponent( 0);
        //Err.pr( "Will setPane to be a " + narrowWorkerPanel.getClass().getName());
        sbI.setPane( 0, narrowWorkerPanel);
    }
    
    public void relocatePanelComingBack( SdzBagI sbI)
    {
        alphabetScrollPane.getContentPanel().removeAll();
        alphabetScrollPane.getContentPanel().add(narrowWorkerPanel, BorderLayout.CENTER);
        sbI.setPane(0, alphabetScrollPane);
    }

    public void setPanelNodeTitlePanelGoingOut( SdzBagI sbI)
    {
        RosterWorkers_NEW_FORMATDT dt = ((RosterWorkersStrand)transferVisibleStrand).getDt();
        ComponentTableView tableView = dt.ui0.getQuickViewRosterSlotsPanel().getTableView();
        savedEditOnRClick = tableView.getClickListener();
        tableView.setClickListener( null);
        transferVisibleStrand.setPanelNodeTitle( sbI.getPane(0), sbI.getNode(0), sbI.getNode(0).getDisplayName());
    }
    
    public void setPanelNodeTitlePanelComingBack( SdzBagI sbI)
    {
        RosterWorkers_NEW_FORMATDT dt = ((RosterWorkersStrand)transferVisibleStrand).getDt();
        ComponentTableView tableView = dt.ui0.getQuickViewRosterSlotsPanel().getTableView();
        tableView.setClickListener( savedEditOnRClick);
        helper.setPanelNodeTitlePanelComingBack( sbI);
    }
    
    public void performSearch( RuntimeAttribute clickedOnAttribute)
    {
        Cell cell = clickedOnAttribute.getCell();
        WorkerI worker = (WorkerI)cell.getLastNavigatedToDO();
        Assert.notNull( worker, "Do not have a last navigated to worker in cell <" + cell + ">");
        String orderBy = worker.getOrderBy();
        AlphabetActionListener alphabetActionListener = ((RosterWorkersStrand)transferVisibleStrand).volunteerTriggers.alphabetListener; 
        boolean ok = alphabetActionListener.findWorkerStartsWith( orderBy);
        if(!ok)
        {
            Err.error( "Have not been able to find a worker that starts with <" + 
                    orderBy + "> in cell <" + alphabetActionListener.getWorkerCell() + ">");
        }
    }
}
