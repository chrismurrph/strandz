package org.strandz.core.info.convert;

import org.strandz.lgpl.data.objects.Scorecard;

import java.util.List;
import java.math.BigDecimal;

/**
 * User: Chris
 * Date: 27/04/2009
 * Time: 9:09:27 PM
 */
public class ScorecardInManyItemsControlConvert extends AbstractObjectControlConvert
{
    public ScorecardInManyItemsControlConvert()
    {
        typeRequiredByControlAccessors = List.class;
        typeOfObject = Scorecard.class;
    }

    public Object pushOntoScreen( Object scorecard)
    {
        List<BigDecimal> result;
        result = ((Scorecard)scorecard).getThresholds();
        return result;
    }

    public Object pullOffScreen(Object decimals)
    {
        Object result = Scorecard.newInstance( (List<BigDecimal>)decimals);
        return result;
    }
}
