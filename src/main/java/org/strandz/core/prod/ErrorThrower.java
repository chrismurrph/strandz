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
package org.strandz.core.prod;

import org.strandz.core.domain.ErrorThrowerI;
import org.strandz.core.domain.MoveTrackerI;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.domain.exception.ApplicationErrorEnum;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.core.prod.move.MoveTracker;
import org.strandz.core.prod.move.MoveManagerI;
import org.strandz.lgpl.util.IndentationCounter;

import java.util.ArrayList;
import java.util.List;

/**
 * For an example of how this is delivered to the users error handler trigger 
 * in Strandz see Strand.controlActionPerformed(). 
 * For instance examine EnterQueryState.DONT_LEAVE
 */
public class ErrorThrower implements ErrorThrowerI, org.strandz.lgpl.store.ErrorThrowerI
{
    private MoveManagerI moveManager;
    private IndentationCounter indentationCounter;

    ErrorThrower(MoveManagerI moveManager, IndentationCounter indentationCounter)
    {
        this.moveManager = moveManager;
        this.indentationCounter = indentationCounter;
    }

    public void throwApplicationError(
        ValidationException ex,
        ApplicationErrorEnum type)
    {
        throwApplicationError(ex, null, type);
    }

    public void throwApplicationError(
        List error,
        ApplicationErrorEnum type)
    {
        throwApplicationError(null, error, type);
    }

    public void throwApplicationError(
        ValidationException ex, List error,
        ApplicationErrorEnum type)
    {
        // if(!ignoreValidation)
        {
            indentationCounter.unwind();
            ApplicationError ae = new ApplicationError(ex, type);
            if(error != null && (ex != null && ex.getMsg() != null))
            {
                Session.error("Ambiguous as to which error message to use");
            }

            List list = null;
            boolean isList = true;
            if(error != null)
            {
                list = error;
            }
            else
            {
                if(ex.getMsg() instanceof List)
                {
                    list = (List) ex.getMsg();
                }
                else
                {
                    isList = false;
                }
            }
            if(isList)
            {
                ae.setMsg(list);
                moveManager.updateValidationContext(list);
            }
            else
            {
                ae.setMsg(ex.getMsg());
                moveManager.updateValidationContext(ex.getMsg());
            }
            // ae.setLastFieldHolder( block);
            // chk( block);
            throw ae;
        }
    }

    public void throwApplicationError(
        String error, ApplicationErrorEnum type)
    {
        // if(!ignoreValidation)
        {
            indentationCounter.unwind();
            ApplicationError ae = new ApplicationError(error, type);
            // ae.setLastFieldHolder( block);
            List list = new ArrayList();
            list.add(error);
            moveManager.updateValidationContext(list);
            // chk( block);
            throw ae;
        }
    }

    public void throwApplicationError(List error)
    {
        throwApplicationError( error, ApplicationErrorEnum.INTERNAL);
    }
}
