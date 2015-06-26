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
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;

import javax.swing.JLabel;

public class DebugLabel extends JLabel
{
    private static int timesDate = 0;
    private static int timesMonthDate = 0;
    private static int constructedTimes = 0;
    public int id;
    private boolean idSet;
    
    public DebugLabel()
    {
        super();
        putId();
        if(id == 1 || id == 2)
        {
            Err.debug();
        }
        Err.pr( "Created DebugLabel, ID: " + id);
    }

    /**
     * To cope with setText() being called in nullary constructor
     */
    private void putId()
    {
        if(!idSet)
        {
            constructedTimes++;
            id = constructedTimes;
            idSet = true;
        }
    }
    
    public String toString()
    {
        String result = super.toString();
        result = "ID: " + id + " " + result;    
        return result;
    }
    
    public void setText( String txt)
    {
        putId();
        if(txt != null && (txt.equals( "31 Oct 2007") || txt.equals( "30 Sep 2007")))
        {
            timesDate++;
            Err.pr( "--- To write <" + txt + "> to ID: " + id + " times " + timesDate);
            if(id == 5 && timesDate == 1) //1 modelChanged from user's query (CTV.setText())
                                          //3 REFRESH from user (ControlSignatures.setText())
                                          //5 internal REFRESH (ControlSignatures.setText())
            {
                Err.stack();
            }
            super.setText( txt);
        }
        else if(txt != null && txt.equals( "Nufin")) 
        {
            timesMonthDate++;
            Err.pr( "--- To write <Nufin> to ID: " + id + " times " + timesMonthDate);
            super.setText( txt);
        }
        else 
        {
            timesMonthDate++;
            Assert.isFalse( id == 0);
            Err.pr( "--- To write <" + txt + "> to ID: " + id + " times " + timesMonthDate);
            if(timesMonthDate == 156)
            {
                Err.stack();
            }
            super.setText( txt);
        }
    }
}
