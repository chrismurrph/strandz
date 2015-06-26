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

import java.util.ArrayList;
import java.util.List;

/**
 * Globally accessible. If it is set to turnedOn == true
 * then classes that want to tell you why two objects are
 * not equal can put their instrumentation in here.
 * <p/>
 * After two objects have been determined to be not equal,
 * we can find out why using this class.
 *
 * @author Chris Murphy
 */
public class ReasonNotEquals
{
    private static boolean turnedOn = false;
    private static List visitedClasses;
    private static List reasons;

    public static void turnOn(boolean b)
    {
        if(b)
        {
            visitedClasses = new ArrayList();
            reasons = new ArrayList();
        }
        else
        {
            visitedClasses = null;
        }
        turnedOn = b;
    }

    public static boolean isTurnedOn()
    {
        return turnedOn;
    }

    public static void addClassVisiting(String className)
    {
        if(turnedOn)
        {
            visitedClasses.add(className);
        }
    }

    /**
     * This is add rather than set because, well, same reason said in
     * different ways as go from low-down detail to high-up abstract.
     */
    public static void addReason(String reason)
    {
        if(turnedOn)
        {
            ReasonNotEquals.reasons.add(reason);
        }
    }

    public static boolean hasReasons()
    {
        boolean result = !ReasonNotEquals.reasons.isEmpty();
        return result;
    }

    public static String formatReasons()
    {
        List classesVisited = Print.getPrList(visitedClasses,
            "ReasonNotEquals, went thru these classes", new ArrayList());
        String result = "%%REASONS FOR DIFFERENCE:\n"
            + Utils.getStringBufferFromList(classesVisited).toString();
        List reasonsNotEqual = Print.getPrList(reasons, "Reasons", new ArrayList());
        // Doing this made the underlining look silly
        // List nowReversed = pUtils.reverseOrder( reasonsNotEqual);
        result += Utils.getStringBufferFromList(reasonsNotEqual).toString();
        return result;
    }
}
