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

import org.strandz.core.interf.Application;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NodeController;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.VisibleStrandHelper;
import org.strandz.core.interf.StrandI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.view.util.AbstractStrandArea;

import javax.swing.JComponent;

/**
 * Helps implement the interface VisibleStrand
 */
abstract public class AbstractStatusBarVisibleStrandHelper extends VisibleStrandHelper
{
    protected JComponent panel;
    protected Node node;
    private NodeController nodeController;
    // private boolean initCalled = false;
    private JComponent surface;
    private DataStore data;
    private Application a;
    private SBIStatusBarHelper barHelperSBI;
    private static int times = 0;
    
    public AbstractStatusBarVisibleStrandHelper(StrandI strand,
                                              Application a,
                                              AbstractStrandArea.EnclosurePanel surface)
    {
        this( null, strand, a, surface);
    }

    public AbstractStatusBarVisibleStrandHelper(SBIStatusBarHelper barHelperSBI,
                                              StrandI strand,
                                              Application a,
                                              AbstractStrandArea.EnclosurePanel surface)
    {
        this.strand = strand;
        this.surface = surface;
        this.a = a;
        data = a.getDataStore();
        this.barHelperSBI = barHelperSBI;
    }

    public void setPanelNodeTitle(JComponent p,
                                  Node node,
                                  String title)
    {
        /*
        times++;
        Err.pr( "BASH panel being set to " + p.getName() + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        panel = p;
        if(panel == null)
        {
            Err.error("BasicActionableStrandHelper must have a JPanel");
        }
        if(node == null)
        {
            Print.pr("BasicActionableStrandHelper must have a Node");
        }
        this.node = node;
        // Done when setNode, so should not need to do it here
        // attachStatusBarToNode( node);
    }
    
    public void setRequiresRefresh()
    {
        strand.setRequiresRefresh();
    }

    public void display(boolean b)
    {
        if(nodeController == null)
        {
            Err.error("AbstractVisibleStrand s/have its NodeController set");
        }
        /*
        else if(panel == null)
        {
        Err.error("AbstractVisibleStrand s/have its JPanel set");
        }
        */
        else if(getSurface() == null)
        {
            Err.error("AbstractVisibleStrand s/have its Surface set");
        }
        if(!b)
        {
            // Err.pr("Are setting a transaction INvisible here");
            strand.setWhenLastVisibleFalseControllerMemento(
                nodeController.createControllerMemento());
        }
        else
        {
            // if(initCalled == false)
            {
                // initable.sdzInit();
                nodeController.setStrand(strand);
                // Err.pr( "Inside setNodeController()DISPLAY where nodeController is " +
                // nodeController.getId());
                // initCalled = true;
            }
            /*
            * Don't worry about this complication for now.
            *
            //Think can use following but haven't tested:
            //if(strand.lastControllerMemento != null)
            {
            nodeController.setControllerMemento( strand.lastControllerMemento);
            }
            //Seems very dynamic for setting controls can use. As only one
            //ControlSignatures for the whole JVM, another instance of this
            //may have changed the controls set - thus here we always change
            //it back.
            ControlSignatures.setControlInfoImpl( controlInfoJavaFile);
            */
        }
    }

    protected void setupMessageDlg(boolean b, JComponent parent)
    {
        if(b)
        {
            MessageDlg.setInternalFrames(false);
            MessageDlg.setDlgParent(parent);
        }
    }

    public void setNodeController(NodeController nodeController)
    {
        this.nodeController = nodeController;
        // if(initCalled)
        {
            nodeController.setStrand(strand);
            // Err.pr( "Inside setNodeController where nodeController is " +
            // nodeController.getId());
        }
    }

    public NodeController getNodeController()
    {
        return nodeController;
    }

    JComponent getSurface()
    {
        Err.pr( SdzNote.VISIBLE_STRAND_IS_INDEPENDENT, "Surface that are getting (to add onto) is called " + surface.getName() + 
                ", and is type " + surface.getClass().getName() + " (expect *2 printout)");
        return surface;
    }

    /**
     * Called when we go to a new tab etc - whenever we set nodes visible.
     * Calling with null causes the status bar to disappear.
     */
    public NodeStatusBarI refitStatusArea(Node node)
    {
        /*
        times++;
        Err.pr( "NEW refitStatusArea for " + node + " times " + times);
        */
        if(barHelperSBI != null)
        {
            return barHelperSBI.refitStatusArea( node);
        }
        else
        {
            return null;
        }
    }
    
}
