package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;

import java.text.ParseException;

/**
 * User: Chris
 * Date: 6/11/2008
 * Time: 21:20:19
 */
public class MoneyHelper
{
    private MoneyI money;
    private transient int id;
    private transient static int constructedTimes;

    public MoneyHelper( MoneyI money)
    {
        constructedTimes++;
        id = constructedTimes;
        this.money = money;
    }

    public String helperToString()
    {
        String result;
        result = money.getDollars() + "." + Utils.leftPadZero(money.getCents().toString(), 2);
        return result;
    }

    public int helperCompareTo(Object o)
    {
        int result = 0;
        MoneyAmount other = (MoneyAmount) o;
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
        else
        {
            result = Utils.compareTo(abs( money.getDollars()), abs( other.getDollars()));
            if(result == 0)
            {
                result = Utils.compareTo( money.getCents(), other.getCents());
            }
        }
        return result;
    }

    private Integer abs( Integer in)
    {
        Integer result = Math.abs( in);
        return result;
    }

    public int helperHashCode()
    {
        int result = 17;
        result = Utils.hashCode(result, abs( money.getDollars()));
        result = Utils.hashCode(result, money.getCents());
        return result;
    }

    public boolean helperEquals(Object o)
    {
        Utils.chkType(o, money.getClass());

        boolean result = false;
        if(o == money)
        {
            result = true;
        }
        else if(!(o instanceof MoneyAmount))
        {// nufin
        }
        else
        {
            MoneyAmount test = (MoneyAmount) o;
            if(Utils.equals( money.getDollars(), test.getDollars()))
            {
                if(Utils.equals( money.getCents(), test.getCents()))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    public static class Value
    {
        public Integer dollars;
        public Integer cents;
    }

    public static Value add( MoneyI money1, MoneyI money2)
    {
        Value result = new Value();
        // add the cents in dollars and keep the remainder
        int centsSum = money1.getCents() + money2.getCents();
        int leftOverCents;
        int dollarsSum = 0;
        if(centsSum >= 100)
        {
            Assert.isTrue(centsSum < 200);
            leftOverCents = centsSum - 100;
            dollarsSum++;
        }
        else
        {
            leftOverCents = centsSum;
        }
        dollarsSum += money1.getDollars() + money2.getDollars();
        // add the dollars plus what got from the cents
        result.dollars = dollarsSum;
        result.cents = leftOverCents;
        return result;
    }

    public static Value parse( String moneyStr) throws ParseException
    {
        Value result = new Value();
        if(moneyStr.length() < 3)
        {
            throw new ParseException("Money needs to be in format n.nn: " + moneyStr,
                0);
        }
        /*
        int dollarIndex = moneyStr.indexOf('$');
        if(dollarIndex == -1)
        {
            throw new ParseException("Money needs a $ sign: " + moneyStr, 0);
        }
        if(dollarIndex != 0)
        {
            throw new ParseException("Money needs a $ sign at beginning: " + moneyStr,
                0);
        }
        */

        int dotIndex = moneyStr.indexOf('.');
        if(dotIndex == -1)
        {
            throw new ParseException("Money needs a . character: " + moneyStr, 0);
        }

        String first = moneyStr.substring(0, dotIndex);
        String second = moneyStr.substring(dotIndex + 1);
        if(second.length() != 2)
        {
            throw new ParseException(
                "Money requires two digits for cents, got <" + second + ">: "
                    + moneyStr,
                dotIndex);
        }

        result.dollars = Integer.valueOf(first);
        result.cents = Integer.valueOf(second);
        return result;
    }
}
