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
package org.strandz.data.supersix.business;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;

import java.math.BigDecimal;

/**
 * This ratio should spread across to as many resource users as there are.
 * Here we are assuming two, but later can fix this limitation
 */
class Proportions implements Comparable
{
    int first;
    int second;

    Proportions(int first, int second)
    {
        this.first = first;
        this.second = second;
    }

    BigDecimal getFirstAsFraction()
    {
        BigDecimal result = Utils.decimalDivide( first, first + second);
        return result;
    }

    BigDecimal getSecondAsFraction()
    {
        BigDecimal result = Utils.decimalDivide( second, first + second);
        return result;
    }

    public String toString()
    {
        return "first <" + first + "> second <" + second + ">";
    }

    public int compareTo(Object o)
    {
        int result = 0;
        Proportions other = (Proportions)o;
        if(other == null)
        {
            Err.error("Can't cf to null");
        }
//        if(getTotal() != other.getTotal())
//        {
//            Err.error( "Two proportions are not comparable");
//        }
        BigDecimal firstDiff = Utils.absoluteDifference(getFirstAsFraction(), other.getFirstAsFraction());
        BigDecimal secondDiff = Utils.absoluteDifference(getSecondAsFraction(), other.getSecondAsFraction());
        BigDecimal bdResult = firstDiff.add( secondDiff);
        result = bdResult.multiply( new BigDecimal( "1000")).toBigInteger().intValue();
        return result;
    }
}
