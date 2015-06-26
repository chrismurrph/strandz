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
package org.strandz.core.record;

import org.strandz.core.domain.RecorderI;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Logger;
import org.strandz.lgpl.util.TimeUtils;

import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Recorder implements RecorderI
{
    private Logger logger;
    private static String recordingName = "_ml";
    public static final String LOG_DIR = "C:/sdz-zone/bin/log/";
    //
    private Stack recordSites = new Stack();
    private FileWriter out;
    private final String fileName;
    private List recording;
    private XMLRecorderData dataStore;
    private static int times;
    private static int times1;
    private List startupOperations;
    private static boolean switchedOn = false;
    private static boolean debugPlay = true;
    private static boolean debugRecord = false;
    public static final Class CLASSES[] = new Class[]{Operation.class};

    public Recorder()
    {
        String name = TimeUtils.getACurrentFileName(recordingName);
        this.fileName = LOG_DIR + name + ".xml";
        logger = new Logger(LOG_DIR + name + ".log", true,
            "STARTED RECORDER LOGGER");
    }

    public void openRecording()
    {
        if(switchedOn)
        {
            // pr( "ooo Open recording to " + fileName);
            recording = new ArrayList();
        }
    }

    public void closeRecording()
    {
        if(switchedOn)
        {
            dataStore = new XMLRecorderData(fileName, CLASSES);
            dataStore.startTx();
            dataStore.set(Operation.class, recording);
            dataStore.commitTx();
            // pr( "ooo Closed recording of " + fileName);
        }
    }

    public static void prPlay(String txt)
    {
        if(debugPlay)
        {
            Err.pr(txt);
        }
    }

    private void prRecord(String txt)
    {
        if(debugRecord)
        {
            Err.pr(txt);
        }
    }

    public void closeRecordOperation()
    {
        if(switchedOn)
        {
            Object popped = recordSites.pop();
            if(recordSites.size() == 0)
            {
                logger.log(popped.toString(), false);
                prRecord("     >EXITING at " + popped);

                SimpleOperation op = (SimpleOperation) popped;
                recording.add(op);
            }
        }
    }

    public void openRecordOperation(
        OperationEnum obj, String name)
    {
        if(switchedOn)
        {
            if(recordSites.size() == 0)
            {
                /*
                times1++;
                */
                prRecord("     >RECORDING at " + obj + " times " + times1);

                /*
                if(times1 == 5)
                {
                Err.debug();
                }
                */
                SimpleOperation op = null;
                if(obj == OperationEnum.GOTO_NODE
                    || obj == OperationEnum.GOTO_NODE_IGNORE)
                {
                    op = new GoToOperation();
                    ((GoToOperation) op).setNodeName(name);
                }
                else
                {
                    op = new SimpleOperation();
                }
                op.setOperation(obj);
                /*
                * Instead of adding it now, we want to add it when we close.
                * When close means for instance that the SET_VALUE will come
                * before the GOTO that triggered it, which is how it would
                * have worked when the user did it. (With SET_VALUE what we
                * are playing back is what the user actually did to the
                * control - but we only have evidence of it after the POST
                * or GOTO or whatever)
                */
                // recording.add( op);
                recordSites.push(op);
                /*
                if(obj.equals( OperationEnum.EXECUTE_QUERY))
                {
                times++;
                if(times == 0)
                {
                Err.stack();
                }
                }
                */
            }
            else
            {
                recordSites.push(obj);
            }
        }
    }

    public void setRecordingName(String s)
    {
        recordingName = s;
    }

    public void recordSetValue(
        String nodeName, String cellName,
        String attributeName, Method writeMethod,
        Object value)
    {
        if(switchedOn)
        {
            // Class declaringClass = writeMethod.getDeclaringClass();
            String name = null;
            if(writeMethod != null)
            {
                name = writeMethod.getName();
            }

            List toLog = new ArrayList();
            toLog.add(
                "[" + value + "], " + name + /* ", " + declaringClass +*/", "
                    + cellName);
            logger.log(toLog, false);

            //
            SetValueOperation op = new SetValueOperation();
            op.setNodeName(nodeName);
            op.setCellName(cellName);
            op.setAttributeName(attributeName);
            // op.setMethod( writeMethod);
            op.setArg(value);
            op.setOperation(OperationEnum.SET_VALUE);
            prRecord("     >RECORDING at " + op);
            recording.add(op);
        }
    }

    public boolean isStartupOperation(
        int i, Operation op, List startupOperations)
    {
        boolean result = false;
        if(startupOperations != null)
        {
            if(startupOperations.size() >= i + 1)
            {
                if(startupOperations.get(i).equals(op))
                {
                    prPlay("SKIPPING: <" + op + ">");
                    result = true;
                }
            }
        }
        return result;
    }

    public void setStartupOperations(List startupOperations)
    {
        this.startupOperations = startupOperations;
    }

    public String getFileName()
    {
        return fileName;
    }

    public XMLRecorderData getDataStore()
    {
        return dataStore;
    }
}
