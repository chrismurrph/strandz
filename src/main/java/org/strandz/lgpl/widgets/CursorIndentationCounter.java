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

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.IndentationCounter;
import org.strandz.lgpl.util.MessageDlg;

import javax.swing.JFrame;
import java.awt.Cursor;

public class CursorIndentationCounter extends IndentationCounter
{
    private JFrame parent;
    private static int times;
    private static CursorIndentationCounter INSTANCE_ONE;
    private static CursorIndentationCounter INSTANCE_TWO;
    private static int timesConstructed;
    public int id;

    /**
     * May be more of these around with the same name. With this on double
     * clicking will cause errors. With it off have protection of a Glass pane.
     * Two possible solutions. 1./ Make sure no painting occurs while the glass
     * pane is up 2./ Use a multi-threading approach
     */
    private static boolean SMOOTH_SCREEN_REFRESH = false;

    private CursorIndentationCounter( Class operationEnumClass)
    {
        super( operationEnumClass);
        timesConstructed++;
        if(timesConstructed > 2)
        {
            Err.error( this.getClass().getName() + " is supposed to have two instances max");
        }
        id = timesConstructed;
    }
    
    public static CursorIndentationCounter getInstance2()
    {
        if(INSTANCE_TWO == null)
        {
            Err.error( "Need to call same method but with a Class param");
        }
        return INSTANCE_TWO;    
    }
    
    public static CursorIndentationCounter getInstance()
    {
        if(INSTANCE_ONE == null)
        {
            Err.error( "Need to call same method but with a Class param");
        }
        return INSTANCE_ONE;    
    }
    
    public static CursorIndentationCounter getInstance2( Class operationEnumClass)
    {
        if(INSTANCE_TWO == null)
        {
            INSTANCE_TWO = new CursorIndentationCounter( operationEnumClass);
        }
        else if(operationEnumClass != INSTANCE_TWO.operationEnumClass)
        {
            Err.error( "Expected to ignore as it ought to be the same type, previously: " + 
                    INSTANCE_TWO.operationEnumClass + ", but now " + operationEnumClass);
        }
        return INSTANCE_TWO;    
    }
    
    public static CursorIndentationCounter getInstance( Class operationEnumClass)
    {
        if(INSTANCE_ONE == null)
        {
            INSTANCE_ONE = new CursorIndentationCounter( operationEnumClass);
        }
        else if(operationEnumClass != INSTANCE_ONE.operationEnumClass)
        {
            Err.error( "Expected to ignore as it ought to be the same type, previously: " + 
                    INSTANCE_ONE.operationEnumClass + ", but now " + operationEnumClass);
        }
        return INSTANCE_ONE;    
    }
    
    protected void intoFirstIndentation()
    {
        JFrame frame = MessageDlg.getFrame( true);
        if(frame != null)
        {
            if(! SMOOTH_SCREEN_REFRESH)
            {
                MessageDlg.blockUser();
            }
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Err.pr( SdzNote.INDENT, "Wait cursor ON");
//            times++;
//            Err.pr( " WAIT_CURSOR times " + times);
//            if(times == 2)
//            {
//                Err.stack();
//            }
        }
        else
        {
            Err.pr( SdzNote.INDENT, "Unable to put Wait Cursor ON as have no parent");
        }
        parent = frame;
    }

    protected void outFromLastIndentation()
    {
        if(parent != null)
        {
            parent.setCursor(null); //turn off the wait cursor
            MessageDlg.allowUser();
            Err.pr( SdzNote.INDENT, "Wait cursor OFF");
        }
        else
        {
            Err.pr( SdzNote.INDENT, "Wait cursor did not go OFF as have no parent");
        }
    }
}