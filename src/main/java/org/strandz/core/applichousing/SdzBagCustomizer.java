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

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.Independent;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.CustomizerI;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.FieldAttribute;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.OneRowSdzBag;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.TableAttribute;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.view.util.SdzBagCustomizerPanel;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import java.util.ArrayList;
import java.util.List;

public class SdzBagCustomizer extends OneRowSdzBag
        implements ManyPanesViewer, CustomizerI
{
    public Node strandControlNode;
    private Cell strandControlCell;
    private Node paneNode;
    private Cell paneCell;
    private ArrayList beans;
    private SdzBagCustomizerPanel panel;
    private SdzBag sc;
    private static int times;

    public SdzBagCustomizer()
    {
        setName( NameUtils.tailOfPackage( this.getClass().getName()));
        MessageDlg.setDlgParent( this);
        panel = new SdzBagCustomizerPanel();
        panel.init();
        // panel.pLHS.bDelete.addActionListener( this);

        strandControlCell = new Cell();
        strandControlCell.setClazz(new Clazz(org.strandz.core.applichousing.SdzBag.class));

        FieldAttribute fa = new FieldAttribute("name", panel.tfName);
        fa.setItemValidationTrigger(new NameValidationTrigger(fa));
        strandControlCell.addAttribute(fa);
        fa = new FieldAttribute("currentPaneStr", panel.pBelow.lCurrentPane);
        strandControlCell.addAttribute(fa);
        fa = new FieldAttribute("physicalController", panel.tfPhysicalController);
        // Err.pr( "physicalController attribute: " + fa.hashCode());
        strandControlCell.addAttribute(fa);
        fa = new FieldAttribute("statusBar", panel.tfStatusBar);
        strandControlCell.addAttribute(fa);
        strandControlCell.setName("pane");
        strandControlNode = new Node();
        strandControlNode.setAllowed(CapabilityEnum.BLANK_RECORD, true);
        strandControlNode.setName("strandControlNode");
        strandControlNode.setAllowed(CapabilityEnum.POST, true);
        strandControlNode.setCell(strandControlCell);
        paneCell = new Cell();
        paneCell.setClazz(new Clazz(javax.swing.JComponent.class));
        paneCell.addAttribute(new TableAttribute("name"));
        // List indeps = new ArrayList();
        // Independent indep = new Independent();
        // indep.setMasterNode( strandControlNode);
        // indep.setMasterOrDetailField( "panes");
        // indeps.add( indep);
        paneNode = new Node();
        paneNode.setAllowed(CapabilityEnum.IGNORED_CHILD, true); // Only needed with controller etc
        paneNode.setCell(paneCell);
        paneNode.setName("paneNode");
        paneNode.setAllowed(CapabilityEnum.SET_ROW, true);
        paneNode.setTableControl(panel.lpPanes.list);
        paneNode.setAllowed(CapabilityEnum.BLANK_RECORD, true);
        paneNode.addIndependent(new Independent(strandControlNode, "panes"));
        setNode(0, strandControlNode);
        setNode(1, paneNode);
        setPane(0, panel);
        strandControlNode.GOTO(); // access event for NodeStatusBar s/fire from here
        strandControlNode.addDataFlowTrigger(new LocalDataFlowListener());
        setName("StrandControlCustomizer");
        // addStateChangeTrigger( this);
        getStrand().setErrorHandler(new HandlerT());
        getStrand().setEntityManagerTrigger(new EntityManagerT());
    }

    static class HandlerT implements ValidationHandlerTrigger
    {
        public void handleError(ApplicationError e)
        {
            List msg = e.getMsg();
            if(msg != null)
            {
                new MessageDlg(msg, JOptionPane.ERROR_MESSAGE);
                Err.alarm(msg.get(0).toString());
            }
            else
            {
                Print.prThrowable(e, "SdzBagCustomizer.HandlerT");
            }
        }
    }
    
    class EntityManagerT implements EntityManagerTriggerI
    {
        public SdzEntityManagerI fetchEM()
        {
            return null;
        }
    }


    static class NameValidationTrigger implements ItemValidationTrigger
    {
        private RuntimeAttribute attribute;

        NameValidationTrigger(RuntimeAttribute attribute)
        {
            this.attribute = attribute;
        }

        public void validateItem(ChangeEvent validationEvent)
            throws ValidationException
        {
            String txt = (String) attribute.getItemValue();
            if(!attribute.isBlank())
            {
                if(Utils.containsWhitespace(txt))
                {
                    attribute.setInError(true);
                    throw new ValidationException("<" + txt + "> contains whitespace");
                }
            }
            attribute.setInError(false);
        }
    }

    public void setObject(Object obj)
    {
        beans = new ArrayList();
        sc = (SdzBag) obj;
        // Print.pr( "------> Have: " + sc.getPanes().length + " panes");
        for(int i = 0; i <= sc.getPanes().length - 1; i++)
        {
            JComponent pane = sc.getPane(i);
            if(pane.getName() == null)
            {
                Err.error(
                    "All panes must be named, the pane " + pane.getClass().getName()
                        + " is not");
            }
        }
        beans.add(sc);
    }

    public List getData()
    {
        return beans;
    }

    public Node getMasterNode()
    {
        return strandControlNode;
    }

    public Node getDetailNode()
    {
        return paneNode;
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            List list = null;
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                strandControlCell.setData(beans);
            }
            else if(evt.getID() == DataFlowEvent.POST_QUERY)
            {
                panel.tfType.setText(sc.getClass().getName());

                RuntimeAttribute attr = strandControlCell.getAttributeByName(
                    "currentPaneStr");
                panel.pBelow.lCurrentPane.setText((String) attr.getItemValue());
                /*
                * SdzBag.setDefaults() now used instead
                *
                String s = ControlSignatures.getDefaultPhysicalControllerType().getName();
                attr = strandControlCell.getAttributeByName( "physicalController");
                //Err.pr( "PhysicalControllerType value will wipe is " + attr.getControlValue());
                //Err.pr( "physicalController attribute (GOT DYNAM) : " + attr.hashCode());
                if(attr.getItemValue().equals( ""))
                {
                attr.setItemValue( s);
                }

                s = ControlSignatures.getDefaultStatusBarType().getName();
                attr = strandControlCell.getAttributeByName( "statusBar");
                if(attr.getItemValue().equals( ""))
                {
                attr.setItemValue( s);
                }
                */
            }
        }
    }

    /*
    public void stateChangePerformed( StateChangeEvent evt)
    {
    if(evt.getCurrentState() == StateEnum.FROZEN)
    {
    panel.pLHS.bDelete.setEnabled( false);
    }
    else if(evt.getPreviousState() == StateEnum.FROZEN)
    {
    panel.pLHS.bDelete.setEnabled( true);
    }
    }
    */
    public boolean isFocusCycleRoot()
    {
        return true;
    }
}
