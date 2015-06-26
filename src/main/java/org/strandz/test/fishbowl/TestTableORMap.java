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

// import junit.framework.TestCase;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.TableSignatures;
import org.strandz.lgpl.util.Print;

import javax.swing.JTable;

public class TestTableORMap extends TestORMapping
{
    protected void setUp()
    {
        useItem = true;
        tableControl = new JTable();
        tableControl.setName( "Table used by TestTableORMap");
        super.commonSetUp();
    }

    public static void main(String s[])
    {
        TestTableORMap obj = new TestTableORMap();
        obj.setUp();
        obj.testTable();
    }

    public static Test suite()
    {
        return new TestSuite(TestTableORMap.class);
    }

    public void testTable()
    {
        strand.execute(OperationEnum.EXECUTE_QUERY);
        Print.pr(
            "fax was " + faxAttribute.getItemValue() + " at "
                + strand.getCurrentNode().getRow());
        faxAttribute.setItemValue("00000000");

        String id = (String) faxAttribute.getItemName();
        Print.pr("Finding alternative AI using " + id);

        Object AI = TableSignatures.getValueFromColumnName(tableControl, id);
        Print.pr("Alternative AI using " + id + " is " + AI);
        assertTrue(AI.equals("00000000"));
    }

    public void testTableSetGet()
    {
        strand.execute(OperationEnum.EXECUTE_QUERY);
        Print.pr(
            "fax was " + faxAttribute.getItemValue() + " at "
                + strand.getCurrentNode().getRow());
        tableControl.setValueAt("00000000", 0, 3);

        String val = (String) tableControl.getValueAt(0, 3);
        assertTrue(val.equals("00000000"));
    }
}
