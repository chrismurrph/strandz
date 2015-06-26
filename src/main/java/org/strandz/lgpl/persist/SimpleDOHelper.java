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

public class SimpleDOHelper implements DOHelperI
{
    private static int times;

    public ORMTypeEnum getVersion()
    {
        return ORMTypeEnum.NULL;
    }

    public boolean equalsOrError
        (Object obj1,
         Object obj2)
    {
        return true; //so will never error
    }

    public boolean equalityTest
        (Object master,
         Object ref)
    {
        boolean result = false;
        if(master == null && ref == null)
        {
            result = true;
        }
        else if(master != null && ref != null)
        {
            //result = master.equals( ref);
            result = master == ref;
        }
        else
        {
            result = false;
        }
        if(master != null && master.toString().equals("Noreen Neve"))
        {
            times++;
            Err.pr(SdzNote.CANT_ADD_RS, "(POJO)Noreen: master " + master + " ref " + ref + " res: " + result + " times " + times);
            if(times == 0)
            {
                Err.debug();
            }
        }
        if(result) Err.pr(SdzNote.CANT_ADD_RS, "(POJO)EQ: master " + master + " ref " + ref + " res: " + result);
        return result;
    }
}
