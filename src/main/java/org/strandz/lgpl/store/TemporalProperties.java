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
package org.strandz.lgpl.store;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.note.SdzNote;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Properties with the same name have values that have shelf lives.
 */
public class TemporalProperties
{
    private String name;
    /**
     * Returns a Map which may have more than one date, but all same name. 
     */
    private Map<String,List<TemporalProperty>> propertiesMap = new HashMap<String,List<TemporalProperty>>();
    /**
     * To help with debugging
     */
    //private String description;
    private static int constructedTimes;
    private int id;
    
    public TemporalProperties( String name)
    {
        constructedTimes++;
        id = constructedTimes;
        if(id == 0)
        {
            Err.stack( SdzNote.TEMPORAL_PROPERTY);
        }
        this.name = name;
        //this.description = description;
    }

    public void setValue( String name, Object value)
    {
        setValue( name, null, null, value);
    }    
    
    public void setValue( String name, Date startDate, Date endDate, Object value)
    {
        Assert.notNull( value, "Cannot set a property to null in " + getName());
        List<TemporalProperty> list = propertiesMap.get( name);
        if(list == null)
        {
            list = new ArrayList<TemporalProperty>();
            propertiesMap.put( name, list);
        }
        else
        {
            list = propertiesMap.get( name);
        }
        TemporalProperty temporalProperty = new TemporalProperty( name, startDate, endDate); 
        int idx;
        if((idx = list.indexOf( temporalProperty)) != -1)
        {
            TemporalProperty inListTemporalProperty = list.get( idx);
            if(value.equals( inListTemporalProperty.value))
            {
                Err.pr( "Strange setting to same value");
            }
            inListTemporalProperty.value = value;
        }
        else
        {
            temporalProperty.value = value;
            list.add( temporalProperty);
        }
        Err.pr( SdzNote.TEMPORAL_PROPERTY, "TemporalProperty value set to <" + value + "> for <" + 
                temporalProperty.getName() + "> in ID: " + id + " called <" + getName() + ">");
        if(id == 1)
        {
            //Err.stack();
        }
    }
    
    public Object getValue( String name)
    {
        return getValue( name, null);
    }
    
    public Object getValue( String name, Date date)
    {
        Object result = null;
        List<TemporalProperty> propsForDateRanges = propertiesMap.get( name);
        //Assert.notNull( propsForDateRanges, "Have not set any values for property named <" + name + 
        //        "> in " + getClass().getName() + " ID: " + id);
        if(propsForDateRanges != null)
        {
            if(propsForDateRanges.size() == 1)
            {
                TemporalProperty prop = (TemporalProperty)propsForDateRanges.toArray()[0];
                result = prop.value;
            }
            else
            {
                Err.error( "More than one property - so we will need to determine which " +
                        "using date ranges and param <" + date + ">");
            }
        }
        return result;
    }
    
//    private void putProperty( String name, List<TemporalProperty> list)
//    {
//        propertiesMap.put( name, list);
//    }

    public String getName()
    {
        return name;
    }
}
