/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.extent;

import junit.framework.TestCase;
import org.strandz.lgpl.util.Err;

/**
 * A sample test case, testing <code>java.util.ListExtent</code>.
 */
public class TestArrayExtent extends TestCase
{
    protected ArrayExtent fEmpty;
    protected ArrayExtent fFull;

    /*
    public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
    }
    */
    protected void setUp()
    {
        Err.setBatch(true);

        Object[] three = new Object[3];
        three[0] = new Integer(1);
        three[1] = new Integer(2);
        three[2] = new Integer(3);

        Object[] none = new Object[0];
//    fEmpty = new ArrayExtent( none);
        fEmpty = (ArrayExtent) IndependentExtent.createIndependent(none, null, null);
//    fFull = new ArrayExtent( three);
        fFull = (ArrayExtent) IndependentExtent.createIndependent(three, null, null);
    }

    /*
    public static Test suite() {
    return new TestSuite(ListExtentTest.class);
    }
    */
    public void testCantAdd()
    {
        try
        {
            fFull.add(new Integer(1));
        }
        catch(Error e)
        {
            return;
        }
        fail("Should raise an Error");
    }

    public void testContains()
    {
        assertTrue(fFull.contains(new Integer(1)));
        assertTrue(!fEmpty.contains(new Integer(1)));
    }

    public void testElementAt()
    {
        Integer i = (Integer) fFull.get(0);
        assertTrue(i.intValue() == 1);
        try
        {
            fFull.get(fFull.size());
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return;
        }
        fail("Should raise an ArrayIndexOutOfBoundsException");
    }

    public void testCantRemove()
    {
        try
        {
            fFull.remove(new Integer(3));
        }
        catch(Error e)
        {
            return;
        }
        fail("Should raise an Error");
    }
}
