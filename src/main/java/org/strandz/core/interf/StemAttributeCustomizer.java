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
import org.strandz.core.domain.OtherSignatures;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.view.util.StemAttributeCustomizerPanel;

import javax.swing.JOptionPane;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used for editing a StemAttribute at DT.
 *
 * @author Chris Murphy
 */
public class StemAttributeCustomizer extends OneRowSdzBag
        implements CustomizerI
{
    private Node node;
    private Cell cell;
    private ArrayList beans = null;
    private StemAttributeCustomizerPanel panel;
    private StemAttribute object;

    public StemAttributeCustomizer()
    {
        setName( NameUtils.tailOfPackage( this.getClass().getName()));
        MessageDlg.setDlgParent( this);
        panel = new StemAttributeCustomizerPanel();
        panel.init();
        cell = new Cell();
        cell.setClazz(new Clazz(org.strandz.core.interf.StemAttribute.class));

        FieldAttribute fa = new FieldAttribute("dOField", panel.cbDOField);
        cell.addAttribute(fa);
        fa = new FieldAttribute("name", panel.tfName);
        cell.addAttribute(fa);
        node = new Node();
        node.setAllowed(CapabilityEnum.BLANK_RECORD, true);
        node.setName("node");
        node.setAllowed(CapabilityEnum.POST, true);
        node.setCell(cell);
        // node.setStrand( getStrand());
        getStrand().setErrorHandler(new HandlerT());
        getStrand().setEntityManagerTrigger(new EntityManagerT());
        setNode(0, node);
        setPane(0, panel);
        node.GOTO(); // access event for NodeStatusBar s/fire from here
        node.addDataFlowTrigger(new LocalDataFlowListener());
        setName("StemAttributeCustomizer");
    }

    class HandlerT implements ValidationHandlerTrigger
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
                Print.prThrowable(e, "StemAttributeCustomizer.HandlerT");
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

    public void setObject(Object obj)
    {
        beans = new ArrayList();
        object = (StemAttribute) obj;
        beans.add(object);
        setLOV((StemAttribute) obj);
    }

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        {
            List list = null;
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                cell.setData(beans);
                panel.tfType.setText(object.getClass().getName());
                /*
                * Did not work at all! If want to do this will have
                * to set up a seperate focus experiment program.
                if(pUtils.isBlank( panel.tfName.getText()))
                {
                panel.tfType.requestFocusInWindow();
                }
                */
            }
        }
    }

    private void setLOV(StemAttribute att)
    {
        Cell dataCell = att.getCell();
        List inAttributes = Utils.getSubList(dataCell.getAttributes(),
            StemAttribute.class);
        List outAttributes = new ArrayList();
        /*
        for(int i=0; i<=inAttributes.size()-1; i++)
        {
        if(!OtherSignatures.isRareDataField(
        ((StemAttribute)inAttributes.get( i)).getName()))
        {
        outAttributes.add( inAttributes.get( i));
        }
        }
        */
        /*
        * We only want the LOVs to be made up of attributes
        * that do not already exist in the cell.
        */
        BeanInfo bi = null;
        try
        {
            bi = Introspector.getBeanInfo(dataCell.getClazz().getClassObject());
        }
        catch(IntrospectionException ex)
        {
            Err.error(ex.toString());
        }

        PropertyDescriptor pds[] = bi.getPropertyDescriptors();
        List properties = new ArrayList(pds.length);
        for(int i = 0; i <= pds.length - 1; i++)
        {
            if(!nameExistsIn(pds[i].getName(), inAttributes))
            {
                if(!OtherSignatures.isRareDataField(pds[i].getName()))
                {
                    outAttributes.add(new StemAttribute(pds[i].getName()));
                }
            }
        }
        /*
        * Actually adding self as do below may achieve this
        if(outAttributes.isEmpty())
        {
        //Put in this blank value so the LOV validation will
        //be passed
        outAttributes.add( new StemAttribute());
        }
        */
        // Must always put in itself, as vaidation done this way
        outAttributes.add(att);
        //
        cell.setLOV(outAttributes);
    }

    private boolean nameExistsIn(String name, List attributes)
    {
        boolean result = false;
        for(Iterator iter = attributes.iterator(); iter.hasNext();)
        {
            StemAttribute attr = (StemAttribute) iter.next();
            if(attr.getDOField() != null)
            {
                if(attr.getDOField().equals(name))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private void _setLOV(StemAttribute att)
    {
        Cell dataCell = att.getCell();
        BeanInfo bi = null;
        try
        {
            bi = Introspector.getBeanInfo(dataCell.getClazz().getClassObject());
        }
        catch(IntrospectionException ex)
        {
            Err.error(ex.toString());
        }

        PropertyDescriptor pds[] = bi.getPropertyDescriptors();
        List properties = new ArrayList(pds.length);
        properties.add(StemAttribute.NO_NAME_ATTR);
        for(int i = 0; i <= pds.length - 1; i++)
        {
            if(!OtherSignatures.isRareDataField(pds[i].getName()))
            {
                properties.add(pds[i].getName());
            }
        }
        cell.setLOV(properties);
    }

    public boolean isFocusCycleRoot()
    {
        return true;
    }
}
