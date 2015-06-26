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
package org.strandz.lgpl.persist;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.jdo.JDOHelper;

public class JDODOHelper implements DOHelperI
{
    public ORMTypeEnum getVersion()
    {
        return ORMTypeEnum.JDO;
    }

    public boolean equalsOrError
        (Object obj1,
         Object obj2)
    {
        return JDOUtils.equals(obj1, obj2);
    }

    public boolean equalityTest
        (Object master,
         Object ref)
    {
        boolean result = jdoEqualityTest(master, ref);
        //Err.pr( master.toString());
        if(SdzNote.CANT_ADD_RS.isVisible())
        {
            if(master != null && master.toString().equals("Noreen Neve"))
                Err.pr(SdzNote.CANT_ADD_RS, " (JDO)Noreen: master " + master + " ref " + ref + " res: " + result);
            if(result) Err.pr(SdzNote.CANT_ADD_RS, " (JDO)EQ: master " + master + " ref " + ref + " res: " + result);
        }
        return result;
    }

    /**
     * Should probably use same comparison techniques as POET uses.
     * Store hugeList as a hash on reference to get big performance
     * improvement. JDOQL should also be an option.
     * <p/>
     * 60/06/2003 Stopped using equals(), instead == to solve problems
     * that got with strays and copy/paste.
     * ie. With strays insert a new master. Thus have a nullary
     * constructed Client. The strays have a nullary constructed Client
     * by definition, so will match with equals.
     * ie. With pasting when have pasted the master and goTo the child
     * syncDetail called which will grab the detail list from the trigger
     * and try to match details with the master. The source details will
     * match.
     * <p/>
     * 14/06/2003 With XML data references from the detail reference list
     * won't match to the master element unless use equals(). Thus need to
     * stop using XML data when have references. See this problem when
     * re-query.
     * <p/>
     * CANNOT ... get round this problem
     * by having a constructedTimes object id - see Client. (Have not yet gone
     * to extent of allowing user to specify own id interface - as
     * downfall of this solution is that equals() will have become equivalent
     * to ==).
     * <p/>
     * JDO solution will be to see if each is PersistenceCapable, and if
     * so use JDOHelper.getObjectId(this). (Although this call returns null
     * for objects that have just been created).
     * <p/>
     * Will using JDO allow both copy/paste, and re-query problems to be solved?
     */
    private static boolean jdoEqualityTest(
        //IndependentExtent hugeList,
        Object master,
        Object ref)
    {
        boolean result = false;
         /**/
        //if(hugeList.getPM() != null && ref instanceof PersistenceCapable)
        {
            /*
            * Not essential now doing JDO after current user event
            if(JDOHelper.isDeleted( ref))
            {
            //Here the detail record contains a reference to a deleted
            //master. The whole record that this detail element is a part
            //of is an orphaned child. It still exists because even thou
            //the Client was deleted, there was not at the same time a
            //cascading delete of the jobs that refer to it
            }
            else
            */
            {
                /*
                Err.pr( "Are cfing persistent capable classes " +
                master + " and " + ref);
                Err.pr( "Of types " +
                master.getClass().getName() + " and " + ref.getClass().getName());
                */
                Object masterId = JDOHelper.getObjectId(master);
                if(masterId == null)
                {
                    // legitimate as happens when have constructed an object but the DB
                    // doesn't yet know about it.
                    // Session.error( "masterId == null");
                    // Back to the normal comparison method I guess!
                    //result = master.equals( ref); NPE
                    result = Utils.equals(master, ref);
                }
                else
                {
                    Object refId = JDOHelper.getObjectId(ref);
                    result = masterId.equals(refId);
                }
            }
        }
        //else
        {
            //pasted into the SimpleDOHelper
        }
        return result;
    }
}
