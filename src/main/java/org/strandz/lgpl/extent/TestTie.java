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
import org.strandz.lgpl.widgets.ObjComp;
import org.strandz.lgpl.util.NamedI;

import java.awt.Component;
import java.awt.Container;

/**
 * A sample test case, testing <code>java.util.SetExtent</code>.
 */
public class TestTie extends TestCase
{
    NamedContainer container;
    NamedLabel component;

    private static class NamedContainer extends Container implements NamedI
    {
        public String getName()
        {
            return super.getName();
        }
    }

    private static class NamedLabel extends ObjComp implements NamedI
    {
        public String getName()
        {
            return super.getName();
        }
    }

    /*
    * Don't set property this way, but from ant - tried
    * but now trying this
    */
    public TestTie()
    {
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        System.setProperty("java.awt.headless", "true");
        container = new NamedContainer();
        component = new NamedLabel();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        System.setProperty("java.awt.headless", "false");
    }

    public static Test suite()
    {
        return new TestSuite(TestTie.class);
    }

    public void testParentMethodTie()
    {
        Tie tie = Tie.createTie(container, component, Container.class,
            Component.class, "components", null, null);
        assertTrue(tie.getType() == Tie.PARENT_LIST);
        assertTrue(tie instanceof MethodTie);
    }
}
