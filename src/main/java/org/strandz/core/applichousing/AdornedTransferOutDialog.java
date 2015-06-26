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
package org.strandz.core.applichousing;

import org.strandz.core.domain.event.OperationEvent;
import org.strandz.core.interf.AbstractTransferOutDialog;
import org.strandz.core.interf.ActualNodeControllerI;
import org.strandz.core.interf.ApplicationHelper;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.core.interf.StrandI;
import org.strandz.lgpl.util.Assert;
import org.strandz.view.util.StrandArea;

import javax.swing.JComponent;
import java.awt.Frame;
import java.awt.HeadlessException;

abstract public class AdornedTransferOutDialog extends AbstractTransferOutDialog
{
    public AdornedTransferOutDialog( 
            Frame owner, String title, boolean modal, VisibleStrandI transferVisibleStrand, 
            RuntimeAttribute clickedOnAttribute, VisibleStrandI callingStrand, ApplicationHelper applicationHelper)
            throws HeadlessException
    {
        super( owner, title, modal);
        externalStrandArea = new StrandArea();
        externalStrandArea.init();
        StrandI transferStrand = transferVisibleStrand.getSdzBagI().getStrand();
        ActualNodeControllerI physicalController = transferStrand.getNodeController().getPhysicalController();
        externalStrandArea.getToolBarArea().refitArea( (JComponent)physicalController);
        Node targetNode = getTargetNode( transferStrand);
        //Err.pr( "targetNode: " + targetNode);
        SBIStatusBarHelper sbiStatusBarHelper = ((SdzBag)transferVisibleStrand.getSdzBagI()).getBarHelper();
        //sbiStatusBarHelper.refitStatusArea( targetNode);
        NodeStatusBarI physicalStatusBar = sbiStatusBarHelper.nsbFromNode( targetNode);
        Assert.notNull( physicalStatusBar, "Can't find a NodeStatusBarI associated with " + targetNode);
        physicalStatusBar.setNode( targetNode);
        externalStrandArea.getStatusArea().refitArea( 
                (JComponent)physicalStatusBar
        );
        physicalStatusBar.navigated( new OperationEvent( targetNode, null, targetNode.getState()));
        init( owner, transferVisibleStrand, clickedOnAttribute, callingStrand, applicationHelper);
    }        
}
