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
package org.strandz.store.fault;

import org.strandz.lgpl.store.XMLFileData;

import java.util.Collection;

public class XMLFaultData extends XMLFileData
    implements FaultQueries
{
    private static final String testFileName = "C:\\cml\\data\\fault"
        + "\\XMLTestData.xml";
    private static final String devFileName = "C:\\cml\\data\\fault"
        + "\\XMLDevData.xml";
    private static final String prodFileName = "C:\\cml\\data\\fault"
        + "\\XMLProdData.xml";

    public XMLFaultData(String dbName)
    {
        Class classes[] = FaultData.CLASSES;
        // Print.pr( "Have " + classes.length + " data classes in Pet Store");
        super.setClasses(classes);

        String fileName = convertToFileName(dbName);
        super.setFilename(fileName);
    }

    private String convertToFileName(String dbName)
    {
        String result = dbName;
        if(dbName.equals(FaultApplicationData.TEST))
        {
            result = testFileName;
        }
        else if(dbName.equals(FaultApplicationData.DEV))
        {
            result = devFileName;
        }
        else if(dbName.equals(FaultApplicationData.PROD))
        {
            result = prodFileName;
        }
        return result;
    }

    public Collection queryAllFaults()
    {
        return (Collection) get(FaultData.FAULT);
    }

    public Collection queryAllSupportCalls()
    {
        return (Collection) get(FaultData.SUPPORT_CALL);
    }

    public Collection queryAllClientContacts()
    {
        return (Collection) get(FaultData.CLIENT_CONTACT);
    }
}
