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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MutableInteger;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.note.SdzNote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * When a Block needs to perform methods that require knowledge of
 * how it is related to other blocks then it uses this class, which
 * it contains. Thus the state machine can use this class to implement
 * all its rules - such as not being able to navigate more than 1 level
 * down.
 * <p/>
 * If a Node does not have any ties, then it will only
 * feature in this object as a singularNode
 */
abstract public class TiesManager
{
    // all Ties from all Nodes
    protected List ties = new ArrayList();
    boolean gotAllTies = false; // subclasses require
    /**
     * isChildOf uses this as its level
     */
    private int isChildLevel = 0;
    private Class enforceType;

    public TiesManager(Class enforceType)
    {
        Err.pr( SdzNote.TIES_ENFORCE_TYPE, "enforceType constructed to <" + enforceType +
            "> in <" + this.getClass().getName() + ">");
        this.enforceType = enforceType;
    }

    public void purge( Class newEnforceType)
    {
        ties = new ArrayList();
        gotAllTies = false;
        isChildLevel = 0;
        Err.pr( SdzNote.TIES_ENFORCE_TYPE, "Changing enforceType in purge from <" +
            enforceType + "> to <" + newEnforceType + "> in <" + this.getClass().getName() + ">");
        enforceType = newEnforceType;
    }

    public void setEnforceType( Class newEnforceType)
    {
        Err.pr( SdzNote.TIES_ENFORCE_TYPE, "Changing enforceType from <" +
            enforceType + "> to <" + newEnforceType + "> in <" + this.getClass().getName() + ">");
        this.enforceType = newEnforceType;
    }

    public Class getEnforceType()
    {
        return this.enforceType;
    }

    private void chkEnforceType(Object obj)
    {
        if(enforceType == null)
        {
            Err.error("In TiesManager, do not have a type to enforce");
        }
        if(!Utils.instanceOf(obj, enforceType))
        {
            if(obj != null)
            {
                Err.error("Expected " + enforceType + " but got " + obj.getClass());
            }
        }
    }

    /**
     * From an Object, get all its Ties information
     */
    public void addTies(List ties)
    {
        Tie tie;
        for(Iterator en = ties.iterator(); en.hasNext();)
        {
            tie = (Tie) en.next();
            // Err.pr("### added to TiesManager is: " + tie.parentObj + "," + tie.childObj);
            chkEnforceType(tie.parentObj);
            chkEnforceType(tie.childObj);
            this.ties.add(tie);
        }
    }

    /**
     * Called (in consumeNodesIntoRT) straight after all the ties from all the nodes
     * have been collected in here.
     * <p/>
     * In AdaptersList.validate() will have already made sure that all the fields
     * in the schema class exist as prescribed when created Tie. FALSE - only
     * does for attributes, which are fields that will SEE.
     * <p/>
     * Only current function is to check the parent fields (and child fields when
     * references implemented) are of a List type we can support. NO - this
     * will already have been done when constructed a Tie.
     * <p/>
     * NO CURRENT FUNCTION!
     */
    public void setConsistent(boolean b)
    {
        gotAllTies = b;
    }

    public boolean isTopLevel(Object obj)
    {
        //Err.pr( "@-@ Checking if at top level for " + obj);
        chkEnforceType(obj);
        if(!gotAllTies)
        {
            Err.error("Cannot tell if Top Level or not until all Nodes known about");
        }
        if(obj == null)
        {
            Err.error("isTopLevel not written for null objects");
        }

        // simply check obj does not exist as a child in ties
        boolean objIsChild = true;
        Tie tie;
        int i = 0;
        for(Iterator en = ties.iterator(); en.hasNext(); i++)
        {
            tie = (Tie) en.next();
            //Err.pr( "@-@ Looking thru Tie.isTopLevel at " + tie.childObj + " whose parent is " + tie.parentObj);
            if(!Utils.instanceOf(obj, tie.childObj.getClass()))
            {
                Err.error(obj.getClass() + " is not an instance of " + tie.childObj.getClass());
            }
            if(tie.parentObj == null)
            {
                if(tie.childObj == obj)
                {
                    objIsChild = false;
                    //Err.pr( "@-@ Found master MATCH " + tie.childObj + " with " + obj);
                    i++; //tacky
                    break;
                }
                else if(tie.childObj.equals(obj))
                {
                    //Err.error( "@-@ Trying to cf a the former " + tie.childObj + " with newly created " + obj);
                }
                else
                {
                    //Err.pr( "@-@ No match " + tie.childObj + " with " + obj);
                }
            }
        }
        if(objIsChild && i == 0) //don't actually need both, but will always be both
        {
            //Err.pr( "@-@ No ties in TiesManager when trying to find if this is top: " + obj);
        }
        return !objIsChild;
    }

    /**
     * Get all of the next level up for a particular Node
     */
    public Iterator getParentsIterator(Object obj)
    {
        chkEnforceType(obj);
        if(!gotAllTies)
        {
            Err.error("Cannot getParents until all Nodes known about");
        }

        ArrayList v = new ArrayList();
        Tie tie;
        for(Iterator en = ties.iterator(); en.hasNext();)
        {
            tie = (Tie) en.next();
            if(obj == tie.childObj && tie.parentObj != null)
            {
                v.add(tie.parentObj);
            }
        }
        return v.iterator();
    }

    /**
     * Get all of the next level down for a particular Node
     */
    public Iterator getChildren(Object obj)
    {
        chkEnforceType(obj);
        if(obj == null)
        {
            Err.error("isChildOf: obj param cannot be null");
        }
        if(!gotAllTies)
        {
            Err.error("Cannot getChildren until all Nodes known about");
        }

        ArrayList v = new ArrayList();
        Tie tie;
        // new MessageDlg("To find children for " + obj);
        for(Iterator en = ties.iterator(); en.hasNext();)
        {
            tie = (Tie) en.next();
            // new MessageDlg("Looking at parent " + tie.parentObj);
            if(obj == tie.parentObj)
            {
                v.add(tie.childObj);
            }
        }
        // new MessageDlg("Object " + obj + " has " + v.size() + " children");
        return v.iterator();
    }

    /*
    boolean isAnyMasterOf( Node child, Node perhapsMaster)
    {
    if(! gotAllTies)
    {
    Err.error("Cannot tell master relationships until all Nodes known about");
    }
    Err.error("not implemented");
    *
    * easy enough - same as getKingOfTheHills
    *
    return false;
    }
    */

    public boolean gotAllTies()
    {
        return gotAllTies;
    }

    /**
     * All the unattached
     */
    public Iterator getSingles()
    {
        if(!gotAllTies)
        {
            Err.error("Cannot getSingles until all Nodes known about");
        }

        ArrayList v = new ArrayList();
        Tie tie;
        for(Iterator en = ties.iterator(); en.hasNext();)
        {
            tie = (Tie) en.next();
            // new MessageDlg("parentObj will look at is " + tie.parentObj + ", for " + tie.childObj);
            if(null == tie.parentObj)
            {
                // new MessageDlg("Single getting is " + tie.childObj);
                v.add(tie.childObj);
            }
        }
        return v.iterator();
    }

    /**
     * bottoms paramter used as ArrayList when isChildOf calls itself. When called
     * from outside will have only one element in it.
     */
    public boolean isChildOf(Object top, ArrayList bottoms, MutableInteger levelsMoved)
    {
        if(top == null)
        {
            Err.error("isChildOf: Top cannot be null");
        }
        if(bottoms == null)
        {
            Err.error("isChildOf: Bottoms cannot be null");
        }
        chkEnforceType(top);
        isChildLevel++;
        if(!gotAllTies)
        {
            Err.error("Cannot isChildOf until all Blocks known about");
        }

        Object obj;
        Tie tie;
        boolean parentIsTop = false;
        ArrayList parentObjs = new ArrayList();
        for(Iterator e = bottoms.iterator(); e.hasNext();)
        {
            obj = e.next();
            // Err.pr("-^-obj have got is " + obj);
            for(Iterator en = ties.iterator(); en.hasNext();)
            {
                tie = (Tie) en.next();
                if(obj == tie.childObj)
                {
                    // Err.pr("-^-Where " + obj + " is child parent is " + tie.parentObj);
                    if(tie.parentObj == top)
                    {
                        parentIsTop = true;
                        break;
                    }
                    if(tie.parentObj != null)
                    {
                        parentObjs.add(tie.parentObj);
                    }
                }
            }
        }
        if(parentIsTop)
        {
            levelsMoved.setValue(isChildLevel);
            isChildLevel = 0;
            // Err.pr("-^- To return True, levelsMoved is now " + levelsMoved.getValue());
            return true;
        }
        else
        {
            if(parentObjs.isEmpty())
            {
                isChildLevel = 0;
                // Err.pr("-^- To return True, isChildLevel will now be " + isChildLevel);
                return false;
            }
            else
            {
                // Err.pr("-^- To next level, isChildLevel at " + isChildLevel);
                return isChildOf(top, parentObjs, levelsMoved);
            }
        }
    }
    /**
     * Get all of the highest level independents for a particular Node.
     * NOT REQUIRED !
     */
    /*
    ArrayList getKingOfTheHills( ArrayList nodes)
    {
    if(! gotAllTies)
    {
    Err.error("Cannot getKingOfTheHills until all Nodes known about");
    }
    kingHillParents.clear();
    if(kingHillLevel == 0)
    {
    kingHillResults.clear();
    }
    kingHillLevel++;
    Err.pr("The next level up we will now investigate is " + kingHillLevel);
    Tie tie;
    Node node;
    for(Iterator e=nodes.iterator(); e.hasNext();)
    {
    node = (Node)e.next();
    boolean nodeChildSomewhere = false;
    for(Iterator en=ties.iterator(); en.hasNext();)
    {
    tie = (Tie)en.next();
    if(node == tie.childObj)
    {
    kingHillParents.add( tie.parentObj);
    Err.pr("node " + node + " has parent node " + tie.parentObj);
    nodeChildSomewhere = true;
    }
    }
    if(nodeChildSomewhere == false)
    {
    //Notice that what return (a few lines down) does not need to
    //be used here, except where first time in find have no parents.
    kingHillResults.add( node);
    }
    }
    if(kingHillParents.isEmpty())
    {
    //In case where will really (not recursively) return here there
    //have been no parents found at all
    if(kingHillLevel == 1)
    {
    kingHillLevel--; //bring down to 0 for next invocation
    Err.pr("1./ kingHillLevel now back to " + kingHillLevel);
    }
    return kingHillResults; //only last return ever used,
    //so doesn't actually matter
    //what return here - crap - see other comments
    }
    else
    {
    getKingOfTheHills( kingHillParents);
    kingHillLevel--;
    }
    if(kingHillLevel != 1)
    {
    Err.error("King Hill Level incorrect");
    }
    else
    {
    kingHillLevel--; //bring down to 0 for next invocation
    Err.pr("2./ kingHillLevel now back to " + kingHillLevel);
    }
    return kingHillResults;
    }
    */

    /*
    private ArrayList kingHillResults = new ArrayList();
    private ArrayList kingHillParents = new ArrayList();
    private int kingHillLevel = 0;
    */

}
