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
package org.strandz.service.wombatrescue;

import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.data.wombatrescue.business.UploadRosterServiceI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;

import java.io.File;

public class ServerUploadRosterService implements UploadRosterServiceI
{    
    public static void main(String[] args)
    {
        //See TestFileUpload
    }
    
    public ServerUploadRosterService()
    {
        //Doesn't need a Datastore
    }
        
    public String uploadRoster(String roster, int whichMonth, MonthInYearI monthInYear)
    {
        String result = getFileName( whichMonth);
        String rosterText;
        //if(Utils.OS_NAME.startsWith("Windows"))
        {
            rosterText = "<pre>" + Utils.NEWLINE + monthInYear +
                    ", printed " + TimeUtils.getProseCurrentTimestamp() +
            Utils.NEWLINE + roster + "</pre>";
        }
        /*
        else
        {
            //When served by a web server does not need the <PRE> tags
            rosterText = monthInYear + ", printed " + 
                    TimeUtils.getProseCurrentTimestamp() + Utils.SEPARATOR + roster;
        }
        */
        //writeOverLast( whichMonth);
        FileUtils.writeFile( rosterText, result);
        return result;
    }
    
    private static String getFileName( int whichMonth)
    {
        String result = null;
        if(Utils.OS_NAME.startsWith("Windows"))
        {
            if(whichMonth == RosteringConstants.CURRENT)
            {
                result = RosteringConstants.WINDOWS_CURRENT_FILENAME;
            }
            else if(whichMonth == RosteringConstants.JUST_GONE)
            {
                result = RosteringConstants.WINDOWS_JUST_GONE_FILENAME;
            }
            else if(whichMonth == RosteringConstants.TEST)
            {
                result = RosteringConstants.WINDOWS_TEST_FILENAME;
            }
            else
            {
                Err.error( "Unknown in RosteringConstants: " + whichMonth);
            }
        }
        else
        {
            if(whichMonth == RosteringConstants.CURRENT)
            {
                result = RosteringConstants.UNIX_CURRENT_FILENAME;
            }
            else if(whichMonth == RosteringConstants.JUST_GONE)
            {
                result = RosteringConstants.UNIX_JUST_GONE_FILENAME;
            }
            else if(whichMonth == RosteringConstants.TEST)
            {
                result = RosteringConstants.UNIX_TEST_FILENAME;
            }
            else
            {
                Err.error( "Unknown in RosteringConstants: " + whichMonth);
            }
        }
        return result;
    }
    
    private static void writeOverLast( int whichMonth)
    {
        if(whichMonth == RosteringConstants.NEXT)
        {
            String currentFileName = getFileName( RosteringConstants.CURRENT);        
            String nextFileName = getFileName( RosteringConstants.NEXT);
            StringBuffer buf = FileUtils.readFile( new File(nextFileName), true);
            if(buf != null)
            {
                String nextFile = buf.toString();
                if(nextFile != null)
                {
                    Err.pr( "Next file, have just read, follows");
                    Err.pr( nextFile);
                }
            }
        }
    }

    public String getName()
    {
        return "Upload Roster (Server)";
    }
}
