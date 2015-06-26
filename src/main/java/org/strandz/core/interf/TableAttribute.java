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

import org.strandz.core.domain.DOAdapter;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.TableItemAdapter;
import org.strandz.core.domain.CalculationPlace;
import org.strandz.lgpl.widgets.IconEnum;
import org.strandz.lgpl.util.Err;

import java.util.List;

/*
 * TODO - seem to be able to have one of these w/out having set
 * a TableControl for the Node. Small note could fix sometime.
 */

/**
 * This class encompasses the mapping between a DO field and a column in a
 * table. A table is defined as a control that allows the user to see many
 * rows at once. Thus for Swing controls that have been 'plugged-in' we have
 * set up a JTable, JList and JTextArea. For example see
 * <code>org.strandz.core.info.impl.swing.JTableModelImpl</code>
 *
 * @author Chris Murphy
 */
public class TableAttribute extends VisualAttribute implements TableAttributeI
{
    private static final transient IconEnum TABLE_ATTRIBUTE_ICON = IconEnum.DATE_PICKER;
    private Class clazz;
    private Class defaultClass;
    private String columnHeading;
    private static int constructedTimes;
    private int orderOfConstruction;

    // XMLEncode
    public TableAttribute()
    {
        constructedTimes++;
        orderOfConstruction = constructedTimes;
        // Err.pr( "                     CREATED TableAttribute withOUT A clazz (from XMLEncode)" + " index: " + index);
    }

    public TableAttribute(Attribute attrib)
    {
        super(attrib);
        constructedTimes++;
        orderOfConstruction = constructedTimes;
        // Err.pr( "                     CREATED TableAttribute withOUT A clazz (from another Attribute)" + " index: " + index);
    }

    public TableAttribute(String dataFieldName)
    {
        this(dataFieldName, null, new Integer(-1));
    }

    public TableAttribute(String dataFieldName, Integer ordinal)
    {
        this(dataFieldName, null, ordinal);
    }

    /*
    * Not stored so why should api need to provide it? Need a test
    * for timespace.
    */
    public TableAttribute(String dataFieldName, Class clazz)
    {
        this(dataFieldName, clazz, new Integer(-1));
    }

    private TableAttribute(String dataFieldName,
                           Class clazz,
                           Integer ordinal)
    {
        super(dataFieldName, ordinal);
        /*
        if(clazz == null)
        {
        Err.error( "clazz constructor param cannot be null");
        }
        */
        this.clazz = clazz;
        constructedTimes++;
        orderOfConstruction = constructedTimes;
        // Err.pr( "                     CREATED TableAttribute with clazz " + clazz + " index: " + index);
    }
    
    public ItemAdapter createItemAdapter( DOAdapter doAdapter, int column, CalculationPlace calculationPlace)
    {
        //Err.pr( "creating ItemAdapter in TableAttribute ID: " + getId() + " which has enabled " + isEnabled());
        ItemAdapter result = new TableItemAdapter(column, getCell(),
                isAlwaysEnabled(), getName(),
                getColumnHeading(),
                isUpdate(),
                getItemValidationTrigger(),
                getItemChangeTrigger(),
                getCell().getNode().getErrorThrowerI(),
                getOrdinal().intValue(), doAdapter,
                this, calculationPlace, isEnabled(), isReadExternally());
        return result;
    }

    /*
    public void setCell( AbstractCell cell)
    {
    super.setCell( cell);
    }
    */
    /*
    public void setDOField( String v)
    {
    super.setDOField( v);
    }
    */

    /*
    private static Class getDefaultClazz( AbstractCell cell,
        String dataFieldName)
    {
      Class result = null;
      if(cell != null && dataFieldName != null)
      {
        Class abstractCellClass = cell.getClazz();
        if(abstractCellClass != null)
        {
          result = pSelfReference.classOfMethodInClass( abstractCellClass,
              dataFieldName);
          if(result == null)
          {
            Err.error(
                "Did not find field " + dataFieldName + " in " + abstractCellClass);
          }
        }
        else
        {
          Err.error(
              "cell is supposed to have a clazz when setDefaultClazz() called");
        }
      }
      else
      {
        Err.error( "Expected non null params for getDefaultClazz() call");
      }
      return result;
    }
    */

    public boolean equals(Object o)
    {
        // All types of attributes need to be compared with each other
        // pUtils.chkType( o, this.getClass());

        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof TableAttribute))
        {
            result = false;
        }
        else
        {
            result = false;

            TableAttribute test = (TableAttribute) o;
            if(clazz == null ? test.clazz == null : clazz.equals(test.clazz))
            {
                if((getDOField() == null
                    ? test.getDOField() == null
                    : getDOField().equals(test.getDOField())))
                {
                    result = true;
                }
            }
            /*
            Err.pr( "clazz: " + clazz);
            Err.pr( "test.clazz: " + test.clazz);
            if(clazz.equals( test.clazz) &&
            getDataFieldName().equals( test.getDataFieldName()))
            {
            result = true;
            }
            else
            {
            //Err.pr( "component comparison failed: " + clazz +
            //        " with " + test.clazz);
            //Err.pr( "\tOR dataFieldName comparison failed: " + dataFieldName +
            //        " with " + test.dataFieldName);
            result = false;
            }
            */
        }
        return result;
    }

    public List getItemList()
    {
        return ((AbstractTableItemAdapter) getItemAdapter()).getItemList();
    }

    public void setItemList(Object value)
    {
        ((AbstractTableItemAdapter) getItemAdapter()).setItemList(value);
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (clazz == null ? 0 : clazz.hashCode());
        result = 37 * result + getDOField().hashCode();
        return result;
    }
    
    public Object getItem()
    {
        Object result = null;
        if(getItemAdapter() != null)
        {
            result = getItemAdapter().getItem();
        }
        return result;
    }

    /* We create another attribute rather than set a new component
       on an existing attribute
    public void setItem( Object item)
    {
        if(getItemAdapter() != null)
        {
            getItemAdapter().setItem( );
        }
    }
    */

    /* Moved up
    public Object getItemName()
    {
        String result = null;
        if(getItemAdapter() != null)
        {
            result = getItemAdapter().getItemName();
        }
        if(result == null || result.equals(""))
        {
            result = NameUtils.variableToDisplay(getDOField());
        }
        return result;
    }
    */

    // XMLEncode
    /*
    public void setClazz( Class clazz)
    {
    this.clazz = clazz;
    }
    */
    /*
    public Class getClazz()
    {
      Class result = null;
      if(clazz == null)
      {
        if(defaultClass == null)
        {
          defaultClass = getDefaultClazz( getCell(), getDOField());
        }
        result = defaultClass;
      }
      else
      {
        result = clazz;
      }
      // Err.pr( "                     getClazz() returning " + result + " for index " + index);
      return result;
    }
    */

    /*
    * No variable so no need to put in xml. getTableControl()
    * refers to the node's tableControl
    *
    public void setTableControl( Object tableControl)
    {
    if(tableControl == null)
    {
    Err.error( "tableControl is null");
    }
    this.tableControl = tableControl;
    }
    */
    /*
    public Object getTableControl()
    {
    return tableControl;
    }
    */

    public String toString()
    {
        //Object field = getItem();
        //String compStr = field.getClass().getName();
        String res = new String();
        res = res + "[TableAttribute dOField: <" + getDOField() + ">, " + "cell: <"
            + getCell() + ">, " + "name: <" + getName() + ">]";
        return res;
    }

    public IconEnum getIconEnum()
    {
        return TABLE_ATTRIBUTE_ICON;
    }

    public String getColumnHeading()
    {
        return columnHeading;
    }

    public void setColumnHeading(String columnHeading)
    {
        this.columnHeading = columnHeading;
    }

    /*
     * These are used, for instance to see if hasChanged()
    public Object getItemValue()
    {
      Err.error( "Implementation problem that getItemValue() exists in a TABLEAttribute");
      return null;
    }
    public void setItemValue( Object value)
    {
      Err.error( "Implementation problem that setItemValue() exists in a TABLEAttribute");
    }
    */
}
