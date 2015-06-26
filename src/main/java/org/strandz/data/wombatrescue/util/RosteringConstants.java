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
package org.strandz.data.wombatrescue.util;

public class RosteringConstants
{
    // Many BOs at once, but last picked stored in globalCurrentBO,
    // These not used but there for debugging cnvenience
    public final static String CURRENT_STR = "CURRENT";
    public final static String NEXT_STR = "NEXT";
    public final static String LATEST_CODE_CURRENT_STR = "LATEST CODE CURRENT";
    public final static String LATEST_CODE_NEXT_STR = "LATEST CODE NEXT";
    public final static String ONE_AFTER_STR = "ONE AFTER";
    public final static String JUST_GONE_STR = "JUST GONE";
    // What to display
    public final static int ROSTER = 0;
    public final static int UNROSTERED_VOLS = 2;
    public final static int ROSTERED_NO_EMAIL_VOLS = 3;
    public final static int UNROSTERED_STATUS = 4;
    // Don't need to allocate roster slots first
    public final static int YAHOO_BUT_UNKNOWN_VOLS = 3;
    public final static int NOT_ON_YAHOO_VOLS = 4;
    public final static int CURRENT = 0;
    public final static int NEXT = 1;
    //use JUST_GONE
    //public final static int OLD = -1;
    public final static int ONE_AFTER = 2;
    public final static int JUST_GONE = 3;
    public final static int TEST = -99;
    //TODO - put this in the wombat property file
    public final static String YAHOO_FILENAME = "C:\\Teresa House\\yahoo-emails.txt";
    
    public static final String WINDOWS_CURRENT_FILENAME = "C:/temp/roster.html";
    public static final String WINDOWS_JUST_GONE_FILENAME = "C:/temp/old_roster.html";
    public static final String WINDOWS_TEST_FILENAME = "C:/temp/test_roster.html";
    public static final String UNIX_CURRENT_FILENAME = "/var/www/teresahouse/roster.html";
    public static final String UNIX_JUST_GONE_FILENAME = "/var/www/teresahouse/old_roster.html";
    public static final String UNIX_TEST_FILENAME = "/var/www/teresahouse/test_roster.html";
    
    public static final String CANNOT_COMMIT = "You will need to restart the application before Commit will work";
}
