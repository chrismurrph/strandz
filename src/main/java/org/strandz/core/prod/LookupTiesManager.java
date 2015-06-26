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
package org.strandz.core.prod;

import org.strandz.core.domain.DOAdapter;
import org.strandz.core.domain.LookupTiesManagerI;
import org.strandz.core.prod.view.FieldObj;
import org.strandz.lgpl.extent.OrphanTie;
import org.strandz.lgpl.extent.Tie;
import org.strandz.lgpl.extent.TiesManager;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LookupTiesManager extends TiesManager
    implements LookupTiesManagerI
{

    public LookupTiesManager(Class enforceType)
    {
        super(enforceType);
        Err.pr(SdzNote.GENERIC, "Constructed LookupTiesManager enforcing type " + enforceType);
    }

    private List recursiveGetRouteTo(
        List tiesResult, Tie targetTie, Class objType)
    {
        Class targetChildType = ((FieldObj) targetTie.getChild()).getInstantiable().getClassObject();
        // Err.pr( "Child side of target type is " + targetChildType);
        for(Iterator en = ties.iterator(); en.hasNext();)
        {
            Tie tie = (Tie) en.next();
            if(!(tie instanceof OrphanTie))
            {
                if(tie.getChild() != null)
                {// Err.pr( "tie has child of type " + tie.getChild().getClass().getName());
                }
                else
                {
                    Err.pr("No child " + tie);
                }
                if(tie.getParent() != null)
                {// Err.pr( "tie has parent of type " + tie.getParent().getClass().getName());
                }
                else
                {
                    Err.pr("No parent " + tie);
                }
                if(((FieldObj) tie.getChild()).getInstantiable().getClassObject() == objType)
                {
                    // Err.pr( "Found a tie with child type " + objType);
                    Class tieParentType = ((FieldObj) tie.getParent()).getInstantiable().getClassObject();
                    // Err.pr( "It has parent type " + tieParentType);
                    if(targetChildType == tieParentType)
                    {
                        tiesResult.add(tie);

                        List list = new ArrayList();
                        list.addAll(tiesResult);
                        return recursiveGetRouteTo(list, targetTie,
                            ((FieldObj) tie.getParent()).getInstantiable().getClassObject());
                    }
                    else
                    {
                    }
                }
            }
        }
        return tiesResult;
    }

    /**
     * Get the route to an itemAdapter from a particular data object type.
     */
    public List getRouteTo(DOAdapter ad, Class objType)
    {
        List result = new ArrayList();
        Tie tie = ad.getTie();
        if(tie != null)
        {
            if(objType == ((FieldObj) tie.getChild()).getInstantiable().getClassObject())
            {
                result.add(tie);
            }
            else
            {
                result = recursiveGetRouteTo(result, tie, objType);
                result.add(tie);
            }
        }
        return result;
    }
}
