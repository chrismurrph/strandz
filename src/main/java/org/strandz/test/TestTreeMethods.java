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
package org.strandz.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.strandz.lgpl.util.TreeUtils;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public class TestTreeMethods extends TestCase
{
    private JTree tree;
    private StringBuffer anObject; // to have a different type
    private DefaultMutableTreeNode aNode;

    public static void main(String s[])
    {
        TestTreeMethods obj = new TestTreeMethods();
        obj.setUp();
        // obj.testGettingAllTreeNodesTop();
    }

    protected void setUp()
    {
        // Err.setBatch( false);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("The Java Series");
        anObject = new StringBuffer("Books for Java Implementers");
        aNode = new DefaultMutableTreeNode(anObject);
        createNodes(top);
        tree = new JTree(top);
    }

    private void createNodes(DefaultMutableTreeNode top)
    {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;
        category = new DefaultMutableTreeNode("Books for Java Programmers");
        top.add(category);
        book = new DefaultMutableTreeNode
            ("The Java Tutorial: Object-Oriented " + "Programming for the Internet");
        category.add(book);
        book = new DefaultMutableTreeNode
            ("The Java Tutorial Continued: The Rest of the JDK");
        category.add(book);
        book = new DefaultMutableTreeNode
            ("The JFC Swing Tutorial: " + "A Guide to Constructing GUIs");
        category.add(book);
        top.add(aNode);
        book = new DefaultMutableTreeNode
            ("The Java Virtual Machine Specification");
        aNode.add(book);
        book = new DefaultMutableTreeNode
            ("The Java Language Specification");
        aNode.add(book);
    }

    public static Test suite()
    {
        return new TestSuite(TestTreeMethods.class);
    }

    /*
    public void testGettingAllTreeNodesTop()
    {
    List list = pTreeUtils.getAllTreeNodes( tree, tree.getModel(), 0);
    Err.pr( "Got " + list);
    Err.pr( "altogether " + list.size());
    assertTrue(list.size() == 8);
    }
    */

    public void testGettingAllTreeNodes()
    {
        List list = TreeUtils.getAllTreeNodes(tree.getModel(), 1);
        // Err.pr( "Got " + list);
        // Err.pr( "altogether " + list.size());
        assertTrue(list.size() == 7);
    }

    public void testGettingAllTreeNodesTwo()
    {
        List list = TreeUtils.getAllTreeNodes(tree.getModel(), 2);
        // Err.pr( "Got " + list);
        // Err.pr( "altogether " + list.size());
        assertTrue(list.size() == 5);
    }

    public void testGettingAllTreeNodesType()
    {
        List list = TreeUtils.getAllTreeNodes(tree.getModel(),
            StringBuffer.class);
        // Err.pr( "Got " + list);
        // Err.pr( "altogether " + list.size());
        assertTrue(list.size() == 1);
    }

    public void testFindTreeNode()
    {
        DefaultMutableTreeNode node = TreeUtils.findTreeNode(tree.getModel(),
            anObject);
        // Err.pr( "Got " + node);
        assertTrue(node == aNode);
    }
}
