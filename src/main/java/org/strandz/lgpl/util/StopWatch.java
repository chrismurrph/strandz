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
package org.strandz.lgpl.util;

import java.util.Date;

public class StopWatch
{
    private Date start, end;
    private boolean active = true;

    public StopWatch( boolean active)
    {
        this.active = active;
    }
    
    public StopWatch()
    {
    }

    /**
     * Used as the 'starting call' to see how long it takes some code to complete
     */
    public void startTiming()
    {
        if(active)
        {
            getTiming();
        }
    }

    /**
     * At the same time as the timing is started we get the starting time
     * returned
     *
     * @return the timestamp when this method was called
     */
    public Date getTiming()
    {
        start = new Date();
        return start;
    }
    
    public void stopTiming()
    {
        if(active)
        {
            end = new Date();
        }
    }

    public long getResult()
    {
        if(active)
        {
            Assert.notNull( start, "StopWatch is yet to be started");
            Assert.notNull( end, "StopWatch has not been stopped");
            long result = end.getTime() - start.getTime();
            return result;
        }
        else
        {
            return 0;
        }
    }
            
    public String getElapsedTimeStr()
    {
        return getElapsedTimeStr( start, end);
    }
    
    public static String getElapsedTimeStr( Date start, Date end)
    {
        Assert.notNull( start, "StopWatch is yet to be started");
        Assert.notNull( end, "StopWatch has not been stopped");
        long lengthOfTime = end.getTime() - start.getTime();
        return String.valueOf(lengthOfTime) + " ms";
    }
    
    /**
     * Returns true if the first and second times are closer together than period
     *
     * @param first  start time
     * @param second end time
     * @param period time in milliseconds that want to be outside of
     * @return true if the times were too close
     */
    public static boolean areTimesClose( Date first, Date second, int period)
    {
        boolean result = false;
        long lengthOfTime = second.getTime() - first.getTime();
        if(lengthOfTime < period)
        {
            result = true;
        }
        return result;
    }
}
