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

import org.strandz.core.domain.IdEnum;
import org.strandz.lgpl.util.Err;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A Block only needs to contain one SubRecordObj, as the composite pattern (used
 * recursively on children) will apply to all the RecordObjs that can be looked
 * up from this one central point. Trouble with this is that Block will need to
 * make a call to create all these related objects. This is what Builder does.
 * A block will make a call to createRecordObj which will then go to
 * createAbstObj which will create a FieldObj or TableObj. Inside SubRecordObj the
 * magic of recursion occurs in its constructor by calling createAbstObj if it
 * can see that its going to have children.
 */
public class Builder
{
    // public static final int TABLE = 1;
    // public static final int FIELD = 2;
    //private static NodeTableModelImplI ntmii_s;

    public static FieldObj createTableObj(SubRecordObj parent,
                                          ViewBlockI block,
                                          CreatableI instantiable,
                                          NodeTableModelImplI ntmii)
    {
        //ntmii_s = ntmii;
        return (FieldObj) createRecordObj(IdEnum.TABLE, parent, block, instantiable,
            ntmii);
    }

    public static SubRecordObj createRecordObj(IdEnum id,
                                               SubRecordObj parent,
                                               ViewBlockI block,
                                               CreatableI instantiable,
                                               NodeTableModelImplI ntmii)
    {
        ArrayList v = new ArrayList();
        v.add(instantiable);
        return (SubRecordObj) createAbstObj(id, parent, block, v, ntmii);
    }

    static AbstInterf createAbstObj(IdEnum id,
                                    SubRecordObj parent,
                                    ViewBlockI block,
                                    ArrayList instantiables,
                                    NodeTableModelImplI ntmii)
    {
        AbstInterf self = null;
        if(instantiables.size() > 1)
        {
            // Err.error("Not yet equipped for > 1 at any given level");
            /*
            * makeComposite can call this many times with an instantiables of
            * size 1 many times. (Which is same thing as doing size == 1 line, so
            * do size == 1 line!) Will then finally create the composite, fill
            * it and pass it back
            */
            self = makeComposite(id, parent, block, instantiables, ntmii);
        }
        else if(instantiables.size() == 1)
        {
            self = new FieldObj(id, parent, block,
                ((CreatableI) instantiables.get(0)), ntmii);
        }
        else
        {
            Err.error("No instantiable!");
        }
        return self;
    }

    /**
     * Create a Composite, create all the RecordObjs from ArrayList
     * instantiables, and add into composite, and return composite.
     */
    private static CompositeInterf makeComposite(IdEnum id,
                                                 SubRecordObj parent,
                                                 ViewBlockI block,
                                                 ArrayList instantiables,
                                                 NodeTableModelImplI ntmii)
    {
        CompositeInterf compositeInterf = new CompFieldObj();
        CreatableI inst;
        SubRecordObj chiefObj = null;
        for(Iterator e = instantiables.iterator(); e.hasNext();)
        {
            inst = (CreatableI) e.next();
            if(inst == null)
            {
                Err.error("Didn't expect instantiable to be null");
            }
            else
            {
                chiefObj = new FieldObj(id, parent, block, inst, ntmii);
                compositeInterf.add(chiefObj);
            }
        }
        return compositeInterf;
    }
} // end class
