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
package org.strandz.core.domain.event;

import org.strandz.core.domain.constants.TransactionEnum;

// import strandz.domain.ProdNodeInterface;

public class TransactionEvent
{
    /**
     * In PRE_CLOSE if raise an ApplicationError then nothing more
     * will happen. (So user s/sort things out b4 calling this).
     * POST_CLOSE_SUCCESS removed as user can do himself
     * POST_CLOSE_SUCCESS will occur only if user hasn't aborted,
     * which would normally be after a successful validation/commit.
     * (May want a method called focusOnFault - BULL - user should
     * do this).
     */
    public static final TransactionEnum PRE_CLOSE = TransactionEnum.PRE_CLOSE;
    //Not used or required
    //public static final TransactionEnum POST_CLOSE_SUCCESS = TransactionEnum.POST_CLOSE_SUCCESS;
    private TransactionEnum id;
    // private ProdNodeInterface node;

    /**
     * Constructs an DataFlowEvent object with the specified source object.
     *
     * @param source the object where the event originated
     * @param id     the type of event
     */
    public TransactionEvent(TransactionEnum id)
    {
        this.id = id;
    }

    public TransactionEnum getID()
    {
        return id;
    }
    /*
    public void setNode( ProdNodeInterface node)
    {
    this.node = node;
    }
    */
    /*
    public ProdNodeInterface getNode()
    {
    return node;
    }
    */
}
