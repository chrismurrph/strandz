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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashSet;

/**
 * A sample test case, testing <code>java.util.SetExtent</code>.
 */
public class TestSetExtent extends TestCase
{
    protected SetExtent fEmpty;
    protected SetExtent fFull;

    /*
    public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
    }
    */
    protected void setUp()
    {
        fEmpty = new SetExtent(new HashSet(), null, null);
        fFull = new SetExtent(new HashSet(), null, null);
        fFull.add(new Integer(1));
        fFull.add(new Integer(2));
        fFull.add(new Integer(3));
    }

    public static Test suite()
    {
        return new TestSuite(TestSetExtent.class);
    }

    public void testCapacity()
    {
        int size = fFull.size();
        for(int i = 0; i < 100; i++)
        {
            fFull.add(new Integer(i));
        }
        assertTrue(fFull.size() == 100 + size);
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
        catch(IndexOutOfBoundsException e)
        {
            return;
        }
        fail("Should raise an IndexOutOfBoundsException");
    }

    public void testRemoveElement()
    {
        fFull.remove(new Integer(3));
        assertTrue(!fFull.contains(new Integer(3)));
    }
}
