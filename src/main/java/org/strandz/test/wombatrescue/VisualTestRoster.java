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
package org.strandz.test.wombatrescue;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.strandz.lgpl.util.MessageDlg;

import javax.swing.JFrame;

/**
 * See $SDZ/junit/bin/jUnit.xml for how this test suite has been excluded from running on server 
 */
public class VisualTestRoster extends NonVisualTestRoster
{
    public static void main(String s[]) throws Exception
    {
        VisualTestRoster obj = new VisualTestRoster();
        obj.setUp();
        //obj.testCopyPaste();
        //obj.testIntervalRSValidationWeekly();
        //obj.testPaneNode();
        //obj.testNotInsertBlank();
        //obj.testRosterLookup();
        //
        //obj.testEffectOfDetailChange();
        //
        //obj.testDetailToDetail();
        //obj.testDeleteRosterSlot();
        obj.testMemoryVersion();
        //obj.testComponentTableViewTabSwitch();
        //obj.testComponentTableViewTabSwitchWithRefresh();
        //obj.insertRS();
        //
        obj.tearDown();
    }
    
    public VisualTestRoster()
    {
        MessageDlg.setFrame( new JFrame());
    }
        
    boolean getNonVisual()
    {
        return false;
    }
    
//Getting those strange 'Why plugging a NULL into flexibility that used to be flexible'
//errors that don't exist on non-applichousing version
    public static Test suite()
    {
        return new TestSuite(VisualTestRoster.class);
    }

    /**
     * IDE works better with the method actually here
     */
    public void testMemoryVersion()
    {
        super.testMemoryVersion();
    }        
}
