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
package org.strandz.core.interf;

import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.DOAdapter;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.NodeTableMethods;
import org.strandz.core.domain.NonVisualTableItemAdapter;
import org.strandz.core.domain.TableSignatures;
import org.strandz.core.domain.CalculationPlace;
import org.strandz.core.prod.NodeTableModelImpl;
import org.strandz.core.widgets.TableComp;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.SdzNote;

import java.util.ArrayList;
import java.util.List;

/**
 * A marker class for a NonVisualAttribute. Required so that
 * when copying and pasting we know to collect a list. Will
 * also be used for subcutaneous testing.
 * TODO Integrate TableComp into TableSignatures in same way that
 * did for NonVisualTableAttribute - that way conversions will
 * happen properly. As part of this get/setItem methods will rely
 * on a base class.
 *
 * @author Chris Murphy
 */
public class NonVisualTableAttribute extends NonVisualAttribute implements TableAttributeI
{
    private TableComp tableComp;
    private int nonVisualColumn = Utils.UNSET_INT;
    
    private static List EMPTY = new ArrayList();
    
    public NonVisualTableAttribute()
    {
        super();
        init();
    }

    public NonVisualTableAttribute(String dataFieldName)
    {
        super(dataFieldName);
        init();
    }
    
    public NonVisualTableAttribute( Attribute attribute)
    {
        super( attribute);
        init();
    }
    
    public ItemAdapter createItemAdapter( DOAdapter doAdapter, int column, CalculationPlace calculationPlace)
    {
        ItemAdapter result = new NonVisualTableItemAdapter(
                column, 
                getCell(),
                isAlwaysEnabled(), 
                getName(),
                null,
                isUpdate(),
                getItemValidationTrigger(),
                getItemChangeTrigger(),
                getCell().getNode().getErrorThrowerI(),
                Utils.UNSET_INT, 
                doAdapter,
                this, 
                (TableComp)getItem(),                
                calculationPlace,
                isEnabled(),
                isReadExternally());
        Err.pr( SdzNote.MANY_NON_VISUAL_ATTRIBS, "NonVisualTableItemAdapter created for " + getName() + " at column " + column);
        nonVisualColumn = column;
        return result;
    }
    
    private void init()
    {
        tableComp = new TableComp();
        TableSignatures.setTableBuffer( tableComp, 1);
    }
    
    public Object getItem()
    {
        return tableComp;
    }
    
    public List getItemList()
    {
        List result = null;
        //Err.pr( "Doing getItemList() and model is a " + tableComp.getModel().getClass());
        NonVisualTableItemAdapter tableItemAdapter = (NonVisualTableItemAdapter)getItemAdapter();
        if(tableItemAdapter != null)
        {
            NodeTableMethods nodeTableMethods = (NodeTableMethods) tableComp.getModel();
            Assert.notNull( nodeTableMethods);
            nodeTableMethods.getNodeTableModel().setNonVisualOrdinal( nonVisualColumn);
            result = tableItemAdapter.getItemList();
            nodeTableMethods.getNodeTableModel().setNonVisualOrdinal( NodeTableModelImpl.VISUAL_MODE);
        }
        else
        {
            //At DT should not need an ItemAdapter, yet this method called by toString()
            result = EMPTY;
        }
        return result;
    }

    /*
    public List getItemList()
    {
        List result;
        NonVisualTableItemAdapter tableItemAdapter = (NonVisualTableItemAdapter)getItemAdapter();
        if(tableItemAdapter != null)
        {
            result = tableItemAdapter.getItemList();
        }
        else
        {
            //At DT should not need an ItemAdapter, yet this method called by toString()
            result = EMPTY;
        }
        return result;
    }
    */

    public void setItemList(Object value)
    {
        ((AbstractTableItemAdapter) getItemAdapter()).setItemList(value);
    }

    /* Doesn't work same as getBIValue() - needs to be for current row which is what super does
    public Object getItemValue()
    {
        return getItemList();
    } 
    */   
}
