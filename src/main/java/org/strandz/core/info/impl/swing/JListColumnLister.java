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
package org.strandz.core.info.impl.swing;

import org.strandz.core.info.domain.AbstractOwnTableMethods;
import org.strandz.lgpl.util.Err;

import javax.swing.JList;
import javax.swing.ListModel;
import java.util.ArrayList;
import java.util.List;

public class JListColumnLister extends AbstractOwnTableMethods
{
    public List getList(Object tableControl, String identifier)
    {
        List result = new ArrayList();
        JList list = (JList) tableControl;
        ListModel model = list.getModel();
        int size = model.getSize();
        for(int i = 0; i <= size - 1; i++)
        {
            result.add(model.getElementAt(i));
        }
        return result;
    }

    public void setList(Object tableControl, String identifier, Object value)
    {
        Err.error("JListColumnLister setList() not yet implemented");
    }

    public Object getValue(Object tableControl, String identifier)
    {
        Err.error("JListColumnLister getValue() not yet implemented");
        return null;
    }

    public void setValue(Object tableControl, String identifier, Object value)
    {
        Err.error("JListColumnLister setValue() not yet implemented");
    }

    public Object getControlAt(Object tableControl, int row, int col)
    {
        Err.error("JListColumnLister getRenderer() not yet implemented");
        return null;
    }

    public Object getControlAt(Object tableControl, int col)
    {
        Err.error("getControlAt()" + " not implemented");
        return null;
    }

    public boolean isEditableControlAt(Object tableControl, int col)
    {
        Err.error("isEditableControlAt()" + " not implemented");
        return false;
    }

    public void setControlAt(Object tableControl, int row, int col, Object renderer)
    {
        Err.error("JListColumnLister setRenderer() not yet implemented");
    }

    public void repositionTo(Object tableControl, int row, int col)
    {
        Err.error("JListColumnLister repositionTo() not yet implemented");
    }
}
