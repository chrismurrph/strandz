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

/**
 * Used so that validation always passed by MoveTracker
 */
public class NullAdapter extends ItemAdapter
{
    public NullAdapter()
    {
        super();
    }

    public Object getItemValue()
    {
        return null;
    }

    public Object getItem()
    {
        return null;
    }

    public Object getItemValue( IdEnum idEnum)
    {
        return null;
    }

    public void setItemValue(Object value, IdEnum idEnum)
    {
    }

    public void requestFocus()
    {
    }

    public IdEnum createIdEnum( String desc)
    {
        return null;
    }

    public IdEnum createIdEnum( int row, String desc)
    {
        return null;
    }
    
    public IdEnum createIdEnum( Class clazz)
    {
        return null;
    }

    public void makeUntouchable(boolean b)
    {
    }

    public void fireItemValidateEvent()
    {
    }

    public ItemAdapter getOriginalAdapter()
    {
        return this;
    }
    
    public Object getTableControl()
    {
        return null;
    }
    
    public void fireItemChangeEventOnAllOthers()
    {
    }
}
