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
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.note.SdzNote;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * In a detail block there is one SubRecordObj which may be the detail of many
 * masters - the many masters bit is why we need lots of Ties. This object
 * is contained in a block. Even thou every Cell becomes a SubRecordObj,
 * the SubRecordObj we are refering to here is one that is not at the top level,
 * and is looked up by none.
 * <p/>
 * This class does the detail syncing when the master changes to another
 * record, and sets the data of any detail which is done by reference.
 * <p/>
 * The Ties that an Object may have with other Objects are kept here. These ties
 * reach upwards (ie. originally there was only one tie). ie./ STORE WITH
 * DEPENDENT SIDE OBJECT.
 */
public class Ties
{
    private HasCombinationExtent dependentObj; // the dependent side object which may have many ties
    // reaching upwards
    private ArrayList ties = new ArrayList();

    public Ties(HasCombinationExtent obj)
    {
        // Err.pr( "$$ CREATING Ties");
        if(obj == null)
        {
            Err.error("Ties constructor cannot have a null obj");
        }
        this.dependentObj = obj;
    }

    public Iterator tieiterator()
    {
        return ties.iterator();
    }

    public boolean isEmptyOfTies()
    {
        return ties.isEmpty();
    }

    /**
     * Alot of validation has disappeared from here, as Node initially takes the
     * call to setIndependent. We can now change the end objects of the Tie
     * elements to Blocks, which support HasCombinationExtent, which Ties requires.
     */
    public void addTie(Tie tie)
    {
        /*
        if(!ties.isEmpty())
        {
        Err.error("TIME TO CHANGE addIndependent(): only 1 independent currently allowed per obj");
        }
        */
        ties.add(tie);
    }

    /**
     * Currently singular. Gets the parent block
     */
    /*
    Block getIndependent()
    {
    Err.error("getIndependent() to be banished");
    if(ties.isEmpty())
    {
    return null;
    }
    Object parent = ((Tie)ties.firstElement()).parentObj;
    return parent.block;
    }
    */
    private Tie getTie(Object parent)
    {
        Tie tie;
        Tie result = null;
        int count = 0;
        // new MessageDlg("parent wanting: " + parent);
        for(Iterator e = ties.iterator(); e.hasNext();)
        {
            tie = (Tie) e.next();
            // new MessageDlg("of " + tie.childObj + " the parent object is " + tie.parentObj);
            // new MessageDlg("of " + tie.childObj.getClass() + " the parent object is " + tie.parentObj.getClass());
            if(tie.parentObj == parent)
            {
                result = tie;
                count++;
            }
        }
        if(count != 1)
        {
            Print.prList( ties, "All ties");
            Err.error(
                dependentObj + ":<" + count + ">for the parent obj " + parent
                    + " there must be one tie, instead we have found " + count + " that match with parent <" + 
                        parent + ">");
        }
        return result;
    }

    /**
     * Always called on the child, never the parent.
     * <p/>
     * masterElement - the master object that a child block will need to sync
     * with. The syncing is across one of many ties that a child block will
     * have.
     * parentObj - which parent obj has been navigated to
     * <p/>
     * null DEErrorContainer returned means no error occurred.
     */
    public DEErrorContainer setNewListVector(Object masterElement, Object parentObj)
    {
        DEErrorContainer result = null;
        Tie parentTie = getTie(parentObj);
        /*
        * This will become more complex for 2 reasons:
        * 1./ If have more than one parent, will have to get the vector fields
        *     from the other parents as well (apart from parentObj, which is
        *     the obj that the movement event will have come from). Will need
        *     to form a new vector as a combination of all the other vectors.
        *     Perhaps each tie should always keep an up to date reference to
        *     the current parent object and its vector. Right now would be the
        *     only time need to do it. Would do it for parentObj's tie.
        * 2./ Where a reference relationship exists we will have to dynamically
        *     construct and fill the vector ourselves. From initialData that
        *     must be with the obj, will have to get all the matches where the
        *     parent element itself (masterElement) is equal to the value of the
        *     child field value. To get the matching child field values can either full scan
        *     thru the whole thing, or, if have stored initialData initially as an
        *     index, use the index that will have on the reference field. In
        *     fact, as there are as many index fields as there are fk fields, should
        *     have both a vector and several indicies associated with every
        *     setInitialData call. No - just do full list scan. When it comes to
        *     Extents later on, will be able to use real indexes for large Extents.
        */
        // WHEN uncomment this will get child query happening twice!
        //Err.pr( ">>>setList to be called for child " + dependentObj.getName() + " on extent " +
        // parentTie.dependentExtent);
        DEErrorContainer errCon = parentTie.getDependentExtentErrorContainer();
        if(!errCon.isInError())
        {
            DependentExtent extent = errCon.getDependentExtent();
            extent.setList(masterElement, parentTie);
            Err.pr( SdzNote.NODE_GROUP, ">>>setList BEEN called for child <" + dependentObj.getName() + "> on extent " + errCon.getDependentExtent());
        }
        else
        {
            result = errCon;
            Err.pr( SdzNote.NODE_GROUP, ">>>setList failed for child <" + dependentObj.getName() + ">");
        }
        return result;
    }

    /**
     * Following methods required so that the customiser can call addIndependent()
     * itself. (choosePotentialTie() will actually call it).
     * <p/>
     * Will examine the instantiable of all nodes that are currently in the
     * builder environment. For now can fake this by passing a list as
     * parameter. Used for displaying in a Customizer environment, so will
     * collect and return an Iterator of DisplayTie (same as Tie, but has
     * a var to indicate whether display has been reversed or not. This is
     * because same Tie will appear in different formats depending on what
     * Object it is being shown for).
     */

    /**
     * Will examine the instantiable of all nodes that are currently in the
     * builder environment. For now can fake this by passing a list as
     * parameter. Used for displaying in a Customizer environment, so will
     * collect and return an Iterator of DisplayTie (same as Tie, but has
     * a var to indicate whether display has been reversed or not. This is
     * because same Tie will appear in different formats depending on what
     * Object it is being shown for).
     */
    public Iterator getPotentialTies()
    {
        return (new ArrayList()).iterator();
    }

    public Iterator getExistingTies()
    {
        return (new ArrayList()).iterator();
    }

    void choosePotentialTie()
    {
    }

    /**
     * Called as a result of user calling setData on an
     * Cell. You only need to do this for a child that is joined by
     * reference. Here we are creating one/many of those extents that is
     * especially for a Tie.
     */
    public void setDependentExtents(IndependentExtent independentExtent)
    {
        Tie tie;
        for(Iterator e = tieiterator(); e.hasNext();)
        {
            tie = (Tie) e.next();
            if(tie.getType() == Tie.CHILD_REFERENCE)
            {
                /*
                * Know that the dataRecords of Block will not
                * have been overwritten for a child Block.
                */
                // new MessageDlg("To call setDependentExtent for a child reference");
                /*
                * Get to this code as part of setInitialData(). We don't want the
                * structural relationships of data describing objects to be repeated.
                * Can go thru here when reload, and will have to go thru here if have
                * a double-header setup. (As in TestComplexNavigation)
                */
                if(tie.dependentExtent == null)
                {
                    // Err.pr( "%%%   Will call setDependentExtent for tie " + tie);
                    // Err.pr( "%%%   obj.getCombinationExtent(): " + obj.getCombinationExtent());
                    Err.pr( "ReferenceExtent to be created where dep extent comes from " + dependentObj.getName());
                    Err.pr( "\tand tie is " + tie);
                    Err.pr( "\tand hugeList is " + independentExtent);
                    ReferenceExtent refExt = NavExtent.createReference(independentExtent,
                        dependentObj, tie);
                    // Err.pr( "----> NavExtent.createReference gives " + refExt.size());
                    tie.setDependentExtent(refExt);
                }
                else
                {// Err.pr( "%%%   Will NOT call setDependentExtent for tie " + tie);
                }
                /*
                * Need to test double header reloading to see if this is even necessary.
                * Above code doesn't seem to call setList() ie. it is purely structural.
                *
                * We do however want to acknowledge that the data may be completely
                * different.
                else
                {
                tie.getDependentExtent().setList( masterElement, tie);
                }
                */
            }
            else if(tie.getType() == Tie.PARENT_LIST)
            {
                Err.error(
                    "Do not need initialData when the Object is a child in an aggregate list");
                // OR Err.error("initialData can only be provided for top level and reference nodes");
            }
            else
            {
                Err.error("Both not yet supported");
            }
        }
    }
} // end class
