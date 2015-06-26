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

import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.domain.event.ItemChangeTrigger;
import org.strandz.core.widgets.TableComp;
import org.strandz.lgpl.util.Assert;

public class NonVisualTableItemAdapter extends AbstractTableItemAdapter
{
    private TableComp nonVisualTable;
    
    public NonVisualTableItemAdapter(int column,
                            AbstractCell cell,
                            boolean alwaysEnabled,
                            String name,
                            String columnHeading,
                            boolean update,
                            ItemValidationTrigger itemValidationTrigger,
                            ItemChangeTrigger itemChangeTrigger,
                            ErrorThrowerI errorThrower,
                            int ordinal,
                            DOAdapter doAdapter,
                            Object source,
                            TableComp nonVisualTable,
                            CalculationPlace calculationPlace,
                            boolean enabled,
                            boolean readExternally)
    {
        super(column, cell, alwaysEnabled, name, columnHeading, update, 
              itemValidationTrigger, itemChangeTrigger, errorThrower, 
              ordinal, doAdapter, source, calculationPlace, readExternally);
        Assert.notNull( nonVisualTable);
        this.nonVisualTable = nonVisualTable;
        setEnabled( enabled); //if after col set then will pick up correct column
    }
    
    public Object getTableControl()
    {
        Object result;
        result = nonVisualTable;
        return result;
    }
}
