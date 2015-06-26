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

import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import java.util.Properties;

abstract public class JDOPortableConnectionInfo extends JDOConnectionInfo
{
    public JDOPortableConnectionInfo(
        String propsFileName,
        ConnectionEnum connectionEnum,
        int estimatedConnectDuration,
        int loadLookupDataDuration)
    {
        this( new String[]{ propsFileName}, connectionEnum, estimatedConnectDuration, loadLookupDataDuration);
    }
    
    public JDOPortableConnectionInfo(
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
            if(PropertyUtils.getPortableProperties(propsFileName, this, true) != null)
            {
                timesFound++;
                if(timesFound > 1)
                {
                    Print.prArray( propsFileNames, "More than one of these");
                    Err.error( "Only expect one resource to exist, already have <" + result + ">");
                }
                else
                {
                    result = PropertyUtils.getPortableProperties(propsFileName, this);
                }
            }
        }
        if(timesFound == 0)
        {
            Print.prArray( propsFileNames, "None of these");
            Err.error( "Even thou many to choose from (above), none were found as a portable resource (ie. in a jar file)");
        }
        return result;
    }
}
