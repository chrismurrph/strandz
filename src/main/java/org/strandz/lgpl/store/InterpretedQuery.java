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
package org.strandz.lgpl.store;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.NoTaskTimeBandMonitorI;
import org.strandz.lgpl.util.TimeBand;
import org.strandz.lgpl.util.Err;

abstract public class InterpretedQuery extends DomainQuery
{
    boolean wrapInList = false;
    private NoTaskTimeBandMonitorI monitor;
    private boolean didStart = false;
    boolean compileWhenConnect = true;
    int estimatedDuration = TimeBand.NO_ESTIMATE_GIVEN;

    public static final boolean NOISY = SdzNote.MONITOR_QUERIES.isVisible();

    public InterpretedQuery(Class queryOn,
                            String id,
                            NoTaskTimeBandMonitorI monitor,
                            boolean wrapInList)
    {
        this(
            queryOn,
            id,
            monitor,
            wrapInList, true);
    }

    public InterpretedQuery(Class queryOn,
                            String id,
                            NoTaskTimeBandMonitorI monitor)
    {
        this(
            queryOn,
            id,
            monitor,
            false, true);
    }

    public InterpretedQuery(Class queryOn,
                            String id,
                            NoTaskTimeBandMonitorI monitor,
                            int estimatedDuration)
    {
        this(
            queryOn,
            id,
            monitor,
            false, true, estimatedDuration);
    }
    
    public InterpretedQuery(Class queryOn,
                            String id,
                            NoTaskTimeBandMonitorI monitor,
                            boolean wrapInList,
                            boolean compileWhenConnect,
                            int estimatedDuration)
    {
        if(queryOn == null)
        {
            //No longer an error, CLASS_QUERY does
            //Err.error( "A JDOQuery must have ");
        }
        this.queryOn = queryOn;
        this.id = id;
        this.monitor = monitor;
        this.wrapInList = wrapInList;
        this.compileWhenConnect = compileWhenConnect;
        this.estimatedDuration = estimatedDuration;
    }

    public InterpretedQuery(Class queryOn,
                            String id,
                            NoTaskTimeBandMonitorI monitor,
                            boolean wrapInList,
                            boolean compileWhenConnect)
    {
        if(queryOn == null)
        {
            //No longer an error, CLASS_QUERY does
            //Err.error( "A JDOQuery must have ");
        }
        this.queryOn = queryOn;
        this.id = id;
        this.monitor = monitor;
        this.wrapInList = wrapInList;
        this.compileWhenConnect = compileWhenConnect;
    }

    void start(String name)
    {
        //String txt = "QUERY ";
        //if(!useCache)
        String txt = "QUERY ";
        if(NOISY)
        {
//            if(monitor.getTask() != null)
//            {
//                //As the same monitor is used for every query then the monitor will
//                //already (after the first time anyway) have an internally created 
//                //DoNothingLogTask on it 
//            }
            if(estimatedDuration == TimeBand.NO_ESTIMATE_GIVEN)
            {
                monitor.start(txt + name);
            }
            else
            {
                monitor.start( new TimeBand( txt + name, estimatedDuration));
            }
        }
        didStart = true;
    }

    protected void stop()
    {
        if(NOISY && didStart)
        {
            monitor.stop();
            didStart = false;
        }
    }
}
