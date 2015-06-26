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
package org.strandz.lgpl.store;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.JDONote;

import java.util.Properties;

abstract public class JDOByFileConnectionInfo extends JDOConnectionInfo
{
    
    public JDOByFileConnectionInfo(
        String propsFileName,
        ConnectionEnum connectionEnum,
        int estimatedConnectDuration,
        int loadLookupDataDuration)
    {
        this( new String[]{ propsFileName}, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
    }
    
    public JDOByFileConnectionInfo(
        String propsFileNames[],
        ConnectionEnum connectionEnum,
        int estimatedConnectDuration,
        int loadLookupDataDuration)
    {
        super(propsFileNames, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
    }

    public Properties getProperties()
    {
        Properties result = null;
        int timesFound = 0;
        for(int i = 0; i < propsFileNames.length; i++)
        {
            String propsFileName = propsFileNames[i];
            //Err.pr( "To getProperties() for " + propsFileName);
            //Err.pr( "OS is " + Utils.OS_NAME);
            if(PropertyUtils.getProperties(propsFileName, true) != null)
            {
                timesFound++;
                if(timesFound > 1)
                {
                    Print.prArray( propsFileNames, "More than one of these");
                    Err.error( "Only expect one properties file to exist, already have <" + result + ">");
                }
                else
                {
                    result = PropertyUtils.getProperties(propsFileName);
                    if(SdzNote.LOAD_FILE_PROPERTIES.isVisible() || JDONote.RELOAD_PM_KODO_BUG.isVisible())
                    {
                        Err.pr( "Loaded properties from " + propsFileName);
                        Print.prProperties( result);
                    }
                }
            }
        }
        if(timesFound == 0)
        {
            Print.prArray( propsFileNames, "None of these");
            Err.error( "Even thou some to choose from, did not find any existing " + propsFileNames.length + 
                    " files (see above (now server formatted properly!!))");
        }
        return result;
    }

//    public DomainQueriesI createDomainQueries()
//    {
//        return null;
//    }
}
