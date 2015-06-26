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
package org.strandz.core.interf;

import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * pUtil exists in many packages. It always contains utility methods specific to that
 * package
 *
 * @author Chris Murphy
 */
public class InterfUtils
{
    /**
     * Return a subset of inList, shorter according to how the fields of each
     * element match with the itemValues of the attributes in queryAttributes.
     * ie. we are looking for matches. Important to note that a new ArrayList
     * is always returned. (Collections returned from JDO cannot be altered).
     * Could also do this matching with a query to improve efficiency (would need
     * all kinds of dynamic query kind of stuff).
     */
    public static List refineFromMatchingValues
        (
            Collection<Object> inList, List<RuntimeAttribute> queryAttributes
        )
    {
        List result;
        if(queryAttributes.isEmpty())
        {
            result = new ArrayList<Object>(inList);
        }
        else
        {
            result = new ArrayList();
            for(Iterator iter = inList.iterator(); iter.hasNext();)
            {
                Object element = iter.next();
                boolean matchingRecord = true;
                // Cell lastCell = null;
                for(Iterator iterator = queryAttributes.iterator(); iterator.hasNext();)
                {
                    RuntimeAttribute attribute = (RuntimeAttribute) iterator.next();
                    debugRefine();
                    if(!attribute.itemMatchesData(element))
                    {
                        matchingRecord = false;
                        break;
                    }
                    else
                    {
                        matchingRecord = true;
                    }
                    // lastCell = thisCell;
                }
                if(matchingRecord)
                {
                    result.add(element);
                }
            }
        }
        return result;
    }
    
    private static void debugRefine()
    {
        // Print.pr( "attribute: " + attribute);
        //Cell thisCell = attribute.getCell();
        /*
        if(lastCell != null)
        {
        if(thisCell != lastCell)
        {
        Session.error( "Have not yet coded for looking up to another cell");
        }
        }
        if(attribute.getName().equals( "shiftPreference Name"))
        {
        Tie tie = attribute.getAdapter().getTie();
        Err.pr( tie);
        //These FieldObjs
        Err.pr( "parent: " + tie.getParent());
        Err.pr( "child: " + tie.getChild());
        //FieldObj fieldObj = (FieldObj)tie.getParent();
        Err.pr( "Tie gives a: " + tie.getFieldValue( element).getClass());
        Object obj = tie.getFieldValue( element);
        //Err.stack();
        }
        */
    }

    /* 10/05/05 This not being used AND Strandz no longer depending on JDO
    public static Extent refineFromMatchingValues
        (
        Extent inList, List queryAttributes
        )
    {
      Session.error( "Do a query and get a collection rather than this (for now)");
      for(Iterator iterator = queryAttributes.iterator(); iterator.hasNext();)
      {
        ItemMatcher attribute = (ItemMatcher)iterator.next();
      }
      return inList;
    }
    */

    public static String formAttributeName(Cell cell, String fieldName)
    {
        String result = null;
        String doClassName = cell.getClazz().getClassObject().getName();
        doClassName = NameUtils.findClassAtEndOfPackage(doClassName);
        doClassName = NameUtils.lowerCaseFirstChar(doClassName);
        if(fieldName != null)
        {
            result = doClassName + " " + fieldName;
        }
        else
        {// Give no name. It is toShow()s job to make it not come out
            // as a null
            // result = doClassName + Attribute.NO_FIELD_NAME_STR;
        }
        return result;
    }

    /**
     * In response to TestTable.testCanMoveFromInvalidChild.
     * Investigate whether <param>node</param>
     *
     * @param node
     */
    /*
    public static void debugMasterDetail( Node node)
    {
      Block block = node.getBlock();

    }
    */
    public static List collectNonVisualTableAttributes(Node node)
    {
        List result = Utils.getSubList(node.getAttributes(), NonVisualTableAttribute.class);
        return result;
    }
}
