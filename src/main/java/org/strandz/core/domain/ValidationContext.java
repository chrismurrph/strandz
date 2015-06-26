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
package org.strandz.core.domain;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import java.util.List;

public class ValidationContext
{
    private Object strand;
    private Object node;
    private Object cell;
    private Object attribute;
    private List message;
    private Object msg;
    private boolean ok = true;
    private static boolean debug = false;
    private static int times;
    private static int constructedTimes;
    private int id;

    public ValidationContext()
    {
        constructedTimes++;
        id = constructedTimes;
        /*
        Err.pr( "Constructed ValidationContext ID " + id);
        if(id == 0)
        {
          Err.stack();
        }
        */
    }

    public Object getAttribute()
    {
        return attribute;
    }

    public Object getCell()
    {
        return cell;
    }

    public String toString()
    {
        String result = "isOk " + ok + " msg " + getMessage() + " node " + node + " attribute " + attribute + " ID: " + id;
        return result;
    }

    public Object getMessage()
    {
        Object result = null;
        if(message != null)
        {
            result = message;
        }
        else
        {
            result = msg;
        }
        return result;
    }

    public Object getNode()
    {
        return node;
    }

    public Object getStrand()
    {
        return strand;
    }

    public void setAttribute(Object attribute)
    {
        this.attribute = attribute;
    }

    public void setCell(Object cell)
    {
        this.cell = cell;
    }

    public void setMessage(List message)
    {
        if(message == null)
        {
            Err.error("Cannot setMessage() to null");
        }
        this.message = message;
    }

    public void setMessage(Object msg)
    {
        if(msg == null)
        {
            Err.error("Cannot setMessage() to null");
        }
        this.msg = msg;
    }

    public void setNode(Object node)
    {
        this.node = node;
    }

    public void setStrand(Object strand)
    {
        this.strand = strand;
    }

    public boolean isOk()
    {
        /*
        times++;
        Err.pr( "Returning validationContext " + ok + " times " + times);
        if(times == 11)
        {
        Err.debug();
        }
        */
        pr("Returning validationContext OK " + ok);
        return ok;
    }

    private void pr(String txt)
    {
        if(debug)
        {
            String msg = "Val Ctx ID: " + id + " " + txt;
            if(msg.equals("Val Ctx ID: 1020 Set validationContext OK to false"))
            {
                Err.stack(SdzNote.NV_PASTE_NOT_WORKING, this.toString());
            }
            Err.pr(SdzNote.NV_PASTE_NOT_WORKING, msg);
        }
    }

    public void setOk(boolean ok)
    {
        this.ok = ok;
        pr("Set validationContext OK to " + ok);
    }
}
