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
package org.strandz.lgpl.util;

import java.util.List;
import java.util.StringTokenizer;

public class MsgSubstituteUtils
{

    public static void main(String[] args)
    {
        String base = "This $ is not like $";
        String params[] = {"ELEPHANT", "COW"};
        String formed = formMsg(base, params);
        Err.pr(formed);

        boolean ok = conformsToMsg(base, formed);
        if(ok)
        {
            Err.pr(formed + " conforms to " + base);
        }
        else
        {
            Err.pr(formed + " - DOES NOT conform to - [" + base + "]");
        }
    }

    public static boolean conformsToMsg(String toParse, List toConform)
    {
        String msg = null;
        if(toConform.size() != 1)
        {
            Err.error("Not yet coded for multi-part message");
        }
        else
        {
            msg = (String) toConform.get(0);
        }
        return conformsToMsg(toParse, msg);
    }

    /**
     * Every tokenized part of
     *
     * @param toParse   - boiler as well as $s
     * @param toConform - message that may have gone through formMsg
     * @return
     */
    public static boolean conformsToMsg(String toParse, String toConform)
    {
        boolean result = true;
        final String TOK_SEP = "$";
        if(toConform.indexOf(TOK_SEP) != -1)
        {
            Err.error("Have params the wrong way round");
        }

        StringTokenizer st = new StringTokenizer(toParse, TOK_SEP, true);
        int lastIndex = -1;
        for(; st.hasMoreTokens();)
        {
            String tok = st.nextToken();
            if(tok.equals(TOK_SEP))
            {
            }
            else
            {
                int index = toConform.indexOf(tok);
                if(index == -1)
                {
                    result = false;
                    break; // does not contain the boiler at i in toParse
                }
                else
                {
                    if(!(index > lastIndex))
                    {
                        // each bit of boiler must be after the last bit of boiler
                        Err.pr("Expect to be going consequtively through <" + toConform + ">");
                        Err.error("Error was found as " + tok + " is at " + index
                            + " and lastIndex was " + lastIndex);
                    }
                    lastIndex = index;
                }
            }
        }
        return result;
    }

    public static String formMsg(String toParse, Object toSubstitute[])
    {
        String params[] = new String[toSubstitute.length];
        for(int i = 0; i <= params.length - 1; i++)
        {
            String toSubstituteStr = null;
            Object obj = toSubstitute[i];
            if(obj instanceof String)
            {
                toSubstituteStr = (String) obj;
            }
            else
            {
                toSubstituteStr = obj.toString();
            }
            params[i] = toSubstituteStr;
        }
        return formMsg(toParse, params);
    }

    public static String formMsg(String toParse, Object toSubstitute)
    {
        String toSubstituteStr = null;
        if(toSubstitute instanceof String)
        {
            toSubstituteStr = (String) toSubstitute;
        }
        else
        {
            toSubstituteStr = toSubstitute.toString();
        }
        String params[] = {toSubstituteStr};
        return formMsg(toParse, params);
    }

    /*
    * For each occurance of $, substitute a value from
    * the array arg.
    */
    public static String formMsg(String toParse, String toSubstitute[])
    {
        StringBuffer result = new StringBuffer();
        final String TOK_SEP = "$";
        StringTokenizer st = new StringTokenizer(toParse, TOK_SEP, true);
        int numDollars = 0;
        for(; st.hasMoreTokens();)
        {
            String tok = st.nextToken();
            if(tok.equals(TOK_SEP))
            {
                result.append(toSubstitute[numDollars]);
                numDollars++;
            }
            else
            {
                result.append(tok);
            }
        }
        if(numDollars != toSubstitute.length)
        {
            Err.error("Cannot substitute " + toSubstitute.length + " variables into <"
                + toParse + "> because have found " + numDollars + " dollars(tokens)");
        }
        return result.toString();
    }
}
