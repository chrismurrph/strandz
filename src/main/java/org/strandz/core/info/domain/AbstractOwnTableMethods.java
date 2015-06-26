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
package org.strandz.core.info.domain;

import java.util.List;

/**
 * As well as being used as a column lister (either strictly for cutting and
 * pasting, or not at all), is good place to add other generic things may
 * wish to do with a table. For example where have to deal with other models,
 * renderers etc to do simple things such as get/set the background colour,
 * add a method here - getRendererAtColumn( int/id) will be used to set the
 * background colour in-error.
 */
abstract public class AbstractOwnTableMethods
{
    abstract public List getList(Object tableControl, String identifier);

    abstract public void setList(Object tableControl, String identifier, Object value);

    abstract public Object getValue(Object tableControl, String identifier);

    abstract public void setValue(Object tableControl, String identifier, Object value);

    abstract public Object getControlAt(Object tableControl, int row, int col);

    abstract public Object getControlAt(Object tableControl, int col);

    abstract public boolean isEditableControlAt(Object tableControl, int col);
    
    abstract public void setControlAt(Object tableControl, int row, int col, Object renderer);

    abstract public void repositionTo(Object tableControl, int row, int col);
}
