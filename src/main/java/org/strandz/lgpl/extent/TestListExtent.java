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
import org.strandz.lgpl.persist.EntityManagerProviderI;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.persist.EntityManagerFactory;

import java.util.ArrayList;

/**
 * A sample test case, testing <code>java.util.ListExtent</code>.
 */
public class TestListExtent extends TestCase
{
    protected ListExtent fEmpty;
    protected ListExtent fFull;
    /*
    public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
    }
    */

    private class EMProvider implements EntityManagerProviderI
    {
        private SdzEntityManagerI em;

        EMProvider(SdzEntityManagerI em)
        {
            this.em = em;
        }

        public SdzEntityManagerI getEntityManager()
        {
            return em;
        }
    }

    protected void setUp()
    {
        //Normally IndependentExtent acts as an abstract factory for ListExtent and the others,
        //and thus creates the SdzEntityManagerI for us. In this special case we do the
        //same thing manually.
        SdzEntityManagerI em = EntityManagerFactory.createNullSdzEMI();
        EMProvider provider = new EMProvider(em);
        fEmpty = new ListExtent(new ArrayList(), provider, null);
        fFull = new ListExtent(new ArrayList(), provider, null);
        fFull.add(new Integer(1));
        fFull.add(new Integer(2));
        fFull.add(new Integer(3));
    }

    public static Test suite()
    {
        return new TestSuite(TestListExtent.class);
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
