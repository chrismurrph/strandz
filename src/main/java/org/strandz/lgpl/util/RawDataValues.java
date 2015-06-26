package org.strandz.lgpl.util;

import org.strandz.lgpl.store.OnLast;
import org.strandz.lgpl.store.Start;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.SequenceGenerator;

public class RawDataValues
{
    //private Object lastValue;
    //private OnLast lastOnLast;
    private String name;
    private Object someValues[];
    private SequenceGenerator sequenceGenerator;
    private SequenceGenerator randomSequenceGenerator;

    private static final int WANTED_SIZE = 34;

    public RawDataValues(String name, Object[] someValues)
    {
        Assert.notNull(someValues);
        Assert.isTrue(someValues.length == WANTED_SIZE, "Size of " + name +
                " s/be " + WANTED_SIZE + " but is " + someValues.length);
        this.name = name;
        this.someValues = someValues;
        randomSequenceGenerator = new SequenceGenerator( 1000, someValues.length);
    }

    private Object next()
    {
        Object result = null;
        Integer next = sequenceGenerator.next();
        if(next != null)
        {
            result = someValues[next];
        }
        else
        {
            /*
             * If are going back in time 17 times then need 17 examples
             */
            Print.prArray(someValues, "Values defined");
            Err.error("Only have " + someValues.length + ", yet trying to index using idx " +
                    sequenceGenerator.getTimesThru() + ", in <" + name + ">");
        }
        return result;
    }

    public Object getValue(Start start, OnLast onLast)
    {
        Object result = null;
        Assert.oneOnly(start, onLast, "Must either be starting or continuing, can't be doing both");
        if(start != null)
        {
            /*
             * We create a sequence generator which will be used thereafter for fetching the next value
             * of the sequence when subsequent getValue( null, OnLast.FROM_LAST) calls are made
             */
            sequenceGenerator = new SequenceGenerator(start.getOrdinal(), someValues.length);
            result = next();
        }
        else
        {
            if(onLast == OnLast.FROM_LAST)
            {
                Assert.notNull( sequenceGenerator, "Have not started, so don't yet have a sequence generator");
                result = next();
            }
            else if(onLast == OnLast.RANDOM)
            {
                Integer randomPick = randomSequenceGenerator.next();
                result = someValues[randomPick];
            }
            else
            {
                Err.error("Not yet defined what to do for " + onLast);
            }
        }
        //lastValue = result;
        //lastOnLast = onLast;
        return result;
    }
}
