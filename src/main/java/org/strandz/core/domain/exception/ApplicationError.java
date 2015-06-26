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
package org.strandz.core.domain.exception;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.util.ExceptionUtils;
import org.strandz.lgpl.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * For an example of how this is delivered to the users error handler trigger 
 * in Strandz see Strand.controlActionPerformed(). 
 * For instance examine EnterQueryState.DONT_LEAVE
 */
public class ApplicationError extends Error
{
    private List msg;
    private ApplicationErrorEnum type;

    public ApplicationError(Throwable th, ApplicationErrorEnum type)
    {
        super(th);
        if(th == null)
        {// nufin - means was a built in error message
        }
        else if(!(th instanceof ValidationException))
        {
            Err.error("ApplicationError being constructed with " + th);
        }
        if(th != null)
        {
            Object obj = ((ValidationException) th).getMsg();
            if(obj instanceof String)
            {
                setMsg(obj);
            }
            else if(obj instanceof MsgInfo)
            {
                setMsg(((MsgInfo) obj).text);
            }
            else if(obj instanceof List)
            {
                setMsg( (List)obj);
            }
            else if(obj == null)
            {
                Print.prThrowable(th, "ApplicationError");
                Err.error(
                    "Originating ValidationException has not had "
                        + "setMsg() called on it");
            }
            else
            {
                Err.error(
                    "Time to write code for ApplicationError to"
                        + " be constructed with " + obj.getClass());
            }
        }
        this.type = type;
    }

    public ApplicationError(List msg, ApplicationErrorEnum type)
    {
        setMsg(msg);
        this.type = type;
    }

    public ApplicationError(String s, ApplicationErrorEnum type)
    {
        setMsg(s);
        this.type = type;
    }
    
    public ApplicationErrorEnum getType()
    {
        return type;
    }

    public void setMsg(Object s)
    {
        ExceptionUtils.chkMsg(s, this);
        msg = new ArrayList();
        msg.add(s);
    }

    public void setMsg(List msg)
    {
        ExceptionUtils.chkMsg(msg, this);
        this.msg = msg;
    }

    public List getMsg()
    {
        return msg;
    }
}
