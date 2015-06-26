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

import org.strandz.lgpl.note.SdzNote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * This static class is used to redirect sdtout and sdterr to files. Thus
 * a section of code that is printing to the screen can be made to print
 * to a file instead. It does not currently have the ability to output to
 * both places simultaneusly.
 *
 * @author Chris Murphy
 */
public class IORedirectorUtils
{
    private static PrintStream psOutSaved;
    private static PrintStream psErrSaved;

    /**
     * Make so that writes (<code>System.out.println()</code>) to System.out will actually go to the
     * file specified here
     *
     * @param outFile the file that all stdout will go to after this call
     * @see #dontOutputToStdOut()
     */
    public static void outputToStdOut(String outFile)
    {
        PrintStream ps = null;
        if(outFile != null && !outFile.equals(""))
        {
            psOutSaved = System.out;
            try
            {
                ps = new PrintStream(new FileOutputStream(outFile));
            }
            catch(Exception e)
            {
                Err.error("Redirect:  Unable to open output file " + outFile);
            }
            Err.pr("stdout to be redirected to " + outFile);
            System.setOut(ps);
        }
    }

    /**
     * Make so that writes (<code>System.err.println()</code>) to System.err will actually go to the
     * file specified here
     *
     * @param errFile the file that all stderr will go to after this call
     * @see #dontOutputToStdErr()
     */
    public static void outputToStdErr(String errFile)
    {
        PrintStream ps = null;
        if(errFile != null && !errFile.equals(""))
        {
            File file = new File(errFile);
            // file.delete(); //lets create another one
            psErrSaved = System.err;
            try
            {
                ps = new PrintStream(new FileOutputStream(file)// This experiment yielded nothing in the JTabbedPane tab
                    // new ByteArrayOutputStream()
                );
            }
            catch(Exception e)
            {
                Err.error("Redirect:  Unable to open output file " + errFile);
            }
            Err.pr(SdzNote.GENERIC, "stderr to be redirected to " + errFile);
            System.setErr(ps);
        }
    }

    /**
     * Turn off the redirection to something other than stdout - ie. back to normal
     *
     * @see #outputToStdOut(String)
     */
    public static void dontOutputToStdOut()
    {
        /*
        if(psOutSaved == null)
        {
          Err.error( "dontOutputToStdOut(): Unlikely that the original stdout was null");
        }
        */
        System.setOut(psOutSaved);
    }

    /**
     * Turn off the redirection to something other than stdout - ie. back to normal
     * @see #outputToStdErr(String)
     */
    public static void dontOutputToStdErr()
    {
        /*
        if(psOutSaved == null)
        {
          Err.error( "dontOutputToStdErr(): Unlikely that the original stderr was null");
        }
        */
        System.setErr(psErrSaved);
    }
}
