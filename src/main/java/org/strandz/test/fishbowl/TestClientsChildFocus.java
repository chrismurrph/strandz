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
package org.strandz.test.fishbowl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.Independent;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.NodeDefaultTrigger;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.FieldAttribute;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.ReferenceLookupAttribute;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.TableAttribute;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.data.fishbowl.objects.Client;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.store.fishbowl.FishbowlDataStoreFactory;
import org.strandz.store.fishbowl.pFishbowlClasses;
import org.strandz.view.fishbowl.ClientUI;
import org.strandz.view.fishbowl.PromotionFieldUI;
import org.strandz.view.fishbowl.PromotionTableUI;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.List;

public class TestClientsChildFocus extends TestCase
{
    //private DataStore data = FishbowlApplicationData.getInstance().getData();
    private DataStore data = new FishbowlDataStoreFactory( true).getDataStore();
    private ClientUI clientUI;
    private JPanel promotionUI;
    private Cell contactCell;
    private Node contactNode;
    private Cell promotionCell;
    private Cell promotionTypeCell;
    private Node promotionNode;
    private RuntimeAttribute promotionTypeDescription;
    private Cell clientCell;
    private Node clientNode;
    private Strand strand;
    private Independent promotionIndependent;
    private boolean useDefault = true;
    private static String DEFAULT_DESC = "8753 1654";
    private static String DIFF_PHONE = "0400 152 679";
    private static boolean useTable = false;

    public static void main(String s[])
    {
        TestClientsChildFocus obj = new TestClientsChildFocus();
        obj.setUp();
        //obj.testChildFocus();
        obj.testPreInsertDefault();
        // obj.testChildFocus();
        // obj.testDefaultNotDirty();
        // obj.testPostMakesNotDirtyAgain();
    }

    protected void setUp()
    {
        Err.setBatch(false);
        clientUI = new ClientUI();
        clientUI.init();
        if(!useTable)
        {
            promotionUI = new PromotionFieldUI();
            ((PromotionFieldUI) promotionUI).init();
        }
        else
        {
            promotionUI = new PromotionTableUI();
            ((PromotionTableUI) promotionUI).init();
        }
        clientCell = new Cell();
        clientCell.setClazz(new Clazz(org.strandz.data.fishbowl.objects.Client.class));

        FieldAttribute fa = new FieldAttribute("name",
            clientUI.clientGUI.tfCompanyName);
        // fa.setAlwaysEnabled( true);
        clientCell.addAttribute(fa);
        fa = new FieldAttribute("phone", clientUI.clientGUI.tfPhone);
        // fa.setAlwaysEnabled( true);
        clientCell.addAttribute(fa);
        /*
        fa = new FieldAttribute( "fileExtension", panel.tfFileExtension);
        //fa.setAlwaysEnabled( true);
        clientCell.addAttribute( fa);
        fa = new FieldAttribute( "packagePrefix", panel.tfPackagePrefix);
        //fa.setAlwaysEnabled( true);
        clientCell.addAttribute( fa);
        fa = new FieldAttribute( "requiredType", panel.tfRequiredType);
        //fa.setAlwaysEnabled( true);
        clientCell.addAttribute( fa);
        fa = new FieldAttribute( "preferredControllerInterface", panel.cbController);
        //fa.setAlwaysEnabled( true);
        //fa.setUpdate( false);
        clientCell.addAttribute( fa);
        */
        clientNode = new Node();
        /*
        * Defaulting for client record
        */
        clientNode.addNodeDefaultTrigger(new LocalClientNodeDefaultListener());
        clientNode.setAllowed(CapabilityEnum.BLANK_RECORD, true);
        clientNode.setName("clientNode");
        clientNode.setAllowed(CapabilityEnum.INSERT_AFTER_PLACE, true);
        clientNode.setCell(clientCell);
        contactCell = new Cell();
        contactNode = new Node();
        // contactNode.setBlankRecord( true);
        contactNode.setName("contactNode");
        contactNode.setTableControl(clientUI.contactUI.tableView);
        contactNode.setCell(contactCell);
        contactCell.setClazz(new Clazz(org.strandz.data.fishbowl.objects.Contact.class));
        contactCell.addAttribute(new TableAttribute("person"));
        // contactCell.addAttribute( new TableAttribute( "initialiser", String.class));
        contactNode.addIndependent(new Independent(clientNode, "contacts"));
        promotionNode = new Node();
        promotionNode.setName("promotionNode");
        promotionNode.setAll(true);
        promotionCell = new Cell();
        promotionTypeCell = new Cell();
        new_setupPromotion();
        strand = new Strand();
        strand.setName("Test Child Focus");
        clientNode.addDataFlowTrigger(new LocalDataFlowListener());
        clientNode.setStrand(strand);
        contactNode.setStrand(strand);
        promotionNode.setStrand(strand);
        strand.setErrorHandler(new HandlerT());
        strand.setEntityManagerTrigger(new EntityManagerT());
        clientNode.GOTO();
    }

    private void old_setupPromotion()
    {
        ReferenceLookupAttribute ref = new ReferenceLookupAttribute();
        ref.setDOField("promotionType");
        // ref.setName( "promotionType");
        promotionCell.addAttribute(ref);
        promotionCell.setClazz(new Clazz(org.strandz.data.fishbowl.objects.Promotion.class));
        promotionNode.setCell(promotionCell);
        promotionTypeCell.setRefField("promotionType");
        promotionCell.setCell(promotionCell.getCells().length, promotionTypeCell);
        if(useTable)
        {
            promotionTypeDescription = new TableAttribute("description");
        }
        else
        {
            promotionTypeDescription = new FieldAttribute("description",
                ((PromotionFieldUI) promotionUI).cbPromotionType);
        }
        promotionTypeCell.setClazz(
            new Clazz(org.strandz.data.fishbowl.objects.PromotionType.class));
        promotionTypeCell.addAttribute(promotionTypeDescription);
        Err.pr(SdzNote.INTERMITTENT_UNIT_TEST_FAILURE,
            "promotionTypeCell s/be set up now");
        if(useTable)
        {
            promotionNode.setTableControl(
                ((PromotionTableUI) promotionUI).getTableView());
        }
        // setNode( 1, promotionNode);
        // setPanelNodeTitle( promotionUI, promotionNode, "Promotion");

        // promotionNode.setStrand( getStrand());
        // try promotionNode.setUpdate( false);
        // We don't ever want any updating. This makes NOT updating
        // as strong as possible.
        // try promotionNode.setEditInsertsBeforeCommit( false);
        promotionNode.setAllowed(CapabilityEnum.INSERT_AFTER_PLACE, true);
        promotionNode.setAllowed(CapabilityEnum.REMOVE, true);
        promotionIndependent = new Independent(clientNode, "client");
        promotionNode.addIndependent(promotionIndependent);
        Err.pr(
            "Cell " + promotionTypeCell + " has been given the attribute "
                + promotionTypeDescription);
    }
    
    /*
     * Worked here but not when I tried to put it in InterfUtils ...
     */
    public static void createLookup( Node lookupRequesterNode, Cell lookupRequesterCell,  
                                     Cell beingLookedupCell, RuntimeAttribute endPointAttribute,
                                     Class lookupRequesterClass, Class beingLookedupClass, String refField)    
    {
        ReferenceLookupAttribute ref = new ReferenceLookupAttribute();
        ref.setDOField( refField);
        lookupRequesterCell.addAttribute(ref);
        lookupRequesterCell.setClazz(new Clazz( lookupRequesterClass));
        if(lookupRequesterNode.getCell() == null)
        {
            lookupRequesterNode.setCell(lookupRequesterCell);
        }
        beingLookedupCell.setRefField( refField);
        lookupRequesterCell.setCell(lookupRequesterCell.getCells().length, beingLookedupCell);
        beingLookedupCell.setClazz(
            new Clazz( beingLookedupClass));
        beingLookedupCell.addAttribute(endPointAttribute);
    }
    
    private void new_setupPromotion()
    {
        if(useTable)
        {
            promotionTypeDescription = new TableAttribute("description");
        }
        else
        {
            promotionTypeDescription = new FieldAttribute("description",
                ((PromotionFieldUI) promotionUI).cbPromotionType);
        }
        createLookup( promotionNode, promotionCell, promotionTypeCell, promotionTypeDescription, 
                      org.strandz.data.fishbowl.objects.Promotion.class, 
                      org.strandz.data.fishbowl.objects.PromotionType.class, "promotionType");
        /*
        ReferenceLookupAttribute ref = new ReferenceLookupAttribute();
        ref.setDOField("promotionType");
        // ref.setName( "promotionType");
        promotionCell.addAttribute(ref);
        promotionCell.setClazz(new Clazz(org.strandz.data.fishbowl.objects.Promotion.class));
        promotionNode.setCell(promotionCell);
        promotionTypeCell.setRefField("promotionType");
        promotionCell.setCell(promotionCell.getCells().length, promotionTypeCell);
        promotionTypeCell.setClazz(
            new Clazz(org.strandz.data.fishbowl.objects.PromotionType.class));
        promotionTypeCell.addAttribute(promotionTypeDescription);
        */
        Err.pr(SdzNote.INTERMITTENT_UNIT_TEST_FAILURE,
            "promotionTypeCell s/be set up now");
        if(useTable)
        {
            promotionNode.setTableControl(
                ((PromotionTableUI) promotionUI).getTableView());
        }
        promotionNode.setAllowed(CapabilityEnum.INSERT_AFTER_PLACE, true);
        promotionNode.setAllowed(CapabilityEnum.REMOVE, true);
        promotionIndependent = new Independent(clientNode, "client");
        promotionNode.addIndependent(promotionIndependent);
        Err.pr(
            "Cell " + promotionTypeCell + " has been given the attribute "
                + promotionTypeDescription);
    }

    public static Test suite()
    {
        return new TestSuite(TestClientsChildFocus.class);
    }

    /*
    public void testLookedUpChild()
    {
    strand.EXECUTE_QUERY();
    promotionNode.goTo();
    strand.INSERT();
    promotionTypeDescription.setItemValue( "Christmas cards 1999");
    strand.POST();
    }
    */

    public void testChildFocus()
    {
        Err.pr(SdzNote.INTERMITTENT_UNIT_TEST_FAILURE,
            "Saw this one out of 3 once when did 'current', which is all tests together");
        // load clientNode
        strand.EXECUTE_QUERY();

        // Err.pr( "Have loaded " + clientNode.getMaxCursorPosition());
        FieldAttribute attr = (FieldAttribute) clientCell.getAttributes().get(0);
        // Err.pr( "Value got is <" + attr.getControlValue() + ">");
        strand.INSERT();
        contactNode.GOTO();
        /*
        ce = new
        InputControllerEvent( OperationEnum.INSERT);
        try
        {
        strand.controlActionPerformed( ce);
        }
        catch(ValidationException ex)
        {
        Err.error( ex);
        }
        */
        /*
        try
        {
        clientNode.goNode();
        }
        catch (GoNodeChangeException e)
        {
        Err.pr( e);
        }
        */
         /**/
        assertTrue(true);
    }

    public void testPreInsertDefault()
    {
        // Err.pr( "Def value is <" + DEFAULT_DESC + ">");
        FieldAttribute attr = (FieldAttribute) clientCell.getAttributeByName("phone");
        if(attr == null)
        {
            Print.prList(clientCell.getAttributes(), "TestClientsChildFocus");
            Err.error();
        }
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        // Err.pr( "Value after insert is <" + attr.getItemValue() + ">");
        assertTrue(attr.getItemValue().equals(DEFAULT_DESC));
    }

    public void testDefaultNotDirty()
    {
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        Print.prList(clientNode.getDirtyAttributes(), "TestClientsChildFocus");
        assertFalse(clientNode.isDirty());
    }

    public void testChangeMakesDirty()
    {
        FieldAttribute attr = (FieldAttribute) clientCell.getAttributeByName("phone");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        attr.setItemValue(DIFF_PHONE);
        assertTrue(clientNode.isDirty());
    }

    public void testPostMakesNotDirtyAgain()
    {
        Err.pr(SdzNote.INTERMITTENT_UNIT_TEST_FAILURE,
            "Saw this one out of 3 once when did 'current', which is all tests together");
        Err.pr(SdzNote.FILL_DATA_FOR_DISCONNECTED_NODES,
            "We shouldn't need to do this exeqry");
        strand.execute(OperationEnum.EXECUTE_QUERY);
        Err.pr(SdzNote.FILL_DATA_FOR_DISCONNECTED_NODES,
            "Alternative to EXECUTE_QUERY is to not rely on promotion at all (but this not thread-safe in a unit test)");

        // promotionNode.removeIndependent( promotionIndependent);

        FieldAttribute attr = (FieldAttribute) clientCell.getAttributeByName("phone");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        attr.setItemValue(DIFF_PHONE);
        strand.execute(OperationEnum.POST);
        assertFalse(clientNode.isDirty());
    }

    public void testPostQueryDefault()
    {
        Err.pr(SdzNote.INTERMITTENT_UNIT_TEST_FAILURE,
            "Saw this one out of 3 once when did 'current', which is all tests together");

        FieldAttribute attr = (FieldAttribute) clientCell.getAttributeByName("phone");
        if(attr == null)
        {
            Print.prList(clientCell.getAttributes(), "TestClientsChildFocus");
            Err.error();
        }
        useDefault = true;
        strand.execute(OperationEnum.EXECUTE_QUERY);
        Print.pr("Have loaded " + clientNode.getRowCount());
        useDefault = false;
        assertTrue(attr.getItemValue().equals(DEFAULT_DESC));
    }

    class LocalClientNodeDefaultListener implements NodeDefaultTrigger
    {
        public void nodeChange(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_INSERT)
            {
                Client c = new Client();
                c.setPhone(DEFAULT_DESC);
                clientCell.setDefaultElement(c);
            }
        }
    }


    class HandlerT implements ValidationHandlerTrigger
    {
        public void handleError(ApplicationError e)
        {
            List msg = e.getMsg();
            if(msg != null)
            {
                new MessageDlg(msg, JOptionPane.ERROR_MESSAGE);
                // Err.pr( msg);
                Err.alarm(msg.get(0).toString());
            }
            else
            {
                Print.prThrowable(e, "TestClientsChildFocus");
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

    class LocalDataFlowListener implements DataFlowTrigger
    {
        public void dataFlowPerformed(DataFlowEvent evt)
        // throws ValidationException
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                data.rollbackTx();
                data.startTx();

                List list = (List) data.get(pFishbowlClasses.CLIENT);
                clientCell.setData(list);

                List promotionData = (List) data.get(pFishbowlClasses.PROMOTION);
                Print.prList(promotionData, "Promotion data to set");
                promotionCell.setData(promotionData);
                list = (List) data.get(pFishbowlClasses.PROMOTION_TYPE);
                Print.prList(list, "Promotion data have set");
                promotionTypeCell.setLOV(list);
            }
            else if(evt.getID() == DataFlowEvent.POST_QUERY && useDefault)
            {
                // Client c = new Client();
                // c.setPhone( DEFAULT_DESC);
                Err.pr("Default code happening after query");
                clientCell.getAttributeByName("phone").setItemValue(DEFAULT_DESC);
            }
        }
    }
}
