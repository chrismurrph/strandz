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
package org.strandz.lgpl.util;

import org.strandz.lgpl.note.SdzNote;

import java.util.Stack;

abstract public class IndentationCounter
{
    private Stack<Object> stack = new Stack<Object>();
    private int indentations;
    public Class operationEnumClass;

    public IndentationCounter( Class operationEnumClass)
    {
        this.operationEnumClass = operationEnumClass;
    }
    
    public void unwind()
    {
        Err.pr( SdzNote.INDENT, "Unwinding from stack: " + stack);
        SdzNote.INDENT.incTimes();
        if(SdzNote.INDENT.getTimes() == 3)
        {
            Err.stack();
        }
        while(isIndented())
        {
            indent( false, "Unwind");
        }
        Assert.isTrue( indentations == 0);
        Assert.isTrue( stack.isEmpty());
//        Err.pr( SdzNote.INDENT, "I can't explain why next line needed - indentations is somehow at " +
//                "0 (when expect 1) before get to this method");
//        outFromLastIndentation();
    }
        
//    private void indent( boolean b)
//    {
//        indentForReason( b, null);
//    }
    
    public void indent( boolean b, Object id)
    {
        Assert.notNull( id);
        indentForReason( b, id);
    }
    
    private void indentForReason( boolean b, Object reason)
    {
        if(b)
        {
            if(SdzNote.INDENT.isVisible())
            {
                Err.pr( "to indent b/c " + reason);
                if(reason.toString().equals( "Next"))
                {
                    Err.debug();
                }
                Err.pr( "\ttimes " + indentations);
            }
            startIndentation( reason);
        }
        else
        {
            endIndentation( reason);
            Err.pr(SdzNote.INDENT, "done UN-indent b/c " + reason);
            Err.pr(SdzNote.INDENT, "\ttimes " + indentations);
        }
    }
    
    public boolean isIndented()
    {
        boolean result = !stack.isEmpty();
        boolean isIndented = indentations > 0; 
        Assert.isTrue( result == isIndented);
        return result;
    }
    
    public boolean isDoingOneOf( Object ids[])
    {
        boolean result = false;
        for(int i = 0; i < ids.length; i++)
        {
            Object id = ids[i];
            if(isDoingOperation( id))
            {
                result = true;
                break;
            }
        }
        return result;
    }
    
    public boolean isDoingOperation( Object id)
    {
        boolean result = false;
        if(/*id instanceof OperationEnum*/ id.getClass().equals( operationEnumClass))
        {
            int where = stack.search( id);
            if(where != -1)
            {
                result = true;
            }
        }
        return result;
    }
    
    abstract protected void intoFirstIndentation();
    abstract protected void outFromLastIndentation();    
    
    private void startIndentation( Object reason)
    {
        stack.push( reason);
        indentations++;
        if(indentations == 1)
        {
            intoFirstIndentation();        
        }
    }
            
    private void endIndentation( Object reason)
    {
        if(indentations == 0)
        {
            Err.error( "Already done last indentation in " + this.getClass().getName() + " trying again with <" + reason + ">");
        }
        stack.pop();
        indentations--;
        if(indentations == 0)
        {
            outFromLastIndentation();        
        }
    }
}
