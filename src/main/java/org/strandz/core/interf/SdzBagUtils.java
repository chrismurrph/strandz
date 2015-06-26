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
import org.strandz.core.domain.TableSignatures;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class
 *
 * @author Chris Murphy
 */
public class SdzBagUtils
{
    private static int times;

    /**
     * This method required to purge references to old pane where
     * are in situation where have two instances of a container in
     * memory. To write the xml file, all references need to be pointing
     * to the same pane!
     * <p/>
     * By references we mean attribute references to field type controls
     * as well as node references to tables.
     * <p/>
     * Method used here simplistic - make sure same number of flattened
     * out controls with same types. If fails then nullify all controls
     * and user will have to do the mapping again. Should get warning of
     * this.
     * <p/>
     * More sophisticated method would be to get a containership stack
     * for all leaf-controls in each container. Each element in stack to be
     * made up of the clazz and an ordinal. ie. 3rd container object at
     * 2nd level down is a JPanel. 1st at 3rd level down is a JTextField.
     * Definition of leaf-control will require use of terminatingControls
     * etc. With this method could implement things like "if have added a
     * component to a panel then only re-do the mapping for that panel".
     * <p/>
     * ABOVE considered too complicated. Better to have a parser which
     * will setName() of every control to the name of its variable. Then
     * here we can match on class type and name.
     * <p/>
     * Is now also used for validation where the attributes controls do
     * not need to be set, hence the set param. Will be called on the sbI
     * just after it has been read in from the xml file.
     */
    public static PanelUpdateInfo updateAttributesAndNodesWithNewPane(
        List nodes,
        JComponent oldPane,
        JComponent newPane,
        boolean replacePane)
    {
        PanelUpdateInfo result = new PanelUpdateInfo();
        boolean nullifyControls = false;
        // Err.pr( "oldPane: " + oldPane.getName());
        if(oldPane != null)
        {
            result.addInfo("oldPane: " + oldPane.getName());
        }
        result.addInfo("newPane: " + newPane.getName());
        if(replacePane)
        {
            /*
            * Since we are replacing the pane, then we have to get rid of any
            * references (in a node) to any tables in the old pane.
            */
            /*
            //nullifyTableControls( nodes, oldControls);
            //nullifyAttributesControls( nodes, oldControls);
            * These three together:
            */
            // updateAttributesWithNullPane( nodes, oldPane);
            List oldControls = ComponentUtils.getAllControls(oldPane);
            // This call only affects the nodes:
            nullifyTableControls(nodes, oldControls);
            Err.debug();
        }

        /*
        * This is a very normal thing to do when [Update Bean] has been pressed, as
        * it should be after the .class file has been recompiled, and we want the
        * new panel to be integrated into the SdzBagI/XML file.
        *
        if(oldPane.getName().equals( newPane.getName()))
        {
        Err.error( "Why asking to update attributes of the same pane: " + newPane.getName());
        }
        */
        // pUtils.checkParentage( oldControls);
        List newControls = null;
        if(newPane == null)
        {
            Err.error("Should call updateAttributesWithNullPane() instead");
        }
        else
        {
            result.addInfo("newPane: " + newPane.getName());
            newControls = ComponentUtils.getAllControls(newPane);
            // Print.prList( newControls);
        }
        for(Iterator iter = getAllAttributes(nodes).iterator(); iter.hasNext();)
        {
            Attribute attribute = (Attribute) iter.next();
            Component control = null;
            if(attribute instanceof FieldAttribute)
            {
                FieldAttribute fieldAttribute = (FieldAttribute) attribute;
                control = (Component) fieldAttribute.getItem();
                if(control != null)
                {
                    int index = -1;
                    if(ControlSignatures.isFieldControl(control.getClass()))
                    {
                        index = indexByNameExcludePrefix(newControls, control);
                        if(index == -1)
                        {
                            result.addInfo(
                                "NOT found index for " + control.getClass() + ", "
                                    + control.getName());
                            // Convert to a StemAttribute as this control is no longer
                            // found in the new panel, and we don't want to keep a
                            // reference to an orphan.
                            if(replacePane)
                            {
                                Attribute.toStemAttribute( fieldAttribute, 
                                "Old control cannot be name matched with any of the new controls");
                            }
                        }
                    }
                    else
                    {
                        result.addInfo(
                            "Disgarding old control: " + control.getClass() + ", "
                                + control.getName());
                    }
                    if(index != -1)
                    {
                        Component newControl = (Component) newControls.get(index);
                        if(ControlSignatures.isFieldControl(newControl.getClass()))
                        {
                            // Err.pr( "Examining " + newControl.getClass() + ", " + newControl.getName());
                            /*
                            * Thought would Err.pr( Bug.cantChangeControlType ... - but commented out
                            * instead, as labels can be used as fields
                            if(newControl instanceof JLabel)
                            {
                            Err.error( "Got JLabel from a " + control.getClass() + " called " + control.getName());
                            }
                            */
                            if(replacePane)
                            {
                                fieldAttribute.setItem(newControl);
                            }
                        }
                        else
                        {
                            result.addInfo(
                                "Disgarding new control: " + newControl.getClass() + ", "
                                    + newControl.getName());
                        }
                    }
                    else
                    {
                        String msg = "Control with name " + control.getName()
                            + " does not have an equivalently named control in "
                            + "the new panel, <" + newPane.getName() + ">";
                        Err.pr(org.strandz.lgpl.note.SdzDsgnrNote.GONE_CONTROL, msg);
                        result.addInfo(msg);
                        // Now doing dynamically, see PanelCompatibleIconService
                        // fieldAttribute.setNoLongerMatching( true);
                    }
                }
                /*
                else
                {
                Err.error( "Field Attribute without a control: " + fieldAttribute);
                }
                */
                if(fieldAttribute.getItem() == null)
                {
                    result.setError(
                        "Did we have to discard the control for <" + fieldAttribute
                            + "> ?");
                    break;
                }
            }
        }
        changedTableControl(nodes, /* oldControls,*/newControls, result,
            newPane.getName(), replacePane);
        // Err.pr( "$$$   Did not need to nullify controls (done reset)");
        return result;
    }

    private static void nullifyTableControls(List nodes, List oldControls)
    {
        for(Iterator allNodesIt = nodes.iterator(); allNodesIt.hasNext();)
        {
            Node node = (Node) allNodesIt.next();
            int index = oldControls.indexOf(node.getTableControl());
            if(index != -1)
            {
                node.setTableControl(null);
            }
        }
    }

    /**
     * May get a situation in the future where a node straddles across
     * more than one pane. When happens will need a slight re-write of
     * this method/pPaneControlsUtils methods. For now use this cop-out
     * that the first link that can find to a pane (either thru a
     * FieldAttribute or a Node's table) will be the node to that pane.
     */
    public static List nodesAssociatedWithPane(List nodes, JComponent pane)
    {
        List result = new ArrayList();
        for(Iterator iter = nodes.iterator(); iter.hasNext();)
        {
            Node node = (Node) iter.next();
            Component tableControl = (Component) node.getTableControl();
            if(tableControl != null)
            {
                if(ComponentUtils.controlIsInContainer(tableControl, pane))
                {
                    result.add(node);
                }
            }
        }
        for(Iterator iter = getAllAttributes(nodes).iterator(); iter.hasNext();)
        {
            Attribute attribute = (Attribute) iter.next();
            if(Utils.instanceOf(attribute, FieldAttribute.class))
            {
                Node node = (Node) attribute.getCell().getNode();
                FieldAttribute fieldAttribute = (FieldAttribute) attribute;
                Component control = (Component) fieldAttribute.getItem();
                if(ComponentUtils.controlIsInContainer(control, pane))
                {
                    if(!result.contains(node))
                    {
                        result.add(node);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Find every node that contains a table control. Get the table controls name and use
     * this name to get the new table control. The new table control can now be attached
     * to the node.
     *
     * @param nodes
     * @param newControls
     */
    private static void changedTableControl(
        List nodes,
        List newControls,
        PanelUpdateInfo info,
        String newPanelName,
        boolean replacePane)
    {
        /*
        times++;
        Err.pr( "In changedTableControl times " + times);
        if(times == 2)
        {
        Err.debug();
        }
        */
        for(Iterator iter = nodes.iterator(); iter.hasNext();)
        {
            Node node = (Node) iter.next();
            Component tableControl = (Component) node.getTableControl();
            if(tableControl != null)
            {
                int index = indexByNameExcludePrefix(newControls, tableControl);
                if(index == -1)
                {
                    info.addInfo(
                        "Could not find a table component inside "
                            + tableControl.getName() + ", a "
                            + tableControl.getClass().getName()
                            + " which contains these components:");

                    List list = Print.getPrList(newControls,
                        "SdzBagUtils.changedTableControl()", new ArrayList());
                    list.add("The name of the panel we are looking at is " + newPanelName);
                    info.addInfo(list);
                    break;
                }
                else if(replacePane)
                {
                    Component newControl = (Component) newControls.get(index);
                    node.setTableControl(newControl);
                }
            }
            else
            {// Err.pr( "/// control null");
            }
        }
    }

    public static List getAllAttributes(List nodes)
    {
        List result = new ArrayList();
        for(Iterator iter = nodes.iterator(); iter.hasNext();)
        {
            Node node = (Node) iter.next();
            for(Iterator iter1 = node.getCells().iterator(); iter1.hasNext();)
            {
                Cell cell = (Cell) iter1.next();
                List subList = Utils.getSubList(cell.getAttributes(),
                    RuntimeAttribute.class);
                for(Iterator iter2 = subList.iterator(); iter2.hasNext();)
                {
                    RuntimeAttribute attribute = (RuntimeAttribute) iter2.next();
                    result.add(attribute);
                }
            }
        }
        return result;
    }

    public static void updateAttributesWithNullPane(List nodes, Container oldPane, String reason)
    {
        // Err.pr( "oldPane: " + oldPane.hashCode());
        List oldControls = ComponentUtils.getAllControls(oldPane);
        /*
        * Currently this will give an error, but it is too extreme
        * a thing to do anyway.
        */
        // Err.pr( "$$$   Will nullify controls (EXPECT ERROR!!)");
        nullifyAttributesControls(nodes, oldControls, reason);
        nullifyTableControls(nodes, oldControls);
        // Err.pr( "$$$   Have nullified controls");
    }

    private static void nullifyAttributesControls(List nodes, List oldControls, String reason)
    {
        for(Iterator iter = getAllAttributes(nodes).iterator(); iter.hasNext();)
        {
            Attribute attribute = (Attribute) iter.next();
            if(attribute instanceof FieldAttribute)
            {
                FieldAttribute fieldAttribute = (FieldAttribute) attribute;
                int index = oldControls.indexOf(fieldAttribute.getItem());
                if(index != -1)
                {
                    /*
                    Err.pr(
                    "------------To turn " + fieldAttribute + " into a StemAttribute");
                    */
                    Attribute.toStemAttribute(fieldAttribute, reason);
                }
            }
        }
        // Err.pr( "Sanity Check: To check " + allNodesIt + " nodes");
    }

     /**/
    public static int indexByNameExcludePrefix(List controls, Component control)
    {
        int result = controls.indexOf(control);
        /*
        if(control.getName().equals( "tfFirstShift"))
        {
        Err.debug();
        }
        */
        if(result == -1)
        {
            int i = 0;
            for(Iterator iter = controls.iterator(); iter.hasNext(); i++)
            {
                Component element = (Component) iter.next();
                if(ControlSignatures.isFieldControl(element.getClass()))
                {
                    if(!TableSignatures.isTableControl(control.getClass())
                        && element.getName() != null
                        && /*
               * May want to doctor the xml file if have changed the type
               * of a control and want to make the name reflect the type
               * again.
               */// element.getClass() == control.getClass() &&
                        ControlSignatures.getNameExcludeRequiredPrefixForControl(element).equals(
                            ControlSignatures.getNameExcludeRequiredPrefixForControl(
                                control)))
                    {
                        // This needed because StreetAddress and PostalAddress elements same as per above
                        String elementParentName = null;
                        if(element.getParent() != null)
                        {
                            elementParentName = element.getParent().getName();
                        }

                        String controlParentName = null;
                        if(control.getParent() != null)
                        {
                            controlParentName = control.getParent().getName();
                        }
                        if(elementParentName != null && controlParentName != null)
                        {
                            if(elementParentName.equals(controlParentName))
                            {
                                result = i;
                                break;
                            }
                            // When have put a control in another panel. If it is the only
                            // control with this name then we won't miss it.
                            else if(ComponentUtils.controlNameIsUniqueIn(control.getName(), controls))
                            {
                                result = i;
                                break;
                            }
                        }
                        else if(elementParentName == null && controlParentName == null)
                        {
                            result = i;
                            break;
                        }
                        else // parent didn't used to have a name,
                        // but now has, or the other way round
                        {
                            result = i;
                            break;
                        }
                    }
                }
                else if(TableSignatures.isTableControl(element.getClass()))
                {
                    if(element.getName() != null
                        && element.getName().equals(control.getName()))
                    {
                        String elementParentName = element.getParent().getName();
                        String controlParentName = control.getParent().getName();
                        if(elementParentName != null && controlParentName != null)
                        {
                            if(elementParentName.equals(controlParentName))
                            {
                                result = i;
                                break;
                            }
                            // When have put a control in another panel. If it is the only
                            // control with this name then we won't miss it.
                            else if(ComponentUtils.controlNameIsUniqueIn(control.getName(), controls))
                            {
                                result = i;
                                break;
                            }
                        }
                        else if(elementParentName == null && controlParentName == null)
                        {
                            result = i;
                            break;
                        }
                        else // parent didn't used to have a name,
                        // but now has, or the other way round
                        {
                            result = i;
                            break;
                        }
                    }
                }
                else
                {// See comments at extraOpsToPane for remedy to this
                    // Err.error( "What kind of control is this? " + element.getClass());
                }
            }
        }
        return result;
    }

    public static List getChildrenPanels(Container container)
    {
        List result = new ArrayList();
        List components = ComponentUtils.getAllControls(container);
        for(Iterator iter = components.iterator(); iter.hasNext();)
        {
            Component comp = (Component) iter.next();
            if(ControlSignatures.isLookThruButStructuralControl(comp.getClass()))
            {
                String fullClassName = comp.getClass().getName();
                String packageName = NameUtils.baseOfPackage(fullClassName);
                if(!ControlSignatures.isSystemWidgetPackage(packageName, fullClassName))
                {
                    result.add(comp);
                }
            }
        }
        return result;
    }

    /*
    * b/cause collectViewControls will properly recurse through, don't
    * need this happening beforehand anymore. This was a poor attempt
    * at collectViewControls.
    *
    public static List getFlatPanels( List panes)
    {
    Err.pr( "getFlatPanels() starts off with:");
    Print.prList( panes);
    List result = new ArrayList();
    for(Iterator iter = panes.iterator(); iter.hasNext();)
    {
    Container comp = (Container)iter.next();
    result.add( comp);
    result.addAll( getChildrenPanels( comp));
    }
    Err.pr( "getFlatPanels() ends up with:");
    Print.prList( result);
    return result;
    }
    */

    /**
     * This method will return us the leaf controls that are seen on the
     * ControlsTreeView - it has similar logic to
     * ControlsTreeModel.attachNodes() (in Designer), and is similarly recursive.
     */
    public static List collectViewControls(List result, Object obj)
    {
        if(obj instanceof Container)
        {
            if(obj instanceof JComponent)
            {
                String name = ((JComponent) obj).getName();
                if(name == null)
                {
                    name = obj.getClass().toString();
                }
                // Err.pr( "Attaching nodes inside " + name);
            }

            Component controls[] = ((Container) obj).getComponents();
            for(int i = 0; i <= controls.length - 1; i++)
            {
                Class clazz = controls[i].getClass();
                if(!ControlSignatures.isTerminatingControl(clazz))
                {
                    if(!ControlSignatures.isVisibleTerminatingControl(clazz))
                    {
                        if(!ControlSignatures.isLookThruControl(clazz))
                        {
                            if(Utils.containsByInstance(result, controls[i]) == 0)
                            {
                                result.add(controls[i]);
                                // Err.pr( "ADDED 1: " + controls[i].getName());
                            }
                            collectViewControls(result, controls[i]);
                        }
                        else
                        {
                            collectViewControls(result, controls[i]);
                        }
                    }
                    else
                    {
                        if(Utils.containsByInstance(result, controls[i]) == 0)
                        {
                            result.add(controls[i]);
                            /*
                            Err.pr( "ADDED 2: " + controls[i].getName());
                            times++;
                            if(times == 6)
                            {
                            Err.stack();
                            }
                            */
                        }
                    }
                }
            }
        }
        else
        {// got things like Box$Filler
            // logger.log( obj.getClass() + " not a Container");
        }
        return result;
    }
}
