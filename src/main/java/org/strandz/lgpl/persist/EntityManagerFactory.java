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
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Assert;

import java.util.Properties;

public class EntityManagerFactory
{
    public static SdzEntityManagerI createNullSdzEMI()
    {
        SdzEntityManagerI result;
        result = new NullEntityManager();
        Err.pr(SdzNote.EMP_ERRORS, "Created a SdzEntityManagerI: " + result);
        return result;
    }
        
    public static SdzEntityManagerI createSdzEMI( MemoryData memoryData)
    {
        SdzEntityManagerI result;
        result = new MemoryEntityManager( memoryData);
        Err.pr(SdzNote.EMP_ERRORS, "Created a SdzEntityManagerI: " + result);
        return result;
    }

    public static SdzEntityManagerI createSdzEMI(
        ORMTypeEnum enumId, String connectionFileNameOrURL, Properties properties)
    {
        SdzEntityManagerI result = null;
        Assert.oneOnly(connectionFileNameOrURL, properties);
        if(enumId == ORMTypeEnum.JDO)
        {
            result = JDOEntityManagerFactory.createSdzEMI( enumId, connectionFileNameOrURL, properties);
        }
        else if(enumId.isCayenne())
        {
            result = CayenneEntityManagerFactory.createSdzEMI( enumId, connectionFileNameOrURL, properties);
        }
        else
        {
            Err.pr( "connectionFileNameOrURL: " + connectionFileNameOrURL);
            Print.prProperties( properties);
            Err.error( "Do not support an ORM of type " + enumId);
        }
        Err.pr(SdzNote.EMP_ERRORS, "Created a SdzEntityManagerI: " + result);
        return result;
    }

}
