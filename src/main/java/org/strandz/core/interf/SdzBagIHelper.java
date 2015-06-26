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
package org.strandz.core.interf;

import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Publisher;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.IconEnum;
import org.strandz.view.util.StrandArea;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class helps a SdzBagI (also known as a sbI) to do its job.
 * See <code>SdzBagI</code>
 * <p/>
 * Always has a NodeController (as the SdzBagI always has one),
 * but can encompass not having a physical representation
 * of it. For this effect init() with null
 *
 * @author Chris Murphy
 */
public class SdzBagIHelper
{
    private static final transient IconEnum CONTROLLER_ICON = IconEnum.BEAN_16;
    public final Strand strand = new Strand();
    private CopyPasteBuffer copyPasteBuffer = new CopyPasteBuffer();
    public NodeController nodeController = new NodeController();
    private List nodes = new ArrayList();
    private List panes = new ArrayList();
    private JPanel surface; // ControllerInterface imple
    // private ToolBarPane toolBarPane;
    // private StrandArea strandArea;
    private StandardPanes standardPanes;
    public String name;
    private String source;
    private boolean doneFitControlArea = false;
    private int currentPane = 0;
    private boolean currentPaneSet = false;
    private ControlAttributeReassigner mergerer = new ControlAttributeReassigner();
    private PanelUpdateInfo panelUpdateInfo = new PanelUpdateInfo();
    private static int constructedTimes;
    private int index;
    private static int times2;

    public IconEnum getIconEnum()
    {
        return CONTROLLER_ICON;
    }

    public String toShow()
    {
        return name;
    }

    public SdzBagIHelper()
    {
        constructedTimes++;
        index = constructedTimes;
        /*
        Err.pr( "+++++++++constructed SdzBagIHelper " + index);
        if(index == 0)
        {
        Err.stack();
        }
        */
    }

    /**
     * Where a ToolBarPane is not required (OneRowStrandControl), this
     * is called with a null toolBarPane.
     * surface arg is often SdzBag
     */
    public void init(JPanel surface, StandardPanes standardPanes)
    {
        if(standardPanes != null)
        {
            chkParams(surface, standardPanes.getStrandArea());
        }
        this.surface = surface;
        this.standardPanes = standardPanes;
        nodeController.setStrand(strand);
        // nodeController.setAllowActionsListener( this);
    }

    private void chkParams(JPanel surface, JPanel strandArea)
    {
        if(surface != null)
        {
            if(strandArea != null)
            {
                Err.error("strandArea must be null if using surface");
            }
        }
        else
        {
            if(strandArea == null)
            {
                Err.error("strandArea must not be null if not using surface");
            }
        }
    }

    /**
     * Not all implementations of SdzBagI need to have a StrandArea.
     * Those that don't provide a surface. For those that do we use the
     * enclosure. What exists depends on the constructor.
     *
     */
    private JComponent getUnknownSurface()
    {
        JComponent result = surface;
        if(standardPanes != null)
        {
            result = standardPanes.getStrandArea().getEnclosure();
        }
        return result;
    }

    private void fitControllerArea( ActualNodeControllerI actual)
    {
        if(!doneFitControlArea)
        {
            if(StrandArea.USE_TOOL_BAR_AREA)
            {
                Assert.notNull( standardPanes.getStrandArea(), "No strandArea");
                Assert.notNull( standardPanes.getStrandArea().getToolBarArea(), "StrandArea does not have a ToolBarArea");
                standardPanes.getStrandArea().getToolBarArea().refitArea(
                    (JComponent) actual);
            }
            else
            {
                standardPanes.getStrandArea().getToolBarPane().refitControllerArea(
                    (JComponent) actual);
            }
            doneFitControlArea = true;
            Err.pr( SdzNote.NO_HOUSING_HELPERS, "Done fitControllerArea() with " + actual.getClass().getName() +
                    " on " + strand);
            /*
            times++;
            if(times == 2)
            {
                Err.stack();
            }
            */
        }
        else 
        {
            Err.alarm( "Have already done fitControllerArea(), so PhysicalController was not added");
        }
    }

    public boolean validateBean()
    {
        return strand.validateBean();
    }

    public boolean validateBean(boolean childrenToo)
    {
        return strand.validateBean(childrenToo);
    }

    public void setName(String name)
    {
        this.name = name;
        strand.setName(name);
    }

    public Strand getStrand()
    {
        return strand;
    }

    public Node[] getNodes()
    {
        return (Node[]) nodes.toArray(new Node[0]);
    }

    public void setNodes(Node[] nodes)
    {
        for(int i = 0; i <= nodes.length - 1; i++)
        {
            Assert.notNull( nodes[i], "No node found at index " + i);
            nodes[i].setStrand(strand);
        }
        this.nodes.clear();
        this.nodes.addAll(Arrays.asList(nodes));
    }

    public Node getNode(int index)
    {
        return (Node) nodes.get(index);
    }

    public void setNode(int index, Node node)
    {
        node.setStrand(strand);
        if(nodes.size() == index)
        {
            nodes.add(index, node);
        }
        else
        {
            nodes.set(index, node);
        }
    }

    public boolean removeNode(Node node)
    {
        boolean ok = strand.getNodes().remove(node);
        if(ok)
        {
            ok = nodes.remove(node);
        }
        return ok;
    }

    private PanelUpdateInfo extraOpsToPane(
        JComponent pane,
        StrandI strand,
        JComponent oldPane,
        int position,
        boolean fromChild)
    {
        PanelUpdateInfo result = null;
        List lNodes = null;
        if(oldPane != null)
        {
            lNodes = SdzBagUtils.nodesAssociatedWithPane(nodes, oldPane);
            Print.prList(lNodes, "NOT GOT ORPHANS");
        }
        else
        {
            List debugList = SdzBagUtils.nodesAssociatedWithPane(nodes, pane);
            Err.pr(org.strandz.lgpl.note.SdzDsgnrNote.GONE_CONTROL, "This debugList to be ONLY for one pane");
            //
            // lNodes = debugList;
            lNodes = nodes;
            Err.pr(org.strandz.lgpl.note.SdzDsgnrNote.GONE_CONTROL, "Is this too many nodes?");
            Print.prList(lNodes, org.strandz.lgpl.note.SdzDsgnrNote.GONE_CONTROL.getName());
        }
        if(pane == null)
        {
            SdzBagUtils.updateAttributesWithNullPane(lNodes, oldPane, "extraOpsToPane");
        }
        else // if(oldPane != null)
        {
            boolean replacePane = (oldPane != null);
            result = SdzBagUtils.updateAttributesAndNodesWithNewPane(lNodes,
                oldPane, pane, replacePane);
        }
        if(result == null)
        {
            result = new PanelUpdateInfo(); // So that this method never returns null
        }
        if(!fromChild)
        {
            alterContained(oldPane, pane, position);
        }
        return result;
    }

    private void alterContained(
        JComponent oldPane, JComponent pane, int position)
    {
        Err.pr(SdzNote.GENERIC, "+++++++++++alterContained");

        JComponent unknownSurface = getUnknownSurface();
        /*
        Err.pr( "unknownSurface is a " + unknownSurface.getClass().getName());
        Err.pr( "name is " + unknownSurface.getName() + ", children are: ");
        Print.prArray( unknownSurface.getComponents());
        Err.pr( "parent is a " + unknownSurface.getParent().getClass().getName() + " called " + unknownSurface.getParent().getName());
        Err.pr( "parent's parent is a " + unknownSurface.getParent().getParent().getClass().getName() + " called " + unknownSurface.getParent().getParent().getName());
        */
        if(oldPane != null)
        {
            unknownSurface.remove(oldPane);
        }
        if(pane != null)
        {
            // if(!pUtils.savingFile)
            {
                if(currentPaneSet)
                {
                    if(getCurrentPane() == position)
                    {
                        /*
                        times++;
                        Err.pr( "+++++++++++Adding pane " + pane.getName() +
                        " because at position " + position + " for " + name + " (" + index + ") times " + times);
                        if(times == 0) // 6
                        {
                        Err.pr( "WOULD STACK");
                        }
                        */
                        unknownSurface.removeAll();
                        unknownSurface.add(pane, BorderLayout.CENTER);
                    }
                    else
                    {
                        unknownSurface.remove(pane);
                        /*
                        Err.pr( "+++++++++++Just removed pane " + pane.getName() +
                        " because at position " + position + " for " + name + " (" + index + ")");
                        */
                    }
                }
                else
                {
                    if(0 == position)
                    {
                        /*
                        times++;
                        Err.pr( "+++++NOT+++++Adding pane " + pane.getName() +
                        " because at position " + position + " for " + name + " (" + index + ") times " + times);
                        */
                        unknownSurface.removeAll();
                        unknownSurface.add(pane, BorderLayout.CENTER);
                        /*
                        if(times == 0) // 6
                        {
                        Err.pr( "WOULD STACK");
                        }
                        */
                    }
                    else
                    {
                        unknownSurface.remove(pane);
                        /*
                        Err.pr( "+++++NOT+++++Just removed pane " + pane.getName() +
                        " because at position " + position + " for " + name + " (" + index + ")");
                        */
                    }
                }
            }
        }
    }

    /**
     * Get the equivalent 'old' pane that this pane will be replacing. First we
     * see if a pane with the same name is one of the top level panels that the
     * user has expressly loaded.
     * If it is not then we loop again, this time looking at the children of these
     * panes. In this way we are supporting the replacement of any pane, not just
     * a pane which the user explicitly added as a guiClass on a Context.
     * <p/>
     * position param added back from legacy code later, but is only used for
     * when XML is driving the call to this method.
     *
     * @param name
     */
    private JComponent getOldPane(String name, int position)
    {
        times2++;
        Err.pr(
            "ooo In getOldPane() for " + name + " at postion " + position
                + " times " + times2);
        if(times2 == 25)
        {
            Err.debug();
        }

        JComponent result = null;
        if(mergerer.isNotXMLEncoderCall() || position >= panes.size())
        {
            for(Iterator iter = panes.iterator(); iter.hasNext();)
            {
                JComponent pane = (JComponent) iter.next();
                if(pane.getName().equals(name))
                {
                    result = pane;
                    break;
                }
            }
            Err.pr("ooo Did not find an equivalently named pane at top level");
            if(result == null)
            {
                for(Iterator iter = panes.iterator(); iter.hasNext();)
                {
                    JComponent pane = (JComponent) iter.next();
                    List children = SdzBagUtils.getChildrenPanels(pane);
                    for(Iterator iter2 = children.iterator(); iter2.hasNext();)
                    {
                        JComponent childPane = (JComponent) iter2.next();
                        if(childPane.getName().equals(name))
                        {
                            result = childPane;
                            break;
                        }
                    }
                }
                Err.pr(
                    "ooo Did not find an equivalently named pane when looking thru children");
            }
        }
        else
        {
            Err.pr("ooo posn >= panes.size so not looking at other panes");
        }
        if(result != null)
        {
            Err.pr(
                "ooo Old pane from " + name + " is " + result.getName() + " at "
                    + position + " size " + panes.size());
        }
        else
        {
            Err.pr(
                "ooo Got null old pane from " + name + " at " + position + " size "
                    + panes.size());
        }
        return result;
    }

    private PanelUpdateInfo extraOpsToPaneAndChildren(
        JComponent pane, StrandI strand, int position)
    {
        /*
        times1++;
        Err.alarm( "### extraOpsToPaneAndChildren() at " + position + " for " + pane.getName() + " times " + times1);
        if(times1 == 0)
        {
        Err.stack();
        }
        */
        PanelUpdateInfo result = extraOpsToPane(pane, strand, position, false);
        if(!result.isInError())
        {
            List children = SdzBagUtils.getChildrenPanels(pane);
            for(Iterator iter = children.iterator(); iter.hasNext();)
            {
                JComponent childPane = (JComponent) iter.next();
                Err.pr("Examining childPane: " + childPane);

                PanelUpdateInfo info = extraOpsToPane(childPane, strand, position, true);
                result.append(info);
                if(info.isInError())
                {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * When reading straight in from the xml file we don't have any
     * corresponding pane, so oldPane will always be null. We use this
     * fact to not 'set' the controls on the attributes. Thus this
     * method can now be used for both dynamic reading in [Update Bean]
     * and when reading straight in from the xml file. No matter how
     * doing, will get the same (as in equals) instance of PanelUpdateInfo.
     * In the case where we are reading from an xml file (well from the sbI
     * that came from the xml file), then we don't do as much work, as the
     * attributes are already set with controls.
     *
     * @param pane
     * @param strand
     * @param position
     */
    private PanelUpdateInfo extraOpsToPane(
        JComponent pane, StrandI strand, int position, boolean fromChild)
    {
        PanelUpdateInfo result = null;
        Err.pr("In extraOpsToPane with <" + pane.getName() + "> at " + position);

        // JComponent oldPane = getOldPane( position);
        JComponent oldPane = getOldPane(pane.getName(), position);
        result = extraOpsToPane(pane, strand, oldPane, position, fromChild);
        return result;
    }

    public JComponent[] getPanes()
    {
        // Err.pr( "------> Getting: " + panes.size() + " panes");
        // Causes InvocationTargetException when Err.error() due to a null pane
        // Print.prList( panes, "Calling getPanes() in " + name);
        return (JComponent[]) panes.toArray(new JComponent[panes.size()]);
    }

    // Bit convoluted, going to and fro array/list
    public void setPanes(JComponent[] panes)
    {
        // for(int i = 0; i <= panes.length - 1; i++)
        {
            /*
            Err.pr( SdzDsgnrBug.goneControl, "Setting paneS at " + i + " to " + panes[i].getName());
            PanelUpdateInfo info = extraOpsToPaneAndChildren( panes[i], strand, i);
            panelUpdateInfo.append( info);
            if(info.isInError())
            {
            //What to do if panelUpdateInfo happens twice? Prolly just
            //keep going. Everthing needs to be recorded, so have a list
            //of the error parts. Info parts no problem. Use copy constructor.
            Err.pr( "In error");
            break;
            }
            else
            */
            {
                // Err.pr( "Not in error");
                this.panes.clear();

                List list = Arrays.asList(panes);
                this.panes.addAll(list);
            }
        }
    }

    public JComponent getPane(int index)
    {
        // Err.pr( "About to getPane() from DebugList ID: " + ((DebugList)panes).id);
        return (JComponent) panes.get(index);
    }

    /**
     * This called upon [Update Bean] press
     *
     * @param newPanes
     */
    public PanelUpdateInfo setPanesReturnInfo(JComponent newPanes[], SdzBagI beanCI)
    {
        PanelUpdateInfo result = mergerer.setNotXMLEncoderCall(true, this.getPanes(), beanCI);
        if(!result.isInError())
        {
            result = mergerer.setNewPanes(newPanes);
            if(!result.isInError())
            {
                setPanes(newPanes);
            }
            else
            {
                Err.alarm( "Not able to set panes on SdzBag, 1");
            }
        }
        else
        {
            Err.alarm( "Not able to set panes on SdzBag, 2");
        }
        mergerer.setNotXMLEncoderCall(false);
        return result;
    }

    public void setPane(int index, JComponent pane)
    {
        newExtraOps(strand, pane, index);
        if(panes.size() == index)
        {
            panes.add(index, pane);
        }
        else
        {
            panes.set(index, pane);
        }
    }

    /*
    public void setPane( int index, JComponent pane)
    {
    if(!pBeans.isDesignTime())
    {
    PanelUpdateInfo info = setPane( index, pane, false);
    // Usually not used, so can comment this out if like, but good debugging aid!
    panelUpdateInfo.append( info);
    }
    else
    {
    PanelUpdateInfo info = setPane( index, pane, true);
    panelUpdateInfo.append( info);
    }
    }
    */

    /*
    private PanelUpdateInfo setPane( int index, JComponent pane, boolean returnIfError)
    {
    //PanelUpdateInfo result = extraOpsToPaneAndChildren( pane, strand, index);
    newExtraOps( strand, pane);
    if(!result.isInError())
    {
    if(panes.size() == index)
    {
    panes.add( index, pane);
    }
    else
    {
    panes.set( index, pane);
    }
    }
    return result;
    }
    */
    public boolean removePane(JComponent pane)
    {
        int index = indexOfPane(pane);
        boolean ok = panes.remove(pane);
        if(ok)
        {
            // extraOpsToPane( null, strand, pane, -99, false);
            newExtraOps(strand, pane, -99);
        }
        if(ok && index == getCurrentPane())
        {
            setCurrentPane(-1);
        }
        return ok;
    }

    private void newExtraOps(StrandI strand, JComponent pane, int position)
    {
        alterContained(null, pane, position);
    }

    public int indexOfPane(JComponent pane)
    {
        return panes.indexOf(pane);
    }

    public void addTransactionTrigger(CloseTransactionTrigger listener)
    {
        strand.addTransactionTrigger(listener);
    }

    public void removeTransactionTrigger(CloseTransactionTrigger listener)
    {
        strand.removeTransactionTrigger(listener);
    }

    public Publisher getTransactionTriggers()
    {
        return strand.getTransactionTriggers();
    }

    public void setPhysicalController(ActualNodeControllerI actual)
    {
        nodeController.setPhysicalController(actual);
        fitControllerArea( actual);
    }

    public ActualNodeControllerI getPhysicalController()
    {
        return nodeController.getPhysicalController();
    }

    public void addStateChangeListener(StateChangeTrigger listener)
    {
        strand.addStateChangeTrigger(listener);
    }

    public void removeStateChangeListener(StateChangeTrigger listener)
    {
        strand.removeStateChangeTrigger(listener);
    }

    public void copyItemValues()
    {
        List nodes = strand.getNodes();
        copyItemValues(nodes);
    }

    public void copyItemValues(Node node)
    {
        List nodes = new ArrayList();
        nodes.add(node);
        copyItemValues(nodes);
    }

    public void copyItemValues(List nodes)
    {
        Node node = strand.getCurrentNode();
        List attribs = SdzBagUtils.getAllAttributes(nodes);
        copyPasteBuffer.copyItemValues(attribs, node);
    }

    public void pasteItemValues()
    {
        copyPasteBuffer.pasteItemValues();
    }

    public CopyPasteBuffer getCopyPasteBuffer()
    {
        return copyPasteBuffer;
    }

    public String toString()
    {
        String result = null;
        if(!nodes.isEmpty())
        {
            result = "[";
            for(Iterator iter = nodes.iterator(); iter.hasNext();)
            {
                Node node = (Node) iter.next();
                result += (node + ",");
            }
            result += "]";
            for(Iterator iter = panes.iterator(); iter.hasNext();)
            {
                result += "[";

                Component comp = (Component) iter.next();
                result += (comp.getName() + ",");
            }
            result += "]";
        }
        else
        {
            result = name;
        }
        if(Utils.isBlank(result))
        {
            result = "NO NAME SdzBagI";
            // Err.error( "Cannot have a nameless SdzBagI");
        }
        return result;
    }

    public void setCurrentPane(int index)
    {
        int oldIndex = currentPane;
        currentPane = index;
        currentPaneSet = true;
        if(!BeansUtils.isDesignTime())
        {
            Err.pr(SdzNote.CURRENT_PANE,
                "At Runtime, about to setCurrentPane() to " + currentPane + " for " + name +
                    ", oldIndex was " + oldIndex);
            Assert.isTrue( oldIndex >= 0, "When setCurrentPane(), oldIndex cannot be " + oldIndex);
            //Assert.isTrue( currentPane >= 0, "When setCurrentPane(), cannot set it to " + currentPane);
            if(currentPane != -1)
            {
                alterContained(getPane(oldIndex), getPane(currentPane), currentPane);
                getPane(currentPane).repaint();
            }
            else
            {
                alterContained(getPane(oldIndex), null, Utils.UNSET_INT);
            }
        }
         /**/
        Err.pr(SdzNote.CURRENT_PANE,
            "currentPane set to " + index + " for " + name + " ("
                + this.index + ")");
        /*
        int i=0;
        for (Iterator iter = panes.iterator(); iter.hasNext(); i++)
        {
        JComponent pane = (JComponent)panes.get( index);
        extraOpsToPane( pane, strand, i);
        }
        */
        /*
        * name is not a BeanInfo property so will be null when as part of putting
        * a new - SdzBago xml file a new SdzBag created for comparison purposes.
        *
        if(name == null)
        {
        Err.error( "SdzBagIHelper has no name");
        }
        */
        /*
        if(name.equals( "Clients") && index == 1)
        {
        Err.stack();
        }
        */
    }

    public int getCurrentPane()
    {
        Err.pr(SdzNote.CURRENT_PANE, "+++++++++++getCurrentPane() to return " + currentPane + " for " + name + " (" + index + ")");
        return currentPane;
    }

    public void setCurrentPaneStr(String s)
    {
        int i = 0;
        for(Iterator iter = panes.iterator(); iter.hasNext(); i++)
        {
            JComponent pane = (JComponent) iter.next();
            if(pane.getName().equals(s))
            {
                setCurrentPane(i);
            }
        }
        // setCurrentPane( panes.indexOf( s));
    }

    public String getCurrentPaneStr()
    {
        String result = null;
        if(panes.size() > 0)
        {
            if(getCurrentPane() != -1)
            {
                result = ((JComponent) panes.get(getCurrentPane())).getName();
            }
        }
        return result;
    }

    public PanelUpdateInfo getPanelUpdateInfo()
    {
        return panelUpdateInfo;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        if(source == null || source.equals(""))
        {
            Err.error("Should not intentionally set the CIs source blank");
        }
        this.source = source;
    }
}
