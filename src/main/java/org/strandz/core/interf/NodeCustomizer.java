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

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.NodeDefaultTrigger;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.view.util.NodeCustomizerPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for editing a Node at DT.
 *
 * @author Chris Murphy
 */
public class NodeCustomizer extends OneRowSdzBag
        implements CustomizerI, ActionListener
{
    private Node node;
    private Cell cell;
    private ArrayList beans = null;
    private FieldAttribute table;
    private NodeCustomizerPanel panel;

    public NodeCustomizer()
    {
        setName( NameUtils.tailOfPackage( this.getClass().getName()));
        MessageDlg.setDlgParent( this);
        panel = new NodeCustomizerPanel();
        panel.init();
        panel.btnRemoveTableControl.addActionListener(this);
        cell = new Cell();
        cell.setClazz(new Clazz(org.strandz.core.interf.Node.class));

        FieldAttribute fa = new FieldAttribute("name", panel.tfName);
        cell.addAttribute(fa);
        fa = new FieldAttribute("title", panel.tfTitle);
        cell.addAttribute(fa);
        table = new FieldAttribute("tableControl", panel.tfTableControl);
        cell.addAttribute(table);
        fa = new FieldAttribute("enterQuery", panel.cbEnterQuery);
        cell.addAttribute(fa);
        fa = new FieldAttribute("executeSearch", panel.cbExecuteSearch);
        cell.addAttribute(fa);
        fa = new FieldAttribute("executeQuery", panel.cbLoad);
        cell.addAttribute(fa);
        fa = new FieldAttribute("update", panel.cbUpdate);
        cell.addAttribute(fa);
        fa = new FieldAttribute("insert", panel.cbInsert);
        cell.addAttribute(fa);
        fa = new FieldAttribute("remove", panel.cbDelete);
        cell.addAttribute(fa);
        fa = new FieldAttribute("post", panel.cbPost);
        cell.addAttribute(fa);
        fa = new FieldAttribute("commitReload", panel.cbCommitReload);
        cell.addAttribute(fa);
        fa = new FieldAttribute("commitOnly", panel.cbCommitOnly);
        cell.addAttribute(fa);
        fa = new FieldAttribute("previous", panel.cbUp);
        cell.addAttribute(fa);
        fa = new FieldAttribute("next", panel.cbDown);
        cell.addAttribute(fa);
        fa = new FieldAttribute("setRow", panel.cbSetRow);
        cell.addAttribute(fa);
        fa = new FieldAttribute("editInsertsBeforeCommit",
            panel.cbEditInsertsBeforeCommit);
        cell.addAttribute(fa);
        fa = new FieldAttribute("ignoredChild", panel.cbIgnoredChild);
        cell.addAttribute(fa);
        fa = new FieldAttribute("cascadeDelete", panel.cbCascadeDelete);
        cell.addAttribute(fa);
        fa = new FieldAttribute("focusCausesNodeChange", panel.cbFocusNode);
        cell.addAttribute(fa);
        cell.setName("cell");
        node = new Node();
        node.setAllowed(CapabilityEnum.BLANK_RECORD, true);
        node.setName("node");
        node.setAllowed(CapabilityEnum.POST, true);
        node.setAllowed(CapabilityEnum.COMMIT_ONLY, true);
        node.setCell(cell);
        node.addNodeDefaultTrigger(new LocalNodeDefaultTrigger());
        setNode(0, node);
        setPane(0, panel);
        node.GOTO(); // access event for NodeStatusBar s/fire from here
        node.addDataFlowTrigger(new LocalDataFlowListener());
        setName("NodeCustomizer");
    }

    public void actionPerformed(ActionEvent evt)
    {
        if(evt.getSource() == panel.btnRemoveTableControl)
        {
            table.setItemValue(null);
        }
    }

    public FieldAttribute getTableAttribute()
    {
        return table;
    }

    public void setObject(Object obj)
    {
        beans = new ArrayList();

        Node node = (Node) obj;
        beans.add(node);
    }

    public Node getNodeEditing()
    {
        return (Node) beans.get(0);
    }

    /*
    * NOT USED
    * This what we want, but must be achieved in another way as
    * the object has already been created by the time the customizer
    * comes around.
    *
    * POST_QUERY is another thought, but normally relies on logic such
    * as -if it's blank, then set the default-. Here would have to have
    * vars in Node that tell us that say, data field insert has not yet
    * been set.
    *
    * Best solution is to use static method in here that is applied when
    * a node has just been created... however have NOW NOT chosen a POST_QUERY
    * method whereby whether have been customized is stored in Bean. Thus
    * have polluted them all, and not even with a BeanHelper!
    *
    * Reason actually have to do it properly is that if user doesn't customize,
    * then defaults won't happen, so user will run and be told can't do say
    * SET_ROW.
    *
    * Will find defaulting in sdzdsgnr where Node has just been created. Each
    * Capability will have a default. (Node.setDefaults() in BTM).
    */
    public class LocalNodeDefaultTrigger implements NodeDefaultTrigger
    {
        public void nodeChange(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_INSERT)
            {// applyDefaultsToNewNode();
            }
        }
    }

    public Cell getCell()
    {
        return cell;
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            List list = null;
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                cell.setData(beans);
            }
            else if(evt.getID() == DataFlowEvent.POST_QUERY)
            {/*
         if(!getNodeEditing().isAlreadyBeenCustomized())
         {
         //Err.pr( "first time, had not been customized before");
         applyDefaultsToNewNode();
         getNodeEditing().setAlreadyBeenCustomized( true);
         }
         else
         {
         //Err.pr( "NOT first time, been customized before");
         }
         */}
        }
    }

    public boolean isFocusCycleRoot()
    {
        return true;
    }
}
