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
package org.strandz.data.util;

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FileUserDetailsProvider implements UserDetailsProviderI
{
    private String fileName = null;
    private List<String> fileContents = new ArrayList<String>();
    
    private static final String USER_DETAILS_FILENAME = "testuserdetails.csv";
    private static final String SERVER_DIRECTORY = "/usr/local/sdz-zone/property-files/";
    private static final String DESKTOP_DIRECTORY = "C:\\sdz-zone\\property-files\\";
    
    public FileUserDetailsProvider()
    {
        if(Utils.OS_NAME.startsWith("Windows"))
        {
            fileName = DESKTOP_DIRECTORY + USER_DETAILS_FILENAME;
        }
        else
        {
            fileName = SERVER_DIRECTORY + USER_DETAILS_FILENAME;
        }
        StringBuffer buf = FileUtils.readFile( new File( fileName), false);
        StringTokenizer st = new StringTokenizer( buf.toString(), ",");
        for(int i = 0; st.hasMoreTokens(); i++)
        {
            String tok = st.nextToken();
            fileContents.add( tok);
        }
    }

    public Object[] get(int i)
    {
        Assert.isTrue( i == 0, "Expected that will only hold one line in " + fileName);
        return fileContents.toArray();
    }

    public int size()
    {
        return 1;
    }
}
