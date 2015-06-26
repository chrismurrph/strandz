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
package org.strandz.core.prod.view;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.lgpl.util.CautiousArrayList;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.SdzNote;

import java.util.Iterator;

/**
 * Composite pattern for AbstFieldObj. Used when a SubRecordObj has many lookups. These
 * lookups are also know as children. Note that a Tie uses the terms child and
 * parent the opposite way around.
 */
class CompFieldObj extends CompositeInterf
    implements AbstFieldObj
{
    public void blankoutDisplay(OperationEnum currentOperation, String reason, boolean removeComboItems)
    {
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            ((AbstFieldObj) e.next()).blankoutDisplay( currentOperation, 
                                                       "blankoutDisplay from " + this.getClass().getName() + ", because " + reason,
                                                       removeComboItems);
        }
    }
    
    public void displayLovObjects()
    {
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            ((AbstFieldObj) e.next()).displayLovObjects();
        }
    }

    /**
     * Will all have to be blank for this to be true.
     */
    public boolean displayIsBlank()
    {
        boolean result = true;
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            if(!((AbstFieldObj) e.next()).displayIsBlank())
            {
                result = false;
                break;
            }
        }
        return result;
    }

    /*
    public boolean UIIsBlankOrDefault()
    {
    boolean result = true;
    for( Iterator e = vector.iterator(); e.hasNext();)
    {
    if(! ((AbstFieldObj)e.next()).UIIsBlankOrDefault())
    {
    result = false;
    break;
    }
    }
    return result;
    }
    */
    public void setDisplay(OperationEnum currentOperation,
                           int idx, 
                           Object element,
                           boolean changedValuesOnly, 
                           boolean set,
                           String reason)
    {
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            ((AbstFieldObj) e.next()).setDisplay( currentOperation, idx, element, changedValuesOnly, set, 
                                                 "setDisplay from " + this.getClass().getName() + ", because " + reason);
        }
    }

    public void getDisplay(Object element, boolean adding)
    {
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            AbstFieldObj obj = (AbstFieldObj) e.next();
            // Err.pr( "CompFieldObj.getDisplay() " + obj);
            obj.getDisplay(element, adding);
        }
    }

    public boolean haveFieldsChanged()
    {
        boolean result = false;
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            AbstFieldObj fieldObj = (AbstFieldObj)e.next();
            Err.pr( SdzNote.SET_OBJCOMP_TABLE, "fieldObj to consider: " + fieldObj);
            if(fieldObj.haveFieldsChanged())
            {
                Err.pr( SdzNote.SET_OBJCOMP_TABLE, "fields have changed for " + fieldObj);
                result = true;
                break;
            }
        }
        return result;
    }

    /*
    public void applyDifferences( Object element)
    {
    for( Iterator e = vector.iterator(); e.hasNext();)
    {
    ((AbstFieldObj)e.next()).getDisplay( element, false);
    }
    }
    */
    public void setDisplayEnabled(boolean editable)
    {
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            ((AbstFieldObj) e.next()).setDisplayEnabled(editable);
        }
    }

    public CautiousArrayList getAdapters(CautiousArrayList v, int visualType, boolean tableOnly)
    {
        CautiousArrayList ret = new CautiousArrayList();
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            AbstFieldObj abstObj = (AbstFieldObj) e.next();
            ret.addAll(abstObj.getAdapters(v, visualType, tableOnly));
        }
        return ret;
    }

    public void fillAdaptersFlatObjectsTable(Object leftElement)
    {
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            ((AbstFieldObj) e.next()).fillAdaptersFlatObjectsTable(leftElement);
        }
    }
    
    public Object getDerivedAt( int idx, Object element)
    {
        for(Iterator e = vector.iterator(); e.hasNext();)
        {
            ((AbstFieldObj) e.next()).getDerivedAt( idx, element);
        }
        return null;
    }
    
    /*
    public void backgroundAdd()
    {
    for( Iterator e = vector.iterator(); e.hasNext();)
    {
    ((AbstFieldObj)e.next()).backgroundAdd();
    }
    }
    */
} // end class CompositeObj
