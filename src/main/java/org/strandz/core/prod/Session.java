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
package org.strandz.core.prod;

import org.strandz.core.prod.move.MoveTracker;
import org.strandz.core.prod.move.MoveManagerI;
import org.strandz.core.prod.move.NullMoveTracker;
import org.strandz.core.record.Recorder;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.IndentationCounter;
import org.strandz.lgpl.util.Domain;

import java.util.List;

public class Session
{
    private static MoveManagerI moveManager;
    private static Recorder recorder;
    private static ErrorThrower errorThrower;
    private static int times;

    static
    {
        createRecorder();
    }

    public static void error(String s)
    {
        getRecorder().closeRecording();
        Err.error(s);
    }

    public static void error()
    {
        getRecorder().closeRecording();
        Err.error();
    }

    public static void error(Throwable th)
    {
        getRecorder().closeRecording();
        Err.error(th);
    }

    public static void error(List errors)
    {
        getRecorder().closeRecording();
        Err.error(errors);
    }

    public static void error(Throwable th, String s)
    {
        getRecorder().closeRecording();
        Err.error(th, s);
    }

    private static void createRecorder()
    {
        recorder = new Recorder();
        recorder.openRecording();
    }

    private static void createErrorThrower(MoveManagerI moveManager, IndentationCounter indentationCounter)
    {
        errorThrower = new ErrorThrower(moveManager, indentationCounter);
    }

    /**
     * There may be many Strands, but there only needs to be
     * one MoveManager per user session. As MoveManager
     * contains validation state we make sure that a reset
     * is done every time a new Strand is created. I'm not
     * 100% sure this is the perfect place given all kinds
     * of weird dynamic code that may be created, but it
     * should be good for now.
     * TODO - above didn't work at all (see Strand constructor)
     * Really MoveTracker needs to be looked at. One problem is
     * say when that NEXT() should fail, but doesn't b/c a new
     * field is focused on - error messages for record and field
     * things need to be kept separate - so need two lots of errors
     * and error histories to be kept
     */
    static public MoveManagerI createMoveManager( IndentationCounter indentationCounter)
    {
        boolean nullTracker = false;
        String trackerBool = Domain.getInstance().getPropertiesHolder().getValue( "nullTracker", true);
        if("true".equals( trackerBool))
        {
            nullTracker = true;
        }
        if(moveManager == null)
        {
            if(nullTracker)
            {
                moveManager = new NullMoveTracker();
            }
            else
            {
                moveManager = new MoveTracker();
            }
            createErrorThrower(moveManager, indentationCounter);
        }
        /*
        else
        {
        moveManager.reset();
        }
        */
        return moveManager;
    }

    /**
     * Enables us to easily, and from anywhere, get the only MoveTracker
     */
    public static MoveManagerI getMoveManager()
    {
        if(moveManager == null)
        {
            Session.error(
                "If have created a Strand, then a MoveManager s/be available");
            // createMoveManager();
        }
        return moveManager;
    }

    public static Recorder getRecorder()
    {
        return recorder;
    }

    public static ErrorThrower getErrorThrower()
    {
        return errorThrower;
    }

    /*
    private static String readLine( LineNumberReader lnr, String fileName)
    {
      String result = null;
      try
      {
        result = lnr.readLine();
      }
      catch(IOException ex)
      {
        Session.error( "Could not readLine from file - <" + fileName + ">");
      }
      return result;
    }
    */
    /*
    public static void replayRecorded(
    String fileName, SdzBagI sbI)
    {
    recorder.replayRecorded( fileName, null, sbI, null);
    }

    public static void replayRecorded(
    String fileName, SdzBagI sbI, List startupCode)
    {
    recorder.replayRecorded( fileName, null, sbI, startupCode);
    }
    */
}
