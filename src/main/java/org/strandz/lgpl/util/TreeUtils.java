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
package org.strandz.lgpl.util;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Enumeration;

/**
 * This static class has all the small utility methods used to do with
 * JTree. Many could just as easily be performed by using actual JTree
 * methods!
 *
 * @author Chris Murphy
 */
public class TreeUtils
{
    private static int treeNodeLevel;

    /**
     * Get a flat list of all the treeNodes where the
     * userObject is of a certain class
     *
     * @param model
     * @param clazz
     * @return flattened list of treeNodes
     */
    public static List getAllTreeNodes(TreeModel model, Class clazz)
    {
        return getAllTreeNodes(model, 1, clazz, null);
    }

    /**
     * Get a flat list of all the treeNodes where the
     * userObject is of a certain class, starting from the passed in treeNode
     *
     * @param model tree's model
     * @param clazz if not null, only get these types of objects
     * @param root  if not null, where to start recursing from
     * @return flattened list of treeNodes
     */
    public static List getAllTreeNodes(TreeModel model,
                                       Class clazz,
                                       DefaultMutableTreeNode root)
    {
        return getAllTreeNodes(model, 1, clazz, root);
    }

    /**
     * Get a flat list of all the treeNodes
     *
     * @param model tree's model
     * @return flattened list of treeNodes
     */
    public static List getAllTreeNodes(TreeModel model)
    {
        return getAllTreeNodes(model, 1, null, null);
    }

    /**
     * Get a flat list of all the treeNodes at or below a certain level, starting
     * from the topmost treeNode.
     *
     * @param model      tree's model
     * @param startLevel method will not look at anything above this level
     * @return flattened list of treeNodes
     */
    public static List getAllTreeNodes(TreeModel model,
                                       int startLevel)
    {
        return getAllTreeNodes(model, startLevel, null, null);
    }

    /**
     * Get a flat list of all the treeNodes at or below a certain level, where the
     * userObject is of a certain class, starting from the topmost treeNode.
     *
     * @param model      tree's model
     * @param startLevel method will not look at anything above this level
     * @param clazz      if not null, only get these types of objects
     * @return flattened list of treeNodes
     */
    public static List getAllTreeNodes(TreeModel model,
                                       int startLevel,
                                       Class clazz)
    {
        return getAllTreeNodes(model, startLevel, clazz, null);
    }

    /**
     * Get a flat list of all the treeNodes at or below a certain level, where the
     * userObject is of a certain class, starting from a particular root, or the
     * topmost treeNode if root is null.
     *
     * @param model      tree's model
     * @param startLevel method will not look at anything above this level
     * @param clazz      if not null, only get these types of objects
     * @param root       if not null, where to start recursing from
     * @return flattened list of treeNodes
     */
    public static List getAllTreeNodes(TreeModel model,
                                       int startLevel,
                                       Class clazz,
                                       DefaultMutableTreeNode root)
    {
        List nodesResult = new ArrayList();
        // DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Err.pr( "^^ model is type: " + model.getClass().getName());
        if(root == null)
        {
            root = (DefaultMutableTreeNode) model.getRoot();
        }
        treeNodeLevel = 0;
        return getAllTreeNodes(model, root, nodesResult, startLevel, clazz);
    }

    private static List getAllTreeNodes(TreeModel model,
                                        DefaultMutableTreeNode parent,
                                        List nodesResult,
                                        int startLevel,
                                        Class clazz)
    {
        treeNodeLevel++;
        if(parent != null)
        {
            for(int i = 0; i < model.getChildCount(parent); i++)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    model.getChild(parent, i);
                nodesResult = getAllTreeNodes(model, node, nodesResult, startLevel,
                    clazz);
                if(treeNodeLevel >= startLevel)
                {
                    if(clazz == null || node.getUserObject().getClass() == clazz)
                    {
                        // Err.pr( "At treeNodeLevel: " + treeNodeLevel + " will add " + node);
                        nodesResult.add(node);
                    }
                }
            }
        }
        treeNodeLevel--;
        return nodesResult;
    }

    /**
     * Given an object, tell the tree and the user that this is the one that is currently
     * selected. As part of doing this the relevant treeNode should be scrolled to.
     *
     * @param tree  tree whose userObject we want to see selected
     * @param model tree's model
     * @param obj   where we want to go, in getUserObject terms
     */
    public static void setSelectedTreeNode(JTree tree,
                                           TreeModel model,
                                           Object obj)
    {
        DefaultMutableTreeNode node = findTreeNode(model, obj);
        TreeNode treeNodes[] = null;
        if(model instanceof DefaultTreeModel)
        {
            treeNodes = ((DefaultTreeModel) model).getPathToRoot(node);
        }
        else
        {
            Err.error("Need to code a getPathToRoot() method in "
                + model.getClass().getName());
        }

        // Print.prArray( treeNodes);
        TreePath treePath = new TreePath(treeNodes);
        // Err.pr( "Path to root is " + treePath);
        // Err.pr( "root is " + model.getRoot());
        tree.scrollPathToVisible(treePath);
        tree.setSelectionPath(treePath);
        ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
        // Err.pr( "S/now have selected " + treePath);
    }

    /**
     * Given a list of treeNodes, return those which are at the same level. May be
     * many at the same level
     *
     * @param multipleTreeNodes
     * @return list of nodes at the lowest level
     */
    public static List findTreeNodesWithLowestLevel(List multipleTreeNodes)
    {
        List result = new ArrayList();
        int lowest = 99;
        for(Iterator iter = multipleTreeNodes.iterator(); iter.hasNext();)
        {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) iter.next();
            int level = treeNode.getLevel();
            if(level < lowest)
            {
                lowest = level;
                result.clear();
                result.add(treeNode);
            }
            else if(level == lowest)
            {
                result.add(treeNode);
            }
        }
        return result;
    }

    /**
     * Given an obj, find the treeNode that holds it.
     *
     * @param model tree's model
     * @param obj   object we need to find the treeNode for
     * @return treeNode that has obj as its getUserObject()
     */
    public static DefaultMutableTreeNode findTreeNode(TreeModel model,
                                                      Object obj)
    {
        DefaultMutableTreeNode result = null;
        List nodesResult = new ArrayList();
        //DefaultMutableTreeNode parent = (DefaultMutableTreeNode) model.getRoot();
        nodesResult = getAllTreeNodes(model);
        for(Iterator iter = nodesResult.iterator(); iter.hasNext();)
        {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) iter.next();
            Object userObj = treeNode.getUserObject();
            /*
            if(debug)
            {
            Err.pr( "Will cf userObj: " + userObj);
            Err.pr( "With obj: " + obj);
            if(userObj.toString().equals( "tfEmail"))
            {
            Err.debug();
            }
            }
            */
            if(obj.equals(userObj))
            {
                result = treeNode;
                break;
            }
        }
        /*
        * Happens when navigate to a different Context which has a
        * different tree model.
        if(result == null)
        {
        Err.error( "Did not find a " + obj.getClass().getName() +
        "(has paneIndex gone to -1 ??)");
        }
        */
        return result;
    }

    /**
     * If an explicit pick has not been done then null will be returned.
     *
     * @param tree
     * @return
     */
    public static DefaultMutableTreeNode pickFirstIfNone(JTree tree)
    {
        DefaultMutableTreeNode result = null;
        // Err.pr( "In pickFirstIfNone for " + tree);
        if(tree.isSelectionEmpty())
        {
            // Err.pr( "pickFirstIfNone isSelectionEmpty true, SO NOW SELECTED FIRST");
            tree.setSelectionRow(0);
        }
        if(tree.getSelectionCount() != 1)
        {// Not an error - user may have selected another application and
            // then gone back to this one.
            // Err.error( "Expected one row to be selected");
        }
        else
        {
            TreePath treePath = tree.getSelectionPath();
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            // Err.pr( "pickFirstIfNone to return " + treeNode);
            result = treeNode;
        }
        return result;
    }
    
    public static void expandAll( JTree tree, DefaultMutableTreeNode treeNode)
    {
        Enumeration en = treeNode.breadthFirstEnumeration();
        while(en.hasMoreElements())
        {
            DefaultMutableTreeNode treeN = (DefaultMutableTreeNode) en.nextElement();
            tree.expandPath( new TreePath( treeN.getPath()));
        }
    }

    /**
     * The definition of collapsing and expanding is like what you can do
     * with your mouse on that treeNode. This method makes sure that treeNodes
     * of a particular type are displayed, but everthing below them is collapsed.
     * Just telling a treeNode to be collapsed will at the same time make it
     * visible.
     * Have yet to test with cells that contains cells - answer is that goes
     * to the deepest cell which is the right thing to do.
     * 
     * @param tree
     * @param model
     * @param expandedTo
     */
    public static void expandCollapseTree(JTree tree, TreeModel model, Class expandedTo)
    {
        Enumeration en = ((DefaultMutableTreeNode) model.getRoot()).breadthFirstEnumeration();
        while(en.hasMoreElements())
        {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) en.nextElement();
            Object userObj = treeNode.getUserObject();
            if(userObj == null)
            {
            }
            else
            {
                // Err.pr( "Looking at a " + userObj.getClass().getName());
                if(Utils.instanceOf(userObj, expandedTo))
                {
                    TreePath treePath = new TreePath(treeNode.getPath());
                    tree.collapsePath(treePath);
                    // Err.pr( "Expanded " + userObj);
                }
            }
        }
    }
}
