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

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.IdentifierI;
import org.strandz.lgpl.note.SdzNote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CalculationPlace
{
    private List<CalcAdapter> calcs = new ArrayList<CalcAdapter>();
    
    private static int timesCalc;
    private static int times;
    private static final boolean TURNED_OFF = false;
    private static final boolean DEBUG = SdzNote.NO_CALCS.isVisible();

    public CalculatedResultI getCalculator( ItemAdapter itemAdapter)
    {
        CalculatedResultI result = null;
        for(Iterator<CalcAdapter> iterator = calcs.iterator(); iterator.hasNext();)
        {
            CalcAdapter calcAdapter = iterator.next();
            if(calcAdapter.adapter == itemAdapter)
            {
                result = calcAdapter.calc;
                break;
            }
        }
        return result;
    }

    private static class CalcAdapter
    {
        CalculatedResultI calc;
        ItemAdapter adapter;
        
        public String toString()
        {
            return adapter.getName();
        }
    }
    
    public CalculationPlace()
    {
        
    }
    
    public void addCalc( CalculatedResultI calc, ItemAdapter itemAdapter)
    {
        Assert.notNull( itemAdapter);
        CalcAdapter calcAdapter = new CalcAdapter();
        calcAdapter.calc = calc;
        calcAdapter.adapter = itemAdapter;
        calcs.add( calcAdapter);
        times++;
        Err.pr( SdzNote.NO_CALCS, "addCalc being done " + times + " times for <" + itemAdapter.getName() + ">");
        //Err.stack();
    }
    
    public void fireCalculationFromSync( int id, Object obj, int row)
    {
        if(SdzNote.NO_CALCS.isVisible())
        {
            timesCalc++;
            String longTxt = "fireCalculationFromSync(): " + obj + " for row " + row + " times " + timesCalc;
            String shortTxt = "fireCalculationFromSync() in AdaptersList ID: " + id;
            Err.pr( shortTxt);
            if(timesCalc >= 5)
            {
                if(!calcs.isEmpty())
                {
                    //Err.debug();
                }
            }
        }
        doCalculations( obj, row);
    }

    public void fireCalculationFromPopulateRow( int row)
    {
        //doCalculations( obj);
    }
    
    public void fireCalculationFromItemAdapter( Object obj)
    {
        doCalculations( obj, Utils.UNSET_INT);
    }
    
    private void doCalculations( Object obj, int row)
    {
        if(!TURNED_OFF)
        {
            if(DEBUG && !calcs.isEmpty())
            {
                Print.prList( calcs, "About to fire calculation on these calcs");
                //Err.pr( "About to do calcs on " + calcs.size());
            }
            for(Iterator<CalcAdapter> iterator = calcs.iterator(); iterator.hasNext();)
            {
                CalcAdapter calcAdapter = iterator.next();
                if(DEBUG)
                {
                    IdentifierI identifier = (IdentifierI)obj;
                    if(identifier.getId() == 1845 || identifier.getId() == 2812)
                    {
                        Err.debug();
                    }
                }
                pr( "Doing calc on " + obj);
                Object calcResult = calcAdapter.calc.getBackCalcResult( obj);
                if(row == Utils.UNSET_INT)
                {
                    calcAdapter.adapter.setItemValue( calcResult);
                }
                else
                {
                    AbstractTableItemAdapter tableAdapter = (AbstractTableItemAdapter)calcAdapter.adapter;
                    IdEnum idEnum = tableAdapter.createIdEnum( row, "Calcing value for a row");
                    if(calcResult == null)
                    {
                        Err.pr( "No calcResult from <" + obj + ">");
                    }
                    else
                    {
                        calcAdapter.adapter.setItemValue( calcResult, idEnum);
                    }
                }
            }
        }
    }
    
    private static void pr( String txt)
    {
        if(DEBUG)
        {
            Err.pr( txt);
        }
    }
}
