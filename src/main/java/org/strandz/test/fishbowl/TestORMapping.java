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
import org.strandz.lgpl.util.Clazz;
import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.FieldAttribute;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NonVisualFieldAttribute;
import org.strandz.core.interf.NonVisualTableAttribute;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.TableAttribute;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.relational.CRUDEnum;
import org.strandz.core.relational.ORMapExtent;
import org.strandz.core.relational.Statement;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.store.fishbowl.FishbowlDataStoreFactory;
import org.strandz.store.fishbowl.pFishbowlClasses;
import org.strandz.view.fishbowl.ClientUI;

import javax.swing.JTable;
import java.util.ArrayList;
import java.util.List;

public class TestORMapping extends TestCase
{
    private ClientUI clientUI;
    private DataStore data = new FishbowlDataStoreFactory( true).getDataStore();
    private RuntimeAttribute nameAttribute;
    private RuntimeAttribute emailAttribute;
    private RuntimeAttribute phoneAttribute;
    RuntimeAttribute faxAttribute;
    private Cell clientCell;
    private Node clientNode;
    Strand strand;
    private ORMapExtent queryList;
    private List clientPKColNames = new ArrayList();
    boolean useItem = false;
    JTable tableControl;
    private static int times;

    public static void main(String s[])
    {
        TestORMapping obj = new TestORMapping();
        obj.setUp();
        obj.testSimply();
        obj.tearDown();
    }

    void prMsg(int times)
    {
        Err.pr("MSG: " + times + " " + strand.getErrorMessage());
    }

    protected void setUp()
    {
        useItem = true;
        tableControl = null;
        commonSetUp();
    }

    protected void tearDown()
    {
        String txt = strand.getErrorMessage();
        if(txt != null && txt.indexOf("Cannot Insert Duplicate PK") != -1)
        {
            strand.REMOVE();
        }
        /*
        Err.pr( "tearDown() , node currently on is " + strand.getCurrentNode());
        boolean ok = clientNode.GOTO();
        if(!ok)
        {
          Err.stack( "tearDown() NOT ok, Not able to GOTO " + clientNode + " because " + strand.getErrorMessage());
        }
        else
        {
          Err.pr( "tearDown() ok");
        }
        */
    }

    void commonSetUp()
    {
        Err.setBatch(false); // so get a bell
        clientUI = new ClientUI();
        clientUI.init();
        clientCell = new Cell();
        clientCell.setClazz(new Clazz(org.strandz.data.fishbowl.objects.Client.class));
        //
        if(useItem)
        {
            if(tableControl == null)
            {
                Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS, "USING FieldAttributes");
                nameAttribute = new FieldAttribute("name",
                    clientUI.clientGUI.tfCompanyName);
                emailAttribute = new FieldAttribute("email", clientUI.clientGUI.tfEmail);
                phoneAttribute = new FieldAttribute("phone", clientUI.clientGUI.tfPhone);
                faxAttribute = new FieldAttribute("fax", clientUI.clientGUI.tfFax);
            }
            else
            {
                Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS, "USING TableAttributes");
                nameAttribute = new TableAttribute("name");
                emailAttribute = new TableAttribute("email");
                phoneAttribute = new TableAttribute("phone");
                faxAttribute = new TableAttribute("fax");
            }
        }
        else
        {
            if(tableControl != null)
            {
                Print.pr("USING NonVisualTableAttributes");
                nameAttribute = new NonVisualTableAttribute();
                nameAttribute.setDOField("name");
                emailAttribute = new NonVisualTableAttribute();
                emailAttribute.setDOField("email");
                phoneAttribute = new NonVisualTableAttribute();
                phoneAttribute.setDOField("phone");
                faxAttribute = new NonVisualTableAttribute();
                faxAttribute.setDOField("fax");
            }
            else
            {
                Print.pr("USING NonVisualFieldAttributes");
                nameAttribute = new NonVisualFieldAttribute();
                nameAttribute.setDOField("name");
                emailAttribute = new NonVisualFieldAttribute();
                emailAttribute.setDOField("email");
                phoneAttribute = new NonVisualFieldAttribute();
                phoneAttribute.setDOField("phone");
                faxAttribute = new NonVisualFieldAttribute();
                faxAttribute.setDOField("fax");
            }
        }
        clientCell.addAttribute(nameAttribute);
        clientCell.addAttribute(emailAttribute);
        clientCell.addAttribute(phoneAttribute);
        clientCell.addAttribute(faxAttribute);
        //
        clientNode = new Node();
        clientNode.setAllowed(CapabilityEnum.BLANK_RECORD, true);
        clientNode.setName("clientNode");
        clientNode.setAllowed(CapabilityEnum.INSERT_AFTER_PLACE, true);
        clientNode.setAllowed(CapabilityEnum.POST, true);
        clientNode.setAllowed(CapabilityEnum.SET_ROW, true);
        clientNode.setAllowed(CapabilityEnum.REMOVE, true);
        clientNode.setCell(clientCell);
        if(tableControl != null)
        {
            clientNode.setTableControl(tableControl);
        }
        strand = new Strand();
        strand.setName("Test Raw Statements");
        strand.setErrorHandler(new HandlerT());
        strand.setEntityManagerTrigger(new EntityManagerT());
        clientNode.addDataFlowTrigger(new LocalDataFlowListener());
        clientNode.setStrand(strand);
        clientPKColNames.add("name");
        clientPKColNames.add("email");
        /*
        times++;
        Err.pr("To GOTO times " + times);
        if(times == 11)
        {
            Err.debug();
        }
        */
        boolean ok = clientNode.GOTO();
        if(!ok)
        {
            Err.stack("Not able to GOTO " + clientNode + " from " + strand.getCurrentNode() + " because " + strand.getErrorMessage());
        }
    }

    public static Test suite()
    {
        return new TestSuite(TestORMapping.class);
    }

    public void testRawUpdateStatements()
    {
        strand.execute(OperationEnum.EXECUTE_QUERY);
        Print.pr(
            "-------> fax was " + faxAttribute.getItemValue() + " at "
                + strand.getCurrentNode().getRow());
        faxAttribute.setItemValue("00000000");
        Print.pr(
            "-------> email was " + emailAttribute.getItemValue() + " at "
                + strand.getCurrentNode().getRow());
        Print.pr("Num of rows is " + strand.getCurrentNode().getRowCount());
        strand.SET_ROW(1);
        Print.pr(
            "-------> fax was " + faxAttribute.getItemValue() + " at "
                + strand.getCurrentNode().getRow());
        faxAttribute.setItemValue("99999999");
        Print.pr(
            "-------> email was " + emailAttribute.getItemValue() + " at "
                + strand.getCurrentNode().getRow());
        strand.execute(OperationEnum.POST);

        List statements = queryList.getStatements();
        Print.prList(statements, "TestORMapping");
        assertTrue(statements.size() == 2);
        assertTrue(((Statement) statements.get(0)).getType() == CRUDEnum.UPDATE);
    }

    public void testSimply()
    {
        strand.execute(OperationEnum.EXECUTE_QUERY);
        Print.prList(queryList, "TestORMapping");
        // setClient( null, null, null, "00000000");
        // strand.SET_ROW( 1);
        // setClient(  null, null, null, "99999999");
         /**/
        // strand.execute( OperationEnum.INSERT_AFTER_PLACE);
        // setClient( "Angel1","angel1@legal.com.au","0403162669","99999999");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angel2", "angel2@legal.com.au", "0403162669", "99999999");
        // strand.execute( OperationEnum.POST);
        strand.SET_ROW(2);
        setClient(null, null, null, "11111111");
         /**/
        strand.execute(OperationEnum.POST);
        queryList.coalesce();
    }

    /*
    ------------------------
    value: StingRay Bay, gov@seaweedsoftware.com.au, 02 9326 3664, 02 9326 3664
    value: Cornish Pasties Pty Ltd, andrewc@walker.com.au, 02 3333 3333, 02 3333 3333
    ------------------------
    */
    /*
    value: 1052737427325 UPDATE, PK values: <StingRay Bay>,<gov@seaweedsoftware.com.au>,NON PK values: <null>,<00000000>,
    value: 1052737427415 INSERT_AFTER_PLACE, PK values: <Angel1>,<angel1@legal.com.au>,NON PK values: <0403162669>,<99999999>,
    value: 1052737427515 UPDATE, PK values: <Cornish Pasties Pty Ltd>,<andrewc@walker.com.au>,NON PK values: <null>,<11111111>,
    */
    public void testRawLoadsStatements()
    {
        strand.execute(OperationEnum.EXECUTE_QUERY);
        setClient(null, null, null, "00000000");
        strand.SET_ROW(0);
        setClient(null, null, null, "99999999");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angel1", "angel1@legal.com.au", "0403162669", "99999999");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angel2", "angel2@legal.com.au", "0403162669", "99999999");
        strand.SET_ROW(0);
        setClient(null, null, null, "11111111");
        strand.SET_ROW(1);
        setClient(null, null, null, "22222222");
        strand.SET_ROW(2);
        setClient(null, null, null, "33333333");
        strand.execute(OperationEnum.POST);
        setClient(null, null, null, "44444444");
        strand.execute(OperationEnum.POST);
        strand.execute(OperationEnum.REMOVE);
        queryList.coalesce();

        List output = queryList.getStatements();
         /**/
        Print.pr(output.size());
        /*
        * This what would expect if INSERT_AFTER_PLACE inserting before the current row.
        * Now that row is incremented as the b/ground add is done, I after current is
        * the effect.
        assertTrue( output.size() == 3);
        //Err.pr( "First state of type: " + ((Statement)output.get(0)).getType());
        assertTrue( ((Statement)output.get(0)).getType() == CRUDEnum.INSERT_AFTER_PLACE);
        //Err.pr( "Second state of type: " + ((Statement)output.get(1)).getType());
        assertTrue( ((Statement)output.get(1)).getType() == CRUDEnum.INSERT_AFTER_PLACE);
        //Err.pr( "Third state of type: " + ((Statement)output.get(2)).getType());
        assertTrue( ((Statement)output.get(2)).getType() == CRUDEnum.DELETE);
        */
        assertTrue(output.size() == 2);
        Print.pr("First state of type: " + ((Statement) output.get(0)).getType());
        assertTrue(((Statement) output.get(0)).getType() == CRUDEnum.INSERT);
        Print.pr("Second state of type: " + ((Statement) output.get(1)).getType());
        assertTrue(((Statement) output.get(1)).getType() == CRUDEnum.UPDATE);
    }

    public void testUpdateNotToNull()
    {
        strand.execute(OperationEnum.EXECUTE_QUERY);
        // String origPhone = "0403162669";
        // null means wont call set
        setClient(null, null, null, "99999999");
        strand.execute(OperationEnum.POST);
        queryList.coalesce();

        List output = queryList.getStatements();
        assertTrue(output.size() == 1);

        //
        Statement statement = (Statement) output.get(0);
        List nonPkList = statement.getNonPkValues();
        Statement.NullObject nullObj = (Statement.NullObject) nonPkList.get(1);
        assertTrue(!nullObj.isNull());
        // assertTrue( nonPkList.get( 0).toString().equals( "NULL"));
    }

    public void testUpdateToNull()
    {
        strand.execute(OperationEnum.EXECUTE_QUERY);

        String origPhone = "0403162669";
        setClient(null, null, origPhone, "99999999");
        strand.execute(OperationEnum.POST);
        phoneAttribute.setItemValue(null); // will make an UPDATE to NULL happen
        Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS, "B4 post");
        strand.execute(OperationEnum.POST);
        Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS, "After post");
        queryList.coalesce();

        List output = queryList.getStatements();
        assertTrue(output.size() == 1);

        //
        Statement statement = (Statement) output.get(0);
        Err.pr("STATE: " + statement);
        List nonPkList = statement.getNonPkValues();
        assertTrue(!nonPkList.get(0).equals(origPhone));
        Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS, "nonPkList first value is <" + nonPkList.get(0) + ">");
        /*
        * Merde - I was wrong about th following!!:
        * This assertion will fail if use applichousing attributes. Not sure
        * that this is something we need to fix. Non-visuals prolly
        * have a more pure interpretation of 'setting an item to null'
        * - basically it will always be changed to a "" for components
        * like TextFields - and we can use Converts to change this
        * behaviour if we want. TODO in case decide later to convert
        * the Comp component into acting more like Swing components.
        * Whatever - we need a unit test to highlight the difference
        * and keep it the same.
        */
        assertTrue( "toString() of a " + nonPkList.get(0).getClass().getName() + " doesn't return \"NULL\" but <" + nonPkList.get(0).toString() + ">", nonPkList.get(0).toString().equals("NULL"));
    }

    public void testInsertToNull()
    {
        strand.execute(OperationEnum.INSERT_AFTER_PLACE); // where consumeNodesIntoRT occurs, so must occur before

        // setQuery
        List list = new ArrayList();
        queryList = clientCell.setQuery(list, clientPKColNames);

        // note that null in this call means 'don't set it'
        String origPhone = "0403162669";
        setClient("Angel2", "angel2@legal.com.au", origPhone, "99999999");
        strand.execute(OperationEnum.POST);
        phoneAttribute.setItemValue(null);
        strand.execute(OperationEnum.POST);
        queryList.coalesce();

        List output = queryList.getStatements();
        assertTrue(output.size() == 1);

        Statement statement = (Statement) output.get(0);
        List nonPkList = statement.getNonPkValues();
        assertTrue(((Statement.NullObject) nonPkList.get(0)).isNull());
    }

    /**
     * Can set all you like to what it already is, but if it is not
     * a change then won't come up as a capital NULL.
     */
    public void testInsertToNullSame()
    {
        strand.execute(OperationEnum.INSERT_AFTER_PLACE); // where consumeNodesIntoRT occurs, so must occur before

        // setQuery
        List list = new ArrayList();
        queryList = clientCell.setQuery(list, clientPKColNames);
        // note that null in this call means 'don't set it'
        setClient("Angel2", "angel2@legal.com.au", null, "99999999");
        phoneAttribute.setItemValue(null);
        strand.execute(OperationEnum.POST);
        queryList.coalesce();

        List output = queryList.getStatements();
        assertTrue(output.size() == 1);

        Statement statement = (Statement) output.get(0);
        List nonPkList = statement.getNonPkValues();
        assertTrue(!((Statement.NullObject) nonPkList.get(0)).isNull());
    }

    public void testInsertNotToNull()
    {
        strand.execute(OperationEnum.INSERT_AFTER_PLACE); // where consumeNodesIntoRT occurs, so must occur before

        // setQuery
        List list = new ArrayList();
        queryList = clientCell.setQuery(list, clientPKColNames);
        // note that null in this call means 'don't set it'
        setClient("Angel2", "angel2@legal.com.au", null, null);
        strand.execute(OperationEnum.POST);
        queryList.coalesce();

        List output = queryList.getStatements();
        assertTrue(output.size() == 1);

        Statement statement = (Statement) output.get(0);
        List nonPkList = statement.getNonPkValues();
        Statement.NullObject nullObj = (Statement.NullObject) nonPkList.get(0);
        assertTrue(!nullObj.isNull());
    }

    private void setClient(String name,
                           String email,
                           String phone,
                           String fax)
    {
        if(name != null)
        {
            nameAttribute.setItemValue(name);
        }
        if(email != null)
        {
            emailAttribute.setItemValue(email);
        }
        if(phone != null)
        {
            phoneAttribute.setItemValue(phone);
        }
        if(fax != null)
        {
            faxAttribute.setItemValue(fax);
        }
    }

    public void testRawInsertStatements()
    {
        strand.execute(OperationEnum.INSERT_AFTER_PLACE); // where consumeNodesIntoRT occurs

        List list = new ArrayList();
        queryList = clientCell.setQuery(list, clientPKColNames);
        setClient("Chris", "cjmurphy@ozemail.com.au", "87531654", "00000000");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angeline", "angeline@legal.com.au", "0403162669", "99999999");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angel1", "angel1@legal.com.au", "0403162669", "99999999");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angel2", "angel2@legal.com.au", "0403162669", "99999999");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angel3", "angel3@legal.com.au", "0403162669", "99999999");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angel4", "angel4@legal.com.au", "0403162669", "99999999");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angel5", "angel5@legal.com.au", "0403162669", "99999999");
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        setClient("Angel6", "angel6@legal.com.au", "0403162669", "99999999");
        strand.execute(OperationEnum.POST);

        List statements = queryList.getStatements();
        Print.prList(statements, "TestORMapping");
        assertTrue(statements.size() == 8);
        assertTrue(((Statement) statements.get(0)).getType() == CRUDEnum.INSERT);
        queryList.coalesce();
    }

     /**/
    public void testSamePKInsert()
    {
        Err.setBatch(true);
        strand.execute(OperationEnum.INSERT_AFTER_PLACE); // where consumeNodesIntoRT occurs

        List list = new ArrayList();
        queryList = clientCell.setQuery(list, clientPKColNames);
        nameAttribute.setItemValue("Chris");
        emailAttribute.setItemValue("cjmurphy@ozemail.com.au");
        phoneAttribute.setItemValue("87531654");
        faxAttribute.setItemValue("00000000");
        //
        strand.execute(OperationEnum.INSERT_AFTER_PLACE);
        nameAttribute.setItemValue("Chris");
        emailAttribute.setItemValue("cjmurphy@ozemail.com.au");
        phoneAttribute.setItemValue("87531654");
        faxAttribute.setItemValue("00000000");
        //
        strand.execute(OperationEnum.POST);

        List statements = queryList.getStatements();
        Print.prList(statements, "TestORMapping");
        assertTrue(statements.size() == 1);
        assertTrue(((Statement) statements.get(0)).getType() == CRUDEnum.INSERT);
    }

     /**/

    public void testInsertNoPK()
    {
        Err.setBatch(true);
        strand.execute(OperationEnum.INSERT_AFTER_PLACE); // where consumeNodesIntoRT occurs

        List list = new ArrayList();
        queryList = clientCell.setQuery(list, clientPKColNames);
        phoneAttribute.setItemValue("87531654");
        faxAttribute.setItemValue("00000000");

        boolean failure = false;
        try
        {
            strand.execute(OperationEnum.POST);
        }
        catch(Error err)
        {
            failure = true;
        }
        assertTrue(failure);
    }

    public void testRawRemoveStatements()
    {
        strand.execute(OperationEnum.EXECUTE_QUERY);
        strand.execute(OperationEnum.REMOVE);

        // strand.execute( OperationEnum.REMOVE);
        List statements = queryList.getStatements();
        Print.prList(statements, "TestORMapping");
        // assertTrue( statements.size() == 2);
        // assertTrue( ((Statement)statements.get(0)).getType() == CRUDEnum.DELETE);
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
                Err.pr("Num of clients is " + list.size());
                queryList = clientCell.setQuery(list, clientPKColNames);
                /*
                ContextPanel panel = (ContextPanel)controllerInterface.getPane();
                panel.cbController.removeAllItems();
                ArrayList controllers = (ArrayList)
                ControlSignatures.getControllerInterfaces();
                for(Iterator it = controllers.iterator(); it.hasNext();)
                {
                panel.cbController.addItem( (String)it.next());
                }
                */
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
                // new MessageDlg( msg);
                Print.pr(msg);
                // Err.soundOff();
            }
            else
            {
                Print.prThrowable(e, "TestORMapping");
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
}
