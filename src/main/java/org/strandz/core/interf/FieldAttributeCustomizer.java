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
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.view.util.FieldAttributeCustomizerPanel;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for editing a FieldAttribute at DT.
 *
 * @author Chris Murphy
 */
public class FieldAttributeCustomizer extends OneRowSdzBag
        implements CustomizerI
{
    private Node node;
    private Cell cell;
    private ArrayList beans = null;
    private FieldAttributeCustomizerPanel panel;
    private FieldAttribute object;

    public FieldAttributeCustomizer()
    {
        setName( NameUtils.tailOfPackage( this.getClass().getName()));
        MessageDlg.setDlgParent( this);
        panel = new FieldAttributeCustomizerPanel();
        panel.init();
        cell = new Cell();
        cell.setClazz(new Clazz(org.strandz.core.interf.FieldAttribute.class));

        FieldAttribute fa = new FieldAttribute("dOField", panel.lfDOField);
        cell.addAttribute(fa);
        fa = new FieldAttribute("name", panel.tfName);
        cell.addAttribute(fa);
        // Not this way, as way done is to control.getName() - see PRE_QUERY
        // fa = new FieldAttribute( "itemLabel", panel.lfItem);
        // cell.addAttribute( fa);
        fa = new FieldAttribute("enabled", panel.cbEnabled);
        cell.addAttribute(fa);

        node = new Node();
        node.setAllowed(CapabilityEnum.BLANK_RECORD, true);
        node.setName("node");
        node.setAllowed(CapabilityEnum.POST, true);
        node.setCell(cell);
        setNode(0, node);
        setPane(0, panel);
        node.GOTO(); // access event for NodeStatusBar s/fire from here
        node.addDataFlowTrigger(new LocalDataFlowListener());
        setName("FieldAttributeCustomizer");
    }

    public void setObject(Object obj)
    {
        beans = new ArrayList();
        object = (FieldAttribute) obj;
        beans.add(object);
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            List list = null;
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                cell.setData(beans);
                panel.lfType.setText(object.getClass().getName());

                Component comp = (Component) object.getItem();
                String txt = null;
                if(comp == null)
                {
                    txt = "NULL COMPONENT";
                }
                else
                {
                    txt = comp.getName();
                }
                panel.lfItem.setText(txt);
            }
        }
    }

    public boolean isFocusCycleRoot()
    {
        return true;
    }
}
