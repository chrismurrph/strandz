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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is an implementation of ErrorLogger, an interface that can be logged to.
 * It outputs both to a Refreshable and to a file. It can be marked to not write to a
 * file at all, for instance if Web Start is being used. A Refreshable may for instance
 * be hooked up to a screen
 *
 * @author Chris Murphy
 */
public class Logger implements ErrorLogger
{
    private FileWriter out;
    private final List list = new ArrayList();
    private String fileName;
    private Refreshable refreshable;
    /**
     * true if this class (not object) is able to write to the filesystem.
     * It is necessary to set this to false for a certificate-less web
     * start application.
     */
    private static boolean interactWithFileSystem = false;
    private static int times;
    private boolean debugging = false;

    /**
     * Keeps a file of text lines, and allows the user to write to this file
     * using many log() methods. The GUI display (see Refreshable interface)
     * is simulataneously updated.
     *
     * @param fileName  The name of the file that the log messages will go to
     * @param reset     true if we want to wipe all the prior existing lines
     * @param startText the heading of the file, that will appear on the first line
     */
    public Logger(String fileName, boolean reset, String startText)
    {
        /*
        * Opening for append
        */
        try
        {
            File file = new File(fileName);
            if(reset && interactWithFileSystem)
            {
                file.delete();
            }
            if(interactWithFileSystem)
            {
                out = new FileWriter(file, true);
            }
            if(startText == null)
            {
                startText = "STARTED ERRORLOGGER";
            }
            log(startText, false);
        }
        catch(IOException e)
        {
            Err.error("Could not create or open file - <" + fileName + ">");
        }
        // Err.setBatch( false); //so can hear sound of error
        this.fileName = fileName;
    }

    /**
     * Set the Refreshable interface that will receive refresh events each time
     * a message (or list of messages) is output.
     *
     * @param v
     */
    public void setRefreshable(Refreshable v)
    {
        refreshable = v;
    }

    /**
     * Get the internal list of message lines
     *
     * @return a Vector of every line that has been output since Logger construction
     */
    public List getList()
    {
        return list;
    }

    /**
     * Log a message
     *
     * @param msg A String message
     */
    public void log(String msg)
    {
        String msgs[] = {msg};
        log(msgs, true, true);
    }

    /**
     * Log a message. Slightly strange parameters here. The first message is
     * no more special than the subsequent messages.
     *
     * @param mainMsg    A String message
     * @param restOfMsgs More String messages
     */
    public void log(String mainMsg, List restOfMsgs)
    {
        if(restOfMsgs == null)
        {
            restOfMsgs = new ArrayList();
        }
        restOfMsgs.add(0, mainMsg);
        log((String[]) restOfMsgs.toArray(new String[restOfMsgs.size()]), true,
            true);
    }

    /**
     * Log a message
     *
     * @param msg A String message
     */
    public void log(List msg)
    {
        log((String[]) msg.toArray(new String[msg.size()]), true, true);
    }

    /**
     * Log a message
     *
     * @param msg     A String message
     * @param audible true if you can hear a beep as the message is written
     */
    public void log(List msg, boolean audible)
    {
        log((String[]) msg.toArray(new String[msg.size()]), true, audible);
    }

    /**
     * Log a message
     *
     * @param msg A String message
     */
    public void log(String msg[])
    {
        log(msg, true, true);
    }

    /**
     * Log a message
     *
     * @param msg     A String message
     * @param audible true if you can hear a beep as the message is written
     */
    public void log(String msg, boolean audible)
    {
        String msgs[] = {msg};
        log(msgs, true, audible);
    }

    /**
     * Log a message
     *
     * @param msg     An array of String messages
     * @param audible true if you can hear a beep as the message is written
     */
    public void log(String msg[], boolean audible)
    {
        log(msg, true, audible);
    }

    private void quickMessage()
    {
        System.err.println("Have written error to <" + fileName + ">");
    }

    private void log(String msgs[], boolean notToStdErr, boolean audible)
    {
        try
        {
            String stamp = new Date().toString();
            // Used for multi-line messages so that Date does not appear on
            // lines after the first
            StringBuffer seperator = new StringBuffer();
            for(int j = 0; j <= stamp.length() + 1 - 1; j++) // +1 is for "|"
            {
                seperator.append(" ");
            }
            for(int i = 0; i <= msgs.length - 1; i++)
            {
                if(interactWithFileSystem)
                {
                    if(i == 0)
                    {
                        out.write(stamp + "|" + msgs[i] + Utils.NEWLINE);
                    }
                    else
                    {
                        out.write(new String(seperator) + msgs[i] + Utils.NEWLINE);
                    }
                }
                list.add(msgs[i]);
            }
            if(debugging)
            {
                times++;
                list.add("WAS MESSAGE NUMBER " + times);
                if(times == 0)
                {
                    Err.stack();
                }
            }
            else
            {// list.add( "");
            }

            RefreshEvent evt = new RefreshEvent(Logger.class);
            evt.from = list.size();
            evt.to = list.size() + 1;
            if(refreshable != null)
            {
                refreshable.refresh(evt);
            }
            if(interactWithFileSystem) out.flush();
            // out.close();
        }
        catch(IOException e)
        {
            Err.error("Could not write to - <" + fileName + ">");
        }
        if(!notToStdErr)
        {
            quickMessage();
        }
        if(audible)
        {
            Err.alarm(msgs[0]);
        }
    }

    static private String stack2string(Error e)
    {
        String result = null;
        try
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            result = "------" + Utils.NEWLINE + sw.toString() + "------"
                + Utils.NEWLINE;
        }
        catch(Exception e2)
        {
            Err.error("bad stack2string");
        }
        return result;
    }

    /**
     * Get the file name for messages
     * @return The file name that the log messages were written to
     */
    public String getFileName()
    {
        return fileName;
  }
}
