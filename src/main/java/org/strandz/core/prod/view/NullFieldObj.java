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

import java.util.ArrayList;
import java.util.List;

/**
 * Composite pattern. This null is used by a SubRecordObj that has no children.
 * Such a SubRecordObj does not have any lookups, it's the end of the line.
 */
class NullFieldObj implements AbstFieldObj
{
    public void blankoutDisplay(OperationEnum currentOperation, String reason, boolean removeComboItems)
    {
    }
    
    public void displayLovObjects()
    {
    }

    public boolean displayIsBlank()
    {
        return true;
    }

    // public boolean DisplayIsBlankOrDefault(){ return true;}
    public void setDisplay(OperationEnum currentOperation,
                           int idx, 
                           Object element,
                           boolean changedValuesOnly, 
                           boolean set,
                           String reason)
    {
    }

    public void getDisplay(
        Object element, boolean changedValuesOnly)
    {
    }

    public boolean haveFieldsChanged()
    {
        return false;
    }

    // public void applyDifferences( Object element){}
    public void setDisplayEnabled(boolean enabled)
    {
    }

    public List getOutputAttributes()
    {
        return new ArrayList();
    }

    public CautiousArrayList getAdapters(CautiousArrayList v, int visualType, boolean tableOnly)
    {
        return null;
    }

    public void fillAdaptersFlatObjectsTable(Object leftElement)
    {
    }
    
    public Object getDerivedAt( int idx, Object element)
    {
        return null;        
    }
    // public void backgroundAdd(){}
} // end class
