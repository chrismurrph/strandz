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

import org.strandz.core.domain.NodeChangeEvent;
import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.domain.OtherSignatures;
import org.strandz.core.domain.event.AccessEvent;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.StandardPanes;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.StrandI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.IdentifierI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.view.util.AbstractStrandArea;

import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SBIStatusBarHelper implements NodeChangeListener
{
    //private Strand strand;
    StandardPanes standardPanes;
    //Node is key for finding NodeStatusBar
    private Set<NodeSBAssociation> statusBars = new HashSet<NodeSBAssociation>();
    private NodeStatusBarI barForType;
    /**
     * Current NodeStatusBarI
     */
    NodeStatusBarI nsb;
    private static final boolean useStatus = true;
    private static int times = 0;
    private boolean partOfApplication = false;
    // temp
    private boolean statusAreaRefitted = false;
    boolean refitStatusAreaDone;

    void init( StrandI strand, StandardPanes standardPanes)
    {
        //this.strand = strand;
        this.standardPanes = standardPanes;
        strand.addNodeChangeListener(this);
        // Err.pr( "&&&& strand that has CISBH as listener is: " + strand.timesConstruct);
    }
    
    /**
     * Called when we go to a new tab etc - whenever we set nodes visible.
     * Calling with null causes the status bar to disappear.
     */
    public NodeStatusBarI refitStatusArea( Node node)
    {
        nsb = nsbFromNode(node);
        // Err.pr( "barHelperSBISBI.nsb: " + barHelperSBISBI.nsb);
        // Err.pr( "barHelperSBISBI.statusPane: " + barHelperSBISBI.statusPane);
        if(getStrandArea() != null)
        {
            // Needed when created SimplestApplication
            // barHelperSBISBI.statusPane.refitStatusArea( (JComponent)barHelperSBISBI.nsb);
            if(nsb instanceof JComponent)
            {
                getStrandArea().getStatusArea().refitArea(
                    (JComponent)nsb);
            }
        }
        refitStatusAreaDone = true;
        return nsb;
    }

    public void setPartOfApplication(boolean b)
    {
        if(b != partOfApplication)
        {
            if(b)
            {
                // In this case even thou the StatusPane is contained elsewhere,
                // the listener is still here. Perhaps not completely intuitive,
                // but done whilst moving away from old code, so may change when
                // everything working:
                // strand.removeNodeChangeListener( this);
                standardPanes.setStrandArea(null);
            }
            partOfApplication = b;
        }
    }

    public void setStrandArea(AbstractStrandArea belowMenuPanel)
    {
        standardPanes.setStrandArea(belowMenuPanel);
    }

    /**
     * Not necessary, but here due to NodeChangeListener
     */
    public void accessChange(AccessEvent evt)
    {// Err.pr("$$ ACCESS: " + evt);
    }

     /**/
    /**
     * ... called when the same NodeStatusBar has to service > 1 node. ie./
     * refitStatusArea (or the nonApplication version) has already ensured
     * that we are talking to the right nsb.
     */
    public void nodeChangePerformed(NodeChangeEvent evt)
    {
        if(refitStatusAreaDone)
        {
            /*
            times++;
            Err.pr("=== SBIStatusBarHelper.nodeChangePerformed: " + evt + " times " + times);
            if (times == 0)
            {
            Err.pr("third");
            }
            */
            Node targetNode = (Node) evt.getSource();
            if(nsb != null)
            {
                nsb.setNode(targetNode);
            }
            else
            {
                Err.error(
                    "Have not called refitStatusArea() or "
                        + "nonApplicationRefitStatusArea()");
                // nsbFromNode will not have been called if running from an Application
                // nsb = nsbFromNode( targetNode);
                nsb.setNode(targetNode);
            }
        }
    }

    /**
     * Done while setting up the infrastructure of the
     * VisibleStrand. We create list of statusBars that we
     * use later by calling this for each node that we have.
     * 
     * Called both from setting node/s and status bar. Thus keep property
     * setting order independence - one property (node/s or status bar) will 
     * always be null if it is the first one called, but both won't be null
     * on the second call. 
     */
    void attachStatusBarToNode(Node node)
    {
        // NodeStatusBarInterface nsb = null;
        /*
        * This was only necessary if implementing design-time
        if(nsb != null)
        {
        nsb.setNode( node);
        }
        */
        /*
        times++;
        Err.pr( "--  attachStatusBarToNode for " + node + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
        if(barForType != null)
        {
            NodeSBAssociation nodeSBAssociation = new NodeSBAssociation(node);
            // nodeSBAssociation.setNodeStatusBar( nsb);
            // Err.pr( "\tnode " + node + " attached to " + nsb.hashCode() + " in " + this);
            statusBars.add(nodeSBAssociation);
            nodeSBAssociation.setNodeStatusBar(/*createNSB()*/ barForType);
            if(!partOfApplication)
            {
                nonApplicationRefitStatusArea(node);
            }
        }
    }

    void detachStatusBarFromNode(Node node)
    {
        // Err.pr("detachStatusBarFromNode( Node node) NOT TESTED");
        NodeStatusBarI nsb = nsbFromNode(node);
        statusBars.remove(nsb);
    }

    /**
     * May return nulls.
     * <p/>
     * Originally there was only going to be one NodeStatusBar
     * per Node, but as you can have many nodes on the one screen
     * this turns out not to be the case.
     */
    public NodeStatusBarI nsbFromNode(Node node)
    {
        /*
        * StatusBar use is set for all Nodes and is done as late
        * as possible.
        */
        if(useStatus == false)
        {
            for(Iterator it = statusBars.iterator(); it.hasNext();)
            {
                NodeSBAssociation nodeSBAssociation = (NodeSBAssociation) it.next();
                nodeSBAssociation.setNodeStatusBar(null);
            }
        }
        // Err.pr( "**  Have got " + statusBars.size() + " nodes");
        for(Iterator it = statusBars.iterator(); it.hasNext();)
        {
            NodeSBAssociation nodeSBAssociation = (NodeSBAssociation) it.next();
            // Err.pr( "**  got " + nodeSBAssociation.node);
        }
        /**/
        List<Node> nodesLookedAt = new ArrayList<Node>();
        for(Iterator it = statusBars.iterator(); it.hasNext();)
        {
            NodeSBAssociation nodeSBAssociation = (NodeSBAssociation) it.next();
            nodesLookedAt.add( nodeSBAssociation.node);
            if(node == nodeSBAssociation.node)
            {
                if(nodeSBAssociation.nsb == null)
                {
                    // Err.error( "When createNSB was called we cannot have had any nodes");
                    /*
                    * setStatusBarType has not been called so we use the default.
                    */
                    NodeStatusBarI bar = null;
                    try
                    {
                        bar = (NodeStatusBarI) OtherSignatures.getDefaultStatusBarType().newInstance();
                    }
                    catch(InstantiationException e)
                    {
                        Err.error(e);
                    }
                    catch(IllegalAccessException e)
                    {
                        Err.error(e);
                    }
                    nodeSBAssociation.setNodeStatusBar(bar);
                    if(nodeSBAssociation.node == null)
                    {
                        Err.error("DOES NOT HAPPEN - S/take last chance to have a node");
                    }
                }
                else if(nodeSBAssociation.node != nodeSBAssociation.nsb.getNode())
                {// Err.pr(" node " + nodeSBAssociation.node + " attached to " + nodeSBAssociation.nsb);
                }
                nodeSBAssociation.nsb.setName("NodeStatusBar for " + nodeSBAssociation.node.getName());
                return nodeSBAssociation.nsb;
            }
        }
        /**/
        Print.prList( nodesLookedAt, "All nodes looked at");
        Err.pr( "Can only get nodes from statusBars, and this is all the status bars:");
        Print.prSet( statusBars);
        Err.pr( "If none above then make sure you have set a statusBar property in the SdzBagI (often XML file)");
        Err.error("We don't yet know about node: " + node);
        /*
        if(nsb == null)
        {
        Err.error( "When don't know about a node (" +
        node + "), need to have a current NodeStatusBar");
        }
        Err.pr( "2 From " + node + " get " + nsb);
        */
        return null; // for compiler
    }

    /**
     * Associating a node with a NodeStatusBar
     */
    private class NodeSBAssociation
    {
        private Node node = null;
        private NodeStatusBarI nsb = null;

        private NodeSBAssociation(Node node)
        {
            this.node = node;
            // Old code didn't have
            // setNodeOfBar();
        }

        private void setNodeStatusBar(NodeStatusBarI nsb)
        {
            /*
            if(nsb == null)
            {
                Err.error("nsb == null");
            }
            */
            // Err.pr("\tFor " + node + " nsb is now " + nsb);
            this.nsb = nsb;
            // Old code didn't have
            // setNodeOfBar();
        }

        private void setNodeOfBar()
        {
            if(nsb != null && node != null)
            {
                nsb.setNode(node);
            }
        }
    }

    /**
     * Assuming that, from parsing the xml, setting the status bar,
     * which will call this, is being called after have set all the
     * nodes, which will set up all the status bars that we use here.
     * (in attachStatusBarToNode)
     * NO - could not make that assumption, thus going thru statusBars
     * and calling createNSB() where necessary.
     */
    public void setStatusBar(NodeStatusBarI bar)
    {
        barForType = bar;
        /*
        if(barForType != null)
        {
        Err.pr("barForType been set to " + barForType.getClass().getName());
        }
        else
        {
        Err.pr("barForType been set to NULL");
        }
        */
        for(Iterator iter = statusBars.iterator(); iter.hasNext();)
        {
            NodeSBAssociation nodeSBAssociation = (NodeSBAssociation) iter.next();
            if(nodeSBAssociation.nsb == null)
            {
                nodeSBAssociation.setNodeStatusBar(barForType);
            }
        }
    }

    public NodeStatusBarI getStatusBar()
    {
        return barForType;
    }

    private NodeStatusBarI createNSB()
    {
        NodeStatusBarI result;
        if(barForType != null)
        {
            result = barForType;
        }
        else
        {
            result = new NodeStatusBar();
            Err.stack( SdzNote.NO_HOUSING_HELPERS, 
                    "If Status Bar has not been set then in SdzBag then don't default it, yet have defaulted ID: " + 
                            ((NodeStatusBar)result).id);
        }
        return result;
    }

    private void nonApplicationRefitStatusArea(Node node)
    {
        if(!statusAreaRefitted)
        {
            if(partOfApplication)
            {
                Err.error(
                    "If part of an Application then this will be called from there");
            }
            nsb = nsbFromNode(node);
            // statusPane.refitStatusArea( (JComponent)nsb);
            if(nsb instanceof JComponent)
            {
                standardPanes.getStrandArea().getStatusArea().refitArea((JComponent) nsb);
            }
            refitStatusAreaDone = true;
            statusAreaRefitted = true;
        }
    }

    public AbstractStrandArea getStrandArea()
    {
        return standardPanes.getStrandArea();
    }

    public IdentifierI getNode()
    {
        return null;
    }
}
