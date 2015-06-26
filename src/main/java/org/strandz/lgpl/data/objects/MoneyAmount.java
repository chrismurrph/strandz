/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.text.ParseException;


public class MoneyAmount implements MoneyI, Comparable
{
    private Integer dollars;
    private Integer cents;
    private transient MoneyHelper helper;

    public static final MoneyAmount NULL = new MoneyAmount( 0, 0);
    public static final MoneyAmount ZERO = NULL;

    public static void main(String[] args)
    {
        /*
        MoneyAmount oneTen = new MoneyAmount(new Integer(1), new Integer(10));
        Assert.isTrue( oneTen.toString().equals( "1.10"));
        MoneyAmount oneFifty = new MoneyAmount(new Integer(1), new Integer(50));
        MoneyAmount twoOhFive = new MoneyAmount(new Integer(2), new Integer(5));
        MoneyAmount twoOhFiveAgain = new MoneyAmount(new Integer(2), new Integer(5));
        if(!(twoOhFive.compareTo( oneFifty) > 0))
        {
            Err.error( twoOhFive + " s/be greater than " + oneFifty);
        }
        if(!(oneTen.compareTo( oneFifty) < 0))
        {
            Err.error( oneTen + " s/be less than " + oneFifty);
        }
        Assert.isTrue( oneFifty.greaterThanOrEqual( oneTen));
        Assert.isTrue( oneFifty.greaterThan( oneTen));
        Assert.isTrue( twoOhFive.greaterThanOrEqual( twoOhFiveAgain));
        Assert.isTrue( !twoOhFive.greaterThan( twoOhFiveAgain));
        */
        String str = formatFor( "11.6");
        Err.pr( str);
    }

    public static String formatFor( String in)
    {
        String result;
        int dot = in.indexOf( '.');
        if(dot == -1)
        {
            result = in + ".00";
        }
        else
        {
            if(dot == in.length()-2)
            {
                result = in + "0";
            }
            else
            {
                result = in;
            }
        }
        return result;
    }

    public MoneyAmount()
    {
        helper = new MoneyHelper( this);
        this.dollars = new Integer( 0);
        this.cents = new Integer( 0);
    }

    public MoneyAmount(Integer dollars, Integer cents)
    {
        this();
        this.dollars = dollars;
        this.cents = cents;
    }

    public MoneyAmount(int dollars)
    {
        this();
        this.dollars = new Integer( dollars);
        this.cents = new Integer( 0);
    }

    public MoneyAmount(int dollars, int cents)
    {
        this();
        this.dollars = new Integer( dollars);
        this.cents = new Integer( cents);
    }

    public static MoneyAmount newInstance(String moneyStr)
    {
        MoneyAmount result;
        MoneyHelper.Value value = null;
        try
        {
            value = MoneyHelper.parse(moneyStr);
        }
        catch(ParseException ex)
        {
            Err.error(ex);
        }
        result = new MoneyAmount( value.dollars, value.cents);
        return result;
    }

    /**
     * Useful for calling from Ruby
     */
    public boolean isZero()
    {
        return dollars.equals( NULL.getDollars()) && cents.equals( NULL.getCents());
    }

    /*
    public MoneyAmount add( String moneyAmountStr)
    {
        MoneyAmount moneyAmount = MoneyAmount.newInstance( moneyAmountStr);
        return add( moneyAmount);
    }
    */

    public MoneyAmount add( MoneyAmount moneyAmount)
    {
        MoneyAmount result;
        MoneyHelper.Value value = helper.add( this, moneyAmount);
        result = new MoneyAmount( value.dollars, value.cents);
        return result;
    }

    public String toString()
    {
        return helper.helperToString();
    }

    public int compareTo(Object o)
    {
        return helper.helperCompareTo( o);
    }

    public boolean greaterThanOrEqual( MoneyAmount moneyAmount)
    {
        boolean result = this.compareTo(moneyAmount) >= 0;
        return result;
    }

    public boolean lessThanOrEqual( MoneyAmount moneyAmount)
    {
        boolean result = this.compareTo(moneyAmount) <= 0;
        return result;
    }

    public boolean greaterThan(  MoneyAmount moneyAmount)
    {
        boolean result = this.compareTo(moneyAmount) > 0;
        return result;
    }

    public boolean equals(Object o)
    {
        return helper.helperEquals( o);
    }

    public int hashCode()
    {
        return helper.helperHashCode();
    }

    public Integer getCents()
    {
        return cents;
    }

    public Integer getDollars()
    {
        return dollars;
    }

    public void setCents(Integer cents)
    {
        this.cents = cents;
    }

    public void setDollars(Integer dollars)
    {
        this.dollars = dollars;
    }

    public static Float divide(MoneyAmount value, MoneyAmount divideBy)
    {
        Float result;
        Float floatValue = new Float( value.getDollars() + "." + Utils.leftPadZero(value.getCents().toString(), 2));
        Float floatDivideBy = new Float( divideBy.getDollars() + "." + Utils.leftPadZero(divideBy.getCents().toString(), 2));
        if(floatDivideBy == 0f)
        {
            /*
             * This stops divide by zero errors. We don't know yet if this is the right thing
             * to do in all circumstances. At the moment there is no capability for calculations
             * to return null/nil. If they do there is a crash. When you see nothing showing up
             * in a graph over a time period that means that data (Moneys) were not recorded
             * over that time.
             */
            result = new Float( 0f);
        }
        else
        {
            result = floatValue.floatValue() / floatDivideBy.floatValue();
        }
        //Err.pr( "From " + value + " divided by " + divideBy + " will ret " + result);
        return result;
    }
}
