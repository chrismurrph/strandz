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

import org.strandz.core.domain.ControlSignatures;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.DialogEmbellisherI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.widgets.CustomDialog;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.beans.PropertyChangeListener;

/**
 * This class looks complex and we want to move it to another location
 *
 * @author Chris Murphy
 */
public class ControlAttributeReassigner
{
    private boolean notXMLEncoderCall = false;
    private PanelUpdateInfo panelUpdateInfo;
    private List<ControlReferencer> controlReferencers = new ArrayList<ControlReferencer>();
    //private JComponent[] oldPaneFlatList;
    private JComponent[] newPaneFlatList;
    private List allNodes;
    private static int times;

    public boolean isNotXMLEncoderCall()
    {
        return notXMLEncoderCall;
    }

    public void setNotXMLEncoderCall(boolean b)
    {
        setNotXMLEncoderCall(b, null, null);
    }

    public PanelUpdateInfo setNotXMLEncoderCall(boolean b, JComponent[] panes, SdzBagI beanCI)
    {
        panelUpdateInfo = new PanelUpdateInfo();
        if(b)
        {
            setupControlReferencers( beanCI);
            if(!panelUpdateInfo.isInError())
            {
                // oldPaneFlatList = pPaneControlsUtils.getFlatPanels( panes);
                //oldPaneFlatList = panes;
                findNewControlsToIntroduce(panes, "ControlAttributeReassigner.findNewControlsToIntroduce() from setNotXMLEncoderCall()");
            }
        }
        else if(panes != null)
        {
            Err.error("Setting to false is just supposed to be setting");
        }
        this.notXMLEncoderCall = b;
        return panelUpdateInfo;
    }

    /**
     * First call setNotXMLEncoderCall(), then load the new panes into memory,
     * then receive them here.
     *
     * @param newPanes
     * @return PanelUpdateInfo - an error messages container
     */
    public PanelUpdateInfo setNewPanes(JComponent[] newPanes)
    {
        if(!notXMLEncoderCall)
        {
            Err.error("setNewPanes() is being called at wrong time");
        }
        Utils.chkNoNulls(newPanes, "setNewPanes()");
        introduceNewPanes(newPanes);
        return panelUpdateInfo;
    }

    private List findNewControlsToIntroduce(JComponent[] paneFlatList, String id)
    {
        List result = new ArrayList();
        if(SdzNote.REASSIGN_CONTROL.isVisible())
        {
            Print.prArray(paneFlatList, id);
        }
        for(int i = 0; i < paneFlatList.length; i++)
        {
            JComponent container = paneFlatList[i];
            String paneContainerName = container.getName();
            if(Utils.isBlank(paneContainerName))
            {
                panelUpdateInfo.setError(
                    "Container of type " + container.getClass().getName()
                        + " has not been named");
                break;
            }
            else
            {
                List leaves = SdzBagUtils.collectViewControls(new ArrayList(),
                    container);
                if(SdzNote.REASSIGN_CONTROL.isVisible())
                {
                    Print.prList( leaves, "Called collectViewControls() with " + container);
                }
                for(Iterator iter1 = leaves.iterator(); iter1.hasNext();)
                {
                    Component comp = (Component) iter1.next();
                    String leafName = comp.getName();
                    if(Utils.isBlank(leafName))
                    {
                        String msg = "Component of type <" + comp.getClass().getName() + "> in <"
                                + paneContainerName + ">, who has parent <" + comp.getParent().getName() + "> has not been named"; 
                        panelUpdateInfo.setError( msg);
                        Err.error( msg); //Won't be ignored!
                        break;
                    }
                    else
                    {
                        result.add(comp);
                    }
                }
                if(panelUpdateInfo.isInError()) // need to break out of 2nd loop too
                {
                    break;
                }
            }
        }
        return result;
    }

    private void setupControlReferencers( SdzBagI beanCI)
    {
        controlReferencers.clear();
        allNodes = beanCI.getStrand().getNodes();
        if(allNodes == null)
        {
            Err.error("Nodes returned should at least be an empty list");
        }

        List allAttributes = SdzBagUtils.getAllAttributes(allNodes);
        for(Iterator iter = allAttributes.iterator(); iter.hasNext();)
        {
            RuntimeAttribute attr = (RuntimeAttribute) iter.next();
            if(ControlReferencer.isAttributeSuitable(attr))
            {
                ControlReferencer referencer = new ControlReferencer(panelUpdateInfo,
                    attr);
                if(panelUpdateInfo.isInError())
                {
                    break;
                }
                controlReferencers.add(referencer);
            }
        }
    }

    private void introduceNewPanes(JComponent[] panes)
    {
        // newPaneFlatList = pPaneControlsUtils.getFlatPanels( panes);
        newPaneFlatList = panes;

        // pUtils.chkNoNulls( newPaneFlatList);
        // pUtils.chkNoInstanceDups( newPaneFlatList);
        List newLeaves = findNewControlsToIntroduce( newPaneFlatList, "ControlAttributeReassigner.findNewControlsToIntroduce() from introduceNewPanes()");
        if(SdzNote.REASSIGN_CONTROL.isVisible())
        {
            Print.prList( newLeaves, "All controls would expect here?");
        }
        // pUtils.chkNoInstanceDups( newLeaves);
        performReAssignment( newLeaves);
    }

    /**
     * Everything about the original control with the ability to set a new
     * control in place of the existing one.
     */
    private static class ControlReferencer
    {
        private PanelUpdateInfo panelUpdateInfo;
        private String oldControlName;
        private String oldParentPanelName;
        private RuntimeAttribute oldAttribute;
        private Attribute newAttribute;
        private Node node;
        private int timesSetControl;
        // private String newControlName;
        // private String newParentPanelName;
        
        ControlReferencer(PanelUpdateInfo panelUpdateInfo, RuntimeAttribute attribute)
        {
            this.panelUpdateInfo = panelUpdateInfo;
            if(Utils.instanceOf(attribute, TableAttribute.class))
            {
                Node node = (Node) ((TableAttribute) attribute).getCell().getNode();
                Component table = (Component) node.getTableControl();
                oldControlName = table.getName();
                if(Utils.isBlank(oldControlName))
                {
                    panelUpdateInfo.setError(
                        "Attribute " + attribute
                            + " has a table control with no name, but is of type "
                            + table.getClass().getName());
                }
                else
                {
                    if(getParent(table) == null)
                    {
                        panelUpdateInfo.addInfo(
                            "Attribute " + attribute + " has a table control called "
                                + table.getName() + " that is an orphan");
                    }
                    else
                    {
                        oldParentPanelName = getParent(table).getName();
                        if(Utils.isBlank(oldParentPanelName))
                        {
                            panelUpdateInfo.setError(
                                "Attribute " + attribute
                                    + " has a table control whose parent has no name, but is of type "
                                    + getParent(table).getClass().getName());
                        }
                        else
                        {
                            /* Too much info for user
                            panelUpdateInfo.addInfo(
                                "Attribute <" + attribute.getName()
                                    + "> originally referred to <" + oldControlName + "> in <"
                                    + oldParentPanelName + ">");
                            */
                            this.node = node;
                        }
                    }
                }
            }
            else
            {
                Object item = attribute.getItem();
                Component comp = (Component) item;
                oldControlName = comp.getName();
                if(Utils.isBlank(oldControlName))
                {
                    panelUpdateInfo.setError(
                        "Attribute " + attribute
                            + " has a control with no name, but is of type "
                            + comp.getClass().getName());
                }
                else
                {
                    if(getParent(comp) == null)
                    {
                        panelUpdateInfo.addInfo(
                            "Attribute " + attribute + " has an orphan control <"
                                + comp.getClass().getName() + "> named <" + oldControlName + ">");
                    }
                    else
                    {
                        oldParentPanelName = getParent(comp).getName();
                        if(Utils.isBlank(oldParentPanelName))
                        {
                            panelUpdateInfo.setError(
                                "Attribute " + attribute
                                    + " has a control whose parent has no name, but is of type "
                                    + getParent(comp).getClass().getName());
                        }
                        else
                        {
                            /* Too much info for user
                            panelUpdateInfo.addInfo(
                                "Attribute <" + attribute.getName()
                                    + "> originally referred to <" + oldControlName + "> in <"
                                    + oldParentPanelName + ">");
                            */        
                        }
                    }
                }
            }
            if(!panelUpdateInfo.isInError())
            {
                this.oldAttribute = attribute;
                timesSetControl = 0;
            }
        }

        public String toString()
        {
            String result = oldControlName + " in " + oldParentPanelName;
            return result;
        }

        /**
         * Only collect attributes that are attached to a control. This
         * basically gets rid of attributes that have a Comp as their item,
         * but makes sure that all TableAttributes are included.
         */
        static boolean isAttributeSuitable(RuntimeAttribute attribute)
        {
            boolean result = true;
            Object item = attribute.getItem();
            if(!Utils.instanceOf(item, Component.class))
            {
                result = false;
            }
            if(!result && Utils.instanceOf(attribute, TableAttribute.class))
            {
                Node node = (Node) ((TableAttribute) attribute).getCell().getNode();
                Object table = node.getTableControl();
                if(table == null)
                {
                    Err.error("S/not actually have a TableAttribute without a table");
                }
                else
                {
                    // Originally marked as unsuitable because it didn't have an item, this
                    // has been changed to be suitable because it is a TableAttribute
                    // with a table.
                    result = true;
                }
            }
            return result;
        }

        private void setControlOnAttribute(Component comp, String sameParentName)
        {
            timesSetControl++;
            if(timesSetControl > 1)
            {
                String msg1 = "Two parent names the same <" + sameParentName + "> and trying to assign an orphan for the "
                    + timesSetControl + " time";
                Err.alarm(msg1);
                panelUpdateInfo.addInfo(msg1);
                String msg2 = "Onto <" + oldAttribute + "> putting <" + comp.getName() + ">"; 
                Err.alarm(msg2);
                panelUpdateInfo.addInfo( msg2);
            }
            if(!ControlSignatures.isFieldControl(comp.getClass()))
            {
                /*
                 * Property change stuff is to remove effect of changing all the attributes
                 * to be table attributes
                 */
                if(node != null)
                {
                    PropertyChangeListener listeners[] = node.getPropertyChangeListeners( "tableControl");
                    Assert.isTrue( listeners.length == 1);
                    node.removePropertyChangeListener( "tableControl", listeners[0]);
                    node.setTableControl( comp); //Will be assigned many times but that's ok
                    node.addPropertyChangeListener("tableControl", listeners[0]);
                }
                newAttribute = oldAttribute;
            }
            else
            {
                FieldAttribute fa = (FieldAttribute) Attribute.setControlOnAttribute(comp,
                    oldAttribute);
                newAttribute = fa;
            }
        }

        public void setNoControlOnAttribute( String reason)
        {
            StemAttribute sa = Attribute.toStemAttribute( oldAttribute, reason);
            String msg = "Are having to turn an oldAttribute into a StemAttribute b/c name didn't match: " + oldAttribute;
            Err.alarm(msg);
            panelUpdateInfo.addInfo(msg);
            newAttribute = sa;
        }

        boolean wasAssignedANewControl()
        {
            return (timesSetControl >= 1);
        }
    }

    /**
     * Do a loop on all the new controls that have just collected. Inner loop
     * on all the ControlReferencers. If there is a match for both name and
     * parent name, then to the attribute in the ControlReferencer we put the
     * new control (and note that we have done so in the ControlReferencer).
     * For a table we will set the control on the node rather than the
     * attribute - and it may be set loads of times but that is not a problem.
     * Next we go through all the ControlReferencers that have not had an
     * assignment done for them, for these attributes will now have to be
     * turned into StemAttributes.
     *
     * @return PanelUpdateInfo - an error messages container
     */
    private PanelUpdateInfo performReAssignment(List newControls)
    {
        if(newControls.isEmpty())
        {
            panelUpdateInfo.addInfo("There are no new controls");
        }
        else
        {
            /* Too much info for user
            panelUpdateInfo.addInfo(
                Print.getPrList(newControls, "newControls", new ArrayList()));
            */    
        }
        if(controlReferencers.isEmpty())
        {
            panelUpdateInfo.addInfo("There are no controlReferencers");
        }
        else
        {
            /* Too much info for user
            panelUpdateInfo.addInfo(
                Print.getPrList(controlReferencers, "ControlReferencers",
                    new ArrayList()));
            */        
        }
        for(Iterator iter = newControls.iterator(); iter.hasNext();)
        {
            Component newComp = (Component) iter.next();
            String newName = newComp.getName();
            String newParentName = getParent(newComp).getName();
            /*
             * In the case where newComp is a Table style control this loop
             * will not break out but will instead set the control on every
             * attribute 
             */
            for(Iterator iter1 = controlReferencers.iterator(); iter1.hasNext();)
            {
                ControlReferencer referencer = (ControlReferencer) iter1.next();
                times++;
                Err.pr(SdzNote.REASSIGN_CONTROL, "Cfing <" + referencer.oldControlName + "> with <" + newName + "> times " + times);
                if(times == 0 || newName.equals( "tfPostcode"))
                {
                    Err.debug();
                }
                if(referencer.oldControlName.equals(newName))
                {
                    // referencer.oldParentPanelName can be null for orphans
                    if(referencer.oldParentPanelName == null || newParentName.equals(referencer.oldParentPanelName))
                    {
                        referencer.setControlOnAttribute(newComp, referencer.oldParentPanelName);
                        if(ControlSignatures.isFieldControl(newComp.getClass()))
                        {
                            break;
                        }
                    }
                    else
                    {
                        /*
                         * Now we get interactive with the user for controls that have exactly the
                         * same name, but didn't pass the parenthood tests above. Names not always
                         * unique so we have to ask ... 
                         */
                        showSimpleMatchDialog( newComp, referencer);
                        if(ControlSignatures.isFieldControl(newComp.getClass()))
                        {
                            break; //whether the user decides to match or not we are finished with that control
                        }
                    }
                }
            }
        }
        /*
         * Any Referencers that were not able to be matched to the new set of controls
         * can only be made into StemAttributes.
         */
        for(Iterator iter1 = controlReferencers.iterator(); iter1.hasNext();)
        {
            ControlReferencer referencer = (ControlReferencer) iter1.next();
            if(!referencer.wasAssignedANewControl())
            {
                referencer.setNoControlOnAttribute( "Was not able to be matched against any of the new set of controls");
            }
        }
        /*
        * For every node look at all the attributes of the type TableAttribute. If
        * don't find any and the node has a table control, then get rid of it. (For
        * tables it is not enough just to convert the type of the attribute).
        */
        for(Iterator iter2 = allNodes.iterator(); iter2.hasNext();)
        {
            Node node = (Node) iter2.next();
            for(Iterator iter1 = node.getCells().iterator(); iter1.hasNext();)
            {
                Cell cell = (Cell) iter1.next();
                List tableList = Utils.getSubList(cell.getAllAttributes(),
                    TableAttribute.class);
                if(tableList.isEmpty() && node.getTableControl() != null)
                {
                    node.setTableControl(null);
                    panelUpdateInfo.addInfo(
                        "Node <" + node.getName()
                            + "> no longer needed a table control, as it no longer has any table attributes");
                }
            }
        }
        allNodes = null;
        return panelUpdateInfo;
    }

    private void showSimpleMatchDialog( Component newComp, ControlReferencer referencer)
    {
        SimpleMatchDialoger simpleMatchDialoger = new SimpleMatchDialoger( newComp, referencer);
        CustomDialog dialog = new CustomDialog
            (MessageDlg.getFrame(), simpleMatchDialoger, "New Control match to existing Attribute",
                "Do you want to bind the new control named <" + newComp.getName() + "> to attribute \n<" + 
                        referencer.oldAttribute + ">", null, 40,
                new String[]{"Yes", "No"}, null, null);
        simpleMatchDialoger.setDialog( dialog);
        MessageDlg.setFrame( new JFrame());
        dialog.setUseOkButton( false);
        dialog.setUseCancelButton( false);
        dialog.setUseTextField( false);
        dialog.init();
        dialog.pack();
        dialog.setLocationRelativeTo(MessageDlg.getFrame());
        dialog.setVisible( true);
    }
    
    private static class SimpleMatchDialoger implements DialogEmbellisherI
    {
        Component newComp; 
        ControlReferencer referencer;
        Dialog dialog;
        
        public SimpleMatchDialoger( Component newComp, ControlReferencer referencer)
        {
            this.newComp = newComp;
            this.referencer = referencer;
        }

        public void setDialog(Dialog dialog)
        {
            this.dialog = dialog;
        }

        public boolean validate(String txt)
        {
            return true;
        }

        public void actionPerformed(ActionEvent e)
        {
            String pressed = e.getActionCommand();
            if(pressed.equals( "Yes"))
            {
                referencer.setControlOnAttribute(newComp, "NO NAME");
            }
            dialog.setVisible( false);
        }
    }

    private static Component getParent(Component comp)
    {
        if(comp == null)
        {
            Err.error("Cannot get the parent of a null component");
        }

        Component result = comp;
        while(true)
        {
            result = result.getParent();
            if(result == null)
            {
                break;
            }
            if(!ControlSignatures.isLookThruControl(result.getClass()))
            {
                break;
            }
        }
        return result;
    }
}
