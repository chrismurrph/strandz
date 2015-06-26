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

import org.strandz.data.wombatrescue.business.MockRosterService;
import org.strandz.data.wombatrescue.calculated.MonthTransferObj;
import org.strandz.data.wombatrescue.calculated.RosterTransferObj;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

/**
 * This is a test class for testing RosterServiceI on the developer workstation
 * against a locally running tomcat.  
 */
public class LocalServerMockRosterService extends MockRosterService
{
    /**
     * As this class is prolly only going to be called via Spring, then this main method will
     * not be used in production. However it is useful for testing that the the right
     * classes are being included and that the jar manifest is correct. Thus we get a 
     * permanent record of which classes need to be included in localHttpServer.jar or
     * remoteHttpServer.jar and which jar files need to be deployed alongside. ie. these
     * jar files are made runnable via jar -f xxx.jar, and are self documenting as to their
     * dependencies. 
     */
    public static void main(String[] args)
    {
        LocalServerMockRosterService service = new LocalServerMockRosterService();
        Err.pr( "Created " + service);
        Err.pr( "About to try and call display service:");
        MonthTransferObj month = new MonthTransferObj( MonthInYear.JULY, 2006);
        RosterTransferObj received = service.getRoster( RosteringConstants.ROSTER, month);
        Err.pr( "DEBUGGING Roster was ok: " + !Utils.isBlank( received.getRoster().toString()));
        Err.pr( "---------------START ROSTER---------------");
        Err.pr( received.getRoster().toString());
        Err.pr( "----------------END ROSTER----------------");
    }

    public String toString()
    {
        return "LocalServerMockRosterService";
    }
}
