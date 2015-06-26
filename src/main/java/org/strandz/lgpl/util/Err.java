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

import org.strandz.lgpl.note.Note;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;

/**
 * The main 'instrumentation style debugging' class. Note the integration with
 * <code>Note</code> so that messages can be grouped together that are
 * all instumenting the same 'topic'. With modern coding tools it is very
 * easy to find out all the places in the source code where a particular
 * note is being instrumented.
 * <p/>
 * Once an application is completely polished
 * NONE of these messages should be appearing at RT. Such intentional messages
 * are in the realm of logging, and this is not a logging class.
 * <p/>
 * Err.error()/Err.stack() etc are in the realm of assertions.
 * <p/>
 * Specifically:
 * 1 Where there is a real exception where nothing further is to be done,
 * then the program should not progress, or should only progress from
 * events that come from EventDispatchThread. These exceptions can be
 * runtime exceptions or exceptions generated by Err.error(). What this
 * means is that there should be no catching and not doing anything with
 * exceptions. If there is an error, any error, then ALWAYS tell EVERYONE.
 * 2 Whether in applichousing or non-applichousing environment s/be told to this class. If
 * applichousing then a Dialog will come up on calls to error(). Also
 * getDefaultToolkit() should never be called by a non-applichousing application.
 * This true except for cases where printing used, such as OutputTest.
 * 3 A dialog won't be shown for runtime errors that the system generates
 * (Err.error() does not generate). Thus where program is applichousing setting
 * batch = false is not enough. Here will have to catch all
 * exceptions around the thread of each program. The fact that this is
 * a "System" exception should be clearly shown.
 * 4 Dialogs for interaction with the user, for example if has pressed the
 * wrong key and needs to be informed, should NOT come thru to here. In such
 * cases use this form in the code itself:
 * <code>JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);</code>
 * OR <code>MessageDlg</code> OR <code>CustomDialog</code>
 * 5 OutOfMemoryError is never to be displayed using a Dialog, as no memory
 * is left to create one. See 3.
 *
 * @author Chris Murphy
 */
public class Err
{
    private static boolean batch = true;
    private static boolean visualDurationMonitor = false;
    private static boolean printStackTrace = false;
    private static int times;
    
    private static final int TIMES_BEEP = 1;
    private static final boolean ENCLOSING_PROGRAM_CATCHES_EXCEPTIONS = true;

    public static final boolean HAS_WINDOWING_TOOLKIT = false;
    
    /**
     * Abort the current thread due to an invalid state being reached in
     * the program. The classic example is the default of a case statement
     * when all conditions are supposed to be met. This is a 'programmer
     * assertion error'.
     *
     * @param s A message saying what the problem condition is
     */
    public static void error(String s)
    {
        alarm();
        showError(s);

        Error e = new Error(s);
        if(printStackTrace)
        {
            e.printStackTrace(System.err);
        }
        else
        {
            throw e;
        }
    }

    /**
     * Abort the current thread due to a system generated exception
     *
     * @param th A system generated exception
     */
    public static void error(Throwable th)
    {
        alarm();
        showErrorWatchMemory(th, th.getMessage());

        Error e = new Error(th.getMessage());
        if(printStackTrace)
        {
            e.printStackTrace(System.err);
        }
        else
        {
            throw e;
        }
    }

    /**
     * Abort the current thread due to an invalid state being reached in
     * the program. The classic example is the default of a case statement
     * when all conditions are supposed to be met. This is a 'programmer
     * assertion error'.
     *
     * @param note The area under investigation that this instrumentation is for
     * @param s    A message saying what the problem condition is
     */
    public static void error(Note note, String s)
    {
        pr(note, s);
        error(s); //this makes an audible sound, thus not needed above
    }

    public static void error(String s, Throwable th)
    {
        error(th, s);
    }

    /**
     * Abort the current thread due to a system generated exception
     *
     * @param th A system generated exception
     * @param s  A context sensitive message.
     */
    public static void error(Throwable th, String s)
    {
        alarm();

        /*
         * th.getMessage() will almost never be as good as s
         */
        String throwableMessage = th.getMessage();
        if(throwableMessage == null || "".equals(throwableMessage))
        {
            showErrorWatchMemory(th, s);
        }
        else
        {
            showErrorWatchMemory(th, s/* throwableMessage*/);
        }
        throw new Error(s + '\n' + "OTHER DESC: <" + throwableMessage + ">");
    }

    private static void showErrorWatchMemory(Throwable th, String message)
    {
        if(th.getClass() != OutOfMemoryError.class)
        {
            showError(message/* th.getMessage()*/);
            // here we don't need to th.printStackTrace(); as throw new Error
            // is always done after this call whether batch or not, and
            // the throwing prints the stack trace for us.
            if(ENCLOSING_PROGRAM_CATCHES_EXCEPTIONS)
            {
                Print.pr(stack2string(th));
            }
        }
        else
        {// Perhaps do nothing rather than create a String!
            // throw new Error() call will give us the info anyway
            // Err.pr( "OUT OF MEMORY ERROR:\t" + s);
        }
    }

    /**
     * Used by programmers when want to have a Debugger breakpoint at a
     * certain condition for which special temporary code has been written.
     * Such code is normally commented in and out as needed. Note that it
     * is not normally deleted as it may be needed again! An alternative to
     * commenting in and out would be to use Bug.isVisible().
     */
    public static void debug()
    {
        Print.pr("DEBUG HERE");
        //stack();
    }

    public static void debug( String s)
    {
        Print.pr("DEBUG HERE: " + s);
        //stack();
    }

    /**
     * Perform a stack trace to aid debugging
     *
     * @see #stack(String)
     */
    public static void stack()
    {
        error("STACK ME");
    }

    public static void kill()
    {
        System.exit( -1);
    }

    /**
     * 'stacking' is more to do with temporary debugging than erroring.
     * Thus when close to going live one would expect there to be none
     * of these - and during normal coding hardly any.
     *
     * @param s informative message as to why this stack trace is occuring
     */
    public static void stack(String s)
    {
        error("STACK ME: " + s);
    }

    /**
     * When an error situation has developed you might want to
     * take a look at the memory with a profiler
     * @param str
     */
    public static void freeze(String str)
    {
        while(true)
        {
            try
            {
                Err.pr( "FROZEN: " + str);
                Thread.sleep(240000); //every 4 minutes
            }
            catch(InterruptedException e)
            {
                Err.error(e);
            }
        }
    }

    /**
     * Converts any <code>Throwable</code> into a String. This
     * could be used for example to write a stack trace to a
     * log file and then continue on, otherwise ignoring the
     * exception.
     *
     * @param e the Throwable we are interested in
     * @return the stack trace the Throwable represents
     */
    static private String stack2string(Throwable e)
    {
        String result = null;
        try
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            result = "------\n" + sw.toString() + "------\n";
        }
        catch(Exception e2)
        {
            error("bad stack2string");
        }
        return result;
    }


    private static void showError(String s)
    {
        if(batch || !HAS_WINDOWING_TOOLKIT) // don't need 2nd boolean for current
        // applications
        {
            Print.pr("PROGRAM ERROR:\n" + s);
        }
        else
        {
            // Need better than this so shows stack trace from th too - for moment at
            // least stack trace is going to stdout.
            JOptionPane.showMessageDialog(null, s, "Program Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * General instrumentation method
     *
     * @param s An object representing the problem (should have a good toString() method)
     */
    public static void pr(Object s)
    {
        Print.pr(s);
    }

    /**
     * General instrumentation method. Note that if the note
     * is not visible then nor will the message be.
     *
     * @param note The note that are currently instrumenting
     * @param str  Debug message
     */
    public static void pr(Note note, String str)
    {
        pr(note, str, false);
    }

    /**
     * General instrumentation method.
     *
     * @param visible Whether to display the message or not
     * @param str     Debug message
     */
    public static void pr(boolean visible, String str)
    {
        pr(visible, str, false);
    }

    /**
     * General instrumentation method. Note that if the note
     * is not visible then nor will the message be.
     *
     * @param note The note that are currently instrumenting
     * @param msg  Debug message (as a list)
     */
    public static void pr(Note note, java.util.List msg)
    {
        pr(note, msg, false);
    }

    private static void pr(Note note, java.util.List msg, boolean alarm)
    {
        if(note.isVisible())
        {
            if(alarm)
            {
                msg.add(0, "ALARM");
                alarm();
            }
            Print.pr(msg);
        }
    }

    private static void pr(Note note, String str, boolean alarm)
    {
        if(note.isVisible())
        {
            if(alarm)
            {
                str = "ALARM: " + str;
                alarm();
            }
            Print.pr(str);
        }
    }

    private static void pr(boolean isVisible, String str, boolean alarm)
    {
        if(isVisible)
        {
            if(alarm)
            {
                str = "ALARM: " + str;
                alarm();
            }
            Print.pr(str);
        }
    }


    /**
     * Make a noise to alert the user that something wrong
     * was attempted, or to alert the programmer to a note.
     * Note two uses, so we may split this into two differently
     * named method later on.
     */
    private static void alarm()
    {
        if(!batch)
        {
            // temp();
            Toolkit.getDefaultToolkit().beep();
             /**/
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException ex)
            {// no big deal
            }
             /**/
            for(int i = 0; i <= (TIMES_BEEP - 1) - 1; i++)
            {
                Toolkit.getDefaultToolkit().beep();
                // again so can hear multiple errors
                 /**/
                try
                {
                    Thread.sleep(200);
                }
                catch(InterruptedException ex)
                {// no big deal
                }
                 /**/
            }
        }
        else
        {
            Print.pr("WARNING ALARM BELL IN BATCH ENVIRONMENT");
        }
        //throw new Error();
    }

    /**
     * Make a noise and instrument why at the same time. For programmers only.
     *
     * @param txt why the noise is being made
     */
    public static void alarm(String txt)
    {
        if(txt == null)
        {
            error("Alarm must be called with a reason");
        }
        pr("ALARM: " + txt);
        /*
        if("Ignore".equals( txt))
        {
            times++;
            if (times == 1)
            {
                stack();
            }
        }
        */
        alarm();
    }

    /**
     * Determine whether this static class outputs errors to a Dialog or to stderr.
     * The call to this method should be one of the first in any program. The default
     * is true.
     *
     * @param b false if best way of communicating errors to the user is with Dialogs
     */
    public static void setBatch( boolean b)
    {
        Err.pr( SdzNote.BATCH, "Setting batch to " + b + " times " + times);
        batch = b;
    }

    /**
     * Find out where error output is going to.
     *
     * @return true if the error is going to stderr
     * @see #setBatch(boolean)
     */
    public static boolean isBatch()
    {
        // error( "Should not be being used in this jar file");
        return batch;
    }

    /**
     * Determine whether a Dialog is used for duration messages
     */
    public static boolean isVisualDurationMonitor()
    {
        return visualDurationMonitor;
    }

    /**
     * Determine whether a Dialog is used for duration messages
     */
    public static void setVisualDurationMonitor(boolean visualDurationMonitor)
    {
        Err.visualDurationMonitor = visualDurationMonitor;
    }

    /**
     * Use this method to set the response to errors to be milder.
     *
     * @param b true to be less severe on errors by printing a stack trace but not
     *          actually throwing it on error.
     */
    public static void setPrintStackTrace(boolean b)
    {
        printStackTrace = b;
    }

    /**
     * General instrumentation method
     *
     * @param str Debug message
     */
    public static void pr(String str)
    {
        /*
        if(str.equals(""))
        {
            stack();
        }
        */
        Print.pr(str);
    }

    /**
     * General instrumentation method
     *
     * @param i A number representing the error
     */
    public static void pr(int i)
    {
        pr("NUMBER: " + i);
        // stack();
    }

    /**
     * An error has occured for which there is no message
     */
    public static void error()
    {
        error("UNDEFINED ERROR");
    }

    /**
     * Perform a stack trace to aid debugging
     *
     * @param note The Bug we are stacking for
     * @see #stack(String)
     */
    public static void stack(Note note)
    {
        if(note.isCauseProblem())
        {
            error("STACK ME for " + note.getName());
        }
        else
        {
            if(note.isVisible())
            {
                pr("WOULD STACK ME for " + note.getName());
            }
        }
    }

    /**
     * Perform a stack trace to aid debugging
     *
     * @param note
     * @param msg
     * @see #stack(String)
     */
    public static void stack(Note note, String msg)
    {
        if(note.isCauseProblem())
        {
            error("STACK ME for " + note.getName() + " " + msg);
        }
        else
        {
            if(note.isVisible())
            {
                pr("WOULD STACK ME for " + note.getName() + " " + msg);
            }
        }
    }

    /**
     * Perform a stack trace to aid debugging
     *
     * @param b
     * @see #stack(String)
     */
    public static void stack(boolean b)
    {
        error("STACK ME: " + b);
    }

    /**
     * Don't want to actually stack trace, but do want to still mark this
     * point in the code. The programmer might toggle between using stack()
     * and stackOff().
     */
    public static void stackOff()
    {
        pr("Would STACK ME");
    }

    /**
     * Don't want to actually stack trace, but do want to still mark this
     * point in the code. The programmer might toggle between using stack()
     * and stackOff().
     *
     * @param note Bug that this code line is associated with
     */
    public static void stackOff(Note note)
    {
        pr("Would STACK ME for " + note.getName());
    }

    /**
     * Don't want to actually stack trace, but do want to still mark this
     * point in the code. The programmer might toggle between using stack()
     * and stackOff().
     *
     * @param note Bug that this code line is associated with
     * @param msg  Informative message
     */
    public static void stackOff(Note note, String msg)
    {
        pr("Would STACK ME for " + note.getName() + " " + msg);
    }

    public static void stackOff(String msg)
    {
        pr("Would STACK ME for " + msg);
    }
    
    /**
     * Don't want to actually stack trace, but do want to still mark this
     * point in the code. The programmer might toggle between using error()
     * and errorOff().
     *
     * @param note Bug that this code line is associated with
     * @param msg  Informative message
     * @see #error(String)
     */
    public static void errorOff(Note note, String msg)
    {
        pr("Would ERROR for " + note.getName() + " " + msg);
    }

    /**
     * Variation on error(String)
     *
     * @param errors The many lines that describe the error condition
     * @see #error(String)
     */
    public static void error(String[] errors)
    {
        error( Arrays.asList( errors));
    }

    /**
     * Variation on error(String)
     *
     * @param errorLines The many lines that describe the error condition
     * @see #error(String)
     */
    public static void error(List errorLines)
    {
//        for(int i = 0; i < errorLines.size(); i++)
//        {
//            error((String) errorLines.get(i));
//        }
        String errMsg = Utils.getStringBufferFromList( errorLines).toString();
        error( errMsg);
    }

    /*
    * These three lines if ever need to implement error( String mainMsg, List errorLines)
    * a little differently:
    *  List list = Print.getPrList( nvTables, "Nowhere to attach the NonVisualTables", new ArrayList());
    *  String errMsg = pUtils.getStringBufferFromList( list).toString();
    *  Err.error( errMsg);
    */

    /**
     * Variation on error(String)
     *
     * @param mainMsg    The main error message
     * @param errorLines Extra error lines if required
     * @see #error(String)
     */
    public static void error(String mainMsg, List errorLines)
    {
        alarm();
        showError(mainMsg);
        showErrorLines(errorLines);

        Error e = new Error(mainMsg);
        if(printStackTrace)
        {
            e.printStackTrace(System.err);
        }
        else
        {
            throw e;
        }
    }

    public static void alarm(Note note, String s)
    {
        pr(note, s, true);
    }

    private static void showErrorLines(List lines)
    {
        if(batch || !HAS_WINDOWING_TOOLKIT)
        {
            for(Iterator iter = lines.iterator(); iter.hasNext();)
            {
                //String line = (String)iter.next();
                //Surely this is more flexible
                String line = iter.next().toString();
                Print.pr(line);
            }
        }
        else
        {
            throw new Error("Have not written code to display many error lines in a Dialog");
        }
    }
}