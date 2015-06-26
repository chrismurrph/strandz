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

import org.strandz.core.domain.constants.DataFlowEnum;

// import strandz.domain.ProdNodeInterface;

public class DataFlowEvent
{
    /**
     * Place to call setData()/setQuery()/setProcedure() on the Cell
     * that you need to fill
     */
    public static final DataFlowEnum PRE_QUERY = DataFlowEnum.PRE_QUERY;
    /**
     * This one fires once all the records have been retrieved. So far been
     * used for:
     * 1./ Defaulting when test that not blank means that is first time.
     * Used Attribute.setItemValue()
     * 2./ Same as 1, but real defaulting for first time goes into
     * customizer, where Data Object knows that it is first time
     * 3./ In conjunction with navigated, to get all the places where
     * first displayed.
     */
    public static final DataFlowEnum POST_QUERY = DataFlowEnum.POST_QUERY;
    /**
     * For defaulting values into a row when the user presses INSERT.
     * The trigger is a per-Node trigger
     */
    public static final DataFlowEnum PRE_INSERT = DataFlowEnum.PRE_INSERT;
    private DataFlowEnum id;
    private Object source;
    // private ProdNodeInterface node;

    /**
     * Constructs an DataFlowEvent object with the specified source object.
     *
     * @param id the type of event
     */
    public DataFlowEvent(DataFlowEnum id, Object source)
    {
        this.id = id;
        this.source = source;
    }

    public DataFlowEnum getID()
    {
        return id;
    }

    public String toString()
    {
        return id.toString();
    }

    public Object getSource()
    {
        return source;
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
