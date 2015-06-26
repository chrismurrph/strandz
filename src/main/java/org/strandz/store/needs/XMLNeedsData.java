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
package org.strandz.store.needs;

import org.strandz.data.needs.objects.Contact;
import org.strandz.lgpl.store.XMLFileData;
import org.strandz.lgpl.util.DebugList;
import org.strandz.lgpl.util.Err;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class XMLNeedsData extends XMLFileData
    implements NeedsQueries
{
    private static final String wombatFileName = "C:\\cml\\data\\needs"
        + "\\XMLNeedsData.xml";
    private static final String testFileName = "C:\\cml\\data\\needs"
        + "\\XMLTestData.xml";
    private static final String devFileName = "C:\\cml\\data\\needs"
        + "\\XMLDevData.xml";
    private static final String prodFileName = "C:\\cml\\data\\needs"
        + "\\XMLProdData.xml";
    private static final String volsFileName = "C:\\cml\\data\\needs"
        + "\\XMLVolsData.xml";
    private static final FirstSecondNameOrder orderer = new FirstSecondNameOrder();

    public XMLNeedsData()
    {
        this(wombatFileName);
    }

    public XMLNeedsData(String dbName)
    {
        Class classes[] = NeedsData.CLASSES;
        // Print.pr( "Have " + classes.length + " classes");
        super.setClasses(classes);

        String fileName = convertToFileName(dbName);
        super.setFilename(fileName);
    }

    protected List createList(Class clazz)
    {
        List result = super.createList(clazz);
        if(clazz == NeedsData.CONTACT)
        {
            result = new DebugList();
        }
        return result;
    }

    /*
    public int writeData()
    {
    Err.pr( "$$$ TO writeData()");
    return super.writeData();
    }
    */

    private String convertToFileName(String dbName)
    {
        String result = dbName;
        if(dbName.equals(NeedsApplicationData.TEST))
        {
            result = testFileName;
        }
        else if(dbName.equals(NeedsApplicationData.DEV))
        {
            result = devFileName;
            // Err.error( "Will probably never do as XML file");
        }
        else if(dbName.equals(NeedsApplicationData.PROD))
        {
            result = prodFileName;
            // Err.error( "Will probably never do as XML file");
        }
        /*
        else if(dbName.equals( NeedsApplicationData.VOLS))
        {
        result = volsFileName;
        //Err.error( "Will probably never do as XML file");
        }
        */
        return result;
    }

    public Collection queryAllContacts()
    {
        List result = (List) get(NeedsData.CONTACT);
        Collections.sort(result, orderer);
        return result;
    }

    private static class FirstSecondNameOrder implements Comparator
    {
        public int compare(Object one, Object two)
        {
            int result = 0;
            if(!(one instanceof Contact))
            {
                return 1;
            }
            if(!(two instanceof Contact))
            {
                return 1;
            }

            Contact con1 = (Contact) one;
            Contact con2 = (Contact) two;
            result = contactCf(con1, con2);
            return result;
        }

        /*
        * Trying to duplicate this:
        * q.setOrdering( "firstName ascending, secondName ascending");
        */
        private static int contactCf(Contact con1, Contact con2)
        {
            int result = -99;
            String text1 = con1.getSecondName();
            String text2 = con2.getSecondName();
            for(int i = 0; result == -99; i++)
            {
                if(text1 == null && text2 == null)
                {
                    if(i == 0)
                    {
                        text1 = con1.getFirstName();
                        text2 = con2.getFirstName();
                    }
                    /*
                    else if(i==1)
                    {
                    text1 = vol1.getGroupName();
                    text2 = vol2.getGroupName();
                    }
                    */
                    else
                    {
                        Err.error("Stopping infinite loop on ordering");
                    }
                }
                else if(text1 == null)
                {
                    if(i == 0)
                    {
                        text1 = con1.getFirstName();
                    }
                    /*
                    else if(i==1)
                    {
                    text1 = vol1.getGroupName();
                    }
                    */
                    else
                    {
                        Err.error("Stopping infinite loop on ordering");
                    }
                }
                else if(text2 == null)
                {
                    if(i == 0)
                    {
                        text2 = con2.getFirstName();
                    }
                    /*
                    else if(i==1)
                    {
                    text2 = con2.getGroupName();
                    }
                    */
                    else
                    {
                        Err.error("Stopping infinite loop on ordering");
                    }
                }
                else
                {
                    result = text1.compareTo(text2);
                }
            }
            return result;
        }
    }
}
